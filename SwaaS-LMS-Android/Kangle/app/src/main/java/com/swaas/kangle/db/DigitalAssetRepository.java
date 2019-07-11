package com.swaas.kangle.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.swaas.kangle.db.Contract.AssetAnalyticsContract;
import com.swaas.kangle.playerPart.DigitalAssets;
import com.swaas.kangle.utils.DateHelper;

import java.util.ArrayList;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sayellessh on 10-08-2017.
 */

public class DigitalAssetRepository extends DatabaseHandler {

//    private static final LogTracer LOG_TRACER = LogTracer.instance(DigitalAssetRepository.class);
//    private GetDigitalAssetAPIListenerCB getDigitalAssetAPIListenerCB;
    Context mContext;
    DatabaseHandler mDatabaseHandler;
    SQLiteDatabase mSQLiteDatabase;
//    InsertDA mInsertDA;
    GetDA mGetDA;

    public DigitalAssetRepository(Context context) {
        super(context);
        mContext = context;
        mDatabaseHandler = new DatabaseHandler(mContext);
    }

    public void DBConnectionOpen() throws SQLException {
        mSQLiteDatabase = mDatabaseHandler.getWritableDatabase();
    }

    public void DBConnectionClose() throws SQLException {
        if (mSQLiteDatabase.isOpen()) {
            mSQLiteDatabase.close();
        }
    }

    public static String Create_Digital_Asset_Analytics_Details() {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.TABLE_NAME)
                .append("(")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails._ID)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.PART_ID)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.PLAYED_TIME_DURATION)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.DETAILED_DATETIME)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.TIME_ZONE)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.IS_PREVIEW)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.IS_SYNCED)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.SYNCED_DATETIME)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.PART_URL)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.SESSIONID)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.DETAILED_STARTTIME)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.DETAILED_ENDTIME)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.PLAYER_START_TIME)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.PLAYER_END_TIME)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.CUSTOMER_DETAILED_ID)
                .append(" TEXT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.PLAYMODE)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.LIKE)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.RATING)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.LATITUDE)
                .append(" DECIMAL(15,2),")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.LONGITUDE)
                .append(" DECIMAL(15,2),")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.COURSE_ID)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.SECTION_ID)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.PUBLISH_ID)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.DA_TYPE)
                .append(" INT,")
                .append(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.DA_CODE)
                .append(" INT);");
        return builder.toString();
    }

    public void insertOrUpdateDAAnalyticsDetails(DigitalAssets digitalAssets, boolean isDelete) {
        try {
            DBConnectionOpen();
            if (isDelete) {
                mSQLiteDatabase.delete(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.TABLE_NAME, null, null);
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.DA_CODE, digitalAssets.getDA_Code());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.PLAYED_TIME_DURATION, digitalAssets.getPlayed_Time_Duration());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.DETAILED_DATETIME, DateHelper.getCurrentDate());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.TIME_ZONE, displayTimeZone(TimeZone.getDefault()));
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.IS_PREVIEW, digitalAssets.getIsPreview());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.IS_SYNCED, digitalAssets.getIsSynced());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.SYNCED_DATETIME, digitalAssets.getSyncedDateTime());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.PART_ID, digitalAssets.getPart_Id());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.PART_URL, digitalAssets.getPart_URL());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.SESSIONID, digitalAssets.getSessionId());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.DETAILED_STARTTIME, digitalAssets.getDetailed_StartTime());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.DETAILED_ENDTIME, digitalAssets.getDetailed_EndTime());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.PLAYER_START_TIME, digitalAssets.getPlayer_Start_Time());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.PLAYER_END_TIME, digitalAssets.getPlayer_End_Time());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.PLAYMODE, digitalAssets.getPlayMode());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.LIKE, digitalAssets.getLike());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.RATING, digitalAssets.getRating());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.LATITUDE, digitalAssets.getLattitude());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.LONGITUDE, digitalAssets.getLongitude());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.CUSTOMER_DETAILED_ID, digitalAssets.getCustomer_Detailed_Id());

            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.COURSE_ID, digitalAssets.getCourse_Id());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.SECTION_ID, digitalAssets.getSection_Id());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.PUBLISH_ID, digitalAssets.getPublish_Id());
            contentValues.put(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.DA_TYPE, digitalAssets.getDA_Type());

            if (digitalAssets.getId() != 0) {
                mSQLiteDatabase.update(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.TABLE_NAME, contentValues, AssetAnalyticsContract.DigitalAssetAnalyticsDetails._ID + "=?", new String[]{String.valueOf(digitalAssets.getId())});
            } else {
                long rowCount = mSQLiteDatabase.insert(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.TABLE_NAME, null, contentValues);
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



    public void getUnSyncedDigitalAssetAnalytics() {
        Cursor cursor = null;
        ArrayList<DigitalAssets> digitalAssets = null;
        try {
            DBConnectionOpen();

            cursor = mSQLiteDatabase.rawQuery("select *  From  tran_Digital_Asset_Analytics  AAD WHERE AAD.is_Synced ='0'", null);
            digitalAssets = getUnSyncedDigitalAssets(cursor);
            mGetDA.getDASuccess(digitalAssets);
        } catch (Exception e) {
            mGetDA.getDAFailure(e.toString());
        } finally {
            DBConnectionClose();
        }



    }

    public ArrayList<DigitalAssets> getUnSyncedSecondDigitalAssetAnalytics() {
/*
        Cursor cursor = null;
        ArrayList<DigitalAssets> digitalAssets = null;
        try {
            DBConnectionOpen();
            cursor = mSQLiteDatabase.rawQuery("select AAD._ID,AAD.Part_Id,AAD.Played_Time_Duration,AAD.Detailed_DateTime,AAD.Time_Zone,AAD.is_Preview,AAD.is_Synced,AAD.Synced_DateTime," +
                    "AAD.Part_URL,AAD.SessionId,AAD.Detailed_StartTime,AAD.Detailed_EndTime,AAD.Player_Start_Time,AAD.Player_End_Time," +
                    "AAD.Customer_Detailed_Id," +
                    "AAD.PlayMode,AAD.Like,AAD.Rating,AAD.Latitude,AAD.Longitude,AAD.DA_Code," +
                    "AAD.Course_Id,AAD.Section_Id,AAD.Publish_Id,AAD.DA_Type From  tran_Asset_Analytics_Details AAD where AAD.is_Synced ='1'", null);
            digitalAssets = getUnSyncedDigitalAssets(cursor);

        } catch (Exception e) {
            Log.e(e.toString(),"Error");
        } finally {
            DBConnectionClose();
        }
*/



        Cursor cursor = null;
        ArrayList<DigitalAssets> digitalAssets = null;
        try {
            DBConnectionOpen();

   /*         database.delete(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.TABLE_NAME,
                    AssetAnalyticsContract.DigitalAssetAnalyticsDetails.PLAYED_TIME_DURATION + "=?", new String[]{String.valueOf("0")});
*/
            cursor = mSQLiteDatabase.rawQuery("select *  From  tran_Digital_Asset_Analytics  AAD WHERE AAD.is_Synced ='0'", null);
            digitalAssets = getUnSyncedDigitalAssets(cursor);
            mGetDA.getDASuccess(digitalAssets);
        } catch (Exception e) {
            mGetDA.getDAFailure(e.toString());
        } finally {
            DBConnectionClose();
        }







        return digitalAssets;
    }

    public void updateSyncedDigitalAssetAnalytics(int ID){
        String updateQuery = "UPDATE tran_Asset_Analytics_Details SET is_Synced='1' WHERE _ID="+ ID +"";
        try {
            DBConnectionOpen();
            mSQLiteDatabase.execSQL(updateQuery);
        }catch(Exception e){
            Log.d("",e.toString());
        } finally {
            DBConnectionClose();
        }
    }

    public void deleteSyncedDigitalAssetAnalytics(int ID){
        Cursor cursor = null;
        try {
            DBConnectionClose();
            String rawQuery = "DELETE FROM tran_Asset_Analytics_Details WHERE _ID = " + ID + "";
            DBConnectionOpen();
            mSQLiteDatabase.execSQL(rawQuery);
            DBConnectionOpen();
        }
        catch (Exception e){e.printStackTrace();}
        finally {
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
                    digitalAssets.add(da);


                    digitalAssets.add(da);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.e(e.toString(),"Error");
        } finally {

        }
        return digitalAssets;
    }

    public int getAssetSessionId(int daCode,int courseId,int sectionId) {
        int sessionId = 0;
        Cursor cursor = null;
        try {
            DBConnectionOpen();
            cursor = mSQLiteDatabase.rawQuery("SELECT SessionId FROM tran_Asset_Analytics_Details WHERE DA_Code=? And Course_ID=? And Section_Id=? ORDER BY _id DESC LIMIT 1",
                    new String[]{String.valueOf(daCode),String.valueOf(courseId),String.valueOf(sectionId)});
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                sessionId = cursor.getInt(cursor.getColumnIndex(AssetAnalyticsContract.DigitalAssetAnalyticsDetails.SESSIONID));
            }
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

    public void updateDigitalAnalyticsAfterUploaded(DigitalAssets digitalAssets) {
        try {
            DBConnectionOpen();
            mSQLiteDatabase.execSQL("Update tran_Digital_Asset_Analytics set is_Synced = 1,Synced_DateTime = '" + digitalAssets.getSyncedDateTime() + "' where _id='" + digitalAssets.getId() + "'");
        } catch (Exception e) {
            Log.d("parm updateDAQuery", e.getMessage());
        } finally {
            DBConnectionClose();
        }

    }

    public void updateCustomerFeedbackAfterUploaded(DigitalAssets digitalAssets) {
        try {
            DBConnectionOpen();
            mSQLiteDatabase.execSQL("Update tbl_EL_Customer_DA_Feedback set Is_Synched = 1 where _id='" + digitalAssets.getId() + "'");
        } catch (Exception e) {
            Log.d("parm updateDAQuery", e.getMessage());
        } finally {
            DBConnectionClose();
        }

    }


    public interface GetDA {
        void getDASuccess(ArrayList<DigitalAssets> digitalAssets);

        void getDAFailure(String message);
    }

    public void setmGetDA(GetDA getDA) {
        mGetDA = getDA;
    }


}
