package com.swaas.kangle.CheckList.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swaas.kangle.CheckList.model.UserCCHistoryDetailModel;
import com.swaas.kangle.R;

import java.util.List;

/**
 * Created by Sayellessh on 18-09-2018.
 */

public class UserCCHistoryListsAdapter extends RecyclerView.Adapter<UserCCHistoryListsAdapter.RecyclerHolder> {

    Context context;
    List<UserCCHistoryDetailModel> checklistModelList;
    private static MyClickListener myClickListener;
    int Count = 0;
    String checklistName;

    public UserCCHistoryListsAdapter(Context context, List<UserCCHistoryDetailModel> courseModels,String ChecklistName) {
        this.context = context;
        this.checklistModelList = courseModels;
        this.checklistName = ChecklistName;
    }


    public void setCourseListadapter(List<UserCCHistoryDetailModel> courseModelList) {
        this.checklistModelList = courseModelList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userhistorylist_forcoursechklst_item,parent,false);
        return new RecyclerHolder(view, context);
    }

    @Override
    public void onBindViewHolder(final RecyclerHolder holder, final int position) {
        final UserCCHistoryDetailModel model = checklistModelList.get(position);

        //holder.user_name.setText(context.getResources().getString(R.string.Attempt)+" : "+String.valueOf(model.getAttempt_Number()));
        holder.user_name.setText(context.getResources().getString(R.string.Attempt)+" : "+(position+1));
        /*holder.course_name.setText(checklistName.toString());*/
        holder.course_name.setVisibility(View.GONE);

        holder.user_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onItemClick(model);
            }
        });
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public int getItemCount() {
        return checklistModelList.size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {
        Context ctxt;
        View user_item;
        TextView user_name, course_name;

        public RecyclerHolder(View itemView, Context context) {
            super(itemView);
            this.ctxt = context;
            user_item = itemView.findViewById(R.id.user_item);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            course_name = (TextView) itemView.findViewById(R.id.course_name);
        }
    }

    public interface MyClickListener {

        //public void onItemClick(int courseId, boolean isSequenceEnabled);
        public void onItemClick(UserCCHistoryDetailModel model);

    }
}
