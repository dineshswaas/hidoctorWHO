package com.swaas.kangle.DigitalAssetPlayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.swaas.kangle.DownloadActivity;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.playerPart.DigitalAssets;
import com.swaas.kangle.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Sayellessh on 20-09-2017.
 */

public class PlayMode {

     Activity mActivity;
    DownloadActivity downloadActivity;
    Context mContext;

    public PlayMode(Activity activity){
        mActivity = activity;
        mContext = mActivity;
        downloadActivity = new DownloadActivity(mContext);
    }

    public void onlinePlayClcik(DigitalAssetsMaster  digitalAssetsList,int CurrentDigigtalAsset){
        ArrayList<DigitalAssets> assets = new ArrayList<DigitalAssets>();
        DigitalAssetsMaster courseAssetModel = digitalAssetsList;
        //for (DigitalAssetsMaster courseAssetModel : digitalAssetsList) {
            DigitalAssets digitalAssets = new DigitalAssets();
            digitalAssets.setDA_Code(Integer.parseInt(courseAssetModel.getDAID()));
            digitalAssets.setDA_Online_URL(courseAssetModel.getOnlineURL());
             digitalAssets.setAsset_Name(courseAssetModel.getDAName());
            if (courseAssetModel.getDA_Type().equalsIgnoreCase("document")) {
                if (courseAssetModel.getOnlineURL().endsWith(".pdf")) {
                    digitalAssets.setDA_Type(com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.util.Constants.PDFASSET);
                }
            }else if(courseAssetModel.getDA_Type().equalsIgnoreCase("articulate") ||
                    courseAssetModel.getDA_Type().equalsIgnoreCase("html5")){
                digitalAssets.setDA_Type(com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.util.Constants.HTMLASSET);
            } else if (courseAssetModel.getDA_Type().equalsIgnoreCase("mp3")) {
                digitalAssets.setDA_Type(com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.util.Constants.AUDIOASSET);
            } else if (courseAssetModel.getDA_Type().equalsIgnoreCase("video")) {
                if(courseAssetModel.getVideoType().equalsIgnoreCase("BV")) {
                    digitalAssets.setDA_Type(com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.util.Constants.BRIGHTCOVE);
                    digitalAssets.setVideoId(courseAssetModel.getVideoId());
                    digitalAssets.setPolicyKey(courseAssetModel.getPolicyKey());
                    digitalAssets.setAccountId(courseAssetModel.getAccountId());
                }else{
                    digitalAssets.setDA_Type(com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.util.Constants.VIDEOASSET);
                }
            } else if (courseAssetModel.getDA_Type().equalsIgnoreCase("image")){
                digitalAssets.setDA_Type(com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.util.Constants.IMAGEASSET);
            }
            digitalAssets.setTotal_Views(courseAssetModel.getTotalViews());
            digitalAssets.setLike(courseAssetModel.getTotalLikes());
            digitalAssets.setRating(courseAssetModel.getTotalRatings());

            /*if (!courseAssetModel.getOnlineURL().endsWith(".doc") && !courseAssetModel.getOnlineURL().endsWith(".ppt") &&
                    !courseAssetModel.getOnlineURL().endsWith(".xls") &&!courseAssetModel.getOnlineURL().endsWith(".xlsx")
                    && !courseAssetModel.getOnlineURL().endsWith(".ppt") && !courseAssetModel.getOnlineURL().endsWith(".doc")
                    && !courseAssetModel.getOnlineURL().endsWith(".docx")){*/
                assets.add(digitalAssets);
            //}
        //}
        Bundle bundle = new Bundle();
        bundle.putSerializable("value", assets);
        Intent intentToPlayer = new Intent(mActivity, DigitalAssetPlayerActivity.class);
        intentToPlayer.putExtras(bundle);
        intentToPlayer.putExtra(Constants.POSITION,CurrentDigigtalAsset);
        mActivity.startActivity(intentToPlayer);
    }

    public void offlinePlayClcik(DigitalAssetsMaster  digitalAssetsList,int CurrentDigigtalAsset){
        ArrayList<DigitalAssets> assets = new ArrayList<DigitalAssets>();
        DigitalAssetsMaster courseAssetModel = digitalAssetsList;
        //for (DigitalAssetsMaster courseAssetModel : digitalAssetsList) {
            DigitalAssets digitalAssets = new DigitalAssets();
            digitalAssets.setDA_Code(Integer.parseInt(courseAssetModel.getDAID()));
            digitalAssets.setDA_Offline_URL(courseAssetModel.getOfflineURL());
            digitalAssets.setAsset_Name(courseAssetModel.getDAName());
            if (courseAssetModel.getDA_Type().equalsIgnoreCase("document")) {
                if (courseAssetModel.getOnlineURL().endsWith(".pdf")) {
                    digitalAssets.setDA_Type(com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.util.Constants.PDFASSET);
                }
            }else if(courseAssetModel.getDA_Type().equalsIgnoreCase("articulate") ||
                    courseAssetModel.getDA_Type().equalsIgnoreCase("html5")){
                digitalAssets.setDA_Type(com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.util.Constants.HTMLASSET);
            } else if (courseAssetModel.getDA_Type().equalsIgnoreCase("mp3")) {
                digitalAssets.setDA_Type(com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.util.Constants.AUDIOASSET);
            } else if (courseAssetModel.getDA_Type().equalsIgnoreCase("video")) {
                digitalAssets.setDA_Type(com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.util.Constants.VIDEOASSET);
            } else if (courseAssetModel.getDA_Type().equalsIgnoreCase("image")){
                digitalAssets.setDA_Type(com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.util.Constants.IMAGEASSET);
            }

            digitalAssets.setTotal_Views(courseAssetModel.getTotalViews());
            digitalAssets.setLike(courseAssetModel.getTotalLikes());
            digitalAssets.setRating(courseAssetModel.getTotalRatings());

            if (!courseAssetModel.getOnlineURL().endsWith(".doc") && !courseAssetModel.getOnlineURL().endsWith(".ppt") &&
                    !courseAssetModel.getOnlineURL().endsWith(".xls") &&!courseAssetModel.getOnlineURL().endsWith(".xlsx")
                    && !courseAssetModel.getOnlineURL().endsWith(".ppt") && !courseAssetModel.getOnlineURL().endsWith(".doc")
                    && !courseAssetModel.getOnlineURL().endsWith(".docx")){
                assets.add(digitalAssets);
            //}
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("value", assets);
        Intent intentToPlayer = new Intent(mActivity, DigitalAssetPlayerActivity.class);
        intentToPlayer.putExtras(bundle);
        intentToPlayer.putExtra(Constants.POSITION,CurrentDigigtalAsset);
        mActivity.startActivity(intentToPlayer);
    }

    public void onDownloadClicked(DigitalAssetsMaster digitalAssetsMaster){
        if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("document")) {
            if (digitalAssetsMaster.getOnlineURL().endsWith(".ppt")
                    || digitalAssetsMaster.getOnlineURL().endsWith(".pptx")
                    || digitalAssetsMaster.getOnlineURL().endsWith(".pdf")
                    || digitalAssetsMaster.getOnlineURL().endsWith(".doc")
                    || digitalAssetsMaster.getOnlineURL().endsWith(".docx")) {
                downloadActivity.startDownload(mActivity,digitalAssetsMaster);
            } else {
                downloadActivity.startDownload(mActivity,digitalAssetsMaster);
            }
        } else {
            downloadActivity.startDownload(mContext,digitalAssetsMaster);
        }
    }
}
