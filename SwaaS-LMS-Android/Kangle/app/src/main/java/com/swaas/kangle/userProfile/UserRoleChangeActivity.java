package com.swaas.kangle.userProfile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.swaas.kangle.API.model.LandingPageAccess;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.API.service.UserService;
import com.swaas.kangle.LPCourse.CourseListActivity;
import com.swaas.kangle.R;
import com.swaas.kangle.ThemeClass;
import com.swaas.kangle.UploadActivity;
import com.swaas.kangle.UploadService.DigitalAssetAnalyticsUpSyncService;
import com.swaas.kangle.db.DigitalAssetTransactionRepository;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.models.MenuModel;
import com.swaas.kangle.playerPart.DigitalAssets;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.userProfile.models.UserRoleModel;
import com.swaas.kangle.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class UserRoleChangeActivity extends AppCompatActivity {

    UserRoleModel userObject;
    Context context = this;
    TextView loadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_role_change);

        loadingText = (TextView) findViewById(R.id.loadingText);
        if(getIntent() != null)
        {
            userObject = (UserRoleModel) getIntent().getSerializableExtra("New_User_Object");
        }
        if(userObject != null) {
            uploadLocalDataToServerFirst();
        }
    }

    public void uploadLocalDataToServerFirst(){
        UploadActivity uploadActivity = new UploadActivity(context);
        if(NetworkUtils.checkIfNetworkAvailable(context)){
            uploadActivity.uploadTrackingTable();
            uploadActivity.uploadTestTableRecords();
            sendDigitalAssetAnalyticsToServer();
        }
        PreferenceUtils.setUserId(context, userObject.getUser_Id());
        getUserRefreshDetails();
    }

    private void sendDigitalAssetAnalyticsToServer() {
        DigitalAssetTransactionRepository transactionRepository = new DigitalAssetTransactionRepository(context);
        transactionRepository.setmGetDA(new DigitalAssetTransactionRepository.GetDA() {
            @Override
            public void getDASuccess(ArrayList<DigitalAssets> digitalAssetList) {
                if(digitalAssetList != null && digitalAssetList.size() > 0){
                    Intent analyticsIntent = new Intent(UserRoleChangeActivity.this,DigitalAssetAnalyticsUpSyncService.class);
                    startService(analyticsIntent);
                }
            }

            @Override
            public void getDAFailure(String message) {

            }
        });
        transactionRepository.getUnSyncedDigitalAssetAnalytics();
    }

    private void getUserRefreshDetails()
    {
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        UserService userService = retrofitAPI.create(UserService.class);
        Call call = userService.getUserInfo(PreferenceUtils.getUserId(context), PreferenceUtils.getSubdomainName(context), PreferenceUtils.getCompnayId(context));
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Response<List<User>> response, Retrofit retrofit) {
                List<User> apiResponse = response.body();
                if (apiResponse != null && apiResponse.size()>0) {
                    Gson gson = new Gson();
                    String userobj = gson.toJson(apiResponse.get(0));
                    PreferenceUtils.setUser(context,userobj);
                    if(userobj != null){
                        getMenus();
                        //getMenusNewApi();
                    }
                } else {
                    loadingText.setText(getResources().getString(R.string.common_google_play_services_invalid_account_title));
                    Toast.makeText(context,getResources().getString(R.string.common_google_play_services_invalid_account_title),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("UserRefresh","error");
                loadingText.setVisibility(View.INVISIBLE);
                Toast.makeText(context,getResources().getString(R.string.erroroccured),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getMenus() {
        if (NetworkUtils.checkIfNetworkAvailable(context)) {
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            UserService userService = retrofitAPI.create(UserService.class);
            Call call = userService.getLandingPageAccess(PreferenceUtils.getSubdomainName(context), PreferenceUtils.getUserId(context), PreferenceUtils.getCompnayId(context));
            call.enqueue(new Callback<List<LandingPageAccess>>() {
                @Override
                public void onResponse(Response<List<LandingPageAccess>> response, Retrofit retrofit) {
                    List<LandingPageAccess> apiResponse = response.body();
                    if (apiResponse != null) {
                        Gson gson = new Gson();
                        String landingobj = gson.toJson(apiResponse.get(0));
                        PreferenceUtils.setLandingPageAccess(context, landingobj);
                        SetTheme();
                    } else {
                        Log.d("retrofit", "error 2");
                        //error
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("Login", "error");
                    //error
                }
            });
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }

    //New Menu Api method
    public void getMenusNewApi() {
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        UserService userService = retrofitAPI.create(UserService.class);
        Call call = userService.getLandingmenus_API(PreferenceUtils.getSubdomainName(context), PreferenceUtils.getUserId(context),
                PreferenceUtils.getCompnayId(context),"en_us");
        call.enqueue(new Callback<List<MenuModel>>() {
            @Override
            public void onResponse(Response<List<MenuModel>> response, Retrofit retrofit) {
                List<MenuModel> apiResponse = response.body();
                if (apiResponse != null) {
                    converttoLandingObject(apiResponse);
                } else {
                    Log.d("retrofit", "error 2");
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Log.d("Login", "error");
                //error
            }
        });
    }
    public void converttoLandingObject(List<MenuModel> menu){
        LandingPageAccess landingpage = new LandingPageAccess();
        for(MenuModel m : menu){
            if(m.getMenu_id() == 1){
                landingpage.setLibrary("Y");
            }
            if(m.getMenu_id() == 2){
                landingpage.setCourse("L");
            }
            if(m.getMenu_id() == 3){
                landingpage.setChecklist("Y");
            }
            if(m.getMenu_id() == 4){
                landingpage.setTask("Y");
            }
        }
        Gson gson = new Gson();
        String landingobj = gson.toJson(landingpage);
        PreferenceUtils.setLandingPageAccess(context, landingobj);

        SetTheme();
    }

    public void SetTheme(){
        final ThemeClass t = new ThemeClass(context);
        t.setGetThemeDataCBListner(new ThemeClass.GetThemeDataCBListner() {

            @Override
            public void GetThemeDataSuccessCB(boolean Success) {
                if(Success){
                    refreshingApp();
                }
            }

            @Override
            public void GetThemeDataFailureCB(boolean message) {
                t.setTheme();
            }
        });
        t.setTheme();
    }

    private void refreshingApp() {
        Intent intent = new Intent(context, CourseListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
