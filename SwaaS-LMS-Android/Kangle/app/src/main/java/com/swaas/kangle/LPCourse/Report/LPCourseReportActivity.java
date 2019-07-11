package com.swaas.kangle.LPCourse.Report;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.LPCourse.LPCourseService;
import com.swaas.kangle.LPCourse.model.LPCourseReportModel;
import com.swaas.kangle.R;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.CommonUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LPCourseReportActivity extends AppCompatActivity {

    LPCourseReportAdapter lpCourseReportAdapter;
    EmptyRecyclerView recyclerView;
    ImageView companylogo;
    View mEmptyView;
    TextView courseName,SectionName,expiry_date;
    Context mContext;
    ProgressDialog mProgressDialog;
    List<LPCourseReportModel> reportList;
    int SectionId,CourseId,PublishId;
    String SectionDate;
    boolean isfromtest;

    CoordinatorLayout activity_list;
    ImageView thumbnail;
    View header;
    String CourseThumbnail;

    int Course_Status_INT;

    CardView cardViewLayout;
    RelativeLayout sectionsreportslist,thumbnaillayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpcourse_report);

        mContext = LPCourseReportActivity.this;
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        reportList = new ArrayList<>();
        initializeView();
        if(getIntent()!= null){
            CourseId = getIntent().getIntExtra(Constants.Course_Id,0);
            PublishId =  getIntent().getIntExtra(Constants.Publish_Id,0);
            SectionId = getIntent().getIntExtra(Constants.Section_Id,0);
            SectionDate = getIntent().getStringExtra("SectionDate");
            isfromtest = getIntent().getBooleanExtra("isfromtest",false);
            Course_Status_INT = getIntent().getIntExtra("Course_Status_INT",0);
            CourseThumbnail = getIntent().getStringExtra(Constants.Course_Thumbnail);
        }
        setUpRecyclerView();
        setthemeforView();

        getReportforSection();
        onClickListeners();
    }

    public void initializeView(){
        companylogo = (ImageView) findViewById(R.id.companylogo);
        mEmptyView = findViewById(R.id.empty_view);
        recyclerView = (EmptyRecyclerView)findViewById(R.id.reportrecyclerview);
        courseName = (TextView) findViewById(R.id.courseName);
        SectionName = (TextView) findViewById(R.id.SectionName);

        header = findViewById(R.id.header);
        activity_list = (CoordinatorLayout) findViewById(R.id.parent_layout);
        expiry_date = (TextView) findViewById(R.id.expiry_date);
        thumbnail = (ImageView)findViewById(R.id.thumbnail);

        cardViewLayout = (CardView) findViewById(R.id.cardViewLayout);
        sectionsreportslist = (RelativeLayout) findViewById(R.id.sectionsreportslist);
        thumbnaillayout=(RelativeLayout)findViewById(R.id.layoutthumbnail);
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void setthemeforView(){
        if(PreferenceUtils.getSubdomainName(mContext).contains("tacobell")){
            Ion.with(thumbnail).fitXY().placeholder(R.drawable.tacobell_default).fitXY().error(R.drawable.tacobell_default).fitXY().load(
                    (!TextUtils.isEmpty(CourseThumbnail)) ?
                            CourseThumbnail : CourseThumbnail);
        }else {
            Ion.with(thumbnail).fitXY().placeholder(R.drawable.courses).fitXY().error(R.drawable.courses).fitXY().load(
                    (!TextUtils.isEmpty(CourseThumbnail)) ?
                            CourseThumbnail : CourseThumbnail);
        }

            thumbnail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.ICON_COLOR)));
        thumbnail.setBackgroundColor(Color.parseColor(Constants.ICON_COLOR));
        thumbnaillayout.setBackgroundColor(Color.parseColor(Constants.ICON_COLOR));

        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        activity_list.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        cardViewLayout.setCardBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        sectionsreportslist.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.CARDBACKGROUND_COLOR)));

        expiry_date.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        companylogo.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        courseName.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        SectionName.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
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
            int UserId = PreferenceUtils.getUserId(mContext);
            Call call = userService.getLPSectionAttemptDetails(SubdomainName,CompanyId,CourseId,UserId,PublishId,SectionId,offsetFromUtc);
            call.enqueue(new Callback<ArrayList<LPCourseReportModel>>() {

                @Override
                public void onResponse(Response<ArrayList<LPCourseReportModel>> response, Retrofit retrofit) {
                    List<LPCourseReportModel> courseListModel = response.body();
                    if (courseListModel != null && courseListModel.size() > 0) {
                        reportList = courseListModel;
                        courseName.setText(reportList.get(0).getCourse_Name());
                        SectionName.setText(reportList.get(0).getSection_Name());
                        Collections.reverse(reportList);
                        loadaadapterData();
                        dismissProgressDialog();
                        //loadadapterclick();
                    } else {
                        mEmptyView.setVisibility(View.VISIBLE);
                        dismissProgressDialog();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(), "Error");
                    mEmptyView.setVisibility(View.VISIBLE);
                    dismissProgressDialog();
                }
            });

        }
    }

    public void loadaadapterData(){
        if(!isfromtest){
            expiry_date.setVisibility(View.VISIBLE);
            /*if(reportList.get(0).is_Qualified()){
                expiry_date.setText(getResources().getString(R.string.completed_course));
                expiry_date.setTextColor(Color.parseColor(Constants.COMPLETED_COLOR));
            }else{*/
                if(Course_Status_INT == Constants.COMPLETED){
                    expiry_date.setText(getResources().getString(R.string.completed_course));
                    expiry_date.setTextColor(Color.parseColor(Constants.COMPLETED_COLOR));
                }else if(Course_Status_INT == Constants.COURSE_EXPIRED){
                    expiry_date.setText(getResources().getString(R.string.expired_shortened));
                    expiry_date.setTextColor(Color.parseColor(Constants.EXPIRED_COLOR));
                }else if(Course_Status_INT == Constants.MAX_ATTEMPTS_REACHED){
                    expiry_date.setText(getResources().getString(R.string.max_attempts_reached_shortened));
                    expiry_date.setTextColor(Color.parseColor(Constants.EXPIRED_COLOR));
                }else if(Course_Status_INT == Constants.YET_TO_START){
                    expiry_date.setText(getResources().getString(R.string.yet_to_start));
                    expiry_date.setTextColor(Color.parseColor(Constants.YET_TO_START_COLOR));
                } else if(Course_Status_INT == Constants.INPROGRESS){
                    expiry_date.setText(getResources().getString(R.string.in_progress));
                    expiry_date.setTextColor(Color.parseColor(Constants.INPROGRESS_COLOR));
                } else if(Course_Status_INT == Constants.PARTIALLY_COMPLETED){
                    expiry_date.setText(getResources().getString(R.string.partialy_shortened));
                    expiry_date.setTextColor(Color.parseColor(Constants.PARTIALLY_COMPLETED_COLOR));
                }
            //}
        }else{
            expiry_date.setVisibility(View.GONE);
        }
        if(reportList != null && reportList.size() > 0) {
            lpCourseReportAdapter = new LPCourseReportAdapter(mContext, reportList,SectionDate,isfromtest);
            recyclerView.setAdapter(lpCourseReportAdapter);
            loadadapterclick();
        }
    }

    public void loadadapterclick(){

        lpCourseReportAdapter.setOnItemClickListener(new LPCourseReportAdapter.MyClickListener() {
            @Override
            public void onItemClick(LPCourseReportModel courseId) {
                LPCourseReportModel courseModel = courseId;
                //CourseModel courseModel = courseModelList);
                Intent intent = new Intent(mContext,LPCourseReportSummaryActivity.class);
                intent.putExtra(Constants.Exam_Id,courseModel.getCourse_Section_User_Exam_Id());
                intent.putExtra("showfullsummary",reportList.get(0).getShowFullSummary());
                intent.putExtra("showattemptcount",courseModel.getAttempt_Number());
                startActivity(intent);
            }
        });
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
        if(!isFinishing()){
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }
}
