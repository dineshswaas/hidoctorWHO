package com.swaas.kangle.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.swaas.kangle.R;

/**
 * Created by Sayellessh on 10-05-2017.
 */

public class MessageDialog {

    Dialog logoutdialog,menudialog,checklistconfirm;
    //Dialog menudialog;
    Dialog helpdialog,helppopupdialog,updatepopupdialog;
    Dialog photodialog,attachmentdialog;
    Context context;

    /*Approve and Reject*/
    CustomFontTextView logout,change_password;
    CustomFontTextView my_profile,go_offline;
    TextView sendmail,canceldialog;
    TextView logoutOk,cancellogout,buttonOK;
    TextView closehelp;
    WebView helpview;
    View logoutlayout,emaillayout,headingsection,headingupdate,checklistheaderpoplayout,headingview;
    Dialog prerequsite,custommessageDialog;
    TextView message,messages,okpre,cancel,heading,msg;
    CustomFontTextView takephoto,choosefromgallery,document,takevideo;
    ImageView attachedimage;
    ImageView close,delete,popupclosebtn;
    TextView Oksubmit,cancelsubmit,confirmmessageText;

    Dialog ConfirmDownloaddialog;
    View ConfirmDownloadlayout;
    TextView downloadOk,cancelldw,filename,filesize;


    public MessageDialog(Context context) {
        this.context = context;
        //initDialog();
        initlogoutDialog();
        showEmailDialog();
        logOutDialog();
        showhelppopupDialog();
        UpdatePopUp();
        prerequsite();
        PhotoDialogView();
        AttachmentPopUpDialog();
        CustommessageDialog();
        ChecklistConfirmationPopup();
        ConfirmDownloadPopup();
    }

    private void initlogoutDialog() {
        menudialog = new Dialog(context);
        menudialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        menudialog.setContentView(R.layout.dialog_logout);
        logout = (CustomFontTextView) menudialog.findViewById(R.id.logout);
        change_password = (CustomFontTextView) menudialog.findViewById(R.id.change_password);

    }

    private void initDialog() {
        menudialog = new Dialog(context);
        menudialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        menudialog.setContentView(R.layout.dialog_asset_menus);
        my_profile = (CustomFontTextView) menudialog.findViewById(R.id.my_profile);
        go_offline = (CustomFontTextView) menudialog.findViewById(R.id.go_offline);
    }

    private void showEmailDialog(){
        helpdialog = new Dialog(context);
        helpdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        helpdialog.setContentView(R.layout.dialog_asset_email);
        emaillayout = helpdialog.findViewById(R.id.emaillayout);
        sendmail = (TextView) helpdialog.findViewById(R.id.sendmail);
        canceldialog = (TextView) helpdialog.findViewById(R.id.canceldialog);
    }

    private void logOutDialog(){
        logoutdialog = new Dialog(context);
        logoutdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        logoutdialog.setContentView(R.layout.dialog_user_logout);
        logoutlayout = logoutdialog.findViewById(R.id.logoutlayout);
        logoutOk = (TextView) logoutdialog.findViewById(R.id.logoutOk);
        cancellogout = (TextView) logoutdialog.findViewById(R.id.cancellogout);
    }

    private void showhelppopupDialog(){
        helppopupdialog = new Dialog(context);
        helppopupdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        helppopupdialog.setContentView(R.layout.dialog_help_layout);
        closehelp = (TextView) helppopupdialog.findViewById(R.id.closehelp);
        helpview = (WebView) helppopupdialog.findViewById(R.id.helpview);
    }

    private void UpdatePopUp(){
        updatepopupdialog = new Dialog(context);
        updatepopupdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        updatepopupdialog.setContentView(R.layout.dialog_app_new_version);
        headingupdate = updatepopupdialog.findViewById(R.id.headingupdate);
        buttonOK = (TextView) updatepopupdialog.findViewById(R.id.buttonOk);
    }

    private void prerequsite() {
        prerequsite = new Dialog(context);
        prerequsite.requestWindowFeature(Window.FEATURE_NO_TITLE);
        prerequsite.setContentView(R.layout.dialog_prerequsite_logout);
        headingview = prerequsite.findViewById(R.id.heading);
        msg = (TextView) prerequsite.findViewById(R.id.msg);
        okpre = (TextView) prerequsite.findViewById(R.id.Okpre);
        cancel = (TextView) prerequsite.findViewById(R.id.cancelpre);
    }

    private void PhotoDialogView(){
        photodialog = new Dialog(context);
        photodialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        photodialog.setContentView(R.layout.dialog_choose_photo);
        takephoto = (CustomFontTextView) photodialog.findViewById(R.id.takephoto);
        choosefromgallery = (CustomFontTextView) photodialog.findViewById(R.id.fromgallery);
        document = (CustomFontTextView) photodialog.findViewById(R.id.document);
        takevideo = (CustomFontTextView) photodialog.findViewById(R.id.takevideo);

    }

    private void AttachmentPopUpDialog(){
        attachmentdialog = new Dialog(context);
        attachmentdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        attachmentdialog.setContentView(R.layout.dialog_attached_photo);
        attachedimage = (ImageView) attachmentdialog.findViewById(R.id.attachement_image);
        close = (ImageView) attachmentdialog.findViewById(R.id.close);
        delete = (ImageView) attachmentdialog.findViewById(R.id.delete);
    }

    private void CustommessageDialog() {
        custommessageDialog = new Dialog(context);
        custommessageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custommessageDialog.setContentView(R.layout.dialog_message_custom);
        headingsection = custommessageDialog.findViewById(R.id.headingsection);
        message = (TextView) custommessageDialog.findViewById(R.id.message);
        popupclosebtn = (ImageView) custommessageDialog.findViewById(R.id.popupclosebtn);
        heading = (TextView) custommessageDialog.findViewById(R.id.heading);
    }

    private void ChecklistConfirmationPopup(){
        checklistconfirm = new Dialog(context);
        checklistconfirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        checklistconfirm.setContentView(R.layout.dialog_checklistconfirm_layout);
        checklistheaderpoplayout = checklistconfirm.findViewById(R.id.checklistheaderpoplayout);
        Oksubmit = (TextView) checklistconfirm.findViewById(R.id.Oksubmit);
        cancelsubmit = (TextView) checklistconfirm.findViewById(R.id.cancelsubmit);
        confirmmessageText = (TextView) checklistconfirm.findViewById(R.id.confirmmessageText);
    }

    private void ConfirmDownloadPopup(){
        ConfirmDownloaddialog = new Dialog(context);
        ConfirmDownloaddialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ConfirmDownloaddialog.setContentView(R.layout.dialog_confirm_download_popup);
        ConfirmDownloadlayout = ConfirmDownloaddialog.findViewById(R.id.downloadlayout);
        downloadOk = (TextView) ConfirmDownloaddialog.findViewById(R.id.downloadOk);
        cancelldw = (TextView) ConfirmDownloaddialog.findViewById(R.id.cancelldw);
        filename = (TextView) ConfirmDownloaddialog.findViewById(R.id.filename);
        filesize = (TextView) ConfirmDownloaddialog.findViewById(R.id.filesize);
    }


    public void ShowCourseExtend(final Context context,final String messg, View.OnClickListener btOne,
                                 View.OnClickListener btTwo,
                                 boolean isCancelable){

        prerequsite.setCancelable(isCancelable);

        headingview.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        okpre.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        cancel.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));

        msg.setText(messg);

        if(btOne != null){
            okpre.setOnClickListener(btOne);
        }

        if(btTwo != null){
            cancel.setOnClickListener(btTwo);
        }

        if (!((Activity) context).isFinishing()) {
            prerequsite.show();
        }
    }

    public void showConfirmDownloadPopup(final Context context,String fn,String fls,
                                         View.OnClickListener btOne,View.OnClickListener btTwo,
                                         boolean isCancelable){

        ConfirmDownloaddialog.setCancelable(isCancelable);

        ConfirmDownloadlayout.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        downloadOk.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        cancelldw.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));

        filename.setText(fn.toString());
        filesize.setText(fls.toString());

        if(btOne != null){
            downloadOk.setOnClickListener(btOne);
        }
        if(btTwo != null){
            cancelldw.setOnClickListener(btTwo);
        }
        if (!((Activity) context).isFinishing()) {
            ConfirmDownloaddialog.show();
        }

    }



    public void showCustomAlertMessageDialog(final Context context,final String headingstring,final String string1,
                                             View.OnClickListener btOne, boolean isCancelable){

        custommessageDialog.setCancelable(isCancelable);
        headingsection.setBackgroundColor(context.getResources().getColor(R.color.black));

        heading.setText(headingstring);
        message.setText(string1);

        custommessageDialog.show();

        if(btOne!= null){
            popupclosebtn.setOnClickListener(btOne);
        }

        if (!((Activity) context).isFinishing()) {
            custommessageDialog.show();
        }
    }



    public void showAlertDialog(final Context context,boolean showbtn2,boolean isoffline,
                                          View.OnClickListener btOne,View.OnClickListener btTwo,
                                          boolean isCancelable) {
        if(isoffline){
            go_offline.setText(this.context.getResources().getString(R.string.goOnline));
        } else{
            go_offline.setText(this.context.getResources().getString(R.string.gooffline));
        }

        if(!showbtn2){
            my_profile.setVisibility(View.GONE);
        }
        menudialog.setCancelable(isCancelable);
        if(btOne != null) {
            go_offline.setOnClickListener(btOne);
        }
        if(btTwo != null){
            my_profile.setOnClickListener(btTwo);
        }
        /*if(negativeListener != null)
            laterButton.setOnClickListener(negativeListener);*/

        if (!((Activity) context).isFinishing()) {
            menudialog.show();
        }
    }

    public void showMenuDialog(final Context context,boolean showbtn2,
                                View.OnClickListener btOne,View.OnClickListener btTwo,
                                boolean isCancelable) {
        menudialog.setCancelable(isCancelable);

        if(!showbtn2){
            change_password.setVisibility(View.GONE);
        }
        if(btOne != null) {
            logout.setOnClickListener(btOne);
        }
        if(btTwo != null){
            change_password.setOnClickListener(btTwo);
        }

        if (!((Activity) context).isFinishing()) {
            menudialog.show();
        }
    }

    public void showEmailPop(final Context context,View.OnClickListener btOne,View.OnClickListener btTwo,
                                boolean isCancelable) {
        helpdialog.setCancelable(isCancelable);

        emaillayout.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        sendmail.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        canceldialog.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));

        if(btOne != null){
            sendmail.setOnClickListener(btOne);
        }
        if(btTwo != null){
            canceldialog.setOnClickListener(btTwo);
        }
        if (!((Activity) context).isFinishing()) {
            helpdialog.show();
        }
    }

    public void ShowLogout(final Context context,View.OnClickListener btOne,View.OnClickListener btTwo,
                           boolean isCancelable){

        logoutdialog.setCancelable(isCancelable);
        logoutlayout.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        logoutOk.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        cancellogout.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));

        if(btOne != null){
            logoutOk.setOnClickListener(btOne);
        }
        if(btTwo != null){
            cancellogout.setOnClickListener(btTwo);
        }
        if (!((Activity) context).isFinishing()) {
            logoutdialog.show();
        }
    }


    public void Showhelppopup(final Context context,View.OnClickListener btOne,String url,
                           boolean isCancelable){

        helppopupdialog.setCancelable(isCancelable);

        if(btOne != null){
            closehelp.setOnClickListener(btOne);
        }

        helpview.getSettings().setDomStorageEnabled(true);
        helpview.getSettings().setJavaScriptEnabled(true);
        helpview.getSettings().setLoadWithOverviewMode(true);
        helpview.getSettings().setUseWideViewPort(true);
        helpview.getSettings().setBuiltInZoomControls(true);
        helpview.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
            }
        });
        helpview.loadUrl(url);

        if (!((Activity) context).isFinishing()) {
            helppopupdialog.show();
        }
    }


    public void showUpdatePopUp(final Context context,View.OnClickListener btOne,
                                boolean isCancelable){
        updatepopupdialog.setCancelable(isCancelable);

        headingupdate.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        buttonOK.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));

        if(btOne != null){
            buttonOK.setOnClickListener(btOne);
        }

        if (!((Activity) context).isFinishing()) {
            updatepopupdialog.show();
        }
    }


    //photoDialog
    public void showPhotoDialog(final Context context,
                               View.OnClickListener btOne,View.OnClickListener btTwo,
                                View.OnClickListener btThree,View.OnClickListener btFour,
                               boolean isCancelable,boolean one,boolean two,boolean three,boolean four) {
        photodialog.setCancelable(isCancelable);
        if(!one){
            takephoto.setVisibility(View.GONE);
        }
        if(!two){
            choosefromgallery.setVisibility(View.GONE);
        }
        if(!three){
            document.setVisibility(View.GONE);
        }
        if(!four){
            takevideo.setVisibility(View.GONE);
        }

        if(btOne != null) {
            takephoto.setOnClickListener(btOne);
        }
        if(btTwo != null){
            choosefromgallery.setOnClickListener(btTwo);
        }
        if(btThree != null){
            document.setOnClickListener(btThree);
        }
        if(btFour != null){
            takevideo.setOnClickListener(btFour);
        }
        if (!((Activity) context).isFinishing()) {
            photodialog.show();
        }
    }

    public void showAttachmentpopupDialog(final Context context,String attachmentURL,
                                          View.OnClickListener btOne,View.OnClickListener btTwo,
                                          boolean isCancelable) {
        Ion.with(context).load(attachmentURL).intoImageView(attachedimage);

        if(btOne != null) {
            close.setOnClickListener(btOne);
        }
        if(btTwo != null){
            delete.setOnClickListener(btTwo);
        }

        if (!((Activity) context).isFinishing()) {
            attachmentdialog.show();
        }

    }


    public void showChecklistConfirmationPopup(final Context context,String Message,View.OnClickListener btOne,View.OnClickListener btTwo,
                                               boolean isCancelable){

        checklistconfirm.setCancelable(isCancelable);

        checklistheaderpoplayout.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        Oksubmit.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        cancelsubmit.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));

        confirmmessageText.setText(Message.toString());

        if(btOne != null){
            Oksubmit.setOnClickListener(btOne);
        }
        if(btTwo != null){
            cancelsubmit.setOnClickListener(btTwo);
        }
        if (!((Activity) context).isFinishing()) {
            checklistconfirm.show();
        }
    }

    public void dialogDismiss(){
        if(menudialog != null){
            menudialog.dismiss();
        }

        if(logoutdialog !=null){
            logoutdialog.dismiss();
        }

        if(menudialog != null){
            menudialog.dismiss();
        }

        if(helpdialog != null){
            helpdialog.dismiss();
        }

        if(helppopupdialog != null){
            helppopupdialog.dismiss();
        }

        if(updatepopupdialog != null){
            updatepopupdialog.dismiss();
        }

        if(prerequsite != null){
            prerequsite.dismiss();
        }

        if(photodialog != null){
            photodialog.dismiss();
        }

        if(attachmentdialog != null){
            attachmentdialog.dismiss();
        }
        if(checklistconfirm != null){
            checklistconfirm.dismiss();
        }
        if(ConfirmDownloaddialog != null){
            ConfirmDownloaddialog.dismiss();
        }
        if(custommessageDialog != null){
            custommessageDialog.dismiss();
        }

    }
}
