package com.swaas.kangle.db.Contract;

import android.provider.BaseColumns;

/**
 * Created by Sayellessh on 10-08-2017.
 */

public class AssetAnalyticsContract {

    public static final class DigitalAssetAnalyticsDetails implements BaseColumns {

        public static final String TABLE_NAME = "tran_Asset_Analytics_Details";

        public static final String DA_CODE = "DA_Code";
        public static final String PART_ID = "Part_Id";
        public static final String PLAYED_TIME_DURATION = "Played_Time_Duration";
        public static final String DETAILED_DATETIME = "Detailed_DateTime";
        public static final String TIME_ZONE = "Time_Zone";
        public static final String IS_PREVIEW = "is_Preview";
        public static final String IS_SYNCED = "is_Synced";
        public static final String SYNCED_DATETIME = "Synced_DateTime";
        public static final String PART_URL = "Part_URL";
        public static final String SESSIONID = "SessionId";
        public static final String DETAILED_STARTTIME = "Detailed_StartTime";
        public static final String DETAILED_ENDTIME = "Detailed_EndTime";
        public static final String PLAYER_START_TIME = "Player_Start_Time";
        public static final String PLAYER_END_TIME = "Player_End_Time";
        public static final String CUSTOMER_DETAILED_ID = "Customer_Detailed_Id";
        public static final String PLAYMODE = "PlayMode";
        public static final String LIKE = "Like";
        public static final String RATING = "Rating";
        public static final String LATITUDE = "Latitude";
        public static final String LONGITUDE = "Longitude";

        public static final String COURSE_ID = "Course_Id";
        public static final String SECTION_ID = "Section_Id";
        public static final String PUBLISH_ID = "Publish_Id";

        public static final String DA_TYPE = "DA_Type";
    }

    public static final class DigitalAssetAnalytics implements BaseColumns {

        public static final String TABLE_NAME = "tran_Digital_Asset_Analytics";

        public static final String DA_CODE = "DA_Code";
        public static final String PART_ID = "Part_Id";
        public static final String PLAYED_TIME_DURATION = "Played_Time_Duration";
        public static final String DETAILED_DATETIME = "Detailed_DateTime";
        public static final String TIME_ZONE = "Time_Zone";
        public static final String IS_PREVIEW = "is_Preview";
        public static final String IS_SYNCED = "is_Synced";
        public static final String SYNCED_DATETIME = "Synced_DateTime";
        public static final String PART_URL = "Part_URL";
        public static final String SESSIONID = "SessionId";
        public static final String DETAILED_STARTTIME = "Detailed_StartTime";
        public static final String DETAILED_ENDTIME = "Detailed_EndTime";
        public static final String PLAYER_START_TIME = "Player_Start_Time";
        public static final String PLAYER_END_TIME = "Player_End_Time";
        public static final String CUSTOMER_DETAILED_ID = "Customer_Detailed_Id";
        public static final String PLAYMODE = "PlayMode";
        public static final String LIKE = "Like";
        public static final String RATING = "Rating";
        public static final String LATITUDE = "Latitude";
        public static final String LONGITUDE = "Longitude";
        public static final String DA_TYPE = "DA_Type";
        public static final String TOTAL_DISLIKE = "TotalDislikes";
        public static final String USER_LIKE = "User_Like";
        public static final String USER_RATING = "User_Rating";
        public static final String TOTAL_VIEWS = "TotalViews";
        public static final String Is_Downloaded = "Is_Downloaded";
        public static final String COURSE_ID = "Course_ID";
        public static final String SECTION_ID = "Section_Id";
    }


    public static final class CustomerDAFeedback implements BaseColumns {

        public static final String TABLE_NAME = "tbl_EL_Customer_DA_Feedback";

        public static final String COURSE_ID = "Course_Id";
        public static final String SECTION_ID = "Section_Id";
        public static final String DA_CODE = "DA_Code";
        public static final String DA_TYPE = "DA_Type";
        public static final String RATING = "Rating";
        public static final String USER_LIKE = "User_Like";
        public static final String FEEDBACK = "Feedback";
        public static final String IS_SYNCHED = "Is_Synched";
        public static final String SOURCE_OF_ENTRY = "Source_Of_Entry";
        public static final String TIME_ZONE = "Time_Zone";
        public static final String UPDATED_DATE = "Updated_Date";
        public static final String UPDATED_DATETIME = "Updated_Datetime";
    }

}
