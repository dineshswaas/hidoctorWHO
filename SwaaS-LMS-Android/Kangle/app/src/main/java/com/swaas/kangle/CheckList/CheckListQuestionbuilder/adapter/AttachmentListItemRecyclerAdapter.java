package com.swaas.kangle.CheckList.CheckListQuestionbuilder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.swaas.kangle.CheckList.CheckListQuestionbuilder.QuestionActivity;
import com.swaas.kangle.CheckList.model.Acknowledgement_urls;
import com.swaas.kangle.R;
import com.swaas.kangle.TaskModule.CreateTaskActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sayellessh on 25-05-2018.
 */

public class AttachmentListItemRecyclerAdapter extends RecyclerView.Adapter<AttachmentListItemRecyclerAdapter.ViewHolder>  {

    QuestionActivity mActivity;
    CreateTaskActivity createTaskActivity;
    List<Acknowledgement_urls> mAcknowledgement_urls;
    boolean a=false;
    View view;

    public AttachmentListItemRecyclerAdapter(QuestionActivity activity, List<Acknowledgement_urls> acknowledgement_urlses)  {
        mActivity =  activity;
        mAcknowledgement_urls = acknowledgement_urlses;
    }

    public AttachmentListItemRecyclerAdapter(CreateTaskActivity createTaskActivity1, ArrayList<Acknowledgement_urls> urls, boolean b) {
        createTaskActivity=createTaskActivity1;
        mAcknowledgement_urls=urls;
        a=b;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(a) {
            view = LayoutInflater.from(createTaskActivity).inflate(R.layout.file_attachment_list_item, parent, false);
        } else {
            view = LayoutInflater.from(mActivity).inflate(R.layout.file_attachment_list_item, parent, false);
        }
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Acknowledgement_urls asset = mAcknowledgement_urls.get(position);

        if(asset.getUrl().endsWith(".mp4")) {
            holder.thumbnail.setImageResource(R.drawable.smallicon_mp4);
        }else if(asset.getUrl().endsWith(".pdf")) {
            holder.thumbnail.setImageResource(R.drawable.smallicon_pdf);
        }else {
            holder.thumbnail.setImageResource(R.drawable.icon_jpeg);
        }

        if (!a) {
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.loadViewbasedonURL(asset, position);
                }
            });
        }
        else
        {
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createTaskActivity.loadViewbasedonURL(asset, position);
                }
            });
        }

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAcknowledgement_urls.remove(position);
                notifyDataSetChanged();
                if(a) {
                    createTaskActivity.reloadAdapter(mAcknowledgement_urls);
                } else {
                    mActivity.reloadAdapter(mAcknowledgement_urls);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = mAcknowledgement_urls.size();
        } catch (Exception e) {

        }
        return count;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        final ImageView thumbnail,remove;
        final View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            remove = (ImageView) itemView.findViewById(R.id.remove);
            mView = itemView.findViewById(R.id.new_item);
        }
    }
}
