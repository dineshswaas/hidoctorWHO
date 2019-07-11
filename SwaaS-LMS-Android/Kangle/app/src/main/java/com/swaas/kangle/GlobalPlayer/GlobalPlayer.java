package com.swaas.kangle.GlobalPlayer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.API.service.AssetService;
import com.swaas.kangle.AssetDetailActivity;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.R;
import com.swaas.kangle.adapter.AssetChildRecyclerAdapter;
import com.swaas.kangle.adapter.AssetRecyclerAdapter;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.db.DigitalAssetTransactionRepository;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.models.AssetId;
import com.swaas.kangle.models.AssetImages;
import com.swaas.kangle.models.DigitalAssetTransactionMaster;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.models.LstAssetImageModel;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.NetworkUtils;
import com.swaas.kangle.utils.PinchZoomImageVIew;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

/**
 * Created by Sayellessh on 29-04-2017.
 */

public class GlobalPlayer extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener{

    GestureLibrary gLibrary;
    GestureOverlayView mView;
    ImageButton prev,next;
    ImageButton pptnext,pptprev;
    TextView showhidetray;
    RelativeLayout mainview;
    View view;
    VideoView mVideo;
    PinchZoomImageVIew imageview;
    //ImageView imageview;
    WebView webview;
    String filetype = null;
    int i;
    Context mContext;
    int position,position1;
    String daid;
    List<DigitalAssetTransactionMaster> mList;
    LstAssetImageModel lstAssetImageModel;
    DigitalAssetsMaster digitalAssetsMaster;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;
    DigitalAssetTransactionMaster digitalAssetTransactionMaster;
    DigitalAssetTransactionRepository digitalAssetTransactionRepository;
    long starttime;
    DigitalAssetsMaster previousAsset;
    boolean liked = false;

    ArrayList<AssetImages> assetImages;

    EmptyRecyclerView TrayLayout,childTrayLayout;
    LinearLayoutManager mLayoutManager1,mLayoutManager2;
    AssetRecyclerAdapter assetRecyclerAdapter;
    AssetChildRecyclerAdapter assetChildRecyclerAdapter;
    RelativeLayout tray_layout;
    ProgressDialog pDialog;
    Retrofit retrofitAPI;
    AssetService assetService;
    Gson gsonget;

    RelativeLayout overlayGesture;
    LinearLayout fullbottomtray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_player);

        mContext = GlobalPlayer.this;

        initialiseViews();
        getSupportActionBar().hide();

        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(mContext);
        digitalAssetTransactionRepository = new DigitalAssetTransactionRepository(mContext);
        digitalAssetTransactionMaster = new DigitalAssetTransactionMaster();
        mList = new ArrayList<DigitalAssetTransactionMaster>();
        retrofitAPI = RetrofitAPIBuilder.getInstance();
        assetService = retrofitAPI.create(AssetService.class);
        gsonget = new Gson();
        if(getIntent()!= null) {
            daid = getIntent().getStringExtra("DAID");
            position = getIntent().getIntExtra("position",0);
        }
        setUpRecyclerView();
        bindOnCickEvents();
        getAssetDetail(daid);
        loadAssets();
    }

    public void setUpRecyclerView(){
        mLayoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        mLayoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        TrayLayout.setLayoutManager(mLayoutManager1);
        TrayLayout.scrollToPosition(position);
        childTrayLayout.setLayoutManager(mLayoutManager2);
    }

    public void bindOnCickEvents(){
        showhidetray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullbottomtray.isShown()){
                    showhidetray.setText("Hide");
                    fullbottomtray.setVisibility(View.GONE);
                    overlayGesture.setVisibility(View.GONE);

                }else{
                    showhidetray.setText("Hide");
                    fullbottomtray.setVisibility(View.VISIBLE);
                    overlayGesture.setVisibility(View.VISIBLE);
                }
            }
        });

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullbottomtray.isShown()){
                    showhidetray.setText("Hide");
                    fullbottomtray.setVisibility(View.GONE);
                    overlayGesture.setVisibility(View.GONE);

                }else{
                    showhidetray.setText("Hide");
                    fullbottomtray.setVisibility(View.VISIBLE);
                    overlayGesture.setVisibility(View.VISIBLE);
                }
            }
        });

        webview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullbottomtray.isShown()){
                    showhidetray.setText("Hide");
                    fullbottomtray.setVisibility(View.GONE);
                    overlayGesture.setVisibility(View.GONE);

                }else{
                    showhidetray.setText("Hide");
                    fullbottomtray.setVisibility(View.VISIBLE);
                    overlayGesture.setVisibility(View.VISIBLE);
                }
            }
        });

        mVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullbottomtray.isShown()){
                    showhidetray.setText("Hide");
                    fullbottomtray.setVisibility(View.GONE);
                    overlayGesture.setVisibility(View.GONE);

                }else{
                    showhidetray.setText("Hide");
                    fullbottomtray.setVisibility(View.VISIBLE);
                    overlayGesture.setVisibility(View.VISIBLE);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position+1 != assetRecyclerAdapter.getItemCount() ){
                    digitalAssetsMaster = assetRecyclerAdapter.getItemAt(position);
                    //digitalAssetsMaster = digitalAssetHeaderRepository.getNextorPreviousAssetDetails(digitalAssetsMaster.getSlno()+1);
                    if(digitalAssetsMaster != null) {
                        position++;
                        if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("video")) {
                            initvideo(digitalAssetsMaster);
                        } else if(digitalAssetsMaster.getDA_Type().equalsIgnoreCase("image")){
                            initImage(digitalAssetsMaster);
                        } else if(digitalAssetsMaster.getDA_Type().equalsIgnoreCase("document")){
                            if(digitalAssetsMaster.getOnlineURL().endsWith(".pptx")
                                    ||digitalAssetsMaster.getOnlineURL().endsWith(".ppt")
                                    || digitalAssetsMaster.getDAName().endsWith(".pdf")){
                                getPPTAssetImages(digitalAssetsMaster);
                            }
                        }else if(digitalAssetsMaster.getDA_Type().equalsIgnoreCase("articulate")
                                || digitalAssetsMaster.getDA_Type().equalsIgnoreCase("html")){
                            //if(digitalAssetsMaster.getOnlineURL().endsWith(".ppt")){
                                initWebView(digitalAssetsMaster);
                            //}
                        }
                    }else{

                    }
                }

            }
        });


        pptnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position1+1 != assetChildRecyclerAdapter.getItemCount() ){
                    lstAssetImageModel = new LstAssetImageModel();
                    lstAssetImageModel = assetChildRecyclerAdapter.getItemAt(position1++);
                    //digitalAssetsMaster = digitalAssetHeaderRepository.getNextorPreviousAssetDetails(digitalAssetsMaster.getSlno()+1);
                    if(lstAssetImageModel != null) {
                        initpptImage(lstAssetImageModel);
                    }else{

                    }
                }else{
                    pptnext.setVisibility(View.GONE);
                    pptprev.setVisibility(View.GONE);
                    next.setVisibility(View.VISIBLE);
                    prev.setVisibility(View.VISIBLE);
                    childTrayLayout.setVisibility(View.GONE);
                    TrayLayout.setVisibility(View.VISIBLE);
                    next.performClick();
                }
            }
        });

        pptprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position1 != 0){
                    lstAssetImageModel = new LstAssetImageModel();
                    lstAssetImageModel = assetChildRecyclerAdapter.getItemAt(position1--);
                    //digitalAssetsMaster = digitalAssetHeaderRepository.getNextorPreviousAssetDetails(digitalAssetsMaster.getSlno()+1);
                    if(lstAssetImageModel != null) {
                        initpptImage(lstAssetImageModel);
                    }else{

                    }
                }else{
                    pptprev.setVisibility(View.GONE);
                    pptnext.setVisibility(View.GONE);
                    prev.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                    childTrayLayout.setVisibility(View.GONE);
                    TrayLayout.setVisibility(View.VISIBLE);
                    prev.performClick();
                }
            }
        });


        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position != 0) {
                    digitalAssetsMaster = assetRecyclerAdapter.getItemAt(position);
                    //digitalAssetsMaster = digitalAssetHeaderRepository.getNextorPreviousAssetDetails(digitalAssetsMaster.getSlno()-1);
                    if (digitalAssetsMaster != null) {
                        position--;
                        if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("video")) {
                            initvideo(digitalAssetsMaster);
                        } else if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("image")) {
                            initImage(digitalAssetsMaster);
                        } else if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("document")) {
                            if(digitalAssetsMaster.getOnlineURL().endsWith(".pptx")
                                    ||digitalAssetsMaster.getOnlineURL().endsWith(".ppt")
                                    || digitalAssetsMaster.getDAName().endsWith(".pdf") ){
                                getPPTAssetImages(digitalAssetsMaster);
                            }
                        }else if(digitalAssetsMaster.getDA_Type().equalsIgnoreCase("articulate")
                                || digitalAssetsMaster.getDA_Type().equalsIgnoreCase("html")){
                            //if(digitalAssetsMaster.getOnlineURL().endsWith(".ppt")){
                                initWebView(digitalAssetsMaster);
                            //}
                        }
                    } else {

                    }
                }
            }
        });

        mainview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullbottomtray.isShown()){
                    showhidetray.setText("Hide");
                    fullbottomtray.setVisibility(View.GONE);
                    overlayGesture.setVisibility(View.GONE);

                }else{
                    showhidetray.setText("Hide");
                    fullbottomtray.setVisibility(View.VISIBLE);
                    overlayGesture.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void getAssetDetail(String daid){
        digitalAssetsMaster = digitalAssetHeaderRepository.getAssetDetailForPlayer(daid);
        starttime = new Date().getTime();
        if(digitalAssetsMaster != null) {
            if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("video")) {
                initvideo(digitalAssetsMaster);
            } else if(digitalAssetsMaster.getDA_Type().equalsIgnoreCase("image")){
                initImage(digitalAssetsMaster);
            } else if(digitalAssetsMaster.getDA_Type().equalsIgnoreCase("document")){
                if(digitalAssetsMaster.getOnlineURL().endsWith(".pptx")
                        ||digitalAssetsMaster.getOnlineURL().endsWith(".ppt")
                        || digitalAssetsMaster.getDAName().endsWith(".pdf") ){
                    getPPTAssetImages(digitalAssetsMaster);
                }else if(digitalAssetsMaster.getOnlineURL().endsWith(".docx")||
                        digitalAssetsMaster.getOnlineURL().endsWith(".exel") ||
                        digitalAssetsMaster.getOnlineURL().endsWith(".doc")){
                    loadOtherDocuments(digitalAssetsMaster);
                }
            }else if(digitalAssetsMaster.getDA_Type().equalsIgnoreCase("articulate")
                    || digitalAssetsMaster.getDA_Type().equalsIgnoreCase("html")){
                //if(digitalAssetsMaster.getOnlineURL().endsWith(".ppt")){
                    initWebView(digitalAssetsMaster);
                //}
            }
        }else{

        }
    }

    public void loadAssets(){
        List<DigitalAssetsMaster> digitalAssets = digitalAssetHeaderRepository.getAllAssets();
        assetRecyclerAdapter = new AssetRecyclerAdapter(mContext,digitalAssets);
        TrayLayout.setAdapter(assetRecyclerAdapter);

        assetRecyclerAdapter.setOnItemClickListener(new AssetRecyclerAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                GlobalPlayer.this.position = position;
            }
        });
    }

    public void loadChildAssets(ArrayList<AssetImages> assetimage){

        ArrayList<AssetImages> pptimages = assetimage;
        ArrayList<LstAssetImageModel> pptimagesset = (ArrayList<LstAssetImageModel>) pptimages.get(0).lstAssetImageModel;
        assetChildRecyclerAdapter = new AssetChildRecyclerAdapter(mContext,pptimagesset);
        childTrayLayout.setAdapter(assetChildRecyclerAdapter);
        TrayLayout.setVisibility(View.GONE);
        childTrayLayout.setVisibility(View.VISIBLE);
        prev.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        pptprev.setVisibility(View.VISIBLE);
        pptnext.setVisibility(View.VISIBLE);
        initpptImage((LstAssetImageModel) assetimage.get(0).lstAssetImageModel.get(0));
        assetChildRecyclerAdapter.setOnItemClickListener(new AssetChildRecyclerAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                GlobalPlayer.this.position1 = position;
            }
        });
    }

    @Override
    public void onBackPressed() {
        dismissProgressDialog();
        insertAnalytics(previousAsset,true);
    }

    public void insertBulkAnalytics(List<DigitalAssetTransactionMaster> list){
        digitalAssetTransactionRepository.bulkInsert(list);
    }

    public void insertAnalytics(DigitalAssetsMaster analyticsAsset,boolean backpressed){
        DigitalAssetTransactionMaster digitalAssetTransactionMaster = new DigitalAssetTransactionMaster();
        digitalAssetTransactionMaster.setDAID(analyticsAsset.getDAID());
        digitalAssetTransactionMaster.setIs_Read(true);
        if(liked) {
            digitalAssetTransactionMaster.setUser_Like(1);
            digitalAssetTransactionMaster.setTotalLikes(analyticsAsset.getTotalLikes()+1);
        }
        digitalAssetTransactionMaster.setPlaytime(new Date().getTime() - starttime);
        digitalAssetTransactionMaster.setRecorddate(new Date().toString());
        digitalAssetTransactionMaster.setTotalViews(analyticsAsset.getTotalViews()+1);
        if(liked){
            //digitalAssetTransactionRepository.insertOrUpdateRecord(digitalAssetTransactionMaster,"playwithlike");
        }else{
            //digitalAssetTransactionRepository.insertOrUpdateRecord(digitalAssetTransactionMaster,"play");
        }
            mList.add(digitalAssetTransactionMaster);
        liked = false;
        //Log.d("playtime",String.valueOf(playtime));
        if(backpressed) {
            insertBulkAnalytics(mList);
            gotodetails();
        }
    }

    public void gotodetails(){
        Intent i = new Intent(GlobalPlayer.this, AssetDetailActivity.class);
        i.putExtra("DAID",digitalAssetsMaster.getDAID());
        startActivity(i);
        finish();
    }

    public void initialiseViews(){
        try {
            gLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
            prev = (ImageButton) findViewById(R.id.prev);
            next = (ImageButton) findViewById(R.id.next);
            pptnext = (ImageButton) findViewById(R.id.pptnext);
            pptprev = (ImageButton) findViewById(R.id.pptprev);
            showhidetray = (TextView) findViewById(R.id.show_hide_tray);
            view = findViewById(R.id.view);
            mVideo = (VideoView) findViewById(R.id.videoview);
            mainview = (RelativeLayout) findViewById(R.id.main_view);
            imageview = (PinchZoomImageVIew) findViewById(R.id.imageview);
            webview = (WebView) findViewById(R.id.webview);

            TrayLayout = (EmptyRecyclerView) findViewById(R.id.TrayLayout);
            childTrayLayout = (EmptyRecyclerView) findViewById(R.id.childTrayLayout);
            tray_layout = (RelativeLayout) findViewById(R.id.tray_layout);

            overlayGesture = (RelativeLayout) findViewById(R.id.overlayGesture);
            fullbottomtray = (LinearLayout) findViewById(R.id.fulltay);

            if (gLibrary != null) {
                if (!gLibrary.load()) {
                    //Log.e("GestureSample", "Gesture library was not loaded...");
                    finish();
                } else {
                    //mView1 = (GestureOverlayView) findViewById(R.id.gestures1);
                    mView = (GestureOverlayView) findViewById(R.id.gesturecontrol);
                    mView.addOnGesturePerformedListener(this);
                    //mView1.addOnGesturePerformedListener(this);
                }
            }

            File wallpaperDirectory = new File("/sdcard/GlobalPlayer/");
            File createassetfolder = new File("/sdcard/GlobalPlayer/asset/");
            if(!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs();
                if(!createassetfolder.exists()) {
                    createassetfolder.mkdirs();
                }
            }

        }catch (Exception e){
            Log.e("Error",e.toString());
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    public void initWebView(DigitalAssetsMaster digitalAssetsMaster){
        try {
            if (previousAsset == null) {
                previousAsset = digitalAssetsMaster;
            } else {
                insertAnalytics(previousAsset, false);
                previousAsset = digitalAssetsMaster;
            }
            webview.setVisibility(View.VISIBLE);
            imageview.setVisibility(View.GONE);
            mVideo.setVisibility(View.GONE);
            dismissProgressDialog();
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setLoadWithOverviewMode(true);
            webview.getSettings().setUseWideViewPort(true);
            webview.setWebViewClient(new WebViewClient(){

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
                @Override
                public void onPageFinished(WebView view, final String url) {
                }
            });
            webview.loadUrl(digitalAssetsMaster.getOnlineURL());
        }catch(Exception e){
            Log.e("errorstring",e.toString());
        }
    }

    public void initImage(DigitalAssetsMaster digitalAssetsMaster)  {
        try {
            if(previousAsset == null){
                previousAsset = digitalAssetsMaster;
            } else {
                insertAnalytics(previousAsset,false);
                previousAsset = digitalAssetsMaster;
            }
            dismissProgressDialog();
            starttime = new Date().getTime();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            mVideo.setVisibility(View.GONE);
            imageview.setVisibility(View.VISIBLE);
            webview.setVisibility(View.GONE);
            String pathName = digitalAssetsMaster.getOnlineURL().toString();
            imageview.setImageBitmap(getBitmapFromURL(pathName));
        }catch (Exception e){
            Log.e("errorstring",e.toString());
        }
    }

    public void initpptImage(LstAssetImageModel lstAssetImageModelitem){
        try {
            /*if(previousAsset == null){
                previousAsset = digitalAssetsMaster;
            } else {
                insertAnalytics(previousAsset,false);
                previousAsset = digitalAssetsMaster;
            }*/
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            dismissProgressDialog();
            mVideo.setVisibility(View.GONE);
            imageview.setVisibility(View.VISIBLE);
            webview.setVisibility(View.GONE);
            String pathName = lstAssetImageModelitem.getImage_Url().toString();
            imageview.setImageBitmap(getBitmapFromURL(pathName));
        }catch (Exception e){
            Log.e("errorstring",e.toString());
        }
    }

    public void initvideo(DigitalAssetsMaster digitalAssetsMaster) {
        try {
            if(previousAsset == null){
                previousAsset = digitalAssetsMaster;
            } else {
                insertAnalytics(previousAsset,false);
                previousAsset = digitalAssetsMaster;
            }
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(mVideo);
            //String url = "";
            //specifying the location of media file
            /*if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
                 url = digitalAssetsMaster.getOnlineURL().toString();
            } else {
                 url = digitalAssetsMaster.getOfflineURL().toString();
            }*/
            //starting the videoView
            dismissProgressDialog();
            starttime = new Date().getTime();
            imageview.setVisibility(View.GONE);
            mVideo.setVisibility(View.VISIBLE);
            webview.setVisibility(View.GONE);
            mVideo.setMediaController(mediaController);
            mVideo.setVideoURI(Uri.parse(digitalAssetsMaster.getOnlineURL().toString()));
            mVideo.requestFocus();
            mVideo.start();

            mVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
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

    public void loadOtherDocuments(DigitalAssetsMaster digitalAssetsMaster){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(digitalAssetsMaster.getOnlineURL()));
        startActivity(browserIntent);
    }

    /*public void initppt(DigitalAssetsMaster digitalAssetsMaster,int position){
        try {
            if(previousAsset == null){
                previousAsset = digitalAssetsMaster;
            } else {
                insertAnalytics(previousAsset,false);
                previousAsset = digitalAssetsMaster;
            }
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            mVideo.setVisibility(View.GONE);
            imageview.setVisibility(View.VISIBLE);
            String pathName = digitalAssetsMaster.getLstEncodedUrls().get(position).toString();
            imageview.setImageBitmap(getBitmapFromURL(pathName));
        }catch (Exception e){
            Log.e("errorstring",e.toString());
        }

    }*/

    public void getPPTAssetImages(DigitalAssetsMaster digitalAssetsMaster){
        if(NetworkUtils.checkIfNetworkAvailable(mContext)) {

            AssetId assetId = new AssetId();
            assetId.setAssetId(digitalAssetsMaster.getDAID());
            List<Integer> intasset = new ArrayList<Integer>();
            intasset.add(Integer.parseInt(digitalAssetsMaster.getDAID()));
            String[] newarray = {digitalAssetsMaster.getDAID()};

            User userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);
            String subdomain = PreferenceUtils.getSubdomainName(mContext);
            int companyId = userobj.getCompany_Id();
            Call call = assetService.getAssetImages(subdomain,companyId,newarray);
            call.enqueue(new Callback<ArrayList<AssetImages>>() {

                @Override
                public void onResponse(Response<ArrayList<AssetImages>> response, Retrofit retrofit) {
                    ArrayList<AssetImages> apiResponse= response.body();
                    if (apiResponse != null) {
                        assetImages = apiResponse;
                        Log.d("log","assetsForBrowses");
                        loadChildAssets(assetImages);
                        //gotoCategoriesView();
                    } else {
                        dismissProgressDialog();
                        Toast.makeText(mContext,mContext.getResources().getString(R.string.erroroccured),Toast.LENGTH_SHORT).show();
                        Log.d("retrofit","error 2");
                        //error
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("retrofit","error 2");
                }
            });
        } else {
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = gLibrary.recognize(gesture);

        // one prediction needed
        if (predictions.size() > 0) {
            Prediction prediction = predictions.get(0);
            // checking prediction
            if (prediction.score > 1.0) {
                // and action
                if(prediction.name.equalsIgnoreCase("like")){
                    //makeText(this, prediction.name, LENGTH_SHORT).show();
                    makeText(this, "Thanks for your like", LENGTH_LONG).show();
                    liked = true;
                }else{
                    makeText(this, prediction.name, LENGTH_SHORT).show();
                }

            }
        }
    }

    public void showProgressDialog(){
        pDialog = new ProgressDialog(GlobalPlayer.this);
        pDialog.setMessage(getResources().getString(R.string.loading));
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.setIndeterminate(false);
        pDialog.show();
    }

    public void dismissProgressDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }
}
