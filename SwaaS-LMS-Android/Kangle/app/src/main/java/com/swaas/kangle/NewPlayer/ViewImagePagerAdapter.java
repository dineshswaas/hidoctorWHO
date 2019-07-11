package com.swaas.kangle.NewPlayer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.models.LstAssetImageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sayellessh on 12-05-2017.
 */

public class ViewImagePagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragments = new ArrayList<>();
    private Context mContext;

    public ViewImagePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    public void addFragment(Fragment fragment, String title, LstAssetImageModel eventDocument) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("eventDocument",eventDocument);
        fragment.setArguments(bundle);
        mFragments.add(fragment);

    }

    public void addAssetFragmant(Fragment assetfragment, String title, DigitalAssetsMaster digitalAssetsMaster){
        Bundle bundle = new Bundle();
        bundle.putSerializable("assetimage",digitalAssetsMaster);
        assetfragment.setArguments(bundle);
        mFragments.add(assetfragment);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    public Fragment getItem(int position,int position2) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
}
