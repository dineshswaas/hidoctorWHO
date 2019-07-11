package com.swaas.kangle.NewPlayer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.API.service.AssetService;
import com.swaas.kangle.R;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.db.DigitalAssetTransactionRepository;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.models.AssetId;
import com.swaas.kangle.models.AssetImages;
import com.swaas.kangle.models.DigitalAssetTransactionMaster;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Sayellessh on 12-05-2017.
 */

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ViewHolder>{

    PlayerActivity mActivity;
    List<DigitalAssetsMaster> mAssets;
    String extension = null;
    String filename = null;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;
    DigitalAssetTransactionMaster digitalAssetTransactionMaster;
    DigitalAssetTransactionRepository digitalAssetTransactionRepository;
    String offlineurl;
    DigitalAssetsMaster assetData;
    private ProgressDialog pDialog;
    static int position1;
    ArrayList<AssetImages> pptassetimage;
    Retrofit retrofitAPI;
    AssetService assetService;
    Gson gsonget;

    public ImageViewAdapter(PlayerActivity activity, List<DigitalAssetsMaster> asset)  {
        mActivity =  activity;
        mAssets = asset;
        retrofitAPI = RetrofitAPIBuilder.getInstance();
        assetService = retrofitAPI.create(AssetService.class);
        gsonget = new Gson();
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(mActivity);
        digitalAssetTransactionRepository = new DigitalAssetTransactionRepository(mActivity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.new_image_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DigitalAssetsMaster asset = mAssets.get(position);

        if (asset.getDA_Type().equalsIgnoreCase("video")) {
            holder.type_of_asset.setImageResource(R.drawable.icon_mp4);
        } else if (asset.getDA_Type().equalsIgnoreCase("articulate")) {
            holder.type_of_asset.setImageResource(R.drawable.icon_html);
        } else if (asset.getDA_Type().equalsIgnoreCase("image")) {
            holder.type_of_asset.setImageResource(R.drawable.icon_jpeg);
        } else if (asset.getDA_Type().equalsIgnoreCase("document")) {
            if(asset.getOnlineURL().endsWith(".pdf")){
                holder.type_of_asset.setImageResource(R.drawable.icon_pdf);
            } else if(asset.getOnlineURL().endsWith(".ppt") || asset.getOnlineURL().endsWith(".pptx")){
                holder.type_of_asset.setImageResource(R.drawable.icon_ppt2);
            } else{
                holder.type_of_asset.setImageResource(R.drawable.icon_doc);
            }
        }

        holder.assetName.setText(asset.getDAName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (asset.getDA_Type().equalsIgnoreCase("document")) {
                    if (asset.getOnlineURL().endsWith(".ppt")
                        || asset.getOnlineURL().endsWith(".pptx")
                        || asset.getOnlineURL().endsWith(".pdf")) {
                        getPPTAssetImages(asset);
                    } else {

                    }
                } else {
                    Intent i = new Intent(mActivity, ViewpagerPlayerActivity.class);
                    i.putExtra("Asset",asset);
                    i.putExtra("isppt","assetType");
                    i.putExtra("position", 0);
                    mActivity.startActivity(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = mAssets.size();
        } catch (Exception e) {

        }
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final ImageView type_of_asset;
        final TextView assetName;
        final View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView.findViewById(R.id.mView);
            type_of_asset = (ImageView) itemView.findViewById(R.id.type_of_asset);
            assetName = (TextView) itemView.findViewById(R.id.assetName);

        }
    }

    public ArrayList<AssetImages> getPPTAssetImages(DigitalAssetsMaster digitalAssetsMaster){
        if(NetworkUtils.checkIfNetworkAvailable(mActivity)) {
            showProgressDialog();
            pptassetimage = new ArrayList<AssetImages>();
            AssetId assetId = new AssetId();
            assetId.setAssetId(digitalAssetsMaster.getDAID());
            List<Integer> intasset = new ArrayList<Integer>();
            intasset.add(Integer.parseInt(digitalAssetsMaster.getDAID()));
            String[] newarray = {digitalAssetsMaster.getDAID()};

            User userobj = gsonget.fromJson(PreferenceUtils.getUser(mActivity), User.class);
            String subdomain = PreferenceUtils.getSubdomainName(mActivity);
            int companyId = userobj.getCompany_Id();
            Call call = assetService.getAssetImages(subdomain,companyId,newarray);
            call.enqueue(new Callback<ArrayList<AssetImages>>() {

                @Override
                public void onResponse(Response<ArrayList<AssetImages>> response, Retrofit retrofit) {
                    ArrayList<AssetImages> apiResponse= response.body();
                    if (apiResponse != null) {
                        pptassetimage = apiResponse;
                        Log.d("log","assetsForBrowses");
                        Intent i = new Intent(mActivity, PPTAssetImagesActivity.class);
                        i.putExtra("AssetImages", pptassetimage);
                        dismissProgressDialog();
                        mActivity.startActivity(i);
                        //gotoCategoriesView();
                    } else {
                        Toast.makeText(mActivity,mActivity.getResources().getString(R.string.erroroccured),Toast.LENGTH_SHORT).show();
                        Log.d("retrofit","error 2");
                        //error
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("retrofit","error 2");
                }
            });
        } else {
            Toast.makeText(mActivity,mActivity.getResources().getString(R.string.error_message),Toast.LENGTH_LONG).show();
        }
        return  pptassetimage;
    }

    public void showProgressDialog(){
        pDialog = new ProgressDialog(mActivity);
        pDialog.setMessage(mActivity.getResources().getString(R.string.loading));
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.setIndeterminate(false);
        pDialog.show();
    }

    public void dismissProgressDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }
}
