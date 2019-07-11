package com.swaas.kangle.CheckList.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swaas.kangle.CheckList.SectionsQuestionDetailActivity;
import com.swaas.kangle.CheckList.model.ReportQuestionAnsersList;
import com.swaas.kangle.R;
import com.swaas.kangle.utils.Constants;

import java.util.List;

/**
 * Created by Sayellessh on 11-05-2018.
 */

public class SectionQuestionDetailsListAdapter extends RecyclerView.Adapter<SectionQuestionDetailsListAdapter.ViewHolder>{

    SectionsQuestionDetailActivity mActivity;
    List<ReportQuestionAnsersList> allquestionslist;

    public SectionQuestionDetailsListAdapter(Context context, List<ReportQuestionAnsersList> questionsList)  {
        mActivity = (SectionsQuestionDetailActivity) context;
        allquestionslist = questionsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.section_questions_list_item, parent, false);
        return new SectionQuestionDetailsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final ReportQuestionAnsersList secdetails = allquestionslist.get(position);
        setthemeforView(holder);
        String questionText = secdetails.getQuestionText();

        if(secdetails.getQuestion_Number_Title() != null &&
                !secdetails.getQuestion_Number_Title().isEmpty()){
            questionText = (secdetails.getQuestion_Number_Title()+". "+questionText);
        }else{
            questionText = ((position+1)+". "+questionText);
        }

        holder.questiontext.setText(questionText);

        //holder.questiontext.setText((position+1)+" . "+secdetails.getQuestionText());

        if(secdetails.getLstAnswers().size() > 0){
            String answer = null;
            for(int i=0;i< secdetails.getLstAnswers().size();i++){
                if(secdetails.getLstAnswers().get(i).getAnswerText() != null) {
                    answer = secdetails.getLstAnswers().get(i).getAnswerText() + ",";
                }
            }
            String substring = null;
            if(answer != null && !answer.isEmpty()) {
                substring = answer.substring(0, answer.length() - 1);
            }
            if(substring != null && !substring.isEmpty()) {
                holder.answers.setText(mActivity.getResources().getString(R.string.Answertext) + " " + substring);
                holder.answers.setVisibility(View.VISIBLE);
            }else{
                holder.answers.setVisibility(View.GONE);
            }
        }else{
            holder.answers.setVisibility(View.GONE);
            holder.answers.setText("");
        }
        if(secdetails.getQuestion_Remarks() != null && !secdetails.getQuestion_Remarks().isEmpty()){
            holder.comments.setVisibility(View.VISIBLE);
        }else{
            holder.comments.setVisibility(View.GONE);
        }

        if(secdetails.lstAttachments != null && secdetails.lstAttachments.size() > 0){
            holder.attachmentsView.setVisibility(View.VISIBLE);
        }else{
            holder.attachmentsView.setVisibility(View.GONE);
        }

        holder.attachmentsView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mActivity.loadattachedImageView(secdetails.lstAttachments.get(0).toString());
            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.showCommentsInPopup(secdetails.getQuestion_Remarks().toString());
            }
        });

    }

    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = allquestionslist.size();
        } catch (Exception e) {

        }
        return count;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView questiontext,answers,comments,attachmentsView;
        final View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView.findViewById(R.id.mView);
            questiontext = (TextView) itemView.findViewById(R.id.question_text);
            answers = (TextView) itemView.findViewById(R.id.answers);
            comments = (TextView) itemView.findViewById(R.id.comments);
            attachmentsView = (TextView) itemView.findViewById(R.id.attachmentsView);
        }
    }

    public void setthemeforView(final ViewHolder holder){

        holder.questiontext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.answers.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.comments.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        holder.comments.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.attachmentsView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        holder.attachmentsView.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
    }
}
