package com.swaas.kangle.db.Filters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.swaas.kangle.CheckList.model.CheckListModel;
import com.swaas.kangle.db.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit.Retrofit;

/**
 * Created by Sayellessh on 31-07-2018.
 */

public class ChecklistCatTagFilterRepository {

    public static final String TABLE_CHECKLIST_CAT_TAG = "tbl_CHECKLIST_CAT_TAG_MASTER";

    public static final String SlNo= "SlNo";
    public static final String Checklist_Id = "Checklist_Id";
    public static final String Checklist_Frequency_Id = "Checklist_Frequency_Id";
    public static final String Checklist_Category_Id = "Checklist_Category_Id";
    public static final String Checklist_Tags = "Checklist_Tags";
    public static final String Category_Name = "Category_Name";
    public static final String Tags= "Tags";
    public static final String Checklist_Type_ID = "Checklist_Type_ID";

    public static final String Checklist_Classification = "Checklist_Classification";

    private Context mContext;

    private DatabaseHandler dbHandler = null;

    private SQLiteDatabase database = null;
    private Retrofit retrofit;

    public ChecklistCatTagFilterRepository(Context context) {
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
        String CREATE_TABLE=" CREATE TABLE IF NOT EXISTS " +TABLE_CHECKLIST_CAT_TAG+" ( "+ SlNo+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +Checklist_Id+ " INT ,"+Tags+" TEXT, " +Checklist_Frequency_Id+" INT, "+Checklist_Category_Id+" TEXT, "
                + Checklist_Tags +" TEXT, "+Checklist_Type_ID+ " INT ,"+Checklist_Classification+ " INT ,"
                +Category_Name+" TEXT )";
        return CREATE_TABLE;
    }


    public void catTagsBulkInsert(List<CheckListModel> assets) {
        DBConnectionOpen();
        try {
            database.delete(TABLE_CHECKLIST_CAT_TAG, null, null);
            ContentValues assetContentValues = new ContentValues();

            for (CheckListModel asset : assets) {
                assetContentValues.clear();
                assetContentValues.put(Checklist_Id, asset.getChecklist_Id());
                assetContentValues.put(Tags, asset.getTags());
                assetContentValues.put(Checklist_Frequency_Id, asset.getChecklist_Frequency_Id());
                assetContentValues.put(Checklist_Category_Id,asset.getChecklist_Category_Id());
                assetContentValues.put(Checklist_Tags, asset.getChecklist_Tags());
                assetContentValues.put(Category_Name, asset.getCategory_Name());
                assetContentValues.put(Checklist_Type_ID,asset.getChecklist_Type_ID());
                assetContentValues.put(Checklist_Classification,asset.getChecklist_Classification());

                database.insert(TABLE_CHECKLIST_CAT_TAG, null, assetContentValues);
            }

        } finally {
            DBConnectionClose();
        }
    }


    public List<CheckListModel> getAllCategory(String CATS,int type,int frequency){
        List<CheckListModel> category = null;
        try{
            String query ="select tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name FROM tbl_CHECKLIST_CAT_TAG_MASTER where " +
                    "tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name not in ("+CATS+") " +
                    " AND tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Classification != 2 "+
                    " AND tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Frequency_Id = "+frequency+
                    " AND tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Type_ID = "+type+ " Group By tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name ";
            DBConnectionOpen();
            Cursor catCursor = database.rawQuery(query, null);
            category = getAllCategoriesFromCursor(catCursor);
            catCursor.close();
            Log.d("parm getAllAccompanistList  size >>> ", String.valueOf(category.size()));
        }catch (Exception e){
            Log.d("parm exception getAllAccompanistList  ",e.toString());
        }finally {
            DBConnectionClose();
        }

        return category;
    }

    public List<CheckListModel> getAllTags(String TAGS,int type,int frequency){
        List<CheckListModel> Tags = null;
        try{
            String query ="select tbl_CHECKLIST_CAT_TAG_MASTER.Tags FROM tbl_CHECKLIST_CAT_TAG_MASTER where " +
                    "tbl_CHECKLIST_CAT_TAG_MASTER.Tags not null AND "+
                    "tbl_CHECKLIST_CAT_TAG_MASTER.Tags not in ("+TAGS+") " +
                    " AND tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Classification != 2 "+
                    " AND tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Frequency_Id = "+frequency+
                    " AND tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Type_ID = "+type+ " Group By tbl_CHECKLIST_CAT_TAG_MASTER.Tags ";
            DBConnectionOpen();
            Cursor catCursor = database.rawQuery(query, null);
            Tags = getAllTagsFromCursor(catCursor);
            catCursor.close();
            Log.d("parm size >>> ", String.valueOf(Tags.size()));
        }catch (Exception e){
            Log.d("parm exception  ",e.toString());
        }finally {
            DBConnectionClose();
        }

        return Tags;
    }

    public List<CheckListModel> getAllCoursechecklistCategory(String CATS){
        List<CheckListModel> category = null;
        try{
            String query ="select tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name FROM tbl_CHECKLIST_CAT_TAG_MASTER where " +
                    "tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name not in ("+CATS+") "+
                    " AND tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Classification = 2 "+
                    " Group By tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name ";
            DBConnectionOpen();
            Cursor catCursor = database.rawQuery(query, null);
            category = getAllCategoriesFromCursor(catCursor);
            catCursor.close();
            Log.d("parm getAllAccompanistList  size >>> ", String.valueOf(category.size()));
        }catch (Exception e){
            Log.d("parm exception getAllAccompanistList  ",e.toString());
        }finally {
            DBConnectionClose();
        }

        return category;
    }

    public List<CheckListModel> getAllCoursechecklistTags(String TAGS){
        List<CheckListModel> Tags = null;
        try{
            String query ="select tbl_CHECKLIST_CAT_TAG_MASTER.Tags FROM tbl_CHECKLIST_CAT_TAG_MASTER where " +
                    "tbl_CHECKLIST_CAT_TAG_MASTER.Tags not null AND "+
                    "tbl_CHECKLIST_CAT_TAG_MASTER.Tags not in ("+TAGS+") "+
                    " AND tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Classification = 2 "+
                    " Group By tbl_CHECKLIST_CAT_TAG_MASTER.Tags ";
            DBConnectionOpen();
            Cursor catCursor = database.rawQuery(query, null);
            Tags = getAllTagsFromCursor(catCursor);
            catCursor.close();
            Log.d("parm size >>> ", String.valueOf(Tags.size()));
        }catch (Exception e){
            Log.d("parm exception  ",e.toString());
        }finally {
            DBConnectionClose();
        }

        return Tags;
    }

    public List<CheckListModel> getCategorybySelectedTags(String Query,String existing,int type,int frequency) {
        List<CheckListModel> digitalAssetsMastersTags = null;
        try{
            String query ="select tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name FROM tbl_CHECKLIST_CAT_TAG_MASTER where " +
                    "tbl_CHECKLIST_CAT_TAG_MASTER.Tags in ("+Query+") AND tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Frequency_Id = "+frequency+
                    " AND tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name not in ("+existing+") AND tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Type_ID = "+type+
                    " Group By tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name ";
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

    public List<CheckListModel> getTagsbySelectedcategorey(String Query,String existing,int type,int frequency) {
        List<CheckListModel> digitalAssetsMastersTags = null;
        try{
            String query ="select tbl_CHECKLIST_CAT_TAG_MASTER.Tags FROM tbl_CHECKLIST_CAT_TAG_MASTER where " +
                    "tbl_CHECKLIST_CAT_TAG_MASTER.Category_Name in ("+Query+") AND tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Frequency_Id = "+frequency +
                    " AND tbl_CHECKLIST_CAT_TAG_MASTER.Tags not null "+
                    " AND tbl_CHECKLIST_CAT_TAG_MASTER.Tags not in ("+existing+") AND tbl_CHECKLIST_CAT_TAG_MASTER.Checklist_Type_ID = "+type+
                    " Group By tbl_CHECKLIST_CAT_TAG_MASTER.Tags ";
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

    public List<CheckListModel> getCCCategorybySelectedTags(String Query) {
        List<CheckListModel> digitalAssetsMastersTags = null;
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

    public List<CheckListModel> getCCTagsbySelectedcategorey(String Query) {
        List<CheckListModel> digitalAssetsMastersTags = null;
        try{
            String query = Query;
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

    public List<CheckListModel> getAssetIdbySelectedCatTag(String Query) {
        List<CheckListModel> digitalAssetsMastersTags = null;
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

    private List<CheckListModel> getAllCategoriesFromCursor(Cursor assetCursor) {
        List<CheckListModel> assetcategories = new ArrayList<CheckListModel>();

        if (assetCursor != null && assetCursor.getCount() > 0) {
            assetCursor.moveToFirst();
            int cat = assetCursor.getColumnIndex(Category_Name);

            do {
                CheckListModel assetsMaster = new CheckListModel();
                assetsMaster.setCategory_Name(assetCursor.getString(cat));

                assetcategories.add(assetsMaster);
            } while (assetCursor.moveToNext());
        }

        return assetcategories;
    }

    private List<CheckListModel> getAllTagsFromCursor(Cursor assetCursor) {
        List<CheckListModel> assettagss = new ArrayList<CheckListModel>();

        if (assetCursor != null && assetCursor.getCount() > 0) {
            assetCursor.moveToFirst();
            int tag = assetCursor.getColumnIndex(Tags);

            do {
                CheckListModel assetsMaster = new CheckListModel();
                assetsMaster.setTags(assetCursor.getString(tag));

                assettagss.add(assetsMaster);
            } while (assetCursor.moveToNext());
        }

        return assettagss;
    }

    private List<CheckListModel> getAllAssetIdFromCursor(Cursor assetCursor) {
        List<CheckListModel> assettagss = new ArrayList<CheckListModel>();

        if (assetCursor != null && assetCursor.getCount() > 0) {
            assetCursor.moveToFirst();
            int ID = assetCursor.getColumnIndex(Checklist_Id);

            do {
                CheckListModel assetsMaster = new CheckListModel();
                assetsMaster.setChecklist_Id(assetCursor.getInt(ID));

                assettagss.add(assetsMaster);
            } while (assetCursor.moveToNext());
        }

        return assettagss;
    }

    public void deleteChecklistCatTag(){
        try{
            DBConnectionOpen();
            database.delete(TABLE_CHECKLIST_CAT_TAG, null, null);
        }catch (Exception e){

        }finally {
            DBConnectionClose();
        }

    }
}
