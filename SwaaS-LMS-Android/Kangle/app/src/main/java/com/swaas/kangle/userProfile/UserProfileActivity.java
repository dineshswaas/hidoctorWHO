package com.swaas.kangle.userProfile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.makeramen.roundedimageview.RoundedImageView;
import com.swaas.kangle.API.model.LandingPageAccess;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.API.service.UserService;
import com.swaas.kangle.AssetListActivity;
import com.swaas.kangle.CheckList.ChecklistLandingActivity;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.LPCourse.CourseListActivity;
import com.swaas.kangle.LPCourse.questionbuilder.db.TestResultRepository;
import com.swaas.kangle.LoginActivity;
import com.swaas.kangle.R;
import com.swaas.kangle.Reports.UserReportActivity;
import com.swaas.kangle.UploadActivity;
import com.swaas.kangle.db.CheckListTempRepository;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.db.DigitalAssetOfflineChildRepository;
import com.swaas.kangle.db.DigitalAssetOfflineRepository;
import com.swaas.kangle.db.DigitalAssetTransactionChildRepository;
import com.swaas.kangle.db.DigitalAssetTransactionRepository;
import com.swaas.kangle.db.Filters.ChecklistCatTagFilterRepository;
import com.swaas.kangle.db.Filters.CourseCatTagFilterRepository;
import com.swaas.kangle.db.Filters.DigitalassetCatTagFilterRepository;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.models.DashBoardDetailsModel;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.report.ViewReportAsPdfActivity;
import com.swaas.kangle.userProfile.UserDetailsAdapter.EducationDetailsAdapter;
import com.swaas.kangle.userProfile.UserDetailsAdapter.InterestAdapter;
import com.swaas.kangle.userProfile.UserDetailsAdapter.LifeEventsAdapter;
import com.swaas.kangle.userProfile.UserDetailsAdapter.WorkDetailsAdapter;
import com.swaas.kangle.userProfile.adapter.RegionDesignationNameAdapter;
import com.swaas.kangle.userProfile.models.UserRoleModel;
import com.swaas.kangle.utils.CommonUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.DateHelper;
import com.swaas.kangle.utils.FileUtils;
import com.swaas.kangle.utils.MessageDialog;
import com.swaas.kangle.utils.NetworkUtils;
import com.swaas.kangle.utils.TouchImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class UserProfileActivity extends AppCompatActivity implements LocationListener {

    RelativeLayout about;

    TextView username,emp_name,emp_regn,emp_dob,emp_start_date,dobtext;
    Spinner emp_desg;
    RoundedImageView user_image;
    LinearLayout about_expandsection;
    ImageView about_icon,backbutton;

    User userobj;
    Context mContext;
    MessageDialog messageDialog;
    DigitalAssetHeaderRepository headerRepository;
    DigitalAssetTransactionRepository transactionRepository;
    DigitalAssetOfflineChildRepository childRepository;
    DigitalAssetTransactionChildRepository transactionChildRepository;
    DigitalAssetOfflineRepository offlineRepository;
    UploadActivity uploadActivity;
    TestResultRepository testResultRepository;

    public List<UserDetails> educationdetails;
    public List<UserDetails> workDetails;
    public List<UserDetails> interest;
    public List<UserDetails> life_events;
    public List<UserDetails> mUserImpDates;
    public List<UserRoleModel> userRoleList;
    UserDetails userdetails;
    UserRoleModel currentUserRole;
    boolean loadfromImpDates;

    EducationDetailsAdapter educationDetailsAdapter;
    WorkDetailsAdapter workDetailsAdapter;
    InterestAdapter interestAdapter;
    LifeEventsAdapter lifeEventsAdapter;

    EmptyRecyclerView educationdetailslist,workDetailsList,interestList,life_eventsList;
    LinearLayoutManager educationdetailsLayout,workDetailsLayout,interestLayout,life_eventsLayout;
    RelativeLayout emptyeducationdetailview,emptyworkDetailsview,emptyinterestview,emptylife_eventsview;
    LinearLayout education_expandsection,work_expandsection,interest_expandsection,life_events_expandsection;

    ProgressDialog mProgressDialog;
    Gson gsonget;

    View editAbout,addWork,addEducation,addInterest,editInterest,addLifeEvents;

    View lpcourse,assetpage,chklistpage,profilepage,reports;
    LinearLayout bottommenus;
    ImageView pos4;
    TextView higlighttext;

    List<DashBoardDetailsModel> dashBoardDetailsModels;

    TextView totorcompValue,assignedvalue,Inprogressvalue,overduevalue,totorcomptext,assignedtext,Inprogresstext,overduetext;

    View overduetab,Inprogresstab,assignedtab,tot_comptab,dashboarddetails,retry;
    public double latitude,longitude;

    View header,topsec;
    ImageView person,dobimg,desig,loc;

    ImageView editimage,closeimgpop,delimage;
    TouchImageView touchimageView;
    RelativeLayout imagelayout;

    //Photovalues
    String mCurrentPhotoPath;
    public static final int REQUEST_IMAGE_CAPTURE = 101;
    public static final int REQUEST_IMAGE_BROWSE = 201;
    public static final int REQUEST_CAMERA_PERMISSION = 120;
    public static final int REQUEST_STORAGE_PERMISSION = 121;
    public static final String DEFAULT_IMAGE_EXTENSION = ".jpg";

    boolean showpopup = false;

    CheckListTempRepository checkListTempRepository;
    DigitalassetCatTagFilterRepository digitalassetCatTagFilterRepository;
    CourseCatTagFilterRepository courseCatTagFilterRepository;
    ChecklistCatTagFilterRepository checklistCatTagFilterRepository;

    RegionDesignationNameAdapter regionDesignationNameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mContext = UserProfileActivity.this;
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        messageDialog= new MessageDialog(mContext);
        uploadActivity = new UploadActivity(mContext);
        headerRepository = new DigitalAssetHeaderRepository(mContext);
        transactionRepository = new DigitalAssetTransactionRepository(mContext);
        offlineRepository = new DigitalAssetOfflineRepository(mContext);
        childRepository = new DigitalAssetOfflineChildRepository(mContext);
        transactionChildRepository = new DigitalAssetTransactionChildRepository(mContext);
        testResultRepository = new TestResultRepository(mContext);

        checkListTempRepository = new CheckListTempRepository(mContext);
        digitalassetCatTagFilterRepository = new DigitalassetCatTagFilterRepository(mContext);
        courseCatTagFilterRepository = new CourseCatTagFilterRepository(mContext);
        checklistCatTagFilterRepository = new ChecklistCatTagFilterRepository(mContext);

        dashBoardDetailsModels = new ArrayList<DashBoardDetailsModel>();

        initializeViews();
        bindClickListener();
        setupRecyclerView();
        //initializebottomnavigation();
        //bottomnavigationonClickevents();
        intializedashboardValues();
        bindonclickDashboardvalues();
        setthemeforView();
        gsonget = new Gson();
        userobj = gsonget.fromJson(PreferenceUtils.getUser(this), User.class);
        userdetails = new UserDetails();
        preloadData();
        //getDashboardDetails();
        getUserImportantDate();
        PreferenceUtils.setVisibleActivityName(mContext,"Profile");
        camerapermission();
        onClickListeners();
    }

    private void onClickListeners() {
        emp_desg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                if(position != 0)
                {

                    UserRoleModel user = regionDesignationNameAdapter.getItem(position);
                    ShowConfirmationDialog("Are you sure. Do you want to change the Role?",user);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });
    }

    public void ShowConfirmationDialog(String message,final UserRoleModel user){
        messageDialog.showChecklistConfirmationPopup(mContext,message,new View.OnClickListener() {
            @Override
            public void onClick(View Approve) {
                messageDialog.dialogDismiss();

                Intent intent = new Intent(mContext, UserRoleChangeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("New_User_Object", user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View close) {
                messageDialog.dialogDismiss();
                refreshAdapter();
            }
        }, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadActivity.insertUserTracking("Profile",latitude,longitude);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public void initializeViews(){
        header = findViewById(R.id.header);
        topsec = findViewById(R.id.topsec);
        person = (ImageView) findViewById(R.id.person);
        desig = (ImageView) findViewById(R.id.desig);
        loc = (ImageView) findViewById(R.id.loc);
        dobimg = (ImageView) findViewById(R.id.dobimg);

        user_image = (RoundedImageView) findViewById(R.id.user_image);
        username = (TextView) findViewById(R.id.username);
        emp_name = (TextView) findViewById(R.id.emp_name);
        emp_regn = (TextView) findViewById(R.id.emp_regn);
        emp_dob = (TextView) findViewById(R.id.emp_dob);
        about = (RelativeLayout) findViewById(R.id.about);
        about_icon = (ImageView) findViewById(R.id.about_icon);
        about_expandsection = (LinearLayout) findViewById(R.id.about_expandsection);
        backbutton = (ImageView) findViewById(R.id.backbutton);

        emp_start_date = (TextView) findViewById(R.id.emp_start_date);
        emp_desg = (Spinner) findViewById(R.id.emp_desg);

        educationdetailslist = (EmptyRecyclerView) findViewById(R.id.educationdetailslist);
        workDetailsList = (EmptyRecyclerView) findViewById(R.id.workDetailsList);
        interestList = (EmptyRecyclerView) findViewById(R.id.interestList);
        life_eventsList = (EmptyRecyclerView) findViewById(R.id.life_eventsList);

        emptyeducationdetailview = (RelativeLayout) findViewById(R.id.emptyeducationdetailview);
        emptyworkDetailsview =(RelativeLayout) findViewById(R.id.emptyworkDetailsview);
        emptyinterestview = (RelativeLayout) findViewById(R.id.emptyinterestview);
        emptylife_eventsview = (RelativeLayout) findViewById(R.id.emptylife_eventsview);

        education_expandsection = (LinearLayout) findViewById(R.id.education_expandsection);
        work_expandsection = (LinearLayout) findViewById(R.id.work_expandsection);
        interest_expandsection = (LinearLayout) findViewById(R.id.interest_expandsection);
        life_events_expandsection =(LinearLayout) findViewById(R.id.life_events_expandsection);

        editimage = (ImageView) findViewById(R.id.editimage);
        touchimageView = (TouchImageView) findViewById(R.id.touchimageView);
        imagelayout = (RelativeLayout) findViewById(R.id.imagelayout);
        delimage = (ImageView) findViewById(R.id.delimage);
        closeimgpop = (ImageView) findViewById(R.id.closeimgpop);

        editAbout = findViewById(R.id.about_edit);
        addWork = findViewById(R.id.add_work);
        addEducation = findViewById(R.id.add_education);
        addInterest = findViewById(R.id.add_interest);
        editInterest = findViewById(R.id.interest_edit);
        addLifeEvents = findViewById(R.id.add_life_events);
        dobtext = (TextView) findViewById(R.id.dobtext);
        editimage.setEnabled(false);
        user_image.setEnabled(false);

    }

    public void setupRecyclerView(){
        educationdetailsLayout = new LinearLayoutManager(this);
        workDetailsLayout = new LinearLayoutManager(this);
        interestLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        //interestLayout = new LinearLayoutManager(this);
        life_eventsLayout = new LinearLayoutManager(this);

        educationdetailslist.setLayoutManager(educationdetailsLayout);
        workDetailsList.setLayoutManager(workDetailsLayout);
        interestList.setLayoutManager(interestLayout);
        life_eventsList.setLayoutManager(life_eventsLayout);

    }

    public void bindClickListener(){

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this,EditUserProfile.class);
                startActivity(intent);
            }
        });

        addWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        editInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserInterestDetails();
            }
        });

        addLifeEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        editimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoDialogPopup();
            }
        });

        //Display profile image

        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);
                if(showpopup){
                    /*imagelayout.setVisibility(View.VISIBLE);
                    loadattachedImageView(userobj.getProfile_Photo_BLOB_URL());*/
                }else{
                    showPhotoDialogPopup();
                }

            }
        });

        closeimgpop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagelayout.setVisibility(View.GONE);
            }
        });

        delimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);
                user.setProfile_Photo_BLOB_URL("");
                Gson gson = new Gson();
                String userobj = gson.toJson(user);
                PreferenceUtils.setUser(mContext,userobj);
                Ion.with(user_image).fitXY().placeholder(R.drawable.default_user_icon).fitXY().error(R.drawable.default_user_icon).fitXY().load(
                        (!TextUtils.isEmpty(user.getProfile_Photo_BLOB_URL())) ?
                                user.getProfile_Photo_BLOB_URL() : String.valueOf(getResources().getDrawable(R.drawable.default_user_icon)));
                editimage.setVisibility(View.GONE);
                showpopup = false;
                imagelayout.setVisibility(View.GONE);
            }
        });

    }

    public void showPhotoDialogPopup(){
        messageDialog.showPhotoDialog(mContext,new View.OnClickListener() {
            @Override
            public void onClick(View Approve) {
                messageDialog.dialogDismiss();
                takePhoto();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View close) {
                messageDialog.dialogDismiss();
                browsePhoto();
            }
        },new View.OnClickListener() {
            @Override
            public void onClick(View close) {

            }
        },new View.OnClickListener() {
            @Override
            public void onClick(View close) {

            }
        },true,true,true,false,false);
    }

    private void getUserDesignationRegionNameList()
    {
        if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
            showProgressDialog();
            int Company_Id = PreferenceUtils.getCompnayId(mContext);
            String UserName = userobj.getFirstName().toString();
            setProgressmessage("Loading...");
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            /*RetrofitCustomApiBuilder retrofitAPI = new RetrofitCustomApiBuilder("https://api.myjson.com/");
            Retrofit retrofit = retrofitAPI.getInstance();*/
            UserService userService = retrofitAPI.create(UserService.class);
            Call call = userService.getUserRefreshDetails(Company_Id,UserName);
            call.enqueue(new Callback<List<UserRoleModel>>() {
                @Override
                public void onResponse(Response<List<UserRoleModel>> response, Retrofit retrofit) {
                    List<UserRoleModel> apiResponse = response.body();
                    dismissProgressDialog();
                    if (apiResponse != null && apiResponse.size() > 0) {
                        userRoleList = apiResponse;
                        if(userRoleList != null && userRoleList.size() > 0) {
                            for(UserRoleModel userdetail : userRoleList) {
                                if(userdetail.getUser_Id() == PreferenceUtils.getUserId(mContext)) {
                                    userRoleList.remove(userdetail);
                                    currentUserRole = userdetail;
                                    break;
                                }
                            }
                        }
                        loaddata();
                        loadAllAdapters();
                    } else {
                        userRoleList = new ArrayList<>();
                        //getUserEducationDetails();
                        loaddata();
                        loadAllAdapters();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(UserProfileActivity.this,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                    dismissProgressDialog();
                }
            });
        }

    }




    public void preloadData(){
        Ion.with(user_image).fitXY().placeholder(R.drawable.default_user_icon).fitXY().error(R.drawable.default_user_icon).fitXY().load(
                (!TextUtils.isEmpty(userobj.getProfile_Photo_BLOB_URL())) ?
                        userobj.getProfile_Photo_BLOB_URL() : String.valueOf(getResources().getDrawable(R.drawable.default_user_icon)));

        if(!TextUtils.isEmpty(userobj.getProfile_Photo_BLOB_URL())){
            editimage.setVisibility(View.VISIBLE);
            //showpopup = true;
        }else{
            editimage.setVisibility(View.GONE);
            //showpopup = false;
        }

        editimage.setVisibility(View.VISIBLE);
        editimage.setVisibility(View.GONE);
        username.setText(userobj.getEmployee_Name());
        emp_name.setText(userobj.getEmployee_Name());
        emp_regn.setText(userobj.getRegion_Name());
    }

    public void loaddata(){
        emp_start_date.setText(mUserImpDates != null && mUserImpDates.size() > 0 ? mUserImpDates.get(0).getDOA() : "-");
        emp_dob.setText(userobj.getDOB().substring(0,userobj.getDOB().length()-17));
        dobtext.setText(getResources().getString(R.string.employee_DOB)+" (mm - dd)");

        if(userRoleList.size() > 0){
            if(loadfromImpDates){
                UserRoleModel userDetail = new UserRoleModel();
                userDetail.setRegion_Name(userobj.getRegion_Name());
                userDetail.setUser_Type_Name(mUserImpDates.get(0).getDesignation());
                userRoleList.add(0, userDetail);
            }else{
                userRoleList.add(0,currentUserRole);
            }
            regionDesignationNameAdapter = new RegionDesignationNameAdapter(mContext, android.R.layout.simple_spinner_item, userRoleList);
            regionDesignationNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            emp_desg.setAdapter(regionDesignationNameAdapter);
        }else{
            UserRoleModel userDetail = new UserRoleModel();
            userDetail.setRegion_Name(userobj.getRegion_Name());
            userDetail.setUser_Type_Name(mUserImpDates != null && mUserImpDates.size() > 0 ? mUserImpDates.get(0).getDesignation() : currentUserRole.getUser_Type_Name());
            userRoleList.add(userDetail);
            regionDesignationNameAdapter = new RegionDesignationNameAdapter(mContext, android.R.layout.simple_spinner_item, userRoleList);
            regionDesignationNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            emp_desg.setAdapter(regionDesignationNameAdapter);
        }

    }

    public void refreshAdapter(){
        regionDesignationNameAdapter = new RegionDesignationNameAdapter(mContext, android.R.layout.simple_spinner_item, userRoleList);
        regionDesignationNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emp_desg.setAdapter(regionDesignationNameAdapter);
    }

    public void logoutDialog(){
        if (NetworkUtils.checkIfNetworkAvailable(mContext)) {
            messageDialog.ShowLogout(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View Approve) {
                    logoutProcess();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View close) {
                    messageDialog.dialogDismiss();
                }
            }, true);
        }else{
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_LONG).show();
        }
    }

    public void logoutProcess(){

        uploadActivity = new UploadActivity(mContext);
        uploadActivity.uploadTrackingTable();
        //uploadActivity.uploadTagAnalytics();
        uploadActivity.uploadTestTableRecords();
        //sendDigitalAssetAnalyticsToServer();

        PreferenceUtils.setUser(mContext,"");
        PreferenceUtils.setSubdomainName(mContext,"");
        PreferenceUtils.setUserId(mContext,0);
        PreferenceUtils.setLanguageChoosen(mContext,"");
        PreferenceUtils.setCompanyId(mContext,0);
        PreferenceUtils.setClientLogo(mContext,"");
        PreferenceUtils.setLandingPageAccess(mContext,"");
        PreferenceUtils.setCompanyThemeModel(mContext,"");

        SharedPreferences share_settings = mContext.getSharedPreferences("KANGLE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share_settings.edit();
        editor.clear();
        editor.commit();

        headerRepository.deleteAssetMaster();
        offlineRepository.deleteAssetOfflineMaster();
        childRepository.deleteAssetOfflineChild();
        transactionChildRepository.deleteAssetChildTransaction();
        testResultRepository.deleteAllTransaction();
        //transactionRepository.deleteTransactionTable();
        transactionRepository.deleteAssetTransactionTable();

        checkListTempRepository.deleteChecklistTemp();
        checklistCatTagFilterRepository.deleteChecklistCatTag();
        digitalassetCatTagFilterRepository.deleteDigitalassetCatTag();
        courseCatTagFilterRepository.deleteCourseCatTag();

        Intent i = new Intent(mContext,LoginActivity.class);
        Toast.makeText(mContext,getResources().getString(R.string.logoutmessage),Toast.LENGTH_SHORT).show();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }


    public void getUserImportantDate(){
        if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
            showProgressDialog();
            setProgressmessage("Getting user Important Date");
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            UserService userService = retrofitAPI.create(UserService.class);
            Call call = userService.getUserImportantDate(userobj.getUserID(),PreferenceUtils.getSubdomainName(mContext),userobj.getCompany_Id());
            call.enqueue(new Callback<List<UserDetails>>() {
                @Override
                public void onResponse(Response<List<UserDetails>> response, Retrofit retrofit) {
                    List<UserDetails> apiResponse = response.body();
                        dismissProgressDialog();
                    if (apiResponse != null && apiResponse.size() > 0) {
                        mUserImpDates = apiResponse;
                        loadfromImpDates = true;
                        //getUserEducationDetails();
                        //loaddata();
                        //loadAllAdapters();
                        //details();
                    } else {
                        mUserImpDates = new ArrayList<UserDetails>();
                        loadfromImpDates = false;
                        //getUserEducationDetails();
                        //loaddata();
                        //loadAllAdapters();
                    }
                    getUserDesignationRegionNameList();
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(UserProfileActivity.this,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                    dismissProgressDialog();
                }
            });
        }else{

        }
    }

    public void getUserEducationDetails(){
        setProgressmessage("Getting user Education Details");
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        UserService userService = retrofitAPI.create(UserService.class);
        Call call = userService.getUserEducationDetails(userobj.getUserID(),PreferenceUtils.getSubdomainName(mContext),userobj.getCompany_Id());
        call.enqueue(new Callback<List<UserDetails>>() {
            @Override
            public void onResponse(Response<List<UserDetails>> response, Retrofit retrofit) {
                List<UserDetails> apiResponse = response.body();
                if (apiResponse != null && apiResponse.size() > 0) {
                    educationdetails = apiResponse;
                    emptyeducationdetailview.setVisibility(View.GONE);
                    educationdetailslist.setVisibility(View.VISIBLE);
                    //getUserWorkDetails();
                } else {
                    educationdetails = new ArrayList<UserDetails>();
                    emptyeducationdetailview.setVisibility(View.VISIBLE);
                    educationdetailslist.setVisibility(View.GONE);
                    //getUserWorkDetails();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(UserProfileActivity.this,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                dismissProgressDialog();
            }
        });
    }

    public void getUserWorkDetails(){
        setProgressmessage("Getting user Work Details");
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        UserService userService = retrofitAPI.create(UserService.class);
        Call call = userService.getUserWorkDetails(userobj.getUserID(),PreferenceUtils.getSubdomainName(mContext),userobj.getCompany_Id());
        call.enqueue(new Callback<List<UserDetails>>() {
            @Override
            public void onResponse(Response<List<UserDetails>> response, Retrofit retrofit) {
                List<UserDetails> apiResponse = response.body();
                if (apiResponse != null && apiResponse.size() > 0) {
                    workDetails = apiResponse;
                    emptyworkDetailsview.setVisibility(View.GONE);
                    workDetailsList.setVisibility(View.VISIBLE);
                    //getUserInterestDetails();
                } else {
                    workDetails = new ArrayList<UserDetails>();
                    emptyworkDetailsview.setVisibility(View.VISIBLE);
                    workDetailsList.setVisibility(View.GONE);
                    //getUserInterestDetails();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(UserProfileActivity.this,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                dismissProgressDialog();
            }
        });
    }

    public void getUserInterestDetails(){
        setProgressmessage("Getting user Interest Details");
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        UserService userService = retrofitAPI.create(UserService.class);
        Call call = userService.getUserInterestDetails(userobj.getUserID(),PreferenceUtils.getSubdomainName(mContext),userobj.getCompany_Id());
        call.enqueue(new Callback<List<UserDetails>>() {
            @Override
            public void onResponse(Response<List<UserDetails>> response, Retrofit retrofit) {
                List<UserDetails> apiResponse = response.body();
                if (apiResponse != null && apiResponse.size() > 0) {
                    interest = apiResponse;
                    emptyinterestview.setVisibility(View.GONE);
                    interestList.setVisibility(View.VISIBLE);
                    editInterest.setVisibility(View.VISIBLE);
                    //getUserLifeEvent();
                } else {
                    interest = new ArrayList<UserDetails>();
                    emptyinterestview.setVisibility(View.VISIBLE);
                    interestList.setVisibility(View.GONE);
                    editInterest.setVisibility(View.GONE);
                    //getUserLifeEvent();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(UserProfileActivity.this,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                dismissProgressDialog();
            }
        });
    }

    public void getUserLifeEvent(){
        setProgressmessage("Getting user Life Events Details");
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        UserService userService = retrofitAPI.create(UserService.class);
        Call call = userService.getUserLifeEvent(userobj.getUserID(),PreferenceUtils.getSubdomainName(mContext),userobj.getCompany_Id());
        call.enqueue(new Callback<List<UserDetails>>() {
            @Override
            public void onResponse(Response<List<UserDetails>> response, Retrofit retrofit) {
                List<UserDetails> apiResponse = response.body();
                if (apiResponse != null && apiResponse.size() > 0) {
                    life_events = apiResponse;
                    life_eventsList.setVisibility(View.VISIBLE);
                    emptylife_eventsview.setVisibility(View.GONE);
                    //loaddata();
                    //loadAllAdapters();
                } else {
                    life_events = new ArrayList<UserDetails>();
                    life_eventsList.setVisibility(View.GONE);
                    emptylife_eventsview.setVisibility(View.VISIBLE);
                    //loaddata();
                    //loadAllAdapters();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(UserProfileActivity.this,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                dismissProgressDialog();
                //error
            }
        });
    }

    public void loadAllAdapters(){
        dismissProgressDialog();
        educationDetailsAdapter = new EducationDetailsAdapter(UserProfileActivity.this,educationdetails);
        workDetailsAdapter = new WorkDetailsAdapter(UserProfileActivity.this,workDetails);
        interestAdapter = new InterestAdapter(UserProfileActivity.this,interest, false);
        lifeEventsAdapter = new LifeEventsAdapter(UserProfileActivity.this,life_events);

        educationdetailslist.setAdapter(educationDetailsAdapter);
        workDetailsList.setAdapter(workDetailsAdapter);
        /*GridLayoutManager grid;
        grid = new GridLayoutManager(this,5);
        interestList.setLayoutManager(grid);*/
        interestList.setAdapter(interestAdapter);
        life_eventsList.setAdapter(lifeEventsAdapter);

    }


    public void editUserInterestDetails(){
        if(interest.size() > 0) {
            interestAdapter = new InterestAdapter(UserProfileActivity.this, interest, true);
            interestList.setAdapter(interestAdapter);
        }else{
            emptyinterestview.setVisibility(View.VISIBLE);
            interestList.setVisibility(View.GONE);
        }

    }


    public void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
    }

    public void setProgressmessage(String Message){
        mProgressDialog.setMessage(Message);
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    //Delete WorkDetails
    public void deleteWorkDetails(final String subdomain, final int companyId,final int userId, int UserWorkId){
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        UserService userService = retrofitAPI.create(UserService.class);
        Call call = userService.deleteUserWork(subdomain,userId,UserWorkId);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Response<Integer> response, Retrofit retrofit) {
                Integer apiResponse = response.body();
                if (apiResponse != null && apiResponse == 1) {
                    refreshWorkDetails(subdomain,userId,companyId);
                } else {
                    Log.d("retrofit","error 2");
                    //error
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Login","error");
                //error
            }
        });
    }

    public void refreshWorkDetails(String subdomain,int userId,int companyId){
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        UserService userService = retrofitAPI.create(UserService.class);
        Call call = userService.getUserWorkDetails(userId,subdomain,companyId);
        call.enqueue(new Callback<List<UserDetails>>() {
            @Override
            public void onResponse(Response<List<UserDetails>> response, Retrofit retrofit) {
                List<UserDetails> apiResponse = response.body();
                if (apiResponse != null && apiResponse.size() > 0) {
                    workDetails = apiResponse;
                    emptyworkDetailsview.setVisibility(View.GONE);
                    workDetailsList.setVisibility(View.VISIBLE);
                    workDetailsAdapter = new WorkDetailsAdapter(UserProfileActivity.this,workDetails);
                    workDetailsList.setAdapter(workDetailsAdapter);
                } else {
                    emptyworkDetailsview.setVisibility(View.VISIBLE);
                    workDetailsList.setVisibility(View.GONE);
                    Log.d("retrofit","error 2");
                    //error
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Login","error");
                //error
            }
        });
    }


    //Delete EducationDetails

    public void deleteEducationDetails(final String subdomain, final int companyId,final int userId, int UserEducationId){
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        UserService userService = retrofitAPI.create(UserService.class);
        Call call = userService.deleteUserEducation(subdomain,userId,UserEducationId);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Response<Integer> response, Retrofit retrofit) {
                Integer apiResponse = response.body();
                if (apiResponse != null && apiResponse == 1) {
                    refreshEducationDetails(subdomain,userId,companyId);
                } else {
                    Log.d("retrofit","error 2");
                    //error
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Login","error");
                //error
            }
        });
    }

    public void refreshEducationDetails(String subdomain,int userId,int companyId){
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        UserService userService = retrofitAPI.create(UserService.class);
        Call call = userService.getUserEducationDetails(userId,subdomain,companyId);
        call.enqueue(new Callback<List<UserDetails>>() {
            @Override
            public void onResponse(Response<List<UserDetails>> response, Retrofit retrofit) {
                List<UserDetails> apiResponse = response.body();
                if (apiResponse != null && apiResponse.size() > 0) {
                    educationdetails = apiResponse;
                    emptyeducationdetailview.setVisibility(View.GONE);
                    educationdetailslist.setVisibility(View.VISIBLE);
                    educationDetailsAdapter = new EducationDetailsAdapter(UserProfileActivity.this,educationdetails);
                    educationdetailslist.setAdapter(educationDetailsAdapter);
                } else {
                    emptyeducationdetailview.setVisibility(View.VISIBLE);
                    educationdetailslist.setVisibility(View.GONE);
                    Log.d("retrofit","error 2");
                    //error
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Login","error");
                //error
            }
        });
    }


    //Delete UserLifeEvent

    public void deleteUserLifeEvent(final String subdomain, final int companyId,final int userId, int UserEventId){
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        UserService userService = retrofitAPI.create(UserService.class);
        Call call = userService.deleteUserEvent(subdomain,userId,UserEventId);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Response<Integer> response, Retrofit retrofit) {
                Integer apiResponse = response.body();
                if (apiResponse != null && apiResponse == 1) {
                    refreshUserLifeEvent(subdomain,userId,companyId);
                } else {
                    Log.d("retrofit","error 2");
                    //error
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Login","error");
                //error
            }
        });
    }

    public void refreshUserLifeEvent(String subdomain,int userId,int companyId){
        setProgressmessage("Getting user Life Events Details");
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        UserService userService = retrofitAPI.create(UserService.class);
        Call call = userService.getUserLifeEvent(userId,subdomain,companyId);
        call.enqueue(new Callback<List<UserDetails>>() {
            @Override
            public void onResponse(Response<List<UserDetails>> response, Retrofit retrofit) {
                List<UserDetails> apiResponse = response.body();
                if (apiResponse != null && apiResponse.size() > 0) {
                    life_events = apiResponse;
                    life_eventsList.setVisibility(View.VISIBLE);
                    emptylife_eventsview.setVisibility(View.GONE);
                    lifeEventsAdapter = new LifeEventsAdapter(UserProfileActivity.this,life_events);
                    life_eventsList.setAdapter(lifeEventsAdapter);
                } else {
                    life_eventsList.setVisibility(View.GONE);
                    emptylife_eventsview.setVisibility(View.VISIBLE);
                    Log.d("retrofit","error 2");
                    //error
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Login","error");
                //error
            }
        });
    }


    //Delete UserUserInterestDetails

    public void deleteUserInterestDetails(final String subdomain, final int companyId,final int userId, int UserInterestId){
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        UserService userService = retrofitAPI.create(UserService.class);
        Call call = userService.deleteUserInterest(subdomain,userId,UserInterestId);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Response<Integer> response, Retrofit retrofit) {
                Integer apiResponse = response.body();
                if (apiResponse != null && apiResponse == 1) {
                    refreshUserInterestDetails(subdomain,userId,companyId);
                } else {
                    Log.d("retrofit","error 2");
                    //error
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Login","error");
                //error
            }
        });
    }

    public void refreshUserInterestDetails(String subdomain,int userId,int companyId){
        setProgressmessage("Getting user Life Events Details");
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        UserService userService = retrofitAPI.create(UserService.class);
        Call call = userService.getUserInterestDetails(userId,subdomain,companyId);
        call.enqueue(new Callback<List<UserDetails>>() {
            @Override
            public void onResponse(Response<List<UserDetails>> response, Retrofit retrofit) {
                List<UserDetails> apiResponse = response.body();
                if (apiResponse != null && apiResponse.size() > 0) {
                    interest = apiResponse;
                    emptyinterestview.setVisibility(View.GONE);
                    interestList.setVisibility(View.VISIBLE);
                    editInterest.setVisibility(View.VISIBLE);
                    interestAdapter = new InterestAdapter(UserProfileActivity.this,interest,false);
                    interestList.setAdapter(interestAdapter);
                } else {
                    emptyinterestview.setVisibility(View.VISIBLE);
                    interestList.setVisibility(View.GONE);
                    editInterest.setVisibility(View.GONE);
                    Log.d("retrofit","error 2");
                    //error
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Login","error");
                //error
            }
        });
    }

    public void setthemeforView(){

        topsec.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        person.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        desig.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        loc.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        dobimg.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
    }


    public void initializebottomnavigation(){
        bottommenus = (LinearLayout) findViewById(R.id.bottommenus);
        lpcourse = findViewById(R.id.lpcourse);
        assetpage = findViewById(R.id.assetpage);
        chklistpage = findViewById(R.id.chklistpage);
        reports = findViewById(R.id.reports);
        profilepage = findViewById(R.id.profilepage);

        pos4 = (ImageView) findViewById(R.id.pos4);
        higlighttext = (TextView) findViewById(R.id.higlighttext);
    }

    public void bottomnavigationonClickevents(){
        int count = 2;
        if(PreferenceUtils.getLandingPageAccess(mContext) != null){
            Gson gsonget = new Gson();
            LandingPageAccess landingobj = gsonget.fromJson(PreferenceUtils.getLandingPageAccess(mContext), LandingPageAccess.class);
            if(landingobj != null) {
                if (landingobj.getLibrary().equalsIgnoreCase("Y")) {
                    assetpage.setVisibility(View.VISIBLE);
                    count += 1;
                }else{
                    assetpage.setVisibility(View.GONE);
                }
                if (landingobj.getCourse().equalsIgnoreCase("L")) {
                    lpcourse.setVisibility(View.VISIBLE);
                    count += 1;
                } else if (landingobj.getCourse().equalsIgnoreCase("S")) {
                    lpcourse.setVisibility(View.VISIBLE);
                    count += 1;
                    //adCourse.setVisibility(View.VISIBLE);
                } else if(landingobj.getCourse().equalsIgnoreCase("A")){
                    lpcourse.setVisibility(View.VISIBLE);
                    count += 1;
                    //lpcourse.setVisibility(View.VISIBLE);
                    //lpcourse.setVisibility(View.VISIBLE);
                }else{
                    lpcourse.setVisibility(View.GONE);
                }
                if (landingobj.getChecklist().equalsIgnoreCase("Y")) {
                    chklistpage.setVisibility(View.VISIBLE);
                    count += 1;
                }else{
                    chklistpage.setVisibility(View.GONE);
                }
            }
        }

        bottommenus.setWeightSum(count);

        lpcourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetworkUtils.checkIfNetworkAvailable(UserProfileActivity.this)){
                    //Toast.makeText(UserProfileActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
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
                if(!NetworkUtils.checkIfNetworkAvailable(UserProfileActivity.this)){
                    Toast.makeText(UserProfileActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,ChecklistLandingActivity.class);
                startActivity(i);
                finish();
            }
        });

        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!NetworkUtils.checkIfNetworkAvailable(UserProfileActivity.this)){
                    Toast.makeText(UserProfileActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,UserReportActivity.class);
                startActivity(i);
                finish();
            }
        });

        profilepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(!NetworkUtils.checkIfNetworkAvailable(UserProfileActivity.this)){
                    Toast.makeText(UserProfileActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,UserProfileActivity.class);
                startActivity(i);
                finish();*/
            }
        });
    }

    public void getDashboardDetails(){
        if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
            showProgressDialog();
            setProgressmessage(getResources().getString(R.string.loading));
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            UserService userService = retrofitAPI.create(UserService.class);
            String offsetFromUtc = CommonUtils.getUtcOffset();
            Call call = userService.getDashboardDetails(PreferenceUtils.getSubdomainName(mContext),userobj.getCompany_Id(),userobj.getUserID(),offsetFromUtc);
            call.enqueue(new Callback<List<DashBoardDetailsModel>>() {

                @Override
                public void onResponse(Response<List<DashBoardDetailsModel>> response, Retrofit retrofit) {
                    List<DashBoardDetailsModel> apiResponse= response.body();
                    if (apiResponse != null) {
                        if(apiResponse.size() > 0 ) {
                            retry.setVisibility(View.GONE);
                            dashboarddetails.setVisibility(View.VISIBLE);
                            dashBoardDetailsModels = apiResponse;
                            Log.d("log", "assetsForBrowses");
                            getUserImportantDate();
                        }else{
                            retry.setVisibility(View.VISIBLE);
                            dashboarddetails.setVisibility(View.GONE);
                            getUserImportantDate();
                        }
                    } else {
                        Log.d("retrofit","error 2");
                        dismissProgressDialog();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(mContext,getResources().getString(R.string.erroroccured),Toast.LENGTH_LONG).show();
                    Log.d("retrofit","error 2");
                    dismissProgressDialog();
                    //emptyview.setVisibility(View.VISIBLE);
                }
            });
        }else{

        }

    }

    public void details(){
        if(dashBoardDetailsModels.size() > 0) {
            if (dashBoardDetailsModels.get(0).getTotal_Count() > 0) {
                dismissProgressDialog();
                loadDetails();
            } else {
                dismissProgressDialog();
                companywiselabel(0);
            }
        }else{
            dismissProgressDialog();
            companywiselabel(0);
        }
    }

    public void intializedashboardValues(){

        totorcompValue = (TextView) findViewById(R.id.totorcompValue);
        assignedvalue = (TextView) findViewById(R.id.assignedvalue);
        Inprogressvalue = (TextView) findViewById(R.id.Inprogressvalue);
        overduevalue = (TextView) findViewById(R.id.overduevalue);
        totorcomptext = (TextView) findViewById(R.id.totorcomptext);
        assignedtext = (TextView) findViewById(R.id.assignedtext);
        Inprogresstext = (TextView) findViewById(R.id.Inprogresstext);
        overduetext = (TextView) findViewById(R.id.overduetext);

        overduetab = findViewById(R.id.overduetab);
        Inprogresstab = findViewById(R.id.Inprogresstab);
        assignedtab = findViewById(R.id.assignedtab);
        tot_comptab = findViewById(R.id.tot_comptab);

        dashboarddetails = findViewById(R.id.dashboarddetails);
        retry = findViewById(R.id.retry);

    }
    public void bindonclickDashboardvalues(){

        overduetab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (overduetext.getText().equals(getResources().getString(R.string.Overdue_course))) {
                    CallToReportActivity(Constants.Over);
                //}
            }
        });

        Inprogresstab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (Inprogresstext.getText().equals(getResources().getString(R.string.in_progress))){
                    CallToReportActivity(Constants.In);
                }else if (Inprogresstext.getText().equals(getResources().getString(R.string.assigned_course))){
                    CallToReportActivity(Constants.Ass);
                }*/
                if (PreferenceUtils.getSubdomainName(mContext).contains("tacobell")){
                    CallToReportActivity(Constants.Ass);
                } else {
                    CallToReportActivity(Constants.In);
                }
            }
        });

        assignedtab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (assignedtext.getText().equals(getResources().getString(R.string.completed_course))){
                    CallToReportActivity(Constants.Com);
                //}
            }
        });

        tot_comptab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceUtils.getSubdomainName(mContext).contains("tacobell")){
                    CallToReportActivity(Constants.Tot);
                } else {
                    CallToReportActivity(Constants.Yet);
                }
            }
        });

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getUserImportantDate();
            }
        });
    }


    public void loadDetails(){
        if(PreferenceUtils.getSubdomainName(mContext).contains("tacobell")) {

            if (dashBoardDetailsModels.get(0).getTotal_Count() !=0 ){
                totorcompValue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTotal_Count())+"");
            }
            if (dashBoardDetailsModels.get(0).getCompleted()!=0){
                assignedvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getCompleted()+dashBoardDetailsModels.get(0).getMax_Attempt_Reached())+"");
            }
            if (dashBoardDetailsModels.get(0).getInProgress() != 0){
                Inprogressvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getInProgress())+"");
            }
            if (dashBoardDetailsModels.get(0).getOverdue()!=0){
                overduevalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getOverdue())+"");
            }

            companywiselabel(1);

        } else {
            if (dashBoardDetailsModels.get(0).getYet_to_Start() !=0 ){
                totorcompValue.setText(String.valueOf(dashBoardDetailsModels.get(0).getYet_to_Start())+"");
            }
            if (dashBoardDetailsModels.get(0).getCompleted()!=0){
                assignedvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getCompleted()+dashBoardDetailsModels.get(0).getMax_Attempt_Reached())+"");
            }
            if (dashBoardDetailsModels.get(0).getInProgress() != 0){
                Inprogressvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getInProgress())+"");
            }
            if (dashBoardDetailsModels.get(0).getOverdue()!=0){
                overduevalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getOverdue())+"");
            }

            companywiselabel(2);
        }
    }

    public void CallToReportActivity(String CourseStatus){
        Intent intent =  new Intent(UserProfileActivity.this, ViewReportAsPdfActivity.class);
        intent.putExtra(Constants.Course_Status,CourseStatus);
        startActivity(intent);
    }

    public void companywiselabel(int val){
        if(val == 1){
            totorcomptext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.total_course)+"</u>"));
            assignedtext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.acquired_course)+"</u>"));
            Inprogresstext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.assigned_course)+"</u>"));
            overduetext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.Overdue_course)+"</u>"));
        }else{
            totorcomptext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.yet_to_start)+"</u>"));
            assignedtext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.completed_course)+"</u>"));
            Inprogresstext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.in_progress)+"</u>"));
            overduetext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.Overdue_course)+"</u>"));
        }
    }


    //Profilephoteo methods

    public void camerapermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = ContextCompat.checkSelfPermission(UserProfileActivity.this, android.Manifest.permission.CAMERA);
            int storagePermission = ContextCompat.checkSelfPermission(UserProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
        String user = "User_Img";
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
        //intent.setType("*/*");
        intent.setType("image/*");
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
                }
            }
        }
    }

    public void onTakingPhoto(Intent data) {
        setImageThumbnail(mCurrentPhotoPath);
    }

    public void onBrowsingPhoto(Intent data) {
        if (data != null) {
            String path = FileUtils.getPath(this, data.getData());
            setImageThumbnail(path);
        }
    }

    public void setImageThumbnail(String imagePath){
        User user = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);
        user.setProfile_Photo_BLOB_URL(imagePath);
        Gson gson = new Gson();
        String userobj = gson.toJson(user);
        PreferenceUtils.setUser(mContext,userobj);
        Ion.with(user_image).fitXY().placeholder(R.drawable.default_user_icon).fitXY().error(R.drawable.default_user_icon).fitXY().load(
                (!TextUtils.isEmpty(user.getProfile_Photo_BLOB_URL())) ?
                        user.getProfile_Photo_BLOB_URL() : String.valueOf(getResources().getDrawable(R.drawable.default_user_icon)));
        editimage.setVisibility(View.VISIBLE);
        /*showpopup = true;*/
    }


    //ImageView

    public void loadattachedImageView(String Url){
        imagelayout.setVisibility(View.VISIBLE);
        Ion.with(this).load(Url).intoImageView(touchimageView);
    }
}
