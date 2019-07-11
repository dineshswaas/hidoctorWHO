package com.swaas.kangle.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.swaas.kangle.API.service.AssetService;
import com.swaas.kangle.DigitalAssetPlayer.PlayMode;
import com.swaas.kangle.DownloadActivity;
import com.swaas.kangle.NewAssetDetailActivity;
import com.swaas.kangle.R;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.db.DigitalAssetTransactionRepository;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.models.AssetImages;
import com.swaas.kangle.models.DigitalAssetTransactionMaster;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.DateHelper;
import com.swaas.kangle.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Retrofit;

/**
 * Created by Sayellessh on 26-04-2017.
 */

public class AssetListItemRecyclerAdapter extends RecyclerView.Adapter<AssetListItemRecyclerAdapter.ViewHolder> {
    Activity mActivity;
    //ArrayList<DigitalAssetsMaster> mAssetsForBrowses;
    List<DigitalAssetsMaster> mAssetsForBrowses;
    String extension = null;
    String filename = null;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;
    DigitalAssetTransactionMaster digitalAssetTransactionMaster;
    DigitalAssetTransactionRepository digitalAssetTransactionRepository;
    String offlineurl;
    DigitalAssetsMaster assetData;
    private ProgressDialog pDialog;
    static int position1;
    DateHelper dateHelper;

    ArrayList<AssetImages> pptassetimage;
    Gson gsonget;
    Retrofit retrofitAPI;
    AssetService assetService;

    DownloadActivity downloadActivity;
    PlayMode playMode;
    boolean isfromRelated;

    public AssetListItemRecyclerAdapter(Activity activity, List<DigitalAssetsMaster> assetsForBrowseList,boolean isfromRelatedfilter)  {
        mActivity =  activity;
        mAssetsForBrowses = assetsForBrowseList;
        gsonget = new Gson();
        retrofitAPI = RetrofitAPIBuilder.getInstance();
        assetService = retrofitAPI.create(AssetService.class);
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(mActivity);
        digitalAssetTransactionRepository = new DigitalAssetTransactionRepository(mActivity);
        downloadActivity = new DownloadActivity(mActivity);
        playMode = new PlayMode(mActivity);
        isfromRelated = isfromRelatedfilter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.asset_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final DigitalAssetsMaster asset = mAssetsForBrowses.get(position);

        setthemeforView(holder);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(mActivity,AssetDetailActivity.class);
                intent.putExtra("DAID",asset.getDAID());
                mActivity.startActivity(intent);*/
                Bundle bundle = new Bundle();
                //bundle.putSerializable("value", (Serializable) mAssetsForBrowses);
                bundle.putSerializable("valuesingle",mAssetsForBrowses.get(position));
                Intent intent = new Intent(mActivity,NewAssetDetailActivity.class);
                intent.putExtras(bundle);
                intent.putExtra("DAID",asset.getDAID());
                intent.putExtra(Constants.POSITION,position);
                mActivity.startActivity(intent);
                if(isfromRelated){
                    mActivity.finish();
                }
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (PreferenceUtils.getOfflineMode(mActivity).equalsIgnoreCase("true")){
                    playMode.offlinePlayClcik(mAssetsForBrowses.get(position),0);
                }else {
                    if (NetworkUtils.checkIfNetworkAvailable(mActivity)) {
                        playMode.onlinePlayClcik(mAssetsForBrowses.get(position),0);
                    } else {
                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }
                }*/
                Bundle bundle = new Bundle();
                //bundle.putSerializable("value", (Serializable) mAssetsForBrowses);
                bundle.putSerializable("valuesingle",mAssetsForBrowses.get(position));
                Intent intent = new Intent(mActivity,NewAssetDetailActivity.class);
                intent.putExtras(bundle);
                intent.putExtra("DAID",asset.getDAID());
                intent.putExtra(Constants.POSITION,position);
                mActivity.startActivity(intent);
                if(isfromRelated){
                    mActivity.finish();
                }
            }
        });

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtils.checkIfNetworkAvailable(mActivity)){
                    assetData = asset;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        int storagePermission = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
                        if (storagePermission == PackageManager.PERMISSION_GRANTED) {
                            assetData = asset;
                            playMode.onDownloadClicked(asset);
                        } else {
                            mActivity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_PERMISSION_STORAGE);
                        }
                    } else {
                        playMode.onDownloadClicked(asset);
                    }
                } else{
                    Toast.makeText(mActivity, mActivity.getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.viewOptionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mActivity, holder.viewOptionsMenu);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                Menu popupMenu = popup.getMenu();
                if(asset.getIsDownloadable().equalsIgnoreCase("y")){
                    if(asset.getDA_Type().equalsIgnoreCase("articulate") || asset.getDA_Type().equalsIgnoreCase("html5")
                            || PreferenceUtils.getOfflineMode(mActivity).equalsIgnoreCase("true")){
                        popupMenu.findItem(R.id.downloaditem).setVisible(false);
                    }else if(!asset.is_Downloaded()) {
                        popupMenu.findItem(R.id.downloaditem).setVisible(true);
                    }
                    /*if(PreferenceUtils.getOfflineMode(mActivity).equalsIgnoreCase("true")){
                        popupMenu.findItem(R.id.downloaditem).setVisible(false);
                    }else if(!asset.is_Downloaded()) {
                        popupMenu.findItem(R.id.downloaditem).setVisible(true);
                    }*/
                }else{
                    popupMenu.findItem(R.id.downloaditem).setVisible(false);
                }

                if(asset.getIsViewable().equalsIgnoreCase("y")) {
                    popupMenu.findItem(R.id.playitem).setVisible(true);
                }else if(asset.getDA_Type().equalsIgnoreCase("articulate")
                        || asset.getDA_Type().equalsIgnoreCase("html5")) {
                    popupMenu.findItem(R.id.playitem).setVisible(true);
                } else {
                    popupMenu.findItem(R.id.playitem).setVisible(false);
                }
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.playitem:
                                if (PreferenceUtils.getOfflineMode(mActivity).equalsIgnoreCase("true")){
                                    playMode.offlinePlayClcik(mAssetsForBrowses.get(position),0);
                                } else {
                                    if (NetworkUtils.checkIfNetworkAvailable(mActivity)) {
                                        playMode.onlinePlayClcik(mAssetsForBrowses.get(position),0);
                                    } else {
                                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                break;
                            case R.id.downloaditem:
                                if(NetworkUtils.checkIfNetworkAvailable(mActivity)){
                                    assetData = asset;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        int storagePermission = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
                                        if (storagePermission == PackageManager.PERMISSION_GRANTED) {
                                            assetData = asset;
                                            playMode.onDownloadClicked(asset);
                                        } else {
                                            mActivity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_PERMISSION_STORAGE);
                                        }
                                    } else {
                                        playMode.onDownloadClicked(asset);
                                    }
                                } else{
                                    Toast.makeText(mActivity, mActivity.getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                                }
                                break;

                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });

        holder.ratingbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.assetname.setText(asset.getDAName());
        holder.memorysize.setText(" "+ asset.getDA_Size_In_MB()+" MB");
        dateHelper = new DateHelper();
        //holder.upload_date.setText(mActivity.getString(R.string.upload_date)+" "+ dateHelper.getUploadDateDisplayFormat(asset.getUploaded_Date(),"MM/dd/yyyy hh:mm:ss a")); //upload date included on 06-02-2018
        String[] date = asset.getUploaded_Date().split(" ");
        date[0].toString();
        holder.upload_date.setText(" "+ date[0].toString()); //upload date included on 06-02-2018
        holder.ratingcount.setText(String.valueOf(asset.getTotalRatings()));
        holder.likecount.setText(String.valueOf(asset.getTotalLikes()));
        holder.viewcount.setText(String.valueOf(asset.getTotalViews()));
        //holder.categoreyName.setText(asset.getDACategoryName());
        String[] words=asset.getUDTags().split("#");
        List<String> tags = new ArrayList<String>();
        for (String word : words){
            tags.add(word);
            if(tags.size()>=3){
                break;
            }
        }
        if(tags.size()==1){
            holder.tagname1.setVisibility(View.VISIBLE);
            holder.tagname1.setText(tags.get(0).toString());
        }else if(tags.size()==2){
            holder.tagname1.setVisibility(View.VISIBLE);
            holder.tagname2.setVisibility(View.VISIBLE);
            holder.tagname1.setText(tags.get(0).toString());
            holder.tagname2.setText(tags.get(1).toString());
        }else if(tags.size()==3){
            holder.tagname1.setVisibility(View.VISIBLE);
            holder.tagname2.setVisibility(View.VISIBLE);
            holder.tagname3.setVisibility(View.VISIBLE);
            holder.tagname1.setText(tags.get(0).toString());
            holder.tagname2.setText(tags.get(1).toString());
            holder.tagname3.setText(tags.get(2).toString());
        }

        if(asset.getIsDownloadable().equalsIgnoreCase("y")){
            if(asset.getDA_Type().equalsIgnoreCase("articulate")
                    || asset.getDA_Type().equalsIgnoreCase("html5")){
                holder.download.setVisibility(View.GONE);
            }else if(!asset.is_Downloaded()) {
                holder.download.setVisibility(View.GONE);
            }
        }

        if(asset.getThumnailURL().contains("wideangleimages")){
            if (asset.getDA_Type().equalsIgnoreCase("video")) {
                holder.thumbnail.setImageResource(R.drawable.icon_mp4);
                holder.doctypeimg.setImageResource(R.drawable.smallicon_mp4);
            } else if (asset.getDA_Type().equalsIgnoreCase("articulate")
                    || asset.getDA_Type().equalsIgnoreCase("html5")) {
                holder.thumbnail.setImageResource(R.drawable.icon_html);
                holder.doctypeimg.setImageResource(R.drawable.smallicon_html);
            } else if (asset.getDA_Type().equalsIgnoreCase("image")) {
                holder.thumbnail.setImageResource(R.drawable.icon_jpeg);
                holder.doctypeimg.setImageResource(R.drawable.smallicon_jpeg);
            } else if (asset.getDA_Type().equalsIgnoreCase("document")) {
                if(asset.getOnlineURL().endsWith(".pdf")){
                    holder.thumbnail.setImageResource(R.drawable.icon_pdf);
                    holder.doctypeimg.setImageResource(R.drawable.smallicon_pdf);
                } else if(asset.getOnlineURL().endsWith(".ppt") || asset.getOnlineURL().endsWith(".pptx")){
                    holder.thumbnail.setImageResource(R.drawable.icon_ppt2);
                    holder.doctypeimg.setImageResource(R.drawable.smallicon_ppt2);
                } else if(asset.getOnlineURL().endsWith("xls") || asset.getOnlineURL().endsWith(".xlsx")){
                    holder.thumbnail.setImageResource(R.drawable.icon_excel);
                    holder.doctypeimg.setImageResource(R.drawable.smallicon_excel);
                } else{
                    holder.thumbnail.setImageResource(R.drawable.icon_doc);
                    holder.doctypeimg.setImageResource(R.drawable.smallicon_doc);
                }

            }

                holder.thumbnail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.ICON_COLOR)));


        }else{
            if(NetworkUtils.checkIfNetworkAvailable(mActivity)){

                if (asset.getDA_Type().equalsIgnoreCase("image")) {
                    Ion.with(holder.thumbnail).fitXY().placeholder(R.drawable.icon_jpeg).fitXY().error(R.drawable.icon_jpeg).fitXY().load(
                            (!TextUtils.isEmpty(asset.getThumnailURL()))? asset.getThumnailURL() : asset.getThumnailURL());
                    holder.doctypeimg.setImageResource(R.drawable.smallicon_jpeg);
                } else if(asset.getDA_Type().equalsIgnoreCase("video")){
                    Ion.with(holder.thumbnail).fitXY().placeholder(R.drawable.icon_mp4).fitXY().error(R.drawable.icon_mp4).fitXY().load(
                            (!TextUtils.isEmpty(asset.getThumnailURL()))? asset.getThumnailURL() : asset.getThumnailURL());
                    holder.doctypeimg.setImageResource(R.drawable.smallicon_mp4);
                }else if(asset.getDA_Type().equalsIgnoreCase("articulate")
                    || asset.getDA_Type().equalsIgnoreCase("html5")){
                    Ion.with(holder.thumbnail).fitXY().placeholder(R.drawable.icon_html).fitXY().error(R.drawable.icon_html).fitXY().load(
                            (!TextUtils.isEmpty(asset.getThumnailURL()))? asset.getThumnailURL() : asset.getThumnailURL());
                    holder.doctypeimg.setImageResource(R.drawable.smallicon_html);
                }else if (asset.getDA_Type().equalsIgnoreCase("document")) {
                    if(asset.getOnlineURL().endsWith(".pdf")){
                        Ion.with(holder.thumbnail).fitXY().placeholder(R.drawable.icon_pdf).fitXY().error(R.drawable.icon_pdf).fitXY().load(
                                (!TextUtils.isEmpty(asset.getThumnailURL()))? asset.getThumnailURL() : asset.getThumnailURL());
                        holder.doctypeimg.setImageResource(R.drawable.smallicon_pdf);
                    } else if(asset.getOnlineURL().endsWith(".ppt") || asset.getOnlineURL().endsWith(".pptx")){
                        Ion.with(holder.thumbnail).fitXY().placeholder(R.drawable.icon_ppt2).fitXY().error(R.drawable.icon_ppt2).fitXY().load(
                                (!TextUtils.isEmpty(asset.getThumnailURL()))? asset.getThumnailURL() : asset.getThumnailURL());
                        holder.doctypeimg.setImageResource(R.drawable.smallicon_ppt2);
                    }else if(asset.getOnlineURL().endsWith(".xls") || asset.getOnlineURL().endsWith(".xlsx")){
                        Ion.with(holder.thumbnail).fitXY().placeholder(R.drawable.icon_excel).fitXY().error(R.drawable.icon_excel).fitXY().load(
                                (!TextUtils.isEmpty(asset.getThumnailURL()))? asset.getThumnailURL() : asset.getThumnailURL());
                        holder.doctypeimg.setImageResource(R.drawable.smallicon_excel);
                    } else{
                        Ion.with(holder.thumbnail).fitXY().placeholder(R.drawable.icon_doc).fitXY().error(R.drawable.icon_doc).fitXY().load(
                                (!TextUtils.isEmpty(asset.getThumnailURL()))? asset.getThumnailURL() : asset.getThumnailURL());
                        holder.doctypeimg.setImageResource(R.drawable.smallicon_doc);
                    }
                }
                //holder.thumbnail.setImageBitmap(getBitmapFromURL(asset.getThumnailURL()));
            } else {
                if (asset.getDA_Type().equalsIgnoreCase("video")) {
                    holder.thumbnail.setImageResource(R.drawable.icon_mp4);
                    holder.doctypeimg.setImageResource(R.drawable.smallicon_mp4);
                } else if (asset.getDA_Type().equalsIgnoreCase("articulate") ||
                        asset.getDA_Type().equalsIgnoreCase("html5")) {
                    holder.thumbnail.setImageResource(R.drawable.icon_html);
                    holder.doctypeimg.setImageResource(R.drawable.smallicon_html);
                } else if (asset.getDA_Type().equalsIgnoreCase("image")) {
                    holder.thumbnail.setImageResource(R.drawable.icon_jpeg);
                    holder.doctypeimg.setImageResource(R.drawable.smallicon_jpeg);
                } else if (asset.getDA_Type().equalsIgnoreCase("document")) {
                    if(asset.getOnlineURL().endsWith(".pdf")){
                        holder.thumbnail.setImageResource(R.drawable.icon_pdf);
                        holder.doctypeimg.setImageResource(R.drawable.smallicon_pdf);
                    } else if(asset.getOnlineURL().endsWith(".ppt") || asset.getOnlineURL().endsWith(".pptx")){
                        holder.thumbnail.setImageResource(R.drawable.icon_ppt2);
                        holder.doctypeimg.setImageResource(R.drawable.smallicon_ppt2);
                    } else if(asset.getOnlineURL().endsWith("xls") || asset.getOnlineURL().endsWith(".xlsx")){
                        holder.thumbnail.setImageResource(R.drawable.icon_excel);
                        holder.doctypeimg.setImageResource(R.drawable.smallicon_excel);
                    }else{
                        holder.thumbnail.setImageResource(R.drawable.icon_doc);
                        holder.doctypeimg.setImageResource(R.drawable.smallicon_doc);
                    }
                }
            }

                holder.thumbnail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.ICON_COLOR)));


        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = mAssetsForBrowses.size();
        } catch (Exception e) {

        }
        return count;
    }

    public void setthemeforView(final ViewHolder holder){

        /*if(PreferenceUtils.getSubdomainName(mActivity).contains("tacobell")){
            Constants.COMPANY_COLOR = "#702082";
            Constants.OPAQUE_COLOR = "#cdcdcd";
            Constants.HEADERBAR_COLOR = "#303030";
            Constants.CARDBACKGROUND_COLOR = "#9C7EB4";
            Constants.COMPLETED_COLOR = "#0F9D58";
            Constants.INPROGRESS_COLOR = "#ffffff";
            Constants.EXPIRED_COLOR = "#f91b00";
            Constants.TEXT_COLOR = "#ffffff";
            Constants.ICON_COLOR = "#ffffff";
            Constants.PARTIALLY_COMPLETED_COLOR = "#FF0000FF";
            Constants.GREY_COLOR = "#8f8f8f";
        }else{
            Constants.COMPANY_COLOR = "#3F51B5";
            Constants.OPAQUE_COLOR = "#cdcdcd";
            Constants.HEADERBAR_COLOR = "#303030";
            Constants.CARDBACKGROUND_COLOR = "#9C7EB4";
            Constants.COMPLETED_COLOR = "#0F9D58";
            Constants.INPROGRESS_COLOR = "#ffffff";
            Constants.EXPIRED_COLOR = "#f91b00";
            Constants.TEXT_COLOR = "#ffffff";
            Constants.ICON_COLOR = "#ffffff";
            Constants.PARTIALLY_COMPLETED_COLOR = "#FF0000FF";
            Constants.GREY_COLOR = "#8f8f8f";
        }*/

            holder.thumbnail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.ICON_COLOR)));


        holder.thumbnail.setBackgroundColor(mActivity.getResources().getColor(R.color.topbar));
        holder.assetname.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.upload_date.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.ratingcount.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.likecount.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.viewcount.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.cardviewLayout.setCardBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        holder.upload_date_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.icon_like.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        holder.icon_views.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        holder.doctypeimg.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final ImageView thumbnail,doctypeimg;
        final ImageView download;
        final TextView assetname,memorysize,upload_date,ratingcount,likecount,viewcount;
        final View mView;
        final View ratingbar;
        final TextView categoreyName;
        final ImageView viewOptionsMenu;
        TextView tagname1,tagname2,tagname3;
        TextView startdatevalue,enddatevalue;
        TextView upload_date_text;
        CardView cardviewLayout;
        ImageView icon_like,icon_views;

        public ViewHolder(View itemView) {
            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            doctypeimg = (ImageView) itemView.findViewById(R.id.doctypeimg);
            assetname = (TextView) itemView.findViewById(R.id.asset_name);
            memorysize = (TextView) itemView.findViewById(R.id.memory_size);
            download = (ImageView) itemView.findViewById(R.id.icon_download);
            ratingcount = (TextView) itemView.findViewById(R.id.rating_count);
            likecount = (TextView) itemView.findViewById(R.id.like_count);
            viewcount = (TextView) itemView.findViewById(R.id.view_count);
            mView = itemView.findViewById(R.id.asset_item);
            ratingbar = itemView.findViewById(R.id.rating_bar);
            viewOptionsMenu = (ImageView) itemView.findViewById(R.id.viewOptionsMenu);

            categoreyName = (TextView) itemView.findViewById(R.id.categoreyName);

            tagname1 = (TextView) itemView.findViewById(R.id.tagname1);
            tagname2 = (TextView) itemView.findViewById(R.id.tagname2);
            tagname3 = (TextView) itemView.findViewById(R.id.tagname3);

            upload_date = (TextView)itemView.findViewById(R.id.upload_date);
            startdatevalue = (TextView)itemView.findViewById(R.id.startdatevalue);
            enddatevalue = (TextView)itemView.findViewById(R.id.enddatevalue);

            cardviewLayout = (CardView) itemView.findViewById(R.id.cardviewLayout);
            upload_date_text = (TextView) itemView.findViewById(R.id.upload_date_text);

            icon_like = (ImageView) itemView.findViewById(R.id.icon_like);
            icon_views = (ImageView) itemView.findViewById(R.id.icon_views);
        }
    }

}
