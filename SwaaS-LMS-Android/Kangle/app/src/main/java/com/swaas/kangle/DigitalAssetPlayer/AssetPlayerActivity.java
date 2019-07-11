/*
package com.swaas.kangle.DigitalAssetPlayer;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.swaas.kangle.DigitalAssetPlayer.adapter.PageSlideAdapter;
import com.swaas.kangle.DigitalAssetPlayer.customviews.MyAssetPlayerViewPager;
import com.swaas.kangle.DigitalAssetPlayer.fragments.ExoAudioPlayerFragment;
import com.swaas.kangle.DigitalAssetPlayer.fragments.ExoPlayerFragment;
import com.swaas.kangle.DigitalAssetPlayer.fragments.ImageViewFragment;
import com.swaas.kangle.DigitalAssetPlayer.fragments.PdfViewFragment;
import com.swaas.kangle.DigitalAssetPlayer.fragments.WebviewFragment;
import com.swaas.kangle.R;
import com.swaas.kangle.db.DigitalAssetRepository;
import com.swaas.kangle.playerPart.DigitalAssets;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.DateHelper;
import com.swaas.kangle.utils.GPSTracker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class AssetPlayerActivity extends AppCompatActivity implements View.OnClickListener,
        OnAssetListPlayerItemClicked, ViewPager.OnPageChangeListener,
        OnAssetChangeListener, MyAssetPlayerViewPager.OnSwipeOutListener, PopupMenu.OnMenuItemClickListener {


    public MyAssetPlayerViewPager AssetPlayerPager;
    public PageSlideAdapter pageSlideAdapter;
    public int CurrentDigigtalAsset;
    public int Endpostion_interept = 10;
    public GPSTracker gpsTracker;
    double latitude, longitude;
    public ContentResolver mContentResolver;
    public Window window;
    private boolean isVideoStarted = false, isVideoNotStarted = false, isPdfViewStarted = false;
    private CountDownTimer mTipGuideTimer;
    public int brightness;
    private DateHelper dateHelper;
    public int isPreview;
    public boolean videocomplete = false;
    public PopupMenu popupMenu;
    public int CurrentGesturePosition;
    //   private ImageView mGestureImage;
    public DigitalAssetRepository mDigitalAssetRepository;
    public List<DigitalAssets> digitalAssetsList, digitalAssetListFeedback;
    protected AlertDialog mAlertDialog;
    public LinearLayout mTransparentView, mAssetListHolder;
    public ImageView mIcDrawerOpenLayout, mShareIconAsset, mAssetSetting, mAssetPlayerBack;
    private TextView mAssetListCrossButton, mTvAutoplayEnable, mAssetName;
    private ImageView mSettings;
    private DateFormat df, df1;
    private Toast mToast;
    private com.github.florent37.viewanimator.ViewAnimator viewAnimator;
    private boolean isToastVisible = false;
    //public AssetListOnPlayerAdapter mAssetListOnPlayerAdapter;
    public RecyclerView mAssetListOnPlayerRecycleview;
    public DigitalAssets mCurrentAssetObject;
    boolean isFromMCMandatory;
    private GestureDetector detector;
    public RelativeLayout mGesture_view_holder;
    private TextView mAssetGotItTipTextview;
    private GestureLibrary gLib;
    public RelativeLayout mActionbarHolder;
    private RelativeLayout mAssetTipLayout;
    public TextView mVolumeTipText, mBrightnessTipText, mSeekbarTipText;
    private TextView mBtn_GestureOverlay_Close, mBtn_GestureOverlay_Closeview;
    boolean isFromStoryAssets;
    TextView previewText;
    ImageView rating1_img, rating2_img, rating3_img, rating4_img, rating5_img,showActionBarImage;
    Handler handler;
    //  Runnable myRunnable;
    int like_rating;
    boolean isFeedbackClicked = false;
    public boolean isForPDFPageBinder = false;
    public boolean isForCheckPermission;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_asset_player_new);
        gLib = GestureLibraries.fromRawResource(this, R.raw.gesture);
        previewText = (TextView) findViewById(R.id.previewText);
        handler = new Handler();
        rating1_img = (ImageView) findViewById(R.id.rating_image1);
        rating2_img = (ImageView) findViewById(R.id.rating_image2);
        rating3_img = (ImageView) findViewById(R.id.rating_image3);
        rating4_img = (ImageView) findViewById(R.id.rating_image4);
        rating5_img = (ImageView) findViewById(R.id.rating_image5);
        showActionBarImage = (ImageView)findViewById(R.id.showActionBarImage);
        showActionBarImage.setVisibility(View.VISIBLE);
        gLib.load();
        getDigitalAssetList();
        SetClickListener();
        PreferenceUtils.setAutoplay(this, false);
        settingPermission();
        PreferenceUtils.setIsGestureEnabled(this, true);

        // showActionBarWithTimer(null);

        //downloadAsset();
      */
/*  GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gesturesview);
        gestures.addOnGesturePerformedListener(handleGestureListener);
        gestures.setGestureStrokeAngleThreshold(90.0f);*//*

        detector = new GestureDetector(this, new GestureTap());



    }

    public void showActionBarWithTimer(View view) {
        showActionBarImage.setVisibility(View.INVISIBLE);
        mActionbarHolder.setVisibility(View.VISIBLE);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(3000, 300) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (mActionbarHolder != null) {
                    mActionbarHolder.setVisibility(View.GONE);
                    showActionBarImage.setVisibility(View.VISIBLE);
                }
                // assetplayeractivity.HideActionBarControll();
            }
        }.start();
    }


    private void UpdateFeedback(DigitalAssets digitalAssets, int likeoption) {

        //    Toast.makeText(getApplicationContext(), String.valueOf(likeoption), Toast.LENGTH_SHORT).show();

        digitalAssetListFeedback = new ArrayList<>();
        DigitalAssets digitalAssetsforfeedback = new DigitalAssets();
        //digitalAssetsforfeedback.setRegion_Code(customer.getRegion_Code());
       // digitalAssetsforfeedback.setCustomer_Code(customer.getCustomer_Code());
       // digitalAssetsforfeedback.setUser_Code(PreferenceUtils.getUserCode(getApplicationContext()));
        digitalAssetsforfeedback.setDA_Code(digitalAssets.getDA_Code());
        digitalAssetsforfeedback.setDA_Type(digitalAssets.getDA_Type());
        digitalAssetsforfeedback.setRating(0);
        digitalAssetsforfeedback.setUser_Like(likeoption);
        digitalAssetsforfeedback.setIs_Synched(0);
        digitalAssetsforfeedback.setSource_Of_Entry(1);
        digitalAssetsforfeedback.setTime_Zone(displayTimeZone(TimeZone.getDefault()));
        digitalAssetsforfeedback.setUpdated_Date(dateHelper.getCurrentDate());
        //   SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());
        digitalAssetsforfeedback.setUpdated_Datetime(currentDate);
        digitalAssetListFeedback.add(digitalAssetsforfeedback);
       // mDigitalAssetRepository.CustomerFeedbackDABulkInsert(digitalAssetListFeedback);

    }

    public void settingPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                showBrightnessSettingAlert();
            }
        } else {

            getBrightness();

        }
    }

    private void showBrightnessSettingAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.brightnessalert));

        //  builder.setTitle(title);

        // Generic error dialogs are used for information only. User only clicks on OK to dismiss it.
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                isForCheckPermission = true;
                startActivityForResult(intent, 200);


            }
        });

        mAlertDialog = builder.create();
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();

    }


    private void getBrightness() {

        mContentResolver = getContentResolver();
        //Get the current window
        window = getWindow();

        try {
            // To handle the auto
            Settings.System.putInt(mContentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            //Get the current system brightness
            brightness = Settings.System.getInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS);

            Log.d("=>brightness", "" + brightness);


        } catch (Settings.SettingNotFoundException e) {
            //Throw an error case it couldn't be retrieved
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }

    }



    private void SetClickListener() {

        mTransparentView.setOnClickListener(this);
        mIcDrawerOpenLayout.setOnClickListener(this);
        mShareIconAsset.setOnClickListener(this);
        mAssetListCrossButton.setOnClickListener(this);
        mAssetSetting.setOnClickListener(this);
        mAssetPlayerBack.setOnClickListener(this);
        mSettings.setOnClickListener(this);
        mShareIconAsset.setOnClickListener(this);
        mBtn_GestureOverlay_Close.setOnClickListener(this);
        mBtn_GestureOverlay_Closeview.setOnClickListener(this);
        mAssetGotItTipTextview.setOnClickListener(this);
        rating1_img.setOnClickListener(this);
        rating2_img.setOnClickListener(this);
        rating3_img.setOnClickListener(this);
        rating4_img.setOnClickListener(this);
        rating5_img.setOnClickListener(this);

        mAssetTipLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                detector.onTouchEvent(event);

                return true;

            }
        });

        //mTvAutoplayEnable.setOnClickListener(this);

    }


    private void ViewInialization() {

        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
        df1 = new SimpleDateFormat("yyyy-MM-dd");
        mDigitalAssetRepository = new DigitalAssetRepository(this);
        mToast = new Toast(this);
        gpsTracker = new GPSTracker(AssetPlayerActivity.this);
        mSettings = (ImageView) findViewById(R.id.asset_setting);
        AssetPlayerPager = (MyAssetPlayerViewPager) findViewById(R.id.asset_slider);
        AssetPlayerPager.setOnSwipeOutListener(this);
        // mTvAutoplayEnable = (TextView) findViewById(R.id.auto_play);
        mTransparentView = (LinearLayout) findViewById(R.id.transparent_view);
        mAssetListHolder = (LinearLayout) findViewById(asset_list_holder);
        mAssetName = (TextView) findViewById(R.id.assetName);
        mBtn_GestureOverlay_Close = (TextView) findViewById(R.id.close_gesture_view_holder);
        //    mGestureImage = (ImageView) findViewById(R.id.image_Gesture);
        mGesture_view_holder = (RelativeLayout) findViewById(R.id.gesture_view_holder);
        mAssetTipLayout = (RelativeLayout) findViewById(R.id.tip_for_asset_swipe);
        mBtn_GestureOverlay_Closeview = (TextView) findViewById(R.id.btn_close_gesture_view_holder);
        mAssetListCrossButton = (TextView) findViewById(R.id.asset_list_cross_button);
        mIcDrawerOpenLayout = (ImageView) findViewById(R.id.drop_list_forward);
        mAssetSetting = (ImageView) findViewById(R.id.asset_setting);
        mVolumeTipText = (TextView) findViewById(R.id.volume_tip_text);
        mBrightnessTipText = (TextView) findViewById(R.id.brightness_tip_text);
        mSeekbarTipText = (TextView) findViewById(R.id.seekbar_tip_text);
        mAssetGotItTipTextview = (TextView) findViewById(R.id.btn_got_it);
        mActionbarHolder = (RelativeLayout) findViewById(R.id.actionbar_holder);
        mAssetPlayerBack = (ImageView) findViewById(R.id.asset_player_back);
        mAssetListOnPlayerRecycleview = (RecyclerView) findViewById(R.id.asset_list_on_player);
        mAssetListOnPlayerRecycleview.setLayoutManager(new LinearLayoutManager(this));
        mAssetListOnPlayerRecycleview.setItemAnimator(new DefaultItemAnimator());

       // mAssetListOnPlayerAdapter = new AssetListOnPlayerAdapter(this, this, digitalAssetsList);
        //mAssetListOnPlayerRecycleview.setAdapter(mAssetListOnPlayerAdapter);
        mShareIconAsset = (ImageView) findViewById(R.id.asset_share);
*/
/*        if (getIntent().getSerializableExtra(getString(R.string.list)) != null) {
            digitalAssetsList = (List<DigitalAssets>) getIntent().getSerializableExtra(getString(R.string.list));
        }*//*

        pageSlideAdapter = new PageSlideAdapter(getSupportFragmentManager(), digitalAssetsList, CurrentDigigtalAsset);
        AssetPlayerPager.setAdapter(pageSlideAdapter);
        AssetPlayerPager.setOnPageChangeListener(this);
        setCurrentSelectedItem();

     */
/*   if (customer != null) {
            mShareIconAsset.setVisibility(View.VISIBLE);

        } else {

            mShareIconAsset.setVisibility(View.GONE);

        }*//*


    }

    private void setCurrentSelectedItem() {


        if (CurrentDigigtalAsset == 0) {
            this.onAssetChanged(0);
        }
        if (CurrentDigigtalAsset != -1) {
            AssetPlayerPager.setCurrentItem(CurrentDigigtalAsset);
        }

    }


    private void getDigitalAssetList() {


        dateHelper = new DateHelper();

        if (getIntent() != null) {




        }

        ViewInialization();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.transparent_view:


                mAssetListHolder.setVisibility(View.GONE);

                break;
            case R.id.drop_list_forward:

                insert_userLike_when_closeClicked();
                mAssetListHolder.setVisibility(View.VISIBLE);

                break;

            case R.id.asset_list_cross_button:
                mAssetListHolder.setVisibility(View.GONE);

                break;

            case R.id.asset_share:

             */
/*   insert_userLike_when_closeClicked();
                Intent intent = new Intent(this, DoctorsVideoFeedbackActivity.class);
                if (storyObject != null) {
                    ArrayList<DigitalAssets> list = new ArrayList<>();

                    for (DigitalAssets digitalAssets : digitalAssetsList) {
                        list.add(digitalAssets);
                    }
                    intent.putExtra(getString(R.string.list), list);
                    intent.putExtra(Constants.CUSTOMER_OBJECT, getIntent().getSerializableExtra(Constants.CUSTOMER_OBJECT));
                    intent.putExtra(getString(R.string.position), CurrentDigigtalAsset);


                } else {

                    intent.putExtra(Constants.CUSTOMER_OBJECT, getIntent().getSerializableExtra(Constants.CUSTOMER_OBJECT));
                    intent.putExtra(getString(R.string.list), getIntent().getSerializableExtra(getString(R.string.list)));
                    intent.putExtra(getString(R.string.position), CurrentDigigtalAsset);


                }
                isFeedbackClicked = true;
                isForPDFPageBinder = true;
                startActivity(intent);*//*


                break;

            case R.id.asset_setting:

                insert_userLike_when_closeClicked();
                popupMenu = new PopupMenu(AssetPlayerActivity.this, mAssetSetting);
                popupMenu.setOnMenuItemClickListener(AssetPlayerActivity.this);
                popupMenu.inflate(R.menu.assetplayer_menu);
                popupMenu.show();

                break;


            */
/*case R.id.auto_play:

                 boolean autoplay = !(PreferenceUtils.getAutoplay(this));
                 PreferenceUtils.setAutoplay(this,autoplay);

                break;
            *//*


            case R.id.asset_player_back:
                onBackPressed();
                break;


            case R.id.close_gesture_view_holder:
            case R.id.btn_close_gesture_view_holder:

                insert_userLike_when_closeClicked();
                //  hideGestureLayerHolder();
                break;

            case R.id.rating_image1:
                stopHandler();
               */
/* rating1_img.setImageResource(R.mipmap.ic_verypoor);
                startHandler();
                like_rating = 1;
                rating5_img.setImageResource(R.mipmap.ic_excellent_grey);
                rating4_img.setImageResource(R.mipmap.ic_good_grey);
                rating3_img.setImageResource(R.mipmap.ic_average_grey);
                rating2_img.setImageResource(R.mipmap.ic_poor_grey);
                viewAnimator = ViewAnimator
                        .animate(rating1_img)
                        .bounceIn()
                        .onStop(new AnimationListener.Stop() {
                            @Override
                            public void onStop() {


                            }
                        })
                        .start();*//*


                break;
            case R.id.rating_image2:
                stopHandler();

               */
/* rating2_img.setImageResource(R.mipmap.ic_poor);
                startHandler();
                like_rating = 2;
                rating5_img.setImageResource(R.mipmap.ic_excellent_grey);
                rating4_img.setImageResource(R.mipmap.ic_good_grey);
                rating3_img.setImageResource(R.mipmap.ic_average_grey);
                rating1_img.setImageResource(R.mipmap.ic_verypoor_grey);
                viewAnimator = com.github.florent37.viewanimator.ViewAnimator
                        .animate(rating2_img)
                        .bounceIn()
                        .onStop(new AnimationListener.Stop() {
                            @Override
                            public void onStop() {


                            }
                        })
                        .start();*//*



                break;
            case R.id.rating_image3:

                stopHandler();
               */
/* rating3_img.setImageResource(R.mipmap.ic_average);
                startHandler();
                like_rating = 3;
                rating5_img.setImageResource(R.mipmap.ic_excellent_grey);
                rating4_img.setImageResource(R.mipmap.ic_good_grey);
                rating2_img.setImageResource(R.mipmap.ic_poor_grey);
                rating1_img.setImageResource(R.mipmap.ic_verypoor_grey);
                viewAnimator = com.github.florent37.viewanimator.ViewAnimator
                        .animate(rating3_img)
                        .bounceIn()
                        .onStop(new AnimationListener.Stop() {
                            @Override
                            public void onStop() {


                            }
                        })
                        .start();*//*



                break;
            case R.id.rating_image4:

                stopHandler();
              */
/*  rating4_img.setImageResource(R.mipmap.ic_good);
                startHandler();
                like_rating = 4;
                rating5_img.setImageResource(R.mipmap.ic_excellent_grey);
                rating3_img.setImageResource(R.mipmap.ic_average_grey);
                rating2_img.setImageResource(R.mipmap.ic_poor_grey);
                rating1_img.setImageResource(R.mipmap.ic_verypoor_grey);
                viewAnimator = com.github.florent37.viewanimator.ViewAnimator
                        .animate(rating4_img)
                        .bounceIn()
                        .onStop(new AnimationListener.Stop() {
                            @Override
                            public void onStop() {


                            }
                        })
                        .start();
*//*


                break;
            case R.id.rating_image5:

                stopHandler();
              */
/*  rating5_img.setImageResource(R.mipmap.ic_excellent);
                startHandler();
                like_rating = 5;
                rating4_img.setImageResource(R.mipmap.ic_good_grey);
                rating3_img.setImageResource(R.mipmap.ic_average_grey);
                rating2_img.setImageResource(R.mipmap.ic_poor_grey);
                rating1_img.setImageResource(R.mipmap.ic_verypoor_grey);
                viewAnimator = com.github.florent37.viewanimator.ViewAnimator
                        .animate(rating5_img)
                        .bounceIn()
                        .onStop(new AnimationListener.Stop() {
                            @Override
                            public void onStop() {


                            }
                        })
                        .start();*//*



                break;
            case R.id.btn_got_it:

                mAssetTipLayout.setVisibility(View.GONE);


                switch (digitalAssetsList.get(CurrentDigigtalAsset).getDA_Type()) {

                    case Constant.IMAGEASSET:

                        PreferenceUtils.setIsImageGuideCompleted(this, true);

                        break;

                    case Constant.AUDIOASSET:


                        PreferenceUtils.setIsAudioGuideCompleted(this, true);
                        Log.d("Gotit", "audio");


                        break;

                    case Constant.HTMLASSET:


                        PreferenceUtils.setIsPlayerGuideCompleted(this, true);
                        Log.d("Gotit", "html");


                        break;

                    case Constant.VIDEOASSET:

                        //  PreferenceUtils.setIsVideoGuideCompleted(this,true);

                        Log.d("Gotit", "video");
                        PreferenceUtils.setIsVideoGuideCompleted(this, true);


                        break;

                    case Constant.PDFASSET:

                        Log.d("Gotit", "pdf");
                        PreferenceUtils.setIsPdfGuideCompleted(this, true);

                        break;

                    default:

                        break;


                }


                */
/*if (mTipGuideTimer != null){
                    mTipGuideTimer.cancel();
                }*//*


                break;

            default:

                break;

        }

    }

    private void insert_userLike_when_closeClicked() {


        if (like_rating > 0) {
            stopHandler();
            updateFeedback_After_updation();

            like_rating = 0;
        } else {
            hideGestureLayerHolder();

        }


    }

    private void updateFeedback_After_updation() {
        UpdateFeedback(digitalAssetsList.get(CurrentGesturePosition), like_rating);
        //  Toast.makeText(getApplicationContext(), "10 seconds", Toast.LENGTH_SHORT).show();
        hideGestureLayerHolder();
 */
/*       rating4_img.setImageResource(R.mipmap.ic_good_grey);
        rating2_img.setImageResource(R.mipmap.ic_poor_grey);
        rating5_img.setImageResource(R.mipmap.ic_excellent_grey);
        rating1_img.setImageResource(R.mipmap.ic_verypoor_grey);
        rating3_img.setImageResource(R.mipmap.ic_average_grey);*//*


    }


    private void stopHandler() {
        handler.removeCallbacks(myRunnable);
    }

    private void startHandler() {

        handler.postDelayed(myRunnable, 10000);
    }


    Runnable myRunnable = new Runnable() {
        public void run() {

            updateFeedback_After_updation();


        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        insert_userLike_when_closeClicked();

        */
/*if (digitalAssetsList.get(AssetPlayerPager.getCurrentItem()).getPlayer_Start_Time()!=null){

            if(isFromMCMandatory){
                storyObject.setCustomer_Code(customer.getCustomer_Code());
                storyObject.setCustomer_region_Code(customer.getRegion_Code());
                mDigitalAssetRepository.insertCustomerIntoMCStoryLog(storyObject);
            }
        }*//*


    }

    private void SetPdfAction(int itemId) {

        try {
            PdfViewFragment pdfViewFragment = (PdfViewFragment) pageSlideAdapter.getFragment(AssetPlayerPager.getCurrentItem());
            if (pdfViewFragment != null) {
                pdfViewFragment.showMenu(itemId);
            }

        } catch (Exception e) {

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        isFeedbackClicked = false;
        isForCheckPermission = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                getBrightness();

            } else {
            }
        } else {
            getBrightness();
        }



*/
/*        if (PreferenceUtils.getIsPlayerGuideCompleted(this)){

            mAssetTipLayout.setVisibility(View.GONE);

        }else {



            *//*
*/
/*mTipGuideTimer = new CountDownTimer(3000,300){

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {

                    mAssetTipLayout.setVisibility(View.GONE);

                    switch (digitalAssetsList.get(CurrentDigigtalAsset).getDA_Type()){

                        case Constant.IMAGEASSET:

                              Log.d("=>image",digitalAssetsList.get(CurrentDigigtalAsset).getDA_Name());

                            break;

                        case  Constant.AUDIOASSET:

                            Log.d("=>audio",digitalAssetsList.get(CurrentDigigtalAsset).getDA_Name());

                            break;

                        case Constant.HTMLASSET:

                            Log.d("=>hrml",digitalAssetsList.get(CurrentDigigtalAsset).getDA_Name());

                            break;

                        case Constant.VIDEOASSET:


                            if(isVideoStarted){



                                Log.d("<=>video",digitalAssetsList.get(CurrentDigigtalAsset).getDA_Name());


                            }else {

                                isVideoNotStarted = true;
                                Log.d("<=>videonotstarted",digitalAssetsList.get(CurrentDigigtalAsset).getDA_Name());

                            }

                            break;

                        case Constant.PDFASSET:

                            Log.d("=>pdf",digitalAssetsList.get(CurrentDigigtalAsset).getDA_Name());

                            break;

                        default:

                            break;


                    }
                }

            }.start();
*//*

    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("=>position_showeditem", AssetPlayerPager.getCurrentItem() + "");
        if (!isFeedbackClicked && !isForCheckPermission) {
            UpdateDetails();
        }


        if (isToastVisible) {
            mToast.cancel();
            isToastVisible = false;
        }


    }

    private void UpdateDetails() {

        if (digitalAssetsList.get(AssetPlayerPager.getCurrentItem()).getPlayer_Start_Time() != null) {

            if (digitalAssetsList.get(AssetPlayerPager.getCurrentItem()).getDA_Type() == 2) {

                UpdatePlayerEndTime(AssetPlayerPager.getCurrentItem(), Endpostion_interept / 1000);

            } else if (digitalAssetsList.get(AssetPlayerPager.getCurrentItem()).getDA_Type() == 3) {

                UpdatePlayerEndTime(AssetPlayerPager.getCurrentItem(), Endpostion_interept / 1000);

            } else if (digitalAssetsList.get(AssetPlayerPager.getCurrentItem()).getDA_Type() == 1) {

                UpdateImagePlayerEndtime(AssetPlayerPager.getCurrentItem(), Calendar.getInstance().getTime());
            } else if (digitalAssetsList.get(AssetPlayerPager.getCurrentItem()).getDA_Type() == 4) {

                UpdatePageEndTime(AssetPlayerPager.getCurrentItem(), Calendar.getInstance().getTime());
            } else {

                InsertFinalParturlEndTime(AssetPlayerPager.getCurrentItem(), Calendar.getInstance().getTime());

            }

        } else {

            if (isFromMCMandatory) {

                if (videocomplete) {

                 */
/*   storyObject.setCustomer_Code(customer.getCustomer_Code());
                    storyObject.setCustomer_region_Code(customer.getRegion_Code());
                    mDigitalAssetRepository.insertCustomerIntoMCStoryLog(storyObject);*//*


                }
            }

        }
        if (digitalAssetsList.get(AssetPlayerPager.getCurrentItem()).getDetailed_StartTime() != null) {

            UpdateEdetailEndTime(Calendar.getInstance().getTime(), AssetPlayerPager.getCurrentItem());

        }

    }

    @Override
    public void OnAssetListRowItemClicked(int postion) {

        AssetPlayerPager.setCurrentItem(postion);
        mAssetListHolder.setVisibility(View.GONE);

    }


    public void AssetPlayBackPositionChange(int position, boolean state) {

        if (position >= 0 && position < digitalAssetsList.size()) {
            AssetPlayerPager.setCurrentItem(position);
        } else {
            if (position >= digitalAssetsList.size()) {

                if (state) {
                    onBackPressed();
                }
                mToast.makeText(this, getString(R.string.lastpagealert), Toast.LENGTH_SHORT).show();

            } else {

                if (state) {
                    onBackPressed();
                }
                mToast.makeText(this, getString(R.string.firstpagealert), Toast.LENGTH_SHORT).show();

            }
        }

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        this.onAssetChanged(position);


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onAssetChanged(int position) {


        android.support.v4.app.Fragment hosted = pageSlideAdapter.getItem(AssetPlayerPager.getCurrentItem());
        if (hosted instanceof ImageViewFragment) {
            HideSettingsButton();
            Log.d("parm", "1");

        } else if (hosted instanceof ExoPlayerFragment) {
            HideSettingsButton();
            Log.d("parm", "2");

        } else if (hosted instanceof ExoAudioPlayerFragment) {
            HideSettingsButton();
            Log.d("parm", "3");
        } else if (hosted instanceof PdfViewFragment) {
            ShowSettingsButton();
            Log.d("parm", "4");
        } else if (hosted instanceof WebviewFragment) {
            HideSettingsButton();
            Log.d("parm", "5");
        }

        mCurrentAssetObject = digitalAssetsList.get(position);
        mAssetName.setText(mCurrentAssetObject.getDA_Name());
        CurrentDigigtalAsset = position;

    }


    public int getCurrentSessionId(int currentAssetPosition) {

        //  return mDigitalAssetRepository.getAssetSessionId(digitalAssetsList.get(currentAssetPosition).getDA_Code());
*/
/*
        if(customer == null || customer.getCustomer_Code() == null || customer.getRegion_Code() == null){
            return 0;
        }
        return mDigitalAssetRepository.generateSessionIdBasedOnDetailedId(DateHelper.getCurrentDate(),customer.getCustomer_Code(),customer.getRegion_Code(),
                digitalAssetsList.get(currentAssetPosition).getDA_Code(),PreferenceUtils.getCustomerDetailedId(this));
*//*



    }


    public boolean InsertEdetailStarttime(Date edetailstarttime, int SessionId, int currentAssetPosition) {
        if (SessionId != 0) {
            digitalAssetsList.get(currentAssetPosition).setSessionId(SessionId);
        } else {
            digitalAssetsList.get(currentAssetPosition).setSessionId(getCurrentSessionId(currentAssetPosition) + 1);
        }
        if (edetailstarttime != null) {
            digitalAssetsList.get(currentAssetPosition).setDetailed_StartTime(df.format(edetailstarttime));

        }
        return true;
    }


    public boolean InsertPlayerStartTime(int playerstarttime, int currentAssetPosition, int playmode) {

        digitalAssetsList.get(currentAssetPosition).setPart_Id(1);
        digitalAssetsList.get(currentAssetPosition).setPlayMode(playmode);
        digitalAssetsList.get(currentAssetPosition).setPlayer_Start_Time(playerstarttime + "");
        setCustomerDetail(currentAssetPosition);
        mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentAssetPosition), false);
        return true;

    }

    public void UpdatePlayerEndTime(int currentPosition, int playerendtime) {

        if (digitalAssetsList.get(currentPosition).getPlayer_Start_Time() != null) {
            digitalAssetsList.get(currentPosition).setPlayed_Time_Duration(getVideoTimeDuration(digitalAssetsList.get(currentPosition).getPlayer_Start_Time(), playerendtime));
            digitalAssetsList.get(currentPosition).setPlayer_End_Time(playerendtime + "");
        }
    }

    private long getVideoTimeDuration(String player_start_time, int playerendtime) {

        int starttime = Integer.parseInt(player_start_time);
        long difference = 0;
        if (starttime > playerendtime) {

            difference = starttime - playerendtime;

        } else {

            difference = playerendtime - starttime;

        }

        return difference;
    }


    public void UpdateEdetailEndTime(Date edetailendtime, int currentAssetPosition) {

        if (edetailendtime != null) {
            digitalAssetsList.get(currentAssetPosition).setDetailed_EndTime(df.format(edetailendtime) + "");
            mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentAssetPosition), false);
            */
/*Update Asset End time After move to next asset*//*

            if(customer != null){
                mDigitalAssetRepository.updateAssetEndTimeAfterMoveToNextAsset(customer.getCustomer_Code(),customer.getRegion_Code(),
                        digitalAssetsList.get(currentAssetPosition).getDA_Code(),digitalAssetsList.get(currentAssetPosition).getSessionId(),
                        digitalAssetsList.get(currentAssetPosition).getCustomer_Detailed_Id(),df.format(edetailendtime));
            }
            digitalAssetsList.get(currentAssetPosition).setId(0);
            digitalAssetsList.get(currentAssetPosition).setPlayer_Start_Time(null);
            digitalAssetsList.get(currentAssetPosition).setPlayer_End_Time(null);
            digitalAssetsList.get(currentAssetPosition).setPlayed_Time_Duration(0);
            digitalAssetsList.get(currentAssetPosition).setDetailed_StartTime(null);
            digitalAssetsList.get(currentAssetPosition).setDetailed_EndTime(null);

        }


    }


    public void MultiUpdateEndTime(int endposition, int currentAssetPosition) {
        if (digitalAssetsList.get(currentAssetPosition).getPlayer_Start_Time() != null) {
            digitalAssetsList.get(currentAssetPosition).setPlayed_Time_Duration(getVideoTimeDuration(digitalAssetsList.get(currentAssetPosition).getPlayer_Start_Time(), endposition));
            digitalAssetsList.get(currentAssetPosition).setPlayer_End_Time("" + endposition);
            mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentAssetPosition), false);
            digitalAssetsList.get(currentAssetPosition).setPlayed_Time_Duration(0);
        }

    }

    public void MultiUpdateStartTime(int startposition, int currentAssetPosition, int playmode) {

        digitalAssetsList.get(currentAssetPosition).setPlayer_Start_Time("" + startposition);
        digitalAssetsList.get(currentAssetPosition).setPlayMode(playmode);
        setCustomerDetail(currentAssetPosition);
        digitalAssetsList.get(currentAssetPosition).setId(0);
        digitalAssetsList.get(currentAssetPosition).setPlayer_End_Time(null);
        mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentAssetPosition), false);

    }


    public void endtimeUpdateOnComplete(int endtime, int currentAssetPosition) {

        if (digitalAssetsList.get(currentAssetPosition).getPlayer_Start_Time() != null) {
            digitalAssetsList.get(currentAssetPosition).setPlayer_End_Time("" + endtime);
            mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentAssetPosition), false);
        }
        videocomplete = true;

    }


    public void InsertImageDetailStartTime(int currentassetposition, Date starttime, int sessionId) {
        if (sessionId != 0) {
            digitalAssetsList.get(currentassetposition).setSessionId(sessionId);
        } else {
            if(customer != null && !TextUtils.isEmpty(customer.getCustomer_Code())){
                sessionId = mDigitalAssetRepository.generateSessionIdBasedOnDetailedId(DateHelper.getCurrentDate(),customer.getCustomer_Code(),customer.getRegion_Code(),
                        digitalAssetsList.get(currentassetposition).getDA_Code(),PreferenceUtils.getCustomerDetailedId(this));
                digitalAssetsList.get(currentassetposition).setSessionId(sessionId + 1);
            */
/*digitalAssetsList.get(currentassetposition).setSessionId(mDigitalAssetRepository.
                    getAssetSessionId(digitalAssetsList.get(currentassetposition).getDA_Code()) + 1);*//*

            }
        }
        digitalAssetsList.get(currentassetposition).setPart_Id(1);
        if (starttime != null) {
            digitalAssetsList.get(currentassetposition).setDetailed_StartTime(df.format(starttime));
        }
        setCustomerDetail(currentassetposition);
    }

    private void setCustomerDetail(int currentassetposition) {

        if (customer != null) {

            digitalAssetsList.get(currentassetposition).setLatitude(gpsTracker.getLatitude());
            digitalAssetsList.get(currentassetposition).setLongitude(gpsTracker.getLongitude());
            digitalAssetsList.get(currentassetposition).setCustomer_Category_Code(customer.getCategory_Code());
            digitalAssetsList.get(currentassetposition).setCustomer_Region_Code(customer.getRegion_Code());
            digitalAssetsList.get(currentassetposition).setCustomer_Speciality_Code(customer.getSpeciality_Code());
            digitalAssetsList.get(currentassetposition).setCustomer_Category_Name(customer.getCategory_Name());
            digitalAssetsList.get(currentassetposition).setCustomer_Name(customer.getCustomer_Name());
            digitalAssetsList.get(currentassetposition).setCustomer_Code(customer.getCustomer_Code());
            digitalAssetsList.get(currentassetposition).setDetailed_DateTime(DateHelper.getCurrentDate());
            digitalAssetsList.get(currentassetposition).setSur_Name(customer.getCustomer_Name());
            digitalAssetsList.get(currentassetposition).setLocalArea(customer.getLocal_Area());
            digitalAssetsList.get(currentassetposition).setCustomer_MDL_Number(customer.getMDL_Number());
            digitalAssetsList.get(currentassetposition).setCustomer_Speciality_Name(customer.getSpeciality_Name());
            digitalAssetsList.get(currentassetposition).setCustomer_Detailed_Id(PreferenceUtils.getCustomerDetailedId(this));
            digitalAssetsList.get(currentassetposition).setTime_Zone(displayTimeZone(TimeZone.getDefault()));
            digitalAssetsList.get(currentassetposition).setIsPreview(0);
            if (storyObject != null) {

                if (storyObject.getStory_From() == DigitalAssetRepository.MODE_MC) {

                    digitalAssetsList.get(currentassetposition).setMC_StoryID(storyObject.getStory_Id());

                } else {

                    digitalAssetsList.get(currentassetposition).setUD_StoryID(storyObject.getStory_Id());

                }

            }

        } else {

            digitalAssetsList.get(currentassetposition).setIsPreview(1);
            digitalAssetsList.get(currentassetposition).setLatitude(gpsTracker.getLatitude());
            digitalAssetsList.get(currentassetposition).setLongitude(gpsTracker.getLongitude());
            digitalAssetsList.get(currentassetposition).setCustomer_Code(null);
            digitalAssetsList.get(currentassetposition).setCustomer_Name(null);
            digitalAssetsList.get(currentassetposition).setSur_Name(null);
            digitalAssetsList.get(currentassetposition).setLocalArea(null);
            digitalAssetsList.get(currentassetposition).setDetailed_DateTime(DateHelper.getCurrentDate());
            digitalAssetsList.get(currentassetposition).setTime_Zone(displayTimeZone(TimeZone.getDefault()));

        }

    }


    public void InsertPartUrlStartTimeEvent(int currentassetpsition, Date parturlstarttime, String parturl) {

        setCustomerDetail(currentassetpsition);
        digitalAssetsList.get(currentassetpsition).setPlayer_Start_Time("0");
        digitalAssetsList.get(currentassetpsition).setPart_URL(parturl);
        mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetpsition), false);

        if (parturlstarttime != null) {
            digitalAssetsList.get(currentassetpsition).setPlayer_Start_Time(df.format(parturlstarttime));
        }


    }

    public void UpdateImagePlayerStarttime(int currentassetposition, Date starttime, int playmode) {

        digitalAssetsList.get(currentassetposition).setPlayer_Start_Time("0");
        digitalAssetsList.get(currentassetposition).setPlayMode(playmode);
        mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetposition), false);
        if (starttime != null) {

            digitalAssetsList.get(currentassetposition).setPlayer_Start_Time(df.format(starttime));

        }

    }

    public void UpdateImagePlayerEndtime(int currentassetposition, Date starttime) {


        if (digitalAssetsList.get(currentassetposition).getPlayer_Start_Time() != null) {

            digitalAssetsList.get(currentassetposition).setPlayed_Time_Duration(getDifferenceInSeconds(digitalAssetsList.get(currentassetposition).getPlayer_Start_Time(), starttime));
            digitalAssetsList.get(currentassetposition).setPlayer_End_Time(getDifferenceInSeconds(digitalAssetsList.get(currentassetposition).getPlayer_Start_Time(), starttime) + "");
            digitalAssetsList.get(currentassetposition).setPlayer_Start_Time("0");
        }
        // digitalAssetsList.get(currentassetposition).setPlayer_End_Time(df.format(starttime));

    }

    private long getDifferenceInSeconds(String player_start_time, Date endtime) {

        Date stardate = null;
        long diffInSec = 0, diffInMs;
        try {

            stardate = df.parse(player_start_time);

        } catch (ParseException e) {
            e.printStackTrace();

        }

        if (endtime == null || stardate == null) {
            diffInSec = 10;
        } else {

            diffInMs = endtime.getTime() - stardate.getTime();
            diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);

        }

        return diffInSec;

    }

    public void InsertHtmlDetailStarttime(int currentassetposition, Date starttime) {

        if (starttime != null) {
            setCustomerDetail(currentassetposition);
            digitalAssetsList.get(currentassetposition).setDetailed_StartTime(df.format(starttime));
            digitalAssetsList.get(currentassetposition).setSessionId(getCurrentSessionId(currentassetposition) + 1);
            mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetposition), false);

        } else {

            setCustomerDetail(currentassetposition);
            digitalAssetsList.get(currentassetposition).setDetailed_StartTime(df.format(Calendar.getInstance().getTime()));
            digitalAssetsList.get(currentassetposition).setSessionId(getCurrentSessionId(currentassetposition) + 1);
            mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetposition), false);


        }


    }


    public void InsertPartUrlStartTime(int currentassetpsition, Date parturlstarttime, String parturl) {

        setCustomerDetail(currentassetpsition);
        digitalAssetsList.get(currentassetpsition).setPlayer_Start_Time("0");
        String target = "cache/";
        int index = parturl.indexOf(target);
        int subIndex = index + target.length();
        Log.d("=>suburl", parturl.substring(subIndex));
        digitalAssetsList.get(currentassetpsition).setPart_URL(parturl.substring(subIndex));
        digitalAssetsList.get(currentassetpsition).setPart_Id(1);
        mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetpsition), false);

        if (parturlstarttime != null) {
            digitalAssetsList.get(currentassetpsition).setPlayer_Start_Time(df.format(parturlstarttime));
        }


    }


    public void InsertFinalParturlEndTime(int currentposition, Date parturlendtime) {

        if (digitalAssetsList.get(currentposition).getPlayer_Start_Time() != null) {
            digitalAssetsList.get(currentposition).setPlayed_Time_Duration(getDifferenceInSeconds(digitalAssetsList.get(currentposition).getPlayer_Start_Time(), parturlendtime));
            digitalAssetsList.get(currentposition).setPlayer_End_Time("" + getDifferenceInSeconds(digitalAssetsList.get(currentposition).getPlayer_Start_Time(), parturlendtime));
            digitalAssetsList.get(currentposition).setPlayer_Start_Time("0");
            mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentposition), false);

        }

    }


    public void InsertParturlEndTime(int currentassetpsition, Date parturlendtime) {

        if (digitalAssetsList.get(currentassetpsition).getPlayer_Start_Time() != null) {

            digitalAssetsList.get(currentassetpsition).setPlayed_Time_Duration(getDifferenceInSeconds(digitalAssetsList.get(currentassetpsition).getPlayer_Start_Time(), parturlendtime));
            digitalAssetsList.get(currentassetpsition).setPlayer_End_Time("" + getDifferenceInSeconds(digitalAssetsList.get(currentassetpsition).getPlayer_Start_Time(), parturlendtime));
            digitalAssetsList.get(currentassetpsition).setPlayer_Start_Time("0");
            mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetpsition), false);
            digitalAssetsList.get(currentassetpsition).setId(0);
            digitalAssetsList.get(currentassetpsition).setPlayer_End_Time(null);
            digitalAssetsList.get(currentassetpsition).setPlayer_Start_Time(null);
        }

    }

    public void InsertUrlEndTime(int currentassetpsition, Date parturlendtime) {

        if (digitalAssetsList.get(currentassetpsition).getPlayer_Start_Time() != null) {

            digitalAssetsList.get(currentassetpsition).setPlayed_Time_Duration(getDifferenceInSeconds(digitalAssetsList.get(currentassetpsition).getPlayer_Start_Time(), parturlendtime));
            digitalAssetsList.get(currentassetpsition).setPlayer_End_Time("" + getDifferenceInSeconds(digitalAssetsList.get(currentassetpsition).getPlayer_Start_Time(), parturlendtime));
            digitalAssetsList.get(currentassetpsition).setPlayer_Start_Time("0");
            mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetpsition), false);

        }


    }

    public void InsertHtmlDetailEndtime(int currentassetposition, Date endtime) {

        digitalAssetsList.get(currentassetposition).setDetailed_EndTime(df.format(endtime));
        mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetposition), false);
        */
/*Update Asset End time After move to next asset*//*

        if(customer != null){
            mDigitalAssetRepository.updateAssetEndTimeAfterMoveToNextAsset(customer.getCustomer_Code(),customer.getRegion_Code(),
                    digitalAssetsList.get(currentassetposition).getDA_Code(),digitalAssetsList.get(currentassetposition).getSessionId(),
                    digitalAssetsList.get(currentassetposition).getCustomer_Detailed_Id(),df.format(endtime));
        }
        digitalAssetsList.get(currentassetposition).setId(0);
        digitalAssetsList.get(currentassetposition).setDetailed_StartTime(null);
        digitalAssetsList.get(currentassetposition).setDetailed_EndTime(null);
        digitalAssetsList.get(currentassetposition).setPart_URL(null);
        digitalAssetsList.get(currentassetposition).setPlayer_Start_Time(null);
        digitalAssetsList.get(currentassetposition).setPlayer_End_Time(null);


    }

    public void UpdateImageDetailEndTime(int currentassetposition, Date endtime) {

        if (digitalAssetsList.get(currentassetposition).getDetailed_StartTime() != null) {

            if (digitalAssetsList.get(currentassetposition).getPlayer_Start_Time() != null) {

                if (!digitalAssetsList.get(currentassetposition).getPlayer_Start_Time().equals("0")) {
                    digitalAssetsList.get(currentassetposition).setPlayer_Start_Time("");
                }
            }
            digitalAssetsList.get(currentassetposition).setDetailed_EndTime(df.format(endtime));
            mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetposition), false);
        }
        digitalAssetsList.get(currentassetposition).setId(0);
        digitalAssetsList.get(currentassetposition).setSessionId(digitalAssetsList.get(currentassetposition).getSessionId() + 1);
        digitalAssetsList.get(currentassetposition).setDetailed_EndTime(null);
        digitalAssetsList.get(currentassetposition).setDetailed_StartTime(null);
        digitalAssetsList.get(currentassetposition).setPlayer_End_Time(null);
        digitalAssetsList.get(currentassetposition).setPlayer_Start_Time(null);

    }


    public void InsertPdfDetailStartTime(int currentassetposition, Date DetailedStartime) {

        digitalAssetsList.get(currentassetposition).setDetailed_StartTime(df.format(DetailedStartime));
        digitalAssetsList.get(currentassetposition).setSessionId(getCurrentSessionId(currentassetposition) + 1);
        setCustomerDetail(currentassetposition);

    }


    public void UpdatePageStartTime(int currentassetposition, Date pagestarttime, int page, int playmode) {

        digitalAssetsList.get(currentassetposition).setPart_Id(page);
        digitalAssetsList.get(currentassetposition).setPlayMode(playmode);
        digitalAssetsList.get(currentassetposition).setPlayer_Start_Time("0");
        mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetposition), false);
        digitalAssetsList.get(currentassetposition).setPlayer_Start_Time(df.format(pagestarttime));


    }


    public void UpdateMultiPageStartTime(int currentassetposition, Date pagestarttime, int page, int playmode) {

        digitalAssetsList.get(currentassetposition).setPlayer_Start_Time(null);
        digitalAssetsList.get(currentassetposition).setPlayer_End_Time(null);
        digitalAssetsList.get(currentassetposition).setId(0);
        digitalAssetsList.get(currentassetposition).setPart_Id(page);
        digitalAssetsList.get(currentassetposition).setPlayer_Start_Time("0");
        digitalAssetsList.get(currentassetposition).setPlayMode(playmode);
        mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetposition), false);
        digitalAssetsList.get(currentassetposition).setPlayer_Start_Time(df.format(pagestarttime));

    }

    public void UpdatePageEndTime(int currentassetposition, Date pageendtime) {

        if (digitalAssetsList.get(currentassetposition).getPlayer_Start_Time() != null) {

            digitalAssetsList.get(currentassetposition).setPlayed_Time_Duration(getDifferenceInSeconds(digitalAssetsList.get(currentassetposition).getPlayer_Start_Time(), pageendtime));
            digitalAssetsList.get(currentassetposition).setPlayer_End_Time(getDifferenceInSeconds(digitalAssetsList.get(currentassetposition).getPlayer_Start_Time(), pageendtime) + "");
            digitalAssetsList.get(currentassetposition).setPlayer_Start_Time("0");
            mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetposition), false);


        }

    }

    public void InsertPdfDetailEndTime(int currentassetposition, Date DetailedEndTime) {

        digitalAssetsList.get(currentassetposition).setDetailed_EndTime(df.format(DetailedEndTime));
        mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetposition), false);
        */
/*Update PDF End time After move to next asset*//*

        if(customer != null){
            mDigitalAssetRepository.updateAssetEndTimeAfterMoveToNextAsset(customer.getCustomer_Code(),customer.getRegion_Code(),
                    digitalAssetsList.get(currentassetposition).getDA_Code(),digitalAssetsList.get(currentassetposition).getSessionId(),
                    digitalAssetsList.get(currentassetposition).getCustomer_Detailed_Id(),df.format(DetailedEndTime));
        }
        digitalAssetsList.get(currentassetposition).setDetailed_EndTime(null);
        digitalAssetsList.get(currentassetposition).setDetailed_StartTime(null);
        digitalAssetsList.get(currentassetposition).setPlayMode(0);
        digitalAssetsList.get(currentassetposition).setPlayer_Start_Time(null);
        digitalAssetsList.get(currentassetposition).setPlayer_End_Time(null);
        digitalAssetsList.get(currentassetposition).setPart_Id(0);
        digitalAssetsList.get(currentassetposition).setId(0);

    }

    public void DeleteLessThanTwoSeconds(int currentassetposition) {

        mDigitalAssetRepository.deleteAssetAnalyticsBasedOnDuration(digitalAssetsList.get(currentassetposition).getDA_Code(), digitalAssetsList.get(currentassetposition).getSessionId(), Constant.DeletePdfPageAnalyticsSeconds);

    }


    @Override
    public void onSwipeOutAtStart() {

        if (!isToastVisible) {

            mToast.makeText(this, getString(R.string.firstpagealert), Toast.LENGTH_SHORT).show();
            isToastVisible = true;
        }

    }

    @Override
    public void onSwipeOutAtEnd() {

        if (!isToastVisible) {

            mToast.makeText(this, getString(R.string.lastpagealert), Toast.LENGTH_SHORT).show();
            isToastVisible = true;
        }


    }

    @Override
    public void onHideToast() {

        if (isToastVisible) {
            mToast.cancel();
            isToastVisible = false;
        }

    }


    public void setCurrentPositon(int currentPosition) {

        Endpostion_interept = currentPosition;

    }


    public void ShowActionBarControll() {

*/
/*        if (mActionbarHolder.getVisibility() == View.VISIBLE) {

            if (popupMenu != null) {
                popupMenu.dismiss();
            }
            mActionbarHolder.setVisibility(View.GONE);
            hideStatusBar();


        } else {
            mActionbarHolder.setVisibility(View.VISIBLE);
            showStatusBar();

        }*//*


    }

    public int getActionBarVisibility() {

        return mActionbarHolder.getVisibility();
    }


    public void showActionBar() {

        //   mActionbarHolder.setVisibility(View.VISIBLE);

    }


    public void hideActionBar() {

        if (popupMenu != null) {
            popupMenu.dismiss();
        }
        mActionbarHolder.setVisibility(View.GONE);

    }


    public void showStatusBar() {

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }

    public void hideStatusBar() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    public void ShowSettingsButton() {

        mSettings.setVisibility(View.VISIBLE);
    }

    public void HideSettingsButton() {

        mSettings.setVisibility(View.INVISIBLE);
    }


    public void HideActionBarControll() {

        if (mActionbarHolder.getVisibility() == View.VISIBLE) {

            if (popupMenu != null) {
                popupMenu.dismiss();
            }
            mActionbarHolder.setVisibility(View.GONE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }

    }

    public void showErrorDialogue(String message) {

        showErrorDialog(null, message);
    }

    public void ShowAlert(String message) {

        ShowAlertDialogue(null, message);
    }


    public void ShowAlertDialogue(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);

        if (title != null && title.length() > 0) {
            // We can show an alert dialog with an empty title. Hence this check.
            builder.setTitle(title);
        }

        // Generic error dialogs are used for information only. User only clicks on OK to dismiss it.
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (AssetPlayerPager.getCurrentItem() + 1 < digitalAssetsList.size()) {
                    AssetPlayerPager.setCurrentItem(AssetPlayerPager.getCurrentItem() + 1);
                }

            }
        });

        mAlertDialog = builder.create();
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();


    }


    public void showErrorDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);

        if (title != null && title.length() > 0) {
            // We can show an alert dialog with an empty title. Hence this check.
            builder.setTitle(title);
        }

        // Generic error dialogs are used for information only. User only clicks on OK to dismiss it.
        builder.setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        mAlertDialog = builder.create();
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }


    private static String displayTimeZone(TimeZone tz) {

        long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset())
                - TimeUnit.HOURS.toMinutes(hours);
        // avoid -4:-30 issue
        minutes = Math.abs(minutes);

        String result = "";
        if (hours > 0) {
            result = String.format("+%d:%02d ", hours, minutes);
        } else {
            result = String.format("-%d:%02d ", hours, minutes);
        }

        return result;

    }


    public void UpdateBrightness(int brightness) {

        if (brightness < 0) {

            brightness = 20;
        } else if (brightness > 255) {

            brightness = 255;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {

                Settings.System.putInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
                //Get the current window attributes
                WindowManager.LayoutParams layoutpars = window.getAttributes();
                //Set the brightness of this window
                layoutpars.screenBrightness = brightness / (float) 255;
                //Apply attribute changes to this window
                window.setAttributes(layoutpars);
            }
        } else {


            Settings.System.putInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
            //Get the current window attributes
            WindowManager.LayoutParams layoutpars = window.getAttributes();
            //Set the brightness of this window
            layoutpars.screenBrightness = brightness / (float) 255;
            //Apply attribute changes to this window
            window.setAttributes(layoutpars);


        }


    }

    public int getBrightnessvalue() {

        return brightness;
    }

    public void showAlerDialogue() {

        final CharSequence options[] = new CharSequence[]{"Goto Settings", "Next Asset", "Close"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.error_network_alert));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                switch (options[which].toString()) {

                    case "Goto Settings":

                        startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));

                        break;

                    case "Next Asset":

                        AssetPlayBackPositionChange(CurrentDigigtalAsset + 1, true);

                        break;

                    case "Close":

                        onBackPressed();
                        break;

                    default:

                        break;

                }


            }
        });
        if (!isFinishing()) {
            builder.show();
        }

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        SetPdfAction(item.getItemId());
        return true;
    }


    public void ShowGestureLayerHolder(int CurrentPosition) {

        CurrentGesturePosition = CurrentPosition;

        if (customer != null) {
            mGesture_view_holder.setVisibility(View.VISIBLE);
        }

    }

    public void hideGestureLayerHolder() {

        mGesture_view_holder.setVisibility(View.GONE);

    }

    public void UpdateVideoStartedState() {


        if (!PreferenceUtils.getIsVideoGuideCompleted(this)) {

            mBrightnessTipText.setVisibility(View.VISIBLE);
            mVolumeTipText.setText("Volume");
            mVolumeTipText.setVisibility(View.VISIBLE);
            mAssetTipLayout.setVisibility(View.VISIBLE);


        }

    }


    public void UpdateImageGuideCompleted() {

        if (!PreferenceUtils.getIsImageGuideCompleted(this))
            UpdateOtherViewToolTip();

    }


    public void UpdateWebviewGuideCompleted() {

        if (!PreferenceUtils.getIsPlayerGuideCompleted(this))
            UpdateOtherViewToolTip();


    }

    public void UpdateAudioGuideCompleted() {

        if (!PreferenceUtils.getIsAudioGuideCompleted(this)) {

            mBrightnessTipText.setVisibility(View.VISIBLE);
            mVolumeTipText.setText("Volume");
            mVolumeTipText.setVisibility(View.VISIBLE);
            mAssetTipLayout.setVisibility(View.VISIBLE);

        }

    }


    public void UpdateOtherViewToolTip() {

        mBrightnessTipText.setVisibility(View.GONE);
        mVolumeTipText.setVisibility(View.GONE);
        mAssetTipLayout.setVisibility(View.VISIBLE);

    }


    public void UpdatePdfLoaded() {

        if (!PreferenceUtils.getIsPdfGuideCompleted(this)) {

            mBrightnessTipText.setVisibility(View.GONE);
            mVolumeTipText.setVisibility(View.VISIBLE);
            mVolumeTipText.setText("Swipe up/Down to navigate pages");
            mAssetTipLayout.setVisibility(View.VISIBLE);

        }

    }

    class GestureTap extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("onSingleTap_test :", "" + e.getAction());
            mAssetTipLayout.setVisibility(View.GONE);
            return true;
        }
    }


}
*/
