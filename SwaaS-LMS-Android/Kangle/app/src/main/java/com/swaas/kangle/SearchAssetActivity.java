package com.swaas.kangle;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;

public class SearchAssetActivity extends AppCompatActivity {

    RelativeLayout filter1_background,filter2_background,filter3_background,filter4_background,sort_background,order_background;
    LinearLayout filter1,filter2,filter3,filter4,sortlayout,orderlayout;
    LinearLayout typesView,weeksView;
    RadioGroup radiogroup;
    RadioButton today,thisweek,twoweeks,allweeks;
    CheckBox document,image,audio,video;
    Context mContext;
    ImageView icon_readunread,icon_weeks,icon_onlinedownloaded,icon_type,icon_order,icon_sort,companylogo;
    TextView readunread,weeks,onlinedownloaded,type,sort,order;
    RadioButton selectedradioButton;
    Button submitbutton;

    LinearLayout header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_asset);

        mContext = SearchAssetActivity.this;
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //getSupportActionBar().hide();
        initialiseViews();
        onBindClickEvents();
       /* if(PreferenceUtils.getSubdomainName(mContext).contains("tacobell")){
            header.setBackgroundColor(getResources().getColor(R.color.tacobellbackground));
        }else {
            header.setBackgroundColor(getResources().getColor(R.color.colorgreenbar));
        }*/
        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));

    }

    public void initialiseViews(){
        filter1_background = (RelativeLayout)findViewById(R.id.filter1_background);
        filter2_background = (RelativeLayout)findViewById(R.id.filter2_background);
        filter3_background = (RelativeLayout)findViewById(R.id.filter3_background);
        filter4_background = (RelativeLayout)findViewById(R.id.filter4_background);
        sort_background = (RelativeLayout)findViewById(R.id.sort_background);
        order_background = (RelativeLayout)findViewById(R.id.order_background);
        filter1 = (LinearLayout)findViewById(R.id.filter1);
        filter2 = (LinearLayout)findViewById(R.id.filter2);
        filter3 = (LinearLayout)findViewById(R.id.filter3);
        filter4 = (LinearLayout)findViewById(R.id.filter4);
        sortlayout = (LinearLayout)findViewById(R.id.sortlayout);
        orderlayout = (LinearLayout)findViewById(R.id.orderlayout);
        today = (RadioButton)findViewById(R.id.today);
        thisweek = (RadioButton)findViewById(R.id.thisweek);
        twoweeks = (RadioButton)findViewById(R.id.twoweeks);
        allweeks = (RadioButton)findViewById(R.id.allweeks);
        document = (CheckBox)findViewById(R.id.document);
        image = (CheckBox)findViewById(R.id.image);
        audio = (CheckBox)findViewById(R.id.audio);
        video = (CheckBox)findViewById(R.id.video);
        icon_readunread = (ImageView)findViewById(R.id.icon_readunread);
        icon_weeks = (ImageView)findViewById(R.id.icon_weeks);
        icon_onlinedownloaded = (ImageView)findViewById(R.id.icon_onlinedownloaded);
        icon_type = (ImageView)findViewById(R.id.icon_type);
        icon_order = (ImageView)findViewById(R.id.icon_order);
        icon_sort = (ImageView)findViewById(R.id.icon_sort);
        readunread = (TextView)findViewById(R.id.readunread);
        weeks = (TextView)findViewById(R.id.weeks);
        onlinedownloaded = (TextView)findViewById(R.id.onlinedownloaded);
        type = (TextView)findViewById(R.id.type);
        sort = (TextView)findViewById(R.id.sort);
        order = (TextView)findViewById(R.id.order);
        submitbutton = (Button) findViewById(R.id.submitbutton);
        companylogo = (ImageView) findViewById(R.id.companylogo);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        typesView = (LinearLayout)findViewById(R.id.typesView); ;
        weeksView = (LinearLayout)findViewById(R.id.weeksView);;

        radiogroup.check(R.id.allweeks);

        header = (LinearLayout) findViewById(R.id.header);
    }

    public void onBindClickEvents(){
        filter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typesView.setVisibility(View.GONE);
                weeksView.setVisibility(View.VISIBLE);
            }
        });

        filter4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typesView.setVisibility(View.VISIBLE);
                weeksView.setVisibility(View.GONE);
            }
        });

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radiogroup.getCheckedRadioButtonId();
                selectedradioButton = (RadioButton) findViewById(selectedId);
                Toast.makeText(SearchAssetActivity.this,selectedradioButton.getText(),Toast.LENGTH_SHORT).show();

            }

        });

        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
