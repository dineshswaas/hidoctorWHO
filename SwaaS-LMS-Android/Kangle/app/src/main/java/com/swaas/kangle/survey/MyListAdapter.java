package com.swaas.kangle.survey;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.swaas.kangle.R;

import java.util.ArrayList;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
    private ArrayList<String> listdata;

    // RecyclerView recyclerView;
    public MyListAdapter(ArrayList<String> listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.rating_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.textView.setText(listdata.get(position));
        holder.checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    holder.checkBox2.setChecked(false);
                    holder.checkBox3.setChecked(false);
                    holder.checkBox4.setChecked(false);
                    holder.checkBox5.setChecked(false);
                    holder.checkBox1.setChecked(true);

            }
        });
        holder.checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.checkBox1.setChecked(false);
                holder.checkBox3.setChecked(false);
                holder.checkBox4.setChecked(false);
                holder.checkBox5.setChecked(false);
                holder.checkBox2.setChecked(true);

            }
        });
        holder.checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.checkBox2.setChecked(false);
                holder.checkBox1.setChecked(false);
                holder.checkBox4.setChecked(false);
                holder.checkBox5.setChecked(false);
                holder.checkBox3.setChecked(true);

            }
        });
        holder.checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.checkBox2.setChecked(false);
                holder.checkBox3.setChecked(false);
                holder.checkBox1.setChecked(false);
                holder.checkBox5.setChecked(false);
                holder.checkBox4.setChecked(true);

            }
        });
        holder.checkBox5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.checkBox2.setChecked(false);
                holder.checkBox3.setChecked(false);
                holder.checkBox4.setChecked(false);
                holder.checkBox1.setChecked(false);
                holder.checkBox5.setChecked(true);

            }
        });

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox1;
        public CheckBox checkBox2;
        public CheckBox checkBox3;
        public CheckBox checkBox4;
        public CheckBox checkBox5;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.checkBox1 = (CheckBox) itemView.findViewById(R.id.check_box1);
            this.checkBox2 = (CheckBox) itemView.findViewById(R.id.check_box2);
            this.checkBox3 = (CheckBox) itemView.findViewById(R.id.check_box3);
            this.checkBox4 = (CheckBox) itemView.findViewById(R.id.check_box4);
            this.checkBox5 = (CheckBox) itemView.findViewById(R.id.check_box5);
            this.textView = (TextView) itemView.findViewById(R.id.text);
        }
    }
}