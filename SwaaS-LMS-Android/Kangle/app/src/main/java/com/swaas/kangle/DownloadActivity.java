package com.swaas.kangle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.gson.Gson;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.API.service.AssetService;
import com.swaas.kangle.NewPlayer.PPTAssetImagesActivity;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.db.DigitalAssetOfflineChildRepository;
import com.swaas.kangle.db.DigitalAssetOfflineRepository;
import com.swaas.kangle.db.DigitalAssetTransactionRepository;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.models.AssetId;
import com.swaas.kangle.models.AssetImages;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.models.LstAssetImageModel;
import com.swaas.kangle.playerPart.DigitalAssets;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.NetworkUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Sayellessh on 19-05-2017.
 */

public class DownloadActivity implements LocationListener{

    Context mContext;
    String extension = null;
    String filename = null;
    String offlineurl;
    private ProgressDialog pDialog;
    ArrayList<AssetImages> pptassetimage;
    Gson gsonget;
    Retrofit retrofitAPI;
    AssetService assetService;
    String pptname;
    DigitalAssetsMaster digitalAssetsMaster;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;
    //DigitalAssetTransactionMaster digitalAssetTransactionMaster;
    DigitalAssets digitalAssetTransactionMaster;
    DigitalAssetTransactionRepository digitalAssetTransactionRepository;
    DigitalAssetOfflineChildRepository childRepository;
    ArrayList<LstAssetImageModel> pptimagesset;
    boolean doneFirstCheck = false;
    int index = 0;
    ArrayList<LstAssetImageModel> slides;
    public double latitude,longitude;

    public DownloadActivity(Context context) {
        mContext = context;
        gsonget = new Gson();
        retrofitAPI = RetrofitAPIBuilder.getInstance();
        assetService = retrofitAPI.create(AssetService.class);
        digitalAssetsMaster = new DigitalAssetsMaster();
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(mContext);
        digitalAssetTransactionRepository = new DigitalAssetTransactionRepository(mContext);
        //digitalAssetTransactionMaster = new DigitalAssetTransactionMaster();
        digitalAssetTransactionMaster = new DigitalAssets();
        childRepository = new DigitalAssetOfflineChildRepository(mContext);
        pptimagesset = new ArrayList<LstAssetImageModel>();
    }

    public void startDownload(final Context context,DigitalAssetsMaster asset) {
        digitalAssetsMaster = asset;
        String url = asset.getOnlineURL();
        extension = url.substring(url.lastIndexOf("."));
        filename = asset.getDAName();
        new DownloadFileAsync().execute(url);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(mContext.getResources().getString(R.string.progressdownload));
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;
            String outputfilepath = null;
            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int lenghtOfFile = conexion.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());
                String assetname = "KAngle";
                final File dir = new File(new File(Environment.getExternalStorageDirectory(), Constants.Foldername), "");
                if (!dir.exists()){
                    dir.mkdir();
                }

                /*File createassetfolder = new File(Environment.getExternalStorageDirectory()+"/"+Constants.Foldername+"/asset/"+assetname+"/");
                if(assetname!= null && !createassetfolder.exists()) {
                    createassetfolder.mkdirs();
                }
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory()+"/"+Constants.Foldername+"/asset/"+assetname+"/"+filename+""+extension);
                outputfilepath = Environment.getExternalStorageDirectory()+"/"+Constants.Foldername+"/asset/"+assetname+"/"+filename+""+extension;*/
                OutputStream output = new FileOutputStream(new File(new File(Environment.getExternalStorageDirectory(), Constants.Foldername), filename+""+extension));
                outputfilepath = Environment.getExternalStorageDirectory()+"/"+Constants.Foldername+"/"+filename+""+extension;

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Exception",e.toString());
            }
            return outputfilepath;

        }

        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String url) {
            offlineurl = url;
            updateasset(offlineurl);
            Toast.makeText(mContext,mContext.getResources().getString(R.string.downloadcomplete), LENGTH_SHORT).show();
        }
    }

    public void updateasset(String offlineurl){
        dismissProgressDialog();
        if(offlineurl != null){
            digitalAssetsMaster.setOfflineURL(offlineurl);
            //digitalAssetsMaster.setOnlineURL(offlineurl);
            digitalAssetsMaster.setDAMode("Offline");
            digitalAssetsMaster.setIs_Downloaded(true);
            digitalAssetsMaster.setIsViewable("Y");
            DigitalAssetOfflineRepository digitalAssetOffline = new DigitalAssetOfflineRepository(mContext);
            List<DigitalAssetsMaster> offlinemaster = new ArrayList<DigitalAssetsMaster>();
            offlinemaster.add(digitalAssetsMaster);
            digitalAssetOffline.insertOrUpdate(offlinemaster);
            //digitalAssetHeaderRepository.updatedownloaded(digitalAssetsMaster);
            digitalAssetTransactionMaster.setDA_Code(Integer.parseInt(digitalAssetsMaster.getDAID()));
            digitalAssetTransactionMaster.setDA_Offline_URL(offlineurl);
            digitalAssetTransactionMaster.setDA_Online_URL(digitalAssetsMaster.getOnlineURL());
            digitalAssetTransactionMaster.setIs_Downloaded(1);
            digitalAssetTransactionMaster.setLattitude(latitude);
            digitalAssetTransactionMaster.setLongitude(longitude);

            digitalAssetTransactionRepository.insertOrUpdateDAAnalytics(digitalAssetTransactionMaster,false);
            //digitalAssetTransactionRepository.insertOrUpdateRecord(digitalAssetTransactionMaster,"download");

        }
    }

    public void startPPTDownload(final Context context,DigitalAssetsMaster asset) {
        digitalAssetsMaster = asset;
        pptname = asset.getDAName();
        doneFirstCheck = false;
        //digitalAssetTransactionMaster.setDAID(digitalAssetsMaster.getDAID());
        digitalAssetTransactionMaster.setDA_Code(Integer.parseInt(digitalAssetsMaster.getDAID()));
        getPPTAssetImages(digitalAssetsMaster,true);
    }

    public ArrayList<AssetImages> getPPTAssetImages(final DigitalAssetsMaster digitalAssetsMaster, final boolean download){
        if(NetworkUtils.checkIfNetworkAvailable(mContext)) {
            pptassetimage = new ArrayList<AssetImages>();
            AssetId assetId = new AssetId();
            assetId.setAssetId(digitalAssetsMaster.getDAID());
            List<Integer> intasset = new ArrayList<Integer>();
            intasset.add(Integer.parseInt(digitalAssetsMaster.getDAID()));
            String[] newarray = {digitalAssetsMaster.getDAID()};

            User userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);
            String subdomain = PreferenceUtils.getSubdomainName(mContext);
            int companyId = userobj.getCompany_Id();
            Call call = assetService.getAssetImages(subdomain,companyId,newarray);
            call.enqueue(new Callback<ArrayList<AssetImages>>() {

                @Override
                public void onResponse(Response<ArrayList<AssetImages>> response, Retrofit retrofit) {
                    ArrayList<AssetImages> apiResponse= response.body();
                    if (apiResponse != null) {
                        pptassetimage = apiResponse;
                        Log.d("log","assetsForBrowses");
                        if(download){
                            pptAssetImagesDownload(pptassetimage);
                        }else{
                            Intent i = new Intent(mContext, PPTAssetImagesActivity.class);
                            i.putExtra("AssetImages", apiResponse);
                            i.putExtra("Asset",digitalAssetsMaster);
                            dismissProgressDialog();
                            mContext.startActivity(i);
                        }

                        //gotoCategoriesView();
                    } else {
                        Toast.makeText(mContext,mContext.getResources().getString(R.string.erroroccured),Toast.LENGTH_SHORT).show();
                        Log.d("retrofit","error 2");
                        //error
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("retrofit","error 2");
                }
            });
        } else {
            ArrayList<LstAssetImageModel> pptImageList = childRepository.getofflineAssetsByDACode(digitalAssetsMaster.getDAID());
            if(pptImageList.size() > 0) {
                Intent i = new Intent(mContext, PPTAssetImagesActivity.class);
                i.putExtra("AssetPPTSlides", pptImageList);
                i.putExtra("Asset",digitalAssetsMaster);
                dismissProgressDialog();
                mContext.startActivity(i);
            } else {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        }
        return  pptassetimage;
    }

    public void pptAssetImagesDownload(ArrayList<AssetImages> pptImages){
        slides = new ArrayList<LstAssetImageModel>();
        ArrayList<AssetImages> pptimages = pptImages;
        pptimagesset = (ArrayList<LstAssetImageModel>) pptimages.get(0).lstAssetImageModel;
        oneByOneDownload(pptimagesset,index);
    }

    public void oneByOneDownload(ArrayList<LstAssetImageModel> pptimagesset,int index){
        showProgressDialog((index+1)+" "+mContext.getResources().getString(R.string.progressdownload));
        LstAssetImageModel assetImages =  pptimagesset.get(index);
        String url = assetImages.getImage_Url();
        extension = url.substring(url.lastIndexOf("."));
        filename = assetImages.getImage_Name();
        new DownloadImageFileAsync().execute(url);
    }

    class DownloadImageFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;
            String outputfilepath = null;
            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int lenghtOfFile = conexion.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());
                String assetname = "KAngle";
                String PPTname = pptname;
                File createassetfolder = new File(Environment.getExternalStorageDirectory()+"/"+Constants.Foldername+"/asset/"+assetname+"/"+PPTname);
                if(assetname!= null && !createassetfolder.exists()) {
                    createassetfolder.mkdirs();
                }
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory()+"/"+Constants.Foldername+"/asset/"+assetname+"/"+PPTname+"/"
                        +filename+""+extension);
                outputfilepath = Environment.getExternalStorageDirectory()+"/"+Constants.Foldername+"/asset/"+assetname+"/"+PPTname+"/"+filename+""+extension;
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Exception",e.toString());
            }
            return outputfilepath;

        }
        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String url) {
            offlineurl = url;
            updatePPTasset(offlineurl);
        }
    }

    public void updatePPTasset(String OfflineUrl){
        childRepository = new DigitalAssetOfflineChildRepository(mContext);
        LstAssetImageModel assetImages =  pptimagesset.get(index);
        assetImages.setOffline_URL(OfflineUrl);
        assetImages.setImage_Url(OfflineUrl);
        //slides.add(assetImages);
        if(!doneFirstCheck){
            boolean newasset = childRepository.insertOrUpdate(assetImages);
            if(newasset){
                childRepository.assetsOfflineInsert(assetImages);
            }else{
                childRepository.deleteExistingAsset(assetImages.getDA_Code());
                childRepository.assetsOfflineInsert(assetImages);
            }
            doneFirstCheck = true;
        }else{
            childRepository.assetsOfflineInsert(assetImages);
        }
        nextAsset();
    }

    public void nextAsset(){
        try{
            if(pptimagesset.size() == index+1){
                dismissProgressDialog();
                updatePPTassetparent();
                //bulkinsert(slides);
            } else {
                index++;
                dismissProgressDialog();
                oneByOneDownload(pptimagesset, index);
            }
        } catch (Exception e){

        }
    }

    public void updatePPTassetparent(){
        dismissProgressDialog();
            digitalAssetsMaster.setOfflineURL(digitalAssetsMaster.getOnlineURL());
            digitalAssetsMaster.setOnlineURL(digitalAssetsMaster.getOnlineURL());
            digitalAssetsMaster.setDAMode("Offline");
            digitalAssetsMaster.setIs_Downloaded(true);
            DigitalAssetOfflineRepository digitalAssetOffline = new DigitalAssetOfflineRepository(mContext);
            List<DigitalAssetsMaster> offlinemaster = new ArrayList<DigitalAssetsMaster>();
            offlinemaster.add(digitalAssetsMaster);
            digitalAssetOffline.insertOrUpdate(offlinemaster);
            //digitalAssetHeaderRepository.updatedownloaded(digitalAssetsMaster);
            digitalAssetTransactionMaster.setDA_Code(Integer.parseInt(digitalAssetsMaster.getDAID()));
            digitalAssetTransactionMaster.setDA_Offline_URL(digitalAssetsMaster.getOnlineURL());
            digitalAssetTransactionMaster.setDA_Online_URL(digitalAssetsMaster.getOnlineURL());
            digitalAssetTransactionMaster.setIs_Downloaded(1);

        //    digitalAssetTransactionRepository.insertOrUpdateRecord(digitalAssetTransactionMaster,"download");
    }

    public void bulkinsert(ArrayList<LstAssetImageModel> slides){
        childRepository.bulkinsert(slides);
    }

    public void showProgressDialog(String Message){
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(Message);
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void dismissProgressDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }
}
