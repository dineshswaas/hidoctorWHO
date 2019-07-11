package com.swaas.kangle;

import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.swaas.hari.hidoctor.ExoPlayerView.upstream.DataSource;
import com.swaas.hari.hidoctor.ExoPlayerView.upstream.DefaultBandwidthMeter;
import com.swaas.hari.hidoctor.ExoPlayerView.upstream.DefaultDataSourceFactory;
import com.swaas.hari.hidoctor.ExoPlayerView.upstream.DefaultHttpDataSourceFactory;
import com.swaas.hari.hidoctor.ExoPlayerView.upstream.HttpDataSource;
import com.swaas.hari.hidoctor.ExoPlayerView.util.Util;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Sayellessh on 08-05-2017.
 */

public class KangleApplication extends MultiDexApplication {

    private static KangleApplication sThis;
    protected String userAgent;


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Stetho.initializeWithDefaults(this);
        sThis = this;
        userAgent = Util.getUserAgent(this, "KangleApplication");

    }


    public static KangleApplication getThis() {
        return sThis;
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }



}
