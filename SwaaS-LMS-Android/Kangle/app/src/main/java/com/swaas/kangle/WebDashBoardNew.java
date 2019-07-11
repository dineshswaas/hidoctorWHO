package com.swaas.kangle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.location.LocationListener;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.API.service.CourseService;
import com.swaas.kangle.LPCourse.SectionActivity;
import com.swaas.kangle.adapter.WebDashBoardListCompletedRecyclerAdapter;
import com.swaas.kangle.adapter.WebDashboardListItemRecyclerAdapter;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.models.DashBoardDetailsModel;
import com.swaas.kangle.models.TopfiveDashBoardAssetDetailModel;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.report.ViewReportAsPdfActivity;
import com.swaas.kangle.utils.CommonUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.MessageDialog;
import com.swaas.kangle.utils.NetworkUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class WebDashBoardNew extends AppCompatActivity implements LocationListener {

    Context mContext;
    PieChart pieChart;
    public int[] yValues;
    public String[] xValues;
    public  static int[] MY_COLORS;
    NestedScrollView scrollview;
    ImageView companylogo,notification,settings;
    View emptyview;
    EmptyRecyclerView latestcourses,completedcourses;
    LinearLayoutManager latestlayoutManager,completedlayoutManager;
    WebDashBoardListCompletedRecyclerAdapter completedRecyclerAdapter;
    WebDashboardListItemRecyclerAdapter dashboardListItemRecyclerAdapter;
    Retrofit retrofitAPI;
    CourseService courseService;
    Gson gsonget;

    String subdomainName;
    String offsetFromUtc;
    User userobj;

    List<DashBoardDetailsModel> dashBoardDetailsModels;
    List<TopfiveDashBoardAssetDetailModel> LatestCourses;
    List<TopfiveDashBoardAssetDetailModel> CompletedCourses;
    int [] x = new int[4];
    boolean isTacobell;

    ProgressDialog mProgressDialog;

    WebView helpView;
    View helplayout;
    TextView closehelp;
    MessageDialog messageDialog;

    RelativeLayout latestsection,completedsection;
    LinearLayout changeViewparam;

    public double latitude,longitude;
    UploadActivity uploadActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getBoolean(R.bool.portrait_only)){
            setContentView(R.layout.activity_web_dash_board_new);
        }else{
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setContentView(R.layout.activity_web_dash_board_new_tab_land);
            } else {
                setContentView(R.layout.activity_web_dash_board_new_tab_port);
            }

        }



        mContext = WebDashBoardNew.this;
        //getSupportActionBar().hide();
        uploadActivity = new UploadActivity(mContext);
        initialisViews();
        bindClickListeners();
        setupRecyclerView();

        gsonget = new Gson();
        retrofitAPI = RetrofitAPIBuilder.getInstance();
        courseService = retrofitAPI.create(CourseService.class);

        subdomainName = PreferenceUtils.getSubdomainName(mContext);
        //offsetFromUtc = CommonUtils.getUtcOffset();
        offsetFromUtc = CommonUtils.getUtcOffsetincluded10k();
        userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);

        dashBoardDetailsModels = new ArrayList<DashBoardDetailsModel>();
        LatestCourses = new ArrayList<TopfiveDashBoardAssetDetailModel>();
        CompletedCourses = new ArrayList<TopfiveDashBoardAssetDetailModel>();

        /*if(PreferenceUtils.getSubdomainName(mContext).contains("tacobell")){
            scrollview.setBackgroundColor(getResources().getColor(R.color.tacobellbackground));
            emptyview.setBackgroundColor(getResources().getColor(R.color.tacobellbackground));
        }else {
            scrollview.setBackgroundColor(getResources().getColor(R.color.otherCompanies));
            emptyview.setBackgroundColor(getResources().getColor(R.color.otherCompanies));
        }*/
        scrollview.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        emptyview.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        Ion.with(companylogo).placeholder(R.color.black).error(R.color.black).load(
                (!TextUtils.isEmpty(PreferenceUtils.getClientLogo(mContext)))? PreferenceUtils.getClientLogo(mContext) : PreferenceUtils.getClientLogo(mContext));

        getDashboardDetails();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadActivity.insertUserTracking("Dashboard",latitude,longitude);
    }

    public void initialisViews(){
        pieChart = (PieChart) findViewById(R.id.piechart);
        scrollview = (NestedScrollView) findViewById(R.id.background);
        companylogo = (ImageView) findViewById(R.id.companylogo);
        latestcourses = (EmptyRecyclerView) findViewById(R.id.latest_courses);
        completedcourses = (EmptyRecyclerView) findViewById(R.id.completed_courses);
        emptyview = findViewById(R.id.empty_view);

        notification = (ImageView) findViewById(R.id.notification);
        settings = (ImageView) findViewById(R.id.settings);
        helpView = (WebView) findViewById(R.id.helpview);
        helplayout = findViewById(R.id.helplayout);
        closehelp = (TextView) findViewById(R.id.closehelp);
        messageDialog = new MessageDialog(mContext);

        latestsection = (RelativeLayout) findViewById(R.id.latest_section);
        completedsection = (RelativeLayout) findViewById(R.id.completed_section);
        changeViewparam = (LinearLayout) findViewById(R.id.changeViewparam);
    }
    public void bindClickListeners(){
        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
                    if(!getResources().getBoolean(R.bool.portrait_only)){
                        loadPopUpHelpView();
                    }else {
                        loadHelpView();
                    }
                }else{
                    Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                }
            }
        });

        closehelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollview.setVisibility(View.VISIBLE);
                helplayout.setVisibility(View.GONE);
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
                    Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void setupRecyclerView(){
        latestlayoutManager = new LinearLayoutManager(this);
        completedlayoutManager = new LinearLayoutManager(this);
        latestcourses.setLayoutManager(latestlayoutManager);
        completedcourses.setLayoutManager(completedlayoutManager);
    }

    public void getDashboardDetails(){
        showProgressDialog();
        Call call = courseService.getDashboardDetails(subdomainName,userobj.getCompany_Id(),userobj.getUserID(),offsetFromUtc);
        call.enqueue(new Callback<List<DashBoardDetailsModel>>() {

            @Override
            public void onResponse(Response<List<DashBoardDetailsModel>> response, Retrofit retrofit) {
                List<DashBoardDetailsModel> apiResponse= response.body();
                if (apiResponse != null) {
                    if(apiResponse.size() > 0 ) {
                        dashBoardDetailsModels = apiResponse;
                        Log.d("log", "assetsForBrowses");
                        getTop5LatesCourses();
                    }else{
                        dismissProgressDialog();
                        getTop5LatesCourses();
                        emptyview.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d("retrofit","error 2");
                    dismissProgressDialog();
                    emptyview.setVisibility(View.VISIBLE);
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
    }

    public void getTop5LatesCourses(){
        Call call = courseService.getTopFiveLatestCourses(subdomainName,userobj.getCompany_Id(),userobj.getUserID(),offsetFromUtc);
        call.enqueue(new Callback<List<TopfiveDashBoardAssetDetailModel>>() {

            @Override
            public void onResponse(Response<List<TopfiveDashBoardAssetDetailModel>> response, Retrofit retrofit) {
                List<TopfiveDashBoardAssetDetailModel> apiResponse= response.body();
                if (apiResponse != null) {
                    if(apiResponse.size() > 0){
                        LatestCourses = apiResponse;
                        Log.d("log", "assetsForBrowses");
                        gettop5CompletedCourses();
                    }else{
                        latestsection.setVisibility(View.GONE);
                        gettop5CompletedCourses();
                        //dismissProgressDialog();
                        //emptyview.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d("retrofit","error 2");
                    dismissProgressDialog();
                    emptyview.setVisibility(View.VISIBLE);
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
    }

    public void gettop5CompletedCourses(){
        Call call = courseService.getTopFiveCompletedCourses(subdomainName,userobj.getCompany_Id(),userobj.getUserID(),offsetFromUtc);
        call.enqueue(new Callback<List<TopfiveDashBoardAssetDetailModel>>() {

            @Override
            public void onResponse(Response<List<TopfiveDashBoardAssetDetailModel>> response, Retrofit retrofit) {
                List<TopfiveDashBoardAssetDetailModel> apiResponse= response.body();
                if (apiResponse != null) {
                    if(apiResponse.size() > 0) {
                        CompletedCourses = apiResponse;
                        Log.d("log", "assetsForBrowses");
                        details();
                    }else{
                        completedsection.setVisibility(View.GONE);
                        details();
                        //dismissProgressDialog();
                        //emptyview.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d("retrofit","error 2");
                    dismissProgressDialog();
                    emptyview.setVisibility(View.VISIBLE);
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
    }

    public void details(){
        if(dashBoardDetailsModels.size() > 0) {
            if (dashBoardDetailsModels.get(0).getTotal_Count() > 0) {
                dismissProgressDialog();
                scrollview.setVisibility(View.VISIBLE);
                loadDetails();
            } else {
                dismissProgressDialog();
                scrollview.setVisibility(View.GONE);
                emptyview.setVisibility(View.VISIBLE);
            }
        }else{
            dismissProgressDialog();
            scrollview.setVisibility(View.GONE);
            emptyview.setVisibility(View.VISIBLE);
        }
    }

    public void loadDetails(){
        if(PreferenceUtils.getSubdomainName(mContext).contains("tacobell")) {

            ArrayList<Integer> xlistitems = new ArrayList<>();
            ArrayList<String>  xNameValues = new ArrayList<>();
            ArrayList<Integer> Colors =  new ArrayList<>();

            if (dashBoardDetailsModels.get(0).getTotal_Count() !=0 ){
                xlistitems.add(dashBoardDetailsModels.get(0).getTotal_Count());
                xNameValues.add(getResources().getString(R.string.total_course));
                Colors.add(getResources().getColor(R.color.loginbutton));

            }
            if (dashBoardDetailsModels.get(0).getCompleted()!=0){
                xlistitems.add(dashBoardDetailsModels.get(0).getCompleted()+dashBoardDetailsModels.get(0).getMax_Attempt_Reached());
                xNameValues.add(getResources().getString(R.string.completed_course));
                Colors.add(getResources().getColor(R.color.buttoncolor));


            }
            if (dashBoardDetailsModels.get(0).getInProgress() != 0){

                xlistitems.add(dashBoardDetailsModels.get(0).getInProgress());
                xNameValues.add(getResources().getString(R.string.assigned_course));
                Colors.add(getResources().getColor(R.color.amber));


            }
            if (dashBoardDetailsModels.get(0).getOverdue()!=0){

                xNameValues.add(getResources().getString(R.string.Overdue_course));
                xlistitems.add(dashBoardDetailsModels.get(0).getOverdue());
                Colors.add(getResources().getColor(R.color.piebrown));


            }
            x = convertIntegers(xlistitems);
            xValues = xNameValues.toArray(new String[0]);;
            MY_COLORS = convertIntegers(Colors);

        } else {

            /*    x = new int[]{dashBoardDetailsModels.get(0).getCompleted(),
                    dashBoardDetailsModels.get(0).getYet_to_Start(),
                    dashBoardDetailsModels.get(0).getInProgress(),
                    dashBoardDetailsModels.get(0).getOverdue()};
            xValues = new String[]{getResources().getString(R.string.completed_course),
                    getResources().getString(R.string.yet_to_start),
                    getResources().getString(R.string.in_progress),
                    getResources().getString(R.string.Overdue_course)};
        */

            ArrayList<Integer> xlistitems = new ArrayList<>();
            ArrayList<String>  xNameValues = new ArrayList<>();
            ArrayList<Integer> Colors =  new ArrayList<>();
            if (dashBoardDetailsModels.get(0).getYet_to_Start() !=0 ){

                xlistitems.add(dashBoardDetailsModels.get(0).getYet_to_Start());
                xNameValues.add(getResources().getString(R.string.yet_to_start));
                Colors.add(getResources().getColor(R.color.pieorange));

            }
            if (dashBoardDetailsModels.get(0).getCompleted()!=0){
                xlistitems.add(dashBoardDetailsModels.get(0).getCompleted()+dashBoardDetailsModels.get(0).getMax_Attempt_Reached());
                xNameValues.add(getResources().getString(R.string.completed_course));
                Colors.add(getResources().getColor(R.color.buttoncolor));
            }
            if (dashBoardDetailsModels.get(0).getInProgress() != 0){

                xlistitems.add(dashBoardDetailsModels.get(0).getInProgress());
                xNameValues.add(getResources().getString(R.string.in_progress));
                Colors.add(getResources().getColor(R.color.amber));

            }
            if (dashBoardDetailsModels.get(0).getOverdue()!=0){

                xNameValues.add(getResources().getString(R.string.Overdue_course));
                xlistitems.add(dashBoardDetailsModels.get(0).getOverdue());
                Colors.add(getResources().getColor(R.color.piebrown));

            }

            x = convertIntegers(xlistitems);
            xValues = xNameValues.toArray(new String[0]);
            MY_COLORS = convertIntegers(Colors);

            /*MY_COLORS = new int[]{getResources().getColor(R.color.loginbutton),
                    getResources().getColor(R.color.pieorange),
                    getResources().getColor(R.color.buttoncolor),
                    getResources().getColor(R.color.piebrown)};
         */

        }

        yValues = x;
        pieChart.setDescription("");
        pieChart.setRotationEnabled(true);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;

                if (xValues[e.getXIndex()].equals(getResources().getString(R.string.completed_course))){

                    CallToReportActivity(Constants.Com);


                }else if (xValues[e.getXIndex()].equals(getResources().getString(R.string.yet_to_start))){

                    CallToReportActivity(Constants.Yet);


                }else if (xValues[e.getXIndex()].equals(getResources().getString(R.string.in_progress))){

                    CallToReportActivity(Constants.In);


                }else if (xValues[e.getXIndex()].equals(getResources().getString(R.string.Overdue_course))){

                    CallToReportActivity(Constants.Over);

                }else if (xValues[e.getXIndex()].equals(getResources().getString(R.string.total_course))){

                    CallToReportActivity(Constants.Tot);

                }else if (xValues[e.getXIndex()].equals(getResources().getString(R.string.assigned_course))){

                    CallToReportActivity(Constants.Ass);

                }


            }

            @Override
            public void onNothingSelected() {

            }
        });

        // setting sample Data for Pie Chart
        setDataForPieChart();
        loadAdapter();
        loadAdapterClick();
    }


    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }


    public void CallToReportActivity(String CourseStatus){

        Intent intent =  new Intent(WebDashBoardNew.this, ViewReportAsPdfActivity.class);
        intent.putExtra(Constants.Course_Status,CourseStatus);
        startActivity(intent);

    }

    public void loadAdapter(){
        dashboardListItemRecyclerAdapter = new WebDashboardListItemRecyclerAdapter(WebDashBoardNew.this,LatestCourses);
        latestcourses.setAdapter(dashboardListItemRecyclerAdapter);
        completedRecyclerAdapter = new WebDashBoardListCompletedRecyclerAdapter(WebDashBoardNew.this,CompletedCourses);
        completedcourses.setAdapter(completedRecyclerAdapter);
    }

    public void loadAdapterClick(){
        dashboardListItemRecyclerAdapter.setOnItemClickListener(new WebDashboardListItemRecyclerAdapter.MyClickListener() {
            @Override
            public void onItemClick(TopfiveDashBoardAssetDetailModel course) {
                Intent intent = new Intent(mContext, SectionActivity.class);
                intent.putExtra(Constants.Course_Id, course.getCourse_Id());
                intent.putExtra(Constants.Publish_Id, course.getPublish_Id());
                intent.putExtra(Constants.Course_Name, course.getCourse_Name());
                intent.putExtra(Constants.Course_Description, course.getCourse_Description());
                intent.putExtra(Constants.Course_Thumbnail, course.getCourse_Image_URL());
                intent.putExtra(Constants.Course_Status, course.getCourse_Status_String());
                intent.putExtra(Constants.Is_From_DashBoard,true);
                intent.putExtra("Course_Status_INT", course.getCourse_Status_Value());
                intent.putExtra("Cats",course.getCategory_Name());
                intent.putExtra("Tags",course.getCourse_Tags());
                Bundle bundle = new Bundle();
                bundle.putSerializable("value", course);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    public void setDataForPieChart() {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int i = 0; i < yValues.length; i++) {
            if (yValues[i] == 0){
                continue;
            }else {
                yVals1.add(new Entry(yValues[i], i));
                xVals.add(xValues[i]);
                colors.add(MY_COLORS[i]);
            }
        }
        /*ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < xValues.length; i++)
            xVals.add(xValues[i]);*/
        // create pieDataSet
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(2);
        dataSet.setSelectionShift(2);
        // adding colors
        /*ArrayList<Integer> colors = new ArrayList<Integer>();
        // Added My Own colors
        for (int c : MY_COLORS)
            colors.add(c);*/

        dataSet.setColors(colors);
        //  create pie data object and set xValues and yValues and set it to the pieChart
        PieData data = new PieData(xVals, dataSet);
        //   data.setValueFormatter(new DefaultValueFormatter());
        //   data.setValueFormatter(new PercentFormatter());
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(18f);
        data.setValueTextColor(getResources().getColor(R.color.white));

        pieChart.setData(data);
        // undo all highlights
        pieChart.highlightValues(null);
        // refresh/update pie chart
        pieChart.invalidate();
        //removed xvalues from Piechart
        pieChart.setDrawSliceText(false);
        // animate piechart
        pieChart.animateXY(2000, 2000);

        // Legends to show on bottom of the graph
        Legend l = pieChart.getLegend();
        if(getResources().getBoolean(R.bool.portrait_only)){
            if(Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("English")){
                //l.setPosition(Legend.LegendPosition.PIECHART_CENTER);
                l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
                l.setXEntrySpace(10);
                l.setYEntrySpace(1);
                l.setTextSize(12f);
                l.setTextColor(getResources().getColor(R.color.white));
            //}else (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("Español")){
            }else {
                l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
                l.setXEntrySpace(2);
                l.setYEntrySpace(2);
                l.setTextSize(12f);
                l.setTextColor(getResources().getColor(R.color.white));
            }/*else {
                l.setPosition(Legend.LegendPosition.PIECHART_CENTER);
                l.setXEntrySpace(2);
                l.setYEntrySpace(2);
                l.setTextSize(12f);
            }*/
        }else{
            l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
            l.setXEntrySpace(10);
            l.setYEntrySpace(10);
            l.setTextSize(18f);
            l.setTextColor(getResources().getColor(R.color.white));
        }


        /*pieChart.setEntryLabelColor(getResources().getColor(R.color.white));
        pieChart.setEntryLabelTextSize(12f);*/
    }

    public void showProgressDialog() {
        if(!isFinishing()) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getResources().getString(R.string.loading));
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if(!isFinishing()){
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    public void loadHelpView(){
        scrollview.setVisibility(View.GONE);
        emptyview.setVisibility(View.GONE);
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
            URL = Constants.COMPANY_BASE_URL+"/HelpFiles/taco/"+lan+"Kanglehelpreportandsupport.htm";
        }else{
            URL = Constants.COMPANY_BASE_URL+"/HelpFiles/other/Kanglehelpreportandsupport.html";
        }
        //String URL = Constants.COMPANY_BASE_URL+"/HelpFiles/"+lan+"Kanglehelpreportandsupport.htm";
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
            URL = Constants.COMPANY_BASE_URL+"/HelpFiles/taco/"+lan+"Kanglehelpreportandsupport.htm";
        }else{
            URL = Constants.COMPANY_BASE_URL+"/HelpFiles/other/Kanglehelpreportandsupport.html";
        }
        //String URL = Constants.COMPANY_BASE_URL+"/HelpFiles/"+lan+"Kanglehelpreportandsupport.htm";
        messageDialog.Showhelppopup(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View Approve) {
                messageDialog.dialogDismiss();
            }
        },URL, false);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
}

    class MyValueFormatter implements ValueFormatter {

    private DecimalFormat mFormat;

    public MyValueFormatter() {
        mFormat = new DecimalFormat("###,###,###"); // use one decimal if needed
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // write your logic here
        return mFormat.format(value) + ""; // e.g. append a dollar-sign
    }
}
