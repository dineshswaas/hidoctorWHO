package com.swaas.kangle.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.swaas.kangle.models.DigitalAssetsMaster;

import java.util.ArrayList;
import java.util.List;

import retrofit.Retrofit;

/**
 * Created by Sayellessh on 05-04-2017.
 */

public class DigitalAssetHeaderRepository {

    public static final String TABLE_DIGITAL_ASSET_MASTER = "tbl_DIGASSETS_MASTER";

    public static final String SlNo= "SlNo";
    public static final String DAID = "DAID";
    public static final String DAMode = "DAMode";
    public static final String offlineURL = "offlineURL";
    public static final String onlineURL = "onlineURL";
    public static final String Tags= "Tags";
    public static final String OnlineOutPutId = "OnlineOutPutId";
    public static final String OfflineOutPutId = "OfflineOutPutId";
    public static final String DocumentType = "DocumentType";
    public static final String IsDownloadable = "IsDownloadable";
    public static final String IsViewable = "IsViewable";
    public static final String IsSharable = "IsSharable";
    public static final String ThumnailURL = "ThumnailURL";
    public static final String DAName = "DAName";
    public static final String DACategoryCode = "DACategoryCode";
    public static final String DACategoryName = "DACategoryName";
    public static final String TotalViews = "TotalViews";
    public static final String TotalLikes = "TotalLikes";
    public static final String TotalDislikes = "TotalDislikes";
    public static final String TotalRatings = "TotalRatings";
    public static final String UDTags = "UDTags";
    public static final String DA_Description = "DA_Description";
    public static final String DA_Type = "DA_Type";
    public static final String DACategoryUsageCount = "DACategoryUsageCount";
    public static final String DA_Size_In_MB = "DA_Size_In_MB";
    public static final String lstAssetImageModel = "lstAssetImageModel";
    public static final String Is_Read = "Is_Read";
    public static final String Is_Downloaded = "Is_Downloaded";
    public static final String Uploaded_Date = "Uploaded_Date";
    public static final String Uploaded_By = "Uploaded_By";
    public static final String lstEncodedUrls = "lstEncodedUrls";//list
    public static final String Is_Asset_Sharable = "Is_Asset_Sharable";
    public static final String DA_File_Name = "DA_File_Name";
    public static final String User_Like = "User_Like";
    public static final String User_Rating = "User_Rating";


    public static final String VideoType = "VideoType";
    public static final String Video_Account_Id = "Video_Account_Id";
    public static final String VideoId = "VideoId";
    public static final String AccountId = "AccountId";
    public static final String PlayerId = "PlayerId";
    public static final String PolicyKey = "PolicyKey";



    private Context mContext;

    private DatabaseHandler dbHandler = null;

    private SQLiteDatabase database = null;
    private Retrofit retrofit;

    public DigitalAssetHeaderRepository(Context context) {
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
        String CREATE_TABLE=" CREATE TABLE IF NOT EXISTS " +TABLE_DIGITAL_ASSET_MASTER+" ( "+ SlNo+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +DAID+ " TEXT ,"+ DAMode+" TEXT, "+offlineURL+" TEXT, "+onlineURL+" TEXT,"+ Tags +" TEXT ,"
                +OnlineOutPutId+" TEXT, "+OfflineOutPutId+" TEXT, "+DocumentType+" TEXT, "+IsDownloadable+" TEXT," +IsViewable +" TEXT, "
                +IsSharable+" TEXT," +ThumnailURL+" TEXT, "+DAName+" TEXT, " +DACategoryCode+" TEXT, " +DACategoryName+" TEXT, "
                +TotalViews+" INT, " +TotalLikes+" INT, " +TotalDislikes+" INT, "+TotalRatings+" INT, "+UDTags+" TEXT, "
                +DA_Description+" TEXT,"+DA_Type+" TEXT,"+DACategoryUsageCount+" INT,"+DA_Size_In_MB+" TEXT,"+lstAssetImageModel+" TEXT,"
                +Is_Read+" INT, "+Is_Downloaded+" INT, "+Uploaded_Date+" TEXT, "+Uploaded_By+" TEXT, "+Is_Asset_Sharable+" TEXT, "
                +DA_File_Name+" TEXT, "+User_Like+" INT, "+User_Rating +" INT,"+ VideoType +" TEXT, "
                +Video_Account_Id+" TEXT, "+VideoId+" TEXT, "+AccountId+" TEXT, "+PlayerId+" TEXT, "+ PolicyKey+" TEXT )";
        return CREATE_TABLE;
    }


    public void assetsBulkInsert(List<DigitalAssetsMaster> assets) {
        DBConnectionOpen();
        database.beginTransaction();
        try {
            database.delete(TABLE_DIGITAL_ASSET_MASTER, null, null);
            ContentValues assetContentValues = new ContentValues();

            for (DigitalAssetsMaster asset : assets) {
                assetContentValues.clear();
                assetContentValues.put(DAID, asset.getDAID());
                assetContentValues.put(DAMode, asset.getDAMode());
                assetContentValues.put(offlineURL,asset.getOfflineURL());
                assetContentValues.put(onlineURL, asset.getOnlineURL());
                assetContentValues.put(Tags, asset.getTags());
                assetContentValues.put(OnlineOutPutId, asset.getOnlineOutPutId());
                assetContentValues.put(OfflineOutPutId,asset.getOfflineOutPutId());
                assetContentValues.put(DocumentType, asset.getDocumentType());
                assetContentValues.put(IsDownloadable,asset.getIsDownloadable());
                assetContentValues.put(IsViewable, asset.getIsViewable());
                assetContentValues.put(IsSharable, asset.getIsSharable());
                assetContentValues.put(ThumnailURL,asset.getThumnailURL());
                assetContentValues.put(DAName, asset.getDAName());
                assetContentValues.put(DACategoryCode, asset.getDACategoryCode());
                assetContentValues.put(DACategoryName,asset.getDACategoryName());
                assetContentValues.put(TotalViews, asset.getTotalViews());
                assetContentValues.put(TotalLikes, asset.getTotalLikes());
                assetContentValues.put(TotalDislikes,asset.getTotalDislikes());
                assetContentValues.put(TotalRatings, asset.getTotalRatings());
                assetContentValues.put(UDTags, asset.getUDTags());
                assetContentValues.put(DA_Description,asset.getDA_Description());
                assetContentValues.put(DA_Type, asset.getDA_Type());
                assetContentValues.put(DACategoryUsageCount, asset.getDACategoryUsageCount());
                assetContentValues.put(DA_Size_In_MB,asset.getDA_Size_In_MB());
                assetContentValues.put(lstAssetImageModel, asset.getLstAssetImageModel());

                assetContentValues.put(Is_Read, asset.is_Read());
                assetContentValues.put(Is_Downloaded,asset.is_Downloaded());
                assetContentValues.put(Uploaded_Date, asset.getUploaded_Date());
                assetContentValues.put(Uploaded_By, asset.getUploaded_By());
                assetContentValues.put(Is_Asset_Sharable,asset.getIs_Asset_Sharable());
                assetContentValues.put(DA_File_Name,asset.getDA_File_Name());
                assetContentValues.put(User_Like,asset.getUser_Like());
                assetContentValues.put(User_Rating,asset.getUser_Rating());

                assetContentValues.put(VideoType,asset.getVideoType());
                assetContentValues.put(Video_Account_Id,asset.getVideo_Account_Id());
                assetContentValues.put(VideoId,asset.getVideoId());
                assetContentValues.put(AccountId,asset.getAccountId());
                assetContentValues.put(PlayerId,asset.getPlayerId());
                assetContentValues.put(PolicyKey,asset.getPolicyKey());



                database.insert(TABLE_DIGITAL_ASSET_MASTER, null, assetContentValues);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            DBConnectionClose();
        }
    }

    public List<DigitalAssetsMaster> getAllAssets(){
        List<DigitalAssetsMaster> listOfAssets = null;
        try{
            String query ="select tbl_DIGASSETS_MASTER.DAID,tbl_DIGASSETS_MASTER.DAName," +
                    "tbl_DIGASSETS_MASTER.DA_Size_In_MB," +
                    "tbl_DIGASSETS_MASTER.DocumentType," +
                    "tbl_DIGASSETS_MASTER.DA_Type," +
                    "tbl_DIGASSETS_MASTER.onlineURL,"+
                    "tbl_DIGASSETS_MASTER.IsDownloadable," +
                    "tbl_DIGASSETS_MASTER.TotalViews," +
                    "tbl_DIGASSETS_MASTER.TotalLikes," +
                    "tbl_DIGASSETS_MASTER.TotalRatings, " +

                    "tbl_DIGASSETS_MASTER.offlineURL, "+
                    "tbl_DIGASSETS_MASTER.Tags, "+
                    "tbl_DIGASSETS_MASTER.DACategoryCode, "+
                    "tbl_DIGASSETS_MASTER.DACategoryName, "+
                    "tbl_DIGASSETS_MASTER.TotalDislikes, "+
                    "tbl_DIGASSETS_MASTER.UDTags, "+
                    "tbl_DIGASSETS_MASTER.DA_Description, "+
                    "tbl_DIGASSETS_MASTER.Is_Read, "+
                    "tbl_DIGASSETS_MASTER.Is_Downloaded, "+
                    "tbl_DIGASSETS_MASTER.User_Like, "+
                    "tbl_DIGASSETS_MASTER.User_Rating ,"+

                    "tbl_DIGASSETS_MASTER.DAMode ,"+
                    "tbl_DIGASSETS_MASTER.OnlineOutPutId ,"+
                    "tbl_DIGASSETS_MASTER.OfflineOutPutId ,"+
                    "tbl_DIGASSETS_MASTER.Uploaded_Date ,"+
                    "tbl_DIGASSETS_MASTER.Uploaded_By ,"+
                    "tbl_DIGASSETS_MASTER.IsViewable ,"+
                    "tbl_DIGASSETS_MASTER.IsSharable ,"+
                    "tbl_DIGASSETS_MASTER.ThumnailURL ,"+
                    "tbl_DIGASSETS_MASTER.DACategoryUsageCount ,"+
                    "tbl_DIGASSETS_MASTER.Is_Asset_Sharable ,"+
                    "tbl_DIGASSETS_MASTER.DA_File_Name ,"+
                    "tbl_DIGASSETS_MASTER.lstAssetImageModel , "+

                    "tbl_DIGASSETS_MASTER.VideoType ,"+
                    "tbl_DIGASSETS_MASTER.Video_Account_Id ,"+
                    "tbl_DIGASSETS_MASTER.VideoId ,"+
                    "tbl_DIGASSETS_MASTER.AccountId ,"+
                    "tbl_DIGASSETS_MASTER.PlayerId ,"+
                    "tbl_DIGASSETS_MASTER.PolicyKey "+

                    "from tbl_DIGASSETS_MASTER";
        DBConnectionOpen();
        Cursor accompanistCursor = database.rawQuery(query, null);
        listOfAssets = getAllAssetsFromCursor(accompanistCursor);
        accompanistCursor.close();
        //getAccompanistDataAPIListnerCB.getAccompanistDataSuccessCB(accompanists);
        Log.d("parm getAllAccompanistList  size >>> ", String.valueOf(listOfAssets.size()));
    }catch (Exception e){
        Log.d("parm exception getAllAccompanistList  ",e.toString());
        //getAccompanistDataAPIListnerCB.getAccompanistDataFailureCB("Error Occured");
    }finally {
        DBConnectionClose();
    }

        return listOfAssets;
    }

    public List<DigitalAssetsMaster> getAssetsbyPage(int load){
        List<DigitalAssetsMaster> listOfAssets = null;
        try{
            String query ="select tbl_DIGASSETS_MASTER.DAID,tbl_DIGASSETS_MASTER.DAName," +
                    "tbl_DIGASSETS_MASTER.DA_Size_In_MB," +
                    "tbl_DIGASSETS_MASTER.DocumentType," +
                    "tbl_DIGASSETS_MASTER.DA_Type," +
                    "tbl_DIGASSETS_MASTER.onlineURL,"+
                    "tbl_DIGASSETS_MASTER.IsDownloadable," +
                    "tbl_DIGASSETS_MASTER.TotalViews," +
                    "tbl_DIGASSETS_MASTER.TotalLikes," +
                    "tbl_DIGASSETS_MASTER.TotalRatings, " +

                    "tbl_DIGASSETS_MASTER.offlineURL, "+
                    "tbl_DIGASSETS_MASTER.Tags, "+
                    "tbl_DIGASSETS_MASTER.DACategoryCode, "+
                    "tbl_DIGASSETS_MASTER.DACategoryName, "+
                    "tbl_DIGASSETS_MASTER.TotalDislikes, "+
                    "tbl_DIGASSETS_MASTER.UDTags, "+
                    "tbl_DIGASSETS_MASTER.DA_Description, "+
                    "tbl_DIGASSETS_MASTER.Is_Read, "+
                    "tbl_DIGASSETS_MASTER.Is_Downloaded, "+
                    "tbl_DIGASSETS_MASTER.User_Like, "+
                    "tbl_DIGASSETS_MASTER.User_Rating ,"+

                    "tbl_DIGASSETS_MASTER.DAMode ,"+
                    "tbl_DIGASSETS_MASTER.OnlineOutPutId ,"+
                    "tbl_DIGASSETS_MASTER.OfflineOutPutId ,"+
                    "tbl_DIGASSETS_MASTER.Uploaded_Date ,"+
                    "tbl_DIGASSETS_MASTER.Uploaded_By ,"+
                    "tbl_DIGASSETS_MASTER.IsViewable ,"+
                    "tbl_DIGASSETS_MASTER.IsSharable ,"+
                    "tbl_DIGASSETS_MASTER.ThumnailURL ,"+
                    "tbl_DIGASSETS_MASTER.DACategoryUsageCount ,"+
                    "tbl_DIGASSETS_MASTER.Is_Asset_Sharable ,"+
                    "tbl_DIGASSETS_MASTER.DA_File_Name ,"+
                    "tbl_DIGASSETS_MASTER.lstAssetImageModel , "+

                    "tbl_DIGASSETS_MASTER.VideoType ,"+
                    "tbl_DIGASSETS_MASTER.Video_Account_Id ,"+
                    "tbl_DIGASSETS_MASTER.VideoId ,"+
                    "tbl_DIGASSETS_MASTER.AccountId ,"+
                    "tbl_DIGASSETS_MASTER.PlayerId ,"+
                    "tbl_DIGASSETS_MASTER.PolicyKey "+

                    "from tbl_DIGASSETS_MASTER "+
                    "LIMIT 20";
                    /*"WHERE tbl_DIGASSETS_MASTER.DAID > LastValue " +
                     "ORDER BY tbl_DIGASSETS_MASTER.DAID" +*/
            DBConnectionOpen();
            Cursor accompanistCursor = database.rawQuery(query, null);
            listOfAssets = getAllAssetsFromCursor(accompanistCursor);
            accompanistCursor.close();
            //getAccompanistDataAPIListnerCB.getAccompanistDataSuccessCB(accompanists);
            Log.d("parm getAllAccompanistList  size >>> ", String.valueOf(listOfAssets.size()));
        }catch (Exception e){
            Log.d("parm exception getAllAccompanistList  ",e.toString());
            //getAccompanistDataAPIListnerCB.getAccompanistDataFailureCB("Error Occured");
        }finally {
            DBConnectionClose();
        }

        return listOfAssets;
    }

    public List<DigitalAssetsMaster> getFilteredAssets(String DAIDs){
        List<DigitalAssetsMaster> listOfAssets = null;
        try{
            String query ="select tbl_DIGASSETS_MASTER.DAID,tbl_DIGASSETS_MASTER.DAName," +
                    "tbl_DIGASSETS_MASTER.DA_Size_In_MB," +
                    "tbl_DIGASSETS_MASTER.DocumentType," +
                    "tbl_DIGASSETS_MASTER.DA_Type," +
                    "tbl_DIGASSETS_MASTER.onlineURL,"+
                    "tbl_DIGASSETS_MASTER.IsDownloadable," +
                    "tbl_DIGASSETS_MASTER.TotalViews," +
                    "tbl_DIGASSETS_MASTER.TotalLikes," +
                    "tbl_DIGASSETS_MASTER.TotalRatings, " +

                    "tbl_DIGASSETS_MASTER.offlineURL, "+
                    "tbl_DIGASSETS_MASTER.Tags, "+
                    "tbl_DIGASSETS_MASTER.DACategoryCode, "+
                    "tbl_DIGASSETS_MASTER.DACategoryName, "+
                    "tbl_DIGASSETS_MASTER.TotalDislikes, "+
                    "tbl_DIGASSETS_MASTER.UDTags, "+
                    "tbl_DIGASSETS_MASTER.DA_Description, "+
                    "tbl_DIGASSETS_MASTER.Is_Read, "+
                    "tbl_DIGASSETS_MASTER.Is_Downloaded, "+
                    "tbl_DIGASSETS_MASTER.User_Like, "+
                    "tbl_DIGASSETS_MASTER.User_Rating ,"+

                    "tbl_DIGASSETS_MASTER.DAMode ,"+
                    "tbl_DIGASSETS_MASTER.OnlineOutPutId ,"+
                    "tbl_DIGASSETS_MASTER.OfflineOutPutId ,"+
                    "tbl_DIGASSETS_MASTER.Uploaded_Date ,"+
                    "tbl_DIGASSETS_MASTER.Uploaded_By ,"+
                    "tbl_DIGASSETS_MASTER.IsViewable ,"+
                    "tbl_DIGASSETS_MASTER.IsSharable ,"+
                    "tbl_DIGASSETS_MASTER.ThumnailURL ,"+
                    "tbl_DIGASSETS_MASTER.DACategoryUsageCount ,"+
                    "tbl_DIGASSETS_MASTER.Is_Asset_Sharable ,"+
                    "tbl_DIGASSETS_MASTER.DA_File_Name ,"+
                    "tbl_DIGASSETS_MASTER.lstAssetImageModel , "+

                    "tbl_DIGASSETS_MASTER.VideoType ,"+
                    "tbl_DIGASSETS_MASTER.Video_Account_Id ,"+
                    "tbl_DIGASSETS_MASTER.VideoId ,"+
                    "tbl_DIGASSETS_MASTER.AccountId ,"+
                    "tbl_DIGASSETS_MASTER.PlayerId ,"+
                    "tbl_DIGASSETS_MASTER.PolicyKey "+

                    "from tbl_DIGASSETS_MASTER where tbl_DIGASSETS_MASTER.DAID in ("+DAIDs+")";
            DBConnectionOpen();
            Cursor accompanistCursor = database.rawQuery(query, null);
            listOfAssets = getAllAssetsFromCursor(accompanistCursor);
            accompanistCursor.close();
            //getAccompanistDataAPIListnerCB.getAccompanistDataSuccessCB(accompanists);
            Log.d("parm getAllAccompanistList  size >>> ", String.valueOf(listOfAssets.size()));
        }catch (Exception e){
            Log.d("parm exception getAllAccompanistList  ",e.toString());
            //getAccompanistDataAPIListnerCB.getAccompanistDataFailureCB("Error Occured");
        }finally {
            DBConnectionClose();
        }

        return listOfAssets;
    }

    public List<DigitalAssetsMaster> getallassetsincludingDownloaded(){
        List<DigitalAssetsMaster> assetswithoffline = null;
        try{
            DBConnectionOpen();
        String query ="SELECT * FROM tbl_DIGASSETS_OFFLINE_MASTER " +
                "UNION SELECT * FROM tbl_DiGASSETS_MASTER " +
                "WHERE DAID NOT IN(SELECT DAID FROM tbl_DIGASSETS_OFFLINE_MASTER)";
            Cursor accompanistCursor = database.rawQuery(query, null);
            assetswithoffline = getAllAssetsFromCursor(accompanistCursor);
            accompanistCursor.close();
        }catch (Exception e){
            Log.d("getallassetsincludingDownloaded",e.toString());
        }finally {
            DBConnectionClose();
        }
        return assetswithoffline;
    }

    public List<DigitalAssetsMaster> getAllCategoryWithCount(){
        List<DigitalAssetsMaster> digitalAssetsMasterscategory = null;
        try{
            String query ="select tbl_DIGASSETS_MASTER.DACategoryName, COUNT(SlNo) AS Count FROM tbl_DIGASSETS_MASTER Group By tbl_DIGASSETS_MASTER.DACategoryName ";
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

    public List<DigitalAssetsMaster> getAssetsByCategoryName(String categoryName){
        List<DigitalAssetsMaster> assetsByCategory = null;
        try{
            String query ="select tbl_DIGASSETS_MASTER.DAID,tbl_DIGASSETS_MASTER.DAName,tbl_DIGASSETS_MASTER.DA_Size_In_MB," +
                    "tbl_DIGASSETS_MASTER.DocumentType," +
                    "tbl_DIGASSETS_MASTER.DA_Type," +
                    "tbl_DIGASSETS_MASTER.onlineURL," +
                    "tbl_DIGASSETS_MASTER.IsDownloadable," +
                    "tbl_DIGASSETS_MASTER.TotalViews," +
                    "tbl_DIGASSETS_MASTER.TotalLikes," +
                    "tbl_DIGASSETS_MASTER.TotalRatings, " +

                    "tbl_DIGASSETS_MASTER.offlineURL, "+
                    "tbl_DIGASSETS_MASTER.Tags, "+
                    "tbl_DIGASSETS_MASTER.DACategoryCode, "+
                    "tbl_DIGASSETS_MASTER.DACategoryName, "+
                    "tbl_DIGASSETS_MASTER.TotalDislikes, "+
                    "tbl_DIGASSETS_MASTER.UDTags, "+
                    "tbl_DIGASSETS_MASTER.DA_Description, "+
                    "tbl_DIGASSETS_MASTER.Is_Read, "+
                    "tbl_DIGASSETS_MASTER.Is_Downloaded, "+
                    "tbl_DIGASSETS_MASTER.User_Like, "+
                    "tbl_DIGASSETS_MASTER.User_Rating ,"+

                    "tbl_DIGASSETS_MASTER.DAMode ,"+
                    "tbl_DIGASSETS_MASTER.OnlineOutPutId ,"+
                    "tbl_DIGASSETS_MASTER.OfflineOutPutId ,"+
                    "tbl_DIGASSETS_MASTER.Uploaded_Date ,"+
                    "tbl_DIGASSETS_MASTER.Uploaded_By ,"+
                    "tbl_DIGASSETS_MASTER.IsViewable ,"+
                    "tbl_DIGASSETS_MASTER.IsSharable ,"+
                    "tbl_DIGASSETS_MASTER.ThumnailURL ,"+
                    "tbl_DIGASSETS_MASTER.DACategoryUsageCount ,"+
                    "tbl_DIGASSETS_MASTER.Is_Asset_Sharable ,"+
                    "tbl_DIGASSETS_MASTER.DA_File_Name ,"+
                    "tbl_DIGASSETS_MASTER.lstAssetImageModel ,"+

                    "tbl_DIGASSETS_MASTER.VideoType ,"+
                    "tbl_DIGASSETS_MASTER.Video_Account_Id ,"+
                    "tbl_DIGASSETS_MASTER.VideoId ,"+
                    "tbl_DIGASSETS_MASTER.AccountId ,"+
                    "tbl_DIGASSETS_MASTER.PlayerId ,"+
                    "tbl_DIGASSETS_MASTER.PolicyKey "+

                    "from tbl_DIGASSETS_MASTER WHERE LOWER(tbl_DIGASSETS_MASTER.DACategoryName) =LOWER('" + categoryName +"')";
            DBConnectionOpen();
            Cursor accompanistCursor = database.rawQuery(query, null);
            assetsByCategory = getAssetsByCategoryFromCursor(accompanistCursor);
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

    public List<DigitalAssetsMaster> getAssetsByTagName(String tagName){
        List<DigitalAssetsMaster> assetsByCategory = null;
        try{
            String query ="select tbl_DIGASSETS_MASTER.DAID,tbl_DIGASSETS_MASTER.DAName,tbl_DIGASSETS_MASTER.DA_Size_In_MB," +
                    "tbl_DIGASSETS_MASTER.DocumentType," +
                    "tbl_DIGASSETS_MASTER.DA_Type," +
                    "tbl_DIGASSETS_MASTER.onlineURL," +
                    "tbl_DIGASSETS_MASTER.IsDownloadable," +
                    "tbl_DIGASSETS_MASTER.TotalViews," +
                    "tbl_DIGASSETS_MASTER.TotalLikes," +
                    "tbl_DIGASSETS_MASTER.TotalRatings, " +

                    "tbl_DIGASSETS_MASTER.offlineURL, "+
                    "tbl_DIGASSETS_MASTER.Tags, "+
                    "tbl_DIGASSETS_MASTER.DACategoryCode, "+
                    "tbl_DIGASSETS_MASTER.DACategoryName, "+
                    "tbl_DIGASSETS_MASTER.TotalDislikes, "+
                    "tbl_DIGASSETS_MASTER.UDTags, "+
                    "tbl_DIGASSETS_MASTER.DA_Description, "+
                    "tbl_DIGASSETS_MASTER.Is_Read, "+
                    "tbl_DIGASSETS_MASTER.Is_Downloaded, "+
                    "tbl_DIGASSETS_MASTER.User_Like, "+
                    "tbl_DIGASSETS_MASTER.User_Rating ,"+

                    "tbl_DIGASSETS_MASTER.DAMode ,"+
                    "tbl_DIGASSETS_MASTER.OnlineOutPutId ,"+
                    "tbl_DIGASSETS_MASTER.OfflineOutPutId ,"+
                    "tbl_DIGASSETS_MASTER.Uploaded_Date ,"+
                    "tbl_DIGASSETS_MASTER.Uploaded_By ,"+
                    "tbl_DIGASSETS_MASTER.IsViewable ,"+
                    "tbl_DIGASSETS_MASTER.IsSharable ,"+
                    "tbl_DIGASSETS_MASTER.ThumnailURL ,"+
                    "tbl_DIGASSETS_MASTER.DACategoryUsageCount ,"+
                    "tbl_DIGASSETS_MASTER.Is_Asset_Sharable ,"+
                    "tbl_DIGASSETS_MASTER.DA_File_Name ,"+
                    "tbl_DIGASSETS_MASTER.lstAssetImageModel ,"+

                    "tbl_DIGASSETS_MASTER.VideoType ,"+
                    "tbl_DIGASSETS_MASTER.Video_Account_Id ,"+
                    "tbl_DIGASSETS_MASTER.VideoId ,"+
                    "tbl_DIGASSETS_MASTER.AccountId ,"+
                    "tbl_DIGASSETS_MASTER.PlayerId ,"+
                    "tbl_DIGASSETS_MASTER.PolicyKey "+

                    "from tbl_DIGASSETS_MASTER WHERE LOWER(tbl_DIGASSETS_MASTER.UDTags) LIKE LOWER('%" + tagName +"#%')";
            DBConnectionOpen();
            Cursor accompanistCursor = database.rawQuery(query, null);
            assetsByCategory = getAssetsByCategoryFromCursor(accompanistCursor);
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

    public List<DigitalAssetsMaster> getRelatedAssetsByCategoryName(String categoryName,String DAID){
        List<DigitalAssetsMaster> assetsByCategory = null;
        try{
            String query ="select tbl_DIGASSETS_MASTER.DAID,tbl_DIGASSETS_MASTER.DAName,tbl_DIGASSETS_MASTER.DA_Size_In_MB," +
                    "tbl_DIGASSETS_MASTER.DocumentType," +
                    "tbl_DIGASSETS_MASTER.DA_Type," +
                    "tbl_DIGASSETS_MASTER.onlineURL," +
                    "tbl_DIGASSETS_MASTER.IsDownloadable," +
                    "tbl_DIGASSETS_MASTER.TotalViews," +
                    "tbl_DIGASSETS_MASTER.TotalLikes," +
                    "tbl_DIGASSETS_MASTER.TotalRatings, " +

                    "tbl_DIGASSETS_MASTER.offlineURL, "+
                    "tbl_DIGASSETS_MASTER.Tags, "+
                    "tbl_DIGASSETS_MASTER.DACategoryCode, "+
                    "tbl_DIGASSETS_MASTER.DACategoryName, "+
                    "tbl_DIGASSETS_MASTER.TotalDislikes, "+
                    "tbl_DIGASSETS_MASTER.UDTags, "+
                    "tbl_DIGASSETS_MASTER.DA_Description, "+
                    "tbl_DIGASSETS_MASTER.Is_Read, "+
                    "tbl_DIGASSETS_MASTER.Is_Downloaded, "+
                    "tbl_DIGASSETS_MASTER.User_Like, "+
                    "tbl_DIGASSETS_MASTER.User_Rating ,"+

                    "tbl_DIGASSETS_MASTER.DAMode ,"+
                    "tbl_DIGASSETS_MASTER.OnlineOutPutId ,"+
                    "tbl_DIGASSETS_MASTER.OfflineOutPutId ,"+
                    "tbl_DIGASSETS_MASTER.Uploaded_Date ,"+
                    "tbl_DIGASSETS_MASTER.Uploaded_By ,"+
                    "tbl_DIGASSETS_MASTER.IsViewable ,"+
                    "tbl_DIGASSETS_MASTER.IsSharable ,"+
                    "tbl_DIGASSETS_MASTER.ThumnailURL ,"+
                    "tbl_DIGASSETS_MASTER.DACategoryUsageCount ,"+
                    "tbl_DIGASSETS_MASTER.Is_Asset_Sharable ,"+
                    "tbl_DIGASSETS_MASTER.DA_File_Name ,"+
                    "tbl_DIGASSETS_MASTER.lstAssetImageModel ,"+

                    "tbl_DIGASSETS_MASTER.VideoType ,"+
                    "tbl_DIGASSETS_MASTER.Video_Account_Id ,"+
                    "tbl_DIGASSETS_MASTER.VideoId ,"+
                    "tbl_DIGASSETS_MASTER.AccountId ,"+
                    "tbl_DIGASSETS_MASTER.PlayerId ,"+
                    "tbl_DIGASSETS_MASTER.PolicyKey "+

                    "from tbl_DIGASSETS_MASTER WHERE LOWER(tbl_DIGASSETS_MASTER.DACategoryName) =LOWER('" + categoryName +"')" +
                    "AND tbl_DIGASSETS_MASTER.DAID != '"+ DAID +"'";
            DBConnectionOpen();
            Cursor accompanistCursor = database.rawQuery(query, null);
            assetsByCategory = getAssetsByCategoryFromCursor(accompanistCursor);
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

    public List<DigitalAssetsMaster> getRelatedAssetsByCategoryNameLimit5(String categoryName,String DAID){
        List<DigitalAssetsMaster> assetsByCategory = null;
        try{
            String query ="select tbl_DIGASSETS_MASTER.DAID,tbl_DIGASSETS_MASTER.DAName,tbl_DIGASSETS_MASTER.DA_Size_In_MB," +
                    "tbl_DIGASSETS_MASTER.DocumentType," +
                    "tbl_DIGASSETS_MASTER.DA_Type," +
                    "tbl_DIGASSETS_MASTER.onlineURL," +
                    "tbl_DIGASSETS_MASTER.IsDownloadable," +
                    "tbl_DIGASSETS_MASTER.TotalViews," +
                    "tbl_DIGASSETS_MASTER.TotalLikes," +
                    "tbl_DIGASSETS_MASTER.TotalRatings, " +

                    "tbl_DIGASSETS_MASTER.offlineURL, "+
                    "tbl_DIGASSETS_MASTER.Tags, "+
                    "tbl_DIGASSETS_MASTER.DACategoryCode, "+
                    "tbl_DIGASSETS_MASTER.DACategoryName, "+
                    "tbl_DIGASSETS_MASTER.TotalDislikes, "+
                    "tbl_DIGASSETS_MASTER.UDTags, "+
                    "tbl_DIGASSETS_MASTER.DA_Description, "+
                    "tbl_DIGASSETS_MASTER.Is_Read, "+
                    "tbl_DIGASSETS_MASTER.Is_Downloaded, "+
                    "tbl_DIGASSETS_MASTER.User_Like, "+
                    "tbl_DIGASSETS_MASTER.User_Rating ,"+

                    "tbl_DIGASSETS_MASTER.DAMode ,"+
                    "tbl_DIGASSETS_MASTER.OnlineOutPutId ,"+
                    "tbl_DIGASSETS_MASTER.OfflineOutPutId ,"+
                    "tbl_DIGASSETS_MASTER.Uploaded_Date ,"+
                    "tbl_DIGASSETS_MASTER.Uploaded_By ,"+
                    "tbl_DIGASSETS_MASTER.IsViewable ,"+
                    "tbl_DIGASSETS_MASTER.IsSharable ,"+
                    "tbl_DIGASSETS_MASTER.ThumnailURL ,"+
                    "tbl_DIGASSETS_MASTER.DACategoryUsageCount ,"+
                    "tbl_DIGASSETS_MASTER.Is_Asset_Sharable ,"+
                    "tbl_DIGASSETS_MASTER.DA_File_Name ,"+
                    "tbl_DIGASSETS_MASTER.lstAssetImageModel ,"+

                    "tbl_DIGASSETS_MASTER.VideoType ,"+
                    "tbl_DIGASSETS_MASTER.Video_Account_Id ,"+
                    "tbl_DIGASSETS_MASTER.VideoId ,"+
                    "tbl_DIGASSETS_MASTER.AccountId ,"+
                    "tbl_DIGASSETS_MASTER.PlayerId ,"+
                    "tbl_DIGASSETS_MASTER.PolicyKey "+

                    "from tbl_DIGASSETS_MASTER WHERE LOWER(tbl_DIGASSETS_MASTER.DACategoryName) =LOWER('" + categoryName +"')" +
                    "AND tbl_DIGASSETS_MASTER.DAID != '"+ DAID +"' LIMIT 5";
            DBConnectionOpen();
            Cursor accompanistCursor = database.rawQuery(query, null);
            assetsByCategory = getAssetsByCategoryFromCursor(accompanistCursor);
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

    public DigitalAssetsMaster getAssetDetail(String daid){
        DigitalAssetsMaster asset = new DigitalAssetsMaster();
    try{
        String query ="select tbl_DIGASSETS_MASTER.SlNo," +
                " tbl_DIGASSETS_MASTER.DAID," +
                "tbl_DIGASSETS_MASTER.DAName," +
                "tbl_DIGASSETS_MASTER.DA_Size_In_MB," +
                "tbl_DIGASSETS_MASTER.DocumentType," +
                "tbl_DIGASSETS_MASTER.DA_Type," +
                "tbl_DIGASSETS_MASTER.IsDownloadable," +
                "tbl_DIGASSETS_MASTER.IsViewable," +
                "tbl_DIGASSETS_MASTER.DA_Description," +
                "tbl_DIGASSETS_MASTER.DACategoryName,"+
                "tbl_DIGASSETS_MASTER.onlineURL,"+
                "tbl_DIGASSETS_MASTER.TotalViews," +
                "tbl_DIGASSETS_MASTER.TotalLikes," +
                "tbl_DIGASSETS_MASTER.TotalRatings, " +

                "tbl_DIGASSETS_MASTER.offlineURL, "+
                "tbl_DIGASSETS_MASTER.Tags, "+
                "tbl_DIGASSETS_MASTER.DACategoryCode, "+
                "tbl_DIGASSETS_MASTER.TotalDislikes, "+
                "tbl_DIGASSETS_MASTER.UDTags, "+
                "tbl_DIGASSETS_MASTER.Is_Read, "+
                "tbl_DIGASSETS_MASTER.Is_Downloaded, "+
                "tbl_DIGASSETS_MASTER.User_Like, "+
                "tbl_DIGASSETS_MASTER.User_Rating, "+

                "tbl_DIGASSETS_MASTER.DAMode ,"+
                "tbl_DIGASSETS_MASTER.OnlineOutPutId ,"+
                "tbl_DIGASSETS_MASTER.OfflineOutPutId ,"+
                "tbl_DIGASSETS_MASTER.Uploaded_Date ,"+
                "tbl_DIGASSETS_MASTER.Uploaded_By ,"+
                "tbl_DIGASSETS_MASTER.IsSharable ,"+
                "tbl_DIGASSETS_MASTER.ThumnailURL ,"+
                "tbl_DIGASSETS_MASTER.DACategoryUsageCount ,"+
                "tbl_DIGASSETS_MASTER.Is_Asset_Sharable ,"+
                "tbl_DIGASSETS_MASTER.DA_File_Name ,"+
                "tbl_DIGASSETS_MASTER.lstAssetImageModel ,"+

                "tbl_DIGASSETS_MASTER.VideoType ,"+
                "tbl_DIGASSETS_MASTER.Video_Account_Id ,"+
                "tbl_DIGASSETS_MASTER.VideoId ,"+
                "tbl_DIGASSETS_MASTER.AccountId ,"+
                "tbl_DIGASSETS_MASTER.PlayerId ,"+
                "tbl_DIGASSETS_MASTER.PolicyKey "+

                " from tbl_DIGASSETS_MASTER WHERE tbl_DIGASSETS_MASTER.DAID = '" + daid +"'";
        DBConnectionOpen();
        Cursor accompanistCursor = database.rawQuery(query, null);
        boolean isReadvalue = false,isdownloadedvalue = false;
        if (accompanistCursor != null && accompanistCursor.getCount() > 0) {
            accompanistCursor.moveToFirst();
            asset.setSlno(accompanistCursor.getInt(accompanistCursor.getColumnIndex(SlNo)));
            asset.setDAID(accompanistCursor.getString(accompanistCursor.getColumnIndex(DAID)));
            asset.setDAName(accompanistCursor.getString(accompanistCursor.getColumnIndex(DAName)));
            asset.setDA_Size_In_MB(accompanistCursor.getString(accompanistCursor.getColumnIndex(DA_Size_In_MB)));
            asset.setDocumentType(accompanistCursor.getString(accompanistCursor.getColumnIndex(DocumentType)));
            asset.setDA_Type(accompanistCursor.getString(accompanistCursor.getColumnIndex(DA_Type)));
            asset.setIsDownloadable(accompanistCursor.getString(accompanistCursor.getColumnIndex(IsDownloadable)));
            asset.setIsViewable(accompanistCursor.getString(accompanistCursor.getColumnIndex(IsViewable)));
            asset.setDA_Description(accompanistCursor.getString(accompanistCursor.getColumnIndex(DA_Description)));
            asset.setDACategoryName(accompanistCursor.getString(accompanistCursor.getColumnIndex(DACategoryName)));
            asset.setOnlineURL(accompanistCursor.getString(accompanistCursor.getColumnIndex(onlineURL)));
            asset.setTotalViews(accompanistCursor.getInt(accompanistCursor.getColumnIndex(TotalViews)));
            asset.setTotalLikes(accompanistCursor.getInt(accompanistCursor.getColumnIndex(TotalLikes)));
            asset.setTotalRatings(accompanistCursor.getInt(accompanistCursor.getColumnIndex(TotalRatings)));

            asset.setOfflineURL(accompanistCursor.getString(accompanistCursor.getColumnIndex(offlineURL)));
            asset.setTags(accompanistCursor.getString(accompanistCursor.getColumnIndex(Tags)));
            asset.setDACategoryCode(accompanistCursor.getString(accompanistCursor.getColumnIndex(DACategoryCode)));
            asset.setTotalDislikes(accompanistCursor.getInt(accompanistCursor.getColumnIndex(TotalDislikes)));
            asset.setUDTags(accompanistCursor.getString(accompanistCursor.getColumnIndex(UDTags)));
            if(accompanistCursor.getInt(accompanistCursor.getColumnIndex(Is_Read)) == 1){
                isReadvalue = true;
            }
            asset.setIs_Read(isReadvalue);
            if(accompanistCursor.getInt(accompanistCursor.getColumnIndex(Is_Downloaded)) == 1){
                isdownloadedvalue = true;
            }
            asset.setIs_Downloaded(isdownloadedvalue);
            asset.setUser_Like(accompanistCursor.getInt(accompanistCursor.getColumnIndex(User_Like)));
            asset.setUser_Rating(accompanistCursor.getInt(accompanistCursor.getColumnIndex(User_Rating)));

            asset.setDAMode(accompanistCursor.getString(accompanistCursor.getColumnIndex(DAMode)));
            asset.setOnlineOutPutId(accompanistCursor.getString(accompanistCursor.getColumnIndex(OnlineOutPutId)));
            asset.setOfflineOutPutId(accompanistCursor.getString(accompanistCursor.getColumnIndex(OfflineOutPutId)));
            asset.setUploaded_Date(accompanistCursor.getString(accompanistCursor.getColumnIndex(Uploaded_Date)));
            asset.setUploaded_By(accompanistCursor.getString(accompanistCursor.getColumnIndex(Uploaded_By)));
            asset.setIsSharable(accompanistCursor.getString(accompanistCursor.getColumnIndex(IsSharable)));
            asset.setThumnailURL(accompanistCursor.getString(accompanistCursor.getColumnIndex(ThumnailURL)));
            asset.setDACategoryUsageCount(accompanistCursor.getInt(accompanistCursor.getColumnIndex(DACategoryUsageCount)));
            asset.setIs_Asset_Sharable(accompanistCursor.getString(accompanistCursor.getColumnIndex(Is_Asset_Sharable)));
            asset.setDA_File_Name(accompanistCursor.getString(accompanistCursor.getColumnIndex(DA_File_Name)));
            asset.setLstAssetImageModel(accompanistCursor.getString(accompanistCursor.getColumnIndex(lstAssetImageModel)));

            asset.setVideoType(accompanistCursor.getString(accompanistCursor.getColumnIndex(VideoType)));
            asset.setVideo_Account_Id(accompanistCursor.getString(accompanistCursor.getColumnIndex(Video_Account_Id)));
            asset.setVideoId(accompanistCursor.getString(accompanistCursor.getColumnIndex(VideoId)));
            asset.setAccountId(accompanistCursor.getString(accompanistCursor.getColumnIndex(AccountId)));
            asset.setPlayerId(accompanistCursor.getString(accompanistCursor.getColumnIndex(PlayerId)));
            asset.setPolicyKey(accompanistCursor.getString(accompanistCursor.getColumnIndex(PolicyKey)));

        }
        accompanistCursor.close();
        //getAccompanistDataAPIListnerCB.getAccompanistDataSuccessCB(accompanists);
        Log.d("parm getAllAccompanistList  size >>> ", String.valueOf(""));
    }catch (Exception e){
        Log.d("parm exception getAllAccompanistList  ",e.toString());
        //getAccompanistDataAPIListnerCB.getAccompanistDataFailureCB("Error Occured");
    }finally {
        DBConnectionClose();
    }

        return asset;
    }

    public DigitalAssetsMaster getAssetDetailForPlayer(String daid){
        DigitalAssetsMaster asset = new DigitalAssetsMaster();
        try{
            String query ="select tbl_DIGASSETS_MASTER.SlNo," +
                    "tbl_DIGASSETS_MASTER.DAID," +
                    "tbl_DIGASSETS_MASTER.DAName," +
                    "tbl_DIGASSETS_MASTER.DA_Size_In_MB," +
                    "tbl_DIGASSETS_MASTER.DocumentType," +
                    "tbl_DIGASSETS_MASTER.DA_Type," +
                    "tbl_DIGASSETS_MASTER.IsDownloadable," +
                    "tbl_DIGASSETS_MASTER.IsViewable," +
                    "tbl_DIGASSETS_MASTER.DA_Description," +
                    "tbl_DIGASSETS_MASTER.DACategoryName,"+
                    "tbl_DIGASSETS_MASTER.onlineURL,"+
                    "tbl_DIGASSETS_MASTER.TotalViews," +
                    "tbl_DIGASSETS_MASTER.TotalLikes," +
                    "tbl_DIGASSETS_MASTER.TotalRatings ," +

                    "tbl_DIGASSETS_MASTER.offlineURL, "+
                    "tbl_DIGASSETS_MASTER.Tags, "+
                    "tbl_DIGASSETS_MASTER.DACategoryCode, "+
                    "tbl_DIGASSETS_MASTER.TotalDislikes, "+
                    "tbl_DIGASSETS_MASTER.UDTags, "+
                    "tbl_DIGASSETS_MASTER.Is_Read, "+
                    "tbl_DIGASSETS_MASTER.Is_Downloaded, "+
                    "tbl_DIGASSETS_MASTER.User_Like, "+
                    "tbl_DIGASSETS_MASTER.User_Rating, "+

                    "tbl_DIGASSETS_MASTER.DAMode ,"+
                    "tbl_DIGASSETS_MASTER.OnlineOutPutId ,"+
                    "tbl_DIGASSETS_MASTER.OfflineOutPutId ,"+
                    "tbl_DIGASSETS_MASTER.Uploaded_Date ,"+
                    "tbl_DIGASSETS_MASTER.Uploaded_By ,"+
                    "tbl_DIGASSETS_MASTER.IsSharable ,"+
                    "tbl_DIGASSETS_MASTER.ThumnailURL ,"+
                    "tbl_DIGASSETS_MASTER.DACategoryUsageCount ,"+
                    "tbl_DIGASSETS_MASTER.Is_Asset_Sharable ,"+
                    "tbl_DIGASSETS_MASTER.DA_File_Name ,"+
                    "tbl_DIGASSETS_MASTER.lstAssetImageModel "+

                    "from tbl_DIGASSETS_MASTER WHERE tbl_DIGASSETS_MASTER.DAID = '" + daid +"'";
            DBConnectionOpen();
            Cursor accompanistCursor = database.rawQuery(query, null);
            boolean isReadvalue = false,isdownloadedvalue = false;
            if (accompanistCursor != null && accompanistCursor.getCount() > 0) {
                accompanistCursor.moveToFirst();
                asset.setSlno(accompanistCursor.getInt(accompanistCursor.getColumnIndex(SlNo)));
                asset.setDAID(accompanistCursor.getString(accompanistCursor.getColumnIndex(DAID)));
                asset.setDAName(accompanistCursor.getString(accompanistCursor.getColumnIndex(DAName)));
                asset.setDA_Size_In_MB(accompanistCursor.getString(accompanistCursor.getColumnIndex(DA_Size_In_MB)));
                asset.setDocumentType(accompanistCursor.getString(accompanistCursor.getColumnIndex(DocumentType)));
                asset.setDA_Type(accompanistCursor.getString(accompanistCursor.getColumnIndex(DA_Type)));
                asset.setIsDownloadable(accompanistCursor.getString(accompanistCursor.getColumnIndex(IsDownloadable)));
                asset.setIsViewable(accompanistCursor.getString(accompanistCursor.getColumnIndex(IsViewable)));
                asset.setDA_Description(accompanistCursor.getString(accompanistCursor.getColumnIndex(DA_Description)));
                asset.setDACategoryName(accompanistCursor.getString(accompanistCursor.getColumnIndex(DACategoryName)));
                asset.setOnlineURL(accompanistCursor.getString(accompanistCursor.getColumnIndex(onlineURL)));
                asset.setTotalViews(accompanistCursor.getInt(accompanistCursor.getColumnIndex(TotalViews)));
                asset.setTotalLikes(accompanistCursor.getInt(accompanistCursor.getColumnIndex(TotalLikes)));
                asset.setTotalRatings(accompanistCursor.getInt(accompanistCursor.getColumnIndex(TotalRatings)));

                asset.setOfflineURL(accompanistCursor.getString(accompanistCursor.getColumnIndex(offlineURL)));
                asset.setTags(accompanistCursor.getString(accompanistCursor.getColumnIndex(Tags)));
                asset.setDACategoryCode(accompanistCursor.getString(accompanistCursor.getColumnIndex(DACategoryCode)));
                asset.setTotalDislikes(accompanistCursor.getInt(accompanistCursor.getColumnIndex(TotalDislikes)));
                asset.setUDTags(accompanistCursor.getString(accompanistCursor.getColumnIndex(UDTags)));
                if(accompanistCursor.getInt(accompanistCursor.getColumnIndex(Is_Read)) == 1){
                    isReadvalue = true;
                }
                asset.setIs_Read(isReadvalue);
                if(accompanistCursor.getInt(accompanistCursor.getColumnIndex(Is_Downloaded)) == 1){
                    isdownloadedvalue = true;
                }
                asset.setIs_Downloaded(isdownloadedvalue);
                asset.setUser_Like(accompanistCursor.getInt(accompanistCursor.getColumnIndex(User_Like)));
                asset.setUser_Rating(accompanistCursor.getInt(accompanistCursor.getColumnIndex(User_Rating)));

                asset.setDAMode(accompanistCursor.getString(accompanistCursor.getColumnIndex(DAMode)));
                asset.setOnlineOutPutId(accompanistCursor.getString(accompanistCursor.getColumnIndex(OnlineOutPutId)));
                asset.setOfflineOutPutId(accompanistCursor.getString(accompanistCursor.getColumnIndex(OfflineOutPutId)));
                asset.setUploaded_Date(accompanistCursor.getString(accompanistCursor.getColumnIndex(Uploaded_Date)));
                asset.setUploaded_By(accompanistCursor.getString(accompanistCursor.getColumnIndex(Uploaded_By)));
                asset.setIsSharable(accompanistCursor.getString(accompanistCursor.getColumnIndex(IsSharable)));
                asset.setThumnailURL(accompanistCursor.getString(accompanistCursor.getColumnIndex(ThumnailURL)));
                asset.setDACategoryUsageCount(accompanistCursor.getInt(accompanistCursor.getColumnIndex(DACategoryUsageCount)));
                asset.setIs_Asset_Sharable(accompanistCursor.getString(accompanistCursor.getColumnIndex(Is_Asset_Sharable)));
                asset.setDA_File_Name(accompanistCursor.getString(accompanistCursor.getColumnIndex(DA_File_Name)));
                asset.setLstAssetImageModel(accompanistCursor.getString(accompanistCursor.getColumnIndex(lstAssetImageModel)));
            }
            accompanistCursor.close();
            //getAccompanistDataAPIListnerCB.getAccompanistDataSuccessCB(accompanists);
            Log.d("parm getAllAccompanistList  size >>> ", String.valueOf(""));
        }catch (Exception e){
            Log.d("parm exception getAllAccompanistList  ",e.toString());
            //getAccompanistDataAPIListnerCB.getAccompanistDataFailureCB("Error Occured");
        }finally {
            DBConnectionClose();
        }

        return asset;
    }

    public void updateIsRead(DigitalAssetsMaster data){
        int value = 0;
        if(data.is_Read()){
            value = 1;
        }
        String updateQuery = "UPDATE tbl_DIGASSETS_TRANSACTION_MASTER SET Is_Read = " + value + ",TotalViews ="+ data.getTotalViews() +" WHERE DAID = '" + data.getDAID() +"'";
        try {
            DBConnectionOpen();
            database.execSQL(updateQuery);
        }catch(Exception e){
            Log.d("",e.toString());
        } finally {
            DBConnectionClose();

        }
    }

    public void updateIsReadwithlike(DigitalAssetsMaster data){
        int value = 0;
        if(data.is_Read()){
            value = 1;
        }
        String updateQuery = "UPDATE tbl_DIGASSETS_TRANSACTION_MASTER SET Is_Read = " + value + ",TotalViews ="+ data.getTotalViews() +",User_Like="
                + data.getUser_Like() +", TotalLikes="+ data.getTotalLikes() +" WHERE DAID = '" + data.getDAID() +"'";
        try {
            DBConnectionOpen();
            database.execSQL(updateQuery);
        }catch(Exception e){
            Log.d("",e.toString());
        } finally {
            DBConnectionClose();

        }
    }

    public void updateIsLiked(DigitalAssetsMaster data){
        String updateQuery = "UPDATE tbl_DIGASSETS_TRANSACTION_MASTER SET User_Like=" + data.getUser_Like() + "," +
                "TotalLikes="+ data.getTotalLikes() +" WHERE DAID = '" + data.getDAID()+"'";
        try {
            DBConnectionOpen();
            database.execSQL(updateQuery);
        }catch(Exception e){
            Log.d("",e.toString());
        } finally {
            DBConnectionClose();

        }
    }

    public void updateRating(DigitalAssetsMaster data){
        String updateQuery = "UPDATE tbl_DIGASSETS_TRANSACTION_MASTER SET User_Rating=" + data.getUser_Rating() + " WHERE DAID= '" + data.getDAID()+"'";
        try {
            DBConnectionOpen();
            database.execSQL(updateQuery);
        }catch(Exception e){
            Log.d("",e.toString());
        } finally {
            DBConnectionClose();

        }
    }

    public void updatedownloaded(DigitalAssetsMaster data){
        int value = 0;
        if(data.is_Downloaded()){
            value = 1;
        }
        String updateQuery = "UPDATE tbl_DIGASSETS_MASTER SET Is_Downloaded=" + value + ", offlineURL = '"+ data.getOfflineURL() +"' WHERE DAID= '" + data.getDAID()+"'";
        try {
            DBConnectionOpen();
            database.execSQL(updateQuery);
        }catch(Exception e){
            Log.d("",e.toString());
        } finally {
            DBConnectionClose();
        }
    }


    public List<DigitalAssetsMaster> getAssetsBysearch(String searchtext){
        List<DigitalAssetsMaster> listOfAssets = null;
        try{
            String query ="select tbl_DIGASSETS_MASTER.DAID,tbl_DIGASSETS_MASTER.DAName," +
                    "tbl_DIGASSETS_MASTER.DA_Size_In_MB," +
                    "tbl_DIGASSETS_MASTER.DocumentType," +
                    "tbl_DIGASSETS_MASTER.DA_Type," +
                    "tbl_DIGASSETS_MASTER.onlineURL,"+
                    "tbl_DIGASSETS_MASTER.IsDownloadable," +
                    "tbl_DIGASSETS_MASTER.TotalViews," +
                    "tbl_DIGASSETS_MASTER.TotalLikes," +
                    "tbl_DIGASSETS_MASTER.TotalRatings, " +

                    "tbl_DIGASSETS_MASTER.offlineURL, "+
                    "tbl_DIGASSETS_MASTER.Tags, "+
                    "tbl_DIGASSETS_MASTER.DACategoryCode, "+
                    "tbl_DIGASSETS_MASTER.DACategoryName, "+
                    "tbl_DIGASSETS_MASTER.TotalDislikes, "+
                    "tbl_DIGASSETS_MASTER.UDTags, "+
                    "tbl_DIGASSETS_MASTER.DA_Description, "+
                    "tbl_DIGASSETS_MASTER.Is_Read, "+
                    "tbl_DIGASSETS_MASTER.Is_Downloaded, "+
                    "tbl_DIGASSETS_MASTER.User_Like, "+
                    "tbl_DIGASSETS_MASTER.User_Rating ,"+

                    "tbl_DIGASSETS_MASTER.DAMode ,"+
                    "tbl_DIGASSETS_MASTER.OnlineOutPutId ,"+
                    "tbl_DIGASSETS_MASTER.OfflineOutPutId ,"+
                    "tbl_DIGASSETS_MASTER.Uploaded_Date ,"+
                    "tbl_DIGASSETS_MASTER.Uploaded_By ,"+
                    "tbl_DIGASSETS_MASTER.IsViewable ,"+
                    "tbl_DIGASSETS_MASTER.IsSharable ,"+
                    "tbl_DIGASSETS_MASTER.ThumnailURL ,"+
                    "tbl_DIGASSETS_MASTER.DACategoryUsageCount ,"+
                    "tbl_DIGASSETS_MASTER.Is_Asset_Sharable ,"+
                    "tbl_DIGASSETS_MASTER.DA_File_Name ,"+
                    "tbl_DIGASSETS_MASTER.lstAssetImageModel , "+

                    "tbl_DIGASSETS_MASTER.VideoType ,"+
                    "tbl_DIGASSETS_MASTER.Video_Account_Id ,"+
                    "tbl_DIGASSETS_MASTER.VideoId ,"+
                    "tbl_DIGASSETS_MASTER.AccountId ,"+
                    "tbl_DIGASSETS_MASTER.PlayerId ,"+
                    "tbl_DIGASSETS_MASTER.PolicyKey "+

                    "from tbl_DIGASSETS_MASTER WHERE tbl_DIGASSETS_MASTER.DAName LIKE '%" + searchtext +"%'";
            DBConnectionOpen();
            Cursor accompanistCursor = database.rawQuery(query, null);
            listOfAssets = getAllAssetsFromCursor(accompanistCursor);
            accompanistCursor.close();
            //getAccompanistDataAPIListnerCB.getAccompanistDataSuccessCB(accompanists);
            Log.d("parm getAllAccompanistList  size >>> ", String.valueOf(listOfAssets.size()));
        }catch (Exception e){
            Log.d("parm exception getAllAccompanistList  ",e.toString());
            //getAccompanistDataAPIListnerCB.getAccompanistDataFailureCB("Error Occured");
        }finally {
            DBConnectionClose();
        }

        return listOfAssets;
    }

    //Cursor section
    private List<DigitalAssetsMaster> getAllAssetsFromCursor(Cursor assetCursor) {

        List<DigitalAssetsMaster> assetsMasters = new ArrayList<DigitalAssetsMaster>();
        boolean downloadedvalue = false, readvalue = false;
        if (assetCursor != null && assetCursor.getCount() > 0) {
            assetCursor.moveToFirst();
            int daid = assetCursor.getColumnIndex(DAID);
            int name = assetCursor.getColumnIndex(DAName);
            int sizeinmb = assetCursor.getColumnIndex(DA_Size_In_MB);
            int documenttype = assetCursor.getColumnIndex(DocumentType);
            int datype = assetCursor.getColumnIndex(DA_Type);
            int onlineUrl = assetCursor.getColumnIndex(onlineURL);
            int isdownloadable = assetCursor.getColumnIndex(IsDownloadable);
            int totalviews = assetCursor.getColumnIndex(TotalViews);
            int totallikes = assetCursor.getColumnIndex(TotalLikes);
            int totalratings = assetCursor.getColumnIndex(TotalRatings);

            int offline = assetCursor.getColumnIndex(offlineURL);
            int tags = assetCursor.getColumnIndex(Tags);
            int DACategory = assetCursor.getColumnIndex(DACategoryCode);
            int DACatName = assetCursor.getColumnIndex(DACategoryName);
            int totalDislikes = assetCursor.getColumnIndex(TotalDislikes);
            int udTags = assetCursor.getColumnIndex(UDTags);
            int DADescription = assetCursor.getColumnIndex(DA_Description);
            int IsRead = assetCursor.getColumnIndex(Is_Read);
            int IsDownloaded = assetCursor.getColumnIndex(Is_Downloaded);
            int UserLike = assetCursor.getColumnIndex(User_Like);
            int UserRating = assetCursor.getColumnIndex(User_Rating);

            int damode = assetCursor.getColumnIndex(DAMode);
            int onlineOutPutId = assetCursor.getColumnIndex(OnlineOutPutId);
            int offlineOutPutId = assetCursor.getColumnIndex(OfflineOutPutId);
            int uploadedDate = assetCursor.getColumnIndex(Uploaded_Date);
            int uploadedBy = assetCursor.getColumnIndex(Uploaded_By);
            int isViewable = assetCursor.getColumnIndex(IsViewable);
            int isSharable = assetCursor.getColumnIndex(IsSharable);
            int thumnailURL = assetCursor.getColumnIndex(ThumnailURL);
            int daCategoryUsageCount = assetCursor.getColumnIndex(DACategoryUsageCount);
            int Isassetsharable = assetCursor.getColumnIndex(Is_Asset_Sharable);
            int DAfilename = assetCursor.getColumnIndex(DA_File_Name);
            int lstassetimagemodel = assetCursor.getColumnIndex(lstAssetImageModel);

            int videotype = assetCursor.getColumnIndex(VideoType);
            int videoaccntId = assetCursor.getColumnIndex(Video_Account_Id);
            int videoId = assetCursor.getColumnIndex(VideoId);
            int accountId = assetCursor.getColumnIndex(AccountId);
            int playerId = assetCursor.getColumnIndex(PlayerId);
            int policyKey = assetCursor.getColumnIndex(PolicyKey);

            do {
                DigitalAssetsMaster assetsMaster = new DigitalAssetsMaster();
                assetsMaster.setDAID(assetCursor.getString(daid));
                assetsMaster.setDAName(assetCursor.getString(name));
                assetsMaster.setDA_Size_In_MB(assetCursor.getString(sizeinmb));
                assetsMaster.setDocumentType(assetCursor.getString(documenttype));
                assetsMaster.setDA_Type(assetCursor.getString(datype));
                assetsMaster.setOnlineURL(assetCursor.getString(onlineUrl));
                assetsMaster.setIsDownloadable(assetCursor.getString(isdownloadable));
                assetsMaster.setTotalViews(assetCursor.getInt(totalviews));
                assetsMaster.setTotalLikes(assetCursor.getInt(totallikes));
                assetsMaster.setTotalRatings(assetCursor.getInt(totalratings));

                assetsMaster.setOfflineURL(assetCursor.getString(offline));
                assetsMaster.setTags(assetCursor.getString(tags));
                assetsMaster.setDACategoryCode(assetCursor.getString(DACategory));
                assetsMaster.setDACategoryName(assetCursor.getString(DACatName));
                assetsMaster.setTotalDislikes(assetCursor.getInt(totalDislikes));
                assetsMaster.setUDTags(assetCursor.getString(udTags));
                assetsMaster.setDA_Description(assetCursor.getString(DADescription));
                if(assetCursor.getInt(IsRead)==1){
                    readvalue = true;
                }
                assetsMaster.setIs_Read(readvalue);
                if(assetCursor.getInt(IsDownloaded)==1){
                    downloadedvalue = true;
                }
                assetsMaster.setIs_Downloaded(downloadedvalue);
                assetsMaster.setUser_Like(assetCursor.getInt(UserLike));
                assetsMaster.setUser_Rating(assetCursor.getInt(UserRating));

                assetsMaster.setDAMode(assetCursor.getString(damode));
                assetsMaster.setOnlineOutPutId(assetCursor.getString(onlineOutPutId));
                assetsMaster.setOfflineOutPutId(assetCursor.getString(offlineOutPutId));
                assetsMaster.setUploaded_Date(assetCursor.getString(uploadedDate));
                assetsMaster.setUploaded_By(assetCursor.getString(uploadedBy));
                assetsMaster.setIsViewable(assetCursor.getString(isViewable));
                assetsMaster.setIsSharable(assetCursor.getString(isSharable));
                assetsMaster.setThumnailURL(assetCursor.getString(thumnailURL));
                assetsMaster.setDACategoryUsageCount(assetCursor.getInt(daCategoryUsageCount));
                assetsMaster.setIs_Asset_Sharable(assetCursor.getString(Isassetsharable));
                assetsMaster.setDA_File_Name(assetCursor.getString(DAfilename));
                assetsMaster.setLstAssetImageModel(assetCursor.getString(lstassetimagemodel));

                assetsMaster.setVideoType(assetCursor.getString(videotype));
                assetsMaster.setVideo_Account_Id(assetCursor.getString(videoaccntId));
                assetsMaster.setVideoId(assetCursor.getString(videoId));
                assetsMaster.setAccountId(assetCursor.getString(accountId));
                assetsMaster.setPlayerId(assetCursor.getString(playerId));
                assetsMaster.setPolicyKey(assetCursor.getString(policyKey));


                assetsMasters.add(assetsMaster);
            } while (assetCursor.moveToNext());
        }
        return assetsMasters;
    }

    private List<DigitalAssetsMaster> getAllCategoriesFromCursor(Cursor assetCursor) {
        List<DigitalAssetsMaster> assetcategories = new ArrayList<DigitalAssetsMaster>();

        if (assetCursor != null && assetCursor.getCount() > 0) {
            assetCursor.moveToFirst();
            int cat = assetCursor.getColumnIndex(DACategoryName);
            int catcount = assetCursor.getColumnIndex("Count");

            do {
                DigitalAssetsMaster assetsMaster = new DigitalAssetsMaster();
                assetsMaster.setDACategoryName(assetCursor.getString(cat));
                assetsMaster.setCategoryCount(assetCursor.getInt(catcount));

                assetcategories.add(assetsMaster);
            } while (assetCursor.moveToNext());
        }

        return assetcategories;
    }

    private List<DigitalAssetsMaster> getAssetsByCategoryFromCursor(Cursor assetCursor){
        List<DigitalAssetsMaster> assetsMasters = new ArrayList<DigitalAssetsMaster>();
        boolean readvalue = false,downloadedvalue = false;
        if (assetCursor != null && assetCursor.getCount() > 0) {
            assetCursor.moveToFirst();
            int daid = assetCursor.getColumnIndex(DAID);
            int name = assetCursor.getColumnIndex(DAName);
            int sizeinmb = assetCursor.getColumnIndex(DA_Size_In_MB);
            int documenttype = assetCursor.getColumnIndex(DocumentType);
            int datype = assetCursor.getColumnIndex(DA_Type);
            int onlineUrl = assetCursor.getColumnIndex(onlineURL);
            int isdownloadable = assetCursor.getColumnIndex(IsDownloadable);
            int totalviews = assetCursor.getColumnIndex(TotalViews);
            int totallikes = assetCursor.getColumnIndex(TotalLikes);
            int totalratings = assetCursor.getColumnIndex(TotalRatings);

            int offline = assetCursor.getColumnIndex(offlineURL);
            int tags = assetCursor.getColumnIndex(Tags);
            int DACategory = assetCursor.getColumnIndex(DACategoryCode);
            int DACatName = assetCursor.getColumnIndex(DACategoryName);
            int totalDislikes = assetCursor.getColumnIndex(TotalDislikes);
            int udTags = assetCursor.getColumnIndex(UDTags);
            int DADescription = assetCursor.getColumnIndex(DA_Description);
            int IsRead = assetCursor.getColumnIndex(Is_Read);
            int IsDownloaded = assetCursor.getColumnIndex(Is_Downloaded);
            int UserLike = assetCursor.getColumnIndex(User_Like);
            int UserRating = assetCursor.getColumnIndex(User_Rating);

            int damode = assetCursor.getColumnIndex(DAMode);
            int onlineOutPutId = assetCursor.getColumnIndex(OnlineOutPutId);
            int offlineOutPutId = assetCursor.getColumnIndex(OfflineOutPutId);
            int uploadedDate = assetCursor.getColumnIndex(Uploaded_Date);
            int uploadedBy = assetCursor.getColumnIndex(Uploaded_By);
            int isViewable = assetCursor.getColumnIndex(IsViewable);
            int isSharable = assetCursor.getColumnIndex(IsSharable);
            int thumnailURL = assetCursor.getColumnIndex(ThumnailURL);
            int daCategoryUsageCount = assetCursor.getColumnIndex(DACategoryUsageCount);
            int Isassetsharable = assetCursor.getColumnIndex(Is_Asset_Sharable);
            int DAfilename = assetCursor.getColumnIndex(DA_File_Name);
            int lstassetimagemodel = assetCursor.getColumnIndex(lstAssetImageModel);

            int videotype = assetCursor.getColumnIndex(VideoType);
            int videoaccntId = assetCursor.getColumnIndex(Video_Account_Id);
            int videoId = assetCursor.getColumnIndex(VideoId);
            int accountId = assetCursor.getColumnIndex(AccountId);
            int playerId = assetCursor.getColumnIndex(PlayerId);
            int policyKey = assetCursor.getColumnIndex(PolicyKey);

            do {
                DigitalAssetsMaster assetsMaster = new DigitalAssetsMaster();
                assetsMaster.setDAID(assetCursor.getString(daid));
                assetsMaster.setDAName(assetCursor.getString(name));
                assetsMaster.setDA_Size_In_MB(assetCursor.getString(sizeinmb));
                assetsMaster.setDocumentType(assetCursor.getString(documenttype));
                assetsMaster.setDA_Type(assetCursor.getString(datype));
                assetsMaster.setOnlineURL(assetCursor.getString(onlineUrl));
                assetsMaster.setIsDownloadable(assetCursor.getString(isdownloadable));
                assetsMaster.setTotalViews(assetCursor.getInt(totalviews));
                assetsMaster.setTotalLikes(assetCursor.getInt(totallikes));
                assetsMaster.setTotalRatings(assetCursor.getInt(totalratings));

                assetsMaster.setOfflineURL(assetCursor.getString(offline));
                assetsMaster.setTags(assetCursor.getString(tags));
                assetsMaster.setDACategoryCode(assetCursor.getString(DACategory));
                assetsMaster.setDACategoryName(assetCursor.getString(DACatName));
                assetsMaster.setTotalDislikes(assetCursor.getInt(totalDislikes));
                assetsMaster.setUDTags(assetCursor.getString(udTags));
                assetsMaster.setDA_Description(assetCursor.getString(DADescription));
                if(assetCursor.getInt(IsRead)==1){
                    readvalue = true;
                }
                assetsMaster.setIs_Read(readvalue);
                if(assetCursor.getInt(IsDownloaded)==1){
                    downloadedvalue = true;
                }
                assetsMaster.setIs_Downloaded(downloadedvalue);
                assetsMaster.setUser_Like(assetCursor.getInt(UserLike));
                assetsMaster.setUser_Rating(assetCursor.getInt(UserRating));

                assetsMaster.setDAMode(assetCursor.getString(damode));
                assetsMaster.setOnlineOutPutId(assetCursor.getString(onlineOutPutId));
                assetsMaster.setOfflineOutPutId(assetCursor.getString(offlineOutPutId));
                assetsMaster.setUploaded_Date(assetCursor.getString(uploadedDate));
                assetsMaster.setUploaded_By(assetCursor.getString(uploadedBy));
                assetsMaster.setIsViewable(assetCursor.getString(isViewable));
                assetsMaster.setIsSharable(assetCursor.getString(isSharable));
                assetsMaster.setThumnailURL(assetCursor.getString(thumnailURL));
                assetsMaster.setDACategoryUsageCount(assetCursor.getInt(daCategoryUsageCount));
                assetsMaster.setIs_Asset_Sharable(assetCursor.getString(Isassetsharable));
                assetsMaster.setDA_File_Name(assetCursor.getString(DAfilename));
                assetsMaster.setLstAssetImageModel(assetCursor.getString(lstassetimagemodel));

                assetsMaster.setVideoType(assetCursor.getString(videotype));
                assetsMaster.setVideo_Account_Id(assetCursor.getString(videoaccntId));
                assetsMaster.setVideoId(assetCursor.getString(videoId));
                assetsMaster.setAccountId(assetCursor.getString(accountId));
                assetsMaster.setPlayerId(assetCursor.getString(playerId));
                assetsMaster.setPolicyKey(assetCursor.getString(policyKey));

                assetsMasters.add(assetsMaster);
            } while (assetCursor.moveToNext());
        }
        return assetsMasters;
    }

    public void deleteAssetMaster(){
        try{
            DBConnectionOpen();
            database.delete(TABLE_DIGITAL_ASSET_MASTER, null, null);
        }catch (Exception e){

        }finally {
            DBConnectionClose();
        }

    }

}
