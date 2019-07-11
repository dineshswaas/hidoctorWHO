package com.swaas.kangle.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.swaas.kangle.AssetListActivity;
import com.swaas.kangle.R;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.utils.Constants;

import java.util.List;

/**
 * Created by Sayellessh on 12-02-2018.
 */

public class TagsListTextRecyclerAdapter extends RecyclerView.Adapter<TagsListTextRecyclerAdapter.ViewHolder>{

    AssetListActivity mActivity;
    List<DigitalAssetsMaster> tags;
    boolean mShowunchecked;

    public TagsListTextRecyclerAdapter(Context context, List<DigitalAssetsMaster> categoryList,boolean showunchecked)  {
        mActivity = (AssetListActivity) context;
        tags = categoryList;
        mShowunchecked = showunchecked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.category_list_text_item, parent, false);
        return new TagsListTextRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final DigitalAssetsMaster tag = tags.get(position);

        setthemeforView(holder);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(mActivity,AssetListByCategoryActivity.class);
                intent.putExtra("TagsName",tag.getTags().toString());
                mActivity.startActivity(intent);*/

                if(tag.istagchecked){
                    holder.checkboxselected.setChecked(false);
                    tag.setIstagchecked(false);
                }else{
                    holder.checkboxselected.setChecked(true);
                    tag.setIstagchecked(true);
                }
                mActivity.filteredtagList(tag);
            }
        });

        if(tag.istagchecked){
            holder.checkboxselected.setVisibility(View.VISIBLE);
            holder.checkboxselected.setChecked(true);
        }else{
            if(mShowunchecked) {
                holder.checkboxselected.setVisibility(View.VISIBLE);
                holder.checkboxselected.setChecked(false);
            }else{
                holder.checkboxselected.setVisibility(View.GONE);
            }
        }

        holder.categoryname.setText(tag.getTags().toString());
        holder.categoreycount.setVisibility(View.GONE);

        holder.checkboxselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tag.istagchecked){
                    holder.checkboxselected.setChecked(false);
                    tag.setIstagchecked(false);
                }else{
                    holder.checkboxselected.setChecked(true);
                    tag.setIstagchecked(true);
                }
                mActivity.filteredtagList(tag);
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = tags.size();
        } catch (Exception e) {

        }
        return count;
    }

    public void setthemeforView(final ViewHolder holder) {
        holder.checkboxselected.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView categoreycount,categoryname;
        final View mView;
        final CheckBox checkboxselected;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView.findViewById(R.id.category_item);
            categoryname = (TextView) itemView.findViewById(R.id.categorey_name);
            categoreycount = (TextView) itemView.findViewById(R.id.categorey_count);
            checkboxselected = (CheckBox) itemView.findViewById(R.id.checkboxselected);
        }
    }
}
