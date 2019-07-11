package com.swaas.kangle.TaskModule;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
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
import com.swaas.kangle.API.model.LandingPageAccess;
import com.swaas.kangle.CheckList.adapter.AttachmentListAdapter;
import com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.PDFView;
import com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.listener.OnLoadCompleteListener;
import com.swaas.kangle.DigitalAssetPlayer.pdfasync.DownloadPdfAysnc;
import com.swaas.kangle.DigitalAssetPlayer.pdfasync.OnPdfDownload;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.KangleApplication;
import com.swaas.kangle.R;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.NetworkUtils;
import com.swaas.kangle.utils.TouchImageView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ViewTaskActivity extends AppCompatActivity implements SeekbarChangedListener, ExoPlayer.EventListener,OnLoadCompleteListener, OnPdfDownload {

    Context mContext;
    TextView tsk_title_text,tsk_description_text,tsk_category_text,tsk_checklistname_text,tsk_questionname_text,
            tsk_duedays_text,tsk_priority_text,tsk_attachments_text,tsk_remarks_text,tsk_status_text,tsk_response_text;
    TextView tsk_title,tsk_description,tsk_category,tsk_checklistname,tsk_questionname,
            tsk_duedays,tsk_priority,tsk_attachments,tsk_remarks,tsk_status,viewstatushistory;
    Button btn_toinprogress,btn_toreview,btn_tocomplete;
    EditText tsk_response;
    NestedScrollView main_layout,viewhistory_view;
    RelativeLayout mainView,historyView;
    LinearLayout header,content,responceLayout,viewhistory_holder;
    ImageView companylogo,edit_task,closehistory, exitattchlist, downloadaspdf;

    TaskListModel taskListModel;
    ArrayList<TaskListModel> statusHistoryList,attachmentsList;

    int isfromPage;
    boolean isFromChecklist;
    String taskstatus;
    ProgressDialog mProgressDialog;
    LinearLayout checklist,question;
    // Attachements purpose
    RelativeLayout imagelayout;
    WebView helpView;
    TextView closehelp;
    TouchImageView touchimageView;
    EmptyRecyclerView attachmentView;
    LinearLayoutManager LL1;
    View attachmentpopView;
    DownloadPdfAysnc downloadPdfAysnc;
    PDFView pdf_player;
    AttachmentListAdapter attachmentListAdapter;
    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer player;
    final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    DataSource.Factory mediaDataSourceFactory;
    EventLogger eventLogger;
    public Handler mainHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        mContext = ViewTaskActivity.this;

        statusHistoryList = new ArrayList<>();
        attachmentsList = new ArrayList<>();

        if(getIntent() != null){
            taskListModel = (TaskListModel) getIntent().getSerializableExtra("value");
            isfromPage = (int) getIntent().getSerializableExtra("isfromPage");
            taskstatus = (String) getIntent().getSerializableExtra("taskstatus");
            isFromChecklist = (boolean) getIntent().getSerializableExtra("isFromChecklist");
        }
        initializeView();
        setupthemeView();
        setupRecyclerView();
        onClicklisteners();
        try {
            loadData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void initializeView(){
        tsk_title_text = (TextView) findViewById(R.id.tsk_title_text);
        tsk_description_text = (TextView) findViewById(R.id.tsk_description_text);
        tsk_category_text = (TextView) findViewById(R.id.tsk_category_text);
        tsk_checklistname_text = (TextView) findViewById(R.id.tsk_checklistname_text);
        tsk_questionname_text = (TextView) findViewById(R.id.tsk_questionname_text);
        tsk_duedays_text = (TextView) findViewById(R.id.tsk_duedays_text);
        tsk_priority_text = (TextView) findViewById(R.id.tsk_priority_text);
        tsk_attachments_text = (TextView) findViewById(R.id.tsk_attachments_text);
        tsk_remarks_text = (TextView) findViewById(R.id.tsk_remarks_text);
        tsk_status_text = (TextView) findViewById(R.id.tsk_status_text);
        tsk_response_text = (TextView) findViewById(R.id.tsk_response_text);

        tsk_title = (TextView) findViewById(R.id.tsk_title);
        tsk_description = (TextView) findViewById(R.id.tsk_description);
        tsk_category = (TextView) findViewById(R.id.tsk_category);
        tsk_checklistname = (TextView) findViewById(R.id.tsk_checklistname);
        tsk_questionname = (TextView) findViewById(R.id.tsk_questionname);
        tsk_duedays = (TextView) findViewById(R.id.tsk_duedays);
        tsk_priority = (TextView) findViewById(R.id.tsk_priority);
        tsk_attachments = (TextView) findViewById(R.id.tsk_attachments);
        tsk_remarks = (TextView) findViewById(R.id.tsk_remarks);
        tsk_status = (TextView) findViewById(R.id.tsk_status);
        viewstatushistory = (TextView) findViewById(R.id.viewstatushistory);

        btn_toinprogress = (Button) findViewById(R.id.btn_toinprogress);
        btn_toreview = (Button) findViewById(R.id.btn_toreview);
        btn_tocomplete = (Button) findViewById(R.id.btn_tocomplete);
        btn_tocomplete.setEnabled(true);
        btn_toinprogress.setEnabled(true);
        btn_toreview.setEnabled(true);
        checklist=(LinearLayout)findViewById(R.id.checklist);
        question=(LinearLayout) findViewById(R.id.question);
        tsk_response = (EditText) findViewById(R.id.tsk_response);
        main_layout = (NestedScrollView) findViewById(R.id.main_layout);
        mainView = (RelativeLayout) findViewById(R.id.mainView);

        header = (LinearLayout) findViewById(R.id.header);
        content = (LinearLayout) findViewById(R.id.content);
        responceLayout = (LinearLayout) findViewById(R.id.responceLayout);

        companylogo = (ImageView) findViewById(R.id.companylogo);
        edit_task = (ImageView) findViewById(R.id.edit_task);

        viewhistory_holder = (LinearLayout) findViewById(R.id.viewhistory_holder);
        viewhistory_view = (NestedScrollView) findViewById(R.id.viewhistory_view);

        historyView = (RelativeLayout) findViewById(R.id.historyView);
        closehistory = (ImageView) findViewById(R.id.closehistory);


        // showing attachements

        imagelayout = (RelativeLayout) findViewById(R.id.imagelayout);
        helpView = (WebView) findViewById(R.id.helpview);
        closehelp = (TextView) findViewById(R.id.closehelp);
        touchimageView = (TouchImageView) findViewById(R.id.touchimageView);

        attachmentView = (EmptyRecyclerView) findViewById(R.id.attachmentView);
        attachmentpopView = findViewById(R.id.attachmentpopView);
        exitattchlist = (ImageView) findViewById(R.id.exitattchlist);

        pdf_player = (PDFView) findViewById(R.id.pdf_player);
        downloadaspdf = (ImageView) findViewById(R.id.downloadaspdf);

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.simple_exoplayer);
        simpleExoPlayerView.setControllerListner(this);
        if(PreferenceUtils.getLandingPageAccess(mContext) != null) {
            Gson gsonget = new Gson();
            LandingPageAccess landingobj = gsonget.fromJson(PreferenceUtils.getLandingPageAccess(mContext), LandingPageAccess.class);
            if (landingobj != null) {
                if (!TextUtils.isEmpty(landingobj.getChecklist()) && landingobj.getChecklist().equalsIgnoreCase("Y")) {
                    checklist.setVisibility(View.VISIBLE);
                    question.setVisibility(View.VISIBLE);
                }
                else{
                    checklist.setVisibility(View.GONE);
                    question.setVisibility(View.GONE);
                }
            }
        }



    }

    public void setupthemeView(){
        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        content.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.CARDBACKGROUND_COLOR)));;
        mainView.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));

        btn_toinprogress.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.CARDBACKGROUND_COLOR)));
        btn_toreview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.CARDBACKGROUND_COLOR)));
        btn_tocomplete.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.CARDBACKGROUND_COLOR)));
        btn_toinprogress.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        btn_toreview.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        btn_tocomplete.setTextColor(Color.parseColor(Constants.TEXT_COLOR));

        tsk_title_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_description_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));tsk_category_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_checklistname_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));tsk_questionname_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_duedays_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));tsk_priority_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_attachments_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));tsk_remarks_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_status_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));tsk_response_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_title.setTextColor(Color.parseColor(Constants.TEXT_COLOR));tsk_description.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_category.setTextColor(Color.parseColor(Constants.TEXT_COLOR));tsk_checklistname.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_questionname.setTextColor(Color.parseColor(Constants.TEXT_COLOR));tsk_duedays.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_priority.setTextColor(Color.parseColor(Constants.TEXT_COLOR));tsk_remarks.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_status.setTextColor(Color.parseColor(Constants.TEXT_COLOR));

        tsk_attachments.setTextColor(Color.parseColor(Constants.PARTIALLY_COMPLETED_COLOR));
        viewstatushistory.setTextColor(Color.parseColor(Constants.PARTIALLY_COMPLETED_COLOR));

        companylogo.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        edit_task.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));

        tsk_attachments.setText(Html.fromHtml("<u>"+getResources().getString(R.string.view_attachment)+"</u>"));
        viewstatushistory.setText(Html.fromHtml("<u>"+getResources().getString(R.string.status_history)+"</u>"));

        viewhistory_view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.CARDBACKGROUND_COLOR)));
    }

    public void setupRecyclerView(){
        LL1 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        attachmentView.setLayoutManager(LL1);
    }

    public void onClicklisteners(){
        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        edit_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tsk_attachments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAttachmentsList();
            }
        });

        viewstatushistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHistory();
            }
        });

        main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard((ViewTaskActivity) mContext);
            }
        });

        closehistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historyView.setVisibility(View.GONE);
                viewhistory_holder.removeAllViews();

            }
        });

        closehelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setVisibility(View.VISIBLE);
                imagelayout.setVisibility(View.GONE);
                if(player != null) {
                    player.stop();
                }
            }
        });

        exitattchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closepopupView();
            }
        });


    }

    public static void hideSoftKeyboard(ViewTaskActivity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(ViewTaskActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void loadData() throws ParseException {
        if(!isFromChecklist){
            if(isfromPage == Constants.MyTeam){
                btn_toinprogress.setVisibility(View.GONE);
                btn_toreview.setVisibility(View.GONE);
                btn_tocomplete.setVisibility(View.GONE);
                responceLayout.setVisibility(View.GONE);
            }else if(isfromPage == Constants.Review){
                btn_tocomplete.setVisibility(View.VISIBLE);
                btn_toinprogress.setVisibility(View.GONE);
                btn_toreview.setVisibility(View.GONE);
                responceLayout.setVisibility(View.VISIBLE);
            } else {
                if(taskstatus.equalsIgnoreCase(Constants.Task_open_Text)){
                    btn_toinprogress.setVisibility(View.VISIBLE);
                    btn_toreview.setVisibility(View.GONE);
                    btn_tocomplete.setVisibility(View.GONE);
                    responceLayout.setVisibility(View.VISIBLE);
                }else if(taskstatus.equalsIgnoreCase(Constants.Task_Inprogress_Text)){
                    btn_toreview.setVisibility(View.VISIBLE);
                    btn_toinprogress.setVisibility(View.GONE);
                    btn_tocomplete.setVisibility(View.GONE);
                    responceLayout.setVisibility(View.VISIBLE);
                }else if(taskstatus.equalsIgnoreCase(Constants.Task_Review_Text)){
                    if(PreferenceUtils.getTASK_ALLOW_COMPLETE(mContext).equalsIgnoreCase("true")){
                        btn_tocomplete.setVisibility(View.VISIBLE);
                        btn_toinprogress.setVisibility(View.GONE);
                        btn_toreview.setVisibility(View.GONE);
                        responceLayout.setVisibility(View.VISIBLE);
                    }else{
                        btn_toinprogress.setVisibility(View.GONE);
                        btn_toreview.setVisibility(View.GONE);
                        btn_tocomplete.setVisibility(View.GONE);
                        responceLayout.setVisibility(View.GONE);
                    }
                } else {
                    btn_toinprogress.setVisibility(View.GONE);
                    btn_toreview.setVisibility(View.GONE);
                    btn_tocomplete.setVisibility(View.GONE);
                    responceLayout.setVisibility(View.GONE);
                }
            }
            edit_task.setVisibility(View.GONE);
        }else{
            edit_task.setVisibility(View.VISIBLE);
            btn_toinprogress.setVisibility(View.GONE);
            btn_toreview.setVisibility(View.GONE);
            btn_tocomplete.setVisibility(View.GONE);
        }

        tsk_title.setText(checkForNull(taskListModel.getTask_Name()));
        tsk_description.setText(checkForNull(taskListModel.getTask_Description()));
        tsk_checklistname.setText(checkForNull(taskListModel.getChecklist_Name()));
        tsk_questionname.setText(checkForNull(taskListModel.getQuestion_Text()));
        tsk_duedays.setText(checkForNull(String.valueOf(getformatedDate(taskListModel.getTask_Due_Date())).toString()));
        tsk_priority.setText(checkForNull(getPriorityValue(taskListModel.getTask_Priority())));
        tsk_remarks.setText(checkForNull(taskListModel.getTask_Remarks()));
        tsk_status.setText(checkForNull(taskListModel.getTaskStatus()));

        if(taskstatus.equalsIgnoreCase(Constants.Task_open_Text)){
            tsk_status.setTextColor(Color.parseColor(Constants.YET_TO_START_COLOR));
        }else if(taskstatus.equalsIgnoreCase(Constants.Task_Inprogress_Text)){
            tsk_status.setTextColor(Color.parseColor(Constants.INPROGRESS_COLOR));
        }else if(taskstatus.equalsIgnoreCase(Constants.Task_Review_Text)){
            tsk_status.setTextColor(Color.parseColor(Constants.PENDING_APPROVAl_COLOR));
        }else if(taskstatus.equalsIgnoreCase(Constants.Task_Completed_Text)){
            tsk_status.setTextColor(Color.parseColor(Constants.COMPLETED_COLOR));
        }else{
            tsk_status.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        }
    }
    public String getPriorityValue(int value){
        String Priority_string = "";
        if(value == 2){
            Priority_string = mContext.getResources().getString(R.string.medium);
        }else if(value == 3){
            Priority_string = mContext.getResources().getString(R.string.high);
        }else{
            Priority_string =mContext.getResources().getString(R.string.low);
        }
        return Priority_string;
    }
    public String checkForNull(String name){
        String text = "";
        if(name != null){
            text = name;
        }else{
            text = "-";
        }
        return text;
    }

    public void submitProgress(View v){
        btn_toinprogress.setEnabled(false);
        String response = tsk_response.getText().toString().trim();
        if(response.length() > 0) {
            fnChangeStatusTaskApi(response,2,getString(R.string.tsk_acknowledged_succ));
        }else{
            fnChangeStatusTaskApi(getString(R.string.acknowledge),2,getString(R.string.tsk_acknowledged_succ));
        }
    }

    public void submitforReview(View v){
        btn_toreview.setEnabled(false);
        String response = tsk_response.getText().toString().trim();
        if(response.length() > 0) {
            fnChangeStatusTaskApi(response,3,getString(R.string.tsk_mark_for_review));
        }else{
            fnChangeStatusTaskApi(getString(R.string.waiting_for_review),3,getString(R.string.tsk_mark_for_review));
        }
    }

    public void submitComplete(View v){
        btn_tocomplete.setEnabled(false);
        String response = tsk_response.getText().toString().trim();
        if(response.length() > 0) {
            fnChangeStatusTaskApi(response, 4,getString(R.string.tsk_completed_succesfully));
        }else{
            fnChangeStatusTaskApi(getString(R.string.revieweed), 4,getString(R.string.tsk_completed_succesfully));
        }
    }

    public void fnChangeStatusTaskApi(String resp, int status,final String message){
        showProgressDialog();
        int CompanyId = PreferenceUtils.getCompnayId(mContext);
        String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
        TaskStatusModel t = new TaskStatusModel();
        t.response = resp;
        t.response_status = status;
        int UserId = PreferenceUtils.getUserId(mContext);
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            TaskServices taskServices = retrofit.create(TaskServices.class);
            Call call = taskServices.fnChangeStatusTaskApi(SubdomainName, CompanyId, UserId, taskListModel.getTask_Id(),t);
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    String res = response.body();
                    if (res != null) {
                        String result = res.toLowerCase();
                        if (result.contains("success")) {
                            dismissProgressDialog();
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            dismissProgressDialog();
                            Toast.makeText(mContext, getResources().getString(R.string.erroroccured), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        dismissProgressDialog();
                        Toast.makeText(mContext, getResources().getString(R.string.erroroccured), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    dismissProgressDialog();
                    Log.d(t.toString(), "Error");
                }
            });
        }
    }

    //Show History list Start
    public void showHistory() {
        showProgressDialog();
        int CompanyId = PreferenceUtils.getCompnayId(mContext);
        String SubdomainName = PreferenceUtils.getSubdomainName(mContext);

        if (NetworkUtils.isNetworkAvailable(mContext)) {
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            TaskServices taskServices = retrofit.create(TaskServices.class);
            Call call = taskServices.getTaskListStatusHistoryApi(SubdomainName, CompanyId, taskListModel.getTask_Id());
            call.enqueue(new Callback<ArrayList<TaskListModel>>() {

                @Override
                public void onResponse(Response<ArrayList<TaskListModel>> response, Retrofit retrofit) {
                    ArrayList<TaskListModel> mymodels = response.body();
                    if (mymodels != null && mymodels.size() > 0) {
                        statusHistoryList = mymodels;
                        try {
                            loadstatusHistoryListData();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        dismissProgressDialog();
                        Toast.makeText(mContext,getResources().getString(R.string.no_result),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(), "Error");
                }
            });
        }
    }

    public void loadstatusHistoryListData() throws ParseException {
        dismissProgressDialog();
        historyView.setVisibility(View.VISIBLE);
        if(statusHistoryList.size() > 0){
            for (TaskListModel history:  statusHistoryList){
                LinearLayout Parent = new LinearLayout(mContext);
                Parent.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams Parentlayoutparams;
                Parentlayoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Parent.setLayoutParams(Parentlayoutparams);
                Parent.removeAllViews();

                LinearLayout first = new LinearLayout(mContext);
                first.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams firstlayoutparams;
                firstlayoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,2f);
                first.setLayoutParams(firstlayoutparams);
                first.removeAllViews();

                LinearLayout second = new LinearLayout(mContext);
                second.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams secondlayoutparams;
                secondlayoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                second.setLayoutParams(secondlayoutparams);
                second.removeAllViews();

                LinearLayout.LayoutParams textviewparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams textviewparamsleft = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams textviewparamsright = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams viewparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1);

                String status = getStatusString(history.getStatus_while_responded());
                String formateddate = getformatedDate(history.getResponse_created_datetime());
                String username = history.getUser_Name();
                String remarks = history.getResponse_text();
                View v = new View(mContext);
                v.setLayoutParams(viewparams);
                v.setBackgroundColor(getResources().getColor(R.color.white));
                TextView text1 = new TextView(mContext);
                text1.setLayoutParams(textviewparamsleft);
                text1.setPadding(0,5,5,0);
                if(mContext.getResources().getBoolean(R.bool.portrait_only)){
                    text1.setTextSize(12);
                }else {
                    text1.setTextSize(16);
                }
                text1.setTypeface(text1.getTypeface(), Typeface.ITALIC);

                TextView text2 = new TextView(mContext);
                text2.setLayoutParams(textviewparamsright);
                text2.setPadding(0,5,5,0);
                if(mContext.getResources().getBoolean(R.bool.portrait_only)){
                    text2.setTextSize(12);
                }else {
                    text2.setTextSize(16);
                }

                TextView text3 = new TextView(mContext);
                text3.setLayoutParams(textviewparams);
                text3.setPadding(0,5,5,0);
                if(mContext.getResources().getBoolean(R.bool.portrait_only)){
                    text3.setTextSize(12);
                }else {
                    text3.setTextSize(16);
                }

                TextView text4 = new TextView(mContext);
                text4.setLayoutParams(textviewparams);
                text4.setPadding(0,5,5,0);
                if(mContext.getResources().getBoolean(R.bool.portrait_only)){
                    text4.setTextSize(12);
                }else {
                    text4.setTextSize(16);
                }
                text1.setTextColor(Color.parseColor(getTextColor(history.getStatus_while_responded())));
                text1.setText(status+" - ");
                text2.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
                text2.setText("("+formateddate+")");
                text2.setGravity(Gravity.CENTER);

                text3.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
                text3.setText(username);
                text4.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
                text4.setText(remarks);

                first.addView(text1);
                first.addView(text2);
                second.addView(text3);
                second.addView(text4);
                second.addView(v);

                Parent.addView(first);
                Parent.addView(second);
                viewhistory_holder.addView(Parent);
            }
        }else{
            LinearLayout.LayoutParams textviewparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView text1 = new TextView(mContext);
            text1.setLayoutParams(textviewparams);
            text1.setPadding(0,5,5,0);
            if(mContext.getResources().getBoolean(R.bool.portrait_only)){
                text1.setTextSize(12);
            }else {
                text1.setTextSize(16);
            }
            text1.setText(getResources().getString(R.string.no_result));
            text1.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
            viewhistory_holder.addView(text1);
        }
    }

    public String getStatusString(int statusinteger){
        String Status = "";
        if(statusinteger == Constants.Task_InProgress){
            Status = Constants.Task_Inprogress_Text;
        }else if(statusinteger == Constants.Task_Markforreview){
            Status = Constants.Task_Review_Text;
        }else if(statusinteger == Constants.Task_Complete){
            Status = Constants.Task_Completed_Text;
        }else {
            Status = Constants.Task_open_Text;
        }
        return Status;
    }

    public String getformatedDate(String dates) throws ParseException{
        String duedate = dates.toString();
        String onlydate = duedate;
        int t = onlydate.indexOf("T");
        String nd = onlydate.substring(0,t);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = (Date)formatter.parse(nd);
        SimpleDateFormat convertFormat = new SimpleDateFormat("dd-MMM-yyyy");
        return convertFormat.format(date);
    }

    public String getTextColor(int statusinteger){
        String Status_color = "";
        if(statusinteger == Constants.Task_InProgress){
            Status_color = Constants.PARTIALLY_COMPLETED_COLOR;
        }else if(statusinteger == Constants.Task_Markforreview){
            Status_color = "#ac423d";
        }else if(statusinteger == Constants.Task_Complete){
            Status_color = Constants.COMPLETED_COLOR;
        }else {
            Status_color = Constants.TEXT_COLOR;
        }
        return Status_color;
    }
    //show HIstorylist end

    //Attachments listing

    public void ShowAttachmentsList(){
        showProgressDialog();
        int CompanyId = PreferenceUtils.getCompnayId(mContext);
        String SubdomainName = PreferenceUtils.getSubdomainName(mContext);

        if (NetworkUtils.isNetworkAvailable(mContext)) {
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            TaskServices taskServices = retrofit.create(TaskServices.class);
            Call call = taskServices.getTaskAttachmentDataApi(SubdomainName, CompanyId, taskListModel.getTask_Id());
            call.enqueue(new Callback<ArrayList<TaskListModel>>() {

                @Override
                public void onResponse(Response<ArrayList<TaskListModel>> response, Retrofit retrofit) {
                    ArrayList<TaskListModel> mymodels = response.body();
                    if (mymodels != null && mymodels.size() > 0) {
                        attachmentsList = mymodels;
                        loadAttachmentsData(attachmentsList);
                    } else {
                        dismissProgressDialog();
                        Toast.makeText(mContext,getString(R.string.no_attachment_found),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(), "Error");
                }
            });
        }
    }

    public void loadAttachmentsData(ArrayList<TaskListModel> attachmentsList){
        dismissProgressDialog();
        loadAttachmentListPopup(attachmentsList);
    }

    public void loadAttachmentListPopup(List<TaskListModel> attachments){
        List<String> urlDataList = new ArrayList<>();
        if(attachments != null && attachments.size() > 0)
        {
            for(TaskListModel url : attachments)
            {
                urlDataList.add(url.getAttachment_URL());
            }
        }

        if(urlDataList != null && urlDataList.size() > 0)
        {
            attachmentListAdapter = new AttachmentListAdapter(mContext, urlDataList, true);
            attachmentView.setAdapter(attachmentListAdapter);
            attachmentpopView.setVisibility(View.VISIBLE);
        }



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

    public void loadnewPDFView(String URL) {
        content.setVisibility(View.GONE);
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

    public void closepopupView(){
        attachmentpopView.setVisibility(View.GONE);
    }


    public void loadattachedImageView(String Url){
        content.setVisibility(View.GONE);
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
}
