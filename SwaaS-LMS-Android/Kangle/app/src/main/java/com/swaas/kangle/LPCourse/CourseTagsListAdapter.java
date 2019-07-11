package com.swaas.kangle.LPCourse;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.swaas.kangle.R;
import com.swaas.kangle.utils.Constants;

import java.util.List;

/**
 * Created by Sayellessh on 13-02-2018.
 */

public class CourseTagsListAdapter extends RecyclerView.Adapter<CourseTagsListAdapter.ViewHolder> {
    CourseListActivity mActivity;
    List<CourseModel> tags;
    boolean mShowunchecked;

    public CourseTagsListAdapter(Context context, List<CourseModel> tagList,boolean showunchecked)  {
        mActivity = (CourseListActivity) context;
        tags = tagList;
        mShowunchecked = showunchecked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.course_category_list_item, parent, false);
        return new CourseTagsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final CourseModel tagss = tags.get(position);

        setthemeforView(holder);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mActivity.loadCoursebyTags(tagss);
                if(tagss.istagchecked){
                    holder.checkboxselected.setChecked(false);
                    tagss.setIstagchecked(false);
                }else{
                    holder.checkboxselected.setChecked(true);
                    tagss.setIstagchecked(true);
                }
                mActivity.filteredtagList(tagss);
            }
        });

        if(tagss.istagchecked){
            holder.checkboxselected.setVisibility(View.VISIBLE);
            holder.checkboxselected.setChecked(true);
        }else{
            if(mShowunchecked) {
                holder.checkboxselected.setVisibility(View.VISIBLE);
                holder.checkboxselected.setChecked(false);
            }else{
                holder.checkboxselected.setVisibility(View.GONE);
            }
        }

        if(tagss.getTags() != null && !tagss.getTags().isEmpty()) {
            holder.categoryname.setText(tagss.getTags().toString());
        }

        holder.checkboxselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tagss.istagchecked){
                    holder.checkboxselected.setChecked(false);
                    tagss.setIstagchecked(false);
                }else{
                    holder.checkboxselected.setChecked(true);
                    tagss.setIstagchecked(true);
                }
                mActivity.filteredtagList(tagss);
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = tags.size();
        } catch (Exception e) {

        }
        return count;
    }

    public void setthemeforView(final ViewHolder holder) {
        holder.checkboxselected.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView categoryname;
        final View mView;
        final CheckBox checkboxselected;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView.findViewById(R.id.category_item);
            categoryname = (TextView) itemView.findViewById(R.id.categorey_name);
            checkboxselected = (CheckBox) itemView.findViewById(R.id.checkboxselected);
        }
    }
}
