package com.swaas.kangle.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.swaas.kangle.CheckList.CheckListService;
import com.swaas.kangle.CheckList.model.CheckListModel;
import com.swaas.kangle.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Sayellessh on 26-04-2018.
 */

public class CheckListTempRepository {

    public static final String TABLE_CHECKLIST_TEMP_TABLE = "tbl_TEMP_CHECKLIST";

    public static final String SlNo= "SlNo";
    public static final String Checklist_Id = "Checklist_Id";
    public static final String Checklist_Name = "Checklist_Name";
    public static final String Checklist_Type = "Checklist_Type";
    public static final String Checklist_Type_ID = "Checklist_Type_ID";
    public static final String Checklist_Frequency_Type = "Checklist_Frequency_Type";
    public static final String Checklist_Frequency_Id = "Checklist_Frequency_Id";
    public static final String Checklist_Description = "Checklist_Description";
    public static final String Checklist_Category_Id = "Checklist_Category_Id";
    public static final String Checklist_Tags= "Checklist_Tags";
    public static final String Checklist_Image_URL = "Checklist_Image_URL";
    public static final String Category_Name = "Category_Name";
    public static final String Valid_From = "Valid_From";
    public static final String Valid_To = "Valid_To";
    public static final String Publish_Id = "Publish_Id";
    public static final String Checklist_Status_String = "Checklist_Status_String";
    public static final String Valid_From_String = "Valid_From_String";
    public static final String Valid_To_String = "Valid_To_String";
    public static final String Checklist_User_Assignment_Id = "Checklist_User_Assignment_Id";
    public static final String Company_Id = "Company_Id";
    public static final String Publish_Date = "Publish_Date";
    public static final String Publish_Date_String = "Publish_Date_String";
    public static final String Status = "Status";
    public static final String Checklist_Status_Value = "Checklist_Status_Value";
    public static final String Last_Test_Date_String = "Last_Test_Date_String";
    public static final String Last_Test_Date = "Last_Test_Date";
    public static final String CheckList_Publish_Group_Id = "CheckList_Publish_Group_Id";

    public static final String Checklist_Classification = "Checklist_Classification";
    public static final String Ref_Id = "Ref_Id";


    private Context mContext;

    private GetChecklistDataCBListner getChecklistDataCBListner;

    private DatabaseHandler dbHandler = null;

    private SQLiteDatabase database = null;
    private Retrofit retrofit;

    public CheckListTempRepository(Context context) {
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
        String CREATE_TABLE=" CREATE TABLE IF NOT EXISTS " +TABLE_CHECKLIST_TEMP_TABLE+" ( "+ SlNo+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +Checklist_Id+ " INT ,"+ Checklist_Name+" TEXT, "+Checklist_Type+" TEXT, "+Checklist_Type_ID+" INT,"
                + Checklist_Frequency_Type +" TEXT ,"+Checklist_Frequency_Id+" INT, "+Checklist_Description+" TEXT, "
                +Checklist_Category_Id+" TEXT, "+Checklist_Tags+" TEXT," +Checklist_Image_URL +" TEXT, "+Category_Name+" TEXT,"
                +Valid_From+" TEXT, "+Valid_To+" TEXT, " +Publish_Id+" INT, "+Checklist_Status_String+" TEXT, "
                +Valid_From_String+" TEXT, " +Valid_To_String+" TEXT, "+Checklist_User_Assignment_Id+" INT, "
                +Company_Id+" INT, "+Publish_Date+" TEXT,"+Publish_Date_String+" TEXT,"+Status+" TEXT,"+Checklist_Status_Value+" INT,"
                +Last_Test_Date_String+" TEXT,"+ Last_Test_Date+" TEXT ,"+ Checklist_Classification +" INT ,"+
                Ref_Id+" INT ,"+CheckList_Publish_Group_Id+" INT )";
        return CREATE_TABLE;
    }


    public void checkListBulkInsert(List<CheckListModel> chklists) {
        DBConnectionOpen();
        try {
            database.delete(TABLE_CHECKLIST_TEMP_TABLE, null, null);
            ContentValues contentValues = new ContentValues();

            for (CheckListModel chklist : chklists) {
                contentValues.clear();
                contentValues.put(Checklist_Id, chklist.getChecklist_Id());
                contentValues.put(Checklist_Name, chklist.getChecklist_Name());
                contentValues.put(Checklist_Type,chklist.getChecklist_Type());
                contentValues.put(Checklist_Type_ID, chklist.getChecklist_Type_ID());
                contentValues.put(Checklist_Frequency_Type, chklist.getChecklist_Frequency_Type());
                contentValues.put(Checklist_Frequency_Id, chklist.getChecklist_Frequency_Id());
                contentValues.put(Checklist_Description,chklist.getChecklist_Description());
                contentValues.put(Checklist_Category_Id, chklist.getChecklist_Category_Id());
                contentValues.put(Checklist_Tags,chklist.getChecklist_Tags());
                contentValues.put(Checklist_Image_URL, chklist.getChecklist_Image_URL());
                contentValues.put(Category_Name, chklist.getCategory_Name());
                contentValues.put(Valid_From,chklist.getValid_From());
                contentValues.put(Valid_To, chklist.getValid_To());
                contentValues.put(Publish_Id, chklist.getPublish_Id());
                contentValues.put(Checklist_Status_String, chklist.getChecklist_Status_String());
                contentValues.put(Valid_From_String, chklist.getValid_From());
                contentValues.put(Valid_To_String,chklist.getValid_To_String());
                contentValues.put(Checklist_User_Assignment_Id, chklist.getChecklist_User_Assignment_Id());
                contentValues.put(Company_Id, chklist.getCompany_Id());
                contentValues.put(Publish_Date,chklist.getPublish_Date());
                contentValues.put(Publish_Date_String,chklist.getPublish_Date_String());
                contentValues.put(Status, chklist.getStatus());
                contentValues.put(Checklist_Status_Value, chklist.getChecklist_Status_Value());
                contentValues.put(Last_Test_Date_String,chklist.getLast_Test_Date_String());
                contentValues.put(Last_Test_Date, chklist.getLast_Test_Date());
                contentValues.put(CheckList_Publish_Group_Id,chklist.getCheckList_Publish_Group_Id());
                contentValues.put(Checklist_Classification,chklist.getChecklist_Classification());
                contentValues.put(Ref_Id,chklist.getRef_Id());

                database.insert(TABLE_CHECKLIST_TEMP_TABLE, null, contentValues);
            }

        } finally {
            DBConnectionClose();
        }
    }

    public List<CheckListModel> getAllData(){
        List<CheckListModel> listOfitems = null;
        try{
            String query ="select tbl_TEMP_CHECKLIST.Checklist_Id," +
                    "tbl_TEMP_CHECKLIST.Checklist_Name," +
                    "tbl_TEMP_CHECKLIST.Checklist_Type," +
                    "tbl_TEMP_CHECKLIST.Checklist_Type_ID," +
                    "tbl_TEMP_CHECKLIST.Checklist_Frequency_Type," +
                    "tbl_TEMP_CHECKLIST.Checklist_Frequency_Id,"+
                    "tbl_TEMP_CHECKLIST.Checklist_Description," +
                    "tbl_TEMP_CHECKLIST.Checklist_Category_Id," +
                    "tbl_TEMP_CHECKLIST.Checklist_Tags," +
                    "tbl_TEMP_CHECKLIST.Checklist_Image_URL, " +

                    "tbl_TEMP_CHECKLIST.Category_Name, "+
                    "tbl_TEMP_CHECKLIST.Valid_From, "+
                    "tbl_TEMP_CHECKLIST.Valid_To, "+
                    "tbl_TEMP_CHECKLIST.Publish_Id, "+
                    "tbl_TEMP_CHECKLIST.Checklist_Status_String, "+
                    "tbl_TEMP_CHECKLIST.Valid_From_String, "+
                    "tbl_TEMP_CHECKLIST.Valid_To_String, "+
                    "tbl_TEMP_CHECKLIST.Checklist_User_Assignment_Id, "+
                    "tbl_TEMP_CHECKLIST.Company_Id, "+
                    "tbl_TEMP_CHECKLIST.Publish_Date, "+
                    "tbl_TEMP_CHECKLIST.Publish_Date_String, "+
                    "tbl_TEMP_CHECKLIST.Status ,"+

                    "tbl_TEMP_CHECKLIST.Checklist_Status_Value ,"+
                    "tbl_TEMP_CHECKLIST.Last_Test_Date_String ,"+
                    "tbl_TEMP_CHECKLIST.Last_Test_Date ," +
                    "tbl_TEMP_CHECKLIST.CheckList_Publish_Group_Id , " +
                    "tbl_TEMP_CHECKLIST.Checklist_Classification , "+
                    "tbl_TEMP_CHECKLIST.Ref_Id "+
                    " from tbl_TEMP_CHECKLIST ";
            DBConnectionOpen();
            Cursor accompanistCursor = database.rawQuery(query, null);
            listOfitems = getAllFromCursor(accompanistCursor);
            accompanistCursor.close();
            //getAccompanistDataAPIListnerCB.getAccompanistDataSuccessCB(accompanists);
            Log.d("parm get size >>> ", String.valueOf(listOfitems.size()));
        }catch (Exception e){
            Log.d("parm exception ",e.toString());
            //getAccompanistDataAPIListnerCB.getAccompanistDataFailureCB("Error Occured");
        }finally {
            DBConnectionClose();
        }

        return listOfitems;
    }

    public List<CheckListModel> getrefinedData(int type,int frequency){
        List<CheckListModel> listOfitems = null;
        try{
            String query ="select tbl_TEMP_CHECKLIST.Checklist_Id," +
                    "tbl_TEMP_CHECKLIST.Checklist_Name," +
                    "tbl_TEMP_CHECKLIST.Checklist_Type," +
                    "tbl_TEMP_CHECKLIST.Checklist_Type_ID," +
                    "tbl_TEMP_CHECKLIST.Checklist_Frequency_Type," +
                    "tbl_TEMP_CHECKLIST.Checklist_Frequency_Id,"+
                    "tbl_TEMP_CHECKLIST.Checklist_Description," +
                    "tbl_TEMP_CHECKLIST.Checklist_Category_Id," +
                    "tbl_TEMP_CHECKLIST.Checklist_Tags," +
                    "tbl_TEMP_CHECKLIST.Checklist_Image_URL, " +

                    "tbl_TEMP_CHECKLIST.Category_Name, "+
                    "tbl_TEMP_CHECKLIST.Valid_From, "+
                    "tbl_TEMP_CHECKLIST.Valid_To, "+
                    "tbl_TEMP_CHECKLIST.Publish_Id, "+
                    "tbl_TEMP_CHECKLIST.Checklist_Status_String, "+
                    "tbl_TEMP_CHECKLIST.Valid_From_String, "+
                    "tbl_TEMP_CHECKLIST.Valid_To_String, "+
                    "tbl_TEMP_CHECKLIST.Checklist_User_Assignment_Id, "+
                    "tbl_TEMP_CHECKLIST.Company_Id, "+
                    "tbl_TEMP_CHECKLIST.Publish_Date, "+
                    "tbl_TEMP_CHECKLIST.Publish_Date_String, "+
                    "tbl_TEMP_CHECKLIST.Status ,"+

                    "tbl_TEMP_CHECKLIST.Checklist_Status_Value ,"+
                    "tbl_TEMP_CHECKLIST.Last_Test_Date_String ,"+
                    "tbl_TEMP_CHECKLIST.Last_Test_Date ," +
                    "tbl_TEMP_CHECKLIST.CheckList_Publish_Group_Id ," +
                    "tbl_TEMP_CHECKLIST.Checklist_Classification , "+
                    "tbl_TEMP_CHECKLIST.Ref_Id "+
                    "from tbl_TEMP_CHECKLIST where tbl_TEMP_CHECKLIST.Checklist_Frequency_Id = "+frequency+" AND tbl_TEMP_CHECKLIST.Checklist_Type_ID = "+ type;
            DBConnectionOpen();
            Cursor accompanistCursor = database.rawQuery(query, null);
            listOfitems = getAllFromCursor(accompanistCursor);
            accompanistCursor.close();
            //getAccompanistDataAPIListnerCB.getAccompanistDataSuccessCB(accompanists);
            Log.d("parm getAll ", String.valueOf(listOfitems.size()));
        }catch (Exception e){
            Log.d("parm except",e.toString());
            //getAccompanistDataAPIListnerCB.getAccompanistDataFailureCB("Error Occured");
        }finally {
            DBConnectionClose();
        }

        return listOfitems;
    }

    public List<CheckListModel> getFilteredData(String CHKId){
        List<CheckListModel> listOfitems = null;
        try{
            String query ="select tbl_TEMP_CHECKLIST.Checklist_Id," +
                    "tbl_TEMP_CHECKLIST.Checklist_Name," +
                    "tbl_TEMP_CHECKLIST.Checklist_Type," +
                    "tbl_TEMP_CHECKLIST.Checklist_Type_ID," +
                    "tbl_TEMP_CHECKLIST.Checklist_Frequency_Type," +
                    "tbl_TEMP_CHECKLIST.Checklist_Frequency_Id,"+
                    "tbl_TEMP_CHECKLIST.Checklist_Description," +
                    "tbl_TEMP_CHECKLIST.Checklist_Category_Id," +
                    "tbl_TEMP_CHECKLIST.Checklist_Tags," +
                    "tbl_TEMP_CHECKLIST.Checklist_Image_URL, " +

                    "tbl_TEMP_CHECKLIST.Category_Name, "+
                    "tbl_TEMP_CHECKLIST.Valid_From, "+
                    "tbl_TEMP_CHECKLIST.Valid_To, "+
                    "tbl_TEMP_CHECKLIST.Publish_Id, "+
                    "tbl_TEMP_CHECKLIST.Checklist_Status_String, "+
                    "tbl_TEMP_CHECKLIST.Valid_From_String, "+
                    "tbl_TEMP_CHECKLIST.Valid_To_String, "+
                    "tbl_TEMP_CHECKLIST.Checklist_User_Assignment_Id, "+
                    "tbl_TEMP_CHECKLIST.Company_Id, "+
                    "tbl_TEMP_CHECKLIST.Publish_Date, "+
                    "tbl_TEMP_CHECKLIST.Publish_Date_String, "+
                    "tbl_TEMP_CHECKLIST.Status ,"+

                    "tbl_TEMP_CHECKLIST.Checklist_Status_Value ,"+
                    "tbl_TEMP_CHECKLIST.Last_Test_Date_String ,"+
                    "tbl_TEMP_CHECKLIST.Last_Test_Date ," +
                    "tbl_TEMP_CHECKLIST.CheckList_Publish_Group_Id " +
                    "from tbl_TEMP_CHECKLIST where tbl_TEMP_CHECKLIST.Checklist_Id in ("+CHKId+")";
            DBConnectionOpen();
            Cursor accompanistCursor = database.rawQuery(query, null);
            listOfitems = getAllFromCursor(accompanistCursor);
            accompanistCursor.close();
            //getAccompanistDataAPIListnerCB.getAccompanistDataSuccessCB(accompanists);
            Log.d("parm get", String.valueOf(listOfitems.size()));
        }catch (Exception e){
            Log.d("parm exc",e.toString());
            //getAccompanistDataAPIListnerCB.getAccompanistDataFailureCB("Error Occured");
        }finally {
            DBConnectionClose();
        }

        return listOfitems;
    }

    public List<CheckListModel> getAllYetToStartData(int type,int frequency){
        List<CheckListModel> listOfitems = null;
        try{
            String query ="select tbl_TEMP_CHECKLIST.Checklist_Id," +
                    "tbl_TEMP_CHECKLIST.Checklist_Name," +
                    "tbl_TEMP_CHECKLIST.Checklist_Type," +
                    "tbl_TEMP_CHECKLIST.Checklist_Type_ID," +
                    "tbl_TEMP_CHECKLIST.Checklist_Frequency_Type," +
                    "tbl_TEMP_CHECKLIST.Checklist_Frequency_Id,"+
                    "tbl_TEMP_CHECKLIST.Checklist_Description," +
                    "tbl_TEMP_CHECKLIST.Checklist_Category_Id," +
                    "tbl_TEMP_CHECKLIST.Checklist_Tags," +
                    "tbl_TEMP_CHECKLIST.Checklist_Image_URL, " +

                    "tbl_TEMP_CHECKLIST.Category_Name, "+
                    "tbl_TEMP_CHECKLIST.Valid_From, "+
                    "tbl_TEMP_CHECKLIST.Valid_To, "+
                    "tbl_TEMP_CHECKLIST.Publish_Id, "+
                    "tbl_TEMP_CHECKLIST.Checklist_Status_String, "+
                    "tbl_TEMP_CHECKLIST.Valid_From_String, "+
                    "tbl_TEMP_CHECKLIST.Valid_To_String, "+
                    "tbl_TEMP_CHECKLIST.Checklist_User_Assignment_Id, "+
                    "tbl_TEMP_CHECKLIST.Company_Id, "+
                    "tbl_TEMP_CHECKLIST.Publish_Date, "+
                    "tbl_TEMP_CHECKLIST.Publish_Date_String, "+
                    "tbl_TEMP_CHECKLIST.Status ,"+

                    "tbl_TEMP_CHECKLIST.Checklist_Status_Value ,"+
                    "tbl_TEMP_CHECKLIST.Last_Test_Date_String ,"+
                    "tbl_TEMP_CHECKLIST.Last_Test_Date ," +
                    "tbl_TEMP_CHECKLIST.CheckList_Publish_Group_Id ," +
                    "tbl_TEMP_CHECKLIST.Checklist_Classification , "+
                    "tbl_TEMP_CHECKLIST.Ref_Id "+
                    "from tbl_TEMP_CHECKLIST WHERE tbl_TEMP_CHECKLIST.Checklist_Status_Value = 0 "+
                    "AND tbl_TEMP_CHECKLIST.Checklist_Frequency_Id = "+frequency+" AND tbl_TEMP_CHECKLIST.Checklist_Type_ID = "+ type;
            DBConnectionOpen();
            Cursor accompanistCursor = database.rawQuery(query, null);
            listOfitems = getAllFromCursor(accompanistCursor);
            accompanistCursor.close();
            //getAccompanistDataAPIListnerCB.getAccompanistDataSuccessCB(accompanists);
            Log.d("parm get", String.valueOf(listOfitems.size()));
        }catch (Exception e){
            Log.d("parm exc",e.toString());
            //getAccompanistDataAPIListnerCB.getAccompanistDataFailureCB("Error Occured");
        }finally {
            DBConnectionClose();
        }

        return listOfitems;
    }

    private List<CheckListModel> getAllFromCursor(Cursor assetCursor) {

        List<CheckListModel> chklist = new ArrayList<CheckListModel>();
        if (assetCursor != null && assetCursor.getCount() > 0) {
            assetCursor.moveToFirst();

            int id = assetCursor.getColumnIndex(Checklist_Id);
            int name = assetCursor.getColumnIndex(Checklist_Name);
            int type = assetCursor.getColumnIndex(Checklist_Type);
            int typeId = assetCursor.getColumnIndex(Checklist_Type_ID);
            int frequencytype = assetCursor.getColumnIndex(Checklist_Frequency_Type);
            int frequencyId = assetCursor.getColumnIndex(Checklist_Frequency_Id);
            int description = assetCursor.getColumnIndex(Checklist_Description);
            int categoryId = assetCursor.getColumnIndex(Checklist_Category_Id);
            int tags = assetCursor.getColumnIndex(Checklist_Tags);
            int URL = assetCursor.getColumnIndex(Checklist_Image_URL);

            int catname = assetCursor.getColumnIndex(Category_Name);
            int fromvalid = assetCursor.getColumnIndex(Valid_From);
            int tovalid = assetCursor.getColumnIndex(Valid_To);
            int publishid = assetCursor.getColumnIndex(Publish_Id);
            int statusstring = assetCursor.getColumnIndex(Checklist_Status_String);
            int fromstring = assetCursor.getColumnIndex(Valid_From_String);
            int tostring = assetCursor.getColumnIndex(Valid_To_String);
            int assignmentid = assetCursor.getColumnIndex(Checklist_User_Assignment_Id);
            int companyId = assetCursor.getColumnIndex(Company_Id);
            int publishdate = assetCursor.getColumnIndex(Publish_Date);
            int publishdatestring = assetCursor.getColumnIndex(Publish_Date_String);

            int status = assetCursor.getColumnIndex(Status);
            int statusvalue = assetCursor.getColumnIndex(Checklist_Status_Value);
            int lasttestdatestring = assetCursor.getColumnIndex(Last_Test_Date_String);
            int lasttestdate = assetCursor.getColumnIndex(Last_Test_Date);
            int checklistgroupId = assetCursor.getColumnIndex(CheckList_Publish_Group_Id);
            int CheckClassification = assetCursor.getColumnIndex(Checklist_Classification);
            int RefId = assetCursor.getColumnIndex(Ref_Id);

            do {
                CheckListModel listMaster = new CheckListModel();
                listMaster.setChecklist_Id(assetCursor.getInt(id));
                listMaster.setChecklist_Name(assetCursor.getString(name));
                listMaster.setChecklist_Type(assetCursor.getString(type));
                listMaster.setChecklist_Type_ID(assetCursor.getInt(typeId));
                listMaster.setChecklist_Frequency_Type(assetCursor.getString(frequencytype));
                listMaster.setChecklist_Frequency_Id(assetCursor.getInt(frequencyId));
                listMaster.setChecklist_Description(assetCursor.getString(description));
                listMaster.setChecklist_Category_Id(assetCursor.getString(categoryId));
                listMaster.setChecklist_Tags(assetCursor.getString(tags));
                listMaster.setChecklist_Image_URL(assetCursor.getString(URL));

                listMaster.setCategory_Name(assetCursor.getString(catname));
                listMaster.setValid_From(assetCursor.getString(fromvalid));
                listMaster.setValid_To(assetCursor.getString(tovalid));
                listMaster.setPublish_Id(assetCursor.getInt(publishid));
                listMaster.setChecklist_Status_String(assetCursor.getString(statusstring));
                listMaster.setValid_From_String(assetCursor.getString(fromstring));
                listMaster.setValid_To_String(assetCursor.getString(tostring));
                listMaster.setChecklist_User_Assignment_Id(assetCursor.getInt(assignmentid));
                listMaster.setCompany_Id(assetCursor.getInt(companyId));
                listMaster.setPublish_Date(assetCursor.getString(publishdate));
                listMaster.setPublish_Date_String(assetCursor.getString(publishdatestring));

                listMaster.setStatus(assetCursor.getString(status));
                listMaster.setChecklist_Status_Value(assetCursor.getInt(statusvalue));
                listMaster.setLast_Test_Date_String(assetCursor.getString(lasttestdatestring));
                listMaster.setLast_Test_Date(assetCursor.getString(lasttestdate));
                listMaster.setCheckList_Publish_Group_Id(assetCursor.getInt(checklistgroupId));

                listMaster.setChecklist_Classification(assetCursor.getInt(CheckClassification));
                listMaster.setRef_Id(assetCursor.getInt(RefId));

                chklist.add(listMaster);
            } while (assetCursor.moveToNext());
        }
        return chklist;
    }

    public void deleteChecklistTemp(){
        try{
            DBConnectionOpen();
            database.delete(TABLE_CHECKLIST_TEMP_TABLE, null, null);
        }catch (Exception e){

        }finally {
            DBConnectionClose();
        }

    }

    public interface GetChecklistDataCBListner {
        void GetChecklistDataSuccessCB(List<CheckListModel> customers);
        void GetChecklistDataFailureCB(String message);
    }

    public void setGetChecklistDataCBListner(GetChecklistDataCBListner getCustomerDataCBListner) {
        this.getChecklistDataCBListner = getCustomerDataCBListner;
    }

    public void getChecklistDataFromAPI(String SubdomainName, int CompanyId, int UserId,String offsetFromUtc){
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            CheckListService checklistService = retrofit.create(CheckListService.class);

            Call call = checklistService.getAvailableChecklists(SubdomainName, CompanyId, UserId, offsetFromUtc);
            call.enqueue(new Callback<ArrayList<CheckListModel>>() {

                @Override
                public void onResponse(Response<ArrayList<CheckListModel>> response, Retrofit retrofit) {
                    ArrayList<CheckListModel> checkListModels = response.body();
                    if (checkListModels != null && checkListModels.size() > 0) {
                        getChecklistDataCBListner.GetChecklistDataSuccessCB(checkListModels);
                    } else {
                        getChecklistDataCBListner.GetChecklistDataSuccessCB(checkListModels);
                    }
                }
                 @Override
                 public void onFailure(Throwable t) {
                     Log.d(t.toString(), "Error");
                     getChecklistDataCBListner.GetChecklistDataFailureCB("");
                 }
            });
        }
    }
}
