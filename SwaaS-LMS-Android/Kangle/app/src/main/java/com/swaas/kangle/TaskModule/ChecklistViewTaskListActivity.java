package com.swaas.kangle.TaskModule;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.swaas.kangle.CheckList.CheckListQuestionbuilder.QuestionActivity;
import com.swaas.kangle.CheckList.model.UserforCourseChecklist;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.R;
import com.swaas.kangle.TaskModule.adapter.ViewTaskListAdapter;
import com.swaas.kangle.utils.Constants;

import java.util.ArrayList;

public class ChecklistViewTaskListActivity extends AppCompatActivity {

    Toolbar toolbar;
    EmptyRecyclerView recyclerView;
    Context context = this;
    RelativeLayout tasklist_mainView;
    ArrayList<TaskListModel> taskFilteredList;
    ViewTaskListAdapter viewTaskListAdapter;
    LinearLayoutManager LL1;
    RelativeLayout tasklst_empty_view;
    boolean isFromQuestion = false;
    boolean isfromcoursechecklist;
    ArrayList<UserforCourseChecklist> userCourseList;
    int questionId, sectionId, courseId, checkListId,ChecklistGroupId;
    public static final int EDIT_TASK_DATA = 111;
    boolean isGroupChecklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_view_task_list);

        taskFilteredList = new ArrayList<>();
        userCourseList = new ArrayList<>();
        getIntetentData();
        initializeViews();
        setupthemeView();
        setUpRecyclerView();
        loadMyTaskData();

    }

    private void loadMyTaskData()
    {
        if(taskFilteredList != null &&  taskFilteredList.size() > 0)
        {
            viewTaskListAdapter = new ViewTaskListAdapter(context,taskFilteredList);
            recyclerView.setVisibility(View.VISIBLE);
            tasklst_empty_view.setVisibility(View.GONE);
            recyclerView.setAdapter(viewTaskListAdapter);
            loadadapterClick();
        }
        else
        {
            recyclerView.setVisibility(View.GONE);
            tasklst_empty_view.setVisibility(View.VISIBLE);
        }


    }

    private void setUpRecyclerView()
    {
        LL1 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(LL1);
    }

    private void getIntetentData() {
        if(getIntent()!= null)
        {
            taskFilteredList = (ArrayList<TaskListModel>)getIntent().getSerializableExtra("FilterRelatedTaskList");
            isFromQuestion = getIntent().getBooleanExtra("fromquestion", false);
            isfromcoursechecklist = getIntent().getBooleanExtra("isfromcoursechecklist", false);
            questionId = getIntent().getIntExtra("questionId", 0);
            sectionId = getIntent().getIntExtra("sectionId", 0);
            checkListId = getIntent().getIntExtra("checklistId", 0);
            courseId = getIntent().getIntExtra("courseId", 0);
            ChecklistGroupId = getIntent().getIntExtra("ChecklistGroupId",0);
            isGroupChecklist = getIntent().getBooleanExtra("isGroupChecklist",false);
            if(isfromcoursechecklist){
                userCourseList = (ArrayList<UserforCourseChecklist>)getIntent().getSerializableExtra("Userlist");
            }
        }
    }

    private void setupthemeView()
    {
        toolbar.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        tasklist_mainView.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));

        // changed toolbar back icon tint color
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_white_36dp, null);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, Color.parseColor(Constants.TEXT_COLOR));
        getSupportActionBar().setHomeAsUpIndicator(drawable);
    }

    private void initializeViews()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white_36dp));
        getSupportActionBar().setTitle(getString(R.string.viewTasak_list));

        recyclerView = (EmptyRecyclerView) findViewById(R.id.viewTaskrecycler);
        tasklist_mainView = (RelativeLayout) findViewById(R.id.tasklist_mainView);
        tasklst_empty_view = (RelativeLayout) findViewById(R.id.tasklst_empty_view);

    }

    public void loadadapterClick(){
        viewTaskListAdapter.setOnItemClickListener(new ViewTaskListAdapter.MyClickListener() {
            @Override
            public void onItemClick(TaskListModel tsk) {
               gotoViewTaskActivity(tsk);
            }
        });
    }

    private void gotoViewTaskActivity(TaskListModel tsklist) {

        if(taskFilteredList != null && taskFilteredList.size() > 0)
        {
            for(TaskListModel listModel : taskFilteredList)
            {
                if(listModel.equals(tsklist))
                {
                    taskFilteredList.remove(listModel);
                    break;
                }
            }
        }


        Bundle bundle = new Bundle();
        Intent intent = new Intent(context, CreateTaskActivity.class);
        bundle.putSerializable("editTask", tsklist);
        bundle.putSerializable("fromquestion",true);
        bundle.putSerializable("isfromcoursechecklist",isfromcoursechecklist);
        bundle.putSerializable("isFromTaskEdit", true);
        bundle.putSerializable("Userlist", (ArrayList<UserforCourseChecklist>)getIntent().getSerializableExtra("Userlist"));
        bundle.putSerializable("courseId",courseId);
        bundle.putSerializable("questionId",questionId);
        bundle.putSerializable("sectionId",sectionId);
        bundle.putSerializable("checklistId", checkListId);
        bundle.putSerializable("ChecklistGroupId",ChecklistGroupId);
        intent.putExtra("isGroupChecklist",isGroupChecklist);
        intent.putExtras(bundle);
        startActivityForResult(intent, EDIT_TASK_DATA);
    }

    @Override
    public void onBackPressed() {

            Bundle bundle = new Bundle();
            Intent intent = new Intent(context, QuestionActivity.class);
            bundle.putSerializable("UpdatedList", taskFilteredList);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case EDIT_TASK_DATA: {
                    gettingEditedTaskList(data);
                    break;
                }
            }
        }
    }

    private void gettingEditedTaskList(Intent data)
    {
        TaskListModel editeditem = (TaskListModel)data.getSerializableExtra("EditedItem");
        taskFilteredList.add(editeditem);
        viewTaskListAdapter.notifyDataSetChanged();
    }

}
