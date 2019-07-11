package com.swaas.kangle.LPCourse;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.swaas.kangle.API.model.LandingPageAccess;
import com.swaas.kangle.AssetListActivity;
import com.swaas.kangle.DigitalAssetPlayer.DigitalAssetPlayerActivity;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.LPCourse.Report.LPCourseReportActivity;
import com.swaas.kangle.LPCourse.Report.LPCourseReportSummaryActivity;
import com.swaas.kangle.LPCourse.model.AnwerUploadModel;
import com.swaas.kangle.LPCourse.model.CourseUserAnswers;
import com.swaas.kangle.LPCourse.model.CourseUserAssessDet;
import com.swaas.kangle.LPCourse.model.CourseUserAssessHeader;
import com.swaas.kangle.LPCourse.model.QuestionAndAnswerModel;
import com.swaas.kangle.LPCourse.model.QuestionAnswerListModel;
import com.swaas.kangle.LPCourse.model.QuestionBaseModel;
import com.swaas.kangle.LPCourse.model.QuestionCourseListModel;
import com.swaas.kangle.LPCourse.model.QuestionQuestionListModel;
import com.swaas.kangle.LPCourse.questionbuilder.QuestionActivity;
import com.swaas.kangle.R;
import com.swaas.kangle.UploadService.DigitalAssetAnalyticsUpSyncService;
import com.swaas.kangle.db.DigitalAssetTransactionRepository;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.playerPart.AssetPlayerActivity;
import com.swaas.kangle.playerPart.DigitalAssets;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.CommonUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.CustomFontTextView;
import com.swaas.kangle.utils.MessageDialog;
import com.swaas.kangle.utils.NetworkUtils;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.swaas.kangle.LPCourse.questionbuilder.QuestionActivity.compareArrays;

/**
 * Created by saiprasath on 8/10/2017.
 */

public class SectionActivity extends AppCompatActivity {

    SectionAdapter sectionAdapter;
    List<SectionModel> sectionModelList = new ArrayList<SectionModel>();
    EmptyRecyclerView recyclerView;
    ImageView companylogo;
    View mEmptyView;
    Context mContext;
    ProgressDialog mProgressDialog;
    int CourseId,PublishId;
    String CourseName,CourseThumbnail,CourseDescription;
    Retrofit retrofitAPI;
    LPCourseService lpService;
    String SubdomainName;
    int CompanyId;
    String offsetFromUtc;
    int UserId;
    TextView mTextTitle,text_title1;
    TextView mTextSubTitle;
    CourseModel cs;
    LinearLayout checklist_sec;

    AssetAnalyticsUpsynctoServer assetAnalyticsUpsynctoServer;
    private final int REQUEST_CODE_TO_UPDATE_ASSET = 1;
    //private boolean isSequenceEnabled = false;
    String courseStatus;
    boolean isFromDashboard;
    LinearLayout mainLinearLayout;

    TextView cat_value,tag_value,tag_text,cat_text;
    ImageView thumbnail,expanddes_cat,completecertificate;
    String catvalue,tagvalue;
    LinearLayout segmentprogresslayout,header;
    TextView completion_status;
    View desccattaglayout;
    int Course_Status_INT;

    String selectedSectionName = "";

    CardView cardViewLayout;
    TextView checklistReport;
    TextView certificate;
    MessageDialog messageDialog;

    CustomFontTextView no_of_assets,no_of_sections,no_of_questions,no_of_checklist;
    TextView enddatetime;
    Boolean isReportEnabled,ispending,isbackallowed;
    TextView reportbtn;

    private CountDownTimer mTestTimer;
    private TextView mTimerText,time_expiry_text;
    private long TimeAsMilli;
    RelativeLayout question_timer_holder;
    TextView section_name_text;
    Boolean istimerrunning = false;
    int QuestionLoadCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.section_recycler_layout);
        mContext = SectionActivity.this;
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        isbackallowed = false;
        assetAnalyticsUpsynctoServer = new AssetAnalyticsUpsynctoServer();
        assetAnalyticsUpsynctoServer.AssetAnalyticsUpsynctoServer(mContext,this);
        messageDialog = new MessageDialog(mContext);
        initializeView();
        if(getIntent() != null){
            cs = (CourseModel) getIntent().getSerializableExtra("value");
            CourseName = getIntent().getStringExtra(Constants.Course_Name);
            CourseThumbnail = getIntent().getStringExtra(Constants.Course_Thumbnail);
            CourseDescription = getIntent().getStringExtra(Constants.Course_Description);
            CourseId = getIntent().getIntExtra(Constants.Course_Id,0);
            PublishId =  getIntent().getIntExtra(Constants.Publish_Id,0);
            courseStatus = getIntent().getStringExtra(Constants.Course_Status);
            isFromDashboard = getIntent().getBooleanExtra(Constants.Is_From_DashBoard,false);
            catvalue = getIntent().getStringExtra("Cats");
            tagvalue = getIntent().getStringExtra("Tags");
            Course_Status_INT = getIntent().getIntExtra("Course_Status_INT",0);
            //isSequenceEnabled =  getIntent().getBooleanExtra(Constants.ISSEQUENCEENABLED,false);
            isReportEnabled = getIntent().getBooleanExtra(Constants.Evaluation_Mode,false);
            ispending = getIntent().getBooleanExtra("Show pending for evaluation",false);
            if (isReportEnabled)
            {
                TimeAsMilli = getIntent().getLongExtra("Timer",0);

            }

        }
        setUpRecyclerView();
        setthemeforView();
        mTimerText.setText("00:00:00");
        //getListOfSections();

        onClickListeners();
        desccattaglayout.setVisibility(View.VISIBLE);
        expanddes_cat.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListOfSections();
        if (isReportEnabled && !ispending)
        {
            int count = 0 ;

            question_timer_holder.setVisibility(View.VISIBLE);
            if (ispending)
            {
                question_timer_holder.setVisibility(View.GONE);
                reportbtn.setVisibility(View.VISIBLE);
                reportbtn.setText(R.string.pending_for_evaluation);
                reportbtn.setEnabled(false);
                reportbtn.setAlpha((float) 0.5);
            }

        }
    }

    public void initializeView(){
        companylogo = (ImageView) findViewById(R.id.companylogo);
        mEmptyView = findViewById(R.id.empty_view);
        recyclerView = (EmptyRecyclerView)findViewById(R.id.recyclerView);

        mTextTitle = (TextView)findViewById(R.id.text_title);
        text_title1 = (TextView)findViewById(R.id.text_title1);
        mTextSubTitle = (TextView)findViewById(R.id.text_description);

        mainLinearLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);

        cat_text = (TextView) findViewById(R.id.cat_text);
        cat_value = (TextView) findViewById(R.id.cat_value);

        tag_text = (TextView) findViewById(R.id.tag_text);
        tag_value = (TextView) findViewById(R.id.tag_value);

        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        segmentprogresslayout = (LinearLayout) findViewById(R.id.segmentprogresslayout);
        header = (LinearLayout)findViewById(R.id.header);
        expanddes_cat = (ImageView)findViewById(R.id.expanddes_cat);
        completion_status = (TextView)findViewById(R.id.completion_status);
        desccattaglayout = findViewById(R.id.desccattaglayout);
        completecertificate = (ImageView) findViewById(R.id.completecertificate);
        cardViewLayout = (CardView) findViewById(R.id.cardViewLayout);

        checklistReport = (TextView) findViewById(R.id.checklistReport);
        certificate =(TextView) findViewById(R.id.certifcate);
        no_of_assets = (CustomFontTextView) findViewById(R.id.no_of_assets);
        no_of_sections = (CustomFontTextView) findViewById(R.id.no_of_sections);
        no_of_questions = (CustomFontTextView) findViewById(R.id.no_of_questions);
        no_of_checklist = (CustomFontTextView) findViewById(R.id.no_of_checklist);
        checklist_sec = (LinearLayout) findViewById(R.id.no_of_checklist_sec);
        enddatetime = (TextView) findViewById(R.id.enddatetime);
//        certificate.setEnabled(false);
        reportbtn = (TextView) findViewById(R.id.reportsbutton);
        question_timer_holder = (RelativeLayout) findViewById(R.id.question_timer_holder);
        time_expiry_text = (TextView) findViewById(R.id.time_expiry_text);
        mTimerText = (TextView) findViewById(R.id.question_time_text);
        section_name_text = (TextView) findViewById(R.id.section_name_text);
        section_name_text.setText(CourseName);
    }

    public void setthemeforView(){

        if(PreferenceUtils.getSubdomainName(mContext).contains("tacobell")){

            Ion.with(thumbnail).fitXY().placeholder(R.drawable.tacobell_default).fitXY().error(R.drawable.tacobell_default).fitXY().load(
                    (!TextUtils.isEmpty(CourseThumbnail)) ?
                            CourseThumbnail : CourseThumbnail);

        }else{
            Ion.with(thumbnail).fitXY().placeholder(R.drawable.courses).fitXY().error(R.drawable.courses).fitXY().load(
                    (!TextUtils.isEmpty(CourseThumbnail)) ?
                            CourseThumbnail : CourseThumbnail);
        }

        thumbnail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.ICON_COLOR)));


        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        mainLinearLayout.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        cardViewLayout.setCardBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        companylogo.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));

        mTextTitle.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        text_title1.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        mTextSubTitle.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        cat_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        cat_value.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tag_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tag_value.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        expanddes_cat.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));

        checklistReport.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
//        certificate.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
//        if(PreferenceUtils.getSubdomainName(mContext).toLowerCase().contains("shakeys"))
//        {
//            certificate.setBackgroundColor(Color.parseColor(Constants.ICON_COLOR));
//        }
//        else
//        {
//            certificate.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
//        }
        no_of_assets.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        no_of_sections.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        no_of_questions.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        no_of_checklist.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        enddatetime.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        reportbtn.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        reportbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        question_timer_holder.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        time_expiry_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        mTimerText.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        loadData();
    }

    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setEmptyView(mEmptyView);

        //Bitmap bitmap = BitmapFactory.decodeFile(CourseThumbnail);
        //thumbnail.setImageBitmap(bitmap);
    }

    public void loadData(){
        mTextTitle.setText(CourseName);
        text_title1.setText(CourseName);
        if(CourseDescription.isEmpty()){
            mTextSubTitle.setText(CourseName);
        } else{
            mTextSubTitle.setText(CourseDescription);
        }

        if(tagvalue!= null) {
            if (!tagvalue.isEmpty()) {
                if(tagvalue.length() > 0){
                    String tagsstring = tagvalue.toString().replace("^"," , ");
                    tag_value.setText(tagsstring.toString());
                }else {
                    tag_value.setText(" - ");
                }
            } else {
                tag_value.setText(" - ");
            }
        }

        if(catvalue!= null) {
            if (!catvalue.isEmpty()) {
                cat_value.setText(catvalue.toString());
            } else {
                cat_value.setText(" - ");
            }
        }
        if (ispending)
        {
            completion_status.setText(" " + getResources().getString(R.string.pending_for_evaluation));
            completion_status.setTextColor(Color.parseColor(Constants.PENDING_APPROVAl_COLOR));
            reportbtn.setText(R.string.pending_for_evaluation);
            reportbtn.setVisibility(View.VISIBLE);
            reportbtn.setEnabled(false);
            reportbtn.setAlpha((float) 0.5);
            isbackallowed = true;
        }
        else {
            if (Course_Status_INT == Constants.COMPLETED) {
                if (cs.getEvaluation_Type() == 1) {
                    if (cs.getEvaluation_Type() == 1 && cs.getEvaluation_Status() == 1) {
                        completion_status.setText(" " + getResources().getString(R.string.completed_course));
                        completion_status.setTextColor(Color.parseColor(Constants.COMPLETED_COLOR));
                        if (isReportEnabled) {
                            isbackallowed = true;
                            reportbtn.setVisibility(View.VISIBLE);
                            reportbtn.setText(R.string.Marksheet);
                        }
                        if (ispending) {
                            reportbtn.setText(R.string.pending_for_evaluation);
                            reportbtn.setEnabled(false);
                            reportbtn.setAlpha((float) 0.5);
                            isbackallowed = true;
                        }
                    } else {
                        completion_status.setText(" " + getResources().getString(R.string.pending_for_evaluation));
                        completion_status.setTextColor(Color.parseColor(Constants.PENDING_APPROVAl_COLOR));
                    }
                } else {
                    completion_status.setText(" " + getResources().getString(R.string.completed_course));
                    completion_status.setTextColor(Color.parseColor(Constants.COMPLETED_COLOR));
                    if (isReportEnabled) {
                        reportbtn.setVisibility(View.VISIBLE);
                        reportbtn.setText(R.string.Marksheet);
                        isbackallowed = true;
                    }
                    if (ispending) {
                        reportbtn.setText(R.string.pending_for_evaluation);
                        reportbtn.setEnabled(false);
                        reportbtn.setAlpha((float) 0.5);
                        isbackallowed = true;
                    }
                }
                completecertificate.setVisibility(View.VISIBLE);
            } else if (Course_Status_INT == Constants.COURSE_EXPIRED) {
                completion_status.setText(getResources().getString(R.string.expired_shortened));
                completion_status.setTextColor(Color.parseColor(Constants.EXPIRED_COLOR));
                completecertificate.setVisibility(View.GONE);
                isbackallowed = true;

            } else if (Course_Status_INT == Constants.MAX_ATTEMPTS_REACHED) {
                completion_status.setText(getResources().getString(R.string.max_attempts_reached_shortened));
                completion_status.setTextColor(Color.parseColor(Constants.EXPIRED_COLOR));
                completecertificate.setVisibility(View.GONE);
                isbackallowed = true;
            } else if (Course_Status_INT == Constants.YET_TO_START) {
                completion_status.setText(getResources().getString(R.string.yet_to_start));
                completion_status.setTextColor(Color.parseColor(Constants.YET_TO_START_COLOR));
                completecertificate.setVisibility(View.GONE);
            } else if (Course_Status_INT == Constants.INPROGRESS) {
                completion_status.setText(getResources().getString(R.string.in_progress));
                completion_status.setTextColor(Color.parseColor(Constants.INPROGRESS_COLOR));
                completecertificate.setVisibility(View.GONE);
            } else if (Course_Status_INT == Constants.PARTIALLY_COMPLETED) {
                completion_status.setText(getResources().getString(R.string.partialy_shortened));
                completion_status.setTextColor(Color.parseColor(Constants.PARTIALLY_COMPLETED_COLOR));
                completecertificate.setVisibility(View.VISIBLE);
            }
            else if(cs.getCourse_Status_Value()==Constants.COURSE_EXPIRED)
            {
                isbackallowed = true;
                question_timer_holder.setVisibility(View.GONE);
            }

        }
    }


    public void StartTimer(long minutesAsMilli) {

        mTestTimer = new CountDownTimer(minutesAsMilli,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                mTimerText.setText(String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), // The change is in this line
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                Date date = null;
                try {

                    String DateAsString = String.valueOf(mTimerText.getText());
                    //String kept = DateAsString.substring( 0, DateAsString.indexOf("."));
                    date = sdf.parse(DateAsString);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long TimeAsMilli;
                long  hoursAsMilli = TimeUnit.HOURS.toMillis(date.getHours());
                long minutesAsMilli  =  TimeUnit.MINUTES.toMillis(date.getMinutes());
                long secondsAsMilli = TimeUnit.SECONDS.toMillis(date.getSeconds());
                TimeAsMilli = hoursAsMilli+minutesAsMilli+secondsAsMilli;
                PreferenceUtils.setTimer(mContext,TimeAsMilli);

            }

            @Override
            public void onFinish() {
                mTimerText.setText("00:00:00");
                //OnTimeOutOccured();
                PreferenceUtils.setTimer(mContext,0);

            }

        }.start();

    }
    //
    public void onClickListeners(){
        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        expanddes_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(desccattaglayout.isShown()){
                    desccattaglayout.setVisibility(View.GONE);
                    expanddes_cat.setImageResource(R.drawable.ic_keyboard_arrow_down_black_36dp);
                }else{
                    desccattaglayout.setVisibility(View.VISIBLE);
                    expanddes_cat.setImageResource(R.drawable.ic_keyboard_arrow_up_black_36dp);
                }
            }
        });

        reportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,LPCourseReportSummaryActivity.class);
                intent.putExtra("iscourseReport",true);
                intent.putExtra("courseID",CourseId);
                intent.putExtra("showfullsummary",2);
                //intent.putExtra("showattemptcount",courseModel.getAttempt_Number());
                startActivity(intent);
            }
        });
        checklistReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoCoursechecklistreportlist(0);
            }
        });
//        certificate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //pass courseid,userid,user details...
//                Intent intent= new Intent(SectionActivity.this,Certificatewebview.class);
//                startActivity(intent);
//            }
//        });
    }

    /*public void gotoSectionsQuestionDetailActivity(int Secid){
        Intent intent = new Intent(mContext, SectionsQuestionDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ChecklistId",cs.getRef_Id());
        bundle.putSerializable("CourseId",CourseId);
        bundle.putSerializable("SectionId",Secid);
        bundle.putSerializable("UserId",UserId);
        bundle.putSerializable("Attemptcount",0);
        bundle.putSerializable("checklistName","");
        bundle.putSerializable("isfromHistory",false);
        bundle.putSerializable("isfromcoursechecklist",true);
        intent.putExtras(bundle);

        startActivity(intent);
    }*/

    public void gotoCoursechecklistreportlist(int Secid){
        Intent intent = new Intent(mContext, SectionChecklistReportActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ChecklistId",0);
        bundle.putSerializable("CourseId",CourseId);
        bundle.putSerializable("SectionId",Secid);
        bundle.putSerializable("UserId",UserId);
        bundle.putSerializable("Attemptcount",0);
        bundle.putSerializable("checklistName","");
        bundle.putSerializable("isfromHistory",false);
        bundle.putSerializable("isfromcoursechecklist",true);
        bundle.putSerializable("isfromSection",false);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    public void gotoSectionchecklistreportlist(int Secid){
        Intent intent = new Intent(mContext, SectionChecklistReportActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ChecklistId",0);
        bundle.putSerializable("CourseId",CourseId);
        bundle.putSerializable("SectionId",Secid);
        bundle.putSerializable("UserId",UserId);
        bundle.putSerializable("Attemptcount",0);
        bundle.putSerializable("checklistName","");
        bundle.putSerializable("isfromHistory",false);
        bundle.putSerializable("isfromcoursechecklist",true);
        bundle.putSerializable("isfromSection",true);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void onBackPressed() {


        if (isReportEnabled && !ispending) {
            //back action disabled

            if(isbackallowed)
            {
                Intent i = new Intent(mContext, CourseListActivity.class);
                startActivity(i);
                finish();
            }
            else
            {
                ShowAlert(getString(R.string.Are_you_sure_back),getResources().getString(R.string.alert));
            }
        }
        else
        {
            if (isFromDashboard) {
                Intent i = new Intent(mContext, CourseListActivity.class);
                startActivity(i);
                finish();
            } else {
                super.onBackPressed();
            }
        }
    }
    private void ShowAlert(String Message, String Title) {

        final AlertDialog alertDialog = new AlertDialog.Builder(
                SectionActivity.this).create();

        alertDialog.setCancelable(false);
        // Setting Dialog Title
        alertDialog.setTitle(Title);
        // Setting Dialog Message
        alertDialog.setMessage(Message);

        // Setting OK Button
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,SectionActivity.this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed

                int count = 0 ;
                for (int i = 0 ;i < sectionModelList.size();i++)
                {
                    if ((sectionModelList.get(i).getSection_Status_Value() == Constants.YET_TO_START))
                    {
//                            updatequestionanwser(sectionModelList.get(i).getSection_Id(),sectionModelList.get(i).getCourse_User_Assignment_Id(),sectionModelList.get(i).getCouse_User_Section_Mapping_Id());
//                            updateemptyanswer();
                        count = count+1;
                    }

                }

                if (count == sectionModelList.size()) {
                    StartDashboardActivtiy();
                }
                else if (count>0 && count<sectionModelList.size())
                {
                    for (int i = 0 ;i < sectionModelList.size();i++)
                    {
                        if ((sectionModelList.get(i).getSection_Status_Value() == Constants.YET_TO_START))
                        {
                            updatequestionanwser(sectionModelList.get(i).getSection_Id(),sectionModelList.get(i).getCourse_User_Assignment_Id(),sectionModelList.get(i).getCouse_User_Section_Mapping_Id());
                            updateemptyanswer();
                        }

                    }
                }

            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,SectionActivity.this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        // Showing Alert Message
        alertDialog.show();

    }

    void updateemptyanswer()
    {
        if (PreferenceUtils.getquestionanswerlist(SectionActivity.this,"key")!=null && PreferenceUtils.getquestionanswerlist(SectionActivity.this,"key").size() > 0){
            QuestionActivity questionActivity = new QuestionActivity();
            ArrayList<QuestionAndAnswerModel> list = PreferenceUtils.getquestionanswerlist(SectionActivity.this,"key");
            Checknetworkandupload(UploadAnswerProcess(list),true,false,true);
            QuestionLoadCount = list.get(0).getLstCourse().get(0).getQuestionLoadCount();
//            PreferenceUtils.setQuestionAnswerList("key",null,SectionActivity.this);
        }
    }
    private void StartDashboardActivtiy() {

        //Intent intent =  new Intent(this, LandingActivity.class);
        Intent intent =  new Intent(this, CourseListActivity.class);
        intent.putExtra(com.swaas.kangle.utils.Constants.From_Question,false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }



    public void getListOfSections(){
        if (!isFinishing()){
            dismissProgressDialog();
        }
        if(NetworkUtils.checkIfNetworkAvailable(mContext)){
            if(!isFinishing()){
                showProgressDialog();
            }
            retrofitAPI = RetrofitAPIBuilder.getInstance();
            lpService = retrofitAPI.create(LPCourseService.class);
            //offsetFromUtc = CommonUtils.getUtcOffset();
            offsetFromUtc = CommonUtils.getUtcOffsetincluded10k();
            Log.d("getUTC",offsetFromUtc);
            CompanyId  = PreferenceUtils.getCompnayId(mContext);
            SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            UserId = PreferenceUtils.getUserId(mContext);
            Call call = lpService.getKASectionDetailsOfCourse(SubdomainName,CompanyId,UserId,CourseId,PublishId);


            call.enqueue(new Callback<ArrayList<SectionModel>>() {

                @Override
                public void onResponse(Response<ArrayList<SectionModel>> response, Retrofit retrofit) {
                    ArrayList<SectionModel> courseListModel= response.body();
                    if (courseListModel != null) {
                        sectionModelList = courseListModel;
                        //sectionAdapter = new SectionAdapter(mContext,sectionModelList,isSequenceEnabled);
                        setProgressList();
                        sectionAdapter = new SectionAdapter(mContext,sectionModelList,isReportEnabled);
                        recyclerView.setAdapter(sectionAdapter);
                        if(!isFinishing())
                            dismissProgressDialog();
                        loadadapterclick();
                        //setProgressList();
                        //Toast.makeText(mContext, "You have "+sectionModelList.size()+" Section(s)" , Toast.LENGTH_SHORT).show();
                        Log.d("sectionModelList",String.valueOf(sectionModelList.size()));
                        /*if(sectionModelList.size()>2){
                            AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams)collaapsingToolbarLayout.getLayoutParams();
                            layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL| AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                            collaapsingToolbarLayout.setLayoutParams(layoutParams);
                        }
                        else{AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams)collaapsingToolbarLayout.getLayoutParams();
                            layoutParams.setScrollFlags(0);
                            collaapsingToolbarLayout.setLayoutParams(layoutParams);
                        }*/
                    } else {
                        //courseListModel is null
                        dismissProgressDialog();
                        recyclerView.setEmptyView(mEmptyView);
                        Toast.makeText(mContext, "No Section ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(),"Error");
                }
            });

        }else{
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
        }
    }

    public void setProgressList(){
        LinearLayout l3 = new LinearLayout(mContext);
        l3.setOrientation(LinearLayout.HORIZONTAL);
        int sizenew = sectionModelList.size();
        l3.setWeightSum(sizenew);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,12,1.0f);
        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30,LinearLayout.LayoutParams.MATCH_PARENT,
        //80);
        params.setMargins(10,0,5,0);
        for (final SectionModel secmodel : sectionModelList) {
            final View labl = new View(mContext);
            labl.setLayoutParams(params);
            labl.setPadding(10,0,10,10);

            if(secmodel.getSection_Status_Value() == Constants.COMPLETED){
                labl.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_allcorners_green));
            }else if(secmodel.getSection_Status_Value() == Constants.COURSE_EXPIRED){
                labl.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_allcorners_red));
            }else if(secmodel.getSection_Status_Value() == Constants.MAX_ATTEMPTS_REACHED){
                labl.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_allcorners_red));
            }else if(secmodel.getSection_Status_Value() == Constants.YET_TO_START){
                labl.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_allcorners_grey));
            } else if(secmodel.getSection_Status_Value() == Constants.INPROGRESS){
                labl.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_allcorners_bluecolor));
            }
            if (isReportEnabled && ispending)
            {
                labl.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_orange));
            }
            l3.addView(labl);
        }
        segmentprogresslayout.addView(l3);
    }

    public void loadadapterclick() {
        no_of_sections.setText(sectionModelList.size()+" "+getResources().getString(R.string.section_s));
        int questioncount = 0, AssetCount = 0;
        int ChecklistTotalcount = 0;
        for(int i=0;i<sectionModelList.size();i++){
            AssetCount = (AssetCount + sectionModelList.get(i).getNo_Of_Assets_Mapped());
            questioncount = (questioncount + sectionModelList.get(i).getNo_Of_Visible_Questions());
            ChecklistTotalcount = (ChecklistTotalcount + sectionModelList.get(i).getSection_Checklist_Count());
        }
        no_of_assets.setText(AssetCount +" "+getResources().getString(R.string.asset_s));
        no_of_questions.setText(questioncount +" "+getResources().getString(R.string.question_s));
        if(PreferenceUtils.getLandingPageAccess(mContext) != null) {
            Gson gsonget = new Gson();
            LandingPageAccess landingobj = gsonget.fromJson(PreferenceUtils.getLandingPageAccess(mContext), LandingPageAccess.class);
            if (landingobj != null) {
                if (!TextUtils.isEmpty(landingobj.getChecklist()) && landingobj.getChecklist().equalsIgnoreCase("Y")) {
                    ChecklistTotalcount = ChecklistTotalcount + cs.getCourse_Checklist_Count();
                    checklist_sec.setVisibility(View.VISIBLE);
                    no_of_checklist.setText(ChecklistTotalcount+" "+getResources().getString(R.string.checklist_s));
                }
                else{
                    checklist_sec.setVisibility(View.GONE);
                }
            }
        }

        enddatetime.setText(getResources().getString(R.string.expires_on)+" : "+ cs.getValid_To().toString());



        if (NetworkUtils.isNetworkAvailable(mContext)) {

            if(cs != null){
                if(cs.getEvaluation_Type() == 1 && cs.getCourse_Status_Value() == Constants.COMPLETED){
                    if(cs.getCourse_Evaluation_Status() != 5){
                        checklistReport.setVisibility(View.VISIBLE);

                    }else{
                        checklistReport.setVisibility(View.GONE);
                    }
                }else{
                    checklistReport.setVisibility(View.GONE);
                }
                checklistReport.setText(getResources().getString(R.string.evaluation_report));
//                if(cs.getCourse_Status_Value()==Constants.COMPLETED&&cs.isShow_Print_Certificate())
//                {
//                    certificate.setVisibility(View.GONE);
//                }
            }

            sectionAdapter.setOnItemClickListener(new SectionAdapter.MyClickListener() {
                @Override
                public void onItemClick(final int sectionId,final int course_user_assignment_id,final int sectionMapId) {
                    if(NetworkUtils.isNetworkAvailable()) {
                        showProgressDialog();
                        String mSubDomainName = PreferenceUtils.getSubdomainName(mContext);
                        int mCompanyId = PreferenceUtils.getCompnayId(mContext);
                        Call call = lpService.getLPAssetsByCourseId(mSubDomainName, mCompanyId, CourseId, sectionId);
                        call.enqueue(new Callback<ArrayList<CourseAssetModel>>() {
                            @Override
                            public void onResponse(Response<ArrayList<CourseAssetModel>> response, Retrofit retrofit) {
                                //On Response
                                if (response != null) {
                                    PreferenceUtils.setCourse_User_Assignment_Id(mContext, course_user_assignment_id);
                                    PreferenceUtils.setCouse_User_Section_Mapping_Id(mContext, sectionMapId);
                                    insertHeaderDetails(response.body(), sectionId, course_user_assignment_id, sectionMapId);
                                    //loadAssetToPlayer(response.body(),sectionId) ;
                                    //dismissProgressDialog();

                                } else {
                                    //courseAssetModelList is null
                                    recyclerView.setEmptyView(mEmptyView);
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                //On Failure
                            }
                        });
                    }else{
                        Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onTakeTestClick(String secname,int section_id, int course_user_assignment_id, int sectionMapId) {
                    selectedSectionName = secname;
                    insertTestHeaderDetails(section_id,course_user_assignment_id,sectionMapId);
                    if (!istimerrunning) {
                        istimerrunning = true;
                    }
                    //getListOfQuestions(section_id,course_user_assignment_id,sectionMapId);
                }

                @Override
                public void onReportClick(int sectionId,int courseid,int publishid) {
                    if(NetworkUtils.isNetworkAvailable()) {
                        Intent intent = new Intent(mContext, LPCourseReportActivity.class);
                        intent.putExtra(Constants.Course_Id, courseid);
                        intent.putExtra(Constants.Publish_Id, publishid);
                        intent.putExtra(Constants.Section_Id, sectionId);
                        intent.putExtra("SectionDate", sectionModelList.get(0).getValid_To());
                        intent.putExtra("isfromtest", false);
                        intent.putExtra("Course_Status_INT", Course_Status_INT);
                        intent.putExtra(Constants.Course_Thumbnail, CourseThumbnail);
                        startActivity(intent);
                    }else{
                        Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onextendcourseClick(SectionModel sectionModel) {
                    ShowCourseExtendpopup(sectionModel);
                }

            });



        }
        else{
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
        }
    }

    //included for the purpose of noquestionand for asset pass
    public void insertHeaderDetails(final ArrayList<CourseAssetModel> courseAssetModelList,final int section_id, final int course_user_assignment_id, final int SectionMapId){
        if(NetworkUtils.isNetworkAvailable()){
            retrofitAPI = RetrofitAPIBuilder.getInstance();
            lpService = retrofitAPI.create(LPCourseService.class);
            int CompanyId  = PreferenceUtils.getCompnayId(SectionActivity.this);
            String  SubdomainName = PreferenceUtils.getSubdomainName(SectionActivity.this);
            int UserId = PreferenceUtils.getUserId(SectionActivity.this);
            Call call = lpService.insertLPCourseSectionUserExamHeader(SubdomainName,CourseId,course_user_assignment_id,
                    SectionMapId,PublishId,UserId,CompanyId,section_id);
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    String success= response.body();
                    if (success != null) {
                        //if(!success.equalsIgnoreCase("0")) {
                        dismissProgressDialog();
                        loadAssetToPlayer(courseAssetModelList,section_id) ;
                        //insertTestHeaderDetails(section_id, course_user_assignment_id, SectionMapId);
                        //}
                    } else {
                        dismissProgressDialog();
                        //courseListModel is null
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(),"Error");
                    dismissProgressDialog();
                    Toast.makeText(mContext,getResources().getString(R.string.erroroccured),Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            if(mProgressDialog != null){
                dismissProgressDialog();
            }
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
        }
    }

    public void insertTestHeaderDetails(final int section_id, final int course_user_assignment_id, final int SectionMapId){
        if(NetworkUtils.isNetworkAvailable()){
            if(mProgressDialog != null){
                dismissProgressDialog();
            }
            showProgressDialog();
            retrofitAPI = RetrofitAPIBuilder.getInstance();
            lpService = retrofitAPI.create(LPCourseService.class);
            int CompanyId  = PreferenceUtils.getCompnayId(SectionActivity.this);
            String  SubdomainName = PreferenceUtils.getSubdomainName(SectionActivity.this);
            int UserId = PreferenceUtils.getUserId(SectionActivity.this);
            Call call = lpService.insertLPCourseSectionUserExamHeader(SubdomainName,CourseId,course_user_assignment_id,
                    SectionMapId,PublishId,UserId,CompanyId,section_id);
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    String success= response.body();
                    if (success != null) {
                        if(!success.equalsIgnoreCase("0")) {
                            dismissProgressDialog();
                            getListOfQuestions(section_id, course_user_assignment_id, SectionMapId);
                        }else{
                            dismissProgressDialog();
                            insertTestHeaderDetails(section_id, course_user_assignment_id, SectionMapId);
                        }
                    } else {
                        dismissProgressDialog();
                        //courseListModel is null
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(),"Error");
                    dismissProgressDialog();
                    Toast.makeText(mContext,getResources().getString(R.string.erroroccured),Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
        }
    }


    public void getListOfQuestions(final int section_id, final int course_user_assignment_id, final int SectionMapId){

        if(NetworkUtils.isNetworkAvailable()){
            showProgressDialog();
            retrofitAPI = RetrofitAPIBuilder.getInstance();
            lpService = retrofitAPI.create(LPCourseService.class);
            int CompanyId  = PreferenceUtils.getCompnayId(SectionActivity.this);
            String  SubdomainName = PreferenceUtils.getSubdomainName(SectionActivity.this);
            int UserId = PreferenceUtils.getUserId(SectionActivity.this);
            Call call = lpService.getLPQuestionAnswerDetails(SubdomainName,CompanyId,UserId,CourseId,section_id,PublishId);
            call.enqueue(new Callback<ArrayList<QuestionBaseModel>>() {

                @Override
                public void onResponse(Response<ArrayList<QuestionBaseModel>> response, Retrofit retrofit) {
                    ArrayList<QuestionBaseModel> questionBaseModels= response.body();
                    if (questionBaseModels != null) {
                        dismissProgressDialog();
                        ArrayList<QuestionAndAnswerModel> questionAndAnswerModels =  CallToGetQuestionModel(questionBaseModels,course_user_assignment_id,SectionMapId,section_id);
                        if(questionAndAnswerModels != null && questionAndAnswerModels.size() > 0) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("value", questionAndAnswerModels);
                            final Intent intentToQuestionActivity = new Intent(mContext, QuestionActivity.class);
                            intentToQuestionActivity.putExtras(bundle);
                            intentToQuestionActivity.putExtra(Constants.Course_Id, CourseId);
                            intentToQuestionActivity.putExtra(Constants.Publish_Id, PublishId);
                            intentToQuestionActivity.putExtra(Constants.Section_Id, section_id);
                            intentToQuestionActivity.putExtra("SectionDate", sectionModelList.get(0).getValid_To());
                            intentToQuestionActivity.putExtra("SectionName",selectedSectionName);
                            intentToQuestionActivity.putExtra(Constants.Course_Thumbnail, CourseThumbnail);
                            isFromDashboard = false;
                            if (isReportEnabled) {
                                intentToQuestionActivity.putExtra(Constants.Evaluation_Mode, true);
                                //istimerrunning = true;
                                if (istimerrunning) {
                                    if (PreferenceUtils.getTimer(mContext)!= 0 )
                                    {
                                        StartTimer(PreferenceUtils.getTimer(mContext));
                                    }
                                    else
                                    {
                                        StartTimer(TimeAsMilli);
                                    }
                                    showProgressDialog();
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Do something after 5s = 5000ms
                                            intentToQuestionActivity.putExtra("Timer", mTimerText.getText());
                                            startActivity(intentToQuestionActivity);
                                            dismissProgressDialog();
                                        }
                                    }, 1000);
                                    istimerrunning = false;
                                }
                                else
                                {
                                    intentToQuestionActivity.putExtra("Timer", mTimerText.getText());
                                    startActivity(intentToQuestionActivity);
                                }
                            }
                            else
                            {
                                intentToQuestionActivity.putExtra(Constants.Evaluation_Mode, false);
                                startActivity(intentToQuestionActivity);
                            }


                        }else{
                            Toast.makeText(SectionActivity.this,getResources().getString(R.string.noQuestions),Toast.LENGTH_SHORT).show();
                        }
                    } else {

                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(),"Error");
                }


            });

        }else{
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
        }
    }
    public void updatequestionanwser(final int section_id, final int course_user_assignment_id, final int SectionMapId){

        if(NetworkUtils.isNetworkAvailable()){
            showProgressDialog();
            retrofitAPI = RetrofitAPIBuilder.getInstance();
            lpService = retrofitAPI.create(LPCourseService.class);
            int CompanyId  = PreferenceUtils.getCompnayId(SectionActivity.this);
            String  SubdomainName = PreferenceUtils.getSubdomainName(SectionActivity.this);
            int UserId = PreferenceUtils.getUserId(SectionActivity.this);
            Call call = lpService.getLPQuestionAnswerDetails(SubdomainName,CompanyId,UserId,CourseId,section_id,PublishId);
            call.enqueue(new Callback<ArrayList<QuestionBaseModel>>() {

                @Override
                public void onResponse(Response<ArrayList<QuestionBaseModel>> response, Retrofit retrofit) {
                    ArrayList<QuestionBaseModel> questionBaseModels= response.body();
                    if (questionBaseModels != null) {
                        dismissProgressDialog();
                        ArrayList<QuestionAndAnswerModel> questionAndAnswerModels =  CallToGetQuestionModel(questionBaseModels,course_user_assignment_id,SectionMapId,section_id);
                        if(questionAndAnswerModels != null && questionAndAnswerModels.size() > 0) {
                            PreferenceUtils.setQuestionAnswerList("key",questionAndAnswerModels,mContext);
                        }else{
                            Toast.makeText(SectionActivity.this,getResources().getString(R.string.noQuestions),Toast.LENGTH_SHORT).show();
                        }
                    } else {

                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(),"Error");
                }


            });

        }else{
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
        }
    }


    private ArrayList<QuestionAndAnswerModel> CallToGetQuestionModel(ArrayList<QuestionBaseModel> questionBaseModels, int course_user_assignment_id, int sectionMapId, int section_id) {

        ArrayList<QuestionAndAnswerModel> questinandanswermodellist = new ArrayList<>();
        QuestionBaseModel questionbasemodel =  questionBaseModels.get(0);
        for (QuestionQuestionListModel question : questionbasemodel.lstQuestion){
            QuestionAndAnswerModel questionandanswermodel = new QuestionAndAnswerModel();
            if(questionbasemodel.getLstMatchingQA() != null && questionbasemodel.getLstMatchingQA().size()>0 ) {
                questionandanswermodel.setLstMatchingQA(questionbasemodel.getLstMatchingQA());
                questionandanswermodel.setLstRandom(questionbasemodel.getLstRandom());
            }
            questionandanswermodel.setLstCourse(questionbasemodel.getLstCourse());
            questionandanswermodel.setQuestionModel(question);
            ArrayList<QuestionAnswerListModel> AnswerList = new ArrayList<>();
            for (QuestionAnswerListModel answer: questionbasemodel.lstAnswer){
                if (answer.getQuestion_Id() == question.getQuestion_Id()){
                    AnswerList.add(answer);
                }
            }
            questionandanswermodel.setLstAnswer(AnswerList);
            questionandanswermodel.setSectionMapId(sectionMapId);
            questionandanswermodel.setSectionId(section_id);
            questionandanswermodel.setCourseAssignmentId(course_user_assignment_id);

            questinandanswermodellist.add(questionandanswermodel);
        }

        return questinandanswermodellist;
    }


    public void loadAssetToPlayer(ArrayList<CourseAssetModel> courseAssetModelList, int sectionId) {
        if(mProgressDialog != null){
            dismissProgressDialog();
        }
        List<CourseAssetModel> courseAssetlList = courseAssetModelList;
        ArrayList<DigitalAssets> assets = new ArrayList<DigitalAssets>();

        if (courseAssetModelList != null) {

            for (CourseAssetModel courseAssetModel : courseAssetlList) {
                DigitalAssets digitalAssets = new DigitalAssets();
                digitalAssets.setDA_Code(Integer.parseInt(courseAssetModel.DA_Code));
                digitalAssets.setDA_Online_URL(courseAssetModel.getFile_Path());
                digitalAssets.setAsset_Name(courseAssetModel.getAsset_Name());
                digitalAssets.setDA_Type_Name(courseAssetModel.getDA_Type());
                /*if (courseAssetModel.getFile_Path().endsWith("html")) {
                    digitalAssets.setDA_Type(com.swaas.kangle.playerPart.customviews.pdf.util.Constants.HTMLASSET);
                } else if (courseAssetModel.getFile_Path().endsWith("mp3")) {
                    digitalAssets.setDA_Type(com.swaas.kangle.playerPart.customviews.pdf.util.Constants.AUDIOASSET);
                } else if (courseAssetModel.getFile_Path().endsWith("mp4")) {
                    digitalAssets.setDA_Type(com.swaas.kangle.playerPart.customviews.pdf.util.Constants.VIDEOASSET);
                } else if (courseAssetModel.getFile_Path().endsWith("pdf")) {
                    digitalAssets.setDA_Type(com.swaas.kangle.playerPart.customviews.pdf.util.Constants.PDFASSET);
                } else if (courseAssetModel.getFile_Path().endsWith("jpg") || courseAssetModel.getFile_Path().endsWith("jpeg")
                        || courseAssetModel.getFile_Path().endsWith("png") || courseAssetModel.getFile_Path().endsWith("gif")){
                    digitalAssets.setDA_Type(com.swaas.kangle.playerPart.customviews.pdf.util.Constants.IMAGEASSET);
                }*/

                if (courseAssetModel.getDA_Type().equalsIgnoreCase("document")) {
                    if (courseAssetModel.getFile_Path().endsWith(".pdf")) {
                        digitalAssets.setDA_Type(com.swaas.kangle.playerPart.customviews.pdf.util.Constants.PDFASSET);
                    }
                }else if(courseAssetModel.getDA_Type().equalsIgnoreCase("articulate") ||
                        courseAssetModel.getDA_Type().equalsIgnoreCase("html5")){
                    digitalAssets.setDA_Type(com.swaas.kangle.playerPart.customviews.pdf.util.Constants.HTMLASSET);
                } else if (courseAssetModel.getDA_Type().equalsIgnoreCase("mp3")) {
                    digitalAssets.setDA_Type(com.swaas.kangle.playerPart.customviews.pdf.util.Constants.AUDIOASSET);
                } else if (courseAssetModel.getDA_Type().equalsIgnoreCase("video")) {
                    if(courseAssetModel.getVideoType().equalsIgnoreCase("BV")) {
                        digitalAssets.setDA_Type(com.swaas.kangle.playerPart.customviews.pdf.util.Constants.BRIGHTCOVE);
                        digitalAssets.setVideoId(courseAssetModel.getVideoId());
                        digitalAssets.setPolicyKey(courseAssetModel.getPolicyKey());
                        digitalAssets.setAccountId(courseAssetModel.getAccountId());
                    }else{
                        digitalAssets.setDA_Type(com.swaas.kangle.playerPart.customviews.pdf.util.Constants.VIDEOASSET);
                    }
                } else if (courseAssetModel.getDA_Type().equalsIgnoreCase("image")){
                    digitalAssets.setDA_Type(com.swaas.kangle.playerPart.customviews.pdf.util.Constants.IMAGEASSET);
                }

                //if (!courseAssetModel.getFile_Path().endsWith(".doc") && !courseAssetModel.getFile_Path().endsWith(".ppt")){
                digitalAssets.setSection_Id(sectionId);
                digitalAssets.setCourse_Id(CourseId);
                digitalAssets.setIsPreview(0);
                assets.add(digitalAssets);
                //}
            }

            Bundle bundle = new Bundle();
            bundle.putSerializable("value", assets);
           // Intent intentToPlayer = new Intent(mContext, AssetPlayerActivity.class);
           Intent intentToPlayer = new Intent(mContext, DigitalAssetPlayerActivity.class);
            intentToPlayer.putExtras(bundle);
            intentToPlayer.putExtra(Constants.POSITION,0);
            intentToPlayer.putExtra(Constants.Course_Id,CourseId);
            intentToPlayer.putExtra(Constants.Publish_Id,PublishId);
            intentToPlayer.putExtra(Constants.Section_Id,sectionId);
            startActivityForResult(intentToPlayer,REQUEST_CODE_TO_UPDATE_ASSET);
        }else{
            Toast.makeText(mContext,getResources().getString(R.string.noassetsfound),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_TO_UPDATE_ASSET){
            showProgressDialog();
            assetAnalyticsUpsynctoServer.getAnalyticsfromDb(true,PreferenceUtils.getCourse_User_Assignment_Id(mContext),PreferenceUtils.getCouse_User_Section_Mapping_Id(mContext));
            sendDigitalAssetAnalyticsToServer();
        }

    }

    private void sendDigitalAssetAnalyticsToServer() {
        DigitalAssetTransactionRepository transactionRepository = new DigitalAssetTransactionRepository(this);
        transactionRepository.setmGetDA(new DigitalAssetTransactionRepository.GetDA() {
            @Override
            public void getDASuccess(ArrayList<DigitalAssets> digitalAssetList) {
                if(digitalAssetList != null && digitalAssetList.size() > 0){
                    Intent analyticsIntent = new Intent(SectionActivity.this,DigitalAssetAnalyticsUpSyncService.class);
                    startService(analyticsIntent);
                }
            }

            @Override
            public void getDAFailure(String message) {

            }
        });
        transactionRepository.getUnSyncedDigitalAssetAnalytics();
    }

    public void ShowCourseExtendpopup(final SectionModel sectionModel){
        messageDialog.ShowCourseExtend(mContext,  getString(R.string.extend_course), new View.OnClickListener() {
            @Override
            public void onClick(View Approve) {
                requestExtentDateForUsers(sectionModel);
                messageDialog.dialogDismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View close) {
                messageDialog.dialogDismiss();
            }
        }, true);
    }

    public void requestExtentDateForUsers(final SectionModel sectionModel){
        if(NetworkUtils.checkIfNetworkAvailable(mContext)){
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            LPCourseService userService = retrofitAPI.create(LPCourseService.class);

            String offsetFromUtc = CommonUtils.getUtcOffsetincluded10k();
            int CompanyId = PreferenceUtils.getCompnayId(mContext);
            String SubdomainName = PreferenceUtils.getSubdomainName(mContext);
            CourseExtendModel c = new CourseExtendModel();
            c.setCompanyId(CompanyId);
            c.setCourseId(sectionModel.getCourse_Id());
            c.setSectionUserMappingIds(String.valueOf(sectionModel.getCouse_User_Section_Mapping_Id())+",");
            c.setExtentAttemptCount(sectionModel.getExtendAttempts());
            c.setLocalTimeZone(offsetFromUtc);
            c.setOffsetValue(offsetFromUtc);
            c.setSubdomainName(SubdomainName);

            Call call = userService.LPCourseAutoExtentAttemptsForUsers(c);
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    String courseListModel = response.body();
                    if(courseListModel != null){
                        if(courseListModel.equals("Success")){
                            getListOfSections();
                        }
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(), "Error");
                    //mEmptyView.setVisibility(View.VISIBLE);
                }
            });
        }else{

        }
    }

    public void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
    public void Checknetworkandupload(final String AnswerModelString, final boolean isLastQuestion, final boolean isTimeout, final boolean isBackpressed){

        if(NetworkUtils.checkIfNetworkAvailable(SectionActivity.this)){
            if (!isBackpressed){
                showProgressDialog();
            }
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            LPCourseService lpService = retrofitAPI.create(LPCourseService.class);
            int CompanyId  = PreferenceUtils.getCompnayId(this);
            String  SubdomainName = PreferenceUtils.getSubdomainName(this);
            int UserId = PreferenceUtils.getUserId(this);
            Gson gsonget = new Gson();
            AnwerUploadModel answermodel = gsonget.fromJson(AnswerModelString,AnwerUploadModel.class);
            Call call = lpService.insertTestCourseResponse(SubdomainName,CompanyId,UserId,QuestionLoadCount,isLastQuestion,isTimeout,answermodel);
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    String courseAssetListModel= response.body();
                    if (courseAssetListModel != null) {

                        StartDashboardActivtiy();
                        if (!isBackpressed){
                            dismissProgressDialog();
                            if (courseAssetListModel.contains("COMPLETED")){
                                PreferenceUtils.setQuestionAnswerList("key",null,mContext);
                                if (isTimeout){
                                    // ShowAlert(getResources().getString(R.string.time_Out),getResources().getString(R.string.warning),false);
                                }else {
                                    //ShowAlert(getResources().getString(R.string.finished),"",false);

                                }
                            }else {

                                Log.d("error","error");
                            }

                        }
                        //COMPLETED~1~Your course has been partially submitted.~549
                    }
                }
                @Override
                public void onFailure(Throwable t) {

                  /*  testResultRepository.insertTestRecord(AnswerModelString,CalculatePercentage(),QuestionLoadCount,isLastQuestion+"",isTimeout+"");
                    ShowAlert(getResources().getString(R.string.testsavedoffline),getResources().getString(R.string.warning),true);*/
                    Log.d(t.toString(),"Error");
                }
            });
        }else{

//            testResultRepository.insertTestRecord(AnswerModelString,CalculatePercentage(),QuestionLoadCount,isLastQuestion+"",isTimeout+"");
//            ShowAlert(getResources().getString(R.string.testsavedoffline),getResources().getString(R.string.warning),true);
        }
    }
    public String UploadAnswerProcess(ArrayList<QuestionAndAnswerModel> questionandanswerlist) {

        AnwerUploadModel answerupload =  new AnwerUploadModel();
        answerupload.setLstCourseUserAnswers(new Gson().toJson(getCourseUserAnswer(questionandanswerlist)));
        answerupload.setLstCourseUserAssessHeader(new Gson().toJson(getCourseUserAssetHeader(questionandanswerlist)));
        answerupload.setLstCourseUserAssessDet(new Gson().toJson(getCourseAssetDetails(questionandanswerlist)));
        Log.d("test",new Gson().toJson(answerupload));
        return new Gson().toJson(answerupload);

    }
    private List<CourseUserAssessHeader> getCourseUserAssetHeader(ArrayList<QuestionAndAnswerModel> questionandanswerlist) {

        List<CourseUserAssessHeader> courseuserAssessHeaderList = new ArrayList<>();
        CourseUserAssessHeader courseAssetHeader = new CourseUserAssessHeader();

        courseAssetHeader.Company_Id = String.valueOf(PreferenceUtils.getCompnayId(this));
        QuestionCourseListModel questioncoursemodel = questionandanswerlist.get(0).getLstCourse().get(0);
        courseAssetHeader.Course_ID = questioncoursemodel.getCourse_Id();
        courseAssetHeader.Course_Section_User_Exam_Id = 1;
        courseAssetHeader.Course_User_Assignment_Id = questionandanswerlist.get(0).getCourseAssignmentId();
        courseAssetHeader.Couse_User_Section_Mapping_Id = questionandanswerlist.get(0).getSectionMapId();
        courseAssetHeader.Section_ID = questionandanswerlist.get(0).getSectionId();
        courseAssetHeader.User_Id = PreferenceUtils.getUserId(this);
        courseAssetHeader.Publish_ID = questioncoursemodel.getPublish_ID();
        courseAssetHeader.Achieved_Percentage = 0;
        courseAssetHeader.Pass_Percentage = String.valueOf(questioncoursemodel.getPass_Percentage());

        if (courseAssetHeader.Achieved_Percentage >= questioncoursemodel.getPass_Percentage()) {
            courseAssetHeader.Is_Qualified = 1;
        } else {

            courseAssetHeader.Is_Qualified = 0;

        }
        courseAssetHeader.Local_TimeZone = new Date().toString();
        //courseAssetHeader.Offset_Value = CommonUtils.getUtcOffset();
        courseAssetHeader.Offset_Value = CommonUtils.getUtcOffsetincluded10k();
        courseuserAssessHeaderList.add(courseAssetHeader);

        return courseuserAssessHeaderList;
    }

    private List<CourseUserAnswers> getCourseUserAnswer(ArrayList<QuestionAndAnswerModel> questionandanswerlist) {


        List<CourseUserAnswers> courseuseranswerList =  new ArrayList<>();
        for (QuestionAndAnswerModel questionandanswermodel : questionandanswerlist){

            if (questionandanswermodel.getQuestionModel().getQuestion_Type() != 1){

                CourseUserAnswers courseuseranswer = new CourseUserAnswers();
                if (questionandanswermodel.getQuestionModel().getQuestion_Type() == 6)
                {
                    courseuseranswer.Company_Id = String.valueOf(PreferenceUtils.getCompnayId(this));
                    courseuseranswer.User_Answer_Text = questionandanswermodel.getChoosenAnswer();
                    courseuseranswer.User_Id = PreferenceUtils.getUserId(this);
                    courseuseranswer.Question_Id = String.valueOf(questionandanswermodel.getQuestionModel().getQuestion_Id());
                }
                else if (questionandanswermodel.getQuestionModel().getQuestion_Type() == 0)
                {
                    courseuseranswer.Company_Id = String.valueOf(PreferenceUtils.getCompnayId(this));
                    courseuseranswer.User_Answer_Text = questionandanswermodel.getChoosenAnswer();
                    courseuseranswer.User_Id = PreferenceUtils.getUserId(this);
                    courseuseranswer.Question_Id = String.valueOf(questionandanswermodel.getQuestionModel().getQuestion_Id());
                }
                else if (questionandanswermodel.getQuestionModel().getQuestion_Type() !=2){

                    courseuseranswer.Company_Id = String.valueOf(PreferenceUtils.getCompnayId(this));
                    courseuseranswer.Text = questionandanswermodel.getChoosenAnswer();
                    courseuseranswer.User_Id = PreferenceUtils.getUserId(this);
                    courseuseranswer.Question_Id = String.valueOf(questionandanswermodel.getQuestionModel().getQuestion_Id());

                }else {

                    courseuseranswer.Company_Id = String.valueOf(PreferenceUtils.getCompnayId(this));
                    courseuseranswer.Answer_Id = questionandanswermodel.getChoosenAnswerId();
                    courseuseranswer.User_Id = PreferenceUtils.getUserId(this);
                    courseuseranswer.Question_Id = String.valueOf(questionandanswermodel.getQuestionModel().getQuestion_Id());

                }
                courseuseranswerList.add(courseuseranswer);

            }else {

                if (questionandanswermodel.getChoosenAnswerId()!=null){

                    String [] choosenanswerid = questionandanswermodel.getChoosenAnswerId().split(",");

                    for (int i=0; i< choosenanswerid.length; i++){

                        CourseUserAnswers courseanswer = new CourseUserAnswers();
                        courseanswer.Company_Id = String.valueOf(PreferenceUtils.getCompnayId(this));
                        courseanswer.User_Id = PreferenceUtils.getUserId(this);
                        courseanswer.Answer_Id = choosenanswerid[i];
                        courseanswer.Question_Id = String.valueOf(questionandanswermodel.getQuestionModel().getQuestion_Id());
                        courseuseranswerList.add(courseanswer);
                    }
                }

            }

        }

        return courseuseranswerList;

    }



    private List<CourseUserAssessDet> getCourseAssetDetails(ArrayList<QuestionAndAnswerModel> questionandanswerlist) {

        List<CourseUserAssessDet> courseuserassetdetails = new ArrayList<>();

        for (QuestionAndAnswerModel questionanswermodel : questionandanswerlist) {

            CourseUserAssessDet courseuserasset = new CourseUserAssessDet();
            courseuserasset.Question_Type = questionanswermodel.getQuestionModel().Question_Type;
            courseuserasset.Company_Id = String.valueOf(PreferenceUtils.getCompnayId(this));
            if (questionanswermodel.getQuestionModel().getQuestion_Type() != 1) {

                if (questionanswermodel.getChoosenAnswer() != null){
                    courseuserasset.Count_of_User_Answers = 1;
                }else {
                    courseuserasset.Count_of_User_Answers = 0;
                }
                if (questionanswermodel.getChoosenAnswer() != null && questionanswermodel.getChoosenAnswer().length() > 0) {

                    if (questionanswermodel.getChoosenAnswer().equalsIgnoreCase(questionanswermodel.getCorrectAnswer())) {
                        courseuserasset.Is_Correct = true;
                        courseuserasset.Count_Of_User_Correct_Answers = 1;
                    } else {
                        courseuserasset.Is_Correct = false;
                        courseuserasset.Count_Of_User_Correct_Answers = 0;

                    }

                }

                QuestionCourseListModel CourseHeader = questionanswermodel.getLstCourse().get(0);
                courseuserasset.Course_ID = CourseHeader.getCourse_Id();
                courseuserasset.User_Id = PreferenceUtils.getUserId(this);
                courseuserasset.Publish_ID = CourseHeader.getPublish_ID();
                courseuserasset.Section_Id = questionanswermodel.getSectionId();
                courseuserasset.Question_ID = String.valueOf(questionanswermodel.getQuestionModel().getQuestion_Id());
                courseuserasset.Couse_User_Section_Mapping_Id = questionanswermodel.getSectionMapId();
                courseuserasset.Course_User_Assignment_Id = questionanswermodel.getCourseAssignmentId();
                courseuserasset.Negative_Mark = questionanswermodel.getQuestionModel().getNegative_Mark();
                courseuserassetdetails.add(courseuserasset);

            } else {

                if (questionanswermodel.getChoosenAnswer() != null && questionanswermodel.getChoosenAnswer().length() > 0
                        && questionanswermodel.getCorrectAnswer() != null && questionanswermodel.getCorrectAnswer().length() > 0) {

                    String[] choosenanswerlist = questionanswermodel.getChoosenAnswer().split(",");
                    String[] coreectanserlist = questionanswermodel.getCorrectAnswer().split(",");

                    if (choosenanswerlist!=null){
                        courseuserasset.Count_of_User_Answers = choosenanswerlist.length;
                    }

                    if (compareArrays(choosenanswerlist, coreectanserlist)) {
                        courseuserasset.Is_Correct = true;
                        courseuserasset.Count_Of_User_Correct_Answers = coreectanserlist.length;
                    } else {
                        courseuserasset.Is_Correct = false;
                        courseuserasset.Count_Of_User_Correct_Answers =  getUserCorrectAnswer(questionanswermodel);
                    }
                }

                QuestionCourseListModel CourseHeader = questionanswermodel.getLstCourse().get(0);
                courseuserasset.Course_ID = CourseHeader.getCourse_Id();
                courseuserasset.User_Id = PreferenceUtils.getUserId(this);
                courseuserasset.Publish_ID = CourseHeader.getPublish_ID();
                courseuserasset.Section_Id = questionanswermodel.getSectionId();
                courseuserasset.Question_ID = String.valueOf(questionanswermodel.getQuestionModel().getQuestion_Id());
                courseuserasset.Couse_User_Section_Mapping_Id = questionanswermodel.getSectionMapId();
                courseuserasset.Course_User_Assignment_Id = questionanswermodel.getCourseAssignmentId();
                courseuserasset.Negative_Mark = questionanswermodel.getQuestionModel().getNegative_Mark();
                courseuserassetdetails.add(courseuserasset);

            }

        }

        return courseuserassetdetails;

    }
    private int getUserCorrectAnswer(QuestionAndAnswerModel questionanswermodel) {

        int correctanswercount = 0;


        if (questionanswermodel.getChoosenAnswer()!=null){

            String[] choosenanswer = questionanswermodel.getChoosenAnswer().split(",");
            String[] correctanswer = questionanswermodel.getCorrectAnswer().split(",");

            if (choosenanswer!=null && choosenanswer.length>0){

                for (int i=0; i<choosenanswer.length;i++){

                    String choosenanswerresult = choosenanswer[i];

                    for (int j=0;j<correctanswer.length;j++){
                        if (choosenanswerresult.equals(correctanswer[j])){
                            correctanswercount = correctanswercount+1;
                        }
                    }

                }

            }

        }

        return correctanswercount;
    }


}
