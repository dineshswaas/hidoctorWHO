package com.swaas.kangle.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.swaas.kangle.R;
import com.swaas.kangle.SubDiscussionListActivity;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.models.PostComment;
import com.swaas.kangle.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Sayellessh on 16-05-2017.
 */

public class NewDiscussionListItemRecyclerAdapter extends RecyclerView.Adapter<NewDiscussionListItemRecyclerAdapter.ViewHolder> {

    Activity mActivity;
    //ArrayList<DigitalAssetsMaster> mAssetsForBrowses;
    List<PostComment> mPosts;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;
    String daid;

    public NewDiscussionListItemRecyclerAdapter(Activity activity, List<PostComment> postcomments,String DAID)  {
        mActivity =  activity;
        mPosts = postcomments;
        daid = DAID;
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(mActivity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.new_asset_related_discussion_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        final PostComment post = mPosts.get(position);

        setthemeforView(holder);

        try{
            holder.posted_user_name.setText(post.getPostedByName().toString());
            holder.commentText.setText(post.getMessage().toString());
            Ion.with(holder.user_profileimage).fitCenter().placeholder(R.drawable.default_user_icon).fitXY().error(R.drawable.icon_jpeg).fitXY().load(
                    (!TextUtils.isEmpty(post.getPostedByAvatar()))? post.getPostedByAvatar() : post.getPostedByAvatar());
            //holder.user_profileimage.setImageBitmap(getBitmapFromURL(post.getPostedByAvatar()));
            if(post.getPostComments().size() > 0){
                holder.subcomment.setVisibility(View.VISIBLE);
                holder.subcomment.setText(Html.fromHtml("<u>"+mActivity.getResources().getString(R.string.view_all_replies)+"</u>"));
                holder.subcomment.setTypeface(holder.subcomment.getTypeface(), Typeface.ITALIC);
            }else{
                holder.subcomment.setVisibility(View.GONE);
            }
        } catch (Exception e){
            Log.d("Error",e.toString());
        }

        holder.subcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mActivity, SubDiscussionListActivity.class);
                i.putExtra("PostObject",post);
                i.putExtra("DAID",daid);
                mActivity.startActivity(i);
            }
        });

        holder.replyimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mActivity, SubDiscussionListActivity.class);
                i.putExtra("PostObject",post);
                i.putExtra("DAID",daid);
                mActivity.startActivity(i);
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

    public void setthemeforView(final ViewHolder holder){

        holder.posted_user_name.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.commentText.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.subcomment.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.coments_item.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        holder.replyimage.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final ImageView user_profileimage,replyimage;
        final TextView posted_user_name,commentText,subcomment;
        final LinearLayout coments_item;

        public ViewHolder(View itemView) {
            super(itemView);

            coments_item = (LinearLayout) itemView.findViewById(R.id.coments_item);
            user_profileimage = (ImageView) itemView.findViewById(R.id.user_profileimage);
            posted_user_name = (TextView) itemView.findViewById(R.id.posted_user_name);
            commentText = (TextView) itemView.findViewById(R.id.commentText);
            subcomment = (TextView) itemView.findViewById(R.id.subcomment);
            replyimage = (ImageView) itemView.findViewById(R.id.replyimage);
        }
    }
}
