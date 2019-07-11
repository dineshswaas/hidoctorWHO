package com.swaas.kangle.DigitalAssetPlayer;

import android.view.MotionEvent;

/**
 * Created by Hariharan on 12/6/17.
 */

public interface PlayerBrightnessVolumeEventsListener {

    void onVolumeUpScroll(MotionEvent event, float delta);

    void onVolumeDownScroll(MotionEvent event, float delta);

    void onBrightnessUpScroll(MotionEvent event, float delta);

    void onBrightnessDownScroll(MotionEvent event, float delta);

}
