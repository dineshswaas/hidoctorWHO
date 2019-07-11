package com.swaas.kangle.fragmants;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.API.service.AssetService;
import com.swaas.kangle.NewAssetDetailActivity;
import com.swaas.kangle.R;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.models.PostComment;
import com.swaas.kangle.models.WallPost;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.NetworkUtils;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Sayellessh on 16-05-2017.
 */

public class commentViewpagerFragment extends Fragment {

    TextView comment,textlength;
    Button post,disablepost;
    NewAssetDetailActivity mActivity;
    DigitalAssetsMaster assetDetails;
    ArrayList<PostComment> relatedPosts;
    PostComment postsuccess;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (NewAssetDetailActivity) getActivity();
        relatedPosts = new ArrayList<PostComment>();
        postsuccess = new PostComment();
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(mActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.comment_viewpager_fragment,container,false);

        Bundle bundle = new Bundle();
        assetDetails = (DigitalAssetsMaster) getArguments().getSerializable("assetDetails");


        comment = (EditText) v.findViewById(R.id.comment);
        post = (Button) v.findViewById(R.id.post);
        disablepost = (Button) v.findViewById(R.id.disablepost);
        textlength = (TextView) v.findViewById(R.id.textlength);


        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textlength.setText(100 - comment.getText().toString().trim().length()+" "
                        + getResources().getString(R.string.charactersremainig));
                if(comment.getText().toString().trim().length() > 100){
                    post.setVisibility(View.GONE);
                }else{
                    post.setVisibility(View.VISIBLE);
                    disablepost.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String comentText = comment.getText().toString().trim();
                if(comment.getText().toString().trim().length() == 0){
                    disablepost.setVisibility(View.VISIBLE);
                    post.setVisibility(View.GONE);
                }
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

    public void submitNewpost(){
        if(NetworkUtils.checkIfNetworkAvailable(mActivity)) {
            String message = comment.getText().toString().trim();
            mActivity.showLoading(mActivity.getResources().getString(R.string.postingcoments));
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            AssetService assetService = retrofitAPI.create(AssetService.class);
            Gson gsonget = new Gson();
            User userobj = gsonget.fromJson(PreferenceUtils.getUser(mActivity), User.class);
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
                    PostComment apiResponse = response.body();
                    if (apiResponse != null) {
                        postsuccess = apiResponse;
                        Log.d("log", postsuccess.toString());
                        relatedPosts.add(postsuccess);
                        comment.setText("");
                        //gotoCategoriesView();
                        mActivity.dismissProgressDialog();
                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.postsuccess), Toast.LENGTH_SHORT).show();
                        mActivity.loadAdapter(relatedPosts);
                    } else {
                        mActivity.dismissProgressDialog();
                        Log.d("retrofit", "error 2");
                        //error
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    mActivity.dismissProgressDialog();
                    Log.d("retrofit", "error 2");
                }
            });

        }else{
            Toast.makeText(mActivity,mActivity.getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
        }
    }
}
