package com.swaas.hari.hidoctor.ExoPlayerView;

/**
 * Created by Hariharan on 27/6/17.
 */

public interface SeekbarChangedListener {

    public void onStartSeek(long position);
    public void onStopSeek(long position);
    public void onNextClicked();
    public void  onPreviousClicked();
    public void onNextTenClicked();
    public void  onPreviousTenClicked();
    public void onPlayClicked();
    public void onPauseClicked();




}
