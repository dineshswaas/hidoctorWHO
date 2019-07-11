package com.swaas.kangle.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swaas.kangle.LPCourse.model.QuestionAndAnswerModel;
import com.swaas.kangle.models.ThemeModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sayellessh on 05-04-2017.
 */

public class PreferenceUtils {
    private static final String KANGLE = "KANGLE";
    private static final String COMPANY_ID="companyId";
    private static final String SUBDOMAIN_NAME="companyURL";
    private static final String USER_ID="userId";
    private static final String USER="user";
    private static final String COMPANY_CODE="companyCode";
    private static final String LANGUAGE = "language";
    private static final String LANDINPAGEACCESS = "LandingPageAccess";
    private static final String OFFLINE = "offline";
    private static final String STARTTIME = "starttime";
    private static final String ENDTIME = "endtime";
    private static final String CLIENTLOGO = "clientLogo";
    private static final String USERAGENT = "userAgent";
    private static final String IS_GESTURE_ENABLED = "is_gesture_enabled";
    private static final String IS_PLAYER_GUIDE_COMPLETED = "is_player_guide_completed";
    private static final String IS_AUDIO_GUIDECOMPLETED ="is_audio_guide_completed";
    private static final String IS_Image_GUIDECOMPLETED = "is_image_guide_completed";
    private static final String IS_VIDEO_GUIDE_COMPLETED = "is_video_guide_completed";
    private static final String IS_PDF_GUIDE_COMPLETED = "is_pdf_guide_completed";
    private static final String COURSE_ID="course_id";
    private static final String SECTION_ID="section_id";
    private static final String Course_User_Assignment_Id="Course_User_Assignment_Id";
    private static final String Couse_User_Section_Mapping_Id="Couse_User_Section_Mapping_Id";
    private static final String CUSTOMER_DETAILED_ID = "Customer_Detailed_Id";
    private static final String NWEAvisible = "NWEAvisible";
    private static final String visibleActivity = "visibleActivity";
    private static final String COMPANYTHEMEMODEL = "CompanyThemeModel";

    private static final String TASK_HASCHILDUSERS = "Task_Haschildusers";
    private static final String TASK_ALLOW_COMPLETE = "TASK_ALLOWCOMPLETE";
    private static final String THEMELIST = "themelist";
    private static final Object ThemeModel = "ThemeModel";
    private static final String USERNAME = "username";
    private static final String IS_FORCE_UPDATE_AVAILABLE = "force_update_available";
    private static final String IS_FORCE_UPDATE_VERSION = "force_update_version";
    private static final String QUESTION_ANSWER_LIST = "question_answer_list";
    private static final String Timer = "timer";



    public static boolean getNWEAvisible(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE,Context.MODE_PRIVATE);
        boolean mCourseId = sharedPreferences.getBoolean(NWEAvisible,false);
        return mCourseId;
    }
    public static void setNWEAvisible(Context context,boolean visible){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(NWEAvisible,visible);
        editor.commit();
    }

    public static String getVisibleActivityName(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        String  subdomainname = sharedPreferences.getString(visibleActivity, null);
        return subdomainname;
    }

    public static void setVisibleActivityName(Context context, String activity)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(visibleActivity, activity);
        editor.commit();
    }

    public static int getSectionId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE,Context.MODE_PRIVATE);
        int mSectionId = sharedPreferences.getInt(SECTION_ID,0);
        return mSectionId;
    }
    public static void setSectionId(Context context,int SectionId){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SECTION_ID,SectionId);
        editor.commit();
    }

    public static int getUserId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        int  userId = sharedPreferences.getInt(USER_ID, 0);
        return userId;
    }


    public static void setIsGestureEnabled(Context context, boolean isFrom) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_GESTURE_ENABLED, isFrom);
        editor.commit();
    }

    public static boolean getIsGestureEnabled(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        boolean isFromCRM = sharedPreferences.getBoolean(IS_GESTURE_ENABLED, false);
        return isFromCRM;
    }



    public static void setIsPlayerGuideCompleted(Context context, boolean playercompleted) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_PLAYER_GUIDE_COMPLETED, playercompleted);
        editor.commit();

    }


    public static boolean getIsPlayerGuideCompleted(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        boolean playerguidesate = sharedPreferences.getBoolean(IS_PLAYER_GUIDE_COMPLETED, false);
        return playerguidesate;

    }


    public static void setIsVideoGuideCompleted(Context context, boolean playercompleted) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_VIDEO_GUIDE_COMPLETED, playercompleted);
        editor.commit();

    }


    public static boolean getIsVideoGuideCompleted(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        boolean videoguide = sharedPreferences.getBoolean(IS_VIDEO_GUIDE_COMPLETED, false);
        return videoguide;

    }


    public static void setIsPdfGuideCompleted(Context context, boolean playercompleted) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_PDF_GUIDE_COMPLETED, playercompleted);
        editor.commit();

    }


    public static boolean getIsPdfGuideCompleted(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        boolean pdfguidestates = sharedPreferences.getBoolean(IS_PDF_GUIDE_COMPLETED, false);
        return pdfguidestates;

    }



    public static void setIsImageGuideCompleted(Context context, boolean playercompleted) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_Image_GUIDECOMPLETED, playercompleted);
        editor.commit();

    }


    public static boolean getIsImageGuideCompleted(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        boolean pdfguidestates = sharedPreferences.getBoolean(IS_Image_GUIDECOMPLETED, false);
        return pdfguidestates;

    }



    public static void setIsAudioGuideCompleted(Context context, boolean playercompleted) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_AUDIO_GUIDECOMPLETED, playercompleted);
        editor.commit();

    }


    public static boolean getIsAudioGuideCompleted(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        boolean pdfguidestates = sharedPreferences.getBoolean(IS_AUDIO_GUIDECOMPLETED, false);
        return pdfguidestates;

    }





    public static void setUserId(Context context, int userId)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(USER_ID, userId);
        editor.commit();
    }

    public static int getCompnayId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        int  companyId = sharedPreferences.getInt(COMPANY_ID, 0);
        return companyId;
    }

    public static void setCompanyId(Context context, int companyId)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(COMPANY_ID, companyId);
        editor.commit();
    }

    public static String getSubdomainName(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        String  subdomainname = sharedPreferences.getString(SUBDOMAIN_NAME, null);
        return subdomainname;
    }

    public static void setSubdomainName(Context context, String subdomainname)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SUBDOMAIN_NAME, subdomainname);
        editor.commit();
    }

    public static String getUser(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        String  userobj = sharedPreferences.getString(USER, null);
        return userobj;
    }

    public static void setUser(Context context, String user)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER, user);
        editor.commit();
    }

    public static String getLandingPageAccess(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        String  landingobj = sharedPreferences.getString(LANDINPAGEACCESS, null);
        return landingobj;
    }

    public static void setLandingPageAccess(Context context, String status)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LANDINPAGEACCESS, status);
        editor.commit();
    }

    public static String getLanguageChoosen(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        String  subdomainname = sharedPreferences.getString(LANGUAGE, "English");
        return subdomainname;
    }

    public static void setLanguageChoosen(Context context, String subdomainname)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LANGUAGE, subdomainname);
        editor.commit();
    }



    public static String getCompanyCode(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        String  subdomainname = sharedPreferences.getString(COMPANY_CODE, "");
        return subdomainname;
    }

    public static void setCompanyCode(Context context, String companyCode)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COMPANY_CODE, companyCode);
        editor.commit();
    }



    public static String getOfflineMode(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        String  subdomainname = sharedPreferences.getString(OFFLINE, "false");
        return subdomainname;
    }

    public static void setOfflineMode(Context context, String offline)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OFFLINE, offline);
        editor.commit();
    }

    public static long getStartTime(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        long  companyId = sharedPreferences.getLong(STARTTIME, Long.parseLong("0"));
        return companyId;
    }

    public static void setStartTime(Context context, Long startime)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(STARTTIME, startime);
        editor.commit();
    }

    public static long getEndTime(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        long  companyId = sharedPreferences.getLong(ENDTIME, Long.parseLong("0"));
        return companyId;
    }

    public static void setEndTime(Context context, Long endtime)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(ENDTIME, endtime);
        editor.commit();
    }

    public static String getClientLogo(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        String  subdomainname = sharedPreferences.getString(CLIENTLOGO, "");
        return subdomainname;
    }

    public static void setClientLogo(Context context, String url)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CLIENTLOGO, url);
        editor.commit();
    }

    public static String getUserAgent(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        String  subdomainname = sharedPreferences.getString(USERAGENT, "");
        return subdomainname;
    }

    public static void setUserAgent(Context context, String useragent)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERAGENT, useragent);
        editor.commit();
    }

    public static int  getCourse_User_Assignment_Id(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        int  ID = sharedPreferences.getInt(Course_User_Assignment_Id, 0);
        return ID;
    }

    public static void  setCourse_User_Assignment_Id(Context context,int value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Course_User_Assignment_Id, value);
        editor.commit();
    }

    public static int  getCouse_User_Section_Mapping_Id(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        int  id = sharedPreferences.getInt(Couse_User_Section_Mapping_Id, 0);
        return id;
    }
    public static void  setCouse_User_Section_Mapping_Id(Context context,int value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Couse_User_Section_Mapping_Id, value);
        editor.commit();
    }


    public static String getCompanyThemeModel(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        String  landingobj = sharedPreferences.getString(COMPANYTHEMEMODEL, null);
        return landingobj;
    }

    public static void setCompanyThemeModel(Context context, String theme)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COMPANYTHEMEMODEL, theme);
        editor.commit();
    }

    public static String getTASK_HASCHILDUSERS(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE,Context.MODE_PRIVATE);
        String has_childusers = sharedPreferences.getString(TASK_HASCHILDUSERS,null);
        return has_childusers;
    }
    public static void setTASK_HASCHILDUSERS(Context context,String has_childusers){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TASK_HASCHILDUSERS,has_childusers);
        editor.commit();
    }

    public static String getTASK_ALLOW_COMPLETE(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE,Context.MODE_PRIVATE);
        String has_childusers = sharedPreferences.getString(TASK_ALLOW_COMPLETE,null);
        return has_childusers;
    }
    public static void setTASK_ALLOW_COMPLETE(Context context,String allowChild){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TASK_ALLOW_COMPLETE,allowChild);
        editor.commit();
    }
    public static  <T> void setQuestionAnswerList(String key, List<T> list,Context context){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        setlist(key, json,context);    // This line is IMPORTANT !!!
    }
    public static void setlist(String key, String value,Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(QUESTION_ANSWER_LIST), Context.MODE_PRIVATE);
        if (sharedPref != null) {
            SharedPreferences.Editor prefsEditor = sharedPref.edit();
            prefsEditor.putString(key, value);
            prefsEditor.commit();
        }
    }
    public static ArrayList<QuestionAndAnswerModel> getquestionanswerlist(Context context, String key){
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(QUESTION_ANSWER_LIST), Context.MODE_PRIVATE);
        if (sharedPref != null) {

            Gson gson = new Gson();
            List<QuestionAndAnswerModel> themeModels;

            String string = sharedPref.getString(key, null);
            Type type = new TypeToken<List<QuestionAndAnswerModel>>() {
            }.getType();
            themeModels = gson.fromJson(string, type);
            return (ArrayList<QuestionAndAnswerModel>) themeModels;
        }
        return null;
    }

    public static  <T> void setTheme(String key, List<T> list,Context context) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        set(key, json,context);
    }

    public static void set(String key, String value,Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(ThemeModel), Context.MODE_PRIVATE);
        if (sharedPref != null) {
            SharedPreferences.Editor prefsEditor = sharedPref.edit();
            prefsEditor.putString(key, value);
            prefsEditor.commit();
        }
    }

    public static List<ThemeModel> getTheme(String key,Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(ThemeModel), Context.MODE_PRIVATE);
        if (sharedPref != null) {

            Gson gson = new Gson();
            List<ThemeModel> themeModels;

            String string = sharedPref.getString(key, null);
            Type type = new TypeToken<List<ThemeModel>>() {
            }.getType();
            themeModels = gson.fromJson(string, type);
            return themeModels;
        }
        return null;
    }
    public static String getUsername(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        String  username = sharedPreferences.getString(USERNAME, "name");
        return username;
    }

    public static void setUsername(Context context, String username)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME, username);
        editor.commit();
    }

    public static boolean getIsForUpdateAvailable(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        boolean autoplay = sharedPreferences.getBoolean(IS_FORCE_UPDATE_AVAILABLE, false);
        return autoplay;
    }

    public static void setIsForUpdateAvailable(Context context, boolean codeOfContactValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_FORCE_UPDATE_AVAILABLE, codeOfContactValue);
        editor.commit();
    }

    public static String getIsForUpdateVersion(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        String autoplay = sharedPreferences.getString(IS_FORCE_UPDATE_VERSION, null);
        return autoplay;
    }

    public static void setIsForUpdateVersion(Context context, String codeOfContactValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IS_FORCE_UPDATE_VERSION, codeOfContactValue);
        editor.commit();
    }
    public static long getTimer(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE,Context.MODE_PRIVATE);
        long timer = sharedPreferences.getLong(Timer,0);
        return timer;
    }
    public static void setTimer(Context context,long timer){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(Timer,timer);
        editor.commit();
    }

    public static int getCustomerDetailedId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt(CUSTOMER_DETAILED_ID, 0);
        return id;
    }

    public static void setCustomerDetailedId(Context context, int id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KANGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CUSTOMER_DETAILED_ID, id);
        editor.commit();
    }

}
