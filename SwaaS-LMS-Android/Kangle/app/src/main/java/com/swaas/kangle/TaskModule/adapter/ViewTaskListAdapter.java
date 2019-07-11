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

public class ViewTaskListAdapter extends RecyclerView.Adapter<ViewTaskListAdapter.RecyclerHolder> {

    Context context;
    List<TaskListModel> taskListModelList;
    private static MyClickListener myClickListener;

    public ViewTaskListAdapter(Context context, List<TaskListModel> taskModels) {
        this.context = context;
        this.taskListModelList = taskModels;
    }

    public interface MyClickListener{
        public void onItemClick(TaskListModel tsk);

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
        holder.ass_by.setText(taskListModel.getUser_Name() != null ? (context.getString(R.string.assignedby)+taskListModel.getUser_Name().toString()) : "-");
        holder.ass_to.setText(taskListModel.getAssigned_User_Name() != null ? (context.getString(R.string.assignedto)+taskListModel.getAssigned_User_Name().toString()) : " - ");
        holder.status.setText("Edit");
        holder.due_date.setText("-");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myClickListener != null) {
                    myClickListener.onItemClick(taskListModel);
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
