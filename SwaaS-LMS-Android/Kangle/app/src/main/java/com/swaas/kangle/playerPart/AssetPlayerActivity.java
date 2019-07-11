package com.swaas.kangle.playerPart;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
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
import com.swaas.kangle.R;
import com.swaas.kangle.db.DigitalAssetRepository;
import com.swaas.kangle.playerPart.adapter.PageSlideAdapter;
import com.swaas.kangle.playerPart.customviews.MyAssetPlayerViewPager;
import com.swaas.kangle.playerPart.customviews.pdf.util.Constants;
import com.swaas.kangle.playerPart.fragments.PdfViewFragment;
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
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class AssetPlayerActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, OnAssetChangeListener, MyAssetPlayerViewPager.OnSwipeOutListener, PopupMenu.OnMenuItemClickListener {


    public MyAssetPlayerViewPager AssetPlayerPager;
    public PageSlideAdapter pageSlideAdapter;
    public int CurrentDigigtalAsset;
    public int Endpostion_interept = 10;
    public GPSTracker gpsTracker;
    public ContentResolver mContentResolver;
    public Window window;
    public TextView mAssetCount;
    public ImageView mBack,closebutton;
    private boolean isVideoStarted = false, isVideoNotStarted = false, isPdfViewStarted = false;
    public  int brightness;
    private DateHelper dateHelper;
    public  int isPreview;
    public boolean videocomplete = false;
    public PopupMenu popupMenu;
    public DigitalAssetRepository mDigitalAssetRepository;
    public List<DigitalAssets> digitalAssetsList,digitalAssetListFeedback;
    protected AlertDialog mAlertDialog;
    private DateFormat df,df1;
    private ImageView mCompanyLogo;
    private Toast mToast;
    //private Story storyObject;
    private com.github.florent37.viewanimator.ViewAnimator viewAnimator;
    private boolean isToastVisible = false;
    public DigitalAssets mCurrentAssetObject;
    private GestureDetector detector;
    public RelativeLayout mGesture_view_holder;
    private TextView mAssetGotItTipTextview;
    private GestureLibrary gLib;
    private RelativeLayout mActionbarHolder;
    private RelativeLayout mAssetTipLayout;
    private ImageView mAssetCloseImage;
    public TextView mVolumeTipText,mBrightnessTipText,mSeekbarTipText;
    private TextView mBtn_GestureOverlay_Close,mBtn_GestureOverlay_Closeview;
    public int CurrentGesturePosition;
    private ImageView mGestureImage;
    private int CourseId, PublishId, SectionId;
    public LinearLayout mTransparentView, mAssetListHolder;
    public ImageView mIcDrawerOpenLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_asset_player);
       //getSupportActionBar().hide();
        gLib = GestureLibraries.fromRawResource(this,R.raw.gesture);
        gLib.load();
        getDigitalAssetList();
        SetClickListener();
        settingPermission();
        GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gesturesview);
        gestures.addOnGesturePerformedListener(handleGestureListener);
        gestures.setGestureStrokeAngleThreshold(90.0f);
        detector = new GestureDetector(this, new GestureTap());


    }


    private GestureOverlayView.OnGesturePerformedListener handleGestureListener = new GestureOverlayView.OnGesturePerformedListener() {
        @Override
        public void onGesturePerformed(GestureOverlayView gestureView,
                                       Gesture gesture) {

            ArrayList<Prediction> predictions = gLib.recognize(gesture);

            // one prediction needed
            if (predictions.size() > 0) {
                Prediction prediction = predictions.get(0);
                // checking prediction
                if (prediction.score > 1.0) {
                    // and action

                    if (prediction.name.equals("lineupdown")){

                        mGestureImage.setVisibility(View.VISIBLE);
                        mGestureImage.setImageResource(R.mipmap.ic_avg);
                        viewAnimator = com.github.florent37.viewanimator.ViewAnimator
                                .animate(mGestureImage)
                                .bounceIn()
                                .onStop(new AnimationListener.Stop() {
                                    @Override
                                    public void onStop() {

                                        Log.d("=>test",""+digitalAssetsList.get(CurrentGesturePosition).getDA_Name());
                                        mGestureImage.setVisibility(View.GONE);

                                    }
                                })
                                .start();



                    }else if (prediction.name.equals("like ")) {

                       // UpdateFeedback(digitalAssetsList.get(CurrentGesturePosition),2);
                        mGestureImage.setVisibility(View.VISIBLE);
                        mGestureImage.setImageResource(R.mipmap.ic_like);
                        viewAnimator = com.github.florent37.viewanimator.ViewAnimator
                                .animate(mGestureImage)
                                .bounceIn()
                                .onStop(new AnimationListener.Stop() {
                                    @Override
                                    public void onStop() {

                                        Log.d("=>test",""+digitalAssetsList.get(CurrentGesturePosition).getDA_Name());
                                        mGestureImage.setVisibility(View.GONE);


                                    }
                                })
                                .start();

                    }else if (prediction.name.equals("dislike ")){

                        mGestureImage.setVisibility(View.GONE);
                        mGestureImage.setVisibility(View.VISIBLE);
                        mGestureImage.setImageResource(R.mipmap.ic_dislike);
                        viewAnimator = com.github.florent37.viewanimator.ViewAnimator
                                .animate(mGestureImage)
                                .bounceIn()
                                .onStop(new AnimationListener.Stop() {
                                    @Override
                                    public void onStop() {

                                        Log.d("=>test","stopped");
                                        mGestureImage.setVisibility(View.GONE);
                                    }
                                })
                                .start();


                    }else {

                        Toast.makeText(AssetPlayerActivity.this, prediction.name, Toast.LENGTH_SHORT).show();
                    }

                    hideGestureLayerHolder();

                }
            }
        }
    };

    public void settingPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                showBrightnessSettingAlert();
            }
        }else {

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
                startActivityForResult(intent, 200);


            }
        });

        mAlertDialog = builder.create();
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();

    }


    private void getBrightness() {

        mContentResolver= getContentResolver();
        //Get the current window
        window = getWindow();

        try
        {
            // To handle the auto
            Settings.System.putInt(mContentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            //Get the current system brightness
            brightness = Settings.System.getInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS);

            Log.d("=>brightness",""+brightness);


        }
        catch (Settings.SettingNotFoundException e)
        {
            //Throw an error case it couldn't be retrieved
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }

    }


    private void SetClickListener() {


        mAssetTipLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                detector.onTouchEvent(event);

                return true;

            }
        });


        mAssetGotItTipTextview.setOnClickListener(this);
        mBack.setOnClickListener(this);
        closebutton.setOnClickListener(this);

    }


//    public Set<String> areas(final List<Employee> employees) {
//        Set<String> areas = new HashSet<>();
//        for(final Employee employee: employees) {
//            areas.add(employee.getArea());
//        }
//        return areas;
//    }


    private void ViewInialization() {

        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df1 =  new SimpleDateFormat("yyyy-MM-dd");
        mDigitalAssetRepository = new DigitalAssetRepository(this);
        mToast =  new Toast(this);
        gpsTracker = new GPSTracker(AssetPlayerActivity.this);
        AssetPlayerPager = (MyAssetPlayerViewPager) findViewById(R.id.asset_slider);
        AssetPlayerPager.setOnSwipeOutListener(this);
        mBack = (ImageView) findViewById(R.id.asset_player_back);
        mAssetCount = (TextView) findViewById(R.id.asset_count);
        mGestureImage = (ImageView) findViewById(R.id.image_Gesture);
        mGesture_view_holder = (RelativeLayout) findViewById(R.id.gesture_view_holder);
        mAssetTipLayout = (RelativeLayout) findViewById(R.id.tip_for_asset_swipe);
        mBtn_GestureOverlay_Closeview = (TextView) findViewById(R.id.btn_close_gesture_view_holder);
        mActionbarHolder = (RelativeLayout) findViewById(R.id.actionbar_holder);
        mVolumeTipText = (TextView) findViewById(R.id.volume_tip_text);
        mBrightnessTipText = (TextView) findViewById(R.id.brightness_tip_text);
        mSeekbarTipText = (TextView) findViewById(R.id.seekbar_tip_text);
        mAssetGotItTipTextview = (TextView) findViewById(R.id.btn_got_it);
        PreferenceUtils.setIsGestureEnabled(this,true);
        pageSlideAdapter = new PageSlideAdapter(getSupportFragmentManager(), digitalAssetsList, CurrentDigigtalAsset);
        AssetPlayerPager.setAdapter(pageSlideAdapter);
        AssetPlayerPager.setOnPageChangeListener(this);
        setCurrentSelectedItem();
        mIcDrawerOpenLayout = (ImageView) findViewById(R.id.drop_list_forward);
        closebutton = (ImageView) findViewById(R.id.close_button);
        mTransparentView = (LinearLayout) findViewById(R.id.transparent_view);
        mAssetListHolder = (LinearLayout) findViewById(R.id.asset_list_holder);
        mTransparentView.setOnClickListener(this);
        mIcDrawerOpenLayout.setOnClickListener(this);
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


            CourseId = getIntent().getIntExtra(com.swaas.kangle.utils.Constants.Course_Id,0);
            PublishId =  getIntent().getIntExtra(com.swaas.kangle.utils.Constants.Publish_Id,0);
            SectionId =  getIntent().getIntExtra(com.swaas.kangle.utils.Constants.Section_Id,0);



            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();

             digitalAssetsList= (List<DigitalAssets>)bundle.getSerializable("value");

            //digitalAssetsList = (ArrayList<DigitalAssets>) getIntent().getSerializableExtra(Constants.LIST);
            CurrentDigigtalAsset = getIntent().getIntExtra(Constants.POSITION, 0);

     }

        ViewInialization();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.transparent_view:
                mAssetListHolder.setVisibility(View.GONE);

                break;
            case R.id.asset_player_back:
                onBackPressed();
                break;

            case R.id.drop_list_forward:
                mAssetListHolder.setVisibility(View.VISIBLE);
                break;

            //closebutton included 08-feb-2018
            case R.id.close_button:
                onBackPressed();
                break;

            case R.id.btn_close_gesture_view_holder:
                hideGestureLayerHolder();
                break;


            case R.id.btn_got_it:

                mAssetTipLayout.setVisibility(View.GONE);


                switch (digitalAssetsList.get(CurrentDigigtalAsset).getDA_Type()){

                    case Constants.IMAGEASSET:

                        PreferenceUtils.setIsImageGuideCompleted(this,true);

                        break;

                    case  Constants.AUDIOASSET:


                        PreferenceUtils.setIsAudioGuideCompleted(this,true);
                        Log.d("Gotit", "audio");


                        break;

                    case Constants.HTMLASSET:


                        PreferenceUtils.setIsPlayerGuideCompleted(this,true);
                        Log.d("Gotit", "html");


                        break;

                    case Constants.VIDEOASSET:

                        //  PreferenceUtils.setIsVideoGuideCompleted(this,true);

                        Log.d("Gotit", "video");
                        PreferenceUtils.setIsVideoGuideCompleted(this,true);


                        break;

                    case Constants.PDFASSET:

                        Log.d("Gotit", "pdf");
                        PreferenceUtils.setIsPdfGuideCompleted(this,true);

                        break;

                    default:

                        break;


                }


                /*if (mTipGuideTimer != null){
                    mTipGuideTimer.cancel();
                }*/

                break;




            default:

                break;

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent returnIntent = new Intent();
//        returnIntent.putExtra("_ID",mCurrentAssetObject.ID);
//        returnIntent.putExtra("DA_code",mCurrentAssetObject.getDA_Code());
//        returnIntent.putExtra("DA_type",mCurrentAssetObject.getDA_Type());
//        setResult(Activity.RESULT_OK,returnIntent);
//        finish();
    }

    private void SetPdfAction(int itemId) {

        try {
            PdfViewFragment pdfViewFragment = (com.swaas.kangle.playerPart.fragments.PdfViewFragment) pageSlideAdapter.getFragment(AssetPlayerPager.getCurrentItem());
            if (pdfViewFragment!= null){
                pdfViewFragment.showMenu(itemId);
            }

        }catch (Exception e){

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (Settings.System.canWrite(this)){
                getBrightness();

            }else {
            }
        }else {
            getBrightness();
        }


/*
        List<Employee> employeeList = new ArrayList<>();

        Employee employeezero = new Employee();
        employeezero.setArea("Guindy");
        employeezero.setName("hari");

        Employee employeeone = new Employee();
        employeeone.setArea("Gundy");
        employeeone.setName("harione");

        Employee employeethree = new Employee();
        employeethree.setArea("Ekkattuthangal");
        employeethree.setName("harione");

        employeeList.add(employeezero);
        employeeList.add(employeeone);
        employeeList.add(employeethree);

        Log.d("<==>Size",""+areas(employeeList).size());

        Set<String> hash = areas(employeeList);

      */


    }




    @Override
    protected void onPause() {
        super.onPause();
        Log.d("=>position_showeditem", AssetPlayerPager.getCurrentItem() + "");
        UpdateDetails();

        if (isToastVisible){
            mToast.cancel();
            isToastVisible = false;
        }


    }

    private void UpdateDetails() {

        if (digitalAssetsList.get(AssetPlayerPager.getCurrentItem()).getPlayer_Start_Time()!=null){

            if (digitalAssetsList.get(AssetPlayerPager.getCurrentItem()).getDA_Type() == Constants.VIDEOASSET){

                UpdatePlayerEndTime(AssetPlayerPager.getCurrentItem(),Endpostion_interept/1000);

            }else if (digitalAssetsList.get(AssetPlayerPager.getCurrentItem()).getDA_Type() == Constants.AUDIOASSET){

                UpdatePlayerEndTime(AssetPlayerPager.getCurrentItem(),Endpostion_interept/1000);

            }else if (digitalAssetsList.get(AssetPlayerPager.getCurrentItem()).getDA_Type() == Constants.BRIGHTCOVE){

                UpdateBrightCovePlayerEndTime(AssetPlayerPager.getCurrentItem(),Calendar.getInstance().getTime());

            }
            else if (digitalAssetsList.get(AssetPlayerPager.getCurrentItem()).getDA_Type()== Constants.IMAGEASSET){

                UpdateImagePlayerEndtime(AssetPlayerPager.getCurrentItem(),Calendar.getInstance().getTime());
            }
            else if (digitalAssetsList.get(AssetPlayerPager.getCurrentItem()).getDA_Type() == Constants.PDFASSET){

                UpdatePageEndTime(AssetPlayerPager.getCurrentItem(),Calendar.getInstance().getTime());
            }
            else {

                InsertFinalParturlEndTime(AssetPlayerPager.getCurrentItem(),Calendar.getInstance().getTime());

            }

        }else {



        }
        if (digitalAssetsList.get(AssetPlayerPager.getCurrentItem()).getDetailed_StartTime()!=null){

            UpdateEdetailEndTime(Calendar.getInstance().getTime(),AssetPlayerPager.getCurrentItem());

        }

    }



    public void AssetPlayBackPositionChange(int position, boolean state) {

        if (position>=0&&position<digitalAssetsList.size()){
            AssetPlayerPager.setCurrentItem(position);
        }else {
            if (position>=digitalAssetsList.size()){

                if (state){
                    onBackPressed();
                }
                mToast.makeText(this,getString(R.string.lastpagealert),Toast.LENGTH_SHORT).show();

            }else {

                if (state){
                    onBackPressed();
                }
                mToast.makeText(this,getString(R.string.firstpagealert),Toast.LENGTH_SHORT).show();

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

        mCurrentAssetObject = digitalAssetsList.get(position);
        CurrentDigigtalAsset = position;
        mAssetCount.setText((position+1)+"/"+digitalAssetsList.size());

    }


    public int getCurrentSessionId(int currentAssetPosition) {

        return mDigitalAssetRepository.getAssetSessionId(digitalAssetsList.get(currentAssetPosition).getDA_Code(),CourseId,SectionId);

    }


    public boolean InsertEdetailStarttime(Date edetailstarttime, int SessionId, int currentAssetPosition) {
        if (SessionId!=0){
            digitalAssetsList.get(currentAssetPosition).setSessionId(SessionId);
        }else {
            digitalAssetsList.get(currentAssetPosition).setSessionId(getCurrentSessionId(currentAssetPosition)+1);
        }
        if (edetailstarttime != null ){
            digitalAssetsList.get(currentAssetPosition).setDetailed_StartTime(df.format(edetailstarttime));

        }
        return true;
    }


    public boolean InsertPlayerStartTime(int playerstarttime, int currentAssetPosition,int playmode) {

        digitalAssetsList.get(currentAssetPosition).setPart_Id(1);
        digitalAssetsList.get(currentAssetPosition).setPlayMode(playmode);
        digitalAssetsList.get(currentAssetPosition).setPlayer_Start_Time(playerstarttime + "");
        setCustomerDetail(currentAssetPosition);
        mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentAssetPosition), false);
        return true;

    }

    public void UpdatePlayerEndTime(int currentPosition, int playerendtime) {

        if (digitalAssetsList.get(currentPosition).getPlayer_Start_Time()!=null){
            digitalAssetsList.get(currentPosition).setPlayed_Time_Duration(getVideoTimeDuration(digitalAssetsList.get(currentPosition).getPlayer_Start_Time(),playerendtime));
            digitalAssetsList.get(currentPosition).setPlayer_End_Time(playerendtime + "");
        }
    }

    public void UpdateBrightCovePlayerEndTime(int currentPosition, Date parturlendtime) {

        digitalAssetsList.get(currentPosition).setPlayed_Time_Duration(getDifferenceInSeconds(digitalAssetsList.get(currentPosition).getDetailed_StartTime(),parturlendtime));
        digitalAssetsList.get(currentPosition).setPlayer_End_Time(""+getDifferenceInSeconds(digitalAssetsList.get(currentPosition).getDetailed_StartTime(),parturlendtime));

    }

    private long getVideoTimeDuration(String player_start_time, int playerendtime) {

        int starttime =  Integer.parseInt(player_start_time);
        long difference = 0;
        if (starttime > playerendtime){

            difference = starttime  - playerendtime;

        }else {

            difference =  playerendtime - starttime ;

        }

        return difference;
    }


    public void UpdateEdetailEndTime(Date edetailendtime, int currentAssetPosition) {

        if (edetailendtime != null){
            digitalAssetsList.get(currentAssetPosition).setDetailed_EndTime(df.format(edetailendtime) + "");
            mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentAssetPosition), false);
            digitalAssetsList.get(currentAssetPosition).setId(0);
            digitalAssetsList.get(currentAssetPosition).setPlayer_Start_Time(null);
            digitalAssetsList.get(currentAssetPosition).setPlayer_End_Time(null);
            digitalAssetsList.get(currentAssetPosition).setPlayed_Time_Duration(0);
            digitalAssetsList.get(currentAssetPosition).setDetailed_StartTime(null);
            digitalAssetsList.get(currentAssetPosition).setDetailed_EndTime(null);

        }


    }


    public void MultiUpdateEndTime(int endposition, int currentAssetPosition) {
        if (digitalAssetsList.get(currentAssetPosition).getPlayer_Start_Time()!=null){
            digitalAssetsList.get(currentAssetPosition).setPlayed_Time_Duration(getVideoTimeDuration(digitalAssetsList.get(currentAssetPosition).getPlayer_Start_Time(),endposition));
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




    public void endtimeUpdateOnComplete(int endtime,int currentAssetPosition){

        if (digitalAssetsList.get(currentAssetPosition).getPlayer_Start_Time()!=null)
        {
            digitalAssetsList.get(currentAssetPosition).setPlayer_End_Time("" + endtime);
            mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentAssetPosition), false);
        }
        videocomplete = true;

    }


    public void InsertImageDetailStartTime(int currentassetposition, Date starttime, int sessionId) {

        if (sessionId!= 0 ){
            digitalAssetsList.get(currentassetposition).setSessionId(getCurrentSessionId(currentassetposition));
        }else {

            digitalAssetsList.get(currentassetposition).setSessionId(mDigitalAssetRepository.getAssetSessionId(digitalAssetsList.get(currentassetposition).getDA_Code(),CourseId,SectionId)+1);
        }
        digitalAssetsList.get(currentassetposition).setPart_Id(1);
        if (starttime != null){
            digitalAssetsList.get(currentassetposition).setDetailed_StartTime(df.format(starttime));
        }
        setCustomerDetail(currentassetposition);
    }

    private void setCustomerDetail(int currentassetposition) {


        digitalAssetsList.get(currentassetposition).setCourse_Id(CourseId);
        digitalAssetsList.get(currentassetposition).setSection_Id(SectionId);
        digitalAssetsList.get(currentassetposition).setPublish_Id(PublishId);

     /*   if (customer!=null) {

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
            if(storyObject != null){

                if (storyObject.getStory_From() == DigitalAssetRepository.MODE_MC){

                    digitalAssetsList.get(currentassetposition).setMC_StoryID(storyObject.getStory_Id());

                }else {

                    digitalAssetsList.get(currentassetposition).setUD_StoryID(storyObject.getStory_Id());

                }

            }

        }else {

            digitalAssetsList.get(currentassetposition).setIsPreview(1);
            digitalAssetsList.get(currentassetposition).setLatitude(gpsTracker.getLatitude());
            digitalAssetsList.get(currentassetposition).setLongitude(gpsTracker.getLongitude());
            digitalAssetsList.get(currentassetposition).setCustomer_Code("");
            digitalAssetsList.get(currentassetposition).setDetailed_DateTime(DateHelper.getCurrentDate());
            digitalAssetsList.get(currentassetposition).setTime_Zone(displayTimeZone(TimeZone.getDefault()));

        }
*/
    }



    public void InsertPartUrlStartTimeEvent(int currentassetpsition,Date parturlstarttime,String parturl){

        setCustomerDetail(currentassetpsition);
        digitalAssetsList.get(currentassetpsition).setPlayer_Start_Time("0");
        digitalAssetsList.get(currentassetpsition).setPart_URL(parturl);
       mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetpsition), false);

        if(parturlstarttime != null ){
            digitalAssetsList.get(currentassetpsition).setPlayer_Start_Time(df.format(parturlstarttime));
        }


    }

    public void UpdateImagePlayerStarttime(int currentassetposition,Date starttime,int playmode){

        digitalAssetsList.get(currentassetposition).setPlayer_Start_Time("0");
        digitalAssetsList.get(currentassetposition).setPlayMode(playmode);
        mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetposition), false);
        if (starttime != null){

            digitalAssetsList.get(currentassetposition).setPlayer_Start_Time(df.format(starttime));

        }

    }

    public void UpdateImagePlayerEndtime(int currentassetposition, Date starttime) {


        if (digitalAssetsList.get(currentassetposition).getPlayer_Start_Time()!=null){

            digitalAssetsList.get(currentassetposition).setPlayed_Time_Duration(getDifferenceInSeconds(digitalAssetsList.get(currentassetposition).getPlayer_Start_Time(),starttime));
            digitalAssetsList.get(currentassetposition).setPlayer_End_Time(getDifferenceInSeconds(digitalAssetsList.get(currentassetposition).getPlayer_Start_Time(),starttime)+"");
            digitalAssetsList.get(currentassetposition).setPlayer_Start_Time("0");
        }
        // digitalAssetsList.get(currentassetposition).setPlayer_End_Time(df.format(starttime));

    }

    private long getDifferenceInSeconds(String player_start_time, Date endtime) {

        Date stardate = null;
        long diffInSec = 0,diffInMs;
        try {

            stardate = df.parse(player_start_time);

        } catch (ParseException e) {
            e.printStackTrace();

        }

        if (endtime == null|| stardate == null)
        {
            diffInSec = 10;
        }else {

            diffInMs  = endtime.getTime() - stardate.getTime();
            diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);

        }

        return diffInSec;

    }

    public void InsertHtmlDetailStarttime(int currentassetposition,Date starttime){

        if (starttime != null){
            setCustomerDetail(currentassetposition);
            digitalAssetsList.get(currentassetposition).setDetailed_StartTime(df.format(starttime));
            digitalAssetsList.get(currentassetposition).setSessionId(getCurrentSessionId(currentassetposition)+1);
            mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetposition), false);

        }else {

            setCustomerDetail(currentassetposition);
            digitalAssetsList.get(currentassetposition).setDetailed_StartTime(df.format(Calendar.getInstance().getTime()));
            digitalAssetsList.get(currentassetposition).setSessionId(getCurrentSessionId(currentassetposition)+1);
            mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetposition), false);


        }


    }



    public void InsertPartUrlStartTime(int currentassetpsition,Date parturlstarttime,String parturl){

        setCustomerDetail(currentassetpsition);
        digitalAssetsList.get(currentassetpsition).setPlayer_Start_Time("0");
        digitalAssetsList.get(currentassetpsition).setPart_URL(parturl);
        mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetpsition), false);

        if(parturlstarttime != null ){
            digitalAssetsList.get(currentassetpsition).setPlayer_Start_Time(df.format(parturlstarttime));
        }


    }


    public void InsertFinalParturlEndTime(int currentposition,Date parturlendtime){

        if (digitalAssetsList.get(currentposition).getPlayer_Start_Time()!=null){
            digitalAssetsList.get(currentposition).setPlayed_Time_Duration(getDifferenceInSeconds(digitalAssetsList.get(currentposition).getPlayer_Start_Time(),parturlendtime));
            digitalAssetsList.get(currentposition).setPlayer_End_Time(""+getDifferenceInSeconds(digitalAssetsList.get(currentposition).getPlayer_Start_Time(),parturlendtime));
            digitalAssetsList.get(currentposition).setPlayer_Start_Time("0");
           mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentposition), false);

        }

    }


    public void InsertParturlEndTime(int currentassetpsition,Date parturlendtime){

        if (digitalAssetsList.get(currentassetpsition).getPlayer_Start_Time()!=null){

            digitalAssetsList.get(currentassetpsition).setPlayed_Time_Duration(getDifferenceInSeconds(digitalAssetsList.get(currentassetpsition).getPlayer_Start_Time(),parturlendtime));
            digitalAssetsList.get(currentassetpsition).setPlayer_End_Time(""+getDifferenceInSeconds(digitalAssetsList.get(currentassetpsition).getPlayer_Start_Time(),parturlendtime));
            digitalAssetsList.get(currentassetpsition).setPlayer_Start_Time("0");
            mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetpsition), false);
            digitalAssetsList.get(currentassetpsition).setId(0);
            digitalAssetsList.get(currentassetpsition).setPlayer_End_Time(null);
            digitalAssetsList.get(currentassetpsition).setPlayer_Start_Time(null);
        }

    }

    public void InsertUrlEndTime(int currentassetpsition,Date parturlendtime){

        if (digitalAssetsList.get(currentassetpsition).getPlayer_Start_Time()!=null){

            digitalAssetsList.get(currentassetpsition).setPlayed_Time_Duration(getDifferenceInSeconds(digitalAssetsList.get(currentassetpsition).getPlayer_Start_Time(),parturlendtime));
            digitalAssetsList.get(currentassetpsition).setPlayer_End_Time(""+getDifferenceInSeconds(digitalAssetsList.get(currentassetpsition).getPlayer_Start_Time(),parturlendtime));
            digitalAssetsList.get(currentassetpsition).setPlayer_Start_Time("0");
            mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetpsition), false);

        }



    }
    public void InsertHtmlDetailEndtime(int currentassetposition,Date endtime){

        digitalAssetsList.get(currentassetposition).setDetailed_EndTime(df.format(endtime));
        mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetposition), false);
        digitalAssetsList.get(currentassetposition).setId(0);
        digitalAssetsList.get(currentassetposition).setDetailed_StartTime(null);
        digitalAssetsList.get(currentassetposition).setDetailed_EndTime(null);
        digitalAssetsList.get(currentassetposition).setPart_URL(null);
        digitalAssetsList.get(currentassetposition).setPlayer_Start_Time(null);
        digitalAssetsList.get(currentassetposition).setPlayer_End_Time(null);


    }

    public void UpdateImageDetailEndTime(int currentassetposition, Date endtime) {

        if (digitalAssetsList.get(currentassetposition).getDetailed_StartTime()!=null){

            if (digitalAssetsList.get(currentassetposition).getPlayer_Start_Time()!= null){

                if (!digitalAssetsList.get(currentassetposition).getPlayer_Start_Time().equals("0")){
                    digitalAssetsList.get(currentassetposition).setPlayer_Start_Time("");
                }
            }
            digitalAssetsList.get(currentassetposition).setDetailed_EndTime(df.format(endtime));
           mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetposition), false);
        }
        digitalAssetsList.get(currentassetposition).setId(0);
        digitalAssetsList.get(currentassetposition).setSessionId(digitalAssetsList.get(currentassetposition).getSessionId()+1);
        digitalAssetsList.get(currentassetposition).setDetailed_EndTime(null);
        digitalAssetsList.get(currentassetposition).setDetailed_StartTime(null);
        digitalAssetsList.get(currentassetposition).setPlayer_End_Time(null);
        digitalAssetsList.get(currentassetposition).setPlayer_Start_Time(null);

    }


    public void InsertPdfDetailStartTime(int currentassetposition, Date DetailedStartime){

        digitalAssetsList.get(currentassetposition).setDetailed_StartTime(df.format(DetailedStartime));
        digitalAssetsList.get(currentassetposition).setSessionId(getCurrentSessionId(currentassetposition)+1);
        setCustomerDetail(currentassetposition);

    }


    public void UpdatePageStartTime(int currentassetposition,Date pagestarttime,int page,int playmode){

        digitalAssetsList.get(currentassetposition).setPart_Id(page);
        digitalAssetsList.get(currentassetposition).setPlayMode(playmode);
        digitalAssetsList.get(currentassetposition).setPlayer_Start_Time("0");
        mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetposition),false);
        digitalAssetsList.get(currentassetposition).setPlayer_Start_Time(df.format(pagestarttime));


    }


    public void UpdateMultiPageStartTime(int currentassetposition,Date pagestarttime,int page,int playmode){

        digitalAssetsList.get(currentassetposition).setPlayer_Start_Time(null);
        digitalAssetsList.get(currentassetposition).setPlayer_End_Time(null);
        digitalAssetsList.get(currentassetposition).setId(0);
        digitalAssetsList.get(currentassetposition).setPart_Id(page);
        digitalAssetsList.get(currentassetposition).setPlayer_Start_Time("0");
        digitalAssetsList.get(currentassetposition).setPlayMode(playmode);
        mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetposition),false);
        digitalAssetsList.get(currentassetposition).setPlayer_Start_Time(df.format(pagestarttime));

    }

    public void UpdatePageEndTime(int currentassetposition,Date pageendtime){

        if (digitalAssetsList.get(currentassetposition).getPlayer_Start_Time()!=null){

            digitalAssetsList.get(currentassetposition).setPlayed_Time_Duration(getDifferenceInSeconds(digitalAssetsList.get(currentassetposition).getPlayer_Start_Time(),pageendtime));
            digitalAssetsList.get(currentassetposition).setPlayer_End_Time(getDifferenceInSeconds(digitalAssetsList.get(currentassetposition).getPlayer_Start_Time(),pageendtime)+"");
            digitalAssetsList.get(currentassetposition).setPlayer_Start_Time("0");
            mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetposition),false);


        }

    }

    public void InsertPdfDetailEndTime(int currentassetposition,Date DetailedEndTime){

        digitalAssetsList.get(currentassetposition).setDetailed_EndTime(df.format(DetailedEndTime));
        mDigitalAssetRepository.insertOrUpdateDAAnalyticsDetails(digitalAssetsList.get(currentassetposition),false);
        digitalAssetsList.get(currentassetposition).setDetailed_EndTime(null);
        digitalAssetsList.get(currentassetposition).setDetailed_StartTime(null);
        digitalAssetsList.get(currentassetposition).setPlayMode(0);
        digitalAssetsList.get(currentassetposition).setPlayer_Start_Time(null);
        digitalAssetsList.get(currentassetposition).setPlayer_End_Time(null);
        digitalAssetsList.get(currentassetposition).setPart_Id(0);
        digitalAssetsList.get(currentassetposition).setId(0);

    }

    public void DeleteLessThanTwoSeconds(int currentassetposition){

       // mDigitalAssetRepository.deleteAssetAnalyticsBasedOnDuration(digitalAssetsList.get(currentassetposition).getDA_Code(),digitalAssetsList.get(currentassetposition).getSessionId(), Constant.DeletePdfPageAnalyticsSeconds);

    }


    @Override
    public void onSwipeOutAtStart() {

        if (!isToastVisible){

            mToast.makeText(this,getString(R.string.firstpagealert),Toast.LENGTH_SHORT).show();
            isToastVisible = true;
        }

    }

    @Override
    public void onSwipeOutAtEnd() {

        if (!isToastVisible){

            mToast.makeText(this,getString(R.string.lastpagealert),Toast.LENGTH_SHORT).show();
            isToastVisible = true;
        }


    }

    @Override
    public void onHideToast() {

        if (isToastVisible){
            mToast.cancel();
            isToastVisible = false;
        }

    }



    public void setCurrentPositon(int currentPosition) {

        Endpostion_interept = currentPosition;

    }


    public void ShowActionBarControll() {

        if (mActionbarHolder.getVisibility()==View.VISIBLE){

            if (popupMenu != null){
                popupMenu.dismiss();
            }

            mActionbarHolder.setVisibility(View.GONE);
            hideStatusBar();


        }else {

            mActionbarHolder.setVisibility(View.VISIBLE);
            showStatusBar();

        }

    }

    public int getActionBarVisibility(){

        return mActionbarHolder.getVisibility();
    }



    public void showActionBar(){

        mActionbarHolder.setVisibility(View.VISIBLE);

    }


    public void hideActionBar(){

        if (popupMenu != null){
            popupMenu.dismiss();
        }
        mActionbarHolder.setVisibility(View.GONE);

    }


    public void showStatusBar(){

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }

    public void hideStatusBar(){

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    public void ShowSettingsButton(){

        //     mSettings.setVisibility(View.VISIBLE);
    }

    public void HideSettingsButton(){

      //  mSettings.setVisibility(View.INVISIBLE);
    }



    public void HideActionBarControll(){

        if (mActionbarHolder.getVisibility()==View.VISIBLE){

            if (popupMenu != null){
                popupMenu.dismiss();
            }
            mActionbarHolder.setVisibility(View.GONE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }

    }

    public void showErrorDialogue(String message) {

        showErrorDialog(null, message);
    }

    public void ShowAlert(String message){

        ShowAlertDialogue(null,message);
    }


    public void ShowAlertDialogue(String title,String message){

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

                if (AssetPlayerPager.getCurrentItem()+1<digitalAssetsList.size()){
                    AssetPlayerPager.setCurrentItem(AssetPlayerPager.getCurrentItem()+1);
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


    public void UpdateBrightness(int brightness){

        if (brightness<0){

            brightness = 20;
        }else if (brightness>255){

            brightness = 255;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (Settings.System.canWrite(this)){

                Settings.System.putInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
                //Get the current window attributes
                WindowManager.LayoutParams layoutpars = window.getAttributes();
                //Set the brightness of this window
                layoutpars.screenBrightness = brightness / (float)255;
                //Apply attribute changes to this window
                window.setAttributes(layoutpars);
            }
        }else {


            Settings.System.putInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
            //Get the current window attributes
            WindowManager.LayoutParams layoutpars = window.getAttributes();
            //Set the brightness of this window
            layoutpars.screenBrightness = brightness / (float)255;
            //Apply attribute changes to this window
            window.setAttributes(layoutpars);


        }


    }

    public int getBrightnessvalue(){

        return brightness;
    }

    public void  showAlerDialogue(){

        final CharSequence options[] = new CharSequence[] {"Goto Settings", "Next Asset", "Close"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.error_network_alert));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                switch (options[which].toString()){

                    case "Goto Settings" :

                        startActivity(new Intent(Settings.ACTION_SETTINGS));

                        break;

                    case  "Next Asset":

                        AssetPlayBackPositionChange(CurrentDigigtalAsset+1,true);

                        break;

                    case  "Close":

                        onBackPressed();
                        break;

                    default:

                        break;

                }


            }
        });
        if (!isFinishing()){
            builder.show();
        }

    }



    @Override
    public boolean onMenuItemClick(MenuItem item) {
        SetPdfAction(item.getItemId());
        return true;
    }


    public void ShowGestureLayerHolder(int CurrentPosition){

        CurrentGesturePosition = CurrentPosition;
        mGesture_view_holder.setVisibility(View.VISIBLE);

    }

    public void hideGestureLayerHolder(){

        mGesture_view_holder.setVisibility(View.GONE);

    }



    public void UpdateVideoStartedState() {


        if (!PreferenceUtils.getIsVideoGuideCompleted(this)){

            mBrightnessTipText.setVisibility(View.VISIBLE);
            mVolumeTipText.setText("Volume");
            mVolumeTipText.setVisibility(View.VISIBLE);
            mAssetTipLayout.setVisibility(View.VISIBLE);

        }

    }



    public void UpdateImageGuideCompleted(){

        if (!PreferenceUtils.getIsImageGuideCompleted(this))
             UpdateOtherViewToolTip();

    }


    public void UpdateWebviewGuideCompleted(){

        if (!PreferenceUtils.getIsPlayerGuideCompleted(this))
            UpdateOtherViewToolTip();


    }

    public void UpdateAudioGuideCompleted(){

        if (!PreferenceUtils.getIsAudioGuideCompleted(this)){

            mBrightnessTipText.setVisibility(View.VISIBLE);
            mVolumeTipText.setText("Volume");
            mVolumeTipText.setVisibility(View.VISIBLE);
            mAssetTipLayout.setVisibility(View.VISIBLE);

        }

    }



    public void UpdateOtherViewToolTip(){

        mBrightnessTipText.setVisibility(View.GONE);
        mVolumeTipText.setVisibility(View.GONE);
        mAssetTipLayout.setVisibility(View.VISIBLE);

    }


    public void UpdatePdfLoaded() {

        if (!PreferenceUtils.getIsPdfGuideCompleted(this)){

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
