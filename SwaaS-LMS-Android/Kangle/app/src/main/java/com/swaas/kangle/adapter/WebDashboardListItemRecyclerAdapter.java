package com.swaas.kangle.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.swaas.kangle.R;
import com.swaas.kangle.models.TopfiveDashBoardAssetDetailModel;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;

import java.util.List;

/**
 * Created by Sayellessh on 12-06-2017.
 */

public class WebDashboardListItemRecyclerAdapter extends RecyclerView.Adapter<WebDashboardListItemRecyclerAdapter.ViewHolder> {

    Activity mActivity;
    List<TopfiveDashBoardAssetDetailModel> topfiveDashBoardAssetDetailModelList;
    private static MyClickListener myClickListener;

    public WebDashboardListItemRecyclerAdapter(Activity activity, List<TopfiveDashBoardAssetDetailModel> list)  {
        mActivity =  activity;
        topfiveDashBoardAssetDetailModelList = list ;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.webdashboard_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TopfiveDashBoardAssetDetailModel model = topfiveDashBoardAssetDetailModelList.get(position);

        holder.coursename.setText(model.getCourse_Name().toString());
        if(PreferenceUtils.getSubdomainName(mActivity).contains("tacobell")) {
            holder.progressstatus.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.tacobellbackground), PorterDuff.Mode.SRC_IN);
            Ion.with(holder.thumbnail).fitXY().placeholder(R.drawable.tacobell_default).fitXY().error(R.drawable.tacobell_default).fitXY().load(
                    (!TextUtils.isEmpty(model.getCourse_Image_URL()))? model.getCourse_Image_URL() : model.getCourse_Image_URL());
        }else{
            holder.progressstatus.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.loginbutton), PorterDuff.Mode.SRC_IN);
            Ion.with(holder.thumbnail).fitXY().placeholder(R.drawable.courses).fitXY().error(R.drawable.courses).fitXY().load(
                    (!TextUtils.isEmpty(model.getCourse_Image_URL()))? model.getCourse_Image_URL() : model.getCourse_Image_URL());
        }
        //if (model.getCourse_Status_String().equalsIgnoreCase("Completed")) {
        if (model.getCourse_Status_Value() == Constants.COMPLETED) {
            holder.enddate.setText(" "+mActivity.getResources().getString(R.string.completed_course));
            holder.enddate.setTextColor(mActivity.getResources().getColor(R.color.buttoncolor));
            holder.progressstatus.setProgress(100);
            //holder.progressstatus.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.tacobellbackground), PorterDuff.Mode.SRC_IN);
            holder.progressstatus.setScaleY(20f);
            holder.coursedirect.setText(mActivity.getResources().getString(R.string.view));
        }else if (model.getCourse_Status_Value() == Constants.YET_TO_START) {
            //holder.enddate.setText(" "+DateHelper.getDisplayFormat(model.getValid_To_String(),"dd-MM-yyyy"));
            holder.enddate.setText(" "+model.getValid_To());
            holder.enddate.setTextColor(Color.WHITE);
            holder.progressstatus.setProgress(0);
            //holder.progressstatus.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.tacobellbackground), PorterDuff.Mode.SRC_IN);
            holder.progressstatus.setScaleY(20f);
            holder.coursedirect.setText(mActivity.getResources().getString(R.string.Begin));
        }else if (model.getCourse_Status_Value() == Constants.INPROGRESS) {
           // holder.enddate.setText(" "+DateHelper.getDisplayFormat(model.getValid_To_String(),"dd-MM-yyyy"));
            holder.enddate.setText(" "+model.getValid_To());
            holder.enddate.setTextColor(Color.WHITE);
            double coursePercentage = 0.0;
            if(model.getNo_Of_Sections_Completed() > 0){
                coursePercentage = (Double.parseDouble(String.valueOf(model.getNo_Of_Sections_Completed())) / Double.parseDouble(String.valueOf(model.getTotal_Sections())));
                coursePercentage = Math.round(coursePercentage * 100);
                int progresspercent = Integer.parseInt(String.valueOf(Math.round(coursePercentage)));
                holder.progressstatus.setProgress(progresspercent);
            }else{
                holder.progressstatus.setProgress(5);
            }
            //holder.progressstatus.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.tacobellbackground), PorterDuff.Mode.SRC_IN);
            holder.progressstatus.setScaleY(20f);
            holder.coursedirect.setText(mActivity.getResources().getString(R.string.resume_course));
        }else if (model.getCourse_Status_Value() == Constants.MAX_ATTEMPTS_REACHED) {
            holder.enddate.setText(" "+mActivity.getResources().getString(R.string.max_attempts_reached_shortened));
            holder.enddate.setTextColor(mActivity.getResources().getColor(R.color.red));
            holder.progressstatus.setProgress(100);
            holder.progressstatus.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.topbar), PorterDuff.Mode.SRC_IN);
            holder.progressstatus.setScaleY(20f);
            holder.coursedirect.setText(mActivity.getResources().getString(R.string.Report));
        }else if (model.getCourse_Status_Value() == Constants.COURSE_EXPIRED) {
            holder.enddate.setText(" "+mActivity.getResources().getString(R.string.expired_shortened));
            holder.enddate.setTextColor(mActivity.getResources().getColor(R.color.red));
            holder.progressstatus.setProgress(100);
            holder.progressstatus.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.topbar), PorterDuff.Mode.SRC_IN);
            holder.progressstatus.setScaleY(20f);
            holder.coursedirect.setText(mActivity.getResources().getString(R.string.Report));

        }

        if(model.getCourse_Description() != null || !model.getCourse_Description().equalsIgnoreCase("")) {
            holder.description_name.setVisibility(View.GONE);
            holder.description_name.setText(model.getCourse_Description());
        }else{
            holder.description_name.setVisibility(View.GONE);
        }

        if(!mActivity.getResources().getBoolean(R.bool.portrait_only)){
            holder.coursedirect.setVisibility(View.VISIBLE);
        }

        //holder.startdate.setText(" "+ DateHelper.getDisplayFormat(model.getValid_From_String(),"dd-MM-yyyy"));
        holder.startdate.setText(" "+model.getValid_From());

        holder.coursedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onItemClick(model);
            }
        });

        holder.cardViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myClickListener != null) {
                    myClickListener.onItemClick(model);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = topfiveDashBoardAssetDetailModelList.size();
        } catch (Exception e) {

        }
        return count;
    }

    public void setOnItemClickListener(MyClickListener myClickListener){
        this.myClickListener = myClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final ImageView thumbnail;
        final TextView coursename;
        final TextView description_name,startdate,enddate,coursedirect;
        final ProgressBar progressstatus;
        CardView cardViewLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            cardViewLayout = (CardView)itemView.findViewById(R.id.cardviewLayout);
            thumbnail = (ImageView) itemView.findViewById(R.id.coursethumbnail);
            coursename = (TextView) itemView.findViewById(R.id.course_name);
            description_name = (TextView) itemView.findViewById(R.id.description_name);
            startdate = (TextView) itemView.findViewById(R.id.start_date);
            enddate = (TextView) itemView.findViewById(R.id.end_date);
            progressstatus = (ProgressBar)itemView.findViewById(R.id.progress_status);
            coursedirect = (TextView) itemView.findViewById(R.id.coursedirect);
        }
    }

    public interface MyClickListener{

        //public void onItemClick(int courseId, boolean isSequenceEnabled);
        public void onItemClick(TopfiveDashBoardAssetDetailModel course);

    }
}
