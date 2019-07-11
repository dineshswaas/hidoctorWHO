package com.swaas.kangle.DigitalAssetPlayer;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.swaas.kangle.R;
import com.swaas.kangle.playerPart.DigitalAssets;

import java.util.List;

public class AssetListOnPlayerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context mContext;
    private OnAssetListPlayerItemClicked onAssetListPlayerItemClicked;
    public List<DigitalAssets> DigitalAssetList;
    public String DigitalAssetName;
    public static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_STORY_ITEM = 2;
    int count = 1;


    public AssetListOnPlayerAdapter(Context context, OnAssetListPlayerItemClicked onAssetListPlayerItemClicked, List<DigitalAssets> digitalAssetsList){

        this.mContext = context;
        this.onAssetListPlayerItemClicked = onAssetListPlayerItemClicked;
        this.DigitalAssetList = digitalAssetsList;

        if(DigitalAssetList != null && DigitalAssetList.size() > 0){
            for(DigitalAssets digitalAsset : DigitalAssetList)
            {
                digitalAsset.setDisplay_Order(count++);

            }
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        View view;


        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(mContext).inflate(R.layout.asset_list_on_player_row,parent,false);
            viewHolder = new MyAssetListOnPlayerViewHolder(view);
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        DigitalAssets digitalAssets = DigitalAssetList.get(position);
            ((MyAssetListOnPlayerViewHolder)holder).display_order.setText(String.valueOf(digitalAssets.getDisplay_Order()));
            ((MyAssetListOnPlayerViewHolder)holder).mAssetname.setText(DigitalAssetList.get(position).getAsset_Name());
            LoadThumbnail(DigitalAssetList.get(position),((MyAssetListOnPlayerViewHolder)holder).img_asset_list_thumnail);
            ((MyAssetListOnPlayerViewHolder)holder).mAssetListRowHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onAssetListPlayerItemClicked.OnAssetListRowItemClicked(position);

                }
            });

    }





    @Override
    public int getItemViewType(int position) {
        return DigitalAssetList.get(position).getViewType();
    }

    private void LoadThumbnail(DigitalAssets digitalAssets, ImageView img_asset_list_thumnail) {

        if (digitalAssets.getDA_Thumbnail_URL()!=null&&digitalAssets.getDA_Thumbnail_URL().length()>0){

            Ion.with(mContext).load(digitalAssets.getDA_Thumbnail_URL()).intoImageView(img_asset_list_thumnail);
        }else {

            loadLocalThumbnail(digitalAssets,img_asset_list_thumnail);

        }

    }

    private void loadLocalThumbnail(DigitalAssets digitalAssets, ImageView img_asset_list_thumnail) {

        switch (digitalAssets.getDA_Type_Name()){

            case "IMAGE" :

                img_asset_list_thumnail.setImageResource(R.mipmap.ic_image_yellow);

                break;

            case "VIDEO" :

                img_asset_list_thumnail.setImageResource(R.mipmap.ic_video_blue);

                break;

            case "AUDIO" :

                img_asset_list_thumnail.setImageResource(R.mipmap.ic_audio_li_red);

                break;

            case "PDF" :

                img_asset_list_thumnail.setImageResource(R.mipmap.ic_pdf_red);

                break;

            case "HTML5" :

                img_asset_list_thumnail.setImageResource(R.mipmap.ic_html_lightgreen);

                break;

            default:

                break;
        }

    }

    @Override
    public int getItemCount() {
        return DigitalAssetList.size()>0?DigitalAssetList.size():0;
    }





    public class MyAssetListOnPlayerViewHolder extends RecyclerView.ViewHolder {

        public TextView mAssetname, display_order;
        public ImageView img_asset_list_thumnail;
        public LinearLayout mAssetListRowHolder;


        public MyAssetListOnPlayerViewHolder(View itemView) {
            super(itemView);

            mAssetname =  (TextView)itemView .findViewById(R.id.asset_name);
            mAssetListRowHolder = (LinearLayout)itemView.findViewById(R.id.asset_list_rowholder);
            img_asset_list_thumnail = (ImageView) itemView.findViewById(R.id.iv_asset_list_thumnail);
            display_order = (TextView)itemView.findViewById(R.id.circle_shape_display_order);

        }

    }




}