package com.swaas.kangle.CheckList.CheckListQuestionbuilder.customviews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Sayellessh on 01-05-2018.
 */

public class QuestionViewPager extends ViewPager {

    public QuestionViewPager(Context context) {
        super(context);
    }

    public QuestionViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return false;
    }
}
