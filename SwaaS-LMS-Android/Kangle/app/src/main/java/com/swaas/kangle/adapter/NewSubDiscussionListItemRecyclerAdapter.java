package com.swaas.kangle.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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
import com.swaas.kangle.models.PostComment;
import com.swaas.kangle.models.PostSubComment;
import com.swaas.kangle.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Sayellessh on 18-05-2017.
 */

public class NewSubDiscussionListItemRecyclerAdapter extends RecyclerView.Adapter<NewSubDiscussionListItemRecyclerAdapter.ViewHolder>{


    Activity mActivity;
    PostComment mPosts;
    List<PostSubComment> subposts;

    public NewSubDiscussionListItemRecyclerAdapter(Activity activity, List<PostSubComment> postcomments)  {
        mActivity =  activity;
        subposts = postcomments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.new_asset_related_sub_discussion_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        final PostSubComment post = subposts.get(position);
        setthemeforView(holder);

        try{
            holder.posted_user_name.setText(post.getCommentedByName());
            holder.commentText.setText(post.getMessage().toString());
            Ion.with(holder.user_profileimage).fitCenter().placeholder(R.drawable.default_user_icon).fitXY().error(R.drawable.icon_jpeg).fitXY().load(
                    (!TextUtils.isEmpty(post.getCommentedByAvatar()))? post.getCommentedByAvatar() : post.getCommentedByAvatar());
            //holder.user_profileimage.setImageBitmap(getBitmapFromURL(post.getCommentedByAvatar()));
        } catch (Exception e){
            Log.d("Error",e.toString());
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = subposts.size();
        } catch (Exception e) {

        }
        return count;
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

        /*if(PreferenceUtils.getSubdomainName(mActivity).contains("tacobell")){
            Constants.COMPANY_COLOR = "#702082";
            Constants.OPAQUE_COLOR = "#cdcdcd";
            Constants.HEADERBAR_COLOR = "#303030";
            Constants.CARDBACKGROUND_COLOR = "#9C7EB4";
            Constants.COMPLETED_COLOR = "#0F9D58";
            Constants.INPROGRESS_COLOR = "#ffffff";
            Constants.EXPIRED_COLOR = "#f91b00";
            Constants.TEXT_COLOR = "#ffffff";
            Constants.ICON_COLOR = "#ffffff";
            Constants.PARTIALLY_COMPLETED_COLOR = "#FF0000FF";
            Constants.GREY_COLOR = "#8f8f8f";
        }else{
            Constants.COMPANY_COLOR = "#3F51B5";
            Constants.OPAQUE_COLOR = "#cdcdcd";
            Constants.HEADERBAR_COLOR = "#303030";
            Constants.CARDBACKGROUND_COLOR = "#9C7EB4";
            Constants.COMPLETED_COLOR = "#0F9D58";
            Constants.INPROGRESS_COLOR = "#ffffff";
            Constants.EXPIRED_COLOR = "#f91b00";
            Constants.TEXT_COLOR = "#ffffff";
            Constants.ICON_COLOR = "#ffffff";
            Constants.PARTIALLY_COMPLETED_COLOR = "#FF0000FF";
            Constants.GREY_COLOR = "#8f8f8f";
        }*/


        holder.posted_user_name.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.commentText.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        final ImageView user_profileimage;
        final TextView posted_user_name,commentText;
        final LinearLayout coments_item;

        public ViewHolder(View itemView) {
            super(itemView);

            user_profileimage = (ImageView) itemView.findViewById(R.id.user_profileimage);
            posted_user_name = (TextView) itemView.findViewById(R.id.posted_user_name);
            commentText = (TextView) itemView.findViewById(R.id.commentText);

            coments_item = (LinearLayout) itemView.findViewById(R.id.coments_item);
        }

    }
}
