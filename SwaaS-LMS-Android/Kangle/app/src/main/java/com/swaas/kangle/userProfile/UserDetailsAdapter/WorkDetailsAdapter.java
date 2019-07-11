package com.swaas.kangle.userProfile.UserDetailsAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swaas.kangle.R;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.userProfile.UserDetails;
import com.swaas.kangle.userProfile.UserProfileActivity;

import java.util.List;

/**
 * Created by Sayellessh on 26-02-2018.
 */

public class WorkDetailsAdapter extends RecyclerView.Adapter<WorkDetailsAdapter.WorkDetailsRecyclerHolder>{

    Context context;
    List<UserDetails> workModelList;
    UserProfileActivity activity;

    public WorkDetailsAdapter(UserProfileActivity mActivity,List<UserDetails> workmodel) {
        this.workModelList = workmodel;
        activity = mActivity;
        context = mActivity;
    }


    @Override
    public WorkDetailsRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.workdetails_list_item, parent, false);
        return new WorkDetailsRecyclerHolder(view,context);
    }

    @Override
    public void onBindViewHolder(final WorkDetailsRecyclerHolder holder, final int position) {
        final UserDetails wrkDetails = workModelList.get(position);

        holder.workName.setText(wrkDetails.getWork_Name().toString());
        holder.workposition.setText(wrkDetails.getWork_Position().toString());

        if(wrkDetails.getIs_Current_Work() == 1) {
            holder.workduration.setText(String.valueOf(wrkDetails.getWork_From())+" - "+
                    context.getResources().getString(R.string.present_text)+" , " +
                    wrkDetails.getCity_Name());
        }else{
            holder.workduration.setText(String.valueOf(wrkDetails.getWork_From())+" - "+
                    String.valueOf(wrkDetails.getWork_To())+" , " +
                    wrkDetails.getCity_Name());
        }

        holder.editworkdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.deleteworkdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.deleteWorkDetails(PreferenceUtils.getSubdomainName(context), PreferenceUtils.getCompnayId(context), PreferenceUtils.getUserId(context),wrkDetails.getUser_Work_Id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return workModelList.size();
    }

    public class WorkDetailsRecyclerHolder extends RecyclerView.ViewHolder{
        Context ctxt;
        View mView;
        TextView workName,workposition,workduration;
        ImageView editworkdetails,deleteworkdetails;

        public WorkDetailsRecyclerHolder(View itemView,Context context) {
            super(itemView);
            this.ctxt = context;
            mView = itemView.findViewById(R.id.work_listItem);
            workName = (TextView)itemView.findViewById(R.id.wrk_name);
            workposition = (TextView)itemView.findViewById(R.id.work_position);
            workduration = (TextView)itemView.findViewById(R.id.work_duration);
            editworkdetails = (ImageView) itemView.findViewById(R.id.edit_work_details);
            deleteworkdetails = (ImageView) itemView.findViewById(R.id.delete_work_details);
        }
    }
}
