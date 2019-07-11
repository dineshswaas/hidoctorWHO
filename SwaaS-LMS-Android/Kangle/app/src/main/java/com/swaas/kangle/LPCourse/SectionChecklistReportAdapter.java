package com.swaas.kangle.LPCourse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swaas.kangle.CheckList.model.UserforCourseChecklist;
import com.swaas.kangle.R;

import java.util.List;

/**
 * Created by Sayellessh on 25-10-2018.
 */

public class SectionChecklistReportAdapter extends RecyclerView.Adapter<SectionChecklistReportAdapter.CheckListRecyclerHolder>{

    Context context;
    List<UserforCourseChecklist> checklistModelList;
    private static MyClickListener myClickListener;
    int Count = 0;

    public SectionChecklistReportAdapter(Context context, List<UserforCourseChecklist> courseModels) {
        this.context = context;
        this.checklistModelList = courseModels;
    }

    @Override
    public CheckListRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userslist_forcoursechklst_item,parent,false);
        return new CheckListRecyclerHolder(view, context);
    }

    @Override
    public void onBindViewHolder(final CheckListRecyclerHolder holder, int position) {
        final UserforCourseChecklist courseModel = checklistModelList.get(position);

        holder.user_name.setText(courseModel.getChecklist_Name());
        holder.course_name.setText(courseModel.getCourse_Name());
        holder.section_name.setText(courseModel.getSection_Name());
        holder.status_img.setVisibility(View.GONE);
        holder.status.setVisibility(View.GONE);

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
