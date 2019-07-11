package com.swaas.kangle.survey;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.swaas.kangle.CheckList.model.CheckListModel;
import com.swaas.kangle.R;
import com.swaas.kangle.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Sayellessh on 29-05-2018.
 */

public class SurveyOnetimeCheckListItemAdapter extends RecyclerView.Adapter<SurveyOnetimeCheckListItemAdapter.CheckListRecyclerHolder>{

    Context context;
    List<CheckListModel> checklistModelList;
    private static MyClickListener myClickListener;
    private SimpleDateFormat simpleDateFormat;

    public SurveyOnetimeCheckListItemAdapter(Context context, List<CheckListModel> courseModels) {
        this.context = context;
        this.checklistModelList = courseModels;
        simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    }



    public void setCourseListadapter(List<CheckListModel> courseModelList){
        this.checklistModelList = courseModelList;
        notifyDataSetChanged();
    }

    @Override
    public CheckListRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.check_list_item, parent, false);
        return new CheckListRecyclerHolder(view,context);
    }

    @Override
    public void onBindViewHolder(CheckListRecyclerHolder holder, int position) {
        final CheckListModel courseModel = checklistModelList.get(position);

        setthemeforView(holder);
        holder.mTitle.setText(courseModel.getChecklist_Name()+" - "+courseModel.getPublish_Date_String());
        holder.mThumbnail.setVisibility(View.VISIBLE);
        holder.defaultthumbnailview.setVisibility(View.GONE);
        Ion.with(holder.mThumbnail).fitXY().placeholder(R.drawable.icon_checklist_default).fitXY().error(R.drawable.icon_checklist_default).fitXY().load(
                (!TextUtils.isEmpty(courseModel.getChecklist_Image_URL())) ?
                        courseModel.getChecklist_Image_URL() : courseModel.getChecklist_Image_URL());
        /*if(courseModel.getChecklist_Image_URL() != null && !courseModel.getChecklist_Image_URL().isEmpty()){
            holder.mThumbnail.setVisibility(View.VISIBLE);
            holder.defaultthumbnailview.setVisibility(View.GONE);
            Ion.with(holder.mThumbnail).fitXY().placeholder(R.drawable.icon_jpeg).fitXY().error(R.drawable.icon_jpeg).fitXY().load(
                    (!TextUtils.isEmpty(courseModel.getChecklist_Image_URL())) ?
                            courseModel.getChecklist_Image_URL() : courseModel.getChecklist_Image_URL());
        }else{
            holder.mThumbnail.setVisibility(View.GONE);
            holder.defaultthumbnailview.setVisibility(View.VISIBLE);
            String name = courseModel.getChecklist_Name().substring(0,2);
            holder.firstText.setText(name.toString());
        }*/

        holder.categoreyname.setText(courseModel.getCategory_Name());
        if(courseModel.getChecklist_Status_Value() == Constants.COMPLETED){
            holder.mCourseEndTime.setText(context.getResources().getString(R.string.completed_course));
            holder.mCourseEndTime.setTextColor(Color.parseColor(Constants.COMPLETED_COLOR));
        }else if(courseModel.getChecklist_Status_Value() == Constants.COURSE_EXPIRED){
            holder.mCourseEndTime.setText(context.getResources().getString(R.string.expired_shortened));
            holder.mCourseEndTime.setTextColor(Color.parseColor(Constants.EXPIRED_COLOR));
        } else {
            holder.mCourseEndTime.setText(context.getResources().getString(R.string.expires_on)+" \n"+courseModel.getValid_To());
            holder.mCourseEndTime.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        }

        holder.typeofChklist.setText("O");

        holder.cardViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myClickListener != null) {
                    myClickListener.onItemClick(courseModel.getChecklist_Id(),courseModel.getChecklist_Status_Value()
                            ,courseModel.getChecklist_User_Assignment_Id());
                }
            }
        });
    }

    public void setOnItemClickListener(MyClickListener myClickListener){
        this.myClickListener = myClickListener;
    }

    @Override
    public int getItemCount() {
        return checklistModelList.size();
    }

    public void setthemeforView(final CheckListRecyclerHolder holder){

        holder.cardViewLayout.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        holder.mTitle.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.categoreyname.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
    }

    public class CheckListRecyclerHolder extends RecyclerView.ViewHolder{
        Context ctxt;
        View mView;
        RelativeLayout cardViewLayout;
        TextView mTitle,categoreyname;
        ImageView mThumbnail,timedurationicon;
        View defaultthumbnailview;
        TextView firstText;
        TextView mCourseEndTime;
        TextView typeofChklist;

        public CheckListRecyclerHolder(View itemView,Context context) {
            super(itemView);
            this.ctxt = context;
            cardViewLayout = (RelativeLayout)itemView.findViewById(R.id.cardviewLayout);
            mTitle = (TextView)itemView.findViewById(R.id.texttitle);
            mThumbnail = (ImageView)itemView.findViewById(R.id.thumbnail);
            defaultthumbnailview = itemView.findViewById(R.id.defaultthumbnailview);
            timedurationicon = (ImageView)itemView.findViewById(R.id.timedurationicon);
            categoreyname = (TextView)itemView.findViewById(R.id.categoreyname);
            mCourseEndTime = (TextView)itemView.findViewById(R.id.end_time);
            firstText = (TextView)itemView.findViewById(R.id.firstText);
            typeofChklist = (TextView)itemView.findViewById(R.id.type_of_chklist_text);
        }
    }

    public interface MyClickListener{

        //public void onItemClick(int courseId, boolean isSequenceEnabled);
        public void onItemClick(int courseId,int checklistStatus,int CUAId);

    }
}
