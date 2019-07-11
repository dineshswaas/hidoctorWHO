package com.swaas.kangle.LPCourse.questionbuilder.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
 * Created by Hariharan on 16/8/17.
 */

public class SingleOptionQuestionFragment extends Fragment {


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
        final View view = LayoutInflater.from(mContext).inflate(R.layout.question_with_single_option,container,false);
        mBtnSubmit = (TextView) view.findViewById(R.id.btn_submit);
        mTextQuestion = (TextView) view.findViewById(R.id.label_text_question);
        mMutiOptionLayout = (LinearLayout) view.findViewById(R.id.option_holder);
        label_text_question_holder = (LinearLayout)view.findViewById(R.id.label_text_question_holder);
        mHintLayoutHolder = (LinearLayout) view.findViewById(R.id.hint_layout_holder);
        mDescriptionTextQuestion = (TextView) view.findViewById(R.id.question_description_text);


        label_text_question_holder.setBackgroundColor(Color.parseColor(com.swaas.kangle.utils.Constants.COMPANY_COLOR));



        mTextQuestion.setText(String.valueOf(mPosition+1)+ "."+questionAndAnswerModel.getQuestionModel().getQuestion_Text());
        if(questionAndAnswerModel.getQuestionModel().getQuestion_Description().length() > 0) {
            mDescriptionTextQuestion.setVisibility(View.VISIBLE);
            mDescriptionTextQuestion.setText(questionAndAnswerModel.getQuestionModel().getQuestion_Description());
        }else{
            mDescriptionTextQuestion.setVisibility(View.GONE);
        }
        mQuestionImageView = (ImageView) view.findViewById(R.id.question_image);
        final RadioGroup ll = new RadioGroup(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);

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

        for (QuestionAnswerListModel answermodel :questionAndAnswerModel.getLstAnswer()  ) {
            final RadioButton rdbtn = new RadioButton(mContext);
            rdbtn.setId(answermodel.Answer_Id);
            rdbtn.setPadding(30,30,30,30);
            if (answermodel.getIs_Correct_Answer() == 1){

                questionAndAnswerModel.setCorrectAnswerId(""+answermodel.Answer_Id);
                questionAndAnswerModel.setCorrectAnswer(answermodel.Answer_Text);
            }

            rdbtn.setText(answermodel.Answer_Text);
            rdbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked){

                        questionAndAnswerModel.setChoosenAnswerId(""+rdbtn.getId());
                        questionAndAnswerModel.setChoosenAnswer(rdbtn.getText().toString());

                    }

                }
            });

            ll.addView(rdbtn);
        }


        mMutiOptionLayout.addView(ll);

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
            mBtnSubmit.setVisibility(View.GONE);
            if(mPosition == questionActivity.questionandanswerlist.size()-1){
                mBtnSubmit.setVisibility(View.VISIBLE);
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