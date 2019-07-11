package com.swaas.kangle.DigitalAssetPlayer.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swaas.kangle.DigitalAssetPlayer.DigitalAssetPlayerActivity;
import com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.PDFView;
import com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.listener.OnLoadCompleteListener;
import com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.listener.OnPageChangeListener;
import com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.listener.OnSingleTapTouchListener;
import com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.listener.PdfSwipeUpDownListener;
import com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.util.Constants;
import com.swaas.kangle.DigitalAssetPlayer.pdfasync.DownloadPdfAysnc;
import com.swaas.kangle.DigitalAssetPlayer.pdfasync.OnPdfDownload;
import com.swaas.kangle.R;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.NetworkUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hariharan on 24/5/17.
 */

public class PdfViewFragment extends Fragment implements OnPdfDownload, OnPageChangeListener, OnLoadCompleteListener, OnSingleTapTouchListener, View.OnClickListener, PdfSwipeUpDownListener {

    private String mAssetUrl;
    private Context mContext;
    private CountDownTimer waitTimer,Page_holderTimer,Page_holderTimer_Countinues;
    private PDFView mPdfview;
    private int DAname;
    public boolean isWentFromBackground = false;
    private ImageView mPrevious, mNext;
    private String filename;
    private String PdfPath;
    public boolean onLoadComplete = false;
    private LinearLayout mMenuHolder;
    private TextView mSinglePageChanger,mContinousPageCahnger;
    private CountDownTimer countDownTimer;
    private int CurrentAssetPosition;
    public boolean isSinglePageTap;
    private int playmode;
    public boolean loadpdfOnlineinitial = false;
    private TextView mProgressText;
    public LinearLayout mPageHolder;
    private TextView mCurrentpagenumber, mTotalPAgenumber;
    private RelativeLayout mMediaControllerHolder;
    private DigitalAssetPlayerActivity assetPlayerActivity;
    public boolean isVisible=false;
    public boolean alreadyDownloaded = false,AlreadyOffline = false;
    public int alreadyloadedpagenumber = -1,PreviousPageNumber = -1, CurrentPageNumber = -1, TotalPageNumber = -1;
    private String mOnlineUrl,mOfflineUrl;
    private Date DetailedStartTime , PageStartTime;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext =  getActivity().getApplicationContext();
        assetPlayerActivity =  (DigitalAssetPlayerActivity)getActivity();
        Bundle bundle = getArguments();
        DownloadPdfAysnc downloadPdfAysnc =  new DownloadPdfAysnc(mContext,this);
        if (bundle!=null){
            DAname =  bundle.getInt("filename");
            filename = ""+DAname;
            playmode =  bundle.getInt("playmode");
            CurrentAssetPosition = bundle.getInt("Index");
            if (playmode == 0){
                mOfflineUrl = bundle.getString("url");
            }else {
                mOnlineUrl = bundle.getString("url");
                if (NetworkUtils.isNetworkAvailable()){
                    downloadPdfAysnc.execute(mOnlineUrl,filename);
                }else {

                    if (isVisible){
                        if (mProgressText!=null){
                            mProgressText.setText("Check your internet connection");
                        }
                        assetPlayerActivity.showAlerDialogue();
                        //assetPlayerActivity.showErrorDialogue(getString(R.string.no_network));
                    }
                    if (waitTimer!=null){
                        waitTimer.cancel();
                    }
                }

            }

        }
    }

    @Override
    public void onStop() {
        super.onStop();

        isWentFromBackground = true;


    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        isWentFromBackground = false;
    }

    private void LoadOfflinePdf() {

        if (mOfflineUrl!=null && mOfflineUrl.length() > 0){

            PdfPath = mOfflineUrl;

            mPdfview.setListenerForSinglePage(this);
            mPdfview.fromUri(Uri.parse("file:///"+mOfflineUrl)).defaultPage(0)
                    .onPageChange(this)
                    .enableAnnotationRendering(true)
                    .onLoad(this)
                    //.enableSingletap(this)
                    .load();
            loadpdfOnlineinitial = true;
            //mPdfview.fitToWidth();

        }else {

            if (mMediaControllerHolder!=null)
                mMediaControllerHolder.setVisibility(View.GONE);

            if (assetPlayerActivity!=null){
                assetPlayerActivity.HideActionBarControll();
            }
            if (waitTimer!=null)
                waitTimer.cancel();
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){

            if (playmode!=0){

                if (!NetworkUtils.isNetworkAvailable()){

                    if (assetPlayerActivity!=null){

                        assetPlayerActivity.showAlerDialogue();
                        //assetPlayerActivity.showErrorDialogue(getString(R.string.no_network));
                        if (mProgressText!=null){
                            mProgressText.setText("Check your internet connection");
                        }
                    }

                }

            }

            isVisible = true;
            Log.d("Pdf","visible");
            if (alreadyloadedpagenumber !=-1){
                assetPlayerActivity.digitalAssetsList.get(CurrentAssetPosition).setIncreasePDFSessionId(true);
                setTimer(alreadyloadedpagenumber);
                Log.d("=>already",alreadyloadedpagenumber+"");
                assetPlayerActivity.UpdatePdfLoaded();

            }
        }
        else {
            isVisible = false;

            if (assetPlayerActivity!=null){
                assetPlayerActivity.HideSettingsButton();
                assetPlayerActivity.hideActionBar();

            }

            Log.d("Pdf","notvisible");
            if (waitTimer!=null){
                waitTimer.cancel();
                waitTimer = null;
            }

            if (countDownTimer != null){
                countDownTimer.cancel();
            }

            if (PageStartTime !=null){
                assetPlayerActivity.UpdatePageEndTime(CurrentAssetPosition,Calendar.getInstance().getTime());
            }

            if (DetailedStartTime !=null){
                assetPlayerActivity.InsertPdfDetailEndTime(CurrentAssetPosition,Calendar.getInstance().getTime());
            }
            //Query for delete the below 2 seconds data
            //assetPlayerActivity.DeleteLessThanTwoSeconds(CurrentAssetPosition);

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.e("Pdf","onCreateView");
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_asset_pdf_viewer,container,false);
        mPdfview = (PDFView) view.findViewById(R.id.asset_pdf_player);
        mPrevious = (ImageView) view.findViewById(R.id.pdf_previous);
        mSinglePageChanger = (TextView) view.findViewById(R.id.singlepageChanger);
        mContinousPageCahnger = (TextView) view.findViewById(R.id.ContinousPageChanger);
        mCurrentpagenumber = (TextView) view.findViewById(R.id.currentpage_number);
        mTotalPAgenumber = (TextView) view.findViewById(R.id.total_pagenumber);
        mPageHolder = (LinearLayout) view.findViewById(R.id.page_holder);
        mProgressText = (TextView) view.findViewById(R.id.progress_text);
        mMenuHolder = (LinearLayout) view.findViewById(R.id.menu_holder);
        mMenuHolder.setVisibility(View.GONE);
        mMediaControllerHolder = (RelativeLayout)view.findViewById(R.id.media_controller_holder);
        mNext = (ImageView) view.findViewById(R.id.pdf_next);
        setOnclickListeners();
        mPdfview.useBestQuality(false);
        return view;
    }


    private void setOnclickListeners() {

        mPrevious.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mSinglePageChanger.setOnClickListener(this);
        mContinousPageCahnger.setOnClickListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();

        if (isVisible){

            DetailedStartTime = Calendar.getInstance().getTime();
            assetPlayerActivity.InsertPdfDetailStartTime(CurrentAssetPosition,DetailedStartTime);

            if (isWentFromBackground){

                if (playmode == Constants.ONLINE){

                    DownloadPdfAysnc downloadPdfAysnc =  new DownloadPdfAysnc(mContext,this);
                    if (NetworkUtils.isNetworkAvailable()){
                        downloadPdfAysnc.execute(mOnlineUrl,filename);
                    }else {

                        if (isVisible){
                            if (mProgressText!=null){
                                mProgressText.setText("Check your internet connection");
                            }
                            assetPlayerActivity.showAlerDialogue();
                            //assetPlayerActivity.showErrorDialogue(getString(R.string.no_network));
                        }

                    }

                }

            }

        }


        if (assetPlayerActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

            setUpLandscape();

        }else {

            setPortrait();

        }


        Log.e("Pdf","onResume");
        LoadOfflinePdf();


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

    @Override
    public void onPause() {
        super.onPause();
        Log.e("Pdf","pause");


    }



    @Override
    public void onPdfDownloaded(String pdfpath) {

        if (pdfpath!=null){
            PdfPath = pdfpath;
            LoadOnlinePdf();

        }else {

            if (assetPlayerActivity!=null){
                assetPlayerActivity.showAlerDialogue();
                //assetPlayerActivity.showErrorDialogue(getString(R.string.no_network));
                if (mProgressText != null){
                    mProgressText.setText("Check your internet connection");
                }
            }
        }

    }

    private void LoadOnlinePdf() {

        mPdfview.setListenerForSinglePage(this);
        mPdfview.fromFile(new File(PdfPath)).defaultPage(0)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .load();
        loadpdfOnlineinitial = true;


    }

    @Override
    public void onPageChanged(int page, int pageCount) {

        Log.d("=>checkingsingle", "" + page);

        if (isVisible) {

            if (PreviousPageNumber == -1 && alreadyloadedpagenumber == -1) {
                setTimer(page);
                Log.d("=>test", "" + page);
                PreviousPageNumber = page;
            } else {
                if (PageStartTime != null) {
                    Log.d("=>previous", "" + PreviousPageNumber);
                    assetPlayerActivity.UpdatePageEndTime(CurrentAssetPosition, Calendar.getInstance().getTime());
                }
                PageStartTime = Calendar.getInstance().getTime();
                assetPlayerActivity.UpdateMultiPageStartTime(CurrentAssetPosition, PageStartTime, page + 1, playmode);
                PreviousPageNumber = page;
                Log.d("=>currentpage", page + "");

            }
            alreadyloadedpagenumber = page;

        } else {
            alreadyloadedpagenumber = page;
        }

        if (!isSinglePageTap) {
            int pagenumber = page + 1;
            mCurrentpagenumber.setText(pagenumber + "");
            mTotalPAgenumber.setText(pageCount + "");

        }
    }


    private void showNextAssetAlert() {

        new CountDownTimer(2000, 300){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                assetPlayerActivity.ShowAlert(getString(R.string.willingtonextasset));

            }
        }.start();

    }

    @Override
    public void loadComplete(int nbPages) {

        mPdfview.setVisibility(View.VISIBLE);
        mProgressText.setVisibility(View.GONE);
        Log.e("=>pdf","onloadcomplete"+nbPages);
        mPdfview.enableSingletap(this);
        if (isSinglePageTap){

            if (Page_holderTimer != null){
                Page_holderTimer.cancel();
            }
            mPageHolder.setVisibility(View.VISIBLE);
            mCurrentpagenumber.setText(CurrentPageNumber+"");
            mTotalPAgenumber.setText(TotalPageNumber+"");

            Page_holderTimer = new CountDownTimer(2000, 300) {
                public void onTick(long millisUntilFinished) {
                    //called every 300 milliseconds, which could be used to
                    //send messages or some other action
                }
                public void onFinish() {
                    mPageHolder.setVisibility(View.GONE);

                }
            }.start();
        }

        if (loadpdfOnlineinitial){
            singlePageShow();
            loadpdfOnlineinitial =false;
        }else {

            if (isVisible){

                if (isSinglePageTap){

                    if (CurrentPageNumber<2){
                        assetPlayerActivity.UpdatePdfLoaded();
                    }


                }
            }
        }

    }

    private void setTimer(final int page) {

        waitTimer = new CountDownTimer(1000, 300) {
            public void onTick(long millisUntilFinished) {
                //called every 300 milliseconds, which could be used to
                //send messages or some other action
            }
            public void onFinish() {

                Calendar calendarfinish =Calendar.getInstance();
                calendarfinish.add(Calendar.SECOND,-1);
                DetailedStartTime = calendarfinish.getTime();
                PageStartTime = calendarfinish.getTime();
                assetPlayerActivity.InsertPdfDetailStartTime(CurrentAssetPosition,DetailedStartTime);
                assetPlayerActivity.UpdatePageStartTime(CurrentAssetPosition,PageStartTime,page+1,playmode);
                assetPlayerActivity.digitalAssetsList.get(CurrentAssetPosition).setIncreasePDFSessionId(false);
            }
        }.start();
    }


    @Override
    public void onSingletapTouch() {

        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(6000,300) {
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
            if (isVisible){
                assetPlayerActivity.ShowSettingsButton();
            }

        }
    }

    @Override
    public void onTwoFingerDoubleTouch() {
        if (PreferenceUtils.getIsGestureEnabled(mContext)){

            assetPlayerActivity.ShowGestureLayerHolder(CurrentAssetPosition);

        }
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


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.pdf_previous:

                assetPlayerActivity.AssetPlayBackPositionChange(CurrentAssetPosition-1, false);

                break;

            case R.id.pdf_next:

                assetPlayerActivity.AssetPlayBackPositionChange(CurrentAssetPosition+1, false);

                break;

            case R.id.singlepageChanger:

                break;

            case R.id.ContinousPageChanger:

                break;

            default:

                break;

        }

    }

    private void UpdatePage(String onlinePdfPath, int currentPageNumber, int totalPageNumber) {

        mPdfview.fromFile(new File(onlinePdfPath)).defaultPage(currentPageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .pages(currentPageNumber)
                .load();
        onPageChanged(currentPageNumber,totalPageNumber);
        CurrentPageNumber = currentPageNumber+1;

    }

    @Override
    public void onSwipeUp() {

        MovetoNextPage();

    }
    @Override
    public void onSwipeDown() {

        MovetoPreviousPage();

    }

    @Override
    public void onScroll() {

        if (Page_holderTimer_Countinues!=null){
            Page_holderTimer_Countinues.cancel();
        }
        assetPlayerActivity.showStatusBar();
        if (!isSinglePageTap){
            mPageHolder.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onScrollEnding() {

        Page_holderTimer_Countinues = new CountDownTimer(2000, 300) {
            public void onTick(long millisUntilFinished) {
                //called every 300 milliseconds, which could be used to
                //send messages or some other action
            }
            public void onFinish() {
                mPageHolder.setVisibility(View.GONE);

            }
        }.start();

    }



    private void MovetoNextPage() {

        if (isSinglePageTap){

            if ((CurrentPageNumber)<TotalPageNumber){
                UpdatePage(PdfPath,CurrentPageNumber,TotalPageNumber);
            }

        }

    }

    private void MovetoPreviousPage() {

        if (isSinglePageTap){

            if (CurrentPageNumber-2 >= 0) {
                UpdatePage(PdfPath,CurrentPageNumber-2,TotalPageNumber);

            }

        }


    }


    public void showMenu(int itemId) {

        switch (itemId){

            case R.id.singlepage:

                singlePageShow();

                break;

            case R.id.continuespage:

                loadpdfOnlineinitial = false;
                mMenuHolder.setVisibility(View.GONE);
                isSinglePageTap = false;
                mPageHolder.setVisibility(View.GONE);
                mPdfview.setListenerForSinglePage(this);
                mPdfview.fromFile(new File(PdfPath)).defaultPage(CurrentPageNumber)
                        .onPageChange(this)
                        .enableAnnotationRendering(true)
                        .onLoad(this)
                        .load();



                break;

            default:

                break;

        }

    }

    private void singlePageShow() {

        mMenuHolder.setVisibility(View.GONE);
        isSinglePageTap = true;
        if (CurrentPageNumber == -1){
            CurrentPageNumber = mPdfview.getCurrentPage();
        }
        if (TotalPageNumber == -1){

            TotalPageNumber = mPdfview.getPageCount();
        }
        mPdfview.setListenerForSinglePage(this);
        UpdatePage(PdfPath,CurrentPageNumber,TotalPageNumber);


    }


}

