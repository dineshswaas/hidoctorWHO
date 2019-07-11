package com.swaas.kangle.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.swaas.kangle.models.LstAssetImageModel;

import java.util.ArrayList;
import java.util.List;

import retrofit.Retrofit;

/**
 * Created by Sayellessh on 22-05-2017.
 */

public class DigitalAssetOfflineChildRepository  {

    public static final String TABLE_DIGITAL_ASSET_OFFLINE_CHILD = "tbl_DIGASSETS_OFFLINE_CHILD";

    public static final String SlNo= "SlNo";
    public static final String Company_Id = "Company_Id";
    public static final String DA_Code = "DA_Code";
    public static final String Image_Url = "Image_Url";
    public static final String Offline_URL = "Offline_URL";
    public static final String Image_Name = "Image_Name";
    public static final String Image_Id = "Image_Id";

    private Context mContext;

    private DatabaseHandler dbHandler = null;

    private SQLiteDatabase database = null;
    private Retrofit retrofit;

    public DigitalAssetOfflineChildRepository(Context context) {
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

    public static String Create() {
        String CREATE_TABLE=" CREATE TABLE IF NOT EXISTS " +TABLE_DIGITAL_ASSET_OFFLINE_CHILD+" ( "+ SlNo+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Company_Id + " INT ," + DA_Code + " TEXT ," + Offline_URL + " TEXT ," +Image_Url + " TEXT , " + Image_Name+" TEXT )";
        return CREATE_TABLE;
    }

    public boolean insertOrUpdate(LstAssetImageModel assets){
        boolean newasset = false;
        if(getIDValue(assets.getDA_Code()) == null || getIDValue(assets.getDA_Code()).size() == 0 ){
            newasset = true;
        } else {
            newasset = false;
        }
        return newasset;
    }

    public void bulkinsert(ArrayList<LstAssetImageModel> assets){
        for(LstAssetImageModel asset : assets){
            DBConnectionOpen();
            try{
                ContentValues assetContentValues = new ContentValues();

                assetContentValues.clear();
                assetContentValues.put(Company_Id, asset.getCompany_Id());
                assetContentValues.put(DA_Code, asset.getDA_Code());
                assetContentValues.put(Image_Url,asset.getImage_Url());
                assetContentValues.put(Image_Name, asset.getImage_Name());
                assetContentValues.put(Offline_URL, asset.getOffline_URL());
                assetContentValues.put(Image_Id,asset.getImage_Id());

                database.insert(TABLE_DIGITAL_ASSET_OFFLINE_CHILD , null, assetContentValues);

            } catch(Exception e){
                Log.d("Errorinsertingchildtable",e.toString());
            }finally {
                DBConnectionClose();
            }
        }
    }

    public void assetsOfflineInsert(LstAssetImageModel asset) {
        DBConnectionOpen();
        try{
            ContentValues assetContentValues = new ContentValues();

            assetContentValues.clear();
            assetContentValues.put(Company_Id, asset.getCompany_Id());
            assetContentValues.put(DA_Code, asset.getDA_Code());
            assetContentValues.put(Image_Url,asset.getImage_Url());
            assetContentValues.put(Image_Name, asset.getImage_Name());
            assetContentValues.put(Offline_URL, asset.getOffline_URL());
            assetContentValues.put(Image_Id,asset.getImage_Id());

            database.insert(TABLE_DIGITAL_ASSET_OFFLINE_CHILD , null, assetContentValues);

        } catch(Exception e){
            Log.d("Errorinsertingchildtable",e.toString());
        }finally {
            DBConnectionClose();
        }
    }

    public List<String> getIDValue(String data) {
        List<String> value = new ArrayList<String>();
        try{
            DBConnectionOpen();
            String selectQuery = " SELECT tbl_DIGASSETS_OFFLINE_CHILD.DA_Code FROM tbl_DIGASSETS_OFFLINE_CHILD WHERE tbl_DIGASSETS_OFFLINE_CHILD.DA_Code='"+data+"'";
            Cursor cursor = database.rawQuery(selectQuery, null);
            if(cursor!= null && cursor.getCount()>0) {
                cursor.moveToFirst();
                value.add(cursor.getString(cursor.getColumnIndex(DA_Code)));
                //value =  asset.getDAID();
            } else {
                Log.d("DateCount",0+"" );
                value =  null ;
            }
            cursor.close();
        }catch(Exception e){
            Log.d("CursorError",e.toString());
        }finally {
            DBConnectionClose();
        }
        return value;
    }


    public ArrayList<LstAssetImageModel> getofflineAssetsByDACode (String dacode){
        ArrayList<LstAssetImageModel> assetsByCategory = null;
        try{
            String query ="select tbl_DIGASSETS_OFFLINE_CHILD.DA_Code, " +
                    "tbl_DIGASSETS_OFFLINE_CHILD.Company_Id, " +
                    "tbl_DIGASSETS_OFFLINE_CHILD.Image_Url, " +
                    "tbl_DIGASSETS_OFFLINE_CHILD.Offline_URL, " +
                    "tbl_DIGASSETS_OFFLINE_CHILD.Image_Id,"+
                    "tbl_DIGASSETS_OFFLINE_CHILD.Image_Name " +
                    "from tbl_DIGASSETS_OFFLINE_CHILD WHERE tbl_DIGASSETS_OFFLINE_CHILD.DA_Code ='" + dacode +"'";
            DBConnectionOpen();
            Cursor accompanistCursor = database.rawQuery(query, null);
            assetsByCategory = getofflineAssetsByDACodeFromCursor(accompanistCursor);
            accompanistCursor.close();
            //getAccompanistDataAPIListnerCB.getAccompanistDataSuccessCB(accompanists);
            Log.d("parm getAllAccompanistList  size >>> ", String.valueOf(assetsByCategory.size()));
        }catch (Exception e){
            Log.d("parm exception getAllAccompanistList  ",e.toString());
            //getAccompanistDataAPIListnerCB.getAccompanistDataFailureCB("Error Occured");
        }finally {
            DBConnectionClose();
        }
        return assetsByCategory;
    }


    //cursorsection
    private ArrayList<LstAssetImageModel> getofflineAssetsByDACodeFromCursor(Cursor assetCursor){
        ArrayList<LstAssetImageModel> assetsMasters = new ArrayList<LstAssetImageModel>();
        boolean readvalue = false,downloadedvalue = false;
        if (assetCursor != null && assetCursor.getCount() > 0) {
            assetCursor.moveToFirst();
            int CompanyId = assetCursor.getColumnIndex(Company_Id);
            int DaCode = assetCursor.getColumnIndex(DA_Code);
            int imageurl = assetCursor.getColumnIndex(Image_Url);
            int imagename = assetCursor.getColumnIndex(Image_Name);
            int offlineUrl = assetCursor.getColumnIndex(Offline_URL);
            int imageId = assetCursor.getColumnIndex(Image_Id);

            do {
                LstAssetImageModel assetsMaster = new LstAssetImageModel();
                assetsMaster.setCompany_Id(assetCursor.getInt(CompanyId));
                assetsMaster.setDA_Code(assetCursor.getString(DaCode));
                assetsMaster.setImage_Url(assetCursor.getString(imageurl));
                assetsMaster.setImage_Name(assetCursor.getString(imagename));
                assetsMaster.setOffline_URL(assetCursor.getString(offlineUrl));
                assetsMaster.setImage_Id(assetCursor.getInt(imageId));

                assetsMasters.add(assetsMaster);
            } while (assetCursor.moveToNext());
        }
        return assetsMasters;
    }

    //delete record
    public boolean deleteExistingAsset(String data){
        boolean success = false;
        try {
            DBConnectionClose();
            String rawQuery = "DELETE FROM tbl_DIGASSETS_OFFLINE_CHILD WHERE " + DA_Code + "= '" + data + "'";
            DBConnectionOpen();
            database.execSQL(rawQuery);
            success = true;
        }catch (Exception e){
            Log.d("error",e.toString());
        }finally {
            DBConnectionClose();
        }
        return success;
    }

    public void deleteAssetOfflineChild(){
        try{
            DBConnectionOpen();
            database.delete(TABLE_DIGITAL_ASSET_OFFLINE_CHILD, null, null);
        }catch (Exception e){

        }finally {
            DBConnectionClose();
        }

    }
}
