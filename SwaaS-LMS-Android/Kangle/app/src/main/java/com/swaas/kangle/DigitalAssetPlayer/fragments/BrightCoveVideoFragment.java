package com.swaas.kangle.DigitalAssetPlayer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightcove.player.edge.Catalog;
import com.brightcove.player.edge.VideoListener;
import com.brightcove.player.event.EventEmitter;
import com.brightcove.player.model.Video;
import com.brightcove.player.view.BrightcoveExoPlayerVideoView;
import com.google.gson.Gson;
import com.swaas.kangle.DigitalAssetPlayer.DigitalAssetPlayerActivity;
import com.swaas.kangle.R;
import com.swaas.kangle.playerPart.DigitalAssets;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sayellessh on 03-04-2018.
 */

public class BrightCoveVideoFragment extends Fragment {

    private Context mContext;
    private String mVideoUrl;
    private int CurrentAssetPosition,playmode;
    private DigitalAssetPlayerActivity assetPlayerActivity;
    private BrightcoveExoPlayerVideoView brightcoveVideoView;

    /*public String AccountId = "4800266849001";
    public String PolicyKey = "BCpkADawqM3n0ImwKortQqSZCgJMcyVbb8lJVwt0z16UD0a_h8MpEYcHyKbM8CGOPxBRp0nfSVdfokXBrUu3Sso7Nujv3dnLo0JxC_lNXCl88O7NJ0PR0z2AprnJ_Lwnq7nTcy1GBUrQPr5e";
    public String VideoId = "5255514387001";*/


    /*public String AccountId = "5758753464001";
    public String PolicyKey = "BCpkADawqM3RSUdhZcVqUb2t1BZvbQ6rTIJECTg-V_RL85NDVPC0oOyMyXwvnooP2RvMUb2GMYZKRKMZbCCBhuD6X2K25H6k2gi_fN3dCGiuFWYuPr4Ag579FpxPh432zuAl9i5DToX-CgLV";
    public String VideoId = "5758850080001";*/

    public String AccountId;
    public String PolicyKey;
    public String VideoId;
    private Date DetailStartTime = null;
    public int initialStartTime = -1;
    DigitalAssets assetobj;
    Gson gsonget;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        gsonget = new Gson();
        mContext =  getActivity().getApplicationContext();
        assetPlayerActivity = (DigitalAssetPlayerActivity)getActivity();

        /*if (bundle!=null){
            mVideoUrl = bundle.getString("url");
            CurrentAssetPosition = bundle.getInt("Index");
            playmode =  bundle.getInt("playmode");
        }*/

        if (bundle!=null){

            mVideoUrl = bundle.getString("url");
            CurrentAssetPosition = bundle.getInt("Index");
            playmode =  bundle.getInt("playmode");
            VideoId = bundle.getString("VideoId");
            AccountId = bundle.getString("AccountId");
            PolicyKey = bundle.getString("PolicyKey");

        }


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_brightcove_player_viewer,container,false);
        brightcoveVideoView = (BrightcoveExoPlayerVideoView) view.findViewById(R.id.brightcove_video_view);
        setTimer();
        initializePlayer();
        return view;
    }

    public void initializePlayer(){
        try{
            EventEmitter eventEmitter = brightcoveVideoView.getEventEmitter();
            Catalog catalog = new Catalog(eventEmitter,AccountId,PolicyKey);
            catalog.findVideoByID( VideoId, new VideoListener() {

                // Add the video found to the queue with add().
                // Start playback of the video with start().
                @Override
                public void onVideo(Video video) {
                    brightcoveVideoView.add(video);
                    brightcoveVideoView.start();
                }

                @Override
                public void onError(String s) {
                    //throw new RuntimeException(s);
                }
            });
        }catch (Exception e){
            Log.e("Error",e.toString());
        }

    }

    private void setTimer() {
        Calendar calendarfinish =Calendar.getInstance();
        calendarfinish.add(Calendar.SECOND,-1);
        DetailStartTime = calendarfinish.getTime();
        assetPlayerActivity.InsertEdetailStarttime(DetailStartTime,0,CurrentAssetPosition);
        initialStartTime = 0;
        assetPlayerActivity.InsertPlayerStartTime(initialStartTime, CurrentAssetPosition,playmode);
    }
}
