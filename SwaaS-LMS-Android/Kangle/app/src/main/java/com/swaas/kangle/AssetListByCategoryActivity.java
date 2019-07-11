package com.swaas.kangle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
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

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.swaas.kangle.API.model.LandingPageAccess;
import com.swaas.kangle.CheckList.ChecklistLandingActivity;
import com.swaas.kangle.LPCourse.CourseListActivity;
import com.swaas.kangle.TaskModule.TaskListActivity;
import com.swaas.kangle.adapter.AssetListItemRecyclerAdapter;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.db.DigitalAssetOfflineRepository;
import com.swaas.kangle.db.Filters.DigitalassetCatTagFilterRepository;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.MessageDialog;
import com.swaas.kangle.utils.NetworkUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AssetListByCategoryActivity extends AppCompatActivity {

    ImageView companylogo,notification,settings;
    ImageView icon_close,icon_dashboard,icon_search,icon_expandslider;
    TextView allasset;
    EmptyRecyclerView recyclerView;
    View mEmptyView;
    AssetListItemRecyclerAdapter adapter;
    LinearLayoutManager mLayoutmanager;
    Context mContext;
    ProgressDialog mProgressDialog;
    ArrayList<DigitalAssetsMaster> assetsForBrowses;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;
    DigitalAssetOfflineRepository offlineRepository;
    boolean catViewVisible = false;
    String category = null;
    String tagName = null;
    RelativeLayout activity_list;

    EditText searchededittext;
    TextView gobutton;
    RelativeLayout searchlayout;
    RelativeLayout bottomheader;
    LinearLayout header;

    WebView helpView;
    View helplayout;
    TextView closehelp;

    MessageDialog messageDialog;

    boolean searchenabled = false;
    GridLayoutManager grid;

    View lpcourse,assetpage,chklistpage,profilepage,taskpage;
    LinearLayout bottommenus;
    ImageView pos1;
    TextView higlighttext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_list_by_category);

        mContext = AssetListByCategoryActivity.this;
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(mContext);
        offlineRepository = new DigitalAssetOfflineRepository(mContext);
        //getSupportActionBar().hide();

        if(getIntent() != null){
            category =  getIntent().getStringExtra("categoreyName");
            tagName = getIntent().getStringExtra("TagsName");
        }

        initializeView();
        onClickListeners();
        setUpRecyclerView();
        showProgressDialog();
        initializebottomnavigation();
        bottomnavigationonClickevents();
        setthemeforView();
        Ion.with(companylogo).placeholder(R.color.black).error(R.color.black).load(
                (!TextUtils.isEmpty(PreferenceUtils.getClientLogo(mContext)))? PreferenceUtils.getClientLogo(mContext) : PreferenceUtils.getClientLogo(mContext));
        File imgFile = new File("/storage/sdcard0/SwaaS LMS/companylogo.jpg");

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            companylogo.setImageBitmap(myBitmap);

        }
        if(category != null) {
            getAssetsByCategories(category);
        }else if(tagName != null){
            getAssetsDAIDs();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        /*mLayoutmanager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutmanager);
        recyclerView.setEmptyView(mEmptyView);*/

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        if(getResources().getBoolean(R.bool.portrait_only)){
            //recyclerView.setLayoutManager(linearLayoutManager);
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
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(searchenabled){
            searchlayout.setVisibility(View.GONE);
            bottomheader.setVisibility(View.VISIBLE);
            searchenabled=false;
        } else {
            super.onBackPressed();
        }
    }

    public void onClickListeners(){

        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext,AssetListActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
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

        icon_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        icon_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        icon_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchlayout.setVisibility(View.VISIBLE);
                searchededittext.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchededittext, InputMethodManager.SHOW_IMPLICIT);
                searchenabled = true;
                if(bottomheader.isShown()){
                    bottomheader.setVisibility(View.GONE);
                }

            }
        });

        gobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchenabled = false;
                if(searchededittext.length()<=0){
                    Toast.makeText(mContext,getResources().getString(R.string.validText),Toast.LENGTH_SHORT).show();
                }else{
                    bottomheader.setVisibility(View.VISIBLE);
                    searchlayout.setVisibility(View.GONE);
                    allasset.setText(searchededittext.getText().toString());
                    icon_close.setVisibility(View.VISIBLE);
                    searchededittext.getText().toString();
                    View view = AssetListByCategoryActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    loadAssetsBySearch(searchededittext.getText().toString().trim());
                }
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
                    if(!getResources().getBoolean(R.bool.portrait_only)){
                        loadPopUpHelpView();
                    }else {
                        loadHelpView();
                    }
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
    }

    public void loadAssetsBySearch(String searchtext){
        searchededittext.setText("");
        List<DigitalAssetsMaster> digitalAssetsMasterList = digitalAssetHeaderRepository.getAssetsBysearch(searchtext);
        adapter = new AssetListItemRecyclerAdapter(AssetListByCategoryActivity.this, digitalAssetsMasterList,true);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemViewCacheSize(digitalAssetsMasterList.size());
    }

    public void getAssetsByCategories(String category){
        if(PreferenceUtils.getOfflineMode(mContext).equalsIgnoreCase("true")){
            List<DigitalAssetsMaster> asset = offlineRepository.getofflineAssetsByCategoryName(category);
            dismissProgressDialog();
            icon_close.setVisibility(View.VISIBLE);
            allasset.setText(category);
            adapter = new AssetListItemRecyclerAdapter(AssetListByCategoryActivity.this,asset,true);
            recyclerView.setAdapter(adapter);
            recyclerView.setItemViewCacheSize(asset.size());
        }else{
            List<DigitalAssetsMaster> asset = digitalAssetHeaderRepository.getAssetsByCategoryName(category);
            dismissProgressDialog();
            icon_close.setVisibility(View.VISIBLE);
            allasset.setText(category);
            adapter = new AssetListItemRecyclerAdapter(AssetListByCategoryActivity.this,asset,true);
            recyclerView.setAdapter(adapter);
            recyclerView.setItemViewCacheSize(asset.size());
        }
    }

    public void getAssetsDAIDs(){
        try{
            String TAGS = "";
            String[] tags = tagName.split("#");
            for (String t : tags) {
                TAGS =  TAGS + "," + "'" + t.toString() + "'";
            }
            StringBuilder sb = new StringBuilder(TAGS);
            sb.deleteCharAt(0);
            String values = sb.toString().replace("''", "'");
            values.length();

            String selectQuery = "SELECT tbl_DIGASSETS_CAT_TAG_MASTER.DAID FROM tbl_DIGASSETS_CAT_TAG_MASTER WHERE tbl_DIGASSETS_CAT_TAG_MASTER.Tags in ("+values+") ";
            selectQuery += " Group by DAID";

            DigitalassetCatTagFilterRepository digitalassetCatTagFilterRepository = new DigitalassetCatTagFilterRepository(mContext);
            List<DigitalAssetsMaster> DAIDlist = digitalassetCatTagFilterRepository.getAssetIdbySelectedCatTag(selectQuery);
            DAIDlist.size();
            getrelatedassetsbyTags(DAIDlist);
        }catch(Exception e){

        }
    }

    public void getrelatedassetsbyTags(List<DigitalAssetsMaster> DAIDlist){
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
        List<DigitalAssetsMaster> asset = digitalAssetHeaderRepository.getFilteredAssets(values);
        dismissProgressDialog();
        icon_close.setVisibility(View.VISIBLE);
        allasset.setText(mContext.getResources().getString(R.string.filtered_by));
        adapter = new AssetListItemRecyclerAdapter(AssetListByCategoryActivity.this,asset,true);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemViewCacheSize(asset.size());
    }

    public void getAssetsByTags(String tags){
        if(PreferenceUtils.getOfflineMode(mContext).equalsIgnoreCase("true")){
            List<DigitalAssetsMaster> asset = offlineRepository.getofflineAssetsByCategoryName(category);
            dismissProgressDialog();
            icon_close.setVisibility(View.VISIBLE);
            allasset.setText(category);
            adapter = new AssetListItemRecyclerAdapter(AssetListByCategoryActivity.this,asset,true);
            recyclerView.setAdapter(adapter);
        }else{

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

    public void initializeView() {
        companylogo = (ImageView) findViewById(R.id.companylogo);
        mEmptyView = findViewById(R.id.empty_view);
        notification= (ImageView) findViewById(R.id.notification);
        settings = (ImageView) findViewById(R.id.settings);
        icon_close= (ImageView) findViewById(R.id.icon_close);
        icon_dashboard = (ImageView) findViewById(R.id.icon_dashboard);
        icon_search = (ImageView) findViewById(R.id.icon_search);
        icon_expandslider = (ImageView) findViewById(R.id.icon_expandslider);
        allasset = (TextView) findViewById(R.id.allasset);
        recyclerView = (EmptyRecyclerView) findViewById(R.id.recyclerView);
        activity_list = (RelativeLayout)findViewById(R.id.activity_asset_list);

        searchededittext = (EditText) findViewById(R.id.searchededittext);
        searchlayout = (RelativeLayout) findViewById(R.id.searchlayout);
        gobutton = (TextView) findViewById(R.id.gobutton);

        bottomheader = (RelativeLayout) findViewById(R.id.bottomheader);
        header = (LinearLayout) findViewById(R.id.header);

        helpView = (WebView) findViewById(R.id.helpview);
        helplayout = findViewById(R.id.helplayout);
        closehelp = (TextView) findViewById(R.id.closehelp);
        messageDialog = new MessageDialog(mContext);
    }

    public void setthemeforView(){
        activity_list.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        gobutton.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        header.setBackgroundColor(Color.parseColor(Constants.HEADERBAR_COLOR));
        bottomheader.setBackgroundColor(Color.parseColor(Constants.HEADERBAR_COLOR));

        icon_expandslider.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));
        settings.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));
        icon_search.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));
        notification.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));

        allasset.setTextColor(Color.parseColor(Constants.TOPBARICON_COLOR));
        icon_close.setColorFilter(Color.parseColor(Constants.TOPBARICON_COLOR));

        pos1.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        higlighttext.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
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
        //String URL = Constants.COMPANY_BASE_URL+"/HelpFiles/"+lan+"Kanglehelpassetlibrary.htm";
        messageDialog.Showhelppopup(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View Approve) {
                messageDialog.dialogDismiss();
            }
        },URL, false);
    }

    public void initializebottomnavigation(){
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
                    //adCourse.setVisibility(View.VISIBLE);
                } else if(!TextUtils.isEmpty(landingobj.getCourse()) && landingobj.getCourse().equalsIgnoreCase("A")){
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
            }
        }

        bottommenus.setWeightSum(count);

        lpcourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetworkUtils.checkIfNetworkAvailable(AssetListByCategoryActivity.this)){
                    //Toast.makeText(AssetListByCategoryActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,CourseListActivity.class);
                startActivity(i);
                finish();
            }
        });

        assetpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*finish();*/
            }
        });

        chklistpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetworkUtils.checkIfNetworkAvailable(AssetListByCategoryActivity.this)){
                    Toast.makeText(AssetListByCategoryActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,ChecklistLandingActivity.class);
                startActivity(i);
                finish();
            }
        });

        taskpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!NetworkUtils.checkIfNetworkAvailable(AssetListByCategoryActivity.this)){
                    Toast.makeText(AssetListByCategoryActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,TaskListActivity.class);
                startActivity(i);
                finish();
            }
        });

        profilepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetworkUtils.checkIfNetworkAvailable(AssetListByCategoryActivity.this)) {
                    Toast.makeText(AssetListByCategoryActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(mContext,MoreMenuActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
