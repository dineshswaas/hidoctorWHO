package com.swaas.kangle.LPCourse;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.koushikdutta.ion.Ion;
import com.swaas.kangle.R;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.DateHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.swaas.hari.hidoctor.ExoPlayerView.metadata.id3.ChapterFrame.ID;

/**
 * Created by saiprasath on 8/10/2017.
 */

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CoursesRecyclerHolder> {
    Context context;
    List<CourseModel> courseModelList;
    private static MyClickListener myClickListener;
    private SimpleDateFormat simpleDateFormat;
    //private boolean isSequenceEnabled = true;

    public CourseListAdapter(Context context,List<CourseModel> courseModels) {
        this.context = context;
        this.courseModelList = courseModels;
        simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    }

    public void setCourseListadapter(List<CourseModel> courseModelList){
        this.courseModelList = courseModelList;
        notifyDataSetChanged();
    }


    @Override
    public CoursesRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_recycler, parent, false);
        return new CoursesRecyclerHolder(view,context);
    }

    @Override
    public void onBindViewHolder(final CoursesRecyclerHolder holder, final int position) {
        final CourseModel courseModel = courseModelList.get(position);

        setthemeforView(holder);
        holder.mTitle.setText(courseModel.getCourse_Name());
        holder.mSubTitle.setText(courseModel.getSectionDetails().size()+" "+context.getResources().getString(R.string.section_s));
        holder.mSubTitle1.setText(courseModel.getNo_of_Assets()+"");
        holder.mSubTitle2.setText(courseModel.getNo_of_Questions()+"");
        holder.mSubTitle3.setText((courseModel.getNo_Course_Checklist()+courseModel.getNo_Section_Checklist())+"");
        /*if(courseModel.getCourse_Description().isEmpty()){
            holder.mSubTitle.setVisibility(View.GONE);
            *//*LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(4,5,0,5);
            holder.mTitle.setLayoutParams(layoutParams);*//*
        }
        else {
            holder.mSubTitle.setText(courseModel.getCourse_Description());
        }*/
        if(!courseModel.getPrerequisite().equalsIgnoreCase("") &&
                courseModel.getCourse_Status_Value() == 0) {
            holder.blockcourse.setVisibility(View.VISIBLE);
        } else {
            holder.blockcourse.setVisibility(View.GONE);
        }

        holder.cardViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (holder.mPlaybtn.getText().toString().startsWith(context.getResources().getString(R.string.expired))){
//                    holder.cardViewLayout.setClickable(false);
//                    Toast.makeText(context, "You cannot click as the course is expired", Toast.LENGTH_SHORT).show();
//                }
                if (myClickListener != null) {
                    //myClickListener.onItemClick(courseModel.getCourse_Id(),isSequenceEnabled);
                    myClickListener.onItemClick(courseModel.getCourse_Id());
                }
            }
        });
        //holder.mCourseStartTime.setText(" "+DateHelper.getDisplayFormat(courseModel.getValid_From_String(),"dd-MM-yyyy"));
        holder.mCourseStartTime.setText(" "+courseModel.getValid_From());
        if(PreferenceUtils.getSubdomainName(context).contains("tacobell")){
            //holder.mProgressStatus.getProgressDrawable().setColorFilter(context.getResources().getColor(R.color.tacobellbackground), PorterDuff.Mode.SRC_IN);
            holder.mProgressStatus.setForegroundProgressColor(context.getResources().getColor(R.color.tacobellbackground));
            Ion.with(holder.mThumbnail).fitXY().placeholder(R.drawable.tacobell_default).fitXY().error(R.drawable.tacobell_default).fitXY().load(
                    (!TextUtils.isEmpty(courseModel.getCourse_Image_URL())) ?
                            courseModel.getCourse_Image_URL() : courseModel.getCourse_Image_URL());
            holder.mThumbnail.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.black)));
            holder.mThumbnail.setBackgroundColor(context.getResources().getColor(R.color.black));
        }else{
            //holder.mProgressStatus.getProgressDrawable().setColorFilter(context.getResources().getColor(R.color.loginbutton), PorterDuff.Mode.SRC_IN);
            holder.mProgressStatus.setForegroundProgressColor(context.getResources().getColor(R.color.loginbutton));
            Ion.with(holder.mThumbnail).fitXY().placeholder(R.drawable.courses).fitXY().error(R.drawable.courses).fitXY().load(
                    (!TextUtils.isEmpty(courseModel.getCourse_Image_URL())) ?
                            courseModel.getCourse_Image_URL() : courseModel.getCourse_Image_URL());
            //holder.mThumbnail.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));

            holder.mThumbnail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.ICON_COLOR)));
            holder.mThumbnail.setBackgroundColor(Color.parseColor(Constants.ICON_COLOR));

        }


        if (courseModel.getCourse_Status_Value() == Constants.COMPLETED) {
            holder.mCompletionString.setVisibility(View.VISIBLE);
            holder.mCompletionString.setText("100%");
            holder.mCourseStartTime.setVisibility(View.GONE);
            holder.mCourseEndTime.setVisibility(View.VISIBLE);
            holder.mCourseEndTime.setText(" "+context.getResources().getString(R.string.completed_course));
            holder.mProgressStatus.setProgress(100);
            //holder.mProgressStatus.getProgressDrawable().setColorFilter(context.getResources().getColor(R.color.tacobellbackground), PorterDuff.Mode.SRC_IN);
            holder.mProgressStatus.setScaleY(1.1f);
            holder.coursedirect.setText(context.getResources().getString(R.string.view));
            holder.new_tag.setVisibility(View.GONE);
            if(courseModel.getEvaluation_Type() == 1){
                if(courseModel.getEvaluation_Type() == 1 && courseModel.getEvaluation_Status() == 1){
                    holder.mCourseEndTime.setText(" "+context.getResources().getString(R.string.completed_course));
                    holder.mCourseEndTime.setTextColor(Color.parseColor(Constants.COMPLETED_COLOR));
                }else{
                    holder.mCourseEndTime.setText(" "+context.getResources().getString(R.string.pending_for_evaluation));
                    holder.mCourseEndTime.setTextColor(Color.parseColor(Constants.PENDING_APPROVAl_COLOR));
                }
            }else{
                holder.mCourseEndTime.setText(" "+context.getResources().getString(R.string.completed_course));
                holder.mCourseEndTime.setTextColor(Color.parseColor(Constants.COMPLETED_COLOR));
            }
        } else if (courseModel.getCourse_Status_Value() == Constants.INPROGRESS) {
            double coursePercentage = (Double.parseDouble(String.valueOf(courseModel.getNo_Of_Sections_Completed())) / Double.parseDouble(String.valueOf(courseModel.getTotal_Sections())));
            coursePercentage = Math.round(coursePercentage * 100);
            if (coursePercentage > 0.0) {
                int progresspercent = Integer.parseInt(String.valueOf(Math.round(coursePercentage)));
                holder.mCompletionString.setVisibility(View.VISIBLE);
                holder.mProgressStatus.setProgress(progresspercent);
                holder.mProgressStatus.setScaleY(1.1f);
                //holder.mProgressStatus.getProgressDrawable().setColorFilter(context.getResources().getColor(R.color.tacobellbackground), PorterDuff.Mode.SRC_IN);
                holder.mProgressStatus.setProgress(progresspercent);
                holder.mCompletionString.setText(progresspercent + context.getResources().getString(R.string.complete_percent));
            } else if (coursePercentage == 100.0) {
                holder.mCompletionString.setVisibility(View.VISIBLE);
                holder.mProgressStatus.setProgress(100);
                holder.mProgressStatus.setScaleY(1.1f);
                holder.mCompletionString.setText("100%");
            } else {
                holder.mCompletionString.setVisibility(View.VISIBLE);
                //holder.mCompletionString.setText("1" + context.getResources().getString(R.string.complete_percent));
                holder.mCompletionString.setText("1%");
                holder.mProgressStatus.setProgress(5);
                //holder.mProgressStatus.getProgressDrawable().setColorFilter(context.getResources().getColor(R.color.tacobellbackground), PorterDuff.Mode.SRC_IN);
                holder.mProgressStatus.setScaleY(1.1f);
            }
            holder.mCourseStartTime.setVisibility(View.GONE);
            holder.mCourseEndTime.setVisibility(View.VISIBLE);
            //holder.mCourseEndTime.setText(" "+DateHelper.getDisplayFormat(courseModel.getValid_To_String(),"dd-MM-yyyy"));
            holder.mCourseEndTime.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
            holder.coursedirect.setText(context.getResources().getString(R.string.resume_course));
            holder.new_tag.setVisibility(View.GONE);

            if(courseModel.getIsCourseRestart() == 1){
                holder.mCourseEndTime.setText(context.getResources().getString(R.string.reassigned_by));
                holder.mCourseEndTime.setTextColor(Color.parseColor(Constants.COMPLETED_COLOR));
            }else{
                holder.mCourseEndTime.setText(context.getResources().getString(R.string.expires_on)+" "+courseModel.getValid_To());
                holder.mCourseEndTime.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
                String validdateString = DateHelper.getDisplayFormat(courseModel.getValid_To(),"dd-MMM-yyyy ");
                DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                Date curdate = new Date();
                Date Valid_To = new Date();
                try {
                    Valid_To = (Date)formatter.parse(courseModel.getValid_To());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            /*long diff = Valid_To.getTime() - curdate.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000);*/
                int diffInDays = (int) ((Valid_To.getTime() - curdate.getTime()) / (1000 * 60 * 60 * 24));
                if(diffInDays > 0){
                    if(diffInDays < 5) {
                        if (diffInDays == 0) {
                            holder.mCourseEndTime.setText(R.string.expires_today);
                        } else if (diffInDays == 1) {
                            holder.mCourseEndTime.setText(R.string.expires_tomo);
                        } else {
                            holder.mCourseEndTime.setText(R.string.expires_in + "\n" + diffInDays + R.string.days);
                        }
                        holder.mCourseEndTime.setTextColor(Color.parseColor(Constants.EXPIRED_COLOR));
                    }
                }
            }

        } else if (courseModel.getCourse_Status_Value() == Constants.YET_TO_START) {
            holder.mCompletionString.setVisibility(View.VISIBLE);
            holder.mCompletionString.setText("-");
            holder.mCourseStartTime.setVisibility(View.GONE);
            holder.mCourseEndTime.setVisibility(View.VISIBLE);
            //holder.mCourseEndTime.setText(" "+DateHelper.getDisplayFormat(courseModel.getValid_To_String(),"dd-MM-yyyy"));
            //holder.mCourseEndTime.setText("Expires on"+"\n"+courseModel.getValid_To_String());
            holder.mCourseEndTime.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
            holder.mProgressStatus.setProgress(0);
            //holder.mProgressStatus.getProgressDrawable().setColorFilter(context.getResources().getColor(R.color.tacobellbackground), PorterDuff.Mode.SRC_IN);
            holder.mProgressStatus.setScaleY(1.1f);
            holder.coursedirect.setText(context.getResources().getString(R.string.Begin));
            holder.new_tag.setVisibility(View.GONE);

            if(courseModel.getIsCourseRestart() == 1){
                holder.mCourseEndTime.setText(context.getResources().getString(R.string.reassigned_by));
            }else{
                holder.mCourseEndTime.setText(context.getResources().getString(R.string.expires_on)+" "+courseModel.getValid_To());

                String validdateString = DateHelper.getDisplayFormat(courseModel.getValid_From(),"dd-MMM-yyyy");
                DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                Date curdate = new Date();
                Date Valid_To = new Date();
                try {
                    Valid_To = (Date)formatter.parse(courseModel.getValid_To());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int diffInDays = (int) ((Valid_To.getTime() - curdate.getTime()) / (1000 * 60 * 60 * 24));
                if(diffInDays > 0){
                    if(diffInDays < 5) {
                        if (diffInDays == 0) {
                            holder.mCourseEndTime.setText(R.string.expires_today);
                        } else if (diffInDays == 1) {
                            holder.mCourseEndTime.setText(R.string.expires_tomo);
                        } else {
                            holder.mCourseEndTime.setText(R.string.expires_in + "\n" + diffInDays + R.string.days);
                        }
                        holder.mCourseEndTime.setTextColor(Color.parseColor(Constants.EXPIRED_COLOR));
                    }
                }
            }
        } else if (courseModel.getCourse_Status_Value() == Constants.MAX_ATTEMPTS_REACHED) {
            double coursePercentage = 0.0;
            int progresspercent = 5;
            if(courseModel.getNo_Of_Sections_Completed()>0){
                coursePercentage =(Double.parseDouble(String.valueOf(courseModel.getNo_Of_Sections_Completed())) / Double.parseDouble(String.valueOf(courseModel.getTotal_Sections())));
                coursePercentage = Math.round(coursePercentage * 100);
                progresspercent = Integer.parseInt(String.valueOf(Math.round(coursePercentage)));
            }else{
                progresspercent = 100;
            }

            holder.mCompletionString.setVisibility(View.VISIBLE);
            holder.mCompletionString.setText("-");
            holder.mCourseStartTime.setVisibility(View.GONE);
            holder.mCourseEndTime.setVisibility(View.VISIBLE);
            holder.mCourseEndTime.setText(" "+context.getResources().getString(R.string.max_attempts_reached_shortened));
            holder.mCourseEndTime.setTextColor(Color.parseColor(Constants.EXPIRED_COLOR));
            holder.mProgressStatus.setProgress(progresspercent);
            //holder.mProgressStatus.getProgressDrawable().setColorFilter(context.getResources().getColor(R.color.topbar), PorterDuff.Mode.SRC_IN);
            holder.mProgressStatus.setForegroundProgressColor(context.getResources().getColor(R.color.topbar));
            holder.mProgressStatus.setScaleY(1.1f);
            holder.coursedirect.setText(context.getResources().getString(R.string.Report));
            holder.new_tag.setVisibility(View.GONE);
        } else if (courseModel.getCourse_Status_Value() == Constants.COURSE_EXPIRED) {
            double coursePercentage = 0.0;
            int progresspercent = 5;
            if(courseModel.getNo_Of_Sections_Completed()>0){
                coursePercentage =(Double.parseDouble(String.valueOf(courseModel.getNo_Of_Sections_Completed())) / Double.parseDouble(String.valueOf(courseModel.getTotal_Sections())));
                coursePercentage = Math.round(coursePercentage * 100);
                progresspercent = Integer.parseInt(String.valueOf(Math.round(coursePercentage)));
            }else{
                progresspercent = 100;
            }

            holder.mCompletionString.setVisibility(View.VISIBLE);
            holder.mCompletionString.setText("-");
            holder.mCourseStartTime.setVisibility(View.GONE);
            holder.mCourseEndTime.setVisibility(View.VISIBLE);
            holder.mCourseEndTime.setText(" "+context.getResources().getString(R.string.expired_shortened));
            holder.mCourseEndTime.setTextColor(Color.parseColor(Constants.EXPIRED_COLOR));
            holder.mProgressStatus.setProgress(progresspercent);
            //holder.mProgressStatus.getProgressDrawable().setColorFilter(context.getResources().getColor(R.color.topbar), PorterDuff.Mode.SRC_IN);
            holder.mProgressStatus.setForegroundProgressColor(context.getResources().getColor(R.color.topbar));
            holder.mProgressStatus.setScaleY(1.1f);
            holder.coursedirect.setText(context.getResources().getString(R.string.Report));
            holder.new_tag.setVisibility(View.GONE);
        } else if (courseModel.getCourse_Status_Value() == Constants.PARTIALLY_COMPLETED) {
            double coursePercentage = 0.0;
            int progresspercent = 5;
            if(courseModel.getNo_Of_Sections_Completed()>0){
                coursePercentage =(Double.parseDouble(String.valueOf(courseModel.getNo_Of_Sections_Completed())) / Double.parseDouble(String.valueOf(courseModel.getTotal_Sections())));
                coursePercentage = Math.round(coursePercentage * 100);
                progresspercent = Integer.parseInt(String.valueOf(Math.round(coursePercentage)));
            }else{
                progresspercent = 60;
            }

            holder.mCompletionString.setVisibility(View.VISIBLE);
            holder.mCompletionString.setText(progresspercent+"%");
            holder.mCourseStartTime.setVisibility(View.GONE);
            holder.mCourseEndTime.setVisibility(View.VISIBLE);
            holder.mCourseEndTime.setText(" "+context.getResources().getString(R.string.partialy_shortened));
            holder.mCourseEndTime.setTextColor(Color.parseColor(Constants.PARTIALLY_COMPLETED_COLOR));
            holder.mProgressStatus.setProgress(progresspercent);
            //holder.mProgressStatus.getProgressDrawable().setColorFilter(context.getResources().getColor(R.color.tacobellbackground), PorterDuff.Mode.SRC_IN);
            holder.mProgressStatus.setScaleY(1.1f);
            holder.coursedirect.setText(context.getResources().getString(R.string.view));
            holder.new_tag.setVisibility(View.GONE);
        }

        holder.coursedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onItemClick(courseModel.getCourse_Id());
            }
        });

        if ( courseModel.getEvaluation_Mode()!=null && courseModel.getManual_Evaluation_Status() == 0 && courseModel.getEvaluation_Mode().equalsIgnoreCase("MANUAL") && !(courseModel.getCourse_Status_Value() == Constants.YET_TO_START))
        {
            holder.mCourseEndTime.setText(" "+context.getResources().getString(R.string.pending_for_evaluation));
            holder.mCourseEndTime.setTextColor(Color.parseColor(Constants.PENDING_APPROVAl_COLOR));
        }

        final List<CourseSectionProgressModel> sectionModelList = courseModel.getSectionDetails();
        if(sectionModelList != null){
            final LinearLayout l3 = new LinearLayout(context);
            l3.setOrientation(LinearLayout.HORIZONTAL);
            int sizenew = sectionModelList.size();
            l3.setWeightSum(sizenew);
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,12,1.0f);
            //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30,LinearLayout.LayoutParams.MATCH_PARENT,
            //80);
            params.setMargins(10,0,5,0);
            for (CourseSectionProgressModel secmodel : sectionModelList) {
                final View labl = new View(context);
                labl.setLayoutParams(params);
                labl.setPadding(10, 0, 10, 10);
                if (courseModel.getEvaluation_Mode()!=null && courseModel.getEvaluation_Mode().equalsIgnoreCase("MANUAL")
                        && !(secmodel.getSection_Status_Value().equals(String.valueOf(Constants.YET_TO_START)))
                        && courseModel.getManual_Evaluation_Status()==0)
                {
                    labl.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_orange));
                }
                else {
                    //labl.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_allcorners_green));
                    if (secmodel.getSection_Status_Value().equals(String.valueOf(Constants.INPROGRESS))) {
                        labl.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_allcorners_bluecolor));
                    } else if (secmodel.getSection_Status_Value().equals(String.valueOf(Constants.COMPLETED))) {
                        labl.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_allcorners_green));
                    } else if (secmodel.getSection_Status_Value().equals(String.valueOf(Constants.MAX_ATTEMPTS_REACHED))) {
                        labl.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_allcorners_red));
                    } else if (secmodel.getSection_Status_Value().equals(String.valueOf(Constants.COURSE_EXPIRED))) {
                        labl.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_allcorners_red));
                    } else if (secmodel.getSection_Status_Value().equals(String.valueOf(Constants.PARTIALLY_COMPLETED))) {
                        labl.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_allcorners_green));
                    } else {
                        labl.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_allcorners_grey));
                    }
                }
                l3.addView(labl);
            }
            holder.segmentprogresslayout.addView(l3);
        }
    }

    @Override
    public int getItemCount() {
        return courseModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnItemClickListener(MyClickListener myClickListener){
        this.myClickListener = myClickListener;
    }

    public void setthemeforView(final CoursesRecyclerHolder holder){

        holder.mTitle.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.mSubTitle.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.mSubTitle1.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.mSubTitle2.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.mSubTitle3.setTextColor(Color.parseColor(Constants.TEXT_COLOR));


        holder.cardViewLayout.setCardBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));

    }

    public class CoursesRecyclerHolder extends RecyclerView.ViewHolder{
        Context ctxt;
        View mView;
        CardView cardViewLayout;
        TextView mTitle;
        TextView mSubTitle;
        TextView mSubTitle1;
        TextView mSubTitle2,mSubTitle3;
        ImageView mThumbnail;
        CircleProgressbar  mProgressStatus;
        TextView mCompletionString;
        TextView mCourseStartTime;
        TextView mCourseEndTime,coursedirect;
        View blockcourse,new_tag;
        LinearLayout segmentprogresslayout;

        public CoursesRecyclerHolder(View itemView,Context context) {
            super(itemView);
            this.ctxt = context;
            cardViewLayout = (CardView)itemView.findViewById(R.id.cardviewLayout);
            mTitle = (TextView)itemView.findViewById(R.id.texttitle);
            mSubTitle = (TextView)itemView.findViewById(R.id.textSubtitle);
            mThumbnail = (ImageView)itemView.findViewById(R.id.thumbnail);
            mProgressStatus = (CircleProgressbar)itemView.findViewById(R.id.progress_status);
            mCompletionString = (TextView)itemView.findViewById(R.id.completion_string);
            mCourseStartTime = (TextView)itemView.findViewById(R.id.start_time);
            mCourseEndTime = (TextView)itemView.findViewById(R.id.end_time);
            blockcourse = itemView.findViewById(R.id.blockcourse);
            coursedirect = (TextView) itemView.findViewById(R.id.coursedirect);
            new_tag = itemView.findViewById(R.id.new_tag);

            segmentprogresslayout = (LinearLayout) itemView.findViewById(R.id.segmentprogresslayout);

            mSubTitle1 = (TextView)itemView.findViewById(R.id.textSubtitle1);
            mSubTitle2 = (TextView)itemView.findViewById(R.id.textSubtitle2);
            mSubTitle3 = (TextView)itemView.findViewById(R.id.textSubtitle3);
        }
    }

    public interface MyClickListener{

        //public void onItemClick(int courseId, boolean isSequenceEnabled);
        public void onItemClick(int courseId);

    }

    public CourseModel getItemAt(int position){
        return courseModelList.get(position);
    }
}

