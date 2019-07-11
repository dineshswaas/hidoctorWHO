package com.swaas.kangle;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.API.service.UserService;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.models.UpdatePasswordModel;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.NetworkUtils;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ChangePasswordActivity extends AppCompatActivity {

    Context mContext;
    EditText oldpassword,newpassword,confirmpassword;
    Button changesubmit;
    RelativeLayout parent;
    LinearLayout header;
    ImageView companylogo;
    Gson gsonget;
    ProgressDialog mProgressDialog;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mContext = ChangePasswordActivity.this ;
        //getSupportActionBar().hide();

        initaliseViews();
        bindOnCLickEvent();
        setthemeforView();
        loadViews();
    }

    public void initaliseViews(){
        header = (LinearLayout) findViewById(R.id.header);
        parent = (RelativeLayout) findViewById(R.id.parent);
        oldpassword = (EditText) findViewById(R.id.oldpassword);
        newpassword = (EditText) findViewById(R.id.newpassword);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);
        changesubmit = (Button) findViewById(R.id.newpasswordsubmit);
        companylogo = (ImageView) findViewById(R.id.companylogo);
        title = (TextView) findViewById(R.id.title);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void bindOnCLickEvent(){
        changesubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldpassword.getText().toString().trim().length() == 0
                        || newpassword.getText().toString().trim().length() == 0
                        || confirmpassword.getText().toString().trim().length() == 0){
                    Toast.makeText(mContext,mContext.getResources().getString(R.string.allfieldsare),Toast.LENGTH_SHORT).show();
                } else {
                    if(oldpassword.getText().toString().trim().equalsIgnoreCase(newpassword.getText().toString().trim())) {
                        Toast.makeText(mContext,mContext.getResources().getString(R.string.newpasswordcannot),Toast.LENGTH_SHORT).show();
                    } else if(!newpassword.getText().toString().trim().equalsIgnoreCase(confirmpassword.getText().toString().trim())){
                        Toast.makeText(mContext,mContext.getResources().getString(R.string.passworddoesnot),Toast.LENGTH_SHORT).show();
                    }else{
                        changepassword();
                    }
                }
            }
        });

        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        oldpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (oldpassword.getCompoundDrawables()[2] != null) {
                    boolean tappedX = event.getX() > (oldpassword.getWidth() - oldpassword.getPaddingRight() - oldpassword.getCompoundDrawables()[2].getIntrinsicWidth());
                    if (tappedX) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (oldpassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                                oldpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            } else {
                                oldpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            }

                            oldpassword.setSelection(oldpassword.getText().toString().trim().length());
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        newpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (newpassword.getCompoundDrawables()[2] != null) {
                    boolean tappedX = event.getX() > (newpassword.getWidth() - newpassword.getPaddingRight() - newpassword.getCompoundDrawables()[2].getIntrinsicWidth());
                    if (tappedX) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (newpassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                                newpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            } else {
                                newpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            }
                            newpassword.setSelection(newpassword.getText().toString().trim().length());
                        }
                        return true;
                    }
                }
                return false;
            }
        });

    }

    public void setthemeforView(){

        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        companylogo.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        parent.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        changesubmit.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        changesubmit.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        title.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
    }

    public void loadViews(){

        oldpassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (oldpassword.getText().toString().trim().length() == 1) {
                    if (oldpassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        oldpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_visibility_black_24dp, 0);
                    } else {
                        oldpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_visibility_off_black_24dp, 0);
                    }
                    oldpassword.setCompoundDrawablePadding(24);
                } else if (oldpassword.getText().toString().trim().length() == 0) {
                    oldpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    if (oldpassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        oldpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_visibility_black_24dp, 0);
                    } else {
                        oldpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_visibility_off_black_24dp, 0);
                    }
                    oldpassword.setCompoundDrawablePadding(24);
                }
            }
        });

        newpassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (newpassword.getText().toString().trim().length() == 1) {
                    if (newpassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        newpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_visibility_black_24dp, 0);
                    } else {
                        newpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_visibility_off_black_24dp, 0);
                    }
                    newpassword.setCompoundDrawablePadding(24);
                } else if (newpassword.getText().toString().trim().length() == 0) {
                    newpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    if (newpassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        newpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_visibility_black_24dp, 0);
                    } else {
                        newpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_visibility_off_black_24dp, 0);
                    }
                    newpassword.setCompoundDrawablePadding(24);
                }
            }
        });
    }

    public void changepassword(){
        if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
            showProgressDialog();
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            UserService userService = retrofitAPI.create(UserService.class);

            gsonget = new Gson();
            User userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);
            String subdomainName = PreferenceUtils.getSubdomainName(mContext);
            UpdatePasswordModel updatePasswordModel = new UpdatePasswordModel();
            updatePasswordModel.setCompany_Id(userobj.getCompany_Id());
            updatePasswordModel.setCompany_Code(userobj.getCompany_Code());
            updatePasswordModel.setUser_Name(userobj.getFirstName());
            updatePasswordModel.setUser_Code(userobj.getUser_Code());
            updatePasswordModel.setOld_Password(oldpassword.getText().toString().trim());
            updatePasswordModel.setNew_Password(newpassword.getText().toString().trim());
            updatePasswordModel.setConfirm_Password(confirmpassword.getText().toString().trim());

            Call call = userService.updatePassword(subdomainName,userobj.getCompany_Id(),updatePasswordModel);
            call.enqueue(new Callback<UpdatePasswordModel>() {

                @Override
                public void onResponse(Response<UpdatePasswordModel> response, Retrofit retrofit) {
                    UpdatePasswordModel apiResponse = response.body();
                    if (apiResponse != null) {
                        if(apiResponse.isStatus()){
                            dismissProgressDialog();
                            oldpassword.setText("");newpassword.setText("");confirmpassword.setText("");
                            Toast.makeText(mContext,apiResponse.getMessage().toString(),Toast.LENGTH_SHORT).show();
                        } else {
                            dismissProgressDialog();
                            Toast.makeText(mContext,apiResponse.getMessage().toString(),Toast.LENGTH_SHORT).show();
                        }
                        Log.d("log","assetsForBrowses");
                    } else {
                        dismissProgressDialog();
                        Log.d("retrofit","error 2");
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    dismissProgressDialog();
                    Log.d("retrofit","error 2");
                }
            });
        } else {
            Toast.makeText(mContext,getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
        }
    }

    public void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
