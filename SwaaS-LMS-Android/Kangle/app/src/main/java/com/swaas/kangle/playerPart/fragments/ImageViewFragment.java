package com.swaas.kangle.playerPart.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.swaas.kangle.R;
import com.swaas.kangle.playerPart.AssetPlayerActivity;
import com.swaas.kangle.playerPart.customviews.image.OnSingleTapImage;
import com.swaas.kangle.playerPart.customviews.image.TouchImageView;
import com.swaas.kangle.utils.NetworkUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hariharan on 23/5/17.
 */

public class ImageViewFragment extends Fragment implements OnSingleTapImage, View.OnClickListener {

    private String mImageUrl;
    private Context mContext;
    private AssetPlayerActivity assetPlayerActivity;
    private  CountDownTimer waitTimer;
    private boolean isVisible = false;
    private int CurrentAssetPosition;
    CountDownTimer countDownTimer;
    TouchImageView imageView;
    private boolean ImageLoadedAlready = false;
    private RelativeLayout mMediaControllerHolder;
    private ProgressDialog pDialog;
    private int CurrentSessionId;
    private int playmode;
    private boolean isWentBackground = false;
    private ImageView mPrevious,mNext;
    private boolean imageStartDateCaptured = false;
    private Date ImageStartDate,mPlayerStartDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mImageUrl = bundle.getString("url");
        CurrentAssetPosition = bundle.getInt("Index");
        playmode =  bundle.getInt("playmode");
        assetPlayerActivity = (AssetPlayerActivity)getActivity();
      }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            isVisible = true;


            if (ImageLoadedAlready){

                assetPlayerActivity.UpdateImageGuideCompleted();

            }

            Log.d("===>actualtime",""+Calendar.getInstance().getTime());
            waitTimer = new CountDownTimer(1000, 300) {

                public void onTick(long millisUntilFinished) {
                    //called every 300 milliseconds, which could be used to
                    //send messages or some other action
                }
                public void onFinish() {

                    //After 6000 milliseconds (6 sec) finish current
                    //if you would like to execute something when time finishes
                    Calendar test=Calendar.getInstance();
                    test.add(Calendar.SECOND,-1);
                    Log.d("===>playerStartdate",""+test.getTime());
                    mPlayerStartDate = test.getTime();
                    assetPlayerActivity.InsertImageDetailStartTime(CurrentAssetPosition,mPlayerStartDate,0);
                    if (ImageStartDate!=null){
                        assetPlayerActivity.UpdateImagePlayerStarttime(CurrentAssetPosition,ImageStartDate,playmode);
                    }else {
                        ImageStartDate = test.getTime();
                        assetPlayerActivity.UpdateImagePlayerStarttime(CurrentAssetPosition,test.getTime(),playmode);

                    }

                }
            }.start();


            if (playmode!=0){

                if (!NetworkUtils.isNetworkAvailable()){
                    if (pDialog!=null){
                        pDialog.dismiss();
                    }
                    if (assetPlayerActivity!=null)
                          assetPlayerActivity.showAlerDialogue();
                        //assetPlayerActivity.showErrorDialogue(getString(R.string.no_network));
                }

            }
            Log.d("image","visible");

        }
        else {

            isVisible = false;
            isWentBackground = false;
            if(waitTimer != null) {
                Log.e("timer","notnull");
                waitTimer.cancel();
                waitTimer = null;
            }

            if (pDialog!=null){
                pDialog.dismiss();
            }
            if (assetPlayerActivity!=null){

                assetPlayerActivity.HideActionBarControll();
            }
            if (mMediaControllerHolder!=null)
                mMediaControllerHolder.setVisibility(View.GONE);

            Log.d("image","notvisible");

            if (ImageStartDate !=null){
                Log.d("===>ImgEnddate",Calendar.getInstance().getTime()+"");
                assetPlayerActivity.UpdateImagePlayerEndtime(CurrentAssetPosition,Calendar.getInstance().getTime());
            }
            if (mPlayerStartDate!=null){
                Log.d("===>playerEnddate",Calendar.getInstance().getTime()+"");
                assetPlayerActivity.UpdateImageDetailEndTime(CurrentAssetPosition,Calendar.getInstance().getTime());
            }

            ImageStartDate = null;

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("Image","pause");

    }

    @Override
    public void onStop() {
        super.onStop();
        isWentBackground  = true;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.e("Image","onCreateView");
        mContext =  getActivity().getApplicationContext();
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_image_view,container,false);
        imageView = (TouchImageView) view.findViewById(R.id.iv_imagecontent);
        mPrevious = (ImageView) view.findViewById(R.id.img_previous);
        mNext = (ImageView) view.findViewById(R.id.img_next);
        mMediaControllerHolder = (RelativeLayout) view.findViewById(R.id.media_controller_holder);
        setOnClickListener();
        imageView.setSingleTapOnImageListener(this);
        loadImageUrl(imageView,playmode);
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        isWentBackground = false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            setUpLandscape();

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

            setPortrait();

        }
    }

    private void setUpLandscape(){

        mPrevious.setImageResource(R.mipmap.ic_previous_land);
        mNext.setImageResource(R.mipmap.ic_next_land);

    }


    private void setPortrait() {

        mNext.setImageResource(R.mipmap.ic_next);
        mPrevious.setImageResource(R.mipmap.ic_previous);

    }

    private void setOnClickListener() {

        mPrevious.setOnClickListener(this);
        mNext.setOnClickListener(this);

    }

    private void showProgressDialogue(String message) {

        pDialog = new ProgressDialog(assetPlayerActivity);
        // Set progressbar title
        pDialog.setTitle(mContext.getResources().getString(R.string.app_name));
        // Set progressbar message
        pDialog.setMessage(message);
        pDialog.setIndeterminate(false);

        // Show progressbar
        pDialog.show();

    }

    private void loadImageUrl(TouchImageView imageView, final int playmode) {

        if (isVisible){
            showProgressDialogue(getString(R.string.please_wait_progress));
        }
        if (playmode == 0 ){
            Ion.with(this).load(mImageUrl).intoImageView(imageView).setCallback(new FutureCallback<ImageView>() {
                @Override
                public void onCompleted(Exception e, ImageView result) {
                    Log.d("image","loaded");

                    if (pDialog!=null){
                        pDialog.dismiss();
                    }

                    if (isVisible){
                        assetPlayerActivity.UpdateImageGuideCompleted();
                    } else {

                        ImageLoadedAlready = true;
                    }

                    waitTimer = new CountDownTimer(1000, 300) {

                        public void onTick(long millisUntilFinished) {
                            //called every 300 milliseconds, which could be used to
                            //send messages or some other action
                        }
                        public void onFinish() {

                            //After 6000 milliseconds (6 sec) finish current
                            //if you would like to execute something when time finishes
                            Calendar test=Calendar.getInstance();
                            test.add(Calendar.SECOND,-3);
                            ImageStartDate = test.getTime();
                            if (ImageStartDate!=null){
                                Log.d("===>ImageStartDate",ImageStartDate+"");
                            }

                            if (imageStartDateCaptured){

                                assetPlayerActivity.UpdateImagePlayerStarttime(CurrentAssetPosition,ImageStartDate,playmode);

                            }

                        }
                    }.start();
                }
            });
        }else {

            if (NetworkUtils.isNetworkAvailable()){

                Ion.with(this).load(mImageUrl).intoImageView(imageView).setCallback(new FutureCallback<ImageView>() {
                    @Override
                    public void onCompleted(Exception e, ImageView result) {
                        Log.d("image","loaded");
                        if (pDialog!=null){
                            pDialog.dismiss();
                        }


                        if (isVisible){
                            assetPlayerActivity.UpdateImageGuideCompleted();
                        } else {

                            ImageLoadedAlready = true;
                        }

                        waitTimer = new CountDownTimer(1000, 300) {

                            public void onTick(long millisUntilFinished) {
                                //called every 300 milliseconds, which could be used to
                                //send messages or some other action
                            }
                            public void onFinish() {

                                //After 3000 milliseconds (3 sec) finish current
                                //if you would like to execute something when time finishes
                                Calendar test=Calendar.getInstance();
                                test.add(Calendar.SECOND,-1);
                                ImageStartDate = test.getTime();
                                if (ImageStartDate!=null){
                                    Log.d("===>ImageStartDate",ImageStartDate+"");
                                }

                                if (imageStartDateCaptured){

                                    assetPlayerActivity.UpdateImagePlayerStarttime(CurrentAssetPosition,ImageStartDate,playmode);

                                }
                            }
                        }.start();
                    }
                });

            }else {
                if (waitTimer!=null){
                    waitTimer.cancel();
                }
                if (pDialog!=null){
                    pDialog.dismiss();
                }
                if (playmode!=0){

                    if (isVisible){

                          assetPlayerActivity.showAlerDialogue();
                        //assetPlayerActivity.showErrorDialogue(getString(R.string.no_network));

                    }

                }
            }

        }
        Log.d("imageurl",mImageUrl);

    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("detach","detach");
    }



    @Override
    public void onResume() {
        super.onResume();
        Log.e("Image","onResume");

        if (isVisible){

            if (isWentBackground){

                imageStartDateCaptured = true;
                if (mPlayerStartDate!=null){
                    assetPlayerActivity.InsertImageDetailStartTime(CurrentAssetPosition,mPlayerStartDate,CurrentSessionId);

                }else {

                    mPlayerStartDate = Calendar.getInstance().getTime();
                    assetPlayerActivity.InsertImageDetailStartTime(CurrentAssetPosition,mPlayerStartDate,CurrentSessionId);

                }
                loadImageUrl(imageView,playmode);
                isWentBackground = false;
            }
        }

        if (assetPlayerActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

            setUpLandscape();

        }else {

            setPortrait();

        }


    }


    @Override
    public void OnSingleTap() {
        Log.d("img","singleTapConfirm");

        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(3000,300) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if (mMediaControllerHolder.getVisibility()==View.VISIBLE) {
                    mMediaControllerHolder.setVisibility(View.GONE);
                }
                assetPlayerActivity.HideActionBarControll();
            }
        }.start();

        assetPlayerActivity.ShowActionBarControll();

        if (mMediaControllerHolder.getVisibility() == View.VISIBLE) {
            mMediaControllerHolder.setVisibility(View.GONE);

        } else {

            mMediaControllerHolder.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public void OnTwoFingerDoubleTap() {


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){


            case R.id.img_previous:

                assetPlayerActivity.AssetPlayBackPositionChange(CurrentAssetPosition-1, false);

                break;

            case R.id.img_next:

                assetPlayerActivity.AssetPlayBackPositionChange(CurrentAssetPosition+1, false);

                break;

            default:

                break;

        }

    }
}
