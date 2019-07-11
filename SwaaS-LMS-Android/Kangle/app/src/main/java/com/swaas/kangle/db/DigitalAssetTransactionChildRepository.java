package com.swaas.kangle.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.swaas.kangle.models.DigitalAssetTransactionChild;
import com.swaas.kangle.playerPart.DigitalAssets;

import java.util.ArrayList;
import java.util.List;

import retrofit.Retrofit;

/**
 * Created by Sayellessh on 13-05-2017.
 */

public class DigitalAssetTransactionChildRepository {

    public static final String TABLE_DIGITAL_ASSET_CHILD_TRANSACTION = "tbl_DIGASSETS_TRANSACTION_CHILD_MASTER";

    public static final String SlNo= "SlNo";
    public static final String DAID = "DAID";
    public static final String Play_Time = "Playtime";
    public static final String SlideNo = "SlideNo";
    public static final String User_Like = "User_Like";
    public static final String Is_Read = "Is_Read";

    public static final String RecordDate = "Recorddate";

    public static final String IsSynced = "IsSynced";

    private Context mContext;

    private DatabaseHandler dbHandler = null;

    private SQLiteDatabase database = null;
    private Retrofit retrofit;

    public DigitalAssetTransactionChildRepository(Context context) {
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
        String CREATE_TABLE=" CREATE TABLE IF NOT EXISTS " +TABLE_DIGITAL_ASSET_CHILD_TRANSACTION+" ( "+ SlNo+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +DAID+ " TEXT ,"+Play_Time+" TEXT ,"+SlideNo+" TEXT ,"
                +Is_Read+" INT, "+User_Like+" INT, "+IsSynced+" INT )";
        return CREATE_TABLE;
    }

    public void bulkInsert(List<DigitalAssetTransactionChild> data){
        for(DigitalAssetTransactionChild digassets : data){
            //insertRecord(digassets);
        }
    }

    public void insertRecord(DigitalAssets data){
        try {
            int valueread = 0;
            if(data.getIsPreview() == 1){
                valueread = 1;
            }
            DBConnectionOpen();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DAID, data.getDA_Code());
            contentValues.put(Is_Read, valueread);
            contentValues.put(Play_Time,data.getPlayed_Time_Duration());
            contentValues.put(User_Like, data.getUser_Like());
            contentValues.put(SlideNo,data.getPart_Id());
            contentValues.put(IsSynced, data.getIsSynced());
            contentValues.put(RecordDate,data.getDetailed_StartTime());

            long rowCount = database.insert(TABLE_DIGITAL_ASSET_CHILD_TRANSACTION, null, contentValues);
            Log.d("rowcount",String.valueOf(rowCount));
        } catch (Exception e) {
            Log.e("Error inserting child records",e.toString());
        } finally {
            DBConnectionClose();
        }
    }

    public List<DigitalAssetTransactionChild> getAllValues(){
        List<DigitalAssetTransactionChild> digitalAssetTransactionMasters = new ArrayList<DigitalAssetTransactionChild>();

        try{
            String query ="select tbl_DIGASSETS_TRANSACTION_CHILD_MASTER.SlNo, " +
                    "tbl_DIGASSETS_TRANSACTION_CHILD_MASTER.DAID," +
                    "tbl_DIGASSETS_TRANSACTION_CHILD_MASTER.Playtime," +
                    "tbl_DIGASSETS_TRANSACTION_CHILD_MASTER.SlideNo,"+
                    "tbl_DIGASSETS_TRANSACTION_CHILD_MASTER.User_Like,"+
                    "tbl_DIGASSETS_TRANSACTION_CHILD_MASTER.Recorddate,"+
                    "tbl_DIGASSETS_TRANSACTION_CHILD_MASTER.Is_Read from tbl_DIGASSETS_TRANSACTION_CHILD_MASTER " +
                    "WHERE tbl_DIGASSETS_TRANSACTION_CHILD_MASTER.IsSynced = 0";

            DBConnectionOpen();
            Cursor trackingCursor = database.rawQuery(query, null);
            digitalAssetTransactionMasters = getAllFromCursor(trackingCursor);
            trackingCursor.close();
        }catch (Exception e){
            Log.d("parm exception getAllAccompanistList  ",e.toString());
            //getAccompanistDataAPIListnerCB.getAccompanistDataFailureCB("Error Occured");
        }finally {
            DBConnectionClose();
        }

        return digitalAssetTransactionMasters;
    }

    private List<DigitalAssetTransactionChild> getAllFromCursor(Cursor trackCursor) {

        List<DigitalAssetTransactionChild> trackingModelList = new ArrayList<DigitalAssetTransactionChild>();
        boolean value = false;
        if (trackCursor != null && trackCursor.getCount() > 0) {
            trackCursor.moveToFirst();
            int slno = trackCursor.getColumnIndex(SlNo);
            int assetid = trackCursor.getColumnIndex(DAID);
            int play = trackCursor.getColumnIndex(Play_Time);
            int slide = trackCursor.getColumnIndex(SlideNo);
            int liked = trackCursor.getColumnIndex(User_Like);
            int recdate = trackCursor.getColumnIndex(RecordDate);
            int isread = trackCursor.getColumnIndex(Is_Read);

            do {
                DigitalAssetTransactionChild trackingModel = new DigitalAssetTransactionChild();
                trackingModel.setSlNo(trackCursor.getInt(slno));
                trackingModel.setDAID(trackCursor.getString(assetid));
                trackingModel.setPlaytime(Long.parseLong(trackCursor.getString(play)));
                trackingModel.setSlideNo(trackCursor.getInt(slide));
                trackingModel.setLiked(trackCursor.getInt(liked));
                trackingModel.setRecordDate(trackCursor.getString(recdate));
                if(trackCursor.getInt(isread)==1){
                    value = true;
                }
                trackingModel.setRead(value);

                trackingModelList.add(trackingModel);
            } while (trackCursor.moveToNext());
        }
        return trackingModelList;
    }

    public void deleteRecord(int slno){
        try {
            DBConnectionClose();
            String rawQuery = "DELETE FROM tbl_DIGASSETS_TRANSACTION_CHILD_MASTER WHERE " + SlNo + "= '" + slno + "'";
            DBConnectionOpen();
            database.execSQL(rawQuery);
        }catch (Exception e){

        }finally {
            DBConnectionClose();
        }
    }
    public void deleteAssetChildTransaction(){
        try{
            DBConnectionOpen();
            database.delete(TABLE_DIGITAL_ASSET_CHILD_TRANSACTION, null, null);
        }catch (Exception e){

        }finally {
            DBConnectionClose();
        }

    }
}
