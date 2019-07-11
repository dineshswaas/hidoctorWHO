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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.swaas.kangle.CheckList.adapter.CourseCheckListCategoryListTextRecyclerAdapter;
import com.swaas.kangle.CheckList.adapter.CourseCheckListTagsListTextRecyclerAdapter;
import com.swaas.kangle.CheckList.adapter.CourseChecklistItemAdapter;
import com.swaas.kangle.CheckList.model.CheckListModel;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.R;
import com.swaas.kangle.db.CheckListTempRepository;
import com.swaas.kangle.db.Filters.ChecklistCatTagFilterRepository;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.CommonUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class CourseChecklistListActivity extends AppCompatActivity {

    Context mContext;
    CourseChecklistItemAdapter courseChecklistItemAdapter;
    EmptyRecyclerView checklistrecyclerView;
    EmptyRecyclerView cattag_recyclerView;

    CheckListTempRepository checkListTempRepository;

    View catselection,tagselection;
    TextView cat_filtered_count,tags_filtered_count;
    TextView clear_assetfilter,filtered_text;
    View assetfilterheading;
    TextView emptytagsview;
    ImageView close_filter;
    ImageView icon_cats,icon_tags;
    ImageView tickfilter,emptyimage,expandslider;
    TextView filterheadingtext,empty_message,retrybutton;
    View cattagmenus;

    boolean tagfiltered,catFiltered;
    List<String> tagslist,catlist;
    String TAGS = "",CATS = "";
    ImageView mCourseFilter;

    View clear_assetfilter_img,checklist_empty_view;
    CourseCheckListCategoryListTextRecyclerAdapter categoryListTextRecyclerAdapter;
    CourseCheckListTagsListTextRecyclerAdapter tagsListTextRecyclerAdapter;
    boolean isFilterenabled = false;
    List<CheckListModel> CategoryLists;
    List<CheckListModel> TagsLists;
    ChecklistCatTagFilterRepository checklistCatTagFilterRepository;
    LinearLayoutManager coursechklstLLM,linearLayoutManager;
    List<CheckListModel> digitalAssetsMasterListfilterd;

    int AssignmentId, groupId;
    String PublishDate;
    ProgressDialog mProgressDialog;

    View course_chklst_empty_view,mainnestedView;
    RelativeLayout searchlayout;

    List<CheckListModel> chkModelList = new ArrayList<CheckListModel>();
    ImageView companylogo;
    TextView typeoflist,clearfilters,applyfilters;
    LinearLayout header;
    RelativeLayout filtersection,filterheading;
    SearchView msearchtext;
    SearchManager searchManager;
    ImageView icon_search,closesearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_checklist_list);

        mContext = CourseChecklistListActivity.this;
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        initializeView();
        setUpRecyclerView();

        tagslist = new ArrayList<>();
        catlist = new ArrayList<>();
        setthemeforView();
        typeoflist.setText(getResources().getString(R.string.course_checklist));
        checkListTempRepository = new CheckListTempRepository(mContext);
        checklistCatTagFilterRepository = new ChecklistCatTagFilterRepository(mContext);
        getListOfCheckList();
        onClickListeners();
    }

    public void initializeView(){
        companylogo = (ImageView) findViewById(R.id.companylogo);
        expandslider = (ImageView) findViewById(R.id.icon_expandslider);
        mainnestedView = findViewById(R.id.mainnestedView);

        typeoflist = (TextView) findViewById(R.id.typeoflist);
        checklistrecyclerView = (EmptyRecyclerView) findViewById(R.id.checklistrecyclerView);
        course_chklst_empty_view = findViewById(R.id.course_chklst_empty_view);

        header = (LinearLayout) findViewById(R.id.header);

        cattag_recyclerView = (EmptyRecyclerView) findViewById(R.id.cattag_recyclerView);
        filtersection = (RelativeLayout) findViewById(R.id.filtersection);
        close_filter = (ImageView) findViewById(R.id.close_filter);
        catselection = findViewById(R.id.catselection);
        tagselection = findViewById(R.id.tagselection);

        clearfilters = (TextView) findViewById(R.id.clearfilters);
        applyfilters = (TextView) findViewById(R.id.applyfilters);

        assetfilterheading =  findViewById(R.id.assetfilterheading);
        filtered_text = (TextView) findViewById(R.id.filtered_text);
        clear_assetfilter = (TextView) findViewById(R.id.clear_assetfilter);
        mCourseFilter = (ImageView)findViewById(R.id.icon_filter);
        cat_filtered_count = (TextView) findViewById(R.id.cat_filtered_count);
        tags_filtered_count = (TextView) findViewById(R.id.tags_filtered_count);

        emptytagsview = (TextView)findViewById(R.id.emptytagsview);
        checklist_empty_view = findViewById(R.id.checklist_empty_view);
        clear_assetfilter_img = findViewById(R.id.clear_assetfilter_img);

        filterheadingtext = (TextView)findViewById(R.id.filterheadingtext);
        cattagmenus = findViewById(R.id.cattagmenus);
        icon_cats = (ImageView)findViewById(R.id.icon_cats);
        icon_tags = (ImageView)findViewById(R.id.icon_tags);

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
        coursechklstLLM = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        if(getResources().getBoolean(R.bool.portrait_only)){
            checklistrecyclerView.setLayoutManager(coursechklstLLM);
        }else{
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                GridLayoutManager grid = new GridLayoutManager(this,3);
                checklistrecyclerView.setLayoutManager(grid);
            }else{
                GridLayoutManager grid = new GridLayoutManager(this,2);
                checklistrecyclerView.setLayoutManager(grid);
            }
        }
        cattag_recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void setthemeforView(){

        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        companylogo.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        mainnestedView.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));

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
        typeoflist.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
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
        List<CheckListModel> fullmodel = checkListTempRepository.getAllData();
        if(!isFilterenabled) {
            getLocalCourseChecklistData(fullmodel);
        }else{
            getCoursesbyIDs();
        }
    }

    public void getLocalCourseChecklistData(List<CheckListModel> list){
        chkModelList = list;
        loadCourseChecklist(chkModelList);
    }

    public void loadCourseChecklist(List<CheckListModel> chkModelList){
        dismissProgressDialog();
        List<CheckListModel> onetimechkModelList = new ArrayList<CheckListModel>();
        for (CheckListModel chkclst:chkModelList){
            if(chkclst.getChecklist_Classification() == 2) {
                onetimechkModelList.add(chkclst);
            }
        }
        courseChecklistItemAdapter = new CourseChecklistItemAdapter(mContext,onetimechkModelList);
        if(onetimechkModelList.size()>0){
            checklistrecyclerView.setAdapter(courseChecklistItemAdapter);
            checklistrecyclerView.setVisibility(View.VISIBLE);
            course_chklst_empty_view.setVisibility(View.GONE);
        }else{
            checklistrecyclerView.setVisibility(View.GONE);
            course_chklst_empty_view.setVisibility(View.VISIBLE);
        }
        loadCourseChecklistAdapterClick();
    }

    public void loadCourseChecklistAdapterClick(){
        courseChecklistItemAdapter.setOnItemClickListener(new CourseChecklistItemAdapter.MyClickListener() {
            @Override
            public void onItemClick(int Id, int checklistStatus, int CUAId) {
                if(NetworkUtils.isNetworkAvailable()) {
                    for(CheckListModel chk:chkModelList){
                        if(chk.getChecklist_Id() == Id){
                            AssignmentId = chk.getChecklist_User_Assignment_Id();
                            PublishDate = chk.getPublish_Date_String();
                            groupId = chk.getCheckList_Publish_Group_Id();
                            int checkListGroupId = chk.getChecklist_Type_ID();
                            getListOfUsers(Id,chk.getChecklist_Name(),CUAId,PublishDate,groupId,chk.getChecklist_Description(), checkListGroupId);
                        }
                    }
                }else{
                    Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getListOfUsers(final int ChecklistId, final String chklistname, final int CUAId,
                               final String PublishDate, final int chklstgroupid, final String chklstdesc, int checkListTypeId){

        if(NetworkUtils.isNetworkAvailable()){
            Intent sectionQuestionActivity = new Intent(mContext, CourseChecklistUsersActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("ChecklistId",ChecklistId);
            bundle.putSerializable("name",chklistname);
            bundle.putSerializable("CUAId",CUAId);
            bundle.putSerializable("PublishDate",PublishDate);
            bundle.putSerializable("ChecklistGroupId",chklstgroupid);
            bundle.putSerializable("chklstdesc",chklstdesc);
            bundle.putSerializable("CheckListTypeId", checkListTypeId);
            sectionQuestionActivity.putExtras(bundle);
            startActivity(sectionQuestionActivity);

        }else{
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
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
                getLocalData();
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
                getLocalData();
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

        expandslider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtersection.setVisibility(View.VISIBLE);
                catlist.clear();
                tagslist.clear();
                CategoryLists = checklistCatTagFilterRepository.getAllCoursechecklistCategory(CATS);
                TagsLists = checklistCatTagFilterRepository.getAllCoursechecklistTags(TAGS);
                cattag_recyclerView.setVisibility(View.VISIBLE);
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
            }
        });

        tagselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTagsSlider();
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

    public void loadSearchdata(String newText){
        List<CheckListModel> msearchcourse = new ArrayList<>();
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
                getLocalCourseChecklistData(filteredList);
            } else {
                checklistrecyclerView.setVisibility(View.GONE);
                showhideemptystate(true,getResources().getString(R.string.no_result),Constants.NORESULTSNUM);
            }
        }else{
            checklistrecyclerView.setVisibility(View.GONE);
            showhideemptystate(true,getResources().getString(R.string.no_result),Constants.NORESULTSNUM);
        }
    }

    public void clearfiltersFunction(){
        catlist.clear();
        tagslist.clear();
        CATS = "";TAGS = "";
        catFiltered = false; tagfiltered = false;
        showApplyButton();
        isFilterenabled = false;
        CategoryLists = checklistCatTagFilterRepository.getAllCoursechecklistCategory(CATS);
        TagsLists = checklistCatTagFilterRepository.getAllCoursechecklistTags(TAGS);
        categoryListTextRecyclerAdapter = new CourseCheckListCategoryListTextRecyclerAdapter(mContext, CategoryLists);
        categoryListTextRecyclerAdapter.notifyDataSetChanged();
        tagsListTextRecyclerAdapter = new CourseCheckListTagsListTextRecyclerAdapter(mContext,TagsLists);
        tagsListTextRecyclerAdapter.notifyDataSetChanged();
        catselection.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        tagselection.setBackgroundColor(Color.parseColor(Constants.OPAQUE_COLOR));
        cattag_recyclerView.setVisibility(View.VISIBLE);
        cattag_recyclerView.setAdapter(categoryListTextRecyclerAdapter);
    }







    //Flter Function
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
                    selectQuery += "tbl_CHECKLIST_CAT_TAG_MASTER.Tags in ("+TAGS+") ";
                }
            }
            selectQuery += " Group by Checklist_Id";
            List<CheckListModel> DAIDlist = checklistCatTagFilterRepository.getAssetIdbySelectedCatTag(selectQuery);
            filteredlist(DAIDlist);
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

    private void loadFilteredList(List<CheckListModel> chk) {
        isFilterenabled = true;
        getLocalCourseChecklistData(chk);
    }

    public void loadCategorySlider(){
        try{
            togglefilterselection(1);
            String valuenames = "";
            if(tagslist.size() == 0){
                List<CheckListModel> digitalAssetsMasterCategoryList = checklistCatTagFilterRepository.getAllCoursechecklistCategory(CATS);
                reloadCatSlider(digitalAssetsMasterCategoryList);
            }else{
                if(tagslist.size()>0){
                    String tagnames = "";
                    for (String catname : tagslist) {
                        tagnames =  tagnames + "," + "'" + catname.toString() + "'";
                    }
                    StringBuilder sb = new StringBuilder(tagnames);
                    sb.deleteCharAt(0);
                    valuenames = sb.toString().replace("''", "'");
                }
                String selectQuery = "select tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name FROM tbl_CHECKLIST_CAT_TAG_MASTER ";
                if (tagslist.size() > 0) {
                    selectQuery += " WHERE ";
                    if (tagslist.size() > 0) {
                        selectQuery += " tbl_CHECKLIST_CAT_TAG_MASTER.Tags in ("+valuenames+") ";
                    }
                }
                selectQuery += " AND tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name not in ("+CATS+") Group By tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name ";
                List<CheckListModel> digitalAssetsMasterCategoryList = checklistCatTagFilterRepository.getCCCategorybySelectedTags(selectQuery);
                reloadCatSlider(digitalAssetsMasterCategoryList);
            }
        }catch (Exception e){

        }
    }

    public void loadTagsSlider(){
        try{
            togglefilterselection(2);
            String valuenames = "";
            if(catlist.size() == 0){
                List<CheckListModel> digitalAssetsMasterTagsList = checklistCatTagFilterRepository.getAllCoursechecklistTags(TAGS);
                reloadTagSlider(digitalAssetsMasterTagsList);
            }else{
                if(catlist.size()>0){
                    String catnames = "";
                    for (String catname : catlist) {
                        catnames = catnames + "," + "'" + catname.toString() + "'";
                    }
                    StringBuilder sb = new StringBuilder(catnames);
                    sb.deleteCharAt(0);
                    valuenames = sb.toString().replace("''", "'");
                }
                String selectQuery = "select tbl_CHECKLIST_CAT_TAG_MASTER.Tags FROM tbl_CHECKLIST_CAT_TAG_MASTER ";
                if (catlist.size() > 0) {
                    selectQuery += " WHERE ";
                    if (catlist.size() > 0) {
                        selectQuery += " tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name in ("+valuenames+") ";
                    }
                }
                selectQuery += " AND tbl_CHECKLIST_CAT_TAG_MASTER.Tags not null AND tbl_CHECKLIST_CAT_TAG_MASTER.Tags not in ("+TAGS+") Group By tbl_CHECKLIST_CAT_TAG_MASTER.Tags ";
                List<CheckListModel> digitalAssetsMasterTagsList = checklistCatTagFilterRepository.getCCTagsbySelectedcategorey(selectQuery);
                reloadTagSlider(digitalAssetsMasterTagsList);
            }
        }catch (Exception e){

        }

    }

    public void reloadCatSlider(List<CheckListModel> newmodel){
        List<CheckListModel> catrefresh = new ArrayList<>();
        List<CheckListModel> newrefreshed = new ArrayList<>();
        for(CheckListModel existing : CategoryLists){
            if(existing.iscatchecked){
                catrefresh.add(existing);
            }
        }
        for(CheckListModel ne : newmodel){
            catrefresh.add(ne);
        }
        emptytagsview.setVisibility(View.GONE);
        cattag_recyclerView.setVisibility(View.VISIBLE);
        CategoryLists = catrefresh;
        categoryListTextRecyclerAdapter = new CourseCheckListCategoryListTextRecyclerAdapter(mContext, CategoryLists);
        cattag_recyclerView.setAdapter(categoryListTextRecyclerAdapter);
    }

    public void reloadTagSlider(List<CheckListModel> newmodel){
        List<CheckListModel> tagrefresh = new ArrayList<>();
        for(CheckListModel existing : TagsLists){
            if(existing.istagchecked){
                tagrefresh.add(existing);
            }
        }
        for(CheckListModel ne : newmodel){
            tagrefresh.add(ne);
        }
        TagsLists = tagrefresh;
        showtagsView(TagsLists,true);
    }

    public void showtagsView(List<CheckListModel> digitalAssetsMasterTagsList,boolean status){
        if(digitalAssetsMasterTagsList.size() > 0){
            emptytagsview.setVisibility(View.GONE);
            cattag_recyclerView.setVisibility(View.VISIBLE);
            tagsListTextRecyclerAdapter = new CourseCheckListTagsListTextRecyclerAdapter(mContext,digitalAssetsMasterTagsList);
            cattag_recyclerView.setAdapter(tagsListTextRecyclerAdapter);
        }else{
            cattag_recyclerView.setVisibility(View.GONE);
            emptytagsview.setVisibility(View.VISIBLE);
        }
    }

    public void togglefilterselection(int num){
        cattagmenus.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        if(num == 1){
            filterheadingtext.setText(getResources().getString(R.string.category));
            catselection.setBackgroundColor(Color.WHITE);
            icon_cats.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
            icon_tags.setColorFilter(Color.WHITE);
            tagselection.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));

        }else if(num == 2){
            filterheadingtext.setText(getResources().getString(R.string.Tags));
            tagselection.setBackgroundColor(Color.WHITE);
            icon_tags.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
            icon_cats.setColorFilter(Color.WHITE);
            catselection.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        }
    }

    public void showApplyButton(){
        if(catFiltered || tagfiltered){
            applyfilters.setVisibility(View.VISIBLE);
            applyfilters.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            applyfilters.setEnabled(true);
        }else{
            applyfilters.setBackgroundColor(Color.parseColor(Constants.GREY_COLOR));
            applyfilters.setEnabled(false);
        }
    }

    public void showhideemptystate(boolean showempty,String message,int type){
        if(showempty){

            if(checklist_empty_view != null)
            {
                checklist_empty_view.setVisibility(View.VISIBLE);
            }

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
