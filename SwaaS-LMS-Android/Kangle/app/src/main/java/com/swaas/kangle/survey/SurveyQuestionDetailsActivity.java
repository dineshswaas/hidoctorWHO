package com.swaas.kangle.survey;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
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
import com.swaas.kangle.CheckList.CheckListQuestionbuilder.adapter.AttachmentListItemRecyclerAdapter;
import com.swaas.kangle.CheckList.CheckListQuestionbuilder.adapter.MultiPleQuestionPerPage;
import com.swaas.kangle.CheckList.CheckListQuestionbuilder.adapter.QuestionPageSlider;
import com.swaas.kangle.CheckList.CheckListQuestionbuilder.customviews.QuestionViewPager;
import com.swaas.kangle.CheckList.CheckListQuestionbuilder.db.TestResultRepository;
import com.swaas.kangle.CheckList.CheckListService;
import com.swaas.kangle.CheckList.EvaluateUserforChecklistActivity;
import com.swaas.kangle.CheckList.SectionsQuestionDetailActivity;
import com.swaas.kangle.CheckList.model.Acknowledgement_urls;
import com.swaas.kangle.CheckList.model.AnwerUploadModel;
import com.swaas.kangle.CheckList.model.ChecklistFileUploadResult;
import com.swaas.kangle.CheckList.model.ChkLstSectionsQuestionAnswerList;
import com.swaas.kangle.CheckList.model.CourseUserAnswers;
import com.swaas.kangle.CheckList.model.CourseUserAssessDet;
import com.swaas.kangle.CheckList.model.CourseUserAssessHeader;
import com.swaas.kangle.CheckList.model.QuestionAndAnswerModel;
import com.swaas.kangle.CheckList.model.QuestionCourseListModel;
import com.swaas.kangle.CheckList.model.QuestionQuestionListModel;
import com.swaas.kangle.CheckList.model.UserforCourseChecklist;
import com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.PDFView;
import com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.listener.OnLoadCompleteListener;
import com.swaas.kangle.DigitalAssetPlayer.pdfasync.DownloadPdfAysnc;
import com.swaas.kangle.DigitalAssetPlayer.pdfasync.OnPdfDownload;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.KangleApplication;
import com.swaas.kangle.R;
import com.swaas.kangle.TaskModule.ChecklistViewTaskListActivity;
import com.swaas.kangle.TaskModule.CreateTaskActivity;
import com.swaas.kangle.TaskModule.TaskListModel;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.CommonUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.FileUtils;
import com.swaas.kangle.utils.MessageDialog;
import com.swaas.kangle.utils.NetworkUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class SurveyQuestionDetailsActivity extends AppCompatActivity implements SeekbarChangedListener, ExoPlayer.EventListener,View.OnClickListener, OnLoadCompleteListener, OnPdfDownload {

    private QuestionViewPager mQuestionPager;
    private LinearLayout mMutipleQuestionOptionHolder;
    private TextView mTimerText,mBtnSubmit;
    private ArrayList<ChkLstSectionsQuestionAnswerList> sectionslist;
    private ArrayList<QuestionAndAnswerModel> questionandanswerlist;
    ProgressDialog mProgressDialog;
    private long TimeAsMilli;
    private TestResultRepository testResultRepository;
    private RecyclerView mMutipleQuestionRecycleView;
    private  boolean isSingleQuestionPerPage = false;
    private int QuestionLoadCount;
    String validtodate;
    int courseId,SectionId,publishId;

    TextView selectedsection,acknowledgeText,
            skip,overallcomment,disablesubmit,chklisttitle,cancellist,draft;

    TextView descriptiontext,fulldescriptiontext,showmoredesc,
            chkdescriptiontext,fullchkdescriptiontext,chkshowmoredesc;

    EditText onbehalfof,logedinUsername;
    ImageView previoussection,nextsection,previous1section,next1section,companylogo;
    CheckBox acknowledgechk;
    String checklistName;
    int position = 0;
    int ChecklistPublishType;
    int AssignmentId;
    public static final String DEFAULT_IMAGE_EXTENSION = ".jpg";
    private String mCurrentPhotoPath;

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_IMAGE_BROWSE = 2;
    static final int REQUEST_VIDEO_CAPTURE = 3;
    public static final int REQUEST_CAMERA_PERMISSION = 120;
    public static final int REQUEST_STORAGE_PERMISSION = 121;

    ImageView attachmentfile,addattachment;
    View attachmentlayout;
    Context mContext;
    public int ChecklistId,CUMId,ChecklistGroupId, checklistTypeId;
    public String PublishDate,ChecklistDescription;
    String compressedImagePath;
    long imageSizeInBytes;
    long imageSizeInKb;
    QuestionAndAnswerModel attachmentmodel;
    SurveyMultiPleQuestionPerPage multiPleQuestionPerPage;
    MessageDialog messageDialog;
    View Description,ChkDescription,overall,imagelayout;

    LinearLayoutManager layoutManager;
    EmptyRecyclerView attachmentfilerecycler;
    SurveyAttachmentListItemRecyclerAdapter adapter;
    List<Acknowledgement_urls> ackurls;
    ImageView close,delete;
    public int attachmentposition;
    WebView helpView;

    PDFView pdf_player;
    VideoView videoView;
    DownloadPdfAysnc downloadPdfAysnc;

    int questionsadapterpos = 0;

    ArrayList<ChkLstSectionsQuestionAnswerList> answeredsecmodel;

    final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    DataSource.Factory mediaDataSourceFactory;
    EventLogger eventLogger;
    public Handler mainHandler;
    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer player;

    private ArrayList<UserforCourseChecklist> selectedusers;
    boolean isfromcclst;

    boolean isdraftedbyuser = false;
    LinearLayout buttonsview,header;

    public static final int TASK_LIST_CODE = 333;
    public static final int NEW_TASK_CREATION = 444;
    public static final int OVERALL_VIEW_TASK_LIST = 555;
    public static final int NEW_OVERALL_TASK_CREATION = 556;

    ImageView addoveralltask,viewoveralltask;
    View overalltaskbuttonsLayout;
    boolean isGroupChecklist;

    ArrayList<TaskListModel> gettingCheckListTaskList,overallTaskList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = SurveyQuestionDetailsActivity.this;
        setContentView(R.layout.activity_checklist_question);
        mQuestionPager = (QuestionViewPager) findViewById(R.id.pager_questions);
        mTimerText = (TextView) findViewById(R.id.question_time_text);
        testResultRepository =  new TestResultRepository(this);
        mMutipleQuestionOptionHolder = (LinearLayout) findViewById(R.id.multiple_question_holder);
        mMutipleQuestionRecycleView = (RecyclerView) findViewById(R.id.multiple_question_view);
        mBtnSubmit = (TextView) findViewById(R.id.btn_submit_mutiple);
        mMutipleQuestionRecycleView.setLayoutManager(new LinearLayoutManager(this));
        //mMutipleQuestionRecycleView.setItemAnimator(new DefaultItemAnimator());
        mBtnSubmit.setOnClickListener(this);

        selectedsection = (TextView) findViewById(R.id.selectedsection);

        previoussection = (ImageView) findViewById(R.id.previous_section);
        nextsection = (ImageView) findViewById(R.id.next_section);
        previous1section = (ImageView) findViewById(R.id.previous1_section);
        next1section = (ImageView) findViewById(R.id.next1_section);

        descriptiontext =(TextView) findViewById(R.id.descriptiontext);
        fulldescriptiontext = (TextView) findViewById(R.id.fulldescriptiontext);
        showmoredesc = (TextView) findViewById(R.id.showmoredesc);

        chkdescriptiontext =(TextView) findViewById(R.id.chkdescriptiontext);
        fullchkdescriptiontext = (TextView) findViewById(R.id.fullchkdescriptiontext);
        chkshowmoredesc = (TextView) findViewById(R.id.chkshowmoredesc);

        acknowledgechk =(CheckBox) findViewById(R.id.acknowledgechk);
        acknowledgeText =(TextView) findViewById(R.id.acknowledgeText);
        overallcomment = (TextView) findViewById(R.id.overallcomment);
        disablesubmit = (TextView) findViewById(R.id.btn_disable_submit_mutiple);
        cancellist = (TextView) findViewById(R.id.btn_cancel);
        skip = (TextView) findViewById(R.id.skip);
        disablesubmit.setOnClickListener(this);
        cancellist.setOnClickListener(this);

        logedinUsername = (EditText) findViewById(R.id.logedinUsername);
        onbehalfof = (EditText) findViewById(R.id.onbehalfof);

        addattachment = (ImageView) findViewById(R.id.addattachment);
        attachmentfile = (ImageView) findViewById(R.id.attachmentfile);
        attachmentlayout = findViewById(R.id.attachmentlayout);

        companylogo = (ImageView) findViewById(R.id.companylogo);
        chklisttitle = (TextView) findViewById(R.id.chklisttitle);
        companylogo.setOnClickListener(this);
        messageDialog = new MessageDialog(mContext);

        Description = findViewById(R.id.Description);
        ChkDescription = findViewById(R.id.ChkDescription);
        overall = findViewById(R.id.overall);

        attachmentfilerecycler = (EmptyRecyclerView) findViewById(R.id.attachmentfilerecycler);
        ackurls = new ArrayList<>();
        imagelayout = findViewById(R.id.imagelayout);

        close = (ImageView) findViewById(R.id.close);
        delete = (ImageView) findViewById(R.id.delete);
        helpView = (WebView) findViewById(R.id.helpview);

        pdf_player = (PDFView) findViewById(R.id.pdf_player);
        videoView = (VideoView) findViewById(R.id.video_View);

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.simple_exoplayer);
        simpleExoPlayerView.setControllerListner(this);

        draft = (TextView) findViewById(R.id.btn_draft);
        draft.setOnClickListener(this);

        buttonsview = (LinearLayout) findViewById(R.id.buttonsview);

        header = (LinearLayout)findViewById(R.id.header);

        addoveralltask = (ImageView) findViewById(R.id.addoveralltask);
        viewoveralltask = (ImageView) findViewById(R.id.viewoveralltask);
        overalltaskbuttonsLayout = findViewById(R.id.overalltaskbuttonsLayout);

        gettingCheckListTaskList = new ArrayList<>();
        overallTaskList = new ArrayList<>();
        onClickListeners();
        setthemeforView();
        camerapermission();
        getSectionsList();
        setupRecyclerView();
        getoverallTaskEnabled();
        addQuestiontypeMatch();
    }

    public void getoverallTaskEnabled(){
        if(sectionslist.get(0).getLstCourse().is_Task_Enabled()){
            overalltaskbuttonsLayout.setVisibility(View.VISIBLE);
        } else {
            overalltaskbuttonsLayout.setVisibility(View.GONE);
        }
    }

    public void setthemeforView(){

        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        overall.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        /*previoussection.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        nextsection.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));*/
        previoussection.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        nextsection.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        companylogo.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        chklisttitle.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
    }

    public void setupRecyclerView(){
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        attachmentfilerecycler.setLayoutManager(layoutManager);
    }

    public void onClickListeners(){
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

        previous1section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = position-1;
                loadQuestionsOfpreviourornextSection();
            }
        });

        next1section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = position+1;
                loadQuestionsOfpreviourornextSection();
            }
        });

        acknowledgechk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(logedinUsername.length() > 0 && onbehalfof.length() > 0 && acknowledgechk.isChecked()){
                    disablesubmit.setVisibility(View.GONE);
                }else{
                    disablesubmit.setVisibility(View.VISIBLE);
                }
            }
        });

        onbehalfof.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(logedinUsername.length() > 0 && onbehalfof.length() > 0 && acknowledgechk.isChecked()){
                    disablesubmit.setVisibility(View.GONE);
                }else{
                    disablesubmit.setVisibility(View.VISIBLE);
                }
            }
        });


        logedinUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(logedinUsername.length() > 0 && onbehalfof.length() > 0 && acknowledgechk.isChecked()){
                    disablesubmit.setVisibility(View.GONE);
                }else{
                    disablesubmit.setVisibility(View.VISIBLE);
                }
            }
        });

        addattachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageDialog.showPhotoDialog(mContext,new View.OnClickListener() {
                    @Override
                    public void onClick(View Approve) {
                        messageDialog.dialogDismiss();
                        takePhoto(null,0);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View close) {
                        messageDialog.dialogDismiss();
                        browsePhoto(null,0);
                    }
                },new View.OnClickListener() {
                    @Override
                    public void onClick(View close) {
                        messageDialog.dialogDismiss();
                        browsePhoto(null,0);
                    }
                },new View.OnClickListener() {
                    @Override
                    public void onClick(View close) {
                        messageDialog.dialogDismiss();
                        TakeVideoIntent(null,0);
                    }
                },true,true,true,true,false);
            }
        });

        attachmentfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageDialog.showAttachmentpopupDialog(mContext,sectionslist.get(position).getLstCourse().getOverallAttachmentURL()
                        ,new View.OnClickListener() {
                            @Override
                            public void onClick(View Approve) {
                                messageDialog.dialogDismiss();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View close) {
                                messageDialog.dialogDismiss();
                                sectionslist.get(position).getLstCourse().setOverallAttachmentURL(null);
                                attachmentfile.setVisibility(View.GONE);
                                addattachment.setVisibility(View.VISIBLE);
                            }
                        },true);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagelayout.setVisibility(View.GONE);
                player.stop();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagelayout.setVisibility(View.GONE);
                ackurls.remove(attachmentposition);
                reloadAdapter(ackurls);
            }
        });


        chkshowmoredesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chkdescriptiontext.isShown()){
                    chkdescriptiontext.setVisibility(View.GONE);
                    fullchkdescriptiontext.setVisibility(View.VISIBLE);
                    chkshowmoredesc.setText(getResources().getString(R.string.less));
                }else{
                    chkdescriptiontext.setVisibility(View.VISIBLE);
                    fullchkdescriptiontext.setVisibility(View.GONE);
                    chkshowmoredesc.setText(getResources().getString(R.string.more));
                }
            }
        });

        showmoredesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(descriptiontext.isShown()){
                    descriptiontext.setVisibility(View.GONE);
                    fulldescriptiontext.setVisibility(View.VISIBLE);
                    showmoredesc.setText(getResources().getString(R.string.less));
                }else{
                    descriptiontext.setVisibility(View.VISIBLE);
                    fulldescriptiontext.setVisibility(View.GONE);
                    showmoredesc.setText(getResources().getString(R.string.more));
                }
            }
        });

        addoveralltask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoOverallCreateTaskActivity(sectionslist.get(0).getLstCourse().getCourse_Id());
            }
        });

        viewoveralltask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gotoOverallViewTaskListActivity(sectionslist.get(0).getLstCourse().getCourse_Id());
            }
        });

    }

    public void loadViewbasedonURL(Acknowledgement_urls urls,int position){
        attachmentposition = position;
        if(urls.getUrl().endsWith(".png") || urls.getUrl().endsWith(".jpg")){
            String url = urls.getUrl();
            imagelayout.setVisibility(View.GONE);
            loadImage(url,position);
        }else{
            String url = urls.getUrl();
            loadOtherFiles(url,position);
        }
    }

    public void loadImage(String url,final int pos){
        messageDialog.showAttachmentpopupDialog(mContext,url
                ,new View.OnClickListener() {
                    @Override
                    public void onClick(View Approve) {
                        messageDialog.dialogDismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View close) {
                        messageDialog.dialogDismiss();
                        //sectionslist.get(position).getLstCourse().setOverallAttachmentURL(null);
                        ackurls.remove(pos);
                        reloadAdapter(ackurls);
                    }
                },true);
    }

    public void loadOtherFiles(String url,final int pos){
        if(url.endsWith(".pdf")) {
            loadnewPDFView(url);
        }else{
            initializePlayer(url);
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

    public void loadnewPDFView(String URL) {
        imagelayout.setVisibility(View.VISIBLE);
        File file = new File(URL);
        if (NetworkUtils.isNetworkAvailable()) {
            downloadPdfAysnc =  new DownloadPdfAysnc(mContext,this);
            downloadPdfAysnc.execute(URL, file.getName());
        }
    }

    public void LoadOnlinePdf(String pdfpath){
        videoView.setVisibility(View.GONE);
        pdf_player.setVisibility(View.VISIBLE);
        pdf_player.useBestQuality(false);
        pdf_player.fromFile(new File(pdfpath)).defaultPage(0)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .load();
    }

    public void getSectionsList(){
        if (getIntent() != null) {
            sectionslist = (ArrayList<ChkLstSectionsQuestionAnswerList>) getIntent().getSerializableExtra("value");
            checklistName = (String) getIntent().getSerializableExtra("name");
            ChecklistPublishType = (int) getIntent().getSerializableExtra("ChecklistPublishType");
            AssignmentId = (int) getIntent().getSerializableExtra("AssignmentId");

            ChecklistId = (int) getIntent().getSerializableExtra("ChecklistId");
            CUMId = (int) getIntent().getSerializableExtra("CUMId");
            PublishDate = (String) getIntent().getSerializableExtra("PublishDate");
            ChecklistGroupId = (int) getIntent().getSerializableExtra("ChecklistGroupId");
            isGroupChecklist = (boolean) getIntent().getSerializableExtra("isGroupChecklist");
            ChecklistDescription = (String) getIntent().getSerializableExtra("ChecklistDescription");
            isfromcclst = (boolean) getIntent().getSerializableExtra("isfromcoursechecklist");
            checklistTypeId = (int) getIntent().getSerializableExtra("CheckListTypeId");
            if(isfromcclst) {
                selectedusers = (ArrayList<UserforCourseChecklist>) getIntent().getSerializableExtra("CCuserlist");
                mBtnSubmit.setText(R.string.submit_result);
                draft.setVisibility(View.VISIBLE);
                buttonsview.setWeightSum(3);
                gettingCheckListTaskList =(ArrayList<TaskListModel>) getIntent().getSerializableExtra(Constants.TaskList);
            }else{
                selectedusers = new ArrayList<>();
                mBtnSubmit.setText(mContext.getResources().getString(R.string.Submit));
                draft.setVisibility(View.VISIBLE);
                buttonsview.setWeightSum(3);
                gettingCheckListTaskList =(ArrayList<TaskListModel>) getIntent().getSerializableExtra(Constants.TaskList);
            }
        }

        insertSectionheaderdetilas();
        loadQuestionsOfpreviourornextSection();
        if(checklistName!= null){
            chklisttitle.setText(checklistName);
            Gson gsonget = new Gson();
            User userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);
            //logedinUsername.setText(userobj.getEmployee_Name());
            //onbehalfof.setText(userobj.getEmployee_Name());
        }
    }

    public void insertSectionheaderdetilas(){

        for(ChkLstSectionsQuestionAnswerList section:sectionslist){
            insertHeaderdetails(section);
        }
    }

    public void loadQuestionsOfpreviourornextSection(){

        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(mContext) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        smoothScroller.setTargetPosition(position);
        LinearLayoutManager layoutManager = (LinearLayoutManager) mMutipleQuestionRecycleView.getLayoutManager();
        layoutManager.startSmoothScroll(smoothScroller);
        //mMutipleQuestionRecycleView.smoothScrollToPosition(0);
        //layoutManager.smoothScrollToPosition(mMutipleQuestionRecycleView, null, 0);
        //mMutipleQuestionRecycleView.scrollToPosition(0);
        //mMutipleQuestionRecycleView.getLayoutManager().scrollToPosition(0);

        if(sectionslist.size()>0){
            if(sectionslist.size() == 1){
                previoussection.setVisibility(View.INVISIBLE);
                nextsection.setVisibility(View.INVISIBLE);
                previous1section.setVisibility(View.INVISIBLE);
                next1section.setVisibility(View.INVISIBLE);
            }else{
                if(position == 0){
                    previoussection.setVisibility(View.INVISIBLE);
                    nextsection.setVisibility(View.VISIBLE);
                    previous1section.setVisibility(View.INVISIBLE);
                    next1section.setVisibility(View.VISIBLE);

                }else if(position == sectionslist.size()-1){
                    previoussection.setVisibility(View.VISIBLE);
                    nextsection.setVisibility(View.INVISIBLE);
                    previous1section.setVisibility(View.VISIBLE);
                    next1section.setVisibility(View.INVISIBLE);

                }else{
                    previoussection.setVisibility(View.VISIBLE);
                    nextsection.setVisibility(View.VISIBLE);
                    previous1section.setVisibility(View.VISIBLE);
                    next1section.setVisibility(View.VISIBLE);
                }
            }
        }
        getSingleSectionDetail(position);
    }

    private void getSingleSectionDetail(int position){
        if(sectionslist.get(position).getLstCourse().getSection_Name() != null
                && !sectionslist.get(position).getLstCourse().getSection_Name().isEmpty()) {
            selectedsection.setText(getResources().getString(R.string.section_title)+ (position+1)+" "+getResources().getString(R.string.of)+" "+ sectionslist.size()+" \n "+sectionslist.get(position).getLstCourse().getSection_Name().toString());
        }else{
            selectedsection.setText("Section 1");
        }
        if(sectionslist.get(position).getLstCourse().getTest_Description() != null
                && !sectionslist.get(position).getLstCourse().getTest_Description().isEmpty()) {
            Description.setVisibility(View.VISIBLE);
            descriptiontext.setText(sectionslist.get(position).getLstCourse().getTest_Description().toString());
            fulldescriptiontext.setText(sectionslist.get(position).getLstCourse().getTest_Description().toString());
            fulldescriptiontext.setMovementMethod(new ScrollingMovementMethod());
        }else{
            Description.setVisibility(View.GONE);
        }

        if(ChecklistDescription != null && !ChecklistDescription.isEmpty()) {
            ChkDescription.setVisibility(View.VISIBLE);
            chkdescriptiontext.setText(ChecklistDescription.toString());
            fullchkdescriptiontext.setText(ChecklistDescription.toString());
            fullchkdescriptiontext.setMovementMethod(new ScrollingMovementMethod());
        }else{
            ChkDescription.setVisibility(View.GONE);
        }


        if(sectionslist.get(position).getQuestionanswerList().size() > 0){
            GetQuestionList((ArrayList<QuestionAndAnswerModel>) sectionslist.get(position).getQuestionanswerList());
            //addQuestiontypeMatch();
        }else{

        }

    }


    public void insertHeaderdetails(ChkLstSectionsQuestionAnswerList sec){
        if(NetworkUtils.isNetworkAvailable()){
            //showProgressDialog();
            /*RetrofitCustomApiBuilder retrofitcustomAPI = new RetrofitCustomApiBuilder("https://api.myjson.com/");
            Retrofit retrofit = retrofitcustomAPI.getInstance();*/

            Retrofit retrofit = RetrofitAPIBuilder.getInstance();

            CheckListService checklistService = retrofit.create(CheckListService.class);
            int CompanyId = PreferenceUtils.getCompnayId(mContext);
            Log.d("CompanyId", String.valueOf(CompanyId));
            String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            int UserId = PreferenceUtils.getUserId(mContext);
            int ChecklistId = sec.getLstCourse().getCourse_Id();
            int ChecklistUserAssignMentId = AssignmentId;
            int ChecklistUserSectionId = sec.getLstCourse().getChecklist_Section_User_Mapping_Id();
            int publishId = sec.getLstCourse().getPublish_ID();
            int sectionId = sec.getLstCourse().getSection_Id();
            //int sectionId = sec.getQuestionanswerList().get(0).questionModel.getSection_Id();

            //Call call = checklistService.getSectionsandQuestion();
            //{subdomainName}/{ChecklistId}/{ChecklistUserAssignMentId}/{ChecklistUserSectionId}/{publishId}/{userId}/{companyId}/{sectionId}
            Call call = checklistService.insertLPChecklistSectionUserExamHeader(
                    SubdomainName, ChecklistId,ChecklistUserAssignMentId,ChecklistUserSectionId,publishId,UserId,CompanyId,sectionId);
            call.enqueue(new Callback<Integer>() {

                @Override
                public void onResponse(Response<Integer> response, Retrofit retrofit) {
                    Integer resp= response.body();
                    if (resp != null && resp > 0) {
                        //dismissProgressDialog();

                    } else {

                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(mContext,"Error inserting header",Toast.LENGTH_SHORT).show();
                    Log.d(t.toString(),"Error");
                }
            });

        }else{
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
        }
    }

    private void GetQuestionList(ArrayList<QuestionAndAnswerModel> answermodel) {
        //selectedsection.setText(sectionslist.get(position).getLstCourse().getSection_Name().toString());
        //descriptiontext.setText(sectionslist.get(position).getLstCourse().getCourse_Description().toString());
        //if (getIntent() != null){
        questionandanswerlist = answermodel;

        courseId = getIntent().getIntExtra(Constants.Course_Id,0);
        publishId =  getIntent().getIntExtra(Constants.Publish_Id,0);
        SectionId = getIntent().getIntExtra(Constants.Section_Id,0);
        validtodate = getIntent().getStringExtra("SectionDate");
        QuestionLoadCount = questionandanswerlist.get(0).getLstCourse().get(0).getQuestionLoadCount();
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        try {

            String DateAsString = questionandanswerlist.get(0).getLstCourse().get(0).getDuration();
            String kept = DateAsString.substring( 0, DateAsString.indexOf("."));
            date = sdf.parse(kept);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        long  hoursAsMilli = TimeUnit.HOURS.toMillis(date.getHours());
        long minutesAsMilli  =  TimeUnit.MINUTES.toMillis(date.getMinutes());
        long secondsAsMilli = TimeUnit.SECONDS.toMillis(date.getSeconds());
        TimeAsMilli = hoursAsMilli+minutesAsMilli+secondsAsMilli;

        if (questionandanswerlist.get(0).getLstCourse().get(0).getQuestionLoadCount() == 0){

            isSingleQuestionPerPage = false;

        }else if (questionandanswerlist.get(0).getLstCourse().get(0).getQuestionLoadCount() == 1){

            isSingleQuestionPerPage = true;
        }


        if(isSingleQuestionPerPage){

            mMutipleQuestionOptionHolder.setVisibility(View.GONE);
            mQuestionPager.setVisibility(View.VISIBLE);
            QuestionPageSlider questionPageSlider = new QuestionPageSlider(getSupportFragmentManager(),questionandanswerlist);
            mQuestionPager.setAdapter(questionPageSlider);

        } else {

            mMutipleQuestionOptionHolder.setVisibility(View.VISIBLE);
            mQuestionPager.setVisibility(View.GONE);
            multiPleQuestionPerPage =  new SurveyMultiPleQuestionPerPage(this,questionandanswerlist,this);
            mMutipleQuestionRecycleView.setAdapter(multiPleQuestionPerPage);

        }

        //}

    }

    private void ShowAlert(String Message, String Title, final boolean offline,final boolean successfull,final int typeoferror) {

        final AlertDialog alertDialog = new AlertDialog.Builder(
                SurveyQuestionDetailsActivity.this).create();

        alertDialog.setCancelable(false);
        // Setting Dialog Title
        alertDialog.setTitle(Title);
        // Setting Dialog Message
        alertDialog.setMessage(Message);

        // Setting OK Button
        alertDialog.setButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                if(successfull){
                    if(isdraftedbyuser) {
                        alertDialog.dismiss();
                        finish();
                    }else{
                        StartReportActiity();
                    }
                }else{
                    if(typeoferror == 2) {
                        alertDialog.dismiss();
                        finish();
                    }else{
                        alertDialog.dismiss();
                    }
                }
            }
        });
        // Showing Alert Message
        alertDialog.show();

    }

    private void OnQuestionFinishedEvent(ArrayList<QuestionAndAnswerModel> questionsectionQuestionandAnswer) {

        if (questionsectionQuestionandAnswer.size()>0){

            //Checknetworkandupload(UploadAnswerProcess(questionsectionQuestionandAnswer),true,false, false);
            Checknetworkandupload(UploadAnswerProcess(questionsectionQuestionandAnswer),true,false, false);

        }
    }

    public void checkstring(final String answeruploadmodel){
        String uploadmodel = answeruploadmodel;
    }

    private AnwerUploadModel UploadAnswerProcess(ArrayList<QuestionAndAnswerModel> questionandanswerlist) {

        AnwerUploadModel answerupload =  new AnwerUploadModel();
        answerupload.setLstChecklistUserAnswers(getCourseUserAnswer(questionandanswerlist));
        answerupload.setLstChecklistUserAssessHeader(getCourseUserAssetHeader(sectionslist));
        answerupload.setLstChecklistUserAssessDet(getCourseAssetDetails(questionandanswerlist));
        if(isdraftedbyuser){
            answerupload.setTaskCreatelist(getonlyQuesionTaskDetails(gettingCheckListTaskList));
        }else {
            answerupload.setTaskCreatelist(getAllTaskDetails(gettingCheckListTaskList));
        }
        Log.d("test",new Gson().toJson(answerupload));
        //return new Gson().toJson(answerupload);
        return answerupload;
    }

    private List<TaskListModel> getAllTaskDetails(ArrayList<TaskListModel> tasks){
        List<TaskListModel> mTasklist = new ArrayList<>();
        for(TaskListModel t : tasks){
            if(isdraftedbyuser){
                t.setIs_Draft_Task(1);
            } else {
                t.setIs_Draft_Task(0);
            }

            if(checklistTypeId == 3)
            {
                t.setGroupChecklistCheck(2);
            }
            else
            {
                if(checklistTypeId == 1)
                {
                    t.setGroupChecklistCheck(1);
                }
                else if(checklistTypeId == 2)
                {
                    t.setGroupChecklistCheck(2);
                }
            }

            t.setChecklist_Group_Id(ChecklistGroupId);


            mTasklist.add(t);
        }
        mTasklist.addAll(overallTaskList);
        return mTasklist;
    }

    private List<TaskListModel> getonlyQuesionTaskDetails(ArrayList<TaskListModel> tasks){
        List<TaskListModel> mTasklist = new ArrayList<>();
        for(TaskListModel t : tasks){
            if(isdraftedbyuser){
                t.setIs_Draft_Task(1);
            } else {
                t.setIs_Draft_Task(0);
            }

            if(checklistTypeId == 3)
            {
                t.setGroupChecklistCheck(2);
            }
            else
            {
                if(checklistTypeId == 1)
                {
                    t.setGroupChecklistCheck(1);
                }
                else if(checklistTypeId == 2)
                {
                    t.setGroupChecklistCheck(2);
                }
            }
            t.setChecklist_Group_Id(ChecklistGroupId);

            mTasklist.add(t);
        }
        return mTasklist;
    }

    private List<CourseUserAssessHeader> getCourseUserAssetHeader(ArrayList<ChkLstSectionsQuestionAnswerList> sectionslists) {

        List<CourseUserAssessHeader> courseuserAssessHeaderList = new ArrayList<>();
        for(ChkLstSectionsQuestionAnswerList sec:sectionslists){
            CourseUserAssessHeader courseAssetHeader = new CourseUserAssessHeader();

            courseAssetHeader.Company_Id = String.valueOf(PreferenceUtils.getCompnayId(this));
            QuestionCourseListModel questioncoursemodel = sec.getLstCourse();
            courseAssetHeader.Checklist_ID = questioncoursemodel.getCourse_Id();
            courseAssetHeader.Checklist_Section_User_Exam_Id = 1;
            courseAssetHeader.Checklist_User_Assignment_Id = sec.getLstCourse().getChecklist_User_Mapping_Id();
            courseAssetHeader.Couse_User_Section_Mapping_Id = sec.getLstCourse().getChecklist_Section_User_Mapping_Id();
            courseAssetHeader.Section_ID = sec.getLstCourse().getSection_Id();
            courseAssetHeader.User_Id = PreferenceUtils.getUserId(this);
            courseAssetHeader.Publish_ID = sec.getLstCourse().getPublish_ID();
            courseAssetHeader.Achieved_Percentage = 100;//Integer.parseInt(CalculatePercentage());
            courseAssetHeader.Pass_Percentage = "100";//String.valueOf(questioncoursemodel.getPass_Percentage());
            courseAssetHeader.Is_Qualified = 1;
            courseAssetHeader.Local_TimeZone = new Date().toString();
            courseAssetHeader.Offset_Value = CommonUtils.getUtcOffsetincluded10k();
            courseAssetHeader.Is_Acknowledge = 1;
            courseAssetHeader.Checklist_Publish_Type = ChecklistPublishType;
            courseAssetHeader.CheckList_Publish_Group_Id = ChecklistGroupId;
            courseAssetHeader.User_Course_Checklist_Mapping_Id = sec.getLstCourse().getUser_Course_Checklist_Mapping_Id();
            if(isdraftedbyuser){
                courseAssetHeader.Is_Draft = "0";
            }else{
                courseAssetHeader.Is_Draft = "1";
            }

            if(overallcomment.getText() != null){
                if(overallcomment.getText().length()>0) {
                    courseAssetHeader.Acknowledge_Comments = overallcomment.getText().toString();
                }else{
                    courseAssetHeader.Acknowledge_Comments = "";
                }
            }
            if(questioncoursemodel.getOverallAttachmentURL() != null){
                courseAssetHeader.Acknowledge_img_url = questioncoursemodel.getOverallAttachmentURL();
            }else{
                courseAssetHeader.Acknowledge_img_url = "";
            }

            if(ackurls.size() > 0){
                courseAssetHeader.Ack_urls = ackurls;
            }else{
                courseAssetHeader.Ack_urls = ackurls;
            }

            if(onbehalfof.getText() != null){
                if(onbehalfof.getText().length()>0) {
                    courseAssetHeader.On_Behalf = onbehalfof.getText().toString();
                }else{
                    courseAssetHeader.On_Behalf = "";
                }
            }

            if(logedinUsername.getText() != null){
                if(logedinUsername.getText().length()>0) {
                    courseAssetHeader.Ack_Given_Name = logedinUsername.getText().toString();
                }else{
                    courseAssetHeader.Ack_Given_Name = "";
                }
            }

            courseuserAssessHeaderList.add(courseAssetHeader);

        }


        return courseuserAssessHeaderList;
    }

    private List<CourseUserAnswers> getCourseUserAnswer(ArrayList<QuestionAndAnswerModel> questionandanswerlist) {


        List<CourseUserAnswers> courseuseranswerList =  new ArrayList<>();
        for (QuestionAndAnswerModel questionandanswermodel : questionandanswerlist){

            if (questionandanswermodel.getQuestionModel().getQuestion_Type() != 1){

                CourseUserAnswers courseuseranswer = new CourseUserAnswers();

                if (questionandanswermodel.getQuestionModel().getQuestion_Type() ==0){

                    courseuseranswer.Company_Id = String.valueOf(PreferenceUtils.getCompnayId(this));
                    courseuseranswer.User_Answer_Text = questionandanswermodel.getChoosenAnswer();
                    courseuseranswer.User_Id = PreferenceUtils.getUserId(this);
                    courseuseranswer.Question_Id = String.valueOf(questionandanswermodel.getQuestionModel().getQuestion_Id());
                    courseuseranswer.Section_Id = questionandanswermodel.getQuestionModel().getSection_Id();
                    courseuseranswer.Checklist_Id = questionandanswermodel.getLstCourse().get(0).getCourse_Id();

                }else {

                    courseuseranswer.Company_Id = String.valueOf(PreferenceUtils.getCompnayId(this));
                    courseuseranswer.Answer_Id = questionandanswermodel.getChoosenAnswerId();
                    courseuseranswer.User_Id = PreferenceUtils.getUserId(this);
                    courseuseranswer.Question_Id = String.valueOf(questionandanswermodel.getQuestionModel().getQuestion_Id());
                    courseuseranswer.Section_Id = questionandanswermodel.getQuestionModel().getSection_Id();
                    courseuseranswer.Checklist_Id = questionandanswermodel.getLstCourse().get(0).getCourse_Id();

                }
                courseuseranswerList.add(courseuseranswer);

            }else {

                if (questionandanswermodel.getChoosenAnswerId()!=null){

                    String [] choosenanswerid = questionandanswermodel.getChoosenAnswerId().split(",");

                    for (int i=0; i< choosenanswerid.length; i++){

                        CourseUserAnswers courseanswer = new CourseUserAnswers();
                        courseanswer.Company_Id = String.valueOf(PreferenceUtils.getCompnayId(this));
                        courseanswer.User_Id = PreferenceUtils.getUserId(this);
                        courseanswer.Answer_Id = choosenanswerid[i];
                        courseanswer.Question_Id = String.valueOf(questionandanswermodel.getQuestionModel().getQuestion_Id());
                        courseanswer.Section_Id = questionandanswermodel.getQuestionModel().getSection_Id();
                        courseanswer.Checklist_Id = questionandanswermodel.getLstCourse().get(0).getCourse_Id();
                        courseuseranswerList.add(courseanswer);
                    }
                }

            }

        }

        return courseuseranswerList;

    }



    private List<CourseUserAssessDet> getCourseAssetDetails(ArrayList<QuestionAndAnswerModel> questionandanswerlist) {

        List<CourseUserAssessDet> courseuserassetdetails = new ArrayList<>();

        for (QuestionAndAnswerModel questionanswermodel : questionandanswerlist) {

            CourseUserAssessDet courseuserasset = new CourseUserAssessDet();
            courseuserasset.Question_Type = questionanswermodel.getQuestionModel().Question_Type;
            courseuserasset.Company_Id = String.valueOf(PreferenceUtils.getCompnayId(this));
            if (questionanswermodel.getQuestionModel().getQuestion_Type() != 1) {

                if (questionanswermodel.getChoosenAnswer() != null){
                    courseuserasset.Count_of_User_Answers = 1;
                    courseuserasset.Is_Correct = true;
                    courseuserasset.Count_Of_User_Correct_Answers = 1;
                }else {
                    courseuserasset.Count_of_User_Answers = 0;
                    courseuserasset.Is_Correct = false;
                    courseuserasset.Count_Of_User_Correct_Answers = 0;
                }

                QuestionCourseListModel CourseHeader = questionanswermodel.getLstCourse().get(0);
                courseuserasset.Checklist_ID = CourseHeader.getCourse_Id();
                courseuserasset.User_Id = PreferenceUtils.getUserId(this);
                courseuserasset.Publish_ID = CourseHeader.getPublish_ID();
                courseuserasset.Section_Id = CourseHeader.getSection_Id();
                courseuserasset.Question_ID = String.valueOf(questionanswermodel.getQuestionModel().getQuestion_Id());
                courseuserasset.Couse_User_Section_Mapping_Id = CourseHeader.getChecklist_Section_User_Mapping_Id();
                courseuserasset.Checklist_User_Assignment_Id = CourseHeader.getChecklist_User_Mapping_Id();
                courseuserasset.Negative_Mark = questionanswermodel.getQuestionModel().getNegative_Mark();
                courseuserasset.Local_TimeZone = new Date().toString();
                courseuserasset.Offset_Value = CommonUtils.getUtcOffsetincluded10k();
                courseuserasset.Question_Remarks = questionanswermodel.getCommentText();
                courseuserasset.CheckList_Publish_Group_Id = ChecklistGroupId;

                if(questionanswermodel.getAttachmentURL()!= null) {
                    courseuserasset.Question_Remark_Url = questionanswermodel.getAttachmentURL();
                }
                courseuserasset.Checklist_Publish_Type = ChecklistPublishType;

                courseuserassetdetails.add(courseuserasset);

            } else {

                if (questionanswermodel.getChoosenAnswer() != null && questionanswermodel.getChoosenAnswer().length() > 0) {

                    String[] choosenanswerlist = questionanswermodel.getChoosenAnswer().split(",");
                    //String[] coreectanserlist = questionanswermodel.getCorrectAnswer().split(",");

                    if (choosenanswerlist!=null){
                        courseuserasset.Count_of_User_Answers = choosenanswerlist.length;
                        courseuserasset.Is_Correct = true;
                        courseuserasset.Count_Of_User_Correct_Answers =choosenanswerlist.length;
                    }
                }

                QuestionCourseListModel CourseHeader = questionanswermodel.getLstCourse().get(0);
                courseuserasset.Checklist_ID = CourseHeader.getCourse_Id();
                courseuserasset.User_Id = PreferenceUtils.getUserId(this);
                courseuserasset.Publish_ID = CourseHeader.getPublish_ID();
                courseuserasset.Section_Id = CourseHeader.getSection_Id();
                courseuserasset.Question_ID = String.valueOf(questionanswermodel.getQuestionModel().getQuestion_Id());
                courseuserasset.Couse_User_Section_Mapping_Id = CourseHeader.getChecklist_Section_User_Mapping_Id();
                courseuserasset.Checklist_User_Assignment_Id = CourseHeader.getChecklist_User_Mapping_Id();
                courseuserasset.Negative_Mark = questionanswermodel.getQuestionModel().getNegative_Mark();
                courseuserasset.Local_TimeZone = new Date().toString();
                courseuserasset.Offset_Value = CommonUtils.getUtcOffsetincluded10k();
                courseuserasset.Question_Remarks = questionanswermodel.getCommentText();
                courseuserasset.CheckList_Publish_Group_Id = ChecklistGroupId;

                if(questionanswermodel.getAttachmentURL()!= null) {
                    courseuserasset.Question_Remark_Url = questionanswermodel.getAttachmentURL();
                }
                courseuserasset.Checklist_Publish_Type = ChecklistPublishType;

                courseuserassetdetails.add(courseuserasset);

            }

        }

        return courseuserassetdetails;

    }

    private int getUserCorrectAnswer(QuestionAndAnswerModel questionanswermodel) {

        int correctanswercount = 0;


        if (questionanswermodel.getChoosenAnswer()!=null){

            String[] choosenanswer = questionanswermodel.getChoosenAnswer().split(",");
            String[] correctanswer = questionanswermodel.getCorrectAnswer().split(",");

            if (choosenanswer!=null && choosenanswer.length>0){

                for (int i=0; i<choosenanswer.length;i++){

                    String choosenanswerresult = choosenanswer[i];

                    for (int j=0;j<correctanswer.length;j++){
                        if (choosenanswerresult.equals(correctanswer[j])){
                            correctanswercount = correctanswercount+1;
                        }
                    }

                }

            }

        }

        return correctanswercount;
    }

    private String CalculatePercentage() {

        float totalquestion = questionandanswerlist.size();
        float correctanswer = 0;

        for (QuestionAndAnswerModel questionandanswermodel : questionandanswerlist){

            Log.d("==>correctanswer",""+questionandanswermodel.getCorrectAnswer());
            Log.d("==>choosenanswer",""+questionandanswermodel.getChoosenAnswer());

            if (questionandanswermodel.getQuestionModel().getQuestion_Type() != 1){


                if (questionandanswermodel.getChoosenAnswer()!=null && questionandanswermodel.getChoosenAnswer().length() > 0){

                    if (questionandanswermodel.getChoosenAnswer().equalsIgnoreCase(questionandanswermodel.getCorrectAnswer())){
                        correctanswer = correctanswer+1;
                    }else {

                        if (questionandanswermodel.getQuestionModel().isNegative()){

                            if (questionandanswermodel.getQuestionModel().getNegative_Mark()!= 0){

                                correctanswer = correctanswer - questionandanswermodel.getQuestionModel().getNegative_Mark();

                            }
                        }

                    }

                }

            }
            else {

                if (questionandanswermodel.getChoosenAnswer() != null && questionandanswermodel.getChoosenAnswer().length()>0){

                    String [] choosenanswerlist = questionandanswermodel.getChoosenAnswer().split(",");
                    String [] coreectanserlist = questionandanswermodel.getCorrectAnswer().split(",");
                    if (compareArrays(choosenanswerlist,coreectanserlist)){
                        correctanswer = correctanswer +1;
                    }else {

                        if (questionandanswermodel.getQuestionModel().isNegative()){

                            if (questionandanswermodel.getQuestionModel().getNegative_Mark()!= 0){

                                correctanswer = correctanswer - questionandanswermodel.getQuestionModel().getNegative_Mark();

                            }
                        }

                    }

                }

            }

        }

        Log.d("==>test",""+correctanswer);
        Log.d("==>total",""+totalquestion);
        float percentage ;


        if (correctanswer < 0 ){

            percentage = 0;

        }else {

            percentage = (correctanswer/totalquestion) * 100;
        }

        int per = (int) percentage;
        return String.valueOf(per);

    }

    public static boolean compareArrays(String[] arr1, String[] arr2) {
        HashSet<String> set1 = new HashSet<String>(Arrays.asList(arr1));
        HashSet<String> set2 = new HashSet<String>(Arrays.asList(arr2));
        return set1.equals(set2);
    }

    private void StartReportActiity() {

        Intent sectionQuestionActivity = new Intent(mContext, SectionsQuestionDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ChecklistId",ChecklistId);
        bundle.putSerializable("name",checklistName);
        bundle.putSerializable("CUMId",CUMId);
        bundle.putSerializable("PublishDate",PublishDate);
        bundle.putSerializable("ChecklistGroupId",ChecklistGroupId);
        bundle.putSerializable("isfromcoursechecklist",false);
        sectionQuestionActivity.putExtras(bundle);
        startActivity(sectionQuestionActivity);
        finish();
    }

    public void Checknetworkandupload(final AnwerUploadModel AnswerModelString, final boolean isLastQuestion, final boolean isTimeout, final boolean isBackpressed){

        if(NetworkUtils.checkIfNetworkAvailable(SurveyQuestionDetailsActivity.this)){
            if (!isBackpressed){
                showProgressDialog(getResources().getString(R.string.please_wait_progress));
            }
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            CheckListService chkService = retrofitAPI.create(CheckListService.class);
            int CompanyId  = PreferenceUtils.getCompnayId(this);
            String  SubdomainName = PreferenceUtils.getSubdomainName(this);
            int UserId = PreferenceUtils.getUserId(this);
            Gson gsonget = new Gson();
            AnwerUploadModel answermodel = AnswerModelString;
            String answermodelstring = new Gson().toJson(AnswerModelString);
            AnwerUploadModel answermodeltest = gsonget.fromJson(answermodelstring,AnwerUploadModel.class);
            Call call = chkService.InsertChecklistResponse(SubdomainName,CompanyId,UserId,QuestionLoadCount,isLastQuestion,isTimeout,answermodel);
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    String courseAssetListModel= response.body();
                    if (courseAssetListModel != null) {

                        if (!isBackpressed){
                            dismissProgressDialog();
                            if (courseAssetListModel.contains("COMPLETED")){
                                if(isdraftedbyuser){
                                    ShowAlert(getResources().getString(R.string.ChecklistdraftededSuccess),"",false,true,0);
                                }else {
                                    ShowAlert(getResources().getString(R.string.ChecklistSubmittedSuccess), "", false, true, 0);
                                }
                            }else {
                                ShowAlert(getResources().getString(R.string.errorsubmitingchecklist),getResources().getString(R.string.warning),false,false,2);
                                Log.d("error","error");
                            }

                        }
                        //COMPLETED~1~Your course has been partially submitted.~549
                    }else{
                        dismissProgressDialog();
                        ShowAlert(getResources().getString(R.string.errorsubmitingchecklist),getResources().getString(R.string.warning),false,false,2);
                        //finish();
                    }
                }
                @Override
                public void onFailure(Throwable t) {

                    //testResultRepository.insertTestRecord(new Gson().toJson(AnswerModelString),CalculatePercentage(),QuestionLoadCount,isLastQuestion+"",isTimeout+"");
                    ShowAlert(getResources().getString(R.string.errorsubmitingchecklist),getResources().getString(R.string.warning),false,false,2);
                    Log.d(t.toString(),"Error");
                }
            });
        }else{

            //testResultRepository.insertTestRecord(AnswerModelString,CalculatePercentage(),QuestionLoadCount,isLastQuestion+"",isTimeout+"");
            ShowAlert(getResources().getString(R.string.error_message),getResources().getString(R.string.warning),true,false,1);
        }
    }

    public void gotoUserSelection(final AnwerUploadModel AnswerModelString, final boolean isLastQuestion, final boolean isTimeout, final boolean isBackpressed){
        if(NetworkUtils.checkIfNetworkAvailable(SurveyQuestionDetailsActivity.this)){
            if (!isBackpressed){
                showProgressDialog(getResources().getString(R.string.please_wait_progress));
            }
            AnwerUploadModel answermodel = AnswerModelString;

            Bundle bundle = new Bundle();
            bundle.putSerializable("value", answermodel);
            bundle.putSerializable("ChecklistId",ChecklistId);
            bundle.putSerializable("isLastQuestion",isLastQuestion);
            bundle.putSerializable("isTimeout",isTimeout);
            bundle.putSerializable("isBackpressed",isBackpressed);
            bundle.putSerializable("QuestionLoadCount",QuestionLoadCount);
            bundle.putSerializable("selectedusers",selectedusers);
            Intent intentToQuestionActivity = new Intent(mContext, EvaluateUserforChecklistActivity.class);
            intentToQuestionActivity.putExtras(bundle);
            dismissProgressDialog();
            startActivity(intentToQuestionActivity);
            finish();
        }else{
            ShowAlert(getResources().getString(R.string.error_message),getResources().getString(R.string.warning),true,false,1);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        super.onPause();


    }

    public void loaditem(int position){
        multiPleQuestionPerPage.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void onSubmitAnswer(int position){


        if (position >= questionandanswerlist.size() ){

            OnQuestionFinished();

        }else {

            mQuestionPager.setCurrentItem(mQuestionPager.getCurrentItem()+1);

        }

    }

    private void OnQuestionFinished() {

        boolean isAnswereAllMandatoryQuestion = false;

        ArrayList<QuestionAndAnswerModel> AllsectionQuestionandAnswer = new ArrayList<>();
        ArrayList<QuestionAndAnswerModel> QAmodel = new ArrayList<>();
        answeredsecmodel = new ArrayList<>();
        for(ChkLstSectionsQuestionAnswerList sec:sectionslist){
            sec.getQuestionanswerList();
            AllsectionQuestionandAnswer.addAll(sec.getQuestionanswerList());
        }

        int noofquestionanswers = 1;
        boolean allquestionschecked = false;
        for (QuestionAndAnswerModel questionAndAnswerModel :AllsectionQuestionandAnswer){
            if(questionAndAnswerModel.questionModel.getIs_Required() == 1){
                if (questionAndAnswerModel.getChoosenAnswer()!=null && questionAndAnswerModel.getChoosenAnswer().length() > 0){
                    isAnswereAllMandatoryQuestion = true;
                }else {
                    isAnswereAllMandatoryQuestion = false;
                    break;
                }
            }
            noofquestionanswers ++;
        }


        if (noofquestionanswers - 1 == AllsectionQuestionandAnswer.size()) {
            allquestionschecked = true;
        }

        boolean answeredone = false;
        if(allquestionschecked) {
            for (QuestionAndAnswerModel answered : AllsectionQuestionandAnswer) {
                if (answered.getChoosenAnswer() != null || answered.getChoosenAnswerId() != null) {
                    answeredone = true;
                }
            }
            isAnswereAllMandatoryQuestion = true;
        }

        boolean addsec = false;
        ArrayList<QuestionAndAnswerModel> OnlyAnsweredQuestions = new ArrayList<>();
        for(ChkLstSectionsQuestionAnswerList sec:sectionslist){
            addsec = false;
            for(QuestionAndAnswerModel qa :sec.getQuestionanswerList()){
                if(qa.getChoosenAnswer()!= null && !qa.getChoosenAnswer().isEmpty()){
                    QAmodel.add(qa);
                    addsec = true;
                }
            }
            if(addsec) {
                answeredsecmodel.add(sec);
            }
        }
        OnlyAnsweredQuestions.addAll(QAmodel);

        if (isAnswereAllMandatoryQuestion && answeredone && noofquestionanswers-1 == AllsectionQuestionandAnswer.size()){
            showConfirmationPopup(OnlyAnsweredQuestions,getResources().getString(R.string.confirmsubmitchecklist));
            //Toast.makeText(mContext,"selected one",Toast.LENGTH_LONG).show();
        }else {
            if(!isAnswereAllMandatoryQuestion) {
                ShowAlert(getResources().getString(R.string.mandatoryMessage), getResources().getString(R.string.warning), false, false, 1);
            }else if(!answeredone){
                ShowAlert(getResources().getString(R.string.answeratleastone), getResources().getString(R.string.warning), false, false, 1);
            }
        }
    }


    public void showProgressDialog(String message) {
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


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_submit_mutiple:

                if (questionandanswerlist.size()>0){
                    OnQuestionFinished();
                }

                break;

            case R.id.btn_disable_submit_mutiple:
                Toast.makeText(this,getResources().getString(R.string.kindlyack),Toast.LENGTH_SHORT).show();
                break;

            case R.id.companylogo:
                onBackPressed();
                break;

            case R.id.btn_cancel:
                onBackPressed();
                break;

            case R.id.btn_draft:
                saveAsDraft();
                break;

            default:

                break;

        }

    }

    public void saveAsDraft(){
        isdraftedbyuser = true;
        ArrayList<QuestionAndAnswerModel> AllsectionQuestionandAnswer = new ArrayList<>();
        ArrayList<QuestionAndAnswerModel> QAmodel = new ArrayList<>();
        answeredsecmodel = new ArrayList<>();
        for(ChkLstSectionsQuestionAnswerList sec:sectionslist){
            sec.getQuestionanswerList();
            AllsectionQuestionandAnswer.addAll(sec.getQuestionanswerList());
        }

        int noofquestionanswers = 1;
        boolean allquestionschecked = false;
        for (QuestionAndAnswerModel questionAndAnswerModel :AllsectionQuestionandAnswer){
            noofquestionanswers ++;
        }


        if (noofquestionanswers - 1 == AllsectionQuestionandAnswer.size()) {
            allquestionschecked = true;
        }

        boolean answeredone = false;
        if(allquestionschecked) {
            for (QuestionAndAnswerModel answered : AllsectionQuestionandAnswer) {
                if (answered.getChoosenAnswer() != null || answered.getChoosenAnswerId() != null) {
                    answeredone = true;
                }
            }
        }

        boolean addsec = false;
        ArrayList<QuestionAndAnswerModel> OnlyAnsweredQuestions = new ArrayList<>();
        for(ChkLstSectionsQuestionAnswerList sec:sectionslist){
            addsec = false;
            for(QuestionAndAnswerModel qa :sec.getQuestionanswerList()){
                if(qa.getChoosenAnswer()!= null && !qa.getChoosenAnswer().isEmpty()){
                    QAmodel.add(qa);
                    addsec = true;
                }
            }
            if(addsec) {
                answeredsecmodel.add(sec);
            }
        }
        OnlyAnsweredQuestions.addAll(QAmodel);

        if (answeredone && noofquestionanswers-1 == AllsectionQuestionandAnswer.size()){
            showConfirmationPopup(OnlyAnsweredQuestions,getResources().getString(R.string.confirmdraftchecklist));
        }else if(!answeredone){
            ShowAlert(getResources().getString(R.string.answeratleastone), getResources().getString(R.string.warning), false, false, 1);
        }
    }

    public void showConfirmationPopup(final ArrayList<QuestionAndAnswerModel> questionsectionQuestionandAnswer,final String message){
        if(isfromcclst){
            if(isdraftedbyuser){
                messageDialog.showChecklistConfirmationPopup(mContext,message,new View.OnClickListener() {
                    @Override
                    public void onClick(View Approve) {
                        messageDialog.dialogDismiss();
                        OnQuestionDraftedCusChklstEvent(questionsectionQuestionandAnswer);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View close) {
                        messageDialog.dialogDismiss();
                        //finish();
                    }
                }, false);
            }else {
                OnQuestionFinishedCusChklstEvent(questionsectionQuestionandAnswer);
            }
        }else {
            messageDialog.showChecklistConfirmationPopup(mContext, message, new View.OnClickListener() {
                @Override
                public void onClick(View Approve) {
                    messageDialog.dialogDismiss();
                    OnQuestionFinishedEvent(questionsectionQuestionandAnswer);
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View close) {
                    messageDialog.dialogDismiss();
                    //finish();
                }
            }, false);
        }
    }

    private void OnQuestionFinishedCusChklstEvent(ArrayList<QuestionAndAnswerModel> questionsectionQuestionandAnswer) {
        if (questionsectionQuestionandAnswer.size()>0){
            gotoUserSelection(UploadAnswerProcess(questionsectionQuestionandAnswer),true,false, false);
        }
    }

    private void OnQuestionDraftedCusChklstEvent(ArrayList<QuestionAndAnswerModel> questionsectionQuestionandAnswer) {
        if (questionsectionQuestionandAnswer.size()>0){
            Checknetworkandupload(draftSelectedUserList(UploadAnswerProcess(questionsectionQuestionandAnswer),false),true,false, false);
        }
    }

    public AnwerUploadModel draftSelectedUserList(final AnwerUploadModel AnswerModelString, final boolean isBackpressed){
        AnwerUploadModel answermodel = AnswerModelString;
        if (!isBackpressed){
            showProgressDialog(getResources().getString(R.string.please_wait_progress));
        }
        List<UserforCourseChecklist> courseuserassetdetails = new ArrayList<>();
        for (UserforCourseChecklist questionanswermodel : selectedusers) {
            UserforCourseChecklist courseuserasset = new UserforCourseChecklist();
            courseuserasset.User_Id = questionanswermodel.getUser_Id();
            courseuserasset.Course_Id = questionanswermodel.getCourse_Id();
            courseuserasset.Section_Id = questionanswermodel.getSection_Id();
            courseuserasset.Company_Id = PreferenceUtils.getCompnayId(this);
            courseuserasset.CheckList_id = ChecklistId;
            courseuserasset.CourseCheckListDraft = "1";
            courseuserasset.Evaluation_Status = "0";
            courseuserasset.Checklist_Result_Status = "";
            courseuserasset.setChecklist_Restart(false);
            courseuserassetdetails.add(courseuserasset);
        }
        answermodel.setUserlistsobj(courseuserassetdetails);
        return answermodel;
    }


    //File Upload methods
    public void camerapermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = ContextCompat.checkSelfPermission(SurveyQuestionDetailsActivity.this, android.Manifest.permission.CAMERA);
            int storagePermission = ContextCompat.checkSelfPermission(SurveyQuestionDetailsActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission == PackageManager.PERMISSION_GRANTED && storagePermission == PackageManager.PERMISSION_GRANTED) {

            } else if (permission == PackageManager.PERMISSION_GRANTED && storagePermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
            } else if (permission != PackageManager.PERMISSION_GRANTED && storagePermission == PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA, READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
            }
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 1) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    } else {
                        if (grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(mContext, "Camera and storage access permission denied", Toast.LENGTH_LONG).show();
                        } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(mContext, "Storage access permission denied", Toast.LENGTH_LONG).show();
                        } else if (grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(mContext, "Camera access permission denied", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    } else {
                        Toast.makeText(mContext, "Permission denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case REQUEST_STORAGE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setImageIntent(false);
                } else {
                    Toast.makeText(mContext, "Permission denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void TakeVideoIntent(QuestionAndAnswerModel model,int position) {
        if(model != null) {
            attachmentmodel = model;
            questionsadapterpos = position;
        }else{
            attachmentmodel = null;
        }
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            //fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            long maxVideoSize = 20*1024*1024; // 20 MB
            takeVideoIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, maxVideoSize);
            /*MediaRecorder recorder = new MediaRecorder();
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);*/
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    public void takePhoto(QuestionAndAnswerModel model,int position) {
        if(model != null) {
            attachmentmodel = model;
            questionsadapterpos = position;
        }else{
            attachmentmodel = null;
        }
        if(Build.VERSION.SDK_INT >= 24){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    return;
                }
                if (photoFile != null) {
                    Uri photoURI = null;
                    photoURI = FileProvider.getUriForFile(this, this.getApplicationContext()
                            .getPackageName() + ".provider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }else{
            try {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File photo = createImageFile();
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photo));
                mCurrentPhotoPath = photo.getPath();
                //model.setAttachmentURL(mCurrentPhotoPath);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String appDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getString(R.string.app_name)
                + File.separator +Constants.IMAGES;
        File storageDir = new File(appDir);
        if (!storageDir.exists()) {
            boolean out = storageDir.mkdirs();
            if (!out) {
                throw new IOException("Unable to make directory, error occurred.");
            }
        }
        File image = File.createTempFile(
                getGeneratedFileName(this), DEFAULT_IMAGE_EXTENSION, storageDir
        );

        mCurrentPhotoPath = image.getPath();
        return image;
    }

    public static String getGeneratedFileName(Context context) {
        //String user = String.valueOf(PreferenceUtils.getUserId(context));
        String user = "Img_Kangle";
        return user;
    }

    public void browsePhoto(QuestionAndAnswerModel model,int position) {
        if(model != null) {
            attachmentmodel = model;
            questionsadapterpos = position;
        }else{
            attachmentmodel = null;
        }
        if (Build.VERSION.SDK_INT < 19) {
            setImageIntent(true);
        } else if (Build.VERSION.SDK_INT >= 23) {
            int permission = checkSelfPermission(READ_EXTERNAL_STORAGE);
            if (permission == PackageManager.PERMISSION_GRANTED) {
                setImageIntent(false);
            } else {
                requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
            }
        } else {
            setImageIntent(false);
        }
    }

    private void setImageIntent(boolean versionBelow19) {
        Intent intent = new Intent();
        intent.setType("*/*");
        if (versionBelow19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        startActivityForResult(intent, REQUEST_IMAGE_BROWSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE: {
                    onTakingPhoto(data);
                    break;
                }
                case REQUEST_IMAGE_BROWSE: {
                    onBrowsingPhoto(data);
                    break;
                }
                case REQUEST_VIDEO_CAPTURE: {
                    onTakingVideo(data);
                    break;
                }
                case TASK_LIST_CODE: {
                    gettingCheckListTaskListData(data);
                    break;
                }
                case NEW_TASK_CREATION: {
                    addnewCreatedTaskToList(data);
                    break;
                }
                case OVERALL_VIEW_TASK_LIST: {
                    overallTaskListData(data);
                    break;
                }
                case NEW_OVERALL_TASK_CREATION: {
                    addnewoverallTaskToList(data);
                    break;
                }
            }
        }
    }

    public void gettingCheckListTaskListData(Intent data)
    {
        ArrayList<TaskListModel> updatedList = (ArrayList<TaskListModel>)data.getSerializableExtra("UpdatedList");
        gettingCheckListTaskList.addAll(updatedList);
    }

    public void addnewCreatedTaskToList(Intent data){
        TaskListModel updatedList = (TaskListModel) data.getSerializableExtra("CreatedTask");
        gettingCheckListTaskList.add(updatedList);
    }

    public void overallTaskListData(Intent data)
    {
        ArrayList<TaskListModel> updatedList = (ArrayList<TaskListModel>)data.getSerializableExtra("UpdatedList");
        overallTaskList.addAll(updatedList);
    }

    public void addnewoverallTaskToList(Intent data){
        TaskListModel updatedList = (TaskListModel) data.getSerializableExtra("CreatedTask");
        overallTaskList.add(updatedList);
    }


    public void onTakingPhoto(Intent data) {
        setImageThumbnail(mCurrentPhotoPath);
    }

    public void onTakingVideo(Intent data) {
        Uri videoUri = data.getData();
        videoView.setVideoURI(videoUri);
        mCurrentPhotoPath = videoUri.getPath();
        setImageThumbnail(mCurrentPhotoPath);
        //setImageThumbnail(mCurrentPhotoPath);
    }

    public void onBrowsingPhoto(Intent data) {
        if (data != null) {
            String path = FileUtils.getPath(this, data.getData());
            setImageThumbnail(path);
        }
    }

    public void setImageThumbnail(String imagePath) {
        if(imagePath != null){
            mCurrentPhotoPath = imagePath;
//        Ion.with(this).load(imagePath).intoImageView(mHolder.thumbnailPreview);
            Bitmap bitmapImage = BitmapFactory.decodeFile(mCurrentPhotoPath);
            if(bitmapImage != null){
                int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
                //mHolder.thumbnailPreview.setImageBitmap(scaled);
                //mHolder.thumbnailPreview.setVisibility(View.VISIBLE);
                compressImage(mCurrentPhotoPath);
            }else{
                String fileURI = mCurrentPhotoPath;
                File file = new File(fileURI);
                long fileSizeInKB = file.length() / 1024;
                if(imagePath.endsWith(".3gp") || imagePath.endsWith(".mp4") || imagePath.endsWith(".pdf")){
                    if(imagePath.endsWith(".pdf")){
                        if (imagePath != null && fileSizeInKB > 0) {
                            if(fileSizeInKB > 10000){
                                Toast.makeText(getApplicationContext(),"File size exceeding 10Mb",Toast.LENGTH_LONG).show();
                            }else{
                                uploadToServer(mCurrentPhotoPath);
                            }
                        }
                    } else {
                        if (imagePath != null && fileSizeInKB > 0) {
                            if (fileSizeInKB > 20000) {
                                Toast.makeText(getApplicationContext(), "File size exceeding 20Mb", Toast.LENGTH_LONG).show();
                            } else {
                                uploadToServer(mCurrentPhotoPath);
                            }
                        }
                    }
                }else{
                    //Toast.makeText(getApplicationContext(),"File not Supported",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        compressedImagePath = null;
        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            compressedImagePath = filename;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        uploadToServer(compressedImagePath);
        return filename;
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public void getImageFileSize(String imagePath) {

        if (imagePath != null && !TextUtils.isEmpty(imagePath)) {
            mCurrentPhotoPath = imagePath;
            File imageFile = new File(mCurrentPhotoPath);
            if (imageFile.exists()) {
                imageSizeInBytes = imageFile.length();
                Log.d("Image size in bytes", imageSizeInBytes + "");
                imageSizeInKb = imageSizeInBytes / 1024;
                Log.d("Image size in kb", imageSizeInKb + "");
            } else {
                Log.d("Image File", "File does not exists");
            }
        }
    }

    public void uploadToServer(String compressedImagePath){
        if(NetworkUtils.checkIfNetworkAvailable(SurveyQuestionDetailsActivity.this)) {
            showProgressDialog(getResources().getString(R.string.imgupload));
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            CheckListService chkService = retrofitAPI.create(CheckListService.class);
            int CompanyId = PreferenceUtils.getCompnayId(this);
            String SubdomainName = PreferenceUtils.getSubdomainName(this);
            int UserId = PreferenceUtils.getUserId(this);
            final int section_id,question_id;
            if(attachmentmodel != null) {
                section_id = attachmentmodel.getQuestionModel().Section_Id;
                question_id = attachmentmodel.getQuestionModel().Question_Id;
            }else{
                section_id = sectionslist.get(position).getLstCourse().getSection_Id();
                question_id = 0;
            }

            String fileURI = compressedImagePath;
            File file = new File(fileURI);
            if (file.exists()) {
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
                RequestBody localFileName = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                Call call = chkService.uploadFile(SubdomainName, CompanyId, UserId, ChecklistId, section_id,
                        question_id, fileBody, localFileName);
                call.enqueue(new Callback<ChecklistFileUploadResult>() {

                    @Override
                    public void onResponse(Response<ChecklistFileUploadResult> response, Retrofit retrofit) {
                        ChecklistFileUploadResult URL = response.body();
                        if (URL != null) {
                            dismissProgressDialog();
                            if(attachmentmodel != null) {
                                attachmentmodel.setAttachmentURL(URL.FileUri);
                                multiPleQuestionPerPage.notifyDataSetChanged();
                                //multiPleQuestionPerPage.notifyItemChanged(questionsadapterpos);
                            }else{
                                sectionslist.get(position).getLstCourse().setOverallAttachmentURL(URL.FileUri);
                                loadattachmentList(URL.FileUri);
                            /*Ion.with(attachmentfile).fitXY().placeholder(R.drawable.icon_jpeg).fitXY().error(R.drawable.icon_jpeg).fitXY().load(
                                    (!TextUtils.isEmpty(URL.FileUri)) ?
                                            URL.FileUri : URL.FileUri);*/
                            }
                        } else {
                            dismissProgressDialog();
                            ShowAlert(getResources().getString(R.string.erroruploadingImage), getResources().getString(R.string.warning), false, false,1);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dismissProgressDialog();
                        //testResultRepository.insertTestRecord(new Gson().toJson(AnswerModelString),CalculatePercentage(),QuestionLoadCount,
                        ShowAlert(getResources().getString(R.string.erroruploadingImage), getResources().getString(R.string.warning), false, false,1);
                        Log.d(t.toString(), "Error");
                    }
                });
            }
        }else{
            ShowAlert(getResources().getString(R.string.error_message),getResources().getString(R.string.warning),true,false,1);
        }
    }

    public void loadattachmentList(String Url){
        Acknowledgement_urls urls = new Acknowledgement_urls();
        urls.setUrl(Url);
        ackurls.add(urls);
        reloadAdapter(ackurls);
    }

    public void reloadAdapter(List<Acknowledgement_urls> urls){
        if(ackurls.size() < 4) {
            if(ackurls.size() == 3){
                addattachment.setVisibility(View.GONE);
            }else{
                addattachment.setVisibility(View.VISIBLE);
            }
            adapter = new SurveyAttachmentListItemRecyclerAdapter(this, urls);
            attachmentfilerecycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
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


    @Override
    public void onStartSeek(long position) {

    }

    @Override
    public void onStopSeek(long position) {

    }

    @Override
    protected void onStop() {
        super.onStop();
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

    //Task Module
    public void gotoChecklistViewTaskListActivity(QuestionAndAnswerModel questionAndAnswerModel)
    {
        ArrayList<TaskListModel> questionRlTasklist = new ArrayList<>();
        if(gettingCheckListTaskList != null && gettingCheckListTaskList.size() > 0)
        {
            for(TaskListModel taskListModel : gettingCheckListTaskList)
            {
                if(taskListModel.getSource_Qustionid() == questionAndAnswerModel.getQuestionModel().getQuestion_Id()){
                    questionRlTasklist.add(taskListModel);
                }
            }
        }

        if(questionRlTasklist != null && questionRlTasklist.size() > 0)
        {
            for(TaskListModel taskListModel : questionRlTasklist)
            {
                gettingCheckListTaskList.remove(taskListModel);

            }
        }

        Bundle bundle = new Bundle();
        Intent intent = new Intent(mContext, ChecklistViewTaskListActivity.class);
        bundle.putSerializable("FilterRelatedTaskList", questionRlTasklist);
        if(isfromcclst) {
            bundle.putSerializable("isfromcoursechecklist",isfromcclst);
            bundle.putSerializable("Userlist",selectedusers);
        }
        bundle.putSerializable("fromquestion",true);
        bundle.putSerializable("courseId",questionAndAnswerModel.getQuestionModel().getCourse_Id());
        bundle.putSerializable("questionId",questionAndAnswerModel.getQuestionModel().getQuestion_Id());
        bundle.putSerializable("sectionId",questionAndAnswerModel.getQuestionModel().getSection_Id());
        bundle.putSerializable("checklistId", ChecklistId);
        intent.putExtra("ChecklistGroupId",ChecklistGroupId);
        intent.putExtra("isGroupChecklist",isGroupChecklist);
        intent.putExtras(bundle);
        startActivityForResult(intent, TASK_LIST_CODE);

    }

    public void gotoCreateTaskActivity(QuestionAndAnswerModel questionAndAnswerModel)
    {
        Intent intent= new Intent(mContext, CreateTaskActivity.class);
        if(isfromcclst) {
            intent.putExtra("isfromcoursechecklist",isfromcclst);
            intent.putExtra("Userlist",selectedusers);
        }
        intent.putExtra("fromquestion",true);
        intent.putExtra("isFromTaskEdit",false);
        intent.putExtra("ChecklistGroupId",ChecklistGroupId);
        intent.putExtra("isGroupChecklist",isGroupChecklist);
        intent.putExtra("courseId",questionAndAnswerModel.getQuestionModel().getCourse_Id());
        intent.putExtra("questionId",questionAndAnswerModel.getQuestionModel().getQuestion_Id());
        intent.putExtra("sectionId",questionAndAnswerModel.getQuestionModel().getSection_Id());
        intent.putExtra("checklistId", ChecklistId);
        startActivityForResult(intent,NEW_TASK_CREATION);
    }


    public void gotoOverallViewTaskListActivity(int Course_Id) {
        ArrayList<TaskListModel> questionRlTasklist = new ArrayList<>();
        if(overallTaskList != null && overallTaskList.size() > 0) {
            for(TaskListModel taskListModel : overallTaskList) {
                questionRlTasklist.add(taskListModel);
            }
        }

        if(questionRlTasklist != null && questionRlTasklist.size() > 0) {
            for(TaskListModel taskListModel : questionRlTasklist) {
                overallTaskList.remove(taskListModel);
            }
        }

        Bundle bundle = new Bundle();
        Intent intent = new Intent(mContext, ChecklistViewTaskListActivity.class);
        bundle.putSerializable("FilterRelatedTaskList", questionRlTasklist);
        if(isfromcclst) {
            bundle.putSerializable("isfromcoursechecklist",isfromcclst);
            bundle.putSerializable("Userlist",selectedusers);
        }
        bundle.putSerializable("fromquestion",true);
        bundle.putSerializable("courseId",Course_Id);
        bundle.putSerializable("questionId",0);
        bundle.putSerializable("sectionId",0);
        bundle.putSerializable("checklistId", ChecklistId);
        intent.putExtra("ChecklistGroupId",ChecklistGroupId);
        intent.putExtras(bundle);
        startActivityForResult(intent, OVERALL_VIEW_TASK_LIST);
    }

    public void gotoOverallCreateTaskActivity(int Course_Id)
    {
        Intent intent= new Intent(mContext, CreateTaskActivity.class);
        if(isfromcclst) {
            intent.putExtra("isfromcoursechecklist",isfromcclst);
            intent.putExtra("Userlist",selectedusers);
        }
        intent.putExtra("fromquestion",true);
        intent.putExtra("isFromTaskEdit",false);
        intent.putExtra("ChecklistGroupId",ChecklistGroupId);
        intent.putExtra("courseId",Course_Id);
        intent.putExtra("questionId",0);
        intent.putExtra("sectionId",0);
        intent.putExtra("checklistId", ChecklistId);
        startActivityForResult(intent,NEW_OVERALL_TASK_CREATION);
    }

    private void addQuestiontypeMatch() {

        QuestionAndAnswerModel dummymodel = new QuestionAndAnswerModel();
        QuestionQuestionListModel question = new QuestionQuestionListModel();
        question.setActive(true);
        question.setDisplay_Order(4);
        question.setQuestion_Type(9);
        question.setQuestion_Hint("match the following");
        question.setQuestion_Text("How satisfied or dissatisfied are you with each of the following ?");
        question.setQuestion_Id(1224);
        question.setQuestion_Description("Description");
        dummymodel.setQuestionModel(question);
        questionandanswerlist.add(dummymodel);
    }
}
