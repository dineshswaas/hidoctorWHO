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
 * Created by Sayellessh on 24-02-2018.
 */
public class EducationDetailsAdapter extends RecyclerView.Adapter<EducationDetailsAdapter.EducationRecyclerHolder> {

    Context context;
    List<UserDetails> educationModelList;
    UserProfileActivity activity;

    public EducationDetailsAdapter(UserProfileActivity mActivity,List<UserDetails> educationmodel) {
        this.educationModelList = educationmodel;
        activity = mActivity;
        context = mActivity;
    }


    @Override
    public EducationRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.education_list_item, parent, false);
        return new EducationRecyclerHolder(view,context);
    }

    @Override
    public void onBindViewHolder(final EducationRecyclerHolder holder, final int position) {
        final UserDetails eduDetails = educationModelList.get(position);

        holder.educationName.setText(eduDetails.getEducation_Name().toString());
        holder.instituteName.setText(eduDetails.getInstitution_Name().toString());

        if(eduDetails.getIs_Current_Education() == 1) {
            holder.educationDuration.setText(String.valueOf(eduDetails.getEducation_From())+" - "+
                    context.getResources().getString(R.string.present_text)+" , " +
                    eduDetails.getCity_Name());
        }else{
            holder.educationDuration.setText(String.valueOf(eduDetails.getEducation_From())+" - "+
                    String.valueOf(eduDetails.getEducation_To())+" , " +
                    eduDetails.getCity_Name());
        }

        holder.editeducationdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.deleteeducationdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.deleteEducationDetails(PreferenceUtils.getSubdomainName(context), PreferenceUtils.getCompnayId(context), PreferenceUtils.getUserId(context),eduDetails.getUser_Education_Id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return educationModelList.size();
    }

    public class EducationRecyclerHolder extends RecyclerView.ViewHolder{
        Context ctxt;
        View mView;
        TextView educationName,instituteName,educationDuration;
        ImageView editeducationdetails,deleteeducationdetails;

        public EducationRecyclerHolder(View itemView,Context context) {
            super(itemView);
            this.ctxt = context;
            mView = itemView.findViewById(R.id.edu_listItem);
            educationName = (TextView)itemView.findViewById(R.id.education_name);
            instituteName = (TextView)itemView.findViewById(R.id.institute_name);
            educationDuration = (TextView)itemView.findViewById(R.id.education_duration);

            editeducationdetails = (ImageView) itemView.findViewById(R.id.edit_edu_details);
            deleteeducationdetails = (ImageView) itemView.findViewById(R.id.delete_edu_details);
        }
    }
}
