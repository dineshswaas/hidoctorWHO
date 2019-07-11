package com.swaas.kangle.LPCourse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.swaas.kangle.R;

public class Certificatewebview extends AppCompatActivity {
    ImageView companylogo;
    WebView webView;
    ProgressDialog mProgressDialog;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.certificate_webview);
        companylogo = (ImageView) findViewById(R.id.companylogo);
        webView = (WebView) findViewById(R.id.webView_certificate);
        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initWebView();
    }
    private void initWebView() {
        showProgressDialog();
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                dismissProgressDialog();
            }
        });
        webView.loadUrl("https://login.kangle.me/LPCourse/CourseCertificate/?cid=118&cuaid=85769&courseId=686&al=1&cl=We&layout=1&coId=118&uId=9121&rCode=REC00000002&sdn=shakeys.elearning.kangle.me&lang=en-US");
    }

    public void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

}
