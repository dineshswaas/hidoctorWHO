package com.swaas.kangle.TaskModule;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.swaas.kangle.API.model.LandingPageAccess;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.AssetListActivity;
import com.swaas.kangle.CheckList.ChecklistLandingActivity;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.LPCourse.CourseListActivity;
import com.swaas.kangle.MoreMenuActivity;
import com.swaas.kangle.Notification.NotificationActivity;
import com.swaas.kangle.Notification.NotificationModel;
import com.swaas.kangle.Notification.NotificationTempRepository;
import com.swaas.kangle.R;
import com.swaas.kangle.TaskModule.adapter.TasklistItemAdapter;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.NetworkUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TaskListActivity extends AppCompatActivity {

    Context mContext;

    ImageView companylogo,create_Task_icon;
    RelativeLayout parent_view;
    LinearLayout header;

    ArrayList<TaskListModel> taskListModels;
    ArrayList<TaskGroupListModel> taskgroupListModels,temp;

    TasklistItemAdapter tasklistItemAdapter,myTeamTasklistAdapter,reviewTasklistAdapter;

    LinearLayoutManager LL1,LL2,LL3;

    EmptyRecyclerView myTaskrecycler,myTeamrecycler,reviewrecycler;
    RelativeLayout tasklst_view,content_view;

    View lpcourse,assetpage,chklistpage,profilepage,bottommenusection,taskpage;
    LinearLayout bottommenus;
    ImageView pos3;
    TextView higlighttext;
    ProgressDialog mProgressDialog;

    View Review,my_team;
    Gson gsonget;
    TextView Reviewtext,my_teamtext;
    ImageView Reviewarrow,my_teamarrow;

    LinearLayout my_list,group_list;
    View my_list_view,my_grplist_view;
    TextView my_list_text,my_grplist_text;

    ImageView notification,chatview;
    View notificationsec,chatviewsec;
    TextView notificationcount,chatcount;
    LinearLayout tabmenusection;
    View tabsection;
    RelativeLayout showhidereviewcontent,showhidemy_teamcontent,my_team_empty_view,review_empty_view,tasklst_empty_view;
    int checkEnabledTab = 1;
    User userobj;

    ImageView filter,search;
    boolean mCheckOpen,mCheckInprogress,mCheckReview,mCheckComplete;
    CheckBox openCheck,InprogressCheck,reviewCheck,completedCheck;
    View openCheck_view,InprogressCheck_view,reviewCheck_view,completedCheck_view;

    RelativeLayout filtersection;
    ImageView close_filter,icon_cats;
    View cattagmenus;
    TextView filterheadingtext,filtered_text,clear_assetfilter,cat_filtered_count,
            clearfilters,applyfilters;
    View catselection,assetfilterheading;

    RelativeLayout check_box;
    ImageView tickfilter;
    View clear_assetfilter_img;
    //  TextView emptytext;

    boolean isFilterenabled;

    LinearLayout searchlayout;
    ImageView icon_search,closesearch;
    SearchView msearchtext;
    SearchManager searchManager;
    TextView course,asset,checklist,task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        mContext = TaskListActivity.this;

        taskListModels = new ArrayList<>();
        taskgroupListModels = new ArrayList<>();
        temp = new ArrayList<>();
        gsonget = new Gson();
        initializeView();

        userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);
        setUpRecyclerView();
        initializebottomnavigation();
        bottomnavigationonClickevents();
        onClicklisteners();
        setupthemeView();
        getnotificationcount();
        if(PreferenceUtils.getTASK_HASCHILDUSERS(mContext) == null){
            checkForChildUsers();
        }else{
            loadViews();
        }
        if(PreferenceUtils.getTASK_ALLOW_COMPLETE(mContext) == null){
            getAdminlevelUserlistApi();
        }
        //getMyListRecords();
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

    public void initializeView(){
        companylogo = (ImageView) findViewById(R.id.companylogo);
        create_Task_icon = (ImageView) findViewById(R.id.create_Task_icon);

        parent_view = (RelativeLayout) findViewById(R.id.parent_view);
        header = (LinearLayout) findViewById(R.id.header);
        tasklst_view = (RelativeLayout) findViewById(R.id.tasklst_view);
        content_view = (RelativeLayout) findViewById(R.id.content_view);

        myTaskrecycler = (EmptyRecyclerView) findViewById(R.id.myTaskrecycler);
        reviewrecycler = (EmptyRecyclerView) findViewById(R.id.reviewtaskrecyclerView);
        myTeamrecycler = (EmptyRecyclerView) findViewById(R.id.myteamtaskrecyclerView);

        myTaskrecycler.setNestedScrollingEnabled(false);
        reviewrecycler.setNestedScrollingEnabled(false);
        myTeamrecycler.setNestedScrollingEnabled(false);

        Review = findViewById(R.id.Review);
        Reviewtext = (TextView) findViewById(R.id.Reviewtext);
        Reviewarrow = (ImageView) findViewById(R.id.Reviewarrow);

        my_team = findViewById(R.id.my_team);
        my_teamtext = (TextView) findViewById(R.id.my_teamtext);
        my_teamarrow = (ImageView) findViewById(R.id.my_teamarrow);

        my_list = (LinearLayout) findViewById(R.id.my_list);
        my_list_view = findViewById(R.id.my_list_view);
        my_list_text = (TextView) findViewById(R.id.my_list_text);
        group_list = (LinearLayout) findViewById(R.id.group_list);
        my_grplist_view = findViewById(R.id.my_grplist_view);
        my_grplist_text = (TextView) findViewById(R.id.my_grplist_text);

        showhidereviewcontent = (RelativeLayout) findViewById(R.id.showhidereviewcontent);
        showhidemy_teamcontent = (RelativeLayout) findViewById(R.id.showhidemy_teamcontent);
        my_team_empty_view = (RelativeLayout) findViewById(R.id.my_team_empty_view);
        review_empty_view = (RelativeLayout) findViewById(R.id.review_empty_view);
        tasklst_empty_view = (RelativeLayout) findViewById(R.id.tasklst_empty_view);

        notification= (ImageView) findViewById(R.id.notification);
        chatview = (ImageView) findViewById(R.id.chatview);
        notificationsec = findViewById(R.id.notificationsec);
        chatviewsec = findViewById(R.id.chatviewsec);
        notificationcount = (TextView) findViewById(R.id.notificationcount);
        chatcount = (TextView) findViewById(R.id.chatcount);

        tabmenusection = (LinearLayout) findViewById(R.id.tabmenusection);
        tabsection = findViewById(R.id.tabsection);

        filter = (ImageView) findViewById(R.id.filter_icon);
        search = (ImageView) findViewById(R.id.icon_search);

        filtersection = (RelativeLayout) findViewById(R.id.filtersection);
        close_filter = (ImageView) findViewById(R.id.close_filter);
        catselection = findViewById(R.id.catselection);
        clearfilters = (TextView) findViewById(R.id.clearfilters);
        applyfilters = (TextView) findViewById(R.id.applyfilters);
        assetfilterheading =  findViewById(R.id.assetfilterheading);
        filtered_text = (TextView) findViewById(R.id.filtered_text);
        clear_assetfilter = (TextView) findViewById(R.id.clear_assetfilter);
        cat_filtered_count = (TextView) findViewById(R.id.cat_filtered_count);
        filterheadingtext = (TextView)findViewById(R.id.filterheadingtext);
        cattagmenus = findViewById(R.id.cattagmenus);
        icon_cats = (ImageView)findViewById(R.id.icon_cats);
        tickfilter = (ImageView) findViewById(R.id.tickfilter);
        clear_assetfilter_img = findViewById(R.id.clear_assetfilter_img);
        //    emptytext = (TextView) findViewById(R.id.emptytext);

        openCheck_view = findViewById(R.id.openCheck_view);
        InprogressCheck_view = findViewById(R.id.InprogressCheck_view);
        reviewCheck_view = findViewById(R.id.reviewCheck_view);
        completedCheck_view = findViewById(R.id.completedCheck_view);

        check_box = (RelativeLayout) findViewById(R.id.check_box);
        searchlayout = (LinearLayout) findViewById(R.id.searchlayout);
        msearchtext = (SearchView) findViewById(R.id.searchtext);
        closesearch = (ImageView) findViewById(R.id.closesearch);
        msearchtext = (SearchView) findViewById(R.id.searchtext);
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        openCheck = (CheckBox) findViewById(R.id.openCheck);
        InprogressCheck = (CheckBox) findViewById(R.id.InprogressCheck);
        reviewCheck = (CheckBox) findViewById(R.id.reviewCheck);
        completedCheck = (CheckBox) findViewById(R.id.completedCheck);
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

    public void setUpRecyclerView(){
        LL1 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        LL2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        LL3 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        myTaskrecycler.setLayoutManager(LL1);
        myTeamrecycler.setLayoutManager(LL2);
        reviewrecycler.setLayoutManager(LL3);
    }

    public void setupthemeView(){
        String logo = PreferenceUtils.getClientLogo(mContext);
        Ion.with(companylogo).placeholder(R.color.topbar).error(R.color.topbar).load(
                (!TextUtils.isEmpty(logo))? logo : logo);
        File imgFile = new File("/storage/sdcard0/SwaaS LMS/companylogo.jpg");

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            companylogo.setImageBitmap(myBitmap);

        }
        header.setBackgroundColor(Color.parseColor(Constants.HEADERBAR_COLOR));
        parent_view.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        pos3.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        // higlighttext.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
        Reviewtext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        my_teamtext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        my_teamarrow.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        Reviewarrow.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));

        notification.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));
        chatview.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));

        create_Task_icon.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));

        filter.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));
        search.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));

        openCheck.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        InprogressCheck.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        reviewCheck.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        completedCheck.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));

        closesearch.setBackgroundColor(Color.parseColor(Constants.HEADERBAR_COLOR));
        closesearch.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));

        toggletabs();
        togglefilterselection();
    }

    public void onClicklisteners(){
        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        my_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEnabledTab = 1;
                toggletabs();
                tasklst_view.setVisibility(View.VISIBLE);
                content_view.setVisibility(View.GONE);
                getMyListRecords();
            }
        });

        group_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEnabledTab = 2;
                toggletabs();
                content_view.setVisibility(View.VISIBLE);
                tasklst_view.setVisibility(View.GONE);
                getMyTeamlistRecords();
            }
        });

        Review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showhidereviewcontent.isShown()){
                    showhidereviewcontent.setVisibility(View.GONE);
                    Reviewarrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_36dp));
                }else{
                    showhidereviewcontent.setVisibility(View.VISIBLE);
                    Reviewarrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_36dp));
                }
            }
        });

        my_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showhidemy_teamcontent.isShown()){
                    showhidemy_teamcontent.setVisibility(View.GONE);
                    my_teamarrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_36dp));
                }else{
                    showhidemy_teamcontent.setVisibility(View.VISIBLE);
                    my_teamarrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_36dp));
                }
            }
        });

        notificationsec.setOnClickListener(new View.OnClickListener() {
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

        create_Task_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext,CreateTaskActivity.class);
                i.putExtra("fromquestion",false);
                i.putExtra("isfromcoursechecklist",false);
                i.putExtra("isFromTaskEdit",false);
                startActivity(i);
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
                    filtersection.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(mContext,"Enable your network ",Toast.LENGTH_SHORT).show();
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchlayout.setVisibility(View.VISIBLE);
                tabsection.setVisibility(View.GONE);
            }
        });

        applyfilters.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    applyfilters.setBackgroundColor(Color.WHITE);
                    applyfilters.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    applyfilters.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
                    applyfilters.setTextColor(Color.WHITE);
                    filtersection.setVisibility(View.GONE);
                    tabsection.setVisibility(View.GONE);
                    assetfilterheading.setVisibility(View.VISIBLE);
                    bottommenusection.setVisibility(View.GONE);
                    String htmlString="<u>"+getResources().getString(R.string.clearfilter)+"</u>";
                    filtered_text.setText(Html.fromHtml(htmlString));
                    isFilterenabled = true;
                    try {
                        loadFilteredList();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

        clearfilters.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    clearfilters.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
                    clearfilters.setTextColor(Color.WHITE);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    clearfilters.setBackgroundColor(Color.WHITE);
                    clearfilters.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
                    clearFilters();
                    filtersection.setVisibility(View.GONE);
                    bottommenusection.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        close_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFilterenabled){
                    filtersection.setVisibility(View.GONE);
                    assetfilterheading.setVisibility(View.VISIBLE);
                    bottommenusection.setVisibility(View.GONE);
                }else {
                    filtersection.setVisibility(View.GONE);
                    bottommenusection.setVisibility(View.VISIBLE);
                    showApplyButton();
                }
            }
        });

        filtered_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtersection.setVisibility(View.GONE);
                assetfilterheading.setVisibility(View.GONE);
                isFilterenabled = false;
                bottommenusection.setVisibility(View.VISIBLE);
                clearFilters();
            }
        });

        clear_assetfilter_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtersection.setVisibility(View.VISIBLE);
                bottommenusection.setVisibility(View.GONE);
                assetfilterheading.setVisibility(View.GONE);
            }
        });

        checkboxclicks();
        searchClicks();
    }

    public void checkboxclicks(){

        openCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showApplyButton();
            }
        });

        InprogressCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showApplyButton();
            }
        });

        reviewCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showApplyButton();
            }
        });
        completedCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showApplyButton();
            }
        });

        openCheck_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(openCheck.isChecked()){
                    openCheck.setChecked(false);
                }else{
                    openCheck.setChecked(true);
                }
                showApplyButton();
            }
        });

        InprogressCheck_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InprogressCheck.isChecked()){
                    InprogressCheck.setChecked(false);
                }else{
                    InprogressCheck.setChecked(true);
                }
                showApplyButton();
            }
        });

        reviewCheck_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reviewCheck.isChecked()){
                    reviewCheck.setChecked(false);
                }else{
                    reviewCheck.setChecked(true);
                }
                showApplyButton();
            }
        });

        completedCheck_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(completedCheck.isChecked()){
                    completedCheck.setChecked(false);
                }else{
                    completedCheck.setChecked(true);
                }
                showApplyButton();
            }
        });
    }

    public void reloadPage(){
        tabsection.setVisibility(View.VISIBLE);
        if(checkEnabledTab == 2){
            getMyTeamlistRecords();
        }else{
            getMyListRecords();
        }
    }

    public void searchClicks(){
        closesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchlayout.setVisibility(View.GONE);
                reloadPage();
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
                if(checkEnabledTab == 2){
                    loadMyGroupTaskSearchdata(newText);
                }else {
                    loadMyTaskSearchdata(newText);
                }
                return false;
            }
        });
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
    }

    public void checkForChildUsers(){
        int CompanyId = PreferenceUtils.getCompnayId(mContext);
        String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
        String usercode = userobj.getUser_Code();
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            TaskServices taskServices = retrofit.create(TaskServices.class);

            Call call = taskServices.getChildUserdetailsTaskApi(SubdomainName, CompanyId, usercode);
            call.enqueue(new Callback<ArrayList<TaskListModel>>() {

                @Override
                public void onResponse(Response<ArrayList<TaskListModel>> response, Retrofit retrofit) {
                    ArrayList<TaskListModel> mymodels = response.body();
                    if (mymodels != null && mymodels.size() > 0) {
                        PreferenceUtils.setTASK_HASCHILDUSERS(mContext,"true");
                    } else {
                        PreferenceUtils.setTASK_HASCHILDUSERS(mContext,"false");
                    }
                    loadViews();
                }
                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(), "Error");
                }
            });
        }
    }

    public void loadViews(){
        if(PreferenceUtils.getTASK_HASCHILDUSERS(mContext).equalsIgnoreCase("true")){
            tabmenusection.setWeightSum(2);
            create_Task_icon.setVisibility(View.VISIBLE);
            group_list.setVisibility(View.VISIBLE);
        }else{
            tabmenusection.setWeightSum(1);
            create_Task_icon.setVisibility(View.GONE);
            group_list.setVisibility(View.GONE);
        }
    }

    public void getAdminlevelUserlistApi(){
        int CompanyId = PreferenceUtils.getCompnayId(mContext);
        String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
        String usercode = userobj.getUser_Code();
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            TaskServices taskServices = retrofit.create(TaskServices.class);

            Call call = taskServices.fngetAdminlevelUserlistApi(SubdomainName, CompanyId, usercode);
            call.enqueue(new Callback<ArrayList<TaskListModel>>() {

                @Override
                public void onResponse(Response<ArrayList<TaskListModel>> response, Retrofit retrofit) {
                    ArrayList<TaskListModel> mymodels = response.body();
                    if (mymodels != null && mymodels.size() > 0) {
                        PreferenceUtils.setTASK_ALLOW_COMPLETE(mContext,"true");
                    } else {
                        PreferenceUtils.setTASK_ALLOW_COMPLETE(mContext,"false");
                    }
                }
                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(), "Error");
                }
            });
        }
    }

    public void getMyListRecords(){
        tasklst_view.setVisibility(View.VISIBLE);
        content_view.setVisibility(View.GONE);
        showProgressDialog();
        int CompanyId = PreferenceUtils.getCompnayId(mContext);
        String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
        int UserId = PreferenceUtils.getUserId(mContext);
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            TaskServices taskServices = retrofit.create(TaskServices.class);

            Call call = taskServices.getTaskListDataApi(SubdomainName, CompanyId, UserId);
            call.enqueue(new Callback<ArrayList<TaskListModel>>() {

                @Override
                public void onResponse(Response<ArrayList<TaskListModel>> response, Retrofit retrofit) {
                    ArrayList<TaskListModel> mymodels = response.body();
                    if (mymodels != null && mymodels.size() > 0) {
                        taskListModels = mymodels;
                        try {
                            if(isFilterenabled){
                                loadFilteredList();
                            }else {
                                loadMyTaskData(taskListModels);

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                            dismissProgressDialog();
                        }
                    } else {

                        dismissProgressDialog();
                        myTaskrecycler.setVisibility(View.GONE);
                        tasklst_empty_view.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(), "Error");
                    dismissProgressDialog();
                }
            });
        }
        else {
            dismissProgressDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(checkEnabledTab == 2){
            Reviewarrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_36dp));
            my_teamarrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_36dp));
            getMyTeamlistRecords();
        }else{
            if(NetworkUtils.checkIfNetworkAvailable(mContext))
            {
                getMyListRecords();
            }
        }
    }

    public void getMyTeamlistRecords(){
        tasklst_view.setVisibility(View.GONE);
        content_view.setVisibility(View.VISIBLE);
        int CompanyId = PreferenceUtils.getCompnayId(mContext);
        String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
        int UserId = PreferenceUtils.getUserId(mContext);
        String usercode = userobj.getUser_Code();
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            showProgressDialog();
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            TaskServices taskServices = retrofit.create(TaskServices.class);

            Call call = taskServices.getTaskListDataTeamApi(SubdomainName, CompanyId, UserId,usercode);
            call.enqueue(new Callback<ArrayList<TaskGroupListModel>>() {

                @Override
                public void onResponse(Response<ArrayList<TaskGroupListModel>> response, Retrofit retrofit) {
                    ArrayList<TaskGroupListModel> model = response.body();
                    if (model != null && model.size() > 0) {
                        taskgroupListModels = model;
                        try {
                            if(isFilterenabled){
                                loadFilteredList();
                            }else {
                                loadMyTeamTaskData(taskgroupListModels);
                                dismissProgressDialog();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        showemptyTeamView();
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
        else {
            dismissProgressDialog();
        }
    }

    public ArrayList<TaskListModel> FormatTaskdDate(ArrayList<TaskListModel> mtaskListModels) throws ParseException {
        ArrayList<TaskListModel> withformatedDate = new ArrayList<>();
        for(TaskListModel ts : mtaskListModels){
            String duedate = ts.getTask_Due_Date().toString();
            String onlydate = duedate;
            int t = onlydate.indexOf("T");
            String nd = onlydate.substring(0,t);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = (Date)formatter.parse(nd);
            SimpleDateFormat convertFormat = new SimpleDateFormat("dd-MMM-yyyy");
            ts.setFormatedDueDate(convertFormat.format(date));
            withformatedDate.add(ts);
        }
        //taskListModels = withformatedDate;
        return withformatedDate;
    }

    public ArrayList<TaskGroupListModel> FormatTeamTaskdDate(ArrayList<TaskGroupListModel> mtaskgroupListModels) throws ParseException {
        ArrayList<TaskGroupListModel> withformatedDate = new ArrayList<TaskGroupListModel>();
        TaskGroupListModel list = new TaskGroupListModel();
        ArrayList<TaskListModel> reviewformatedDate = new ArrayList<>();
        ArrayList<TaskListModel> otherformateddate = new ArrayList<>();
        if(mtaskgroupListModels!=null&&mtaskgroupListModels.size()>0
                && mtaskgroupListModels.get(0).getLstTaskListReview()!=null
                && mtaskgroupListModels.get(0).getLstTaskListReview().size()>0) {
            for (TaskListModel ts : mtaskgroupListModels.get(0).getLstTaskListReview()) {
                String duedate = ts.getTask_Due_Date().toString();
                String onlydate = duedate;
                int t = onlydate.indexOf("T");
                String nd = onlydate.substring(0, t);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = (Date) formatter.parse(nd);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat convertFormat = new SimpleDateFormat("dd-MMM-yyyy");
                ts.setFormatedDueDate(convertFormat.format(date));
                reviewformatedDate.add(ts);
            }
        }
        if(taskgroupListModels!=null&&taskgroupListModels.size()>0
                && taskgroupListModels.get(0).getLstTaskListOther()!=null&&taskgroupListModels.get(0).getLstTaskListOther().size()>0) {
            for (TaskListModel ts : taskgroupListModels.get(0).getLstTaskListOther()) {
                //for(TaskListModel ts : mtaskgroupListModels.get(0).getLstTaskListReview()){
                String duedate = ts.getTask_Due_Date().toString();
                String onlydate = duedate;
                int t = onlydate.indexOf("T");
                String nd = onlydate.substring(0, t);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = (Date) formatter.parse(nd);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat convertFormat = new SimpleDateFormat("dd-MMM-yyyy");
                ts.setFormatedDueDate(convertFormat.format(date));
                otherformateddate.add(ts);
            }
        }
        list.setLstTaskListReview(reviewformatedDate);
        list.setLstTaskListOther(otherformateddate);
        withformatedDate.add(list);
        //taskgroupListModels = withformatedDate;

        return withformatedDate;
    }

    public void loadMyTaskData(ArrayList<TaskListModel> taskListModels) throws ParseException {
        taskListModels = FormatTaskdDate(taskListModels);
        dismissProgressDialog();
        Collections.reverse(taskListModels);
        tasklistItemAdapter = new TasklistItemAdapter(mContext,taskListModels,Constants.My);
        myTaskrecycler.setVisibility(View.VISIBLE);
        tasklst_empty_view.setVisibility(View.GONE);
        myTaskrecycler.setAdapter(tasklistItemAdapter);
        loadadapterClick();
    }

    public void loadMyTeamTaskData(ArrayList<TaskGroupListModel> taskgroupListModels1) throws ParseException {
        temp.clear();
        temp = FormatTeamTaskdDate(taskgroupListModels1);
        dismissProgressDialog();
        showhidereviewcontent.setVisibility(View.GONE);
        showhidemy_teamcontent.setVisibility(View.GONE);
        if(taskgroupListModels1 != null) {
            if (taskgroupListModels1.get(0).getLstTaskListReview()!=null && taskgroupListModels1.get(0).getLstTaskListReview().size() > 0) {
                reviewrecycler.setVisibility(View.VISIBLE);
                review_empty_view.setVisibility(View.GONE);
                reviewTasklistAdapter = new TasklistItemAdapter(mContext,temp.get(0).getLstTaskListReview(),Constants.Review);
                reviewrecycler.setAdapter(reviewTasklistAdapter);

            }else{
                reviewrecycler.setVisibility(View.GONE);
                review_empty_view.setVisibility(View.VISIBLE);
            }
            if(taskgroupListModels1.get(0).getLstTaskListOther()!=null && taskgroupListModels1.get(0).getLstTaskListOther().size() > 0) {
                myTeamrecycler.setVisibility(View.VISIBLE);
                my_team_empty_view.setVisibility(View.GONE);
                myTeamTasklistAdapter = new TasklistItemAdapter (mContext,temp.get(0).getLstTaskListOther(), Constants.MyTeam);
                myTeamrecycler.setAdapter(myTeamTasklistAdapter);
            }else{
                myTeamrecycler.setVisibility(View.GONE);
                my_team_empty_view.setVisibility(View.VISIBLE);
            }
            mytemadapterClick();
        }
    }

    public void loadadapterClick(){
        tasklistItemAdapter.setOnItemClickListener(new TasklistItemAdapter.MyClickListener() {
            @Override
            public void onItemClick(TaskListModel tsk, int frompage) {
                getTaskDetailsData(tsk.getTask_Id(), frompage, tsk.getTaskStatus());
            }
        });
    }

    private void getTaskDetailsData(int taskId, final int frompage, final String taskStatus)
    {
        showProgressDialog();
        int CompanyId = PreferenceUtils.getCompnayId(mContext);
        String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            TaskServices taskServices = retrofit.create(TaskServices.class);
            Call call = taskServices.getTaskListDetailsApi(SubdomainName, CompanyId, taskId);
            call.enqueue(new Callback<ArrayList<TaskListModel>>() {

                @Override
                public void onResponse(Response<ArrayList<TaskListModel>> response, Retrofit retrofit) {
                    ArrayList<TaskListModel> taskmodels = response.body();
                    dismissProgressDialog();
                    if (taskmodels != null && taskmodels.size() > 0) {
                        gotoViewTaskActivity(taskmodels.get(0),frompage,taskStatus);
                    }
                }
                @Override
                public void onFailure(Throwable t) {
                    dismissProgressDialog();
                    Log.d(t.toString(), "Error");
                }
            });
        }
        else {
            dismissProgressDialog();
        }
    }

    public void mytemadapterClick(){
        if(reviewTasklistAdapter != null){
            reviewTasklistAdapter.setOnItemClickListener(new TasklistItemAdapter.MyClickListener() {
                @Override
                public void onItemClick(TaskListModel tsk, int frompage) {
                    //    gotoViewTaskActivity(tsk,frompage,tsk.getTaskStatus());
                    getTaskDetailsData(tsk.getTask_Id(), frompage, tsk.getTaskStatus());
                }
            });
        }

        if(myTeamTasklistAdapter != null){
            myTeamTasklistAdapter.setOnItemClickListener(new TasklistItemAdapter.MyClickListener() {
                @Override
                public void onItemClick(TaskListModel tsk, int frompage) {
                    //   gotoViewTaskActivity(tsk,frompage,tsk.getTaskStatus());
                    getTaskDetailsData(tsk.getTask_Id(), frompage, tsk.getTaskStatus());
                }
            });
        }

    }

    public void gotoViewTaskActivity(TaskListModel tsk,int frompage,String taskstatus){
        Bundle bundle = new Bundle();
        bundle.putSerializable("value", tsk);
        bundle.putSerializable("isfromPage",frompage);
        bundle.putSerializable("taskstatus",taskstatus);
        bundle.putSerializable("isFromChecklist",false);
        Intent intent = new Intent(mContext, ViewTaskActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void showemptyTeamView(){
        if(taskgroupListModels != null) {
            if (taskgroupListModels.get(0).getLstTaskListReview()!=null && taskgroupListModels.get(0).getLstTaskListReview().size() < 0) {
                reviewrecycler.setVisibility(View.GONE);
                review_empty_view.setVisibility(View.VISIBLE);
            }
            if(taskgroupListModels.get(0).getLstTaskListOther()!=null && taskgroupListModels.get(0).getLstTaskListOther().size() < 0) {
                myTeamrecycler.setVisibility(View.GONE);
                my_team_empty_view.setVisibility(View.VISIBLE);
            }
        }else{
            reviewrecycler.setVisibility(View.GONE);
            myTeamrecycler.setVisibility(View.GONE);
            review_empty_view.setVisibility(View.VISIBLE);
            my_team_empty_view.setVisibility(View.VISIBLE);
        }
    }

    //Filter Functions
    public void loadFilteredList() throws ParseException {
        dismissProgressDialog();
        mCheckOpen = openCheck.isChecked();
        mCheckInprogress = InprogressCheck.isChecked();
        mCheckReview = reviewCheck.isChecked();
        mCheckComplete = completedCheck.isChecked();

        if(!mCheckOpen && !mCheckInprogress && !mCheckReview && !mCheckComplete){
            if(checkEnabledTab == 2) {
                loadMyTeamTaskData(taskgroupListModels);
            }else {
                loadMyTaskData(taskListModels);
            }
        } else {
            ArrayList<TaskListModel> fTaskList = new ArrayList<>();
            ArrayList<TaskGroupListModel> fGroupTaskList = new ArrayList<>();
            if (checkEnabledTab == 2) {
                ArrayList<TaskListModel> freviewList = new ArrayList<>();
                ArrayList<TaskListModel> fotherList = new ArrayList<>();
                if (taskgroupListModels.get(0).lstTaskListReview.size() > 0)
                {
                    for (TaskListModel t : taskgroupListModels.get(0).lstTaskListReview) {
                        if (mCheckOpen) {
                            if ((t.getTask_Status()==(Constants.Task_open))) {
                                freviewList.add(t);
                            }
                        }
                        if (mCheckInprogress) {
                            if (t.getTask_Status()==(Constants.Task_InProgress)) {
                                freviewList.add(t);
                            }
                        }
                        if (mCheckReview) {
                            if (t.getTask_Status()==(Constants.Task_Markforreview)) {
                                freviewList.add(t);
                            }
                        }
                        if (mCheckComplete) {
                            if (t.getTask_Status()==(Constants.Task_Complete)) {
                                freviewList.add(t);
                            }
                        }

                    }
                    if( freviewList!=null && freviewList.size()>0) {
                        TaskGroupListModel temp = new TaskGroupListModel();
                        temp.setLstTaskListReview(freviewList);
                        fGroupTaskList.add(temp);
                        fGroupTaskList.get(0).setLstTaskListReview(freviewList);
                    }
                    else {
                        fGroupTaskList.clear();
                    }

                }
                if(taskgroupListModels.get(0).lstTaskListOther.size()>0) {
                    for (TaskListModel t : taskgroupListModels.get(0).lstTaskListOther) {
                        if (mCheckOpen) {
                            if (t.getTask_Status()==(Constants.Task_open)) {
                                fotherList.add(t);
                            }
                        }
                        if (mCheckInprogress) {
                            if (t.getTask_Status()==(Constants.Task_InProgress)) {
                                fotherList.add(t);
                            }
                        }
                        if (mCheckReview) {
                            if (t.getTask_Status()==(Constants.Task_Markforreview)) {
                                fotherList.add(t);
                            }
                        }
                        if (mCheckComplete) {
                            if (t.getTask_Status()==(Constants.Task_Complete)) {
                                fotherList.add(t);
                            }
                        }
                    }
                    if(fotherList!=null && fotherList.size()>0) {
                        TaskGroupListModel temp = new TaskGroupListModel();
                        temp.setLstTaskListReview(fotherList);
                        fGroupTaskList.add(temp);
                        if(freviewList.size()<1)
                        {
                            fGroupTaskList.get(0).setLstTaskListReview(null);
                        }
                        fGroupTaskList.get(0).setLstTaskListOther(fotherList);
                    }
                    else
                    {   TaskGroupListModel temp = new TaskGroupListModel();
                        temp.setLstTaskListReview(fotherList);
                        fGroupTaskList.add(temp);

                        fGroupTaskList.get(0).setLstTaskListOther(null);
                    }

                }
                loadMyTeamTaskData(fGroupTaskList);


            } else {
                for (TaskListModel t : taskListModels) {
                    if (mCheckOpen) {
                        if(t.getTask_Status()==(Constants.Task_open)) {
                            fTaskList.add(t);
                        }
                    }
                    if (mCheckInprogress) {
                        if(t.getTask_Status()==(Constants.Task_InProgress)) {
                            fTaskList.add(t);
                        }
                    }
                    if (mCheckReview) {
                        if(t.getTask_Status()==(Constants.Task_Markforreview)) {
                            fTaskList.add(t);
                        }
                    }
                    if (mCheckComplete) {
                        if(t.getTask_Status()==(Constants.Task_Complete)) {
                            fTaskList.add(t);
                        }
                    }
                }

                loadMyTaskData(fTaskList);

            }
        }
    }

    public void clearFilters(){
        openCheck.setChecked(false);
        InprogressCheck.setChecked(false);
        reviewCheck.setChecked(false);
        completedCheck.setChecked(false);

        mCheckOpen = openCheck.isChecked();
        mCheckInprogress = InprogressCheck.isChecked();
        mCheckReview = reviewCheck.isChecked();
        mCheckComplete = completedCheck.isChecked();

        reloadPage();
    }

    //Search Function
    public void loadMyTaskSearchdata(String newText){
        List<TaskListModel> msearchMyTask;
        msearchMyTask = new ArrayList<>();
        msearchMyTask = taskListModels;
        ArrayList<TaskListModel> filteredList = new ArrayList<>();
        if(msearchMyTask.size() > 0) {
            for (TaskListModel row : msearchMyTask) {
                String name = row.getTask_Name().toLowerCase();
                if (name.contains(newText.toLowerCase())) {
                    filteredList.add(row);
                }
            }
            if (filteredList.size() > 0) {
                try {
                    loadMyTaskData(filteredList);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {

            }
        }else{

        }
    }

    public void loadMyGroupTaskSearchdata(String newText){
        ArrayList<TaskGroupListModel> msearchgroupTask = new ArrayList<>();
        msearchgroupTask = taskgroupListModels;
        ArrayList<TaskListModel> filteredreviewList = new ArrayList<>();
        ArrayList<TaskListModel> filteredotherList = new ArrayList<>();
        if(msearchgroupTask.size() > 0) {
            for (TaskListModel row : msearchgroupTask.get(0).getLstTaskListReview()) {
                String name = row.getTask_Name().toLowerCase();
                if (name.contains(newText.toLowerCase())) {
                    filteredreviewList.add(row);
                }
            }

            for (TaskListModel row : msearchgroupTask.get(0).getLstTaskListOther()) {
                String name = row.getTask_Name().toLowerCase();
                if (name.contains(newText.toLowerCase())) {
                    filteredotherList.add(row);
                }
            }
            if (filteredotherList.size() > 0 || filteredotherList.size() > 0) {
                try {
                    msearchgroupTask.get(0).setLstTaskListReview(filteredreviewList);
                    msearchgroupTask.get(0).setLstTaskListOther(filteredotherList);
                    loadMyTeamTaskData(msearchgroupTask);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {

            }
        }else{

        }
    }

    public void showApplyButton(){
        if(openCheck.isChecked() || InprogressCheck.isChecked()  || reviewCheck.isChecked() || completedCheck.isChecked()){
            applyfilters.setVisibility(View.VISIBLE);
            applyfilters.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            applyfilters.setEnabled(true);
        }else{
            applyfilters.setBackgroundColor(Color.parseColor(Constants.GREY_COLOR));
            applyfilters.setEnabled(false);
        }
    }

    //Toggle tabs
    public void toggletabs(){
        if(checkEnabledTab == 2){
            my_list.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            group_list.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            my_grplist_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
            my_list_text.setTextColor(Color.parseColor(Constants.GREY_COLOR));
            my_grplist_view.setBackgroundColor(Color.parseColor(Constants.TEXT_COLOR));
            my_list_view.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        }else{
            my_list.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            group_list.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            my_list_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
            my_grplist_text.setTextColor(Color.parseColor(Constants.GREY_COLOR));
            my_list_view.setBackgroundColor(Color.parseColor(Constants.TEXT_COLOR));
            my_grplist_view.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        }

    }

    public void togglefilterselection(){
        cattagmenus.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        filterheadingtext.setText(getResources().getString(R.string.category));
        catselection.setBackgroundColor(Color.WHITE);
        icon_cats.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
    }

    //Bottom Navigation
    public void initializebottomnavigation(){
        bottommenusection = (CardView) findViewById(R.id.bottommenusection);
        bottommenus = (LinearLayout) findViewById(R.id.bottommenus);
        lpcourse = findViewById(R.id.lpcourse);
        assetpage = findViewById(R.id.assetpage);
        chklistpage = findViewById(R.id.chklistpage);
        taskpage = findViewById(R.id.reports);
        profilepage = findViewById(R.id.profilepage);
        pos3 = (ImageView) findViewById(R.id.pos3);
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
                if(!NetworkUtils.checkIfNetworkAvailable(TaskListActivity.this)){
                    //Toast.makeText(ChecklistLandingActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,CourseListActivity.class);
                startActivity(i);
                finish();
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
                if(!NetworkUtils.checkIfNetworkAvailable(TaskListActivity.this)){
                    Toast.makeText(TaskListActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,ChecklistLandingActivity.class);
                startActivity(i);
                finish();
            }
        });

        taskpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        profilepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetworkUtils.checkIfNetworkAvailable(TaskListActivity.this)){
                    Toast.makeText(TaskListActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,MoreMenuActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    //Progress dialog
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
