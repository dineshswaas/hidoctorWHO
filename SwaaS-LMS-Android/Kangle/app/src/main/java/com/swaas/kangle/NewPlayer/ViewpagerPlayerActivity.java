package com.swaas.kangle.NewPlayer;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.swaas.kangle.R;
import com.swaas.kangle.db.DigitalAssetTransactionChildRepository;
import com.swaas.kangle.db.DigitalAssetTransactionRepository;
import com.swaas.kangle.models.DigitalAssetTransactionChild;
import com.swaas.kangle.models.DigitalAssetTransactionMaster;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.models.LstAssetImageModel;
import com.swaas.kangle.preferences.PreferenceUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewpagerPlayerActivity extends AppCompatActivity implements LocationListener {

    ViewpagerPlayerActivity mActivity;
    public ViewPager pager;
    ViewImagePagerAdapter imagePagerAdapter;
    List<LstAssetImageModel> pptAssetImages;
    DigitalAssetsMaster mDigitalAssetsMasters;
    List<DigitalAssetsMaster> assets;
    public int curPosition;
    ImageView backclick;
    TextView slidenumber;
    long starttime;
    public double latitude,longitude;

    List<DigitalAssetTransactionMaster> mList;
    DigitalAssetTransactionMaster digitalAssetTransactionMaster;
    List<DigitalAssetTransactionChild> digitalAssetTransactionChild;
    DigitalAssetTransactionRepository digitalAssetTransactionRepository;
    DigitalAssetTransactionChildRepository childRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_player);

        mActivity = ViewpagerPlayerActivity.this;
//        getSupportActionBar().hide();
        initialiseViews();
        pptAssetImages = new ArrayList<LstAssetImageModel>();
        assets = new ArrayList<DigitalAssetsMaster>();
        mList = new ArrayList<DigitalAssetTransactionMaster>();
        digitalAssetTransactionMaster = new DigitalAssetTransactionMaster();
        digitalAssetTransactionChild = new ArrayList<DigitalAssetTransactionChild>();
        digitalAssetTransactionRepository = new DigitalAssetTransactionRepository(mActivity);
        childRepository = new DigitalAssetTransactionChildRepository(mActivity);
        if(getIntent() != null){
            if(getIntent().getStringExtra("isppt").toString().equalsIgnoreCase("assetType")){
                mDigitalAssetsMasters = (DigitalAssetsMaster) getIntent().getSerializableExtra("Asset");
                assets = new ArrayList<DigitalAssetsMaster>();
                assets.add(mDigitalAssetsMasters);
            }else if(getIntent().getStringExtra("isppt").toString().equalsIgnoreCase("PPTImages")) {
                pptAssetImages = (List<LstAssetImageModel>) getIntent().getSerializableExtra("PPTAssetImages");
            }
        }
        curPosition = getIntent().getIntExtra("position",0);

        imagePagerAdapter = new ViewImagePagerAdapter(getSupportFragmentManager(),this);
        if(pptAssetImages.size() > 0){
            for(int assetCount = 0; assetCount< pptAssetImages.size(); assetCount++){
                pptAssetImages.get(assetCount).setPosition(assetCount);
                imagePagerAdapter.addFragment(new PPTImageFragment(),"Image",pptAssetImages.get(assetCount));
            }
        }else if(assets.size()>0){
            for(int assetCount = 0; assetCount< assets.size(); assetCount++){
                assets.get(assetCount).setPosition(assetCount);
                if(assets.get(assetCount).getDA_Type().equalsIgnoreCase("image")) {
                    imagePagerAdapter.addAssetFragmant(new ImageFragment(), "Image", assets.get(assetCount));
                }else if(assets.get(assetCount).getDA_Type().equalsIgnoreCase("video")){
                    imagePagerAdapter.addAssetFragmant(new VideoFragment(),"Image",assets.get(assetCount));
                }else if(assets.get(assetCount).getDA_Type().equalsIgnoreCase("articulate") ||
                        assets.get(assetCount).getDA_Type().equalsIgnoreCase("html5")){
                    imagePagerAdapter.addAssetFragmant(new WebviewFragment(),"Image",assets.get(assetCount));
                }
            }
        }

        if(PreferenceUtils.getStartTime(mActivity) == Long.parseLong(String.valueOf(0))){
            PreferenceUtils.setStartTime(mActivity,new Date().getTime());
            starttime = new Date().getTime();
            Log.d("starttime",String.valueOf(starttime));
        }

        pager.setAdapter(imagePagerAdapter);
        pager.setCurrentItem(curPosition);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int selectedIndex = 0;
            boolean closeactivity = false;
            int prevposition = curPosition;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                selectedIndex = position;
                if(assets.size()>0) {
                    slidenumber.setText(position + 1 + "/" + assets.size());
                }else if(pptAssetImages.size()>0){
                    slidenumber.setText(position + 1 + "/" + pptAssetImages.size());
                }
                if(closeactivity){
                    //insertanalytics(curPosition);
                }
                if(selectedIndex == pptAssetImages.size()-1) {
                    //closeactivity = true;
                }
            }

            @Override
            public void onPageSelected(final int position) {
                Fragment f = imagePagerAdapter.getItem(position);
                curPosition = position;
                if(f instanceof ImageFragment){
                    ((ImageFragment)f).loadImage();
                }else if(f instanceof PPTImageFragment){
                    digitalAssetTransactionChild.add(((PPTImageFragment)f).insertChildAnalytics(pptAssetImages.get(prevposition).getImage_Id()));
                    ((PPTImageFragment)f).setPreferenceUtilEndtime();
                    ((PPTImageFragment)f).loadImage();
                    prevposition = position;
                }else if(f instanceof VideoFragment){
                    ((VideoFragment)f).loadVideo();
                }else if(f instanceof WebviewFragment){
                    ((WebviewFragment)f).loadWebView();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        backclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertanalytics(curPosition);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public void insertanalytics(int position){
        Fragment f = imagePagerAdapter.getItem(curPosition);

        if(f instanceof ImageFragment){
            digitalAssetTransactionMaster = ((ImageFragment)f).getAnalytics();
            digitalAssetTransactionMaster.setLattitude(String.valueOf(latitude));
            digitalAssetTransactionMaster.setLongitude(String.valueOf(longitude));
            mList.add(digitalAssetTransactionMaster);
            insertintoTabel(mList);
        }else if(f instanceof PPTImageFragment){
            digitalAssetTransactionChild.add(((PPTImageFragment) f).insertChildAnalytics(pptAssetImages.get(position).getImage_Id()));
            ((PPTImageFragment)f).setPreferenceUtilEndtime();
            //digitalAssetTransactionMaster = ((PPTImageFragment)f).getAnalytics();
            //digitalAssetTransactionChild.add(((PPTImageFragment)f).getChildAnalytics());
            childRepository = new DigitalAssetTransactionChildRepository(mActivity);
            if(digitalAssetTransactionChild.size()>0){
                childRepository.bulkInsert(digitalAssetTransactionChild);
            }
            finish();
        }else if(f instanceof VideoFragment){
            digitalAssetTransactionMaster = ((VideoFragment)f).getAnalytics();
            digitalAssetTransactionMaster.setLattitude(String.valueOf(latitude));
            digitalAssetTransactionMaster.setLongitude(String.valueOf(longitude));
            mList.add(digitalAssetTransactionMaster);
            insertintoTabel(mList);
        }else if(f instanceof WebviewFragment){
            digitalAssetTransactionMaster = ((WebviewFragment)f).getAnalytics();
            digitalAssetTransactionMaster.setLattitude(String.valueOf(latitude));
            digitalAssetTransactionMaster.setLongitude(String.valueOf(longitude));
            mList.add(digitalAssetTransactionMaster);
            insertintoTabel(mList);
        }
    }

    public void insertintoTabel(List<DigitalAssetTransactionMaster> list){
        digitalAssetTransactionRepository.bulkInsert(list);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initialiseViews(){
        pager = (ViewPager) findViewById(R.id.viewPager);
        backclick = (ImageView) findViewById(R.id.playerbackbutton);
        slidenumber = (TextView) findViewById(R.id.slidenumber);
    }
}
