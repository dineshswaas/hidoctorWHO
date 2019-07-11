package com.swaas.kangle.fragmants;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.API.service.AssetService;
import com.swaas.kangle.AssetDetailActivity;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.R;
import com.swaas.kangle.adapter.DiscussionListItemRecyclerAdapter;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.models.PostComment;
import com.swaas.kangle.models.WallPost;
import com.swaas.kangle.preferences.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Sayellessh on 28-04-2017.
 */

public class AssetRelatedDiscussionsfragment extends Fragment {

    EmptyRecyclerView threadedcoments;
    TextView comment,textlength;
    Button post;
    DiscussionListItemRecyclerAdapter adapter;
    AssetDetailActivity assetDetailActivity;
    LinearLayoutManager mLayoutmanager;
    DigitalAssetsMaster assetDetails;
    ArrayList<PostComment> relatedPosts;
    PostComment postsuccess;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;
    ProgressDialog pDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assetDetailActivity = (AssetDetailActivity) getActivity();
        relatedPosts = new ArrayList<PostComment>();
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(assetDetailActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.asset_related_discussion_fragment,container,false);

        Bundle bundle = new Bundle();
        assetDetails = (DigitalAssetsMaster) getArguments().getSerializable("assetDetails");


        comment = (EditText) v.findViewById(R.id.comment);
        post = (Button) v.findViewById(R.id.post);
        threadedcoments = (EmptyRecyclerView) v.findViewById(R.id.threadedcoments);
        textlength = (TextView) v.findViewById(R.id.textlength);
        setUpRecyclerView();
        getposts();

        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textlength.setText((100 - comment.getText().length()) +" characters remainig");
                if(comment.getText().length() > 100){
                    post.setVisibility(View.GONE);
                }else{
                    post.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitNewpost();
            }
        });
        return v;
    }

    private void setUpRecyclerView() {
        mLayoutmanager = new LinearLayoutManager(assetDetailActivity);
        threadedcoments.setLayoutManager(mLayoutmanager);
    }

    public void submitNewpost(){
        String message = comment.getText().toString();
        showProgressDialog();

        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        AssetService assetService = retrofitAPI.create(AssetService.class);
        Gson gsonget = new Gson();
        User userobj = gsonget.fromJson(PreferenceUtils.getUser(assetDetailActivity), User.class);
        WallPost wallPost = new WallPost();
        wallPost.setCompany_Code(userobj.getCompany_Code());
        wallPost.setCompany_Id(userobj.getCompany_Id());
        wallPost.setContent_Id(assetDetails.getDAID());
        wallPost.setEmployee_Name(userobj.getEmployee_Name());
        wallPost.setMessage(message);
        wallPost.setUser_Id(userobj.getUserID());
        Call call = assetService.postComments(wallPost);
        call.enqueue(new Callback<PostComment>() {

            @Override
            public void onResponse(Response<PostComment> response, Retrofit retrofit) {
                PostComment apiResponse= response.body();
                if (apiResponse != null) {
                    postsuccess = apiResponse;
                    Log.d("log",postsuccess.toString());
                    relatedPosts.add(postsuccess);
                    comment.setText("");
                    loadAdapter(relatedPosts);
                    //gotoCategoriesView();
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

    public void getposts(){
        String assetportion = ("?assetId="+assetDetails.getDAID());
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        AssetService assetService = retrofitAPI.create(AssetService.class);
        Call call = assetService.getComments(assetportion);
        call.enqueue(new Callback<ArrayList<PostComment>>() {

            @Override
            public void onResponse(Response<ArrayList<PostComment>> response, Retrofit retrofit) {
                ArrayList<PostComment> apiResponse= response.body();
                if (apiResponse != null) {
                    relatedPosts = apiResponse;
                    Log.d("log","assetsForBrowses");
                    loadAdapter(relatedPosts);
                    //gotoCategoriesView();
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
        dismissProgressDialog();
        if(relatedPosts.isEmpty()){
            threadedcoments.setVisibility(View.GONE);
        }else {
            adapter = new DiscussionListItemRecyclerAdapter(assetDetailActivity, relatedPosts);
            threadedcoments.setAdapter(adapter);
        }
    }

    public void showProgressDialog(){
        pDialog = new ProgressDialog(assetDetailActivity);
        pDialog.setMessage(getResources().getString(R.string.loading));
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.setIndeterminate(false);
        pDialog.show();
    }

    public void dismissProgressDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }
}
