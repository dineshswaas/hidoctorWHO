package com.swaas.kangle.CheckList.chklistreport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.swaas.kangle.CheckList.model.AllSectionsQADetailModel;
import com.swaas.kangle.CheckList.model.ReportAnswersList;
import com.swaas.kangle.CheckList.model.ReportQuestionAnsersList;
import com.swaas.kangle.R;
import com.swaas.kangle.report.OnReportGenerated;
import com.swaas.kangle.report.Rotate;
import com.swaas.kangle.utils.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Sayellessh on 30-05-2018.
 */

public class ViewChecklistReportAsPdfAsyncTask extends AsyncTask<String,String,String> {

    private Context mContext;
    public BaseFont Book_Antiqua;
    private OnReportGenerated onReportGenerated;
    private List<AllSectionsQADetailModel> mySectionsListModel;
    private String mchecklistname;


    public ViewChecklistReportAsPdfAsyncTask(Context mContext, OnReportGenerated onReportGenerated, List<AllSectionsQADetailModel> sectionsReportModels,String checklistname){
        this.mContext = mContext;
        this.onReportGenerated = onReportGenerated;
        this.mySectionsListModel = sectionsReportModels;
        this.mchecklistname = checklistname;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Book_Antiqua = BaseFont.createFont("assets/arialuni.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return generatePDF();
    }

    @Override
    protected void onPostExecute(String pdfpath) {
        super.onPostExecute(pdfpath);
        if (onReportGenerated != null){
            onReportGenerated.onReportGenerate(pdfpath);
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    private String generatePDF() {

        try {

            /*File dir = new File(Environment.getExternalStorageDirectory().toString()+"/"+ Constants.Foldername
                    +"/"+Constants.CHECKLIST_REPORT+"/");
            if (!dir.exists()){
                dir.mkdir();
            }*/
            final File dir = new File(new File(Environment.getExternalStorageDirectory(), Constants.Foldername), Constants.CHECKLIST_REPORT);
            if (!dir.exists()){
                dir.mkdir();
            }

            String fileName = "ChecklistReport.pdf";
            File FILE = new File(new File(dir.getAbsolutePath(), fileName),"");
            /*File FILE = new File(Environment.getExternalStorageDirectory().toString()+"/"+ Constants.Foldername
                    +"/"+Constants.CHECKLIST_REPORT+"/"+fileName);*/

            if (FILE.exists()){
                FILE.delete();
                FILE = new File(new File(dir.getAbsolutePath(), fileName), "");
                /*FILE = new File(Environment.getExternalStorageDirectory().toString()+"/"+Constants.Foldername
                        +"/"+Constants.CHECKLIST_REPORT+"/"+fileName);*/
            }

            Document document = new Document(PageSize.A1);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();

            Rotate rotate = new Rotate();
            writer.setPageEvent(rotate);

            PdfPTable header = new PdfPTable(1);
            header.setWidthPercentage(100);
            header.addCell(getTbaleCell(mContext.getResources().getString(R.string.checklisttitle)+": "+mchecklistname
                    +"\n"+mContext.getResources().getString(R.string.onbehalfof)+" "+mySectionsListModel.get(0).On_Behalf
                    +"\n"+mContext.getResources().getString(R.string.completed_by)+" "+mySectionsListModel.get(0).Submitted_By
                    +"\n"+mContext.getResources().getString(R.string.submitted_date)+" "+mySectionsListModel.get(0).Submitted_Date,
                    Element.ALIGN_LEFT, new BaseColor(246, 246, 246), new BaseColor(36, 64, 97)));
            document.add(header);
            document.add(new Phrase("\n"));

            for(int j=0; j< mySectionsListModel.size(); j++){
            AllSectionsQADetailModel secmodel = mySectionsListModel.get(j);
                PdfPTable cpt = new PdfPTable(1);
                cpt.setWidthPercentage(100);
                cpt.addCell(getTbaleCell(secmodel.getSectionName(), Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                document.add(cpt);
                document.add(new Phrase("\n"));
                PdfPTable ContentPaymentTable = new PdfPTable(3);
                ContentPaymentTable.setWidthPercentage(100);
                ContentPaymentTable.addCell(getTbaleCell(mContext.getResources().getString(R.string.question_no), Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell(mContext.getResources().getString(R.string.question), Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell(mContext.getResources().getString(R.string.response), Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                //ContentPaymentTable.addCell(getTbaleCell(mContext.getResources().getString(R.string.remarks), Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.setHeaderRows(1);
                for (int i=0; i< secmodel.getLstQuestions().size(); i++){

                    ReportQuestionAnsersList myQuestionModel = secmodel.getLstQuestions().get(i);

                    if (i%2 == 0){
                        if(myQuestionModel.getQuestion_Number_Title() != null && !myQuestionModel.getQuestion_Number_Title().isEmpty()){
                            ContentPaymentTable.addCell(getTbaleCell(myQuestionModel.getQuestion_Number_Title()+"\n",Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        }else{
                            ContentPaymentTable.addCell(getTbaleCell((i+1)+"\n",Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        }
                        ContentPaymentTable.addCell(getTbaleCell(myQuestionModel.getQuestionText(),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        String answerslist = "";
                        for(ReportAnswersList ansList : myQuestionModel.getLstAnswers()){
                            answerslist = ansList.getAnswerText()+",";
                        }
                        String substring = null;
                        if(answerslist != null && !answerslist.isEmpty()) {
                            substring = answerslist.substring(0, answerslist.length() - 1);
                        }
                        ContentPaymentTable.addCell(getTbaleCell(substring,Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        //ContentPaymentTable.addCell(getTbaleCell(myQuestionModel.getQuestion_Remarks(),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                    }else {
                        if(myQuestionModel.getQuestion_Number_Title() != null && !myQuestionModel.getQuestion_Number_Title().isEmpty()){
                            ContentPaymentTable.addCell(getTbaleCell(myQuestionModel.getQuestion_Number_Title()+"\n",Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        }else{
                            ContentPaymentTable.addCell(getTbaleCell((i+1)+"\n",Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        }
                        ContentPaymentTable.addCell(getTbaleCell(myQuestionModel.getQuestionText(),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        String answerslist = "";
                        for(ReportAnswersList ansList : myQuestionModel.getLstAnswers()){
                            answerslist = ansList.getAnswerText()+",";
                        }
                        String substring = null;
                        if(answerslist != null && !answerslist.isEmpty()) {
                            substring = answerslist.substring(0, answerslist.length() - 1);
                        }
                        ContentPaymentTable.addCell(getTbaleCell(substring,Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        //ContentPaymentTable.addCell(getTbaleCell(myQuestionModel.getQuestion_Remarks(),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                    }
                }

                document.add(ContentPaymentTable);
                document.add(new Phrase("\n"));
            }

            PdfPTable bluelinetable = new PdfPTable(1);
            bluelinetable.setWidthPercentage(100);
            BaseColor baseColor = new BaseColor(28, 214, 214);
            bluelinetable.addCell(getEmptyCell("", Element.ALIGN_CENTER, 7f, baseColor));
            document.add(bluelinetable);
            PdfPTable greylinetable = new PdfPTable(1);
            greylinetable.setWidthPercentage(100);
            greylinetable.addCell(getEmptyCell("", Element.ALIGN_CENTER, 3f, BaseColor.GRAY));
            document.add(greylinetable);
            document.add(new Phrase("\n"));
            document.close();

            return FILE.getPath();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public PdfPCell getTbaleCell(String text, int alignment, BaseColor basebackground, BaseColor basefontcolor) {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text);
        p.setFont(new Font(Book_Antiqua, 17, Font.BOLD, basefontcolor));
        p.setAlignment(alignment);
        cell.addElement(p);
        cell.setVerticalAlignment(alignment);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(2f);
        cell.setBorderWidth(2);
        cell.setPaddingBottom(10f);
        cell.setPaddingLeft(10f);
        cell.setBackgroundColor(basebackground);
        cell.setBorderColor(BaseColor.WHITE);
        return cell;
    }

    public PdfPCell getEmptyCell(String text, int alignment, float height, BaseColor baseColor) {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text);
        p.setFont(new Font(Book_Antiqua, 14, Font.BOLD, BaseColor.BLACK));
        p.setAlignment(alignment);
        cell.setBackgroundColor(baseColor);
        cell.addElement(p);
        cell.setFixedHeight(height);
        cell.setVerticalAlignment(alignment);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public static PdfPCell createImageCell(Image img) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell(img, true);
        cell.setPadding(5f);
        cell.setBorderWidth(10f);
        cell.setBorderColor(BaseColor.WHITE);
        return cell;
    }

    class MyFooter extends PdfPageEventHelper {


        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            super.onStartPage(writer, document);


        }

        public void onEndPage(PdfWriter writer, Document document) {


            PdfContentByte cb = writer.getDirectContent();
            Rectangle rect = new Rectangle(document.getPageSize());
            rect.enableBorderSide(1);
            rect.enableBorderSide(2);
            rect.enableBorderSide(4);
            rect.enableBorderSide(8);
            rect.setBorder(2);
            rect.setBorder(Rectangle.BOX);
            rect.setBorderWidth(10);
            BaseColor baseColor = new BaseColor(41,81,121);
            rect.setBorderColor(baseColor);
            try {
                document.add(rect);
            } catch (DocumentException e) {
                e.printStackTrace();
            }

        }
    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    private Bitmap loadImageFromStorage(int type)
    {

        Bitmap b =null;
        try {
            File directory = mContext.getDir("imageDir", Context.MODE_PRIVATE);
            if (directory != null){
                File f = null;
                if (type == 1){
                    f = new File(directory.getAbsolutePath(), "profile.jpg");
                }else if (type == 2){
                    f = new File(directory.getAbsolutePath(), "cover.jpg");
                }
                if (f != null){
                    b = BitmapFactory.decodeStream(new FileInputStream(f));
                }
            }

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return b;

    }

    public  class MyBorder implements PdfPCellEvent {

        public void cellLayout(PdfPCell cell, Rectangle position,
                               PdfContentByte[] canvases) {
            float x1 = position.getLeft() + 5;
            float x2 = position.getRight() - 5;
            float y1 = position.getTop() - 5;
            float y2 = position.getBottom() + 5;
            PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
            canvas.rectangle(x1, y1, x2 - x1, y2 - y1);
            BaseColor baseColor = new BaseColor(23, 55, 94);
            canvas.stroke();
            canvas.setColorStroke(baseColor);
        }
    }

    public  class FillBorder implements PdfPCellEvent {

        public void cellLayout(PdfPCell cell, Rectangle position,
                               PdfContentByte[] canvases) {
            float x1 = position.getLeft() + 5;
            float x2 = position.getRight() - 5;
            float y1 = position.getTop() - 5;
            float y2 = position.getBottom() + 5;
            PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
            canvas.rectangle(x1, y1, x2 - x1, y2 - y1);
            canvas.stroke();
            canvas.setColorStroke(BaseColor.WHITE);

        }
    }
}
