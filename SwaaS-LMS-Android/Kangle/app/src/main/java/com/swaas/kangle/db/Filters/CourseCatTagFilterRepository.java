package com.swaas.kangle.db.Filters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.swaas.kangle.LPCourse.CourseModel;
import com.swaas.kangle.db.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit.Retrofit;

/**
 * Created by Sayellessh on 31-07-2018.
 */

public class CourseCatTagFilterRepository {

    public static final String TABLE_COURSE_CAT_TAG = "tbl_COURSE_CAT_TAG_MASTER";

    public static final String SlNo= "SlNo";
    public static final String Course_Id = "Course_Id";
    public static final String Course_Category_Id = "Course_Category_Id";
    public static final String Course_Tags = "Course_Tags";
    public static final String Category_Name = "Category_Name";
    public static final String Tags = "Tags";

    private Context mContext;

    private DatabaseHandler dbHandler = null;

    private SQLiteDatabase database = null;
    private Retrofit retrofit;

    public CourseCatTagFilterRepository(Context context) {
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
        String CREATE_TABLE=" CREATE TABLE IF NOT EXISTS " +TABLE_COURSE_CAT_TAG+" ( "+ SlNo+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +Course_Id+ " INT ,"+Course_Category_Id+" TEXT, " +Course_Tags+" TEXT, "+Category_Name+" TEXT, "+ Tags +" TEXT )";
        return CREATE_TABLE;
    }


    public void catTagsBulkInsert(List<CourseModel> crs) {
        DBConnectionOpen();
        try {
            database.delete(TABLE_COURSE_CAT_TAG, null, null);
            ContentValues assetContentValues = new ContentValues();

            for (CourseModel cm : crs) {
                assetContentValues.clear();
                assetContentValues.put(Course_Id, cm.getCourse_Id());
                assetContentValues.put(Tags, cm.getTags());
                assetContentValues.put(Course_Category_Id, cm.getCourse_Category_Id());
                assetContentValues.put(Course_Tags,cm.getCourse_Tags());
                assetContentValues.put(Category_Name, cm.getCategory_Name());

                database.insert(TABLE_COURSE_CAT_TAG, null, assetContentValues);
            }

        } finally {
            DBConnectionClose();
        }
    }


    public List<CourseModel> getAllCategory(String CATS){
        List<CourseModel> category = null;
        try{
            String query ="select tbl_COURSE_CAT_TAG_MASTER.Category_Name FROM tbl_COURSE_CAT_TAG_MASTER where " +
                    "tbl_COURSE_CAT_TAG_MASTER.Category_Name not in ("+CATS+") Group By tbl_COURSE_CAT_TAG_MASTER.Category_Name ";
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

    public List<CourseModel> getAllTags(String TAGS){
        List<CourseModel> Tags = null;
        try{
            String query ="select tbl_COURSE_CAT_TAG_MASTER.Tags FROM tbl_COURSE_CAT_TAG_MASTER where " +
                    "tbl_COURSE_CAT_TAG_MASTER.Tags not null AND "+
                    "tbl_COURSE_CAT_TAG_MASTER.Tags not in ("+TAGS+") Group By tbl_COURSE_CAT_TAG_MASTER.Tags ";
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

    public List<CourseModel> getCategorybySelectedTags(String Query,String existing) {
        List<CourseModel> digitalAssetsMastersTags = null;
        try{
            String query ="select tbl_COURSE_CAT_TAG_MASTER.Category_Name FROM tbl_COURSE_CAT_TAG_MASTER where " +
                    "tbl_COURSE_CAT_TAG_MASTER.Tags in ("+Query+") AND tbl_COURSE_CAT_TAG_MASTER.Category_Name not in "
                    +"("+existing+") Group By tbl_COURSE_CAT_TAG_MASTER.Category_Name ";
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

    public List<CourseModel> getTagsbySelectedcategorey(String Query,String existing) {
        List<CourseModel> digitalAssetsMastersTags = null;
        try{
            String query ="select tbl_COURSE_CAT_TAG_MASTER.Tags FROM tbl_COURSE_CAT_TAG_MASTER where " +
                    "tbl_COURSE_CAT_TAG_MASTER.Category_Name in ("+Query+") AND tbl_COURSE_CAT_TAG_MASTER.Tags not null AND " +
                    "tbl_COURSE_CAT_TAG_MASTER.Tags not in" +
                    "("+existing+")Group By tbl_COURSE_CAT_TAG_MASTER.Tags ";
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

    public List<CourseModel> getAssetIdbySelectedCatTag(String Query) {
        List<CourseModel> digitalAssetsMastersTags = null;
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

    private List<CourseModel> getAllCategoriesFromCursor(Cursor assetCursor) {
        List<CourseModel> assetcategories = new ArrayList<CourseModel>();

        if (assetCursor != null && assetCursor.getCount() > 0) {
            assetCursor.moveToFirst();
            int cat = assetCursor.getColumnIndex(Category_Name);

            do {
                CourseModel assetsMaster = new CourseModel();
                assetsMaster.setCategory_Name(assetCursor.getString(cat));

                assetcategories.add(assetsMaster);
            } while (assetCursor.moveToNext());
        }

        return assetcategories;
    }

    private List<CourseModel> getAllTagsFromCursor(Cursor assetCursor) {
        List<CourseModel> assettagss = new ArrayList<CourseModel>();

        if (assetCursor != null && assetCursor.getCount() > 0) {
            assetCursor.moveToFirst();
            int tag = assetCursor.getColumnIndex(Tags);

            do {
                CourseModel assetsMaster = new CourseModel();
                assetsMaster.setTags(assetCursor.getString(tag));

                assettagss.add(assetsMaster);
            } while (assetCursor.moveToNext());
        }

        return assettagss;
    }

    private List<CourseModel> getAllAssetIdFromCursor(Cursor assetCursor) {
        List<CourseModel> assettagss = new ArrayList<CourseModel>();

        if (assetCursor != null && assetCursor.getCount() > 0) {
            assetCursor.moveToFirst();
            int ID = assetCursor.getColumnIndex(Course_Id);

            do {
                CourseModel assetsMaster = new CourseModel();
                assetsMaster.setCourse_Id(assetCursor.getInt(ID));

                assettagss.add(assetsMaster);
            } while (assetCursor.moveToNext());
        }

        return assettagss;
    }

    public void deleteCourseCatTag(){
        try{
            DBConnectionOpen();
            database.delete(TABLE_COURSE_CAT_TAG, null, null);
        }catch (Exception e){

        }finally {
            DBConnectionClose();
        }

    }
}
