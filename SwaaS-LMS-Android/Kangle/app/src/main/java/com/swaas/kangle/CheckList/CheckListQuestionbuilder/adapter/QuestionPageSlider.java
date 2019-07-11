package com.swaas.kangle.CheckList.CheckListQuestionbuilder.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.swaas.kangle.CheckList.model.QuestionAndAnswerModel;
import com.swaas.kangle.CheckList.CheckListQuestionbuilder.fragment.OptionQuestionFragment;
import com.swaas.kangle.CheckList.CheckListQuestionbuilder.fragment.SingleOptionQuestionFragment;
import com.swaas.kangle.CheckList.CheckListQuestionbuilder.fragment.TextOptionQuestionFragment;
import com.swaas.kangle.playerPart.customviews.pdf.util.Constants;

import java.util.ArrayList;

/**
 * Created by Sayellessh on 02-05-2018.
 */

public class QuestionPageSlider extends FragmentStatePagerAdapter {

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

        if (questionAndAnswerModel.getQuestionModel().getQuestion_Type() == 2){
            fragment = new SingleOptionQuestionFragment();
            fragment.setArguments(bundle);
            bundle.putSerializable("val",questionAndAnswerModel);

        } else if (questionAndAnswerModel.getQuestionModel().getQuestion_Type() == 1){

            fragment = new OptionQuestionFragment();
            fragment.setArguments(bundle);
            bundle.putSerializable("val",questionAndAnswerModel);


        }else  {

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
