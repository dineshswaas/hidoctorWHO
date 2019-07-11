package com.swaas.kangle.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.swaas.kangle.NewAssetDetailActivity;
import com.swaas.kangle.R;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sayellessh on 16-05-2017.
 */

public class NewRelatedAssetListItemRecyclerAdapter extends RecyclerView.Adapter<NewRelatedAssetListItemRecyclerAdapter.ViewHolder>  {

    Activity mActivity;
    //ArrayList<DigitalAssetsMaster> mAssetsForBrowses;
    List<DigitalAssetsMaster> mAssetsForBrowses;
    String extension = null;
    String filename = null;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;
    String offlineurl;
    DigitalAssetsMaster assetData;
    private ProgressDialog pDialog;

    public NewRelatedAssetListItemRecyclerAdapter(Activity activity, List<DigitalAssetsMaster> assetsForBrowseList)  {
        mActivity =  activity;
        mAssetsForBrowses = assetsForBrowseList;
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(mActivity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.new_related_asset_list_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {

        final DigitalAssetsMaster asset = mAssetsForBrowses.get(position);

        setthemeforView(holder);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<DigitalAssetsMaster> assets = new ArrayList<DigitalAssetsMaster>();
                assets.add(asset);
                Intent intent = new Intent(mActivity,NewAssetDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("valuesingle",asset);
                //bundle.putSerializable("value", (Serializable) assets);
                intent.putExtras(bundle);
                intent.putExtra("DAID",asset.getDAID());
                intent.putExtra(Constants.POSITION,0);
                mActivity.startActivity(intent);
                //mActivity.finish();
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<DigitalAssetsMaster> assets = new ArrayList<DigitalAssetsMaster>();
                assets.add(asset);
                Intent intent = new Intent(mActivity,NewAssetDetailActivity.class);
                Bundle bundle = new Bundle();
                //bundle.putSerializable("value", (Serializable) assets);
                bundle.putSerializable("valuesingle",asset);
                intent.putExtras(bundle);
                intent.putExtra("DAID",asset.getDAID());
                intent.putExtra(Constants.POSITION,0);
                mActivity.startActivity(intent);
                //mActivity.finish();
            }
        });

        holder.ratingbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.assetname.setText(asset.getDAName());
        holder.ratingcount.setText(String.valueOf(asset.getTotalRatings()));
        holder.likecount.setText(String.valueOf(asset.getTotalLikes()));
        holder.viewcount.setText(String.valueOf(asset.getTotalViews()));
        holder.category_name.setText(asset.getDACategoryName());

        /*if(asset.getIsDownloadable().equalsIgnoreCase("y")){
            holder.download.setVisibility(View.VISIBLE);
        }*/

        if(asset.getThumnailURL().contains("wideangleimages")){
            if (asset.getDA_Type().equalsIgnoreCase("video")) {
                holder.thumbnail.setImageResource(R.drawable.icon_mp4);
            } else if (asset.getDA_Type().equalsIgnoreCase("articulate")
                    || asset.getDA_Type().equalsIgnoreCase("html5")) {
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

                holder.thumbnail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.ICON_COLOR)));

        }else{
            if(NetworkUtils.checkIfNetworkAvailable(mActivity)){
                if (asset.getDA_Type().equalsIgnoreCase("image")) {
                Ion.with(holder.thumbnail).fitXY().placeholder(R.drawable.icon_jpeg).fitXY().error(R.drawable.icon_jpeg).fitXY().load(
                        (!TextUtils.isEmpty(asset.getThumnailURL()))? asset.getThumnailURL() : asset.getThumnailURL());
                } else if(asset.getDA_Type().equalsIgnoreCase("video")){
                    Ion.with(holder.thumbnail).fitXY().placeholder(R.drawable.icon_mp4).fitXY().error(R.drawable.icon_mp4).fitXY().load(
                            (!TextUtils.isEmpty(asset.getThumnailURL()))? asset.getThumnailURL() : asset.getThumnailURL());
                }else if(asset.getDA_Type().equalsIgnoreCase("articulate")
                        || asset.getDA_Type().equalsIgnoreCase("html5")){
                    Ion.with(holder.thumbnail).fitXY().placeholder(R.drawable.icon_html).fitXY().error(R.drawable.icon_html).fitXY().load(
                            (!TextUtils.isEmpty(asset.getThumnailURL()))? asset.getThumnailURL() : asset.getThumnailURL());
                }else if (asset.getDA_Type().equalsIgnoreCase("document")) {
                    if(asset.getOnlineURL().endsWith(".pdf")){
                        Ion.with(holder.thumbnail).fitXY().placeholder(R.drawable.icon_pdf).fitXY().error(R.drawable.icon_pdf).fitXY().load(
                                (!TextUtils.isEmpty(asset.getThumnailURL()))? asset.getThumnailURL() : asset.getThumnailURL());
                    } else if(asset.getOnlineURL().endsWith(".ppt") || asset.getOnlineURL().endsWith(".pptx")){
                        Ion.with(holder.thumbnail).fitXY().placeholder(R.drawable.icon_ppt2).fitXY().error(R.drawable.icon_ppt2).fitXY().load(
                                (!TextUtils.isEmpty(asset.getThumnailURL()))? asset.getThumnailURL() : asset.getThumnailURL());
                    } else{
                        Ion.with(holder.thumbnail).fitXY().placeholder(R.drawable.icon_doc).fitXY().error(R.drawable.icon_doc).fitXY().load(
                                (!TextUtils.isEmpty(asset.getThumnailURL()))? asset.getThumnailURL() : asset.getThumnailURL());
                    }
                }
            } else {
                if (asset.getDA_Type().equalsIgnoreCase("video")) {
                    holder.thumbnail.setImageResource(R.drawable.icon_mp4);
                } else if (asset.getDA_Type().equalsIgnoreCase("articulate")
                        || asset.getDA_Type().equalsIgnoreCase("html5")) {
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
            Constants.EXPIRED_COLOR = "#FFFF0000";
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
            Constants.EXPIRED_COLOR = "#FFFF0000";
            Constants.TEXT_COLOR = "#ffffff";
            Constants.ICON_COLOR = "#ffffff";
            Constants.PARTIALLY_COMPLETED_COLOR = "#FF0000FF";
            Constants.GREY_COLOR = "#8f8f8f";
        }*/

        holder.thumbnail.setBackgroundColor(mActivity.getResources().getColor(R.color.topbar));
        holder.assetname.setTextColor(Color.parseColor(Constants.TEXT_COLOR));

        holder.ratingcount.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.likecount.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.viewcount.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.cardviewLayout.setCardBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));

        holder.icon_like.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        holder.icon_view.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        final ImageView thumbnail;
        final ImageView icon_view,icon_like;
        final TextView assetname,ratingcount,likecount,category_name,viewcount;
        final View mView;
        final View ratingbar;
        final CardView cardviewLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            cardviewLayout = (CardView) itemView.findViewById(R.id.cardviewLayout);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            assetname = (TextView) itemView.findViewById(R.id.asset_name);
            //download = (ImageView) itemView.findViewById(R.id.icon_download);
            ratingcount = (TextView) itemView.findViewById(R.id.rating_count);
            viewcount = (TextView) itemView.findViewById(R.id.view_count);
            likecount = (TextView) itemView.findViewById(R.id.like_count);
            mView = itemView.findViewById(R.id.asset_item);
            ratingbar = itemView.findViewById(R.id.rating_bar);
            category_name = (TextView) itemView.findViewById(R.id.category_name);

            icon_view = (ImageView) itemView.findViewById(R.id.icon_view);
            icon_like = (ImageView) itemView.findViewById(R.id.icon_like);
        }
    }
}
