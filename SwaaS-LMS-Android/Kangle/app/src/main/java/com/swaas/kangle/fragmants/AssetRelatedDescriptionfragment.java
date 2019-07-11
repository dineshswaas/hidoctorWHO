package com.swaas.kangle.fragmants;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.swaas.kangle.AssetDetailActivity;
import com.swaas.kangle.R;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.db.DigitalAssetTransactionRepository;
import com.swaas.kangle.models.DigitalAssetTransactionMaster;
import com.swaas.kangle.models.DigitalAssetsMaster;

/**
 * Created by Sayellessh on 28-04-2017.
 */

public class AssetRelatedDescriptionfragment extends Fragment {

    TextView nameofasset,catvalue,descriptionvalue,ratingcount,likecount;
    LinearLayout likelayout,disabledlikelayout;
    RatingBar ratingBar,disabledRatingbar;
    AssetDetailActivity assetDetailActivity;
    DigitalAssetsMaster assetDetails;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;
    DigitalAssetTransactionMaster digitalAssetTransactionMaster;
    DigitalAssetTransactionRepository digitalAssetTransactionRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assetDetailActivity = (AssetDetailActivity) getActivity();
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(assetDetailActivity);
        digitalAssetTransactionRepository = new DigitalAssetTransactionRepository(assetDetailActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.asset_related_description_fragment,container,false);

        Bundle bundle = new Bundle();
        assetDetails = (DigitalAssetsMaster) getArguments().getSerializable("assetDetails");
        digitalAssetTransactionMaster = new DigitalAssetTransactionMaster();
        digitalAssetTransactionMaster.setDAID(assetDetails.getDAID());

        nameofasset = (TextView) v.findViewById(R.id.name_of_asset);
        catvalue = (TextView) v.findViewById(R.id.cat_value);
        descriptionvalue = (TextView) v.findViewById(R.id.description_value);
        ratingcount = (TextView) v.findViewById(R.id.rating_count);
        likecount = (TextView) v.findViewById(R.id.like_count);
        likelayout = (LinearLayout) v.findViewById(R.id.likelayout);
        disabledlikelayout = (LinearLayout) v.findViewById(R.id.disabledlikelayout);
        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        disabledRatingbar = (RatingBar) v.findViewById(R.id.disabled_rating_bar);

        loadDetails(assetDetails);


        likelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assetDetails.setUser_Like(1);
                likecount.setText(String.valueOf(assetDetails.getTotalLikes()+1));
                assetDetails.setTotalLikes(assetDetails.getTotalLikes()+1);
                //digitalAssetHeaderRepository.updateIsLiked(assetDetails);
                digitalAssetTransactionMaster.setUser_Like(1);
                digitalAssetTransactionMaster.setTotalLikes(assetDetails.getTotalLikes()+1);
                digitalAssetTransactionRepository.insertOrUpdateRecord(digitalAssetTransactionMaster,"like");
                likelayout.setVisibility(View.GONE);
                disabledlikelayout.setVisibility(View.VISIBLE);
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v > 0) {
                    submitRating();
                }
            }
        });
        return v;
    }

    public void submitRating(){
        //Rating choosen
        final float ratings = ratingBar.getRating();
        if (ratings != 0) {
            int currentUserRating = Math.round(ratings);
            digitalAssetTransactionMaster.setUser_Rating(currentUserRating);
            digitalAssetTransactionRepository.insertOrUpdateRecord(digitalAssetTransactionMaster,"rating");
            disabledRatingbar.setRating(currentUserRating);
            disabledRatingbar.setIsIndicator(true);
            ratingBar.setVisibility(View.GONE);
            disabledRatingbar.setVisibility(View.VISIBLE);
            //Log.d("Rating",String.valueOf(currentUserRating));
        }
    }

    public void loadDetails(DigitalAssetsMaster assetDetails){
        nameofasset.setText(assetDetails.getDAName());
        catvalue.setText(assetDetails.getDACategoryName());
        descriptionvalue.setText(assetDetails.getDA_Description());
        ratingcount.setText(String.valueOf(assetDetails.getTotalRatings()));
        likecount.setText(String.valueOf(assetDetails.getTotalLikes()));

        if(assetDetails.getUser_Rating() != 0) {
            disabledRatingbar.setRating(assetDetails.getUser_Rating());
            disabledRatingbar.setIsIndicator(true);
            ratingBar.setVisibility(View.GONE);
            disabledRatingbar.setVisibility(View.VISIBLE);
        }

        if(assetDetails.getUser_Like() != 0){
            disabledlikelayout.setVisibility(View.VISIBLE);
            likelayout.setVisibility(View.GONE);
        }
    }
}
