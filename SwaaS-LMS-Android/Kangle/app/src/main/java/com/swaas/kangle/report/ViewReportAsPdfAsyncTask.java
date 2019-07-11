package com.swaas.kangle.report;

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
import com.swaas.kangle.R;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.report.modle.MyCourseReportModel;
import com.swaas.kangle.utils.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ubuntu on 19/10/16.
 */
public class ViewReportAsPdfAsyncTask extends AsyncTask<String,String,String> {
    
    private Context mContext;
    //private int mReportType; // 0. Attendance 1.Performance  2. Payment
    private int mSelectedMonth, mCourseId, mStudentId;
    private boolean mIsSend;
    private String State;
    public  BaseFont  Book_Antiqua;
    private OnReportGenerated onReportGenerated;
    private List<MyCourseReportModel> myCourseReportModels;
    private String mCourseStatus,mModuleName;
    private String mReportType;

    public ViewReportAsPdfAsyncTask(Context mContext, OnReportGenerated onReportGenerated, List<MyCourseReportModel> myCourseReportModels, String courseStatus,String modulename,String repttype){
        this.mContext = mContext;
        this.onReportGenerated = onReportGenerated;
        this.myCourseReportModels = myCourseReportModels;
        this.mCourseStatus = courseStatus;
        this.mModuleName = modulename;
        this.mReportType = repttype;
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


            /*File dir = new File(Environment.getExternalStorageDirectory().toString() + "/");
            if (!dir.exists()){
                dir.mkdir();
            }*/

            final File dir = new File(new File(Environment.getExternalStorageDirectory(), Constants.Foldername), "");
            if (!dir.exists()){
                dir.mkdir();
            }

            String fileName = (this.mModuleName+"_"+this.mReportType+"Report.pdf").toString();
            File FILE = new File(new File(Environment.getExternalStorageDirectory(), Constants.Foldername), fileName);
            //File FILE = new File(Environment.getExternalStorageDirectory().toString()+"/"+Constants.Foldername+"/"+fileName);

            if (FILE.exists()){
                FILE.delete();
                FILE = new File(new File(Environment.getExternalStorageDirectory(), Constants.Foldername), fileName);
                //FILE = new File(Environment.getExternalStorageDirectory().toString()+"/"+Constants.Foldername+"/"+fileName);
            }

            Document document = new Document(PageSize.A1);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();

            Rotate rotate = new Rotate();
            writer.setPageEvent(rotate);

            /*
            String text = "죄송합니다. 아직 코스가 지정되지 않았습니다. 자세한 내용은 교육 관리자 또는 관리자에게 문의하십시오.";
            PdfPTable HeadingReport = new PdfPTable(1);
            HeadingReport.setWidthPercentage(100);
            HeadingReport.addCell(getTbaleCell(text, Element.ALIGN_CENTER, new BaseColor(146, 208, 80), BaseColor.WHITE));
            document.add(HeadingReport);
            */
            /*
            PdfPTable StudentNametable = new PdfPTable(1);
            StudentNametable.setWidthPercentage(100);
            String Studentname = "죄송합니다. 아직 코스가 지정되지 않았습니다. 자세한 내용은 교육 관리자 또는 관리자에게 문의하십시오.";
            String Spanish = "Oops!\\n No te han asignado cursos.\\n Póngase en contacto con su administrador de entrenamiento o administrador para obtener más información";
            String romanian = "Hopa! Cursurile nu ți-au fost încă atribuite. Contactați managerul de formare sau administratorul pentru mai multe informații";
            StudentNametable.addCell(getCell(Studentname, Element.ALIGN_CENTER));
            StudentNametable.addCell(getCell(Spanish, Element.ALIGN_CENTER));
            StudentNametable.addCell(getCell(romanian, Element.ALIGN_CENTER));
            document.add(StudentNametable);
            document.add(new Phrase("\n"));*/

            //For Tacobell alone no Slno and countr code included
            if(PreferenceUtils.getSubdomainName(mContext).contains("tacobell")){
                PdfPTable ContentPaymentTable = new PdfPTable(11);
                ContentPaymentTable.setWidthPercentage(100);
                //ContentPaymentTable.addCell(getTbaleCell("S.No", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell("FirstName", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell("LastName", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell("User", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell("Region / \n Store", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell("Country", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell("Curriculam", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell(this.mModuleName, Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell("Job Role", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell("Status", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell("Assigned \nDate\n ", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell("Completed \nDate \n ", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.setHeaderRows(1);

                for (int i=0; i<myCourseReportModels.size(); i++){

                    MyCourseReportModel myCourseReportModel = myCourseReportModels.get(i);

                    if (i%2 == 0){

                        //ContentPaymentTable.addCell(getTbaleCell((i+1)+"\n",Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getFirstName(),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getLastName(),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getUser_Name(),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getRegion_Name(),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        if(myCourseReportModel.getCountry().isEmpty()){
                            ContentPaymentTable.addCell(getTbaleCell("NA",Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        }else{
                            ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getCountry(),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        }
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getCategory_Name(),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getModule_Name(),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getUser_Type_Name(),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getStatusCell(myCourseReportModel.getModule_Status_Text()+"\n",Element.ALIGN_CENTER,null,BaseColor.WHITE,mCourseStatus));
                        ContentPaymentTable.addCell(getTbaleCell(getDisplayFormatReport(myCourseReportModel.getPublish_Date(),"dd-MM-yyyy hh:mm:ss a"),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(getDisplayFormatReport(myCourseReportModel.getLast_Access_Date(),"dd-MM-yyyy hh:mm:ss a"),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));

                    }else {

                        //ContentPaymentTable.addCell(getTbaleCell((i+1)+"\n",Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getFirstName(),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getLastName(),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getUser_Name(),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getRegion_Name(),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        if(myCourseReportModel.getCountry().isEmpty()){
                            ContentPaymentTable.addCell(getTbaleCell("NA",Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        }else{
                            ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getCountry(),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        }
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getCategory_Name(),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getModule_Name(),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getUser_Type_Name(),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getStatusCell(myCourseReportModel.getModule_Status_Text()+"\n",Element.ALIGN_CENTER,null,BaseColor.WHITE,mCourseStatus));
                        ContentPaymentTable.addCell(getTbaleCell(getDisplayFormatReport(myCourseReportModel.getPublish_Date(),"dd-MM-yyyy hh:mm:ss a"),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(getDisplayFormatReport(myCourseReportModel.getLast_Access_Date(),"dd-MM-yyyy hh:mm:ss a"),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));

                    }

                }

                document.add(ContentPaymentTable);

                document.add(new Phrase("\n"));

            } else {
                PdfPTable ContentPaymentTable = new PdfPTable(10);
                ContentPaymentTable.setWidthPercentage(100);
                //ContentPaymentTable.addCell(getTbaleCell("S.No", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell("FirstName", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell("LastName", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell("User", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell("Region", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell("Curriculam", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell(this.mModuleName, Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell("Job Role", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell("Status", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell("Assigned \nDate\n ", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.addCell(getTbaleCell("Completed \nDate \n ", Element.ALIGN_CENTER, new BaseColor(94, 135, 176), BaseColor.WHITE));
                ContentPaymentTable.setHeaderRows(1);

                for (int i=0; i<myCourseReportModels.size(); i++){

                    MyCourseReportModel myCourseReportModel = myCourseReportModels.get(i);

                    if (i%2 == 0){

                        //ContentPaymentTable.addCell(getTbaleCell((i+1)+"\n",Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getFirstName(),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getLastName(),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getUser_Name(),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getRegion_Name(),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getCategory_Name(),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getModule_Name(),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getUser_Type_Name(),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getStatusCell(myCourseReportModel.getModule_Status_Text()+"\n",Element.ALIGN_CENTER,null,BaseColor.WHITE,mCourseStatus));
                        ContentPaymentTable.addCell(getTbaleCell(getDisplayFormatReport(myCourseReportModel.getPublish_Date(),"dd-MM-yyyy hh:mm:ss a"),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(getDisplayFormatReport(myCourseReportModel.getLast_Access_Date(),"dd-MM-yyyy hh:mm:ss a"),Element.ALIGN_CENTER,new BaseColor(246, 246, 246),new BaseColor(36, 64, 97)));

                    }else {

                        //ContentPaymentTable.addCell(getTbaleCell((i+1)+"\n",Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getFirstName(),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getLastName(),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getUser_Name(),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getRegion_Name(),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getCategory_Name(),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getModule_Name(),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(myCourseReportModel.getUser_Type_Name(),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getStatusCell(myCourseReportModel.getModule_Status_Text()+"\n",Element.ALIGN_CENTER,null,BaseColor.WHITE,mCourseStatus));
                        ContentPaymentTable.addCell(getTbaleCell(getDisplayFormatReport(myCourseReportModel.getPublish_Date(),"dd-MM-yyyy hh:mm:ss a"),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));
                        ContentPaymentTable.addCell(getTbaleCell(getDisplayFormatReport(myCourseReportModel.getLast_Access_Date(),"dd-MM-yyyy hh:mm:ss a"),Element.ALIGN_CENTER,BaseColor.WHITE,new BaseColor(36, 64, 97)));

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

           // startActivity(new Intent(MainActivity.this, PDFViewerActivity.class));

            return FILE.getPath();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


    private String getAttendDate(Date attendanceDate) {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(attendanceDate);
        SimpleDateFormat df =  new SimpleDateFormat("EEEE, d");
        String attendancedate = df.format(attendanceDate);
        attendancedate = attendancedate+ordinal_suffix_of(calendar1.getTime().getDate());
        return  attendancedate;
    }



    public static String getDisplayFormatReport(String dateStr, String givenFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(givenFormat);
        Date date;
        try {
            date = formatter.parse(dateStr);
            SimpleDateFormat convertFormat = new SimpleDateFormat("dd-MMM-yyyy");
            return convertFormat.format(date);
        } catch (Throwable t) {
            return dateStr;
        }
    }

    public String ordinal_suffix_of(int  i) {
        int  j = i % 10,
                k = i % 100;
        if (j == 1 && k != 11) {
            return "st";
        }
        if (j == 2 && k != 12) {
            return "nd";
        }
        if (j == 3 && k != 13) {
            return  "rd";
        }
        return "th";
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


    public PdfPCell getStatusCell(String text, int alignment, BaseColor basebackground, BaseColor basefontcolor,String courseStatus) {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text);
        p.setFont(new Font(Book_Antiqua, 15, Font.BOLD, basefontcolor));
        p.setAlignment(alignment);
        cell.addElement(p);
        cell.setVerticalAlignment(alignment);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(3f);
        switch (courseStatus){

            case "tot":
                cell.setBackgroundColor(new BaseColor(mContext.getResources().getColor(R.color.loginbutton)));
                break;
            case "com":
                cell.setBackgroundColor(new BaseColor(mContext.getResources().getColor(R.color.buttoncolor)));

                break;
            case "in":
                cell.setBackgroundColor(new BaseColor(mContext.getResources().getColor(R.color.pieorange)));
                break;
            case "over":
                cell.setBackgroundColor(new BaseColor(mContext.getResources().getColor(R.color.piebrown)));

                break;
            case "ass":
                cell.setBackgroundColor(new BaseColor(mContext.getResources().getColor(R.color.amber)));

                break;
            case "yet":
                cell.setBackgroundColor(new BaseColor(mContext.getResources().getColor(R.color.amber)));

                break;
            default:
                cell.setBackgroundColor(new BaseColor(mContext.getResources().getColor(R.color.pieorange)));
                break;
        }
        return cell;
    }


    public PdfPCell getTbaleCell(String text, int alignment, BaseColor basebackground, BaseColor basefontcolor) {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text);
        p.setFont(new Font(Book_Antiqua, 15, Font.BOLD, basefontcolor));
        p.setAlignment(alignment);
        cell.addElement(p);
        cell.setVerticalAlignment(alignment);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(1f);
        cell.setBorderWidth(2);
        cell.setBackgroundColor(basebackground);
        cell.setBorderColor(BaseColor.WHITE);
        return cell;
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
