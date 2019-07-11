package com.swaas.kangle.LPCourse;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.swaas.kangle.CheckList.SectionsQuestionDetailActivity;
import com.swaas.kangle.CheckList.model.UserforCourseChecklist;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.R;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.NetworkUtils;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SectionChecklistReportActivity extends AppCompatActivity {

    Context mContext;
    SectionChecklistReportAdapter sectionChecklistReportAdapter;
    LinearLayoutManager layoutManager;
    EmptyRecyclerView recyclerView;
    NestedScrollView nestedView;
    ProgressDialog mProgressDialog;
    Retrofit retrofitAPI;
    LPCourseService lpService;

    int CompanyId,UserId,ChecklistId;
    String SubdomainName,checklistName;
    boolean isfromcoursechecklist;
    int CourseId,Attemptcount,SectionId;
    boolean isfromHistory,isfromSection;
    ArrayList<UserforCourseChecklist> sectionreportModelList;

    LinearLayout header;
    ImageView companylogo;
    TextView title;
    View empty_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_checklist_report);

        mContext = SectionChecklistReportActivity.this;

        initialiseViews();
        setUpRecyclerView();
        if (getIntent() != null) {
            isfromcoursechecklist = getIntent().getBooleanExtra("isfromcoursechecklist",false);
            if(isfromcoursechecklist){
                ChecklistId = (int) getIntent().getSerializableExtra("ChecklistId");
                CourseId = (int) getIntent().getSerializableExtra("CourseId");
                SectionId = (int) getIntent().getSerializableExtra("SectionId");
                UserId = (int) getIntent().getSerializableExtra("UserId");
                Attemptcount = (int) getIntent().getSerializableExtra("Attemptcount");
                checklistName = (String) getIntent().getSerializableExtra("checklistName");
                isfromHistory = getIntent().getBooleanExtra("isfromHistory",false);
                isfromSection = getIntent().getBooleanExtra("isfromSection",false);
            }
        }
      /*  if(PreferenceUtils.getSubdomainName(mContext).contains("tacobell")){
            nestedView.setBackgroundColor(getResources().getColor(R.color.tacobellbackground));
        }else {
            nestedView.setBackgroundColor(getResources().getColor(R.color.otherCompanies));
        }*/
        nestedView.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));

        title.setText(mContext.getResources().getString(R.string.evaluation_report));
        onClickListeners();
        if(isfromSection){
            getSectionchecklistreportlist();
        }else{
            getCoursechecklistreportlist();
        }

    }

    public void initialiseViews(){
        header = (LinearLayout) findViewById(R.id.header);
        companylogo = (ImageView) findViewById(R.id.companylogo);
        recyclerView = (EmptyRecyclerView) findViewById(R.id.recyclerView);
        nestedView = (NestedScrollView) findViewById(R.id.nestedView);
        title = (TextView) findViewById(R.id.title);
        empty_view = findViewById(R.id.empty_view);
    }

    public void setUpRecyclerView(){
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void onClickListeners(){
        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void getSectionchecklistreportlist(){
        if (!isFinishing()){
            dismissProgressDialog();
        }
        if(NetworkUtils.checkIfNetworkAvailable(mContext)){
            if(!isFinishing()){
                showProgressDialog();
            }
            retrofitAPI = RetrofitAPIBuilder.getInstance();
            lpService = retrofitAPI.create(LPCourseService.class);
            CompanyId  = PreferenceUtils.getCompnayId(mContext);
            SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            UserId = PreferenceUtils.getUserId(mContext);
            Call call = lpService.getSectionChecklistReportList(SubdomainName,CompanyId,UserId,CourseId,SectionId);


            call.enqueue(new Callback<ArrayList<UserforCourseChecklist>>() {

                @Override
                public void onResponse(Response<ArrayList<UserforCourseChecklist>> response, Retrofit retrofit) {
                    ArrayList<UserforCourseChecklist> courseListModel= response.body();
                    if (courseListModel != null && courseListModel.size() > 0) {
                        sectionreportModelList = courseListModel;
                        recyclerView.setVisibility(View.VISIBLE);
                        empty_view.setVisibility(View.GONE);
                        sectionChecklistReportAdapter = new SectionChecklistReportAdapter(mContext,sectionreportModelList);
                        recyclerView.setAdapter(sectionChecklistReportAdapter);
                        if(!isFinishing())
                            dismissProgressDialog();
                        loadadapterclick();
                        Log.d("sectionModelList",String.valueOf(sectionreportModelList.size()));

                    } else {
                        //courseListModel is null
                        recyclerView.setVisibility(View.GONE);
                        empty_view.setVisibility(View.VISIBLE);
                        dismissProgressDialog();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    dismissProgressDialog();
                    Log.d(t.toString(),"Error");
                }
            });

        }else{
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
        }
    }

    public void getCoursechecklistreportlist(){
        if (!isFinishing()){
            dismissProgressDialog();
        }
        if(NetworkUtils.checkIfNetworkAvailable(mContext)){
            if(!isFinishing()){
                showProgressDialog();
            }
            retrofitAPI = RetrofitAPIBuilder.getInstance();
            lpService = retrofitAPI.create(LPCourseService.class);
            CompanyId  = PreferenceUtils.getCompnayId(mContext);
            SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            UserId = PreferenceUtils.getUserId(mContext);
            Call call = lpService.getCourseChecklistReportList(SubdomainName,CompanyId,UserId,CourseId,SectionId);


            call.enqueue(new Callback<ArrayList<UserforCourseChecklist>>() {

                @Override
                public void onResponse(Response<ArrayList<UserforCourseChecklist>> response, Retrofit retrofit) {
                    ArrayList<UserforCourseChecklist> courseListModel= response.body();
                    if (courseListModel != null && courseListModel.size() > 0) {
                        sectionreportModelList = courseListModel;
                        recyclerView.setVisibility(View.VISIBLE);
                        empty_view.setVisibility(View.GONE);
                        sectionChecklistReportAdapter = new SectionChecklistReportAdapter(mContext,sectionreportModelList);
                        recyclerView.setAdapter(sectionChecklistReportAdapter);
                        if(!isFinishing())
                            dismissProgressDialog();
                        loadadapterclick();
                        Log.d("sectionModelList",String.valueOf(sectionreportModelList.size()));

                    } else {
                        //courseListModel is null
                        recyclerView.setVisibility(View.GONE);
                        empty_view.setVisibility(View.VISIBLE);
                        dismissProgressDialog();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    dismissProgressDialog();
                    Log.d(t.toString(),"Error");
                }
            });

        }else{
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
        }
    }

    public void loadadapterclick(){
        sectionChecklistReportAdapter.setOnItemClickListener(new SectionChecklistReportAdapter.MyClickListener() {
            @Override
            public void onItemClick(UserforCourseChecklist checklistId) {
                gotoSectionsQuestionDetailActivity(checklistId);
            }
        });
    }

    public void gotoSectionsQuestionDetailActivity(UserforCourseChecklist id){
        Intent intent = new Intent(mContext, SectionsQuestionDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ChecklistId",id.getCheckList_id());
        bundle.putSerializable("CourseId",CourseId);
        bundle.putSerializable("SectionId",SectionId);
        bundle.putSerializable("UserId",UserId);
        bundle.putSerializable("Attemptcount",Attemptcount);
        bundle.putSerializable("checklistName",id.getChecklist_Name());
        bundle.putSerializable("isfromHistory",false);
        bundle.putSerializable("isfromcoursechecklist",true);
        intent.putExtras(bundle);

        startActivity(intent);
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
