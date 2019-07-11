package com.swaas.kangle.LPCourse.questionbuilder.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.swaas.kangle.LPCourse.model.QuestionAndAnswerModel;
import com.swaas.kangle.LPCourse.questionbuilder.QuestionActivity;
import com.swaas.kangle.R;
import com.swaas.kangle.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MatchTheFollowingFragment extends Fragment {

    private Context mContext;
    private TextView mBtnSubmit;
    private QuestionActivity questionActivity;
    private TextView mTextQuestion,mDescriptionTextQuestion;
    private LinearLayout mMutiOptionLayout,mHintLayoutHolder,label_text_question_holder;
    private int mPosition;
    private String CorrectAnswer;
    private QuestionAndAnswerModel questionAndAnswerModel;
    private ImageView mBtnHint,mQuestionImageView;
    List<Integer> colors = new ArrayList<Integer>();
    List<Integer> selectedlist = new ArrayList<Integer>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        questionActivity = (QuestionActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle != null){
            questionAndAnswerModel= (QuestionAndAnswerModel) bundle.getSerializable("val");
            mPosition = bundle.getInt(com.swaas.kangle.playerPart.customviews.pdf.util.Constants.POSITION);
        }
        String[] colorsTxt = mContext.getResources().getStringArray(R.array.colors);

        for (int i = 0; i < colorsTxt.length; i++) {
            int newColor = Color.parseColor(colorsTxt[i]);
            colors.add(newColor);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.question_with_single_choice_buttontype, container, false);
        mBtnSubmit = (TextView) view.findViewById(R.id.btn_submit);
        mTextQuestion = (TextView) view.findViewById(R.id.label_text_question);
        mMutiOptionLayout = (LinearLayout) view.findViewById(R.id.option_holder);
        mHintLayoutHolder = (LinearLayout) view.findViewById(R.id.hint_layout_holder);
        mDescriptionTextQuestion = (TextView) view.findViewById(R.id.question_description_text);
        label_text_question_holder = (LinearLayout) view.findViewById(R.id.label_text_question_holder);
        label_text_question_holder.setBackgroundColor(Color.parseColor(com.swaas.kangle.utils.Constants.COMPANY_COLOR));
        mTextQuestion.setText(String.valueOf(mPosition+1)+ "."+questionAndAnswerModel.getQuestionModel().getQuestion_Text());
        if(questionAndAnswerModel.getQuestionModel().getQuestion_Description().length() > 0) {
            mDescriptionTextQuestion.setVisibility(View.VISIBLE);
            mDescriptionTextQuestion.setText(questionAndAnswerModel.getQuestionModel().getQuestion_Description());
        }else{
            mDescriptionTextQuestion.setVisibility(View.GONE);
        }
        if (questionAndAnswerModel.getQuestionModel().getQuestion_Type() == Constants.MATCH_THE_FOLLOWING &&
                questionAndAnswerModel.getLstMatchingQA() != null && questionAndAnswerModel.getLstMatchingQA().size() > 0) {
            final String selectedcolor = String.valueOf(R.color.grey);
            final int[] currentpos = {0};

            final LinearLayout l1 = new LinearLayout(mContext);
            l1.setOrientation(LinearLayout.VERTICAL);
            final LinearLayout l2 = new LinearLayout(mContext);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            float sizenew = 1;
            l2.setWeightSum(sizenew);
            l2.setBaselineAligned(true);
            l2.removeAllViews();
            // ArrayList<SelectedOptionModel> selectedOptionModel = new ArrayList<>();
            // SelectedOptionModel temp1 = new SelectedOptionModel();
//            for(int i =0;i<questionAndAnswerModel.getLstMatchingQAList().size();i++)
//            {
//                temp1.setIsclicked(false);
//                //temp1.setColor(questionAndAnswerModel.getMatch_the_folowing_qa_models().get(i).getColor_option());
//                temp1.setPosition(i);
//                selectedOptionModel.add(temp1);
//            }
            //selectedOptionModel1=selectedOptionModel;
            final LinearLayout l3 = new LinearLayout(mContext);
            l3.setOrientation(LinearLayout.VERTICAL);
            l3.setBaselineAligned(true);
            int pos = 0;
//            ArrayList<Match_the_following_QA_model> answermodel = new ArrayList<>();
//            answermodel= (ArrayList<Match_the_following_QA_model>) questionAndAnswerModel.getMatch_the_folowing_qa_models();
            final Button[] yesnobutton = new Button[questionAndAnswerModel.getLstMatchingQA().size()];
            for (int i = 0; i < questionAndAnswerModel.getLstMatchingQA().size(); i++) {
                yesnobutton[i] = new Button(mContext);
                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                //int size = questionAndAnswerModel.getMatch_the_folowing_qa_models().size();
                int width = (int) (display.getWidth() / 2.5);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        width, 225, 1.0f
                );
                params.setMargins(10, 10, 10, 10);

                yesnobutton[i] = new Button(mContext);
                yesnobutton[i].setLayoutParams(params);
                yesnobutton[i].setPadding(30, 30, 30, 30);
                yesnobutton[i].setBackground(mContext.getResources().getDrawable(R.drawable.rounded_allcorners_grey));
                // yesnobutton[i].setId(questionAndAnswerModel.getLstMatchingQAList().get(i).);
                yesnobutton[i].setGravity(Gravity.LEFT);
                yesnobutton[i].setText(questionAndAnswerModel.getLstMatchingQA().get(i).getSubQuestiontext());
                yesnobutton[i].setTextColor(mContext.getResources().getColor(R.color.black));
                yesnobutton[i].setTypeface(yesnobutton[pos].getTypeface(), Typeface.NORMAL);
                yesnobutton[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                yesnobutton[i].setTextSize(10);
                // yesnobutton[i].setOnClickListener(handleOnClick(yesnobutton[i],pos,questionAndAnswerModel));
                questionAndAnswerModel.getLstRandom().get(i).setChoosenAnswer("0");

//              if(questionAndAnswerModel.getMatch_the_folowing_QA_model().get(i).isChoosenleft())
//                {
//                    currentpos=i;
//                    yesnobutton[i].setBackgroundColor(Color.parseColor(questionAndAnswerModels.get(position).getMatch_the_folowing_QA_model().get(i).getColor_option()));
//                    yesnobutton[i].setTextColor(mContext.getResources().getColor(R.color.white));
//                }
                final int finalPos3 = pos;
                yesnobutton[pos].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //int selpos = 0;

                        yesnobutton[finalPos3].setBackgroundColor(colors.get(finalPos3));
                        selectedlist.add(0, colors.get(finalPos3));
                        currentpos[0] = finalPos3;
                        yesnobutton[finalPos3].setTextColor(mContext.getResources().getColor(R.color.white));
                        // questionActivity.loaditem(position);
//                        questionAndAnswerModel.getMatch_the_folowing_QA_model().get(finalPos).setChoosenleft(true);
                        //notifyItemChanged(position);
                    }
                });


                l3.addView(yesnobutton[i]);
                pos = pos + 1;
            }
            final LinearLayout l4 = new LinearLayout(mContext);
            l4.setOrientation(LinearLayout.VERTICAL);
            l4.setBaselineAligned(true);
            int pos1 = 0;
//            ArrayList<Match_the_following_QA_model> answermodel1 = new ArrayList<>();
//            answermodel1= (ArrayList<Match_the_following_QA_model>) questionAndAnswerModel.getMatch_the_folowing_qa_models();
            final Button option[] = new Button[questionAndAnswerModel.getLstMatchingQA().size()];
            for (int j = 0; j < questionAndAnswerModel.getLstMatchingQA().size(); j++) {
                option[j] = new Button(mContext);
                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                //int size = questionAndAnswerModel.getMatch_the_folowing_qa_models().size();
                int width = (int) (display.getWidth() / 2.5);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        width, 225, 1.0f
                );
                params.setMargins(10, 10, 10, 10);
                option[j].setLayoutParams(params);
                option[j].setPadding(30, 30, 30, 30);
                option[j].setBackground(mContext.getResources().getDrawable(R.drawable.rounded_allcorners_grey));
                //option[j].setId(answermodel1.get(j).getQuestion_id());
                option[j].setMinimumHeight(yesnobutton[j].getMinimumHeight());
                option[j].setGravity(Gravity.LEFT);
                option[j].setText(questionAndAnswerModel.getLstRandom().get(j).getRandomAnswertext());
                option[j].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                option[j].setText(answermodel1.get(j).getOrder_Option());
                option[j].setTextColor(mContext.getResources().getColor(R.color.black));
                option[j].setTypeface(option[pos1].getTypeface(), Typeface.NORMAL);
                option[j].setTextSize(10);
                //option[j].setOnClickListener(handleOnClick1(option[j], j, questionAndAnswerModel));


//              if(questionAndAnswerModel.getMatch_the_folowing_QA_model().get(j).isChoosenright())
//                {
//                    option[j].setBackgroundColor(Color.parseColor(questionAndAnswerModels.get(position).getMatch_the_folowing_QA_model().get(currentpos).getColor_option()));
//                    option[j].setTextColor(mContext.getResources().getColor(R.color.white));
//                }

                final int finalPos = j;
                final int finalPos2 = pos1;
                final int[] count = {0};
                option[pos1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        option[finalPos2].setBackgroundColor(selectedlist.get(0));
                        option[finalPos2].setTextColor(mContext.getResources().getColor(R.color.white));
                        questionAndAnswerModel.getLstRandom().get(currentpos[0]).setChoosenAnswer(questionAndAnswerModel.getLstRandom().get(finalPos2).getRandomAnswertext());
                        questionAndAnswerModel.setChoosenAnswer("0c");
//                            for (int i = 0 ;i < questionAndAnswerModel.getLstRandom().size();i++)
//                            {
//                                if(questionAndAnswerModel.getLstRandom().get(i).getChoosenAnswer()
//                                        .equalsIgnoreCase(questionAndAnswerModel.getLstRandom().get(currentpos[0]).getChoosenAnswer()) )
//                                {
//                                    count[0] = count[0] + 1;
//
//                                }
//
//                            }
//                        for (int i = 0 ;i < questionAndAnswerModel.getLstRandom().size();i++) {
//                            if (count[0] >= 1) {
//
//                                    option[i].setBackground(mContext.getResources().getDrawable(R.drawable.rounded_allcorners_grey));
//                                    option[i].setTextColor(mContext.getResources().getColor(R.color.black));
//                                    questionAndAnswerModel.getLstRandom().get(i).setChoosenAnswer("0");
//
//                            }
//                        }
//                        count[0] = 0;
//                        option[finalPos2].setBackgroundColor(selectedlist.get(0));
//                        option[finalPos2].setTextColor(mContext.getResources().getColor(R.color.white));
//                        questionAndAnswerModel.getLstRandom().get(currentpos[0]).setChoosenAnswer(questionAndAnswerModel.getLstRandom().get(finalPos2).getRandomAnswertext());
                    }
                });

                l4.addView(option[j]);
                pos1 = pos1 + 1;

            }
            final int finalPos1 = pos1;

            l2.addView(l3);
            l2.addView(l4);
            l1.addView(l2);
            TextView clearbutton = new TextView(mContext);
            clearbutton.setText("Reset");
            //clearbutton.setBackgroundColor(Color.RED);
            clearbutton.setTextColor(Color.RED);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f
            );
            params1.setMargins(10, 0, 0, 10);
            clearbutton.setLayoutParams(params1);
            l1.addView(clearbutton);
            mMutiOptionLayout.addView(l1);
            // final ArrayList<Match_the_following_QA_model> finalAnswermodel = answermodel1;
            clearbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, " cleared selections", Toast.LENGTH_SHORT).show();
                    for (int j = 0; j < questionAndAnswerModel.getLstMatchingQA().size(); j++) {

                        option[j].setBackground(mContext.getResources().getDrawable(R.drawable.rounded_allcorners_grey));
                        yesnobutton[j].setBackground(mContext.getResources().getDrawable(R.drawable.rounded_allcorners_grey));
                        option[j].setTextColor(mContext.getResources().getColor(R.color.black));
                        yesnobutton[j].setTextColor(mContext.getResources().getColor(R.color.black));
                        questionAndAnswerModel.getLstRandom().get(j).setChoosenAnswer("0");
                    }
                }

            });
        }
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


        if(questionActivity != null && questionActivity.questionandanswerlist.size() == 1){
            mBtnSubmit.setVisibility(View.VISIBLE);
        }else{
            if(mPosition == questionActivity.questionandanswerlist.size()-1){
                mBtnSubmit.setVisibility(View.VISIBLE);
            }else{
                mBtnSubmit.setVisibility(View.GONE);

            }

        }
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
