package com.swaas.kangle;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.swaas.kangle.preferences.PreferenceUtils;

public class CompanyEntryActivity extends AppCompatActivity {

    ImageView logo;
    EditText companyName;
    Button submit;
    Context mContext;
    String company;
    boolean showerror;
    String errormessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_entry);

        mContext = CompanyEntryActivity.this;
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if(getIntent() != null){
            showerror = getIntent().getBooleanExtra("showerror",false);
            errormessage = getIntent().getStringExtra("errormessage");
        }
        initializeViews();
        onClickListeners();

        if(showerror){
            companyName.setError(errormessage);
        }
    }

    public void initializeViews(){
        logo = (ImageView) findViewById(R.id.logo);
        companyName = (EditText) findViewById(R.id.company_name);
        submit = (Button) findViewById(R.id.submit);
    }

    public void onClickListeners(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                company = companyName.getText().toString().toLowerCase().trim();
                if(company.length() == 0 || company ==  null){
                    companyName.setError(getResources().getString(R.string.please_enter_companyname));
                }else{
                    setPreference();
                }
            }
        });
    }

    public void setPreference(){
        boolean removekanglename = company.contains(".kangle.me");
        String fullname = "";
        if(removekanglename) {
            fullname = (company);
        }else{
            fullname = (company + ".kangle.me");
        }
        final String companyfullname = fullname;
        PreferenceUtils.setSubdomainName(mContext,companyfullname);
        gotoLoginActivity();
    }

    public void gotoLoginActivity(){
        Intent login = new Intent(this,LoginActivity.class);
        startActivity(login);
        finish();
    }
}
