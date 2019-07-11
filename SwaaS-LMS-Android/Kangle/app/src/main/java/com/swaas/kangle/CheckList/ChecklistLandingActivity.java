package com.swaas.kangle.CheckList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.swaas.kangle.API.model.LandingPageAccess;
import com.swaas.kangle.AssetListActivity;
import com.swaas.kangle.CheckList.adapter.CourseCheckListCategoryListTextRecyclerAdapter;
import com.swaas.kangle.CheckList.adapter.CourseCheckListTagsListTextRecyclerAdapter;
import com.swaas.kangle.CheckList.adapter.CourseChecklistItemAdapter;
import com.swaas.kangle.CheckList.model.CheckListModel;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.LPCourse.CourseListActivity;
import com.swaas.kangle.MoreMenuActivity;
import com.swaas.kangle.Notification.NotificationActivity;
import com.swaas.kangle.Notification.NotificationModel;
import com.swaas.kangle.Notification.NotificationTempRepository;
import com.swaas.kangle.R;
import com.swaas.kangle.TaskModule.TaskListActivity;
import com.swaas.kangle.UploadActivity;
import com.swaas.kangle.db.CheckListTempRepository;
import com.swaas.kangle.db.Filters.ChecklistCatTagFilterRepository;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.survey.SurveyListActivity;
import com.swaas.kangle.utils.CommonUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.MessageDialog;
import com.swaas.kangle.utils.NetworkUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChecklistLandingActivity extends AppCompatActivity implements LocationListener {

    View Daily,Weekly,Monthly,Onetime,survey;
    Context mContext;
    View mainnestedView;
    CheckListTempRepository checkListTempRepository;
    ImageView companylogo;
    LinearLayout my_list,group_list,coursecheck_list;
    View my_list_view,my_grplist_view,course_checklist_view;
    TextView my_list_text,my_grplist_text,course_checklist_text;
    int checkEnabledTab = 1;

    View lpcourse,assetpage,chklistpage,profilepage,header,taskpage;
    LinearLayout bottommenus;
    CardView bottommenusection;
    ImageView pos2;
    TextView higlighttext;

    UploadActivity uploadActivity;
    public double latitude,longitude;
    ImageView notification,chatview;
    MessageDialog messageDialog;
    TextView onetimetext,dailytext,weeklytext,monthlytext;
    ImageView onetimearrow,dailyarrow,weeklyarrow,monthlyarrow;

    CourseChecklistItemAdapter courseChecklistItemAdapter;
    EmptyRecyclerView course_chklstrecyclerView;
    View course_chklst_view,course_chklst_empty_view,contentView;
    LinearLayoutManager coursechklstLLM,linearLayoutManager;
    List<CheckListModel> chkModelList = new ArrayList<CheckListModel>();

    int AssignmentId, groupId;
    String PublishDate;
    ProgressDialog mProgressDialog;

    View notificationsec,chatviewsec;
    TextView notificationcount,chatcount;

    RelativeLayout filtersection;
    TextView clearfilters,applyfilters;
    EmptyRecyclerView cattag_recyclerView;
    View catselection,tagselection;
    TextView cat_filtered_count,tags_filtered_count;
    TextView clear_assetfilter,filtered_text;
    View assetfilterheading;
    TextView emptytagsview;
    ImageView close_filter;
    ImageView icon_cats,icon_tags;
    ImageView tickfilter,emptyimage,expandslider;
    TextView filterheadingtext,empty_message,retrybutton;
    View cattagmenus;

    boolean tagfiltered,catFiltered;
    List<String> tagslist,catlist;
    String TAGS = "",CATS = "";
    ImageView mCourseFilter;
    TextView mFilteredCountStatus;

    View clear_assetfilter_img,checklist_empty_view;
    CourseCheckListCategoryListTextRecyclerAdapter categoryListTextRecyclerAdapter;
    CourseCheckListTagsListTextRecyclerAdapter tagsListTextRecyclerAdapter;
    boolean isFilterenabled = false;
    List<CheckListModel> CategoryLists;
    List<CheckListModel> TagsLists;
    ChecklistCatTagFilterRepository checklistCatTagFilterRepository;
    List<CheckListModel> digitalAssetsMasterListfilterd;
    View tabsection;
    TextView course,asset,checklist,task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_landing);

        mContext = ChecklistLandingActivity.this;
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        uploadActivity = new UploadActivity(mContext);
        messageDialog = new MessageDialog(mContext);
        initializeView();
        setUpRecyclerView();
        initializebottomnavigation();
        bottomnavigationonClickevents();
        tagslist = new ArrayList<>();
        catlist = new ArrayList<>();
        setupthemeView();
        checkListTempRepository = new CheckListTempRepository(mContext);
        checklistCatTagFilterRepository = new ChecklistCatTagFilterRepository(mContext);

        getListOfCheckList();
        onClickListeners();
        PreferenceUtils.setVisibleActivityName(mContext,"Checklist");

        getnotificationcount();
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

    @Override
    protected void onResume() {
        super.onResume();
        uploadActivity.insertUserTracking("CheckList",latitude,longitude);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public void initializeView(){
        companylogo = (ImageView) findViewById(R.id.companylogo);
        Daily = findViewById(R.id.Daily);
        Weekly = findViewById(R.id.Weekly);
        Monthly = findViewById(R.id.Monthly);
        Onetime = findViewById(R.id.Onetime);
        mainnestedView = findViewById(R.id.mainnestedView);
        my_list = (LinearLayout) findViewById(R.id.my_list);
        my_list_view = findViewById(R.id.my_list_view);
        my_list_text = (TextView) findViewById(R.id.my_list_text);
        group_list = (LinearLayout) findViewById(R.id.group_list);
        my_grplist_view = findViewById(R.id.my_grplist_view);
        my_grplist_text = (TextView) findViewById(R.id.my_grplist_text);
        survey = (View)findViewById(R.id.monthly_layout_survey);
        header = findViewById(R.id.header);

        onetimetext = (TextView) findViewById(R.id.onetimetext);
        dailytext = (TextView) findViewById(R.id.dailytext);
        weeklytext = (TextView) findViewById(R.id.weeklytext);
        monthlytext = (TextView) findViewById(R.id.monthlytext);

        onetimearrow = (ImageView) findViewById(R.id.onetimearrow);
        dailyarrow = (ImageView) findViewById(R.id.dailyarrow);
        weeklyarrow = (ImageView) findViewById(R.id.weeklyarrow);
        monthlyarrow = (ImageView) findViewById(R.id.monthlyarrow);

        course_chklstrecyclerView = (EmptyRecyclerView)findViewById(R.id.course_chklstrecyclerView);
        course_chklst_empty_view = findViewById(R.id.course_chklst_empty_view);

        contentView = findViewById(R.id.content_view);
        coursecheck_list = (LinearLayout) findViewById(R.id.coursecheck_list);
        course_chklst_view = findViewById(R.id.course_chklst_view);
        course_checklist_view = findViewById(R.id.course_checklist_view);
        course_checklist_text = (TextView) findViewById(R.id.course_checklist_text);


        notification= (ImageView) findViewById(R.id.notification);
        chatview = (ImageView) findViewById(R.id.chatview);
        notificationsec = findViewById(R.id.notificationsec);
        chatviewsec = findViewById(R.id.chatviewsec);
        notificationcount = (TextView) findViewById(R.id.notificationcount);
        chatcount = (TextView) findViewById(R.id.chatcount);


        cattag_recyclerView = (EmptyRecyclerView) findViewById(R.id.cattag_recyclerView);
        filtersection = (RelativeLayout) findViewById(R.id.filtersection);
        close_filter = (ImageView) findViewById(R.id.close_filter);
        catselection = findViewById(R.id.catselection);
        tagselection = findViewById(R.id.tagselection);
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
        checklist_empty_view = findViewById(R.id.checklist_empty_view);

        clear_assetfilter_img = findViewById(R.id.clear_assetfilter_img);

        expandslider = (ImageView) findViewById(R.id.icon_expandslider);

        icon_cats = (ImageView) findViewById(R.id.icon_cats);
        icon_tags = (ImageView) findViewById(R.id.icon_tags);
        cattagmenus = findViewById(R.id.cattagmenus);
        filterheadingtext = (TextView)findViewById(R.id.filterheadingtext);
        tickfilter = (ImageView) findViewById(R.id.tickfilter);
        tabsection =  findViewById(R.id.tabsection);
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

    private void setUpRecyclerView() {

        coursechklstLLM = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        if(getResources().getBoolean(R.bool.portrait_only)){
            course_chklstrecyclerView.setLayoutManager(coursechklstLLM);
        }else{
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                GridLayoutManager grid = new GridLayoutManager(this,3);
                course_chklstrecyclerView.setLayoutManager(grid);
                //checklistrecyclerView.setLayoutManager(onetimeLLM);
            }else{
                GridLayoutManager grid = new GridLayoutManager(this,2);
                course_chklstrecyclerView.setLayoutManager(grid);
                //checklistrecyclerView.setLayoutManager(onetimeLLM);
            }
        }
        cattag_recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUpRecyclerView();
    }

    public void onClickListeners(){
        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
            }
        });

        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,SurveyListActivity.class);
                intent.putExtra("checkEnabledTab",checkEnabledTab);
                intent.putExtra("TypeofList",Constants.ONETIME);
                startActivity(intent);
            }
        });

        Onetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,CheckListListActivity.class);
                intent.putExtra("checkEnabledTab",checkEnabledTab);
                intent.putExtra("TypeofList",Constants.ONETIME);
                startActivity(intent);
            }
        });

        Daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,CheckListListActivity.class);
                intent.putExtra("checkEnabledTab",checkEnabledTab);
                intent.putExtra("TypeofList",Constants.DAILY);
                startActivity(intent);
            }
        });

        Weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,CheckListListActivity.class);
                intent.putExtra("checkEnabledTab",checkEnabledTab);
                intent.putExtra("TypeofList",Constants.WEELY);
                startActivity(intent);
            }
        });

        Monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,CheckListListActivity.class);
                intent.putExtra("checkEnabledTab",checkEnabledTab);
                intent.putExtra("TypeofList",Constants.MONTHLY);
                startActivity(intent);
            }
        });

        my_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEnabledTab = 1;
                toggletabs();
                contentView.setVisibility(View.VISIBLE);
                course_chklst_view.setVisibility(View.GONE);
                expandslider.setVisibility(View.GONE);
            }
        });

        group_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEnabledTab = 2;
                toggletabs();
                contentView.setVisibility(View.VISIBLE);
                course_chklst_view.setVisibility(View.GONE);
                expandslider.setVisibility(View.GONE);
            }
        });

        coursecheck_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*checkEnabledTab = 3;
                toggletabs();
                contentView.setVisibility(View.GONE);
                course_chklst_view.setVisibility(View.VISIBLE);
                expandslider.setVisibility(View.VISIBLE);
                List<CheckListModel> fullmodel = checkListTempRepository.getAllData();
                getLocalCourseChecklistData(fullmodel);*/
                checkEnabledTab = 1;
                Intent intent = new Intent(mContext,CourseChecklistListActivity.class);
                startActivity(intent);
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


        applyfilters.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    applyfilters.setBackgroundColor(Color.WHITE);
                    applyfilters.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    applyfilters.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
                    applyfilters.setTextColor(Color.WHITE);
                    contentView.setVisibility(View.GONE);
                    course_chklst_view.setVisibility(View.VISIBLE);
                    getCoursesbyIDs();
                    filtersection.setVisibility(View.GONE);
                    assetfilterheading.setVisibility(View.VISIBLE);
                    String htmlString="<u>"+getResources().getString(R.string.clearfilter)+"</u>";
                    filtered_text.setText(Html.fromHtml(htmlString));
                    isFilterenabled = true;
                }
                return true;
            }
        });

        expandslider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtersection.setVisibility(View.VISIBLE);
                catlist.clear();
                tagslist.clear();
                CategoryLists = checklistCatTagFilterRepository.getAllCoursechecklistCategory(CATS);
                TagsLists = checklistCatTagFilterRepository.getAllCoursechecklistTags(TAGS);
                cattag_recyclerView.setVisibility(View.VISIBLE);
                loadCategorySlider();
                bottommenusection.setVisibility(View.GONE);
            }
        });

        catselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCategorySlider();
            }
        });

        tagselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTagsSlider();
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
                    clearfiltersFunction();
                    filtersection.setVisibility(View.GONE);
                    checkEnabledTab = 3;
                    toggletabs();
                    contentView.setVisibility(View.GONE);
                    course_chklst_view.setVisibility(View.VISIBLE);
                    List<CheckListModel> fullmodel = checkListTempRepository.getAllData();
                    getLocalCourseChecklistData(fullmodel);
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
                }else {
                    filtersection.setVisibility(View.GONE);
                    catlist.clear();
                    tagslist.clear();
                    CATS = "";TAGS = "";
                    catFiltered = false; tagfiltered = false;
                    showApplyButton();
                    checkEnabledTab = 3;
                    toggletabs();
                    contentView.setVisibility(View.GONE);
                    course_chklst_view.setVisibility(View.VISIBLE);
                    List<CheckListModel> fullmodel = checkListTempRepository.getAllData();
                    getLocalCourseChecklistData(fullmodel);
                    bottommenusection.setVisibility(View.VISIBLE);
                }
            }
        });

        filtered_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtersection.setVisibility(View.GONE);
                catlist.clear();
                tagslist.clear();
                CATS = "";
                TAGS = "";
                assetfilterheading.setVisibility(View.GONE);
                isFilterenabled = false;
                clearfiltersFunction();
                checkEnabledTab = 3;
                toggletabs();
                contentView.setVisibility(View.GONE);
                course_chklst_view.setVisibility(View.VISIBLE);
                List<CheckListModel> fullmodel = checkListTempRepository.getAllData();
                getLocalCourseChecklistData(fullmodel);
                bottommenusection.setVisibility(View.VISIBLE);
            }
        });

        clear_assetfilter_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottommenusection.setVisibility(View.GONE);
                filtersection.setVisibility(View.VISIBLE);
            }
        });
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
        mainnestedView.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        pos2.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        //higlighttext.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));

        onetimetext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        dailytext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        weeklytext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        monthlytext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));

        onetimearrow.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        dailyarrow.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        weeklyarrow.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        monthlyarrow.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));

        course_chklstrecyclerView.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));

        notification.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));
        chatview.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));

        toggletabs();
    }

    public void getListOfCheckList() {
        if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
            showProgressDialog();
            checkListTempRepository.setGetChecklistDataCBListner(new CheckListTempRepository.GetChecklistDataCBListner() {
                @Override
                public void GetChecklistDataSuccessCB(List<CheckListModel> checklist) {
                    checkListTempRepository.checkListBulkInsert(checklist);
                    if(checklist != null){
                        dismissProgressDialog();
                        getLocalData();
                        insertintocategoreytagstable();
                    }else{
                        dismissProgressDialog();
                    }
                }

                @Override
                public void GetChecklistDataFailureCB(String message) {

                }
            });
            String offsetFromUtc = CommonUtils.getUtcOffsetincluded10k();
            Log.d("getUTC", offsetFromUtc);
            int CompanyId = PreferenceUtils.getCompnayId(mContext);
            Log.d("CompanyId", String.valueOf(CompanyId));
            String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            int UserId = PreferenceUtils.getUserId(mContext);
            checkListTempRepository.getChecklistDataFromAPI(SubdomainName, CompanyId, UserId, offsetFromUtc);
        }
    }

    public void getLocalData(){
        chkModelList = checkListTempRepository.getAllData();
    }

    public void insertintocategoreytagstable(){
        List<CheckListModel> allchklists = checkListTempRepository.getAllData();
        List<CheckListModel> digitalAssetsListfortags = allchklists;
        List<CheckListModel> cttags = new ArrayList<>();
        for (CheckListModel dig : digitalAssetsListfortags) {
            if(dig.getCategory_Name() != null && !dig.getCategory_Name().isEmpty()){
                if(dig.getChecklist_Tags() != "" && dig.getChecklist_Tags() != null && !dig.getChecklist_Tags().isEmpty()) {
                    String tagsstring = dig.getChecklist_Tags().replace("^","#");
                    String[] tags = tagsstring.split("#");
                    for (String t : tags) {
                        CheckListModel values = new CheckListModel();
                        values.setChecklist_Id(dig.getChecklist_Id());
                        values.setChecklist_Frequency_Id(dig.getChecklist_Frequency_Id());
                        values.setChecklist_Type_ID(dig.getChecklist_Type_ID());
                        values.setChecklist_Category_Id(dig.getChecklist_Category_Id());
                        values.setCategory_Name(dig.getCategory_Name());
                        values.setTags(t);
                        values.setChecklist_Classification(dig.getChecklist_Classification());
                        cttags.add(values);
                    }
                }else{
                    CheckListModel values = new CheckListModel();
                    values.setChecklist_Id(dig.getChecklist_Id());
                    values.setChecklist_Frequency_Id(dig.getChecklist_Frequency_Id());
                    values.setChecklist_Type_ID(dig.getChecklist_Type_ID());
                    values.setChecklist_Category_Id(dig.getChecklist_Category_Id());
                    values.setCategory_Name(dig.getCategory_Name());
                    values.setTags(null);
                    values.setChecklist_Classification(dig.getChecklist_Classification());
                    cttags.add(values);
                }
            }
        }
        checklistCatTagFilterRepository.catTagsBulkInsert(cttags);
    }

    public void getLocalCourseChecklistData(List<CheckListModel> list){
        chkModelList = list;
        loadCourseChecklist(chkModelList);
    }

    public void loadCourseChecklist(List<CheckListModel> chkModelList){
        List<CheckListModel> onetimechkModelList = new ArrayList<CheckListModel>();
        for (CheckListModel chkclst:chkModelList){
            if(chkclst.getChecklist_Classification() == 2) {
                onetimechkModelList.add(chkclst);
            }
        }
        courseChecklistItemAdapter = new CourseChecklistItemAdapter(mContext,onetimechkModelList);
        if(onetimechkModelList.size()>0){
            course_chklstrecyclerView.setAdapter(courseChecklistItemAdapter);
            course_chklstrecyclerView.setVisibility(View.VISIBLE);
            course_chklst_empty_view.setVisibility(View.GONE);
        }else{
            course_chklstrecyclerView.setVisibility(View.GONE);
            course_chklst_empty_view.setVisibility(View.VISIBLE);
        }
        loadCourseChecklistAdapterClick();
    }

    public void loadCourseChecklistAdapterClick(){
        courseChecklistItemAdapter.setOnItemClickListener(new CourseChecklistItemAdapter.MyClickListener() {
            @Override
            public void onItemClick(int Id, int checklistStatus, int CUAId) {
                if(NetworkUtils.isNetworkAvailable()) {
                    for(CheckListModel chk:chkModelList){
                        if(chk.getChecklist_Id() == Id){
                            AssignmentId = chk.getChecklist_User_Assignment_Id();
                            PublishDate = chk.getPublish_Date_String();
                            groupId = chk.getCheckList_Publish_Group_Id();
                            int checklistTypeId = chk.getChecklist_Type_ID();
                            getListOfUsers(Id,chk.getChecklist_Name(),CUAId,PublishDate,groupId,chk.getChecklist_Description(), checklistTypeId);
                        }
                    }
                }else{
                    Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getListOfUsers(final int ChecklistId, final String chklistname, final int CUAId,
                               final String PublishDate, final int chklstgroupid, final String chklstdesc, int checklistTypeId){

        if(NetworkUtils.isNetworkAvailable()){
            Intent sectionQuestionActivity = new Intent(mContext, CourseChecklistUsersActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("ChecklistId",ChecklistId);
            bundle.putSerializable("name",chklistname);
            bundle.putSerializable("CUAId",CUAId);
            bundle.putSerializable("PublishDate",PublishDate);
            bundle.putSerializable("ChecklistGroupId",chklstgroupid);
            bundle.putSerializable("chklstdesc",chklstdesc);
            bundle.putSerializable("CheckListTypeId",checklistTypeId);
            sectionQuestionActivity.putExtras(bundle);
            startActivity(sectionQuestionActivity);

        }else{
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
        }

    }


    public void toggletabs(){
        if(checkEnabledTab == 2){
            my_list.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            group_list.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            coursecheck_list.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            my_grplist_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
            my_list_text.setTextColor(Color.parseColor(Constants.GREY_COLOR));
            course_checklist_text.setTextColor(Color.parseColor(Constants.GREY_COLOR));
            my_grplist_view.setBackgroundColor(Color.parseColor(Constants.TEXT_COLOR));
            my_list_view.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            course_checklist_view.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        }else if(checkEnabledTab == 3){
            group_list.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            my_list.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            coursecheck_list.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            course_checklist_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
            my_grplist_text.setTextColor(Color.parseColor(Constants.GREY_COLOR));
            my_list_text.setTextColor(Color.parseColor(Constants.GREY_COLOR));
            course_checklist_view.setBackgroundColor(Color.parseColor(Constants.TEXT_COLOR));
            my_grplist_view.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            my_list_view.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        }else{
            my_list.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            group_list.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            coursecheck_list.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            my_list_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
            my_grplist_text.setTextColor(Color.parseColor(Constants.GREY_COLOR));
            course_checklist_text.setTextColor(Color.parseColor(Constants.GREY_COLOR));
            my_list_view.setBackgroundColor(Color.parseColor(Constants.TEXT_COLOR));
            my_grplist_view.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            course_checklist_view.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        }

    }

    //Filter Functions starting

    public void getCoursesbyIDs(){
        try{
            String selectQuery = "SELECT tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Id FROM tbl_CHECKLIST_CAT_TAG_MASTER  ";
            if (CATS.length() > 0 || TAGS.length() > 0) {
                selectQuery += "WHERE ";
                if (CATS.length() > 0) {
                    selectQuery += "tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name in ("+CATS+") ";
                }
                if (TAGS.length() > 0) {
                    if (CATS.length() > 0) {
                        selectQuery += " AND ";
                    }
                    selectQuery += "tbl_CHECKLIST_CAT_TAG_MASTER.Tags in ("+TAGS+") ";
                }
            }
            selectQuery += " Group by Checklist_Id";
            List<CheckListModel> DAIDlist = checklistCatTagFilterRepository.getAssetIdbySelectedCatTag(selectQuery);
            filteredlist(DAIDlist);
            /*String query = "";
            if(CATS.length() > 0 && TAGS.length() == 0){
                query = "select tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Id from tbl_CHECKLIST_CAT_TAG_MASTER where " +
                        "tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name in ("+CATS+") Group by Checklist_Id";
            } else if(TAGS.length() > 0 && CATS.length() == 0){
                query = "select tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Id from tbl_CHECKLIST_CAT_TAG_MASTER where " +
                        "tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Tags in ("+TAGS+") Group by Checklist_Id";
            } else if(CATS.length() > 0 && TAGS.length() > 0){
                query = "select tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Id from tbl_CHECKLIST_CAT_TAG_MASTER where " +
                        "tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Tags in ("+TAGS+") AND tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name in ("+CATS+") Group by Checklist_Id";
            }
            List<CheckListModel> DAIDlist = checklistCatTagFilterRepository.getAssetIdbySelectedCatTag(query);
            filteredlist(DAIDlist);*/
        }catch(Exception e){

        }
    }

    public void filteredlist(List<CheckListModel> DAIDlist){
        try{
            if(DAIDlist != null) {
                if(DAIDlist.size() > 0){
                    digitalAssetsMasterListfilterd = new ArrayList<>();
                    for (CheckListModel cms : DAIDlist) {
                        for (CheckListModel cs : chkModelList) {
                            if (cms.getChecklist_Id() == cs.getChecklist_Id()) {
                                digitalAssetsMasterListfilterd.add(cs);
                            }
                        }
                    }
                    loadFilteredList(digitalAssetsMasterListfilterd);
                }
            }else{
                loadFilteredList(chkModelList);
            }
        }catch (Exception e){
            Log.e("Exception",e.toString());
        }

    }

    private void loadFilteredList(List<CheckListModel> chk) {
        isFilterenabled = true;
        int countchecked = 0;
        final ArrayList<CheckListModel> filteredchecklist = new ArrayList<CheckListModel>();
        //loaddatabyintent(chk,checkEnabledTab);
        getLocalCourseChecklistData(chk);
    }

    public void clearfiltersFunction(){
        catlist.clear();
        tagslist.clear();
        CATS = "";TAGS = "";
        catFiltered = false; tagfiltered = false;
        showApplyButton();
        isFilterenabled = false;
        CategoryLists = checklistCatTagFilterRepository.getAllCoursechecklistCategory(CATS);
        TagsLists = checklistCatTagFilterRepository.getAllCoursechecklistTags(TAGS);
        categoryListTextRecyclerAdapter = new CourseCheckListCategoryListTextRecyclerAdapter(mContext, CategoryLists);
        categoryListTextRecyclerAdapter.notifyDataSetChanged();
        tagsListTextRecyclerAdapter = new CourseCheckListTagsListTextRecyclerAdapter(mContext,TagsLists);
        tagsListTextRecyclerAdapter.notifyDataSetChanged();
        catselection.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        tagselection.setBackgroundColor(Color.parseColor(Constants.OPAQUE_COLOR));
        cattag_recyclerView.setVisibility(View.VISIBLE);
        cattag_recyclerView.setAdapter(categoryListTextRecyclerAdapter);
    }

    public void loadCategorySlider(){
        try{
            togglefilterselection(1);
            String valuenames = "";
            if(tagslist.size() == 0){
                List<CheckListModel> digitalAssetsMasterCategoryList = checklistCatTagFilterRepository.getAllCoursechecklistCategory(CATS);
                reloadCatSlider(digitalAssetsMasterCategoryList);
            }else{
                if(tagslist.size()>0){
                    String tagnames = "";
                    for (String catname : tagslist) {
                        tagnames =  tagnames + "," + "'" + catname.toString() + "'";
                    }
                    StringBuilder sb = new StringBuilder(tagnames);
                    sb.deleteCharAt(0);
                    valuenames = sb.toString().replace("''", "'");
                }
                String selectQuery = "select tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name FROM tbl_CHECKLIST_CAT_TAG_MASTER ";
                if (tagslist.size() > 0) {
                    selectQuery += " WHERE ";
                    if (tagslist.size() > 0) {
                        selectQuery += " tbl_CHECKLIST_CAT_TAG_MASTER.Tags in ("+valuenames+") ";
                    }
                }
                selectQuery += " AND tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name not in ("+CATS+") Group By tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name ";
                List<CheckListModel> digitalAssetsMasterCategoryList = checklistCatTagFilterRepository.getCCCategorybySelectedTags(selectQuery);
                reloadCatSlider(digitalAssetsMasterCategoryList);
            }
        }catch (Exception e){

        }
    }

    public void loadTagsSlider(){
        try{
            togglefilterselection(2);
            String valuenames = "";
            if(catlist.size() == 0){
                List<CheckListModel> digitalAssetsMasterTagsList = checklistCatTagFilterRepository.getAllCoursechecklistTags(TAGS);
                reloadTagSlider(digitalAssetsMasterTagsList);
            }else{
                if(catlist.size()>0){
                    String catnames = "";
                    for (String catname : catlist) {
                        catnames = catnames + "," + "'" + catname.toString() + "'";
                    }
                    StringBuilder sb = new StringBuilder(catnames);
                    sb.deleteCharAt(0);
                    valuenames = sb.toString().replace("''", "'");
                }
                String selectQuery = "select tbl_CHECKLIST_CAT_TAG_MASTER.Tags FROM tbl_CHECKLIST_CAT_TAG_MASTER ";
                if (catlist.size() > 0) {
                    selectQuery += " WHERE ";
                    if (catlist.size() > 0) {
                        selectQuery += " tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name in ("+valuenames+") ";
                    }
                }
                selectQuery += " AND tbl_CHECKLIST_CAT_TAG_MASTER.Tags not null AND tbl_CHECKLIST_CAT_TAG_MASTER.Tags not in ("+TAGS+") Group By tbl_CHECKLIST_CAT_TAG_MASTER.Tags ";
                List<CheckListModel> digitalAssetsMasterTagsList = checklistCatTagFilterRepository.getCCTagsbySelectedcategorey(selectQuery);
                reloadTagSlider(digitalAssetsMasterTagsList);
            }
        }catch (Exception e){

        }

    }

    public void reloadCatSlider(List<CheckListModel> newmodel){
        List<CheckListModel> catrefresh = new ArrayList<>();
        List<CheckListModel> newrefreshed = new ArrayList<>();
        for(CheckListModel existing : CategoryLists){
            if(existing.iscatchecked){
                catrefresh.add(existing);
            }
        }
        for(CheckListModel ne : newmodel){
            catrefresh.add(ne);
        }
        /*for(CheckListModel ne : newmodel){
            if(catrefresh.size() > 0){
                for(CheckListModel re : catrefresh){
                    if(!ne.getCategory_Name().equals(re.getCategory_Name())){
                        newrefreshed.add(ne);
                    }
                }
            }else{
                newrefreshed.add(ne);
            }
        }*/
        emptytagsview.setVisibility(View.GONE);
        cattag_recyclerView.setVisibility(View.VISIBLE);
        CategoryLists = catrefresh;
        categoryListTextRecyclerAdapter = new CourseCheckListCategoryListTextRecyclerAdapter(mContext, CategoryLists);
        cattag_recyclerView.setAdapter(categoryListTextRecyclerAdapter);
    }

    public void reloadTagSlider(List<CheckListModel> newmodel){
        List<CheckListModel> tagrefresh = new ArrayList<>();
        List<CheckListModel> newtgrefresh = new ArrayList<>();
        for(CheckListModel existing : TagsLists){
            if(existing.istagchecked){
                tagrefresh.add(existing);
            }
        }
        for(CheckListModel ne : newmodel){
            tagrefresh.add(ne);
        }
        /*for(CheckListModel ne : newmodel){
            if(tagrefresh.size() > 0) {
                for (CheckListModel re : tagrefresh) {
                    if(ne.getTags() != null){
                        if (!ne.getTags().equals(re.getTags())) {
                            newtgrefresh.add(ne);
                            break;
                        }
                    }
                }
            }else{
                newtgrefresh.add(ne);
            }
        }*/
        TagsLists = tagrefresh;
        showtagsView(TagsLists,true);
    }

    public void showtagsView(List<CheckListModel> digitalAssetsMasterTagsList,boolean status){
        if(digitalAssetsMasterTagsList.size() > 0){
            emptytagsview.setVisibility(View.GONE);
            cattag_recyclerView.setVisibility(View.VISIBLE);
            tagsListTextRecyclerAdapter = new CourseCheckListTagsListTextRecyclerAdapter(mContext,digitalAssetsMasterTagsList);
            cattag_recyclerView.setAdapter(tagsListTextRecyclerAdapter);
        }else{
            cattag_recyclerView.setVisibility(View.GONE);
            emptytagsview.setVisibility(View.VISIBLE);
        }
    }

    public void togglefilterselection(int num){
        cattagmenus.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        if(num == 1){
            filterheadingtext.setText(getResources().getString(R.string.category));
            catselection.setBackgroundColor(Color.WHITE);
            icon_cats.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
            icon_tags.setColorFilter(Color.WHITE);
            tagselection.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));

        }else if(num == 2){
            filterheadingtext.setText(getResources().getString(R.string.Tags));
            tagselection.setBackgroundColor(Color.WHITE);
            icon_tags.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
            icon_cats.setColorFilter(Color.WHITE);
            catselection.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        }
    }

    public void showApplyButton(){
        if(catFiltered || tagfiltered){
            applyfilters.setVisibility(View.VISIBLE);
            applyfilters.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            applyfilters.setEnabled(true);
        }else{
            applyfilters.setBackgroundColor(Color.parseColor(Constants.GREY_COLOR));
            applyfilters.setEnabled(false);
        }
    }

    public void showhideemptystate(boolean showempty,String message,int type){
        if(showempty){
            checklist_empty_view.setVisibility(View.VISIBLE);
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
            empty_message.setText(message.toString());
        }else{
            checklist_empty_view.setVisibility(View.GONE);
        }
    }

    public void filteredcatList(CheckListModel cat){
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
                showApplyButton();
            }else{
                CATS = "";
            }
            showApplyButton();

        }catch (Exception e){

        }
    }

    public void filteredtagList(CheckListModel cat){
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

    //Filter Functions ending

    public void initializebottomnavigation(){
        bottommenusection = (CardView) findViewById(R.id.bottommenusection);
        bottommenus = (LinearLayout) findViewById(R.id.bottommenus);
        lpcourse = findViewById(R.id.lpcourse);
        assetpage = findViewById(R.id.assetpage);
        chklistpage = findViewById(R.id.chklistpage);
        taskpage = findViewById(R.id.reports);
        profilepage = findViewById(R.id.profilepage);
        pos2 = (ImageView) findViewById(R.id.pos2);
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
                if(!NetworkUtils.checkIfNetworkAvailable(ChecklistLandingActivity.this)){
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
                /*if(!NetworkUtils.checkIfNetworkAvailable(ChecklistLandingActivity.this)){
                    Toast.makeText(ChecklistLandingActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,ChecklistLandingActivity.class);
                startActivity(i);
                finish();*/
            }
        });

        taskpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!NetworkUtils.checkIfNetworkAvailable(ChecklistLandingActivity.this)){
                    Toast.makeText(ChecklistLandingActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,TaskListActivity.class);
                startActivity(i);
                finish();
            }
        });

        profilepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetworkUtils.checkIfNetworkAvailable(ChecklistLandingActivity.this)){
                    Toast.makeText(ChecklistLandingActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,MoreMenuActivity.class);
                startActivity(i);
                finish();
            }
        });
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
