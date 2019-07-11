package com.swaas.kangle.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.swaas.kangle.LPCourse.CourseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit.Retrofit;

/**
 * Created by Sayellessh on 01-08-2018.
 */

public class CourseListTempRepository {

    public static final String TABLE_COURSE_TEMP_TABLE = "tbl_TEMP_COURSELIST";

    public static final String SlNo= "SlNo";
    public static final String Course_Id = "Course_Id";
    public static final String Course_Name = "Course_Name";
    public static final String Course_Type = "Course_Type";
    public static final String Course_Description = "Course_Description";
    public static final String Course_Category_Id = "Course_Category_Id";
    public static final String Course_Tags = "Course_Tags";
    public static final String Course_Image_URL = "Course_Image_URL";
    public static final String Category_Name = "Category_Name";
    public static final String Valid_From = "Valid_From";
    public static final String Valid_To= "Valid_To";

    public static final String Publish_Id = "Publish_Id";
    public static final String Course_Status_Value = "Course_Status_Value";
    public static final String Course_Status_String = "Course_Status_String";
    public static final String No_Of_Sections_Completed = "No_Of_Sections_Completed";
    public static final String Course_User_Assignment_Id = "Course_User_Assignment_Id";
    public static final String Valid_From_String = "Valid_From_String";
    public static final String Valid_To_String = "Valid_To_String";
    public static final String Company_Id = "Company_Id";
    public static final String Section_Id = "Section_Id";
    public static final String Course_Attempts_Count = "Course_Attempts_Count";
    public static final String Section_Attempts_Count = "Section_Attempts_Count";
    public static final String Total_Sections = "Total_Sections";
    public static final String Prerequisite = "Prerequisite";

    private Context mContext;

    private GetCourselistDataCBListner getChecklistDataCBListner;

    private DatabaseHandler dbHandler = null;

    private SQLiteDatabase database = null;
    private Retrofit retrofit;

    public CourseListTempRepository(Context context) {
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
        String CREATE_TABLE=" CREATE TABLE IF NOT EXISTS " +TABLE_COURSE_TEMP_TABLE+" ( "+ SlNo +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +Course_Id+ " INT ,"+ Course_Name+" TEXT, "+Course_Type+" TEXT, "+Course_Category_Id+" INT,"
                + Course_Description +" TEXT ,"+Section_Id+" INT, "+Course_Status_Value+" INT, "
                +Course_Tags+" TEXT, "+Course_Image_URL+" TEXT," +Category_Name +" TEXT, "+No_Of_Sections_Completed+" INT,"
                +Valid_From+" TEXT, "+Valid_To+" TEXT, " +Publish_Id+" INT, "+Course_Status_String+" TEXT, "
                +Valid_From_String+" TEXT, " +Valid_To_String+" TEXT, "+Course_User_Assignment_Id+" INT, "
                +Company_Id+" INT, "+Course_Attempts_Count+" INT,"+ Section_Attempts_Count+" INT ,"
                +Prerequisite+" TEXT, "+ Total_Sections+" INT )";
        return CREATE_TABLE;
    }


    public void courseListBulkInsert(List<CourseModel> cslists) {
        DBConnectionOpen();
        try {
            database.delete(TABLE_COURSE_TEMP_TABLE, null, null);
            ContentValues contentValues = new ContentValues();

            for (CourseModel cuslist : cslists) {
                contentValues.clear();
                contentValues.put(Course_Id, cuslist.getCourse_Id());
                contentValues.put(Course_Name, cuslist.getCourse_Name());
                contentValues.put(Course_Type,cuslist.getCourse_Type());
                contentValues.put(Course_Description,cuslist.getCourse_Description());
                contentValues.put(Course_Category_Id, cuslist.getCourse_Category_Id());
                contentValues.put(Course_Tags,cuslist.getCourse_Tags());
                contentValues.put(Course_Image_URL, cuslist.getCourse_Image_URL());
                contentValues.put(Category_Name, cuslist.getCategory_Name());
                contentValues.put(Valid_From,cuslist.getValid_From());
                contentValues.put(Valid_To, cuslist.getValid_To());
                contentValues.put(Publish_Id, cuslist.getPublish_Id());
                contentValues.put(Course_Status_Value, cuslist.getCourse_Status_Value());
                contentValues.put(Course_Status_String, cuslist.getCourse_Status_String());
                contentValues.put(No_Of_Sections_Completed,cuslist.getNo_Of_Sections_Completed());
                contentValues.put(Course_User_Assignment_Id,cuslist.getCourse_User_Assignment_Id());
                contentValues.put(Valid_From_String, cuslist.getValid_From());
                contentValues.put(Valid_To_String,cuslist.getValid_To());
                contentValues.put(Company_Id, cuslist.getCompany_Id());
                contentValues.put(Section_Id, cuslist.getSection_Id());
                contentValues.put(Course_Attempts_Count, cuslist.getCourse_Attempts_Count());
                contentValues.put(Section_Attempts_Count,cuslist.getSection_Attempts_Count());
                contentValues.put(Total_Sections, cuslist.getTotal_Sections());
                contentValues.put(Prerequisite, cuslist.getPrerequisite());

                database.insert(TABLE_COURSE_TEMP_TABLE, null, contentValues);
            }

        } finally {
            DBConnectionClose();
        }
    }


    public List<CourseModel> getAllCourses(){
        List<CourseModel> listOfCourses = null;
        try{
            String query ="select tbl_TEMP_COURSELIST.Course_Id," +
                    "tbl_TEMP_COURSELIST.Course_Name," +
                    "tbl_TEMP_COURSELIST.Course_Type," +
                    "tbl_TEMP_COURSELIST.Course_Description," +
                    "tbl_TEMP_COURSELIST.Course_Category_Id," +
                    "tbl_TEMP_COURSELIST.Course_Tags,"+
                    "tbl_TEMP_COURSELIST.Course_Image_URL," +
                    "tbl_TEMP_COURSELIST.Category_Name," +
                    "tbl_TEMP_COURSELIST.Valid_From," +
                    "tbl_TEMP_COURSELIST.Valid_To, " +

                    "tbl_TEMP_COURSELIST.Publish_Id, "+
                    "tbl_TEMP_COURSELIST.Course_Status_Value, "+
                    "tbl_TEMP_COURSELIST.Course_Status_String, "+
                    "tbl_TEMP_COURSELIST.No_Of_Sections_Completed, "+
                    "tbl_TEMP_COURSELIST.Course_User_Assignment_Id, "+
                    "tbl_TEMP_COURSELIST.Valid_From_String, "+
                    "tbl_TEMP_COURSELIST.Valid_To_String, "+
                    "tbl_TEMP_COURSELIST.Company_Id, "+
                    "tbl_TEMP_COURSELIST.Section_Id, "+
                    "tbl_TEMP_COURSELIST.Course_Attempts_Count, "+

                    "tbl_TEMP_COURSELIST.Section_Attempts_Count,"+
                    "tbl_TEMP_COURSELIST.Total_Sections," +
                    "tbl_TEMP_COURSELIST.Prerequisite "+
                    "from tbl_TEMP_COURSELIST";
            DBConnectionOpen();
            Cursor accompanistCursor = database.rawQuery(query, null);
            listOfCourses = getAllAssetsFromCursor(accompanistCursor);
            accompanistCursor.close();
            //getAccompanistDataAPIListnerCB.getAccompanistDataSuccessCB(accompanists);
            Log.d("parm getAllAccompanistList  size >>> ", String.valueOf(listOfCourses.size()));
        }catch (Exception e){
            Log.d("parm exception getAllAccompanistList  ",e.toString());
            //getAccompanistDataAPIListnerCB.getAccompanistDataFailureCB("Error Occured");
        }finally {
            DBConnectionClose();
        }

        return listOfCourses;
    }

    public List<CourseModel> getFilteredCourses(List<Integer> CRSIDs){
        List<CourseModel> listOfCourses = null;
        try{
            String query ="select tbl_TEMP_COURSELIST.Course_Id," +
                    "tbl_TEMP_COURSELIST.Course_Name," +
                    "tbl_TEMP_COURSELIST.Course_Type," +
                    "tbl_TEMP_COURSELIST.Course_Description," +
                    "tbl_TEMP_COURSELIST.Course_Category_Id," +
                    "tbl_TEMP_COURSELIST.Course_Tags,"+
                    "tbl_TEMP_COURSELIST.Course_Image_URL," +
                    "tbl_TEMP_COURSELIST.Category_Name," +
                    "tbl_TEMP_COURSELIST.Valid_From," +
                    "tbl_TEMP_COURSELIST.Valid_To, " +

                    "tbl_TEMP_COURSELIST.Publish_Id, "+
                    "tbl_TEMP_COURSELIST.Course_Status_Value, "+
                    "tbl_TEMP_COURSELIST.Course_Status_String, "+
                    "tbl_TEMP_COURSELIST.No_Of_Sections_Completed, "+
                    "tbl_TEMP_COURSELIST.Course_User_Assignment_Id, "+
                    "tbl_TEMP_COURSELIST.Valid_From_String, "+
                    "tbl_TEMP_COURSELIST.Valid_To_String, "+
                    "tbl_TEMP_COURSELIST.Company_Id, "+
                    "tbl_TEMP_COURSELIST.Section_Id, "+
                    "tbl_TEMP_COURSELIST.Course_Attempts_Count, "+

                    "tbl_TEMP_COURSELIST.Section_Attempts_Count,"+
                    "tbl_TEMP_COURSELIST.Total_Sections, " +
                    "tbl_TEMP_COURSELIST.Prerequisite "+
                    "from tbl_TEMP_COURSELIST where tbl_TEMP_COURSELIST.Course_Id in("+CRSIDs+")";
            DBConnectionOpen();
            Cursor accompanistCursor = database.rawQuery(query, null);
            listOfCourses = getAllAssetsFromCursor(accompanistCursor);
            accompanistCursor.close();
            //getAccompanistDataAPIListnerCB.getAccompanistDataSuccessCB(accompanists);
            Log.d("parm getAllAccompanistList  size >>> ", String.valueOf(listOfCourses.size()));
        }catch (Exception e){
            Log.d("parm exception getAllAccompanistList  ",e.toString());
            //getAccompanistDataAPIListnerCB.getAccompanistDataFailureCB("Error Occured");
        }finally {
            DBConnectionClose();
        }

        return listOfCourses;
    }


    //Cursor section
    private List<CourseModel> getAllAssetsFromCursor(Cursor assetCursor) {

        List<CourseModel> courseModelArrayList = new ArrayList<CourseModel>();
        if (assetCursor != null && assetCursor.getCount() > 0) {
            assetCursor.moveToFirst();
            int id = assetCursor.getColumnIndex(Course_Id);
            int name = assetCursor.getColumnIndex(Course_Name);
            int cType = assetCursor.getColumnIndex(Course_Type);
            int courseDes = assetCursor.getColumnIndex(Course_Description);
            int ccid = assetCursor.getColumnIndex(Course_Category_Id);
            int ct = assetCursor.getColumnIndex(Course_Tags);
            int cim = assetCursor.getColumnIndex(Course_Image_URL);
            int ctn = assetCursor.getColumnIndex(Category_Name);
            int vf = assetCursor.getColumnIndex(Valid_From);
            int vt = assetCursor.getColumnIndex(Valid_To);

            int pi = assetCursor.getColumnIndex(Publish_Id);
            int csv = assetCursor.getColumnIndex(Course_Status_Value);
            int css = assetCursor.getColumnIndex(Course_Status_String);
            int nsc = assetCursor.getColumnIndex(No_Of_Sections_Completed);
            int cuai = assetCursor.getColumnIndex(Course_User_Assignment_Id);
            int vfs = assetCursor.getColumnIndex(Valid_From_String);
            int vts = assetCursor.getColumnIndex(Valid_To_String);
            int ci = assetCursor.getColumnIndex(Company_Id);
            int si = assetCursor.getColumnIndex(Section_Id);
            int cac = assetCursor.getColumnIndex(Course_Attempts_Count);

            int sac = assetCursor.getColumnIndex(Section_Attempts_Count);
            int ts = assetCursor.getColumnIndex(Total_Sections);
            int pr = assetCursor.getColumnIndex(Prerequisite);

            do {
                CourseModel courseModel = new CourseModel();
                courseModel.setCourse_Id(assetCursor.getInt(id));
                courseModel.setCourse_Name(assetCursor.getString(name));
                courseModel.setCourse_Type(assetCursor.getString(cType));
                courseModel.setCourse_Description(assetCursor.getString(courseDes));
                courseModel.setCourse_Category_Id(assetCursor.getString(ccid));
                courseModel.setCourse_Tags(assetCursor.getString(ct));
                courseModel.setCourse_Image_URL(assetCursor.getString(cim));
                courseModel.setCategory_Name(assetCursor.getString(ctn));
                courseModel.setValid_From(assetCursor.getString(vf));
                courseModel.setValid_To(assetCursor.getString(vt));

                courseModel.setPublish_Id(assetCursor.getInt(pi));
                courseModel.setCourse_Status_Value(assetCursor.getInt(csv));
                courseModel.setCourse_Status_String(assetCursor.getString(css));
                courseModel.setNo_Of_Sections_Completed(assetCursor.getInt(nsc));
                courseModel.setCourse_User_Assignment_Id(assetCursor.getInt(cuai));
                courseModel.setValid_From_String(assetCursor.getString(vfs));
                courseModel.setValid_To_String(assetCursor.getString(vts));
                courseModel.setCompany_Id(assetCursor.getInt(ci));
                courseModel.setSection_Id(assetCursor.getInt(si));
                courseModel.setCourse_Attempts_Count(assetCursor.getInt(cac));

                courseModel.setSection_Attempts_Count(assetCursor.getInt(sac));
                courseModel.setTotal_Sections(assetCursor.getInt(ts));
                courseModel.setPrerequisite(assetCursor.getString(pr));

                courseModelArrayList.add(courseModel);
            } while (assetCursor.moveToNext());
        }
        return courseModelArrayList;
    }


    public interface GetCourselistDataCBListner {
        void GetChecklistDataSuccessCB(List<CourseModel> customers);
        void GetChecklistDataFailureCB(String message);
    }

    public void setGetChecklistDataCBListner(GetCourselistDataCBListner getCustomerDataCBListner) {
        this.getChecklistDataCBListner = getCustomerDataCBListner;
    }

}
