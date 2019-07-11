package com.swaas.kangle;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.NetworkUtils;

public class InternetErrorPageActivity extends AppCompatActivity {

    Context mContext;
    TextView retry,offlineassets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_error_page);
        mContext = InternetErrorPageActivity.this;

        initialiseViews();
        onClcikListeners();

        PreferenceUtils.setNWEAvisible(mContext,true);
        if(PreferenceUtils.getVisibleActivityName(mContext).equals("Asset")){
            offlineassets.setVisibility(View.VISIBLE);
        }else{
            offlineassets.setVisibility(View.GONE);
        }
    }

    public void initialiseViews(){
        retry = (TextView) findViewById(R.id.retry);
        offlineassets = (TextView) findViewById(R.id.offlineassets);
    }

    public void onClcikListeners(){
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
                    finish();
                    PreferenceUtils.setNWEAvisible(mContext, false);
                }else{
                    if(NetworkUtils.isNetworkAvailable(mContext)){

                    }
                }
            }
        });

        offlineassets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
