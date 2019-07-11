package com.swaas.kangle.NewPlayer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import com.swaas.kangle.EmptyRecyclerView;
import com.swaas.kangle.R;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.models.DigitalAssetsMaster;

import java.util.List;

public class PlayerActivity extends AppCompatActivity {

    EmptyRecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    ImageViewAdapter viewAdapter;
    Context mContext;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mContext = PlayerActivity.this;
       // getSupportActionBar().hide();
        initialiseViews();
        setUpRecyclerView();
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(mContext);
        getAllAssets();
    }

    public void initialiseViews(){
        recyclerView = (EmptyRecyclerView) findViewById(R.id.playerrecyclerLayout);
    }

    public void setUpRecyclerView(){
        gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.scrollToPosition(0);
    }

    public void getAllAssets(){
        List<DigitalAssetsMaster> digitalAssetsMasterList = digitalAssetHeaderRepository.getAllAssets();
        viewAdapter = new ImageViewAdapter(PlayerActivity.this, digitalAssetsMasterList);
        recyclerView.setAdapter(viewAdapter);
    }

}
