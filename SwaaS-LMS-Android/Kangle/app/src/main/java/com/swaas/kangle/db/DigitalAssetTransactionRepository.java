package com.swaas.kangle.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.swaas.kangle.UploadService.DigitalAssetAnalyticsUpSyncService;
import com.swaas.kangle.db.Contract.AssetAnalyticsContract;
import com.swaas.kangle.models.DigitalAssetTransactionMaster;
import com.swaas.kangle.playerPart.DigitalAssets;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.DateHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit.Retrofit;

/**
 * Created by Sayellessh on 02-05-2017.
 */

public class DigitalAssetTransactionRepository  {

    public static final String TABLE_DIGITAL_ASSET_TRANSACTION = "tbl_DIGASSETS_TRANSACTION_MASTER";

    GetDA mGetDA;

    public static final String SlNo= "SlNo";
    public static final String DAID = "DAID";
    public static final String Play_Time = "Playtime";
    public static final String Is_Read = "Is_Read";
    public static final String Is_Downloaded = "Is_Downloaded";
    public static final String User_Like = "User_Like";
    public static final String User_Rating = "User_Rating";
    public static final String offlineURL = "offlineURL";
    public static final String onlineURL = "onlineURL";
    public static final String DAName = "DAName";
    public static final String DACategoryCode = "DACategoryCode";
    public static final String DACategoryName = "DACategoryName";
    public static final String Tags= "Tags";
    public static final String TotalViews = "TotalViews";
    public static final String TotalLikes = "TotalLikes";
    public static final String TotalDislikes = "TotalDislikes";
    public static final String TotalRatings = "TotalRatings";
    public static final String UDTags = "UDTags";

    public static final String DocumentType = "DocumentType";
    public static final String DA_Description = "DA_Description";
    public static final String DA_Type = "DA_Type";

    public static final String RecordDate = "Recorddate";

    public static final String OfflinePlay = "OfflinePlay";
    public static final String OnlinePlay = "OnlinePlay";
    public static final String Longitude = "Longitude";
    public static final String Lattitude = "Lattitude";

    public static final String IsSynced = "IsSynced";

    private Context mContext;

    private DatabaseHandler dbHandler = null;

    private SQLiteDatabase database = null;
    private Retrofit retrofit;

    public DigitalAssetTransactionRepository(Context context) {
        dbHandler = new DatabaseHandler(context);
        mContext = context;
    }

    public void DBConnectionOpen()  {
        database = dbHandler.getWritableDatabase();
    }

    public void DBConnectionClose() {
        if (database.isOpen()) {
            database.close();
        }
    }

    public static String Create()
    {
        String CREATE_TABLE=" CREATE TABLE IF NOT EXISTS " +TABLE_DIGITAL_ASSET_TRANSACTION+" ( "+ SlNo+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +DAID+ " TEXT ,"+offlineURL+" TEXT, "+onlineURL+" TEXT,"+ Tags +" TEXT ,"+Play_Time+" TEXT ,"
                +DocumentType+" TEXT, "+DAName+" TEXT, " +DACategoryCode+" TEXT, " +DACategoryName+" TEXT, "
                +TotalViews+" INT, " +TotalLikes+" INT, " +TotalDislikes+" INT, "+TotalRatings+" INT, "+UDTags+" TEXT, "
                +DA_Description+" TEXT,"+DA_Type+" TEXT," +Is_Read+" INT, "+Is_Downloaded+" INT, "+User_Like+" INT, "+User_Rating +" INT,"
                +RecordDate+" TEXT ,"+OfflinePlay +" TEXT , "+ OnlinePlay + " TEXT , " +  Longitude + " TEXT , "
                +Lattitude + " TEXT , "+IsSynced+" INT )";
        return CREATE_TABLE;
    }

    public static String Create_Digital_Asset_Analytics() {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.TABLE_NAME)
                .append("(")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics._ID)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.PART_ID)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.PLAYED_TIME_DURATION)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.DETAILED_DATETIME)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.TIME_ZONE)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.IS_PREVIEW)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.IS_SYNCED)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.SYNCED_DATETIME)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.PART_URL)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.SESSIONID)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.DETAILED_STARTTIME)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.DETAILED_ENDTIME)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.PLAYER_START_TIME)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.PLAYER_END_TIME)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.CUSTOMER_DETAILED_ID)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.PLAYMODE)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.LIKE)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.RATING)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.LATITUDE)
                .append(" DECIMAL(15,2),")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.LONGITUDE)
                .append(" DECIMAL(15,2),")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.DA_TYPE)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.USER_LIKE)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.USER_RATING)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.TOTAL_VIEWS)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.TOTAL_DISLIKE)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.Is_Downloaded)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalytics.DA_CODE)
                .append(" INT);");
        return builder.toString();
    }


    public static String Create_Customer_DA_Feedback() {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ")
                .append(AssetAnalyticsContract.CustomerDAFeedback.TABLE_NAME)
                .append("(")
                .append(AssetAnalyticsContract.CustomerDAFeedback._ID)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(AssetAnalyticsContract.CustomerDAFeedback.COURSE_ID)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.CustomerDAFeedback.SECTION_ID)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.CustomerDAFeedback.DA_CODE)
                .append(" INT,")
                .append(AssetAnalyticsContract.CustomerDAFeedback.DA_TYPE)
                .append(" INT,")
                .append(AssetAnalyticsContract.CustomerDAFeedback.RATING)
                .append(" INT,")
                .append(AssetAnalyticsContract.CustomerDAFeedback.USER_LIKE)
                .append(" INT,")
                .append(AssetAnalyticsContract.CustomerDAFeedback.FEEDBACK)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.CustomerDAFeedback.IS_SYNCHED)
                .append(" INT,")
                .append(AssetAnalyticsContract.CustomerDAFeedback.SOURCE_OF_ENTRY)
                .append(" INT,")
                .append(AssetAnalyticsContract.CustomerDAFeedback.TIME_ZONE)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.CustomerDAFeedback.UPDATED_DATE)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.CustomerDAFeedback.UPDATED_DATETIME)
                .append(" TEXT);");

        return builder.toString();
    }



    public void bulkInsert(List<DigitalAssetTransactionMaster> data){
        for(DigitalAssetTransactionMaster digassets : data){
            insertRecord(digassets);
        }
    }

    public void insertOrUpdateDAAnalytics(DigitalAssets digitalAssets, boolean isDelete) {
        try {
            DBConnectionOpen();
            if (isDelete) {
                database.delete(AssetAnalyticsContract.DigitalAssetAnalytics.TABLE_NAME, null, null);
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.DA_CODE, digitalAssets.getDA_Code());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.PLAYED_TIME_DURATION, digitalAssets.getPlayed_Time_Duration());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.DETAILED_DATETIME, DateHelper.getCurrentDate());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.TIME_ZONE, displayTimeZone(TimeZone.getDefault()));

            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.IS_SYNCED, digitalAssets.getIsSynced());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.SYNCED_DATETIME, digitalAssets.getSyncedDateTime());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.PART_ID, digitalAssets.getPart_Id());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.PART_URL, digitalAssets.getPart_URL());

            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.DETAILED_STARTTIME, digitalAssets.getDetailed_StartTime());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.DETAILED_ENDTIME, digitalAssets.getDetailed_EndTime());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.PLAYER_START_TIME, digitalAssets.getPlayer_Start_Time());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.PLAYER_END_TIME, digitalAssets.getPlayer_End_Time());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.PLAYMODE, digitalAssets.getPlayMode());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.LIKE, digitalAssets.getLike());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.RATING, digitalAssets.getRating());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.LATITUDE, digitalAssets.getLattitude());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.LONGITUDE, digitalAssets.getLongitude());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.CUSTOMER_DETAILED_ID, digitalAssets.getCustomer_Detailed_Id());

            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.DA_TYPE, digitalAssets.getDA_Type());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.USER_LIKE, digitalAssets.getUser_Like());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.USER_RATING, digitalAssets.getUser_Rating());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.TOTAL_DISLIKE, digitalAssets.getTotal_Dislikes());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.TOTAL_VIEWS, digitalAssets.getTotal_Views());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.Is_Downloaded, digitalAssets.getIs_Downloaded());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.SECTION_ID, digitalAssets.getSection_Id());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.COURSE_ID, digitalAssets.getCourse_Id());
            if(digitalAssets.getCourse_Id() > 0){
                contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.IS_PREVIEW, 0);
            }else{
                contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.IS_PREVIEW, 1);
                contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.SESSIONID, digitalAssets.getPreviewSessionId());
            }
            if (digitalAssets.getId() != 0) {
                contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.SESSIONID,
                        getAssetSessionIdOnTime(digitalAssets.getDA_Code(), digitalAssets.getCourse_Id(), digitalAssets.getSection_Id(), true));
                database.update(AssetAnalyticsContract.DigitalAssetAnalytics.TABLE_NAME, contentValues, AssetAnalyticsContract.DigitalAssetAnalytics._ID + "=?", new String[]{String.valueOf(digitalAssets.getId())});
            } else {
                if( digitalAssets.getCourse_Id() > 0) {
                    contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.SESSIONID, 0);
                }else{
                    contentValues.put(AssetAnalyticsContract.DigitalAssetAnalytics.SESSIONID, digitalAssets.getPreviewSessionId());
                }

                long rowCount = database.insert(AssetAnalyticsContract.DigitalAssetAnalytics.TABLE_NAME, null, contentValues);
                if (rowCount != -1) {
                    digitalAssets.setId((int) rowCount);
                }
            }
        } catch (Exception e) {
            Log.e(e.toString(),"Error");
        } finally {
            DBConnectionClose();
        }
    }

    private static String displayTimeZone(TimeZone tz) {

        long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset())
                - TimeUnit.HOURS.toMinutes(hours);
        // avoid -4:-30 issue
        minutes = Math.abs(minutes);

        String result = "";
        if (hours > 0) {
            result = String.format("+%d:%02d ", hours, minutes);
        } else {
            result = String.format("-%d:%02d ", hours, minutes);
        }

        return result;

    }


    public int getAssetSessionIdOnTime(int daCode, int courseId, int sectionId, boolean add) {
        int sessionId = 0;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("SELECT SessionId FROM tran_Digital_Asset_Analytics WHERE  Detailed_DateTime ='"+DateHelper.getCurrentDate()+"' AND DA_Code='"+daCode+"' " +
                            "AND Course_ID='"+courseId+"' AND Section_Id='"+sectionId+"' AND Detailed_EndTime <> '' ORDER BY _id DESC LIMIT 1",null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                sessionId = cursor.getInt(cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.SESSIONID));
            }
            if(sessionId == 0){
                sessionId++;
            }else{
                if(add){
                    sessionId++;
                }
            }

        } catch (Exception e) {
            Log.e(e.toString(),"Error");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return sessionId;
    }



    public int getAssetSessionId(int daCode,int courseId,int sectionId) {
        int sessionId = 0;
        Cursor cursor = null;
        try {
            DBConnectionOpen();
            cursor = database.rawQuery("SELECT SessionId FROM tran_Digital_Asset_Analytics WHERE  Detailed_DateTime =? AND DA_Code=?  ORDER BY _id DESC LIMIT 1",
                    new String[]{String.valueOf(DateHelper.getCurrentDate()),String.valueOf(daCode)});
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                sessionId = cursor.getInt(cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.SESSIONID));
            }
            sessionId++;

        } catch (Exception e) {
            Log.e(e.toString(),"Error");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            DBConnectionClose();
        }
        return sessionId;
    }


    public void getUnSyncedDigitalAssetAnalytics() {
        /*Cursor cursor = null;
        ArrayList<DigitalAssets> digitalAssets = null;
        try {
            DBConnectionOpen();
            cursor = database.rawQuery("select AAD._ID,AAD.Part_Id,AAD.Played_Time_Duration," +
                    "AAD.Detailed_DateTime,AAD.Time_Zone,AAD.is_Preview,AAD.is_Synced,AAD.Synced_DateTime," +
                    "AAD.Part_URL,AAD.SessionId,AAD.Detailed_StartTime,AAD.Detailed_EndTime," +
                    "AAD.Player_Start_Time,AAD.Player_End_Time," +
                    "AAD.Customer_Detailed_Id," +
                    "AAD.PlayMode,AAD.Like,AAD.Rating,AAD.Latitude,AAD.Longitude,AAD.DA_Code," +

                    "AAD.User_Like,AAD.User_Rating,AAD.Like,AAD.TotalViews,AAD.DA_Type,AAD.Is_Downloaded From  " +
                    "tran_Digital_Asset_Analytics AAD where AAD.is_Synced ='0'", null);
            digitalAssets = getUnSyncedDigitalAssets(cursor);
            mGetDA.getDASuccess(digitalAssets);

        } catch (Exception e) {
            Log.e(e.toString(),"Error");
            mGetDA.getDAFailure(e.toString());
        } finally {
            DBConnectionClose();
        }*/


        Cursor cursor = null;
        ArrayList<DigitalAssets> digitalAssets = null;
        try {
            DBConnectionOpen();

            database.delete(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.TABLE_NAME,
                    AssetAnalyticsContract.DigitalAssetAnalyticsDetails.PLAYED_TIME_DURATION + "=?", new String[]{String.valueOf("0")});

            cursor = database.rawQuery("select *  From  tran_Digital_Asset_Analytics  AAD WHERE AAD.is_Synced ='0'", null);
            digitalAssets = getUnSyncedDigitalAssets(cursor);
            mGetDA.getDASuccess(digitalAssets);
        } catch (Exception e) {
            mGetDA.getDAFailure(e.toString());
        } finally {
            DBConnectionClose();
        }


    }

    private ArrayList<DigitalAssets> getUnSyncedDigitalAssets(Cursor cursor) {
        ArrayList<DigitalAssets> digitalAssets = new ArrayList<DigitalAssets>();
        try {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    int id = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics._ID);
                    int partId = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.PART_ID);
                    int timeZone = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.TIME_ZONE);
                    int playedTimeDur = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.PLAYED_TIME_DURATION);
                    int detailedDateTime = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.DETAILED_DATETIME);
                    int isPreview = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.IS_PREVIEW);
                    int iSynced = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.IS_SYNCED);
                    int syncedDateTime = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.SYNCED_DATETIME);
                    int partUrl = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.PART_URL);
                    int sessionId = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.SESSIONID);
                    int detailedStartTime = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.DETAILED_STARTTIME);
                    int detailedEndTime = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.DETAILED_ENDTIME);
                    int playerStartTime = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.PLAYER_START_TIME);
                    int playerEndTime = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.PLAYER_END_TIME);
                    int detailedId = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.CUSTOMER_DETAILED_ID);
                    int playMode = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.PLAYMODE);
                    int like = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.LIKE);
                    int rating = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.RATING);
                    int latitude = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.LATITUDE);
                    int longitude = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.LONGITUDE);
                    int daCode = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.DA_CODE);
                    int userlike = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.USER_LIKE);
                    int userrating = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.USER_RATING);
                    int totviews = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.TOTAL_VIEWS);
                    int datype = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.DA_TYPE);
                    int downloaded = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.Is_Downloaded);
                    int courseId = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.COURSE_ID);
                    int sectionId = cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalytics.SECTION_ID);



                    DigitalAssets da = new DigitalAssets();
                    da.setId(cursor.getInt(id));
                    da.setCustomer_Detailed_Id(cursor.getInt(detailedId));
                    da.setDA_Code(cursor.getInt(daCode));
                    da.setDetailed_DateTime(cursor.getString(detailedDateTime));
                    da.setPart_Id(cursor.getInt(partId));
                    da.setPart_URL(cursor.getString(partUrl));
                    da.setSessionId(cursor.getInt(sessionId));
                    da.setDetailed_StartTime(cursor.getString(detailedStartTime));
                    da.setDetailed_EndTime(cursor.getString(detailedEndTime));
                    da.setPlayer_Start_Time(cursor.getString(playerStartTime));
                    da.setPlayer_End_Time(cursor.getString(playerEndTime));
                    da.setPlayed_Time_Duration(cursor.getInt(playedTimeDur));
                    da.setTime_Zone(cursor.getString(timeZone));
                    da.setIsPreview(cursor.getInt(isPreview));
                    da.setIsSynced(cursor.getInt(iSynced));
                    da.setSyncedDateTime(cursor.getString(syncedDateTime));
                    da.setPlayMode(cursor.getInt(playMode));
                    da.setLike(cursor.getInt(like));
                    da.setRating(cursor.getInt(rating));
                    da.setLattitude(cursor.getDouble(latitude));
                    da.setLongitude(cursor.getDouble(longitude));
                    da.setDA_Type(cursor.getInt(datype));
                    da.setUser_Like(cursor.getInt(userlike));
                    da.setUser_Rating(cursor.getInt(userrating));
                    da.setTotal_Views(cursor.getInt(totviews));
                    da.setIs_Downloaded(cursor.getInt(downloaded));
                    da.setSection_Id(cursor.getInt(sectionId));
                    da.setCourse_Id(cursor.getInt(courseId));
                    da.setSource_Of_Entry(1);
                    da.setCompany_Id(PreferenceUtils.getCompnayId(mContext));
                    da.setUser_Id(PreferenceUtils.getUserId(mContext));
                    da.setCompany_Code(PreferenceUtils.getCompanyCode(mContext));
                    digitalAssets.add(da);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.e(e.toString(),"Error");
        } finally {

        }
        return digitalAssets;
    }


    public int generateCustomerDetailedIdBasedOnCustomer(String date, String courseId, String regionCode) {
        int id = 1;
        Cursor cursor = null;
        try {
            DBConnectionOpen();
            cursor = database.rawQuery("SELECT MAX(Customer_Detailed_Id) AS id " +
                    "from tran_Digital_Asset_Analytics where Course_ID  = '" + courseId + "' " +
                    "AND Customer_Region_Code ='" + regionCode + "' AND Detailed_DateTime = '" + date + "'", null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getInt(cursor.getColumnIndex("id"));
            }
            if (id <= 0) {
                PreferenceUtils.setCustomerDetailedId(mContext, 1);
            } else {
                PreferenceUtils.setCustomerDetailedId(mContext, id + 1);
            }

        } catch (Exception e) {

        } finally {
            DBConnectionClose();
        }
        return id;
    }


    public void insertOrUpdateRecord(DigitalAssetTransactionMaster data,String action){
        if(getDAIDValue(data.getDAID()) == null){
            insertRecord(data);
        } else{
            if(action.equalsIgnoreCase("like")){
                updateIsLiked(data);
            } else if(action.equalsIgnoreCase("rating")){
                updateRating(data);
            } else if(action.equalsIgnoreCase("play")){
                updateIsRead(data);
            } else if(action.equalsIgnoreCase("playwithlike")){
                updateIsReadwithlike(data);
            } else if(action.equalsIgnoreCase("download")){
                updatedownloaded(data);
            }
        }
    }

    public List<DigitalAssetTransactionMaster> getAllValues(){
        List<DigitalAssetTransactionMaster> digitalAssetTransactionMasters = new ArrayList<DigitalAssetTransactionMaster>();

        try{
            String query ="select tbl_DIGASSETS_TRANSACTION_MASTER.SlNo, " +
                    "tbl_DIGASSETS_TRANSACTION_MASTER.DAID," +
                    "tbl_DIGASSETS_TRANSACTION_MASTER.Is_Read," +
                    "tbl_DIGASSETS_TRANSACTION_MASTER.User_Like,"+
                    "tbl_DIGASSETS_TRANSACTION_MASTER.User_Rating,"+
                    "tbl_DIGASSETS_TRANSACTION_MASTER.TotalViews," +
                    "tbl_DIGASSETS_TRANSACTION_MASTER.TotalLikes," +
                    "tbl_DIGASSETS_TRANSACTION_MASTER.RecordDate," +
                    "tbl_DIGASSETS_TRANSACTION_MASTER.TotalRatings from tbl_DIGASSETS_TRANSACTION_MASTER " +
                    "WHERE tbl_DIGASSETS_TRANSACTION_MASTER.IsSynced = 0";
            DBConnectionOpen();
            Cursor accompanistCursor = database.rawQuery(query, null);
            digitalAssetTransactionMasters = getAllFromCursor(accompanistCursor);
            accompanistCursor.close();
            //getAccompanistDataAPIListnerCB.getAccompanistDataSuccessCB(accompanists);
            //Log.d("parm getAllAccompanistList  size >>> ", String.valueOf(listOfAssets.size()));
        }catch (Exception e){
            Log.d("parm exception getAll ",e.toString());
            //getAccompanistDataAPIListnerCB.getAccompanistDataFailureCB("Error Occured");
        }finally {
            DBConnectionClose();
        }

        return digitalAssetTransactionMasters;
    }

    private List<DigitalAssetTransactionMaster> getAllFromCursor(Cursor assetCursor) {

        List<DigitalAssetTransactionMaster> assetsMasters = new ArrayList<DigitalAssetTransactionMaster>();
        boolean value = false;
        if (assetCursor != null && assetCursor.getCount() > 0) {
            assetCursor.moveToFirst();
            int slno = assetCursor.getColumnIndex(SlNo);
            int daid = assetCursor.getColumnIndex(DAID);
            int isread = assetCursor.getColumnIndex(Is_Read);
            int userLike = assetCursor.getColumnIndex(User_Like);
            int userrating = assetCursor.getColumnIndex(User_Rating);
            int totalviews = assetCursor.getColumnIndex(TotalViews);
            int totallikes = assetCursor.getColumnIndex(TotalLikes);
            int recorddate = assetCursor.getColumnIndex(RecordDate);
            int totalratings = assetCursor.getColumnIndex(TotalRatings);
            do {
                DigitalAssetTransactionMaster assetsMaster = new DigitalAssetTransactionMaster();
                assetsMaster.setSlNo(assetCursor.getInt(slno));
                assetsMaster.setDAID(assetCursor.getString(daid));
                if(assetCursor.getInt(isread)==1){
                    value = true;
                }
                assetsMaster.setIs_Read(value);
                assetsMaster.setUser_Like(assetCursor.getInt(userLike));
                assetsMaster.setUser_Rating(assetCursor.getInt(userrating));
                assetsMaster.setTotalViews(assetCursor.getInt(totalviews));
                assetsMaster.setTotalLikes(assetCursor.getInt(totallikes));
                assetsMaster.setRecorddate(assetCursor.getString(recorddate));
                assetsMaster.setTotalRatings(assetCursor.getInt(totalratings));

                assetsMasters.add(assetsMaster);
            } while (assetCursor.moveToNext());
        }
        return assetsMasters;
    }

    public List<DigitalAssetTransactionMaster> getAllUpdatedAssetValues(){
        List<DigitalAssetTransactionMaster> digitalAssetTransactionMasters = new ArrayList<DigitalAssetTransactionMaster>();

        try{
            String query ="select tbl_DIGASSETS_TRANSACTION_MASTER.SlNo, " +
                    "tbl_DIGASSETS_TRANSACTION_MASTER.DAID," +
                    "tbl_DIGASSETS_TRANSACTION_MASTER.Is_Downloaded," +
                    "tbl_DIGASSETS_TRANSACTION_MASTER.Playtime," +
                    "tbl_DIGASSETS_TRANSACTION_MASTER.Longitude," +
                    "tbl_DIGASSETS_TRANSACTION_MASTER.Lattitude," +
                    "tbl_DIGASSETS_TRANSACTION_MASTER.OnlinePlay," +
                    "tbl_DIGASSETS_TRANSACTION_MASTER.OfflinePlay" +
                    " from tbl_DIGASSETS_TRANSACTION_MASTER " +
                    "WHERE tbl_DIGASSETS_TRANSACTION_MASTER.IsSynced = 1";
            DBConnectionOpen();
            Cursor accompanistCursor = database.rawQuery(query, null);
            digitalAssetTransactionMasters = getAllAssetAnalyticsFromCursor(accompanistCursor);
            accompanistCursor.close();
            //getAccompanistDataAPIListnerCB.getAccompanistDataSuccessCB(accompanists);
            //Log.d("parm getAllAccompanistList  size >>> ", String.valueOf(listOfAssets.size()));
        }catch (Exception e){

            //getAccompanistDataAPIListnerCB.getAccompanistDataFailureCB("Error Occured");
        }finally {
            DBConnectionClose();
        }

        return digitalAssetTransactionMasters;
    }

    private List<DigitalAssetTransactionMaster> getAllAssetAnalyticsFromCursor(Cursor assetCursor) {

        List<DigitalAssetTransactionMaster> assetsMasters = new ArrayList<DigitalAssetTransactionMaster>();
        boolean value = false;
        if (assetCursor != null && assetCursor.getCount() > 0) {
            assetCursor.moveToFirst();
            int slno = assetCursor.getColumnIndex(SlNo);
            int daid = assetCursor.getColumnIndex(DAID);
            int Downloaded = assetCursor.getColumnIndex(Is_Downloaded);
            int play = assetCursor.getColumnIndex(Play_Time);
            int Lng = assetCursor.getColumnIndex(Longitude);
            int Lat = assetCursor.getColumnIndex(Lattitude);
            int online = assetCursor.getColumnIndex(OnlinePlay);
            int offline = assetCursor.getColumnIndex(OfflinePlay);
            do {
                DigitalAssetTransactionMaster assetsMaster = new DigitalAssetTransactionMaster();
                assetsMaster.setSlNo(assetCursor.getInt(slno));
                assetsMaster.setDAID(assetCursor.getString(daid));
                if(assetCursor.getInt(Downloaded)==1){
                    value = true;
                }
                assetsMaster.setIs_Downloaded(value);
                assetsMaster.setPlaytime(Long.parseLong(assetCursor.getString(play)));
                assetsMaster.setLattitude(assetCursor.getString(Lat));
                assetsMaster.setLongitude(assetCursor.getString(Lng));
                assetsMaster.setOnlinePlay(assetCursor.getString(online));
                assetsMaster.setOfflinePlay(assetCursor.getString(offline));

                assetsMasters.add(assetsMaster);
            } while (assetCursor.moveToNext());
        }
        return assetsMasters;
    }

    public String getDAIDValue(String data) {
        String value = null;
        try{
            DBConnectionOpen();
        String selectQuery = " SELECT tbl_DIGASSETS_TRANSACTION_MASTER.DAID FROM tbl_DIGASSETS_TRANSACTION_MASTER WHERE tbl_DIGASSETS_TRANSACTION_MASTER.DAID='"+data+"'";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if(cursor!= null && cursor.getCount()>0) {
            cursor.moveToFirst();
            value = (cursor.getString(cursor.getColumnIndex(DAID)));
            //value =  asset.getDAID();
        } else {
            Log.d("DateCount",0+"" );
            value =  null ;
        }
        }catch(Exception e){
            Log.d("CursorError",e.toString());
        }finally {
            DBConnectionClose();
        }
        return value;
    }

    public void insertRecord(DigitalAssetTransactionMaster data){
            try {
                int valueread = 0;
                int valuedownloaded = 0;
                if(data.is_Read()){
                    valueread = 1;
                }
                if(data.is_Downloaded()){
                    valuedownloaded = 1;
                }
                DBConnectionOpen();
                ContentValues contentValues = new ContentValues();
                contentValues.put(DAID, data.getDAID());
                contentValues.put(offlineURL, data.getOfflineURL());
                contentValues.put(onlineURL, data.getOnlineURL());
                contentValues.put(Tags, data.getTags());
                contentValues.put(DocumentType, data.getDocumentType());
                contentValues.put(DAName, data.getDAName());
                contentValues.put(DACategoryCode, data.getDACategoryCode());
                contentValues.put(DACategoryName, data.getDACategoryName());
                contentValues.put(TotalViews, data.getTotalViews());
                contentValues.put(TotalLikes, data.getTotalLikes());
                contentValues.put(TotalDislikes, data.getTotalDislikes());
                contentValues.put(TotalRatings, data.getTotalRatings());
                contentValues.put(UDTags, data.getUDTags());
                contentValues.put(DA_Description, data.getDA_Description());
                contentValues.put(DA_Type, data.getDA_Type());
                contentValues.put(Is_Read, valueread);
                contentValues.put(Play_Time,data.getPlaytime());
                contentValues.put(Is_Downloaded, valuedownloaded);
                contentValues.put(User_Like, data.getUser_Like());
                contentValues.put(User_Rating, data.getUser_Rating());
                contentValues.put(RecordDate, data.getRecorddate());
                contentValues.put(OfflinePlay,data.getOfflinePlay());
                contentValues.put(OnlinePlay,data.getOnlinePlay());
                contentValues.put(Lattitude,data.getLattitude());
                contentValues.put(Longitude,data.getLongitude());
                contentValues.put(IsSynced, data.getIsSynced());

                long rowCount = database.insert(TABLE_DIGITAL_ASSET_TRANSACTION, null, contentValues);
                Log.d("rowcount",String.valueOf(rowCount));
                //insertOrUpdateCustomerPersonalCB.getInsertOrUpDateCustomerPersonalSuccessCB(Integer.parseInt(String.valueOf(rowCount)));
            } catch (Exception e) {
                //insertOrUpdateCustomerPersonalCB.getInsertOrUpDateCustomerPersonalFailureCB(e.getMessage());

            } finally {
                DBConnectionClose();
            }
    }

    public void updateIsRead(DigitalAssetTransactionMaster data){
        int value = 0;
        if(data.is_Read()){
            value = 1;
        }
        String updateQuery = "UPDATE tbl_DIGASSETS_TRANSACTION_MASTER SET Is_Read = " + value + ",TotalViews ="+ data.getTotalViews() +" WHERE DAID = '" + data.getDAID() +"'";
        try {
            DBConnectionOpen();
            database.execSQL(updateQuery);
        }catch(Exception e){
            Log.d("",e.toString());
        } finally {
            DBConnectionClose();

        }
    }

    public void updateIsReadwithlike(DigitalAssetTransactionMaster data){
        int value = 0;
        if(data.is_Read()){
            value = 1;
        }
        String updateQuery = "UPDATE tbl_DIGASSETS_TRANSACTION_MASTER SET Is_Read = " + value + ",TotalViews ="+ data.getTotalViews() +",User_Like="
                + data.getUser_Like() +", TotalLikes="+ data.getTotalLikes() +" WHERE DAID = '" + data.getDAID() +"'";
        try {
            DBConnectionOpen();
            database.execSQL(updateQuery);
        }catch(Exception e){
            Log.d("",e.toString());
        } finally {
            DBConnectionClose();

        }
    }

    public void updateIsLiked(DigitalAssetTransactionMaster data){
        String updateQuery = "UPDATE tbl_DIGASSETS_TRANSACTION_MASTER SET User_Like=" + data.getUser_Like() + "," +
                "TotalLikes="+ data.getTotalLikes() +" WHERE DAID = '" + data.getDAID()+"'";
        try {
            DBConnectionOpen();
            database.execSQL(updateQuery);
        }catch(Exception e){
            Log.d("",e.toString());
        } finally {
            DBConnectionClose();

        }
    }

    public void updateRating(DigitalAssetTransactionMaster data){
        String updateQuery = "UPDATE tbl_DIGASSETS_TRANSACTION_MASTER SET User_Rating=" + data.getUser_Rating() + " WHERE DAID= '" + data.getDAID()+"'";
        try {
            DBConnectionOpen();
            database.execSQL(updateQuery);
        }catch(Exception e){
            Log.d("",e.toString());
        } finally {
            DBConnectionClose();

        }
    }

    public void updatedownloaded(DigitalAssetTransactionMaster data){
        int value = 0;
        if(data.is_Downloaded()){
            value = 1;
        }
        String updateQuery = "UPDATE tbl_DIGASSETS_TRANSACTION_MASTER SET Is_Downloaded=" + value + ", offlineURL = '"+ data.getOfflineURL() +"' WHERE DAID= '" + data.getDAID()+"'";
        try {
            DBConnectionOpen();
            database.execSQL(updateQuery);
        }catch(Exception e){
            Log.d("",e.toString());
        } finally {
            DBConnectionClose();
        }
    }

    public void updateRecord(int slno){

        String updateQuery = "UPDATE tbl_DIGASSETS_TRANSACTION_MASTER SET IsSynced = 1 WHERE SlNo = " + slno+ "";
        try {
            DBConnectionOpen();
            database.execSQL(updateQuery);
        }catch(Exception e){
            Log.d("",e.toString());
        } finally {
            DBConnectionClose();
        }
    }

    public void updateSyncedRecord(int id){
        String updateQuery = "UPDATE tran_Digital_Asset_Analytics SET is_Synced = 1 WHERE _id = " + id + "";
        try {
            DBConnectionOpen();
            database.execSQL(updateQuery);
        }catch(Exception e){
            Log.d("",e.toString());
        } finally {
            DBConnectionClose();
        }
    }

    public void RecordDelete(){
        try {
            DBConnectionClose();
            String rawQuery = "DELETE FROM tran_Digital_Asset_Analytics WHERE is_Synced = 1 And (select DATE('now',-1))";
            DBConnectionOpen();
            database.execSQL(rawQuery);
        }catch (Exception e){

        }finally {
            DBConnectionClose();
        }
    }

    public void deleteRecord(int slno){
        try {
            DBConnectionClose();
            String rawQuery = "DELETE FROM tbl_DIGASSETS_TRANSACTION_MASTER WHERE " + SlNo + "= '" + slno + "'";
            DBConnectionOpen();
            database.execSQL(rawQuery);
        }catch (Exception e){

        }finally {
            DBConnectionClose();
        }
    }

    public void deleteTransactionTable(){
        try{
            DBConnectionOpen();
            database.delete(TABLE_DIGITAL_ASSET_TRANSACTION, null, null);
        }catch (Exception e){

        }finally {
            DBConnectionClose();
        }
    }

    public void deleteAssetTransactionTable() {
        try{
            DBConnectionOpen();
        database.delete(AssetAnalyticsContract.DigitalAssetAnalytics.TABLE_NAME, null, null);
        }catch (Exception e){

        }finally {
            DBConnectionClose();
        }
    }

    public void CustomerFeedbackDABulkInsert(List<DigitalAssets> digitalAssetsList) {
        try {
            DBConnectionOpen();
            ContentValues contentValues = new ContentValues();
            int count = 0;
            for (DigitalAssets digitalAssets : digitalAssetsList) {
                contentValues.clear();
                contentValues.put(AssetAnalyticsContract.CustomerDAFeedback.COURSE_ID, digitalAssets.getCourse_Id());
                contentValues.put(AssetAnalyticsContract.CustomerDAFeedback.SECTION_ID, digitalAssets.getSection_Id());
                contentValues.put(AssetAnalyticsContract.CustomerDAFeedback.DA_CODE, digitalAssets.getDA_Code());
                contentValues.put(AssetAnalyticsContract.CustomerDAFeedback.DA_TYPE, digitalAssets.getDA_Type());
                contentValues.put(AssetAnalyticsContract.CustomerDAFeedback.RATING, digitalAssets.getRating());
                contentValues.put(AssetAnalyticsContract.CustomerDAFeedback.USER_LIKE, digitalAssets.getUser_Like());
                contentValues.put(AssetAnalyticsContract.CustomerDAFeedback.FEEDBACK, digitalAssets.getFeedback());
                contentValues.put(AssetAnalyticsContract.CustomerDAFeedback.IS_SYNCHED, digitalAssets.getIs_Synched());
                contentValues.put(AssetAnalyticsContract.CustomerDAFeedback.SOURCE_OF_ENTRY, digitalAssets.getSource_Of_Entry());
                contentValues.put(AssetAnalyticsContract.CustomerDAFeedback.TIME_ZONE, digitalAssets.getTime_Zone());
                contentValues.put(AssetAnalyticsContract.CustomerDAFeedback.UPDATED_DATE, digitalAssets.getUpdated_Date());
                contentValues.put(AssetAnalyticsContract.CustomerDAFeedback.UPDATED_DATETIME, digitalAssets.getUpdated_Datetime());
                long rowCount = database.insert(AssetAnalyticsContract.CustomerDAFeedback.TABLE_NAME, null, contentValues);
                if (rowCount != -1) {
                    count++;
                }
            }
        } catch (Exception e) {

        } finally {
            DBConnectionClose();
        }
    }

    public void getUnSyncedCustomerFeedback() {
        Cursor cursor = null;
        ArrayList<DigitalAssets> customerfeedback = null;
        try {
            DBConnectionOpen();
            cursor = database.rawQuery("select * from  tbl_EL_Customer_DA_Feedback  where Is_Synched = 0 ", null);
            customerfeedback = getUnSyncedCustomerFeedbackData(cursor);
            mGetDA.getDASuccess(customerfeedback);
        } catch (Exception e) {
            mGetDA.getDAFailure(e.toString());
        } finally {
            DBConnectionClose();
        }

    }
    private ArrayList<DigitalAssets> getUnSyncedCustomerFeedbackData(Cursor cursor) {
        ArrayList<DigitalAssets> customerFeedback = new ArrayList<DigitalAssets>();
        try {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    int id = cursor.getColumnIndex(AssetAnalyticsContract.CustomerDAFeedback._ID);
                    int courseId = cursor.getColumnIndex(AssetAnalyticsContract.CustomerDAFeedback.COURSE_ID);
                    int sectionId = cursor.getColumnIndex(AssetAnalyticsContract.CustomerDAFeedback.SECTION_ID);
                    int da_code = cursor.getColumnIndex(AssetAnalyticsContract.CustomerDAFeedback.DA_CODE);
                    int da_type = cursor.getColumnIndex(AssetAnalyticsContract.CustomerDAFeedback.DA_TYPE);
                    int rating = cursor.getColumnIndex(AssetAnalyticsContract.CustomerDAFeedback.RATING);
                    int user_like = cursor.getColumnIndex(AssetAnalyticsContract.CustomerDAFeedback.USER_LIKE);
                    int feedback = cursor.getColumnIndex(AssetAnalyticsContract.CustomerDAFeedback.FEEDBACK);
                    int is_synched = cursor.getColumnIndex(AssetAnalyticsContract.CustomerDAFeedback.IS_SYNCHED);
                    int source_of_entry = cursor.getColumnIndex(AssetAnalyticsContract.CustomerDAFeedback.SOURCE_OF_ENTRY);
                    int time_zone = cursor.getColumnIndex(AssetAnalyticsContract.CustomerDAFeedback.TIME_ZONE);
                    int updated_date = cursor.getColumnIndex(AssetAnalyticsContract.CustomerDAFeedback.UPDATED_DATE);
                    int updated_datetime = cursor.getColumnIndex(AssetAnalyticsContract.CustomerDAFeedback.UPDATED_DATETIME);


                    DigitalAssets da = new DigitalAssets();
                    da.setId(cursor.getInt(id));
                    da.setCourse_Id(cursor.getInt(courseId));
                    da.setSection_Id(cursor.getInt(sectionId));
                    da.setDA_Code(cursor.getInt(da_code));
                    da.setDA_Type(cursor.getInt(da_type));
                    da.setRating(cursor.getInt(rating));
                    da.setUser_Like(cursor.getInt(user_like));
                    da.setFeedback(cursor.getString(feedback));
                    da.setIs_Synched(cursor.getInt(is_synched));
                    da.setSource_Of_Entry(cursor.getInt(source_of_entry));
                    da.setTime_Zone(cursor.getString(time_zone));
                    da.setUpdated_Date(cursor.getString(updated_date));
                    da.setUpdated_Datetime(cursor.getString(updated_datetime));
                    customerFeedback.add(da);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {

        } finally {

        }

        return customerFeedback;
    }

    public interface GetDA {
        void getDASuccess(ArrayList<DigitalAssets> digitalAssets);

        void getDAFailure(String message);
    }

    public void setmGetDA(GetDA getDA) {
        mGetDA = getDA;
    }
}
