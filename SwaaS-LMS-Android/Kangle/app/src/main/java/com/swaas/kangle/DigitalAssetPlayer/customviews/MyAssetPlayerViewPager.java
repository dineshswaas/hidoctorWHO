package com.swaas.kangle.DigitalAssetPlayer.customviews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Hariharan on 31/5/17.
 */

public class MyAssetPlayerViewPager extends ViewPager {

    private Context mContext;

    public MyAssetPlayerViewPager(Context context) {
        super(context);
        mContext = context;
    }

    public MyAssetPlayerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

    }

    float mStartDragX;
    OnSwipeOutListener mListener;


    public void setOnSwipeOutListener(OnSwipeOutListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartDragX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mStartDragX < x && getCurrentItem() == 0) {
                    mListener.onSwipeOutAtStart();

                } else if (mStartDragX > x && getCurrentItem() == getAdapter().getCount() - 1) {
                    mListener.onSwipeOutAtEnd();
                }else {
                    mListener.onHideToast();

                }
                return super.onInterceptTouchEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    public interface OnSwipeOutListener {
        public void onSwipeOutAtStart();
        public void onSwipeOutAtEnd();
        public void onHideToast();
    }


}
