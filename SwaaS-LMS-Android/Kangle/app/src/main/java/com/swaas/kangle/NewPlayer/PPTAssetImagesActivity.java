package com.swaas.kangle.NewPlayer;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.R;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.db.DigitalAssetTransactionRepository;
import com.swaas.kangle.models.AssetImages;
import com.swaas.kangle.models.DigitalAssetTransactionMaster;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.models.LstAssetImageModel;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PPTAssetImagesActivity extends AppCompatActivity implements LocationListener {

    EmptyRecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    Assetadapter viewAdapter;
    Context mContext;
    DigitalAssetsMaster assetdata;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;
    DigitalAssetTransactionMaster digitalAssetTransactionMaster;
    DigitalAssetTransactionRepository digitalAssetTransactionRepository;
    ArrayList<AssetImages> newassetimages;
    ArrayList<LstAssetImageModel> newOfflineImages;
    ImageView backclick;
    TextView slidenumber;
    LinearLayout header;
    long starttime,endtime;
    public double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_clicked_asset);

        mContext = PPTAssetImagesActivity.this;
//        getSupportActionBar().hide();
        initialiseViews();
        setUpRecyclerView();
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(mContext);
        digitalAssetTransactionMaster = new DigitalAssetTransactionMaster();
        digitalAssetTransactionRepository = new DigitalAssetTransactionRepository(mContext);
        if(getIntent() != null){
            assetdata = (DigitalAssetsMaster) getIntent().getSerializableExtra("Asset");
            newassetimages = (ArrayList<AssetImages>) getIntent().getSerializableExtra("AssetImages");
            newOfflineImages = (ArrayList<LstAssetImageModel>) getIntent().getSerializableExtra("AssetPPTSlides");
        }
        bindClickEvents();
       /* if(PreferenceUtils.getSubdomainName(mContext).contains("tacobell")){
            header.setBackgroundColor(getResources().getColor(R.color.tacobellbackground));
        }else {
            header.setBackgroundColor(getResources().getColor(R.color.colorgreenbar));
        }*/
        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));

        /*if(PreferenceUtils.getStartTime(mContext)==0){
            starttime = new Date().getTime();
        }*/
        if(PreferenceUtils.getStartTime(mContext) != Long.parseLong(String.valueOf(0))){
            starttime = PreferenceUtils.getStartTime(mContext);
            endtime = PreferenceUtils.getEndTime(mContext);
        }
        loadImages();
    }

    public void initialiseViews(){
        recyclerView = (EmptyRecyclerView) findViewById(R.id.playerrecyclerLayout);
        backclick = (ImageView) findViewById(R.id.playerbackbutton);
        slidenumber = (TextView) findViewById(R.id.slidenumber);
        header = (LinearLayout) findViewById(R.id.header);
    }

    public void bindClickEvents(){
        backclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InsertAnalytics()){
                    finish();
                }else{
                    InsertAnalytics();
                    finish();
                }
            }
        });
        if(newassetimages!= null) {
            slidenumber.setText(newassetimages.get(0).lstAssetImageModel.size() + "/" + newassetimages.get(0).lstAssetImageModel.size());
        }else if(newOfflineImages != null){
            slidenumber.setText(newOfflineImages.size() + "/" + newOfflineImages.size());
        }
    }

    public void setUpRecyclerView(){
        if(isTablet()){
            gridLayoutManager = new GridLayoutManager(this, 5);
        }else {
            gridLayoutManager = new GridLayoutManager(this, 3);
        }
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.scrollToPosition(0);
    }

    public boolean isTablet() {
        return (mContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public void loadImages(){
        if(newOfflineImages != null && newOfflineImages.size()>0){
            ArrayList<LstAssetImageModel> pptimagesset = newOfflineImages;
            viewAdapter = new Assetadapter(PPTAssetImagesActivity.this, pptimagesset);
            recyclerView.setAdapter(viewAdapter);
        }else {
            ArrayList<AssetImages> pptimages = newassetimages;
            ArrayList<LstAssetImageModel> pptimagesset = (ArrayList<LstAssetImageModel>) pptimages.get(0).lstAssetImageModel;
            viewAdapter = new Assetadapter(PPTAssetImagesActivity.this, pptimagesset);
            recyclerView.setAdapter(viewAdapter);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public boolean InsertAnalytics(){
        boolean success = false;
        long time = Long.parseLong(String.valueOf(0));;
        if(newassetimages != null){
            DigitalAssetTransactionMaster digitalAssetTransactionMaster = new DigitalAssetTransactionMaster();
            if(PreferenceUtils.getStartTime(mContext) > PreferenceUtils.getEndTime(mContext)){
                time = Long.parseLong(String.valueOf(0));
            }else{
                time = PreferenceUtils.getEndTime(mContext) - PreferenceUtils.getStartTime(mContext);
            }
            digitalAssetTransactionMaster.setPlaytime(time);
            digitalAssetTransactionMaster.setRecorddate(new Date().toString());
            digitalAssetTransactionMaster.setDAID(newassetimages.get(0).getDA_Code());
            digitalAssetTransactionMaster.setIs_Read(true);
            digitalAssetTransactionMaster.setOnlinePlay("1");
            digitalAssetTransactionMaster.setOfflinePlay("0");
            digitalAssetTransactionMaster.setLattitude(String.valueOf(latitude));
            digitalAssetTransactionMaster.setLongitude(String.valueOf(longitude));
            digitalAssetTransactionMaster.setTotalViews(assetdata.getTotalViews() + 1);
            List<DigitalAssetTransactionMaster> list = new ArrayList<>();
            list.add(digitalAssetTransactionMaster);
            digitalAssetTransactionRepository.bulkInsert(list);
            success = true;
        }else if(newOfflineImages != null){
            DigitalAssetTransactionMaster digitalAssetTransactionMaster = new DigitalAssetTransactionMaster();
            if(PreferenceUtils.getStartTime(mContext) > PreferenceUtils.getEndTime(mContext)){
                time = Long.parseLong(String.valueOf(0));
            }else{
                time = PreferenceUtils.getEndTime(mContext) - PreferenceUtils.getStartTime(mContext);
            }
            digitalAssetTransactionMaster.setPlaytime(time);
            digitalAssetTransactionMaster.setRecorddate(new Date().toString());
            digitalAssetTransactionMaster.setDAID(newOfflineImages.get(0).getDA_Code());
            digitalAssetTransactionMaster.setIs_Read(true);
            digitalAssetTransactionMaster.setOfflinePlay("1");
            digitalAssetTransactionMaster.setOnlinePlay("0");
            digitalAssetTransactionMaster.setLattitude(String.valueOf(latitude));
            digitalAssetTransactionMaster.setLongitude(String.valueOf(longitude));
            digitalAssetTransactionMaster.setTotalViews(assetdata.getTotalViews()+1);
            List<DigitalAssetTransactionMaster> list = new ArrayList<>();
            list.add(digitalAssetTransactionMaster);
            digitalAssetTransactionRepository.bulkInsert(list);
            success = true;
        }else {
            success = false;
        }
        PreferenceUtils.setStartTime(mContext, Long.parseLong(String.valueOf(0)));
        PreferenceUtils.setEndTime(mContext, Long.parseLong(String.valueOf(0)));
        return success;
    }
}
