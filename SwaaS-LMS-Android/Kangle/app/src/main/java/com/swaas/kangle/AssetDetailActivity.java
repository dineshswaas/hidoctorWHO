package com.swaas.kangle;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.GlobalPlayer.GlobalPlayer;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.db.DigitalAssetTransactionRepository;
import com.swaas.kangle.fragmants.AssetRelatedAssetfragment;
import com.swaas.kangle.fragmants.AssetRelatedDescriptionfragment;
import com.swaas.kangle.fragmants.AssetRelatedDiscussionsfragment;
import com.swaas.kangle.models.DigitalAssetTransactionMaster;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.models.TagAnalytics;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.NetworkUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class AssetDetailActivity extends AppCompatActivity {

    ImageView companylogo,notification,settings,thumbnail;
    Button backbutton,playbutton,downloadbutton;
    private TabLayout tablayout;
    private ViewPager viewpager;
    Context mContext;
    DigitalAssetsMaster digitalAssetsMaster;
    String daid;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;
    DigitalAssetTransactionMaster digitalAssetTransactionMaster;
    DigitalAssetTransactionRepository digitalAssetTransactionRepository;
    String extension = null;
    String filename = null;
    String offlineurl;
    AppBarLayout appbarlayout;
    CoordinatorLayout activity_asset_detail;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_detail);

        mContext = AssetDetailActivity.this;
        //getSupportActionBar().hide();
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(mContext);
        digitalAssetTransactionRepository = new DigitalAssetTransactionRepository(mContext);
        if(getIntent() != null){
            daid = getIntent().getStringExtra("DAID");
        }
        initializeView();
        bindOnClickEvents();
        getAssetDetail(daid);
        setupViewPager(viewpager);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.setSelectedTabIndicatorHeight((int) (4 * getResources().getDisplayMetrics().density));
        tablayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(0);
    }
    public void getAssetDetail(String daid){
        digitalAssetsMaster = digitalAssetHeaderRepository.getAssetDetail(daid);
        digitalAssetTransactionMaster = new DigitalAssetTransactionMaster();
        digitalAssetTransactionMaster.setDAID(digitalAssetsMaster.getDAID());
        loadView();
    }

    public void loadView(){
        if(digitalAssetsMaster != null){
            if(digitalAssetsMaster.getIsDownloadable().equalsIgnoreCase("y")){
                downloadbutton.setVisibility(View.VISIBLE);
            }
            if(digitalAssetsMaster.getIsViewable().equalsIgnoreCase("y")){
                playbutton.setVisibility(View.VISIBLE);
            }

            if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("video")) {
                thumbnail.setImageResource(R.drawable.icon_mp4);
            } else if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("articulate")) {
                thumbnail.setImageResource(R.drawable.icon_html);
            } else if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("image")) {
                thumbnail.setImageResource(R.drawable.icon_jpeg);
            } else if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("document")) {
                if(digitalAssetsMaster.getOnlineURL().endsWith(".pdf")){
                    thumbnail.setImageResource(R.drawable.icon_pdf);
                } else if(digitalAssetsMaster.getOnlineURL().endsWith(".ppt") || digitalAssetsMaster.getOnlineURL().endsWith(".pptx")){
                    thumbnail.setImageResource(R.drawable.icon_ppt2);
                } else{
                    thumbnail.setImageResource(R.drawable.icon_doc);
                }
            }

        }
    }

    public void bindOnClickEvents(){
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<DigitalAssetTransactionMaster> digassets = digitalAssetTransactionRepository.getAllValues();
                Gson gsonget = new Gson();
                User userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);
                TagAnalytics tagAnalytics = new TagAnalytics();
                for(DigitalAssetTransactionMaster digital : digassets){
                    tagAnalytics.setDA_Tag_Analysic_Id(digital.getSlNo());
                    tagAnalytics.setCompany_Code(userobj.getCompany_Code());
                    tagAnalytics.setDA_ID(digital.getDAID());
                    tagAnalytics.setUser_Code(userobj.getUser_Code());
                    tagAnalytics.setUser_Name(userobj.getEmployee_Name());
                    tagAnalytics.setRegion_Code(userobj.getRegion_Code());
                    tagAnalytics.setRegion_Name(userobj.getRegion_Name());
                    tagAnalytics.setDateTime(digital.getRecorddate());
                    tagAnalytics.setDislike(0);
                    tagAnalytics.setLike(digital.getUser_Like());
                    tagAnalytics.setRating(digital.getUser_Rating());
                    tagAnalytics.setCompany_Id(userobj.getCompany_Id());
                }


                //Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
                //AssetService assetService = retrofitAPI.create(AssetService.class);
                /*Call call = assetService.postComments();
                call.enqueue(new Callback<ArrayList<TagAnalytics>>() {

                    @Override
                    public void onResponse(Response<ArrayList<TagAnalytics>> response, Retrofit retrofit) {
                        ArrayList<TagAnalytics> apiResponse= response.body();
                        if (apiResponse != null) {

                        } else {
                            Log.d("retrofit","error 2");
                            //error
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.d("retrofit","error 2");
                    }
                });*/
                finish();
            }
        });

        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,GlobalPlayer.class);
                intent.putExtra("DAID",digitalAssetsMaster.getDAID());
                startActivity(intent);
                finish();
            }
        });

        downloadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtils.checkIfNetworkAvailable(mContext)){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        int storagePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
                        if (storagePermission == PackageManager.PERMISSION_GRANTED) {
                            startDownload(digitalAssetsMaster);
                        } else {
                            AssetDetailActivity.this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_PERMISSION_STORAGE);
                        }
                    } else {
                        startDownload(digitalAssetsMaster);
                    }
                } else{
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.downloadmessage), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void syncdata(){

    }

    public void initializeView() {
        companylogo = (ImageView) findViewById(R.id.companylogo);
        notification= (ImageView) findViewById(R.id.notification);
        settings = (ImageView) findViewById(R.id.settings);
        backbutton = (Button) findViewById(R.id.back);
        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        downloadbutton = (Button) findViewById(R.id.download_button);
        playbutton = (Button) findViewById(R.id.play_button);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        appbarlayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        activity_asset_detail = (CoordinatorLayout) findViewById(R.id.activity_asset_detail);

        if(PreferenceUtils.getSubdomainName(mContext).equalsIgnoreCase("tacobelltest")){
            appbarlayout.setBackgroundColor(getResources().getColor(R.color.tacobellbackground));
            activity_asset_detail.setBackgroundColor(getResources().getColor(R.color.tacobellbackground));
        }else{
            appbarlayout.setBackgroundColor(getResources().getColor(R.color.otherCompanies));
            activity_asset_detail.setBackgroundColor(getResources().getColor(R.color.otherCompanies));
        }

    }

    private void setupViewPager(ViewPager doctors_viewPager) {

        DetailsTabsPagerAdapter assetDetailsViewPager = new DetailsTabsPagerAdapter(getSupportFragmentManager());

        assetDetailsViewPager.addFragment(new AssetRelatedDescriptionfragment(), "Details");
        assetDetailsViewPager.addFragment(new AssetRelatedDiscussionsfragment(), "Discussion");
        assetDetailsViewPager.addFragment(new AssetRelatedAssetfragment(), "Related");
        doctors_viewPager.setAdapter(assetDetailsViewPager);
        doctors_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class DetailsTabsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public DetailsTabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        public Fragment getItem(int index) {

            switch (index) {
                case 0:
                    AssetRelatedDescriptionfragment descriptionfragment = new AssetRelatedDescriptionfragment();
                    Bundle masterBundle = new Bundle();
                    masterBundle.putSerializable("assetDetails", digitalAssetsMaster);
                    descriptionfragment.setArguments(masterBundle);
                    return descriptionfragment;
                case 1:
                    AssetRelatedDiscussionsfragment discussionsfragment = new AssetRelatedDiscussionsfragment();
                    Bundle discussionbundle = new Bundle();
                    discussionbundle.putSerializable("assetDetails", digitalAssetsMaster);
                    discussionsfragment.setArguments(discussionbundle);
                    return discussionsfragment;
                case 2:
                    AssetRelatedAssetfragment relatedAssetfragment = new AssetRelatedAssetfragment();
                    Bundle relatedbundle = new Bundle();
                    relatedbundle.putSerializable("assetDetails", digitalAssetsMaster);
                    relatedAssetfragment.setArguments(relatedbundle);
                    return relatedAssetfragment;
            }

            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    private void startDownload(DigitalAssetsMaster asset) {
        String url = asset.getOnlineURL();
        extension = url.substring(url.lastIndexOf("."));
        filename = asset.getDAName();
        new DownloadFileAsync().execute(url);
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;
            String outputfilepath = null;
            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int lenghtOfFile = conexion.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());
                String assetname = "KAngle";
                File createassetfolder = new File("/sdcard/GlobalPlayer/asset/"+assetname+"/");
                if(assetname!= null && !createassetfolder.exists()) {
                    createassetfolder.mkdirs();
                }
                OutputStream output = new FileOutputStream("/sdcard/GlobalPlayer/asset/"+assetname+"/"+filename+""+extension);
                outputfilepath = "/sdcard/GlobalPlayer/asset/"+assetname+"/"+filename+""+extension;
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Exception",e.toString());
            }
            return outputfilepath;

        }
        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String url) {
            offlineurl = url;
            updateasset(offlineurl);
            Toast.makeText(mContext,mContext.getResources().getString(R.string.downloadcomplete), LENGTH_SHORT).show();
        }
    }

    public void updateasset(String offlineurl){
        dismissProgressDialog();
        if(offlineurl != null){
            digitalAssetsMaster.setOfflineURL(offlineurl);
            digitalAssetsMaster.setIs_Downloaded(true);
            //digitalAssetHeaderRepository.updatedownloaded(digitalAssetsMaster);
            digitalAssetTransactionMaster.setOfflineURL(offlineurl);
            digitalAssetTransactionMaster.setIs_Downloaded(true);
            digitalAssetTransactionRepository.insertOrUpdateRecord(digitalAssetTransactionMaster,"download");

        }
    }

    public void showProgressDialog(){
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Downloading Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    public void dismissProgressDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }
}
