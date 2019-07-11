package com.swaas.kangle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.swaas.kangle.GlobalPlayer.GlobalPlayer;
import com.swaas.kangle.R;
import com.swaas.kangle.models.DigitalAssetsMaster;

import java.util.List;

/**
 * Created by Sayellessh on 09-05-2017.
 */

public class AssetRecyclerAdapter extends RecyclerView.Adapter<AssetRecyclerAdapter.ViewHolder>  {

    private static MyClickListener myClickListener;
    GlobalPlayer mActivity;
    List<DigitalAssetsMaster> assets;

    public AssetRecyclerAdapter(Context context, List<DigitalAssetsMaster> assetList)  {
        mActivity = (GlobalPlayer) context;
        assets = assetList;
    }

    public DigitalAssetsMaster getItemAt(final int position){
        return assets.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.image_list_item, parent, false);
        return new AssetRecyclerAdapter.ViewHolder(view);
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DigitalAssetsMaster asset = assets.get(position);

        holder.mView.setBackgroundColor(mActivity.getResources().getColor(R.color.black));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                if (asset.getDA_Type().equalsIgnoreCase("video")) {
                    mActivity.initvideo(asset);
                } else if(asset.getDA_Type().equalsIgnoreCase("image")){
                    mActivity.initImage(asset);
                } else if(asset.getDA_Type().equalsIgnoreCase("document")){
                    if(asset.getOnlineURL().endsWith(".ppt")
                            || asset.getOnlineURL().endsWith(".ppt")
                            || asset.getDAName().endsWith(".pdf")){
                        //mActivity.loadChildAssets();
                        mActivity.getPPTAssetImages(asset);
                        //mActivity.initppt(asset,position);
                    }
                }else if(asset.getDA_Type().equalsIgnoreCase("articulate")
                        || asset.getDA_Type().equalsIgnoreCase("html")){
                        mActivity.initWebView(asset);
                }
                holder.mView.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
                if(myClickListener != null){
                    myClickListener.onItemClick(position,v);
                }
            }
        });

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
    }



    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = assets.size();
        } catch (Exception e) {

        }
        return count;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final ImageView type_of_asset;
        final View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView.findViewById(R.id.mView);
            type_of_asset = (ImageView) itemView.findViewById(R.id.type_of_asset);

        }
    }
}
