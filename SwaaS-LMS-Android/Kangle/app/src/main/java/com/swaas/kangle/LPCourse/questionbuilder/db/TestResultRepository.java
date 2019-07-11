package com.swaas.kangle.LPCourse.questionbuilder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.swaas.kangle.LPCourse.model.TestResultModel;
import com.swaas.kangle.db.DatabaseHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Retrofit;

/**
 * Created by Sayellessh on 21-08-2017.
 */

public class TestResultRepository {

    public static final String TABLE_TEST = "tbl_TEST_MASTER";

    public static final String SlNo= "SlNo";
    public static final String TestAnswers="TestAnswers";
    public static final String marksSecured="MarksSecured";
    public static final String RecordDate = "Recorddate";
    public static final String QuestionLoadCount = "QuestionLoadCount";
    public static final String isLastQuestion = "isLastQuestion";
    public static final String isTimeout = "isTimeout";
    public static final String IsSynced = "IsSynced";

    private Context mContext;

    private DatabaseHandler dbHandler = null;

    private SQLiteDatabase database = null;
    private Retrofit retrofit;

    public TestResultRepository(Context context) {
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

    public static String CreateTestTable()
    {
        String CREATE_TABLE=" CREATE TABLE IF NOT EXISTS " +TABLE_TEST+" ( "+ SlNo+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +RecordDate+" TEXT ," + TestAnswers + " TEXT , "+marksSecured+" TEXT , "
                + QuestionLoadCount + " INT , "+ isLastQuestion + " TEXT , "+ isTimeout +" TEXT , "+IsSynced+" INT )";
            return CREATE_TABLE;
    }

    public void insertTestRecord(String result,String marks,int LoadCount, String lastQuestion,String timeout){

        try{
            Date newDate = new Date();
            String currentdate = String.valueOf(newDate);
            DBConnectionOpen();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TestAnswers, result);
            contentValues.put(RecordDate, currentdate);
            contentValues.put(marksSecured,marks);
            contentValues.put(QuestionLoadCount,LoadCount);
            contentValues.put(isLastQuestion,lastQuestion);
            contentValues.put(isTimeout,timeout);
            contentValues.put(IsSynced, 0);

            long rowCount = database.insert(TABLE_TEST, null, contentValues);
            Log.d("rowcount",String.valueOf(rowCount));
            //insertOrUpdateCustomerPersonalCB.getInsertOrUpDateCustomerPersonalSuccessCB(Integer.parseInt(String.valueOf(rowCount)));
        } catch (Exception e) {
            //insertOrUpdateCustomerPersonalCB.getInsertOrUpDateCustomerPersonalFailureCB(e.getMessage());

        } finally {
            DBConnectionClose();
        }
    }

    public List<TestResultModel> getAllUnsyncedValues(){
        List<TestResultModel> resultModels = new ArrayList<TestResultModel>();
        try{
            DBConnectionOpen();
            String query =" SELECT tbl_TEST_MASTER.SlNo, " +
                    "tbl_TEST_MASTER.TestAnswers, " +
                    "tbl_TEST_MASTER.MarksSecured, " +
                    "tbl_TEST_MASTER.Recorddate, " +
                    "tbl_TEST_MASTER.QuestionLoadCount, " +
                    "tbl_TEST_MASTER.isLastQuestion, " +
                    "tbl_TEST_MASTER.isTimeout " +
                    "from tbl_TEST_MASTER WHERE tbl_TEST_MASTER.IsSynced = 0";
            Cursor accompanistCursor = database.rawQuery(query, null);
            resultModels = getAllFromCursor(accompanistCursor);
            accompanistCursor.close();
            //getAccompanistDataAPIListnerCB.getAccompanistDataSuccessCB(accompanists);
            //Log.d("parm getAllAccompanistList  size >>> ", String.valueOf(listOfAssets.size()));
        }catch (Exception e){
            Log.d("parm exception getAllAccompanistList  ",e.toString());
            //getAccompanistDataAPIListnerCB.getAccompanistDataFailureCB("Error Occured");
        }finally {
            DBConnectionClose();
        }

        return resultModels;
    }

    private List<TestResultModel> getAllFromCursor(Cursor assetCursor) {

        List<TestResultModel> testResultModels = new ArrayList<TestResultModel>();
        if (assetCursor != null && assetCursor.getCount() > 0) {
            assetCursor.moveToFirst();
            int slno = assetCursor.getColumnIndex(SlNo);
            int testanswer = assetCursor.getColumnIndex(TestAnswers);
            int marks = assetCursor.getColumnIndex(marksSecured);
            int recorddate = assetCursor.getColumnIndex(RecordDate);
            int questionLoadCount = assetCursor.getColumnIndex(QuestionLoadCount);
            int islastQuestion = assetCursor.getColumnIndex(isLastQuestion);
            int istimeout = assetCursor.getColumnIndex(isTimeout);


            do {
                TestResultModel model = new TestResultModel();
                model.setSlno(assetCursor.getInt(slno));
                model.setTestAnswers(assetCursor.getString(testanswer));
                model.setRecordDate(assetCursor.getString(recorddate));
                model.setMarksSecured(assetCursor.getInt(marks));
                model.setQuestionLoadCount(assetCursor.getInt(questionLoadCount));
                model.setIslastQuestion(assetCursor.getString(islastQuestion));
                model.setIstimeout(assetCursor.getString(istimeout));

                testResultModels.add(model);
            } while (assetCursor.moveToNext());
        }
        return testResultModels;
    }

    public void updateIsSynced(int sno){
        String updateQuery = "UPDATE tbl_TEST_MASTER SET IsSynced = 1 WHERE SlNo = '" + sno +"'";
        try {
            DBConnectionOpen();
            database.execSQL(updateQuery);
        }catch(Exception e){
            Log.d("",e.toString());
        } finally {
            DBConnectionClose();
        }
    }

    public void deleteRecord(){
        try {
            DBConnectionClose();
            String rawQuery = "DELETE FROM tbl_TEST_MASTER WHERE " + IsSynced + "= 1";
            DBConnectionOpen();
            database.execSQL(rawQuery);
        }catch (Exception e){
            Log.d("",e.toString());
        }finally {
            DBConnectionClose();
        }
    }

    public void deleteAllTransaction(){
        try{
            DBConnectionOpen();
            database.delete(TABLE_TEST, null, null);
        }catch (Exception e){

        }finally {
            DBConnectionClose();
        }

    }
}
