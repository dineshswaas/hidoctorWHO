package com.swaas.kangle.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swaas.kangle.AssetListActivity;
import com.swaas.kangle.AssetListByCategoryActivity;
import com.swaas.kangle.R;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.preferences.PreferenceUtils;

import java.util.List;

/**
 * Created by Sayellessh on 26-04-2017.
 */

public class CategoryListRecyclerAdapter extends RecyclerView.Adapter<CategoryListRecyclerAdapter.ViewHolder> {

    AssetListActivity mActivity;
    List<DigitalAssetsMaster> cat;

    public CategoryListRecyclerAdapter(Context context, List<DigitalAssetsMaster> categoryList)  {
        mActivity = (AssetListActivity) context;
        cat = categoryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.category_list_item, parent, false);
        return new CategoryListRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DigitalAssetsMaster categories = cat.get(position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity,AssetListByCategoryActivity.class);
                intent.putExtra("categoreyName",categories.getDACategoryName());
                mActivity.startActivity(intent);
            }
        });
        if(PreferenceUtils.getSubdomainName(mActivity).equalsIgnoreCase("tacobell.kangle.me")){
            holder.mView.setBackgroundColor(mActivity.getResources().getColor(R.color.tacobellbackground));
        }else{
            holder.mView.setBackgroundColor(mActivity.getResources().getColor(R.color.otherCompanies));
        }

        holder.categoryname.setText(categories.getDACategoryName());
        holder.categoreycount.setText(String.valueOf(categories.getCategoryCount()));
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

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView categoreycount,categoryname;
        final View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView.findViewById(R.id.category_item);
            categoryname = (TextView) itemView.findViewById(R.id.categorey_name);
            categoreycount = (TextView) itemView.findViewById(R.id.categorey_count);

        }
    }
}
