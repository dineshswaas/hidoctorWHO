package com.swaas.kangle.TaskModule;

import android.Manifest;
import android.app.Activity;
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
import android.graphics.PorterDuff;
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
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.swaas.kangle.CheckList.CheckListService;
import com.swaas.kangle.CheckList.model.Acknowledgement_urls;
import com.swaas.kangle.CheckList.model.ChecklistFileUploadResult;
import com.swaas.kangle.CheckList.model.UserforCourseChecklist;
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
import com.swaas.kangle.utils.DateHelper;
import com.swaas.kangle.utils.FileUtils;
import com.swaas.kangle.utils.MessageDialog;
import com.swaas.kangle.utils.NetworkUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class CreateTaskActivity extends AppCompatActivity implements SeekbarChangedListener, ExoPlayer.EventListener, OnLoadCompleteListener, OnPdfDownload {
    Context mContext;
    ImageView backbutton,attachment;
    EditText tsk_title,tsk_description,tsk_duedays,tsk_remarks;
    TextView tsk_title_text,tsk_description_text,tsk_category,tsk_duedays_text,tsk_priority_text,
            tsk_assignee_text,tsk_attachments_text,tsk_remarks_text,tsk_notify_text;
    TextView show_users;
    RelativeLayout header;
    TextView close_tsk,submit_tsk;
    Spinner cat_spinner,tsk_priority;
    EmptyRecyclerView tsk_attachments_listrecycler,userlist;
    LinearLayoutManager LL1,LL2;
    CheckBox check_single,check_trainee,check_manager;
    RadioButton trainee_rbt,self_rbt;
    ArrayList<TaskListModel> childUserListModel = new ArrayList<>();
    ArrayList<UserforCourseChecklist> usersList = new ArrayList<>();
    Serializable coursechecklistuserlist = new ArrayList<>();
    ArrayList<TaskListModel> selectedlist ;
    TaskListModel submitlist ;
    ArrayList<TaskListModel> temp = new ArrayList<TaskListModel>();
    ProgressDialog mProgressDialog;
    User userobj;
    Gson gsonget;
    String compressedImagePath;
    ImageView attachmentfile,addattachment;
    View attachmentlayout,overall;
    long imageSizeInBytes;
    long imageSizeInKb;
    MessageDialog messageDialog;
    LinearLayoutManager layoutManager;
    public static final String DEFAULT_IMAGE_EXTENSION = ".jpg";
    private String mCurrentPhotoPath;
    List<Acknowledgement_urls> ackurls;
    boolean isfromQuestion,isfromcoursechecklist;
    int GroupChecklistId;

    public static final int REQUEST_IMAGE_CAPTURE = 4;
    public static final int REQUEST_IMAGE_BROWSE = 2;
    static final int REQUEST_VIDEO_CAPTURE = 3;
    public static final int REQUEST_CAMERA_PERMISSION = 120;
    public static final int REQUEST_STORAGE_PERMISSION = 121;
    VideoView videoView;
    EmptyRecyclerView attachmentfilerecycler;
    SimpleExoPlayerView simpleExoPlayerView;
    AttachmentListItemRecyclerAdapter adapter;
    String tasktitle,taskdesc,taskduedays,taskremarks,taskpriroties,taskcategory,tasknotify,taskassigned,taskemp,taskmanger,notify,assignedUserIDs,assignedUsernames;
    int checklistId, sectionId, questionId, courseId;
    TaskListModel editTask;
    boolean viewlist=false;
    boolean isFromTaskEdit = false,isGroupChecklist;

    public static ArrayList<TaskListModel> userslistfinal;
    public static ArrayList<TaskListModel> selecteduser;
    ImageView close,delete;
    public int attachmentposition;
    WebView helpView;

    PDFView pdf_player;
    DownloadPdfAysnc downloadPdfAysnc;
    View imagelayout;
    final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    DataSource.Factory mediaDataSourceFactory;
    EventLogger eventLogger;
    public Handler mainHandler;
    SimpleExoPlayer player;
    LinearLayout bottomsection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        mContext = CreateTaskActivity.this;
        gsonget=new Gson();
        userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);
        ackurls = new ArrayList<>();
        initializeviews();
        setuptheme();
        setuprecycleradapter();
        onClicklistener();
        messageDialog = new MessageDialog(mContext);
        camerapermission();
        setupRecyclerView();
        if(getIntent()!= null) {
            isFromTaskEdit = getIntent().getBooleanExtra("isFromTaskEdit",false);
            isfromQuestion = getIntent().getBooleanExtra("fromquestion",false);
            isfromcoursechecklist = getIntent().getBooleanExtra("isfromcoursechecklist", false);
            if(isfromQuestion){
                editTask = (TaskListModel) getIntent().getSerializableExtra("editTask");
                GroupChecklistId = getIntent().getIntExtra("ChecklistGroupId", 0);
                isGroupChecklist = getIntent().getBooleanExtra("isGroupChecklist", false);
                checklistId = getIntent().getIntExtra("checklistId", 0);
                questionId = getIntent().getIntExtra("questionId", 0);
                sectionId = getIntent().getIntExtra("sectionId", 0);
                courseId = getIntent().getIntExtra("courseId", 0);
            }else{
                GroupChecklistId = 0;checklistId = 0;sectionId = 0;questionId = 0;courseId = 0;isGroupChecklist = false;
            }
            if(isfromcoursechecklist){
                usersList = (ArrayList<UserforCourseChecklist>) getIntent().getSerializableExtra("Userlist");
                if(getIntent().getSerializableExtra("Userlist")!=null) {
                    childUserListModel= new ArrayList<TaskListModel>();
                    for (UserforCourseChecklist coursechecklistuserlist : usersList) {
                        TaskListModel tempmodel= new TaskListModel();
                        tempmodel.setUser_Name(coursechecklistuserlist.getUser_Name());
                        tempmodel.setUser_Id(coursechecklistuserlist.getUser_Id());
                        tempmodel.setCourse_Id(coursechecklistuserlist.getCourse_Id());
                        tempmodel.setCourse_Section_Id(coursechecklistuserlist.getSection_Id());
                        tempmodel.setChecklist_Id(coursechecklistuserlist.getCheckList_id());
                        childUserListModel.add(tempmodel);
                    }
                }
            }else{
                getChildUserList();
            }
            if(editTask != null) {
                loadDatas();
                viewlist =true;
            }
        }

    }

    public void loadDatas()
    {   String userId= String.valueOf(PreferenceUtils.getUserId(mContext));
        self_rbt.setChecked(false);
        trainee_rbt.setChecked(false);
        check_single.setChecked(false);
        check_trainee.setChecked(false);
        check_manager.setChecked(false);
        tsk_title.setText(editTask.getTask_Name());
        tsk_description.setText(editTask.getTask_Description());
        tsk_remarks.setText(editTask.getTask_Remarks());
        tsk_duedays.setText(String.valueOf(editTask.getDuedays()));
        int spinnerPosition = editTask.getTask_Priority();
        tsk_priority.setSelection(spinnerPosition-1);
        if(userId.equals(editTask.getAssigned_User_ID()))
        {
            self_rbt.setChecked(true);
            check_trainee.setEnabled(false);
            check_manager.setEnabled(false);
            check_manager.setChecked(false);
            check_trainee.setChecked(false);        }
        else
        {
            trainee_rbt.setChecked(true);
            show_users.setVisibility(View.VISIBLE);
            if(editTask.getAssigned_User_ID()!=null && editTask.getAssigned_User_ID().length()>0)
            {
                selectedlist = new ArrayList<>();
                String str =editTask.getAssigned_User_ID();
                if(str.contains(",")) {
                    List<String> userIds = Arrays.asList(str.split(","));

                    for(int i=0;i<userIds.size();i++)
                    {
                        TaskListModel temp1=new TaskListModel();
                        temp1.setUser_Id(Integer.parseInt(userIds.get(i)));
                        selectedlist.add(temp1);
                        final SpannableString content = new SpannableString(selectedlist.size()+" "+getResources().getString(R.string.users_selected_msg));
                        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                        show_users.setText(content);
                    }
                }
                else
                {
                    TaskListModel temp1=new TaskListModel();
                    temp1.setUser_Id(Integer.parseInt(str));
                    selectedlist.add(temp1);
                    final SpannableString content = new SpannableString(selectedlist.size()+" "+getResources().getString(R.string.users_selected_msg));
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    show_users.setText(content);
                }
            }
        }
        if(editTask.getNotify_Intimation_type() != null){
            if(editTask.getNotify_Intimation_type().toLowerCase().contains("1")) {
                check_single.setChecked(true);
            }
            if(editTask.getNotify_Intimation_type().toLowerCase().contains("2")) {
                check_trainee.setChecked(true);
            }
            if(editTask.getNotify_Intimation_type().toLowerCase().contains("3")) {
                check_manager.setChecked(true);
            }
        }

        if(editTask.getAttachment_urls_list()!=null&&editTask.getAttachment_urls_list().size()>0)
        {
            ackurls = editTask.getAttachment_urls_list();
            reloadAdapter(editTask.getAttachment_urls_list());
        }
    }

    public void reloadAdapter(List<Acknowledgement_urls> urls){
        if(ackurls.size() < 4) {
            if(ackurls.size() == 3){
                attachment.setVisibility(View.GONE);
            }else{
                attachment.setVisibility(View.VISIBLE);
            }
            adapter = new AttachmentListItemRecyclerAdapter(CreateTaskActivity.this, (ArrayList<Acknowledgement_urls>) urls,true);
            attachmentfilerecycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public void loadViewbasedonURL(Acknowledgement_urls urls,int position){
        attachmentposition = position;
        if(urls.getUrl().endsWith(".png") || urls.getUrl().endsWith(".jpg")){
            String url = urls.getUrl();
            imagelayout.setVisibility(View.GONE);
            bottomsection.setVisibility(View.VISIBLE);
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

    public void LoadOnlinePdf(String pdfpath){
        videoView.setVisibility(View.GONE);
        pdf_player.setVisibility(View.VISIBLE);
        pdf_player.useBestQuality(false);
        pdf_player.fromFile(new File(pdfpath)).defaultPage(0)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .load();
    }

    public void loadnewPDFView(String URL) {
        imagelayout.setVisibility(View.VISIBLE);
        bottomsection.setVisibility(View.GONE);
        File file = new File(URL);
        if (NetworkUtils.isNetworkAvailable()) {
            downloadPdfAysnc =  new DownloadPdfAysnc(mContext,this);
            downloadPdfAysnc.execute(URL, file.getName());
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
        bottomsection.setVisibility(View.GONE);

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


    public void setupRecyclerView(){
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        attachmentfilerecycler.setLayoutManager(layoutManager);
    }
    public void initializeviews(){
        backbutton = (ImageView) findViewById(R.id.backbutton);
        header = (RelativeLayout)findViewById(R.id. header);
        tsk_title = (EditText)findViewById(R.id.tsk_title);
        tsk_description = (EditText)findViewById(R.id.tsk_description);
        tsk_duedays = (EditText)findViewById(R.id.tsk_duedays);
        tsk_remarks = (EditText)findViewById(R.id.tsk_remarks);
        overall=findViewById(R.id.overall);

        tsk_title_text = (TextView)findViewById(R.id.tsk_title_text);
        tsk_description_text = (TextView)findViewById(R.id.tsk_description_text);
        tsk_category = (TextView)findViewById(R.id.tsk_category);
        tsk_duedays_text = (TextView)findViewById(R.id.tsk_duedays_text);
        tsk_priority_text = (TextView)findViewById(R.id.tsk_priority_text);
        tsk_assignee_text = (TextView)findViewById(R.id.tsk_assignee_text);
        tsk_attachments_text = (TextView)findViewById(R.id.tsk_attachments_text);
        tsk_remarks_text = (TextView)findViewById(R.id.tsk_remarks_text);
        tsk_notify_text = (TextView)findViewById(R.id.tsk_notify_text);
        close_tsk = (TextView)findViewById(R.id.close_tsk);
        submit_tsk = (TextView)findViewById(R.id.submit_tsk);

        cat_spinner = (Spinner)findViewById(R.id.cat_spinner);
        tsk_priority = (Spinner)findViewById(R.id.tsk_priority);

        userlist = (EmptyRecyclerView) findViewById(R.id.userlist);

        check_single = (CheckBox)findViewById(R.id.check_single);
        check_trainee = (CheckBox)findViewById(R.id.check_trainee);
        check_manager = (CheckBox)findViewById(R.id.check_manager);
        show_users=(TextView) findViewById(R.id.show_users_button);
        trainee_rbt=(RadioButton)findViewById(R.id.trainee_rbt);
        self_rbt=(RadioButton)findViewById(R.id.self_rbt);
        attachment=(ImageView)findViewById(R.id.add_tsk_attachments);
        videoView = (VideoView) findViewById(R.id.video_View);
        addattachment = (ImageView) findViewById(R.id.addattachment);
        attachmentfile = (ImageView) findViewById(R.id.attachmentfile);
        attachmentlayout = findViewById(R.id.attachmentlayout);
        attachmentfilerecycler = (EmptyRecyclerView) findViewById(R.id.attachmentfilerecycler);
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.simple_exoplayer);

        close = (ImageView) findViewById(R.id.close);
        delete = (ImageView) findViewById(R.id.delete);
        helpView = (WebView) findViewById(R.id.helpview);

        pdf_player = (PDFView) findViewById(R.id.pdf_player);
        imagelayout = findViewById(R.id.imagelayout);
        bottomsection = findViewById(R.id.bottomsection);

        simpleExoPlayerView.setControllerListner(this);
        if(PreferenceUtils.getSubdomainName(mContext).toLowerCase().contains("tacobell")) {
            ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.category, R.layout.spinner);
            cat_spinner.setAdapter(adapter);
            adapter = ArrayAdapter.createFromResource(this, R.array.priority_arrays, R.layout.spinner);
            tsk_priority.setAdapter(adapter);
        }
        tsk_title.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length()>200&&s.length()<1)
                    tsk_title.setText("");
            }
        });
        final SpannableString content = new SpannableString(getResources().getString(R.string.show_users_msg));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        show_users.setText(content);
    }

    public void setuptheme(){
        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        overall.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        /*previoussection.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        nextsection.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));*/
        close_tsk.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
        close_tsk.setBackgroundColor(Color.WHITE);
        submit_tsk.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        submit_tsk.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        tsk_title_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_description_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_duedays_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_priority_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_assignee_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_attachments_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_remarks_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_notify_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_title.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_duedays.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_remarks.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_description.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        cat_spinner.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        tsk_priority.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        check_single.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        check_trainee.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        check_manager.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        trainee_rbt.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        self_rbt.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_category.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        int newColor = getResources().getColor(android.R.color.white);
        attachment.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        check_single.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        check_trainee.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        check_manager.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        trainee_rbt.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        self_rbt.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));

    }


    public void setuprecycleradapter(){
        LL1 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        LL2 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        userlist.setLayoutManager(LL1);

    }

    public void onClicklistener(){


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagelayout.setVisibility(View.GONE);
                bottomsection.setVisibility(View.VISIBLE);
                player.stop();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagelayout.setVisibility(View.GONE);
                bottomsection.setVisibility(View.VISIBLE);
                ackurls.remove(attachmentposition);
                reloadAdapter(ackurls);
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        trainee_rbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_users.setVisibility(View.VISIBLE);
                check_trainee.setEnabled(true);
                check_manager.setEnabled(true);

            }
        });
        self_rbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_users.setVisibility(View.GONE);
                check_trainee.setEnabled(false);
                check_manager.setEnabled(false);
                check_manager.setChecked(false);
                check_trainee.setChecked(false);
                check_single.setChecked(false);
            }
        });
        show_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (childUserListModel.size() > 0) {
                    userlist();
                } else {
                    Toast.makeText(mContext, getResources().getString(R.string.no_user_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });

        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogBox();
            }
        });

        submit_tsk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    submit_tsk.setBackgroundColor(Color.parseColor(Constants.TEXT_COLOR));
                    submit_tsk.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
                } else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
                    submit_tsk.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
                    submit_tsk.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
                    submitDirectTask();
                }
                return true;
            }
        });

        close_tsk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    close_tsk.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
                    close_tsk.setTextColor(Color.WHITE);
                } else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
                    close_tsk.setBackgroundColor(Color.WHITE);
                    close_tsk.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
                    onBackPressed();
                }
                return true;
            }
        });
    }

    public void openDialogBox(){
        messageDialog.showPhotoDialog(mContext,new View.OnClickListener() {
            @Override
            public void onClick(View Approve) {
                //logoutProcess();
                messageDialog.dialogDismiss();
                // questionActivity.takePhoto(questionAndAnswerModel,position);
                takePhoto();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View close) {
                messageDialog.dialogDismiss();
                // questionActivity.browsePhoto(questionAndAnswerModel,position);
                browsePhoto();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View close) {
                messageDialog.dialogDismiss();
                //  questionActivity.browsePhoto(questionAndAnswerModel,position);
                browsePhoto();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View close) {
                messageDialog.dialogDismiss();
                //questionActivity.TakeVideoIntent(questionAndAnswerModel,position);
                TakeVideoIntent();
            }
        }, true,true,true,true,false);
    }

    public void submitDirectTask(){
        if(validate()) {
            submit_tsk.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
            submit_tsk.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            StringBuilder formatted = new StringBuilder();
            StringBuilder usernames = new StringBuilder();
            if(selectedlist!=null&&selectedlist.size()>0) {
                for (int i = 0; i < selectedlist.size(); i++) {
                    formatted.append(selectedlist.get(i).getUser_Id());
                    usernames.append(selectedlist.get(i).getUser_Name());
                    if (i < selectedlist.size() - 1) {
                        formatted.append(',');
                        usernames.append(',');
                    }
                }
                assignedUserIDs = formatted.toString();
                if(usernames.toString().contains("null"))
                {
                    assignedUsernames=editTask.getAssigned_User_Name();
                }
                else {

                    assignedUsernames = usernames.toString();
                }
            } else {
                assignedUserIDs= String.valueOf(PreferenceUtils.getUserId(mContext));
                assignedUsernames = userobj.getFirstName();
            }
            submitlist=new TaskListModel();
            submitlist.setTask_Name(tasktitle);
            submitlist.setTask_Description(taskdesc);
            submitlist.setTask_Category_ID(1);
            submitlist.setDuedays(Integer.parseInt(taskduedays));
            if(tsk_priority.getSelectedItem().toString().equals(mContext.getResources().getString(R.string.low))) {submitlist.setTask_Priority(1);}
            if(tsk_priority.getSelectedItem().toString().equals(mContext.getResources().getString(R.string.medium))) {submitlist.setTask_Priority(2);}
            if(tsk_priority.getSelectedItem().toString().equals(mContext.getResources().getString(R.string.high))) {submitlist.setTask_Priority(3);}
            submitlist.setAssigned_User_ID(assignedUserIDs);
            submitlist.setAttachment_urls_list(ackurls);
            submitlist.setTask_Remarks(taskremarks);
            submitlist.setNotify_Emp(taskemp);
            submitlist.setNotify_Emp_Manager(taskmanger);
            submitlist.setNotify_Self(tasknotify);
            submitlist.setNotify_Intimation_type(notify);
            submitlist.setCreated_DateTime(DateHelper.getCurrentDate());
            submitlist.setIsActive(1);
            submitlist.setSource_Qustionid(questionId);
            submitlist.setCourse_Id(courseId);
            submitlist.setChecklist_Id(checklistId);
            submitlist.setCourse_Section_Id(sectionId);
            submitlist.setCreated_By(PreferenceUtils.getUserId(mContext));
            submitlist.setChecklist_Group_Id(GroupChecklistId);
            if(isGroupChecklist)
                submitlist.setGroupChecklistCheck(2);
            else
                submitlist.setGroupChecklistCheck(1);
            submitlist.setUser_Id(PreferenceUtils.getUserId(mContext));
            submitlist.setTask_Status(1);
            submitlist.setCompany_Id(PreferenceUtils.getCompnayId(mContext));
            submitlist.setAssigned_User_Name(assignedUsernames);
            submitlist.setAssigned_By(userobj.getFirstName());
            submitlist.setUser_Name(userobj.getFirstName());

            ArrayList<TaskListModel> t = new ArrayList<>();
            t.add(submitlist);

            CheckforActivity(t);

        }
    }

    public void CheckforActivity(ArrayList<TaskListModel> t){
        if(isfromQuestion) {
            if(viewlist){
                gotoViewTaskListActivty(submitlist);
            }else{
                gotoQuestionPageActivty(submitlist);
            }
        }
        else {
            CreateTaskAPI(t);
        }
    }



    private void gotoQuestionPageActivty(TaskListModel createdTask) {
        Bundle bundle = new Bundle();
        Intent returnIntent = new Intent();
        bundle.putSerializable("CreatedTask",createdTask);
        returnIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    private void gotoViewTaskListActivty(TaskListModel taskList)
    {
        Bundle bundle = new Bundle();
        Intent returnIntent = new Intent();
        bundle.putSerializable("EditedItem",taskList);
        returnIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    public boolean validate ()
    {   boolean b = true;
        tasktitle = tsk_title.getText().toString().trim();
        taskdesc =tsk_description.getText().toString().trim();
        taskremarks=tsk_remarks.getText().toString().trim();
        taskduedays=tsk_duedays.getText().toString().trim();
        taskcategory = cat_spinner.getSelectedItem().toString();
        taskpriroties= tsk_priority.getSelectedItem().toString();
        if(self_rbt.isChecked())
        {
            taskassigned=self_rbt.toString();
            if(selectedlist!=null) {
                selectedlist.clear();
            }
        }
        if(trainee_rbt.isChecked())
        {
            taskassigned=trainee_rbt.toString();
        }

        if (check_single.isChecked()) {
            notify = "1,";
            tasknotify = "1";
        }
        if (check_trainee.isChecked())
        {
            notify = notify + "2,";
            taskemp = "2";
        }
        if (check_manager.isChecked()) {
            notify = notify + "3,";
            taskmanger = "3";
        }
        if(notify!=null&&notify.length()>0)
            notify = notify.substring(0,notify.length()-1);



        if( tasktitle.length() == 0 && tasktitle.length() > 100) {
            Toast.makeText(mContext,getResources().getString(R.string.task_title_val), Toast.LENGTH_SHORT).show();
            b=false;
        }
        else if(taskdesc.length() > 200)
        {
            Toast.makeText(mContext,getResources().getString(R.string.task_description_val), Toast.LENGTH_SHORT).show();
            b=false;
        }
        else if(taskduedays == null || taskduedays.length() == 0 || taskduedays.equals("0"))
        {
            Toast.makeText(mContext,getResources().getString(R.string.task_due_val), Toast.LENGTH_SHORT).show();
            b=false;
        }
        else if(trainee_rbt.isChecked() == false && self_rbt.isChecked() == false)
        {
            Toast.makeText(mContext,getResources().getString(R.string.task_assignee_val),Toast.LENGTH_SHORT).show();
            b=false;
        }
        else if((trainee_rbt.isChecked() == true && selectedlist == null) ||
                (trainee_rbt.isChecked() && selectedlist.size() ==0))
        {
            Toast.makeText(mContext,getResources().getString(R.string.task_user_val),Toast.LENGTH_SHORT).show();
            b=false;

        }
        else if(taskremarks.length() > 200)
        {
            Toast.makeText(mContext,getResources().getString(R.string.task_remarks_val),Toast.LENGTH_SHORT).show();
            b=false;
        }

        return b;
    }
    public void CreateTaskAPI(ArrayList<TaskListModel> taskListModel)
    {
        int CompanyId = PreferenceUtils.getCompnayId(mContext);
        String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
        int UserID = PreferenceUtils.getUserId(mContext);
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            showProgressDialog();
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            TaskServices taskServices = retrofit.create(TaskServices.class);
            Call call = taskServices.getInsertTaskApi(SubdomainName, CompanyId, UserID,taskListModel);
            call.enqueue(new Callback<Boolean>() {

                @Override
                public void onResponse(Response<Boolean> response, Retrofit retrofit) {
                    boolean model = response.body();
                    if(model) {
                        dismissProgressDialog();
                        Toast.makeText(mContext,getResources().getString(R.string.task_success_msg),Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        dismissProgressDialog();
                        Toast.makeText(mContext,getResources().getString(R.string.task_failure_msg),Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(), "Error");
                    dismissProgressDialog();
                }
            });
        }
    }
    public void getChildUserList(){
        int CompanyId = PreferenceUtils.getCompnayId(mContext);
        String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
        String usercode = userobj.getUser_Code();
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            showProgressDialog();
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            TaskServices taskServices = retrofit.create(TaskServices.class);

            Call call = taskServices.getChildUserlist(SubdomainName, CompanyId, usercode);
            call.enqueue(new Callback<ArrayList<TaskListModel>>() {

                @Override
                public void onResponse(Response<ArrayList<TaskListModel>> response, Retrofit retrofit) {
                    ArrayList<TaskListModel> model = response.body();
                    if (model != null && model.size() > 0) {
                        dismissProgressDialog();
                        childUserListModel = model;

                    } else {
                        dismissProgressDialog();
                    }
                }
                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(), "Error");
                    dismissProgressDialog();
                }
            });
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
    public  void userlist()
    {
        try {
            ArrayList<TaskListModel> userslist = new ArrayList<>();
            if (selectedlist != null && selectedlist.size() > 0) {
                for (TaskListModel users : childUserListModel) {
                    users.setUserchecked(false);
                    for (TaskListModel selected : selectedlist) {
                        if (selected.getUser_Id() == users.getUser_Id()) {
                            users.setUserchecked(true);
                            break;
                        }
                    }
                    userslist.add(users);
                }


            } else {
                for (TaskListModel users : childUserListModel) {
                    users.setUserchecked(false);
                    userslist.add(users);

                }
            }

            Intent intent = new Intent(mContext, UserListActivity.class);
            userslistfinal=userslist;
          /*  Bundle bundle = new Bundle();
            bundle.putSerializable("userlist", userslist);
            intent.putExtras(bundle);*/
            startActivityForResult(intent, 1);
        }
        catch (Exception e)
        {

        }
    }
    public static ArrayList<TaskListModel> getUserslistfinal()
    {
        return userslistfinal;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                selectedlist = UserListActivity.getSelecteduser();
                final SpannableString content = new SpannableString(selectedlist.size()+" "+getResources().getString(R.string.users_selected_msg));
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                show_users.setText(content);
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(mContext,getResources().getString(R.string.task_no_user_val),Toast.LENGTH_SHORT).show();
            }

        }


        if(requestCode==REQUEST_IMAGE_BROWSE)
        {
            onBrowsingPhoto(data);
        }
        if(requestCode ==REQUEST_IMAGE_CAPTURE)
        {
            onTakingPhoto(data);
        }
        if(requestCode == REQUEST_VIDEO_CAPTURE)
        {
            onTakingVideo(data);
        }
    }

    public void camerapermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CAMERA);
            int storagePermission = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
    public void TakeVideoIntent() {

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

    public void takePhoto() {
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
                + File.separator + Constants.IMAGES;
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

    public void browsePhoto() {
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
                if(compressImage(mCurrentPhotoPath).length()/1024<20000)
                {
                    uploadToServer(compressImage(mCurrentPhotoPath));
                }
                else
                {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.file_exceeding_msg), Toast.LENGTH_LONG).show();
                }
            }else{
                String fileURI = mCurrentPhotoPath;
                File file = new File(fileURI);
                long fileSizeInKB = file.length() / 1024;
                if(imagePath.endsWith(".3gp") || imagePath.endsWith(".mp4") || imagePath.endsWith(".pdf")||imagePath.endsWith(".jpg")){
                    if(imagePath.endsWith(".pdf")){
                        if (imagePath != null && fileSizeInKB > 0) {
                            if(fileSizeInKB > 10000){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.file_exceeding_10mb),Toast.LENGTH_LONG).show();
                            }else{
                                uploadToServer(mCurrentPhotoPath);
                            }
                        }
                    } else {
                        if (imagePath != null && fileSizeInKB > 0) {
                            if (fileSizeInKB > 20000) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.file_exceeding_msg), Toast.LENGTH_LONG).show();
                            } else {
                                uploadToServer(mCurrentPhotoPath);
                            }
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.not_supported_msg),Toast.LENGTH_LONG).show();
                }
            }
        }
        else{
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_file_msg),Toast.LENGTH_LONG).show();
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
    public void uploadToServer(String compressedImagePath){
        if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
            showProgressDialog();
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            CheckListService chkService = retrofitAPI.create(CheckListService.class);
            int CompanyId = PreferenceUtils.getCompnayId(this);
            String SubdomainName = PreferenceUtils.getSubdomainName(this);
            int UserId = PreferenceUtils.getUserId(this);
            String fileURI = compressedImagePath;
            File file = new File(fileURI);
            if (file.exists()) {
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
                RequestBody localFileName = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                Call call = chkService.uploadFile(SubdomainName, CompanyId, UserId, 0, 0,
                        0, fileBody, localFileName);
                call.enqueue(new Callback<ChecklistFileUploadResult>() {

                    @Override
                    public void onResponse(Response<ChecklistFileUploadResult> response, Retrofit retrofit) {
                        ChecklistFileUploadResult URL = response.body();
                        if (URL != null) {
                            dismissProgressDialog();
                            loadattachmentList(URL.FileUri);
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
    private void ShowAlert(String Message, String Title, final boolean offline,final boolean successfull,final int typeoferror) {

        final AlertDialog alertDialog = new AlertDialog.Builder(
                mContext).create();

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
                    alertDialog.dismiss();
                    finish();
                }
                else{
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

    @Override
    public void onBackPressed() {
        if(isFromTaskEdit)
        {
            gotoViewTaskListActivty(editTask);
        }
        else
        {
            super.onBackPressed();
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
