package com.swaas.kangle.DigitalAssetPlayer.fragments;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swaas.hari.hidoctor.ExoPlayerView.C;
import com.swaas.hari.hidoctor.ExoPlayerView.DefaultLoadControl;
import com.swaas.hari.hidoctor.ExoPlayerView.DefaultRenderersFactory;
import com.swaas.hari.hidoctor.ExoPlayerView.EventLogger;
import com.swaas.hari.hidoctor.ExoPlayerView.ExoPlaybackException;
import com.swaas.hari.hidoctor.ExoPlayerView.ExoPlayer;
import com.swaas.hari.hidoctor.ExoPlayerView.ExoPlayerFactory;
import com.swaas.hari.hidoctor.ExoPlayerView.PlaybackParameters;
import com.swaas.hari.hidoctor.ExoPlayerView.SeekbarChangedListener;
import com.swaas.hari.hidoctor.ExoPlayerView.SimpleExoPlayer;
import com.swaas.hari.hidoctor.ExoPlayerView.SimpleExoPlayerView;
import com.swaas.hari.hidoctor.ExoPlayerView.Timeline;
import com.swaas.hari.hidoctor.ExoPlayerView.extractor.DefaultExtractorsFactory;
import com.swaas.hari.hidoctor.ExoPlayerView.source.ExtractorMediaSource;
import com.swaas.hari.hidoctor.ExoPlayerView.source.MediaSource;
import com.swaas.hari.hidoctor.ExoPlayerView.source.TrackGroupArray;
import com.swaas.hari.hidoctor.ExoPlayerView.trackselection.AdaptiveTrackSelection;
import com.swaas.hari.hidoctor.ExoPlayerView.trackselection.DefaultTrackSelector;
import com.swaas.hari.hidoctor.ExoPlayerView.trackselection.TrackSelection;
import com.swaas.hari.hidoctor.ExoPlayerView.trackselection.TrackSelectionArray;
import com.swaas.hari.hidoctor.ExoPlayerView.upstream.DataSource;
import com.swaas.hari.hidoctor.ExoPlayerView.upstream.DefaultBandwidthMeter;
import com.swaas.kangle.KangleApplication;
import com.swaas.kangle.R;
import com.swaas.kangle.DigitalAssetPlayer.DigitalAssetPlayerActivity;
import com.swaas.kangle.DigitalAssetPlayer.PlayerBrightnessVolumeEventsListener;
import com.swaas.kangle.DigitalAssetPlayer.customviews.SimpleTwoFingerDoubleTapDetector;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.NetworkUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hariharan on 28/6/17.
 */

public class ExoPlayerFragment extends Fragment implements SeekbarChangedListener, ExoPlayer.EventListener,PlayerBrightnessVolumeEventsListener {

    private Context mContext;
    private String mVideoUrl;
    private int CurrentAssetPosition,playmode;
    private DigitalAssetPlayerActivity assetPlayerActivity;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    public Handler mainHandler;
    public CountDownTimer countDownTimer;
    public  AudioManager audioManager;
    public EventLogger eventLogger;
    public int brightness = 20;
    public Handler mTextHandler;
    public int resumeWindow;
    public boolean isPaused = false;
    public boolean isWentBackground = false;
    public int initialStartTime = -1 , LateStartTime = -1;
    public boolean videoStarted = false;
    private CountDownTimer waitTimer;
    private Date DetailStartTime = null;
    public boolean isPreparing = false;
    private CountDownTimer VolumeTimer,brigtnesstimer;
    public TextView mVolumeText,mBrightnessText;
    public RelativeLayout mBrightness_holder,mVolume_holder;
    public GestureDetector gestureDetector;
    public boolean isVisibleOnce = false;
    public boolean isVisible,isEnded = true;
    public long resumePosition;
    public AVLoadingIndicatorView avi;
    private DefaultTrackSelector trackSelector;
    public boolean playWhenReady = true;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter(); ;
    private DataSource.Factory mediaDataSourceFactory;
    private MediaSource mediaSource;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mContext =  getActivity().getApplicationContext();
        assetPlayerActivity = (DigitalAssetPlayerActivity)getActivity();
        brightness = assetPlayerActivity.getBrightnessvalue();
        if (brightness<20){
            brightness = 20;
        }
        if (bundle!=null){

            mVideoUrl = bundle.getString("url");
            CurrentAssetPosition = bundle.getInt("Index");
            playmode =  bundle.getInt("playmode");
        }


    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            isVisible = true;
            setTimer();
            isVisibleOnce = false;
            if (simpleExoPlayerView!=null){
                initializePlayer();
            }
        }else {

            isVisible = false;
            if (waitTimer!=null){
                waitTimer.cancel();
                waitTimer = null;
            }

            if (assetPlayerActivity!=null){

                assetPlayerActivity.hideGestureLayerHolder();
                assetPlayerActivity.HideActionBarControll();

            }

            if (simpleExoPlayerView!=null){

                if (!simpleExoPlayerView.getController().isVisible()) {
                    simpleExoPlayerView.showController();
                } else {
                    simpleExoPlayerView.getController().hide();
                }

            }

            if (initialStartTime != -1){
                Long end_time = player.getCurrentPosition()/1000;
                int end_pos = end_time.intValue();
                assetPlayerActivity.UpdatePlayerEndTime(CurrentAssetPosition,end_pos);
                initialStartTime = -1;

            }

            if (LateStartTime != -1){
                Long end_time = player.getCurrentPosition()/1000;
                int end_pos = end_time.intValue();
                assetPlayerActivity.UpdatePlayerEndTime(CurrentAssetPosition,end_pos);
                LateStartTime = -1;

            }

            if (DetailStartTime !=null) {

                Log.d("===>DetailEndTime", "" + Calendar.getInstance().getTime());
                assetPlayerActivity.UpdateEdetailEndTime(Calendar.getInstance().getTime(), CurrentAssetPosition);
                DetailStartTime = null;

            }

            releasePlayer(isVisibleOnce);

        }


    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_exo_player_viewer,container,false);
        simpleExoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.simple_exoplayer);
        simpleExoPlayerView.setControllerListner(this);
        avi = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        mVolumeText = (TextView) view.findViewById(R.id.volume_text);
        mBrightnessText = (TextView) view.findViewById(R.id.bright_text);
        mVolume_holder = (RelativeLayout)view.findViewById(R.id.volumeholder);
        mBrightness_holder = (RelativeLayout)view.findViewById(R.id.brightnessholder);
        gestureDetector = new GestureDetector(mContext, new GestureTap());
        mediaDataSourceFactory = buildDataSourceFactory(true);
        mainHandler = new Handler();

        mTextHandler = new Handler(){

            public void handleMessage(Message msg) {

                if (brightness <= 0){
                    brightness = 0;
                    mBrightnessText.setText(0+"%");
                }else if (brightness >= 255){
                    brightness = 255;
                    mBrightnessText.setText(100+"%");
                }else {

                    float brithnesspercent = (brightness*100)/255;
                    int brightnesspercentage = Math.round(brithnesspercent);
                    mBrightnessText.setText(brightnesspercentage+"%");
                }

                int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                int volume_percent = (volume * 100)/15;
                if (volume_percent>100){
                    mVolumeText.setText("100"+"%");
                }else {
                    mVolumeText.setText(volume_percent+"%");
                }

            }
        };

        initializePlayer();

        return view;
    }


    public void startAnim(){

        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        avi.hide();
        // or avi.smoothToHide();
    }

    private void setTimer() {

        waitTimer = new CountDownTimer(1000, 300) {
            public void onTick(long millisUntilFinished) {
                //called every 300 milliseconds, which could be used to
                //send messages or some other action
            }
            public void onFinish() {

                Calendar calendarfinish =Calendar.getInstance();
                calendarfinish.add(Calendar.SECOND,-1);
                DetailStartTime = calendarfinish.getTime();
                assetPlayerActivity.digitalAssetsList.get(CurrentAssetPosition).setIncreaseVideoSessionId(true);
                assetPlayerActivity.InsertEdetailStarttime(DetailStartTime,0,CurrentAssetPosition);
                if (initialStartTime != -1){
                    videoStarted =true;
                    assetPlayerActivity.InsertPlayerStartTime(initialStartTime, CurrentAssetPosition,playmode);

                }

            }
        }.start();
    }


    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((KangleApplication) assetPlayerActivity.getApplication())
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    private void initializePlayer() {

        startAnim();
        audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

        trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        eventLogger = new EventLogger(trackSelector);

        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(mContext),trackSelector
                , new DefaultLoadControl());

        simpleExoPlayerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        Uri uri = Uri.parse(mVideoUrl);
        player.addListener(this);
        player.addListener(eventLogger);
        player.setAudioDebugListener(eventLogger);
        player.setVideoDebugListener(eventLogger);
        player.setMetadataOutput(eventLogger);
        mediaSource = buildMediaSource(uri);

        if (isVisible){
            isPreparing = true;
            player.prepare(mediaSource,true,false);
        }


        simpleExoPlayerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                gestureDetector.onTouchEvent(event);
                multiTouchListener.onTouchEvent(event);
                return true;
            }
        });

        simpleExoPlayerView.getController().hide();


    }

    SimpleTwoFingerDoubleTapDetector multiTouchListener = new SimpleTwoFingerDoubleTapDetector() {
        @Override
        public void onTwoFingerDoubleTap() {
            // Do what you want here, I used a Toast for demonstration

            if (PreferenceUtils.getIsGestureEnabled(mContext)){
                assetPlayerActivity.ShowGestureLayerHolder(CurrentAssetPosition);
            }

        }
    };



    private MediaSource buildMediaSource(Uri uri) {


        return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
                mainHandler, eventLogger);
    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
    }



    @Override
    public void onResume() {
        super.onResume();

       /* if (assetPlayerActivity.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){

            SetLanscapeIcons();

        }else {


            SetPotraitIcons();

        }
        */

        if (isVisible){

            if (isWentBackground){

                startAnim();
                player.seekTo(resumeWindow,resumePosition);
                player.prepare(mediaSource, false, false);
                assetPlayerActivity.InsertEdetailStarttime(DetailStartTime,assetPlayerActivity.getCurrentSessionId(CurrentAssetPosition),CurrentAssetPosition);
                Log.d("<=>frombackgrond","true");
                isPreparing = true;
                videoStarted = false;
                isWentBackground = false;
                clearResumePosition();
            }
        }

    }



    @Override
    public void onPause() {
        super.onPause();

        updateResumePosition();
        InterpetEndTime();

    }


    private void InterpetEndTime() {

        if(isVisible){
            Long inter_pos = player.getCurrentPosition();
            int intermedia_endposition  = inter_pos.intValue();
            assetPlayerActivity.setCurrentPositon(intermedia_endposition);
            Log.d("<=>pause","test");

        }


    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (isVisible){

            isWentBackground  = true;
            Log.d("<=>stop","test");
            player.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (isVisible){

            isWentBackground  = false;
            Log.d("<=>destroy","test");

        }

        releasePlayer(isVisibleOnce);

    }

    private void releasePlayer(boolean isVisibleOnce) {
        if (player != null && !isVisibleOnce) {
            playWhenReady = player.getPlayWhenReady();
            updateResumePosition();
            player.release();
        }
    }


    private void updateResumePosition() {
        resumeWindow = player.getCurrentWindowIndex();
        resumePosition = player.isCurrentWindowSeekable() ? Math.max(0, player.getCurrentPosition())
                : C.TIME_UNSET;
    }

    private void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onStartSeek(long position) {

        if (isVisible){

            if (initialStartTime != -1 || LateStartTime != -1){
                Long curpos =  position;
                int endtime = curpos.intValue();
                assetPlayerActivity.MultiUpdateEndTime(endtime,CurrentAssetPosition);
            }
        }

    }

    @Override
    public void onStopSeek(long position) {

        if (isVisible){

            Long curpos =  position;
            int starttime = curpos.intValue();
            assetPlayerActivity.MultiUpdateStartTime(starttime,CurrentAssetPosition,playmode);
            Log.d("seekstop",position+"");


        }

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }



    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if (playbackState == ExoPlayer.STATE_BUFFERING){
            startAnim();
            Log.d("playerposition_buffer",player.getCurrentPosition()+"");

        }else if (playbackState == ExoPlayer.STATE_IDLE){
            startAnim();

        } else if (playbackState == ExoPlayer.STATE_READY){

            stopAnim();
            if (player.getCurrentPosition()/1000 < 2){
                assetPlayerActivity.UpdateVideoStartedState();
            }
            if (isPreparing){

                if (!videoStarted){
                    Long  currentposition  = player.getCurrentPosition()/1000;
                    LateStartTime          = currentposition.intValue();
                    assetPlayerActivity.InsertPlayerStartTime(LateStartTime, CurrentAssetPosition,playmode);
                    videoStarted =true;


                }else {

                    Long  currentposition  = player.getCurrentPosition()/1000;
                    initialStartTime = currentposition.intValue();
                    Log.d("playerposition_current",initialStartTime+"");
                    isPreparing = false;


                }


            }else {


            }

        }else if (playbackState == ExoPlayer.STATE_ENDED){

            if (isEnded){

                Long endposition = player.getCurrentPosition()/1000;
                int endpos =  endposition.intValue();
                assetPlayerActivity.endtimeUpdateOnComplete(endpos,CurrentAssetPosition);
                Log.d("playerposition_end",endpos+"");
                isEnded = false;
                assetPlayerActivity.showActionBar();
            }

        }

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {


        if (simpleExoPlayerView != null){
            if (simpleExoPlayerView.getController() != null){
                simpleExoPlayerView.getController().hide();
            }
        }

        if (initialStartTime != -1){

            Long endposition = player.getCurrentPosition()/1000;
            int endpos =  endposition.intValue();
            assetPlayerActivity.endtimeUpdateOnComplete(endpos,CurrentAssetPosition);
            Log.d("playerposition_end",endpos+"");

            if (NetworkUtils.isNetworkAvailable()){

                assetPlayerActivity.showErrorDialogue("Some thing went wrong please make sure your asset is not corrupted.");


            }else {

                assetPlayerActivity.showAlerDialogue();
                //assetPlayerActivity.showErrorDialogue(getString(R.string.no_network));

            }


        }else if (LateStartTime != -1){

            Long endposition = player.getCurrentPosition()/1000;
            int endpos =  endposition.intValue();
            assetPlayerActivity.endtimeUpdateOnComplete(endpos,CurrentAssetPosition);
            Log.d("playerposition_end",endpos+"");
            if (NetworkUtils.isNetworkAvailable()){

                assetPlayerActivity.showAlerDialogue();

//                 assetPlayerActivity.showErrorDialogue("Some thing went wrong please make sure your asset is not corrupted.");


            }else {

                assetPlayerActivity.showAlerDialogue();

                //               assetPlayerActivity.showErrorDialogue(getString(R.string.no_network));

            }


        }else {

            if (isVisible){

                if (waitTimer!=null){
                    waitTimer.cancel();
                }

                assetPlayerActivity.showAlerDialogue();

//                 assetPlayerActivity.showErrorDialogue(getString(R.string.no_network));

            }

        }


    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onVolumeUpScroll(MotionEvent event, float delta) {

        if (VolumeTimer!=null){
            VolumeTimer.cancel();
        }

        mBrightness_holder.setVisibility(View.GONE);
        mVolume_holder.setVisibility(View.VISIBLE);
        mTextHandler.obtainMessage(1).sendToTarget();
        int current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current_volume+1
                , 0);


        VolumeTimer = new CountDownTimer(2000,300) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                mVolume_holder.setVisibility(View.GONE);

            }
        }.start();

    }

    @Override
    public void onVolumeDownScroll(MotionEvent event, float delta) {

        if (VolumeTimer!=null){
            VolumeTimer.cancel();
        }
        mBrightness_holder.setVisibility(View.GONE);
        mVolume_holder.setVisibility(View.VISIBLE);
        mTextHandler.obtainMessage(1).sendToTarget();
        mVolume_holder.setVisibility(View.VISIBLE);
        int current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current_volume-1
                , 0);

        VolumeTimer = new CountDownTimer(2000,300) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                mVolume_holder.setVisibility(View.GONE);

            }
        }.start();


    }

    @Override
    public void onBrightnessUpScroll(MotionEvent event, float delta) {


        if (brigtnesstimer !=null){
            brigtnesstimer.cancel();
        }
        mVolume_holder.setVisibility(View.GONE);
        mBrightness_holder.setVisibility(View.VISIBLE);
        mTextHandler.obtainMessage(1).sendToTarget();
        brightness = brightness + 20;
        if (brightness > 2555){
            assetPlayerActivity.UpdateBrightness(255);
        }else {
            assetPlayerActivity.UpdateBrightness(brightness);
        }

        Log.d("=>test","brightnessup");

        brigtnesstimer = new CountDownTimer(2000,300) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                mBrightness_holder.setVisibility(View.GONE);

            }
        }.start();



    }

    @Override
    public void onBrightnessDownScroll(MotionEvent event, float delta) {


        if (brigtnesstimer !=null){
            brigtnesstimer.cancel();
        }
        mVolume_holder.setVisibility(View.GONE);
        mBrightness_holder.setVisibility(View.VISIBLE);
        mTextHandler.obtainMessage(1).sendToTarget();
        Log.d("=>test","brightnessdown");
        brightness = brightness - 20;
        if (brightness<20){
            assetPlayerActivity.UpdateBrightness(20);
        }else {
            assetPlayerActivity.UpdateBrightness(brightness);
        }

        brigtnesstimer = new CountDownTimer(2000,300) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                mBrightness_holder.setVisibility(View.GONE);

            }
        }.start();


    }


    class GestureTap extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            return true;
        }


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            float deltaY = e2.getY() - e1.getY();
            float deltaX = e2.getX() - e1.getX();

            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (Math.abs(deltaX) > SWIPE_THRESHOLD) {
                    if (deltaX > 0) {
                        Log.i("=>test", "Slide right");
                    } else {
                        Log.i("=>test", "Slide left");
                    }
                }
            } else {

                if (Math.abs(deltaY) > SWIPE_THRESHOLD) {
                    if (deltaY > 0 ) {
                        if (e1.getX() > simpleExoPlayerView.getWidth()/2 ){

                            onVolumeDownScroll(e2,deltaY);


                        }else if (e1.getX() < simpleExoPlayerView.getWidth()/2 ){

                            onBrightnessDownScroll(e2,deltaY);

                        }
                    } else {

                        if (e1.getX() > simpleExoPlayerView.getWidth()/2 ){

                            onVolumeUpScroll(e2,deltaY);


                        }else if (e1.getX() < simpleExoPlayerView.getWidth()/2) {

                            onBrightnessUpScroll(e2,deltaY);

                        }

                    }
                }
            }

            return super.onScroll(e1, e2, distanceX, distanceY);


        }



        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            if (countDownTimer!=null){
                countDownTimer.cancel();
            }

            if (simpleExoPlayerView.getController()!=null){

                if (!simpleExoPlayerView.getController().isVisible()) {
                    simpleExoPlayerView.showController();
                } else {
                    simpleExoPlayerView.getController().hide();
                }

                if (assetPlayerActivity.getActionBarVisibility() == View.VISIBLE){

                    assetPlayerActivity.hideActionBar();
                    if (countDownTimer != null){
                        countDownTimer.cancel();
                    }

                }else {
                    assetPlayerActivity.showActionBar();
                }

                StartControllerTimer();

            }



            return true;
        }




    }

    private void StartControllerTimer() {

        countDownTimer =     new CountDownTimer(5000,300) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                assetPlayerActivity.HideActionBarControll();

            }
        }.start();


    }


    @Override
    public void onNextClicked() {

        assetPlayerActivity.AssetPlayBackPositionChange(CurrentAssetPosition+1, false);

    }


    @Override
    public void onPreviousClicked() {

        assetPlayerActivity.AssetPlayBackPositionChange(CurrentAssetPosition-1, false);


    }

    @Override
    public void onNextTenClicked() {

        long position = player.getCurrentPosition();
        Long curpos =  position;
        int endtime = curpos.intValue();
        endtime = endtime/1000;
        Log.d("playerpos_nextten",position+"");
        if (initialStartTime != -1 || LateStartTime != -1){
            assetPlayerActivity.MultiUpdateEndTime(endtime,CurrentAssetPosition);
            assetPlayerActivity.digitalAssetsList.get(CurrentAssetPosition).setIncreaseVideoSessionId(false);
            int NextStartTime = endtime+10;
            if (NextStartTime > player.getDuration()){

            }else {

                assetPlayerActivity.MultiUpdateStartTime(NextStartTime,CurrentAssetPosition,playmode);
                assetPlayerActivity.digitalAssetsList.get(CurrentAssetPosition).setIncreaseVideoSessionId(false);
            }
            Log.d("seekstop",position+"");

        }


    }


    @Override
    public void onPreviousTenClicked() {
        long position = player.getCurrentPosition();
        Long curpos =  position;
        int endtime = curpos.intValue();
        endtime = endtime/1000;
        if (initialStartTime != -1 || LateStartTime != -1){
            assetPlayerActivity.MultiUpdateEndTime(endtime,CurrentAssetPosition);
            int prev_starttime = endtime -10;
            if (prev_starttime< 0){
                assetPlayerActivity.MultiUpdateStartTime(0,CurrentAssetPosition,playmode);
                assetPlayerActivity.digitalAssetsList.get(CurrentAssetPosition).setIncreaseVideoSessionId(false);
            }else {
                assetPlayerActivity.MultiUpdateStartTime(prev_starttime,CurrentAssetPosition,playmode);
                assetPlayerActivity.digitalAssetsList.get(CurrentAssetPosition).setIncreaseVideoSessionId(false);
            }
            Log.d("seekstop",position+"");

        }


        Log.d("playerpos_prevten",player.getCurrentPosition()+"");


    }

    @Override
    public void onPlayClicked() {

        isPaused = false;
        Log.d("<=>test","clickedplay");
        StartControllerTimer();

    }

    @Override
    public void onPauseClicked() {

        Log.d("<=>test","clickedpause");

        isPaused = true;
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }

    }


}
