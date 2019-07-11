package com.swaas.kangle.NewPlayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.swaas.kangle.R;
import com.swaas.kangle.models.DigitalAssetTransactionChild;
import com.swaas.kangle.models.LstAssetImageModel;
import com.swaas.kangle.preferences.PreferenceUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sayellessh on 15-05-2017.
 */

public class PPTImageFragment extends Fragment {

    private View mView;
    LstAssetImageModel ppt;
    ImageView eventImageView;
    public ViewpagerPlayerActivity viewpagerPlayerActivity;
    Context mContext;
    String imageUrl;
    long starttime,endtime;
    long childstarttime;
    DigitalAssetTransactionChild digitalAssetTransactionChild;
    List<DigitalAssetTransactionChild> child;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            mView = inflater.inflate(R.layout.player_image_list_item, container, false);
            eventImageView = (ImageView) mView.findViewById(R.id.event_imageView);
            child = new ArrayList<DigitalAssetTransactionChild>();
            if (getArguments() != null && getArguments().getSerializable("eventDocument") != null) {
                ppt = (LstAssetImageModel) getArguments().getSerializable("eventDocument");
            }

            viewpagerPlayerActivity = (ViewpagerPlayerActivity) getActivity();
            mContext = viewpagerPlayerActivity.getApplicationContext();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadImage();
        childstarttime = new Date().getTime();
    }

    public void loadImage(){
        if(ppt != null){
            imageUrl = ppt.getImage_Url().toString();
        }

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

    public DigitalAssetTransactionChild insertChildAnalytics(int slidenumber){
        endtime = new Date().getTime();
        digitalAssetTransactionChild = new DigitalAssetTransactionChild();
        digitalAssetTransactionChild.setPlaytime(endtime - childstarttime);
        digitalAssetTransactionChild.setDAID(ppt.getDA_Code());
        digitalAssetTransactionChild.setRead(true);
        digitalAssetTransactionChild.setSlideNo(slidenumber);
        digitalAssetTransactionChild.setRecordDate(new Date().toString());
        return digitalAssetTransactionChild;
    }
    public void setPreferenceUtilEndtime(){
        PreferenceUtils.setEndTime(mContext, endtime);
        Log.d("endtime",String.valueOf(endtime));
    }

    public DigitalAssetTransactionChild getChildAnalytics(){
        return digitalAssetTransactionChild;
    }

    /*public DigitalAssetTransactionMaster getAnalytics(){
        DigitalAssetTransactionMaster digitalAssetTransactionMaster = new DigitalAssetTransactionMaster();
        digitalAssetTransactionMaster.setPlaytime(new Date().getTime() - starttime);
        digitalAssetTransactionMaster.setRecorddate(new Date().toString());
        digitalAssetTransactionMaster.setDAID(ppt.getDA_Code());
        digitalAssetTransactionMaster.setIs_Read(true);
        return digitalAssetTransactionMaster;
    }*/
}
