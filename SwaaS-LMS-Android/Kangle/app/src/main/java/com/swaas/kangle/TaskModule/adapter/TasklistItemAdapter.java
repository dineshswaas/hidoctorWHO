package com.swaas.kangle.TaskModule.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swaas.kangle.R;
import com.swaas.kangle.TaskModule.TaskListModel;
import com.swaas.kangle.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Sayellessh on 13-11-2018.
 */

public class TasklistItemAdapter extends RecyclerView.Adapter<TasklistItemAdapter.RecyclerHolder> {

    Context context;
    List<TaskListModel> taskListModelList;
    private static MyClickListener myClickListener;
    public int fromlist;

    public TasklistItemAdapter(Context context, List<TaskListModel> taskModels, int Type) {
        this.context = context;
        this.taskListModelList = taskModels;
        this.fromlist = Type;
    }

    public interface MyClickListener{
        public void onItemClick(TaskListModel tsk,int frompage);

    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_list_item, parent, false);
        return new RecyclerHolder(view,context);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        final TaskListModel taskListModel = taskListModelList.get(position);

        setthemeforView(holder);

        holder.task_name.setText(taskListModel.getTask_Name().toString());
        holder.priority.setText(getPriorityValue(taskListModel.getTask_Priority()));
        if(taskListModel.getAssigned_To() != null){
            holder.ass_to.setText(context.getResources().getString(R.string.assignedto)+taskListModel.getAssigned_To().toString());
        }else{
            holder.ass_to.setText(context.getResources().getString(R.string.assignedto));
        }
        holder.ass_by.setText(context.getResources().getString(R.string.assignedby)+taskListModel.getAssigned_By().toString());
        holder.status.setText(taskListModel.getTaskStatus().toString());
        if(taskListModel.getTaskStatus().equalsIgnoreCase(Constants.Task_open_Text)){
            holder.status.setTextColor(Color.parseColor(Constants.YET_TO_START_COLOR));
        }else if(taskListModel.getTaskStatus().equalsIgnoreCase(Constants.Task_Inprogress_Text)){
            holder.status.setTextColor(Color.parseColor(Constants.INPROGRESS_COLOR));
        }else if(taskListModel.getTaskStatus().equalsIgnoreCase(Constants.Task_Review_Text)){
            holder.status.setTextColor(Color.parseColor(Constants.PENDING_APPROVAl_COLOR));
        }else if(taskListModel.getTaskStatus().equalsIgnoreCase(Constants.Task_Completed_Text)){
            holder.status.setTextColor(Color.parseColor(Constants.COMPLETED_COLOR));
        }else{
            holder.status.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        }
        /*try {
            holder.due_date.setText(getFormatedDate(taskListModel.getTask_Due_Date()).toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        holder.due_date.setText(taskListModel.getFormatedDueDate().toString());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myClickListener != null) {
                    myClickListener.onItemClick(taskListModel,fromlist);
                }
            }
        });
    }

    public void setOnItemClickListener(MyClickListener myClickListener){
        this.myClickListener = myClickListener;
    }
    public String getPriorityValue(int value){
        String Priority_string = "";
        if(value == 2){
            Priority_string = context.getResources().getString(R.string.medium);
        }else if(value == 3){
            Priority_string = context.getResources().getString(R.string.high);
        }else{
            Priority_string =context.getResources().getString(R.string.low);
        }
        return Priority_string;
    }

    public String getFormatedDate(String duedate) throws ParseException {
        String onlydate = duedate;
        int t = onlydate.indexOf("T");
        String nd = onlydate.substring(0,t);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = (Date)formatter.parse(nd);
        SimpleDateFormat convertFormat = new SimpleDateFormat("dd-MMM-yyyy");
        return convertFormat.format(date);
    }

    @Override
    public int getItemCount() {
        return taskListModelList.size();
    }

    public void setthemeforView(final RecyclerHolder holder){

        holder.mView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.CARDBACKGROUND_COLOR)));
        //holder.mView.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
        holder.task_name.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.priority.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.ass_to.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.ass_by.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.status.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.due_date.setTextColor(Color.parseColor(Constants.TEXT_COLOR));

    }


    public class RecyclerHolder extends RecyclerView.ViewHolder{
        Context ctxt;
        LinearLayout mView;
        TextView task_name,priority,ass_to,ass_by,status,due_date;

        public RecyclerHolder(View itemView,Context context) {
            super(itemView);
            this.ctxt = context;
            mView =(LinearLayout) itemView.findViewById(R.id.mView);
            task_name = (TextView) itemView.findViewById(R.id.task_name);
            priority = (TextView) itemView.findViewById(R.id.priority);
            ass_to= (TextView) itemView.findViewById(R.id.ass_to);
            ass_by = (TextView) itemView.findViewById(R.id.ass_by);
            status = (TextView) itemView.findViewById(R.id.status);
            due_date = (TextView) itemView.findViewById(R.id.due_date);
        }
    }
}
