package com.swaas.kangle.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import retrofit.Retrofit;

/**
 * Created by Sayellessh on 05-04-2017.
 */

public class UserRepository {
    public static final String TABLE_USER_DETAILS = "tbl_User_Info";

    public static final String Company_Code = "Company_Code";
    public static final String User_Name = "User_Name";
    public static final String Password = "Password";
    public static final String URL = "URL";
    public static final String User_Code = "User_Code";//PK
    public static final String Region_Code = "Region_Code";
    public static final String Region_Name = "Region_Name";
    public static final String Division_Code = "Division_Code";
    public static final String Division_Name = "Division_Name";
    public static final String User_Type_Code = "User_Type_Code";
    public static final String User_Type_Name = "User_Type_Name";
    public static final String Region_Hierarchy = "Region_Hierarchy";
    public static final String Last_Sync_Date = "Last_Sync_Date";
    public static final String Company_Id = "Company_Id";

    private Context mContext;

    private DatabaseHandler dbHandler = null;

    private SQLiteDatabase database = null;
    private Retrofit retrofit;
    /*private UserAuthenticationAPICallBackListner listner;
    private ChangePasswordAPICallBackListener changePasswordListener;
    private ForgotPasswordAPICallBackListener forgotPasswordListener;*/
    SimpleDateFormat dt = new SimpleDateFormat("dd/MMM/yyy");

    public UserRepository(Context context) {
        dbHandler = new DatabaseHandler(context);
        mContext = context;
    }

    public void DBConnectionOpen() throws SQLException {
        database = dbHandler.getWritableDatabase();
    }

    public void DBConnectionClose() throws SQLException {
        if (database.isOpen()) {
            database.close();
        }
    }


}
