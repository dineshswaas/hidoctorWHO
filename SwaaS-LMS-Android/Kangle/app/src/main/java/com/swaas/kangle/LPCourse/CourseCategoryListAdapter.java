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
 * Created by Sayellessh on 11-08-2017.
 */

public class CourseCategoryListAdapter extends RecyclerView.Adapter<CourseCategoryListAdapter.ViewHolder> {

    CourseListActivity mActivity;
    List<CourseModel> cat;
    boolean mShowunchecked;

    public CourseCategoryListAdapter(Context context, List<CourseModel> categoryList,boolean showunchecked)  {
        mActivity = (CourseListActivity) context;
        cat = categoryList;
        mShowunchecked = showunchecked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.course_category_list_item, parent, false);
        return new CourseCategoryListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final CourseModel categories = cat.get(position);

        setthemeforView(holder);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mActivity.loadCoursebyCategory(categories);
                if(categories.iscatchecked){
                    holder.checkboxselected.setChecked(false);
                    categories.setIscatchecked(false);
                }else{
                    holder.checkboxselected.setChecked(true);
                    categories.setIscatchecked(true);
                }
                mActivity.filteredcatList(categories);
            }
        });

        if(categories.iscatchecked){
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

        holder.categoryname.setText(categories.getCategory_Name().toString());

        holder.checkboxselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(categories.iscatchecked){
                    holder.checkboxselected.setChecked(false);
                    categories.setIscatchecked(false);
                }else{
                    holder.checkboxselected.setChecked(true);
                    categories.setIscatchecked(true);
                }
                mActivity.filteredcatList(categories);
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = cat.size();
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
