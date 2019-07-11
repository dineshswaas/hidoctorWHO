package com.swaas.kangle;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.swaas.kangle.preferences.PreferenceUtils;

public class ForgotPasswordActivity extends AppCompatActivity {

    ImageView logo;
    EditText emailId,companyname;
    Button submit;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mContext = ForgotPasswordActivity.this;

        initializeViews();
        onClickListeners();

        companyname.setText(PreferenceUtils.getSubdomainName(mContext));
    }

    public void initializeViews(){
        logo = (ImageView) findViewById(R.id.logo);
        emailId = (EditText) findViewById(R.id.email_id);
        companyname = (EditText) findViewById(R.id.company_name);
        submit = (Button) findViewById(R.id.submit);
    }

    public void onClickListeners(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
