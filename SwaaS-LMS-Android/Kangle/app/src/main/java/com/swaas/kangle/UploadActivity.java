package com.swaas.kangle;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;

import com.google.gson.Gson;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.API.service.AssetService;
import com.swaas.kangle.API.service.UserTrackerService;
import com.swaas.kangle.LPCourse.LPCourseService;
import com.swaas.kangle.LPCourse.model.AnwerUploadModel;
import com.swaas.kangle.LPCourse.model.TestResultModel;
import com.swaas.kangle.LPCourse.questionbuilder.db.TestResultRepository;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.db.DigitalAssetTransactionChildRepository;
import com.swaas.kangle.db.DigitalAssetTransactionRepository;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.db.UserTrackertableRepository;
import com.swaas.kangle.models.ChildAnalyticsModel;
import com.swaas.kangle.models.DigitalAssetTransactionChild;
import com.swaas.kangle.models.DigitalAssetTransactionMaster;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.models.TagAnalytics;
import com.swaas.kangle.models.UserTrackingModel;
import com.swaas.kangle.preferences.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Sayellessh on 19-05-2017.
 */

public class UploadActivity {

    Context mContext;
    private ProgressDialog pDialog;
    Gson gsonget;
    Retrofit retrofitAPI;
    AssetService assetService;
    UserTrackerService userTrackerService;
    DigitalAssetsMaster digitalAssetsMaster;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;
    DigitalAssetTransactionMaster digitalAssetTransactionMaster;
    DigitalAssetTransactionChild digitalAssetTransactionChild;
    DigitalAssetTransactionRepository digitalAssetTransactionRepository;
    DigitalAssetTransactionChildRepository digitalAssetTransactionChildRepository;
    ProgressDialog mProgressDialog;
    boolean erroroccured = false;
    boolean erroroccured2 = false;
    List<DigitalAssetTransactionMaster> digassets;
    List<DigitalAssetTransactionMaster> uploadeddigassets;
    List<DigitalAssetTransactionChild> uploadchildassets;
    List<UserTrackingModel> uploadPageAnalytics;
    List<TestResultModel> TestresultList;
    int num = 0;
    int next = 0;
    int numnext = 0;
    int childno = 0;
    int testrecnum = 0;
    List<TagAnalytics> tagAnalytics;
    List<TagAnalytics> tagAnalytics1;

    WebSettings webSettings;
    User userobj;
    UserTrackertableRepository trackertableRepository;
    UserTrackingModel pagevisits;

    LPCourseService lpCourseService;
    TestResultRepository testResultRepository;

    public UploadActivity(Context context){
        mContext = context;
        gsonget = new Gson();
        retrofitAPI = RetrofitAPIBuilder.getInstance();
        assetService = retrofitAPI.create(AssetService.class);
        digitalAssetsMaster = new DigitalAssetsMaster();
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(mContext);
        digitalAssetTransactionRepository = new DigitalAssetTransactionRepository(mContext);
        digitalAssetTransactionChildRepository = new DigitalAssetTransactionChildRepository(mContext);
        digitalAssetTransactionMaster = new DigitalAssetTransactionMaster();
        trackertableRepository = new UserTrackertableRepository(mContext);
        testResultRepository = new TestResultRepository(mContext);
        pagevisits = new UserTrackingModel();
        userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);
    }

    //Tag Analytics Upsync Section

    public void uploadTagAnalytics() {
        digassets = digitalAssetTransactionRepository.getAllValues();
        if(digassets.size()>0){
            //showProgressDialog();
            indexFunction(num);
        }else{
            //showProgressDialog();
            uploadAssetAnalytics();
        }
    }

    public void indexFunction(int index) {
        DigitalAssetTransactionMaster dig = digassets.get(index);
        Gson gsonget = new Gson();
        String subdomain = PreferenceUtils.getSubdomainName(mContext);
        User userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);
        tagAnalytics = new ArrayList<TagAnalytics>();
        TagAnalytics tag = new TagAnalytics();
        if(dig != null){
            digitalAssetTransactionMaster = dig;
            tag.setCompany_Code(userobj.getCompany_Code());
            tag.setCompany_Id(userobj.getCompany_Id());
            tag.setCorrelationId("1");
            tag.setAppPlatform("Android");
            tag.setAppSuiteId("1");
            tag.setAppId("2");
            tag.setDACode(Long.parseLong(dig.getDAID()));
            tag.setUser_Code(userobj.getUser_Code());
            tag.setUser_Name(userobj.getEmployee_Name());
            tag.setRegion_Code(userobj.getRegion_Code());
            tag.setRegion_Name(userobj.getRegion_Name());
            tag.setLike(dig.getUser_Like());
            tag.setDislike(0);
            tag.setRating(dig.getUser_Rating());
        }
        tagAnalytics.add(tag);

        retrofitAPI = RetrofitAPIBuilder.getInstance();
        assetService = retrofitAPI.create(AssetService.class);
        Call call = assetService.insertTaganalytics(subdomain,tag);
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                String apiResponse= response.body();
                if (apiResponse != null && apiResponse.equalsIgnoreCase("true")) {
                    try {
                        digitalAssetTransactionRepository.updateRecord(digitalAssetTransactionMaster.getSlNo());
                        if (digassets.size() == num + 1) {
                            uploadAssetAnalytics();
                        } else {
                            num++;
                            indexFunction(num);
                        }
                    }catch (Exception e){
                        Log.d("",e.toString());
                    }
                } else {
                    erroroccured = true;
                    dismissProgressDialog();
                    Log.d("retrofit","error 2");
                    //error
                }
            }

            @Override
            public void onFailure(Throwable t) {
                erroroccured = true;
                dismissProgressDialog();
                Log.d("retrofit","error 2");
            }
        });
    }

    //Asset Analytics Upsync Section

    public void uploadAssetAnalytics(){
        uploadeddigassets = digitalAssetTransactionRepository.getAllUpdatedAssetValues();
        if(uploadeddigassets.size()>0){
            functionUploadIndex(next);
        }else{
            dismissProgressDialog();
        }
    }

    public void functionUploadIndex(int index){
        DigitalAssetTransactionMaster analyticsasset = uploadeddigassets.get(index);
        int value = 0;
        Gson gsonget = new Gson();
        String subdomain = PreferenceUtils.getSubdomainName(mContext);
        User userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);
        tagAnalytics1 = new ArrayList<TagAnalytics>();
        TagAnalytics tagAnalytics = new TagAnalytics();
        if(analyticsasset != null){
            digitalAssetTransactionMaster = analyticsasset;
            tagAnalytics.setCompany_Code(userobj.getCompany_Code());
            tagAnalytics.setCompany_Id(userobj.getCompany_Id());
            tagAnalytics.setCorrelationId("1");
            tagAnalytics.setAppPlatform("Android");
            tagAnalytics.setAppSuiteId("1");
            tagAnalytics.setAppId("2");
            tagAnalytics.setDACode(Long.parseLong(analyticsasset.getDAID()));
            tagAnalytics.setUser_Code(userobj.getUser_Code());
            tagAnalytics.setUser_Name(userobj.getEmployee_Name());
            tagAnalytics.setRegion_Code(userobj.getRegion_Code());
            tagAnalytics.setRegion_Name(userobj.getRegion_Name());
            if(userobj.getDivision_Code() == null
                    || userobj.getDivision_Code().equalsIgnoreCase("")){
                tagAnalytics.setDivisionCode("0");
            }else{
                tagAnalytics.setDivisionCode(userobj.getDivision_Code());
            }
            tagAnalytics.setDivisionName(userobj.getDivision_Name());
            tagAnalytics.setOfflinePlay(analyticsasset.getOfflinePlay());
            tagAnalytics.setOnlinePlay(analyticsasset.getOnlinePlay());
            if(analyticsasset.Is_Downloaded){
                value = 1;
            } else {
                value = 0;
            }
            tagAnalytics.setDownload(String.valueOf(value));
            tagAnalytics.setPlayTime(String.valueOf(analyticsasset.getPlaytime()));
            tagAnalytics.setLattitude(analyticsasset.getLattitude());
            tagAnalytics.setLongitude(analyticsasset.getLongitude());

            tagAnalytics1.add(tagAnalytics);

            retrofitAPI = RetrofitAPIBuilder.getInstance();
            assetService = retrofitAPI.create(AssetService.class);
            Call call = assetService.insertELItemizedBilling(subdomain,tagAnalytics);
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    String apiResponse= response.body();
                    if (apiResponse != null && apiResponse.equalsIgnoreCase("true")) {
                        try{
                            digitalAssetTransactionRepository.deleteRecord(digitalAssetTransactionMaster.getSlNo());
                            if(next+1 >= digassets.size()){
                                uploadChildAnalytics();
                            }else{
                                next++;
                                functionUploadIndex(next);
                            }
                        }catch(Exception e){
                            Log.d("error",e.toString());
                        }
                    } else {
                        dismissProgressDialog();
                        Log.d("retrofit","error 2");
                        //error
                    }
                }

                @Override
                    public void onFailure(Throwable t) {
                        Log.d("retrofit","error 2");
                    dismissProgressDialog();
                }
            });
        }
    }

    //Child Analytics Upsync Section

    public void uploadChildAnalytics(){
        uploadchildassets = digitalAssetTransactionChildRepository.getAllValues();
        if(uploadchildassets.size()>0){
            functionChildIndex(childno);
        }else{
            dismissProgressDialog();
        }
    }

    public void functionChildIndex(int index){
        DigitalAssetTransactionChild analyticsasset = uploadchildassets.get(index);

        Gson gsonget = new Gson();
        String subdomain = PreferenceUtils.getSubdomainName(mContext);
        User userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);

        ChildAnalyticsModel CHILDanalytics = new ChildAnalyticsModel();
        if(analyticsasset != null){
            digitalAssetTransactionChild = analyticsasset;
            CHILDanalytics.setCompany_Id(userobj.getCompany_Id());
            CHILDanalytics.setAsset_Id(Integer.parseInt(analyticsasset.getDAID()));
            CHILDanalytics.setImage_Id(analyticsasset.getSlideNo());
            CHILDanalytics.setDuration(analyticsasset.getPlaytime());
            CHILDanalytics.setViewed_By(userobj.getUserID());
            CHILDanalytics.setLocal_Time_Zone(analyticsasset.getRecordDate());

            retrofitAPI = RetrofitAPIBuilder.getInstance();
            assetService = retrofitAPI.create(AssetService.class);
            Call call = assetService.insertChildAssetAnalytics(subdomain,CHILDanalytics);
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    String apiResponse= response.body();
                    if (apiResponse != null && apiResponse.equalsIgnoreCase("true")) {
                        try{
                            digitalAssetTransactionChildRepository.deleteRecord(digitalAssetTransactionChild.getSlNo());
                            if(next+1 >= uploadchildassets.size()){
                                dismissProgressDialog();
                            }else{
                                childno++;
                                functionChildIndex(childno);
                            }
                        }catch(Exception e){
                            Log.d("error",e.toString());
                        }
                    } else {
                        dismissProgressDialog();
                        Log.d("retrofit","error 2");
                        //error
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("retrofit","error 2");
                    dismissProgressDialog();
                }
            });
        }
    }

    //Progress Dialog Section

    public void showProgressDialog() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getResources().getString(R.string.uploadingData));
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


    //VersionChecking and Updating Section
    public void insertUserTracking(String pagename,double lattitude,double londitude){
        try {
            User userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);
            UserTrackingModel track = new UserTrackingModel();
            track.setCompanyId(String.valueOf(PreferenceUtils.getCompnayId(mContext)));
            track.setUserId(String.valueOf(PreferenceUtils.getUserId(mContext)));
            track.setBrowser(PreferenceUtils.getUserAgent(mContext));
            track.setRegionCode(userobj.getRegion_Code());
            track.setModule(pagename);
            track.setDeviceModel(getDeviceModel());
            track.setDeviceType(getDeviceManufacturer());
            track.setDevice_OS_Type("Android");
            track.setOSVersion(getdeviceVersion());
            track.setLattitude(String.valueOf(lattitude));
            track.setLongitude(String.valueOf(londitude));
            track.setAppVersion(mContext.getResources().getString(R.string.versionName));

            trackertableRepository.insertTrackingData(track);
        }catch (Exception e){

        }
    }

    public static String getDeviceModel() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    public static  String getDeviceManufacturer(){
        String manufacturer = Build.MANUFACTURER;
        return manufacturer;
    }

    public static String getdeviceVersion(){
        String version = Build.VERSION.RELEASE;
        return version;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public void uploadTrackingTable(){
        uploadPageAnalytics = trackertableRepository.getAllValues();
        if(uploadPageAnalytics.size()>0){
            functionUploadtrackIndex(numnext);
        }else{
            dismissProgressDialog();
        }
    }

    public void functionUploadtrackIndex(int index){
        UserTrackingModel analtic = uploadPageAnalytics.get(index);
        String subdomain = PreferenceUtils.getSubdomainName(mContext);

        UserTrackingModel page = new UserTrackingModel();
        if(analtic != null){
            pagevisits = analtic;
            page.setCompanyId(analtic.getCompanyId());
            page.setUserId(analtic.getUserId());
            page.setRegionCode(analtic.getRegionCode());
            page.setModule(analtic.getModule());
            page.setDeviceType(analtic.getDeviceType());
            page.setDeviceModel(analtic.getDeviceModel());
            page.setAppVersion(analtic.getAppVersion());
            page.setDevice_OS_Type(analtic.getDevice_OS_Type());
            page.setBrowser(analtic.getBrowser());
            page.setOSBrowserVersion(analtic.getOSBrowserVersion());
            page.setOSVersion(analtic.getOSVersion());
            page.setUserAnonymous(analtic.getUserAnonymous());
            page.setOtherData1(analtic.getOtherData1());
            page.setOtherData2(analtic.getOtherData2());
            page.setLattitude(analtic.getLattitude());
            page.setLongitude(analtic.getLongitude());
            page.setAddress(analtic.getAddress());

            retrofitAPI = RetrofitAPIBuilder.getInstance();
            userTrackerService = retrofitAPI.create(UserTrackerService.class);
            Call call = userTrackerService.insertUserTracker(page,subdomain);
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    String apiResponse= response.body();
                    if (apiResponse != null && apiResponse.equalsIgnoreCase("true")) {
                        try{
                            trackertableRepository.deleteRecord(pagevisits.getSlNo());
                            if(numnext+1 >= uploadPageAnalytics.size()){
                                dismissProgressDialog();
                            }else{
                                numnext++;
                                functionUploadtrackIndex(numnext);
                            }
                        }catch(Exception e){
                            Log.d("error",e.toString());
                        }
                    } else {
                        dismissProgressDialog();
                        Log.d("retrofit","error 2");
                        //error
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("retrofit","error 2");
                    dismissProgressDialog();
                }
            });
        }
    }


    public void uploadTestTableRecords(){
        TestresultList = testResultRepository.getAllUnsyncedValues();
        if(TestresultList.size()>0){
            uploadTestTableIndex(testrecnum);
        }else{
            dismissProgressDialog();
        }
    }

    public void uploadTestTableIndex(final int index)
    {
        final TestResultModel analtic = TestresultList.get(index);
        String subdomain = PreferenceUtils.getSubdomainName(mContext);
        int companyId = PreferenceUtils.getCompnayId(mContext);
        int userId = PreferenceUtils.getUserId(mContext);
        retrofitAPI = RetrofitAPIBuilder.getInstance();
        lpCourseService = retrofitAPI.create(LPCourseService.class);
        AnwerUploadModel answermodel = gsonget.fromJson(analtic.getTestAnswers(),AnwerUploadModel.class);
        Call call = lpCourseService.insertTestCourseResponse(subdomain,companyId,userId,analtic.getQuestionLoadCount(),Boolean.parseBoolean(analtic.getIslastQuestion()),Boolean.parseBoolean(analtic.getIstimeout()),answermodel);
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                String apiResponse= response.body();
                if (apiResponse != null && apiResponse.equalsIgnoreCase("true")) {
                    try{
                        testResultRepository.updateIsSynced(analtic.getSlno());
                        if(testrecnum+1 >= TestresultList.size()){
                            testResultRepository.deleteRecord();
                            dismissProgressDialog();
                        }else{
                            testrecnum++;
                            uploadTestTableIndex(testrecnum);
                        }
                    }catch(Exception e){
                        Log.d("error",e.toString());
                    }
                } else {
                    //testResultRepository.updateIsSynced(analtic.getSlno());
                    testResultRepository.deleteAllTransaction();
                    dismissProgressDialog();
                    Log.d("retrofit","error 2");
                    //error
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("retrofit","error 2");
                dismissProgressDialog();
            }
        });
    }
}
