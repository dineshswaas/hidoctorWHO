package com.swaas.kangle.userProfile.UserDetailsAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swaas.kangle.R;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.userProfile.UserDetails;
import com.swaas.kangle.userProfile.UserProfileActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Sayellessh on 26-02-2018.
 */

public class LifeEventsAdapter extends RecyclerView.Adapter<LifeEventsAdapter.EventsRecyclerHolder> {
    Context context;
    List<UserDetails> eventsModelList;
    UserProfileActivity activity;

    public LifeEventsAdapter(UserProfileActivity mActivity,List<UserDetails> eventmodel) {
        this.eventsModelList = eventmodel;
        activity = mActivity;
        context = mActivity;
    }


    @Override
    public EventsRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userevents_list_item, parent, false);
        return new EventsRecyclerHolder(view,context);
    }

    @Override
    public void onBindViewHolder(final EventsRecyclerHolder holder, final int position) {
        final UserDetails eventDetails = eventsModelList.get(position);


        holder.eventName.setText(eventDetails.getEventType_Name().toString());
        if(eventDetails.getMode() == 2){
            holder.eventdate.setText(String.valueOf(eventDetails.getYear()));
        }else if(eventDetails.getMode() == 1){
            holder.eventdate.setText(getMonthFullName(eventDetails.getMonth())+"/"+String.valueOf(eventDetails.getYear()));
        }else{
            holder.eventdate.setText(String.valueOf(eventDetails.getDay())+
                    "/"+getMonthFullName(eventDetails.getMonth())+
                    "/"+String.valueOf(eventDetails.getYear()));
        }

        holder.editeventdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.deleteeventdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.deleteUserLifeEvent(PreferenceUtils.getSubdomainName(context), PreferenceUtils.getCompnayId(context), PreferenceUtils.getUserId(context),eventDetails.getUser_Event_Id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsModelList.size();
    }

    public class EventsRecyclerHolder extends RecyclerView.ViewHolder{
        Context ctxt;
        View mView;
        TextView eventName,eventdate;

        ImageView editeventdetails,deleteeventdetails;

        public EventsRecyclerHolder(View itemView,Context context) {
            super(itemView);
            this.ctxt = context;
            mView = itemView.findViewById(R.id.event_listItem);
            eventName = (TextView)itemView.findViewById(R.id.event_name);
            eventdate = (TextView)itemView.findViewById(R.id.daterange_text);

            editeventdetails = (ImageView) itemView.findViewById(R.id.edit_events_details);
            deleteeventdetails = (ImageView) itemView.findViewById(R.id.delete_events_details);
        }
    }

    private String getMonthFullName(int monthNumber) {
        String monthName="";
        if(monthNumber>=0 && monthNumber<12)
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, monthNumber);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
                simpleDateFormat.setCalendar(calendar);
                monthName = simpleDateFormat.format(calendar.getTime());
            } catch (Exception e) {
                if(e!=null)
                    e.printStackTrace();
            }
        return monthName;
    }
}
