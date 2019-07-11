package com.swaas.kangle.LPCourse;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.db.DigitalAssetRepository;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.playerPart.DigitalAssets;
import com.swaas.kangle.playerPart.customviews.pdf.util.Constants;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Sayellessh on 18-08-2017.
 */

public class AssetAnalyticsUpsynctoServer {

    Context mContext;
    SectionActivity mActivity;
    ArrayList<DigitalAssets> digitalAssetList;
    DigitalAssetRepository digitalAssetRepository;
    int i , j ,indexSync=0,indexSyncListSize = 0;
    boolean isfromSection;
    List<CourseAnalyticsModel> assetanalyticslist= new ArrayList<>();
    public void AssetAnalyticsUpsynctoServer(Context context,SectionActivity sectionActivity){
        mContext = context;
        digitalAssetList = new ArrayList<>();
        mActivity = sectionActivity;
    }

    public void AssetAnalyticsUpsynctoServer(Context context){
        mContext = context;
        digitalAssetList = new ArrayList<>();
    }

    public void getAnalyticsfromDb(final boolean status,final int Assignment_Id,final int Section_Mapping_Id){
        i = 0; j = 0;
        isfromSection = status;
        digitalAssetRepository = new DigitalAssetRepository(mContext);
        digitalAssetRepository.setmGetDA(new DigitalAssetRepository.GetDA() {
            @Override
            public void getDASuccess(ArrayList<DigitalAssets> digitalAssetsList) {
                digitalAssetList = digitalAssetsList;
                Gson gsonget = new Gson();
                User userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);

                if(digitalAssetsList.size() > 0){
                    for (DigitalAssets digitalAssets : digitalAssetsList){
                        CourseAnalyticsModel assetanalytics = new CourseAnalyticsModel();
                        assetanalytics.set_Id(digitalAssets.getId());
                        assetanalytics.setCompany_Id(userobj.getCompany_Id());
                        assetanalytics.setDA_Code(String.valueOf(digitalAssets.getDA_Code()));
                        assetanalytics.setUser_Id(String.valueOf(userobj.getUserID()));
                        assetanalytics.setUser_Name(userobj.getFirstName());
                        assetanalytics.setRegion_Code(userobj.getRegion_Code());
                        assetanalytics.setRegion_Name(userobj.getRegion_Name());
                        assetanalytics.setOnline_Play(1);
                        assetanalytics.setOffline_Play(0);
                        assetanalytics.setDoc_Type(getDaType(digitalAssets.getDA_Type()));
                        assetanalytics.setPlay_Time(String.valueOf(digitalAssets.getPlayed_Time_Duration()));
                        assetanalytics.setIs_Preview(false);
                        assetanalytics.setCourse_Id(digitalAssets.getCourse_Id());
                        assetanalytics.setSection_Id(digitalAssets.getSection_Id());
                        assetanalytics.setPublish_Id(digitalAssets.getPublish_Id());
                        assetanalytics.setImage_Id(digitalAssets.getPart_Id());
                        assetanalytics.setLocal_TimeZone(String.valueOf(new Date()));
                        assetanalytics.setCourse_User_Assignment_Id(Assignment_Id);
                        assetanalytics.setCouse_User_Section_Mapping_Id(Section_Mapping_Id);
                        assetanalyticslist.add(assetanalytics);
                    }

                    indexSyncListSize = assetanalyticslist.size();
                    insertAnalyticsToServer(assetanalyticslist.get(indexSync));

                }else{
                        if(status) {
                            if(mActivity != null)
                            {
                                mActivity.getListOfSections();
                            }

                        }
                    }

            }

            @Override
            public void getDAFailure(String message) {

            }
        });
        digitalAssetRepository.getUnSyncedDigitalAssetAnalytics();
    }

    public String getDaType(int type){
        String typename = "";
        //This type is used for the purpose of webservice for upsyncservice
        if(type == Constants.IMAGEASSET)
            typename = "jpg";
        else if(type == Constants.VIDEOASSET)
            typename = "mp4";
        else if(type == Constants.AUDIOASSET)
            typename = "mp3";
        else if(type == Constants.PDFASSET)
            typename = "jpg";
        else if(type == Constants.HTMLASSET)
            typename = "html";
        else if(type == Constants.BRIGHTCOVE)
            typename = "mp4";
        return typename;
    }

    public void insertAnalyticsToServer(final CourseAnalyticsModel assetanalytic){
        if(NetworkUtils.checkIfNetworkAvailable(mContext)){
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            LPCourseService lpService = retrofitAPI.create(LPCourseService.class);
            Call call = lpService.insertLPCourseViewAnalytics(PreferenceUtils.getSubdomainName(mContext),assetanalytic);
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    String courseAssetListModel= response.body();
                    if (courseAssetListModel != null) {
                        digitalAssetRepository.updateSyncedDigitalAssetAnalytics(assetanalytic.get_Id());
                        indexSync++;
                        if(indexSync < indexSyncListSize){
                            insertAnalyticsToServer(assetanalyticslist.get(indexSync));
                        }else{
                            indexSync = 0;
                            insertSecondAnalyticsToServer(assetanalyticslist.get(indexSync));
                        }



                       /* i++;
                        //if(i>(assetanalytic.size()-1)){
                        if(assetanalytic.size()>i){
                            insertAnalyticsToServer(assetanalytic);
                        }else {
                            insertSecondAnalyticsToServer(assetanalytic);
                        }*/
                    } else {
                        //courseListModel is null
                        Toast.makeText(mContext, "Section List is null", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(),"Error");
                }
            });
        }else{
            Toast.makeText(mContext, "no network", Toast.LENGTH_SHORT).show();
        }
    }

    public void insertSecondAnalyticsToServer(final CourseAnalyticsModel assetanalytic){
        if(NetworkUtils.checkIfNetworkAvailable(mContext)){
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            LPCourseService lpService = retrofitAPI.create(LPCourseService.class);
            Call call = lpService.insertAssetMappingMaterialLog(PreferenceUtils.getSubdomainName(mContext),assetanalytic);
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    String courseAssetListModel= response.body();
                    if (courseAssetListModel != null) {
                        digitalAssetRepository.deleteSyncedDigitalAssetAnalytics(assetanalytic.get_Id());
                        indexSync++;
                        if(indexSync < indexSyncListSize){
                            insertSecondAnalyticsToServer(assetanalyticslist.get(indexSync));
                        }else{
                            if(isfromSection) {
                                if(mActivity != null)
                                {
                                    mActivity.getListOfSections();
                                }

                            }
                        }
                    }else{
                        if(mActivity != null)
                        {
                            mActivity.getListOfSections();
                        }

                    }
                }
                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(),"Error");
                }
            });
        }else{
            if(mActivity != null)
            {
                mActivity.getListOfSections();
            }

        }
    }
}
