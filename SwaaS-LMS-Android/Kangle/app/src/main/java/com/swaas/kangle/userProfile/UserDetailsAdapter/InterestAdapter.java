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

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.InterestRecyclerHolder>{

    Context context;
    List<UserDetails> interestModelList;
    UserProfileActivity activity;
    boolean editable;

    public InterestAdapter(UserProfileActivity mActivity,List<UserDetails> interestmodel,boolean EnableEdit) {
        this.interestModelList = interestmodel;
        activity = mActivity;
        context = mActivity;
        editable = EnableEdit;
    }


    @Override
    public InterestRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.interest_list_item, parent, false);
        return new InterestRecyclerHolder(view,context);
    }

    @Override
    public void onBindViewHolder(final InterestRecyclerHolder holder, final int position) {
        final UserDetails intrDetails = interestModelList.get(position);

        holder.interestName.setText(intrDetails.getInterest_Name().toString());

        if(editable){
            holder.removeinterest.setVisibility(View.VISIBLE);
        }

        holder.removeinterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.deleteUserInterestDetails(PreferenceUtils.getSubdomainName(context), PreferenceUtils.getCompnayId(context), PreferenceUtils.getUserId(context),intrDetails.getUser_Interest_Id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return interestModelList.size();
    }

    public class InterestRecyclerHolder extends RecyclerView.ViewHolder{
        Context ctxt;
        View mView;
        TextView interestName;
        ImageView removeinterest;

        public InterestRecyclerHolder(View itemView,Context context) {
            super(itemView);
            this.ctxt = context;
            mView = itemView.findViewById(R.id.interest_item);
            interestName = (TextView)itemView.findViewById(R.id.interest_text);

            removeinterest = (ImageView)itemView.findViewById(R.id.remove_interest);
        }
    }
}
