package com.swaas.kangle.db;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Sayellessh on 20-04-2018.
 */

public class RetrofitCustomApiBuilder {

    private String mcustomURL;
    public RetrofitCustomApiBuilder(String customURL){
        mcustomURL = customURL;
    }

    Retrofit retrofit = null;
    public synchronized Retrofit getInstance() {

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(600, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(600, TimeUnit.SECONDS);

        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(mcustomURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}
