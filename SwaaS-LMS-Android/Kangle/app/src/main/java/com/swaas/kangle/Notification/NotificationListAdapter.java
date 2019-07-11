package com.swaas.kangle.Notification;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swaas.kangle.R;
import com.swaas.kangle.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Sayellessh on 12-10-2018.
 */

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.RecyclerHolder>{

    Context context;
    List<NotificationModel> modelList;
    private static MyClickListener myClickListener;
    private SimpleDateFormat simpleDateFormat;

    public NotificationListAdapter(Context context,List<NotificationModel> courseModels) {
        this.context = context;
        this.modelList = courseModels;
        simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    }


    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_listitem, parent, false);
        return new RecyclerHolder(view,context);
    }

    @Override
    public void onBindViewHolder(final RecyclerHolder holder, final int position) {
        final NotificationModel nModel = modelList.get(position);
        setthemeforView(holder);
        if (position > 0){
            int pos = position - 1;
            if (modelList.get(pos).getOnlyDate().equals(modelList.get(position).getOnlyDate())) {
                holder.headercontent.setVisibility(View.GONE);
            } else {
                holder.headercontent.setVisibility(View.VISIBLE);
                holder.headerdatestring.setText(getformatDate(nModel).toString());
            }
        }else{
            holder.headercontent.setVisibility(View.VISIBLE);
            holder.headerdatestring.setText(getformatDate(nModel).toString());
        }
        holder.titlestring.setText(nModel.getTitle());
        holder.datestring.setText(nModel.getMessage_Date());
        holder.messageString.setText(nModel.getMessage_text());

        if(nModel.getCategoryId() == Constants.Notification_Task){
            holder.typeimg.setImageResource(R.drawable.ic_task_icn);
        }else if(nModel.getCategoryId() == Constants.Notification_Course){
            holder.typeimg.setImageResource(R.drawable.courses_icn);
        }else if(nModel.getCategoryId() == Constants.Notification_Checklist){
            holder.typeimg.setImageResource(R.drawable.ic_checklist_icn);
        } else {
            holder.typeimg.setImageResource(R.drawable.ic_assets_icn);
        }

        holder.clearall.setVisibility(View.VISIBLE);

        holder.cardViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myClickListener != null) {
                    myClickListener.onItemClick(nModel);
                }
            }
        });

        holder.clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public String getformatDate(NotificationModel notificationModel){
        String date = "";
        try {
            Date today = new Date();

            int diffInDays = (int) ((today.getTime() - notificationModel.getDate().getTime()) / (1000 * 60 * 60 * 24));
            if(diffInDays >= 0){
                if (diffInDays < 1 ) {
                    date = context.getResources().getString(R.string.Today);
                } else if (diffInDays == 1) {
                    date = context.getResources().getString(R.string.Yesterday);
                }else{
                    String pattern = "EEEE, MMMM dd, yyyy";
                    SimpleDateFormat simpleDateFormat =new SimpleDateFormat(pattern);
                    date = simpleDateFormat.format(notificationModel.getDate());
                }
            }else{
                String pattern = "EEEE, MMMM dd, yyyy";
                SimpleDateFormat simpleDateFormat =new SimpleDateFormat(pattern);
                date = simpleDateFormat.format(notificationModel.getDate());
                //date = notificationModel.getOnlyDate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnItemClickListener(MyClickListener myClickListener){
        this.myClickListener = myClickListener;
    }

    public void setthemeforView(final RecyclerHolder holder){
        holder.headerdatestring.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.clearall.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.titlestring.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.datestring.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.messageString.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        holder.cardViewLayout.setCardBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder{
        Context ctxt;
        View mView;
        CardView cardViewLayout;
        RelativeLayout headercontent;
        TextView headerdatestring,clearall;
        ImageView typeimg;
        TextView titlestring,datestring,messageString;

        public RecyclerHolder(View itemView,Context context) {
            super(itemView);
            this.ctxt = context;
            cardViewLayout = (CardView)itemView.findViewById(R.id.cardviewLayout);
            headercontent = (RelativeLayout) itemView.findViewById(R.id.headercontent);
            headerdatestring = (TextView)itemView.findViewById(R.id.headerdatestring);
            clearall = (TextView)itemView.findViewById(R.id.clearall);
            typeimg = (ImageView)itemView.findViewById(R.id.typeimg);
            titlestring = (TextView)itemView.findViewById(R.id.titlestring);
            datestring = (TextView)itemView.findViewById(R.id.datestring);
            messageString = (TextView) itemView.findViewById(R.id.messageString);
        }
    }

    public interface MyClickListener{

        //public void onItemClick(int courseId, boolean isSequenceEnabled);
        public void onItemClick(NotificationModel nModel);

    }

    public NotificationModel getItemAt(int position){
        return modelList.get(position);
    }
}
