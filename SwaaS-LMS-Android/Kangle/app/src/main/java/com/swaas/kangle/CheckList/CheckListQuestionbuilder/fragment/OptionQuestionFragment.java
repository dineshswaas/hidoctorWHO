package com.swaas.kangle.CheckList.CheckListQuestionbuilder.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class OptionQuestionFragment extends Fragment {

    private Context mContext;
    private TextView mBtnSubmit;
    private QuestionActivity questionActivity;
    private TextView mTextQuestion,mDescriptionTextQuestion;
    private ImageView mQuestionImageView;
    private LinearLayout mMutiOptionLayout;
    private int mPosition;
    private QuestionAndAnswerModel questionAndAnswerModel;
    private String ChoosenAnswer;
    private LinearLayout mHintLayoutHolder;
    private String ChoosenAnswerId;

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
        View view = LayoutInflater.from(mContext).inflate(R.layout.question_with_more_option,container,false);
        mBtnSubmit = (TextView) view.findViewById(R.id.btn_submit);
        mTextQuestion = (TextView) view.findViewById(R.id.label_text_question);
        mHintLayoutHolder = (LinearLayout) view.findViewById(R.id.hint_layout_holder);
        mMutiOptionLayout = (LinearLayout) view.findViewById(R.id.option_holder);
        mQuestionImageView = (ImageView) view.findViewById(R.id.question_image);
        mDescriptionTextQuestion = (TextView) view.findViewById(R.id.question_description_text);

        mTextQuestion.setText(questionAndAnswerModel.getQuestionModel().Question_Text);
        if(questionAndAnswerModel.getQuestionModel().getQuestion_Description().length() > 0) {
            mDescriptionTextQuestion.setVisibility(View.VISIBLE);
            mDescriptionTextQuestion.setText(questionAndAnswerModel.getQuestionModel().getQuestion_Description());
        }else{
            mDescriptionTextQuestion.setVisibility(View.GONE);
        }

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

        for(QuestionAnswerListModel answermodel : questionAndAnswerModel.getLstAnswer() ) {

            final CheckBox cb = new CheckBox(mContext);
            cb.setPadding(30,30,30,30);
            cb.setId(answermodel.Answer_Id);
            cb.setText(answermodel.Answer_Text);

            if (answermodel.getIs_Correct_Answer() == 1){

                if (questionAndAnswerModel.getCorrectAnswer()!=null){

                    questionAndAnswerModel.setCorrectAnswer(questionAndAnswerModel.getCorrectAnswer()+answermodel.Answer_Text+",");
                    questionAndAnswerModel.setCorrectAnswerId(questionAndAnswerModel.getCorrectAnswerId()+answermodel.Answer_Id+",");

                }else {

                    questionAndAnswerModel.setCorrectAnswer(answermodel.Answer_Text+",");
                    questionAndAnswerModel.setCorrectAnswerId(answermodel.Answer_Id+",");

                }
            }
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        StringBuilder str = new StringBuilder();
                        str.append(buttonView.getText().toString()).append(",");
                        if (ChoosenAnswer!=null&&ChoosenAnswer.length()>0){

                            ChoosenAnswer = ChoosenAnswer + str.toString();
                            ChoosenAnswerId = ChoosenAnswerId+cb.getId()+",";

                        }else {

                            ChoosenAnswer = str.toString();
                            ChoosenAnswerId = cb.getId()+",";

                        }

                    }else {

                        if (ChoosenAnswer!=null && ChoosenAnswer.length()>0) {
                            //StringTokenizer st = new StringTokenizer(savedString, ",");
                            String [] answerlist = ChoosenAnswer.split(",");
                            String [] choosenanswerid = ChoosenAnswerId.split(",");
                            for (int i = 0; i < answerlist.length; i++)
                            {
                                if (answerlist[i].equals(buttonView.getText().toString())) {
                                    answerlist[i] = null;
                                    choosenanswerid[i] = null;
                                    break;
                                }
                            }
                            StringBuilder str = new StringBuilder();
                            StringBuilder stringBuilder =  new StringBuilder();
                            for (int j=0; j<answerlist.length;j++) {
                                if (answerlist[j]!=null){
                                    str.append(answerlist[j]).append(",");
                                }
                                if (choosenanswerid[j] !=null){

                                    stringBuilder.append(choosenanswerid[j]).append(",");
                                }
                            }
                            ChoosenAnswer = str.toString();
                            ChoosenAnswerId = stringBuilder.toString();

                        }

                    }

                    questionAndAnswerModel.setChoosenAnswer(ChoosenAnswer);
                    questionAndAnswerModel.setChoosenAnswerId(ChoosenAnswerId);

                }
            });
            mMutiOptionLayout.addView(cb);
        }

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (questionAndAnswerModel.getChoosenAnswer()!=null && questionAndAnswerModel.getChoosenAnswer().length()>0){
                    questionActivity.onSubmitAnswer(mPosition+1);
                    mBtnSubmit.setEnabled(false);
                }else {

                    Toast.makeText(getContext(),mContext.getResources().getString(R.string.validation_msg_question),Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (questionAndAnswerModel.getQuestionModel().getHint() != null && questionAndAnswerModel.getQuestionModel().getHint().length()>0 ){

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
