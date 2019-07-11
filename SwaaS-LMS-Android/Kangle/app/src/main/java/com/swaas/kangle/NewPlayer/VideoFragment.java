package com.swaas.kangle.NewPlayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.swaas.kangle.R;
import com.swaas.kangle.models.DigitalAssetTransactionMaster;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.models.LstAssetImageModel;

import java.util.Date;

/**
 * Created by Sayellessh on 15-05-2017.
 */

public class VideoFragment extends Fragment {

    private View mView;
    LstAssetImageModel ppt;
    DigitalAssetsMaster digitalAssetsMaster;
    VideoView videoView;
    private ViewpagerPlayerActivity viewpagerPlayerActivity;
    long starttime;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            mView = inflater.inflate(R.layout.player_video_list_item, container, false);
            videoView = (VideoView) mView.findViewById(R.id.videoview);
            if (getArguments() != null && getArguments().getSerializable("eventDocument") != null) {
                ppt = (LstAssetImageModel) getArguments().getSerializable("eventDocument");
            }else if(getArguments() != null && getArguments().getSerializable("assetimage") != null){
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
        loadVideo();
        starttime = new Date().getTime();
    }

    public void loadVideo() {
        try {

            MediaController mediaController = new MediaController(getContext());
            mediaController.setAnchorView(videoView);
            //String url = "";
            //specifying the location of media file
            /*if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
                 url = digitalAssetsMaster.getOnlineURL().toString();
            } else {
                 url = digitalAssetsMaster.getOfflineURL().toString();
            }*/
            //starting the videoView

            videoView.setVisibility(View.VISIBLE);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(Uri.parse(digitalAssetsMaster.getOnlineURL().toString()));
            videoView.requestFocus();
            videoView.start();

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    //onBackPressed();
                                }
                            },
                            3000);
                }
            });
        }catch (Exception e){
            Log.e("Errorinitvideo",e.toString());
        }
    }

    public DigitalAssetTransactionMaster getAnalytics(){
        DigitalAssetTransactionMaster digitalAssetTransactionMaster = new DigitalAssetTransactionMaster();
        digitalAssetTransactionMaster.setPlaytime(new Date().getTime() - starttime);
        digitalAssetTransactionMaster.setRecorddate(new Date().toString());
        digitalAssetTransactionMaster.setDAID(digitalAssetsMaster.getDAID());
        if (digitalAssetsMaster.getOnlineURL().contains(Environment.getExternalStorageDirectory().toString())){
            digitalAssetTransactionMaster.setOnlinePlay("0");
            digitalAssetTransactionMaster.setOfflinePlay("1");
        }else{
            digitalAssetTransactionMaster.setOnlinePlay("1");
            digitalAssetTransactionMaster.setOfflinePlay("0");
        }
        digitalAssetTransactionMaster.setTotalViews(digitalAssetsMaster.getTotalViews()+1);
        digitalAssetTransactionMaster.setIs_Read(true);
        digitalAssetTransactionMaster.setIs_Downloaded(digitalAssetsMaster.is_Downloaded());
       return digitalAssetTransactionMaster;
    }
}
