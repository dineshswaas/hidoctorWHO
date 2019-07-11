package com.swaas.kangle.LPCourse;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.swaas.kangle.API.model.AppInfo;
import com.swaas.kangle.API.model.LandingPageAccess;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.API.service.AppInfoService;
import com.swaas.kangle.AssetListActivity;
import com.swaas.kangle.CheckList.ChecklistLandingActivity;
import com.swaas.kangle.CourseWebView;
import com.swaas.kangle.DashboardActivity;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.LPCourse.model.AnwerUploadModel;
import com.swaas.kangle.LPCourse.model.CourseUserAnswers;
import com.swaas.kangle.LPCourse.model.CourseUserAssessDet;
import com.swaas.kangle.LPCourse.model.CourseUserAssessHeader;
import com.swaas.kangle.LPCourse.model.QuestionAndAnswerModel;
import com.swaas.kangle.LPCourse.model.QuestionCourseListModel;
import com.swaas.kangle.LPCourse.questionbuilder.QuestionActivity;
import com.swaas.kangle.MoreMenuActivity;
import com.swaas.kangle.Notification.NotificationActivity;
import com.swaas.kangle.Notification.NotificationModel;
import com.swaas.kangle.Notification.NotificationTempRepository;
import com.swaas.kangle.R;
import com.swaas.kangle.TaskModule.TaskListActivity;
import com.swaas.kangle.UploadActivity;
import com.swaas.kangle.db.CourseListTempRepository;
import com.swaas.kangle.db.Filters.CourseCatTagFilterRepository;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.models.APIResponse;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.CommonUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.MessageDialog;
import com.swaas.kangle.utils.NetworkUtils;
import com.swaas.kangle.utils.iOSDialog;
import com.swaas.kangle.utils.iOSDialogBuilder;
import com.swaas.kangle.utils.iOSDialogClickListener;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.swaas.kangle.LPCourse.questionbuilder.QuestionActivity.compareArrays;

/**
 * Created by saiprasath on 8/10/2017.
 */

public class CourseListActivity extends AppCompatActivity implements LocationListener {
    CourseListAdapter courseListAdapter;
    CourseCategoryListAdapter categoryListRecyclerAdapter;
    CourseTagsListAdapter tagsListRecyclerAdapter;
    ImageView companylogo,expandfilter,mCourseFilter;
    View mEmptyView;
    EmptyRecyclerView recyclerView;
    RelativeLayout bottomheader;
    Context mContext;
    List<CourseModel> courseModelList = new ArrayList<CourseModel>();
    HashMap<String, Integer> mCourseMap,mCourseTagsMap;
    List<String> courseModelsCat;

    ProgressDialog mProgressDialog;
    CheckBox mCheckInProgress;
    CheckBox mCheckCompleted;
    CheckBox mCheckYetToStart;
    CheckBox mCheckExpired;
    CheckBox mCheckMaxAttempts;
    RelativeLayout checkBoxGroupView;
    boolean completed_checked,yet_to_start_checked,in_progress_checked,expired,max_attempts_reached;
    AssetAnalyticsUpsynctoServer assetAnalyticsUpsynctoServer;
    TextView mFilteredCountStatus;
    PackageInfo packageInfo = null;
    TextView emptymessage;
    Dialog prerequsite;
    RelativeLayout activity_list;
    GridLayoutManager grid;
    UploadActivity uploadActivity;
    public double latitude,longitude;
    MessageDialog messageDialog;
    ImageView notification,settings,chatview;
    TextView closehelp;
    WebView helpView;
    View helplayout;

    List<String> courseModelsTags;

    boolean isFilterenabled = false;
    String fromcatfilter = "",fromtagfilter = "";

    View lpcourse,assetpage,chklistpage,profilepage,bottommenusection,taskpage;
    LinearLayout bottommenus;
    LinearLayout header;


    //filter changes
    RelativeLayout filtersection;
    TextView clearfilters,applyfilters;
    ImageView close_filter;
    EmptyRecyclerView cattag_recyclerView;
    View catselection,tagselection,filterlay;
    TextView cat_filtered_count,tags_filtered_count;

    TextView clear_assetfilter,filtered_text;
    View assetfilterheading;

    boolean tagfiltered,catFiltered;
    List<String> tagslist,catlist;
    List<CourseModel> digitalAssetsMasterCategoryLists;
    List<CourseModel> digitalAssetsMasterTagsLists;
    String TAGS = "",CATS = "";
    TextView emptytagsview;

    CourseListTempRepository courseListTempRepository;
    CourseCatTagFilterRepository courseCatTagFilterRepository;
    List<CourseModel> digitalAssetsMasterListfilterd;

    private static int firstVisibleInListview;

    View clear_assetfilter_img;
    ImageView pos0;
    TextView higlighttext;

    View checkInProgress,checkyettostart,checkcompleted,checkexpired,checkmax_attempts;

    TextView filterheadingtext,retrybutton;

    View cattagmenus;
    ImageView icon_cats,icon_tags,icon_filter,icon_search,closesearch;

    SearchView msearchtext;
    SearchManager searchManager;
    List<CourseModel> msearchcourse;
    LinearLayout searchlayout;

    RelativeLayout filterheading,headersection;
    ImageView tickfilter,emptyimage;

    View notificationsec,chatviewsec;
    TextView notificationcount,chatcount;
    int QuestionLoadCount;
    TextView course,asset,checklist,task;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_recycler);
        mContext = CourseListActivity.this;
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        assetAnalyticsUpsynctoServer = new AssetAnalyticsUpsynctoServer();
        assetAnalyticsUpsynctoServer.AssetAnalyticsUpsynctoServer(mContext);
        uploadActivity = new UploadActivity(mContext);

        courseListTempRepository = new CourseListTempRepository(mContext);
        courseCatTagFilterRepository = new CourseCatTagFilterRepository(mContext);

        initializeView();
        setUpRecyclerView();
        initializebottomnavigation();
        bottomnavigationonClickevents();
        setthemeforView();
        if(NetworkUtils.checkIfNetworkAvailable(mContext)){
            if(PreferenceUtils.getCourse_User_Assignment_Id(mContext) != 0 && PreferenceUtils.getCouse_User_Section_Mapping_Id(mContext) != 0) {
                assetAnalyticsUpsynctoServer.getAnalyticsfromDb(false, PreferenceUtils.getCourse_User_Assignment_Id(mContext), PreferenceUtils.getCouse_User_Section_Mapping_Id(mContext));
            }else{
                assetAnalyticsUpsynctoServer.getAnalyticsfromDb(false, 0, 0);
            }
        }
        else
        {
            emptyimage.setImageResource(R.drawable.no_results);
            emptymessage.setText(getString(R.string.oops_no_result) +getString(R.string.enable_network));
        }
        uploadActivity.uploadTestTableRecords();
        tagslist = new ArrayList<>();
        catlist = new ArrayList<>();
        //getListOfCourses();
        onClickListeners();
        showApplyButton();
        if(PreferenceUtils.getLandingPageAccess(mContext) != null) {
            Gson gsonget = new Gson();
            LandingPageAccess landingobj = gsonget.fromJson(PreferenceUtils.getLandingPageAccess(mContext), LandingPageAccess.class);
            if (landingobj != null) {
                if (!TextUtils.isEmpty(landingobj.getLibrary()) && landingobj.getLibrary().equalsIgnoreCase("Y")) {
                    notificationsec.setVisibility(View.VISIBLE);
                }
                else
                {
                    notificationsec.setVisibility(View.GONE);
                }
            }
        }

    }

    public void getnotificationcount(){
        String subdomainName = PreferenceUtils.getSubdomainName(this);
        int CompanyId = PreferenceUtils.getCompnayId(this);
        int UserId = PreferenceUtils.getUserId(this);

        NotificationTempRepository notificationTempRepository = new NotificationTempRepository(mContext);
        notificationTempRepository.setGetNotificationDataCBListner(new NotificationTempRepository.GetNotificationDataCBListner() {
            @Override
            public void GetNotificationDataSuccessCB(ArrayList<NotificationModel> customers) {
                if(customers != null && customers.size() > 0){
                    int num = 0;
                    num = (customers.get(0).getCount() + customers.get(1).getCount());
                    if(num > 0){
                        notificationcount.setVisibility(View.VISIBLE);
                        if(num > 99) {
                            notificationcount.setText("99+");
                        }else{
                            notificationcount.setText(num + "");
                        }
                    }else{
                        notificationcount.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void GetNotificationDataFailureCB(String message) {

            }
        });

        notificationTempRepository.getNotificationHubCountFromApi(subdomainName,CompanyId,UserId);
        chatviewsec.setVisibility(View.GONE);
    }

    public void initializeView(){
        companylogo = (ImageView) findViewById(R.id.companylogo);
        mEmptyView = findViewById(R.id.empty_view);
        recyclerView = (EmptyRecyclerView)findViewById(R.id.recyclerView);
        expandfilter = (ImageView) findViewById(R.id.icon_expandslider);
        bottomheader = (RelativeLayout) findViewById(R.id.bottomheader);
        headersection = (RelativeLayout) findViewById(R.id.headersection);
        checkBoxGroupView = (RelativeLayout)findViewById(R.id.check_box);
        mCheckCompleted = (CheckBox)findViewById(R.id.check_completed);
        mCheckInProgress = (CheckBox)findViewById(R.id.check_in_progress);
        mCheckYetToStart = (CheckBox)findViewById(R.id.check_yet_to_start);
        mCheckExpired = (CheckBox)findViewById(R.id.check_expired);
        mCheckMaxAttempts = (CheckBox)findViewById(R.id.max_attempts);
        emptymessage = (TextView) findViewById(R.id.emptymessage);

        activity_list = (RelativeLayout)findViewById(R.id.activity_asset_list);
        settings = (ImageView) findViewById(R.id.settings);
        messageDialog = new MessageDialog(mContext);
        notification = (ImageView) findViewById(R.id.notification);
        chatview = (ImageView) findViewById(R.id.chatview);
        helpView = (WebView) findViewById(R.id.helpview);
        helplayout = findViewById(R.id.helplayout);
        closehelp = (TextView) findViewById(R.id.closehelp);

        header = (LinearLayout) findViewById(R.id.header);


        cattag_recyclerView = (EmptyRecyclerView) findViewById(R.id.cattag_recyclerView);
        filtersection = (RelativeLayout) findViewById(R.id.filtersection);
        close_filter = (ImageView) findViewById(R.id.close_filter);
        catselection = findViewById(R.id.catselection);
        tagselection = findViewById(R.id.tagselection);
        filterlay = findViewById(R.id.filterlay);
        clearfilters = (TextView) findViewById(R.id.clearfilters);
        applyfilters = (TextView) findViewById(R.id.applyfilters);

        assetfilterheading =  findViewById(R.id.assetfilterheading);
        filtered_text = (TextView) findViewById(R.id.filtered_text);
        clear_assetfilter = (TextView) findViewById(R.id.clear_assetfilter);
        mCourseFilter = (ImageView)findViewById(R.id.icon_filter);
        cat_filtered_count = (TextView) findViewById(R.id.cat_filtered_count);
        tags_filtered_count = (TextView) findViewById(R.id.tags_filtered_count);
        mFilteredCountStatus = (TextView)findViewById(R.id.label_filtered_count);
        emptytagsview = (TextView)findViewById(R.id.emptytagsview);

        checkInProgress = findViewById(R.id.checkInProgress);
        checkyettostart = findViewById(R.id.checkyettostart);
        checkcompleted = findViewById(R.id.checkcompleted);
        checkexpired = findViewById(R.id.checkexpired);
        checkmax_attempts = findViewById(R.id.checkmax_attempts);

        filterheadingtext = (TextView)findViewById(R.id.filterheadingtext);
        cattagmenus = findViewById(R.id.cattagmenus);
        icon_cats = (ImageView)findViewById(R.id.icon_cats);
        icon_tags = (ImageView)findViewById(R.id.icon_tags);
        icon_filter = (ImageView) findViewById(R.id.icon_filter);

        searchlayout = (LinearLayout) findViewById(R.id.searchlayout);
        msearchtext = (SearchView) findViewById(R.id.searchtext);
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        icon_search = (ImageView) findViewById(R.id.icon_search);
        closesearch = (ImageView) findViewById(R.id.closesearch);

        tickfilter = (ImageView) findViewById(R.id.tickfilter);
        filterheading = (RelativeLayout) findViewById(R.id.filterheading);

        emptyimage = (ImageView) findViewById(R.id.emptyimage);
        retrybutton = (TextView) findViewById(R.id.retrybutton);


        notificationsec = findViewById(R.id.notificationsec);
        chatviewsec = findViewById(R.id.chatviewsec);
        notificationcount = (TextView) findViewById(R.id.notificationcount);
        chatcount = (TextView) findViewById(R.id.chatcount);
        course = (TextView) findViewById(R.id.coursetext);
        asset = (TextView) findViewById(R.id.assettext);
        checklist = (TextView) findViewById(R.id.checklisttext);
        task = (TextView) findViewById(R.id.tasktext);
        if(PreferenceUtils.getLandingPageAccess(mContext) != null){
            Gson gsonget = new Gson();
            LandingPageAccess landingobj = gsonget.fromJson(PreferenceUtils.getLandingPageAccess(mContext), LandingPageAccess.class);
            if(landingobj != null) {

                if (!TextUtils.isEmpty(landingobj.getCourseText()))
                {
                    course.setText(landingobj.getCourseText());
                }
                if (!TextUtils.isEmpty(landingobj.getAssetText()))
                {
                    asset.setText(landingobj.getAssetText());
                }

                if (!TextUtils.isEmpty(landingobj.getChecklistText()))
                {
                    checklist.setText(landingobj.getChecklistText());
                }

                if (!TextUtils.isEmpty(landingobj.getTaskText()))
                {
                    task.setText(landingobj.getTaskText());
                }
            }
        }

    }

    public void setthemeforView(){

        header.setBackgroundColor(Color.parseColor(Constants.HEADERBAR_COLOR));
        activity_list.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        String landingobj = PreferenceUtils.getLandingPageAccess(mContext);
        String logo = PreferenceUtils.getClientLogo(mContext);
        Ion.with(companylogo).placeholder(R.color.topbar).error(R.color.topbar).load(
                (!TextUtils.isEmpty(logo))? logo : logo);
        File imgFile = new File("/storage/sdcard0/SwaaS LMS/companylogo.jpg");

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            companylogo.setImageBitmap(myBitmap);

        }
        expandfilter.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));
        settings.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));
        pos0.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        course.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
        clearfilters.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
        //pos0.setColorFilter(ContextCompat.getColor(mContext, Integer.parseInt(String.valueOf(companycolor))), android.graphics.PorterDuff.Mode.MULTIPLY);

        mCheckInProgress.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        mCheckCompleted.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        mCheckYetToStart.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        mCheckExpired.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        mCheckMaxAttempts.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));

        emptymessage.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        closesearch.setBackgroundColor(Color.parseColor(Constants.HEADERBAR_COLOR));
        closesearch.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));
        icon_search.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));
        filtered_text.setTypeface(filtered_text.getTypeface(), Typeface.ITALIC);

        filterheading.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        filterheadingtext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        close_filter.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));

        assetfilterheading.setBackgroundColor(Color.parseColor(Constants.HEADERBAR_COLOR));
        filtered_text.setTextColor(Color.parseColor(Constants.TOPBARICON_COLOR));
        tickfilter.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));

        emptyimage.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        retrybutton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.CARDBACKGROUND_COLOR)));
        retrybutton.setTextColor(Color.parseColor(Constants.TEXT_COLOR));

        notification.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));
        chatview.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));
    }

    public void initializebottomnavigation(){
        bottommenusection = findViewById(R.id.bottommenusection);
        bottommenus = (LinearLayout) findViewById(R.id.bottommenus);
        lpcourse = findViewById(R.id.lpcourse);
        assetpage = findViewById(R.id.assetpage);
        chklistpage = findViewById(R.id.chklistpage);
        taskpage = findViewById(R.id.reports);
        profilepage = findViewById(R.id.profilepage);
        clear_assetfilter_img = findViewById(R.id.clear_assetfilter_img);

        pos0 = (ImageView) findViewById(R.id.pos0);
        higlighttext = (TextView) findViewById(R.id.higlighttext);
    }

    public void bottomnavigationonClickevents(){
        int count = 1;
        if(PreferenceUtils.getLandingPageAccess(mContext) != null){
            Gson gsonget = new Gson();
            LandingPageAccess landingobj = gsonget.fromJson(PreferenceUtils.getLandingPageAccess(mContext), LandingPageAccess.class);
            if(landingobj != null) {
                if (!TextUtils.isEmpty(landingobj.getLibrary()) && landingobj.getLibrary().equalsIgnoreCase("Y")) {
                    assetpage.setVisibility(View.VISIBLE);
                    count += 1;
                }else{
                    assetpage.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(landingobj.getCourse()) && landingobj.getCourse().equalsIgnoreCase("L")) {
                    lpcourse.setVisibility(View.VISIBLE);
                    count += 1;
                } else if (!TextUtils.isEmpty(landingobj.getCourse()) && landingobj.getCourse().equalsIgnoreCase("S")) {
                    lpcourse.setVisibility(View.VISIBLE);
                    count += 1;
                    //adCourse.setVisibility(View.VISIBLE);
                } else if(!TextUtils.isEmpty(landingobj.getCourse()) && landingobj.getCourse().equalsIgnoreCase("A")){
                    lpcourse.setVisibility(View.VISIBLE);
                    count += 1;
                    //lpcourse.setVisibility(View.VISIBLE);
                    //lpcourse.setVisibility(View.VISIBLE);
                }else{
                    lpcourse.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(landingobj.getChecklist()) && landingobj.getChecklist().equalsIgnoreCase("Y")) {
                    chklistpage.setVisibility(View.VISIBLE);
                    count += 1;
                }else{
                    chklistpage.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(landingobj.getTask()) && landingobj.getTask().equalsIgnoreCase("Y")) {
                    taskpage.setVisibility(View.VISIBLE);
                    count += 1;
                }else{
                    taskpage.setVisibility(View.GONE);
                }
            }
        }

        bottommenus.setWeightSum(count);

        lpcourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(!NetworkUtils.checkIfNetworkAvailable(CourseListActivity.this)){
                    //Toast.makeText(CourseListActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,CourseListActivity.class);
                startActivity(i);
                finish();*/
            }
        });

        assetpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent assetview = new Intent(mContext,AssetListActivity.class);
                startActivity(assetview);
                finish();
            }
        });

        chklistpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetworkUtils.checkIfNetworkAvailable(CourseListActivity.this)){
                    Toast.makeText(CourseListActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,ChecklistLandingActivity.class);
                startActivity(i);
                finish();
            }
        });

        taskpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!NetworkUtils.checkIfNetworkAvailable(CourseListActivity.this)){
                    Toast.makeText(CourseListActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,TaskListActivity.class);
                startActivity(i);
                finish();
            }
        });

        profilepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetworkUtils.checkIfNetworkAvailable(CourseListActivity.this)){
                    Toast.makeText(CourseListActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,MoreMenuActivity.class);
                //Intent i = new Intent(mContext,NotificationActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        cattag_recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        if(getResources().getBoolean(R.bool.portrait_only)){
            //recyclerView.setLayoutManager(linearLayoutManager);
            grid = new GridLayoutManager(this,2);
            recyclerView.setLayoutManager(grid);
        }else{
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                /*grid = new GridLayoutManager(this,2);
                recyclerView.setLayoutManager(grid);*/
                //recyclerView.setLayoutManager(linearLayoutManager);
                grid = new GridLayoutManager(this,5);
                recyclerView.setLayoutManager(grid);
            }else{
                grid = new GridLayoutManager(this,3);
                recyclerView.setLayoutManager(grid);
                //recyclerView.setLayoutManager(linearLayoutManager);
            }
        }
        firstVisibleInListview = grid.findFirstVisibleItemPosition();
        cattag_recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUpRecyclerView();
    }

    public void loadSearchdata(String newText){
        msearchcourse = new ArrayList<>();
        msearchcourse = courseModelList;
        List<CourseModel> filteredList = new ArrayList<>();
        if(msearchcourse.size() > 0) {
            for (CourseModel row : msearchcourse) {
                String name = row.getCourse_Name().toLowerCase();
                if (name.contains(newText.toLowerCase())) {
                    filteredList.add(row);
                }
            }
            if (filteredList.size() > 0) {
                showhideemptystate(false,"",0);
                recyclerView.setVisibility(View.VISIBLE);
                courseListAdapter = new CourseListAdapter(mContext, filteredList);
                recyclerView.setAdapter(courseListAdapter);
                courseListAdapter.notifyDataSetChanged();
            } else {
                recyclerView.setVisibility(View.GONE);
                showhideemptystate(true,getResources().getString(R.string.no_result),Constants.NORESULTSNUM);
            }
        }else{
            recyclerView.setVisibility(View.GONE);
            showhideemptystate(true,getResources().getString(R.string.no_result),Constants.NORESULTSNUM);
        }
    }

    public void onClickListeners(){
        retrybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListOfCourses();
            }
        });
        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
            }
        });

        closesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchlayout.setVisibility(View.GONE);
                notificationsec.setVisibility(View.VISIBLE);
                header.setVisibility(View.VISIBLE);
            }
        });

        icon_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchlayout.setVisibility(View.VISIBLE);
                notificationsec.setVisibility(View.GONE);
                header.setVisibility(View.INVISIBLE);
            }
        });

        searchlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        msearchtext.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));

        msearchtext.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchlayout.setVisibility(View.GONE);
                return false;
            }
        });

        msearchtext.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() > 0){
                    closesearch.setVisibility(View.GONE);
                }else{
                    closesearch.setVisibility(View.VISIBLE);
                }
                loadSearchdata(newText);
                return false;
            }
        });

        checkInProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckInProgress.isChecked()){
                    mCheckInProgress.setChecked(false);
                }else{
                    mCheckInProgress.setChecked(true);
                }
                showApplyButton();
            }
        });
        checkyettostart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckYetToStart.isChecked()){
                    mCheckYetToStart.setChecked(false);
                }else{
                    mCheckYetToStart.setChecked(true);
                }
                showApplyButton();
            }
        });
        checkcompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckCompleted.isChecked()){
                    mCheckCompleted.setChecked(false);
                }else{
                    mCheckCompleted.setChecked(true);
                }
                showApplyButton();
            }
        });
        checkexpired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckExpired.isChecked()){
                    mCheckExpired.setChecked(false);
                }else{
                    mCheckExpired.setChecked(true);
                }
                showApplyButton();
            }
        });
        checkmax_attempts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckMaxAttempts.isChecked()){
                    mCheckMaxAttempts.setChecked(false);
                }else{
                    mCheckMaxAttempts.setChecked(true);
                }
                showApplyButton();
            }
        });

        close_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFilterenabled){
                    filtersection.setVisibility(View.GONE);
                    bottommenusection.setVisibility(View.GONE);
                    assetfilterheading.setVisibility(View.GONE);
                    header.setVisibility(View.VISIBLE);
                }else{
                    filtersection.setVisibility(View.GONE);
                    catlist.clear();
                    tagslist.clear();
                    CATS = "";TAGS = "";
                    mCheckCompleted.setChecked(false);
                    mCheckYetToStart.setChecked(false);
                    mCheckInProgress.setChecked(false);
                    mCheckExpired.setChecked(false);
                    mCheckMaxAttempts.setChecked(false);
                    catFiltered = false; tagfiltered = false;
                    showApplyButton();
                    getCourses();
                    bottommenusection.setVisibility(View.VISIBLE);
                }

            }
        });

        catselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCategorySlider();
                checkBoxGroupView.setVisibility(View.GONE);
            }
        });

        tagselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTagsSlider();
                checkBoxGroupView.setVisibility(View.GONE);
            }
        });

        filterlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleselection(3);
                cattag_recyclerView.setVisibility(View.GONE);
                checkBoxGroupView.setVisibility(View.VISIBLE);
            }
        });

        clearfilters.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    clearfilters.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
                    clearfilters.setTextColor(Color.WHITE);
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    clearfilters.setBackgroundColor(Color.WHITE);
                    clearfilters.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
                    clearfiltersFunction();
                    filtersection.setVisibility(View.GONE);
                    catFiltered= false; tagfiltered = false;
                    getCourses();
                    bottommenusection.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        applyfilters.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    applyfilters.setBackgroundColor(Color.WHITE);
                    applyfilters.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    applyfilters.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
                    applyfilters.setTextColor(Color.WHITE);
                    getCoursesbyIDs();
                    filtersection.setVisibility(View.GONE);
                    header.setVisibility(View.INVISIBLE);
                    assetfilterheading.setVisibility(View.VISIBLE);
                    String htmlString="<u>"+getResources().getString(R.string.clearfilter)+"</u>";
                    filtered_text.setText(Html.fromHtml(htmlString));
                    isFilterenabled = true;
                    bottommenusection.setVisibility(View.GONE);
                }
                return true;
            }
        });

        clear_assetfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtersection.setVisibility(View.GONE);
                catlist.clear();
                tagslist.clear();
                CATS = "";TAGS = "";
                assetfilterheading.setVisibility(View.GONE);
                header.setVisibility(View.VISIBLE);
                isFilterenabled = false;
                clearfiltersFunction();
                bottommenusection.setVisibility(View.VISIBLE);
                getListOfCourses();
            }
        });

        expandfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtersection.setVisibility(View.VISIBLE);
                catlist.clear();
                tagslist.clear();
                digitalAssetsMasterCategoryLists = courseCatTagFilterRepository.getAllCategory(CATS);
                digitalAssetsMasterTagsLists = courseCatTagFilterRepository.getAllTags(TAGS);
                cattag_recyclerView.setVisibility(View.VISIBLE);
                checkBoxGroupView.setVisibility(View.GONE);
                loadCategorySlider();
                bottommenusection.setVisibility(View.GONE);
            }
        });

        clear_assetfilter_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottommenusection.setVisibility(View.GONE);
                assetfilterheading.setVisibility(View.GONE);
                header.setVisibility(View.VISIBLE);
                filtersection.setVisibility(View.VISIBLE);

            }
        });

        filterheadingtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        filtered_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtersection.setVisibility(View.GONE);
                catlist.clear();
                tagslist.clear();
                CATS = "";TAGS = "";
                assetfilterheading.setVisibility(View.GONE);
                header.setVisibility(View.VISIBLE);
                isFilterenabled = false;
                clearfiltersFunction();
                bottommenusection.setVisibility(View.VISIBLE);
                getListOfCourses();
            }
        });


        /*mCourseFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxGroupView.getVisibility() == View.VISIBLE){
                    checkBoxGroupView.setVisibility(View.GONE);
                    bottomheader.setVisibility(View.GONE);
                }
                else{
                    checkBoxGroupView.setVisibility(View.VISIBLE);
                    bottomheader.setVisibility(View.VISIBLE);
                }
            }
        });*/
        mCheckCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showApplyButton();
            }
        });

        mCheckYetToStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showApplyButton();
            }
        });
        mCheckInProgress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showApplyButton();
            }
        });
        mCheckExpired.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showApplyButton();
            }
        });
        mCheckMaxAttempts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showApplyButton();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.checkIfNetworkAvailable(mContext)) {
                    messageDialog.showEmailPop(mContext, new View.OnClickListener() {
                        @Override
                        public void onClick(View Approve) {
                            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                            emailIntent.setType("plain/text");
                            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{Constants.SUPPORT_EMAIL});
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, Constants.Foldername+" support");
                            startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.sendemail)));
                            messageDialog.dialogDismiss();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View close) {
                            messageDialog.dialogDismiss();
                        }
                    }, true);
                }else{
                    Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                }

            }
        });

        notificationsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
                    /*if(!getResources().getBoolean(R.bool.portrait_only)){
                        loadPopUpHelpView();
                    }else {
                        loadHelpView();
                    }*/
                    Intent i = new Intent(mContext,NotificationActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                }
            }
        });

        chatviewsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
                    Intent i = new Intent(mContext,NotificationActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                }
            }
        });



        closehelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                helplayout.setVisibility(View.GONE);
            }
        });

        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SYSTEM_UI_FLAG_HIDE_NAVIGATION) {
                    // Now I have to check if the user has scrolled down or up.
                    int currentFirstVisible = grid.findFirstVisibleItemPosition();
                    if(currentFirstVisible > firstVisibleInListview) {
                        header.animate().translationY(-(header.getHeight()));
                        header.setVisibility(View.GONE);
                    } else {
                        header.animate().translationY(0f);
                        header.setVisibility(View.VISIBLE);
                    }

                    firstVisibleInListview = currentFirstVisible;

                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });*/

    }

    public void clearfiltersFunction(){
        catlist.clear();
        tagslist.clear();
        CATS = "";TAGS = "";
        catFiltered = false; tagfiltered = false;
        mCheckCompleted.setChecked(false);
        mCheckYetToStart.setChecked(false);
        mCheckInProgress.setChecked(false);
        mCheckExpired.setChecked(false);
        mCheckMaxAttempts.setChecked(false);
        showApplyButton();
        isFilterenabled = false;
        digitalAssetsMasterCategoryLists = courseCatTagFilterRepository.getAllCategory(CATS);
        digitalAssetsMasterTagsLists = courseCatTagFilterRepository.getAllTags(TAGS);
        categoryListRecyclerAdapter = new CourseCategoryListAdapter(mContext, digitalAssetsMasterCategoryLists,true);
        categoryListRecyclerAdapter.notifyDataSetChanged();
        tagsListRecyclerAdapter = new CourseTagsListAdapter(mContext,digitalAssetsMasterTagsLists,true);
        tagsListRecyclerAdapter.notifyDataSetChanged();
        catselection.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        tagselection.setBackgroundColor(Color.parseColor(Constants.OPAQUE_COLOR));
        filterlay.setBackgroundColor(Color.parseColor(Constants.OPAQUE_COLOR));
        cattag_recyclerView.setVisibility(View.VISIBLE);
        checkBoxGroupView.setVisibility(View.GONE);
        cattag_recyclerView.setAdapter(categoryListRecyclerAdapter);
    }

    public Set<String> getCategoreyList(final List<CourseModel> coursecategory) {
        Set<String> courselist = new HashSet<>();
        for(final CourseModel coursecat: coursecategory) {
            courselist.add(coursecat.getCategory_Name());
        }
        return courselist;
    }

    public void loadCoursebyCategory(String categoryName){
        List<CourseModel> courseAssetscategory= new ArrayList<>();
        for(CourseModel courseModel: courseModelList){
            if(categoryName.equalsIgnoreCase(courseModel.getCategory_Name())){
                courseAssetscategory.add(courseModel);
            }
        }
        setUpRecyclerView();
        courseListAdapter = new CourseListAdapter(mContext,courseAssetscategory);
        recyclerView.setAdapter(courseListAdapter);
    }

    public void loadCoursebyTags(String tagName){
        List<CourseModel> courseAssetsTags= new ArrayList<>();
        for(CourseModel courseModel: courseModelList){
            //courseModel.getCourse_Tags().replace("^","#");
            if(courseModel.getCourse_Tags()!= null && !courseModel.getCourse_Tags().isEmpty()) {
                if (courseModel.getCourse_Tags().contains(tagName)) {
                    courseAssetsTags.add(courseModel);
                }
            }
        }
        setUpRecyclerView();
        courseListAdapter = new CourseListAdapter(mContext,courseAssetsTags);
        recyclerView.setAdapter(courseListAdapter);
    }

    public void getListOfCourses(){
        if (!isFinishing()){
            dismissProgressDialog();
        }
        if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
            PreferenceUtils.setNWEAvisible(mContext, false);
            PreferenceUtils.setVisibleActivityName(mContext,"Course");
            if (!isFinishing()){
                showProgressDialog();
            }
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            LPCourseService userService = retrofitAPI.create(LPCourseService.class);

            //String offsetFromUtc = CommonUtils.getUtcOffset();
            String offsetFromUtc = CommonUtils.getUtcOffsetincluded10k();
            Log.d("getUTC", offsetFromUtc);
            int CompanyId = PreferenceUtils.getCompnayId(mContext);
            Log.d("CompanyId", String.valueOf(CompanyId));
            String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            int UserId = PreferenceUtils.getUserId(mContext);
            Call call = userService.getAvailableKACourses(SubdomainName, CompanyId, UserId, offsetFromUtc);
            call.enqueue(new Callback<ArrayList<CourseModel>>() {

                @Override
                public void onResponse(Response<ArrayList<CourseModel>> response, Retrofit retrofit) {
                    ArrayList<CourseModel> courseListModel = response.body();
                    if (courseListModel != null && courseListModel.size() > 0) {
                        showhideemptystate(false,"",0);
                        courseModelList = courseListModel;
                        //courseListTempRepository.courseListBulkInsert(courseModelList);
                        icon_search.setVisibility(View.VISIBLE);
                        expandfilter.setVisibility(View.VISIBLE);
                        getCourses();
                        insertintocategoreytagstable();
                    } else {
                        showhideemptystate(true,getResources().getString(R.string.emptyDashboard),Constants.NORESULTSNUM);
                        recyclerView.setVisibility(View.GONE);
                        icon_search.setVisibility(View.INVISIBLE);
                        expandfilter.setVisibility(View.GONE);
                        if (!isFinishing()){
                            dismissProgressDialog();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(), "Error");
                    showhideemptystate(true,getResources().getString(R.string.error_message),Constants.INTERNETERRORNUM);
                    if (!isFinishing()){
                        dismissProgressDialog();
                    }
                }
            });

        }else{
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
            showhideemptystate(true,getResources().getString(R.string.error_message),Constants.INTERNETERRORNUM);
            recyclerView.setVisibility(View.GONE);
            if (!isFinishing()){
                dismissProgressDialog();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListOfCourses();
        getnotificationcount();
        if(isFilterenabled){

        }else{
            filtersection.setVisibility(View.GONE);
            catlist.clear();
            tagslist.clear();
            CATS = "";
            TAGS = "";
            catFiltered = false; tagfiltered = false;
            assetfilterheading.setVisibility(View.GONE);
            header.setVisibility(View.VISIBLE);
            isFilterenabled = false;
            clearfiltersFunction();
            bottommenusection.setVisibility(View.VISIBLE);
        }

        uploadActivity.insertUserTracking("LP",latitude,longitude);
        uploadActivity = new UploadActivity(mContext);
        if(NetworkUtils.checkIfNetworkAvailable(mContext)){
            uploadActivity.uploadTrackingTable();
        }
        else
        {
            emptyimage.setImageResource(R.drawable.interenet_error_image);
            emptymessage.setText(getString(R.string.oops_no_result) +getString(R.string.enable_network));
        }
        if (PreferenceUtils.getquestionanswerlist(CourseListActivity.this,"key")!=null && PreferenceUtils.getquestionanswerlist(CourseListActivity.this,"key").size() > 0){
            QuestionActivity questionActivity = new QuestionActivity();
            ArrayList<QuestionAndAnswerModel> list = PreferenceUtils.getquestionanswerlist(CourseListActivity.this,"key");
            Checknetworkandupload(UploadAnswerProcess(list),false,false,true);
            QuestionLoadCount = list.get(0).getLstCourse().get(0).getQuestionLoadCount();
            // PreferenceUtils.setQuestionAnswerList("key",null,CourseListActivity.this);
        }
        checkForAppUpdates();
    }

    private void checkForAppUpdates() {
        Gson gsonget = new Gson();
        User userobj = gsonget.fromJson(PreferenceUtils.getUser(CourseListActivity.this), User.class);
        if(TextUtils.isEmpty(PreferenceUtils.getCompanyCode(mContext))){
            PreferenceUtils.setCompanyCode(mContext,userobj.getCompany_Code());
        }
        PackageManager packageManager = this.getPackageManager();
        try {
            packageInfo = packageManager.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            Log.d("DashboardActivity", e1.getMessage());
        }
        if (packageInfo != null) {
            getLatestAppVersionFromAPI();
        }

    }

    private void getLatestAppVersionFromAPI() {
        if (NetworkUtils.checkIfNetworkAvailable(this)) {
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            AppInfoService appInfoService = retrofitAPI.create(AppInfoService.class);
            Call call = appInfoService.getLatestAppVersion(PreferenceUtils.getCompanyCode(mContext), Constants.OS_NAME);
            call.enqueue(new Callback<APIResponse<AppInfo>>() {
                @Override
                public void onResponse(Response<APIResponse<AppInfo>> response, Retrofit retrofit) {
                    APIResponse apiResponse = response.body();
                    List<AppInfo> appInfo = null;
                    if (apiResponse != null)
                        appInfo = apiResponse.getResultDetails();
                    if (appInfo != null && appInfo.size() > 0 &&
                            appInfo.get(0).isUpgradeRequired() == 1) {
                        if (Double.valueOf(packageInfo.versionCode) < Double.valueOf(appInfo.get(0).getVersion())) {
                            PreferenceUtils.setIsForUpdateVersion(CourseListActivity.this, appInfo.get(0).getVersion());
                            PreferenceUtils.setIsForUpdateAvailable(CourseListActivity.this, true);
                            showForceUpdateAlert();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("Error", "Getting version from API" + t.getMessage());
                }
            });
        } else {
            if (PreferenceUtils.getIsForUpdateAvailable(CourseListActivity.this)) {
                if (packageInfo.versionCode < Integer.parseInt(PreferenceUtils.getIsForUpdateVersion(CourseListActivity.this))) {
                    showForceUpdateAlert();
                } else {
                    PreferenceUtils.setIsForUpdateAvailable(CourseListActivity.this, false);
                    //checkAppUpgrade();
                }
            }/*else{
                checkAppUpgrade();
            }*/
        }
    }

    private void showForceUpdateAlert() {

        new iOSDialogBuilder(mContext)
                .setTitle("New Version Available")
                .setSubtitle(getResources().getString(R.string.update_alert))
                .setBoldPositiveLabel(false)
                .setCancelable(false)
                .setSingleButtonView(true)
                .setPositiveListener("",null)
                .setNegativeListener("",null)
                .setSinglePositiveListener("UPDATE", new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dialog.dismiss();
                        Intent viewIntent = new Intent("android.intent.action.VIEW",
                                Uri.parse(Constants.APP_URL));
                        startActivity(viewIntent);
                    }
                })
                .build().show();

    }


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public void getCourses(){
        if(!isFilterenabled) {
            //courseModelList = courseListTempRepository.getAllCourses();
            if(courseModelList.size() > 0) {
                showhideemptystate(false,"",0);
                recyclerView.setVisibility(View.VISIBLE);
                courseListAdapter = new CourseListAdapter(mContext, courseModelList);
                recyclerView.setAdapter(courseListAdapter);
                if (!isFinishing()) {
                    dismissProgressDialog();
                }
            }else{
                showhideemptystate(true,getResources().getString(R.string.emptyDashboard),Constants.EMPTYLISTNUM);
                recyclerView.setVisibility(View.GONE);
            }
            loadadapterclick();
        }else{
            getCoursesbyIDs();
            //getfilteres();
        }
    }

    public void loadadapterclick(){

        courseListAdapter.setOnItemClickListener(new CourseListAdapter.MyClickListener() {
            @Override
            //public void onItemClick(int courseId, boolean isSequenceEnabled) {
            public void onItemClick(int courseId) {
                if(NetworkUtils.isNetworkAvailable()) {
                    CourseModel courseModel = new CourseModel();
                    for (CourseModel course : courseModelList) {
                        if (courseId == course.getCourse_Id()) {
                            courseModel = course;
                            break;
                        }
                    }
                    if (!courseModel.getPrerequisite().equalsIgnoreCase("") &&
                            courseModel.getCourse_Status_Value() == 0) {
                        ShowPreRequsite(courseModel.getPrerequisite());
                    } else if(courseModel.getCourse_Status_Value() == Constants.COURSE_EXPIRED) {
                        if(courseModel.isCourseExtend()){
                            if(courseModel.getAutoExtendDays() < courseModel.getExtendLimits()){
                                ShowCourseExtendpopup(courseModel);
                            }else{
                                gotosectionActivity(courseModel);
                            }
                        }else{
                            gotosectionActivity(courseModel);
                        }
                    }else if(courseModel.getCourse_Status_Value() != Constants.COURSE_EXPIRED) {
                        if(courseModel.getIsCourseRestart() == 1) {
                            courseCheckListRestartUpdate(courseModel);
                            gotosectionActivity(courseModel);
                        }else{
                            gotosectionActivity(courseModel);
                        }
                    }else {
                        gotosectionActivity(courseModel);
                    }
                }else{
                    Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void gotosectionActivity(CourseModel courseModel){
        Intent intent = new Intent(mContext, SectionActivity.class);
        intent.putExtra(Constants.Course_Id, courseModel.getCourse_Id());
        intent.putExtra(Constants.Publish_Id, courseModel.getPublish_Id());
        intent.putExtra(Constants.Course_Name, courseModel.getCourse_Name());
        intent.putExtra(Constants.Course_Description, courseModel.getCourse_Description());
        intent.putExtra(Constants.Course_Thumbnail, courseModel.getCourse_Image_URL());
        intent.putExtra(Constants.Course_Status, courseModel.getCourse_Status_String());
        intent.putExtra("Course_Status_INT", courseModel.getCourse_Status_Value());
        intent.putExtra("Cats",courseModel.getCategory_Name());
        intent.putExtra("Tags",courseModel.getCourse_Tags());
        Bundle bundle = new Bundle();
        bundle.putSerializable("value", courseModel);
        intent.putExtras(bundle);
        intent.putExtra(Constants.Is_From_DashBoard,false);
        if (courseModel.getEvaluation_Mode()!=null && courseModel.getEvaluation_Mode().equalsIgnoreCase("MANUAL"))
        {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            Date date = null;
            try {

                String DateAsString = courseModel.getMinimum_Duration();
                //String kept = DateAsString.substring( 0, DateAsString.indexOf("."));
                date = sdf.parse(DateAsString);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            long TimeAsMilli;
            long  hoursAsMilli = TimeUnit.HOURS.toMillis(date.getHours());
            long minutesAsMilli  =  TimeUnit.MINUTES.toMillis(date.getMinutes());
            long secondsAsMilli = TimeUnit.SECONDS.toMillis(date.getSeconds());
            TimeAsMilli = hoursAsMilli+minutesAsMilli+secondsAsMilli;
            intent.putExtra("Timer",TimeAsMilli);
            PreferenceUtils.setTimer(mContext,TimeAsMilli);
            intent.putExtra(Constants.Evaluation_Mode, true);

            if(courseModel.getManual_Evaluation_Status()==0 && !(courseModel.getCourse_Status_Value()==Constants.YET_TO_START))
            {
                intent.putExtra(Constants.Evaluation_Mode, true);
                intent.putExtra("Show pending for evaluation",true);
            }
        }
        else
        {
            intent.putExtra(Constants.Evaluation_Mode,false);
        }

        startActivity(intent);
    }

    public void ShowCourseExtendpopup(final CourseModel courseModel){
        messageDialog.ShowCourseExtend(mContext, getString(R.string.extend_course), new View.OnClickListener() {
            @Override
            public void onClick(View Approve) {
                requestExtentDateForUsers(courseModel);
                messageDialog.dialogDismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View close) {
                messageDialog.dialogDismiss();
                gotosectionActivity(courseModel);
            }
        }, true);
    }

    public void requestExtentDateForUsers(final CourseModel courseModel){
        if(NetworkUtils.checkIfNetworkAvailable(mContext)){
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            LPCourseService userService = retrofitAPI.create(LPCourseService.class);

            String offsetFromUtc = CommonUtils.getUtcOffsetincluded10k();
            int CompanyId = PreferenceUtils.getCompnayId(mContext);
            String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            CourseExtendModel c = new CourseExtendModel();
            c.setCompanyId(CompanyId);
            c.setCourseId(courseModel.getCourse_Id());
            c.setCourseUserMappingIds(String.valueOf(courseModel.getCourse_User_Assignment_Id()));
            c.setExtendDays(courseModel.getExtendDays());
            c.setLocalTimeZone(offsetFromUtc);
            c.setOffsetValue(offsetFromUtc);
            c.setSubdomainName(SubdomainName);

            Call call = userService.LPCourseAutoExtentDateForUsers(c);
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    String courseListModel = response.body();
                    if(courseListModel != null){
                        if(courseListModel.equals("Success")){
                            gotosectionActivity(courseModel);
                        }
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(), "Error");
                }
            });
        }else{

        }
    }

    public void courseCheckListRestartUpdate(final CourseModel courseModel){
        if(NetworkUtils.checkIfNetworkAvailable(mContext)){
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            LPCourseService userService = retrofitAPI.create(LPCourseService.class);

            int CompanyId = PreferenceUtils.getCompnayId(mContext);
            String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            int UserId = PreferenceUtils.getUserId(mContext);

            CourseExtendModel c = new CourseExtendModel();
            c.setCompanyId(CompanyId);
            c.setCourseId(courseModel.getCourse_Id());
            c.setUser_Id(UserId);
            c.setSubdomainName(SubdomainName);

            Call call = userService.CourseCheckListRestartUpdate(CompanyId,courseModel.getCourse_Id(),UserId,SubdomainName);
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    String courseListModel = response.body();
                    if(courseListModel != null){
                        if(courseListModel.equals("Success")){
                            //gotosectionActivity(courseModel);
                        }
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(), "Error");
                }
            });
        }else{

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

    public void ShowPreRequsite(String premessage){
        /*messageDialog.ShowPreRequsite(mContext, premessage, new View.OnClickListener() {
            @Override
            public void onClick(View Approve) {
                messageDialog.dialogDismiss();
            }
        }, true);*/
        String message = getResources().getString(R.string.prerequsitebefore)+" '"+ premessage+"' "+
                getResources().getString(R.string.prerequsiteafter);
        messageDialog.showCustomAlertMessageDialog(mContext,"Pre Requsite", message, new View.OnClickListener() {
            @Override
            public void onClick(View Approve) {
                messageDialog.dialogDismiss();
            }
        }, true);
    }

    private void loadFilteredList(List<CourseModel> csmodel) {
        if (!isFinishing() || mProgressDialog != null) {
            dismissProgressDialog();
        }
        isFilterenabled = true;
        int countchecked = 0;
        final ArrayList<CourseModel> filteredCourse = new ArrayList<CourseModel>();
        completed_checked = mCheckCompleted.isChecked();
        yet_to_start_checked = mCheckYetToStart.isChecked();
        in_progress_checked = mCheckInProgress.isChecked();
        expired = mCheckExpired.isChecked();
        max_attempts_reached = mCheckMaxAttempts.isChecked();
        if(!completed_checked && !yet_to_start_checked && !in_progress_checked && !expired && !max_attempts_reached){
            courseListAdapter = new CourseListAdapter(mContext,csmodel);
            recyclerView.setAdapter(courseListAdapter);
            mFilteredCountStatus.setVisibility(View.INVISIBLE);
        } else {
            if (completed_checked) {
                for (CourseModel courseModel : csmodel) {
                    if (courseModel.getCourse_Status_Value() == 2) {
                        filteredCourse.add(courseModel);
                    }
                }
                countchecked++;
            }
            if (yet_to_start_checked) {
                for (CourseModel courseModel : csmodel) {
                    if (courseModel.getCourse_Status_Value() == 0) {
                        filteredCourse.add(courseModel);
                    }
                }
                countchecked++;
            }
            if (in_progress_checked) {
                for (CourseModel courseModel : csmodel) {
                    if (courseModel.getCourse_Status_Value() == 1) {
                        filteredCourse.add(courseModel);
                    }
                }
                countchecked++;
            }
            if (expired){
                for (CourseModel courseModel : csmodel) {
                    if (courseModel.getCourse_Status_Value() == 4) {
                        filteredCourse.add(courseModel);
                    }
                }
                countchecked++;
            }
            if (max_attempts_reached) {
                for (CourseModel courseModel : csmodel) {
                    if (courseModel.getCourse_Status_Value() == 3){
                        filteredCourse.add(courseModel);
                    }
                }
                countchecked++;
            }
            /*mFilteredCountStatus.setText(String.valueOf(countchecked));
            mFilteredCountStatus.setVisibility(View.VISIBLE);*/
            showhideemptystate(false,"",0);
            courseListAdapter = new CourseListAdapter(mContext,filteredCourse);
            recyclerView.setAdapter(courseListAdapter);
            //courseListAdapter.notifyDataSetChanged();
            if(filteredCourse.size() == 0){
                showhideemptystate(true,getResources().getString(R.string.no_result),Constants.NORESULTSNUM);
            }
        }
    }

    public void loadPopUpHelpView(){
        String lan = "";
        String language = Locale.getDefault().getDisplayLanguage();
        if(language.equalsIgnoreCase("English")){
            lan = "en-";
        } else if(language.equalsIgnoreCase("Español")){
            lan = "es-";
        }
        String URL = "";
        if(PreferenceUtils.getSubdomainName(mContext).contains("tacobell")){
            URL = Constants.COMPANY_BASE_URL+"/HelpFiles/taco/"+lan+"Kanglehelpcourse.htm";
        }else{
            URL = Constants.COMPANY_BASE_URL+"/HelpFiles/other/Kanglehelpcourse.html";
        }
        //String URL = Constants.COMPANY_BASE_URL+"/HelpFiles/"+lan+"Kanglehelpcourse.htm";
        messageDialog.Showhelppopup(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View Approve) {
                messageDialog.dialogDismiss();
            }
        },URL, false);
    }

    public void loadHelpView(){
        recyclerView.setVisibility(View.GONE);
        helplayout.setVisibility(View.VISIBLE);
        String lan = "";
        String language = Locale.getDefault().getDisplayLanguage();
        if(language.equalsIgnoreCase("English")){
            lan = "en-";
        } else if(language.equalsIgnoreCase("Español")){
            lan = "es-";
        }
        String URL = "";
        if(PreferenceUtils.getSubdomainName(mContext).contains("tacobell")){
            URL = Constants.COMPANY_BASE_URL+"/HelpFiles/taco/"+lan+"Kanglehelpcourse.htm";
        }else{
            URL = Constants.COMPANY_BASE_URL+"/HelpFiles/other/Kanglehelpcourse.html";
        }
        //String URL = Constants.COMPANY_BASE_URL+"/HelpFiles/"+lan+"Kanglehelpcourse.htm";
        WebSettings settings = helpView.getSettings();
        settings.setDomStorageEnabled(true);
        helpView.getSettings().setJavaScriptEnabled(true);
        helpView.getSettings().setLoadWithOverviewMode(true);
        helpView.getSettings().setUseWideViewPort(true);
        helpView.getSettings().setBuiltInZoomControls(true);
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

    //filter Functions

    public void loadCategorySlider(){
        try{
            toggleselection(1);
            if(tagslist.size()>0){
                String tagnames = "";
                for (String catname : tagslist) {
                    tagnames =  tagnames + "," + "'" + catname.toString() + "'";
                }
                StringBuilder sb = new StringBuilder(tagnames);
                sb.deleteCharAt(0);
                String valuenames = sb.toString().replace("''", "'");
                List<CourseModel> digitalAssetsMasterCategoryList = courseCatTagFilterRepository.getCategorybySelectedTags(valuenames,CATS);
                reloadCatSlider(digitalAssetsMasterCategoryList);
            } else {
                List<CourseModel> digitalAssetsMasterCategoryList = courseCatTagFilterRepository.getAllCategory(CATS);
                reloadCatSlider(digitalAssetsMasterCategoryList);
            }
        }catch (Exception e){

        }
    }

    public void loadTagsSlider(){
        try{
            toggleselection(2);
            if(catlist.size()>0){
                String catnames = "";
                for (String catname : catlist) {
                    catnames = catnames + "," + "'" + catname.toString() + "'";
                }
                StringBuilder sb = new StringBuilder(catnames);
                sb.deleteCharAt(0);
                String valuenames = sb.toString().replace("''", "'");
                List<CourseModel> digitalAssetsMasterTagsList = courseCatTagFilterRepository.getTagsbySelectedcategorey(valuenames,TAGS);
                reloadTagSlider(digitalAssetsMasterTagsList);
            }else{
                List<CourseModel> digitalAssetsMasterTagsList = courseCatTagFilterRepository.getAllTags(TAGS);
                reloadTagSlider(digitalAssetsMasterTagsList);
            }
        }catch (Exception e){

        }

    }

    public void reloadCatSlider(List<CourseModel> newmodel){
        List<CourseModel> catrefresh = new ArrayList<>();
        List<CourseModel> newrefreshed = new ArrayList<>();
        for(CourseModel existing : digitalAssetsMasterCategoryLists){
            if(existing.iscatchecked){
                catrefresh.add(existing);
            }
        }
        for(CourseModel ne : newmodel){
            catrefresh.add(ne);
        }
        emptytagsview.setVisibility(View.GONE);
        cattag_recyclerView.setVisibility(View.VISIBLE);
        digitalAssetsMasterCategoryLists = catrefresh;
        categoryListRecyclerAdapter = new CourseCategoryListAdapter(mContext, digitalAssetsMasterCategoryLists,true);
        cattag_recyclerView.setAdapter(categoryListRecyclerAdapter);
    }

    public void reloadTagSlider(List<CourseModel> newmodel){
        List<CourseModel> tagrefresh = new ArrayList<>();
        List<CourseModel> newtgrefresh = new ArrayList<>();
        for(CourseModel existing : digitalAssetsMasterTagsLists){
            if(existing.istagchecked){
                tagrefresh.add(existing);
            }
        }
        for(CourseModel ne : newmodel){
            tagrefresh.add(ne);
        }
        digitalAssetsMasterTagsLists = tagrefresh;
        showtagsView(digitalAssetsMasterTagsLists,true);
    }

    public void showtagsView(List<CourseModel> digitalAssetsMasterTagsList,boolean status){
        if(digitalAssetsMasterTagsList.size() > 0){
            emptytagsview.setVisibility(View.GONE);
            checkBoxGroupView.setVisibility(View.GONE);
            cattag_recyclerView.setVisibility(View.VISIBLE);
            tagsListRecyclerAdapter = new CourseTagsListAdapter(mContext,digitalAssetsMasterTagsList,status);
            cattag_recyclerView.setAdapter(tagsListRecyclerAdapter);
        }else{
            cattag_recyclerView.setVisibility(View.GONE);
            checkBoxGroupView.setVisibility(View.GONE);
            emptytagsview.setVisibility(View.VISIBLE);
        }
    }

    public void insertintocategoreytagstable(){
        List<CourseModel> digitalAssetsListfortags = courseModelList;
        List<CourseModel> cttags = new ArrayList<>();
        for (CourseModel dig : digitalAssetsListfortags) {
            if(dig.getCategory_Name() != null && !dig.getCategory_Name().isEmpty()){
                if(dig.getCourse_Tags() != "" && dig.getCourse_Tags() != null && !dig.getCourse_Tags().isEmpty()) {
                    String tagsstring = dig.getCourse_Tags().replace("^","#");
                    String[] tags = tagsstring.split("#");
                    for (String t : tags) {
                        CourseModel values = new CourseModel();
                        values.setCourse_Id(dig.getCourse_Id());
                        values.setCourse_Category_Id(dig.getCourse_Category_Id());
                        values.setCourse_Tags(dig.getCourse_Tags());
                        values.setCategory_Name(dig.getCategory_Name());
                        values.setTags(t);
                        cttags.add(values);
                    }
                }else{
                    CourseModel values = new CourseModel();
                    values.setCourse_Id(dig.getCourse_Id());
                    values.setCourse_Category_Id(dig.getCourse_Category_Id());
                    values.setCourse_Tags(dig.getCourse_Tags());
                    values.setCategory_Name(dig.getCategory_Name());
                    values.setTags(null);
                    cttags.add(values);
                }
            }
        }
        courseCatTagFilterRepository.catTagsBulkInsert(cttags);
    }

    public void filteredcatList(CourseModel cat){
        try{
            if(cat.iscatchecked()) {
                catlist.add(cat.getCategory_Name());
            }else{
                catlist.remove(cat.getCategory_Name());
            }
            if(catlist.size() > 0){
                catFiltered = true;
            }else{
                catFiltered = false;
            }

            if(catFiltered){
                String catnames = "";
                for (String catname : catlist) {
                    catnames = catnames + "," + "'" + catname.toString() + "'";
                }
                StringBuilder sb = new StringBuilder(catnames);
                sb.deleteCharAt(0);
                String valuenames = sb.toString().replace("''", "'");
                CATS = valuenames;
            }else{
                CATS = "";
            }
            showApplyButton();
        }catch (Exception e){

        }
    }

    public void filteredtagList(CourseModel cat){
        try{
            if(cat.istagchecked()) {
                tagslist.add(cat.getTags());
            }else{
                tagslist.remove(cat.getTags());
            }
            if(tagslist.size() > 0){
                tagfiltered = true;
            }else{
                tagfiltered = false;
            }

            if(tagfiltered) {
                String tagnames = "";
                for (String catname : tagslist) {
                    tagnames = tagnames + "," + "'" + catname.toString() + "'";
                }
                StringBuilder sb = new StringBuilder(tagnames);
                sb.deleteCharAt(0);
                String valuenames = sb.toString().replace("''", "'");
                TAGS = valuenames;
            }else{
                TAGS = "";
            }
            showApplyButton();
        }catch(Exception e){

        }
    }

    public void getCoursesbyIDs(){
        try{
            String selectQuery = "SELECT tbl_COURSE_CAT_TAG_MASTER.Course_Id FROM tbl_COURSE_CAT_TAG_MASTER  ";
            if (CATS.length() > 0 || TAGS.length() > 0) {
                selectQuery += "WHERE ";
                if (CATS.length() > 0) {
                    selectQuery += "tbl_COURSE_CAT_TAG_MASTER.Category_Name in ("+CATS+") ";
                }
                if (TAGS.length() > 0) {
                    if (CATS.length() > 0) {
                        selectQuery += " AND ";
                    }
                    selectQuery += "tbl_COURSE_CAT_TAG_MASTER.Tags in ("+TAGS+") ";
                }
            }
            selectQuery += " Group by Course_Id";
            List<CourseModel> DAIDlist = courseCatTagFilterRepository.getAssetIdbySelectedCatTag(selectQuery);
            filteredlist(DAIDlist);
            /*String query = "";
            if(CATS.length() > 0 && TAGS.length() == 0){
                query = "select tbl_COURSE_CAT_TAG_MASTER.Course_Id from tbl_COURSE_CAT_TAG_MASTER where " +
                        "tbl_COURSE_CAT_TAG_MASTER.Category_Name in ("+CATS+") Group by Course_Id";
            } else if(TAGS.length() > 0 && CATS.length() == 0){
                query = "select tbl_COURSE_CAT_TAG_MASTER.Course_Id from tbl_COURSE_CAT_TAG_MASTER where " +
                        "tbl_COURSE_CAT_TAG_MASTER.Tags in ("+TAGS+") Group by Course_Id";
            } else if(CATS.length() > 0 && TAGS.length() > 0){
                query = "select tbl_COURSE_CAT_TAG_MASTER.Course_Id from tbl_COURSE_CAT_TAG_MASTER where " +
                        "tbl_COURSE_CAT_TAG_MASTER.Tags in ("+TAGS+") AND tbl_COURSE_CAT_TAG_MASTER.Category_Name in ("+CATS+") Group by Course_Id";
            }
            List<CourseModel> DAIDlist = courseCatTagFilterRepository.getAssetIdbySelectedCatTag(query);
            filteredlist(DAIDlist);*/
        }catch(Exception e){

        }
    }

    public void filteredlist(List<CourseModel> DAIDlist){
        try{
            if(DAIDlist != null) {
                if(DAIDlist.size() > 0){
                    digitalAssetsMasterListfilterd = new ArrayList<>();
                    for (CourseModel cms : DAIDlist) {
                        for (CourseModel cs : courseModelList) {
                            if (cms.getCourse_Id() == cs.getCourse_Id()) {
                                digitalAssetsMasterListfilterd.add(cs);
                            }
                        }
                    }
                    loadFilteredList(digitalAssetsMasterListfilterd);
                }
            }else{
                loadFilteredList(courseModelList);
            }
        }catch (Exception e){
            Log.e("Exception",e.toString());
        }

    }

    public void showApplyButton(){
        if(catFiltered || tagfiltered || mCheckCompleted.isChecked() || mCheckYetToStart.isChecked() || mCheckInProgress.isChecked() || mCheckExpired.isChecked()
                || mCheckMaxAttempts.isChecked()){
            applyfilters.setVisibility(View.VISIBLE);
            applyfilters.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            applyfilters.setEnabled(true);
        }else{
            applyfilters.setBackgroundColor(Color.parseColor(Constants.GREY_COLOR));
            applyfilters.setEnabled(false);
        }
    }

    public void toggleselection(int num){
        cattagmenus.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        if(num == 1){
            filterheadingtext.setText(getResources().getString(R.string.category));
            catselection.setBackgroundColor(Color.WHITE);
            icon_cats.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
            icon_tags.setColorFilter(Color.WHITE);
            icon_filter.setColorFilter(Color.WHITE);
            tagselection.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
            filterlay.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        }else if(num == 2){
            filterheadingtext.setText(getResources().getString(R.string.Tags));
            tagselection.setBackgroundColor(Color.WHITE);
            icon_tags.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
            icon_cats.setColorFilter(Color.WHITE);
            icon_filter.setColorFilter(Color.WHITE);
            filterlay.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
            catselection.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        }else if(num == 3) {
            emptytagsview.setVisibility(View.GONE);
            filterheadingtext.setText(getResources().getString(R.string.status));
            filterlay.setBackgroundColor(Color.WHITE);
            icon_filter.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
            icon_tags.setColorFilter(Color.WHITE);
            icon_cats.setColorFilter(Color.WHITE);
            catselection.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
            tagselection.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        }
    }

    public void showhideemptystate(boolean showempty,String message,int type){
        if(showempty){
            mEmptyView.setVisibility(View.VISIBLE);
            if(type == Constants.INTERNETERRORNUM){
                emptyimage.setVisibility(View.VISIBLE);
                emptyimage.setImageResource(R.drawable.interenet_error_image);
                retrybutton.setVisibility(View.VISIBLE);
            }else if(type == Constants.NORESULTSNUM){
                emptyimage.setVisibility(View.VISIBLE);
                emptyimage.setImageResource(R.drawable.no_results);
                retrybutton.setVisibility(View.GONE);
            }else if(type == Constants.EMPTYLISTNUM){
                emptyimage.setVisibility(View.GONE);
                retrybutton.setVisibility(View.GONE);
            }
            emptymessage.setText(message.toString());
        }else{
            mEmptyView.setVisibility(View.GONE);
        }
    }

    public String UploadAnswerProcess(ArrayList<QuestionAndAnswerModel> questionandanswerlist) {

        AnwerUploadModel answerupload =  new AnwerUploadModel();
        answerupload.setLstCourseUserAnswers(new Gson().toJson(getCourseUserAnswer(questionandanswerlist)));
        answerupload.setLstCourseUserAssessHeader(new Gson().toJson(getCourseUserAssetHeader(questionandanswerlist)));
        answerupload.setLstCourseUserAssessDet(new Gson().toJson(getCourseAssetDetails(questionandanswerlist)));
        Log.d("test",new Gson().toJson(answerupload));
        return new Gson().toJson(answerupload);

    }

    private List<CourseUserAssessHeader> getCourseUserAssetHeader(ArrayList<QuestionAndAnswerModel> questionandanswerlist) {

        List<CourseUserAssessHeader> courseuserAssessHeaderList = new ArrayList<>();
        CourseUserAssessHeader courseAssetHeader = new CourseUserAssessHeader();

        courseAssetHeader.Company_Id = String.valueOf(PreferenceUtils.getCompnayId(this));
        QuestionCourseListModel questioncoursemodel = questionandanswerlist.get(0).getLstCourse().get(0);
        courseAssetHeader.Course_ID = questioncoursemodel.getCourse_Id();
        courseAssetHeader.Course_Section_User_Exam_Id = 1;
        courseAssetHeader.Course_User_Assignment_Id = questionandanswerlist.get(0).getCourseAssignmentId();
        courseAssetHeader.Couse_User_Section_Mapping_Id = questionandanswerlist.get(0).getSectionMapId();
        courseAssetHeader.Section_ID = questionandanswerlist.get(0).getSectionId();
        courseAssetHeader.User_Id = PreferenceUtils.getUserId(this);
        courseAssetHeader.Publish_ID = questioncoursemodel.getPublish_ID();
        courseAssetHeader.Achieved_Percentage = 0;
        courseAssetHeader.Pass_Percentage = String.valueOf(questioncoursemodel.getPass_Percentage());

        if (courseAssetHeader.Achieved_Percentage >= questioncoursemodel.getPass_Percentage()) {
            courseAssetHeader.Is_Qualified = 1;
        } else {

            courseAssetHeader.Is_Qualified = 0;

        }
        courseAssetHeader.Local_TimeZone = new Date().toString();
        //courseAssetHeader.Offset_Value = CommonUtils.getUtcOffset();
        courseAssetHeader.Offset_Value = CommonUtils.getUtcOffsetincluded10k();
        courseuserAssessHeaderList.add(courseAssetHeader);

        return courseuserAssessHeaderList;
    }

    private List<CourseUserAnswers> getCourseUserAnswer(ArrayList<QuestionAndAnswerModel> questionandanswerlist) {


        List<CourseUserAnswers> courseuseranswerList =  new ArrayList<>();
        for (QuestionAndAnswerModel questionandanswermodel : questionandanswerlist){

            if (questionandanswermodel.getQuestionModel().getQuestion_Type() != 1){

                CourseUserAnswers courseuseranswer = new CourseUserAnswers();
                if (questionandanswermodel.getQuestionModel().getQuestion_Type() == 6)
                {
                    courseuseranswer.Company_Id = String.valueOf(PreferenceUtils.getCompnayId(this));
                    courseuseranswer.User_Answer_Text = questionandanswermodel.getChoosenAnswer();
                    courseuseranswer.User_Id = PreferenceUtils.getUserId(this);
                    courseuseranswer.Question_Id = String.valueOf(questionandanswermodel.getQuestionModel().getQuestion_Id());
                }
                else if (questionandanswermodel.getQuestionModel().getQuestion_Type() == 0)
                {
                    courseuseranswer.Company_Id = String.valueOf(PreferenceUtils.getCompnayId(this));
                    courseuseranswer.User_Answer_Text = questionandanswermodel.getChoosenAnswer();
                    courseuseranswer.User_Id = PreferenceUtils.getUserId(this);
                    courseuseranswer.Question_Id = String.valueOf(questionandanswermodel.getQuestionModel().getQuestion_Id());
                }
                else if (questionandanswermodel.getQuestionModel().getQuestion_Type() !=2){

                    courseuseranswer.Company_Id = String.valueOf(PreferenceUtils.getCompnayId(this));
                    courseuseranswer.Text = questionandanswermodel.getChoosenAnswer();
                    courseuseranswer.User_Id = PreferenceUtils.getUserId(this);
                    courseuseranswer.Question_Id = String.valueOf(questionandanswermodel.getQuestionModel().getQuestion_Id());

                }else {

                    courseuseranswer.Company_Id = String.valueOf(PreferenceUtils.getCompnayId(this));
                    courseuseranswer.Answer_Id = questionandanswermodel.getChoosenAnswerId();
                    courseuseranswer.User_Id = PreferenceUtils.getUserId(this);
                    courseuseranswer.Question_Id = String.valueOf(questionandanswermodel.getQuestionModel().getQuestion_Id());

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
                }else {
                    courseuserasset.Count_of_User_Answers = 0;
                }
                if (questionanswermodel.getChoosenAnswer() != null && questionanswermodel.getChoosenAnswer().length() > 0) {

                    if (questionanswermodel.getChoosenAnswer().equalsIgnoreCase(questionanswermodel.getCorrectAnswer())) {
                        courseuserasset.Is_Correct = true;
                        courseuserasset.Count_Of_User_Correct_Answers = 1;
                    } else {
                        courseuserasset.Is_Correct = false;
                        courseuserasset.Count_Of_User_Correct_Answers = 0;

                    }

                }

                QuestionCourseListModel CourseHeader = questionanswermodel.getLstCourse().get(0);
                courseuserasset.Course_ID = CourseHeader.getCourse_Id();
                courseuserasset.User_Id = PreferenceUtils.getUserId(this);
                courseuserasset.Publish_ID = CourseHeader.getPublish_ID();
                courseuserasset.Section_Id = questionanswermodel.getSectionId();
                courseuserasset.Question_ID = String.valueOf(questionanswermodel.getQuestionModel().getQuestion_Id());
                courseuserasset.Couse_User_Section_Mapping_Id = questionanswermodel.getSectionMapId();
                courseuserasset.Course_User_Assignment_Id = questionanswermodel.getCourseAssignmentId();
                courseuserasset.Negative_Mark = questionanswermodel.getQuestionModel().getNegative_Mark();
                courseuserassetdetails.add(courseuserasset);

            } else {

                if (questionanswermodel.getChoosenAnswer() != null && questionanswermodel.getChoosenAnswer().length() > 0
                        && questionanswermodel.getCorrectAnswer() != null && questionanswermodel.getCorrectAnswer().length() > 0) {

                    String[] choosenanswerlist = questionanswermodel.getChoosenAnswer().split(",");
                    String[] coreectanserlist = questionanswermodel.getCorrectAnswer().split(",");

                    if (choosenanswerlist!=null){
                        courseuserasset.Count_of_User_Answers = choosenanswerlist.length;
                    }

                    if (compareArrays(choosenanswerlist, coreectanserlist)) {
                        courseuserasset.Is_Correct = true;
                        courseuserasset.Count_Of_User_Correct_Answers = coreectanserlist.length;
                    } else {
                        courseuserasset.Is_Correct = false;
                        courseuserasset.Count_Of_User_Correct_Answers =  getUserCorrectAnswer(questionanswermodel);
                    }
                }

                QuestionCourseListModel CourseHeader = questionanswermodel.getLstCourse().get(0);
                courseuserasset.Course_ID = CourseHeader.getCourse_Id();
                courseuserasset.User_Id = PreferenceUtils.getUserId(this);
                courseuserasset.Publish_ID = CourseHeader.getPublish_ID();
                courseuserasset.Section_Id = questionanswermodel.getSectionId();
                courseuserasset.Question_ID = String.valueOf(questionanswermodel.getQuestionModel().getQuestion_Id());
                courseuserasset.Couse_User_Section_Mapping_Id = questionanswermodel.getSectionMapId();
                courseuserasset.Course_User_Assignment_Id = questionanswermodel.getCourseAssignmentId();
                courseuserasset.Negative_Mark = questionanswermodel.getQuestionModel().getNegative_Mark();
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
    public void Checknetworkandupload(final String AnswerModelString, final boolean isLastQuestion, final boolean isTimeout, final boolean isBackpressed){

        if(NetworkUtils.checkIfNetworkAvailable(CourseListActivity.this)){
            if (!isBackpressed){
                showProgressDialog();
            }
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            LPCourseService lpService = retrofitAPI.create(LPCourseService.class);
            int CompanyId  = PreferenceUtils.getCompnayId(this);
            String  SubdomainName = PreferenceUtils.getSubdomainName(this);
            int UserId = PreferenceUtils.getUserId(this);
            Gson gsonget = new Gson();
            AnwerUploadModel answermodel = gsonget.fromJson(AnswerModelString,AnwerUploadModel.class);
            Call call = lpService.insertTestCourseResponse(SubdomainName,CompanyId,UserId,QuestionLoadCount,isLastQuestion,isTimeout,answermodel);
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    String courseAssetListModel= response.body();
                    if (courseAssetListModel != null) {

                        if (!isBackpressed){
                            dismissProgressDialog();
                            if (courseAssetListModel.contains("COMPLETED")){
                                PreferenceUtils.setQuestionAnswerList("key",null,CourseListActivity.this);
                                if (isTimeout){
                                    // ShowAlert(getResources().getString(R.string.time_Out),getResources().getString(R.string.warning),false);
                                }else {
                                    //ShowAlert(getResources().getString(R.string.finished),"",false);

                                }
                            }else {

                                Log.d("error","error");
                            }

                        }
                        //COMPLETED~1~Your course has been partially submitted.~549
                    }
                }
                @Override
                public void onFailure(Throwable t) {

                  /*  testResultRepository.insertTestRecord(AnswerModelString,CalculatePercentage(),QuestionLoadCount,isLastQuestion+"",isTimeout+"");
                    ShowAlert(getResources().getString(R.string.testsavedoffline),getResources().getString(R.string.warning),true);*/
                    Log.d(t.toString(),"Error");
                }
            });
        }else{

//            testResultRepository.insertTestRecord(AnswerModelString,CalculatePercentage(),QuestionLoadCount,isLastQuestion+"",isTimeout+"");
//            ShowAlert(getResources().getString(R.string.testsavedoffline),getResources().getString(R.string.warning),true);
        }
    }

}