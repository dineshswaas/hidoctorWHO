package com.swaas.kangle.Notification;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.swaas.kangle.db.DatabaseHandler;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.utils.NetworkUtils;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Sayellessh on 09-11-2018.
 */

public class NotificationTempRepository {


    private Context mContext;
    private GetNotificationDataCBListner getNotificationDataCBListner;

    private DatabaseHandler dbHandler = null;
    private SQLiteDatabase database = null;
    private Retrofit retrofit;

    public NotificationTempRepository(Context context) {
        dbHandler = new DatabaseHandler(context);
        mContext = context;
    }

    public interface GetNotificationDataCBListner {
        void GetNotificationDataSuccessCB(ArrayList<NotificationModel> customers);
        void GetNotificationDataFailureCB(String message);
    }

    public void setGetNotificationDataCBListner(GetNotificationDataCBListner getNotificationDataCBListners) {
        this.getNotificationDataCBListner = getNotificationDataCBListners;
    }

    public void getNotificationDataFromAPI(String SubdomainName, int CompanyId, int UserId,String type,String offsetFromUtc){
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            NotificationService notificationService = retrofit.create(NotificationService.class);
            Call call = notificationService.getNotifiationHubDetails(SubdomainName, CompanyId, UserId, offsetFromUtc);
            call.enqueue(new Callback<ArrayList<NotificationModel>>() {

                @Override
                public void onResponse(Response<ArrayList<NotificationModel>> response, Retrofit retrofit) {
                    ArrayList<NotificationModel> checkListModels = response.body();
                    if (checkListModels != null && checkListModels.size() > 0) {
                        getNotificationDataCBListner.GetNotificationDataSuccessCB(checkListModels);
                    } else {
                        getNotificationDataCBListner.GetNotificationDataSuccessCB(checkListModels);
                    }
                }
                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(), "Error");
                    getNotificationDataCBListner.GetNotificationDataFailureCB("");
                }
            });
        }
    }


    public void getNotificationHubCountFromApi(String SubdomainName, int CompanyId, int UserId){
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            NotificationService notificationService = retrofit.create(NotificationService.class);

            Call call = notificationService.getNotificationHubCount(SubdomainName, CompanyId, UserId);
            call.enqueue(new Callback<ArrayList<NotificationModel>>() {

                @Override
                public void onResponse(Response<ArrayList<NotificationModel>> response, Retrofit retrofit) {
                    ArrayList<NotificationModel> checkListModels = response.body();
                    if (checkListModels != null && checkListModels.size() > 0) {
                        getNotificationDataCBListner.GetNotificationDataSuccessCB(checkListModels);
                    } else {
                        getNotificationDataCBListner.GetNotificationDataSuccessCB(checkListModels);
                    }
                }
                @Override
                public void onFailure(Throwable t) {
                    Log.d(t.toString(), "Error");
                    getNotificationDataCBListner.GetNotificationDataFailureCB("");
                }
            });
        }
    }
}
