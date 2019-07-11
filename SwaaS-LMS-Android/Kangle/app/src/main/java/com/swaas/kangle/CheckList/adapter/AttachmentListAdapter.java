package com.swaas.kangle.CheckList.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swaas.kangle.CheckList.SectionsQuestionDetailActivity;
import com.swaas.kangle.R;
import com.swaas.kangle.TaskModule.ViewTaskActivity;

import java.util.List;

/**
 * Created by Sayellessh on 25-05-2018.
 */

public class AttachmentListAdapter extends RecyclerView.Adapter<AttachmentListAdapter.ViewHolder>{

    Context mActivity;
    List<String> allattachmentList;

    boolean ismFromTask;

    public AttachmentListAdapter(Context context, List<String> attachmentList, boolean isFromTask)  {
        mActivity = context;
        allattachmentList = attachmentList;
        ismFromTask = isFromTask;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.section_attachments_list_item, parent, false);
        return new AttachmentListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final String secdetails = allattachmentList.get(position);
        holder.AttachmentName.setText(mActivity.getString(R.string.attachment)+(position+1));

        if(secdetails.endsWith(".3gp") || secdetails.endsWith(".mp4") || secdetails.endsWith(".mov") || secdetails.endsWith(".MOV")) {
            holder.attachmenttypeimage.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.smallicon_mp4));
        } else if(secdetails.endsWith(".pdf")){
            holder.attachmenttypeimage.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.smallicon_pdf));
        }else{
            holder.attachmenttypeimage.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.smallicon_jpeg));
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ismFromTask)
                {
                    ((ViewTaskActivity)mActivity).loadattachedImageView(secdetails);
                    ((ViewTaskActivity)mActivity).closepopupView();
                }
                else
                {
                   ((SectionsQuestionDetailActivity)mActivity).loadattachedImageView(secdetails);
                   ((SectionsQuestionDetailActivity)mActivity).closepopupView();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = allattachmentList.size();
        } catch (Exception e) {

        }
        return count;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView AttachmentName;
        final ImageView attachmenttypeimage;
        final View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView.findViewById(R.id.mView);
            attachmenttypeimage = (ImageView) itemView.findViewById(R.id.attachmenttypeimage);
            AttachmentName = (TextView) itemView.findViewById(R.id.AttachmentName);
        }
    }
}
