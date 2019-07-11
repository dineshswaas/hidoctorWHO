package com.swaas.kangle.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swaas.kangle.AssetDetailActivity;
import com.swaas.kangle.R;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.models.DigitalAssetsMaster;

import java.util.List;

/**
 * Created by Sayellessh on 04-05-2017.
 */

public class RelatedAssetListItemRecyclerAdapter extends RecyclerView.Adapter<RelatedAssetListItemRecyclerAdapter.ViewHolder>  {
    Activity mActivity;
    //ArrayList<DigitalAssetsMaster> mAssetsForBrowses;
    List<DigitalAssetsMaster> mAssetsForBrowses;
    String extension = null;
    String filename = null;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;
    String offlineurl;
    DigitalAssetsMaster assetData;
    private ProgressDialog pDialog;

    public RelatedAssetListItemRecyclerAdapter(Activity activity, List<DigitalAssetsMaster> assetsForBrowseList)  {
        mActivity =  activity;
        mAssetsForBrowses = assetsForBrowseList;
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(mActivity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.related_asset_list_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {

        final DigitalAssetsMaster asset = mAssetsForBrowses.get(position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity,AssetDetailActivity.class);
                intent.putExtra("DAID",asset.getDAID());
                mActivity.startActivity(intent);
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity,AssetDetailActivity.class);
                intent.putExtra("DAID",asset.getDAID());
                mActivity.startActivity(intent);
            }
        });

        holder.ratingbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.assetname.setText(asset.getDAName());
        holder.memorysize.setText(asset.getDA_Size_In_MB()+" MB");
        holder.ratingcount.setText(String.valueOf(asset.getTotalRatings()));
        holder.likecount.setText(String.valueOf(asset.getTotalLikes()));
        holder.viewcount.setText(String.valueOf(asset.getTotalViews()));

        /*if(asset.getIsDownloadable().equalsIgnoreCase("y")){
            holder.download.setVisibility(View.VISIBLE);
        }*/


        if (asset.getDA_Type().equalsIgnoreCase("video")) {
            holder.thumbnail.setImageResource(R.drawable.icon_mp4);
        } else if (asset.getDA_Type().equalsIgnoreCase("articulate")) {
            holder.thumbnail.setImageResource(R.drawable.icon_html);
        } else if (asset.getDA_Type().equalsIgnoreCase("image")) {
            holder.thumbnail.setImageResource(R.drawable.icon_jpeg);
        } else if (asset.getDA_Type().equalsIgnoreCase("document")) {
            if(asset.getOnlineURL().endsWith(".pdf")){
                holder.thumbnail.setImageResource(R.drawable.icon_pdf);
            } else if(asset.getOnlineURL().endsWith(".ppt") || asset.getOnlineURL().endsWith(".pptx")){
                holder.thumbnail.setImageResource(R.drawable.icon_ppt2);
            } else{
                holder.thumbnail.setImageResource(R.drawable.icon_doc);
            }

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


    public static class ViewHolder extends RecyclerView.ViewHolder{

        final ImageView thumbnail;
        //final ImageView download;
        final TextView assetname,memorysize,ratingcount,likecount,viewcount;
        final View mView;
        final View ratingbar;

        public ViewHolder(View itemView) {
            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            assetname = (TextView) itemView.findViewById(R.id.asset_name);
            memorysize = (TextView) itemView.findViewById(R.id.memory_size);
            //download = (ImageView) itemView.findViewById(R.id.icon_download);
            ratingcount = (TextView) itemView.findViewById(R.id.rating_count);
            likecount = (TextView) itemView.findViewById(R.id.like_count);
            viewcount = (TextView) itemView.findViewById(R.id.view_count);
            mView = itemView.findViewById(R.id.asset_item);
            ratingbar = itemView.findViewById(R.id.rating_bar);

        }
    }
}
