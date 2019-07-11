package com.swaas.kangle;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
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
import com.koushikdutta.ion.Ion;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.API.service.AssetService;
import com.swaas.kangle.adapter.NewSubDiscussionListItemRecyclerAdapter;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.models.PostComment;
import com.swaas.kangle.models.PostSubComment;
import com.swaas.kangle.models.WallPost;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SubDiscussionListActivity extends AppCompatActivity {

    EmptyRecyclerView subdiscussionList;
    Activity mActivity;
    EditText edittext;
    Button submitpost,disablesubmit;
    TextView pendingcharectors;
    ImageView user_profileimage;
    TextView posted_user_name,commentText,commens_text,assettitle;
    NewSubDiscussionListItemRecyclerAdapter recyclerAdapter;
    LinearLayoutManager subDiscusslayoutManager;
    PostComment postobject;
    String AssetDAID;
    PostSubComment comment;

    ImageView companylogo;

    List<PostSubComment> postSubComments;

    LinearLayout header;
    ImageView sendpost;
    RelativeLayout totalview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_discussion_list);

        mActivity = SubDiscussionListActivity.this;
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //getSupportActionBar().hide();
        postSubComments = new ArrayList<PostSubComment>();
        if(getIntent() != null){
            postobject = (PostComment) getIntent().getSerializableExtra("PostObject");
            AssetDAID = getIntent().getStringExtra("DAID");
        }
        comment = new PostSubComment();
        initialiseViews();
        bindonclickevents();
        setUpRecyclerView();

        setthemeforView();

        loadViews();
    }

    public void initialiseViews(){
        user_profileimage = (ImageView) findViewById(R.id.user_profileimage);
        posted_user_name = (TextView) findViewById(R.id.posted_user_name);
        commentText = (TextView) findViewById(R.id.commentText);
        edittext = (EditText) findViewById(R.id.edittext);
        submitpost = (Button) findViewById(R.id.submitpost);
        subdiscussionList = (EmptyRecyclerView) findViewById(R.id.subdiscussionList);
        pendingcharectors = (TextView) findViewById(R.id.pendingcharectors);
        companylogo = (ImageView) findViewById(R.id.companylogo);

        disablesubmit = (Button) findViewById(R.id.disablesubmit);

        header = (LinearLayout) findViewById(R.id.header);
        sendpost = (ImageView) findViewById(R.id.sendpost);

        commens_text = (TextView) findViewById(R.id.commens_text);
        assettitle = (TextView) findViewById(R.id.assettitle);
        totalview = (RelativeLayout) findViewById(R.id.totalview);
    }

    public void bindonclickevents(){

        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pendingcharectors.setText((100 - edittext.getText().toString().trim().length())+" "
                        + getResources().getString(R.string.charactersremainig));
                if(edittext.getText().toString().trim().length() > 100){
                    submitpost.setVisibility(View.GONE);
                    disablesubmit.setVisibility(View.VISIBLE);
                    sendpost.setVisibility(View.GONE);
                }else{
                    submitpost.setVisibility(View.VISIBLE);
                    disablesubmit.setVisibility(View.GONE);
                    sendpost.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edittext.getText().toString().trim().length() == 0){
                    submitpost.setVisibility(View.GONE);
                    disablesubmit.setVisibility(View.VISIBLE);
                    sendpost.setVisibility(View.GONE);
                }
            }
        });

        submitpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitNewcomment();
            }
        });

        sendpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitNewcomment();
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    public void setUpRecyclerView(){
        subDiscusslayoutManager = new LinearLayoutManager(this);
        subdiscussionList.setLayoutManager(subDiscusslayoutManager);
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

        posted_user_name.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        commentText.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        companylogo.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        commens_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        pendingcharectors.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        sendpost.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        assettitle.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        totalview.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        edittext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));

    }

    public void loadViews(){
        posted_user_name.setText(postobject.getPostedByName());
        commentText.setText(postobject.getMessage().toString());
        Ion.with(user_profileimage).fitCenter().placeholder(R.drawable.default_user_icon).fitXY().error(R.drawable.icon_jpeg).fitXY().load(
                (!TextUtils.isEmpty(postobject.getPostedByAvatar()))? postobject.getPostedByAvatar() : postobject.getPostedByAvatar());
        //user_profileimage.setImageBitmap(getBitmapFromURL(postobject.getPostedByAvatar()));
        if(postobject.getPostComments() != null) {
            postSubComments = postobject.getPostComments();
        }else{
            postSubComments = new ArrayList<PostSubComment>();
        }
        loadAdapter(postSubComments,false);
    }

    public void loadAdapter(List<PostSubComment> subPostComments,boolean append){
        if(append){
            postSubComments.add(subPostComments.get(0));
        }
        recyclerAdapter = new NewSubDiscussionListItemRecyclerAdapter(SubDiscussionListActivity.this, postSubComments);
        subdiscussionList.setAdapter(recyclerAdapter);
    }

    public void submitNewcomment(){
        String message = edittext.getText().toString().trim();
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        AssetService assetService = retrofitAPI.create(AssetService.class);
        Gson gsonget = new Gson();
        User userobj = gsonget.fromJson(PreferenceUtils.getUser(mActivity), User.class);
        WallPost wallPost = new WallPost();
        wallPost.setCompany_Code(userobj.getCompany_Code());
        wallPost.setCompany_Id(userobj.getCompany_Id());
        wallPost.setPost_Id(postobject.getPostId());
        wallPost.setContent_Id(AssetDAID);
        wallPost.setEmployee_Name(userobj.getEmployee_Name());
        wallPost.setMessage(message);
        wallPost.setUser_Id(userobj.getUserID());
        Call call = assetService.postSubComments(wallPost);
        call.enqueue(new Callback<PostSubComment>() {

            @Override
            public void onResponse(Response<PostSubComment> response, Retrofit retrofit) {
                PostSubComment apiResponse= response.body();
                if (apiResponse != null) {
                    List<PostSubComment> comment = new ArrayList<PostSubComment>();
                    comment.add(apiResponse);
                    edittext.setText("");
                    //gotoCategoriesView();
                    loadAdapter(comment,true);
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

}
