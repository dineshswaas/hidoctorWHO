package com.swaas.kangle;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.swaas.kangle.API.model.LandingPageAccess;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.API.service.AssetService;
import com.swaas.kangle.CheckList.ChecklistLandingActivity;
import com.swaas.kangle.LPCourse.CourseListActivity;
import com.swaas.kangle.Notification.NotificationActivity;
import com.swaas.kangle.Notification.NotificationModel;
import com.swaas.kangle.Notification.NotificationTempRepository;
import com.swaas.kangle.TaskModule.TaskListActivity;
import com.swaas.kangle.UploadService.DigitalAssetAnalyticsUpSyncService;
import com.swaas.kangle.adapter.AssetListItemRecyclerAdapter;
import com.swaas.kangle.adapter.CategoryListRecyclerAdapter;
import com.swaas.kangle.adapter.CategoryListTextRecyclerAdapter;
import com.swaas.kangle.adapter.DocTypeListTextRecycerAdapter;
import com.swaas.kangle.adapter.TagsListTextRecyclerAdapter;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.db.DigitalAssetOfflineRepository;
import com.swaas.kangle.db.DigitalAssetTransactionRepository;
import com.swaas.kangle.db.Filters.DigitalassetCatTagFilterRepository;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.playerPart.DigitalAssets;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.CommonUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.MessageDialog;
import com.swaas.kangle.utils.NetworkUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AssetListActivity extends AppCompatActivity implements LocationListener {

    ImageView companylogo,notification,settings,chatview;
    ImageView icon_close,icon_dashboard,icon_search,icon_expandslider,offlinemode;
    TextView allasset;
    EmptyRecyclerView recyclerView;
    View mEmptyView;
    AssetListItemRecyclerAdapter adapter;
    CategoryListRecyclerAdapter categoryListRecyclerAdapter;
    CategoryListTextRecyclerAdapter categoryListTextRecyclerAdapter;
    TagsListTextRecyclerAdapter tagsListTextRecyclerAdapter;
    DocTypeListTextRecycerAdapter docTypeListTextRecycerAdapter;
    LinearLayoutManager mLayoutmanager;
    //GridLayoutManager grid;
    Context mContext;
    ProgressDialog mProgressDialog;
    ArrayList<DigitalAssetsMaster> assetsForBrowses;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;
    DigitalAssetOfflineRepository digitalAssetOfflineRepository;
    boolean catViewVisible = false;
    MessageDialog messageDialog;
    boolean isoffline = false;
    RelativeLayout bottomheader;
    RelativeLayout activity_list;
    EditText searchededittext;
    TextView gobutton;
    LinearLayout searchlayout;
    boolean searchenabled = false;

    WebView helpView;
    View helplayout;
    TextView closehelp;
    public double latitude,longitude;
    UploadActivity uploadActivity;
    GridLayoutManager grid;

    RelativeLayout cat_tag_section;
    View cat_selected,tag_selected;
    LinearLayout categorey_filter,tags_filter;
    ImageView icon_closecattags;

    boolean isCatTagSectionShown = false;

    View lpcourse,assetpage,chklistpage,profilepage,bottommenusection,taskpage;
    LinearLayout bottommenus;
    ImageView pos1;
    TextView higlighttext;

    //new UI changes
    View header;

    //filter changes
    RelativeLayout filtersection;
    TextView clearfilters,applyfilters;
    EmptyRecyclerView cattag_recyclerView;
    ImageView close_filter;

    TextView clear_assetfilter,filtered_text;
    View assetfilterheading,catselection,tagselection,filterlay;

    DigitalassetCatTagFilterRepository digitalassetCatTagFilterRepository;
    DigitalAssetTransactionRepository transactionRepository;

    boolean tagfiltered,catFiltered,docFiltered;
    List<String> tagslist,catlist,filterlist;
    List<DigitalAssetsMaster> digitalAssetsMasterCategoryLists;
    List<DigitalAssetsMaster> digitalAssetsMasterTagsLists;
    List<DigitalAssetsMaster> digitalAssetsMasterDAtypeLists;
    String TAGS = "",CATS = "",FLTR = "";

    ImageView mCourseFilter;
    TextView cat_filtered_count,tags_filtered_count,mFilteredCountStatus,emptytagsview,
            filterheadingtext,retrybutton,emptymessage;
    View checklist_empty_view;

    View clear_assetfilter_img;

    boolean filtered;
    private static int firstVisibleInListview;

    View cattagmenus;
    ImageView icon_cats,icon_tags,icon_filter,closesearch;

    SearchView msearchtext;
    SearchManager searchManager;
    List<DigitalAssetsMaster> msearchassets;

    RelativeLayout filterheading;
    ImageView tickfilter,emptyimage;

    View notificationsec,chatviewsec;
    TextView notificationcount,chatcount;
    TextView course,asset,checklist,task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_list);

        mContext = AssetListActivity.this;
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(mContext);
        digitalAssetOfflineRepository = new DigitalAssetOfflineRepository(mContext);
        digitalassetCatTagFilterRepository = new DigitalassetCatTagFilterRepository(mContext);
        transactionRepository = new DigitalAssetTransactionRepository(mContext);

        uploadActivity = new UploadActivity(mContext);
        tagslist = new ArrayList<>();
        catlist = new ArrayList<>();
        filterlist = new ArrayList<>();
        // getSupportActionBar().hide();
        initializeView();
        onClickListeners();
        setUpRecyclerView();
        initializebottomnavigation();
        bottomnavigationonClickevents();
        setthemeforView();
        //getAssetsForBrowse();

        if(NetworkUtils.checkIfNetworkAvailable(mContext)){
            sendDigitalAssetAnalyticsToServer();
        }
        PreferenceUtils.setVisibleActivityName(mContext,"Asset");

        getnotificationcount();

        if(PreferenceUtils.getLandingPageAccess(mContext) != null) {
            Gson gsonget = new Gson();
            LandingPageAccess landingobj = gsonget.fromJson(PreferenceUtils.getLandingPageAccess(mContext), LandingPageAccess.class);
            if (landingobj != null) {
                if (!TextUtils.isEmpty(landingobj.getLibrary()) && landingobj.getLibrary().equalsIgnoreCase("Y")) {
                    notificationsec.setVisibility(View.VISIBLE);
                }
                else
                {
                    notificationsec.setVisibility(View.GONE);
                }
            }
        }
    }


    public void getnotificationcount(){
        String subdomainName = PreferenceUtils.getSubdomainName(this);
        int CompanyId = PreferenceUtils.getCompnayId(this);
        int UserId = PreferenceUtils.getUserId(this);

        NotificationTempRepository notificationTempRepository = new NotificationTempRepository(mContext);
        notificationTempRepository.setGetNotificationDataCBListner(new NotificationTempRepository.GetNotificationDataCBListner() {
            @Override
            public void GetNotificationDataSuccessCB(ArrayList<NotificationModel> customers) {
                if(customers != null && customers.size() > 0){
                    int num = 0;
                    /*for(NotificationModel n: customers){
                        if(n.getCategory_Id().equals("A")){
                            num = num + n.getCount();
                        }
                        if(n.getCategory_Id().equals("C")){
                            num = num + n.getCount();
                        }

                    }*/
                    num = (customers.get(0).getCount() + customers.get(1).getCount());
                    if(num > 0){
                        notificationcount.setVisibility(View.VISIBLE);
                        if(num > 99) {
                            notificationcount.setText("99+");
                        }else{
                            notificationcount.setText(num + "");
                        }
                    }else{
                        notificationcount.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void GetNotificationDataFailureCB(String message) {

            }
        });

        notificationTempRepository.getNotificationHubCountFromApi(subdomainName,CompanyId,UserId);
    }


    private void sendDigitalAssetAnalyticsToServer() {
        transactionRepository.setmGetDA(new DigitalAssetTransactionRepository.GetDA() {
            @Override
            public void getDASuccess(ArrayList<DigitalAssets> digitalAssetList) {
                if(digitalAssetList != null && digitalAssetList.size() > 0){
                    Intent analyticsIntent = new Intent(AssetListActivity.this,DigitalAssetAnalyticsUpSyncService.class);
                    startService(analyticsIntent);
                }
            }

            @Override
            public void getDAFailure(String message) {

            }
        });
        transactionRepository.getUnSyncedDigitalAssetAnalytics();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(searchenabled){
            searchlayout.setVisibility(View.GONE);
            notificationsec.setVisibility(View.VISIBLE);
            searchenabled=false;
        } else {
            super.onBackPressed();
        }
    }

    public void loadSearchdata(String newText){
        msearchassets = new ArrayList<DigitalAssetsMaster>();
        if(!isoffline){
            msearchassets = assetsForBrowses;
        }else{
            msearchassets = digitalAssetOfflineRepository.getAllOfflineAssets();
        }
        List<DigitalAssetsMaster> filteredList = new ArrayList<>();
        if(msearchassets!=null && msearchassets.size() > 0) {
            for (DigitalAssetsMaster row : msearchassets) {

                // name match condition. this might differ depending on your requirement
                // here we are looking for name or phone number match
                String name = row.getDAName().toLowerCase();
                if (name.contains(newText.toLowerCase())) {
                    filteredList.add(row);
                }
            }
            if (filteredList.size() > 0) {
                adapter = new AssetListItemRecyclerAdapter(AssetListActivity.this, filteredList, false);
                recyclerView.setVisibility(View.VISIBLE);
                showhideemptystate(false,"",0);
                recyclerView.setAdapter(adapter);
                recyclerView.setItemViewCacheSize(filteredList.size());

                adapter.notifyDataSetChanged();
            } else {
                recyclerView.setVisibility(View.GONE);
                showhideemptystate(true,getResources().getString(R.string.no_result),Constants.NORESULTSNUM);
            }
        }else{
            recyclerView.setVisibility(View.GONE);
            showhideemptystate(true,getResources().getString(R.string.no_result),Constants.NORESULTSNUM);
        }
    }

    public void onClickListeners(){

        retrybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isoffline = false;
                PreferenceUtils.setOfflineMode(mContext ,"false");
                getAssetsForBrowse();

            }
        });
        msearchtext.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));

        msearchtext.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchlayout.setVisibility(View.GONE);
                return false;
            }
        });

        closesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchlayout.setVisibility(View.GONE);
                notificationsec.setVisibility(View.VISIBLE);
                header.setVisibility(View.VISIBLE);
                getOnlineAssets();
            }
        });

        searchlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
            }
        });

        icon_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(catViewVisible){
                    getOnlineAssets();
                }
            }
        });

        icon_expandslider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtersection.setVisibility(View.VISIBLE);
                catlist.clear();
                tagslist.clear();
                filterlist.clear();
                CATS = "";TAGS = "";FLTR = "";
                digitalAssetsMasterCategoryLists = digitalassetCatTagFilterRepository.getAllCategory(CATS);
                digitalAssetsMasterTagsLists = digitalassetCatTagFilterRepository.getAllTags(TAGS);
                digitalAssetsMasterDAtypeLists = digitalassetCatTagFilterRepository.getAllDocTypes(FLTR);
                gotoCategoreyListView();
                bottommenusection.setVisibility(View.GONE);
            }
        });

        icon_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchlayout.setVisibility(View.VISIBLE);
                notificationsec.setVisibility(View.GONE);
                searchededittext.requestFocus();
                header.setVisibility(View.INVISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchededittext, InputMethodManager.SHOW_IMPLICIT);
                searchenabled = true;
                if(cat_tag_section.isShown()){
                    cat_tag_section.setVisibility(View.GONE);
                    isCatTagSectionShown = false;
                }
                /*Intent i = new Intent(mContext,SearchAssetActivity.class);
                startActivity(i);*/
            }
        });

        icon_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomheader.setVisibility(View.GONE);
                cat_tag_section.setVisibility(View.GONE);
                isCatTagSectionShown = false;
                setUpRecyclerView();
                getOnlineAssets();
            }
        });

        icon_closecattags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomheader.setVisibility(View.GONE);
                cat_tag_section.setVisibility(View.GONE);
                isCatTagSectionShown = false;
                setUpRecyclerView();
                getOnlineAssets();
            }
        });

        gobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchenabled = false;
                if(searchededittext.length()<=0){
                    Toast.makeText(mContext,getResources().getString(R.string.validText),Toast.LENGTH_SHORT).show();
                }else{
                    //bottomheader.setVisibility(View.VISIBLE);
                    searchlayout.setVisibility(View.GONE);
                    notificationsec.setVisibility(View.VISIBLE);
                    filtered_text.setText(getString(R.string.filtered_by)+"' "+searchededittext.getText().toString()+" '");
                    //icon_close.setVisibility(View.VISIBLE);
                    assetfilterheading.setVisibility(View.VISIBLE);
                    searchededittext.getText().toString();

                    View view = AssetListActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    loadAssetsBySearch(searchededittext.getText().toString().trim());
                }
            }
        });

        notificationsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
                    /*if(!getResources().getBoolean(R.bool.portrait_only)){
                        loadPopUpHelpView();
                    }else {
                        loadHelpView();
                    }*/
                    Intent i = new Intent(mContext,NotificationActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                }
            }
        });

        chatviewsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
                    Intent i = new Intent(mContext,NotificationActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                }
            }
        });

        closehelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                helplayout.setVisibility(View.GONE);
            }
        });

        String offline = PreferenceUtils.getOfflineMode(mContext);
        if(offline.equalsIgnoreCase("true")){
            offlinemode.setVisibility(View.VISIBLE);
            notificationsec.setVisibility(View.GONE);
            icon_expandslider.setVisibility(View.GONE);
            icon_search.setVisibility(View.INVISIBLE);
            chatviewsec.setVisibility(View.GONE);
        }else{
            offlinemode.setVisibility(View.GONE);
            // notificationsec.setVisibility(View.VISIBLE);
            chatviewsec.setVisibility(View.GONE);
            icon_expandslider.setVisibility(View.VISIBLE);
            icon_search.setVisibility(View.VISIBLE);
        }

        offlinemode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtils.checkIfNetworkAvailable(mContext)){
                    isoffline = false;
                    PreferenceUtils.setOfflineMode(mContext ,"false");
                    getAssetsForBrowse();
                }else{
                    Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                }
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

        /*categorey_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorey_filter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                cat_selected.setBackgroundColor(getResources().getColor(R.color.white));
                tags_filter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tag_selected.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                gotoCategoreyListView();
            }
        });

        tags_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tags_filter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tag_selected.setBackgroundColor(getResources().getColor(R.color.white));
                categorey_filter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                cat_selected.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                gotoTagsListView();
            }
        });*/

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
                filterlist.clear();
                CATS = "";TAGS = "";FLTR = "";
                showApplyButton();
                assetfilterheading.setVisibility(View.GONE);
                header.setVisibility(View.VISIBLE);
                filtered = false;
                bottommenusection.setVisibility(View.VISIBLE);
                getOnlineAssets();
            }
        });

        close_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filtered){
                    filtersection.setVisibility(View.GONE);
                    bottommenusection.setVisibility(View.GONE);
                    assetfilterheading.setVisibility(View.GONE);
                    header.setVisibility(View.VISIBLE);
                }else {
                    filtersection.setVisibility(View.GONE);
                    catlist.clear();
                    tagslist.clear();
                    filterlist.clear();
                    CATS = "";TAGS = "";FLTR = "";
                    catFiltered = false; tagfiltered = false; docFiltered = false;
                    showApplyButton();
                    filtered = false;
                    bottommenusection.setVisibility(View.VISIBLE);
                    getOnlineAssets();
                }
            }
        });

        catselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCategoreyListView();
            }
        });

        filterlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gototypeFilterListView();
            }
        });

        tagselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTagsListView();
            }
        });

        clearfilters.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    clearfilters.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
                    clearfilters.setTextColor(Color.WHITE);
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    clearfilters.setBackgroundColor(Color.WHITE);
                    clearfilters.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
                    clearfiltersFunction();
                    filtered = false;
                    filtersection.setVisibility(View.GONE);
                    bottommenusection.setVisibility(View.VISIBLE);
                    getOnlineAssets();
                }
                return true;
            }
        });

        applyfilters.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    applyfilters.setBackgroundColor(Color.WHITE);
                    applyfilters.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    applyfilters.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
                    applyfilters.setTextColor(Color.WHITE);
                    getAssetsDAIDs();
                    filtersection.setVisibility(View.GONE);
                    header.setVisibility(View.INVISIBLE);
                    assetfilterheading.setVisibility(View.VISIBLE);
                    String htmlString="<u>"+getResources().getString(R.string.clearfilter)+"</u>";
                    filtered_text.setText(Html.fromHtml(htmlString));
                    //filtered_text.setText("Clear Filters");
                    filtered = true;
                    bottommenusection.setVisibility(View.GONE);
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
                filterlist.clear();
                CATS = "";TAGS = "";FLTR = "";
                showApplyButton();
                assetfilterheading.setVisibility(View.GONE);
                header.setVisibility(View.VISIBLE);
                filtered = false;
                bottommenusection.setVisibility(View.VISIBLE);
                getOnlineAssets();

            }
        });

        clear_assetfilter_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottommenusection.setVisibility(View.GONE);
                assetfilterheading.setVisibility(View.GONE);
                header.setVisibility(View.VISIBLE);
                filtersection.setVisibility(View.VISIBLE);
            }
        });

        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SYSTEM_UI_FLAG_HIDE_NAVIGATION) {
                    // Now I have to check if the user has scrolled down or up.
                    int currentFirstVisible = grid.findFirstVisibleItemPosition();
                    if(currentFirstVisible > firstVisibleInListview) {
                        header.animate().translationY(-(header.getHeight()));
                        header.setVisibility(View.GONE);
                    } else {
                        header.animate().translationY(0f);
                        header.setVisibility(View.VISIBLE);
                    }
                    firstVisibleInListview = currentFirstVisible;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });*/

        searchededittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loadAssetsBySearch(searchededittext.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void clearfiltersFunction(){
        catlist.clear();
        tagslist.clear();
        filterlist.clear();
        CATS = "";TAGS = "";FLTR = "";
        catFiltered = false; tagfiltered = false; docFiltered = false;
        showApplyButton();
        digitalAssetsMasterCategoryLists = digitalassetCatTagFilterRepository.getAllCategory(CATS);
        digitalAssetsMasterTagsLists = digitalassetCatTagFilterRepository.getAllTags(TAGS);
        digitalAssetsMasterDAtypeLists = digitalassetCatTagFilterRepository.getAllDocTypes(FLTR);
        categoryListTextRecyclerAdapter = new CategoryListTextRecyclerAdapter(mContext, digitalAssetsMasterCategoryLists,true);
        categoryListTextRecyclerAdapter.notifyDataSetChanged();
        tagsListTextRecyclerAdapter = new TagsListTextRecyclerAdapter(mContext, digitalAssetsMasterTagsLists,true);
        tagsListTextRecyclerAdapter.notifyDataSetChanged();
        docTypeListTextRecycerAdapter = new DocTypeListTextRecycerAdapter(mContext,digitalAssetsMasterDAtypeLists,true);
        docTypeListTextRecycerAdapter.notifyDataSetChanged();
        catselection.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        tagselection.setBackgroundColor(Color.parseColor(Constants.OPAQUE_COLOR));
        filterlay.setBackgroundColor(Color.parseColor(Constants.OPAQUE_COLOR));
        cattag_recyclerView.setAdapter(categoryListTextRecyclerAdapter);
    }

    public void getOfflineAssets(){
        catViewVisible = false;
        icon_close.setVisibility(View.GONE);
        List<DigitalAssetsMaster> digitalAssetsMasterList = digitalAssetOfflineRepository.getAllOfflineAssets();
        allasset.setText(R.string.allassets);
        dismissProgressDialog();
        if(digitalAssetsMasterList.size() > 0){
            adapter = new AssetListItemRecyclerAdapter(AssetListActivity.this,digitalAssetsMasterList,false);
            recyclerView.setVisibility(View.VISIBLE);
            showhideemptystate(false,"",0);
            recyclerView.setAdapter(adapter);
            recyclerView.setItemViewCacheSize(digitalAssetsMasterList.size());
        }else{
            recyclerView.setVisibility(View.GONE);
            //showmsg(1);
            showhideemptystate(true, getResources().getString(R.string.error_offline), Constants.INTERNETERRORNUM);
        }
    }

    public void getOfflineAssetCatFolderView(){
        catViewVisible = true;
        allasset.setText(R.string.category);
        dismissProgressDialog();
        List<DigitalAssetsMaster> digitalAssetsMasterCategoryList = digitalAssetOfflineRepository.getAllOfflineCategoryWithCount();
        categoryListRecyclerAdapter = new CategoryListRecyclerAdapter(mContext, digitalAssetsMasterCategoryList);
        recyclerView.setAdapter(categoryListRecyclerAdapter);

        categoryListRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        /*mLayoutmanager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutmanager);
//        int mNoOfColumns = calculateNoOfColumns(mContext);
//        grid = new GridLayoutManager(this,mNoOfColumns);
//        recyclerView.setLayoutManager(grid);
        recyclerView.setEmptyView(mEmptyView);*/

        recyclerView.setHasFixedSize(true);
        cattag_recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        if(getResources().getBoolean(R.bool.portrait_only)){
            grid = new GridLayoutManager(this,2);
            recyclerView.setLayoutManager(grid);
        }else{
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                grid = new GridLayoutManager(this,5);
                recyclerView.setLayoutManager(grid);
            }else{
                grid = new GridLayoutManager(this,3);
                recyclerView.setLayoutManager(grid);
                //recyclerView.setLayoutManager(linearLayoutManager);
            }
        }
        firstVisibleInListview = grid.findFirstVisibleItemPosition();
        cattag_recyclerView.setLayoutManager(linearLayoutManager);
    }



    public void getAssetsForBrowse(){
        Log.d("start","0");
        if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
            PreferenceUtils.setOfflineMode(mContext ,"false");
            showProgressDialog();
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            final AssetService assetService = retrofitAPI.create(AssetService.class);

            String offsetFromUtc = CommonUtils.getUtcOffset();
            Log.d("getUTC",offsetFromUtc);
            Gson gsonget = new Gson();
            User userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);
            Log.d("start apistart","0");
            Call call = assetService.getAssetsForBrowse(userobj.getCompany_Code(),userobj.getUser_Code(),userobj.getRegion_Code(),
                    userobj.getUser_Type_Code(), userobj.getCompany_Id(),offsetFromUtc);
            call.enqueue(new Callback<ArrayList<DigitalAssetsMaster>>() {

                @Override
                public void onResponse(Response<ArrayList<DigitalAssetsMaster>> response, Retrofit retrofit) {
                    ArrayList<DigitalAssetsMaster> apiResponse= response.body();
                    if (apiResponse != null) {
                        Log.d("start apiresponse","0");
                        assetsForBrowses = apiResponse;
                        if(assetsForBrowses.size() > 0){
                            Log.d("log","assetsForBrowses");
                            Log.d("start b4 DBInsert","0");
                            digitalAssetHeaderRepository.assetsBulkInsert(assetsForBrowses);
                            Log.d("start DBInsert done","0");
                            offlinemode.setVisibility(View.GONE);
                            notificationsec.setVisibility(View.VISIBLE);
                            chatviewsec.setVisibility(View.GONE);
                            icon_expandslider.setVisibility(View.VISIBLE);
                            icon_search.setVisibility(View.VISIBLE);
                            insertintocategoreytagstable();
                            getOnlineAssets();
                        }else{
                            dismissProgressDialog();
                            recyclerView.setVisibility(View.GONE);
                            showhideemptystate(true,getResources().getString(R.string.noassetsfound),Constants.NORESULTSNUM);
                            icon_expandslider.setVisibility(View.GONE);
                            icon_search.setVisibility(View.INVISIBLE);
                            Log.d("retrofit","error 2");
                        }
                    } else {
                        dismissProgressDialog();
                        recyclerView.setVisibility(View.GONE);
                        showhideemptystate(true,getResources().getString(R.string.error_message),Constants.INTERNETERRORNUM);
                        icon_expandslider.setVisibility(View.GONE);
                        icon_search.setVisibility(View.INVISIBLE);
                        Log.d("retrofit","error 2");
                        //error
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    dismissProgressDialog();
                    Toast.makeText(mContext,getResources().getString(R.string.erroroccured),Toast.LENGTH_LONG).show();
                    Log.d("retrofit","error 2");
                }
            });
        } else {
            getOfflineAssets();
            isoffline = true;
            PreferenceUtils.setOfflineMode(mContext ,"true");
            offlinemode.setVisibility(View.VISIBLE);
            notificationsec.setVisibility(View.GONE);
            chatviewsec.setVisibility(View.GONE);
            icon_expandslider.setVisibility(View.GONE);
            icon_search.setVisibility(View.INVISIBLE);
            //digitalAssetHeaderRepository.deleteAssetMaster();
            dismissProgressDialog();
            Toast.makeText(mContext,getResources().getString(R.string.error_offline),Toast.LENGTH_LONG).show();
        }
        /**/

    }

    public void showmsg(int val){
        if(val == 1){
            showhideemptystate(true, getResources().getString(R.string.error_offline), Constants.NORESULTSNUM);
        }else {
            showhideemptystate(true, getResources().getString(R.string.error_offline), Constants.INTERNETERRORNUM);
        }
    }

    public void getOnlineAssets(){
        if(!isoffline) {
            catViewVisible = false;
            icon_close.setVisibility(View.GONE);
            Log.d("start get fromDB","0");
            List<DigitalAssetsMaster> digitalAssetsMasterList = digitalAssetHeaderRepository.getAllAssets();
            //List<DigitalAssetsMaster> digitalAssetsMasterListpage = digitalAssetHeaderRepository.getAssetsbyPage(1);
            allasset.setText(R.string.allassets);
            dismissProgressDialog();
            Log.d("start adapterload","0");
            if(digitalAssetsMasterList.size() > 0){
                adapter = new AssetListItemRecyclerAdapter(AssetListActivity.this, digitalAssetsMasterList,false);
                recyclerView.setVisibility(View.VISIBLE);
                showhideemptystate(false,"",0);
                recyclerView.setAdapter(adapter);
                recyclerView.setItemViewCacheSize(digitalAssetsMasterList.size());
                Log.d("start adapterloaded","0");
            }else{
                recyclerView.setVisibility(View.GONE);
                showhideemptystate(true,getResources().getString(R.string.no_result),Constants.NORESULTSNUM);
            }
        }else{
            getOfflineAssets();
        }
    }

    public void loadAssetsBySearch(String searchtext){
        searchededittext.setText("");
        List<DigitalAssetsMaster> digitalAssetsMasterList = digitalAssetHeaderRepository.getAssetsBysearch(searchtext);
        setUpRecyclerView();
        adapter = new AssetListItemRecyclerAdapter(AssetListActivity.this, digitalAssetsMasterList,false);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemViewCacheSize(digitalAssetsMasterList.size());
    }

    public void gotoOfflineCategoreyListView(){
        allasset.setText(R.string.category);
        icon_close.setVisibility(View.VISIBLE);
        dismissProgressDialog();
        List<DigitalAssetsMaster> digitalAssetsMasterCategoryList = digitalAssetOfflineRepository.getAllOfflineCategoryWithCount();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //categoryListTextRecyclerAdapter = new CategoryListTextRecyclerAdapter(mContext, digitalAssetsMasterCategoryList);
        recyclerView.setAdapter(categoryListTextRecyclerAdapter);
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

    @Override
    protected void onResume() {
        super.onResume();
        if(filtered) {
            filtersection.setVisibility(View.GONE);
            catlist.clear();
            tagslist.clear();
            filterlist.clear();
            CATS = "";TAGS = "";FLTR = "";
            assetfilterheading.setVisibility(View.GONE);
            header.setVisibility(View.VISIBLE);
            clearfiltersFunction();
            uploadActivity.insertUserTracking("BookShelf",latitude,longitude);
            if(isCatTagSectionShown){
                clearCatTags();
            }
        } else {
            filtersection.setVisibility(View.GONE);
            catlist.clear();
            tagslist.clear();
            filterlist.clear();
            CATS = "";TAGS = "";FLTR = "";
            assetfilterheading.setVisibility(View.GONE);
            header.setVisibility(View.VISIBLE);
            bottommenusection.setVisibility(View.VISIBLE);
            clearfiltersFunction();
            uploadActivity.insertUserTracking("BookShelf",latitude,longitude);
            if(isCatTagSectionShown){
                clearCatTags();
            }
        }
        setthemeforView();
        //if(NetworkUtils.checkIfNetworkAvailable(mContext)){
        sendDigitalAssetAnalyticsToServer();
        getAssetsForBrowse();

        //}
        //getOnlineAssets;
    }

    public void initializeView() {
        companylogo = (ImageView) findViewById(R.id.companylogo);
        mEmptyView = findViewById(R.id.empty_view);
        notification= (ImageView) findViewById(R.id.notification);
        chatview = (ImageView) findViewById(R.id.chatview);
        settings = (ImageView) findViewById(R.id.settings);
        icon_close= (ImageView) findViewById(R.id.icon_close);
        icon_dashboard = (ImageView) findViewById(R.id.icon_dashboard);
        icon_search = (ImageView) findViewById(R.id.icon_search);
        icon_expandslider = (ImageView) findViewById(R.id.icon_expandslider);
        allasset = (TextView) findViewById(R.id.allasset);
        recyclerView = (EmptyRecyclerView) findViewById(R.id.recyclerView);
        offlinemode = (ImageView) findViewById(R.id.offlineicon);
        messageDialog = new MessageDialog(mContext);
        bottomheader = (RelativeLayout) findViewById(R.id.bottomheader);
        activity_list = (RelativeLayout)findViewById(R.id.activity_asset_list);

        searchededittext = (EditText) findViewById(R.id.searchededittext);
        searchlayout = (LinearLayout) findViewById(R.id.searchlayout);
        gobutton = (TextView) findViewById(R.id.gobutton);

        helpView = (WebView) findViewById(R.id.helpview);
        helplayout = findViewById(R.id.helplayout);
        closehelp = (TextView) findViewById(R.id.closehelp);

        cat_tag_section = (RelativeLayout) findViewById(R.id.cat_tag_section);
        cat_selected = findViewById(R.id.cat_selected);
        tag_selected = findViewById(R.id.tag_selected);
        categorey_filter = (LinearLayout) findViewById(R.id.categorey_filter);
        tags_filter = (LinearLayout) findViewById(R.id.tags_filter);
        icon_closecattags = (ImageView) findViewById(R.id.icon_closecattags);

        header = findViewById(R.id.header);

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

        filterheadingtext = (TextView) findViewById(R.id.filterheadingtext);
        cattagmenus = findViewById(R.id.cattagmenus);
        icon_cats = (ImageView)findViewById(R.id.icon_cats);
        icon_tags = (ImageView)findViewById(R.id.icon_tags);
        icon_filter = (ImageView) findViewById(R.id.icon_filter);

        msearchtext = (SearchView) findViewById(R.id.searchtext);
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        closesearch = (ImageView) findViewById(R.id.closesearch);

        tickfilter = (ImageView) findViewById(R.id.tickfilter);
        filterheading = (RelativeLayout) findViewById(R.id.filterheading);

        emptyimage = (ImageView) findViewById(R.id.emptyimage);
        retrybutton = (TextView) findViewById(R.id.retrybutton);
        emptymessage = (TextView) findViewById(R.id.emptymessage);

        notificationsec = findViewById(R.id.notificationsec);
        chatviewsec = findViewById(R.id.chatviewsec);
        notificationcount = (TextView) findViewById(R.id.notificationcount);
        chatcount = (TextView) findViewById(R.id.chatcount);
        course = (TextView) findViewById(R.id.coursetext);
        asset = (TextView) findViewById(R.id.assettext);
        checklist = (TextView) findViewById(R.id.checklisttext);
        task = (TextView) findViewById(R.id.tasktext);
        if(PreferenceUtils.getLandingPageAccess(mContext) != null){
            Gson gsonget = new Gson();
            LandingPageAccess landingobj = gsonget.fromJson(PreferenceUtils.getLandingPageAccess(mContext), LandingPageAccess.class);
            if(landingobj != null) {

                if (!TextUtils.isEmpty(landingobj.getCourseText()))
                {
                    course.setText(landingobj.getCourseText());
                }
                if (!TextUtils.isEmpty(landingobj.getAssetText()))
                {
                    asset.setText(landingobj.getAssetText());
                }

                if (!TextUtils.isEmpty(landingobj.getChecklistText()))
                {
                    checklist.setText(landingobj.getChecklistText());
                }

                if (!TextUtils.isEmpty(landingobj.getTaskText()))
                {
                    task.setText(landingobj.getTaskText());
                }
            }
        }
    }

    //For GridView to manage number of grids using screen size this should be used
    /*public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }*/

    public void setthemeforView(){

        Ion.with(companylogo).placeholder(R.color.topbar).error(R.color.topbar).load(
                (!TextUtils.isEmpty(PreferenceUtils.getClientLogo(mContext)))? PreferenceUtils.getClientLogo(mContext) : PreferenceUtils.getClientLogo(mContext));
        File imgFile = new File("/storage/sdcard0/SwaaS LMS/companylogo.jpg");

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            companylogo.setImageBitmap(myBitmap);

        }
        activity_list.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        gobutton.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        header.setBackgroundColor(Color.parseColor(Constants.HEADERBAR_COLOR));

        icon_expandslider.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));
        settings.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));
        offlinemode.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));
        icon_search.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));

        pos1.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
//        higlighttext.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
        clearfilters.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
        closesearch.setBackgroundColor(Color.parseColor(Constants.HEADERBAR_COLOR));
        closesearch.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));
        filtered_text.setTypeface(filtered_text.getTypeface(), Typeface.ITALIC);

        filterheading.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        filterheadingtext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        close_filter.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));

        assetfilterheading.setBackgroundColor(Color.parseColor(Constants.HEADERBAR_COLOR));
        filtered_text.setTextColor(Color.parseColor(Constants.TOPBARICON_COLOR));
        tickfilter.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));

        emptyimage.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        retrybutton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.CARDBACKGROUND_COLOR)));
        retrybutton.setTextColor(Color.parseColor(Constants.TEXT_COLOR));

        notification.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));
        chatview.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));
    }

    public void loadHelpView(){
        recyclerView.setVisibility(View.GONE);
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
            URL = Constants.COMPANY_BASE_URL+"/HelpFiles/taco/"+lan+"Kanglehelpassetlibrary.htm";
        }else{
            URL = Constants.COMPANY_BASE_URL+"/HelpFiles/other/Kanglehelpassetlibrary.html";
        }
        //String URL = Constants.COMPANY_BASE_URL+"/HelpFiles/"+lan+"Kanglehelpassetlibrary.htm";
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
            URL = Constants.COMPANY_BASE_URL+"/HelpFiles/taco/"+lan+"Kanglehelpassetlibrary.htm";
        }else{
            URL = Constants.COMPANY_BASE_URL+"/HelpFiles/other/Kanglehelpassetlibrary.html";
        }
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

    public void clearCatTags(){
        bottomheader.setVisibility(View.GONE);
        cat_tag_section.setVisibility(View.GONE);
        isCatTagSectionShown = false;
        setUpRecyclerView();
        getOnlineAssets();
    }

    public void initializebottomnavigation(){
        bottommenusection = findViewById(R.id.bottommenusection);
        bottommenus = (LinearLayout) findViewById(R.id.bottommenus);
        lpcourse = findViewById(R.id.lpcourse);
        assetpage = findViewById(R.id.assetpage);
        chklistpage = findViewById(R.id.chklistpage);
        taskpage = findViewById(R.id.reports);
        profilepage = findViewById(R.id.profilepage);
        pos1 = (ImageView) findViewById(R.id.pos1);
        higlighttext = (TextView) findViewById(R.id.higlighttext);
    }

    public void bottomnavigationonClickevents(){
        int count = 1;
        if(PreferenceUtils.getLandingPageAccess(mContext) != null){
            Gson gsonget = new Gson();
            LandingPageAccess landingobj = gsonget.fromJson(PreferenceUtils.getLandingPageAccess(mContext), LandingPageAccess.class);
            if(landingobj != null) {
                if (!TextUtils.isEmpty(landingobj.getLibrary()) && landingobj.getLibrary().equalsIgnoreCase("Y")) {
                    assetpage.setVisibility(View.VISIBLE);
                    count += 1;
                }else{
                    assetpage.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(landingobj.getCourse()) && landingobj.getCourse().equalsIgnoreCase("L")) {
                    lpcourse.setVisibility(View.VISIBLE);
                    count += 1;
                } else if (!TextUtils.isEmpty(landingobj.getCourse()) && landingobj.getCourse().equalsIgnoreCase("S")) {
                    lpcourse.setVisibility(View.VISIBLE);
                    count += 1;
                    //adCourse.setVisibility(View.VISIBLE);
                } else if(!TextUtils.isEmpty(landingobj.getCourse()) && landingobj.getCourse().equalsIgnoreCase("A")){
                    lpcourse.setVisibility(View.VISIBLE);
                    count += 1;
                    //lpcourse.setVisibility(View.VISIBLE);
                    //lpcourse.setVisibility(View.VISIBLE);
                }else{
                    lpcourse.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(landingobj.getChecklist()) && landingobj.getChecklist().equalsIgnoreCase("Y")) {
                    chklistpage.setVisibility(View.VISIBLE);
                    count += 1;
                }else{
                    chklistpage.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(landingobj.getTask()) && landingobj.getTask().equalsIgnoreCase("Y")) {
                    taskpage.setVisibility(View.VISIBLE);
                    count += 1;
                }else{
                    taskpage.setVisibility(View.GONE);
                }
            }else{
                bottommenusection.setVisibility(View.GONE);
            }
        }

        bottommenus.setWeightSum(count);

        lpcourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetworkUtils.checkIfNetworkAvailable(AssetListActivity.this)){
                    //Toast.makeText(AssetListActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,CourseListActivity.class);
                startActivity(i);
                finish();
            }
        });

        assetpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent assetview = new Intent(mContext,AssetListActivity.class);
                startActivity(assetview);
                finish();*/
            }
        });

        chklistpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetworkUtils.checkIfNetworkAvailable(AssetListActivity.this)){
                    Toast.makeText(AssetListActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,ChecklistLandingActivity.class);
                startActivity(i);
                finish();
            }
        });

        taskpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!NetworkUtils.checkIfNetworkAvailable(AssetListActivity.this)){
                    Toast.makeText(AssetListActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,TaskListActivity.class);
                startActivity(i);
                finish();
            }
        });

        profilepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetworkUtils.checkIfNetworkAvailable(AssetListActivity.this)){
                    Toast.makeText(AssetListActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,MoreMenuActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    //FilterFunctionality

    public void gotoCategoreyListView(){
        try{
            toggleselection(1);
            if(!isoffline) {
                allasset.setText(R.string.category);
                icon_close.setVisibility(View.VISIBLE);
                dismissProgressDialog();
                //List<DigitalAssetsMaster> digitalAssetsMasterCategoryList = digitalAssetHeaderRepository.getAllCategoryWithCount();
                //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
                String valuenames = "",valuenames1 = "";
                if(tagslist.size() == 0 && filterlist.size() == 0){
                    List<DigitalAssetsMaster> digitalAssetsMasterCategoryList = digitalassetCatTagFilterRepository.getAllCategory(CATS);
                    reloadCatSlider(digitalAssetsMasterCategoryList);
                }else{
                    if(tagslist.size() > 0) {
                        String tagnames = "";
                        for (String catname : tagslist) {
                            tagnames = tagnames + "," + "'" + catname.toString() + "'";
                        }
                        StringBuilder sb = new StringBuilder(tagnames);
                        sb.deleteCharAt(0);
                        valuenames = sb.toString().replace("''", "'");
                    }
                    if(filterlist.size() > 0){
                        String filternames = "";
                        for (String catname : filterlist) {
                            filternames =  filternames + "," + "'" + catname.toString() + "'";
                        }
                        StringBuilder sb1 = new StringBuilder(filternames);
                        sb1.deleteCharAt(0);
                        valuenames1 = sb1.toString().replace("''", "'");
                    }

                    String selectQuery = "select tbl_DIGASSETS_CAT_TAG_MASTER.DACategoryName FROM tbl_DIGASSETS_CAT_TAG_MASTER ";
                    if (tagslist.size() > 0|| filterlist.size() > 0) {
                        selectQuery += " WHERE ";
                        if (tagslist.size() > 0) {
                            selectQuery += " tbl_DIGASSETS_CAT_TAG_MASTER.Tags in ("+valuenames+") ";
                        }
                        if (filterlist.size() > 0) {
                            if (tagslist.size() > 0) {
                                selectQuery += " AND ";
                            }

                            selectQuery += " tbl_DIGASSETS_CAT_TAG_MASTER.DA_Type in ("+valuenames1+") ";
                        }
                    }
                    selectQuery += " AND tbl_DIGASSETS_CAT_TAG_MASTER.DACategoryName not in ("+CATS+") Group By tbl_DIGASSETS_CAT_TAG_MASTER.DACategoryName ";
                    List<DigitalAssetsMaster> digitalAssetsMasterCategoryList = digitalassetCatTagFilterRepository.getCategorybySelectedTags(selectQuery);
                    reloadCatSlider(digitalAssetsMasterCategoryList);
                }
            }else{
                gotoOfflineCategoreyListView();
            }
        }catch (Exception e){

        }
    }

    public void gotoTagsListView(){
        try{
            toggleselection(2);
            if(!isoffline) {
                allasset.setText("TAGS");
                icon_close.setVisibility(View.VISIBLE);
                dismissProgressDialog();
                //List<DigitalAssetsMaster> digitalAssetsMasterCategoryList = digitalAssetHeaderRepository.getAllCategoryWithCount();
                //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
                String valuenames = "",valuenames1 = "";
                if(catlist.size() == 0 && filterlist.size() == 0){
                    List<DigitalAssetsMaster> digitalAssetsMasterTagsList = digitalassetCatTagFilterRepository.getAllTags(TAGS);
                    reloadTagSlider(digitalAssetsMasterTagsList);
                }else{
                    if(catlist.size() > 0) {
                        String catnames = "";
                        for (String catname : catlist) {
                            catnames = catnames + "," + "'" + catname.toString() + "'";
                        }
                        StringBuilder sb = new StringBuilder(catnames);
                        sb.deleteCharAt(0);
                        valuenames = sb.toString().replace("''", "'");
                    }
                    if (filterlist.size() > 0) {
                        String filternames = "";
                        for (String catname : filterlist) {
                            filternames = filternames + "," + "'" + catname.toString() + "'";
                        }
                        StringBuilder sb1 = new StringBuilder(filternames);
                        sb1.deleteCharAt(0);
                        valuenames1 = sb1.toString().replace("''", "'");
                    }

                    String selectQuery = "select tbl_DIGASSETS_CAT_TAG_MASTER.Tags FROM tbl_DIGASSETS_CAT_TAG_MASTER ";
                    if (catlist.size() > 0 || filterlist.size() > 0) {
                        selectQuery += "WHERE ";
                        if (catlist.size() > 0) {
                            selectQuery += "tbl_DIGASSETS_CAT_TAG_MASTER.DACategoryName in ("+valuenames+") ";
                        }
                        if (filterlist.size() > 0) {
                            if (catlist.size() > 0) {
                                selectQuery += " AND ";
                            }
                            selectQuery += "tbl_DIGASSETS_CAT_TAG_MASTER.DA_Type in ("+valuenames1+") ";
                        }
                    }
                    selectQuery += " AND tbl_DIGASSETS_CAT_TAG_MASTER.Tags not in ("+TAGS+") Group By tbl_DIGASSETS_CAT_TAG_MASTER.Tags ";

                    List<DigitalAssetsMaster> digitalAssetsMasterTagsList = digitalassetCatTagFilterRepository.getTagsbySelectedcategorey(selectQuery);
                    reloadTagSlider(digitalAssetsMasterTagsList);
                }
            }else{
                gotoOfflineCategoreyListView();
            }
        }catch(Exception e){

        }
    }

    public void gototypeFilterListView(){
        try{
            toggleselection(3);
            if(!isoffline) {
                allasset.setText(R.string.category);
                icon_close.setVisibility(View.VISIBLE);
                dismissProgressDialog();
                //List<DigitalAssetsMaster> digitalAssetsMasterCategoryList = digitalAssetHeaderRepository.getAllCategoryWithCount();
                //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
                String valuenames = "",valuenames1 = "";
                if(tagslist.size() == 0 && catlist.size() == 0){
                    List<DigitalAssetsMaster> digitalAssetsMasterCategoryList = digitalassetCatTagFilterRepository.getAllDocTypes(FLTR);
                    reloadDatypeFilter(digitalAssetsMasterCategoryList);
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
                    if(catlist.size()>0) {
                        String filternames = "";
                        for (String catname : catlist) {
                            filternames =  filternames + "," + "'" + catname.toString() + "'";
                        }
                        StringBuilder sb1 = new StringBuilder(filternames);
                        sb1.deleteCharAt(0);
                        valuenames1 = sb1.toString().replace("''", "'");
                    }

                    String selectQuery = "select tbl_DIGASSETS_CAT_TAG_MASTER.DA_Type FROM tbl_DIGASSETS_CAT_TAG_MASTER ";
                    if (tagslist.size() > 0 || catlist.size() > 0) {
                        selectQuery += "WHERE ";
                        if (tagslist.size() > 0) {
                            selectQuery += "tbl_DIGASSETS_CAT_TAG_MASTER.Tags in ("+valuenames+") ";
                        }
                        if (catlist.size() > 0) {
                            if (tagslist.size() > 0) {
                                selectQuery += " AND ";
                            }
                            selectQuery += "tbl_DIGASSETS_CAT_TAG_MASTER.DACategoryName in ("+valuenames1+") ";
                        }
                    }
                    selectQuery += " AND tbl_DIGASSETS_CAT_TAG_MASTER.DA_Type not in ("+FLTR+") Group By tbl_DIGASSETS_CAT_TAG_MASTER.DA_Type ";
                    //List<DigitalAssetsMaster> digitalAssetsMasterCategoryList = digitalassetCatTagFilterRepository.getDAtypebySelectedcategoreyquery(selectQuery);
                    List<DigitalAssetsMaster> digitalAssetsMasterCategoryList = digitalassetCatTagFilterRepository.getDAtypebySelectedcategorey(selectQuery);
                    reloadDatypeFilter(digitalAssetsMasterCategoryList);
                }
            }else{
                gotoOfflineCategoreyListView();
            }
        }catch (Exception e){

        }
    }

    public void reloadCatSlider(List<DigitalAssetsMaster> newmodel){
        List<DigitalAssetsMaster> catrefresh = new ArrayList<>();
        for(DigitalAssetsMaster existing : digitalAssetsMasterCategoryLists){
            if(existing.iscatchecked){
                catrefresh.add(existing);
            }
        }
        for(DigitalAssetsMaster ne : newmodel){
            catrefresh.add(ne);
        }
        digitalAssetsMasterCategoryLists = catrefresh;
        categoryListTextRecyclerAdapter = new CategoryListTextRecyclerAdapter(mContext, digitalAssetsMasterCategoryLists,true);
        cattag_recyclerView.setAdapter(categoryListTextRecyclerAdapter);
    }

    public void reloadTagSlider(List<DigitalAssetsMaster> newmodel){
        List<DigitalAssetsMaster> tagrefresh = new ArrayList<>();
        for(DigitalAssetsMaster existing : digitalAssetsMasterTagsLists){
            if(existing.istagchecked){
                tagrefresh.add(existing);
            }
        }
        for(DigitalAssetsMaster ne : newmodel){
            tagrefresh.add(ne);
        }
        digitalAssetsMasterTagsLists = tagrefresh;
        tagsListTextRecyclerAdapter = new TagsListTextRecyclerAdapter(mContext, digitalAssetsMasterTagsLists,true);
        cattag_recyclerView.setAdapter(tagsListTextRecyclerAdapter);
    }

    public void reloadDatypeFilter(List<DigitalAssetsMaster> newmodel){
        List<DigitalAssetsMaster> tagrefresh = new ArrayList<>();
        for(DigitalAssetsMaster existing : digitalAssetsMasterDAtypeLists){
            if(existing.isfltrchecked){
                tagrefresh.add(existing);
            }
        }
        for(DigitalAssetsMaster ne : newmodel){
            tagrefresh.add(ne);
        }
        digitalAssetsMasterDAtypeLists = tagrefresh;
        docTypeListTextRecycerAdapter = new DocTypeListTextRecycerAdapter(mContext, digitalAssetsMasterDAtypeLists,true);
        cattag_recyclerView.setAdapter(docTypeListTextRecycerAdapter);
    }

    public void insertintocategoreytagstable(){
        Log.d("start get CAT TAGS ","0");
        List<DigitalAssetsMaster> digitalAssetsListfortags = digitalAssetHeaderRepository.getAllAssets();
        ArrayList<DigitalAssetsMaster> cattags = new ArrayList<>();
        for (DigitalAssetsMaster dig : digitalAssetsListfortags) {
            if(dig.getUDTags() != null && !dig.getUDTags().isEmpty()) {
                String[] tags = dig.getUDTags().split("#");
                for (String t : tags) {
                    DigitalAssetsMaster values = new DigitalAssetsMaster();
                    values.setDAID(dig.getDAID());
                    values.setDACategoryCode(dig.getDACategoryCode());
                    values.setDACategoryName(dig.getDACategoryName());
                    values.setTags(t);
                    values.setUDTags(dig.getUDTags());
                    values.setDA_Type(dig.getDA_Type());
                    cattags.add(values);
                }
            }
        }
        Log.d("start DBInsert CAT TAGS start","0");
        digitalassetCatTagFilterRepository.catTagsBulkInsert(cattags);
        Log.d("start DBInsert CAT TAGS finish","0");
    }

    public void filteredcatList(DigitalAssetsMaster cat){
        try{
            if(cat.iscatchecked()) {
                catlist.add(cat.getDACategoryName());
            }else{
                catlist.remove(cat.getDACategoryName());
            }
            if(catlist.size() > 0){
                catFiltered = true;
            }else{
                catFiltered = false;
            }

            if(catFiltered) {
                String catnames = "";
                for (String catname : catlist) {
                    catnames = catnames + "," + "'" + catname.toString() + "'";
                }
                StringBuilder sb = new StringBuilder(catnames);
                sb.deleteCharAt(0);
                String valuenames = sb.toString().replace("''", "'");
                CATS = valuenames;
            }else{
                CATS = "";
            }
            showApplyButton();
        }catch (Exception e){

        }
    }

    public void filteredtagList(DigitalAssetsMaster cat){
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

            if(tagfiltered){
                String tagnames = "";
                for (String catname : tagslist) {
                    tagnames =  tagnames + "," + "'" + catname.toString() + "'";
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

    public void filtereddocList(DigitalAssetsMaster cat){
        try{
            if(cat.isfltrchecked()) {
                filterlist.add(cat.getDA_Type());
            }else{
                filterlist.remove(cat.getDA_Type());
            }
            if(filterlist.size() > 0){
                docFiltered = true;
            }else{
                docFiltered = false;
            }

            if(docFiltered){
                String tagnames = "";
                for (String catname : filterlist) {
                    tagnames =  tagnames + "," + "'" + catname.toString() + "'";
                }
                StringBuilder sb = new StringBuilder(tagnames);
                sb.deleteCharAt(0);
                String valuenames = sb.toString().replace("''", "'");
                FLTR = valuenames;
            }else{
                FLTR = "";
            }
            showApplyButton();
        }catch(Exception e){

        }
    }

    public void getAssetsDAIDs(){
        try{
            String selectQuery = "SELECT tbl_DIGASSETS_CAT_TAG_MASTER.DAID FROM tbl_DIGASSETS_CAT_TAG_MASTER ";
            if (CATS.length() > 0 || TAGS.length() > 0 || FLTR.length() > 0) {
                selectQuery += "WHERE ";
                if (CATS.length() > 0) {
                    selectQuery += "tbl_DIGASSETS_CAT_TAG_MASTER.DACategoryName in ("+CATS+") ";
                }
                if (TAGS.length() > 0) {
                    if (CATS.length() > 0) {
                        selectQuery += " AND ";
                    }
                    selectQuery += "tbl_DIGASSETS_CAT_TAG_MASTER.Tags in ("+TAGS+") ";
                }
                if (FLTR.length() > 0) {
                    if (CATS.length() > 0 || TAGS.length() > 0) {
                        selectQuery += " AND ";
                    }
                    selectQuery += "tbl_DIGASSETS_CAT_TAG_MASTER.DA_Type in ("+FLTR+")";
                }
            }
            selectQuery += " Group by DAID";

            List<DigitalAssetsMaster> DAIDlist = digitalassetCatTagFilterRepository.getAssetIdbySelectedCatTag(selectQuery);
            DAIDlist.size();
            filteredlist(DAIDlist);
        }catch(Exception e){

        }
    }

    public void filteredlist(List<DigitalAssetsMaster> DAIDlist){
        try{
            List<String> assetsId = new ArrayList<>();
            String assetIdnames = "";
            for (DigitalAssetsMaster catname : DAIDlist) {
                assetsId.add(catname.getDAID());
            }
            for (String catname : assetsId) {
                assetIdnames =  assetIdnames + "," + "'" + catname.toString() + "'";
            }
            StringBuilder sb = new StringBuilder(assetIdnames);
            sb.deleteCharAt(0);
            String values = sb.toString().replace("''", "'");
            values.length();
            List<DigitalAssetsMaster> digitalAssetsMasterListfilterd = digitalAssetHeaderRepository.getFilteredAssets(values);
            adapter = new AssetListItemRecyclerAdapter(AssetListActivity.this, digitalAssetsMasterListfilterd,false);
            recyclerView.setAdapter(adapter);
            recyclerView.setItemViewCacheSize(digitalAssetsMasterListfilterd.size());
        }catch (Exception e){
            Log.e("Exception",e.toString());
        }

    }

    public void showApplyButton(){
        if(catFiltered || tagfiltered || docFiltered){
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
            mEmptyView.setVisibility(View.VISIBLE);
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
            emptymessage.setText(message.toString());
        }else{
            mEmptyView.setVisibility(View.GONE);
        }
    }
}
