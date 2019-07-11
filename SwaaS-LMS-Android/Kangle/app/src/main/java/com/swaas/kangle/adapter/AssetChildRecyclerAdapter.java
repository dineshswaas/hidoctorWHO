package com.swaas.kangle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.swaas.kangle.GlobalPlayer.GlobalPlayer;
import com.swaas.kangle.R;
import com.swaas.kangle.models.LstAssetImageModel;

import java.util.List;

/**
 * Created by Sayellessh on 09-05-2017.
 */

public class AssetChildRecyclerAdapter extends RecyclerView.Adapter<AssetChildRecyclerAdapter.ViewHolder>  {

    private static MyClickListener myClickListener;
    GlobalPlayer mActivity;
    List<LstAssetImageModel> pptassets;
    public static String url;

    public AssetChildRecyclerAdapter(Context context, List<LstAssetImageModel> assetList)  {
        mActivity = (GlobalPlayer) context;
        pptassets = assetList;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public LstAssetImageModel getItemAt(final int position){
        return pptassets.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.image_list_item, parent, false);
        return new AssetChildRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        final LstAssetImageModel asset = pptassets.get(position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               url = asset.getImage_Url();
                mActivity.initpptImage(asset);

                if(myClickListener != null){
                    myClickListener.onItemClick(position,v);
                }
            }
        });


        holder.type_of_asset.setImageResource(R.drawable.icon_jpeg);
    }

    public void initImage(LstAssetImageModel url){
        mActivity.initpptImage(url);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = pptassets.size();
        } catch (Exception e) {

        }
        return count;
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
    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}