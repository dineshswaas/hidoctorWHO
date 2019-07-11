package com.swaas.kangle.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.swaas.kangle.models.UserTrackingModel;

import java.util.ArrayList;
import java.util.List;

import retrofit.Retrofit;

/**
 * Created by Sayellessh on 19-06-2017.
 */

public class UserTrackertableRepository {

    public static final String TABLE_USER_TRACKING = "tbl_USER_TRACKING_TABLE";

    public static final String SlNo= "SlNo";
    public static final String CompanyId = "CompanyId";
    public static final String UserId = "UserId";
    public static final String RegionCode = "RegionCode";
    public static final String Module = "Module";
    public static final String DeviceType = "DeviceType";
    public static final String DeviceModel = "DeviceModel";
    public static final String AppVersion = "AppVersion";
    public static final String Device_OS_Type = "Device_OS_Type";
    public static final String Browser = "Browser";
    public static final String OSBrowserVersion = "OSBrowserVersion";
    public static final String OSVersion = "OSVersion";
    public static final String UserAnonymous = "UserAnonymous";
    public static final String OtherData1 = "OtherData1";
    public static final String OtherData2 = "OtherData2";
    public static final String longitude = "longitude";
    public static final String lattitude = "lattitude";
    public static final String Address = "Address";

    public static final String IsSynced = "IsSynced";



    private Context mContext;

    private DatabaseHandler dbHandler = null;

    private SQLiteDatabase database = null;
    private Retrofit retrofit;

    public UserTrackertableRepository(Context context) {
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
        String CREATE_TABLE=" CREATE TABLE IF NOT EXISTS " +TABLE_USER_TRACKING+" ( "+ SlNo+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +CompanyId+ " TEXT ,"+UserId+" TEXT, "+RegionCode+" TEXT,"+ Module +" TEXT ,"+DeviceType+" TEXT ,"
                +DeviceModel+" TEXT, "+AppVersion+" TEXT, " +Device_OS_Type+" TEXT, " +Browser+" TEXT, "
                +OSBrowserVersion+" TEXT, " +OSVersion+" TEXT, " +UserAnonymous+" TEXT, "+OtherData1+" TEXT, "+OtherData2+" TEXT, "
                +longitude+" TEXT,"+lattitude+" TEXT," +Address+" TEXT, "+IsSynced+" INT )";
        return CREATE_TABLE;
    }

    public void insertTrackingData(UserTrackingModel data){
            try {
                DBConnectionOpen();
                ContentValues contentValues = new ContentValues();
                contentValues.put(CompanyId, data.getCompanyId());
                contentValues.put(UserId, data.getUserId());
                contentValues.put(RegionCode, data.getRegionCode());
                contentValues.put(Module, data.getModule());
                contentValues.put(DeviceType, data.getDeviceType());
                contentValues.put(DeviceModel, data.getDeviceModel());
                contentValues.put(AppVersion, data.getAppVersion());
                contentValues.put(Device_OS_Type, data.getDevice_OS_Type());
                contentValues.put(Browser, data.getBrowser());
                contentValues.put(OSBrowserVersion, data.getOSBrowserVersion());
                contentValues.put(OSVersion, data.getOSVersion());
                contentValues.put(UserAnonymous, data.getUserAnonymous());
                contentValues.put(OtherData1, data.getOtherData1());
                contentValues.put(OtherData2, data.getOtherData2());
                contentValues.put(longitude, data.getLongitude());
                contentValues.put(lattitude, data.getLattitude());
                contentValues.put(Address,data.getAddress());
                contentValues.put(IsSynced, data.getIsSynced());

                long rowCount = database.insert(TABLE_USER_TRACKING, null, contentValues);
                Log.d("rowcount",String.valueOf(rowCount));
            } catch (Exception e) {

            } finally {
                DBConnectionClose();
            }
        }

    public List<UserTrackingModel> getAllValues(){
        List<UserTrackingModel> digitalAssetTransactionMasters = new ArrayList<UserTrackingModel>();

        try{
            String query ="select tbl_USER_TRACKING_TABLE.SlNo, " +
                    "tbl_USER_TRACKING_TABLE.CompanyId," +
                    "tbl_USER_TRACKING_TABLE.UserId," +
                    "tbl_USER_TRACKING_TABLE.RegionCode,"+
                    "tbl_USER_TRACKING_TABLE.Module,"+
                    "tbl_USER_TRACKING_TABLE.DeviceType," +
                    "tbl_USER_TRACKING_TABLE.DeviceModel," +
                    "tbl_USER_TRACKING_TABLE.AppVersion," +
                    "tbl_USER_TRACKING_TABLE.Device_OS_Type," +
                    "tbl_USER_TRACKING_TABLE.Browser,"+
                    "tbl_USER_TRACKING_TABLE.OSBrowserVersion,"+
                    "tbl_USER_TRACKING_TABLE.OSVersion," +
                    "tbl_USER_TRACKING_TABLE.UserAnonymous," +
                    "tbl_USER_TRACKING_TABLE.OtherData1," +
                    "tbl_USER_TRACKING_TABLE.OtherData2," +
                    "tbl_USER_TRACKING_TABLE.lattitude,"+
                    "tbl_USER_TRACKING_TABLE.longitude,"+
                    "tbl_USER_TRACKING_TABLE.Address from tbl_USER_TRACKING_TABLE " +
                    "WHERE tbl_USER_TRACKING_TABLE.IsSynced = 0";
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

    public void updateRecord(int slno){

        String updateQuery = "UPDATE tbl_USER_TRACKING_TABLE SET IsSynced = 1 WHERE SlNo = " + slno+ "";
        try {
            DBConnectionOpen();
            database.execSQL(updateQuery);
        }catch(Exception e){
            Log.d("",e.toString());
        } finally {
            DBConnectionClose();
        }
    }

    public void deleteRecord(int slno){
        try {
            DBConnectionClose();
            String rawQuery = "DELETE FROM tbl_USER_TRACKING_TABLE WHERE SlNo = " + slno + "";
            DBConnectionOpen();
            database.execSQL(rawQuery);
        }catch (Exception e){

        }finally {
            DBConnectionClose();
        }
    }

    private List<UserTrackingModel> getAllFromCursor(Cursor trackCursor) {

        List<UserTrackingModel> trackingModelList = new ArrayList<UserTrackingModel>();
        if (trackCursor != null && trackCursor.getCount() > 0) {
            trackCursor.moveToFirst();
            int slno = trackCursor.getColumnIndex(SlNo);
            int companyId = trackCursor.getColumnIndex(CompanyId);
            int userId = trackCursor.getColumnIndex(UserId);
            int regionCode = trackCursor.getColumnIndex(RegionCode);
            int module = trackCursor.getColumnIndex(Module);
            int deviceType = trackCursor.getColumnIndex(DeviceType);
            int deviceModel = trackCursor.getColumnIndex(DeviceModel);
            int appVersion = trackCursor.getColumnIndex(AppVersion);
            int device_OS_Type = trackCursor.getColumnIndex(Device_OS_Type);
            int browser = trackCursor.getColumnIndex(Browser);
            int oSBrowserVersion = trackCursor.getColumnIndex(OSBrowserVersion);
            int oSVersion = trackCursor.getColumnIndex(OSVersion);
            int userAnonymous = trackCursor.getColumnIndex(UserAnonymous);
            int otherData1 = trackCursor.getColumnIndex(OtherData1);
            int otherData2 = trackCursor.getColumnIndex(OtherData2);
            int Longitude = trackCursor.getColumnIndex(longitude);
            int Lattitude = trackCursor.getColumnIndex(lattitude);
            int address = trackCursor.getColumnIndex(Address);
            do {
                UserTrackingModel trackingModel = new UserTrackingModel();
                trackingModel.setSlNo(trackCursor.getInt(slno));
                trackingModel.setCompanyId(trackCursor.getString(companyId));
                trackingModel.setUserId(trackCursor.getString(userId));
                trackingModel.setRegionCode(trackCursor.getString(regionCode));
                trackingModel.setModule(trackCursor.getString(module));
                trackingModel.setDeviceType(trackCursor.getString(deviceType));
                trackingModel.setDeviceModel(trackCursor.getString(deviceModel));
                trackingModel.setAppVersion(trackCursor.getString(appVersion));
                trackingModel.setDevice_OS_Type(trackCursor.getString(device_OS_Type));
                trackingModel.setBrowser(trackCursor.getString(browser));
                trackingModel.setOSBrowserVersion(trackCursor.getString(oSBrowserVersion));
                trackingModel.setOSVersion(trackCursor.getString(oSVersion));
                trackingModel.setUserAnonymous(trackCursor.getString(userAnonymous));
                trackingModel.setOtherData1(trackCursor.getString(otherData1));
                trackingModel.setOtherData2(trackCursor.getString(otherData2));
                trackingModel.setLattitude(trackCursor.getString(Lattitude));
                trackingModel.setLongitude(trackCursor.getString(Longitude));
                trackingModel.setAddress(trackCursor.getString(address));

                trackingModelList.add(trackingModel);
            } while (trackCursor.moveToNext());
        }
        return trackingModelList;
    }



}
