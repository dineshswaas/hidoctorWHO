package com.swaas.kangle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.NetworkUtils;

/**
 * Created by Sayellessh on 27-08-2018.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("alert NetworkChangeReceiver","message");
        if(!NetworkUtils.checkIfNetworkAvailable(context)){
            if(!PreferenceUtils.getNWEAvisible(context)){
                Intent i = new Intent(context,InternetErrorPageActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }
    }
}
