package com.swaas.kangle.TaskModule.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swaas.kangle.R;
import com.swaas.kangle.TaskModule.TaskListModel;
import com.swaas.kangle.utils.Constants;

import java.util.List;

/**
 * Created by barath on 11/27/2018.
 */

public class ChildUserListAdapter extends RecyclerView.Adapter<ChildUserListAdapter.RecyclerHolder> {
    Context context;
    List<TaskListModel> taskListModelList;
    boolean c;
    public LinearLayout mView;
   public TextView user_name;
    public CheckBox checkbox;
    private static ChildUserListAdapter.MyClickListener myClickListener;
    @NonNull
    @Override
    public ChildUserListAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.child_user_list, parent, false);
        return new RecyclerHolder(view,context);

    }
    public ChildUserListAdapter(Context context, List<TaskListModel> users) {
        this.context = context;
        this.taskListModelList = users;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChildUserListAdapter.RecyclerHolder holder, final int position) {
        final TaskListModel taskListModel = taskListModelList.get(position);
        if(taskListModel.getUser_Name()!=null) {
            holder.user_name.setText(taskListModel.getUser_Name().toString());
            holder.checkbox.setEnabled(true);
        }
        if(taskListModel.isUserchecked()){
            holder.checkbox.setChecked(true);
        }else{
            holder.checkbox.setChecked(false);
        }

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    taskListModel.setUserchecked(true);
                }else{
                    taskListModel.setUserchecked(false);
                }
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(taskListModel.isUserchecked()){
                    holder.checkbox.setChecked(false);
                    taskListModel.setUserchecked(false);
                }else{
                    holder.checkbox.setChecked(true);
                    taskListModel.setUserchecked(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskListModelList.size();
    }

    public boolean checkall(boolean b) {
        c=b;
        return b;
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder{
        Context ctxt;
        public LinearLayout mView;
        public TextView user_name;
        public CheckBox checkbox;

        public RecyclerHolder(View itemView, Context context) {
            super(itemView);
            this.ctxt = context;
            mView =(LinearLayout) itemView.findViewById(R.id.mView);
            user_name = (TextView) itemView.findViewById(R.id.username);
            checkbox=(CheckBox) itemView.findViewById(R.id.check_box);
            mView.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
            user_name.setBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
            user_name.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
            checkbox.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        }
    }
    public interface MyClickListener{
        public void onItemClick(TaskListModel tsk);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


}
