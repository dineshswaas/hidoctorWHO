package com.swaas.kangle.LPCourse.Report;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swaas.kangle.LPCourse.model.LPCourseReportModel;
import com.swaas.kangle.R;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.DateHelper;

import java.util.List;

/**
 * Created by Sayellessh on 29-08-2017.
 */

public class LPCourseReportAdapter extends RecyclerView.Adapter<LPCourseReportAdapter.ViewHolder>{

    Context context;
    List<LPCourseReportModel> courseReportModelList;
    private static MyClickListener myClickListener;
    DateHelper datehelper;
    String mSectionDate;
    boolean fromtest;

    public LPCourseReportAdapter(Context context,List<LPCourseReportModel> courseModels,String SectionDate,boolean isfromtest) {
        this.context = context;
        this.courseReportModelList = courseModels;
        datehelper = new DateHelper();
        mSectionDate = SectionDate;
        fromtest = isfromtest;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lp_course_report_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final LPCourseReportModel courseReportModel = courseReportModelList.get(position);

        setthemeforView(holder);

        holder.assessmentDate.setText(datehelper.getDisplayFormatwithtime(courseReportModel.getFormatted_Section_Exam_Start_Time(),"dd-MMM-yyyy hh:mm:ss a"));
        holder.numberattempts.setText(context.getResources().getString(R.string.Attempt)+" : "+String.valueOf(courseReportModel.getAttempt_Number()));
        holder.lastdatetocomplete.setText(DateHelper.getDisplayFormat(mSectionDate,"yyyy-MM-dd hh:mm:ss"));
        if(courseReportModel.is_Qualified()){
            holder.result.setBackground(context.getResources().getDrawable(R.drawable.ic_check_circle_black_48dp));
        }else {
            holder.result.setBackground(context.getResources().getDrawable(R.drawable.ic_cancel_black_48dp));
        }
        if(courseReportModel.getShowFullSummary()>0 && courseReportModel.getNo_Of_Questions_Mapped()>0){
            holder.showsummary.setVisibility(View.VISIBLE);
        }

        if(courseReportModel.getShowFullSummary()>0 && courseReportModel.getNo_Of_Questions_Mapped()>0){
            holder.mitem.setEnabled(true);
        }else{
            holder.mitem.setEnabled(false);
        }

        holder.mitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onItemClick(courseReportModel);
            }
        });

        holder.showsummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onItemClick(courseReportModel);
            }
        });

        if(position == courseReportModelList.size()-1) {
            holder.end_bar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return courseReportModelList.size();
    }
    public void setOnItemClickListener(MyClickListener myClickListener){
        this.myClickListener = myClickListener;
    }

    public void setthemeforView(final ViewHolder holder){
        /*if(PreferenceUtils.getSubdomainName(context).contains("tacobell")){
            Constants.CARDBACKGROUND_COLOR = "#9C7EB4";
            Constants.COMPLETED_COLOR = "#0F9D58";
            Constants.INPROGRESS_COLOR = "#ffffff";
            Constants.EXPIRED_COLOR = "#FFFF0000";
            Constants.TEXT_COLOR = "#ffffff";
            Constants.ICON_COLOR = "#ffffff";
            Constants.PARTIALLY_COMPLETED_COLOR = "#FF0000FF";
        }else{
            Constants.CARDBACKGROUND_COLOR = "#9C7EB4";
            Constants.COMPLETED_COLOR = "#0F9D58";
            Constants.INPROGRESS_COLOR = "#ffffff";
            Constants.EXPIRED_COLOR = "#FFFF0000";
            Constants.TEXT_COLOR = "#ffffff";
            Constants.ICON_COLOR = "#ffffff";
            Constants.PARTIALLY_COMPLETED_COLOR = "#FF0000FF";
        }*/

        holder.numberattempts.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.assessmentDate.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.list_item.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        holder.required_icon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.TEXT_COLOR)));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final View showsummary;
        final TextView assessmentDate,lastdatetocomplete,numberattempts;
        final ImageView result;
        final View end_bar,mitem,list_item;
        final TextView required_icon;

        public ViewHolder(View itemView) {
            super(itemView);

            list_item = itemView.findViewById(R.id.list_item);
            required_icon = (TextView) itemView.findViewById(R.id.required_icon);
            result = (ImageView) itemView.findViewById(R.id.viewresult);
            showsummary = itemView.findViewById(R.id.viewsummary_icon);
            mitem = itemView.findViewById(R.id.viewsummary_result);
            assessmentDate = (TextView) itemView.findViewById(R.id.attemptdate);
            lastdatetocomplete = (TextView) itemView.findViewById(R.id.lastdatetocomplete);
            numberattempts = (TextView) itemView.findViewById(R.id.numberattempts);
            end_bar = itemView.findViewById(R.id.end_bar);
        }
    }

    public interface MyClickListener{
        public void onItemClick(LPCourseReportModel courseId);
    }
    public LPCourseReportModel getItemAt(int position){
        return courseReportModelList.get(position);
    }
}
