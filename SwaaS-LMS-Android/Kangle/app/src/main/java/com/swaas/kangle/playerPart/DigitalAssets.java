package com.swaas.kangle.playerPart;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dinesh on 5/23/2017.
 */

public class DigitalAssets implements Serializable {
    int ID;
    int DA_Code;
    int Company_Id;
    int User_Id;
    String DA_Name;
    int Is_Downloadable;
    int DA_Type;
    float DA_Size_In_MB;
    String Uploaded_Date;
    String Uploaded_By;
    String Blob_Url;
    String Status_Action;
    String DA_Category_Name;
    String DA_Thumbnail_URL;
    String DA_Type_Name;
    String DA_Description;
    int Is_Default_Thumbnail;
    String FromDate;
    String ToDate;
    int DA_Detail_Code;
    String DA_Tag_Code;
    String DA_Tag_Value;
    String DA_Tag_Name;
    String Image_Url;
    String Image_Name;
    int Image_Id;
    int Is_Viewable;
    int Is_Shareable;
    int Is_Downloaded;
    String Effective_From;
    String Effective_To;
    int Number_Of_Parts;
    int Total_Played_Time_Duration;
    String DA_FileName;
    String Start_Time;
    String End_Time;
    String EnCoding_Preset;
    int Source_Type;
    long Played_Time_Duration;
    String Played_DateTime;
    String Time_Zone;
    int isPreview;
    int isSynced;
    String SyncedDateTime;
    int DCRId;
    int Visit_Id;
    String DCR_Visit_Code;
    String DCR_Date;
    String DA_Online_URL;
    String DA_Offline_URL, Part_URL, StartTime, EndTime, Player_Start_Time, Player_End_Time;
    boolean isSelected;
    int viewType, SessionId, Customer_Detailed_Id, Part_Id,PreviewSessionId;
    int Total_Viewed_Pages;
    int Total_Unique_Pages_Count;
    int PlayMode, Likes, Rating, id;
    double Latitude, Longitude;
    String Detailed_DateTime;
    String Detailed_StartTime;
    String Detailed_EndTime;
    String Company_Code;
    String User_Code;
    String Customer_Region_Code;
    String Customer_Speciality_Name;
    String Customer_MDL_Number;
    String Customer_Category_Name;
    String Customer_Category_Code;
    String Customer_Speciality_Code;
    int Source_Of_Entry;

    long download_Id;
    int Max_Customer_Detailed_Id;
    int Doc_Type;
    int HDUser_Id;

    //Added for dashboard output
    int Total_Assets;
    int Detailed_Assets;
    int Non_Detailed_Assets;
    String User_Name;
    String Employee_Name;
    String User_Type_Name;
    String CustomerImage_URL;

    //Added for story
    int Display_Order;
    long Story_Id;
    long MC_StoryID = 0;
    long UD_StoryID = 0;
    String Asset_Name;
    List<DigitalAssets> lstAssetDoctor_Details;
    List<DigitalAssets> lstCustomerAsset_Details;
    int Asset_Id;
    int Total_Views;
    int Total_Detail_time_duration;
    int Asset_Type;
    double Asset_Size;
    String Asset_Downloadable;
    int AssetID;
    int Total_Count;

    int User_Like;
    int User_Rating;

    String Feedback;
    int Is_Synched;
    String Updated_Date;
    String Updated_Datetime;

    String Html_Start_Page;

    int Course_Id;
    int Publish_Id;
    int Section_Id;

    int Total_Dislikes;


    //BrightCove Video Property Added
    public String VideoType;
    public String Video_Account_Id;

    public String VideoId;
    public String AccountId;
    public String PlayerId;
    public String PolicyKey;
    public boolean increasePDFSessionId;
    public boolean increaseVideoSessionId;
    public boolean increaseHTMLSessionId;


    public int getPreviewSessionId() {
        return PreviewSessionId;
    }

    public void setPreviewSessionId(int previewSessionId) {
        PreviewSessionId = previewSessionId;
    }

    public boolean isIncreaseHTMLSessionId() {
        return increaseHTMLSessionId;
    }

    public void setIncreaseHTMLSessionId(boolean increaseHTMLSessionId) {
        this.increaseHTMLSessionId = increaseHTMLSessionId;
    }

    public boolean isIncreaseVideoSessionId() {
        return increaseVideoSessionId;
    }

    public void setIncreaseVideoSessionId(boolean increaseVideoSessionId) {
        this.increaseVideoSessionId = increaseVideoSessionId;
    }

    public boolean isIncreasePDFSessionId() {
        return increasePDFSessionId;
    }

    public void setIncreasePDFSessionId(boolean increasePDFSessionId) {
        this.increasePDFSessionId = increasePDFSessionId;
    }

    public String getDA_Type_Name() {
        return DA_Type_Name;
    }

    public void setDA_Type_Name(String DA_Type_Name) {
        this.DA_Type_Name = DA_Type_Name;
    }

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public int getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(int user_Id) {
        User_Id = user_Id;
    }

    public String getVideoType() {
        return VideoType;
    }

    public void setVideoType(String videoType) {
        VideoType = videoType;
    }

    public String getVideo_Account_Id() {
        return Video_Account_Id;
    }

    public void setVideo_Account_Id(String video_Account_Id) {
        Video_Account_Id = video_Account_Id;
    }

    public String getVideoId() {
        return VideoId;
    }

    public void setVideoId(String videoId) {
        VideoId = videoId;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getPlayerId() {
        return PlayerId;
    }

    public void setPlayerId(String playerId) {
        PlayerId = playerId;
    }

    public String getPolicyKey() {
        return PolicyKey;
    }

    public void setPolicyKey(String policyKey) {
        PolicyKey = policyKey;
    }

    //BrightCoveVideo Property

    public int getCourse_Id() {
        return Course_Id;
    }

    public void setCourse_Id(int course_Id) {
        Course_Id = course_Id;
    }

    public int getPublish_Id() {
        return Publish_Id;
    }

    public void setPublish_Id(int publish_Id) {
        Publish_Id = publish_Id;
    }

    public int getSection_Id() {
        return Section_Id;
    }

    public void setSection_Id(int section_Id) {
        Section_Id = section_Id;
    }

    public String getHtml_Start_Page() {
        return Html_Start_Page;
    }

    public void setHtml_Start_Page(String html_Start_Page) {
        Html_Start_Page = html_Start_Page;
    }

    public long getStory_Id() {
        return Story_Id;
    }

    public void setStory_Id(long story_Id) {
        Story_Id = story_Id;
    }

    public long getMC_StoryID() {
        return MC_StoryID;
    }

    public void setMC_StoryID(long MC_StoryID) {
        this.MC_StoryID = MC_StoryID;
    }

    public long getUD_StoryID() {
        return UD_StoryID;
    }

    public void setUD_StoryID(long UD_StoryID) {
        this.UD_StoryID = UD_StoryID;
    }



    public int getDisplay_Order() {
        return Display_Order;
    }

    public void setDisplay_Order(int display_Order) {
        Display_Order = display_Order;
    }

    public int getTotal_Assets() {
        return Total_Assets;
    }

    public void setTotal_Assets(int total_Assets) {
        Total_Assets = total_Assets;
    }

    public int getDetailed_Assets() {
        return Detailed_Assets;
    }

    public void setDetailed_Assets(int detailed_Assets) {
        Detailed_Assets = detailed_Assets;
    }

    public int getNon_Detailed_Assets() {
        return Non_Detailed_Assets;
    }

    public void setNon_Detailed_Assets(int non_Detailed_Assets) {
        Non_Detailed_Assets = non_Detailed_Assets;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getEmployee_Name() {
        return Employee_Name;
    }

    public void setEmployee_Name(String employee_Name) {
        Employee_Name = employee_Name;
    }

    public String getUser_Type_Name() {
        return User_Type_Name;
    }

    public void setUser_Type_Name(String user_Type_Name) {
        User_Type_Name = user_Type_Name;
    }

    public int getHDUser_Id() {
        return HDUser_Id;
    }

    public void setHDUser_Id(int HDUser_Id) {
        this.HDUser_Id = HDUser_Id;
    }

    public int getDoc_Type() {
        return Doc_Type;
    }

    public void setDoc_Type(int doc_Type) {
        Doc_Type = doc_Type;
    }

    public int getMax_Customer_Detailed_Id() {
        return Max_Customer_Detailed_Id;
    }

    public void setMax_Customer_Detailed_Id(int max_Customer_Detailed_Id) {
        Max_Customer_Detailed_Id = max_Customer_Detailed_Id;
    }

    public boolean isPlaying() {
        return IsPlaying;
    }

    public void setPlaying(boolean playing) {
        IsPlaying = playing;
    }

    boolean IsPlaying;

    public long getDownload_Id() {
        return download_Id;
    }

    public void setDownload_Id(long download_Id) {
        this.download_Id = download_Id;
    }

    public String getDetailed_StartTime() {
        return Detailed_StartTime;
    }

    public void setDetailed_StartTime(String detailed_StartTime) {
        Detailed_StartTime = detailed_StartTime;
    }

    public String getDetailed_EndTime() {
        return Detailed_EndTime;
    }

    public void setDetailed_EndTime(String detailed_EndTime) {
        Detailed_EndTime = detailed_EndTime;
    }

    public String getDetailed_DateTime() {
        return Detailed_DateTime;
    }

    public void setDetailed_DateTime(String detailed_DateTime) {
        Detailed_DateTime = detailed_DateTime;
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getPlayMode() {
        return PlayMode;
    }

    public void setPlayMode(int playMode) {
        PlayMode = playMode;
    }

    public int getLike() {
        return Likes;
    }

    public void setLike(int like) {
        Likes = like;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public double getLattitude() {
        return Latitude;
    }

    public void setLattitude(double lattitude) {
        Latitude = lattitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public int getPart_Id() {
        return Part_Id;
    }

    public void setPart_Id(int part_Id) {
        Part_Id = part_Id;
    }

    public String getPart_URL() {
        return Part_URL;
    }

    public void setPart_URL(String part_URL) {
        Part_URL = part_URL;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getPlayer_Start_Time() {
        return Player_Start_Time;
    }

    public void setPlayer_Start_Time(String player_Start_Time) {
        Player_Start_Time = player_Start_Time;
    }

    public String getPlayer_End_Time() {
        return Player_End_Time;
    }

    public void setPlayer_End_Time(String player_End_Time) {
        Player_End_Time = player_End_Time;
    }

    public int getSessionId() {
        return SessionId;
    }

    public void setSessionId(int sessionId) {
        SessionId = sessionId;
    }

    public int getCustomer_Detailed_Id() {
        return Customer_Detailed_Id;
    }

    public void setCustomer_Detailed_Id(int customer_Detailed_Id) {
        Customer_Detailed_Id = customer_Detailed_Id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getDA_Online_URL() {
        return DA_Online_URL;
    }

    public void setDA_Online_URL(String DA_Online_URL) {
        this.DA_Online_URL = DA_Online_URL;
    }

    public String getDA_Offline_URL() {
        return DA_Offline_URL;
    }

    public void setDA_Offline_URL(String DA_Offline_URL) {
        this.DA_Offline_URL = DA_Offline_URL;
    }

    public int getDCRId() {
        return DCRId;
    }

    public void setDCRId(int DCRId) {
        this.DCRId = DCRId;
    }

    public int getVisit_Id() {
        return Visit_Id;
    }

    public void setVisit_Id(int visit_Id) {
        Visit_Id = visit_Id;
    }

    public String getDCR_Visit_Code() {
        return DCR_Visit_Code;
    }

    public void setDCR_Visit_Code(String DCR_Visit_Code) {
        this.DCR_Visit_Code = DCR_Visit_Code;
    }

    public String getDCR_Date() {
        return DCR_Date;
    }

    public void setDCR_Date(String DCR_Date) {
        this.DCR_Date = DCR_Date;
    }

    public long getPlayed_Time_Duration() {
        return Played_Time_Duration;
    }

    public void setPlayed_Time_Duration(long played_Time_Duration) {
        Played_Time_Duration = played_Time_Duration;
    }

    public String getPlayed_DateTime() {
        return Played_DateTime;
    }

    public void setPlayed_DateTime(String played_DateTime) {
        Played_DateTime = played_DateTime;
    }

    public String getTime_Zone() {
        return Time_Zone;
    }

    public void setTime_Zone(String time_Zone) {
        Time_Zone = time_Zone;
    }

    public int getIsPreview() {
        return isPreview;
    }

    public void setIsPreview(int isPreview) {
        this.isPreview = isPreview;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }

    public String getSyncedDateTime() {
        return SyncedDateTime;
    }

    public void setSyncedDateTime(String syncedDateTime) {
        SyncedDateTime = syncedDateTime;
    }

    public int getSource_Type() {
        return Source_Type;
    }

    public void setSource_Type(int source_Type) {
        Source_Type = source_Type;
    }

    public String getEnCoding_Preset() {
        return EnCoding_Preset;
    }

    public void setEnCoding_Preset(String enCoding_Preset) {
        EnCoding_Preset = enCoding_Preset;
    }

    public String getStart_Time() {
        return Start_Time;
    }

    public void setStart_Time(String start_Time) {
        Start_Time = start_Time;
    }

    public String getEnd_Time() {
        return End_Time;
    }

    public void setEnd_Time(String end_Time) {
        End_Time = end_Time;
    }

    public String getDA_FileName() {
        return DA_FileName;
    }

    public void setDA_FileName(String DA_FileName) {
        this.DA_FileName = DA_FileName;
    }

    public int getIs_Viewable() {
        return Is_Viewable;
    }

    public void setIs_Viewable(int is_Viewable) {
        Is_Viewable = is_Viewable;
    }

    public int getIs_Shareable() {
        return Is_Shareable;
    }

    public void setIs_Shareable(int is_Shareable) {
        Is_Shareable = is_Shareable;
    }

    public int getIs_Downloaded() {
        return Is_Downloaded;
    }

    public void setIs_Downloaded(int is_Downloaded) {
        Is_Downloaded = is_Downloaded;
    }

    public String getEffective_From() {
        return Effective_From;
    }

    public void setEffective_From(String effective_From) {
        Effective_From = effective_From;
    }

    public String getEffective_To() {
        return Effective_To;
    }

    public void setEffective_To(String effective_To) {
        Effective_To = effective_To;
    }

    public int getNumber_Of_Parts() {
        return Number_Of_Parts;
    }

    public void setNumber_Of_Parts(int number_Of_Parts) {
        Number_Of_Parts = number_Of_Parts;
    }

    public int getTotal_Duration_in_Seconds() {
        return Total_Played_Time_Duration;
    }

    public void setTotal_Duration_in_Seconds(int total_Duration_in_Seconds) {
        Total_Played_Time_Duration = total_Duration_in_Seconds;
    }

    public int getDA_Code() {
        return DA_Code;
    }

    public void setDA_Code(int DA_Code) {
        this.DA_Code = DA_Code;
    }

    public String getDA_Name() {
        return DA_Name;
    }

    public void setDA_Name(String DA_Name) {
        this.DA_Name = DA_Name;
    }

    public int getIs_Downloadable() {
        return Is_Downloadable;
    }

    public void setIs_Downloadable(int is_Downloadable) {
        Is_Downloadable = is_Downloadable;
    }

    public int getDA_Type() {
        return DA_Type;
    }

    public void setDA_Type(int DA_Type) {
        this.DA_Type = DA_Type;
    }

    public float getDA_Size_In_MB() {
        return DA_Size_In_MB;
    }

    public void setDA_Size_In_MB(float DA_Size_In_MB) {
        this.DA_Size_In_MB = DA_Size_In_MB;
    }

    public String getUploaded_Date() {
        return Uploaded_Date;
    }

    public void setUploaded_Date(String uploaded_Date) {
        Uploaded_Date = uploaded_Date;
    }

    public String getUploaded_By() {
        return Uploaded_By;
    }

    public void setUploaded_By(String uploaded_By) {
        Uploaded_By = uploaded_By;
    }

    public String getBlob_Url() {
        return Blob_Url;
    }

    public void setBlob_Url(String blob_Url) {
        Blob_Url = blob_Url;
    }

    public String getStatus_Action() {
        return Status_Action;
    }

    public void setStatus_Action(String status_Action) {
        Status_Action = status_Action;
    }

    public String getDA_Category_Name() {
        return DA_Category_Name;
    }

    public void setDA_Category_Name(String DA_Category_Name) {
        this.DA_Category_Name = DA_Category_Name;
    }

    public String getDA_Thumbnail_URL() {
        return DA_Thumbnail_URL;
    }

    public void setDA_Thumbnail_URL(String DA_Thumbnail_URL) {
        this.DA_Thumbnail_URL = DA_Thumbnail_URL;
    }

    public String getDA_Description() {
        return DA_Description;
    }

    public void setDA_Description(String DA_Description) {
        this.DA_Description = DA_Description;
    }

    public int getIs_Default_Thumbnail() {
        return Is_Default_Thumbnail;
    }

    public void setIs_Default_Thumbnail(int is_Default_Thumbnail) {
        Is_Default_Thumbnail = is_Default_Thumbnail;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public int getDA_Detail_Code() {
        return DA_Detail_Code;
    }

    public void setDA_Detail_Code(int DA_Detail_Code) {
        this.DA_Detail_Code = DA_Detail_Code;
    }

    public String getDA_Tag_Code() {
        return DA_Tag_Code;
    }

    public void setDA_Tag_Code(String DA_Tag_Code) {
        this.DA_Tag_Code = DA_Tag_Code;
    }

    public String getDA_Tag_Value() {
        return DA_Tag_Value;
    }

    public void setDA_Tag_Value(String DA_Tag_Value) {
        this.DA_Tag_Value = DA_Tag_Value;
    }

    public String getDA_Tag_Name() {
        return DA_Tag_Name;
    }

    public void setDA_Tag_Name(String DA_Tag_Name) {
        this.DA_Tag_Name = DA_Tag_Name;
    }

    public String getImage_Url() {
        return Image_Url;
    }

    public void setImage_Url(String image_Url) {
        Image_Url = image_Url;
    }

    public String getImage_Name() {
        return Image_Name;
    }

    public void setImage_Name(String image_Name) {
        Image_Name = image_Name;
    }

    public int getImage_Id() {
        return Image_Id;
    }

    public void setImage_Id(int image_Id) {
        Image_Id = image_Id;
    }

    public int getTotalViewedPage() {
        return Total_Viewed_Pages;
    }

    public void setTotalViewedPage(int totalViewedPage) {
        this.Total_Viewed_Pages = totalViewedPage;
    }

    public int getUniqueViewedPage() {
        return Total_Unique_Pages_Count;
    }

    public void setUniqueViewedPage(int uniqueViewedPage) {
        this.Total_Unique_Pages_Count = uniqueViewedPage;
    }

    public String getCompany_Code() {
        return Company_Code;
    }

    public void setCompany_Code(String company_Code) {
        Company_Code = company_Code;
    }

    public String getUser_Code() {
        return User_Code;
    }

    public void setUser_Code(String user_Code) {
        User_Code = user_Code;
    }


    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public String getCustomer_Region_Code() {
        return Customer_Region_Code;
    }

    public void setCustomer_Region_Code(String customer_Region_Code) {
        Customer_Region_Code = customer_Region_Code;
    }

    public String getCustomer_Speciality_Name() {
        return Customer_Speciality_Name;
    }

    public void setCustomer_Speciality_Name(String customer_Speciality_Name) {
        Customer_Speciality_Name = customer_Speciality_Name;
    }

    public String getCustomer_Category_Name() {
        return Customer_Category_Name;
    }

    public void setCustomer_Category_Name(String customer_Category_Name) {
        Customer_Category_Name = customer_Category_Name;
    }

    public String getCustomer_Category_Code() {
        return Customer_Category_Code;
    }

    public void setCustomer_Category_Code(String customer_Category_Code) {
        Customer_Category_Code = customer_Category_Code;
    }

    public String getCustomer_Speciality_Code() {
        return Customer_Speciality_Code;
    }

    public void setCustomer_Speciality_Code(String customer_Speciality_Code) {
        Customer_Speciality_Code = customer_Speciality_Code;
    }

    public int getSource_Of_Entry() {
        return Source_Of_Entry;
    }

    public void setSource_Of_Entry(int source_Of_Entry) {
        Source_Of_Entry = source_Of_Entry;
    }

    public String getCustomer_MDL_Number() {
        return Customer_MDL_Number;
    }

    public void setCustomer_MDL_Number(String customer_MDL_Number) {
        Customer_MDL_Number = customer_MDL_Number;
    }

    public String getAsset_Name() {
        return Asset_Name;
    }

    public void setAsset_Name(String asset_Name) {
        Asset_Name = asset_Name;
    }

    public List<DigitalAssets> getLstAssetDoctor_Details() {
        return lstAssetDoctor_Details;
    }

    public void setLstAssetDoctor_Details(List<DigitalAssets> lstAssetDoctor_Details) {
        this.lstAssetDoctor_Details = lstAssetDoctor_Details;
    }

    public List<DigitalAssets> getLstCustomerAsset_Details() {
        return lstCustomerAsset_Details;
    }

    public void setLstCustomerAsset_Details(List<DigitalAssets> lstCustomerAsset_Details) {
        this.lstCustomerAsset_Details = lstCustomerAsset_Details;
    }

    public int getAsset_Id() {
        return Asset_Id;
    }

    public void setAsset_Id(int asset_Id) {
        Asset_Id = asset_Id;
    }

    public int getTotal_Views() {
        return Total_Views;
    }

    public void setTotal_Views(int total_Views) {
        Total_Views = total_Views;
    }

    public int getTotal_Detail_time_duration() {
        return Total_Detail_time_duration;
    }

    public void setTotal_Detail_time_duration(int total_Detail_time_duration) {
        Total_Detail_time_duration = total_Detail_time_duration;
    }

    public int getAsset_Type() {
        return Asset_Type;
    }

    public void setAsset_Type(int asset_Type) {
        Asset_Type = asset_Type;
    }

    public double getAsset_Size() {
        return Asset_Size;
    }

    public void setAsset_Size(double asset_Size) {
        Asset_Size = asset_Size;
    }

    public String getAsset_Downloadable() {
        return Asset_Downloadable;
    }

    public void setAsset_Downloadable(String asset_Downloadable) {
        Asset_Downloadable = asset_Downloadable;
    }

    public String getCustomerImage_URL() {
        return CustomerImage_URL;
    }

    public void setCustomerImage_URL(String customerImage_URL) {
        CustomerImage_URL = customerImage_URL;
    }

    public int getAssetID() {
        return AssetID;
    }

    public void setAssetID(int assetID) {
        AssetID = assetID;
    }

    public int getTotal_Count() {
        return Total_Count;
    }

    public void setTotal_Count(int total_Count) {
        Total_Count = total_Count;
    }

    public int getUser_Like() {
        return User_Like;
    }

    public void setUser_Like(int user_Like) {
        User_Like = user_Like;
    }

    public String getFeedback() {
        return Feedback;
    }

    public void setFeedback(String feedback) {
        Feedback = feedback;
    }

    public int getIs_Synched() {
        return Is_Synched;
    }

    public void setIs_Synched(int is_Synched) {
        Is_Synched = is_Synched;
    }

    public String getUpdated_Date() {
        return Updated_Date;
    }

    public void setUpdated_Date(String updated_Date) {
        Updated_Date = updated_Date;
    }

    public String getUpdated_Datetime() {
        return Updated_Datetime;
    }

    public void setUpdated_Datetime(String updated_Datetime) {
        Updated_Datetime = updated_Datetime;
    }

    public int getTotal_Dislikes() {
        return Total_Dislikes;
    }

    public void setTotal_Dislikes(int total_Dislikes) {
        Total_Dislikes = total_Dislikes;
    }

    public int getUser_Rating() {
        return User_Rating;
    }

    public void setUser_Rating(int user_Rating) {
        User_Rating = user_Rating;
    }
}
