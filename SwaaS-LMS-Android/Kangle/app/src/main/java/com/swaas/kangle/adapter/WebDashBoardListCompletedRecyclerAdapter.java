package com.swaas.kangle.adapter;

import android.app.Activity;
import android.graphics.PorterDuff;
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

import java.util.List;

/**
 * Created by Sayellessh on 13-06-2017.
 */

public class WebDashBoardListCompletedRecyclerAdapter extends RecyclerView.Adapter<WebDashBoardListCompletedRecyclerAdapter.ViewHolder> {

    Activity mActivity;
    List<TopfiveDashBoardAssetDetailModel> topfiveDashBoardAssetDetailModels;

    public WebDashBoardListCompletedRecyclerAdapter(Activity activity, List<TopfiveDashBoardAssetDetailModel> list)  {
        mActivity =  activity;
        topfiveDashBoardAssetDetailModels = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.webdashboard_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TopfiveDashBoardAssetDetailModel model = topfiveDashBoardAssetDetailModels.get(position);

        holder.coursedirect.setVisibility(View.GONE);
        holder.coursename.setText(model.getCourse_Name().toString());
        if (model.getCourse_Status_String().equalsIgnoreCase("Completed")) {
            holder.enddate.setText(" "+mActivity.getResources().getString(R.string.completed_course));
            holder.enddate.setTextColor(mActivity.getResources().getColor(R.color.buttoncolor));
        }

        if(PreferenceUtils.getSubdomainName(mActivity).contains("tacobell")) {
            holder.progressstatus.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.tacobellbackground), PorterDuff.Mode.SRC_IN);
            Ion.with(holder.thumbnail).fitXY().placeholder(R.drawable.tacobell_default).fitXY().error(R.drawable.tacobell_default).fitXY().load(
                    (!TextUtils.isEmpty(model.getCourse_Image_URL()))? model.getCourse_Image_URL() : model.getCourse_Image_URL());
        }else{
            holder.progressstatus.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.loginbutton), PorterDuff.Mode.SRC_IN);
            Ion.with(holder.thumbnail).fitXY().placeholder(R.drawable.courses).fitXY().error(R.drawable.courses).fitXY().load(
                    (!TextUtils.isEmpty(model.getCourse_Image_URL()))? model.getCourse_Image_URL() : model.getCourse_Image_URL());
        }
        /*Ion.with(holder.thumbnail).fitXY().placeholder(R.drawable.icon_jpeg).fitXY().error(R.drawable.icon_jpeg).fitXY().load(
                (!TextUtils.isEmpty(model.getCourse_Image_URL()))? model.getCourse_Image_URL() : model.getCourse_Image_URL());*/

        if(model.getCourse_Description() != null || !model.getCourse_Description().equalsIgnoreCase("")) {
            holder.description_name.setVisibility(View.GONE);
            holder.description_name.setText(model.getCourse_Description());
        }else{
            holder.description_name.setVisibility(View.GONE);
        }
        //holder.startdate.setText(" "+DateHelper.getDisplayFormat(model.getValid_From_String(),"dd-MM-yyyy"));
        holder.startdate.setText(" "+model.getValid_From());
        holder.progressstatus.setProgress(100);
        //holder.progressstatus.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.tacobellbackground), PorterDuff.Mode.SRC_IN);
        holder.progressstatus.setScaleY(20f);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = topfiveDashBoardAssetDetailModels.size();
        } catch (Exception e) {

        }
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final ImageView thumbnail;
        final TextView coursename;
        final TextView description_name,startdate,enddate,coursedirect;
        final ProgressBar progressstatus;

        public ViewHolder(View itemView) {
            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.coursethumbnail);
            coursename = (TextView) itemView.findViewById(R.id.course_name);
            description_name = (TextView) itemView.findViewById(R.id.description_name);
            startdate = (TextView) itemView.findViewById(R.id.start_date);
            enddate = (TextView) itemView.findViewById(R.id.end_date);
            progressstatus = (ProgressBar)itemView.findViewById(R.id.progress_status);
            coursedirect = (TextView)itemView.findViewById(R.id.coursedirect);

        }
    }
}
