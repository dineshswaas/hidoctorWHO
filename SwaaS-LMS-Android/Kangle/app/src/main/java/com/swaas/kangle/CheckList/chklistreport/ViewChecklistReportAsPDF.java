package com.swaas.kangle.CheckList.chklistreport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.swaas.kangle.CheckList.model.AllSectionsQADetailModel;
import com.swaas.kangle.R;
import com.swaas.kangle.playerPart.customviews.pdf.PDFView;
import com.swaas.kangle.playerPart.customviews.pdf.listener.OnLoadCompleteListener;
import com.swaas.kangle.playerPart.customviews.pdf.listener.OnPageChangeListener;
import com.swaas.kangle.playerPart.customviews.pdf.listener.OnRenderListener;
import com.swaas.kangle.report.OnReportGenerated;
import com.swaas.kangle.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewChecklistReportAsPDF extends AppCompatActivity implements OnReportGenerated, OnPageChangeListener, OnLoadCompleteListener, OnRenderListener, View.OnClickListener {

    private PDFView mPdfView;
    private ProgressDialog mProgressDialog;
    private ImageView mBtnBack,mActionShare;
    public String CourseStatus;
    public String mPdfPath;
    ArrayList<AllSectionsQADetailModel> sectionslist;
    public String checklistname;
    RelativeLayout header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_checklist_report_as_pdf);

        mPdfView = (PDFView) findViewById(R.id.mReportPdfView);
        mBtnBack = (ImageView) findViewById(R.id.btn_back);
        header = (RelativeLayout) findViewById(R.id.header);
        mActionShare = (ImageView) findViewById(R.id.action_share);
        mPdfView.useBestQuality(false);
        getReportList();
        mBtnBack.setOnClickListener(this);
        mActionShare.setOnClickListener(this);

        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
    }

    public void getReportList(){
        if (getIntent() != null) {
            sectionslist = (ArrayList<AllSectionsQADetailModel>) getIntent().getSerializableExtra("value");
            checklistname = (String) getIntent().getSerializableExtra("checklistname");
        }
        GenratePdf(sectionslist);
    }

    public void showProgressDialog() {
        if(!isFinishing()) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getResources().getString(R.string.loading));
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

    private void GenratePdf(List<AllSectionsQADetailModel> myChecklistReportModels) {

        ViewChecklistReportAsPdfAsyncTask viewReportAsPdfAsyncTask = new ViewChecklistReportAsPdfAsyncTask(this,this,myChecklistReportModels,checklistname);
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

            Toast.makeText(this,pdfpath,Toast.LENGTH_LONG).show();
        }else{
            Log.d("Error","Error Occured");
        }
    }

    @Override
    public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
        mPdfView.fitToWidth();
    }

    @Override
    public void loadComplete(int nbPages) {
        dismissProgressDialog();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }
}
