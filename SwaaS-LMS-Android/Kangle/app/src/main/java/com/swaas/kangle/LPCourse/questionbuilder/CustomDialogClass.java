package com.swaas.kangle.LPCourse.questionbuilder;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.swaas.kangle.R;
import com.swaas.kangle.playerPart.customviews.image.TouchImageView;

/**
 * Created by Hariharan on 5/9/17.
 */

public class CustomDialogClass extends Dialog implements
        View.OnClickListener {

    public Activity mActivity;
    public TouchImageView mImageView;
    public TextView mBtnClose;
    private  String imageUrl;

    public CustomDialogClass(Activity mActivity , String imageUrl) {
        super(mActivity);
        // TODO Auto-generated constructor stub
        this.mActivity = mActivity;
        this.imageUrl = imageUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_dailogue_fragment);
        mBtnClose = (TextView) findViewById(R.id.btn_close);
        mImageView = (TouchImageView) findViewById(R.id.question_image);
        mBtnClose.setOnClickListener(this);
        Ion.with(getContext()).load(imageUrl).intoImageView(mImageView);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_close:

                 dismiss();

                break;

            default:

                break;
        }

    }
}