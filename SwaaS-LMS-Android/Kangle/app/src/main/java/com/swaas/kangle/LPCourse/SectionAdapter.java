package com.swaas.kangle.LPCourse;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.swaas.kangle.R;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.DateHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by saiprasath on 8/10/2017.
 */

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionRecyclerHolder> {
    private Context context;
    SectionActivity mActivity;
    List<SectionModel> sectionModelList = new ArrayList<SectionModel>();
    public static MyClickListener myClickListener;
    SectionRecyclerHolder sectionRecyclerHolder;
    private boolean isAlreadyComplted=false;
    private boolean isSequenceEnabled ;
    private boolean isreportEnabled = false;
    int count = 0;
    int count1= 0;
    //public SectionAdapter(Context ctxt, List<SectionModel> sectionModels, boolean isSequenceEnabled){
    public SectionAdapter(Context ctxt, List<SectionModel> sectionModels, boolean b){
        this.context = ctxt;
        this.sectionModelList = sectionModels;
        mActivity = (SectionActivity) ctxt;
        isreportEnabled = b;

        //this.isSequenceEnabled = isSequenceEnabled;
    }
    public void setOnItemClickListener(MyClickListener myClickListener){
        this.myClickListener = myClickListener;
    }
    @Override
    public SectionRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.sections_layout,parent,false);
        return new SectionRecyclerHolder(root,context);
    }

    @Override
    public void onBindViewHolder(final SectionRecyclerHolder holder, final int position) {
        final SectionModel sectionModel = sectionModelList.get(position);

        setthemeforView(holder);

        isSequenceEnabled = sectionModelList.get(0).is_Course_Section_Mandatory();
        if (isSequenceEnabled  && !isreportEnabled){

            if (position > 0){

                int pos = position - 1;
                if(sectionModelList.get(pos).getSection_Status_Value() == Constants.COMPLETED){

                    holder.mSectionLaoutCard.clearAnimation();
                    holder.mPlaybtn.setEnabled(true);
                    holder.showmandatory.setEnabled(true);
                    holder.reportbtn.setEnabled(true);
                    holder.taketest.setEnabled(true);
                    holder.mSectionLaoutCard.setEnabled(true);
                    //holder.mSectionLaoutCard.setBackgroundColor(context.getResources().getColor(R.color.white));
                    holder.mSectionLaoutCard.setOnClickListener(null);
                    holder.prerequsite.setVisibility(View.GONE);

                }else {
                    if(sectionModelList.get(position).getSection_Status_Value() == Constants.YET_TO_START) {
                        AlphaAnimation alpha = new AlphaAnimation(0.5F, 0.5F);
                        alpha.setDuration(0); // Make animation instant
                        alpha.setFillAfter(true);
                        // Tell it to persist after the animation ends
                        // And then on your layout
                        holder.mSectionLaoutCard.startAnimation(alpha);
                        holder.mPlaybtn.setEnabled(false);
                        holder.taketest.setEnabled(false);
                        holder.showmandatory.setEnabled(false);
                        holder.reportbtn.setEnabled(false);
                        holder.mSectionLaoutCard.setEnabled(false);
                        holder.mSectionLaoutCard.setBackgroundColor(context.getResources().getColor(R.color.black_tran));
                        holder.prerequsite.setVisibility(View.VISIBLE);

                        holder.prerequsite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(context, context.getResources().getString(R.string.sectionmandatealert), Toast.LENGTH_SHORT).show();
                            }
                        });

                        holder.mSectionLaoutCard.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Toast.makeText(context, context.getResources().getString(R.string.sectionmandatealert), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }

            }
        }

        /*if(position == 0){*/
        sectionModel.IsMandatoryOpen = true;
        holder.detailssectionlayout.setVisibility(View.VISIBLE);
        holder.showmandatory.setImageResource(R.drawable.ic_keyboard_arrow_up_black_36dp);
        //} else {
            /*sectionModel.IsMandatoryOpen = false;
            holder.detailssectionlayout.setVisibility(View.GONE);
            holder.showmandatory.setImageResource(R.drawable.ic_keyboard_arrow_down_black_36dp);*/
        //}

        holder.mSectionName.setText(sectionModel.getSection_Name()+"");
        holder.mNoOfContents.setText(String.valueOf(sectionModel.getNo_Of_Assets_Mapped())+" "+context.getResources().getString(R.string.Contents_to_read));

        String validdateString = DateHelper.getDisplayFormat(sectionModel.getValid_To(),"yyyy-MM-dd hh:mm:ss");
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Date curdate = new Date();
        Date Valid_To = new Date();
        try {
            Valid_To = (Date)formatter.parse(validdateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar validtoInstance = Calendar.getInstance();
        validtoInstance.setTime(Valid_To);
        validtoInstance.add(Calendar.DATE,1);
        boolean isexpired = curdate.after(validtoInstance.getTime());
        if (sectionModel.getSection_Status_Value() != Constants.COMPLETED) {
            if (sectionModel.getSection_Max_Attempts() > sectionModel.getSection_Attempts_Count() && !isexpired) {
                if (sectionModel.Is_Read_Assets) {
                    if (sectionModel.getSection_Status_Value() == Constants.YET_TO_START) {
                        holder.status_icon.setVisibility(View.GONE);
                        holder.status_icon.setImageResource(R.drawable.ic_content_paste_black_36dp);
                        holder.status_text.setText(context.getResources().getString(R.string.yet_to_start));
                        holder.status_text.setTextColor(Color.parseColor(Constants.YET_TO_START_COLOR));
                        //holder.taketest.setVisibility(View.VISIBLE);
                        if(sectionModel.getNo_Of_Visible_Questions() > 0){
                            holder.takeTextbutton.setVisibility(View.VISIBLE);
                            holder.takeTextbutton.setEnabled(true);
                            holder.disabletakeTextbutton.setVisibility(View.GONE);
                        }else{
                            holder.takeTextbutton.setVisibility(View.GONE);
                            holder.disabletakeTextbutton.setVisibility(View.GONE);
                        }
                        holder.takeTextbutton.setText(context.getResources().getString(R.string.take_assignment));
                        /*holder.course_status_icon.setVisibility(View.GONE);*/
                        holder.attemptspending.setVisibility(View.VISIBLE);
                        holder.attemptspending.setText(context.getResources().getString(R.string.Attempts_taken) +" "+ sectionModel.getSection_Attempts_Count() + " of " +
                                sectionModel.getSection_Max_Attempts());
                        holder.mPlaybtn.setVisibility(View.VISIBLE);
                    } else if (sectionModel.getSection_Status_Value() == Constants.INPROGRESS) {
                        //holder.taketest.setVisibility(View.VISIBLE);
                        holder.status_icon.setVisibility(View.GONE);
                        holder.status_icon.setImageResource(R.drawable.ic_more_new);
                        holder.status_text.setText(context.getResources().getString(R.string.in_progress));
                        holder.status_text.setTextColor(Color.parseColor(Constants.INPROGRESS_COLOR));
                        if(sectionModel.getNo_Of_Visible_Questions() > 0){
                            holder.takeTextbutton.setVisibility(View.VISIBLE);
                            holder.takeTextbutton.setEnabled(true);
                            holder.disabletakeTextbutton.setVisibility(View.GONE);
                        }else{
                            holder.takeTextbutton.setVisibility(View.GONE);
                            holder.disabletakeTextbutton.setVisibility(View.GONE);
                        }
                        holder.takeTextbutton.setText(context.getResources().getString(R.string.retake_assignment));
                        /*holder.course_status_icon.setVisibility(View.GONE);*/
                        holder.attemptspending.setVisibility(View.VISIBLE);
                        holder.attemptspending.setText(context.getResources().getString(R.string.Attempts_taken) +" "+ sectionModel.getSection_Attempts_Count() + " of " +
                                sectionModel.getSection_Max_Attempts());
                        holder.mPlaybtn.setVisibility(View.VISIBLE);
                    } else if (sectionModel.getSection_Status_Value() == Constants.COMPLETED) {
                        holder.status_icon.setVisibility(View.GONE);
                        /*holder.course_status_icon.setVisibility(View.VISIBLE);
                        holder.course_status_icon.setImageResource(R.drawable.ic_check_new);*/
                        //holder.taketest.setVisibility(View.GONE);
                        holder.takeTextbutton.setVisibility(View.GONE);
                        holder.disabletakeTextbutton.setVisibility(View.GONE);
                        holder.mPlaybtn.setVisibility(View.VISIBLE);
                        if(sectionModel.getSection_Ref_Id() != 0){
                            if(sectionModel.getEvaluation_Type() == 1){
                                if(sectionModel.getEvaluation_Type() == 1 && sectionModel.getEvaluation_Status() == 1) {
                                    holder.course_status_icon.setImageResource(R.drawable.ic_check_new);
                                    holder.status_icon.setImageResource(R.drawable.ic_check_new);
                                    holder.status_text.setText(context.getResources().getString(R.string.completed_course));
                                    holder.status_text.setTextColor(Color.parseColor(Constants.COMPLETED_COLOR));
                                }else{
                                    holder.course_status_icon.setImageResource(R.drawable.ic_check_new_pfe);
                                    holder.status_icon.setImageResource(R.drawable.ic_check_new_pfe);
                                    holder.status_text.setText(context.getResources().getString(R.string.pending_for_evaluation));
                                    holder.status_text.setTextColor(Color.parseColor(Constants.PENDING_APPROVAl_COLOR));
                                }
                                if(sectionModel.getEvaluation_Status() != 5 && sectionModel.getEvaluation_Status() != 2){
                                    holder.secchecklistReport.setVisibility(View.VISIBLE);
                                }else{
                                    holder.secchecklistReport.setVisibility(View.GONE);
                                }
                            }else{
                                holder.secchecklistReport.setVisibility(View.GONE);
                                holder.course_status_icon.setImageResource(R.drawable.ic_check_new);
                                holder.status_icon.setImageResource(R.drawable.ic_check_new);
                                holder.status_text.setText(context.getResources().getString(R.string.completed_course));
                                holder.status_text.setTextColor(Color.parseColor(Constants.COMPLETED_COLOR));
                            }
                        }else{
                            holder.course_status_icon.setImageResource(R.drawable.ic_check_new);
                            holder.status_icon.setImageResource(R.drawable.ic_check_new);
                            holder.status_text.setText(context.getResources().getString(R.string.completed_course));
                            holder.status_text.setTextColor(Color.parseColor(Constants.COMPLETED_COLOR));
                        }
                        holder.attemptspending.setVisibility(View.VISIBLE);
                        holder.attemptspending.setText(context.getResources().getString(R.string.Attempts_taken) +" "+ sectionModel.getSection_Attempts_Count() + " of " +
                                sectionModel.getSection_Max_Attempts());
                    } else if (sectionModel.getSection_Status_Value() == Constants.MAX_ATTEMPTS_REACHED) {
                        holder.status_icon.setVisibility(View.GONE);
                        holder.status_icon.setImageResource(R.drawable.ic_cancel_new);
                        holder.status_text.setText(context.getResources().getString(R.string.max_attempts_reached_shortened));
                        holder.status_text.setTextColor(Color.parseColor(Constants.EXPIRED_COLOR));
                        /*holder.course_status_icon.setVisibility(View.VISIBLE);
                        holder.course_status_icon.setImageResource(R.drawable.ic_cancel_new);*/
                        //holder.taketest.setVisibility(View.GONE);
                        if(sectionModel.getNo_Of_Visible_Questions() > 0){
                            holder.takeTextbutton.setVisibility(View.GONE);
                            holder.disabletakeTextbutton.setVisibility(View.VISIBLE);
                        }else{
                            holder.takeTextbutton.setVisibility(View.GONE);
                            holder.disabletakeTextbutton.setVisibility(View.GONE);
                        }
                        holder.disabletakeTextbutton.setText(context.getResources().getString(R.string.retake_assignment));
                        holder.mPlaybtn.setVisibility(View.GONE);
                        holder.attemptspending.setVisibility(View.VISIBLE);
                        holder.attemptspending.setText(context.getResources().getString(R.string.Attempts_taken) +" "+ sectionModel.getSection_Attempts_Count() + " of " +
                                sectionModel.getSection_Max_Attempts());

                    } else if (sectionModel.getSection_Status_Value() == Constants.COURSE_EXPIRED) {
                        holder.status_icon.setVisibility(View.GONE);
                        holder.status_icon.setImageResource(R.drawable.ic_cancel_new);
                        holder.status_text.setText(context.getResources().getString(R.string.expired_shortened));
                        holder.status_text.setTextColor(Color.parseColor(Constants.EXPIRED_COLOR));
                        /*holder.course_status_icon.setVisibility(View.VISIBLE);
                        holder.course_status_icon.setImageResource(R.drawable.ic_cancel_new);*/
                        //holder.taketest.setVisibility(View.GONE);
                        if(sectionModel.getNo_Of_Visible_Questions() > 0){
                            holder.takeTextbutton.setVisibility(View.GONE);
                            holder.disabletakeTextbutton.setVisibility(View.VISIBLE);
                        }else{
                            holder.takeTextbutton.setVisibility(View.GONE);
                            holder.disabletakeTextbutton.setVisibility(View.GONE);
                        }
                        holder.disabletakeTextbutton.setText(context.getResources().getString(R.string.retake_assignment));
                        holder.mPlaybtn.setVisibility(View.GONE);
                        holder.attemptspending.setVisibility(View.VISIBLE);
                        holder.attemptspending.setText(context.getResources().getString(R.string.Attempts_taken) +" "+ sectionModel.getSection_Attempts_Count() + " of " +
                                sectionModel.getSection_Max_Attempts());
                    }
                } else {
                    if (sectionModel.getSection_Status_Value() == Constants.YET_TO_START) {
                        holder.status_icon.setVisibility(View.GONE);
                        //holder.taketest.setVisibility(View.GONE);
                        holder.status_text.setText(context.getResources().getString(R.string.yet_to_start));
                        holder.status_text.setTextColor(Color.parseColor(Constants.YET_TO_START_COLOR));
                        holder.attemptspending.setVisibility(View.VISIBLE);
                        holder.attemptspending.setText(context.getResources().getString(R.string.Attempts) +" "+ sectionModel.getSection_Max_Attempts());
                        holder.takeTextbutton.setVisibility(View.GONE);
                        holder.disabletakeTextbutton.setVisibility(View.GONE);
                        /*holder.course_status_icon.setVisibility(View.GONE);*/
                        //holder.status_icon.setImageResource(R.drawable.ic_more_new);
                        holder.mPlaybtn.setVisibility(View.VISIBLE);
                    }else if(sectionModel.getSection_Status_Value() == Constants.INPROGRESS){
                        holder.status_icon.setVisibility(View.GONE);
                        holder.status_icon.setImageResource(R.drawable.ic_more_new);
                        holder.status_text.setText(context.getResources().getString(R.string.in_progress));
                        holder.status_text.setTextColor(Color.parseColor(Constants.INPROGRESS_COLOR));
                        if(sectionModel.getNo_Of_Visible_Questions() > 0){
                            holder.takeTextbutton.setVisibility(View.VISIBLE);
                            holder.disabletakeTextbutton.setVisibility(View.GONE);
                            holder.takeTextbutton.setEnabled(true);
                        }else{
                            holder.takeTextbutton.setVisibility(View.GONE);
                            holder.disabletakeTextbutton.setVisibility(View.GONE);
                        }
                        holder.takeTextbutton.setText(context.getResources().getString(R.string.retake_assignment));
                        /*holder.course_status_icon.setVisibility(View.GONE);*/
                        holder.attemptspending.setVisibility(View.VISIBLE);
                        holder.attemptspending.setText(context.getResources().getString(R.string.Attempts_taken) +" "+sectionModel.getSection_Attempts_Count() + " of " +
                                sectionModel.getSection_Max_Attempts());
                        holder.mPlaybtn.setVisibility(View.VISIBLE);
                    }
                    holder.attemptspending.setVisibility(View.VISIBLE);
                    holder.attemptspending.setText(context.getResources().getString(R.string.Attempts_taken) +" "+ sectionModel.getSection_Attempts_Count() + " of " +
                            sectionModel.getSection_Max_Attempts());
                }
            } else {
                if(isexpired){
                    holder.status_icon.setVisibility(View.GONE);
                    if(sectionModel.getSection_Status_Value() == Constants.YET_TO_START){
                        holder.status_icon.setImageResource(R.drawable.ic_content_paste_black_36dp);
                        holder.status_text.setText(context.getResources().getString(R.string.yet_to_start));
                        holder.status_text.setTextColor(Color.parseColor(Constants.YET_TO_START_COLOR));
                    } else if(sectionModel.getSection_Status_Value() == Constants.INPROGRESS){
                        holder.status_icon.setImageResource(R.drawable.ic_more_new);
                        holder.status_text.setText(context.getResources().getString(R.string.in_progress));
                        holder.status_text.setTextColor(Color.parseColor(Constants.INPROGRESS_COLOR));
                    }else{
                        holder.status_icon.setImageResource(R.drawable.ic_cancel_new);
                        holder.status_text.setText(context.getResources().getString(R.string.expired_shortened));
                        holder.status_text.setTextColor(Color.parseColor(Constants.EXPIRED_COLOR));
                    }
                    /*holder.course_status_icon.setVisibility(View.VISIBLE);
                    holder.course_status_icon.setImageResource(R.drawable.ic_cancel_new);*/
                    //holder.taketest.setVisibility(View.GONE);
                    if(sectionModel.getNo_Of_Visible_Questions() > 0){
                        holder.takeTextbutton.setVisibility(View.GONE);
                        holder.disabletakeTextbutton.setVisibility(View.VISIBLE);
                    }else{
                        holder.takeTextbutton.setVisibility(View.GONE);
                        holder.disabletakeTextbutton.setVisibility(View.GONE);
                    }
                    holder.disabletakeTextbutton.setText(context.getResources().getString(R.string.retake_assignment));
                    holder.mPlaybtn.setVisibility(View.GONE);
                    holder.attemptspending.setVisibility(View.VISIBLE);
                    holder.attemptspending.setText(context.getResources().getString(R.string.Attempts_taken) +" "+ sectionModel.getSection_Attempts_Count() + " of " +
                            sectionModel.getSection_Max_Attempts());
                }else{
                    holder.status_icon.setVisibility(View.GONE);
                    holder.status_icon.setImageResource(R.drawable.ic_cancel_new);
                    holder.status_text.setText(context.getResources().getString(R.string.max_attempts_reached_shortened));
                    holder.status_text.setTextColor(Color.parseColor(Constants.EXPIRED_COLOR));
                    /*holder.course_status_icon.setVisibility(View.VISIBLE);
                    holder.course_status_icon.setImageResource(R.drawable.ic_cancel_new);*/
                    //holder.taketest.setVisibility(View.GONE);
                    if(sectionModel.getNo_Of_Visible_Questions() > 0){
                        holder.takeTextbutton.setVisibility(View.GONE);
                        holder.disabletakeTextbutton.setVisibility(View.VISIBLE);
                    }else{
                        holder.takeTextbutton.setVisibility(View.GONE);
                        holder.disabletakeTextbutton.setVisibility(View.GONE);
                    }
                    holder.disabletakeTextbutton.setText(context.getResources().getString(R.string.retake_assignment));
                    holder.mPlaybtn.setVisibility(View.GONE);
                }
            }
        }else{
            holder.status_icon.setVisibility(View.GONE);
            holder.status_icon.setImageResource(R.drawable.ic_check_new);
            holder.status_text.setText(context.getResources().getString(R.string.completed_course));
            holder.status_text.setTextColor(Color.parseColor(Constants.COMPLETED_COLOR));
            if(sectionModel.getSection_Ref_Id() != 0){
                if(sectionModel.getEvaluation_Type() == 1){
                    if(sectionModel.getEvaluation_Type() == 1 && sectionModel.getEvaluation_Status() == 1) {
                        holder.course_status_icon.setImageResource(R.drawable.ic_check_new);
                        holder.status_icon.setImageResource(R.drawable.ic_check_new);
                        holder.status_text.setText(context.getResources().getString(R.string.completed_course));
                        holder.status_text.setTextColor(Color.parseColor(Constants.COMPLETED_COLOR));
                    }else{
                        holder.course_status_icon.setImageResource(R.drawable.ic_check_new_pfe);
                        holder.status_icon.setImageResource(R.drawable.ic_check_new_pfe);
                        holder.status_text.setText(context.getResources().getString(R.string.pending_for_evaluation));
                        holder.status_text.setTextColor(Color.parseColor(Constants.PENDING_APPROVAl_COLOR));
                    }
                    if(sectionModel.getEvaluation_Status() != 5 && sectionModel.getEvaluation_Status() != 2){
                        holder.secchecklistReport.setVisibility(View.VISIBLE);
                    }else{
                        holder.secchecklistReport.setVisibility(View.GONE);
                    }
                }else{
                    holder.secchecklistReport.setVisibility(View.GONE);
                    holder.status_icon.setImageResource(R.drawable.ic_check_new);
                }
            }else{
                holder.status_icon.setImageResource(R.drawable.ic_check_new);
            }
            /*holder.course_status_icon.setVisibility(View.VISIBLE);
            holder.course_status_icon.setImageResource(R.drawable.ic_check_new);*/
            //holder.taketest.setVisibility(View.GONE);
            holder.takeTextbutton.setVisibility(View.GONE);
            holder.disabletakeTextbutton.setVisibility(View.GONE);
            holder.mPlaybtn.setVisibility(View.VISIBLE);
            holder.attemptspending.setVisibility(View.VISIBLE);
            holder.attemptspending.setText(context.getResources().getString(R.string.Attempts_taken) +" "+ sectionModel.getSection_Attempts_Count() + " of " +
                    sectionModel.getSection_Max_Attempts());
        }

        if(sectionModel.getSection_Status_Value() == Constants.INPROGRESS){
            holder.status_icon.setVisibility(View.GONE);
            holder.status_icon.setImageResource(R.drawable.ic_more_new);
        }else if(sectionModel.getSection_Status_Value() == Constants.COMPLETED){
            holder.status_icon.setVisibility(View.GONE);
            holder.status_icon.setImageResource(R.drawable.ic_check_new);
        }else if(sectionModel.getSection_Status_Value() == Constants.COURSE_EXPIRED){
            holder.status_icon.setVisibility(View.GONE);
            holder.status_icon.setImageResource(R.drawable.ic_cancel_new);
        }else if(sectionModel.getSection_Status_Value() == Constants.MAX_ATTEMPTS_REACHED){
            holder.status_icon.setVisibility(View.GONE);
            holder.status_icon.setImageResource(R.drawable.ic_cancel_new);
        }else if(sectionModel.getSection_Status_Value() == Constants.PARTIALLY_COMPLETED){
            holder.status_icon.setVisibility(View.GONE);
            holder.status_icon.setImageResource(R.drawable.ic_check_new);
        }else {
            /*holder.status_icon.setVisibility(View.VISIBLE);
            holder.status_icon.setImageResource(R.drawable.ic_content_paste_black_36dp);*/
        }

        if (sectionModel.getSection_Status_Value() != Constants.YET_TO_START){
            if(sectionModel.getSection_Attempts_Count() > 0) {
                //holder.reportbtn.setVisibility(View.VISIBLE);
                holder.reportsbutton.setVisibility(View.VISIBLE);
            }
        }else {
            //holder.reportbtn.setVisibility(View.GONE);
            holder.reportsbutton.setVisibility(View.GONE);
        }

        if(sectionModel.getNo_Of_Assets_Mapped() <= 0){
            holder.mPlaybtn.setVisibility(View.GONE);
        }

        if(sectionModel.getLstMandoryAssets() != null){
            if(sectionModel.getLstMandoryAssets().size()>0){

                holder.msection_detail_holder.setVisibility(View.VISIBLE);

                for (SectionAssetModel sectionassetmodel:  sectionModel.getLstMandoryAssets()){
                    LinearLayout detailcontentlayout = new LinearLayout(context);
                    detailcontentlayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams detaillayoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,2f);
                    detailcontentlayout.setLayoutParams(detaillayoutparams);
                    ImageView imageone = new ImageView(context);
                    LinearLayout.LayoutParams imageviewparams;
                    if(context.getResources().getBoolean(R.bool.portrait_only)){

                        imageviewparams = new LinearLayout.LayoutParams(0,dpToPx(35) ,0.3f);

                    }else {

                        imageviewparams = new LinearLayout.LayoutParams(0,dpToPx(60) ,0.1f);
                    }
                    imageone.setLayoutParams(imageviewparams);
                    imageone.setPadding(10,10,0,10);
                    if (sectionassetmodel.getDA_Type().equalsIgnoreCase("VIDEO")){
                        //imageone.setImageResource(R.drawable.icon_mp4);
                        imageone.setImageResource(R.drawable.ic_status_green);
                    }else if (sectionassetmodel.getDA_Type().equalsIgnoreCase("ARTICULATE")){
                        //imageone.setImageResource(R.drawable.icon_html);
                        imageone.setImageResource(R.drawable.ic_status_green);
                    } else {
                        //imageone.setImageResource(R.drawable.icon_doc);
                        imageone.setImageResource(R.drawable.ic_status_green);
                    }

                    imageone.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));

                    LinearLayout contentlayout = new LinearLayout(context);
                    contentlayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams contentlayoutparams;
                    if(context.getResources().getBoolean(R.bool.portrait_only)){

                        contentlayoutparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,2f);

                    }else {

                        contentlayoutparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,0.9f);
                    }

                    contentlayout.setLayoutParams(contentlayoutparams);
                    LinearLayout.LayoutParams textviewparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView text2 = new TextView(context);
                    text2.setLayoutParams(textviewparams);
                    String nameofmandate = sectionassetmodel.getDA_Name()+" ("+sectionassetmodel.getDA_Type().toLowerCase()+")";
                    //text2.setText();
                    text2.setPadding(0,15,10,0);

                    if(context.getResources().getBoolean(R.bool.portrait_only)){
                        text2.setTextSize(12);
                    }else {
                        text2.setTextSize(16);
                    }
                    text2.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
                    TextView text3 = new TextView(context);
                    text3.setLayoutParams(textviewparams);
                    text3.setPadding(0,5,5,0);
                    text3.setTypeface(text3.getTypeface(), Typeface.ITALIC);
                    if(context.getResources().getBoolean(R.bool.portrait_only)){
                        text3.setTextSize(9);
                    }else {
                        text3.setTextSize(12);
                    }
                    if (sectionassetmodel.getDA_Type().equalsIgnoreCase("VIDEO")){
                        if(Integer.parseInt(sectionassetmodel.getMandatory()) > 60) {
                            String timevalue = String.valueOf(Integer.parseInt(sectionassetmodel.getMandatory())/60);
                            text2.setText(nameofmandate);
                            text3.setText(context.getResources().getString(R.string.watch_for) + " " + timevalue +" "+
                                    context.getResources().getString(R.string.min));
                        }else{
                            text2.setText(nameofmandate);
                            text3.setText( context.getResources().getString(R.string.watch_for) + " " + sectionassetmodel.getMandatory()+" "+
                                    context.getResources().getString(R.string.sec));
                        }
                    }else if(sectionassetmodel.getDA_Type().equalsIgnoreCase("ARTICULATE")){
                        if(Integer.parseInt(sectionassetmodel.getMandatory()) > 60) {
                            String timevalue = String.valueOf(Integer.parseInt(sectionassetmodel.getMandatory())/60);
                            text2.setText(nameofmandate);
                            text3.setText(context.getResources().getString(R.string.view) + " " + timevalue+" "+
                                    context.getResources().getString(R.string.min));
                        }else{
                            text2.setText(nameofmandate);
                            text3.setText(context.getResources().getString(R.string.view) + " " + sectionassetmodel.getMandatory()+" "+
                                    context.getResources().getString(R.string.sec));
                        }
                    }else{
                        text2.setText(nameofmandate);
                        text3.setText(context.getResources().getString(R.string.view)+ " " +sectionassetmodel.getMandatory());
                    }
                    text3.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
                    text2.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
                    contentlayout.addView(text2);
                    contentlayout.addView(text3);
                    detailcontentlayout.addView(imageone);
                    detailcontentlayout.addView(contentlayout);
                    holder.msection_detail_holder.addView(detailcontentlayout);

                }
            }else {
                holder.msection_detail_holder.setVisibility(View.GONE);
            }
        }else{
            holder.msection_detail_holder.setVisibility(View.GONE);
        }

        if (sectionModel.IsMandatoryOpen){

            holder.detailssectionlayout.setVisibility(View.VISIBLE);

        }else {

            holder.detailssectionlayout.setVisibility(View.GONE);

        }

        holder.mSectionDate.setText(DateHelper.getDisplayFormat(sectionModel.getValid_To(),"yyyy-MM-dd hh:mm:ss"));

        holder.taketest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //myClickListener.onTakeTestClick(sectionModel.getSection_Id(),sectionModel.getCourse_User_Assignment_Id(),sectionModel.getCouse_User_Section_Mapping_Id());
            }
        });

        holder.takeTextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.takeTextbutton.setEnabled(false);
                myClickListener.onTakeTestClick(sectionModel.getSection_Name(),sectionModel.getSection_Id(),sectionModel.getCourse_User_Assignment_Id(),sectionModel.getCouse_User_Section_Mapping_Id());
            }

        });

        holder.reportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //myClickListener.onReportClick(sectionModel.getSection_Id(),sectionModel.getCourse_Id(),sectionModel.getPublish_Id());
            }
        });

        holder.reportsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onReportClick(sectionModel.getSection_Id(),sectionModel.getCourse_Id(),sectionModel.getPublish_Id());
            }
        });

        holder.mPlaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onItemClick(sectionModel.getSection_Id(),sectionModel.getCourse_User_Assignment_Id(),sectionModel.getCouse_User_Section_Mapping_Id());
            }
        });
        holder.showmandatory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sectionModel.IsMandatoryOpen) {
                    sectionModel.IsMandatoryOpen = true;
                    holder.detailssectionlayout.setVisibility(View.VISIBLE);
                    holder.showmandatory.setImageResource(R.drawable.ic_keyboard_arrow_up_black_36dp);
                } else {
                    sectionModel.IsMandatoryOpen = false;
                    holder.detailssectionlayout.setVisibility(View.GONE);
                    holder.showmandatory.setImageResource(R.drawable.ic_keyboard_arrow_down_black_36dp);
                }
            }
        });

        holder.msection_detail_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        holder.extendcourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onextendcourseClick(sectionModel);
            }
        });

        if(sectionModel.getCourse_Status_Value() != Constants.COURSE_EXPIRED) {
            if(sectionModel.getSection_Status_Value() == Constants.MAX_ATTEMPTS_REACHED){
                if (sectionModel.isCourseExtend()) {
                    if (sectionModel.getAutoExtendAttemptsCount() < sectionModel.getExtendLimits()) {
                        holder.extendcourse.setVisibility(View.VISIBLE);
                    }else{
                        holder.extendcourse.setVisibility(View.GONE);
                    }
                }else{
                    holder.extendcourse.setVisibility(View.GONE);
                }
            }else{
                holder.extendcourse.setVisibility(View.GONE);
            }
        }else{
            holder.extendcourse.setVisibility(View.GONE);
        }

        holder.secchecklistReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.gotoSectionchecklistreportlist(sectionModel.getSection_Id());
            }
        });
        if (isreportEnabled == true)
        {
            holder.reportsbutton.setVisibility(View.GONE);
            holder.status_text.setVisibility(View.GONE);
            holder.mSectionLaoutCard.setOnClickListener(null);
            //holder.prerequsite.setVisibility(View.GONE);
            holder.attemptspending.setVisibility(View.GONE);
            holder.takeTextbutton.setText(R.string.take_assignment);
            int pos = position -1;
            if(!(sectionModelList.get(position).getSection_Status_Value() == Constants.YET_TO_START)){

                holder.mSectionLaoutCard.clearAnimation();
                holder.mPlaybtn.setEnabled(true);
                holder.showmandatory.setEnabled(true);
                holder.reportbtn.setEnabled(true);
                holder.taketest.setEnabled(true);
                holder.mSectionLaoutCard.setEnabled(true);
                holder.status_text.setVisibility(View.GONE);
                holder.mSectionLaoutCard.setVisibility(View.GONE);
                holder.prerequsite.setVisibility(View.GONE);
                holder.attemptspending.setVisibility(View.GONE);

            }
            else if (  pos>-1 && (!(sectionModelList.get(pos).getSection_Status_Value() == Constants.YET_TO_START))
                    && (sectionModelList.get(position).getSection_Status_Value() == Constants.YET_TO_START) )
            {
                holder.mSectionLaoutCard.clearAnimation();
                holder.mPlaybtn.setEnabled(true);
                holder.showmandatory.setEnabled(true);
                holder.reportbtn.setEnabled(true);
                holder.taketest.setEnabled(true);
                holder.mSectionLaoutCard.setEnabled(true);
                holder.status_text.setVisibility(View.GONE);
                holder.mSectionLaoutCard.setVisibility(View.VISIBLE);
                // holder.prerequsite.setVisibility(View.GONE);
                holder.attemptspending.setVisibility(View.GONE);
            }
            if ((sectionModelList.get(position).getSection_Status_Value() == Constants.YET_TO_START) && pos>-1 && (sectionModelList.get(position-1).getSection_Status_Value() == Constants.YET_TO_START))
            {
                holder.taketest.setVisibility(View.GONE);
            }

            for (int i = 0; i < sectionModelList.size();i++)
            {
                if (sectionModelList.get(i).getSection_Status_Value()== Constants.YET_TO_START)
                {
                    count = count +1;
                }
                if (!(sectionModelList.get(i).getSection_Status_Value() == Constants.YET_TO_START))
                {
                    count1 = count+1;
                }

            }
            if (count == 0 )
            {
                //  mActivity.StartTimer(0);
                //mActivity.reportbtn.setVisibility(View.VISIBLE);
                //mActivity.reportbtn.setText(R.string.pending_for_evaluation);
                //mActivity.reportbtn.setEnabled(false);
                //mActivity.reportbtn.setAlpha((float) 0.5);
                //mActivity.reportbtn.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
                //mActivity.reportbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
                mActivity.question_timer_holder.setVisibility(View.GONE);
                mActivity.isbackallowed = true;
            }

        }
        holder.detailssectionlayout.setVisibility(View.VISIBLE);
        holder.showmandatory.setVisibility(View.INVISIBLE);

    }


    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void setthemeforView(final SectionRecyclerHolder holder){

        holder.mSectionName.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.mNoOfContents.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.attemptspending.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        //holder.cardViewLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
        holder.cardViewSection.setCardBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        holder.showmandatory.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        holder.takeTextbutton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        holder.reportsbutton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        holder.disabletakeTextbutton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));

        holder.takeTextbutton.setTextColor(ColorStateList.valueOf(Color.parseColor(Constants.TEXT_COLOR)));
        holder.reportsbutton.setTextColor(ColorStateList.valueOf(Color.parseColor(Constants.TEXT_COLOR)));
        holder.disabletakeTextbutton.setTextColor(ColorStateList.valueOf(Color.parseColor(Constants.TEXT_COLOR)));

        holder.extendcourse.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.extendcourselayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));

        holder.secchecklistReport.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.secchecklistReportlayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));

    }

    public class SectionRecyclerHolder extends RecyclerView.ViewHolder{
        Context context;
        CardView cardViewSection;
        //        TextView mSno;
        TextView mSectionName,attemptspending;
        TextView mNoOfContents;
        TextView mSectionDate;
        ImageView mPlaybtn;
        ImageView reportbtn;
        RelativeLayout mSectionLaoutCard,prerequsite;
        ImageView taketest,status_icon,course_status_icon;
        LinearLayout msection_detail_holder,detailssectionlayout;
        RelativeLayout mSideLinearLayout,mShowMandatoryHolder;
        ImageView showmandatory;
        TextView status_text;

        TextView reportsbutton,takeTextbutton,disabletakeTextbutton;

        TextView extendcourse,secchecklistReport;
        View extendcourselayout,secchecklistReportlayout;


        public SectionRecyclerHolder(View itemView,Context ctxt) {
            super(itemView);
            this.context = ctxt;
            cardViewSection = (CardView)itemView.findViewById(R.id.cardViewLayout);
            mSectionName = (TextView)itemView.findViewById(R.id.section_name);
            mNoOfContents = (TextView)itemView.findViewById(R.id.no_of_contents);
            mPlaybtn = (ImageView)itemView.findViewById(R.id.playtbtn);
            taketest = (ImageView) itemView.findViewById(R.id.take_test);
            reportbtn = (ImageView) itemView.findViewById(R.id.report_btn);
            mSideLinearLayout = (RelativeLayout)itemView.findViewById(R.id.side_linear_layout);
            mSectionDate = (TextView) itemView.findViewById(R.id.section_date);
            showmandatory = (ImageView) itemView.findViewById(R.id.showmandatory);
            msection_detail_holder = (LinearLayout) itemView.findViewById(R.id.section_detail_holder);
            detailssectionlayout = (LinearLayout) itemView.findViewById(R.id.detailssectionlayout);
            attemptspending = (TextView) itemView.findViewById(R.id.attemptspending);
            mSectionLaoutCard = (RelativeLayout) itemView.findViewById(R.id.section_layout_card);
            status_icon = (ImageView) itemView.findViewById(R.id.status_icon);
            course_status_icon = (ImageView) itemView.findViewById(R.id.course_status_icon);

            reportsbutton = (TextView) itemView.findViewById(R.id.reportsbutton);
            takeTextbutton = (TextView) itemView.findViewById(R.id.takeTextbutton);
            disabletakeTextbutton = (TextView) itemView.findViewById(R.id.disabletakeTextbutton);
            prerequsite = (RelativeLayout) itemView.findViewById(R.id.prerequsite);

            extendcourse = (TextView) itemView.findViewById(R.id.extendcourse);
            extendcourselayout = itemView.findViewById(R.id.extendcourselayout);

            secchecklistReportlayout = itemView.findViewById(R.id.secchecklistReportlayout);
            secchecklistReport = (TextView) itemView.findViewById(R.id.secchecklistReport);

            status_text = (TextView) itemView.findViewById(R.id.status_text);
        }
    }

    @Override
    public int getItemCount() {
        return sectionModelList.size();
    }

    public interface MyClickListener{
        public void onItemClick(int sectionId,int course_user_assignment_id, int sectionMapId);
        public void onTakeTestClick(String secName,int section_id, int course_user_assignment_id, int sectionMapId);
        public void onReportClick(int sectionId,int courseid,int publishid);
        public void onextendcourseClick(SectionModel sectionModel);

    }
}
