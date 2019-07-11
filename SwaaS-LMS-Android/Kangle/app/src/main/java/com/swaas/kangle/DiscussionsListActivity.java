package com.swaas.kangle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.API.service.AssetService;
import com.swaas.kangle.adapter.NewDiscussionListItemRecyclerAdapter;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.models.PostComment;
import com.swaas.kangle.models.WallPost;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DiscussionsListActivity extends AppCompatActivity {

    EmptyRecyclerView emptyRecyclerView;
    Activity mActivity;
    EditText comenttext;
    Button submit,disablesubmit;
    TextView pendingcharectors,commens_text,assettitle;
    NewDiscussionListItemRecyclerAdapter recyclerAdapter;
    LinearLayoutManager DiscusslayoutManager;
    DigitalAssetsMaster digitalAssetsMaster;
    ArrayList<PostComment> relatedPosts;
    ImageView companylogo;
    LinearLayout header;

    private ProgressDialog pDialog;

    ImageView sendpost;
    RelativeLayout totalview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussions_list);

        mActivity = DiscussionsListActivity.this;
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //getSupportActionBar().hide();
        if(getIntent() != null){
            digitalAssetsMaster = (DigitalAssetsMaster) getIntent().getSerializableExtra("Asset");
        }
        relatedPosts = new ArrayList<PostComment>();
        initialiseViews();
        bindOnClickEvents();
        setUpRecyclerView();
        setthemeforView();

        getPosts();
    }

    public void initialiseViews(){
        emptyRecyclerView = (EmptyRecyclerView) findViewById(R.id.discussionList);
        comenttext = (EditText) findViewById(R.id.commenttext);
        submit = (Button) findViewById(R.id.postsubmit);
        pendingcharectors = (TextView) findViewById(R.id.pendingcharectors);
        companylogo = (ImageView) findViewById(R.id.companylogo);

        disablesubmit = (Button) findViewById(R.id.disablesubmit);

        header = (LinearLayout) findViewById(R.id.header);
        sendpost = (ImageView) findViewById(R.id.sendpost);

        commens_text = (TextView) findViewById(R.id.commens_text);
        assettitle = (TextView) findViewById(R.id.assettitle);
        totalview = (RelativeLayout) findViewById(R.id.totalview);
    }

    public void bindOnClickEvents(){

        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        comenttext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pendingcharectors.setText((100 - comenttext.getText().toString().trim().length())+" "
                        + getResources().getString(R.string.charactersremainig));
                if(comenttext.getText().toString().trim().length() > 100){
                    submit.setVisibility(View.GONE);
                    disablesubmit.setVisibility(View.VISIBLE);
                    sendpost.setVisibility(View.GONE);
                }else{
                    submit.setVisibility(View.VISIBLE);
                    disablesubmit.setVisibility(View.GONE);
                    sendpost.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(comenttext.getText().toString().trim().length() == 0){
                    submit.setVisibility(View.GONE);
                    disablesubmit.setVisibility(View.VISIBLE);
                    sendpost.setVisibility(View.GONE);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitNewpost();
            }
        });

        sendpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitNewpost();
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    public void setUpRecyclerView(){
        DiscusslayoutManager = new LinearLayoutManager(this);
        emptyRecyclerView.setLayoutManager(DiscusslayoutManager);
    }

    public void setthemeforView() {

        /*if (PreferenceUtils.getSubdomainName(mActivity).contains("tacobell")) {
            Constants.COMPANY_COLOR = "#702082";
            Constants.OPAQUE_COLOR = "#cdcdcd";
            Constants.HEADERBAR_COLOR = "#303030";
            Constants.CARDBACKGROUND_COLOR = "#9C7EB4";
            Constants.COMPLETED_COLOR = "#0F9D58";
            Constants.INPROGRESS_COLOR = "#ffffff";
            Constants.EXPIRED_COLOR = "#FFFF0000";
            Constants.TEXT_COLOR = "#ffffff";
            Constants.ICON_COLOR = "#ffffff";
            Constants.PARTIALLY_COMPLETED_COLOR = "#FF0000FF";
            Constants.GREY_COLOR = "#8f8f8f";
        } else {
            Constants.COMPANY_COLOR = "#3F51B5";
            Constants.OPAQUE_COLOR = "#cdcdcd";
            Constants.HEADERBAR_COLOR = "#303030";
            Constants.CARDBACKGROUND_COLOR = "#9C7EB4";
            Constants.COMPLETED_COLOR = "#0F9D58";
            Constants.INPROGRESS_COLOR = "#ffffff";
            Constants.EXPIRED_COLOR = "#FFFF0000";
            Constants.TEXT_COLOR = "#ffffff";
            Constants.ICON_COLOR = "#ffffff";
            Constants.PARTIALLY_COMPLETED_COLOR = "#FF0000FF";
            Constants.GREY_COLOR = "#8f8f8f";
        }*/

        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        comenttext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        companylogo.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        commens_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        assettitle.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        pendingcharectors.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        sendpost.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        totalview.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
    }

    public void getPosts(){
        showProgressDialog(mActivity.getResources().getString(R.string.loading));
        String assetportion = ("?assetId="+digitalAssetsMaster.getDAID());
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        AssetService assetService = retrofitAPI.create(AssetService.class);
        Map<String, String> data1 = new HashMap<>();
        data1.put("assetId", digitalAssetsMaster.getDAID());
        Call call = assetService.getNewComments(data1);
        call.enqueue(new Callback<ArrayList<PostComment>>() {

            @Override
            public void onResponse(Response<ArrayList<PostComment>> response, Retrofit retrofit) {
                ArrayList<PostComment> apiResponse = response.body();
                if (apiResponse != null) {
                    relatedPosts = apiResponse;
                    loadAdapter(relatedPosts);
                    dismissProgressDialog();
                    //gotoCategoriesView();
                } else {
                    Log.d("retrofit","error 2");
                    dismissProgressDialog();
                    //error
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dismissProgressDialog();
                Log.d("retrofit","error 2");
            }
        });
    }

    public void submitNewpost(){
        String message = comenttext.getText().toString().trim();
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        AssetService assetService = retrofitAPI.create(AssetService.class);
        Gson gsonget = new Gson();
        User userobj = gsonget.fromJson(PreferenceUtils.getUser(mActivity), User.class);
        WallPost wallPost = new WallPost();
        wallPost.setCompany_Code(userobj.getCompany_Code());
        wallPost.setCompany_Id(userobj.getCompany_Id());
        wallPost.setContent_Id(digitalAssetsMaster.getDAID());
        wallPost.setEmployee_Name(userobj.getEmployee_Name());
        wallPost.setMessage(message);
        wallPost.setUser_Id(userobj.getUserID());
        Call call = assetService.postComments(wallPost);
        call.enqueue(new Callback<PostComment>() {

            @Override
            public void onResponse(Response<PostComment> response, Retrofit retrofit) {
                PostComment apiResponse= response.body();
                if (apiResponse != null) {
                    relatedPosts.add(apiResponse);
                    comenttext.setText("");
                    //gotoCategoriesView();
                    loadAdapter(relatedPosts);
                    Toast.makeText(mActivity,mActivity.getResources().getString(R.string.postsuccess),Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("retrofit","error 2");
                    //error
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("retrofit","error 2");
            }
        });
    }

    public void loadAdapter(List<PostComment> relatedPosts){
        if(relatedPosts.isEmpty()){

        }else {
            recyclerAdapter = new NewDiscussionListItemRecyclerAdapter(DiscussionsListActivity.this, relatedPosts,digitalAssetsMaster.getDAID());
            emptyRecyclerView.setAdapter(recyclerAdapter);
        }
    }

    public void showProgressDialog(String Message){
        pDialog = new ProgressDialog(mActivity);
        pDialog.setMessage(Message);
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    public void dismissProgressDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }
}
