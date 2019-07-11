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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.swaas.kangle.CheckList.CheckListQuestionbuilder.QuestionActivity;
import com.swaas.kangle.CheckList.adapter.DraftListForCourseChklstAdapter;
import com.swaas.kangle.CheckList.adapter.UsersListForCourseChklstAdapter;
import com.swaas.kangle.CheckList.model.ChkLstSectionsQuestionAnswerList;
import com.swaas.kangle.CheckList.model.QuestionAndAnswerModel;
import com.swaas.kangle.CheckList.model.QuestionAnswerListModel;
import com.swaas.kangle.CheckList.model.QuestionBaseModel;
import com.swaas.kangle.CheckList.model.QuestionQuestionListModel;
import com.swaas.kangle.CheckList.model.UserforCourseChecklist;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.R;
import com.swaas.kangle.TaskModule.TaskListModel;
import com.swaas.kangle.TaskModule.TaskServices;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.CommonUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CourseChecklistUsersActivity extends AppCompatActivity {

    Context mContext;

    int ChecklistId,CUAId,ChecklistGroupId, CheckListTypeId;
    String checklistName,PublishDate,chklstdesc,DraftId;
    ArrayList<UserforCourseChecklist> mAlluserslistforchklst,mAlldraftlist;
    UsersListForCourseChklstAdapter usersListForCourseChklstAdapter;
    DraftListForCourseChklstAdapter draftListForCourseChklstAdapter;
    boolean isFilterenabled;

    View header,mEmptyView,content_view,disabledapply;
    EmptyRecyclerView recyclerView,draftrecyclerView;
    LinearLayoutManager linearLayoutManager,linearLayoutManager1;
    TextView text_title;
    String companycolor,opaquecolor;
    ImageView companylogo,filter,filtered,icon_cat;

    RelativeLayout checkBoxGroupView,filterheader,catview;
    CheckBox all,completed,try_again,yettoevaluate;

    TextView mApplyBtn;
    TextView disabledclear,clearfilter,selecteduser,userwisevalue,draftheading;

    ProgressDialog mProgressDialog;

    View usersview;
    RelativeLayout draftlist;
    boolean gotoprevious;

    ArrayList<QuestionBaseModel> mQuestionBaseModels;
    ArrayList<TaskListModel> mTaskLists;
    View user_empty_list;
    TextView empty_txt;
    LinearLayout catmenu;
    RelativeLayout filtersection;
    TextView clearfilters,applyfilters,evaluate;
    EmptyRecyclerView cattag_recyclerView;
    View catselection,tagselection,filterlay;
    TextView cat_filtered_count,tags_filtered_count;
    TextView clear_assetfilter,filtered_text;
    View assetfilterheading;
    TextView emptytagsview;
    ImageView close_filter;

    boolean tagfiltered,catFiltered;
    String TAGS = "",CATS = "";
    ImageView mCourseFilter;
    TextView mFilteredCountStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_checklist_users);

        mContext = CourseChecklistUsersActivity.this;

        initialiseViews();
        setupRecyclerView();
        setthemeforView();
        if(getIntent()!= null){
            ChecklistId = (int) getIntent().getSerializableExtra("ChecklistId");
            checklistName = (String) getIntent().getSerializableExtra("name");
            CUAId = (int) getIntent().getSerializableExtra("CUAId");
            PublishDate = (String) getIntent().getSerializableExtra("PublishDate");
            ChecklistGroupId = (int) getIntent().getSerializableExtra("ChecklistGroupId");
            chklstdesc = (String) getIntent().getSerializableExtra("chklstdesc");;
            CheckListTypeId = (int) getIntent().getSerializableExtra("CheckListTypeId");
        }

        onClickListener();
        DraftId = "0";
        getListOfUsers();
    }


    public void initialiseViews(){
        header = findViewById(R.id.header);
        text_title = (TextView) findViewById(R.id.text_title);
        content_view = findViewById(R.id.content_view);
        companylogo = (ImageView) findViewById(R.id.companylogo);
        recyclerView = (EmptyRecyclerView) findViewById(R.id.recyclerView);
        mEmptyView = findViewById(R.id.empty_view);
        filter = (ImageView) findViewById(R.id.filter);
        filtered = (ImageView) findViewById(R.id.filtered);
        filtersection =findViewById(R.id.filtersection);
        //checkBoxGroupView = (RelativeLayout)findViewById(R.id.check_box);
        all = (CheckBox)findViewById(R.id.all);
        completed = (CheckBox)findViewById(R.id.check_completed);
        try_again = (CheckBox)findViewById(R.id.tryagain);
        yettoevaluate = (CheckBox)findViewById(R.id.check_yet_to_start);
        close_filter = (ImageView) findViewById(R.id.close_filter);
        mApplyBtn = (TextView)findViewById(R.id.applybtn);
        disabledapply = findViewById(R.id.disabledapply);
        disabledclear = (TextView)findViewById(R.id.disabledclear);
        applyfilters = (TextView)findViewById(R.id.applyfilters1);
        evaluate = (TextView)findViewById(R.id.applyfilters);
        clearfilter = (TextView) findViewById(R.id.clearfilters);
        catmenu = findViewById(R.id.cattagmenus);
        icon_cat =(ImageView) findViewById(R.id.icon_cats);
        filterheader = findViewById(R.id.filterheading);
        catview = findViewById(R.id.cat_tag_view);
        selecteduser = (TextView) findViewById(R.id.selecteduser);
        userwisevalue = (TextView) findViewById(R.id.userwisevalue);
        usersview = findViewById(R.id.usersview);

        draftlist = findViewById(R.id.draftlist);
        draftrecyclerView = (EmptyRecyclerView) findViewById(R.id.draftrecyclerView);
        draftheading = (TextView) findViewById(R.id.draftheading);
        user_empty_list = findViewById(R.id.user_empty_list);
        empty_txt = (TextView) findViewById(R.id.empty_txt);
    }

    public void setupRecyclerView(){
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        linearLayoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        draftrecyclerView.setLayoutManager(linearLayoutManager1);
        recyclerView.setEmptyView(mEmptyView);
    }

    public void setthemeforView(){

        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        content_view.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
//        disabledapply.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            evaluate.setBackgroundColor(Color.parseColor(Constants.HEADERBAR_COLOR));
     //   disabledclear.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
    //    filter.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
     //   filtered.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        text_title.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        companylogo.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        selecteduser.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        userwisevalue.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        draftlist.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        user_empty_list.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        empty_txt.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        applyfilters.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        applyfilters.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        clearfilter.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
        catmenu.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        icon_cat.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        filterheader.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        catview.setBackgroundColor(Color.parseColor(Constants.TEXT_COLOR));
    }

    public void onClickListener(){

        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gotoprevious){
                    DraftId = "0";
                    gotoprevious = false;
                    getListOfUsers();
                }else{
                    onBackPressed();
                }
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setChecked(true);
                completed.setChecked(false);
                try_again.setChecked(false);
                yettoevaluate.setChecked(false);
                filtersection.setVisibility(View.VISIBLE);
               /* catlist.clear();
                tagslist.clear();
                digitalAssetsMasterCategoryLists = checklistCatTagFilterRepository.getAllCategory(CATS,checkEnabledTab,frequencyofList);
                digitalAssetsMasterTagsLists = checklistCatTagFilterRepository.getAllTags(TAGS,checkEnabledTab,frequencyofList);*/
               // cattag_recyclerView.setVisibility(View.VISIBLE);
                //checkBoxGroupView.setVisibility(View.GONE);
                //loadCategorySlider();
            }
        });
        close_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtersection.setVisibility(View.GONE);
            }
        });
/*
        mApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter.setVisibility(View.GONE);
                filtered.setVisibility(View.VISIBLE);
                isFilterenabled = true;
                loadFilteredList();
                if (checkBoxGroupView.getVisibility() == View.VISIBLE){
                    checkBoxGroupView.setVisibility(View.GONE);
                }
            }
        });
*/

/*
        disabledapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
*/

        all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EnableClearorapply();
            }
        });
        completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                all.setChecked(false);
                EnableClearorapply();
            }
        });
        try_again.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                all.setChecked(false);
                EnableClearorapply();
            }
        });
        yettoevaluate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                all.setChecked(false);
                EnableClearorapply();
            }
        });

/*
        disabledclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxGroupView.setVisibility(View.GONE);
                all.setChecked(true);
                completed.setChecked(false);
                try_again.setChecked(false);
                yettoevaluate.setChecked(false);
                isFilterenabled = false;
                filter.setVisibility(View.VISIBLE);
                filtered.setVisibility(View.GONE);
                loadFilteredList();
            }
        });
*/

        applyfilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // getListOfQuestions(ChecklistId,checklistName,CUAId,PublishDate,ChecklistGroupId,chklstdesc, CheckListTypeId);
                filter.setVisibility(View.VISIBLE);
                filtersection.setVisibility(View.GONE);
                isFilterenabled = true;
                loadFilteredList();



            }
        });
        evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListOfQuestions(ChecklistId,checklistName,CUAId,PublishDate,ChecklistGroupId,chklstdesc, CheckListTypeId);
            }
        });

/*
        filtered.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(checkBoxGroupView.getVisibility() == View.VISIBLE){
                    checkBoxGroupView.setVisibility(View.GONE);
                } else{
                    checkBoxGroupView.setVisibility(View.VISIBLE);
                }
            }
        });
*/

        clearfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completed.setChecked(false);
                try_again.setChecked(false);
                yettoevaluate.setChecked(false);
                all.setChecked(true);
                //checkBoxGroupView.setVisibility(View.GONE);
                isFilterenabled = false;
                filter.setVisibility(View.VISIBLE);
                filtersection.setVisibility(View.GONE);
                loadFilteredList();
            }
        });

    }

    public void getListOfUsers(){

        if(NetworkUtils.isNetworkAvailable()){
            showProgressDialog(getResources().getString(R.string.please_wait_progress));
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            CheckListService checklistService = retrofit.create(CheckListService.class);
            int CompanyId = PreferenceUtils.getCompnayId(mContext);
            Log.d("CompanyId", String.valueOf(CompanyId));
            String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            int UserId = PreferenceUtils.getUserId(mContext);
            String offsetFromUtc = CommonUtils.getUtcOffset();

            //Call call = checklistService.getSectionsandQuestion();
            Call call = checklistService.getUserforCourseChecklist(SubdomainName,ChecklistId,offsetFromUtc,CompanyId,UserId,0);
            call.enqueue(new Callback<ArrayList<UserforCourseChecklist>>() {

                @Override
                public void onResponse(Response<ArrayList<UserforCourseChecklist>> response, Retrofit retrofit) {
                    ArrayList<UserforCourseChecklist> usersmodel = response.body();
                    if(usersmodel != null && usersmodel.size() > 0){
                        mAlluserslistforchklst =  usersmodel;
                        getListOfDrafts();
                        loadCourseChecklistusers();
                    }else{
                        filter.setVisibility(View.GONE);
                        usersview.setVisibility(View.GONE);
                        mEmptyView.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext,mContext.getResources().getString(R.string.no_user_msg),Toast.LENGTH_SHORT).show();
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

    public void getListOfDrafts(){
        Retrofit retrofit = RetrofitAPIBuilder.getInstance();
        CheckListService checklistService = retrofit.create(CheckListService.class);
        int CompanyId = PreferenceUtils.getCompnayId(mContext);
        Log.d("CompanyId", String.valueOf(CompanyId));
        String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
        int UserId = PreferenceUtils.getUserId(mContext);
        String offsetFromUtc = CommonUtils.getUtcOffset();

        Call call = checklistService.getUserforDraftCourseChecklist(SubdomainName,ChecklistId,offsetFromUtc,CompanyId,UserId,0);
        call.enqueue(new Callback<ArrayList<UserforCourseChecklist>>() {

            @Override
            public void onResponse(Response<ArrayList<UserforCourseChecklist>> response, Retrofit retrofit) {
                ArrayList<UserforCourseChecklist> usersmodel = response.body();
                if(usersmodel != null && usersmodel.size() > 0){
                    mAlldraftlist =  usersmodel;
                    loadDraftList();
                }else{
                    draftlist.setVisibility(View.GONE);
                    dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dismissProgressDialog();
                Log.d(t.toString(),"Error");
            }
        });

    }

    public void loadCourseChecklistusers(){
        dismissProgressDialog();
        ArrayList<UserforCourseChecklist> sorteduserslistforchklst = new ArrayList<>();
        if(DraftId.equals("0")) {
            for (UserforCourseChecklist usr : mAlluserslistforchklst) {
                if (usr.getEvaluation_Status().equals("0")) {
                    sorteduserslistforchklst.add(usr);
                }
            }
            for (UserforCourseChecklist usr : mAlluserslistforchklst) {
                if (usr.getEvaluation_Status().equals("-1")) {
                    sorteduserslistforchklst.add(usr);
                }
            }
            for (UserforCourseChecklist usr : mAlluserslistforchklst) {
                if (usr.getEvaluation_Status().equals("1")) {
                    sorteduserslistforchklst.add(usr);
                }
            }
            mAlluserslistforchklst = sorteduserslistforchklst;
        } else {
            for(UserforCourseChecklist usr : mAlluserslistforchklst){
                usr.setEvaluation_Status("-2");
                sorteduserslistforchklst.add(usr);
            }
            mAlluserslistforchklst = sorteduserslistforchklst;
        }
        getuserscount();
        usersListForCourseChklstAdapter = new UsersListForCourseChklstAdapter(this,mContext,mAlluserslistforchklst);
        if(mAlluserslistforchklst.size()>0){
            recyclerView.setAdapter(usersListForCourseChklstAdapter);
            usersview.setVisibility(View.VISIBLE);
            if(DraftId.equals("0")) {
                filter.setVisibility(View.VISIBLE);
            }
            mEmptyView.setVisibility(View.GONE);
        }else{
            usersview.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        onAdapterclickListener();
        updateUserSelectionCount();
    }

    public void loadDraftList(){
        draftheading.setText("Draft List");
        draftListForCourseChklstAdapter = new DraftListForCourseChklstAdapter(this,mContext,mAlldraftlist);
        if(mAlldraftlist.size()>0){
            draftrecyclerView.setAdapter(draftListForCourseChklstAdapter);
            draftlist.setVisibility(View.VISIBLE);
        }else{
            draftlist.setVisibility(View.GONE);
        }
        onDraftAdapterclickListener();
    }

    public void getListOfDraftedUsers(String draftId){
        DraftId = draftId;
        gotoprevious = true;
        getDraftedUsersList();
        filter.setVisibility(View.GONE);
        draftlist.setVisibility(View.GONE);
    }

    public void getDraftedUsersList(){
        Retrofit retrofit = RetrofitAPIBuilder.getInstance();
        CheckListService checklistService = retrofit.create(CheckListService.class);
        int CompanyId = PreferenceUtils.getCompnayId(mContext);
        String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
        String GUID = DraftId;

        Call call = checklistService.getDraftCourseCheckListUser(GUID,SubdomainName,CompanyId);
        call.enqueue(new Callback<ArrayList<UserforCourseChecklist>>() {

            @Override
            public void onResponse(Response<ArrayList<UserforCourseChecklist>> response, Retrofit retrofit) {
                ArrayList<UserforCourseChecklist> usersmodel = response.body();
                if(usersmodel != null && usersmodel.size() > 0){
                    mAlluserslistforchklst =  usersmodel;
                    loadCourseChecklistusers();
                }else{
                    filter.setVisibility(View.GONE);
                    usersview.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext,mContext.getResources().getString(R.string.no_user_msg),Toast.LENGTH_SHORT).show();
                    dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dismissProgressDialog();
                Log.d(t.toString(),"Error");
            }
        });
    }

    public void getuserscount(){
        int completed = 0,tryagain = 0,yetto = 0,tot = 0;
        tot = mAlluserslistforchklst.size();
        if(DraftId.equals("0")) {
            for (UserforCourseChecklist usr : mAlluserslistforchklst) {
                if (usr.getEvaluation_Status().equals("1"))
                    completed += 1;
                else if (usr.getEvaluation_Status().equals("0"))
                    tryagain += 1;
                else
                    yetto += 1;
            }
            userwisevalue.setText(getResources().getString(R.string.totl_users) + " : " + tot + "\n " + getResources().getString(R.string.completed_course) + " : " + completed
                    + " | " + getResources().getString(R.string.try_again) + " : " + tryagain + " | "
                    + getResources().getString(R.string.yet_to_evaluate) + " : " + yetto);
        }else {
            userwisevalue.setText("");
        }

    }

    public void loadFilteredList(){
        final ArrayList<UserforCourseChecklist> filteredCourse = new ArrayList<>();

        if(all.isChecked() && !completed.isChecked() && !try_again.isChecked() && !yettoevaluate.isChecked()){
            usersListForCourseChklstAdapter = new UsersListForCourseChklstAdapter(this,mContext,mAlluserslistforchklst);
            recyclerView.setAdapter(usersListForCourseChklstAdapter);
        } else {
            if (completed.isChecked()) {
                for (UserforCourseChecklist courseModel : mAlluserslistforchklst) {
                    if (courseModel.getEvaluation_Status().equals("1")) {
                        filteredCourse.add(courseModel);
                    }
                }
            }
            if (try_again.isChecked()) {
                for (UserforCourseChecklist courseModel : mAlluserslistforchklst) {
                    if (courseModel.getEvaluation_Status().equals("0")) {
                        filteredCourse.add(courseModel);
                    }
                }
            }
            if (yettoevaluate.isChecked()){
                for (UserforCourseChecklist courseModel : mAlluserslistforchklst) {
                    if (courseModel.getEvaluation_Status().equals("-1")) {
                        filteredCourse.add(courseModel);
                    }
                }
            }
            if (all.isChecked()) {
                yettoevaluate.setChecked(false);
                completed.setChecked(false);
                try_again.setChecked(false);
                filteredCourse.clear();
                filteredCourse.addAll(mAlluserslistforchklst);
            }
            usersListForCourseChklstAdapter.setCourseListadapter(filteredCourse);
            usersListForCourseChklstAdapter.notifyDataSetChanged();
            if(filteredCourse.size() == 0){
                /*mEmptyView.setVisibility(View.VISIBLE);
                emptymessage.setText(getResources().getString(R.string.no_result));*/
            }
        }
    }

    public void EnableClearorapply(){
        if(!yettoevaluate.isChecked() && !try_again.isChecked() && !completed.isChecked() && all.isChecked()){
            applyfilters.setVisibility(View.VISIBLE);
            clearfilter.setVisibility(View.VISIBLE);
        } else {
            applyfilters.setVisibility(View.VISIBLE);
            clearfilter.setVisibility(View.VISIBLE);
        }
    }

    public void updateUserSelectionCount(){
        int size = 0;
        for(UserforCourseChecklist usr : mAlluserslistforchklst){
            if(usr.isChoosenuser()){
                size += 1;
            }
        }
        selecteduser.setText(getResources().getString(R.string.selected_user)+" : "+size+" / 10");
        if(size == 0){

            evaluate.setVisibility(View.GONE);
        }else{
            evaluate.setVisibility(View.VISIBLE);
        }
    }

    public void getListOfQuestions(final int ChecklistId, final String chklistname, final int CUAId,
                                   final String PublishDate, final int chklstgroupid, final String chklstdesc, final int checkListTypeId){

        if(NetworkUtils.isNetworkAvailable()){
            /*RetrofitCustomApiBuilder retrofitcustomAPI = new RetrofitCustomApiBuilder("https://api.myjson.com/");
            Retrofit retrofit = retrofitcustomAPI.getInstance();*/
            showProgressDialog(getResources().getString(R.string.please_wait_progress));
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();

            CheckListService checklistService = retrofit.create(CheckListService.class);
            int CompanyId = PreferenceUtils.getCompnayId(mContext);
            Log.d("CompanyId", String.valueOf(CompanyId));
            String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            int UserId = PreferenceUtils.getUserId(mContext);

            //Call call = checklistService.getSectionsandQuestion();
            Call call = checklistService.getChecklistQuestionAnswerDetailsAPI(SubdomainName, CompanyId, UserId, ChecklistId,CUAId,DraftId);
            call.enqueue(new Callback<ArrayList<QuestionBaseModel>>() {

                @Override
                public void onResponse(Response<ArrayList<QuestionBaseModel>> response, Retrofit retrofit) {
                    ArrayList<QuestionBaseModel> questionBaseModels= response.body();
                    mQuestionBaseModels = new ArrayList<>();
                    dismissProgressDialog();
                    if (questionBaseModels != null) {
                        mQuestionBaseModels = questionBaseModels;
                        getDraftTaskList(ChecklistId,chklistname,CUAId,PublishDate,chklstgroupid,chklstdesc,DraftId, checkListTypeId);
                    } else {
                        Toast.makeText(CourseChecklistUsersActivity.this,getResources().getString(R.string.noQuestions),Toast.LENGTH_SHORT).show();
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

    public void getDraftTaskList(final int ChecklistId, final String chklistname, final int CUAId,
                                 final String PublishDate, final int chklstgroupid, final String chklstdesc, String DraftId, final int checkListTypeId){
        int QuestionId = 0;
        int GroupChecklistCheck = 1;
        int Course_section_id = 0;
        int Course_Id = 0;//mAlluserslistforchklst.get(0).getCourse_Id();
        int Selected_Course_Id  = 0;//mAlluserslistforchklst.get(0).getCourse_Id();
        int Selected_User_Id = 0;

        if(NetworkUtils.isNetworkAvailable()){
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();

            TaskServices checklistService = retrofit.create(TaskServices.class);
            int CompanyId = PreferenceUtils.getCompnayId(mContext);
            Log.d("CompanyId", String.valueOf(CompanyId));
            String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            int UserId = PreferenceUtils.getUserId(mContext);

            //Call call = checklistService.getSectionsandQuestion();
            Call call = checklistService.getTaskSaveDraftCourseChecklistApi(SubdomainName, CompanyId, ChecklistId,QuestionId ,UserId, GroupChecklistCheck ,
                    Course_section_id,Course_Id, DraftId ,Selected_Course_Id,Selected_User_Id);
            call.enqueue(new Callback<ArrayList<TaskListModel>>() {

                @Override
                public void onResponse(Response<ArrayList<TaskListModel>> response, Retrofit retrofit) {
                    ArrayList<TaskListModel> tasklists= response.body();
                    mTaskLists = new ArrayList<>();
                    if (tasklists != null) {
                        mTaskLists = tasklists;
                    }
                    gotoQuestionActivity(ChecklistId,chklistname,CUAId,PublishDate,chklstgroupid,chklstdesc, checkListTypeId);
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

    public void gotoQuestionActivity(final int ChecklistId, final String chklistname, final int CUAId,
                                     final String PublishDate, final int chklstgroupid, final String chklstdesc, int checkListTypeId){
        ArrayList<ChkLstSectionsQuestionAnswerList> secquestionAndAnswerModels =  CallToGetQuestionModel(mQuestionBaseModels);
        dismissProgressDialog();
        ArrayList<UserforCourseChecklist> selectedusers = CallTogetSelectedusers();
        dismissProgressDialog();
        if(secquestionAndAnswerModels != null && secquestionAndAnswerModels.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("value", secquestionAndAnswerModels);
            bundle.putSerializable("name",chklistname);
            bundle.putSerializable("ChecklistId",ChecklistId);
            bundle.putSerializable("CUMId",CUAId);
            bundle.putSerializable("PublishDate",PublishDate);
            bundle.putSerializable("ChecklistPublishType",1);
            bundle.putSerializable("AssignmentId",CUAId);
            bundle.putSerializable("ChecklistGroupId",chklstgroupid);
            bundle.putSerializable("CheckListTypeId",checkListTypeId);
            bundle.putSerializable("ChecklistDescription",chklstdesc);
            bundle.putSerializable("CCuserlist",selectedusers);
            bundle.putSerializable("isfromcoursechecklist",true);
            bundle.putSerializable(Constants.TaskList,mTaskLists);
            bundle.putSerializable(Constants.DraftId,DraftId);
            bundle.putSerializable("isGroupChecklist",false);
            Intent intentToQuestionActivity = new Intent(mContext, QuestionActivity.class);
            intentToQuestionActivity.putExtras(bundle);
            startActivity(intentToQuestionActivity);
            finish();
        }else{
            dismissProgressDialog();
            Toast.makeText(CourseChecklistUsersActivity.this,getResources().getString(R.string.noQuestions),Toast.LENGTH_SHORT).show();
        }
    }


    public void onAdapterclickListener(){
        usersListForCourseChklstAdapter.setOnItemClickListener(new UsersListForCourseChklstAdapter.MyClickListener() {
            @Override
            public void onItemClick(UserforCourseChecklist userforCourseChecklist) {

                Intent intent = new Intent(mContext, SectionsQuestionDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ChecklistId",ChecklistId);
                bundle.putSerializable("CourseId",userforCourseChecklist.getCourse_Id());
                bundle.putSerializable("SectionId",userforCourseChecklist.getSection_Id());
                bundle.putSerializable("UserId",userforCourseChecklist.getUser_Id());
                bundle.putSerializable("Attemptcount",0);
                bundle.putSerializable("checklistName",checklistName);
                bundle.putSerializable("isfromHistory",false);
                bundle.putSerializable("isfromcoursechecklist",true);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    public void onDraftAdapterclickListener(){
        draftListForCourseChklstAdapter.setOnItemClickListener(new DraftListForCourseChklstAdapter.MyClickListener() {
            @Override
            public void onItemClick(UserforCourseChecklist courseId) {
                getListOfDraftedUsers(courseId.getDraftId());
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

    public ArrayList<UserforCourseChecklist> CallTogetSelectedusers(){
        ArrayList<UserforCourseChecklist> users = new ArrayList<>();

        for(UserforCourseChecklist usr : mAlluserslistforchklst){
            if(usr.isChoosenuser()){
                users.add(usr);
            }
        }

        return users;
    }

    private ArrayList<ChkLstSectionsQuestionAnswerList> CallToGetQuestionModel(ArrayList<QuestionBaseModel> questionBaseModels) {

        ArrayList<ChkLstSectionsQuestionAnswerList> sectionquestinandanswermodellist = new ArrayList<>();

        for (QuestionBaseModel questionbasemodel : questionBaseModels){
            ChkLstSectionsQuestionAnswerList CSQAL= new ChkLstSectionsQuestionAnswerList();
            ArrayList<QuestionAndAnswerModel> questinandanswermodellist = new ArrayList<>();
            for (QuestionQuestionListModel question : questionbasemodel.lstQuestion){
                QuestionAndAnswerModel questionandanswermodel = new QuestionAndAnswerModel();
                questionandanswermodel.setLstCourse(questionbasemodel.getLstCourse());
                questionandanswermodel.setQuestionModel(question);
                ArrayList<QuestionAnswerListModel> AnswerList = new ArrayList<>();
                if(question.getDraft_Remarks() != null && question.getDraft_Remarks().length() > 0){
                    questionandanswermodel.setCommentText(question.getDraft_Remarks().toString());
                }
                if(question.getDraft_Remark_Url() != null && question.getDraft_Remark_Url().length() > 0){
                    questionandanswermodel.setAttachmentURL(question.getDraft_Remark_Url().toString());
                }
                int i = 0;
                for (QuestionAnswerListModel answer: questionbasemodel.lstAnswer){
                    i += 1;
                    if (answer.getQuestion_Id() == question.getQuestion_Id()){
                        if(answer.getIs_Draft_Answer_Id() == 1) {
                            if(question.getQuestion_Type() == Constants.SINGLE_CHOICE_BUTTON_TYPE){
                                answer.setChosenbooleanAnswer(answer.getAnswer_Id());
                                questionandanswermodel.setChoosenAnswer(answer.getAnswer_Text().toString());
                                questionandanswermodel.setChoosenAnswerId(answer.getAnswer_Id()+"");
                            } else if(question.getQuestion_Type() == Constants.YES_NO_NA_TYPE){
                                answer.setChosenbooleanAnswer(answer.getAnswer_Id());
                                questionandanswermodel.setChoosenAnswer(answer.getAnswer_Text().toString());
                                questionandanswermodel.setChoosenAnswerId(answer.getAnswer_Id()+"");
                            }else if(question.getQuestion_Type() == Constants.SCALING_TYPE){
                                int progress = getRelatedAnswerslist(questionbasemodel.lstAnswer,question.getQuestion_Id());
                                answer.setChoosensliderAnswer(progress);
                                answer.setChoosensliderAnswerflag(true);
                                questionandanswermodel.setChoosenAnswer(answer.getAnswer_Text().toString());
                                questionandanswermodel.setChoosenAnswerId(answer.getAnswer_Id()+"");
                            }else if(question.getQuestion_Type() == Constants.RADIO_TYPE){
                                answer.setChosenRadioanswer(true);
                                questionandanswermodel.setChoosenAnswer(answer.getAnswer_Text().toString());
                                questionandanswermodel.setChoosenAnswerId(answer.getAnswer_Id()+"");
                            }else if(question.getQuestion_Type() == Constants.MULTIPLE_CHOICE_TYPE){
                                if(answer.getIs_Draft_Answer_Id() == 1){
                                    answer.setChoosencheckboxAnswer(true);
                                    if (questionandanswermodel.getChoosenAnswer() !=null&& questionandanswermodel.getChoosenAnswer().length()>0){
                                        questionandanswermodel.setChoosenAnswer(questionandanswermodel.getChoosenAnswer() + answer.getAnswer_Text().toString());
                                        questionandanswermodel.setChoosenAnswerId(questionandanswermodel.getChoosenAnswerId()+answer.getAnswer_Id()+",");
                                    }else {
                                        questionandanswermodel.setChoosenAnswer(answer.getAnswer_Text().toString());
                                        questionandanswermodel.setChoosenAnswerId(answer.getAnswer_Id()+",");
                                    }
                                }
                            }
                        }
                        if(answer.getIs_Draft_Answer_Text() != null){
                            if(!answer.getIs_Draft_Answer_Text().equals("")){
                                questionandanswermodel.setChoosenAnswer(answer.getIs_Draft_Answer_Text().toString());
                            }
                        }
                        AnswerList.add(answer);
                    }
                }
                questionandanswermodel.setLstAnswer(AnswerList);
                questinandanswermodellist.add(questionandanswermodel);
            }
            CSQAL.setLstCourse(questionbasemodel.getLstCourse().get(0));
            CSQAL.setQuestionanswerList(questinandanswermodellist);
            sectionquestinandanswermodellist.add(CSQAL);
        }

        return sectionquestinandanswermodellist;
    }

    public int getRelatedAnswerslist(List<QuestionAnswerListModel> answerlist, int Question_Id){
        int finalProgress = 0;
        int Count = 0;
        int i;
        List<QuestionAnswerListModel> answerlistnew = new ArrayList<>();
        for(QuestionAnswerListModel answers:answerlist){
            if(answers.getQuestion_Id() == Question_Id){
                Count = Count+1;
                answerlistnew.add(answers);
            }
        }

        for(i = 0;i<answerlistnew.size();i++){
            if(answerlistnew.get(i).getIs_Draft_Answer_Id() == 1) {
                Log.d("Count - I value",String.valueOf(i));
                break;
            }
        }
        Log.d("Count - ",String.valueOf(Count));
        int progressStep = (100 / (Count - 1));
        finalProgress = progressStep * i;
        Log.d("Count - finalProgress",String.valueOf(finalProgress));
        return finalProgress;
    }
}
