package com.swaas.kangle.CheckList.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.swaas.kangle.CheckList.model.UserforCourseChecklist;
import com.swaas.kangle.R;
import com.swaas.kangle.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Sayellessh on 17-09-2018.
 */

public class EvaluateUsersForChklstAdapter extends RecyclerView.Adapter<EvaluateUsersForChklstAdapter.CheckListRecyclerHolder> {

    Context context;
    List<UserforCourseChecklist> checklistModelList;
    private static MyClickListener myClickListener;
    private SimpleDateFormat simpleDateFormat;
    int Count = 0;

    public EvaluateUsersForChklstAdapter(Context context, List<UserforCourseChecklist> courseModels) {
        this.context = context;
        this.checklistModelList = courseModels;
    }


    public void setCourseListadapter(List<UserforCourseChecklist> courseModelList) {
        this.checklistModelList = courseModelList;
        notifyDataSetChanged();
    }

    @Override
    public CheckListRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.evaluate_userslist_forcoursechklst_item,parent,false);
        return new CheckListRecyclerHolder(view, context);
    }

    @Override
    public void onBindViewHolder(final CheckListRecyclerHolder holder, final int position) {
        final UserforCourseChecklist courseModel = checklistModelList.get(position);

        setthemeforView(holder);
        holder.user_name.setText(courseModel.getUser_Name());
        holder.course_name.setText(courseModel.getCourse_Name());
        courseModel.setEvaluation_Status("1");

        if(courseModel.getEvaluation_Status()=="1"){
            holder.userstatus.setChecked(true);
            holder.check.setVisibility(View.INVISIBLE);
            if(holder.check.isChecked()){
                if(courseModel.isCourseRestart()){
                    courseModel.setCourseRestart(false);
                }
                holder.check.setChecked(false);
            }
        }else{
            holder.userstatus.setChecked(false);
            holder.check.setVisibility(View.VISIBLE);
        }

        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(holder.check.isChecked()){
                    courseModel.setCourseRestart(true);
                }else{
                    courseModel.setCourseRestart(false);
                }
            }
        });

        holder.userstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(courseModel.getEvaluation_Status()=="1"){
                    courseModel.setEvaluation_Status("0");
                    holder.userstatus.setChecked(false);
                    holder.check.setVisibility(View.VISIBLE);
                }else{
                    courseModel.setEvaluation_Status("1");
                    holder.userstatus.setChecked(true);
                    holder.check.setVisibility(View.INVISIBLE);
                    if(holder.check.isChecked()){
                        if(courseModel.isCourseRestart()){
                            courseModel.setCourseRestart(false);
                        }
                        holder.check.setChecked(false);
                    }
                }
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

    public void setthemeforView(final CheckListRecyclerHolder holder){

        holder.category_item.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        holder.user_name.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.course_name.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
    }

    public class CheckListRecyclerHolder extends RecyclerView.ViewHolder {
        Context ctxt;
        View category_item;
        //SwitchCompat userstatus;
        Switch userstatus;
        LinearLayout userdetails,status;
        TextView user_name, course_name;
        CheckBox check;

        public CheckListRecyclerHolder(View itemView, Context context) {
            super(itemView);
            this.ctxt = context;
            category_item = itemView.findViewById(R.id.user_item);
            //userstatus = (SwitchCompat) itemView.findViewById(R.id.userstatus);
            userstatus = (Switch) itemView.findViewById(R.id.userstatus);
            userdetails = (LinearLayout) itemView.findViewById(R.id.userdetails);
            status = (LinearLayout)  itemView.findViewById(R.id.status);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            course_name = (TextView) itemView.findViewById(R.id.course_name);
            check = (CheckBox) itemView.findViewById(R.id.check);
        }
    }

    public interface MyClickListener {

        //public void onItemClick(int courseId, boolean isSequenceEnabled);
        public void onItemClick(int courseId, int checklistStatus, int CUAId);

    }
}
