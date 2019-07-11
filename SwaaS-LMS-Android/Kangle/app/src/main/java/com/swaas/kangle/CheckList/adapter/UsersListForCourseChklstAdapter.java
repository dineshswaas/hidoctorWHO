package com.swaas.kangle.CheckList.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.swaas.kangle.CheckList.CourseChecklistUsersActivity;
import com.swaas.kangle.CheckList.model.UserforCourseChecklist;
import com.swaas.kangle.R;
import com.swaas.kangle.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Sayellessh on 17-09-2018.
 */

public class UsersListForCourseChklstAdapter extends RecyclerView.Adapter<UsersListForCourseChklstAdapter.CheckListRecyclerHolder> {

    Context context;
    CourseChecklistUsersActivity mActivity;
    List<UserforCourseChecklist> checklistModelList;
    private static MyClickListener myClickListener;
    private SimpleDateFormat simpleDateFormat;
    int Count = 0;

    public UsersListForCourseChklstAdapter(CourseChecklistUsersActivity activity, Context context, List<UserforCourseChecklist> courseModels) {
        this.context = context;
        this.checklistModelList = courseModels;
        mActivity = activity;
    }


    public void setCourseListadapter(List<UserforCourseChecklist> courseModelList) {
        this.checklistModelList = courseModelList;
        notifyDataSetChanged();
    }

    @Override
    public CheckListRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userslist_forcoursechklst_item,parent,false);
        return new CheckListRecyclerHolder(view, context);
    }

    @Override
    public void onBindViewHolder(final CheckListRecyclerHolder holder, int position) {
        final UserforCourseChecklist courseModel = checklistModelList.get(position);

        setupthemeforView(holder);
        holder.user_name.setText(courseModel.getUser_Name());
        holder.course_name.setText(courseModel.getCourse_Name());
        holder.section_name.setText(courseModel.getSection_Name());
        if(courseModel.getEvaluation_Status().equals("1")){
            holder.status_img.setVisibility(View.VISIBLE);
            holder.report_button.setVisibility(View.VISIBLE);
            holder.status_img.setImageResource(R.drawable.ic_check_circle_black_48dp);
            holder.checkboxselected.setVisibility(View.INVISIBLE);
            holder.course_status.setText(context.getResources().getString(R.string.completed_course));
            holder.course_status.setTextColor(Color.parseColor(Constants.COMPLETED_COLOR));
        }else if(courseModel.getEvaluation_Status().equals("0")){
            holder.status_img.setVisibility(View.VISIBLE);
            holder.report_button.setVisibility(View.VISIBLE);
            holder.status_img.setImageResource(R.drawable.ic_cancel_black_48dp);
            holder.checkboxselected.setVisibility(View.VISIBLE);
            holder.course_status.setText(context.getResources().getString(R.string.try_again));
            holder.course_status.setTextColor(Color.parseColor(Constants.EXPIRED_COLOR));
        }else if(courseModel.getEvaluation_Status().equals("-2")) {
            holder.status_img.setVisibility(View.INVISIBLE);
            holder.report_button.setVisibility(View.GONE);
            holder.status_img.setImageResource(R.drawable.ic_status_gray);
            holder.checkboxselected.setVisibility(View.VISIBLE);
            holder.course_status.setText(context.getResources().getString(R.string.drafted));
            holder.course_status.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            holder.status_img.setVisibility(View.VISIBLE);
            holder.report_button.setVisibility(View.GONE);
            holder.status_img.setImageResource(R.drawable.ic_status_gray);
            holder.checkboxselected.setVisibility(View.VISIBLE);
            holder.course_status.setText(context.getResources().getString(R.string.yet_to_evaluate));
            holder.course_status.setTextColor(Color.parseColor(Constants.YET_TO_START_COLOR));
        }

        if(courseModel.isChoosenuser()){
            holder.checkboxselected.setImageResource(R.mipmap.ic_green_selected);
        }else{
            holder.checkboxselected.setImageResource(R.mipmap.ic_activity_add_new);
        }

        holder.checkboxselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = 0;
                for(UserforCourseChecklist usr : checklistModelList){
                    if(usr.isChoosenuser()){
                        size += 1;
                    }
                }
                if(courseModel.isChoosenuser()){
                    holder.checkboxselected.setImageResource(R.mipmap.ic_activity_add_new);
                    courseModel.setChoosenuser(false);
                }else{
                    if(size != 10) {
                        holder.checkboxselected.setImageResource(R.mipmap.ic_green_selected);
                        courseModel.setChoosenuser(true);
                    }else{
                        Toast.makeText(context,"can't choose more than 10 user",Toast.LENGTH_SHORT).show();
                    }
                }
                notifyDataSetChanged();
                mActivity.updateUserSelectionCount();
            }
        });

        holder.report_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onItemClick(courseModel);
            }
        });
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public int getItemCount() {
        return checklistModelList.size();
    }

    public void setupthemeforView(CheckListRecyclerHolder holder){
        holder.category_item.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        holder.user_name.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.course_name.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.section_name.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
      //  holder.course_status.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.report_button.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.report_button.setTypeface(holder.report_button.getTypeface(), Typeface.ITALIC);
        holder.report_button.setText(Html.fromHtml("<u>"+mActivity.getResources().getString(R.string.view_report)+"</u>"));
    }

    public class CheckListRecyclerHolder extends RecyclerView.ViewHolder {
        Context ctxt;
        View category_item;
        ImageView checkboxselected;
        LinearLayout userdetails,status;
        TextView user_name, course_name,course_status,report_button,section_name;
        ImageView status_img;

        public CheckListRecyclerHolder(View itemView, Context context) {
            super(itemView);
            this.ctxt = context;
            category_item = itemView.findViewById(R.id.category_item);
            checkboxselected = (ImageView) itemView.findViewById(R.id.checkboxselected);
            userdetails = (LinearLayout) itemView.findViewById(R.id.userdetails);
            status = (LinearLayout)  itemView.findViewById(R.id.status);
            status_img = (ImageView) itemView.findViewById(R.id.status_img);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            course_name = (TextView) itemView.findViewById(R.id.course_name);
            course_status = (TextView) itemView.findViewById(R.id.course_status);
            report_button = (TextView) itemView.findViewById(R.id.report_button);

            section_name = (TextView) itemView.findViewById(R.id.section_name);
        }
    }

    public interface MyClickListener {

        //public void onItemClick(int courseId, boolean isSequenceEnabled);
        public void onItemClick(UserforCourseChecklist courseId);

    }
}
