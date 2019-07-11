package com.swaas.kangle.playerPart.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.swaas.kangle.playerPart.DigitalAssets;
import com.swaas.kangle.playerPart.customviews.pdf.util.Constants;
import com.swaas.kangle.playerPart.fragments.BrightCoveVideoFragment;
import com.swaas.kangle.playerPart.fragments.ExoAudioPlayerFragment;
import com.swaas.kangle.playerPart.fragments.ExoPlayerFragment;
import com.swaas.kangle.playerPart.fragments.ImageViewFragment;
import com.swaas.kangle.playerPart.fragments.PdfViewFragment;
import com.swaas.kangle.playerPart.fragments.WebviewFragment;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Hariharan on 23/5/17.
 */

public class PageSlideAdapter extends FragmentStatePagerAdapter {

    public List<DigitalAssets> digitalAssetsList;
    public int currentdigitalAsset;
    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();


    public PageSlideAdapter(FragmentManager fm, List<DigitalAssets> digitalAssetsList, int currentDigigtalAsset) {
        super(fm);
        this.digitalAssetsList = digitalAssetsList;
        this.currentdigitalAsset = currentDigigtalAsset;

    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        Bundle bundle = new Bundle();

        DigitalAssets digitalAssets = digitalAssetsList.get(position);


        if (digitalAssets.getDA_Type() == Constants.IMAGEASSET){

            bundle.putInt("Index",position);
            if (!TextUtils.isEmpty(digitalAssets.getDA_Offline_URL())){
                bundle.putString("url",digitalAssets.getDA_Offline_URL());
                bundle.putInt("playmode",Constants.OFFLINE);

            }
            else{
                bundle.putString("url",digitalAssets.getDA_Online_URL());
                bundle.putInt("playmode",Constants.ONLINE);

            }
            fragment = new ImageViewFragment();
            fragment.setArguments(bundle);


        }
        else if (digitalAssets.getDA_Type() == Constants.VIDEOASSET){

            bundle.putInt("Index",position);
            if (!TextUtils.isEmpty(digitalAssets.getDA_Offline_URL())){

                bundle.putString("url",digitalAssets.getDA_Offline_URL());
                bundle.putInt("playmode",Constants.OFFLINE);

            }
            else{
                bundle.putString("url",digitalAssets.getDA_Online_URL());
                bundle.putInt("playmode",Constants.ONLINE);

            }

               /* fragment = new VideoViewFragment();
                fragment.setArguments(bundle);
               */

            fragment =  new ExoPlayerFragment();
            fragment.setArguments(bundle);


        }else if (digitalAssets.getDA_Type() == Constants.BRIGHTCOVE){

            bundle.putInt("Index",position);
            bundle.putString("url",digitalAssets.getDA_Online_URL());
            bundle.putInt("playmode",Constants.ONLINE);
            bundle.putString("VideoId",digitalAssets.getVideoId());
            bundle.putString("AccountId",digitalAssets.getAccountId());
            bundle.putString("PolicyKey",digitalAssets.getPolicyKey());

            fragment =  new BrightCoveVideoFragment();
            fragment.setArguments(bundle);


        }
        else if (digitalAssets.getDA_Type() == Constants.PDFASSET){

            bundle.putInt("Index",position);
            bundle.putInt("filename",digitalAssets.getDA_Code());
            if (!TextUtils.isEmpty(digitalAssets.getDA_Offline_URL())){
                bundle.putString("url",digitalAssets.getDA_Offline_URL());
                bundle.putInt("playmode",Constants.OFFLINE);
            }
            else{
                bundle.putString("url",digitalAssets.getDA_Online_URL());
                bundle.putInt("playmode",Constants.ONLINE);
            }
            fragment = new PdfViewFragment();
            fragment.setArguments(bundle);


        }else if (digitalAssets.getDA_Type() == Constants.AUDIOASSET){

            bundle.putInt("Index",position);
            bundle.putInt("filename",digitalAssets.getDA_Code());
            if (!TextUtils.isEmpty(digitalAssets.getDA_Offline_URL())){
                bundle.putString("url",digitalAssets.getDA_Offline_URL());
                bundle.putInt("playmode",Constants.OFFLINE);
            }
            else{
                bundle.putString("url",digitalAssets.getDA_Online_URL());
                bundle.putInt("playmode",Constants.ONLINE);
                bundle.putString("thumnail",digitalAssets.getDA_Thumbnail_URL());
            }

            fragment =  new ExoAudioPlayerFragment();
            fragment.setArguments(bundle);

        }
        else {

            bundle.putInt("Index",position);
            if (!TextUtils.isEmpty(digitalAssets.getDA_Online_URL()))
                bundle.putString("url",digitalAssets.getDA_Online_URL());
            fragment = new WebviewFragment();
            fragment.setArguments(bundle);
        }


/*
        if (position == 1){

            bundle.putInt("Index",position);
            bundle.putString("url","http://maven.apache.org/maven-1.x/maven.pdf");
            fragment = new PdfViewFragment();
            fragment.setArguments(bundle);

        }
        else if (position%2 == 0){
            bundle.putInt("Index",position);
            bundle.putString("url","https://www.cleverfiles.com/howto/wp-content/uploads/2016/08/mini.jpg");
            fragment = new ImageViewFragment();
            fragment.setArguments(bundle);

        }
        else {
            bundle.putInt("Index",position);
            bundle.putString("url","http://www.androidbegin.com/tutorial/AndroidCommercial.3gp");
            fragment = new VideoViewFragment();
            fragment.setArguments(bundle);

        }

*/


        return fragment;
    }

    @Override
    public int getCount() {

        return digitalAssetsList.size()> 0? digitalAssetsList.size():0;

    }


    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Fragment fragment = (Fragment) super.instantiateItem(container, position);
        instantiatedFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        instantiatedFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    public Fragment getFragment(final int position) {
        final WeakReference<Fragment> wr = instantiatedFragments.get(position);
        if (wr != null) {
            return wr.get();
        } else {
            return null;
        }
    }

}
