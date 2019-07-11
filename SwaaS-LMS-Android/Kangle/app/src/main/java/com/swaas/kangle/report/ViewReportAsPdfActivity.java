package com.swaas.kangle.report;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.swaas.kangle.API.service.CourseService;
import com.swaas.kangle.R;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.playerPart.customviews.pdf.PDFView;
import com.swaas.kangle.playerPart.customviews.pdf.listener.OnLoadCompleteListener;
import com.swaas.kangle.playerPart.customviews.pdf.listener.OnPageChangeListener;
import com.swaas.kangle.playerPart.customviews.pdf.listener.OnRenderListener;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.report.modle.MyCourseReportModel;
import com.swaas.kangle.utils.CommonUtils;
import com.swaas.kangle.utils.Constants;

import java.io.File;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ViewReportAsPdfActivity extends AppCompatActivity  implements OnReportGenerated, OnPageChangeListener, OnLoadCompleteListener, OnRenderListener, View.OnClickListener {

     private PDFView mPdfView;
     private ProgressDialog mProgressDialog;
     private CourseService courseService;
     Retrofit retrofitAPI;
     private ImageView mBtnBack,mActionShare;
     public String CourseStatus,modulename;
    public int moduletype,isTeamOrSelf;
     public String mPdfPath;
    RelativeLayout header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report_as_pdf);
        mPdfView = (PDFView) findViewById(R.id.mReportPdfView);
        mBtnBack = (ImageView) findViewById(R.id.btn_back);
        header = (RelativeLayout) findViewById(R.id.header);
        mActionShare = (ImageView) findViewById(R.id.action_share);
        mPdfView.useBestQuality(false);
        retrofitAPI = RetrofitAPIBuilder.getInstance();
        courseService = retrofitAPI.create(CourseService.class);
        getStatus();
        mBtnBack.setOnClickListener(this);
        mActionShare.setOnClickListener(this);

        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));


    }

    private void getStatus() {

         if (getIntent()!=null){

             CourseStatus = getIntent().getStringExtra(com.swaas.kangle.utils.Constants.Course_Status);
             modulename = getIntent().getStringExtra(com.swaas.kangle.utils.Constants.moduleName);
             moduletype = getIntent().getIntExtra(com.swaas.kangle.utils.Constants.moduleType,0);
             isTeamOrSelf = getIntent().getIntExtra(com.swaas.kangle.utils.Constants.isTeamOrSelf,0);
             getReportList();
         }

    }

    private void getReportList() {

        showProgressDialog();
        String subdomainName = PreferenceUtils.getSubdomainName(this);
        int CompanyId = PreferenceUtils.getCompnayId(this);
        int UserId = PreferenceUtils.getUserId(this);

        //Call call = courseService.getMyCourseReportList(subdomainName,CompanyId,UserId, CommonUtils.getUtcOffset(),0,CourseStatus);
        //Call call = courseService.getMyCourseReportList(subdomainName,CompanyId,UserId, CommonUtils.getUtcOffsetincluded10k(),0,CourseStatus);
        Call call = courseService.getMyCommonReportList(subdomainName,CompanyId,UserId, CommonUtils.getUtcOffsetincluded10k(),isTeamOrSelf,CourseStatus,moduletype);
        call.enqueue(new Callback<List<MyCourseReportModel>>() {

            @Override
            public void onResponse(Response<List<MyCourseReportModel>> response, Retrofit retrofit) {
                List<MyCourseReportModel> myCourseReportModels = response.body();
                Log.d("Size",myCourseReportModels.size()+"");
                GenratePdf(myCourseReportModels,CourseStatus,modulename,isTeamOrSelf);

            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(ViewReportAsPdfActivity.this,getResources().getString(R.string.erroroccured),Toast.LENGTH_LONG).show();
                Log.d("retrofit","error 2");
                dismissProgressDialog();
                //emptyview.setVisibility(View.VISIBLE);
            }
        });

    }

    private void GenratePdf(List<MyCourseReportModel> myCourseReportModels, String courseStatus, String modulename,int isTeamOrSelf) {
        String repttype = "";
        if(isTeamOrSelf == 1){
            repttype = "Team";
        }else{
            repttype = "My";
        }
        ViewReportAsPdfAsyncTask viewReportAsPdfAsyncTask = new ViewReportAsPdfAsyncTask(this,this,myCourseReportModels,courseStatus,modulename,repttype);
        viewReportAsPdfAsyncTask.execute();


    }


    @Override
    public void onReportGenerate(String pdfpath) {

        if (pdfpath != null){

            mPdfPath = pdfpath;
            mPdfView.fromFile(new File(pdfpath)).defaultPage(0)
                    .onPageChange(this)
                    .enableAnnotationRendering(true)
                    .onRender(this)
                    .onLoad(this)
                    .load();

        }else{

            Log.d("Error","Error Occured");
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void loadComplete(int nbPages) {

        dismissProgressDialog();

    }

    @Override
    public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {

        mPdfView.fitToWidth();

    }


    public void showProgressDialog() {
        if(!isFinishing()) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getResources().getString(R.string.loadingpleasewait));
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if(!isFinishing()){
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_back:
                 onBackPressed();
                break;

            case R.id.action_share:

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                Uri screenshotUri = Uri.parse("file:///"+mPdfPath);
                sharingIntent.setType("application/pdf");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                startActivity(sharingIntent);

                break;

            default:
                break;


        }

    }
}
