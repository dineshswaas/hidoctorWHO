package com.swaas.kangle.fragmants;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.swaas.kangle.NewAssetDetailActivity;
import com.swaas.kangle.R;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.db.DigitalAssetTransactionRepository;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.playerPart.DigitalAssets;

import java.util.Date;

/**
 * Created by Sayellessh on 16-05-2017.
 */

public class RatingViewpagerFragment extends Fragment implements LocationListener {

    LinearLayout likelayout,disabledlikelayout;
    RatingBar ratingBar,disabledRatingbar;
    TextView ratingcomment;
    Button submit,disabledbutton;
    NewAssetDetailActivity assetDetailActivity;
    DigitalAssetsMaster assetDetails;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;
    //DigitalAssetTransactionMaster digitalAssetTransactionMaster
    DigitalAssets digitalAssetTransactionMaster;
    DigitalAssetTransactionRepository digitalAssetTransactionRepository;
    public double latitude,longitude;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assetDetailActivity = (NewAssetDetailActivity) getActivity();
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(assetDetailActivity);
        digitalAssetTransactionRepository = new DigitalAssetTransactionRepository(assetDetailActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.rating_fragment_layout,container,false);

        Bundle bundle = new Bundle();
        assetDetails = (DigitalAssetsMaster) getArguments().getSerializable("assetDetails");
        //digitalAssetTransactionMaster = new DigitalAssetTransactionMaster();
        //digitalAssetTransactionMaster.setDAID(Integer.parseInt(assetDetails.getDAID()));
        digitalAssetTransactionMaster = new DigitalAssets();
        digitalAssetTransactionMaster.setDA_Code(Integer.parseInt(assetDetails.getDAID()));

        likelayout = (LinearLayout) v.findViewById(R.id.likelayout);
        disabledlikelayout = (LinearLayout) v.findViewById(R.id.disabledlikelayout);
        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        disabledRatingbar = (RatingBar) v.findViewById(R.id.disabled_rating_bar);
        submit = (Button) v.findViewById(R.id.ratingsubmit);
        disabledbutton = (Button) v.findViewById(R.id.disabledratingsubmit);
        ratingcomment = (TextView) v.findViewById(R.id.ratingcomment);

        loadDetails(assetDetails);


        likelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assetDetails.setUser_Like(1);
                disabledbutton.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                likelayout.setVisibility(View.GONE);
                disabledlikelayout.setVisibility(View.VISIBLE);

            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v > 0) {
                    disabledbutton.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                    /*if(v==1){
                        ratingcomment.setText(assetDetailActivity.getResources().getString(R.string.hateIt));
                    }else if(v==2){
                        ratingcomment.setText(assetDetailActivity.getResources().getString(R.string.dislikeIt));
                    }else*/
                    if(v>=3) {
                        ratingcomment.setVisibility(View.VISIBLE);
                    }else{
                        ratingcomment.setVisibility(View.INVISIBLE);
                    }
                    if(v==3){
                        ratingcomment.setText(assetDetailActivity.getResources().getString(R.string.itsok));
                    }else if(v==4){
                        ratingcomment.setText(assetDetailActivity.getResources().getString(R.string.likeIt));
                    }else if(v==5){
                        ratingcomment.setText(assetDetailActivity.getResources().getString(R.string.loveIt));
                    }
                }else{
                    ratingcomment.setVisibility(View.INVISIBLE);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assetDetailActivity.showLoading(assetDetailActivity.getResources().getString(R.string.likeratingsuccess));
                if(assetDetails.getUser_Like() == 1) {
                    submitlike();
                }else{
                    submitRating();
                }

            }
        });
        return v;
    }

    public void submitlike(){
        digitalAssetTransactionMaster.setUser_Like(1);
        digitalAssetTransactionMaster.setLike(assetDetails.getTotalLikes()+1);
        digitalAssetTransactionMaster.setDetailed_DateTime(new Date().toString());
        digitalAssetTransactionMaster.setLattitude(latitude);
        digitalAssetTransactionMaster.setLongitude(longitude);
        digitalAssetTransactionRepository.insertOrUpdateDAAnalytics(digitalAssetTransactionMaster,false);
        //digitalAssetTransactionRepository.insertOrUpdateRecord(digitalAssetTransactionMaster,"like");
        final float ratingsbar = ratingBar.getRating();
        if(ratingsbar != 0) {
            submitRating();
        }
        assetDetailActivity.dismissProgressDialog();
    }

    public void submitRating(){
        //Rating choosen
        final float ratings = ratingBar.getRating();
        if (ratings != 0) {
            int currentUserRating = Math.round(ratings);
            digitalAssetTransactionMaster.setUser_Rating(currentUserRating);
            digitalAssetTransactionMaster.setDetailed_DateTime(new Date().toString());
            digitalAssetTransactionMaster.setLattitude(latitude);
            digitalAssetTransactionMaster.setLongitude(longitude);
            digitalAssetTransactionRepository.insertOrUpdateDAAnalytics(digitalAssetTransactionMaster,false);
            //digitalAssetTransactionRepository.insertOrUpdateRecord(digitalAssetTransactionMaster,"rating");
            disabledRatingbar.setRating(currentUserRating);
            disabledRatingbar.setIsIndicator(true);
            ratingBar.setVisibility(View.GONE);
            disabledRatingbar.setVisibility(View.VISIBLE);
            if(digitalAssetTransactionMaster.getUser_Rating() != 0 && digitalAssetTransactionMaster.getUser_Like() != 0){
                submit.setVisibility(View.GONE);
                disabledbutton.setVisibility(View.INVISIBLE);
            }
            assetDetailActivity.dismissProgressDialog();
            //Log.d("Rating",String.valueOf(currentUserRating));
        }
    }

    public void loadDetails(DigitalAssetsMaster assetDetails){

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

        if(assetDetails.getUser_Rating() != 0 && assetDetails.getUser_Like() != 0){
            submit.setVisibility(View.GONE);
            disabledbutton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
}
