package com.swaas.kangle.LPCourse.questionbuilder.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.swaas.kangle.LPCourse.model.QuestionAndAnswerModel;
import com.swaas.kangle.LPCourse.model.QuestionAnswerListModel;
import com.swaas.kangle.LPCourse.questionbuilder.CustomDialogClass;
import com.swaas.kangle.LPCourse.questionbuilder.QuestionActivity;
import com.swaas.kangle.R;
import com.swaas.kangle.playerPart.customviews.pdf.util.Constants;

/**
 * Created by Sayellessh on 30-10-2018.
 */

public class YesNoNaTypeQuestionFragment extends Fragment {

    private Context mContext;
    private TextView mBtnSubmit;
    private QuestionActivity questionActivity;
    private TextView mTextQuestion,mDescriptionTextQuestion;
    private LinearLayout mMutiOptionLayout,mHintLayoutHolder,label_text_question_holder;
    private int mPosition;
    private String CorrectAnswer;
    private QuestionAndAnswerModel questionAndAnswerModel;
    private ImageView mBtnHint,mQuestionImageView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        questionActivity = (QuestionActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle != null){
            questionAndAnswerModel= (QuestionAndAnswerModel) bundle.getSerializable("val");
            mPosition = bundle.getInt(Constants.POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.question_with_single_choice_buttontype,container,false);
        mBtnSubmit = (TextView) view.findViewById(R.id.btn_submit);
        mTextQuestion = (TextView) view.findViewById(R.id.label_text_question);
        mMutiOptionLayout = (LinearLayout) view.findViewById(R.id.option_holder);
        mHintLayoutHolder = (LinearLayout) view.findViewById(R.id.hint_layout_holder);
        mDescriptionTextQuestion = (TextView) view.findViewById(R.id.question_description_text);
        label_text_question_holder =(LinearLayout)view.findViewById(R.id.label_text_question_holder);
        label_text_question_holder.setBackgroundColor(Color.parseColor(com.swaas.kangle.utils.Constants.COMPANY_COLOR));

        mTextQuestion.setText(String.valueOf(mPosition+1)+ "."+questionAndAnswerModel.getQuestionModel().getQuestion_Text());
        if(questionAndAnswerModel.getQuestionModel().getQuestion_Description().length() > 0) {
            mDescriptionTextQuestion.setVisibility(View.VISIBLE);
            mDescriptionTextQuestion.setText(questionAndAnswerModel.getQuestionModel().getQuestion_Description());
        }else{
            mDescriptionTextQuestion.setVisibility(View.GONE);
        }
        mQuestionImageView = (ImageView) view.findViewById(R.id.question_image);

        final LinearLayout l2 = new LinearLayout(mContext);
        l2.setOrientation(LinearLayout.HORIZONTAL);
        float sizenew = questionAndAnswerModel.getLstAnswer().size();
        l2.setWeightSum(sizenew);

        l2.removeAllViews();

        if (questionAndAnswerModel.getQuestionModel().getQuestion_Image_Url()!= null && questionAndAnswerModel.getQuestionModel().getQuestion_Image_Url().length()>0){
            mQuestionImageView.setVisibility(View.VISIBLE);
            Ion.with(mContext).load(questionAndAnswerModel.getQuestionModel().getQuestion_Image_Url()).intoImageView(mQuestionImageView);

            mQuestionImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CustomDialogClass cdd=new CustomDialogClass(questionActivity,questionAndAnswerModel.getQuestionModel().getQuestion_Image_Url());
                    cdd.show();


                }
            });



        }else {
            mQuestionImageView.setVisibility(View.GONE);

        }

        int pos = 0;
        for (final QuestionAnswerListModel answermodel : questionAndAnswerModel.getLstAnswer()) {
            final TextView yesnobutton = new TextView(mContext);
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            int size = questionAndAnswerModel.getLstAnswer().size();
            int width = display.getWidth()/size;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    width,LinearLayout.LayoutParams.MATCH_PARENT,1.0f
            );
            params.setMargins(10, 0, 10, 10);
            yesnobutton.setLayoutParams(params);
            yesnobutton.setPadding(30,30,30,30);
            yesnobutton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_allcorners_grey));
            yesnobutton.setId(answermodel.Answer_Id);
            yesnobutton.setGravity(Gravity.CENTER);
            yesnobutton.setText(answermodel.Answer_Text);
            yesnobutton.setTextColor(mContext.getResources().getColor(R.color.black));
            yesnobutton.setTypeface(yesnobutton.getTypeface(), Typeface.BOLD);
            if (answermodel.getIs_Correct_Answer() == 1){
                questionAndAnswerModel.setCorrectAnswer(answermodel.Answer_Text);
                questionAndAnswerModel.setCorrectAnswerId(answermodel.Answer_Id+"");
            }
            if(answermodel.isChosenbooleanAnswer() != 0){
                if(pos == 0){
                    if(Integer.parseInt(questionAndAnswerModel.getChoosenAnswerId()) == answermodel.isChosenbooleanAnswer()){
                        yesnobutton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_allcorners_green));
                        yesnobutton.setTextColor(mContext.getResources().getColor(R.color.white));
                    }else{
                        yesnobutton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_allcorners_grey));
                        yesnobutton.setTextColor(mContext.getResources().getColor(R.color.black));
                    }
                }else if(pos == 1){
                    if(Integer.parseInt(questionAndAnswerModel.getChoosenAnswerId()) == answermodel.isChosenbooleanAnswer()){
                        yesnobutton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_allcorners_amber));
                        yesnobutton.setTextColor(mContext.getResources().getColor(R.color.white));
                    }else{
                        yesnobutton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_allcorners_grey));
                        yesnobutton.setTextColor(mContext.getResources().getColor(R.color.black));
                    }
                }else if(pos == 2){
                    if(Integer.parseInt(questionAndAnswerModel.getChoosenAnswerId()) == answermodel.isChosenbooleanAnswer()){
                        yesnobutton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_allcorners_red));
                        yesnobutton.setTextColor(mContext.getResources().getColor(R.color.white));
                    }else{
                        yesnobutton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_allcorners_grey));
                        yesnobutton.setTextColor(mContext.getResources().getColor(R.color.black));
                    }
                }else {
                    yesnobutton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                    yesnobutton.setTextColor(mContext.getResources().getColor(R.color.black));
                }
            }

            yesnobutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    answermodel.setChosenbooleanAnswer(answermodel.Answer_Id);
                    questionAndAnswerModel.setChoosenAnswer(yesnobutton.getText().toString());
                    questionAndAnswerModel.setChoosenAnswerId(yesnobutton.getId()+"");
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(YesNoNaTypeQuestionFragment.this).attach(YesNoNaTypeQuestionFragment.this).commit();
                }
            });

            l2.addView(yesnobutton);
            pos = pos+1;
        }

        mMutiOptionLayout.addView(l2);

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (questionAndAnswerModel.getChoosenAnswer()!=null && questionAndAnswerModel.getChoosenAnswer().length()>0){
                    questionActivity.onSubmitAnswer(mPosition+1);
                }else {

                    Toast.makeText(getContext(),mContext.getResources().getString(R.string.validation_msg_question),Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (questionAndAnswerModel.getQuestionModel().getHint() != null && questionAndAnswerModel.getQuestionModel().getHint().length()>0){

            mHintLayoutHolder.setVisibility(View.VISIBLE);

        }else {

            mHintLayoutHolder.setVisibility(View.GONE);

        }

        mHintLayoutHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowHintDilogue(questionAndAnswerModel.getQuestionModel().getHint());

            }
        });



        if(questionActivity != null && questionActivity.questionandanswerlist.size() == 1){
            mBtnSubmit.setVisibility(View.VISIBLE);
        }else{
            if(mPosition == questionActivity.questionandanswerlist.size()-1){
                mBtnSubmit.setVisibility(View.VISIBLE);
            }else{
                mBtnSubmit.setVisibility(View.GONE);

            }

        }



        return view;
    }

    private void ShowHintDilogue(String message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
