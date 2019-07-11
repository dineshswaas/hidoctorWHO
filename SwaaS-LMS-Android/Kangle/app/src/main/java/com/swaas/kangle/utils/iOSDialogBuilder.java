package com.swaas.kangle.utils;

import android.content.Context;
import android.graphics.Typeface;

public class iOSDialogBuilder {
    private Typeface tf;
    private boolean bold,cancelable,singleView;
    private String title, subtitle, okLabel, koLabel,singleButtonText;
    private Context context;
    private iOSDialogClickListener positiveListener;
    private iOSDialogClickListener negativeListener;
    private iOSDialogClickListener singlePositiveListener;

    public iOSDialogBuilder(Context context) {
        this.context = context;
    }

    public iOSDialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public iOSDialogBuilder setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public iOSDialogBuilder setBoldPositiveLabel(boolean bold) {
        this.bold = bold;
        return this;
    }

    public iOSDialogBuilder setSingleText(String  singleButtonText) {
        this.singleButtonText = singleButtonText;
        return this;
    }

    public iOSDialogBuilder setSingleButtonView(boolean view) {
        this.singleView = view;
        return this;
    }

    public iOSDialogBuilder setFont(Typeface font) {
        this.tf=font;
        return this;
    }
    public iOSDialogBuilder setCancelable(boolean cancelable){
        this.cancelable=cancelable;
        return this;
    }

    public iOSDialogBuilder setNegativeListener(String koLabel, iOSDialogClickListener listener) {
        this.negativeListener=listener;
        this.koLabel=koLabel;
        return this;
    }

    public iOSDialogBuilder setPositiveListener(String okLabel, iOSDialogClickListener listener) {
        this.positiveListener = listener;
        this.okLabel=okLabel;
        return this;
    }

    public iOSDialogBuilder setSinglePositiveListener(String okLabel, iOSDialogClickListener listener) {
        this.singlePositiveListener = listener;
        this.singleButtonText=okLabel;
        return this;
    }

    public iOSDialog build(){
        iOSDialog dialog = new iOSDialog(context,title,subtitle, bold, tf,cancelable,singleView);
        dialog.setNegative(koLabel,negativeListener);
        dialog.setPositive(okLabel,positiveListener);
        dialog.setSinglePositive(singleButtonText,singlePositiveListener);
        return dialog;
    }

}
