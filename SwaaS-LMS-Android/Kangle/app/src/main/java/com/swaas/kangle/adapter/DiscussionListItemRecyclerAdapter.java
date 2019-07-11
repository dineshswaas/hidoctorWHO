package com.swaas.kangle.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.API.service.AssetService;
import com.swaas.kangle.R;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.models.PostComment;
import com.swaas.kangle.models.WallPost;
import com.swaas.kangle.preferences.PreferenceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Sayellessh on 05-05-2017.
 */

public class DiscussionListItemRecyclerAdapter extends RecyclerView.Adapter<DiscussionListItemRecyclerAdapter.ViewHolder> {

    Activity mActivity;
    //ArrayList<DigitalAssetsMaster> mAssetsForBrowses;
    List<PostComment> mPosts;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;

    public DiscussionListItemRecyclerAdapter(Activity activity, List<PostComment> postcomments)  {
        mActivity =  activity;
        mPosts = postcomments;
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(mActivity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.asset_related_discussion_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        final PostComment post = mPosts.get(position);

        holder.posted_user_name.setText(post.getPostedByName().toString()+": ");
        holder.commentText.setText(post.getMessage().toString());
        holder.user_profileimage.setImageBitmap(getBitmapFromURL(post.getPostedByAvatar()));
        try{
        if(post.getPostComments().size()>0){
            for(PostComment postComment:mPosts){
                holder.sub_posted_user_name.setText("TEST");
                holder.sub_commentText.setText(post.getMessage().toString());
                holder.sub_user_profileimage.setImageBitmap(getBitmapFromURL(postComment.getPostedByAvatar()));
            }
        }else{
            //holder.subThreadParentLayout.setVisibility(View.GONE);
        }
        } catch (Exception e){
            Log.d("Error",e.toString());
        }

        holder.postbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messagetext = holder.sub_comment.getText().toString();
               submitNewpost(post,messagetext);
            }
        });
    }

    public void submitNewpost(PostComment post,String commentmessage){
        String message = commentmessage;

        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        AssetService assetService = retrofitAPI.create(AssetService.class);
        Gson gsonget = new Gson();
        User userobj = gsonget.fromJson(PreferenceUtils.getUser(mActivity), User.class);
        WallPost wallPost = new WallPost();
        wallPost.setCompany_Code(userobj.getCompany_Code());
        wallPost.setCompany_Id(userobj.getCompany_Id());
        wallPost.setPostId(post.getPostId());
        wallPost.setEmployee_Name(userobj.getEmployee_Name());
        wallPost.setMessage(message);
        wallPost.setUser_Id(userobj.getUserID());
        Call call = assetService.postComments(wallPost);
        call.enqueue(new Callback<PostComment>() {

            @Override
            public void onResponse(Response<PostComment> response, Retrofit retrofit) {
                PostComment apiResponse= response.body();
                if (apiResponse != null) {
                    //postsuccess = apiResponse;
                    //Log.d("log",postsuccess.toString());
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

    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = mPosts.size();
        } catch (Exception e) {

        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
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

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final ImageView user_profileimage;
        //final ImageView download;
        final TextView posted_user_name,commentText,comment_duration;
        final EditText sub_comment;
        final Button postbutton;
        final View mView;
        final LinearLayout subThreadChildLayout;
        final RelativeLayout subThreadParentLayout;
        final TextView sub_comment_duration;
        final TextView sub_commentText ,sub_posted_user_name;
        final ImageView sub_user_profileimage;


        public ViewHolder(View itemView) {
            super(itemView);

            user_profileimage = (ImageView) itemView.findViewById(R.id.user_profileimage);
            sub_comment = (EditText) itemView.findViewById(R.id.sub_comment);
            posted_user_name = (TextView) itemView.findViewById(R.id.posted_user_name);
            commentText = (TextView) itemView.findViewById(R.id.commentText);
            comment_duration = (TextView) itemView.findViewById(R.id.comment_duration);
            mView = itemView.findViewById(R.id.coments_item);
            postbutton = (Button) itemView.findViewById(R.id.postclick);
            subThreadParentLayout = (RelativeLayout) itemView.findViewById(R.id.subThreadParentLayout);
            subThreadChildLayout = (LinearLayout) itemView.findViewById(R.id.subThreadChildLayout);

            sub_comment_duration = (TextView) subThreadChildLayout.findViewById(R.id.sub_comment_duration);
            sub_commentText = (TextView) subThreadChildLayout.findViewById(R.id.sub_commentText);
            sub_posted_user_name  = (TextView) subThreadChildLayout.findViewById(R.id.sub_posted_user_name);
            sub_user_profileimage = (ImageView) subThreadChildLayout.findViewById(R.id.sub_user_profileimage);
        }

    }
}
