package com.swaas.kangle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.swaas.kangle.API.model.LandingPageAccess;
import com.swaas.kangle.CheckList.ChecklistLandingActivity;
import com.swaas.kangle.LPCourse.CourseListActivity;
import com.swaas.kangle.LPCourse.questionbuilder.db.TestResultRepository;
import com.swaas.kangle.Reports.UserReportActivity;
import com.swaas.kangle.TaskModule.TaskListActivity;
import com.swaas.kangle.db.CheckListTempRepository;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.db.DigitalAssetOfflineChildRepository;
import com.swaas.kangle.db.DigitalAssetOfflineRepository;
import com.swaas.kangle.db.DigitalAssetTransactionChildRepository;
import com.swaas.kangle.db.DigitalAssetTransactionRepository;
import com.swaas.kangle.db.Filters.ChecklistCatTagFilterRepository;
import com.swaas.kangle.db.Filters.CourseCatTagFilterRepository;
import com.swaas.kangle.db.Filters.DigitalassetCatTagFilterRepository;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.userProfile.UserProfileActivity;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.MessageDialog;
import com.swaas.kangle.utils.NetworkUtils;

import java.io.File;

public class MoreMenuActivity extends AppCompatActivity {

    Context mContext;

    View lpcourse,assetpage,chklistpage,morepage,taskpage;
    LinearLayout bottommenus;
    ImageView pos4;
    TextView higlighttext;
    LinearLayout profile,help_support,report,change_pwd,logout;

    MessageDialog messageDialog;
    DigitalAssetHeaderRepository headerRepository;
    DigitalAssetTransactionRepository transactionRepository;
    DigitalAssetOfflineChildRepository childRepository;
    DigitalAssetTransactionChildRepository transactionChildRepository;
    DigitalAssetOfflineRepository offlineRepository;
    UploadActivity uploadActivity;
    TestResultRepository testResultRepository;
    CheckListTempRepository checkListTempRepository;
    DigitalassetCatTagFilterRepository digitalassetCatTagFilterRepository;
    CourseCatTagFilterRepository courseCatTagFilterRepository;
    ChecklistCatTagFilterRepository checklistCatTagFilterRepository;

    RelativeLayout header;
    ImageView profile_icn,support_icn,report_icn,changepwd_icn,logout_icn;
    LinearLayout chat;
    ImageView  chat_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_menu);

        mContext = MoreMenuActivity.this;


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

        initializeViews();
        onClickListeners();
        initializebottomnavigation();
        bottomnavigationonClickevents();
        setthemeforView();
    }

    public void initializeViews(){
        header = (RelativeLayout) findViewById(R.id.header);
        profile = (LinearLayout) findViewById(R.id.profile_layout_card);
        help_support = (LinearLayout) findViewById(R.id.help_support_layout_card);
        report = (LinearLayout) findViewById(R.id.reportclick_layout_card);
        change_pwd = (LinearLayout) findViewById(R.id.change_pwd_layout_card);
        logout = (LinearLayout) findViewById(R.id.logout_layout_card);
        chat = (LinearLayout) findViewById(R.id.chat);
        profile_icn = (ImageView)findViewById(R.id.profile_icn);
        support_icn = (ImageView)findViewById(R.id.support_icn);
        report_icn = (ImageView)findViewById(R.id.report_icn);
        changepwd_icn = (ImageView)findViewById(R.id.changepwd_icn);
        logout_icn = (ImageView)findViewById(R.id.logout_icn);
        chat_icon = (ImageView)findViewById(R.id.chat_icon);
        chat.setVisibility(View.GONE);
        report.setVisibility(View.GONE);
        if(PreferenceUtils.getLandingPageAccess(mContext) != null) {
            Gson gsonget = new Gson();
            LandingPageAccess landingobj = gsonget.fromJson(PreferenceUtils.getLandingPageAccess(mContext), LandingPageAccess.class);
            if (landingobj != null) {
                if (!TextUtils.isEmpty(landingobj.getChat()) && landingobj.getChat().equalsIgnoreCase("Y")) {
                    chat.setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(landingobj.getReport()) && landingobj.getReport().equalsIgnoreCase("Y")) {
                    report.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    public void setthemeforView() {

        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        pos4.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        higlighttext.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
        profile_icn.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        support_icn.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        report_icn.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        changepwd_icn.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        logout_icn.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        chat_icon.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
    }



    public void onClickListeners(){

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!NetworkUtils.checkIfNetworkAvailable(MoreMenuActivity.this)){
                    Toast.makeText(MoreMenuActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,UserProfileActivity.class);
                startActivity(i);
            }
        });

        help_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MoreMenuActivity.this,UserReportActivity.class);
                startActivity(i);
            }
        });

        change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MoreMenuActivity.this,ChangePasswordActivity.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreMenuActivity.this,ChatWebView.class);
                startActivity(intent);
            }
        });
    }

    public void initializebottomnavigation(){
        bottommenus = (LinearLayout) findViewById(R.id.bottommenus);
        lpcourse = findViewById(R.id.lpcourse);
        assetpage = findViewById(R.id.assetpage);
        chklistpage = findViewById(R.id.chklistpage);
        taskpage = findViewById(R.id.reports);
        morepage = findViewById(R.id.morepage);

        pos4 = (ImageView) findViewById(R.id.pos4);
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
                if(!NetworkUtils.checkIfNetworkAvailable(MoreMenuActivity.this)){
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
                if(!NetworkUtils.checkIfNetworkAvailable(MoreMenuActivity.this)){
                    Toast.makeText(MoreMenuActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,ChecklistLandingActivity.class);
                startActivity(i);
                finish();
            }
        });

        taskpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!NetworkUtils.checkIfNetworkAvailable(MoreMenuActivity.this)){
                    Toast.makeText(MoreMenuActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,TaskListActivity.class);
                startActivity(i);
                finish();
            }
        });

        morepage.setOnClickListener(new View.OnClickListener() {
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


    public void logoutDialog(){
        if (NetworkUtils.checkIfNetworkAvailable(mContext)) {
            messageDialog.ShowLogout(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View Approve) {
                    logoutProcess();
                    Toast.makeText(mContext,"App is shutting down...",Toast.LENGTH_LONG).show();
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
        PreferenceUtils.setCompanyCode(mContext,"");
        PreferenceUtils.setSubdomainName(mContext,"");
        PreferenceUtils.setUserId(mContext,0);
        PreferenceUtils.setLanguageChoosen(mContext,"");
        PreferenceUtils.setCompanyId(mContext,0);
        File file = new File(Environment.getExternalStorageDirectory()+"/"+Constants.Foldername);
        PreferenceUtils.setClientLogo(mContext,"");
        PreferenceUtils.setLandingPageAccess(mContext,"");
        PreferenceUtils.setCompanyThemeModel(mContext,"");
        PreferenceUtils.setClientLogo(mContext,null);
        SharedPreferences share_settings = mContext.getSharedPreferences("KANGLE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share_settings.edit();
        editor.clear();
        editor.commit();
        PreferenceUtils.setTheme("key",null,mContext);

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
        share_settings.edit().clear().commit();
        deleteAppData();
        deleteRecursive(file);
        deleteCache(mContext);
        Intent i = new Intent(mContext,LoginActivity.class);
        Toast.makeText(mContext,getResources().getString(R.string.logoutmessage),Toast.LENGTH_SHORT).show();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public static boolean deleteRecursive(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteRecursive(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteRecursive(dir);
        } catch (Exception e) {}
    }

    private void deleteAppData() {
        try {
            // clearing app data
            String packageName = getApplicationContext().getPackageName();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear "+packageName);

        } catch (Exception e) {
            e.printStackTrace();
        } }

}
