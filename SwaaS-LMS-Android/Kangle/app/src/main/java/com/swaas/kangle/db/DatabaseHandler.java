package com.swaas.kangle.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.swaas.kangle.LPCourse.questionbuilder.db.TestResultRepository;
import com.swaas.kangle.db.Contract.AssetAnalyticsContract;
import com.swaas.kangle.db.Filters.ChecklistCatTagFilterRepository;
import com.swaas.kangle.db.Filters.CourseCatTagFilterRepository;
import com.swaas.kangle.db.Filters.DigitalassetCatTagFilterRepository;

/**
 * Created by Sayellessh on 05-04-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Swaas kangle";
    private static final int DATABASE_VERSION = 3;//7
    //private static final int DATABASE_VERSION = 5; /*updated for brightcove prev version*/

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DigitalAssetHeaderRepository.Create());
        db.execSQL(DigitalAssetTransactionRepository.Create());
        db.execSQL(DigitalAssetOfflineRepository.Create());
        db.execSQL(DigitalAssetTransactionChildRepository.Create());
        db.execSQL(DigitalAssetOfflineChildRepository.Create());
        db.execSQL(UserTrackertableRepository.Create());
        db.execSQL(DigitalAssetRepository.Create_Digital_Asset_Analytics_Details());
        db.execSQL(TestResultRepository.CreateTestTable());
        db.execSQL(DigitalAssetTransactionRepository.Create_Digital_Asset_Analytics());
        db.execSQL(DigitalAssetTransactionRepository.Create_Customer_DA_Feedback());
        // Checklist included
        db.execSQL(CheckListTempRepository.Create());

        //FilterTables
        db.execSQL(DigitalassetCatTagFilterRepository.Create());
        db.execSQL(CourseCatTagFilterRepository.Create());
        db.execSQL(ChecklistCatTagFilterRepository.Create());

        //Coursetable included
        db.execSQL(CourseListTempRepository.Create());

        if (!isColumnExist(db, DigitalAssetOfflineChildRepository.TABLE_DIGITAL_ASSET_OFFLINE_CHILD, "Image_Id")) {
            String AddedScript = "ALTER TABLE " + DigitalAssetOfflineChildRepository.TABLE_DIGITAL_ASSET_OFFLINE_CHILD + " ADD COLUMN Image_Id INT";
            db.execSQL(AddedScript);
        }

        if (!isColumnExist(db, DigitalAssetTransactionChildRepository.TABLE_DIGITAL_ASSET_CHILD_TRANSACTION, "Recorddate")) {
            String AddedScript = "ALTER TABLE " + DigitalAssetTransactionChildRepository.TABLE_DIGITAL_ASSET_CHILD_TRANSACTION + " ADD COLUMN Recorddate TEXT";
            db.execSQL(AddedScript);
        }


        //BrightCove Video properties added in DBversion '5'

        if (!isColumnExist(db, DigitalAssetHeaderRepository.TABLE_DIGITAL_ASSET_MASTER, "VideoType")) {
            String AddedScript = "ALTER TABLE " + DigitalAssetHeaderRepository.TABLE_DIGITAL_ASSET_MASTER + " ADD COLUMN VideoType TEXT ";
            db.execSQL(AddedScript);
        }

        if (!isColumnExist(db, DigitalAssetHeaderRepository.TABLE_DIGITAL_ASSET_MASTER, "Video_Account_Id")) {
            String AddedScript = "ALTER TABLE " + DigitalAssetHeaderRepository.TABLE_DIGITAL_ASSET_MASTER + " ADD COLUMN Video_Account_Id TEXT";
            db.execSQL(AddedScript);
        }

        if (!isColumnExist(db, DigitalAssetHeaderRepository.TABLE_DIGITAL_ASSET_MASTER, "VideoId")) {
            String AddedScript = "ALTER TABLE " + DigitalAssetHeaderRepository.TABLE_DIGITAL_ASSET_MASTER + " ADD COLUMN VideoId TEXT";
            db.execSQL(AddedScript);
        }

        if (!isColumnExist(db, DigitalAssetHeaderRepository.TABLE_DIGITAL_ASSET_MASTER, "AccountId")) {
            String AddedScript = "ALTER TABLE " + DigitalAssetHeaderRepository.TABLE_DIGITAL_ASSET_MASTER + " ADD COLUMN AccountId TEXT";
            db.execSQL(AddedScript);
        }

        if (!isColumnExist(db, DigitalAssetHeaderRepository.TABLE_DIGITAL_ASSET_MASTER, "PlayerId")) {
            String AddedScript = "ALTER TABLE " + DigitalAssetHeaderRepository.TABLE_DIGITAL_ASSET_MASTER + " ADD COLUMN PlayerId TEXT";
            db.execSQL(AddedScript);
        }

        if (!isColumnExist(db, DigitalAssetHeaderRepository.TABLE_DIGITAL_ASSET_MASTER, "PolicyKey")) {
            String AddedScript = "ALTER TABLE " + DigitalAssetHeaderRepository.TABLE_DIGITAL_ASSET_MASTER + " ADD COLUMN PolicyKey TEXT";
            db.execSQL(AddedScript);
        }

        //CourseChecklist columns

        if (!isColumnExist(db, CheckListTempRepository.TABLE_CHECKLIST_TEMP_TABLE, "Checklist_Classification")) {
            String AddedScript = "ALTER TABLE " + CheckListTempRepository.TABLE_CHECKLIST_TEMP_TABLE + " ADD COLUMN Checklist_Classification INT";
            db.execSQL(AddedScript);
        }

        if (!isColumnExist(db, CheckListTempRepository.TABLE_CHECKLIST_TEMP_TABLE, "Ref_Id")) {
            String AddedScript = "ALTER TABLE " + CheckListTempRepository.TABLE_CHECKLIST_TEMP_TABLE + " ADD COLUMN Ref_Id INT";
            db.execSQL(AddedScript);
        }

        //CourseChecklist CAT_TAg table columns
        if (!isColumnExist(db, ChecklistCatTagFilterRepository.TABLE_CHECKLIST_CAT_TAG, "Checklist_Classification")) {
            String AddedScript = "ALTER TABLE " + ChecklistCatTagFilterRepository.TABLE_CHECKLIST_CAT_TAG + " ADD COLUMN Checklist_Classification INT";
            db.execSQL(AddedScript);
        }


        //update tran_digital_asset_analytics
        //CourseChecklist CAT_TAg table columns
        if (!isColumnExist(db, AssetAnalyticsContract.DigitalAssetAnalytics.TABLE_NAME, AssetAnalyticsContract.DigitalAssetAnalytics.COURSE_ID)) {
            String AddedScript = "ALTER TABLE " + AssetAnalyticsContract.DigitalAssetAnalytics.TABLE_NAME+ " ADD COLUMN "+ AssetAnalyticsContract.DigitalAssetAnalytics.COURSE_ID +" INT";
            db.execSQL(AddedScript);
        }
        //CourseChecklist CAT_TAg table columns
        if (!isColumnExist(db, AssetAnalyticsContract.DigitalAssetAnalytics.TABLE_NAME, AssetAnalyticsContract.DigitalAssetAnalytics.SECTION_ID)) {
            String AddedScript = "ALTER TABLE " + AssetAnalyticsContract.DigitalAssetAnalytics.TABLE_NAME+ " ADD COLUMN "+ AssetAnalyticsContract.DigitalAssetAnalytics.SECTION_ID +" INT";
            db.execSQL(AddedScript);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void dropDB(SQLiteDatabase db){
        String drop="DROP DATABASE "+DATABASE_NAME;
        db.execSQL(drop);
    }

    public boolean isColumnExist(SQLiteDatabase db, String tableName, String fieldName) {

        boolean isExist = false;
        Cursor res = null;
        try {
            res = db.rawQuery("Select * from " + tableName + " limit 1", null);

            int colIndex = res.getColumnIndex(fieldName);
            if (colIndex != -1) {
                isExist = true;
            }

        } catch (Exception e) {
        Log.d("error",e.toString());
        } finally {
            if (res != null) {
                res.close();
            }
        }
        return isExist;
    }
}
