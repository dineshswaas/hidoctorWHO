package com.swaas.kangle.CheckList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.swaas.kangle.CheckList.adapter.EvaluateUsersForChklstAdapter;
import com.swaas.kangle.CheckList.model.AnwerUploadModel;
import com.swaas.kangle.CheckList.model.UserforCourseChecklist;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.R;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.MessageDialog;
import com.swaas.kangle.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class EvaluateUserforChecklistActivity extends AppCompatActivity {

    Context mContext;

    int ChecklistId;
    EvaluateUsersForChklstAdapter usersListForCourseChklstAdapter;

    View header,mEmptyView,content_view;
    EmptyRecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    TextView text_title;
    String companycolor,opaquecolor;
    ImageView companylogo;

    ProgressDialog mProgressDialog;

    AnwerUploadModel answermodel;
    ArrayList<UserforCourseChecklist> selectedusers;
    public boolean isBackpressed,isTimeout,isLastQuestion;
    public int QuestionLoadCount;

    MessageDialog messageDialog;
    TextView submit,cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_userfor_checklist);

        mContext = EvaluateUserforChecklistActivity.this;
        messageDialog = new MessageDialog(mContext);

        initialiseViews();
        setUpRecyclerView();
        setupTheme();
        if(getIntent() != null){

            answermodel = (AnwerUploadModel) getIntent().getSerializableExtra("value");
            QuestionLoadCount = (int) getIntent().getSerializableExtra("QuestionLoadCount");
            isLastQuestion = (boolean) getIntent().getSerializableExtra("isLastQuestion");
            isTimeout = (boolean) getIntent().getSerializableExtra("isTimeout");
            isBackpressed = (boolean) getIntent().getSerializableExtra("isBackpressed");
            selectedusers = (ArrayList<UserforCourseChecklist>) getIntent().getSerializableExtra("selectedusers");
            ChecklistId = (int) getIntent().getSerializableExtra("ChecklistId");
        }
        onClicklistener();
        loadUsers();
    }


    public void initialiseViews(){
        header = findViewById(R.id.header);
        text_title = (TextView) findViewById(R.id.text_title);
        content_view = findViewById(R.id.content_view);
        companylogo = (ImageView) findViewById(R.id.companylogo);
        recyclerView = (EmptyRecyclerView) findViewById(R.id.recyclerView);
        submit = (TextView) findViewById(R.id.btn_submit_mutiple);
        cancel = (TextView) findViewById(R.id.btn_cancel);
    }

    public void setUpRecyclerView(){
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        if(getResources().getBoolean(R.bool.portrait_only)){
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setEmptyView(mEmptyView);
        }else{
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                GridLayoutManager grid = new GridLayoutManager(this,3);
                recyclerView.setLayoutManager(grid);
                //checklistrecyclerView.setLayoutManager(onetimeLLM);
            }else{
                GridLayoutManager grid = new GridLayoutManager(this,2);
                recyclerView.setLayoutManager(grid);
                //checklistrecyclerView.setLayoutManager(onetimeLLM);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUpRecyclerView();
    }

    public void setupTheme(){


            companycolor = "#3F51B5";
            opaquecolor = "#cdcdcd";
            header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            content_view.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));

    }

    public void onClicklistener(){
        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void loadUsers(){
        usersListForCourseChklstAdapter = new EvaluateUsersForChklstAdapter(mContext,selectedusers);
        if(selectedusers.size()>0){
            recyclerView.setAdapter(usersListForCourseChklstAdapter);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void onSubmit(){
        messageDialog.showChecklistConfirmationPopup(mContext,getResources().getString(R.string.confirmsubmitchecklist),new View.OnClickListener() {
            @Override
            public void onClick(View Approve) {
                messageDialog.dialogDismiss();
                Checknetworkandupload(UploadAnswerProcess());
                //UploadAnswerProcess();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View close) {
                messageDialog.dialogDismiss();
                //finish();
            }
        }, false);
    }

    private AnwerUploadModel UploadAnswerProcess() {

        answermodel.setUserlistsobj(getDetails(selectedusers));
        Log.d("test",new Gson().toJson(answermodel));
        //return new Gson().toJson(answerupload);
        return answermodel;
    }

    private List<UserforCourseChecklist> getDetails(ArrayList<UserforCourseChecklist> questionandanswerlist) {

        List<UserforCourseChecklist> courseuserassetdetails = new ArrayList<>();

        for (UserforCourseChecklist questionanswermodel : questionandanswerlist) {

            UserforCourseChecklist courseuserasset = new UserforCourseChecklist();
            courseuserasset.User_Id = questionanswermodel.getUser_Id();
            courseuserasset.Course_Id = questionanswermodel.getCourse_Id();
            courseuserasset.Section_Id = questionanswermodel.getSection_Id();
            courseuserasset.Company_Id = PreferenceUtils.getCompnayId(this);
            courseuserasset.CheckList_id = ChecklistId;
            courseuserasset.CourseCheckListDraft = "2";
            courseuserasset.Evaluation_Status = questionanswermodel.getEvaluation_Status();
            if(questionanswermodel.getEvaluation_Status().equals("1")) {
                courseuserasset.Checklist_Result_Status = "Pass";
            }else{
                courseuserasset.Checklist_Result_Status = "Fail";
            }
            if(questionanswermodel.isCourseRestart()) {
                courseuserasset.setChecklist_Restart(true);
            }else{
                courseuserasset.setChecklist_Restart(false);
            }

            courseuserassetdetails.add(courseuserasset);
        }

        return courseuserassetdetails;

    }

    public void Checknetworkandupload(final AnwerUploadModel AnswerModelString){

        if(NetworkUtils.checkIfNetworkAvailable(EvaluateUserforChecklistActivity.this)){
            if (!isBackpressed){
                showProgressDialog(getResources().getString(R.string.please_wait_progress));
            }
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            CheckListService chkService = retrofitAPI.create(CheckListService.class);
            int CompanyId  = PreferenceUtils.getCompnayId(this);
            String  SubdomainName = PreferenceUtils.getSubdomainName(this);
            int UserId = PreferenceUtils.getUserId(this);
            Gson gsonget = new Gson();
            AnwerUploadModel answermodel = AnswerModelString;
            String answermodelstring = new Gson().toJson(AnswerModelString);
            AnwerUploadModel answermodeltest = gsonget.fromJson(answermodelstring,AnwerUploadModel.class);
            Call call = chkService.InsertChecklistResponse(SubdomainName,CompanyId,UserId,QuestionLoadCount,isLastQuestion,isTimeout,answermodel);
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    String courseAssetListModel= response.body();
                    if (courseAssetListModel != null) {

                        if (!isBackpressed){
                            dismissProgressDialog();
                            if (courseAssetListModel.contains("COMPLETED")){
                                ShowAlert(getResources().getString(R.string.ChecklistSubmittedSuccess),"",false,true,0);
                            }else {
                                ShowAlert(getResources().getString(R.string.errorsubmitingchecklist),getResources().getString(R.string.warning),false,false,2);
                                Log.d("error","error");
                            }

                        }
                        //COMPLETED~1~Your course has been partially submitted.~549
                    }else{
                        ShowAlert(getResources().getString(R.string.errorsubmitingchecklist),getResources().getString(R.string.warning),false,false,2);
                    }
                }
                @Override
                public void onFailure(Throwable t) {
                    //ShowAlert(getResources().getString(R.string.errorsubmitingchecklist),getResources().getString(R.string.warning),false,false,2);
                    Log.d(t.toString(),"Error");
                }
            });
        }else{
            //ShowAlert(getResources().getString(R.string.error_message),getResources().getString(R.string.warning),true,false,1);
        }
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

    private void ShowAlert(String Message, String Title, final boolean offline,final boolean successfull,final int typeoferror) {

        final AlertDialog alertDialog = new AlertDialog.Builder(
                EvaluateUserforChecklistActivity.this).create();

        alertDialog.setCancelable(false);
        // Setting Dialog Title
        alertDialog.setTitle(Title);
        // Setting Dialog Message
        alertDialog.setMessage(Message);

        // Setting OK Button
        alertDialog.setButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                if(successfull){
                    finish();
                    // StartReportActiity();
                }else{
                    if(typeoferror == 2) {
                        alertDialog.dismiss();
                        finish();
                    }else{
                        alertDialog.dismiss();
                    }
                }
            }
        });
        // Showing Alert Message
        alertDialog.show();

    }
}
