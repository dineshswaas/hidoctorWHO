package com.swaas.kangle.playerPart.customviews.audio;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.swaas.kangle.playerPart.customviews.image.OnSingleTapImage;


/**
 * Created by Hariharan on 22/6/17.
 */

public class MySingleTapRelativeLayout extends RelativeLayout {

    public GestureDetector gestureDetector;
    public OnSingleTapImage onSingleTapImage;

    public MySingleTapRelativeLayout(Context context) {
        super(context);

        gestureDetector = new GestureDetector(context, new GestureListener());

    }

    public MySingleTapRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        gestureDetector = new GestureDetector(context, new GestureListener());

    }

    public MySingleTapRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gestureDetector = new GestureDetector(context, new GestureListener());


    }

    public void setOnSingleTapListener(OnSingleTapImage onSingleTapImage){
        this.onSingleTapImage = onSingleTapImage;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            if (onSingleTapImage!=null)
                 onSingleTapImage.OnSingleTap();

            return true;
        }


    }

}
