package com.swaas.kangle.LPCourse.questionbuilder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.swaas.kangle.LPCourse.CourseListActivity;
import com.swaas.kangle.LPCourse.LPCourseService;
import com.swaas.kangle.LPCourse.Report.LPCourseReportActivity;
import com.swaas.kangle.LPCourse.model.AnwerUploadModel;
import com.swaas.kangle.LPCourse.model.CourseUserAnswers;
import com.swaas.kangle.LPCourse.model.CourseUserAssessDet;
import com.swaas.kangle.LPCourse.model.CourseUserAssessHeader;
import com.swaas.kangle.LPCourse.model.QuestionAndAnswerModel;
import com.swaas.kangle.LPCourse.model.QuestionCourseListModel;
import com.swaas.kangle.LPCourse.questionbuilder.adapter.MultiPleQuestionPerPage;
import com.swaas.kangle.LPCourse.questionbuilder.adapter.QuestionPageSlider;
import com.swaas.kangle.LPCourse.questionbuilder.customviews.QuestionViewPager;
import com.swaas.kangle.LPCourse.questionbuilder.db.TestResultRepository;
import com.swaas.kangle.R;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.CommonUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.NetworkUtils;
import com.tmall.ultraviewpager.UltraViewPager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private UltraViewPager mQuestionPager;
    private CountDownTimer mTestTimer;
    private LinearLayout mMutipleQuestionOptionHolder;
    private TextView mTimerText,mBtnSubmit,section_nametext,time_expiry_text;
    public ArrayList<QuestionAndAnswerModel> questionandanswerlist;
    Retrofit retrofitAPI;
    LPCourseService lpService;
    ProgressDialog mProgressDialog;
    private long TimeAsMilli;
    private TestResultRepository testResultRepository;
    private RecyclerView mMutipleQuestionRecycleView;
    private  boolean isSingleQuestionPerPage = false;
    private int QuestionLoadCount;
    private Point point;
    String validtodate,sectionName;
    int courseId,SectionId,publishId;
    String CourseThumbnail;

    RelativeLayout question_timer_holder;
    MultiPleQuestionPerPage multiPleQuestionPerPage;
    Boolean isMannualCourse = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        mQuestionPager = (UltraViewPager) findViewById(R.id.pager_questions);
        mTimerText = (TextView) findViewById(R.id.question_time_text);
        testResultRepository =  new TestResultRepository(this);
        mMutipleQuestionOptionHolder = (LinearLayout) findViewById(R.id.multiple_question_holder);
        mMutipleQuestionRecycleView = (RecyclerView) findViewById(R.id.multiple_question_view);
        mBtnSubmit = (TextView) findViewById(R.id.btn_submit_mutiple);

        section_nametext = (TextView) findViewById(R.id.section_name_text);

        question_timer_holder = (RelativeLayout) findViewById(R.id.question_timer_holder);
        time_expiry_text = (TextView) findViewById(R.id.time_expiry_text);

        mMutipleQuestionRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mMutipleQuestionRecycleView.setItemAnimator(new DefaultItemAnimator());
        mBtnSubmit.setOnClickListener(this);
        setthemeforView();
        GetQuestionList();
        isMannualCourse =  getIntent().getBooleanExtra(Constants.Evaluation_Mode,false);
        if(isSingleQuestionPerPage){

            mMutipleQuestionOptionHolder.setVisibility(View.GONE);
            mQuestionPager.setVisibility(View.VISIBLE);
            QuestionPageSlider questionPageSlider = new QuestionPageSlider(getSupportFragmentManager(),questionandanswerlist);
            mQuestionPager.setAdapter(questionPageSlider);
            mQuestionPager.initIndicator();
            mQuestionPager.getIndicator()
                    .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                    .setFocusColor(getResources().getColor(R.color.submitbuttoncolor))
                    .setNormalColor(Color.GRAY)
                    .setRadius((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
            //set the alignment
            mQuestionPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
            //construct built-in indicator, and add it to  UltraViewPager
            mQuestionPager.getIndicator().build();
            //set an infinite loop
          //  mQuestionPager.setInfiniteLoop(true);
            //enable auto-scroll mode



            }else {

            mMutipleQuestionOptionHolder.setVisibility(View.VISIBLE);
            mQuestionPager.setVisibility(View.GONE);
            multiPleQuestionPerPage =  new MultiPleQuestionPerPage(this,questionandanswerlist,this);
            mMutipleQuestionRecycleView.setAdapter(multiPleQuestionPerPage);

        }
        if (isMannualCourse)
        {

            StartTimer(PreferenceUtils.getTimer(QuestionActivity.this));
        }
        else {
            StartTimer(TimeAsMilli);
        }



    }

    public void setthemeforView(){
        question_timer_holder.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        section_nametext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        time_expiry_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        mTimerText.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
    }

    private void GetQuestionList() {

        if (getIntent() != null){

            questionandanswerlist = (ArrayList<QuestionAndAnswerModel>) getIntent().getSerializableExtra("value");
            if (questionandanswerlist.size() > 0)
            {
                PreferenceUtils.setQuestionAnswerList("key",questionandanswerlist,QuestionActivity.this);
            }
            courseId = getIntent().getIntExtra(Constants.Course_Id,0);
            publishId =  getIntent().getIntExtra(Constants.Publish_Id,0);
            SectionId = getIntent().getIntExtra(Constants.Section_Id,0);
            validtodate = getIntent().getStringExtra("SectionDate");
            sectionName = getIntent().getStringExtra("SectionName");
            CourseThumbnail = getIntent().getStringExtra(Constants.Course_Thumbnail);
            QuestionLoadCount = questionandanswerlist.get(0).getLstCourse().get(0).getQuestionLoadCount();
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            try {
                String kept;
                String DateAsString = questionandanswerlist.get(0).getLstCourse().get(0).getDuration();
                kept = DateAsString.substring( 0, DateAsString.indexOf("."));
                if (!TextUtils.isEmpty(getIntent().getStringExtra("Timer"))) {
                    kept = getIntent().getStringExtra("Timer");
                }
                date = sdf.parse(kept);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            long  hoursAsMilli = TimeUnit.HOURS.toMillis(date.getHours());
            long minutesAsMilli  =  TimeUnit.MINUTES.toMillis(date.getMinutes());
            long secondsAsMilli = TimeUnit.SECONDS.toMillis(date.getSeconds());
            TimeAsMilli = hoursAsMilli+minutesAsMilli+secondsAsMilli;

            if (questionandanswerlist.get(0).getLstCourse().get(0).getQuestionLoadCount() == 0){

                isSingleQuestionPerPage = false;

            }else if (questionandanswerlist.get(0).getLstCourse().get(0).getQuestionLoadCount() == 1){

                isSingleQuestionPerPage = true;
            }

        }

        if(sectionName !=  null){
            section_nametext.setText(sectionName.toString());
        }
    }


    private void StartTimer(long minutesAsMilli) {

        mTestTimer = new CountDownTimer(minutesAsMilli,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                mTimerText.setText(String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), // The change is in this line
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                if (isMannualCourse)
                {
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
                    PreferenceUtils.setTimer(QuestionActivity.this,TimeAsMilli);
                }

            }

            @Override
            public void onFinish() {
//                PreferenceUtils.setTimer(QuestionActivity.this,TimeAsMilli);
                mTimerText.setText("00:00:00");
                OnTimeOutOccured();

            }

        }.start();

    }

    public void loaditem(int position){
        multiPleQuestionPerPage.notifyDataSetChanged();
    }


    private void ShowAlert(String Message, String Title, final boolean offline) {

        AlertDialog alertDialog = new AlertDialog.Builder(
                QuestionActivity.this).create();

        alertDialog.setCancelable(false);
        // Setting Dialog Title
        alertDialog.setTitle(Title);
        // Setting Dialog Message
        alertDialog.setMessage(Message);

        // Setting OK Button
        alertDialog.setButton(QuestionActivity.this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                //StartDashboardActivtiy();
                if(offline){
                    StartDashboardActivtiy();
                }else {
                    if (isMannualCourse)
                    {
                        QuestionActivity.this.finish();
                    }
                    else
                    {
                        StartReportActiity();
                    }

                }

            }
        });
        // Showing Alert Message
        alertDialog.show();

    }

    private void OnQuestionFinishedEvent() {

        if (questionandanswerlist.size()>0){

            Checknetworkandupload(UploadAnswerProcess(questionandanswerlist),true,false, false);

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
        courseAssetHeader.Achieved_Percentage = Integer.parseInt(CalculatePercentage());
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
                    courseuseranswer.Answer_Id = questionandanswermodel.getChoosenAnswerId();
                    courseuseranswer.Question_Id = String.valueOf(questionandanswermodel.getQuestionModel().getQuestion_Id());
                }
                else if (questionandanswermodel.getQuestionModel().getQuestion_Type() !=2){

                    courseuseranswer.Company_Id = String.valueOf(PreferenceUtils.getCompnayId(this));
                    courseuseranswer.Text = questionandanswermodel.getChoosenAnswer();
                    courseuseranswer.User_Id = PreferenceUtils.getUserId(this);
                    courseuseranswer.User_Answer_Text = questionandanswermodel.getChoosenAnswer();
                    courseuseranswer.Answer_Id = questionandanswermodel.getChoosenAnswerId();
                    courseuseranswer.Question_Id = String.valueOf(questionandanswermodel.getQuestionModel().getQuestion_Id());
                    if(questionandanswermodel.getChoosenAnswer()==null)
                    {
                        for (int i=0;i<questionandanswermodel.getLstAnswer().size();i++)
                        {
                            if (courseuseranswer.User_Answer_Text == questionandanswermodel.getLstAnswer().get(i).getAnswer_Text())
                            {
                                courseuseranswer.Answer_Id = String.valueOf(questionandanswermodel.getLstAnswer().get(i).getAnswer_Id());
                            }
                        }
                    }

                }else {

                    courseuseranswer.Company_Id = String.valueOf(PreferenceUtils.getCompnayId(this));
                    courseuseranswer.Answer_Id = questionandanswermodel.getChoosenAnswerId();
                    courseuseranswer.User_Id = PreferenceUtils.getUserId(this);
                    courseuseranswer.User_Answer_Text = questionandanswermodel.getChoosenAnswer();
                    if(questionandanswermodel.getChoosenAnswer()==null)
                    {
                        for (int i=0;i<questionandanswermodel.getLstAnswer().size();i++)
                        {
                            if (courseuseranswer.User_Answer_Text == questionandanswermodel.getLstAnswer().get(i).getAnswer_Text())
                            {
                                courseuseranswer.Answer_Id = String.valueOf(questionandanswermodel.getLstAnswer().get(i).getAnswer_Id());
                            }
                        }
                    }
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
                        courseanswer.User_Answer_Text = questionandanswermodel.getChoosenAnswer();
                        courseanswer.Answer_Id = choosenanswerid[i];
                        courseanswer.Question_Id = String.valueOf(questionandanswermodel.getQuestionModel().getQuestion_Id());
                        if(questionandanswermodel.getChoosenAnswer()==null)
                        {
                            for (int j=0;i<questionandanswermodel.getLstAnswer().size();j++)
                            {
                                if (courseanswer.User_Answer_Text == questionandanswermodel.getLstAnswer().get(j).getAnswer_Text())
                                {
                                    courseanswer.Answer_Id = String.valueOf(questionandanswermodel.getLstAnswer().get(j).getAnswer_Id());
                                }
                            }
                        }
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
                    }
                    else {
                        if(questionanswermodel.getQuestionModel().getQuestion_Type() == 6)
                        {
                            courseuserasset.Is_Correct = true;
                            courseuserasset.Count_Of_User_Correct_Answers = 1;
                        }
                        else if (questionanswermodel.getQuestionModel().getQuestion_Type() == 7)
                        {
                            int count = 0;
                            for (int i = 0; i < questionanswermodel.getLstMatchingQA().size();i++)
                            {
                                if(questionanswermodel.getLstRandom().get(i).getChoosenAnswer().equalsIgnoreCase(questionanswermodel.getLstMatchingQA().get(i).getSubAnswertext()))
                                {
                                    count = count + 1;
                                }
                            }
                            if(count == questionanswermodel.getLstMatchingQA().size())
                            {
                                courseuserasset.Is_Correct = true;
                                courseuserasset.Count_Of_User_Correct_Answers = 1;
                            }
                            else
                            {
                                courseuserasset.Is_Correct = false;
                                courseuserasset.Count_Of_User_Correct_Answers = 0;
                            }
                        }
                        else {
                            courseuserasset.Is_Correct = false;
                            courseuserasset.Count_Of_User_Correct_Answers = 0;
                        }
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
            else {

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





  /*  private int getCountofCorrectAnswer(ArrayList<QuestionAndAnswerModel> questionandanswerlist){

        int totalquestion = questionandanswerlist.size();
        int correctanswer = 0;

        for (QuestionAndAnswerModel questionandanswermodel : questionandanswerlist){

            Log.d("==>correctanswer",""+questionandanswermodel.getCorrectAnswer());
            Log.d("==>choosenanswer",""+questionandanswermodel.getChoosenAnswer());


            if (questionandanswermodel.getQuestionModel().getQuestion_Type() != 1){

                if (questionandanswermodel.getChoosenAnswer()!=null && questionandanswermodel.getChoosenAnswer().length() > 0){

                    if (questionandanswermodel.getChoosenAnswer().equalsIgnoreCase(questionandanswermodel.getCorrectAnswer())){
                        correctanswer = correctanswer+1;
                    }

                }

            }
            else {

                if (questionandanswermodel.getChoosenAnswer() != null && questionandanswermodel.getChoosenAnswer().length()>0){

                    String [] choosenanswerlist = questionandanswermodel.getChoosenAnswer().split(",");
                    String [] coreectanserlist = questionandanswermodel.getCorrectAnswer().split(",");
                    if (compareArrays(choosenanswerlist,coreectanserlist)){
                        correctanswer = choosenanswerlist.length;
                    }else {

                        correctanswer = 1;

                    }

                }

            }

        }

        Log.d("==>test",""+correctanswer);
        Log.d("==>total",""+totalquestion);


        return correctanswer;
    }
*/



    private String CalculatePercentage() {

        float totalquestion = questionandanswerlist.size();
        float correctanswer = 0;

        for (QuestionAndAnswerModel questionandanswermodel : questionandanswerlist){

            Log.d("==>correctanswer",""+questionandanswermodel.getCorrectAnswer());
            Log.d("==>choosenanswer",""+questionandanswermodel.getChoosenAnswer());

            if (questionandanswermodel.getQuestionModel().getQuestion_Type() != 1){


                if (questionandanswermodel.getChoosenAnswer()!=null && questionandanswermodel.getChoosenAnswer().length() > 0){

                    if (questionandanswermodel.getChoosenAnswer().equalsIgnoreCase(questionandanswermodel.getCorrectAnswer())){
                        correctanswer = correctanswer+1;
                    }else {

                        if (questionandanswermodel.getQuestionModel().isNegative()){

                            if (questionandanswermodel.getQuestionModel().getNegative_Mark()!= 0){

                                correctanswer = correctanswer - questionandanswermodel.getQuestionModel().getNegative_Mark();

                            }
                        }

                    }

                }

            }
            else {

                if (questionandanswermodel.getChoosenAnswer() != null && questionandanswermodel.getChoosenAnswer().length()>0
                        && questionandanswermodel.getCorrectAnswer() != null && questionandanswermodel.getCorrectAnswer().length()>0){

                    String [] choosenanswerlist = questionandanswermodel.getChoosenAnswer().split(",");
                    String [] coreectanserlist = questionandanswermodel.getCorrectAnswer().split(",");
                    if (compareArrays(choosenanswerlist,coreectanserlist)){
                        correctanswer = correctanswer +1;
                    }else {

                        if (questionandanswermodel.getQuestionModel().isNegative()){

                            if (questionandanswermodel.getQuestionModel().getNegative_Mark()!= 0){

                                correctanswer = correctanswer - questionandanswermodel.getQuestionModel().getNegative_Mark();

                            }
                        }

                    }

                }

            }

        }

        Log.d("==>test",""+correctanswer);
        Log.d("==>total",""+totalquestion);
        float percentage ;


        if (correctanswer < 0 ){

            percentage = 0;

        }else {

            percentage = (correctanswer/totalquestion) * 100;
        }

        int per = (int) percentage;
        return String.valueOf(per);

    }

    public static boolean compareArrays(String[] arr1, String[] arr2) {
        HashSet<String> set1 = new HashSet<String>(Arrays.asList(arr1));
        HashSet<String> set2 = new HashSet<String>(Arrays.asList(arr2));
        return set1.equals(set2);
    }

    private void StartDashboardActivtiy() {

        //Intent intent =  new Intent(this, LandingActivity.class);
        Intent intent =  new Intent(this, CourseListActivity.class);
        intent.putExtra(com.swaas.kangle.utils.Constants.From_Question,false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private void StartReportActiity() {

        Intent intent = new Intent(this,LPCourseReportActivity.class);
        intent.putExtra(Constants.Course_Id,courseId);
        intent.putExtra(Constants.Publish_Id,publishId);
        intent.putExtra(Constants.Section_Id,SectionId);
        intent.putExtra("SectionDate",validtodate);
        intent.putExtra("isfromtest",true);
        intent.putExtra("Course_Status_INT", 0);
        intent.putExtra(Constants.Course_Thumbnail, CourseThumbnail);
        startActivity(intent);
        finish();

    }

    public void Checknetworkandupload(final String AnswerModelString, final boolean isLastQuestion, final boolean isTimeout, final boolean isBackpressed){

        if(NetworkUtils.checkIfNetworkAvailable(QuestionActivity.this)){
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

                        if (!isBackpressed){
                            dismissProgressDialog();
                            if (courseAssetListModel.contains("COMPLETED")){
                                PreferenceUtils.setQuestionAnswerList("key",null,QuestionActivity.this);
                                if (isTimeout){
                                    ShowAlert(getResources().getString(R.string.time_Out),getResources().getString(R.string.warning),false);
                                }else {
                                    ShowAlert(getResources().getString(R.string.finished),"",false);

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

                    testResultRepository.insertTestRecord(AnswerModelString,CalculatePercentage(),QuestionLoadCount,isLastQuestion+"",isTimeout+"");
                    ShowAlert(getResources().getString(R.string.testsavedoffline),getResources().getString(R.string.warning),true);
                    Log.d(t.toString(),"Error");
                }
            });
        }else{

            testResultRepository.insertTestRecord(AnswerModelString,CalculatePercentage(),QuestionLoadCount,isLastQuestion+"",isTimeout+"");
            ShowAlert(getResources().getString(R.string.testsavedoffline),getResources().getString(R.string.warning),true);
        }
    }


    @Override
    public void onBackPressed() {

        /*AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(getString(R.string.question_interception_text))

                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }})

                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {

                        if (questionandanswerlist.size()>0){
                            Checknetworkandupload(UploadAnswerProcess(questionandanswerlist), false, false, true);
                        }
                        finish();
                    }

                }).show();*/



    }

    private void OnTimeOutOccured() {

        if (questionandanswerlist.size()>0){
            Checknetworkandupload(UploadAnswerProcess(questionandanswerlist), false, true, false);

        }

    }


    @Override
    protected void onPause() {
        super.onPause();


    }


    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if( mTestTimer != null){
            mTestTimer.cancel();
        }

    }

    public void onSubmitAnswer(int position){


        if (position >= questionandanswerlist.size() ){

            OnQuestionFinished();

        }else {

            mQuestionPager.setCurrentItem(mQuestionPager.getCurrentItem()+1);

        }

    }

    public void showSelectedView(int position){
        mQuestionPager.setCurrentItem(position);
    }

    private void OnQuestionFinished() {

        boolean isAnswereAllQuestion = false;
        int position = -1;
        for (QuestionAndAnswerModel questionAndAnswerModel :questionandanswerlist){
            position++;
            if (questionAndAnswerModel.getChoosenAnswer()!=null && questionAndAnswerModel.getChoosenAnswer().length() > 0){
                isAnswereAllQuestion = true;
            }else {
                isAnswereAllQuestion = false;
                break;

            }

        }

        if (isAnswereAllQuestion){

            mTestTimer.cancel();
            OnQuestionFinishedEvent();

        }else {
            if(position > -1 && mQuestionPager != null && mQuestionPager.getChildCount() > 0){
                mQuestionPager.setCurrentItem(position,true);
            }
            Toast.makeText(this,getResources().getString(R.string.validation_msg_question),Toast.LENGTH_SHORT).show();

        }
        //ShowAlert("You have finished the test.","Congrats", false);

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


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_submit_mutiple:

                if (questionandanswerlist.size()>0){
                    OnQuestionFinished();
                }

                break;

            default:

                break;

        }

    }
}
