package com.swaas.kangle.TaskModule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swaas.kangle.R;
import com.swaas.kangle.TaskModule.adapter.ChildUserListAdapter;
import com.swaas.kangle.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by barath on 11/27/2018.
 */

    public class UserListActivity extends AppCompatActivity {

    Context mContext;
    ImageView backbtn,select_all,close,search;
    RelativeLayout header,searchview;
    TextView submit,headertext;
    EditText searchinput;
    RecyclerView userslist;
    View overall;
    ArrayList<TaskListModel> mUsersListItem,searchlist;
    ChildUserListAdapter childUserListAdapter;
    LinearLayoutManager LL1;
    boolean check;
    String searchtext;
    public static ArrayList<TaskListModel> userslistfinal;
    public static ArrayList<TaskListModel> selecteduser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlist_activity);
        mContext = UserListActivity.this;
        initializeviews();
        getIntentData();
        setUpRecyclerView();
        setupList();
        setuptheme();
        onClicklistener();
        check=true;
        if(mUsersListItem != null && mUsersListItem.size() >0){
            userslist.setItemViewCacheSize(mUsersListItem.size());
        }
    }

    private void setuptheme() {
        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        submit.setTextColor(Color.parseColor(Constants.COMPANY_COLOR));
        userslist.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        overall.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        searchview.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
    }

    public void setUpRecyclerView(){
        LL1 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        userslist.setLayoutManager(LL1);
    }
    private void getIntentData() {
       // mUsersListItem = (ArrayList<TaskListModel>) getIntent().getSerializableExtra("userlist");
        mUsersListItem= CreateTaskActivity.getUserslistfinal();
    }

    private void setupList() {
        if(searchlist!=null&&searchlist.size()>0) {
            int i,j;
            for ( i=0;i<mUsersListItem.size();i++) {
                for(j=0;j<searchlist.size();j++) {
                    if(searchlist.get(j).getUser_Id()==mUsersListItem.get(i).getUser_Id()&&searchlist.get(j).isUserchecked()) {
                        mUsersListItem.get(i).setUserchecked(true);
                    }
                    else if(searchlist.get(j).getUser_Id()==mUsersListItem.get(i).getUser_Id()&& !searchlist.get(j).isUserchecked()) {
                        mUsersListItem.get(i).setUserchecked(false);
                    }
                }
            }
        }

        childUserListAdapter = new ChildUserListAdapter(mContext, (List<TaskListModel>) mUsersListItem);
        userslist.setVisibility(View.VISIBLE);
        userslist.setAdapter(childUserListAdapter);
    }

    private void initializeviews() {
        backbtn=(ImageView) findViewById(R.id.backbutton);
        select_all=(ImageView)findViewById(R.id.select_all);
        submit=(TextView)findViewById(R.id.submit_tsk);
        userslist=(RecyclerView)findViewById(R.id.user_list);
        header=findViewById(R.id.header);
        overall=findViewById(R.id.overall);
        searchview=findViewById(R.id.searchview);
        close=(ImageView)findViewById(R.id.close);
        searchinput=(EditText)findViewById(R.id.textinput);
        search=(ImageView)findViewById(R.id.search);
        headertext=(TextView)findViewById(R.id.headertext);
    }

    private void onClicklistener() {
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<TaskListModel> checkUsers = new ArrayList();
                if(check) {
                    for(TaskListModel t : mUsersListItem){
                        t.setUserchecked(true);
                        checkUsers.add(t);
                    }
                    mUsersListItem = checkUsers;
                    childUserListAdapter.notifyDataSetChanged();
                    select_all.setImageResource(R.mipmap.ic_green_selected);
                    check=false;
                } else {
                    for(TaskListModel t : mUsersListItem){
                        t.setUserchecked(false);
                        checkUsers.add(t);
                    }
                    mUsersListItem = checkUsers;
                    childUserListAdapter.notifyDataSetChanged();
                    select_all.setImageResource(R.drawable.baseline_select_all_white_18);
                    check=true;
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                header.setVisibility(View.VISIBLE);
                searchview.setVisibility(View.VISIBLE);
                backbtn.setVisibility(View.GONE);
                select_all.setVisibility(View.GONE);
                close.setVisibility(View.VISIBLE);
                searchinput.setVisibility(View.VISIBLE);
                headertext.setVisibility(View.GONE);
                searchinput.setText("");

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                header.setVisibility(View.VISIBLE);
                searchview.setVisibility(View.GONE);
                backbtn.setVisibility(View.VISIBLE);
                select_all.setVisibility(View.VISIBLE);
                close.setVisibility(View.GONE);
                searchinput.setVisibility(View.GONE);
                headertext.setVisibility(View.VISIBLE);
                setupList();
            }
        });

        searchinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = String.valueOf(s);
                filter(a);
            }
        });
    }

    public void filter(String s) {
        if(searchlist!=null) {
            searchlist.clear();
        }
        searchlist= new ArrayList<TaskListModel>();
        TaskListModel t = new TaskListModel();
        if(s!=null&&s.length()>0) {
            for (int i = 0; i < mUsersListItem.size(); i++) {
                if (mUsersListItem.get(i).getUser_Name().toLowerCase().contains(s.toLowerCase())) {
                    t.setUser_Id(mUsersListItem.get(i).getUser_Id());
                    t.setUser_Name(mUsersListItem.get(i).getUser_Name());
                    t.setUserchecked(mUsersListItem.get(i).isUserchecked());
                }
            }
            searchlist.add(t);
            if(searchlist!=null&&searchlist.size()>0)
            {
                childUserListAdapter = new ChildUserListAdapter(mContext, (List<TaskListModel>) searchlist);
                userslist.setAdapter(childUserListAdapter);
            }
        }
        else {
            setupList();
        }
    }

    private void submit() {
        childUserListAdapter.notifyDataSetChanged();
        mUsersListItem = (ArrayList<TaskListModel>) getselecteduserlist();
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("result",null);
        returnIntent.putExtras(bundle);
        selecteduser=mUsersListItem;
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    public ArrayList<TaskListModel> getselecteduserlist(){
        ArrayList<TaskListModel> selectedusers = new ArrayList();
        for(TaskListModel user : mUsersListItem){
            if(user.isUserchecked()){
                selectedusers.add(user);
            }
        }
        return selectedusers;
    }
    public static ArrayList<TaskListModel> getSelecteduser()
    {
        return selecteduser;
    }
}
