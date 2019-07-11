package com.swaas.kangle;

import android.content.Context;

import com.google.gson.Gson;
import com.swaas.kangle.models.ThemeModel;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sayellessh on 03-12-2018.
 */

public class ThemeClass {

    Context mContext;

    GetThemeDataCBListner getThemeDataCBListner;


    public interface GetThemeDataCBListner {
        void GetThemeDataSuccessCB(boolean Success);
        void GetThemeDataFailureCB(boolean message);
    }

    public void setGetThemeDataCBListner(GetThemeDataCBListner getNotificationDataCBListners) {
        this.getThemeDataCBListner = getNotificationDataCBListners;
    }

    public ThemeClass(Context context){
        mContext = context;
    }

    public void setTheme(){
        boolean b = false;
        CompanyThemeModel c =new CompanyThemeModel();
        ArrayList<ThemeModel> themeModel = new ArrayList<>();
        if(PreferenceUtils.getTheme("key",mContext)!=null) {
            themeModel= (ArrayList<ThemeModel>) PreferenceUtils.getTheme("key",mContext);
            for(int i =0;i<themeModel.size();i++)
            {
                if(themeModel.get(i).getVariableValue().length()<7)
                {
                    b = true;
                }
            }
            if(b) {
                c.Companycolor = "#1a4e73";
                c.Opaquecolor = "#cdcdcd";
                c.HeaderBarcolor = "#000000";
                c.Cardbackgroundcolor = "#3e7aa5";
                c.Completedcolor = "#26e03e";
                c.Inprogresscolor = "#078dd0";
                c.Expiedcolor = "#da1414";
                c.PendingApproval = "#fd91a6";
                c.YetTostart = "#ffec08";
                c.Textcolor = "#ffffff";
                c.Iconcolor = "#32333A";
                c.Partiallycompletedcolor = "#FF0000FF";
                c.Greycolor = "#8f8f8f";
                c.TopBarIconColor = "#ffffff";
            }
            else {
                if (themeModel != null) {
                    for (int i = 0; i < themeModel.size(); i++) {
                        if (themeModel.get(i).getVariableName().toLowerCase().equalsIgnoreCase("base")) {
                            c.Companycolor = themeModel.get(i).getVariableValue();
                        } else if (themeModel.get(i).getVariableName().toLowerCase().equalsIgnoreCase("header-bg")) {
                            c.HeaderBarcolor = themeModel.get(i).getVariableValue();
                        } else if (themeModel.get(i).getVariableName().toLowerCase().equalsIgnoreCase("box-container-bg")) {
                            c.Cardbackgroundcolor = themeModel.get(i).getVariableValue();
                        } else if (themeModel.get(i).getVariableName().toLowerCase().equalsIgnoreCase("theme-fcolor")) {
                            c.Textcolor = themeModel.get(i).getVariableValue();
                        } else if (themeModel.get(i).getVariableName().toLowerCase().equalsIgnoreCase("img-bg-container")) {
                            c.Iconcolor = themeModel.get(i).getVariableValue();
                        } else if (themeModel.get(i).getVariableName().toLowerCase().equalsIgnoreCase("icon-color")) {
                            c.TopBarIconColor = themeModel.get(i).getVariableValue();
                        }
                    }
                    //  c.Companycolor = theme.get(0);
                    // c.HeaderBarcolor = theme.get(1);
                    // c.Cardbackgroundcolor = theme.get(2);
                    // c.Textcolor = theme.get(3);
                    // c.Iconcolor = theme.get(4);
                    //c.TopBarIconColor = theme.get(5);
                    c.Opaquecolor = "#cdcdcd";
                    c.Completedcolor = "#26e03e";
                    c.Inprogresscolor = "#078dd0";
                    c.Expiedcolor = "#da1414";
                    c.PendingApproval = "#fd91a6";
                    c.YetTostart = "#ffec08";
                    c.Partiallycompletedcolor = "#FF0000FF";
                    c.Greycolor = "#8f8f8f";
                }
            }
        }

       /* if(PreferenceUtils.getSubdomainName(mContext).contains("tacobell")) {
            c.Companycolor = "#702082";
            c.Opaquecolor = "#cdcdcd";
            c.HeaderBarcolor = "#303030";
            c.Cardbackgroundcolor = "#9C7EB4";
            c.Completedcolor = "#26e03e";
            c.Inprogresscolor = "#078dd0";
            c.Expiedcolor = "#da1414";
            c.PendingApproval = "#fd91a6";
            c.YetTostart = "#ffec08";
            c.Textcolor = "#ffffff";
            c.Iconcolor = "#32333A";
            c.Partiallycompletedcolor = "#FF0000FF";
            c.Greycolor = "#8f8f8f";
            c.TopBarIconColor = "#ffffff";

        } else if(PreferenceUtils.getSubdomainName(mContext).contains("swaasmarket")  || PreferenceUtils.getSubdomainName(mContext).toLowerCase().contains("sdemo")) {
            c.Companycolor = "#ac423d";
            c.Opaquecolor = "#cdcdcd";
            c.HeaderBarcolor = "#F8F7F3";
            c.Cardbackgroundcolor = "#e17671";
            c.Completedcolor = "#26e03e";
            c.Inprogresscolor = "#078dd0";
            c.Expiedcolor = "#da1414";
            c.PendingApproval = "#fd91a6";
            c.YetTostart = "#ffec08";
            c.Textcolor = "#ffffff";
            c.Iconcolor = "#32333A";
            c.Partiallycompletedcolor = "#FF0000FF";
            c.Greycolor = "#8f8f8f";
            c.TopBarIconColor = "#ac423d";

        }  else if(PreferenceUtils.getSubdomainName(mContext).contains("shakey")) {
            c.Companycolor = "#1b1917";
            c.Opaquecolor = "#cdcdcd";
            c.HeaderBarcolor = "#99000000";
            c.Cardbackgroundcolor = "#000000";
            c.Completedcolor = "#26e03e";
            c.Inprogresscolor = "#078dd0";
            c.Expiedcolor = "#da1414";
            c.PendingApproval = "#fd91a6";
            c.YetTostart = "#ffec08";
            c.Textcolor = "#ffffff";
            c.Iconcolor = "#d51820";
            c.Partiallycompletedcolor = "#FF0000FF";
            c.Greycolor = "#8f8f8f";
            c.TopBarIconColor = "#ffffff";

        }
*/


        else {
            c.Companycolor = "#1a4e73";
            c.Opaquecolor = "#cdcdcd";
            c.HeaderBarcolor = "#000000";
            c.Cardbackgroundcolor = "#3e7aa5";
            c.Completedcolor = "#26e03e";
            c.Inprogresscolor = "#078dd0";
            c.Expiedcolor = "#da1414";
            c.PendingApproval = "#fd91a6";
            c.YetTostart = "#ffec08";
            c.Textcolor = "#ffffff";
            c.Iconcolor = "#32333A";
            c.Partiallycompletedcolor = "#FF0000FF";
            c.Greycolor = "#8f8f8f";
            c.TopBarIconColor = "#ffffff";

        }

        Gson gson = new Gson();
        String thememodel = gson.toJson(c);
        PreferenceUtils.setCompanyThemeModel(mContext,thememodel);

        Constants.COMPANY_COLOR =c.Companycolor;
        Constants.OPAQUE_COLOR =c.Opaquecolor;
        Constants.HEADERBAR_COLOR =c.HeaderBarcolor;
        Constants.CARDBACKGROUND_COLOR =c.Cardbackgroundcolor;
        Constants.COMPLETED_COLOR =c.Completedcolor;
        Constants.INPROGRESS_COLOR =c.Inprogresscolor;
        Constants.EXPIRED_COLOR =c.Expiedcolor;
        Constants.PENDING_APPROVAl_COLOR = c.PendingApproval;
        Constants.YET_TO_START_COLOR = c.YetTostart;
        Constants.TEXT_COLOR =c.Textcolor;
        Constants.ICON_COLOR =c.Iconcolor;
        Constants.PARTIALLY_COMPLETED_COLOR =c.Partiallycompletedcolor;
        Constants.GREY_COLOR =c.Greycolor;
        Constants.TOPBARICON_COLOR =c.TopBarIconColor;

        getThemeDataCBListner.GetThemeDataSuccessCB(true);
    }

}


