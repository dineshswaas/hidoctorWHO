package com.swaas.kangle.LPCourse.questionbuilder.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.swaas.kangle.LPCourse.model.QuestionAndAnswerModel;
import com.swaas.kangle.LPCourse.questionbuilder.fragment.EssayTypeFragment;
import com.swaas.kangle.LPCourse.questionbuilder.fragment.MatchTheFollowingFragment;
import com.swaas.kangle.LPCourse.questionbuilder.fragment.OptionQuestionFragment;
import com.swaas.kangle.LPCourse.questionbuilder.fragment.SingleChoiceButtonTypeQuestionFragment;
import com.swaas.kangle.LPCourse.questionbuilder.fragment.SingleOptionQuestionFragment;
import com.swaas.kangle.LPCourse.questionbuilder.fragment.TextOptionQuestionFragment;
import com.swaas.kangle.LPCourse.questionbuilder.fragment.YesNoNaTypeQuestionFragment;
import com.swaas.kangle.playerPart.customviews.pdf.util.Constants;


import java.util.ArrayList;

/**
 * Created by Hariharan on 16/8/17.
 */

public class QuestionPageSlider extends FragmentPagerAdapter {

    public ArrayList<QuestionAndAnswerModel> questionAndAnswerModels;

    public QuestionPageSlider(FragmentManager fm, ArrayList<QuestionAndAnswerModel> questionAndAnswerModels) {
        super(fm);
        this.questionAndAnswerModels = questionAndAnswerModels;

    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.POSITION,position);
        QuestionAndAnswerModel questionAndAnswerModel = questionAndAnswerModels.get(position);

        if (questionAndAnswerModel.getQuestionModel().getQuestion_Type() == Constants.SINGLE_CHOICE_BUTTON_TYPE){
            fragment = new SingleChoiceButtonTypeQuestionFragment();
            fragment.setArguments(bundle);
            bundle.putSerializable("val",questionAndAnswerModel);

        }else if (questionAndAnswerModel.getQuestionModel().getQuestion_Type() == Constants.YES_NO_NA_TYPE){
            fragment = new YesNoNaTypeQuestionFragment();
            fragment.setArguments(bundle);
            bundle.putSerializable("val",questionAndAnswerModel);

        } else if (questionAndAnswerModel.getQuestionModel().getQuestion_Type() == Constants.RADIO_TYPE){
            fragment = new SingleOptionQuestionFragment();
            fragment.setArguments(bundle);
            bundle.putSerializable("val",questionAndAnswerModel);

        } else if (questionAndAnswerModel.getQuestionModel().getQuestion_Type() == Constants.MULTIPLE_CHOICE_TYPE){

            fragment = new OptionQuestionFragment();
            fragment.setArguments(bundle);
            bundle.putSerializable("val",questionAndAnswerModel);


        }
        else  if (questionAndAnswerModel.getQuestionModel().getQuestion_Type() == Constants.ESSAY_TYPE){
            fragment = new EssayTypeFragment();
            fragment.setArguments(bundle);
            bundle.putSerializable("val",questionAndAnswerModel);
        }
        else  if (questionAndAnswerModel.getQuestionModel().getQuestion_Type() == Constants.MATCH_THE_FOLLOWING){
            fragment = new MatchTheFollowingFragment();
            fragment.setArguments(bundle);
            bundle.putSerializable("val",questionAndAnswerModel);

        } else  {
            fragment = new TextOptionQuestionFragment();
            fragment.setArguments(bundle);
            bundle.putSerializable("val",questionAndAnswerModel);
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return questionAndAnswerModels.size();
    }
}
