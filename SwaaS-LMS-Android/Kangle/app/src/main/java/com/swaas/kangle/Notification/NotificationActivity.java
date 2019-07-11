package com.swaas.kangle.Notification;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.swaas.kangle.API.model.LandingPageAccess;
import com.swaas.kangle.AssetListActivity;
import com.swaas.kangle.CheckList.ChecklistLandingActivity;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.LPCourse.CourseListActivity;
import com.swaas.kangle.R;
import com.swaas.kangle.TaskModule.TaskListActivity;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.CommonUtils;
import com.swaas.kangle.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    Context mContext;
    LinearLayout header;
    View content_view;
    ImageView companylogo;
    TextView nottitle;
    EmptyRecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView filter;

    NotificationListAdapter notificationListAdapter;
    ArrayList<NotificationModel> notificationModelList;
    NotificationModel notificationModel;

    SearchView msearchtext;
    SearchManager searchManager;
    List<NotificationModel> msearchcourse;
    LinearLayout searchlayout;
    ImageView icon_search,closesearch;

    RelativeLayout filtersection;
    ImageView close_filter,mCourseFilter,icon_cats,icon_duration,icon_filter;
    View cattagmenus;
    TextView filterheadingtext,filtered_text,clear_assetfilter,cat_filtered_count,tags_filtered_count,mFilteredCountStatus,
            emptytagsview,clearfilters,applyfilters;
    View catselection,duration,assetfilterheading,checkAsset,checkCourse,
            checkChecklist,checkTask;

    RelativeLayout check_box,radio_box;
    RadioGroup radiogroup;
    RadioButton oneweek,onemonth,threemonth,all;
    CheckBox mAsset,mCourse,mChecklist,mTask;
    boolean isFilterenabled = false;
    int checkEnabledTab = 1;
    ImageView tickfilter;
    View clear_assetfilter_img;
    TextView emptytext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mContext = NotificationActivity.this;
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        initializeView();
        setUpRecyclerView();
        setthemeforView();
        onClickListeners();

        getNotifications();
        showApplyButton();
    }

    public void initializeView(){
        header = (LinearLayout) findViewById(R.id.header);
        content_view = findViewById(R.id.content_view);
        companylogo = (ImageView) findViewById(R.id.companylogo);
        nottitle = (TextView) findViewById(R.id.nottitle);
        recyclerView = (EmptyRecyclerView)findViewById(R.id.recyclerView);
        filter = (ImageView) findViewById(R.id.icon_expandslider);
        icon_search = (ImageView) findViewById(R.id.icon_search);

        searchlayout = (LinearLayout) findViewById(R.id.searchlayout);
        msearchtext = (SearchView) findViewById(R.id.searchtext);
        closesearch = (ImageView) findViewById(R.id.closesearch);
        msearchtext = (SearchView) findViewById(R.id.searchtext);
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        filtersection = (RelativeLayout) findViewById(R.id.filtersection);
        close_filter = (ImageView) findViewById(R.id.close_filter);
        catselection = findViewById(R.id.catselection);
        duration = findViewById(R.id.duration);

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

        checkAsset = findViewById(R.id.checkAsset);
        checkCourse = findViewById(R.id.checkCourse);
        checkChecklist = findViewById(R.id.checkChecklist);
        checkTask = findViewById(R.id.checkTask);

        oneweek = (RadioButton) findViewById(R.id.oneweek);
        onemonth = (RadioButton) findViewById(R.id.onemonth);
        threemonth = (RadioButton) findViewById(R.id.threemonth);
        all = (RadioButton) findViewById(R.id.all);

        mAsset = (CheckBox) findViewById(R.id.check_Asset);
        mCourse = (CheckBox) findViewById(R.id.check_Course);
        mChecklist = (CheckBox) findViewById(R.id.check_Checklist);
        mTask = (CheckBox) findViewById(R.id.check_Task);

        check_box = (RelativeLayout) findViewById(R.id.check_box);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        radio_box = (RelativeLayout) findViewById(R.id.radio_box);

        filterheadingtext = (TextView)findViewById(R.id.filterheadingtext);
        cattagmenus = findViewById(R.id.cattagmenus);
        icon_cats = (ImageView)findViewById(R.id.icon_cats);
        icon_duration = (ImageView)findViewById(R.id.icon_duration);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        tickfilter = (ImageView) findViewById(R.id.tickfilter);

        clear_assetfilter_img = findViewById(R.id.clear_assetfilter_img);
        emptytext = (TextView) findViewById(R.id.emptytext);
        if(PreferenceUtils.getLandingPageAccess(mContext) != null) {
            Gson gsonget = new Gson();
            LandingPageAccess landingobj = gsonget.fromJson(PreferenceUtils.getLandingPageAccess(mContext), LandingPageAccess.class);
            if (landingobj != null) {
                if (!TextUtils.isEmpty(landingobj.getTask()) && landingobj.getTask().equalsIgnoreCase("Y")) {
                    checkTask.setVisibility(View.VISIBLE);
                }
                else
                {
                    checkTask.setVisibility(View.GONE);
                }
            }
        }
    }

    public void setUpRecyclerView(){
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void setthemeforView(){
        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        content_view.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        nottitle.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        filter.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        closesearch.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        closesearch.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        icon_search.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        emptytext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        filterheadingtext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        close_filter.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        companylogo.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        mAsset.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        mCourse.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        mChecklist.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        mTask.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));

        oneweek.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        onemonth.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        threemonth.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        all.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
    }

    public void onClickListeners(){
        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        closesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchlayout.setVisibility(View.GONE);
                nottitle.setVisibility(View.VISIBLE);
            }
        });

        icon_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notificationModelList!=null&&notificationModelList.size()>0) {
                    searchlayout.setVisibility(View.VISIBLE);
                    nottitle.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(mContext,mContext.getResources().getString(R.string.no_result),Toast.LENGTH_SHORT).show();
                }
            }
        });

        msearchtext.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));

        msearchtext.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchlayout.setVisibility(View.GONE);
                nottitle.setVisibility(View.VISIBLE);
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotifications();
                assetfilterheading.setVisibility(View.GONE);
                isFilterenabled = false;// your code
                if(searchlayout.isShown()) {
                    searchlayout.setVisibility(View.GONE);
                    nottitle.setVisibility(View.VISIBLE);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtersection.setVisibility(View.VISIBLE);
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
                    filtersection.setVisibility(View.GONE);
                    assetfilterheading.setVisibility(View.VISIBLE);
                    String htmlString="<u>"+getResources().getString(R.string.clearfilter)+"</u>";
                    filtered_text.setText(Html.fromHtml(htmlString));
                    isFilterenabled = true;
                    if(notificationModelList!=null&&notificationModelList.size()>0){
                        loadfilteredlist();
                    }
                    else
                    {
                        Toast.makeText(mContext,mContext.getResources().getString(R.string.no_result),Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });

        catselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCategorySlider();
            }
        });

        duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loaddurationSlider();
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
                }
                return true;
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
                    showApplyButton();
                }
            }
        });

        filtered_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtersection.setVisibility(View.GONE);
                assetfilterheading.setVisibility(View.GONE);
                isFilterenabled = false;
                clearfiltersFunction();
                getNotifications();
            }
        });

        clear_assetfilter_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtersection.setVisibility(View.VISIBLE);
            }
        });

        checkboxclicks();
        radioclicks();
        togglefilterselection(1);
    }

    public void checkboxclicks(){

        mAsset.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showApplyButton();
            }
        });

        mCourse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showApplyButton();
            }
        });

        mChecklist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showApplyButton();
            }
        });
        mTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showApplyButton();
            }
        });

        checkAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAsset.isChecked()){
                    mAsset.setChecked(false);
                }else{
                    mAsset.setChecked(true);
                }
                showApplyButton();
            }
        });

        checkCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCourse.isChecked()){
                    mCourse.setChecked(false);
                }else{
                    mCourse.setChecked(true);
                }
                showApplyButton();
            }
        });

        checkChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mChecklist.isChecked()){
                    mChecklist.setChecked(false);
                }else{
                    mChecklist.setChecked(true);
                }
                showApplyButton();
            }
        });

        checkTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTask.isChecked()){
                    mTask.setChecked(false);
                }else{
                    mTask.setChecked(true);
                }
                showApplyButton();
            }
        });
    }

    public void radioclicks(){
        all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showApplyButton();
            }
        });

        oneweek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showApplyButton();
            }
        });
        onemonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showApplyButton();
            }
        });
        threemonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showApplyButton();
            }
        });
    }

    public void loadCategorySlider(){
        togglefilterselection(1);
        check_box.setVisibility(View.VISIBLE);
        radio_box.setVisibility(View.GONE);
    }

    public void loaddurationSlider(){
        togglefilterselection(2);
        check_box.setVisibility(View.GONE);
        radio_box.setVisibility(View.VISIBLE);
    }

    public void clearfiltersFunction(){
        mAsset.setChecked(false);
        mChecklist.setChecked(false);
        mCourse.setChecked(false);
        mTask.setChecked(false);
        oneweek.setChecked(false);
        onemonth.setChecked(false);
        threemonth.setChecked(false);
        all.setChecked(true);
        togglefilterselection(1);
        check_box.setVisibility(View.VISIBLE);
        radio_box.setVisibility(View.GONE);
    }


    public void showApplyButton(){
        if(mAsset.isChecked() || mCourse.isChecked()  || mChecklist.isChecked() || mTask.isChecked() ||
                all.isChecked() || oneweek.isChecked() || threemonth.isChecked() || onemonth.isChecked()){
            applyfilters.setVisibility(View.VISIBLE);
            applyfilters.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
            applyfilters.setEnabled(true);
        }else{
            applyfilters.setBackgroundColor(Color.parseColor(Constants.GREY_COLOR));
            applyfilters.setEnabled(false);
        }
    }

    public void togglefilterselection(int num){
        cattagmenus.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        if(num == 1){
            filterheadingtext.setText(getResources().getString(R.string.category));
            catselection.setBackgroundColor(Color.WHITE);
            icon_cats.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
            icon_duration.setColorFilter(Color.WHITE);
            duration.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));

        }else if(num == 2){
            filterheadingtext.setText(getResources().getString(R.string.Duration));
            duration.setBackgroundColor(Color.WHITE);
            icon_duration.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
            icon_cats.setColorFilter(Color.WHITE);
            catselection.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        }
    }


    public void getNotifications(){

        String subdomainName = PreferenceUtils.getSubdomainName(this);
        int CompanyId = PreferenceUtils.getCompnayId(this);
        int UserId = PreferenceUtils.getUserId(this);
        String offsetFromUtc = CommonUtils.getUtcOffset();

        NotificationTempRepository notificationTempRepository = new NotificationTempRepository(mContext);
        notificationTempRepository.setGetNotificationDataCBListner(new NotificationTempRepository.GetNotificationDataCBListner() {
            @Override
            public void GetNotificationDataSuccessCB(ArrayList<NotificationModel> notificationslist) {
                swipeRefreshLayout.setRefreshing(false);
                ArrayList<NotificationModel> notificationModels = notificationslist;
                if (notificationModels != null && notificationModels.size() > 0) {
                    notificationModelList = notificationModels;
                    try {
                        groupdata(notificationModelList);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //loadData();
                } else {

                }
            }

            @Override
            public void GetNotificationDataFailureCB(String message) {

            }
        });

        notificationTempRepository.getNotificationDataFromAPI(subdomainName,CompanyId,UserId,"A",offsetFromUtc);
    }

    public void groupdata(ArrayList<NotificationModel> nlist) throws ParseException {
        ArrayList<NotificationModel> lst = new ArrayList<>();
        for(NotificationModel not : nlist){
            String onlydate = not.getApp_Message_Date();
            int t = onlydate.indexOf("T");
            String nd = onlydate.substring(0,t);
            not.setOnlyDate(nd);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = (Date)formatter.parse(nd);
            not.setDate(date);
            lst.add(not);
        }

        /*Collections.sort(nlist, new Comparator<NotificationModel>() {
                @Override
                public int compare(NotificationModel lhs, NotificationModel rhs) {
                    return lhs.getDate().compareTo(rhs.getDate());
                }
            });*/
        if(lst.size() > 0){
            recyclerView.setVisibility(View.VISIBLE);
            emptytext.setVisibility(View.GONE);
            notificationListAdapter = new NotificationListAdapter(mContext, lst);
            recyclerView.setAdapter(notificationListAdapter);
            notificationListAdapter.notifyDataSetChanged();
        }else{
            recyclerView.setVisibility(View.GONE);
            emptytext.setVisibility(View.VISIBLE);
        }
        loadAdapterClick();
    }

    public void loadAdapterClick(){
        notificationListAdapter.setOnItemClickListener(new NotificationListAdapter.MyClickListener() {
            @Override
            public void onItemClick(NotificationModel nModel) {
                Intent i;
                if(nModel.getCategoryId() == Constants.Notification_Task){
                    i = new Intent(mContext,TaskListActivity.class);
                }else if(nModel.getCategoryId() == Constants.Notification_Course){
                    i = new Intent(mContext,CourseListActivity.class);
                }else if(nModel.getCategoryId() == Constants.Notification_Checklist){
                    i = new Intent(mContext,ChecklistLandingActivity.class);
                } else {
                    i = new Intent(mContext,AssetListActivity.class);
                }
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
    }

    public void loadData() {
        notificationListAdapter = new NotificationListAdapter(mContext, notificationModelList);
        recyclerView.setAdapter(notificationListAdapter);
    }


    public void loadSearchdata(String newText){
        msearchcourse = new ArrayList<>();
        msearchcourse = notificationModelList;
        ArrayList<NotificationModel> filteredList = new ArrayList<>();
        if(msearchcourse.size() > 0) {
            for (NotificationModel row : msearchcourse) {
                String name = row.getMessage_text().toLowerCase();
                if (name.contains(newText.toLowerCase())) {
                    filteredList.add(row);
                }
            }
            if (filteredList.size() > 0) {
                try {
                    groupdata(filteredList);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {

            }
        }else{

        }
    }

    public void loadfilteredlist(){
        ArrayList<NotificationModel> filtered = new ArrayList<>();
        boolean asset,checklist,course,task;
        asset = mAsset.isChecked();
        checklist = mChecklist.isChecked();
        course = mCourse.isChecked();
        task = mTask.isChecked();
        if(!asset && !checklist && !course && !task ){
            filtered = notificationModelList;
        }else{
            if(asset){
                for(NotificationModel n : notificationModelList) {
                    if (n.getCategoryId() == Constants.Notification_Asset) {
                        filtered.add(n);
                    }
                }
            }
            if(checklist){
                for(NotificationModel n : notificationModelList) {
                    if (n.getCategoryId() == Constants.Notification_Checklist) {
                        filtered.add(n);
                    }
                }
            }
            if(course){
                for(NotificationModel n : notificationModelList) {
                    if (n.getCategoryId() == Constants.Notification_Course) {
                        filtered.add(n);
                    }
                }
            }
            if(task){
                for(NotificationModel n : notificationModelList) {
                    if (n.getCategoryId() == Constants.Notification_Task) {
                        filtered.add(n);
                    }
                }
            }
        }
        gotodatefilter(filtered);
    }

    public void gotodatefilter(ArrayList<NotificationModel> filtered){
        ArrayList<NotificationModel> finallist = new ArrayList<>();
        boolean week,onemnth,threemnth,al;
        week = oneweek.isChecked();
        onemnth = onemonth.isChecked();
        threemnth = threemonth.isChecked();
        al = all.isChecked();
        if(week){
            finallist = calculatedata(filtered,7);
        }else if(onemnth){
            finallist = calculatedata(filtered,30);
        }else if(threemnth){
            finallist = calculatedata(filtered,90);
        } else if(al){
            finallist = filtered;
        }

        try {
            groupdata(finallist);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<NotificationModel> calculatedata(ArrayList<NotificationModel> flist,int num){
        ArrayList<NotificationModel> finallist = new ArrayList<>();
        Date currentDate = getcurrentDate();
        for(NotificationModel lst : flist){
            int diffInDays = (int) ((currentDate.getTime() - lst.getDate().getTime()) / (1000 * 60 * 60 * 24));
            if(diffInDays <= num){
                finallist.add(lst);
            }
        }
        return finallist;
    }

    public Date getcurrentDate(){
        Date now = new Date();
        return now;
    }

}
