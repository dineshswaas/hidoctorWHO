package com.swaas.kangle.CheckList.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.swaas.kangle.CheckList.CourseChecklistListActivity;
import com.swaas.kangle.CheckList.model.CheckListModel;
import com.swaas.kangle.R;
import com.swaas.kangle.utils.Constants;

import java.util.List;

/**
 * Created by Sayellessh on 20-11-2018.
 */

public class CourseCheckListCategoryListTextRecyclerAdapter extends RecyclerView.Adapter<CourseCheckListCategoryListTextRecyclerAdapter.ViewHolder>{

    CourseChecklistListActivity mActivity;
    List<CheckListModel> cat;

    public CourseCheckListCategoryListTextRecyclerAdapter(Context context, List<CheckListModel> categoryList)  {
        mActivity = (CourseChecklistListActivity) context;
        cat = categoryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.course_category_list_item, parent, false);
        return new CourseCheckListCategoryListTextRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final CheckListModel categories = cat.get(position);
        setthemeforView(holder);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mActivity.loadchecklistbyCategory(categories.getCategory_Name().toString());
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
            holder.checkboxselected.setVisibility(View.VISIBLE);
            holder.checkboxselected.setChecked(false);
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
