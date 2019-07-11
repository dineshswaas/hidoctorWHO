package com.swaas.kangle.NewPlayer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.swaas.kangle.R;
import com.swaas.kangle.models.DigitalAssetTransactionMaster;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.utils.TouchImageView;

import java.util.Date;

/**
 * Created by Sayellessh on 12-05-2017.
 */

public class ImageFragment extends Fragment {

    private View mView;
    DigitalAssetsMaster digitalAssetsMaster;
    TouchImageView eventImageView;
    private ViewpagerPlayerActivity viewpagerPlayerActivity;
    String imageUrl;
    long starttime;;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            mView = inflater.inflate(R.layout.player_image_list_item, container, false);
            eventImageView = (TouchImageView) mView.findViewById(R.id.event_imageView);
            if(getArguments() != null && getArguments().getSerializable("assetimage") != null){
                digitalAssetsMaster = (DigitalAssetsMaster) getArguments().getSerializable("assetimage");
            }

             viewpagerPlayerActivity = (ViewpagerPlayerActivity) getActivity();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadImage();
        starttime = new Date().getTime();
    }

    public void loadImage(){
            imageUrl = digitalAssetsMaster.getOnlineURL().toString();

        /*if(imageUrl!=null){
            imageUrl = imageUrl.replaceAll(" ", "%20");
        }*/

        Future<Bitmap> bitmap = Ion.with(getActivity())
                .load(imageUrl).asBitmap();
        bitmap.setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                eventImageView.setImageBitmap(result);
                //LOG_TRACER.d("Position : "+ ppt.getPosition() + " Bitmap >>>>"+result);
            }
        });

    }

    public DigitalAssetTransactionMaster getAnalytics(){
        DigitalAssetTransactionMaster digitalAssetTransactionMaster = new DigitalAssetTransactionMaster();
        digitalAssetTransactionMaster.setPlaytime(new Date().getTime() - starttime);
        digitalAssetTransactionMaster.setRecorddate(new Date().toString());
        if (digitalAssetsMaster.getOnlineURL().contains(Environment.getExternalStorageDirectory().toString())){
            digitalAssetTransactionMaster.setOnlinePlay("0");
            digitalAssetTransactionMaster.setOfflinePlay("1");
        }else{
            digitalAssetTransactionMaster.setOnlinePlay("1");
            digitalAssetTransactionMaster.setOfflinePlay("0");
        }
        digitalAssetTransactionMaster.setDAID(digitalAssetsMaster.getDAID());
        digitalAssetTransactionMaster.setTotalViews(digitalAssetsMaster.getTotalViews()+1);
        digitalAssetTransactionMaster.setIs_Downloaded(digitalAssetsMaster.is_Downloaded());
        digitalAssetTransactionMaster.setIs_Read(true);
        return digitalAssetTransactionMaster;
    }
}
