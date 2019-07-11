package com.swaas.kangle.DigitalAssetPlayer.customviews.video;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by Hariharan on 26/5/17.
 */

public class HiDoctorVideoview extends VideoView {

    public HiDoctorVideoview(Context context) {
        super(context);
    }

    public HiDoctorVideoview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HiDoctorVideoview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


}
