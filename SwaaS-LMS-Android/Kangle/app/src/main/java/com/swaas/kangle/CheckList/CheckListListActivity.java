package com.swaas.kangle.CheckList;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.swaas.kangle.CheckList.CheckListQuestionbuilder.QuestionActivity;
import com.swaas.kangle.CheckList.adapter.CheckListCategoryListTextRecyclerAdapter;
import com.swaas.kangle.CheckList.adapter.CheckListTagsListTextRecyclerAdapter;
import com.swaas.kangle.CheckList.adapter.OnetimeCheckListItemAdapter;
import com.swaas.kangle.CheckList.model.CheckListModel;
import com.swaas.kangle.CheckList.model.ChkLstSectionsQuestionAnswerList;
import com.swaas.kangle.CheckList.model.QuestionAndAnswerModel;
import com.swaas.kangle.CheckList.model.QuestionAnswerListModel;
import com.swaas.kangle.CheckList.model.QuestionBaseModel;
import com.swaas.kangle.CheckList.model.QuestionQuestionListModel;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.R;
import com.swaas.kangle.TaskModule.TaskListModel;
import com.swaas.kangle.TaskModule.TaskServices;
import com.swaas.kangle.db.CheckListTempRepository;
import com.swaas.kangle.db.Filters.ChecklistCatTagFilterRepository;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.CommonUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CheckListListActivity extends AppCompatActivity implements LocationListener {

    CheckListCategoryListTextRecyclerAdapter categoryListTextRecyclerAdapter;
    CheckListTagsListTextRecyclerAdapter tagsListTextRecyclerAdapter;
    OnetimeCheckListItemAdapter CheckListItemAdapter;

    Context mContext;
    List<CheckListModel> chkModelList = new ArrayList<CheckListModel>();
    List<CheckListModel> allchklists = new ArrayList<>();
    List<CheckListModel> yettostartChecklist = new ArrayList<>();
    HashMap<String, Integer> mchecklistMap,mchecklistTagsMap;
    List<String> checklistModelsCat;
    boolean completed_checked,yet_to_start_checked,expired;
    //GridLayoutManager grid;
    public double latitude,longitude;
    List<String> checklistModelsTags;
    boolean isFilterenabled = false;
    int checkEnabledTab = 0,frequencyofList = 10;
    String fromcatfilter = "",fromtagfilter = "";
    LinearLayoutManager onetimeLLM,linearLayoutManager;
    int AssignmentId, groupId;
    String PublishDate;


    ProgressDialog mProgressDialog;
    CheckBox mCheckCompleted;
    CheckBox mCheckYetToStart;
    CheckBox mCheckExpired;
    RelativeLayout checkBoxGroupView;
    View mainnestedView;
    EmptyRecyclerView checklistrecyclerView;
    ImageView companylogo,expandslider;
    TextView typeoflist;
    View checklist_empty_view;
    LinearLayout header;

    //filter changes
    RelativeLayout filtersection;
    TextView clearfilters,applyfilters;
    EmptyRecyclerView cattag_recyclerView;
    View catselection,tagselection,filterlay;
    TextView cat_filtered_count,tags_filtered_count;
    TextView clear_assetfilter,filtered_text;
    View assetfilterheading;
    TextView emptytagsview;
    ImageView close_filter;

    boolean tagfiltered,catFiltered;
    List<String> tagslist,catlist;
    List<CheckListModel> digitalAssetsMasterCategoryLists;
    List<CheckListModel> digitalAssetsMasterTagsLists;
    String TAGS = "",CATS = "";
    ImageView mCourseFilter;
    TextView mFilteredCountStatus;

    CheckListTempRepository checkListTempRepository;
    ChecklistCatTagFilterRepository checklistCatTagFilterRepository;
    List<CheckListModel> digitalAssetsMasterListfilterd;

    View clear_assetfilter_img;


    View checkyettostart,checkcompleted,checkexpired;
    TextView filterheadingtext,empty_message,retrybutton;

    View cattagmenus;
    ImageView icon_cats,icon_tags,icon_filter,icon_search,closesearch;

    SearchView msearchtext;
    SearchManager searchManager;
    List<CheckListModel> msearchcourse;
    RelativeLayout searchlayout;

    RelativeLayout filterheading;
    ImageView tickfilter,emptyimage;

    ArrayList<QuestionBaseModel>  mQuestionBaseModels;
    ArrayList<TaskListModel> mTaskLists;
    public String DraftId = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list_list);

        mContext = CheckListListActivity.this;
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        initializeView();
        setUpRecyclerView();

        checkListTempRepository = new CheckListTempRepository(mContext);
        checklistCatTagFilterRepository = new ChecklistCatTagFilterRepository(mContext);

        if(getIntent() != null){
            checkEnabledTab = getIntent().getIntExtra("checkEnabledTab",1);
            frequencyofList = getIntent().getIntExtra("TypeofList",0);
        }
        setthemeforView();
        tagslist = new ArrayList<>();
        catlist = new ArrayList<>();
        //getListOfCheckList();
        onClickListeners();
    }

    public void initializeView(){
        companylogo = (ImageView) findViewById(R.id.companylogo);
        expandslider = (ImageView) findViewById(R.id.icon_expandslider);
        mainnestedView = findViewById(R.id.mainnestedView);
        checkBoxGroupView = (RelativeLayout)findViewById(R.id.check_box);
        mCheckCompleted = (CheckBox)findViewById(R.id.check_completed);
        mCheckYetToStart = (CheckBox)findViewById(R.id.check_yet_to_start);
        mCheckExpired = (CheckBox)findViewById(R.id.check_expired);

        typeoflist = (TextView) findViewById(R.id.typeoflist);
        checklistrecyclerView = (EmptyRecyclerView) findViewById(R.id.checklistrecyclerView);

        header = (LinearLayout) findViewById(R.id.header);

        cattag_recyclerView = (EmptyRecyclerView) findViewById(R.id.cattag_recyclerView);
        filtersection = (RelativeLayout) findViewById(R.id.filtersection);
        close_filter = (ImageView) findViewById(R.id.close_filter);
        catselection = findViewById(R.id.catselection);
        tagselection = findViewById(R.id.tagselection);
        filterlay = findViewById(R.id.filterlay);
        clearfilters = (TextView) findViewById(R.id.clearfilters);
        applyfilters = (TextView) findViewById(R.id.applyfilters);

        assetfilterheading =  findViewById(R.id.assetfilterheading);
        filtered_text = (TextView) findViewById(R.id.filtered_text);
        clear_assetfilter = (TextView) findViewById(R.id.clear_assetfilter);
        mCourseFilter = (ImageView)findViewById(R.id.icon_filter);
        cat_filtered_count = (TextView) findViewById(R.id.cat_filtered_count);
        tags_filtered_count = (TextView) findViewById(R.id.tags_filtered_count);
        mFilteredCountStatus = (TextView)findViewById(R.id.label_filtered_count);
        emptytagsview = (TextView)findViewById(R.id.emptytagsview);
        checklist_empty_view = findViewById(R.id.checklist_empty_view);

        clear_assetfilter_img = findViewById(R.id.clear_assetfilter_img);

        checkyettostart = findViewById(R.id.checkyettostart);
        checkcompleted = findViewById(R.id.checkcompleted);
        checkexpired = findViewById(R.id.checkexpired);

        filterheadingtext = (TextView)findViewById(R.id.filterheadingtext);
        cattagmenus = findViewById(R.id.cattagmenus);
        icon_cats = (ImageView)findViewById(R.id.icon_cats);
        icon_tags = (ImageView)findViewById(R.id.icon_tags);
        icon_filter = (ImageView) findViewById(R.id.icon_filter);

        empty_message = (TextView) findViewById(R.id.empty_message);
        searchlayout = (RelativeLayout) findViewById(R.id.searchlayout);
        msearchtext = (SearchView) findViewById(R.id.searchtext);
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        icon_search = (ImageView) findViewById(R.id.icon_search);
        closesearch = (ImageView) findViewById(R.id.closesearch);

        tickfilter = (ImageView) findViewById(R.id.tickfilter);
        filterheading = (RelativeLayout) findViewById(R.id.filterheading);

        emptyimage = (ImageView) findViewById(R.id.emptyimage);
        retrybutton = (TextView) findViewById(R.id.retrybutton);
    }

    private void setUpRecyclerView() {
        checklistrecyclerView.setHasFixedSize(true);
        cattag_recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        onetimeLLM = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        if(getResources().getBoolean(R.bool.portrait_only)){
            checklistrecyclerView.setLayoutManager(onetimeLLM);
        }else{
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                GridLayoutManager grid = new GridLayoutManager(this,3);
                checklistrecyclerView.setLayoutManager(grid);
                //checklistrecyclerView.setLayoutManager(onetimeLLM);
            }else{
                GridLayoutManager grid = new GridLayoutManager(this,2);
                checklistrecyclerView.setLayoutManager(grid);
                //checklistrecyclerView.setLayoutManager(onetimeLLM);
            }
        }
        cattag_recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void setthemeforView(){

        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        companylogo.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        mainnestedView.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));

        mCheckYetToStart.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        mCheckCompleted.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        mCheckExpired.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        clearfilters.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
        empty_message.setTextColor(Color.parseColor(Constants.TEXT_COLOR));

        closesearch.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        closesearch.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        icon_search.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        expandslider.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        filtered_text.setTypeface(filtered_text.getTypeface(), Typeface.ITALIC);

        filterheading.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        filterheadingtext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        close_filter.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));

        assetfilterheading.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        filtered_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tickfilter.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));

        emptyimage.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        retrybutton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.CARDBACKGROUND_COLOR)));
        retrybutton.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
    }

    public void getListOfCheckList() {
        if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
            showProgressDialog();
            checkListTempRepository.setGetChecklistDataCBListner(new CheckListTempRepository.GetChecklistDataCBListner() {
                @Override
                public void GetChecklistDataSuccessCB(List<CheckListModel> checklist) {
                    checkListTempRepository.checkListBulkInsert(checklist);
                    if(checklist != null){
                        getLocalData();
                    }else{
                        dismissProgressDialog();
                        checklistrecyclerView.setVisibility(View.GONE);
                        showhideemptystate(true,getResources().getString(R.string.no_result),Constants.NORESULTSNUM);
                        expandslider.setVisibility(View.GONE);
                        icon_search.setVisibility(View.GONE);
                    }
                }

                @Override
                public void GetChecklistDataFailureCB(String message) {
                    dismissProgressDialog();
                }
            });
            String offsetFromUtc = CommonUtils.getUtcOffsetincluded10k();
            Log.d("getUTC", offsetFromUtc);
            int CompanyId = PreferenceUtils.getCompnayId(mContext);
            Log.d("CompanyId", String.valueOf(CompanyId));
            String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            int UserId = PreferenceUtils.getUserId(mContext);
            checkListTempRepository.getChecklistDataFromAPI(SubdomainName, CompanyId, UserId, offsetFromUtc);
        }else{
            checklistrecyclerView.setVisibility(View.GONE);
            showhideemptystate(true,getResources().getString(R.string.error_message),Constants.INTERNETERRORNUM);
        }
    }

    public void getLocalData(){
        if (checkEnabledTab == 0) {
            checkEnabledTab = 1;
        }
        allchklists = checkListTempRepository.getAllData();
        chkModelList = checkListTempRepository.getrefinedData(checkEnabledTab, frequencyofList);
        yettostartChecklist = checkListTempRepository.getAllYetToStartData(checkEnabledTab, frequencyofList);
        insertintocategoreytagstable();
        if(!isFilterenabled) {
            loaddatabyintent(yettostartChecklist, checkEnabledTab);
        }else{
            getCoursesbyIDs();
        }
    }

    public void loaddatabyintent(List<CheckListModel> chModelList,int checkEnableTab){
        dismissProgressDialog();
        if(checkEnableTab == 0){
            checkEnabledTab = 1;
        }else{
            if (checkEnabledTab == Constants.MYLIST) {
                if(frequencyofList == Constants.ONETIME){
                    typeoflist.setText(getResources().getString(R.string.Onetime));
                }else if(frequencyofList == Constants.DAILY){
                    typeoflist.setText(getResources().getString(R.string.daily));
                }else if(frequencyofList == Constants.WEELY){
                    typeoflist.setText(getResources().getString(R.string.weekly));
                }else if(frequencyofList == Constants.MONTHLY){
                    typeoflist.setText(getResources().getString(R.string.monthly));
                }
            } else {
                if(frequencyofList == Constants.ONETIME){
                    typeoflist.setText(getResources().getString(R.string.Onetime));
                }else if(frequencyofList == Constants.DAILY){
                    typeoflist.setText(getResources().getString(R.string.daily));
                }else if(frequencyofList == Constants.WEELY){
                    typeoflist.setText(getResources().getString(R.string.weekly));
                }else if(frequencyofList == Constants.MONTHLY){
                    typeoflist.setText(getResources().getString(R.string.monthly));
                }
            }
        }
        if(chModelList != null && chModelList.size()>0){
            CheckListItemAdapter = new OnetimeCheckListItemAdapter(mContext,chModelList);
            checklistrecyclerView.setAdapter(CheckListItemAdapter);
            checklistrecyclerView.setVisibility(View.VISIBLE);
            showhideemptystate(false,"",0);
            loadadapterclick();
        }else{
            checklistrecyclerView.setVisibility(View.GONE);
            showhideemptystate(true,getResources().getString(R.string.no_result),Constants.NORESULTSNUM);
        }

        if(chkModelList.size() > 0){
            expandslider.setVisibility(View.VISIBLE);
            icon_search.setVisibility(View.VISIBLE);
        }else{
            expandslider.setVisibility(View.GONE);
            icon_search.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUpRecyclerView();
    }

    public void loadSearchdata(String newText){
        msearchcourse = new ArrayList<>();
        msearchcourse = chkModelList;
        List<CheckListModel> filteredList = new ArrayList<>();
        if(msearchcourse.size() > 0) {
            for (CheckListModel row : msearchcourse) {
                String name = row.getChecklist_Name().toLowerCase();
                if (name.contains(newText.toLowerCase())) {
                    filteredList.add(row);
                }
            }
            if (filteredList.size() > 0) {
                loaddatabyintent(filteredList, checkEnabledTab);
            } else {
                checklistrecyclerView.setVisibility(View.GONE);
                showhideemptystate(true,getResources().getString(R.string.no_result),Constants.NORESULTSNUM);
            }
        }else{
            checklistrecyclerView.setVisibility(View.GONE);
            showhideemptystate(true,getResources().getString(R.string.no_result),Constants.NORESULTSNUM);
        }
    }

    public void onClickListeners(){
        retrybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListOfCheckList();
            }
        });

        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        icon_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchlayout.setVisibility(View.VISIBLE);
            }
        });

        closesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchlayout.setVisibility(View.GONE);
                loaddatabyintent(yettostartChecklist, checkEnabledTab);
            }
        });

        searchlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        msearchtext.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));

        msearchtext.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchlayout.setVisibility(View.GONE);
                loaddatabyintent(yettostartChecklist, checkEnabledTab);
                return false;
            }
        });

        msearchtext.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() > 0){
                    closesearch.setVisibility(View.GONE);
                }else{
                    closesearch.setVisibility(View.VISIBLE);
                }
                loadSearchdata(newText);
                return false;
            }
        });

        mCheckCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showApplyButton();
            }
        });

        mCheckYetToStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showApplyButton();
            }
        });
        mCheckExpired.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showApplyButton();
            }
        });

        checkyettostart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckYetToStart.isChecked()){
                    mCheckYetToStart.setChecked(false);
                }else{
                    mCheckYetToStart.setChecked(true);
                }
                showApplyButton();
            }
        });

        checkcompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckCompleted.isChecked()){
                    mCheckCompleted.setChecked(false);
                }else{
                    mCheckCompleted.setChecked(true);
                }
                showApplyButton();
            }
        });

        checkexpired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckExpired.isChecked()){
                    mCheckExpired.setChecked(false);
                }else{
                    mCheckExpired.setChecked(true);
                }
                showApplyButton();
            }
        });

        expandslider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCheckCompleted.setChecked(false);
                mCheckYetToStart.setChecked(false);
                mCheckExpired.setChecked(false);
                filtersection.setVisibility(View.VISIBLE);
                catlist.clear();
                tagslist.clear();
                digitalAssetsMasterCategoryLists = checklistCatTagFilterRepository.getAllCategory(CATS,checkEnabledTab,frequencyofList);
                digitalAssetsMasterTagsLists = checklistCatTagFilterRepository.getAllTags(TAGS,checkEnabledTab,frequencyofList);
                cattag_recyclerView.setVisibility(View.VISIBLE);
                checkBoxGroupView.setVisibility(View.GONE);
                loadCategorySlider();
            }
        });

        close_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFilterenabled){
                    filtersection.setVisibility(View.GONE);
                    assetfilterheading.setVisibility(View.VISIBLE);
                }else {
                    filtersection.setVisibility(View.GONE);
                    catlist.clear();
                    tagslist.clear();
                    CATS = "";TAGS = "";
                    mCheckCompleted.setChecked(false);
                    mCheckYetToStart.setChecked(false);
                    mCheckExpired.setChecked(false);
                    catFiltered = false; tagfiltered = false;
                    showApplyButton();
                    getLocalData();
                }
            }
        });

        catselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCategorySlider();
                checkBoxGroupView.setVisibility(View.GONE);
            }
        });

        tagselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTagsSlider();
                checkBoxGroupView.setVisibility(View.GONE);
            }
        });

        filterlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleselection(3);
                cattag_recyclerView.setVisibility(View.GONE);
                checkBoxGroupView.setVisibility(View.VISIBLE);
            }
        });

        clearfilters.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    clearfilters.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
                    clearfilters.setTextColor(Color.WHITE);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    clearfilters.setBackgroundColor(Color.WHITE);
                    clearfilters.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
                    clearfiltersFunction();
                    filtersection.setVisibility(View.GONE);
                    getLocalData();
                }
                return true;
            }
        });

        applyfilters.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    applyfilters.setBackgroundColor(Color.WHITE);
                    applyfilters.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    applyfilters.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
                    applyfilters.setTextColor(Color.WHITE);
                    getCoursesbyIDs();
                    filtersection.setVisibility(View.GONE);
                    assetfilterheading.setVisibility(View.VISIBLE);
                    String htmlString="<u>"+getResources().getString(R.string.clearfilter)+"</u>";
                    filtered_text.setText(Html.fromHtml(htmlString));
                    isFilterenabled = true;
                }
                return true;
            }
        });

        clear_assetfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtersection.setVisibility(View.GONE);
                catlist.clear();
                tagslist.clear();
                CATS = "";TAGS = "";
                assetfilterheading.setVisibility(View.GONE);
                isFilterenabled = false;
                clearfiltersFunction();
                getLocalData();
            }
        });

        clear_assetfilter_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assetfilterheading.setVisibility(View.GONE);
                filtersection.setVisibility(View.VISIBLE);
            }
        });

        filterheadingtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        filtered_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtersection.setVisibility(View.GONE);
                catlist.clear();
                tagslist.clear();
                CATS = "";
                TAGS = "";
                assetfilterheading.setVisibility(View.GONE);
                isFilterenabled = false;
                clearfiltersFunction();
                getLocalData();
            }
        });
    }

    public void clearfiltersFunction(){
        catlist.clear();
        tagslist.clear();
        CATS = "";TAGS = "";
        mCheckCompleted.setChecked(false);
        mCheckYetToStart.setChecked(false);
        mCheckExpired.setChecked(false);
        catFiltered = false; tagfiltered = false;
        showApplyButton();
        isFilterenabled = false;
        digitalAssetsMasterCategoryLists = checklistCatTagFilterRepository.getAllCategory(CATS,checkEnabledTab,frequencyofList);
        digitalAssetsMasterTagsLists = checklistCatTagFilterRepository.getAllTags(TAGS,checkEnabledTab,frequencyofList);
        categoryListTextRecyclerAdapter = new CheckListCategoryListTextRecyclerAdapter(mContext, digitalAssetsMasterCategoryLists);
        categoryListTextRecyclerAdapter.notifyDataSetChanged();
        tagsListTextRecyclerAdapter = new CheckListTagsListTextRecyclerAdapter(mContext,digitalAssetsMasterTagsLists);
        tagsListTextRecyclerAdapter.notifyDataSetChanged();
        catselection.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        tagselection.setBackgroundColor(Color.parseColor(Constants.OPAQUE_COLOR));
        filterlay.setBackgroundColor(Color.parseColor(Constants.OPAQUE_COLOR));
        cattag_recyclerView.setVisibility(View.VISIBLE);
        checkBoxGroupView.setVisibility(View.GONE);
        cattag_recyclerView.setAdapter(categoryListTextRecyclerAdapter);
    }

    public void loadchecklistbyCategory(String categoryName){
        fromcatfilter = categoryName;
        fromtagfilter = "";
        List<CheckListModel> checklistAssetscategory= new ArrayList<>();
        for(CheckListModel checklistModel: chkModelList){
            if(categoryName.equalsIgnoreCase(checklistModel.getCategory_Name())){
                checklistAssetscategory.add(checklistModel);
            }
        }
        setUpRecyclerView();
    }

    private void loadFilteredList(List<CheckListModel> chk) {
        isFilterenabled = true;
        int countchecked = 0;
        final ArrayList<CheckListModel> filteredchecklist = new ArrayList<CheckListModel>();
        completed_checked = mCheckCompleted.isChecked();
        yet_to_start_checked = mCheckYetToStart.isChecked();
        expired = mCheckExpired.isChecked();
        if(!completed_checked && !yet_to_start_checked && !expired){
            loaddatabyintent(chk,checkEnabledTab);
        } else {
            if (completed_checked) {
                for (CheckListModel checklistModel : chk) {
                    if (checklistModel.getChecklist_Status_Value() == Constants.COMPLETED) {
                        filteredchecklist.add(checklistModel);
                    }
                }
                countchecked++;
            }
            if (yet_to_start_checked) {
                for (CheckListModel checklistModel : chk) {
                    if (checklistModel.getChecklist_Status_Value() == Constants.YET_TO_START) {
                        filteredchecklist.add(checklistModel);
                    }
                }
                countchecked++;
            }
            if (expired){
                for (CheckListModel checklistModel : chk) {
                    if (checklistModel.getChecklist_Status_Value() == Constants.COURSE_EXPIRED) {
                        filteredchecklist.add(checklistModel);
                    }
                }
                countchecked++;
            }
            //loaddatabyselectedtab(filteredchecklist,checkEnabledTab);
            loaddatabyintent(filteredchecklist,checkEnabledTab);
        }
    }

    public void loadchecklistbyTags(String tagName){
        fromtagfilter = tagName;
        fromcatfilter = "";
        //mchecklistFilter.setEnabled(true);
        //allasset.setText(tagName);
        List<CheckListModel> checklistAssetsTags= new ArrayList<>();
        for(CheckListModel checklistModel: chkModelList){
            //courseModel.getCourse_Tags().replace("^","#");
            if(checklistModel.getChecklist_Tags()!= null && !checklistModel.getChecklist_Tags().isEmpty()) {
                if (checklistModel.getChecklist_Tags().contains(tagName)) {
                    checklistAssetsTags.add(checklistModel);
                }
            }
        }
        setUpRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListOfCheckList();
        if(isFilterenabled){

        }else {
            filtersection.setVisibility(View.GONE);
            catlist.clear();
            tagslist.clear();
            CATS = "";
            TAGS = "";
            assetfilterheading.setVisibility(View.GONE);
            isFilterenabled = false;
            clearfiltersFunction();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public void loadadapterclick(){
        dismissProgressDialog();
        CheckListItemAdapter.setOnItemClickListener(new OnetimeCheckListItemAdapter.MyClickListener() {
            @Override
            public void onItemClick(int Id, int checklistStatus, int CUAId) {
                if(NetworkUtils.isNetworkAvailable()) {
                    if(checklistStatus != 0){
                        for(CheckListModel chk:allchklists){
                            if(chk.getChecklist_User_Assignment_Id() == CUAId){
                                AssignmentId = chk.getChecklist_User_Assignment_Id();
                                PublishDate = chk.getPublish_Date_String();
                                groupId = chk.getCheckList_Publish_Group_Id();
                                gotoreport(Id,chk.getChecklist_Name(),AssignmentId,PublishDate,groupId);
                            }
                        }
                    }else{
                        for(CheckListModel chk:allchklists){
                            if(chk.getChecklist_User_Assignment_Id() == CUAId){
                                AssignmentId = chk.getChecklist_User_Assignment_Id();
                                PublishDate = chk.getPublish_Date_String();
                                groupId = chk.getCheckList_Publish_Group_Id();
                                int typeId = chk.getChecklist_Type_ID();
                                getListOfQuestions(Id,chk.getChecklist_Name(),CUAId,PublishDate,groupId,chk.getChecklist_Description(), typeId);
                            }
                        }
                    }
                }else{
                    Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void gotoreport(final int ChecklistId,final String chklistname,final int CUMId,
                           String PublishDate,int chklstgroupid){
        Intent sectionQuestionActivity = new Intent(mContext, SectionsQuestionDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ChecklistId",ChecklistId);
        bundle.putSerializable("name",chklistname);
        bundle.putSerializable("CUMId",CUMId);
        bundle.putSerializable("PublishDate",PublishDate);
        bundle.putSerializable("ChecklistGroupId",chklstgroupid);
        bundle.putSerializable("isfromcoursechecklist",false);
        sectionQuestionActivity.putExtras(bundle);

        startActivity(sectionQuestionActivity);
    }


    public void getListOfQuestions(final int ChecklistId, final String chklistname, final int CUAId,
                                   final String PublishDate, final int chklstgroupid, final String chklstdesc, final int checklist_type_id){

        if(NetworkUtils.isNetworkAvailable()){
            showProgressDialog();
            /*RetrofitCustomApiBuilder retrofitcustomAPI = new RetrofitCustomApiBuilder("https://api.myjson.com/");
            Retrofit retrofit = retrofitcustomAPI.getInstance();*/

            Retrofit retrofit = RetrofitAPIBuilder.getInstance();

            CheckListService checklistService = retrofit.create(CheckListService.class);
            int CompanyId = PreferenceUtils.getCompnayId(mContext);
            Log.d("CompanyId", String.valueOf(CompanyId));
            String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            int UserId = PreferenceUtils.getUserId(mContext);

            //Call call = checklistService.getSectionsandQuestion();
            Call call = checklistService.getChecklistQuestionAnswerDetailsAPI(SubdomainName, CompanyId, UserId, ChecklistId,CUAId,"0");
            call.enqueue(new Callback<ArrayList<QuestionBaseModel>>() {

                @Override
                public void onResponse(Response<ArrayList<QuestionBaseModel>> response, Retrofit retrofit) {
                    ArrayList<QuestionBaseModel> questionBaseModels= response.body();
                    mQuestionBaseModels = new ArrayList<>();
                    dismissProgressDialog();
                    if (questionBaseModels != null) {
                        mQuestionBaseModels = questionBaseModels;
                        getDraftTaskList(ChecklistId,chklistname,CUAId,PublishDate,chklstgroupid,chklstdesc, checklist_type_id);
                    } else {
                        Toast.makeText(CheckListListActivity.this,getResources().getString(R.string.noQuestions),Toast.LENGTH_SHORT).show();
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

    private void getDraftTaskList(final int ChecklistId, final String chklistname, final int CUAId,
                                  final String PublishDate, final int chklstgroupid, final String chklstdesc, final int checklist_type_id) {
        int QuestionId = 0;
        int GroupChecklistCheck = 0;
        if(checkEnabledTab == Constants.MYGROUP){
            GroupChecklistCheck = 2;
        }else{
            GroupChecklistCheck = 1;
        }
        int Course_section_id = 0;

        if(NetworkUtils.isNetworkAvailable()){
            showProgressDialog();
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();

            TaskServices checklistService = retrofit.create(TaskServices.class);
            int CompanyId = PreferenceUtils.getCompnayId(mContext);
            Log.d("CompanyId", String.valueOf(CompanyId));
            String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            int UserId = PreferenceUtils.getUserId(mContext);

            //Call call = checklistService.getSectionsandQuestion();
            Call call = checklistService.getTaskSaveDraftNormalChecklistApi(SubdomainName, CompanyId, ChecklistId,QuestionId ,UserId, GroupChecklistCheck ,Course_section_id,chklstgroupid);
            call.enqueue(new Callback<ArrayList<TaskListModel>>() {

                @Override
                public void onResponse(Response<ArrayList<TaskListModel>> response, Retrofit retrofit) {
                    ArrayList<TaskListModel> tasklists= response.body();
                    mTaskLists = new ArrayList<>();
                    if (tasklists != null) {
                        mTaskLists = tasklists;
                    }
                    gotoQuestionActivity(ChecklistId,chklistname,CUAId,PublishDate,chklstgroupid,chklstdesc, checklist_type_id);
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
                                     final String PublishDate, final int chklstgroupid, final String chklstdesc,int checklist_type_id){
        dismissProgressDialog();
        ArrayList<ChkLstSectionsQuestionAnswerList> secquestionAndAnswerModels =  CallToGetQuestionModel(mQuestionBaseModels);
        if(secquestionAndAnswerModels != null && secquestionAndAnswerModels.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("value", secquestionAndAnswerModels);
            bundle.putSerializable("name",chklistname);
            bundle.putSerializable("ChecklistId",ChecklistId);
            bundle.putSerializable("CUMId",CUAId);
            bundle.putSerializable("PublishDate",PublishDate);
            bundle.putSerializable("ChecklistPublishType",checkEnabledTab);
            bundle.putSerializable("AssignmentId",AssignmentId);
            bundle.putSerializable("ChecklistGroupId",chklstgroupid);
            bundle.putSerializable("ChecklistDescription",chklstdesc);
            bundle.putSerializable("CCuserlist","");
            bundle.putSerializable("isfromcoursechecklist",false);
            bundle.putSerializable(Constants.TaskList,mTaskLists);
            bundle.putSerializable(Constants.DraftId,DraftId);
            /*bundle.putSerializable("ChecklistPublishGroupId", checkList_publish_group_id);*/
            bundle.putSerializable("CheckListTypeId", checklist_type_id);
            if(checkEnabledTab == Constants.MYGROUP){
                bundle.putSerializable("isGroupChecklist",true);
            }else{
                bundle.putSerializable("isGroupChecklist",false);
            }

            Intent intentToQuestionActivity = new Intent(mContext, QuestionActivity.class);
            intentToQuestionActivity.putExtras(bundle);
            startActivity(intentToQuestionActivity);
        }else{
            Toast.makeText(CheckListListActivity.this,getResources().getString(R.string.noQuestions),Toast.LENGTH_SHORT).show();
        }
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
                                        questionandanswermodel.setChoosenAnswer(questionandanswermodel.getChoosenAnswer() + answer.getAnswer_Text().toString()+",");
                                        questionandanswermodel.setChoosenAnswerId(questionandanswermodel.getChoosenAnswerId()+answer.getAnswer_Id()+",");
                                    }else {
                                        questionandanswermodel.setChoosenAnswer(answer.getAnswer_Text().toString()+",");
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

    public int getRelatedAnswerslist(List<QuestionAnswerListModel> answerlist,int Question_Id){
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

    //filter Functions

    public void loadCategorySlider(){
        try{
            toggleselection(1);
            if(tagslist.size()>0){
                String tagnames = "";
                for (String catname : tagslist) {
                    tagnames =  tagnames + "," + "'" + catname.toString() + "'";
                }
                StringBuilder sb = new StringBuilder(tagnames);
                sb.deleteCharAt(0);
                String valuenames = sb.toString().replace("''", "'");
                List<CheckListModel> digitalAssetsMasterCategoryList = checklistCatTagFilterRepository.getCategorybySelectedTags(valuenames,CATS,checkEnabledTab,frequencyofList);
                reloadCatSlider(digitalAssetsMasterCategoryList);
            } else {
                List<CheckListModel> digitalAssetsMasterCategoryList = checklistCatTagFilterRepository.getAllCategory(CATS,checkEnabledTab,frequencyofList);
                reloadCatSlider(digitalAssetsMasterCategoryList);
            }
        }catch (Exception e){

        }
    }

    public void loadTagsSlider(){
        try{
            toggleselection(2);
            if(catlist.size()>0){
                String catnames = "";
                for (String catname : catlist) {
                    catnames = catnames + "," + "'" + catname.toString() + "'";
                }
                StringBuilder sb = new StringBuilder(catnames);
                sb.deleteCharAt(0);
                String valuenames = sb.toString().replace("''", "'");
                List<CheckListModel> digitalAssetsMasterTagsList = checklistCatTagFilterRepository.getTagsbySelectedcategorey(valuenames,TAGS,checkEnabledTab,frequencyofList);
                reloadTagSlider(digitalAssetsMasterTagsList);
            }else{
                List<CheckListModel> digitalAssetsMasterTagsList = checklistCatTagFilterRepository.getAllTags(TAGS,checkEnabledTab,frequencyofList);
                reloadTagSlider(digitalAssetsMasterTagsList);
            }
        }catch (Exception e){

        }

    }

    public void reloadCatSlider(List<CheckListModel> newmodel){
        List<CheckListModel> catrefresh = new ArrayList<>();
        List<CheckListModel> newrefreshed = new ArrayList<>();
        for(CheckListModel existing : digitalAssetsMasterCategoryLists){
            if(existing.iscatchecked){
                catrefresh.add(existing);
            }
        }
        for(CheckListModel ne : newmodel){
            catrefresh.add(ne);
        }
        emptytagsview.setVisibility(View.GONE);
        cattag_recyclerView.setVisibility(View.VISIBLE);
        digitalAssetsMasterCategoryLists = catrefresh;
        categoryListTextRecyclerAdapter = new CheckListCategoryListTextRecyclerAdapter(mContext, digitalAssetsMasterCategoryLists);
        cattag_recyclerView.setAdapter(categoryListTextRecyclerAdapter);
    }

    public void reloadTagSlider(List<CheckListModel> newmodel){
        List<CheckListModel> tagrefresh = new ArrayList<>();
        List<CheckListModel> newtgrefresh = new ArrayList<>();
        for(CheckListModel existing : digitalAssetsMasterTagsLists){
            if(existing.istagchecked){
                tagrefresh.add(existing);
            }
        }
        for(CheckListModel ne : newmodel){
            tagrefresh.add(ne);
        }
        digitalAssetsMasterTagsLists = tagrefresh;
        showtagsView(digitalAssetsMasterTagsLists,true);
    }

    public void showtagsView(List<CheckListModel> digitalAssetsMasterTagsList,boolean status){
        if(digitalAssetsMasterTagsList.size() > 0){
            emptytagsview.setVisibility(View.GONE);
            checkBoxGroupView.setVisibility(View.GONE);
            cattag_recyclerView.setVisibility(View.VISIBLE);
            tagsListTextRecyclerAdapter = new CheckListTagsListTextRecyclerAdapter(mContext,digitalAssetsMasterTagsList);
            cattag_recyclerView.setAdapter(tagsListTextRecyclerAdapter);
        }else{
            cattag_recyclerView.setVisibility(View.GONE);
            checkBoxGroupView.setVisibility(View.GONE);
            emptytagsview.setVisibility(View.VISIBLE);
        }
    }

    public void insertintocategoreytagstable(){
        List<CheckListModel> digitalAssetsListfortags = allchklists;
        List<CheckListModel> cttags = new ArrayList<>();
        for (CheckListModel dig : digitalAssetsListfortags) {
            if(dig.getCategory_Name() != null && !dig.getCategory_Name().isEmpty()){
                if(dig.getChecklist_Tags() != "" && dig.getChecklist_Tags() != null && !dig.getChecklist_Tags().isEmpty()) {
                    String tagsstring = dig.getChecklist_Tags().replace("^","#");
                    String[] tags = tagsstring.split("#");
                    for (String t : tags) {
                        CheckListModel values = new CheckListModel();
                        values.setChecklist_Id(dig.getChecklist_Id());
                        values.setChecklist_Frequency_Id(dig.getChecklist_Frequency_Id());
                        values.setChecklist_Type_ID(dig.getChecklist_Type_ID());
                        values.setChecklist_Category_Id(dig.getChecklist_Category_Id());
                        values.setCategory_Name(dig.getCategory_Name());
                        values.setTags(t);
                        values.setChecklist_Classification(dig.getChecklist_Classification());
                        cttags.add(values);
                    }
                }else{
                    CheckListModel values = new CheckListModel();
                    values.setChecklist_Id(dig.getChecklist_Id());
                    values.setChecklist_Frequency_Id(dig.getChecklist_Frequency_Id());
                    values.setChecklist_Type_ID(dig.getChecklist_Type_ID());
                    values.setChecklist_Category_Id(dig.getChecklist_Category_Id());
                    values.setCategory_Name(dig.getCategory_Name());
                    values.setTags(null);
                    values.setChecklist_Classification(dig.getChecklist_Classification());
                    cttags.add(values);
                }
            }
        }
        checklistCatTagFilterRepository.catTagsBulkInsert(cttags);
    }

    public void filteredcatList(CheckListModel cat){
        try{
            if(cat.iscatchecked()) {
                catlist.add(cat.getCategory_Name());
            }else{
                catlist.remove(cat.getCategory_Name());
            }
            if(catlist.size() > 0){
                catFiltered = true;
            }else{
                catFiltered = false;
            }

            if(catFiltered){
                String catnames = "";
                for (String catname : catlist) {
                    catnames = catnames + "," + "'" + catname.toString() + "'";
                }
                StringBuilder sb = new StringBuilder(catnames);
                sb.deleteCharAt(0);
                String valuenames = sb.toString().replace("''", "'");
                CATS = valuenames;
                showApplyButton();
            }else{
                CATS = "";
            }
            showApplyButton();

        }catch (Exception e){

        }
    }

    public void filteredtagList(CheckListModel cat){
        try{
            if(cat.istagchecked()) {
                tagslist.add(cat.getTags());
            }else{
                tagslist.remove(cat.getTags());
            }
            if(tagslist.size() > 0){
                tagfiltered = true;
            }else{
                tagfiltered = false;
            }

            if(tagfiltered) {
                String tagnames = "";
                for (String catname : tagslist) {
                    tagnames = tagnames + "," + "'" + catname.toString() + "'";
                }
                StringBuilder sb = new StringBuilder(tagnames);
                sb.deleteCharAt(0);
                String valuenames = sb.toString().replace("''", "'");
                TAGS = valuenames;
            }else{
                TAGS = "";
            }
            showApplyButton();
        }catch(Exception e){

        }
    }

    public void getCoursesbyIDs(){
        try{
            String selectQuery = "SELECT tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Id FROM tbl_CHECKLIST_CAT_TAG_MASTER  ";
            if (CATS.length() > 0 || TAGS.length() > 0) {
                selectQuery += "WHERE ";
                if (CATS.length() > 0) {
                    selectQuery += "tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name in ("+CATS+") ";
                }
                if (TAGS.length() > 0) {
                    if (CATS.length() > 0) {
                        selectQuery += " AND ";
                    }
                    selectQuery += "tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Tags in ("+TAGS+") ";
                }
            }
            selectQuery += " Group by Checklist_Id";
            List<CheckListModel> DAIDlist = checklistCatTagFilterRepository.getAssetIdbySelectedCatTag(selectQuery);
            filteredlist(DAIDlist);

            /*String query = "";
            if(CATS.length() > 0 && TAGS.length() == 0){
                query = "select tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Id from tbl_CHECKLIST_CAT_TAG_MASTER where " +
                        "tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name in ("+CATS+") Group by Checklist_Id";
            } else if(TAGS.length() > 0 && CATS.length() == 0){
                query = "select tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Id from tbl_CHECKLIST_CAT_TAG_MASTER where " +
                        "tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Tags in ("+TAGS+") Group by Checklist_Id";
            } else if(CATS.length() > 0 && TAGS.length() > 0){
                query = "select tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Id from tbl_CHECKLIST_CAT_TAG_MASTER where " +
                        "tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Tags in ("+TAGS+") AND tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name in ("+CATS+") Group by Checklist_Id";
            }
            List<CheckListModel> DAIDlist = checklistCatTagFilterRepository.getAssetIdbySelectedCatTag(query);
            filteredlist(DAIDlist);*/
        }catch(Exception e){

        }
    }

    public void filteredlist(List<CheckListModel> DAIDlist){
        try{
            if(DAIDlist != null) {
                if(DAIDlist.size() > 0){
                    digitalAssetsMasterListfilterd = new ArrayList<>();
                    for (CheckListModel cms : DAIDlist) {
                        for (CheckListModel cs : chkModelList) {
                            if (cms.getChecklist_Id() == cs.getChecklist_Id()) {
                                digitalAssetsMasterListfilterd.add(cs);
                            }
                        }
                    }
                    loadFilteredList(digitalAssetsMasterListfilterd);
                }
            }else{
                loadFilteredList(chkModelList);
            }
        }catch (Exception e){
            Log.e("Exception",e.toString());
        }

    }

    public void showApplyButton(){
        if(catFiltered || tagfiltered || mCheckCompleted.isChecked() || mCheckYetToStart.isChecked() || mCheckExpired.isChecked()){
            applyfilters.setVisibility(View.VISIBLE);
            applyfilters.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            applyfilters.setEnabled(true);
        }else{
            applyfilters.setBackgroundColor(Color.parseColor(Constants.GREY_COLOR));
            applyfilters.setEnabled(false);
        }
    }

    public void toggleselection(int num){
        cattagmenus.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        if(num == 1){
            filterheadingtext.setText(getResources().getString(R.string.category));
            catselection.setBackgroundColor(Color.WHITE);
            icon_cats.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
            icon_tags.setColorFilter(Color.WHITE);
            icon_filter.setColorFilter(Color.WHITE);
            tagselection.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
            filterlay.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        }else if(num == 2){
            filterheadingtext.setText(getResources().getString(R.string.Tags));
            tagselection.setBackgroundColor(Color.WHITE);
            icon_tags.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
            icon_cats.setColorFilter(Color.WHITE);
            icon_filter.setColorFilter(Color.WHITE);
            filterlay.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
            catselection.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        }else if(num == 3) {
            emptytagsview.setVisibility(View.GONE);
            filterheadingtext.setText(getResources().getString(R.string.status));
            filterlay.setBackgroundColor(Color.WHITE);
            icon_filter.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
            icon_tags.setColorFilter(Color.WHITE);
            icon_cats.setColorFilter(Color.WHITE);
            catselection.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
            tagselection.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        }
    }

    public void showhideemptystate(boolean showempty,String message,int type){
        if(showempty){
            checklist_empty_view.setVisibility(View.VISIBLE);
            if(type == Constants.INTERNETERRORNUM){
                emptyimage.setVisibility(View.VISIBLE);
                emptyimage.setImageResource(R.drawable.interenet_error_image);
                retrybutton.setVisibility(View.VISIBLE);
            }else if(type == Constants.NORESULTSNUM){
                emptyimage.setVisibility(View.VISIBLE);
                emptyimage.setImageResource(R.drawable.no_results);
                retrybutton.setVisibility(View.GONE);
            }else if(type == Constants.EMPTYLISTNUM){
                emptyimage.setVisibility(View.GONE);
                retrybutton.setVisibility(View.GONE);
            }
            empty_message.setText(message.toString());
        }else{
            checklist_empty_view.setVisibility(View.GONE);
        }
    }
}
