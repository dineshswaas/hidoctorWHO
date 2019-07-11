package com.swaas.kangle.CheckList;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
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
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.CheckList.adapter.AttachmentListAdapter;
import com.swaas.kangle.CheckList.adapter.SectionQuestionDetailsListAdapter;
import com.swaas.kangle.CheckList.chklistreport.ViewChecklistReportAsPDF;
import com.swaas.kangle.CheckList.model.AllSectionsQADetailModel;
import com.swaas.kangle.CheckList.model.ReportQuestionAnsersList;
import com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.PDFView;
import com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.listener.OnLoadCompleteListener;
import com.swaas.kangle.DigitalAssetPlayer.pdfasync.DownloadPdfAysnc;
import com.swaas.kangle.DigitalAssetPlayer.pdfasync.OnPdfDownload;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.KangleApplication;
import com.swaas.kangle.R;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.CommonUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.MessageDialog;
import com.swaas.kangle.utils.NetworkUtils;
import com.swaas.kangle.utils.TouchImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SectionsQuestionDetailActivity extends AppCompatActivity implements SeekbarChangedListener, ExoPlayer.EventListener,OnLoadCompleteListener, OnPdfDownload {


    ImageView companylogo,downloadaspdf;
    TextView title;
    EmptyRecyclerView recyclerView,attachmentView;
    View emptyView;
    LinearLayoutManager layoutManager,layoutManagerl1;
    Context mContext;
    SectionQuestionDetailsListAdapter adapter;
    int ChecklistId,CUMId,ChecklistGroupId;
    String checklistName;
    AllSectionsQADetailModel mAllSectionsQADetailModel;
    ArrayList<AllSectionsQADetailModel> mAllSectionsQADetailModelList;
    int position = 0;
    ImageView previoussection,nextsection;
    TextView selectedsection;
    TextView ChecklistName,completedBy,onBehalfOf,publishDate;
    String PublishDate;
    View parentview,imagelayout,sectionlayout;

    WebView helpView;
    TextView closehelp;
    TouchImageView touchimageView;

    View listview;
    TextView overallcomments,overallimage,viewHistory,acknowledgmentheading;
    View onbehalf,attachmentpopView;
    ImageView exitattchlist;
    AttachmentListAdapter attachmentListAdapter;

    DownloadPdfAysnc downloadPdfAysnc;
    PDFView pdf_player;

    final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    DataSource.Factory mediaDataSourceFactory;
    EventLogger eventLogger;
    public Handler mainHandler;
    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer player;

    boolean isfromcoursechecklist;
    int CourseId,UserId,Attemptcount,SectionId;
    boolean isfromHistory;
    TextView completedlabel,onbehalfoflabel,publishlabel;
    RelativeLayout header;

    LinearLayout sec;
    RelativeLayout overallack;
    boolean showoverallack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections_question_detail);

        mContext = SectionsQuestionDetailActivity.this;
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        initialiseViews();

        if (getIntent() != null) {
            isfromcoursechecklist = getIntent().getBooleanExtra("isfromcoursechecklist",false);
            if(isfromcoursechecklist){
                ChecklistId = (int) getIntent().getSerializableExtra("ChecklistId");
                CourseId = (int) getIntent().getSerializableExtra("CourseId");
                SectionId = (int) getIntent().getSerializableExtra("SectionId");
                UserId = (int) getIntent().getSerializableExtra("UserId");
                Attemptcount = (int) getIntent().getSerializableExtra("Attemptcount");
                checklistName = (String) getIntent().getSerializableExtra("checklistName");
                isfromHistory = getIntent().getBooleanExtra("isfromHistory",false);
            }else{
                ChecklistId = (int) getIntent().getSerializableExtra("ChecklistId");
                checklistName = (String) getIntent().getSerializableExtra("name");
                CUMId = (int) getIntent().getSerializableExtra("CUMId");
                PublishDate = (String) getIntent().getSerializableExtra("PublishDate");
                ChecklistGroupId = (int) getIntent().getSerializableExtra("ChecklistGroupId");
            }
        }

        setupRecyclerView();
        setthemeforView();
        onClickListener();
        if(!isfromcoursechecklist) {
            getListOfAllSectionsQA();
        }else {
            getListOfAllSectionsofCCList();
        }
        loadHeader();
    }

    public void initialiseViews(){
        companylogo = (ImageView) findViewById(R.id.companylogo);
        title = (TextView) findViewById(R.id.title);
        recyclerView = (EmptyRecyclerView) findViewById(R.id.recyclerView);

        emptyView = findViewById(R.id.empty_view);
        previoussection = (ImageView) findViewById(R.id.previous_section);
        nextsection = (ImageView) findViewById(R.id.next_section);
        selectedsection = (TextView) findViewById(R.id.selectedsection);
        ChecklistName = (TextView)findViewById(R.id.ChecklistName);
        completedBy = (TextView)findViewById(R.id.completedBy);
        onBehalfOf = (TextView)findViewById(R.id.onbehalfofvalue);
        publishDate = (TextView)findViewById(R.id.publishDate);
        parentview = findViewById(R.id.parentview);

        sectionlayout = findViewById(R.id.sectionlayout);
        imagelayout = findViewById(R.id.imagelayout);

        helpView = (WebView) findViewById(R.id.helpview);
        closehelp = (TextView) findViewById(R.id.closehelp);

        touchimageView = (TouchImageView) findViewById(R.id.touchimageView);

        listview = findViewById(R.id.listview);
        overallimage = (TextView) findViewById(R.id.overallimage);
        overallcomments = (TextView) findViewById(R.id.overallcomments);

        onbehalf = findViewById(R.id.onbehalf);

        attachmentView = (EmptyRecyclerView) findViewById(R.id.attachmentView);
        attachmentpopView = findViewById(R.id.attachmentpopView);
        exitattchlist = (ImageView) findViewById(R.id.exitattchlist);

        pdf_player = (PDFView) findViewById(R.id.pdf_player);
        downloadaspdf = (ImageView) findViewById(R.id.downloadaspdf);

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.simple_exoplayer);
        simpleExoPlayerView.setControllerListner(this);

        viewHistory = (TextView) findViewById(R.id.viewHistory);
        acknowledgmentheading = (TextView) findViewById(R.id.acknowledgmentheading);

        completedlabel = (TextView) findViewById(R.id.completedlabel);
        onbehalfoflabel = (TextView) findViewById(R.id.onbehalfoflabel);
        publishlabel = (TextView) findViewById(R.id.publishlabel);
        header = (RelativeLayout) findViewById(R.id.header);

        sec = (LinearLayout) findViewById(R.id.sec);
        overallack = (RelativeLayout) findViewById(R.id.overallack);

    }

    public void setupRecyclerView(){
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        layoutManagerl1 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        attachmentView.setLayoutManager(layoutManagerl1);
    }

    public void setthemeforView(){

        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        parentview.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        companylogo.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        title.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        downloadaspdf.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));

        ChecklistName.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        completedlabel.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        completedBy.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        onbehalfoflabel.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        onBehalfOf.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        publishlabel.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        publishDate.setTextColor(Color.parseColor(Constants.TEXT_COLOR));

        previoussection.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.TEXT_COLOR)));
        selectedsection.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        nextsection.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.TEXT_COLOR)));

        overallack.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.CARDBACKGROUND_COLOR)));
        sec.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.CARDBACKGROUND_COLOR)));

        viewHistory.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.CARDBACKGROUND_COLOR)));
        viewHistory.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        overallcomments.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        overallcomments.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        overallimage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        overallimage.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        acknowledgmentheading.setTextColor(Color.parseColor(Constants.TEXT_COLOR));

    }

    public void onClickListener(){
        previoussection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = position-1;
                loadQuestionsOfpreviourornextSection();
            }
        });

        nextsection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = position+1;
                loadQuestionsOfpreviourornextSection();
            }
        });

        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        closehelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sectionlayout.setVisibility(View.VISIBLE);
                imagelayout.setVisibility(View.GONE);
                if(player != null) {
                    player.stop();
                }
            }
        });

        overallimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadattachedImageView(mAllSectionsQADetailModelList.get(0).getLstAttachments().get(0).toString());
                loadAttachmentListPopup(mAllSectionsQADetailModelList.get(0).getLstAttachments());
            }
        });

        overallcomments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentsInPopup(mAllSectionsQADetailModelList.get(0).getAcknowledge_Comments());
            }
        });

        exitattchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closepopupView();
            }
        });

        downloadaspdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("value", mAllSectionsQADetailModelList);
                bundle.putSerializable("checklistname",checklistName);
                Intent intent = new Intent(mContext, ViewChecklistReportAsPDF.class);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        viewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChecklistId = mAllSectionsQADetailModelList.get(0).getChecklistId();
                Intent sectionQuestionActivity = new Intent(mContext, UserCCHistoryListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ChecklistId",ChecklistId);
                bundle.putSerializable("CourseId",CourseId);
                bundle.putSerializable("SectionId",SectionId);
                bundle.putSerializable("UserId",UserId);
                bundle.putSerializable("checklistName",checklistName);
                sectionQuestionActivity.putExtras(bundle);

                startActivity(sectionQuestionActivity);
            }
        });
    }

    public void closepopupView(){
        attachmentpopView.setVisibility(View.GONE);
    }

    public void loadQuestionsOfpreviourornextSection(){
        listview.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        downloadaspdf.setVisibility(View.VISIBLE);

        if(isfromcoursechecklist){
            if(isfromHistory) {
                viewHistory.setVisibility(View.GONE);
            }else{
                if(mAllSectionsQADetailModelList.get(0).getAttempt_Number()>1) {
                    viewHistory.setVisibility(View.VISIBLE);
                }else{
                    viewHistory.setVisibility(View.GONE);
                }
                //viewHistory.setVisibility(View.VISIBLE);
            }
            downloadaspdf.setVisibility(View.VISIBLE);
        }else{
            viewHistory.setVisibility(View.GONE);
            downloadaspdf.setVisibility(View.VISIBLE);
        }

        ArrayList<AllSectionsQADetailModel> section = new ArrayList<>();
        for(AllSectionsQADetailModel all:mAllSectionsQADetailModelList){
            if(all.getLstQuestions().size()>0){
                section.add(all);
            }
        }
        if(section.size()>0){
            if(section.size() == 1){
                previoussection.setVisibility(View.GONE);
                nextsection.setVisibility(View.GONE);
            }else{
                if(position == 0){
                    previoussection.setVisibility(View.INVISIBLE);
                    nextsection.setVisibility(View.VISIBLE);

                }else if(position == mAllSectionsQADetailModelList.size()-1){
                    previoussection.setVisibility(View.VISIBLE);
                    nextsection.setVisibility(View.INVISIBLE);
                }else{
                    previoussection.setVisibility(View.VISIBLE);
                    nextsection.setVisibility(View.VISIBLE);
                }
            }
        }
        loadSingleSectionList();
    }

    public void getListOfAllSectionsQA(){
        if(NetworkUtils.isNetworkAvailable()){

            Retrofit retrofit = RetrofitAPIBuilder.getInstance();

            CheckListService checklistService = retrofit.create(CheckListService.class);
            int CompanyId = PreferenceUtils.getCompnayId(mContext);
            Log.d("CompanyId", String.valueOf(CompanyId));
            String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            int UserId = PreferenceUtils.getUserId(mContext);
            String offsetFromUtc = CommonUtils.getUtcOffsetincluded10k();
            Log.d("getUTC", offsetFromUtc);

            Call call = checklistService.getSectionQuetsionsAnswer(SubdomainName,ChecklistId,offsetFromUtc,CompanyId,UserId,CUMId,ChecklistGroupId);
            call.enqueue(new Callback<ArrayList<AllSectionsQADetailModel>>() {

                @Override
                public void onResponse(Response<ArrayList<AllSectionsQADetailModel>> response, Retrofit retrofit) {
                    ArrayList<AllSectionsQADetailModel> allSectionsQADetailModelList= response.body();
                    if (allSectionsQADetailModelList != null && allSectionsQADetailModelList.size() > 0) {
                        mAllSectionsQADetailModelList =  allSectionsQADetailModelList;
                        loadQuestionsOfpreviourornextSection();
                    } else {
                        listview.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        downloadaspdf.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(),"Error");
                }
            });

        }else{
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
        }
    }

    public void getListOfAllSectionsofCCList(){
        if(NetworkUtils.isNetworkAvailable()){

            Retrofit retrofit = RetrofitAPIBuilder.getInstance();

            CheckListService checklistService = retrofit.create(CheckListService.class);
            int CompanyId = PreferenceUtils.getCompnayId(mContext);
            Log.d("CompanyId", String.valueOf(CompanyId));
            String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            //int UserId = PreferenceUtils.getUserId(mContext);
            String offsetFromUtc = CommonUtils.getUtcOffsetincluded10k();
            Log.d("getUTC", offsetFromUtc);

            Call call = checklistService.getUserwiseCCSectionQuetsionsAnswer(SubdomainName,ChecklistId,offsetFromUtc,CompanyId,UserId,CourseId,Attemptcount,SectionId);
            call.enqueue(new Callback<ArrayList<AllSectionsQADetailModel>>() {

                @Override
                public void onResponse(Response<ArrayList<AllSectionsQADetailModel>> response, Retrofit retrofit) {
                    ArrayList<AllSectionsQADetailModel> allSectionsQADetailModelList= response.body();
                    if (allSectionsQADetailModelList != null && allSectionsQADetailModelList.size() > 0) {
                        mAllSectionsQADetailModelList =  allSectionsQADetailModelList;
                        loadQuestionsOfpreviourornextSection();
                    } else {
                        listview.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        downloadaspdf.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(),"Error");
                }
            });

        }else{
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
        }
    }

    public void loadSingleSectionList(){
        mAllSectionsQADetailModel = mAllSectionsQADetailModelList.get(position);
        if(mAllSectionsQADetailModel.getLstQuestions() != null) {
            loadSectionDetails();
            List<ReportQuestionAnsersList> allquestionslist;
            allquestionslist = mAllSectionsQADetailModel.getLstQuestions();
            adapter = new SectionQuestionDetailsListAdapter(mContext, allquestionslist);
            recyclerView.setAdapter(adapter);
        }else{
            listview.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            downloadaspdf.setVisibility(View.GONE);
        }
    }

    public void loadSectionDetails(){
        selectedsection.setText(mAllSectionsQADetailModel.getSectionName());
        if(mAllSectionsQADetailModel.getSubmitted_By() != null &&
                !mAllSectionsQADetailModel.getSubmitted_Date().isEmpty()){
            completedBy.setText(mAllSectionsQADetailModel.getSubmitted_By());
        }else{
            completedBy.setText("");
        }
        if(mAllSectionsQADetailModel.getSubmitted_Date() != null &&
                !mAllSectionsQADetailModel.getSubmitted_Date().isEmpty()){
            publishDate.setText(mAllSectionsQADetailModel.getSubmitted_Date());
        }else{
            publishDate.setText("");
        }

        if(mAllSectionsQADetailModel.getOn_Behalf() != null &&
                !mAllSectionsQADetailModel.getOn_Behalf().isEmpty()){
            onbehalf.setVisibility(View.VISIBLE);
            onBehalfOf.setText(mAllSectionsQADetailModel.getOn_Behalf());
        }else{
            onbehalf.setVisibility(View.GONE);
            onBehalfOf.setText("");
        }


        if(mAllSectionsQADetailModelList.get(0).getAcknowledge_Comments()!= null &&
                !mAllSectionsQADetailModelList.get(0).getAcknowledge_Comments().isEmpty()) {
            //overallcomments.setText(mAllSectionsQADetailModelList.get(0).getAcknowledge_Comments());
            overallcomments.setVisibility(View.VISIBLE);
        }else{
            overallcomments.setVisibility(View.GONE);
        }

        if(mAllSectionsQADetailModelList.get(0).getLstAttachments() != null &&
                mAllSectionsQADetailModelList.get(0).getLstAttachments().size() > 0){
            overallimage.setVisibility(View.VISIBLE);
        } else {
            overallimage.setVisibility(View.GONE);
        }

        if(overallimage.isShown() || overallcomments.isShown()){
            acknowledgmentheading.setVisibility(View.VISIBLE);
            overallack.setVisibility(View.VISIBLE);
        }else{
            acknowledgmentheading.setVisibility(View.GONE);
            overallack.setVisibility(View.GONE);
        }
    }

    public void loadHeader(){
        Gson gsonget = new Gson();
        User userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);
        title.setText(checklistName.toString());
        ChecklistName.setText(checklistName.toString());
    }

    public void loadpdfattachment(String Url){
        String URL = Constants.PDF_URL+Url;
        //String URL = Constants.COMPANY_BASE_URL+"/HelpFiles/en-Kanglehelpassetlibrary.htm";
        String loadURL = Constants.PDF_URL+URL;
        WebSettings settings = helpView.getSettings();
        settings.setDomStorageEnabled(true);
        helpView.getSettings().setJavaScriptEnabled(true);
        helpView.getSettings().setLoadWithOverviewMode(true);
        helpView.getSettings().setUseWideViewPort(true);
        helpView.getSettings().setBuiltInZoomControls(true);
        helpView.getSettings().setLoadsImagesAutomatically(true);
        helpView.getSettings().setPluginState(WebSettings.PluginState.ON);
        helpView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
            }
        });
        helpView.loadUrl(URL);
    }

    public void loadvideoattachment(String Url){
        String URL = Url;
        //String URL = Constants.COMPANY_BASE_URL+"/HelpFiles/en-Kanglehelpassetlibrary.htm";
        WebSettings settings = helpView.getSettings();
        settings.setDomStorageEnabled(true);
        helpView.getSettings().setJavaScriptEnabled(true);
        helpView.getSettings().setLoadWithOverviewMode(true);
        helpView.getSettings().setUseWideViewPort(true);
        helpView.getSettings().setBuiltInZoomControls(true);
        helpView.getSettings().setLoadsImagesAutomatically(true);
        helpView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        helpView.getSettings().setPluginState(WebSettings.PluginState.ON);
        helpView.setWebChromeClient(new WebChromeClient());
        helpView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
            }
        });
        helpView.loadUrl(URL);
    }



    public void loadAttachmentListPopup(List<String> attachments){
        attachmentListAdapter = new AttachmentListAdapter(mContext, attachments, false);
        attachmentView.setAdapter(attachmentListAdapter);
        attachmentpopView.setVisibility(View.VISIBLE);
    }

    public void loadattachedImageView(String Url){
        sectionlayout.setVisibility(View.GONE);
        imagelayout.setVisibility(View.VISIBLE);
        //String url = Url.equalsIgnoreCase();
        if(Url.endsWith(".pdf")){
            pdf_player.setVisibility(View.VISIBLE);
            touchimageView.setVisibility(View.GONE);
            simpleExoPlayerView.setVisibility(View.GONE);
            //loadpdfattachment(Url);
            loadnewPDFView(Url);
        }else if(Url.endsWith(".3gp") || Url.endsWith(".mp4") || Url.endsWith(".mov") || Url.endsWith(".MOV")){
            touchimageView.setVisibility(View.GONE);
            pdf_player.setVisibility(View.GONE);
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            //loadvideoattachment(Url);
            initializePlayer(Url);
        }else{
            pdf_player.setVisibility(View.GONE);
            touchimageView.setVisibility(View.VISIBLE);
            simpleExoPlayerView.setVisibility(View.GONE);
            Ion.with(this).load(Url).intoImageView(touchimageView);
        }
    }

    public void showCommentsInPopup(String comments){
        final MessageDialog messageDialog = new MessageDialog(mContext);
        messageDialog.showCustomAlertMessageDialog(mContext,getString(R.string.remarks), comments, new View.OnClickListener() {
            @Override
            public void onClick(View Approve) {
                messageDialog.dialogDismiss();
            }
        }, true);
    }

    public void loadnewPDFView(String URL) {
        sectionlayout.setVisibility(View.GONE);
        imagelayout.setVisibility(View.VISIBLE);
        File file = new File(URL);
        if (NetworkUtils.isNetworkAvailable()) {
            downloadPdfAysnc =  new DownloadPdfAysnc(mContext,this);
            downloadPdfAysnc.execute(URL, file.getName());
        }
    }

    public void LoadOnlinePdf(String pdfpath){
        pdf_player.setVisibility(View.VISIBLE);
        pdf_player.useBestQuality(false);
        pdf_player.fromFile(new File(pdfpath)).defaultPage(0)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .load();
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPdfDownloaded(String pdfpath) {
        if (pdfpath!=null){
            LoadOnlinePdf(pdfpath);
        }
    }


    public void initializePlayer(String Url) {

        DefaultTrackSelector trackSelector;
        MediaSource mediaSource;
        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

        trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        eventLogger = new EventLogger(trackSelector);

        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(mContext),trackSelector
                , new DefaultLoadControl());

        simpleExoPlayerView.setPlayer(player);
        player.setPlayWhenReady(true);
        Uri uri = Uri.parse(Url);
        player.addListener(this);
        player.addListener(eventLogger);
        player.setAudioDebugListener(eventLogger);
        player.setVideoDebugListener(eventLogger);
        player.setMetadataOutput(eventLogger);
        mediaDataSourceFactory = buildDataSourceFactory(true);
        mainHandler = new Handler();
        mediaSource = buildMediaSource(uri);

        player.prepare(mediaSource,true,false);

        imagelayout.setVisibility(View.VISIBLE);
        simpleExoPlayerView.setVisibility(View.VISIBLE);


        simpleExoPlayerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        simpleExoPlayerView.getController().hide();


    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
                mainHandler, eventLogger);
    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((KangleApplication) this.getApplication())
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }


    @Override
    public void onStartSeek(long position) {

    }

    @Override
    public void onStopSeek(long position) {

    }

    @Override
    public void onNextClicked() {

    }

    @Override
    public void onPreviousClicked() {

    }

    @Override
    public void onNextTenClicked() {

    }

    @Override
    public void onPreviousTenClicked() {

    }

    @Override
    public void onPlayClicked() {

    }

    @Override
    public void onPauseClicked() {

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

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
}
