package com.swaas.kangle.CheckList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.swaas.kangle.CheckList.adapter.UserCCHistoryListsAdapter;
import com.swaas.kangle.CheckList.model.UserCCHistoryDetailModel;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.R;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.CommonUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.MessageDialog;
import com.swaas.kangle.utils.NetworkUtils;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class UserCCHistoryListActivity extends AppCompatActivity {

    Context mContext;

    int ChecklistId,CourseId,UserId,SectionId;
    public String checklistName;
    UserCCHistoryListsAdapter userCCHistoryListsAdapter;

    View header,mEmptyView,content_view;
    EmptyRecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    TextView text_title;
    String companycolor,opaquecolor;
    ImageView companylogo;

    View empty_view;

    ProgressDialog mProgressDialog;

    MessageDialog messageDialog;
    TextView submit,cancel;

    ArrayList<UserCCHistoryDetailModel> mDetailModelList;
    TextView emptytext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cchistory_list);

        mContext = UserCCHistoryListActivity.this;

        messageDialog = new MessageDialog(mContext);

        initialiseViews();
        setUpRecyclerView();
        setthemeforView();

        if (getIntent() != null) {
            ChecklistId = (int) getIntent().getSerializableExtra("ChecklistId");
            CourseId = (int) getIntent().getSerializableExtra("CourseId");
            SectionId = (int) getIntent().getSerializableExtra("SectionId");
            UserId = (int) getIntent().getSerializableExtra("UserId");
            checklistName = (String) getIntent().getSerializableExtra("checklistName");
        }
        mDetailModelList = new ArrayList<>();
        getList();
        onClicklistener();
    }

    public void initialiseViews(){
        header = findViewById(R.id.header);
        text_title = (TextView) findViewById(R.id.text_title);
        content_view = findViewById(R.id.content_view);
        companylogo = (ImageView) findViewById(R.id.companylogo);
        recyclerView = (EmptyRecyclerView) findViewById(R.id.recyclerView);
        empty_view = findViewById(R.id.empty_view);
        emptytext = (TextView) findViewById(R.id.emptytext);
    }

    public void setUpRecyclerView(){
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setEmptyView(mEmptyView);
    }

    public void setthemeforView(){

        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        content_view.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        companylogo.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        text_title.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        emptytext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));

    }

    public void onClicklistener(){
        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void getList(){
        if(NetworkUtils.isNetworkAvailable()){

            Retrofit retrofit = RetrofitAPIBuilder.getInstance();

            CheckListService checklistService = retrofit.create(CheckListService.class);
            int CompanyId = PreferenceUtils.getCompnayId(mContext);
            Log.d("CompanyId", String.valueOf(CompanyId));
            String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            //int UserId = PreferenceUtils.getUserId(mContext);
            String offsetFromUtc = CommonUtils.getUtcOffsetincluded10k();
            Log.d("getUTC", offsetFromUtc);

            Call call = checklistService.getCourseChecklistAttemptsAPI(SubdomainName,ChecklistId,offsetFromUtc,CompanyId,UserId,CourseId,SectionId);
            call.enqueue(new Callback<ArrayList<UserCCHistoryDetailModel>>() {

                @Override
                public void onResponse(Response<ArrayList<UserCCHistoryDetailModel>> response, Retrofit retrofit) {
                    ArrayList<UserCCHistoryDetailModel> DetailModelList= response.body();
                    if (DetailModelList != null && DetailModelList.size() > 0) {
                        mDetailModelList =  DetailModelList;
                        loaddata();
                    }else{
                        recyclerView.setVisibility(View.GONE);
                        empty_view.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(),"Error");
                }
            });

        }else{
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
        }
    }

    public void loaddata(){
        userCCHistoryListsAdapter = new UserCCHistoryListsAdapter(mContext,mDetailModelList,checklistName);
        if(mDetailModelList.size()>0){
            recyclerView.setAdapter(userCCHistoryListsAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            empty_view.setVisibility(View.GONE);
        }else{
            recyclerView.setVisibility(View.GONE);
            empty_view.setVisibility(View.VISIBLE);
        }
        adapterClick();
    }

    public void adapterClick(){
        userCCHistoryListsAdapter.setOnItemClickListener(new UserCCHistoryListsAdapter.MyClickListener() {
            @Override
            public void onItemClick(UserCCHistoryDetailModel model) {
                Intent i = new Intent(mContext, SectionsQuestionDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ChecklistId",ChecklistId);
                bundle.putSerializable("CourseId",model.getCourse_Id());
                bundle.putSerializable("SectionId",SectionId);
                bundle.putSerializable("UserId",model.getUser_Id());
                bundle.putSerializable("Attemptcount",model.getAttempt_Number());
                bundle.putSerializable("checklistName",checklistName);
                bundle.putSerializable("isfromHistory",true);
                bundle.putSerializable("isfromcoursechecklist",true);
                i.putExtras(bundle);

                startActivity(i);
            }
        });
    }

    public void showProgressDialog(String message) {
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
