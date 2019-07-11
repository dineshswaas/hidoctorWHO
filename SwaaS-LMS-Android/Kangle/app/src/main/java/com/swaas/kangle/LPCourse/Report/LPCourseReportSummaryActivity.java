package com.swaas.kangle.LPCourse.Report;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.LPCourse.LPCourseService;
import com.swaas.kangle.LPCourse.model.LPCourseReportSummaryModel;
import com.swaas.kangle.R;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.CommonUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.DateHelper;
import com.swaas.kangle.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LPCourseReportSummaryActivity extends AppCompatActivity {

    LPCourseSummaryReportAdapter lpCourseReportSummaryAdapter;
    EmptyRecyclerView recyclerView;
    ImageView companylogo;
    View mEmptyView;
    TextView courseName,SectionName,userName,Dateoftest,attemptcount;
    Context mContext;
    ProgressDialog mProgressDialog;
    List<LPCourseReportSummaryModel> reportList;
    int ExamId;
    Dialog explanation;
    int noOfQuestionsAttempted =0;

    TextView scorevalue, noquestionvalue, noQuestionsattendedvalue, correctanswerquestion, negativemarks;
    ImageView resultqualified;
    //CardView carddetails;
    View carddetails;

    CoordinatorLayout activity_list;
    int showsummary,attemptcountvalue;
    View header;
    RelativeLayout questionsdetails;
    TextView scoreText,noQuestionText,noQuestionsattendedtext,
            correctanswerquestiontext,negativemarkstext,resultqualifiedtext,ques;
    Boolean iscourseReport = false;
    int courseID = 0;
    RelativeLayout mark;
    TextView obtainedmarks,totalmarks;
    int obtainedmark,totalmark = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpcourse_report_summary);

        mContext = LPCourseReportSummaryActivity.this;
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        reportList = new ArrayList<>();
        initializeView();

        if(getIntent()!= null){
            ExamId = getIntent().getIntExtra(Constants.Exam_Id,0);
            showsummary = getIntent().getIntExtra("showfullsummary",0);
            attemptcountvalue = getIntent().getIntExtra("showattemptcount",0);
            iscourseReport = getIntent().getBooleanExtra("iscourseReport",false);
            courseID = getIntent().getIntExtra("courseID",0);

        }
        setUpRecyclerView();
        setthemeforView();
        if (iscourseReport == true)
        {
            getReportforCourse();
            mark.setVisibility(View.VISIBLE);
        }
        else {
            getReportforSection();
        }
        onClickListeners();
    }

    public void initializeView(){
        companylogo = (ImageView) findViewById(R.id.companylogo);
        mEmptyView = findViewById(R.id.empty_view);
        recyclerView = (EmptyRecyclerView)findViewById(R.id.reportrecyclerview);
        courseName = (TextView) findViewById(R.id.courseName);
        SectionName = (TextView) findViewById(R.id.SectionName);
        userName = (TextView) findViewById(R.id.userName);
        Dateoftest = (TextView) findViewById(R.id.Dateoftest);
        attemptcount = (TextView) findViewById(R.id.attemptcount);

        scorevalue = (TextView) findViewById(R.id.scorevalue);
        noquestionvalue = (TextView) findViewById(R.id.noquestionvalue);
        noQuestionsattendedvalue = (TextView) findViewById(R.id.noQuestionsattendedvalue);
        correctanswerquestion = (TextView) findViewById(R.id.correctanswerquestion);
        negativemarks = (TextView) findViewById(R.id.negativemarks);
        resultqualified = (ImageView) findViewById(R.id.resultqualified);

        activity_list = (CoordinatorLayout) findViewById(R.id.parent_layout);
        carddetails = findViewById(R.id.carddetails);

        header = findViewById(R.id.header);
        questionsdetails = (RelativeLayout) findViewById(R.id.questionsdetails);

        scoreText = (TextView) findViewById(R.id.scoreText);
        noQuestionText = (TextView) findViewById(R.id.noQuestionText);
        noQuestionsattendedtext = (TextView) findViewById(R.id.noQuestionsattendedtext);
        correctanswerquestiontext = (TextView) findViewById(R.id.correctanswerquestiontext);
        negativemarkstext = (TextView) findViewById(R.id.negativemarkstext);
        resultqualifiedtext = (TextView) findViewById(R.id.resultqualifiedtext);
        ques = (TextView) findViewById(R.id.ques);
        mark = findViewById(R.id.mark);
        obtainedmarks = (TextView) findViewById(R.id.obtained);
        totalmarks =(TextView) findViewById(R.id.total);
    }

    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void setthemeforView(){

        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        activity_list.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        questionsdetails.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.CARDBACKGROUND_COLOR)));
        companylogo.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        SectionName.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        courseName.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        Dateoftest.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        attemptcount.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        scoreText.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        noQuestionText.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        noQuestionsattendedtext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        correctanswerquestiontext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        negativemarkstext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        resultqualifiedtext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        ques.setTextColor(Color.parseColor(Constants.TEXT_COLOR));

        scorevalue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        noquestionvalue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        noQuestionsattendedvalue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        correctanswerquestion.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        negativemarks.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
    }

    public void getReportforSection(){

        if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
            showProgressDialog();
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            LPCourseService userService = retrofitAPI.create(LPCourseService.class);

            //String offsetFromUtc = CommonUtils.getUtcOffset();
            String offsetFromUtc = CommonUtils.getUtcOffsetincluded10k();
            Log.d("getUTC", offsetFromUtc);
            int CompanyId = PreferenceUtils.getCompnayId(mContext);
            Log.d("CompanyId", String.valueOf(CompanyId));
            String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            Call call = userService.getLPSectionQuestionDetails(SubdomainName,ExamId,offsetFromUtc,CompanyId);
            call.enqueue(new Callback<ArrayList<LPCourseReportSummaryModel>>() {

                @Override
                public void onResponse(Response<ArrayList<LPCourseReportSummaryModel>> response, Retrofit retrofit) {
                    List<LPCourseReportSummaryModel> courseListModel = response.body();
                    if (courseListModel != null && courseListModel.size() > 0) {
                        reportList = courseListModel;
                        courseName.setText(reportList.get(0).getCourse_Name());
                        SectionName.setText(reportList.get(0).getSection_Name());
                        userName.setText(reportList.get(0).getUser_Name()+" ("+reportList.get(0).getEmployee_Name()+")");
                        //DateHelper datehelper = new DateHelper();
                        //Dateoftest.setText(datehelper.getDisplayFormatwithUTC(reportList.get(0).getSection_Exam_End_Time(),"yyyy-MM-dd'T'hh:mm:ss"));
                        Dateoftest.setText(mContext.getResources().getString(R.string.attended_on) +" "+reportList.get(0).getFormatted_Section_Exam_Start_Time());
                        loadaadapterData();
                        dismissProgressDialog();
                        calculateReport();
                    } else {
                        carddetails.setVisibility(View.GONE);
                        mEmptyView.setVisibility(View.VISIBLE);
                        questionsdetails.setVisibility(View.GONE);
                        dismissProgressDialog();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(), "Error");
                    mEmptyView.setVisibility(View.VISIBLE);
                    questionsdetails.setVisibility(View.GONE);
                    dismissProgressDialog();
                }
            });

        }
    }

    public void getReportforCourse(){

        if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
            showProgressDialog();
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            LPCourseService userService = retrofitAPI.create(LPCourseService.class);

            //String offsetFromUtc = CommonUtils.getUtcOffset();
            String offsetFromUtc = CommonUtils.getUtcOffsetincluded10k();
            Log.d("getUTC", offsetFromUtc);
            int CompanyId = PreferenceUtils.getCompnayId(mContext);
            int userID = PreferenceUtils.getUserId(mContext);
            Log.d("CompanyId", String.valueOf(CompanyId));
            String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            Call call = userService.getCourseFullcourseReportList(courseID,userID,CompanyId);
            call.enqueue(new Callback<ArrayList<LPCourseReportSummaryModel>>() {

                @Override
                public void onResponse(Response<ArrayList<LPCourseReportSummaryModel>> response, Retrofit retrofit) {
                    List<LPCourseReportSummaryModel> courseListModel = response.body();
                    if (courseListModel != null && courseListModel.size() > 0) {
                        reportList = courseListModel;
                        courseName.setText(reportList.get(0).getCourse_Name());
                        SectionName.setText(reportList.get(0).getSection_Name());
                        userName.setText(reportList.get(0).getUser_Name()+" ("+reportList.get(0).getEmployee_Name()+")");
                        DateHelper datehelper = new DateHelper();
                        Dateoftest.setText((mContext.getResources().getString(R.string.attended_on) +" "+datehelper.getDisplayFormatwithUTC(reportList.get(0).getSection_Exam_End_Time(),"yyyy-MM-dd'T'hh:mm:ss")).substring(0,24));
                        // Dateoftest.setText(mContext.getResources().getString(R.string.attended_on) +" "+reportList.get(0).getFormatted_Section_Exam_Start_Time());
                        loadaadapterData();
                        dismissProgressDialog();
                        calculateReport();
                        calculatemark();
                        if (iscourseReport == true)
                        {
                            obtainedmarks.setText(String.valueOf(obtainedmark));
                            totalmarks.setText(String.valueOf(totalmark));
                        }
                    } else {
                        carddetails.setVisibility(View.GONE);
                        mEmptyView.setVisibility(View.VISIBLE);
                        questionsdetails.setVisibility(View.GONE);
                        dismissProgressDialog();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(), "Error");
                    mEmptyView.setVisibility(View.VISIBLE);
                    questionsdetails.setVisibility(View.GONE);
                    dismissProgressDialog();
                }
            });

        }
    }

    private void calculatemark() {
        if (reportList != null && reportList.size() > 0) {
            for (LPCourseReportSummaryModel model : reportList) {
                obtainedmark = (int) (obtainedmark + model.getMarks_Given());
                //totalmark = totalmark + model.getMarks_Allotted();
                totalmark = (int) model.getTotal_Marks();
            }
        }
    }


    public void loadaadapterData(){
        if (attemptcountvalue == 0)
        {
            attemptcount.setVisibility(View.GONE);
        }
        else
        {
            attemptcount.setText(mContext.getResources().getString(R.string.Attempt)+" : "+String.valueOf(attemptcountvalue));
        }
        for(LPCourseReportSummaryModel model : reportList){
            if(model.getQuestion_Text() != null && model.getQuestion_Text().length() > 0){
                noOfQuestionsAttempted++;
            }
        }
        if(noOfQuestionsAttempted > 0) {
            List<LPCourseReportSummaryModel> newreportlist = new ArrayList<>();
            if(showsummary == 2){
                newreportlist = reportList;
            }else if(showsummary == 1){
                for(LPCourseReportSummaryModel smry : reportList){
                    if(!smry.is_Correct()) {
                        newreportlist.add(smry);
                    }
                }
            }
            if(newreportlist.size() > 0){
                questionsdetails.setVisibility(View.VISIBLE);
            }else{
                questionsdetails.setVisibility(View.GONE);
            }

            lpCourseReportSummaryAdapter = new LPCourseSummaryReportAdapter(mContext, newreportlist,iscourseReport);
            recyclerView.setAdapter(lpCourseReportSummaryAdapter);
            loadadapterclick();
        }
    }

    public void loadadapterclick(){

        lpCourseReportSummaryAdapter.setOnItemClickListener(new LPCourseSummaryReportAdapter.MyClickListener() {
            @Override
            public void onItemClick(String explanation) {
                ShowExplanation(explanation);
            }
        });
    }

    public void ShowExplanation(String messagetext){
        TextView ok,message,cancelpre;
        RelativeLayout heading;

        explanation = new Dialog(mContext);
        explanation.requestWindowFeature(Window.FEATURE_NO_TITLE);
        explanation.setContentView(R.layout.dialog_prerequsite_logout);
        message = (TextView) explanation.findViewById(R.id.msg);
        heading = (RelativeLayout)  explanation.findViewById(R.id.heading);
        ok = (TextView) explanation.findViewById(R.id.Okpre);
        cancelpre = (TextView) explanation.findViewById(R.id.cancelpre);
        explanation.setCancelable(false);

        heading.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        ok.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));

        heading.setVisibility(View.GONE);
        cancelpre.setVisibility(View.GONE);
        message.setText(messagetext);
        explanation.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explanation.dismiss();
            }
        });
    }

    public void calculateReport(){
        int correctanswer = 0;
        double negativemark = 0;
        double Score = 0.0;

        boolean isnegative = false;
        if(reportList != null){
            for(LPCourseReportSummaryModel model : reportList){
                if(model.is_Correct()){
                    correctanswer++;
                }else{
                    if(model.getNegative_Mark()>0){
                        negativemark = negativemark + model.getNegative_Mark();
                    }
                }

            }
            if(reportList.get(0).is_Qualified()){
                resultqualified.setImageResource(R.drawable.ic_check_circle_black_48dp);
                int size = reportList.size();
                double correc = 0.0,dsize = 0.0;
                correc = correctanswer;
                dsize = size;
                Score = (correc/dsize) * 100;
            }else{
                resultqualified.setImageResource(R.drawable.ic_cancel_black_48dp);
                int size = reportList.size();
                double correc = 0.0,dsize = 0.0;
                correc = correctanswer;
                dsize = size;
                Score = (correc/dsize) * 100;
            }

            if(negativemark > Score){
                Score = negativemark - Score;
                isnegative = true;
            }else{
                Score = Score - negativemark;
                isnegative = false;
            }
            if(isnegative) {
                scorevalue.setText(String.valueOf("- "+Math.round(Score)) + " %");
            }else {
                scorevalue.setText(String.valueOf(Math.round(Score)) + " %");
            }
            noquestionvalue.setText(String.valueOf(reportList.size()));
            noQuestionsattendedvalue.setText(String.valueOf(noOfQuestionsAttempted));
            correctanswerquestion.setText(String.valueOf(correctanswer));
            if(reportList.get(0).getNegative_Mark() == 0) {
                negativemarks.setText(String.valueOf(Math.round(reportList.get(0).getNegative_Mark())));
            }else{
                negativemarks.setText(String.valueOf(reportList.get(0).getNegative_Mark()));
            }
        }
    }

    public void onClickListeners(){
        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
        if(!isFinishing()) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        }
    }
}
