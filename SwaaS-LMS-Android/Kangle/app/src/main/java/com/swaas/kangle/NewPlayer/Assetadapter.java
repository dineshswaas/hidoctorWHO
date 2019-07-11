package com.swaas.kangle.NewPlayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.swaas.kangle.R;
import com.swaas.kangle.models.LstAssetImageModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Sayellessh on 12-05-2017.
 */

public class Assetadapter extends RecyclerView.Adapter<Assetadapter.ViewHolder> {

    PPTAssetImagesActivity mActivity;
    List<LstAssetImageModel> pptassets;

    public Assetadapter(PPTAssetImagesActivity context, List<LstAssetImageModel> assetList)  {
        mActivity = (PPTAssetImagesActivity) context;
        pptassets = assetList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.new_image_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final LstAssetImageModel  model= pptassets.get(position);

        //holder.type_of_asset.setImageBitmap(getBitmapFromURL(model.getImage_Url()));
        Ion.with(holder.type_of_asset).fitXY().placeholder(R.drawable.icon_jpeg).fitXY().error(R.drawable.icon_jpeg).fitXY().load(
                (!TextUtils.isEmpty(model.getImage_Url()))? model.getImage_Url() : model.getImage_Url());
        holder.assetName.setText(model.getImage_Name());

        holder.type_of_asset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mActivity,ViewpagerPlayerActivity.class);
                i.putExtra("PPTAssetImages",(Serializable) pptassets);
                i.putExtra("isppt","PPTImages");
                i.putExtra("position",position);
                mActivity.startActivity(i);
            }
        });
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

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
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
}
