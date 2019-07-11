package com.swaas.kangle.LPCourse.Report;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swaas.kangle.LPCourse.model.LPCourseReportSummaryModel;
import com.swaas.kangle.R;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.DateHelper;

import java.util.List;

/**
 * Created by Sayellessh on 29-08-2017.
 */

public class LPCourseSummaryReportAdapter extends RecyclerView.Adapter<LPCourseSummaryReportAdapter.ViewHolder> {

    Context context;
    List<LPCourseReportSummaryModel> courseReportModelList;
    private static MyClickListener myClickListener;
    DateHelper datehelper;
    Boolean issummary;

    public LPCourseSummaryReportAdapter(Context context, List<LPCourseReportSummaryModel> courseModels, Boolean iscourseReport) {
        this.context = context;
        this.courseReportModelList = courseModels;
        datehelper = new DateHelper();
        this.issummary = iscourseReport;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lp_course_report_summary_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final LPCourseReportSummaryModel courseReportModel = courseReportModelList.get(position);

        setthemeforView(holder);

        holder.questiontitle.setText(courseReportModel.getQuestion_Text());
        if (courseReportModel.is_Correct()) {
            //holder.result.setBackgroundColor(context.getResources().getColor(R.color.buttoncolor));
            holder.result_icon.setImageResource(R.drawable.ic_check_circle_black_48dp);
        } else {

            //holder.result.setBackgroundColor(context.getResources().getColor(R.color.red));
            holder.result_icon.setImageResource(R.drawable.ic_cancel_black_48dp);
            if (courseReportModel.getQuestion_Type()==6)
            {
                holder.result_icon.setImageResource(R.drawable.ic_check_circle_black_48dp);
            }
        }

        if(!courseReportModel.getExplanation().equalsIgnoreCase("")){
            holder.showsummary.setVisibility(View.VISIBLE);
            holder.showsummary.setImageResource(R.drawable.ic_info_black_24dp);
        }

        holder.showsummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onItemClick(courseReportModel.getExplanation());
            }
        });
        holder.section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AnswerPage.class);
                intent.putExtra("course",courseReportModel.getCourse_Name());
                intent.putExtra("question",courseReportModel.getQuestion_Text());
                intent.putExtra("answer",courseReportModel.getAnswer_Text());
                intent.putExtra("section",courseReportModel.getSection_Name());
                intent.putExtra("obtained",courseReportModel.getMarks_Given());
                intent.putExtra("total",courseReportModel.getMarks_Allotted());
                context.startActivity(intent);
            }
        });

        if(position == courseReportModelList.size()-1) {
            holder.end_line.setVisibility(View.GONE);
        }
        if(issummary)
        {
            holder.showsummary.setImageResource(R.drawable.ic_navigate_next_white_36dp);
            holder.showsummary.setEnabled(false);
            holder.showsummary.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.section.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return courseReportModelList.size();
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void setthemeforView(final ViewHolder holder) {

        holder.questiontitle.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.showsummary.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView showsummary,result_icon;
        final TextView questiontitle;
        final View result,end_line;
        final RelativeLayout section;

        public ViewHolder(View itemView) {
            super(itemView);

            result = itemView.findViewById(R.id.viewresult);
            result_icon = (ImageView) itemView.findViewById(R.id.viewresult_icon);
            showsummary = (ImageView) itemView.findViewById(R.id.viewsummary_icon);
            questiontitle = (TextView) itemView.findViewById(R.id.attemptdate);
            end_line = itemView.findViewById(R.id.end_line);
            section = itemView.findViewById(R.id.section_linear_layout);
        }
    }

    public interface MyClickListener {
        public void onItemClick(String explanation);
    }

    public LPCourseReportSummaryModel getItemAt(int position) {
        return courseReportModelList.get(position);
    }
}
