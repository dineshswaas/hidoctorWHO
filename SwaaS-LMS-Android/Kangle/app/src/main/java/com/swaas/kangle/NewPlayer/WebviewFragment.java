package com.swaas.kangle.NewPlayer;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.swaas.kangle.R;
import com.swaas.kangle.models.DigitalAssetTransactionMaster;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.models.LstAssetImageModel;

import java.util.Date;

/**
 * Created by Sayellessh on 15-05-2017.
 */

public class WebviewFragment extends Fragment {

    private View mView;
    LstAssetImageModel ppt;
    DigitalAssetsMaster digitalAssetsMaster;
    WebView webView;
    private ViewpagerPlayerActivity viewpagerPlayerActivity;
    long starttime;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            mView = inflater.inflate(R.layout.player_webview_list_item, container, false);
            webView = (WebView) mView.findViewById(R.id.webview);
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
        loadWebView();
        starttime = new Date().getTime();
    }

    public void loadWebView() {
        try {
            WebSettings settings = webView.getSettings();
            settings.setDomStorageEnabled(true);

            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.setWebViewClient(new WebViewClient(){

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
                @Override
                public void onPageFinished(WebView view, final String url) {
                }
            });
            webView.loadUrl(digitalAssetsMaster.getOnlineURL());
        }catch(Exception e){
            Log.e("errorstring",e.toString());
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
        digitalAssetTransactionMaster.setIs_Downloaded(digitalAssetsMaster.is_Downloaded());
        digitalAssetTransactionMaster.setIs_Read(true);
        return digitalAssetTransactionMaster;
    }
}
