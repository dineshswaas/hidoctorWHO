package com.swaas.kangle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.location.LocationListener;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.swaas.kangle.API.model.LandingPageAccess;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.API.service.UserService;
import com.swaas.kangle.CheckList.ChecklistLandingActivity;
import com.swaas.kangle.LPCourse.CourseListActivity;
import com.swaas.kangle.LPCourse.questionbuilder.db.TestResultRepository;
import com.swaas.kangle.UploadService.DigitalAssetAnalyticsUpSyncService;
import com.swaas.kangle.db.CheckListTempRepository;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.db.DigitalAssetOfflineChildRepository;
import com.swaas.kangle.db.DigitalAssetOfflineRepository;
import com.swaas.kangle.db.DigitalAssetTransactionChildRepository;
import com.swaas.kangle.db.DigitalAssetTransactionRepository;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.playerPart.DigitalAssets;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.MessageDialog;
import com.swaas.kangle.utils.NetworkUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LandingActivity extends AppCompatActivity implements LocationListener {

    LinearLayout asset,prepcourse,adCourse,course,training,checklist;
    ImageView asset_image,prepcourse_image,adCourse_image,course_image,training_image,checklist_image;
    ImageView companylogo,notification,settings;
    TextView asset_text,prepcourse_text,adCourse_text,course_text,training_text,checklist_text,Poweredby;
    TextView welcomeuser;
    ImageView logout,helpicon;
    LinearLayout menusorientation,menusorientation1;

    Context mContext;
    Spinner spinner;
    NestedScrollView nestedScroll;
    MessageDialog messageDialog;
    public double latitude,longitude;

    DigitalAssetHeaderRepository headerRepository;
    DigitalAssetTransactionRepository transactionRepository;
    CheckListTempRepository checkListTempRepository;
    DigitalAssetOfflineChildRepository childRepository;
    DigitalAssetTransactionChildRepository transactionChildRepository;
    DigitalAssetOfflineRepository offlineRepository;
    UploadActivity uploadActivity;
    TestResultRepository testResultRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        mContext = LandingActivity.this;

        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        initializeView();
        headerRepository = new DigitalAssetHeaderRepository(mContext);
        transactionRepository = new DigitalAssetTransactionRepository(mContext);
        offlineRepository = new DigitalAssetOfflineRepository(mContext);
        childRepository = new DigitalAssetOfflineChildRepository(mContext);
        transactionChildRepository = new DigitalAssetTransactionChildRepository(mContext);
        testResultRepository = new TestResultRepository(mContext);
        checkListTempRepository = new CheckListTempRepository(mContext);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Language, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
            getMenus();
        } else{
            if(PreferenceUtils.getLandingPageAccess(mContext)!=null){
                loadViews();
            }else{
                getMenus();
            }

        }
        bindclickevents();
        GotoCourseActivity();


    }

    private void GotoCourseActivity() {

        if (getIntent()!=null){

            if (getIntent().getBooleanExtra(Constants.From_Question,false)){

                if(NetworkUtils.checkIfNetworkAvailable(LandingActivity.this)){
                    Intent i = new Intent(mContext,CourseListActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LandingActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }

            }

        }

    }

    public void bindclickevents(){
        asset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent assetview = new Intent(mContext,AssetListActivity.class);
                startActivity(assetview);
            }
        });

        prepcourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtils.checkIfNetworkAvailable(LandingActivity.this)){
                    Intent i = new Intent(mContext,CourseListActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LandingActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
            }
        });

        adCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtils.checkIfNetworkAvailable(LandingActivity.this)){
                    Intent i = new Intent(mContext,CourseListActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LandingActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }

            }
        });

        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtils.checkIfNetworkAvailable(LandingActivity.this)){
                    Intent i = new Intent(mContext,CourseListActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LandingActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
            }
        });

        training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtils.checkIfNetworkAvailable(LandingActivity.this)){
                    //Intent i = new Intent(mContext,DashboardActivity.class);
                    Intent i = new Intent(mContext,WebDashBoardNew.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LandingActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
            }
        });

        checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtils.checkIfNetworkAvailable(LandingActivity.this)){
                    //Intent i = new Intent(mContext,DashboardActivity.class);
                    //Intent i = new Intent(mContext,CheckListListActivity.class);
                    Intent i = new Intent(mContext,ChecklistLandingActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LandingActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageDialog.showMenuDialog(mContext,true,new View.OnClickListener() {
                    @Override
                    public void onClick(View Approve) {
                        //logoutProcess();
                        messageDialog.dialogDismiss();
                        logoutDialog();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View close) {
                        messageDialog.dialogDismiss();
                        Intent i = new Intent(mContext,ChangePasswordActivity.class);
                        startActivity(i);
                    }
                },true);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadActivity = new UploadActivity(mContext);
                if(NetworkUtils.checkIfNetworkAvailable(mContext)){
                    //uploadActivity.uploadTagAnalytics();
                    //sendDigitalAssetAnalyticsToServer();
                }
            }
        });

        helpicon.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_LONG).show();
                }
            }
        });

        uploadActivity = new UploadActivity(mContext);
        if(NetworkUtils.checkIfNetworkAvailable(mContext)){
            uploadActivity.uploadTrackingTable();
            //uploadActivity.uploadTagAnalytics();
            uploadActivity.uploadTestTableRecords();
            sendDigitalAssetAnalyticsToServer();
        }

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

    private void sendDigitalAssetAnalyticsToServer() {
        transactionRepository.setmGetDA(new DigitalAssetTransactionRepository.GetDA() {
            @Override
            public void getDASuccess(ArrayList<DigitalAssets> digitalAssetList) {
                if(digitalAssetList != null && digitalAssetList.size() > 0){
                    Intent analyticsIntent = new Intent(LandingActivity.this,DigitalAssetAnalyticsUpSyncService.class);
                    startService(analyticsIntent);
                }
            }

            @Override
            public void getDAFailure(String message) {

            }
        });
        transactionRepository.getUnSyncedDigitalAssetAnalytics();
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
        headerRepository.deleteAssetMaster();
        offlineRepository.deleteAssetOfflineMaster();
        childRepository.deleteAssetOfflineChild();
        transactionChildRepository.deleteAssetChildTransaction();
        testResultRepository.deleteAllTransaction();
        //transactionRepository.deleteTransactionTable();
        transactionRepository.deleteAssetTransactionTable();
        checkListTempRepository.deleteChecklistTemp();
        //Intent i = new Intent(mContext,LoginActivity.class);
        //Intent i = new Intent(mContext,CompanyEntryActivity.class);
        Intent i = new Intent(mContext,LoginActivity.class);
        Toast.makeText(mContext,getResources().getString(R.string.logoutmessage),Toast.LENGTH_SHORT).show();
        startActivity(i);
        finish();
    }

    public void locationPermission(){
        if(NetworkUtils.checkIfNetworkAvailable(LandingActivity.this)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int storagePermission = ContextCompat.checkSelfPermission(LandingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                if (storagePermission == PackageManager.PERMISSION_GRANTED) {
                    loadViews();
                } else {
                    LandingActivity.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_PERMISSION_LOCATION);
                }
            } else {
                loadViews();
            }
        } else {
            Toast.makeText(LandingActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == Constants.REQUEST_PERMISSION_LOCATION){
            if(grantResults.length > 0 && grantResults[0] == PermissionChecker.PERMISSION_GRANTED){
                loadViews();
            }else{
                Toast.makeText(LandingActivity.this,getResources().getString(R.string.locationpermissoinMessage),Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadActivity = new UploadActivity(mContext);
        uploadActivity.insertUserTracking("Landing",latitude,longitude);
        if(NetworkUtils.checkIfNetworkAvailable(mContext)){
            uploadActivity.uploadTrackingTable();
            //uploadActivity.uploadTagAnalytics();
            uploadActivity.uploadTestTableRecords();
            //chekifappupdaterequired();
            sendDigitalAssetAnalyticsToServer();
        }
    }

    public void getMenus() {
        if (NetworkUtils.checkIfNetworkAvailable(mContext)) {
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            UserService userService = retrofitAPI.create(UserService.class);
            Call call = userService.getLandingPageAccess(PreferenceUtils.getSubdomainName(mContext), PreferenceUtils.getUserId(mContext), PreferenceUtils.getCompnayId(mContext));
            call.enqueue(new Callback<List<LandingPageAccess>>() {
                @Override
                public void onResponse(Response<List<LandingPageAccess>> response, Retrofit retrofit) {
                    List<LandingPageAccess> apiResponse = response.body();
                    if (apiResponse != null) {
                        Gson gson = new Gson();
                        String landingobj = gson.toJson(apiResponse.get(0));
                        PreferenceUtils.setLandingPageAccess(mContext, landingobj);
                        locationPermission();
                    } else {
                        Log.d("retrofit", "error 2");
                        //error
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("Login", "error");
                    //error
                }
            });
        } else {
            if(PreferenceUtils.getLandingPageAccess(mContext) != null){
                loadViews();
            }else {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loadViews(){
        Crashlytics.log(PreferenceUtils.getUserId(mContext) + " - " + PreferenceUtils.getSubdomainName(mContext));
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            menusorientation.setOrientation(LinearLayout.HORIZONTAL);
            menusorientation1.setOrientation(LinearLayout.HORIZONTAL);
        } else {
            menusorientation.setOrientation(LinearLayout.VERTICAL);
            menusorientation1.setOrientation(LinearLayout.VERTICAL);
        }
        if(PreferenceUtils.getSubdomainName(mContext).contains("tacobell")){
            nestedScroll.setBackgroundColor(getResources().getColor(R.color.tacobellbackground));
            course_image.setImageResource(R.drawable.course);
            prepcourse_image.setImageResource(R.drawable.course);
            adCourse_image.setImageResource(R.drawable.course);
            asset_image.setImageResource(R.drawable.toolsvideolibrary);
            training_image.setImageResource(R.drawable.basics_training_uniform);
            checklist_image.setImageResource(R.drawable.checklist);
        }else {
            nestedScroll.setBackgroundColor(getResources().getColor(R.color.otherCompanies));
            course_image.setImageResource(R.drawable.knowledgeevaluation);
            prepcourse_image.setImageResource(R.drawable.knowledgeevaluation);
            adCourse_image.setImageResource(R.drawable.knowledgeevaluation);
            asset_image.setImageResource(R.drawable.assetlibrary);
            training_image.setImageResource(R.drawable.mytraininghistory);
            checklist_image.setImageResource(R.drawable.checklist);
        }
        Gson gsonget = new Gson();
        LandingPageAccess landingobj = gsonget.fromJson(PreferenceUtils.getLandingPageAccess(mContext), LandingPageAccess.class);
        if(landingobj != null) {
            if (landingobj.getLibrary().equalsIgnoreCase("Y")) {
                asset.setVisibility(View.VISIBLE);
            }
            if (landingobj.getCourse().equalsIgnoreCase("A")) {
                //adCourse.setVisibility(View.VISIBLE);
            } else if (landingobj.getCourse().equalsIgnoreCase("S")) {
                //adCourse.setVisibility(View.VISIBLE);
            } else if(landingobj.getCourse().equalsIgnoreCase("L")){
                adCourse.setVisibility(View.VISIBLE);
                training.setVisibility(View.VISIBLE);
            }
            if (landingobj.getChecklist().equalsIgnoreCase("Y")) {
                checklist.setVisibility(View.VISIBLE);
            }
        }else{
            getMenus();
        }
        User userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);
        if(userobj != null) {
            welcomeuser.setText(getResources().getString(R.string.welcome) +" "+userobj.getEmployee_Name()+" ,");
        }else{
            welcomeuser.setText(getResources().getString(R.string.welcome));
        }

        Poweredby.setText("\u00a9"+" "+getResources().getString(R.string.poweredbyswaas));
        Ion.getDefault(mContext).getCache().clear();
        Log.d("ClentLogo",PreferenceUtils.getClientLogo(mContext));
        Ion.with(companylogo).placeholder(R.color.black).error(R.color.black).load(
                (!TextUtils.isEmpty(PreferenceUtils.getClientLogo(mContext)))? PreferenceUtils.getClientLogo(mContext) : PreferenceUtils.getClientLogo(mContext));
        File imgFile = new File("/storage/sdcard0/SwaaS LMS/companylogo.jpg");

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            companylogo.setImageBitmap(myBitmap);

        }
    }

    public void chekifappupdaterequired(){
        if (NetworkUtils.checkIfNetworkAvailable(mContext)) {
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            UserService userService = retrofitAPI.create(UserService.class);
            Call call = userService.checkCurrentBuildVersionLMS(PreferenceUtils.getSubdomainName(mContext),getString(R.string.versionName),"ANDROID");
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    String apiResponse = response.body();
                    if (apiResponse != null && apiResponse.equalsIgnoreCase("NO")) {
                        messageDialog.showUpdatePopUp(mContext, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                messageDialog.dialogDismiss();
                                gotoAppStore();
                            }
                        },false);
                    } else {
                        Log.d("retrofit", "error 2");
                        //error
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("Login", "error");
                    //error
                }
            });
        } else {
            if(PreferenceUtils.getLandingPageAccess(mContext) != null){
                loadViews();
            }else {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void gotoAppStore(){
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void initializeView() {
        asset = (LinearLayout) findViewById(R.id.asset);
        prepcourse = (LinearLayout) findViewById(R.id.prepcourse);
        adCourse = (LinearLayout) findViewById(R.id.adCourse);
        course = (LinearLayout) findViewById(R.id.course);
        training = (LinearLayout) findViewById(R.id.training);
        checklist = (LinearLayout) findViewById(R.id.checklist);

        asset_image = (ImageView) findViewById(R.id.asset_image);
        prepcourse_image = (ImageView) findViewById(R.id.prepcourse_image);
        adCourse_image = (ImageView) findViewById(R.id.adCourse_image);
        course_image = (ImageView) findViewById(R.id.course_image);
        training_image = (ImageView) findViewById(R.id.training_image);
        checklist_image = (ImageView) findViewById(R.id.checklist_image);

        asset_text = (TextView) findViewById(R.id.asset_text);
        prepcourse_text = (TextView) findViewById(R.id.prepcourse_text);
        adCourse_text = (TextView) findViewById(R.id.adCourse_text);
        course_text = (TextView) findViewById(R.id.course_text);
        training_text = (TextView) findViewById(R.id.training_text);
        checklist_text = (TextView) findViewById(R.id.checklist_text);

        companylogo = (ImageView) findViewById(R.id.companylogo);
        notification= (ImageView) findViewById(R.id.notification);
        settings = (ImageView) findViewById(R.id.settings);
        spinner = (Spinner) findViewById(R.id.spinner);

        nestedScroll = (NestedScrollView) findViewById(R.id.nestedScroll);
        messageDialog = new MessageDialog(mContext);

        welcomeuser = (TextView) findViewById(R.id.welcomeuser);
        logout = (ImageView) findViewById(R.id.logout);
        menusorientation = (LinearLayout) findViewById(R.id.menusorientation);
        menusorientation1 = (LinearLayout) findViewById(R.id.menusorientation1);
        helpicon = (ImageView) findViewById(R.id.helpicon);

        Poweredby = (TextView) findViewById(R.id.Poweredby);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
}
