package com.swaas.kangle.Reports;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.swaas.kangle.API.model.LandingPageAccess;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.API.service.UserService;
import com.swaas.kangle.AssetListActivity;
import com.swaas.kangle.CheckList.ChecklistLandingActivity;
import com.swaas.kangle.LPCourse.CourseListActivity;
import com.swaas.kangle.R;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.report.ViewReportAsPdfActivity;
import com.swaas.kangle.userProfile.UserProfileActivity;
import com.swaas.kangle.utils.CommonUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class UserReportActivity extends AppCompatActivity {

    Context mContext;
    LinearLayout header;
    ImageView companylogo;
    ProgressDialog mProgressDialog;

    View overduetab,Inprogresstab,assignedtab,tot_comptab,dashboarddetails,retry;
    View ass_overduetab,ass_Inprogresstab,ass_assignedtab,ass_tot_comptab,ass_dashboarddetails,ass_retry;
    View chk_overduetab,chk_Inprogresstab,chk_assignedtab,chk_tot_comptab,chk_dashboarddetails,chk_retry;
    View tsk_overduetab,tsk_Inprogresstab,tsk_assignedtab,tsk_tot_comptab,tsk_dashboarddetails,tsk_retry;

    TextView totorcompValue,assignedvalue,Inprogressvalue,overduevalue,totorcomptext,assignedtext,Inprogresstext,overduetext;
    TextView ass_totorcompValue,ass_assignedvalue,ass_Inprogressvalue,ass_overduevalue,ass_totorcomptext,ass_assignedtext,ass_Inprogresstext,ass_overduetext;
    TextView chk_totorcompValue,chk_assignedvalue,chk_Inprogressvalue,chk_overduevalue,chk_totorcomptext,chk_assignedtext,chk_Inprogresstext,chk_overduetext;
    TextView tsk_totorcompValue,tsk_assignedvalue,tsk_Inprogressvalue,tsk_overduevalue,tsk_totorcomptext,tsk_assignedtext,tsk_Inprogresstext,tsk_overduetext;
    TextView title_Course,title_Asset,title_checklist,title_tasklist;

    LinearLayout my_list,group_list;
    View my_list_view,my_grplist_view;
    TextView my_list_text,my_grplist_text;
    NestedScrollView mainnestedView;
    RelativeLayout content_view;
    RelativeLayout course_sec,asset_sec,checklist_sec,task_sec;
    Gson gsonget;
    List<ReportModel> dashBoardDetailsModels;
    User userobj;
    int checkEnabledTab = 1;
    int teamorself = 0;

    View lpcourse,assetpage,chklistpage,profilepage,bottommenusection,reports;
    LinearLayout bottommenus;
    ImageView pos3;
    TextView higlighttext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);

        mContext = UserReportActivity.this;

        initializeView();
        //initializebottomnavigation();
        setthemeforView();
        onClickListeners();
        //bottomnavigationonClickevents();

        dashBoardDetailsModels = new ArrayList<ReportModel>();
        gsonget = new Gson();
        userobj = gsonget.fromJson(PreferenceUtils.getUser(this), User.class);
        getDashboardDetails();
        if (!NetworkUtils.isNetworkAvailable(mContext))
        {
            Toast.makeText(mContext,"Please enable your network",Toast.LENGTH_SHORT).show();
        }
    }

    public void initializeView(){

        my_list = (LinearLayout) findViewById(R.id.my_list);
        my_list_view = findViewById(R.id.my_list_view);
        my_list_text = (TextView) findViewById(R.id.my_list_text);

        group_list = (LinearLayout) findViewById(R.id.group_list);
        my_grplist_view = findViewById(R.id.my_grplist_view);
        my_grplist_text = (TextView) findViewById(R.id.my_grplist_text);

        mainnestedView = (NestedScrollView) findViewById(R.id.mainnestedView);
        header = (LinearLayout) findViewById(R.id.header);
        companylogo = (ImageView) findViewById(R.id.companylogo);
        content_view = (RelativeLayout) findViewById(R.id.content_view);
        course_sec =(RelativeLayout)findViewById(R.id.course_section);
        asset_sec =(RelativeLayout)findViewById(R.id.Asset_section);
        checklist_sec =(RelativeLayout)findViewById(R.id.checklist_section);
        task_sec =(RelativeLayout)findViewById(R.id.tasklist_section);

        initializeCourseViews();
        initializeAssetViews();
        initializeChecklistViews();
        initializeTasklistViews();
        checkmenu();
    }

    private void checkmenu() {
        if(PreferenceUtils.getLandingPageAccess(mContext) != null) {
            Gson gsonget = new Gson();
            LandingPageAccess landingobj = gsonget.fromJson(PreferenceUtils.getLandingPageAccess(mContext), LandingPageAccess.class);
            if (landingobj != null) {
                if (!TextUtils.isEmpty(landingobj.getLibrary()) && landingobj.getLibrary().equalsIgnoreCase("Y")) {
                    asset_sec.setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(landingobj.getCourse()) && landingobj.getCourse().equalsIgnoreCase("A")) {
                    //adCourse.setVisibility(View.VISIBLE);
                } else if (!TextUtils.isEmpty(landingobj.getCourse()) && landingobj.getCourse().equalsIgnoreCase("S")) {
                    //adCourse.setVisibility(View.VISIBLE);
                } else if (!TextUtils.isEmpty(landingobj.getCourse()) && landingobj.getCourse().equalsIgnoreCase("L")) {
                    course_sec.setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(landingobj.getTask()) && landingobj.getTask().equalsIgnoreCase("Y")) {
                    task_sec.setVisibility(View.VISIBLE);
                }

                if (!TextUtils.isEmpty(landingobj.getChecklist()) && landingobj.getChecklist().equalsIgnoreCase("Y")) {
                    checklist_sec.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    public void initializeCourseViews(){
        title_Course = (TextView) findViewById(R.id.title_Course);

        overduetab = findViewById(R.id.overduetab);
        Inprogresstab = findViewById(R.id.Inprogresstab);
        assignedtab = findViewById(R.id.assignedtab);
        tot_comptab = findViewById(R.id.tot_comptab);
        dashboarddetails = findViewById(R.id.dashboarddetails);
        retry = findViewById(R.id.retry);
        totorcompValue = (TextView) findViewById(R.id.totorcompValue);
        assignedvalue = (TextView) findViewById(R.id.assignedvalue);
        Inprogressvalue = (TextView) findViewById(R.id.Inprogressvalue);
        overduevalue = (TextView) findViewById(R.id.overduevalue);
        totorcomptext = (TextView) findViewById(R.id.totorcomptext);
        assignedtext = (TextView) findViewById(R.id.assignedtext);
        Inprogresstext = (TextView) findViewById(R.id.Inprogresstext);
        overduetext = (TextView) findViewById(R.id.overduetext);
    }

    public void initializeAssetViews(){
        title_Asset = (TextView) findViewById(R.id.title_Asset);

        ass_overduetab = findViewById(R.id.ass_overduetab);
        ass_Inprogresstab = findViewById(R.id.ass_Inprogresstab);
        ass_assignedtab = findViewById(R.id.ass_assignedtab);
        ass_tot_comptab = findViewById(R.id.ass_tot_comptab);
        ass_dashboarddetails = findViewById(R.id.ass_dashboarddetails);
        ass_retry = findViewById(R.id.ass_retry);
        ass_totorcompValue = (TextView) findViewById(R.id.ass_totorcompValue);
        ass_assignedvalue = (TextView) findViewById(R.id.ass_assignedvalue);
        ass_Inprogressvalue = (TextView) findViewById(R.id.ass_Inprogressvalue);
        ass_overduevalue = (TextView) findViewById(R.id.ass_overduevalue);
        ass_totorcomptext = (TextView) findViewById(R.id.ass_totorcomptext);
        ass_assignedtext = (TextView) findViewById(R.id.ass_assignedtext);
        ass_Inprogresstext = (TextView) findViewById(R.id.ass_Inprogresstext);
        ass_overduetext = (TextView) findViewById(R.id.ass_overduetext);
    }

    public void initializeChecklistViews(){
        title_checklist = (TextView) findViewById(R.id.title_checklist);

        chk_overduetab = findViewById(R.id.chk_overduetab);
        chk_Inprogresstab = findViewById(R.id.chk_Inprogresstab);
        chk_assignedtab = findViewById(R.id.chk_assignedtab);
        chk_tot_comptab = findViewById(R.id.chk_tot_comptab);
        chk_dashboarddetails = findViewById(R.id.chk_dashboarddetails);
        chk_retry = findViewById(R.id.chk_retry);
        chk_totorcompValue = (TextView) findViewById(R.id.chk_totorcompValue);
        chk_assignedvalue = (TextView) findViewById(R.id.chk_assignedvalue);
        chk_Inprogressvalue = (TextView) findViewById(R.id.chk_Inprogressvalue);
        chk_overduevalue = (TextView) findViewById(R.id.chk_overduevalue);
        chk_totorcomptext = (TextView) findViewById(R.id.chk_totorcomptext);
        chk_assignedtext = (TextView) findViewById(R.id.chk_assignedtext);
        chk_Inprogresstext = (TextView) findViewById(R.id.chk_Inprogresstext);
        chk_overduetext = (TextView) findViewById(R.id.chk_overduetext);
    }

    public void initializeTasklistViews(){
        title_tasklist = (TextView) findViewById(R.id.title_tasklist);

        tsk_overduetab = findViewById(R.id.tsk_overduetab);
        tsk_Inprogresstab = findViewById(R.id.tsk_Inprogresstab);
        tsk_assignedtab = findViewById(R.id.tsk_assignedtab);
        tsk_tot_comptab = findViewById(R.id.tsk_tot_comptab);
        tsk_dashboarddetails = findViewById(R.id.tsk_dashboarddetails);
        tsk_retry = findViewById(R.id.tsk_retry);
        tsk_totorcompValue = (TextView) findViewById(R.id.tsk_totorcompValue);
        tsk_assignedvalue = (TextView) findViewById(R.id.tsk_assignedvalue);
        tsk_Inprogressvalue = (TextView) findViewById(R.id.tsk_Inprogressvalue);
        tsk_overduevalue = (TextView) findViewById(R.id.tsk_overduevalue);
        tsk_totorcomptext = (TextView) findViewById(R.id.tsk_totorcomptext);
        tsk_assignedtext = (TextView) findViewById(R.id.tsk_assignedtext);
        tsk_Inprogresstext = (TextView) findViewById(R.id.tsk_Inprogresstext);
        tsk_overduetext = (TextView) findViewById(R.id.tsk_overduetext);
    }

    public void setthemeforView(){

        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        mainnestedView.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        content_view.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        //pos3.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        //higlighttext.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));

        setCourseTextColors();
        setAssetTextColors();
        setChklistTextColors();
        settsklistTextColors();
        toggletabs();
    }

    public void setCourseTextColors(){
        title_Course.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        totorcompValue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        assignedvalue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        Inprogressvalue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        overduevalue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        totorcomptext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        assignedtext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        Inprogresstext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        overduetext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
    }

    public void setAssetTextColors(){
        title_Asset.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        ass_totorcompValue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        ass_assignedvalue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        ass_Inprogressvalue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        ass_overduevalue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        ass_totorcomptext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        ass_assignedtext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        ass_Inprogresstext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        ass_overduetext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));

    }

    public void setChklistTextColors(){
        title_checklist.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        chk_totorcompValue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        chk_assignedvalue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        chk_Inprogressvalue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        chk_overduevalue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        chk_totorcomptext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        chk_assignedtext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        chk_Inprogresstext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        chk_overduetext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
    }

    public void settsklistTextColors(){
        title_tasklist.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_totorcompValue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_assignedvalue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_Inprogressvalue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_overduevalue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_totorcomptext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_assignedtext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_Inprogresstext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tsk_overduetext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
    }

    public void onClickListeners(){

        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        my_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEnabledTab = 1;
                teamorself = 0;
                toggletabs();
                loadDatabytab();
            }
        });

        group_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEnabledTab = 2;
                teamorself = 1;
                toggletabs();
                loadDatabytab();
            }
        });

        bindonclickselforteamDashboardvalues();
        bindonclickselforteamChecklistvalues();
        bindonclickselforteamTaskvalues();
        bindonclickAssetvalues();
        //bindonclickAssetvalues();
    }

    public void toggletabs(){
        if(checkEnabledTab == 2){
            my_list.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
            group_list.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
            my_grplist_text.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
            if(PreferenceUtils.getSubdomainName(mContext).toLowerCase().contains("shakeys"))
            {
                my_grplist_text.setTextColor(Color.GRAY);
            }
            my_list_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
            my_grplist_view.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            my_list_view.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        }else{
            my_list.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
            group_list.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
            my_list_text.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
            if(PreferenceUtils.getSubdomainName(mContext).toLowerCase().contains("shakeys"))
            {
                my_list_text.setTextColor(Color.GRAY);
            }
            my_grplist_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
            my_list_view.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            my_grplist_view.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        }
    }

    public void initializebottomnavigation(){
        bottommenusection = findViewById(R.id.bottommenusection);
        bottommenus = (LinearLayout) findViewById(R.id.bottommenus);
        lpcourse = findViewById(R.id.lpcourse);
        assetpage = findViewById(R.id.assetpage);
        chklistpage = findViewById(R.id.chklistpage);
        reports = findViewById(R.id.reports);
        profilepage = findViewById(R.id.profilepage);
        pos3 = (ImageView) findViewById(R.id.pos3);
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
                } else if(landingobj.getCourse().equalsIgnoreCase("A")){
                    lpcourse.setVisibility(View.VISIBLE);
                    count += 1;
                }else{
                    lpcourse.setVisibility(View.GONE);
                }
                if (landingobj.getChecklist().equalsIgnoreCase("Y")) {
                    chklistpage.setVisibility(View.VISIBLE);
                    count += 1;
                }else{
                    chklistpage.setVisibility(View.GONE);
                }
            }else{
                bottommenusection.setVisibility(View.GONE);
            }
        }

        bottommenus.setWeightSum(count);

        lpcourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetworkUtils.checkIfNetworkAvailable(UserReportActivity.this)){
                    //Toast.makeText(AssetListActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
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
                if(!NetworkUtils.checkIfNetworkAvailable(UserReportActivity.this)){
                    Toast.makeText(UserReportActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,ChecklistLandingActivity.class);
                startActivity(i);
                finish();
            }
        });

        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        profilepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetworkUtils.checkIfNetworkAvailable(UserReportActivity.this)){
                    Toast.makeText(UserReportActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,UserProfileActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    //Dashboard Details
    public void getDashboardDetails(){
        if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
            showProgressDialog();
            setProgressmessage(getResources().getString(R.string.loading));
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            UserService userService = retrofitAPI.create(UserService.class);
            String offsetFromUtc = CommonUtils.getUtcOffset();
            Call call = userService.getDashboardDetailsReport(PreferenceUtils.getSubdomainName(mContext),userobj.getCompany_Id(),userobj.getUserID(),offsetFromUtc);
            call.enqueue(new Callback<List<ReportModel>>() {

                @Override
                public void onResponse(Response<List<ReportModel>> response, Retrofit retrofit) {
                    List<ReportModel> apiResponse= response.body();
                    if (apiResponse != null) {
                        if(apiResponse.size() > 0 ) {
                            retry.setVisibility(View.GONE);
                            dashboarddetails.setVisibility(View.VISIBLE);
                            dashBoardDetailsModels = apiResponse;
                            Log.d("log", "assetsForBrowses");
                            loadDatabytab();
                        }else{
                            retry.setVisibility(View.VISIBLE);
                            dashboarddetails.setVisibility(View.GONE);
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

    public void loadDatabytab(){
        if(checkEnabledTab == 2){
            teamorself = 1;
            myTeamDashboarddetails();
        }else{
            teamorself = 0;
            myDashboarddetails();
        }
    }

    //My Dashboard Details
    public void myDashboarddetails(){
        dismissProgressDialog();
        if(dashBoardDetailsModels.size() > 0) {
            dismissProgressDialog();
            loadCourseDetails();
            loadAssetDetails();
            loadChecklistDetails();
            loadTaskDetails();
        }else{
            dismissProgressDialog();
            companywiselabel(0);
        }
    }
    //Course Details
    public void loadCourseDetails(){
        if(PreferenceUtils.getSubdomainName(mContext).contains("tacobell")) {
            totorcompValue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getTotal_Count())+"");
            assignedvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getCompleted()
                    +dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getMax_Attempt_Reached())+"");
            Inprogressvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getInProgress())+"");
            overduevalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getOverdue())+"");
            companywiselabel(1);

        } else {
            totorcompValue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getYet_to_Start())+"");
            assignedvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getCompleted()
                    +dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getMax_Attempt_Reached())+"");
            Inprogressvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getInProgress())+"");
            overduevalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getOverdue())+"");
            companywiselabel(2);
        }
    }
    //Asset Details
    public void loadAssetDetails(){

        ass_totorcompValue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getTotal_Assets())+"");
        ass_assignedvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getRead_Assets()+""));
        ass_Inprogressvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getUnread_Assets())+"");
        ass_overduevalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getLiked_Assets())+"");

        ass_totorcomptext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.total_assets)+"</u>"));
        ass_assignedtext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.read_assets)+"</u>"));
        ass_Inprogresstext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.unread_assets)+"</u>"));
        ass_overduetext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.liked_assets)+"</u>"));
    }
    //Checklist Details
    public void loadChecklistDetails(){
        chk_totorcompValue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getTotal_Checklist())+"");
        chk_assignedvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getCompleted_Checklist()+""));
        chk_Inprogressvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getYet_to_Start_Checklist())+"");
        chk_overduevalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getExpired_Checklist())+"");

        chk_totorcomptext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.total_checklist)+"</u>"));
        chk_assignedtext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.completed_checklist)+"</u>"));
        chk_Inprogresstext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.yettostart_checklist)+"</u>"));
        chk_overduetext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.expired_checklist)+"</u>"));
    }
    //Tasklist Details
    public void loadTaskDetails(){
        tsk_totorcompValue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getAssigned_Task())+"");
        tsk_assignedvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getCompleted_Task()+""));
        tsk_Inprogressvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getIn_progress_Task())+"");
        tsk_overduevalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getIndividual_Report().get(0).getReview_Task())+"");

        tsk_totorcomptext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.assigned_task)+"</u>"));
        tsk_assignedtext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.completed_task)+"</u>"));
        tsk_Inprogresstext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.in_progress_task)+"</u>"));
        tsk_overduetext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.review_task)+"</u>"));
    }
    //End mydashboard

    //my Team Dashboard Details
    public void myTeamDashboarddetails(){
        dismissProgressDialog();
        if(dashBoardDetailsModels.size() > 0) {
            dismissProgressDialog();
            loadTeamCourseDetails();
            loadAssetTeamDetails();
            loadChecklistTeamDetails();
            loadTaskTeamDetails();
        }else{
            dismissProgressDialog();
            companywiselabel(0);
        }
    }
    //Team Course Details
    public void loadTeamCourseDetails(){
        if(PreferenceUtils.getSubdomainName(mContext).contains("tacobell")) {

            totorcompValue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getTotal_Count_Team())+"");
            assignedvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getCompleted_Team()
                    +dashBoardDetailsModels.get(0).getTeam_Report().get(0).getMax_Attempt_Reached_Team())+"");
            Inprogressvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getInProgress_Team())+"");
            overduevalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getOverdue_Team())+"");

            companywiselabel(1);

        } else {
            totorcompValue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getYet_to_Start_Team())+"");
            assignedvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getCompleted_Team()
                    +dashBoardDetailsModels.get(0).getTeam_Report().get(0).getMax_Attempt_Reached_Team())+"");
            Inprogressvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getInProgress_Team())+"");
            overduevalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getOverdue_Team())+"");
            companywiselabel(2);
        }
    }
    //Team Asset Details
    public void loadAssetTeamDetails(){

        ass_totorcompValue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getTotal_Assets_Team())+"");
        ass_assignedvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getRead_Assets_Team()+""));
        ass_Inprogressvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getUnread_Assets_Team())+"");
        ass_overduevalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getLiked_Assets_Team())+"");

        ass_totorcomptext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.total_assets)+"</u>"));
        ass_assignedtext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.read_assets)+"</u>"));
        ass_Inprogresstext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.unread_assets)+"</u>"));
        ass_overduetext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.liked_assets)+"</u>"));
    }
    //Team Checklist Details
    public void loadChecklistTeamDetails(){
        chk_totorcompValue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getTotal_Checklist_Team())+"");
        chk_assignedvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getCompleted_Checklist_Team()+""));
        chk_Inprogressvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getYet_to_Start_Checklist_Team())+"");
        chk_overduevalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getExpired_Checklist_Team())+"");

        chk_totorcomptext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.total_checklist)+"</u>"));
        chk_assignedtext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.completed_checklist)+"</u>"));
        chk_Inprogresstext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.yettostart_checklist)+"</u>"));
        chk_overduetext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.expired_checklist)+"</u>"));
    }
    //Team Tasklist Details
    public void loadTaskTeamDetails(){
        tsk_totorcompValue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getAssigned_Task_Team())+"");
        tsk_assignedvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getCompleted_Task_Team()+""));
        tsk_Inprogressvalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getIn_progress_Task_Team())+"");
        tsk_overduevalue.setText(String.valueOf(dashBoardDetailsModels.get(0).getTeam_Report().get(0).getReview_Task_Team())+"");

        tsk_totorcomptext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.assigned_task)+"</u>"));
        tsk_assignedtext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.completed_task)+"</u>"));
        tsk_Inprogresstext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.in_progress_task)+"</u>"));
        tsk_overduetext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.review_task)+"</u>"));
    }
    //End myteam dashboard

    public void bindonclickselforteamDashboardvalues(){

        overduetab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToReportActivity(Constants.Over,Constants.Module_Course,1);
            }
        });

        Inprogresstab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceUtils.getSubdomainName(mContext).contains("tacobell")) {
                    CallToReportActivity(Constants.Ass,Constants.Module_Course,1);
                } else {
                    CallToReportActivity(Constants.In,Constants.Module_Course,1);
                }
            }
        });

        assignedtab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToReportActivity(Constants.Com,Constants.Module_Course,1);
            }
        });

        tot_comptab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceUtils.getSubdomainName(mContext).contains("tacobell")) {
                    CallToReportActivity(Constants.Tot,Constants.Module_Course,1);
                } else {
                    CallToReportActivity(Constants.Yet,Constants.Module_Course,1);
                }
            }
        });

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDashboardDetails();
            }
        });
    }

    public void bindonclickAssetvalues(){

        ass_tot_comptab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToReportActivity(Constants.Tot,Constants.Module_Asset,3);
            }
        });

        ass_assignedtab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToReportActivity(Constants.Rd,Constants.Module_Asset,3);
            }
        });

        ass_overduetab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToReportActivity(Constants.Lkd,Constants.Module_Asset,3);
            }
        });

        ass_Inprogresstab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToReportActivity(Constants.Urd,Constants.Module_Asset,3);
            }
        });

        ass_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDashboardDetails();
            }
        });
    }

    public void bindonclickselforteamChecklistvalues(){

        chk_tot_comptab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToReportActivity(Constants.Tot,Constants.Module_Checklist,2);
            }
        });

        chk_assignedtab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToReportActivity(Constants.Com,Constants.Module_Checklist,2);
            }
        });

        chk_Inprogresstab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToReportActivity(Constants.Yet,Constants.Module_Checklist,2);
            }
        });

        chk_overduetab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToReportActivity(Constants.Exp,Constants.Module_Checklist,2);
            }
        });

        chk_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDashboardDetails();
            }
        });
    }

    public void bindonclickselforteamTaskvalues(){

        tsk_tot_comptab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToReportActivity(Constants.Tot,Constants.Module_Task,4);
            }
        });

        tsk_assignedtab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToReportActivity(Constants.Com,Constants.Module_Task,4);
            }
        });

        tsk_Inprogresstab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToReportActivity(Constants.In,Constants.Module_Task,4);
            }
        });

        tsk_overduetab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToReportActivity(Constants.Rev,Constants.Module_Task,4);
            }
        });

        tsk_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDashboardDetails();
            }
        });
    }


    public void CallToReportActivity(String CourseStatus,String module,int moduletype){
        if(checkEnabledTab == 1) {
            Intent intent = new Intent(UserReportActivity.this, ViewReportAsPdfActivity.class);
            intent.putExtra(Constants.Course_Status, CourseStatus);
            intent.putExtra(Constants.moduleName, module);
            intent.putExtra(Constants.moduleType, moduletype);
            intent.putExtra(Constants.isTeamOrSelf, teamorself);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(UserReportActivity.this, ViewReportAsPdfActivity.class);
            intent.putExtra(Constants.Course_Status, CourseStatus);
            intent.putExtra(Constants.moduleName, module);
            intent.putExtra(Constants.moduleType, moduletype);
            intent.putExtra(Constants.isTeamOrSelf, teamorself);
            startActivity(intent);
        }
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

        ass_totorcomptext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.total_assets)+"</u>"));
        ass_assignedtext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.read_assets)+"</u>"));
        ass_Inprogresstext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.unread_assets)+"</u>"));
        ass_overduetext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.liked_assets)+"</u>"));

        chk_totorcomptext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.total_checklist)+"</u>"));
        chk_assignedtext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.completed_checklist)+"</u>"));
        chk_Inprogresstext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.yettostart_checklist)+"</u>"));
        chk_overduetext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.expired_checklist)+"</u>"));

        tsk_totorcomptext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.assigned_task)+"</u>"));
        tsk_assignedtext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.completed_task)+"</u>"));
        tsk_Inprogresstext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.in_progress_task)+"</u>"));
        tsk_overduetext.setText(Html.fromHtml("<u>"+getResources().getString(R.string.review_task)+"</u>"));
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
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

}
