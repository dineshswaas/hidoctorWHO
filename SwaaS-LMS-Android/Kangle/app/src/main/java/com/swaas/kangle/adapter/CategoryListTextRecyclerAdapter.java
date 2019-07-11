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
 * Created by Sayellessh on 04-05-2017.
 */

public class CategoryListTextRecyclerAdapter extends RecyclerView.Adapter<CategoryListTextRecyclerAdapter.ViewHolder>{

    AssetListActivity mActivity;
    List<DigitalAssetsMaster> cat;
    boolean mShowunchecked;

    public CategoryListTextRecyclerAdapter(Context context, List<DigitalAssetsMaster> categoryList,boolean showunchecked)  {
        mActivity = (AssetListActivity) context;
        cat = categoryList;
        mShowunchecked = showunchecked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.category_list_text_item, parent, false);
        return new CategoryListTextRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final DigitalAssetsMaster categories = cat.get(position);

        setthemeforView(holder);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(mActivity,AssetListByCategoryActivity.class);
                intent.putExtra("categoreyName",categories.getDACategoryName());
                mActivity.startActivity(intent);*/

                if(categories.iscatchecked){
                    holder.checkboxselected.setChecked(false);
                    categories.setIscatchecked(false);
                }else{
                    holder.checkboxselected.setChecked(true);
                    categories.setIscatchecked(true);
                }
                mActivity.filteredcatList(categories);
            }
        });

        if(categories.iscatchecked){
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

        holder.categoryname.setText(categories.getDACategoryName());
        holder.categoreycount.setText(String.valueOf(categories.getCategoryCount()));

        holder.checkboxselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(categories.iscatchecked){
                    holder.checkboxselected.setChecked(false);
                    categories.setIscatchecked(false);
                }else{
                    holder.checkboxselected.setChecked(true);
                    categories.setIscatchecked(true);
                }
                mActivity.filteredcatList(categories);
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = cat.size();
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
