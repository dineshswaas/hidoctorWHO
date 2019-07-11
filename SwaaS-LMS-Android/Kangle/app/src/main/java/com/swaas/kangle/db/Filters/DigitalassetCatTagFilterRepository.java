package com.swaas.kangle.db.Filters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.swaas.kangle.db.DatabaseHandler;
import com.swaas.kangle.models.DigitalAssetsMaster;

import java.util.ArrayList;
import java.util.List;

import retrofit.Retrofit;

/**
 * Created by Sayellessh on 31-07-2018.
 */

public class DigitalassetCatTagFilterRepository {

    public static final String TABLE_DIGITAL_ASSET_CAT_TAG = "tbl_DIGASSETS_CAT_TAG_MASTER";

    public static final String SlNo= "SlNo";
    public static final String DAID = "DAID";
    public static final String UDTags = "UDTags";
    public static final String DACategoryCode = "DACategoryCode";
    public static final String DACategoryName = "DACategoryName";
    public static final String DA_Type = "DA_Type";
    public static final String Tags= "Tags";

    private Context mContext;

    private DatabaseHandler dbHandler = null;

    private SQLiteDatabase database = null;
    private Retrofit retrofit;

    public DigitalassetCatTagFilterRepository(Context context) {
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
        String CREATE_TABLE=" CREATE TABLE IF NOT EXISTS " +TABLE_DIGITAL_ASSET_CAT_TAG+" ( "+ SlNo+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +DAID+ " TEXT ,"+DACategoryCode+" TEXT, " +DACategoryName+" TEXT, "+UDTags+" TEXT, "+ Tags +" TEXT, "+DA_Type+" TEXT )";
        return CREATE_TABLE;
    }


    public void catTagsBulkInsert(List<DigitalAssetsMaster> assets) {
        DBConnectionOpen();
        database.beginTransaction();
        try {
            database.delete(TABLE_DIGITAL_ASSET_CAT_TAG, null, null);
            ContentValues assetContentValues = new ContentValues();

            for (DigitalAssetsMaster asset : assets) {
                assetContentValues.clear();
                assetContentValues.put(DAID, asset.getDAID());
                assetContentValues.put(Tags, asset.getTags());
                assetContentValues.put(DACategoryCode, asset.getDACategoryCode());
                assetContentValues.put(DACategoryName,asset.getDACategoryName());
                assetContentValues.put(UDTags, asset.getUDTags());
                assetContentValues.put(DA_Type, asset.getDA_Type());
                database.insert(TABLE_DIGITAL_ASSET_CAT_TAG, null, assetContentValues);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            DBConnectionClose();
        }
    }

    public List<DigitalAssetsMaster> getAllCategory(String CATS){
        List<DigitalAssetsMaster> digitalAssetsMasterscategory = null;
        try{
            String query ="select tbl_DIGASSETS_CAT_TAG_MASTER.DACategoryName FROM tbl_DIGASSETS_CAT_TAG_MASTER where " +
                    "tbl_DIGASSETS_CAT_TAG_MASTER.DACategoryName not in ("+CATS+")Group By tbl_DIGASSETS_CAT_TAG_MASTER.DACategoryName ";
            DBConnectionOpen();
            Cursor catCursor = database.rawQuery(query, null);
            digitalAssetsMasterscategory = getAllCategoriesFromCursor(catCursor);
            catCursor.close();
            //getAccompanistDataAPIListnerCB.getAccompanistDataSuccessCB(accompanists);
            Log.d("parm getAllAccompanistList  size >>> ", String.valueOf(digitalAssetsMasterscategory.size()));
        }catch (Exception e){
            Log.d("parm exception getAllAccompanistList  ",e.toString());
            //getAccompanistDataAPIListnerCB.getAccompanistDataFailureCB("Error Occured");
        }finally {
            DBConnectionClose();
        }

        return digitalAssetsMasterscategory;
    }

    public List<DigitalAssetsMaster> getAllTags(String TAGS){
        List<DigitalAssetsMaster> digitalAssetsMastersTags = null;
        try{
            String query ="select tbl_DIGASSETS_CAT_TAG_MASTER.Tags FROM tbl_DIGASSETS_CAT_TAG_MASTER where " +
                    "tbl_DIGASSETS_CAT_TAG_MASTER.Tags not in ("+TAGS+")Group By tbl_DIGASSETS_CAT_TAG_MASTER.Tags ";
            DBConnectionOpen();
            Cursor catCursor = database.rawQuery(query, null);
            digitalAssetsMastersTags = getAllTagsFromCursor(catCursor);
            catCursor.close();
            Log.d("parm size >>> ", String.valueOf(digitalAssetsMastersTags.size()));
        }catch (Exception e){
            Log.d("parm exception  ",e.toString());
        }finally {
            DBConnectionClose();
        }

        return digitalAssetsMastersTags;
    }

    public List<DigitalAssetsMaster> getAllDocTypes (String FILTER){
        List<DigitalAssetsMaster> digitalAssetsMastersTags = null;
        try{
            String query ="select tbl_DIGASSETS_CAT_TAG_MASTER.DA_Type FROM tbl_DIGASSETS_CAT_TAG_MASTER where " +
                    "tbl_DIGASSETS_CAT_TAG_MASTER.DA_Type not in ("+FILTER+")Group By tbl_DIGASSETS_CAT_TAG_MASTER.DA_Type ";
            DBConnectionOpen();
            Cursor catCursor = database.rawQuery(query, null);
            digitalAssetsMastersTags = getAllDATypeFromCursor(catCursor);
            catCursor.close();
            Log.d("parm size >>> ", String.valueOf(digitalAssetsMastersTags.size()));
        }catch (Exception e){
            Log.d("parm exception  ",e.toString());
        }finally {
            DBConnectionClose();
        }

        return digitalAssetsMastersTags;
    }

    public List<DigitalAssetsMaster> getCategorybySelectedTags(String Query) {
        List<DigitalAssetsMaster> digitalAssetsMastersTags = null;
        try{
            String query = Query;
            DBConnectionOpen();
            Cursor catCursor = database.rawQuery(query, null);
            digitalAssetsMastersTags = getAllCategoriesFromCursor(catCursor);
            catCursor.close();
            Log.d("parm size >>> ", String.valueOf(digitalAssetsMastersTags.size()));
        }catch (Exception e){
            Log.d("parm exception  ",e.toString());
        }finally {
            DBConnectionClose();
        }

        return digitalAssetsMastersTags;
    }

    public List<DigitalAssetsMaster> getTagsbySelectedcategorey(String Query) {
        List<DigitalAssetsMaster> digitalAssetsMastersTags = null;
        try{
            String query =Query;
            DBConnectionOpen();
            Cursor catCursor = database.rawQuery(query, null);
            digitalAssetsMastersTags = getAllTagsFromCursor(catCursor);
            catCursor.close();
            Log.d("parm size >>> ", String.valueOf(digitalAssetsMastersTags.size()));
        }catch (Exception e){
            Log.d("parm exception  ",e.toString());
        }finally {
            DBConnectionClose();
        }

        return digitalAssetsMastersTags;
    }

    public List<DigitalAssetsMaster> getDAtypebySelectedcategorey(String Query) {
        List<DigitalAssetsMaster> digitalAssetsMastersTags = null;
        try{
            String query =Query;
            DBConnectionOpen();
            Cursor catCursor = database.rawQuery(query, null);
            digitalAssetsMastersTags = getAllDATypeFromCursor(catCursor);
            catCursor.close();
            Log.d("parm size >>> ", String.valueOf(digitalAssetsMastersTags.size()));
        }catch (Exception e){
            Log.d("parm exception  ",e.toString());
        }finally {
            DBConnectionClose();
        }

        return digitalAssetsMastersTags;
    }

    public List<DigitalAssetsMaster> getAssetIdbySelectedCatTag(String Query) {
        List<DigitalAssetsMaster> digitalAssetsMastersTags = null;
        try{
            String query = Query;
            DBConnectionOpen();
            Cursor catCursor = database.rawQuery(query, null);
            digitalAssetsMastersTags = getAllAssetIdFromCursor(catCursor);
            catCursor.close();
            Log.d("parm size >>> ", String.valueOf(digitalAssetsMastersTags.size()));
        }catch (Exception e){
            Log.d("parm exception  ",e.toString());
        }finally {
            DBConnectionClose();
        }

        return digitalAssetsMastersTags;
    }


    //Cursor

    private List<DigitalAssetsMaster> getAllCategoriesFromCursor(Cursor assetCursor) {
        List<DigitalAssetsMaster> assetcategories = new ArrayList<DigitalAssetsMaster>();

        if (assetCursor != null && assetCursor.getCount() > 0) {
            assetCursor.moveToFirst();
            int cat = assetCursor.getColumnIndex(DACategoryName);

            do {
                DigitalAssetsMaster assetsMaster = new DigitalAssetsMaster();
                assetsMaster.setDACategoryName(assetCursor.getString(cat));

                assetcategories.add(assetsMaster);
            } while (assetCursor.moveToNext());
        }

        return assetcategories;
    }

    private List<DigitalAssetsMaster> getAllTagsFromCursor(Cursor assetCursor) {
        List<DigitalAssetsMaster> assettagss = new ArrayList<DigitalAssetsMaster>();

        if (assetCursor != null && assetCursor.getCount() > 0) {
            assetCursor.moveToFirst();
            int tag = assetCursor.getColumnIndex(Tags);

            do {
                DigitalAssetsMaster assetsMaster = new DigitalAssetsMaster();
                assetsMaster.setTags(assetCursor.getString(tag));

                assettagss.add(assetsMaster);
            } while (assetCursor.moveToNext());
        }

        return assettagss;
    }

    private List<DigitalAssetsMaster> getAllDATypeFromCursor(Cursor assetCursor){
        List<DigitalAssetsMaster> assettagss = new ArrayList<DigitalAssetsMaster>();

        if (assetCursor != null && assetCursor.getCount() > 0) {
            assetCursor.moveToFirst();
            int datype = assetCursor.getColumnIndex(DA_Type);

            do {
                DigitalAssetsMaster assetsMaster = new DigitalAssetsMaster();
                assetsMaster.setDA_Type(assetCursor.getString(datype));

                assettagss.add(assetsMaster);
            } while (assetCursor.moveToNext());
        }

        return assettagss;
    }

    private List<DigitalAssetsMaster> getAllAssetIdFromCursor(Cursor assetCursor) {
        List<DigitalAssetsMaster> assettagss = new ArrayList<DigitalAssetsMaster>();

        if (assetCursor != null && assetCursor.getCount() > 0) {
            assetCursor.moveToFirst();
            int ID = assetCursor.getColumnIndex(DAID);

            do {
                DigitalAssetsMaster assetsMaster = new DigitalAssetsMaster();
                assetsMaster.setDAID(assetCursor.getString(ID));

                assettagss.add(assetsMaster);
            } while (assetCursor.moveToNext());
        }

        return assettagss;
    }

    public void deleteDigitalassetCatTag(){
        try{
            DBConnectionOpen();
            database.delete(TABLE_DIGITAL_ASSET_CAT_TAG, null, null);
        }catch (Exception e){

        }finally {
            DBConnectionClose();
        }

    }

}
