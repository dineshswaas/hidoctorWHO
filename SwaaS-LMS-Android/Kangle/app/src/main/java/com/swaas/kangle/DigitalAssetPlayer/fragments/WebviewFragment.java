package com.swaas.kangle.DigitalAssetPlayer.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.swaas.kangle.R;
import com.swaas.kangle.DigitalAssetPlayer.DigitalAssetPlayerActivity;
import com.swaas.kangle.DigitalAssetPlayer.customviews.image.OnSingleTapImage;
import com.swaas.kangle.DigitalAssetPlayer.customviews.webview.MyCustomWebview;
import com.swaas.kangle.preferences.PreferenceUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hariharan on 29/5/17.
 */

public class WebviewFragment extends Fragment implements OnSingleTapImage {


    public MyCustomWebview mWebview;
    public String webviewurl;
    public CountDownTimer waitTimer;
    public int CurrentAssetPosition;
    WebSettings settings;
    public String previousurl,CurrentUrl;
    public Date StartDate,PlayerStart;
    public boolean preLoadedUrl = false;
    public Context mContext;
    public boolean isVissible = false;
    public boolean StartdateCaptured = false;
    public int PartID = 1;
    public DigitalAssetPlayerActivity assetplayeractivity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext =  getContext();
        Bundle bundle = getArguments();
        webviewurl = bundle.getString("url");
        CurrentAssetPosition = bundle.getInt("Index");
        assetplayeractivity = (DigitalAssetPlayerActivity) getActivity();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){

            PlayerStart = Calendar.getInstance().getTime();
            isVissible  = true;


            if (assetplayeractivity!=null){

                assetplayeractivity.UpdateWebviewGuideCompleted();
                assetplayeractivity.showActionBar();
            }

            if (preLoadedUrl){
                assetplayeractivity.InsertHtmlDetailStarttime(CurrentAssetPosition,Calendar.getInstance().getTime());
                StartDate = Calendar.getInstance().getTime();
                assetplayeractivity.InsertPartUrlStartTime(CurrentAssetPosition,StartDate,previousurl);
                StartdateCaptured  = true;
            }else {

                if (assetplayeractivity!= null){
                    assetplayeractivity.digitalAssetsList.get(CurrentAssetPosition).setIncreaseHTMLSessionId(true);
                    assetplayeractivity.InsertHtmlDetailStarttime(CurrentAssetPosition,PlayerStart);
                    StartdateCaptured = true;
                }

            }

        }else {

            isVissible = false;

            if (assetplayeractivity != null){

                assetplayeractivity.hideActionBar();

            }

            if (waitTimer!=null){
                waitTimer.cancel();
            }

            if (StartDate != null){
                assetplayeractivity.InsertUrlEndTime(CurrentAssetPosition,Calendar.getInstance().getTime());
            }

            if (PlayerStart!=null){

                assetplayeractivity.InsertHtmlDetailEndtime(CurrentAssetPosition,Calendar.getInstance().getTime());

                if (CurrentAssetPosition ==0){
                    preLoadedUrl = true;
                }
            }


        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if(isVissible){

            PlayerStart = Calendar.getInstance().getTime();
            assetplayeractivity.InsertHtmlDetailStarttime(CurrentAssetPosition,PlayerStart);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_asset_webview_fragment, container, false);
        mWebview = (MyCustomWebview) view.findViewById(R.id.webview);
        mWebview.setHorizontalScrollBarEnabled(true);
        mWebview.setSingleTapOnWebviewListener(this);
        mWebview.setVerticalScrollBarEnabled(true);
        mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        settings = mWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setSaveFormData(true);
        settings.setAllowContentAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowContentAccess(true);

        if (assetplayeractivity!=null){
            assetplayeractivity.showActionBar();
        }
        WebViewClient webViewClient = new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (isVissible){
                    if (assetplayeractivity!=null){
                        assetplayeractivity.showActionBar();
                        assetplayeractivity.UpdateWebviewGuideCompleted();
                    }
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //Log.d("==>endurl", "" + url);
                DodbOperation(url);
            }
        };

        mWebview.setWebViewClient(webViewClient);
        mWebview.addJavascriptInterface(new WebAppInterface(getContext()), "Edetailing");
        LoadWebView();

        return view;

    }

    private void DodbOperation(String url) {
        assetplayeractivity.digitalAssetsList.get(CurrentAssetPosition).setIncreaseHTMLSessionId(false);
        if (previousurl == null) {
            previousurl = url;

            if (isVissible){

                StartDate = Calendar.getInstance().getTime();
                if (!StartdateCaptured){

                    assetplayeractivity.InsertHtmlDetailStarttime(CurrentAssetPosition,StartDate);

                }
                assetplayeractivity.InsertPartUrlStartTime(CurrentAssetPosition,StartDate,previousurl);

            }else {

                preLoadedUrl = true;

            }


        } else if (!previousurl.equals(url)) {

            CurrentUrl = url;

            if (isVissible){
                assetplayeractivity.InsertParturlEndTime(CurrentAssetPosition,Calendar.getInstance().getTime());
                StartDate = Calendar.getInstance().getTime();
                assetplayeractivity.InsertPartUrlStartTime(CurrentAssetPosition,StartDate,CurrentUrl);

            }

            previousurl = url;

        }

    }

    private void LoadWebView() {

        if (webviewurl!=null)
           mWebview.loadUrl(webviewurl);// mWebview.loadUrl("file:///"+webviewurl);
        else
            Log.d("url","null");




    }

    @Override
    public void OnSingleTap() {

    }

    @Override
    public void OnTwoFingerDoubleTap() {
        if (PreferenceUtils.getIsGestureEnabled(mContext)){
            assetplayeractivity.ShowGestureLayerHolder(CurrentAssetPosition);
        }
    }


    public class WebAppInterface {

        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void HD_track(String assetid){

            assetplayeractivity.InsertParturlEndTime(CurrentAssetPosition,Calendar.getInstance().getTime());
            StartDate = Calendar.getInstance().getTime();
            assetplayeractivity.InsertPartUrlStartTimeEvent(CurrentAssetPosition,StartDate,assetid);
            Log.d("==>test",""+assetid);


        }
    }
}


