package com.swaas.kangle.DigitalAssetPlayer.customviews.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

import com.swaas.kangle.DigitalAssetPlayer.customviews.SimpleTwoFingerDoubleTapDetector;
import com.swaas.kangle.DigitalAssetPlayer.customviews.image.OnSingleTapImage;

/**
 * Created by Hariharan on 7/7/17.
 */

public class MyCustomWebview extends WebView {

    public OnSingleTapImage onsingletapimg;


    public MyCustomWebview(Context context) {
        super(context);
    }

    public MyCustomWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCustomWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setSingleTapOnWebviewListener(OnSingleTapImage onsingletapimage) {

        this.onsingletapimg = onsingletapimage;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        multiTouchListener.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    SimpleTwoFingerDoubleTapDetector multiTouchListener = new SimpleTwoFingerDoubleTapDetector() {
        @Override
        public void onTwoFingerDoubleTap() {
            // Do what you want here, I used a Toast for demonstration
            onsingletapimg.OnTwoFingerDoubleTap();
        }
    };


}
