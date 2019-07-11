package com.swaas.kangle;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.MessageDialog;
import com.swaas.kangle.utils.NetworkUtils;

import java.io.File;
import java.util.Locale;

public class CourseWebView extends AppCompatActivity implements LocationListener {

    WebView webView;
    Context mContext;
    ImageView companylogo,notification;
    TextView closehelp;
    View closelayout;
    MessageDialog messageDialog;
    public double latitude,longitude;
    UploadActivity uploadActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_web_view);
        mContext = CourseWebView.this;
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //getSupportActionBar().hide();
        uploadActivity = new UploadActivity(mContext);
        initialiseViews();
        Ion.with(companylogo).placeholder(R.color.black).error(R.color.black).load(
                (!TextUtils.isEmpty(PreferenceUtils.getClientLogo(mContext)))? PreferenceUtils.getClientLogo(mContext) : PreferenceUtils.getClientLogo(mContext));
        loadUrl();
        File imgFile = new File("/storage/sdcard0/SwaaS LMS/companylogo.jpg");

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            companylogo.setImageBitmap(myBitmap);

        }
        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(NetworkUtils.checkIfNetworkAvailable(mContext)){
                 if(!getResources().getBoolean(R.bool.portrait_only)){
                     loadPopUpHelpView();
                 }else {
                     loadHelpView();
                 }
            }else{
                Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
            }
            }
        });

        closehelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closelayout.setVisibility(View.GONE);
                loadUrl();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadActivity.insertUserTracking("LP",latitude,longitude);
    }

    public void initialiseViews(){
        webView = (WebView) findViewById(R.id.webView);
        companylogo = (ImageView) findViewById(R.id.companylogo);
        notification= (ImageView) findViewById(R.id.notification);
        closehelp = (TextView) findViewById(R.id.closehelp);
        closelayout = findViewById(R.id.closelayout);
        messageDialog = new MessageDialog(mContext);
    }

    public void loadUrl(){
        String lanval = "";
        String language = Locale.getDefault().getDisplayLanguage();
        if(language.equalsIgnoreCase("English")){
            lanval = "en-US";
        } else if(language.equalsIgnoreCase("Español")){
            lanval = "es-ES";
        }
        Gson gsonget = new Gson();
        User userobj = gsonget.fromJson(PreferenceUtils.getUser(CourseWebView.this), User.class);
        String URL = Constants.COMPANY_BASE_URL+"/LPCourse/UserLPCourseDetails/?al=1&cl=Mo&layout=1&coId="+userobj.getCompany_Id()+"&uId="
                +userobj.getUserID()+"&rCode="+userobj.getRegion_Code()+"&sdn="+PreferenceUtils.getSubdomainName(CourseWebView.this)+"&lang="+lanval;
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
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
        webView.loadUrl(URL);
    }

    public void loadHelpView(){
        closelayout.setVisibility(View.VISIBLE);
        String lan = "";
        String language = Locale.getDefault().getDisplayLanguage();
        if(language.equalsIgnoreCase("English")){
            lan = "en-";
        } else if(language.equalsIgnoreCase("Español")){
            lan = "es-";
        }
        String URL = Constants.COMPANY_BASE_URL+"/HelpFiles/"+lan+"Kanglehelpcourse.htm";
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        String ua = webView.getSettings().getUserAgentString();
        PreferenceUtils.setUserAgent(mContext,ua);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
            }

            /*@Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // Ignore SSL certificate errors
            }*/
        });
        webView.loadUrl(URL);
    }

    public void loadPopUpHelpView(){
        String lan = "";
        String language = Locale.getDefault().getDisplayLanguage();
        if(language.equalsIgnoreCase("English")){
            lan = "en-";
        } else if(language.equalsIgnoreCase("Español")){
            lan = "es-";
        }
        String URL = Constants.COMPANY_BASE_URL+"/HelpFiles/"+lan+"Kanglehelpcourse.htm";
        messageDialog.Showhelppopup(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View Approve) {
                messageDialog.dialogDismiss();
            }
        },URL, false);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
}
