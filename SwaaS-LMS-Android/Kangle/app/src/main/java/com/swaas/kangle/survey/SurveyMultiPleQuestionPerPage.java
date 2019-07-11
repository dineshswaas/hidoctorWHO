package com.swaas.kangle.survey;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Scroller;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;
import com.swaas.kangle.CheckList.CheckListQuestionbuilder.CustomDialogClass;
import com.swaas.kangle.CheckList.CheckListQuestionbuilder.QuestionActivity;
import com.swaas.kangle.CheckList.model.QuestionAndAnswerModel;
import com.swaas.kangle.CheckList.model.QuestionAnswerListModel;
import com.swaas.kangle.R;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.MessageDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sayellessh on 02-05-2018.
 */

public class SurveyMultiPleQuestionPerPage extends RecyclerView.Adapter<SurveyMultiPleQuestionPerPage.MyMultiPleQuestionViewHolder> {

    private Context mContext;
    private ArrayList<QuestionAndAnswerModel> questionAndAnswerModels;
    private SurveyQuestionDetailsActivity questionActivity;
    MessageDialog messageDialog;
    List<Integer> colors = new ArrayList<Integer>();
    List<Integer> selectedlist = new ArrayList<Integer>();
    public SurveyMultiPleQuestionPerPage(Context context, ArrayList<QuestionAndAnswerModel> questionAndAnswerModels, SurveyQuestionDetailsActivity questionActivity){

        this.mContext = context;
        this.questionAndAnswerModels = questionAndAnswerModels;
        this.questionActivity = questionActivity;
        String[] colorsTxt = context.getResources().getStringArray(R.array.colors);
        for (int i = 0; i < colorsTxt.length; i++) {
            int newColor = Color.parseColor(colorsTxt[i]);
            colors.add(newColor);
        }
    }

    @Override
    public MyMultiPleQuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.survey_checklist_multiplequestion_row_view,parent,false);
        messageDialog = new MessageDialog(mContext);
        return new MyMultiPleQuestionViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final MyMultiPleQuestionViewHolder holder, final int position) {


        final QuestionAndAnswerModel questionAndAnswerModel = questionAndAnswerModels.get(position);

        String questionText = questionAndAnswerModel.getQuestionModel().Question_Text;
        if(questionAndAnswerModel.getQuestionModel().getQuestion_Number_Title() != null &&
                !questionAndAnswerModel.getQuestionModel().getQuestion_Number_Title().isEmpty()){
            questionText = (questionAndAnswerModel.getQuestionModel().getQuestion_Number_Title()+". "+questionText);
        }else{
            questionText = ((position+1)+". "+questionText);
        }

        if(questionAndAnswerModel.getQuestionModel().getIs_Required() == 1) {
            holder.mQuestion_text.setText(questionText+"  *");
        }else{
            holder.mQuestion_text.setText(questionText);
        }

        if(questionAndAnswerModel.getQuestionModel().getQuestion_Description().length() > 0) {
            holder.mDescriptionTextQuestion.setVisibility(View.VISIBLE);
            holder.mDescriptionTextQuestion.setText(questionAndAnswerModel.getQuestionModel().getQuestion_Description());
        }else{
            holder.mDescriptionTextQuestion.setVisibility(View.GONE);
        }

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.comment.setFocusable(true);
            }
        });

        holder.comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                questionAndAnswerModel.setCommentText(holder.comment.getText().toString().trim());
            }
        });

        if(questionAndAnswerModel.getCommentText() != null){
            if(questionAndAnswerModel.getCommentText().length()>0) {
                holder.comment.setText(questionAndAnswerModel.getCommentText().toString().trim());
                holder.attachmentallowedview.setVisibility(View.VISIBLE);
            }
        }

        if (questionAndAnswerModel.getQuestionModel().getQuestion_Image_Url()!=null && questionAndAnswerModel.getQuestionModel().getQuestion_Image_Url().length()>0){

            holder.mQuestionImageview.setVisibility(View.VISIBLE);
            Ion.with(mContext).load(questionAndAnswerModel.getQuestionModel().getQuestion_Image_Url()).intoImageView(holder.mQuestionImageview);

            holder.mQuestionImageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomDialogClass cdd=new CustomDialogClass(questionActivity,questionAndAnswerModel.getQuestionModel().getQuestion_Image_Url());
                    cdd.show();
                }
            });
        }else {
            holder.mQuestionImageview.setVisibility(View.GONE);
        }

        //Binding View based on Answer Types
        if (questionAndAnswerModel.getQuestionModel().getQuestion_Type() == Constants.SINGLE_CHOICE_BUTTON_TYPE){

            final LinearLayout l2 = new LinearLayout(mContext);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            float sizenew = questionAndAnswerModel.getLstAnswer().size();
            l2.setWeightSum(sizenew);
            holder.poolresult.setVisibility(View.VISIBLE);
            l2.removeAllViews();
            holder.mMutiple_question_option_holder.removeAllViews(); //duplication removed when binding 09-03-2018
            for (final QuestionAnswerListModel answermodel : questionAndAnswerModel.getLstAnswer()) {
                final TextView singlechoicebutton = new TextView(mContext);
                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int size = questionAndAnswerModel.getLstAnswer().size();
                int width = display.getWidth()/size;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        width,LinearLayout.LayoutParams.MATCH_PARENT,1.0f
                );
                params.setMargins(10, 0, 10, 10);
               // singlechoicebutton.setLayoutParams(params);
                singlechoicebutton.setPadding(30, 30, 30, 30);
                singlechoicebutton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_allcorners_grey));
                singlechoicebutton.setId(answermodel.Answer_Id);
                singlechoicebutton.setGravity(Gravity.CENTER);
                singlechoicebutton.setText(answermodel.Answer_Text);
                singlechoicebutton.setTextColor(mContext.getResources().getColor(R.color.black));
                singlechoicebutton.setTypeface(singlechoicebutton.getTypeface(), Typeface.BOLD);
                if (answermodel.getIs_Correct_Answer() == 1) {
                    questionAndAnswerModel.setCorrectAnswer(answermodel.Answer_Text);
                    questionAndAnswerModel.setCorrectAnswerId(answermodel.Answer_Id + "");
                }
                if (answermodel.isChosenbooleanAnswer() != 0) {
                    if (Integer.parseInt(questionAndAnswerModel.getChoosenAnswerId()) == answermodel.isChosenbooleanAnswer()) {
                        singlechoicebutton.setBackgroundResource(R.drawable.tags_rounded_corners);
                        GradientDrawable drawable = (GradientDrawable) singlechoicebutton.getBackground();
                        drawable.setColor(Color.parseColor(answermodel.getAnswer_Colour()));
                        //singlechoicebutton.setBackgroundColor(Color.parseColor(answermodel.getAnswer_Colour()));
                        singlechoicebutton.setTextColor(mContext.getResources().getColor(R.color.white));
                    } else {
                        singlechoicebutton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_allcorners_grey));
                        singlechoicebutton.setTextColor(mContext.getResources().getColor(R.color.black));
                    }
                }
                singlechoicebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        answermodel.setChosenbooleanAnswer(answermodel.Answer_Id);
                        questionAndAnswerModel.setChoosenAnswer(singlechoicebutton.getText().toString());
                        questionAndAnswerModel.setChoosenAnswerId(singlechoicebutton.getId()+"");
                        singlechoicebutton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                        questionActivity.loaditem(position);
                        //notifyItemChanged(position);
                    }
                });

               l2.addView(singlechoicebutton);
            }

            holder.mMutiple_question_option_holder.addView(l2);

        } else if (questionAndAnswerModel.getQuestionModel().getQuestion_Type() == Constants.YES_NO_NA_TYPE){

            final LinearLayout l2 = new LinearLayout(mContext);
            float sizenew = questionAndAnswerModel.getLstAnswer().size();
            l2.setWeightSum(sizenew);
            holder.poolresult.setVisibility(View.VISIBLE);
            l2.removeAllViews();
            holder.mMutiple_question_option_holder.removeAllViews(); //duplication removed when binding 09-03-2018
            int value = 20;

            if(questionAndAnswerModel.getQuestionModel().isFroPoolResult()){
                for (final QuestionAnswerListModel answermodel : questionAndAnswerModel.getLstAnswer()) {
                    l2.setOrientation(LinearLayout.VERTICAL);
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View rowView = inflater.inflate(R.layout.survey_progress_bar, null);
                    final RoundedHorizontalProgressBar progressBar = (RoundedHorizontalProgressBar)rowView.findViewById(R.id.progress_bar_1);
                    final TextView progressLabel = (TextView)rowView.findViewById(R.id.progressLabel);
                    final TextView progressTextValue = (TextView)rowView.findViewById(R.id.progressTextValue);
                    progressBar.setProgress(value,true);

                    value = value + 10;
                    progressLabel.setText(answermodel.Answer_Text);
                    progressTextValue.setText(String.valueOf(value+"%"));
                    progressBar.animateProgress(1500, 0, value);
                    if(value == 50){
                        progressBar.setProgressColors(R.color.checklist_amber,R.color.grey);
                    }
                    l2.addView(rowView);
                }

            }else{
                l2.setOrientation(LinearLayout.HORIZONTAL);
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
                            //int selpos = 0;
                            answermodel.setChosenbooleanAnswer(answermodel.Answer_Id);
                            questionAndAnswerModel.setChoosenAnswer(yesnobutton.getText().toString());
                            questionAndAnswerModel.setChoosenAnswerId(yesnobutton.getId()+"");
                            yesnobutton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                        /*for(QuestionAnswerListModel ansmodel : questionAndAnswerModel.getLstAnswer()){
                            if(Integer.parseInt(questionAndAnswerModel.getChoosenAnswerId()) == ansmodel.isChosenbooleanAnswer()) {
                                if (selpos == 0) {
                                    yesnobutton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                                    yesnobutton.setBackgroundColor(mContext.getResources().getColor(R.color.checklist_green));
                                    yesnobutton.setTextColor(mContext.getResources().getColor(R.color.white));
                                }else if(selpos == 1){
                                    yesnobutton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                                    yesnobutton.setBackgroundColor(mContext.getResources().getColor(R.color.checklist_red));
                                    yesnobutton.setTextColor(mContext.getResources().getColor(R.color.white));
                                }else if(selpos == 2){
                                    yesnobutton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                                    yesnobutton.setBackgroundColor(mContext.getResources().getColor(R.color.checklist_grey));
                                    yesnobutton.setTextColor(mContext.getResources().getColor(R.color.white));
                                }else{
                                    yesnobutton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                                    yesnobutton.setTextColor(mContext.getResources().getColor(R.color.black));
                                }
                            }
                            selpos = selpos + 1;
                        }*/
                            questionActivity.loaditem(position);
                            //notifyItemChanged(position);
                        }
                    });

                    l2.addView(yesnobutton);
                    pos = pos+1;
                }
            }

            holder.mMutiple_question_option_holder.addView(l2);

            holder.poolresult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(questionAndAnswerModel.getQuestionModel().isFroPoolResult()){
                        questionAndAnswerModel.getQuestionModel().setFroPoolResult(false);
                    }else{
                        questionAndAnswerModel.getQuestionModel().setFroPoolResult(true);
                    }
                    notifyItemChanged(position);
                }
            });

        } else if (questionAndAnswerModel.getQuestionModel().getQuestion_Type() == Constants.SCALING_TYPE){

            final LinearLayout li1 = new LinearLayout(mContext);
            li1.setOrientation(LinearLayout.VERTICAL);

            final LinearLayout l2 = new LinearLayout(mContext);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            float sizenew = questionAndAnswerModel.getLstAnswer().size();
            l2.setWeightSum(sizenew);
            holder.poolresult.setVisibility(View.GONE);
            final LinearLayout l3 = new LinearLayout(mContext);
            l3.setOrientation(LinearLayout.HORIZONTAL);
            l3.setWeightSum(sizenew);

            LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1.0f);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    80);

            /*final RangeSliderView rangeslider = new RangeSliderView(mContext);
            int size = questionAndAnswerModel.getLstAnswer().size();
            rangeslider.setRangeCount(5);
            rangeslider.setLayoutParams(params);
            rangeslider.setFilledColor(mContext.getResources().getColor(R.color.tacobellbackground));*/

            final int size = questionAndAnswerModel.getLstAnswer().size();
            final SeekBar rangeslider = new SeekBar(mContext);
            rangeslider.setMax(100);
            rangeslider.setLayoutParams(params);

            li1.removeAllViews();
            holder.mMutiple_question_option_holder.removeAllViews(); //duplication removed when binding 09-03-2018

            for (final QuestionAnswerListModel answermodel : questionAndAnswerModel.getLstAnswer()) {
                final TextView labl = new TextView(mContext);
                final TextView labltext = new TextView(mContext);
                labl.setLayoutParams(p1);
                labl.setPadding(10,0,10,10);
                labl.setGravity(Gravity.CENTER);
                labl.setId(answermodel.Answer_Id);
                labl.setText(answermodel.Answer_Text);
                labltext.setLayoutParams(p1);
                labltext.setPadding(10,10,10,10);
                labltext.setGravity(Gravity.CENTER);
                labltext.setId(answermodel.Answer_Id);
                labltext.setText(answermodel.Answer_Label);
                if (answermodel.getIs_Correct_Answer() == 1){
                    questionAndAnswerModel.setCorrectAnswer(answermodel.Answer_Text);
                    questionAndAnswerModel.setCorrectAnswerId(answermodel.Answer_Id+"");
                }
                if(answermodel.isChoosensliderAnswerflag()){
                    //rangeslider.setInitialIndex(answermodel.getChoosensliderAnswer());
                    rangeslider.setProgress(answermodel.getChoosensliderAnswer());
                }
                l2.addView(labl);
                l3.addView(labltext);

                /*rangeslider.setOnSlideListener(new RangeSliderView.OnSlideListener() {
                    @Override
                    public void onSlide(int index) {
                        answermodel.setChoosensliderAnswer(index);
                        answermodel.setChoosensliderAnswerflag(true);
                        questionAndAnswerModel.setChoosenAnswer(questionAndAnswerModel.getLstAnswer().get(index).getAnswer_Text().toString());
                        questionAndAnswerModel.setChoosenAnswerId(questionAndAnswerModel.getLstAnswer().get(index).getAnswer_Id()+"");
                    }
                });*/

                rangeslider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        /*progress = ((int)Math.round(progress/size))*size;
                        seekBar.setProgress(progress);*/
                        int progressStep = 100 / (size-1);

                        int lastDotProgress = Math.round(progress / progressStep) * progressStep;
                        int nextDotProgress = lastDotProgress + progressStep;
                        int midBetweenDots = lastDotProgress + (progressStep / 2);
                        int progresslocation = 0;
                        if (progress > midBetweenDots){
                            seekBar.setProgress(nextDotProgress);
                            progresslocation = nextDotProgress;
                        }else{
                            seekBar.setProgress(lastDotProgress);
                            progresslocation = lastDotProgress;
                        }
                        int indexlocation = progresslocation/progressStep;
                        //Toast.makeText(mContext,"size :"+size+"\n progressStep :"+progressStep+"\n index :"+indexlocation,Toast.LENGTH_LONG).show();
                        answermodel.setChoosensliderAnswer(progresslocation);
                        answermodel.setChoosensliderAnswerflag(true);
                        questionAndAnswerModel.setChoosenAnswer(questionAndAnswerModel.getLstAnswer().get(indexlocation).getAnswer_Text().toString());
                        questionAndAnswerModel.setChoosenAnswerId(questionAndAnswerModel.getLstAnswer().get(indexlocation).getAnswer_Id()+"");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }
            if(questionAndAnswerModel.getQuestionModel().isShow_Scaling_Number()) {
                li1.addView(l3);
            }
            li1.addView(rangeslider);
            li1.addView(l2);

            holder.mMutiple_question_option_holder.addView(li1);


            if (questionAndAnswerModel.getQuestionModel().getMin_Range_Title() != null
                    && !questionAndAnswerModel.getQuestionModel().getMin_Range_Title().isEmpty()
                    && questionAndAnswerModel.getQuestionModel().getMax_Range_Title() != null
                    && !questionAndAnswerModel.getQuestionModel().getMax_Range_Title().isEmpty()) {
                holder.min_max_text.setVisibility(View.VISIBLE);
                holder.min_value.setVisibility(View.VISIBLE);
                holder.max_value.setVisibility(View.VISIBLE);
                holder.min_value.setText(questionAndAnswerModel.getQuestionModel().getMin_Range_Title());
                holder.max_value.setText(questionAndAnswerModel.getQuestionModel().getMax_Range_Title());
            } else {
                holder.min_max_text.setVisibility(View.GONE);
                holder.min_value.setVisibility(View.GONE);
                holder.max_value.setVisibility(View.GONE);
            }

        }else if (questionAndAnswerModel.getQuestionModel().getQuestion_Type() == Constants.RADIO_TYPE){

            final RadioGroup ll = new RadioGroup(mContext);
            ll.setOrientation(LinearLayout.VERTICAL);
            holder.poolresult.setVisibility(View.VISIBLE);
            holder.mMutiple_question_option_holder.removeAllViews(); //duplication removed when binding 09-03-2018
            int value = 10;
            if(questionAndAnswerModel.getQuestionModel().isFroPoolResult()){
                for (final QuestionAnswerListModel answermodel : questionAndAnswerModel.getLstAnswer()) {
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View rowView = inflater.inflate(R.layout.survey_progress_bar, null);
                    final RoundedHorizontalProgressBar progressBar = (RoundedHorizontalProgressBar)rowView.findViewById(R.id.progress_bar_1);
                    final TextView progressLabel = (TextView)rowView.findViewById(R.id.progressLabel);
                    final TextView progressTextValue = (TextView)rowView.findViewById(R.id.progressTextValue);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        progressBar.setProgress(value,true);
                    }

                    value = value + 10;
                    progressLabel.setText(answermodel.Answer_Text);
                    progressTextValue.setText(String.valueOf(value+"%"));
                    progressBar.animateProgress(1500, 0, value);
                    if(value == 50){
                        progressBar.setProgressColors(R.color.checklist_amber,R.color.grey);
                    }
                    ll.addView(rowView);
                }

            }else{

                for (final QuestionAnswerListModel answermodel : questionAndAnswerModel.getLstAnswer()) {
                    final RadioButton rdbtn = new RadioButton(mContext);
                    rdbtn.setPadding(30,30,30,30);
                    rdbtn.setId(answermodel.Answer_Id);
                    rdbtn.setText(answermodel.Answer_Text);
                    if (answermodel.getIs_Correct_Answer() == 1){
                        questionAndAnswerModel.setCorrectAnswer(answermodel.Answer_Text);
                        questionAndAnswerModel.setCorrectAnswerId(answermodel.Answer_Id+"");
                    }
                    if(answermodel.isChosenRadioanswer()){
                        rdbtn.setChecked(true);
                    }
                    rdbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (isChecked){
                                answermodel.setChosenRadioanswer(true);
                                questionAndAnswerModel.setChoosenAnswer(rdbtn.getText().toString());
                                questionAndAnswerModel.setChoosenAnswerId(rdbtn.getId()+"");
                            }

                        }
                    });

                    ll.addView(rdbtn);


                }
            }

            holder.mMutiple_question_option_holder.addView(ll);
            holder.poolresult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(questionAndAnswerModel.getQuestionModel().isFroPoolResult()){
                        questionAndAnswerModel.getQuestionModel().setFroPoolResult(false);
                    }else{
                        questionAndAnswerModel.getQuestionModel().setFroPoolResult(true);
                    }

                    notifyItemChanged(position);
                }
            });


        } else if (questionAndAnswerModel.getQuestionModel().getQuestion_Type() == Constants.MULTIPLE_CHOICE_TYPE){

            holder.mMutiple_question_option_holder.removeAllViews();

            for(final QuestionAnswerListModel answermodel : questionAndAnswerModel.getLstAnswer()) {

                final CheckBox[] cb = {new CheckBox(mContext)};
                cb[0].setPadding(30,30,30,30);
                cb[0].setId(answermodel.Answer_Id);
                cb[0].setText(answermodel.Answer_Text);
                holder.poolresult.setVisibility(View.VISIBLE);
                if(answermodel.isChoosencheckboxAnswer()){
                    cb[0].setChecked(true);
                }

                if (answermodel.getIs_Correct_Answer() == 1){

                    if (questionAndAnswerModel.getCorrectAnswer()!=null){

                        questionAndAnswerModel.setCorrectAnswerId(questionAndAnswerModel.getCorrectAnswerId()+answermodel.Answer_Id+",");
                        questionAndAnswerModel.setCorrectAnswer(questionAndAnswerModel.getCorrectAnswer()+answermodel.Answer_Text+",");

                    }else {

                        questionAndAnswerModel.setCorrectAnswer(answermodel.Answer_Text+",");
                        questionAndAnswerModel.setCorrectAnswerId(answermodel.Answer_Id+",");

                    }
                }

                cb[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            StringBuilder str = new StringBuilder();
                            str.append(buttonView.getText().toString()).append(",");
                            if (questionAndAnswerModel.getChoosenAnswer() !=null&& questionAndAnswerModel.getChoosenAnswer().length()>0){

                                questionAndAnswerModel.setChoosenAnswer(questionAndAnswerModel.getChoosenAnswer() + str.toString());
                                questionAndAnswerModel.setChoosenAnswerId(questionAndAnswerModel.getChoosenAnswerId()+buttonView.getId()+",");

                            }else {

                                questionAndAnswerModel.setChoosenAnswer(str.toString());
                                questionAndAnswerModel.setChoosenAnswerId(buttonView.getId()+",");

                            }
                            answermodel.setChoosencheckboxAnswer(true);
                        }else {

                            if (questionAndAnswerModel.getChoosenAnswer() !=null && questionAndAnswerModel.getChoosenAnswer().length()>0) {
                                //StringTokenizer st = new StringTokenizer(savedString, ",");
                                String [] answerlist = questionAndAnswerModel.getChoosenAnswer().split(",");
                                String [] choosenid = questionAndAnswerModel.getChoosenAnswerId().split(",");
                                for (int i = 0; i < answerlist.length; i++)
                                {
                                    if (answerlist[i].equals(buttonView.getText().toString())) {
                                        answerlist[i] = null;
                                        choosenid[i] = null;
                                        break;
                                    }
                                }
                                StringBuilder str = new StringBuilder();
                                StringBuilder strone = new StringBuilder();
                                for (int j=0; j<answerlist.length;j++) {
                                    if (answerlist[j]!=null)
                                        str.append(answerlist[j]).append(",");
                                    if (choosenid[j]!=null){
                                        strone.append(choosenid[j]).append(",");
                                    }
                                }

                                questionAndAnswerModel.setChoosenAnswer(str.toString());
                                questionAndAnswerModel.setChoosenAnswerId(strone.toString());
                            }
                            answermodel.setChoosencheckboxAnswer(false);
                        }
                    }
                });

                if (questionAndAnswerModel.getChoosenAnswerId()!=null)
                    Log.d("==>testchoosenid",questionAndAnswerModel.getChoosenAnswerId());
                holder.mMutiple_question_option_holder.addView(cb[0]);
            }

        }else if (questionAndAnswerModel.getQuestionModel().getQuestion_Type() == Constants.ESSAY_TYPE){
            final EditText editText = new EditText(mContext);
            // editText.setPadding(30,30,30,30);
            editText.setSingleLine(false);
            editText.setHint(R.string.defaulttext);
            editText.setBackgroundColor(Color.parseColor("#d1d1d3"));
            editText.setScroller(new Scroller(mContext));
            editText.setVerticalScrollBarEnabled(true);
            //editText.setCursorVisible(true);
            //editText.requestFocus();
            //editText.setSelection(0);
            editText.setMinLines(7);
            editText.setGravity(Gravity.TOP);
            editText.setLongClickable(false);


            holder.mMutiple_question_option_holder.removeAllViews();

            for(QuestionAnswerListModel answermodel : questionAndAnswerModel.getLstAnswer()) {
                questionAndAnswerModel.setCorrectAnswer(answermodel.Answer_Text);
            }

            if (questionAndAnswerModel.getChoosenAnswer()!=null && questionAndAnswerModel.getChoosenAnswer().length()>0){

                editText.setText(questionAndAnswerModel.getChoosenAnswer());
            }



            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    questionAndAnswerModel.setChoosenAnswer(editText.getText().toString());

                }
            });


            holder.mMutiple_question_option_holder.addView(editText);
        }else if (questionAndAnswerModel.getQuestionModel().getQuestion_Type() == Constants.MATCH_THE_FOLLOWING &&
                questionAndAnswerModel.getLstMatchingQA() != null && questionAndAnswerModel.getLstMatchingQA().size() > 0){
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
            holder.mMutiple_question_option_holder.removeAllViews(); //duplication removed when binding 09-03-2018
            int pos = 0;
//            ArrayList<Match_the_following_QA_model> answermodel = new ArrayList<>();
//            answermodel= (ArrayList<Match_the_following_QA_model>) questionAndAnswerModel.getMatch_the_folowing_qa_models();
            final Button[] yesnobutton = new Button[questionAndAnswerModel.getLstMatchingQA().size()];
            for (int i=0;i<questionAndAnswerModel.getLstMatchingQA().size();i++) {
                yesnobutton[i] = new Button(mContext);
                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                //int size = questionAndAnswerModel.getMatch_the_folowing_qa_models().size();
                int width = (int) (display.getWidth()/2.5);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        width,150,1.0f
                );
                params.setMargins(10, 10, 10, 10);

                yesnobutton[i] = new Button(mContext);
                yesnobutton[i].setLayoutParams(params);
                yesnobutton[i].setPadding(30,30,30,30);
                yesnobutton[i].setBackground(mContext.getResources().getDrawable(R.drawable.rounded_allcorners_grey));
                // yesnobutton[i].setId(questionAndAnswerModel.getLstMatchingQAList().get(i).);
                yesnobutton[i].setGravity(Gravity.LEFT);
                yesnobutton[i].setText(questionAndAnswerModel.getLstMatchingQA().get(i).getSubQuestiontext());
                yesnobutton[i].setTextColor(mContext.getResources().getColor(R.color.black));
                yesnobutton[i].setTypeface( yesnobutton[pos].getTypeface(), Typeface.NORMAL);
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
                        selectedlist.add(0,colors.get(finalPos3));
                        currentpos[0] = finalPos3;
                        yesnobutton[finalPos3].setTextColor(mContext.getResources().getColor(R.color.white));
                        // questionActivity.loaditem(position);
//                        questionAndAnswerModel.getMatch_the_folowing_QA_model().get(finalPos).setChoosenleft(true);
                        //notifyItemChanged(position);
                    }
                });


                l3.addView( yesnobutton[i]);
                pos = pos+1;
            }
            final LinearLayout l4 = new LinearLayout(mContext);
            l4.setOrientation(LinearLayout.VERTICAL);
            l4.setBaselineAligned(true);
            int pos1 = 0;
//            ArrayList<Match_the_following_QA_model> answermodel1 = new ArrayList<>();
//            answermodel1= (ArrayList<Match_the_following_QA_model>) questionAndAnswerModel.getMatch_the_folowing_qa_models();
            final Button option[] = new Button[questionAndAnswerModel.getLstMatchingQA().size()];
            for (int j=0;j<questionAndAnswerModel.getLstMatchingQA().size();j++) {
                option[j] = new Button(mContext);
                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                //int size = questionAndAnswerModel.getMatch_the_folowing_qa_models().size();
                int width = (int) (display.getWidth()/2.5);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        width,150,1.0f
                );
                params.setMargins(10, 10, 10, 10);
                option[j].setLayoutParams(params);
                option[j].setPadding(30,30,30,30);
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
                pos1 = pos1+1;

            }
            final int finalPos1 = pos1;

            l2.addView(l3);
            l2.addView(l4);
            l1.addView(l2);
            Button clearbutton = new Button(mContext);
            clearbutton.setText("Reset");
            clearbutton.setBackgroundColor(Color.RED);
            clearbutton.setTextColor(Color.WHITE);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f
            );
            params1.setMargins(10, 10, 10, 10);
            clearbutton.setLayoutParams(params1);
            l1.addView(clearbutton);
            holder.mMutiple_question_option_holder.addView(l1);
            // final ArrayList<Match_the_following_QA_model> finalAnswermodel = answermodel1;
            clearbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext," cleared selections",Toast.LENGTH_SHORT).show();
                    for (int j = 0; j< questionAndAnswerModel.getLstMatchingQA().size(); j++) {

                        option[j].setBackground(mContext.getResources().getDrawable(R.drawable.rounded_allcorners_grey));
                        yesnobutton[j].setBackground(mContext.getResources().getDrawable(R.drawable.rounded_allcorners_grey));
                        option[j].setTextColor(mContext.getResources().getColor(R.color.black));
                        yesnobutton[j].setTextColor(mContext.getResources().getColor(R.color.black));
                        questionAndAnswerModel.getLstRandom().get(j).setChoosenAnswer("0");
                    }
                }

            });
        }
        else if (questionAndAnswerModel.getQuestionModel().getQuestion_Type() == 9)
        {
            final LinearLayout l1 = new LinearLayout(mContext);
            l1.setOrientation(LinearLayout.VERTICAL);
            Button button = new Button(mContext);
            button.setPadding(30,30,30,30);
            button.setBackground(mContext.getResources().getDrawable(R.drawable.circular_blue_background));
            button.setText("Click to Rate");
            button.setTextColor(mContext.getResources().getColor(R.color.white));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,Ratingactivity.class);
                    mContext.startActivity(intent);
                }
            });
            l1.addView(button);
            holder.mMutiple_question_option_holder.addView(l1);
        }
        else  {

            EditText editText = new EditText(mContext);
            editText.setPadding(30,30,30,30);
            holder.poolresult.setVisibility(View.GONE);
            holder.mMutiple_question_option_holder.removeAllViews();

            for(QuestionAnswerListModel answermodel : questionAndAnswerModel.getLstAnswer()) {
                questionAndAnswerModel.setCorrectAnswer(answermodel.Answer_Text);
            }

            if (questionAndAnswerModel.getChoosenAnswer()!=null && questionAndAnswerModel.getChoosenAnswer().length()>0){

                editText.setText(questionAndAnswerModel.getChoosenAnswer());
            }



            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    questionAndAnswerModel.setChoosenAnswer(s.toString());

                }
            });


            holder.mMutiple_question_option_holder.addView(editText);

        }

        if (questionAndAnswerModel.getQuestionModel().getHint()!= null && questionAndAnswerModel.getQuestionModel().getHint().length()>0){

            holder.mHintLayoutHolder.setVisibility(View.VISIBLE);

        }else {

            holder.mHintLayoutHolder.setVisibility(View.GONE);

        }

        holder.mHintLayoutHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowHintDilogue(questionAndAnswerModel.getQuestionModel().getHint());

            }
        });

        if(questionAndAnswerModel.getQuestionModel().isAttachment_Allowed() ||
                questionAndAnswerModel.getQuestionModel().is_Remark_Allowed()) {
            if (questionAndAnswerModel.getQuestionModel().is_Remark_Allowed()){
                holder.comment.setVisibility(View.VISIBLE);
            }else{
                holder.comment.setVisibility(View.GONE);}
            if(questionAndAnswerModel.getQuestionModel().isAttachment_Allowed()) {
                holder.addattachment.setVisibility(View.VISIBLE);
            }else{
                holder.addattachment.setVisibility(View.GONE);}
            holder.attachmentallowedview.setVisibility(View.VISIBLE);
        }else{
            holder.attachmentallowedview.setVisibility(View.GONE);
        }

        if(questionAndAnswerModel.getQuestionModel().getIs_Task_Enabled() == 1) {
            holder.taskbuttonsLayout.setVisibility(View.VISIBLE);
        }else {
            holder.taskbuttonsLayout.setVisibility(View.GONE);
        }

        if(questionAndAnswerModel.getAttachmentURL() != null){
            holder.uploadedfile.setVisibility(View.VISIBLE);
            holder.attachmentallowedview.setVisibility(View.VISIBLE);
            holder.remove.setVisibility(View.VISIBLE);
            holder.addattachment.setVisibility(View.GONE);
            if(questionAndAnswerModel.getAttachmentURL().endsWith(".pdf")){
                holder.uploadedfile.setImageResource(R.drawable.smallicon_pdf);
            }else if(questionAndAnswerModel.getAttachmentURL().endsWith(".mp4") ||
                    questionAndAnswerModel.getAttachmentURL().endsWith(".3gp")){
                holder.uploadedfile.setImageResource(R.drawable.smallicon_mp4);
            }else {
                holder.uploadedfile.setImageResource(R.drawable.icon_jpeg);
            }
        }else{
            if(questionAndAnswerModel.getQuestionModel().isAttachment_Allowed()){
                holder.addattachment.setVisibility(View.VISIBLE);
                holder.attachmentallowedview.setVisibility(View.VISIBLE);
            }
            holder.uploadedfile.setVisibility(View.GONE);
            holder.remove.setVisibility(View.GONE);
        }

        holder.addattachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageDialog.showPhotoDialog(mContext,new View.OnClickListener() {
                    @Override
                    public void onClick(View Approve) {
                        //logoutProcess();
                        messageDialog.dialogDismiss();
                        questionActivity.takePhoto(questionAndAnswerModel,position);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View close) {
                        messageDialog.dialogDismiss();
                        questionActivity.browsePhoto(questionAndAnswerModel,position);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View close) {
                        messageDialog.dialogDismiss();
                        questionActivity.browsePhoto(questionAndAnswerModel,position);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View close) {
                        messageDialog.dialogDismiss();
                        questionActivity.TakeVideoIntent(questionAndAnswerModel,position);
                    }
                }, true,true,true,true,false);
            }
        });

        holder.uploadedfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadBasedonURL(questionAndAnswerModel);
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionAndAnswerModel.setAttachmentURL(null);
                notifyItemChanged(position);
            }
        });

        holder.addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionActivity.gotoCreateTaskActivity(questionAndAnswerModel);
            }
        });

        holder.view_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionActivity.gotoChecklistViewTaskListActivity(questionAndAnswerModel);
            }
        });

    }

    public void loadBasedonURL(QuestionAndAnswerModel questionAndAnswerModel){
        if(questionAndAnswerModel.getAttachmentURL().endsWith(".pdf")){
            loadPDFView(questionAndAnswerModel);
        }else if(questionAndAnswerModel.getAttachmentURL().endsWith(".mp4")||
                questionAndAnswerModel.getAttachmentURL().endsWith(".3gp")){
            loadVideoView(questionAndAnswerModel);
        }else{
            loadImageView(questionAndAnswerModel);
        }
    }

    public void loadImageView(final QuestionAndAnswerModel questionAndAnswerModel){
        messageDialog.showAttachmentpopupDialog(mContext,questionAndAnswerModel.getAttachmentURL()
                ,new View.OnClickListener() {
                    @Override
                    public void onClick(View Approve) {
                        //logoutProcess();
                        messageDialog.dialogDismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View close) {
                        messageDialog.dialogDismiss();
                        questionAndAnswerModel.setAttachmentURL(null);
                        notifyDataSetChanged();
                    }
                },true);
    }

    public void loadPDFView(final QuestionAndAnswerModel questionAndAnswerModel){
        questionActivity.loadnewPDFView(questionAndAnswerModel.getAttachmentURL());
    }

    public void loadVideoView(final QuestionAndAnswerModel questionAndAnswerModel){
        questionActivity.initializePlayer(questionAndAnswerModel.getAttachmentURL());
    }


    @Override
    public int getItemCount() {
        return questionAndAnswerModels.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class MyMultiPleQuestionViewHolder extends RecyclerView.ViewHolder{

        private TextView mQuestion_text,mDescriptionTextQuestion,poolresult;
        private LinearLayout mMutiple_question_option_holder,mHintLayoutHolder;
        private ImageView mQuestionImageview;
        private EditText comment;
        private ImageView addattachment,uploadedfile,remove,addtask,view_task;
        private TextView min_value,max_value;
        private View min_max_text,attachmentallowedview,taskbuttonsLayout;
        public MyMultiPleQuestionViewHolder(View itemView) {
            super(itemView);

            mQuestion_text = (TextView) itemView.findViewById(R.id.question_text);
            mMutiple_question_option_holder = (LinearLayout) itemView.findViewById(R.id.mutilple_question_option_holder);
            mHintLayoutHolder = (LinearLayout) itemView.findViewById(R.id.hint_layout_holder);
            mQuestionImageview = (ImageView) itemView.findViewById(R.id.question_image);
            mDescriptionTextQuestion = (TextView) itemView.findViewById(R.id.question_description_text);
            comment = (EditText) itemView.findViewById(R.id.comment);
            addattachment = (ImageView) itemView.findViewById(R.id.addattachment);
            remove = (ImageView) itemView.findViewById(R.id.remove);
            uploadedfile = (ImageView) itemView.findViewById(R.id.attachmentfile);
            poolresult= (TextView)itemView.findViewById(R.id.poolresult);
            min_value = (TextView) itemView.findViewById(R.id.min_value);
            max_value = (TextView) itemView.findViewById(R.id.max_value);
            min_max_text = itemView.findViewById(R.id.min_max_text);
            addtask=(ImageView) itemView.findViewById(R.id.addtask);
            view_task=(ImageView) itemView.findViewById(R.id.view_task_link);
            attachmentallowedview = itemView.findViewById(R.id.attachmentallowedview);
            taskbuttonsLayout = itemView.findViewById(R.id.taskbuttonsLayout);


        }

    }


    private void ShowHintDilogue(String message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(mContext.getString(R.string.ok),
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

