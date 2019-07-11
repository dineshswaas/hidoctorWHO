package com.swaas.kangle.fragmants;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swaas.kangle.AssetDetailActivity;
import com.swaas.kangle.AssetListByCategoryActivity;
import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.R;
import com.swaas.kangle.adapter.RelatedAssetListItemRecyclerAdapter;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.models.DigitalAssetsMaster;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sayellessh on 28-04-2017.
 */

public class AssetRelatedAssetfragment extends Fragment {

    EmptyRecyclerView recyclerView1,recyclerView2;
    TextView Categoreytitle,tagstitle,showmorecategories,showmoretags;
    //View mEmptyView;
    RelatedAssetListItemRecyclerAdapter adapter;
    AssetDetailActivity assetDetailActivity;
    LinearLayoutManager mLayoutmanager,mLayoutmanager2;
    DigitalAssetsMaster assetDetails;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assetDetailActivity = (AssetDetailActivity) getActivity();
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(assetDetailActivity);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.asset_related_asset_fragment,container,false);

        Bundle bundle = new Bundle();
        assetDetails = (DigitalAssetsMaster) getArguments().getSerializable("assetDetails");


        recyclerView1 = (EmptyRecyclerView) v.findViewById(R.id.categories_list);
        recyclerView2 = (EmptyRecyclerView) v.findViewById(R.id.tags_list);
        Categoreytitle = (TextView) v.findViewById(R.id.categ);
        tagstitle = (TextView) v.findViewById(R.id.tag);
        showmorecategories = (TextView) v.findViewById(R.id.showmorecat);
        showmoretags = (TextView) v.findViewById(R.id.showmoretags);
        setUpRecyclerView();
        getrelatedassetsbyCategorey(assetDetails.getDACategoryName());

        Categoreytitle.setText(getString(R.string.category)+": "+ assetDetails.getDACategoryName());
        tagstitle.setText(getString(R.string.Tags)+": "+ assetDetails.getDACategoryName());
        showmorecategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(assetDetailActivity,AssetListByCategoryActivity.class);
                intent.putExtra("categoreyName",assetDetails.getDACategoryName());
                assetDetailActivity.startActivity(intent);
            }
        });

        showmoretags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return v;
    }

    private void setUpRecyclerView() {
        mLayoutmanager = new LinearLayoutManager(assetDetailActivity);
        mLayoutmanager2 = new LinearLayoutManager(assetDetailActivity);
        recyclerView1.setLayoutManager(mLayoutmanager);
        recyclerView2.setLayoutManager(mLayoutmanager2);
    }

    public void getrelatedassetsbyCategorey(String categ){
        List<DigitalAssetsMaster> asset = digitalAssetHeaderRepository.getAssetsByCategoryName(categ);
        List<DigitalAssetsMaster> assetlisttodisplay = new ArrayList<DigitalAssetsMaster>();
        if(asset.size() > 3){
            int i=0;
            for(DigitalAssetsMaster digitalAssetsMaster : asset) {
                assetlisttodisplay.add(digitalAssetsMaster);
                i++;
                if(i>2){
                    break;
                }
            }
        }else{
            assetlisttodisplay = asset;
        }
        adapter = new RelatedAssetListItemRecyclerAdapter(assetDetailActivity,assetlisttodisplay);
        recyclerView1.setAdapter(adapter);
        recyclerView2.setAdapter(adapter);
    }
}


