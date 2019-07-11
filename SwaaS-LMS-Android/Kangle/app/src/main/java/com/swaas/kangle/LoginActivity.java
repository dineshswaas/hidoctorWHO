package com.swaas.kangle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.swaas.kangle.API.model.CheckUser;
import com.swaas.kangle.API.model.LandingPageAccess;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.API.service.UserService;
import com.swaas.kangle.LPCourse.CourseListActivity;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.models.MenuModel;
import com.swaas.kangle.models.ThemeModel;
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

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity {

    EditText username,companyName,password;
    Button submit;
    Context mContext;
    TextView forgotpassword,version,loading;
    String extension = null;
    String filename = null;
    public List<User> mUserList;
    public static List<ThemeModel> themeModel;
    View layout;
    Thread thread;
    TextView changingtext;
    ImageView dot1,dot2;
    boolean onevisible;
    TextSwitcher textSwitcher;
    public String textToShow[] = {"Powerful knowledge sharing platform","Upload | Share | Engage"};
    AlphaAnimation fadeIn;
    AlphaAnimation fadeOut;
    boolean logosucces = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext=LoginActivity.this;
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        initializeView();
        onevisible = true;
        /*textSwitcher.setInAnimation(mContext, android.R.anim.slide_in_left);
        textSwitcher.setOutAnimation(mContext, android.R.anim.slide_out_right);
        textSwitcher.setText(textToShow[0]);*/
        changingtext.setText("Powerful knowledge sharing platform");
        dot1.setImageResource(R.drawable.ic_status_white_dot);
        dot2.setImageResource(R.drawable.ic_status_blue_dot);
        fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
        fadeOut = new AlphaAnimation( 1.0f , 0.0f ) ;
        fadeIn.setDuration(3800);
        fadeIn.setFillAfter(true);
        fadeOut.setDuration(1200);
        fadeOut.setFillAfter(true);
        //fadeOut.setStartOffset(3800+fadeIn.getStartOffset());
        fadeOut.setStartOffset(3800);
        changingtext.startAnimation(fadeIn);
        changingtext.startAnimation(fadeOut);
        toggleview();
        onClickEvents();

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (password.getCompoundDrawables()[2] != null) {
                    boolean tappedX = event.getX() > (password.getWidth() - password.getPaddingRight() - password.getCompoundDrawables()[2].getIntrinsicWidth());
                    if (tappedX) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            } else {
                                password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            }

                            password.setSelection(password.getText().toString().trim().length());
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (password.getText().toString().trim().length() == 1) {
                    if (password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_visibility_black_24dp, 0);
                    } else {
                        password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_visibility_off_black_24dp, 0);
                    }
                    password.setCompoundDrawablePadding(24);
                    /*txtPassword.setSelection(txtPassword.getText().toString().trim().length());*/
                } else if (password.getText().toString().trim().length() == 0) {
                    password.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    if (password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_visibility_black_24dp, 0);
                    } else {
                        password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_visibility_off_black_24dp, 0);
                    }
                    password.setCompoundDrawablePadding(24);
                    /*txtPassword.setSelection(txtPassword.getText().toString().trim().length());*/
                }
            }
        });


    }

    public void toggleview(){
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!thread.isInterrupted()) {
                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(onevisible){
                                    toggle(2);
                                }else{
                                    toggle(1);
                                }

                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();
    }

    public void toggle(int index){
        if(index == 1) {
            //textSwitcher.setText(textToShow[index]);
            changingtext.setText("Powerful knowledge sharing platform");
            onevisible = true;
            dot1.setImageResource(R.drawable.ic_status_white_dot);
            dot2.setImageResource(R.drawable.ic_status_blue_dot);

        } else {
            changingtext.setText("Upload | Share | Engage");
            onevisible = false;
            dot1.setImageResource(R.drawable.ic_status_blue_dot);
            dot2.setImageResource(R.drawable.ic_status_white_dot);
        }
        changingtext.startAnimation(fadeIn);
        changingtext.startAnimation(fadeOut);
    }

    public void onClickEvents(){

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString().trim();
                final String company = companyName.getText().toString().toLowerCase().trim();
                String pswd = password.getText().toString().trim();
                User user = new User();
                user.setPswd(pswd);

                if (name.isEmpty() || company.isEmpty() || pswd.isEmpty()) {
                    if(name.length() == 0 || name == null){
                        username.setError(getResources().getString(R.string.please_enter_username));
                    }else if(company.length() == 0 || company ==  null){
                        companyName.setError(getResources().getString(R.string.please_enter_companyname));
                    } else if(pswd.length() == 0){
                        password.setError(getResources().getString(R.string.please_enter_password));
                    }
                } else {
                    if(company.contains("tacobell") && name.contains("@") && name.contains(".")){
                        username.setError(mContext.getResources().getString(R.string.please_enter_username));
                        username.setText("");
                    }else{
                        setPreference(pswd,company,name);
                    }
                }
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    public void success(String pswd, final String company, final String name){
        if(NetworkUtils.checkIfNetworkAvailable(mContext)){
            loading.setVisibility(View.VISIBLE);
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            RequestBody customerBody = RequestBody.create(MediaType.parse("text/plain"), pswd);
            UserService userService = retrofitAPI.create(UserService.class);
            Call call = userService.checkUserAuthenticationLite(customerBody, company, name);
            call.enqueue(new Callback<CheckUser>() {
                @Override
                public void onResponse(Response<CheckUser> response, Retrofit retrofit) {
                    CheckUser apiResponse = response.body();
                    if (apiResponse != null) {
                        Log.d("Login", String.valueOf(apiResponse.getTransaction_Status()));
                        if (apiResponse.getTransaction_Status() == true) {
                            PreferenceUtils.setCompanyId(mContext, apiResponse.getCompany_Id());
                            PreferenceUtils.setUserId(mContext, apiResponse.getUser_Id());
                            PreferenceUtils.setSubdomainName(mContext, company);
                            PreferenceUtils.setUsername(mContext,name);
                            //getMenus();
                            getMenusNewApi();
                        } else {
                            loading.setVisibility(View.INVISIBLE);
                            if(apiResponse.getAdditional_Context_More().equalsIgnoreCase("USER_NAME")){
                                username.setText("");
                                username.setError(apiResponse.getMessage_To_Display().toString());
                            } else if(apiResponse.getAdditional_Context_More().equalsIgnoreCase("PASSWORD")){
                                password.setText("");
                                password.setError(apiResponse.getMessage_To_Display().toString());
                            } else if(apiResponse.getAdditional_Context_More().equalsIgnoreCase("COMPANY_NAME")){
                                PreferenceUtils.setSubdomainName(mContext, "");
                                /*Intent in = new Intent(LoginActivity.this,CompanyEntryActivity.class);
                                in.putExtra("showerror",true);
                                in.putExtra("errormessage",apiResponse.getMessage_To_Display().toString());
                                startActivity(in);
                                finish();*/
                                companyName.setText("");
                                companyName.setError(apiResponse.getMessage_To_Display().toString());
                            }
                            else {
                                Toast.makeText(mContext, apiResponse.getMessage_To_Display(), Toast.LENGTH_SHORT).show();
                            }
                            Log.d("Login", "error in result");
                            //error in result
                        }
                    } else {
                        Toast.makeText(mContext,mContext.getResources().getString(R.string.erroroccured),Toast.LENGTH_SHORT).show();
                        Log.d("retrofit", "error 2");
                        //error
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(mContext,mContext.getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                    Log.d("Login", "error");
                    //error
                }
            });

        }else{
            loading.setVisibility(View.INVISIBLE);
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_LONG).show();
        }
    }

    public void setPreference(String pwd,String cmp,String usr){
        String company = cmp;
        boolean removekanglename = company.contains(".kangle.me");
        String fullname = "";
        if(removekanglename) {
            fullname = (company);
        }else{
            fullname = (company + ".kangle.me");
        }
        final String companyfullname = fullname;
        PreferenceUtils.setSubdomainName(mContext,companyfullname);
        success(pwd,companyfullname,usr);
    }

    public void getUserTheme()
    {
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        UserService userService = retrofitAPI.create(UserService.class);
        Call call = userService.getTheme(PreferenceUtils.getSubdomainName(mContext),PreferenceUtils.getCompnayId(mContext));
        call.enqueue(new Callback<List<ThemeModel>>() {
            @Override
            public void onResponse(Response<List<ThemeModel>> response, Retrofit retrofit) {
                List<ThemeModel> apiResponse = response.body();
                if (apiResponse != null) {
                    themeModel = apiResponse;
                    /* theme.add(themeModel.get(0).getVariableValue());//company color
                    theme.add(themeModel.get(1).getVariableValue());//header color
                    theme.add(themeModel.get(6).getVariableValue());//card background
                    theme.add(themeModel.get(10).getVariableValue());//font color
                    theme.add(themeModel.get(25).getVariableValue());//thumbnail color
                    theme.add(themeModel.get(9).getVariableValue());//icon color*/
                    PreferenceUtils.setTheme("key",themeModel,mContext);
                    //SetTheme();
                }
                   /* else {
                        //SetTheme();
                    }*/

            }
            @Override
            public void onFailure(Throwable t) {
                Log.d("Login","error");
                loading.setVisibility(View.INVISIBLE);
                Toast.makeText(mContext,mContext.getResources().getString(R.string.erroroccured),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getuserinfo(){
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        UserService userService = retrofitAPI.create(UserService.class);
        Call call = userService.getUserInfo(PreferenceUtils.getUserId(mContext),PreferenceUtils.getSubdomainName(mContext),PreferenceUtils.getCompnayId(mContext));
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Response<List<User>> response, Retrofit retrofit) {
                List<User> apiResponse = response.body();
                if (apiResponse != null && apiResponse.size()>0) {
                    Gson gson = new Gson();
                    String userobj = gson.toJson(apiResponse.get(0));
                    PreferenceUtils.setUser(mContext,userobj);
                    PreferenceUtils.setCompanyCode(mContext,apiResponse.get(0).getCompany_Code());
                    if(userobj != null){
                        if(PreferenceUtils.getClientLogo(mContext).equalsIgnoreCase("")){
                            mUserList = apiResponse;
                            DownloadPermission();
                            getUserTheme();
                        }/*else{
                            SetTheme();
                        }*/
                    }
                } else {
                    loading.setText(mContext.getResources().getString(R.string.common_google_play_services_invalid_account_title));
                    Toast.makeText(mContext,mContext.getResources().getString(R.string.common_google_play_services_invalid_account_title),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Login","error");
                loading.setVisibility(View.INVISIBLE);
                Toast.makeText(mContext,mContext.getResources().getString(R.string.erroroccured),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void DownloadPermission(){
        if(NetworkUtils.checkIfNetworkAvailable(mContext)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int storagePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (storagePermission == PackageManager.PERMISSION_GRANTED) {
                    DownloadLogo();
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_PERMISSION_STORAGE);
                }
            } else {
                DownloadLogo();
            }
        } else{
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == Constants.REQUEST_PERMISSION_STORAGE){
            if(grantResults.length > 0 && grantResults[0] == PermissionChecker.PERMISSION_GRANTED){
                DownloadLogo();
            }else{
                Toast.makeText(mContext,getResources().getString(R.string.storagepermissoinMessage),Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == Constants.REQUEST_PERMISSION_LOCATION){
            if(grantResults.length > 0 && grantResults[0] == PermissionChecker.PERMISSION_GRANTED){
                getuserinfo();
            }else{
                Toast.makeText(LoginActivity.this,getResources().getString(R.string.locationpermissoinMessage),Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void DownloadLogo(){
        if(mUserList != null) {
            loading.setText(getResources().getString(R.string.loadingpleasewait));
            String url = mUserList.get(0).getCompany_Logo();
            extension = url.substring(url.lastIndexOf("."));
            filename = "companylogo";
            new DownloadLogoAsync().execute(url);
        }else{
            getuserinfo();
        }
    }

    class DownloadLogoAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                String companylogo = "companylogo";
                final File dir = new File(new File(Environment.getExternalStorageDirectory(), Constants.Foldername), "");
                if (!dir.exists()){
                    dir.mkdir();
                }

                /*File createassetfolder = new File(Environment.getExternalStorageDirectory()+"/"+Constants.Foldername+"/asset/"+assetname+"/"+companylogo);
                if(assetname!= null && !createassetfolder.exists()) {
                    createassetfolder.mkdirs();
                }
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory()+"/"+Constants.Foldername+"/asset/"+assetname+"/"+companylogo+"/"+filename+""+extension);
                outputfilepath = Environment.getExternalStorageDirectory()+"/"+Constants.Foldername+"/asset/"+assetname+"/"+companylogo+"/"+filename+""+extension;*/
                OutputStream output = new FileOutputStream(new File(new File(Environment.getExternalStorageDirectory(), Constants.Foldername ), filename+""+extension));
                outputfilepath = Environment.getExternalStorageDirectory()+"/"+Constants.Foldername+"/"+filename+""+extension;

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
            //pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String url) {
            updatesuccess(url);
        }
    }

    public void updatesuccess(String Url){
        try {
            PreferenceUtils.setClientLogo(mContext, Url);
            logosucces=true;
            SetTheme();
        }catch (Exception e){

        }
    }

    public void getMenus() {
        if (NetworkUtils.checkIfNetworkAvailable(mContext)) {
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            UserService userService = retrofitAPI.create(UserService.class);
            Call call = userService.getLandingPageAccess(PreferenceUtils.getSubdomainName(mContext), PreferenceUtils.getUserId(mContext), PreferenceUtils.getCompnayId(mContext));
            call.enqueue(new Callback<List<LandingPageAccess>>() {
                @Override
                public void onResponse(Response<List<LandingPageAccess>> response, Retrofit retrofit) {
                    List<LandingPageAccess> apiResponse = response.body();
                    if (apiResponse != null) {
                        LandingPageAccess landingpage = new LandingPageAccess();
                        if (apiResponse.get(0).getChat().equalsIgnoreCase("y"))
                        {
                            landingpage.setChat("Y");
                        }
                        locationPermission();
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
            if(PreferenceUtils.getLandingPageAccess(mContext) != null){
                getuserinfo();
            }else {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //New Menu Api method
    public void getMenusNewApi() {
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        UserService userService = retrofitAPI.create(UserService.class);
        Call call = userService.getLandingmenus_API(PreferenceUtils.getSubdomainName(mContext), PreferenceUtils.getUserId(mContext), PreferenceUtils.getCompnayId(mContext),"en_us");
        call.enqueue(new Callback<List<MenuModel>>() {
            @Override
            public void onResponse(Response<List<MenuModel>> response, Retrofit retrofit) {
                List<MenuModel> apiResponse = response.body();
                if (apiResponse != null) {
                    converttoLandingObject(apiResponse);
                    getMenus();
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
                landingpage.setAssetText(m.getUpdated_label());
            }
            if(m.getMenu_id() == 2){
                landingpage.setCourse("L");
                landingpage.setCourseText(m.getUpdated_label());
            }
            if(m.getMenu_id() == 3){
                landingpage.setChecklist("Y");
                landingpage.setChecklistText(m.getUpdated_label());
            }
            if(m.getMenu_id() == 8){
                landingpage.setTask("Y");
                landingpage.setTaskText(m.getUpdated_label());
            }
            if (m.getMenu_id() == 9)
            {
                landingpage.setChat("Y");
            }
            if (m.getMenu_id() == 12)
            {
                landingpage.setNotification("Y");
            }
            if (m.getMenu_id() == 4)
            {
                landingpage.setReport("Y");
                landingpage.setReportText(m.getUpdated_label());
            }
        }
        Gson gson = new Gson();
        String landingobj = gson.toJson(landingpage);
        PreferenceUtils.setLandingPageAccess(mContext, landingobj);
        locationPermission();
    }

    public void locationPermission(){
        if(NetworkUtils.checkIfNetworkAvailable(LoginActivity.this)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int storagePermission = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                if (storagePermission == PackageManager.PERMISSION_GRANTED) {
                    getuserinfo();
                } else {
                    LoginActivity.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_PERMISSION_LOCATION);
                }
            } else {
                getuserinfo();
            }
        } else {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }

    //New Theme Setup
    public void SetTheme(){
        final ThemeClass t = new ThemeClass(mContext);
        t.setGetThemeDataCBListner(new ThemeClass.GetThemeDataCBListner() {

            @Override
            public void GetThemeDataSuccessCB(boolean Success) {
                if(Success){
                    if(logosucces)
                    {
                        gotocoursepage();
                    }

                }
            }

            @Override
            public void GetThemeDataFailureCB(boolean message) {
                t.setTheme();
            }
        });
        t.setTheme();
    }

    public void gotocoursepage(){
        Gson gsonget = new Gson();
        User userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);
        Log.d("retriveddata", userobj.toString());
        Crashlytics.log(PreferenceUtils.getUserId(mContext) + " - " + PreferenceUtils.getSubdomainName(mContext));
        Gson gsongetlanding = new Gson();
        LandingPageAccess landingobj = gsongetlanding.fromJson(PreferenceUtils.getLandingPageAccess(mContext), LandingPageAccess.class);
        if (!TextUtils.isEmpty(landingobj.getCourse()) && landingobj.getCourse().equalsIgnoreCase("L")) {
            Intent intentLandinActivity = new Intent(getApplicationContext(), CourseListActivity.class);
            startActivity(intentLandinActivity);
        } else if (!TextUtils.isEmpty(landingobj.getCourse()) && landingobj.getCourse().equalsIgnoreCase("S")) {

            Intent intentLandinActivity = new Intent(getApplicationContext(), CourseListActivity.class);
            startActivity(intentLandinActivity);
        } else if(!TextUtils.isEmpty(landingobj.getCourse()) && landingobj.getCourse().equalsIgnoreCase("A")){
            Intent intentLandinActivity = new Intent(getApplicationContext(), CourseListActivity.class);
            startActivity(intentLandinActivity);
        }
        else
        {
            Intent intentLandinActivity = new Intent(getApplicationContext(), AssetListActivity.class);
            startActivity(intentLandinActivity);
        }
        finish();
    }

    public void gotoLandingpage(){
        // How to retrieve your Java object back from the string
        Gson gsonget = new Gson();
        User userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);
        Log.d("retriveddata", userobj.toString());
        Crashlytics.log(PreferenceUtils.getUserId(mContext) + " - " + PreferenceUtils.getSubdomainName(mContext));
        Intent intentLandinActivity = new Intent(getApplicationContext(), LandingActivity.class);
        startActivity(intentLandinActivity);
        finish();
    }

    public void initializeView(){
        username = (EditText) findViewById(R.id.username);
        companyName = (EditText) findViewById(R.id.company_name);
        password = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.submit);
        forgotpassword = (TextView) findViewById(R.id.forgot_password);
        version = (TextView) findViewById(R.id.version_numner);
        loading = (TextView) findViewById(R.id.loading);
        layout = findViewById(R.id.logoheader);
        changingtext = (TextView) findViewById(R.id.changingtext);
        textSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);
        dot1 = (ImageView) findViewById(R.id.dot1);
        dot2 = (ImageView) findViewById(R.id.dot2);
        // getSupportActionBar().hide();
    }


}
