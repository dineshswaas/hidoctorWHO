package com.swaas.kangle.API.service;

import com.swaas.kangle.models.APIResponse;
import com.swaas.kangle.models.AssetImages;
import com.swaas.kangle.models.ChildAnalyticsModel;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.models.PostComment;
import com.swaas.kangle.models.PostSubComment;
import com.swaas.kangle.models.TagAnalytics;
import com.swaas.kangle.models.WallPost;
import com.swaas.kangle.playerPart.DigitalAssets;

import java.util.ArrayList;
import java.util.Map;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by Sayellessh on 24-04-2017.
 */

public interface AssetService {

    @GET("WebAPI/getAssetsForBrowse/{companycode}/{usercode}/{regioncode}/{usertypecode}/{compayId}/{utcoffset}")
    Call<ArrayList<DigitalAssetsMaster>> getAssetsForBrowse(@Path("companycode") String companycode,
                                                            @Path("usercode") String usercode,
                                                            @Path("regioncode") String regioncode,
                                                            @Path("usertypecode") String usertypecode,
                                                            @Path("compayId") int compayId,
                                                            @Path("utcoffset") String utcoffset);

    @GET("api/WallPost")
    Call<APIResponse<PostComment>> getComments(@Path("assetId") String assetId);

    @GET("api/WallPost")
    Call<ArrayList<PostComment>> getNewComments(@QueryMap Map<String, String> options);

    @POST( "api/WallPost")
    Call<PostComment> postComments(@Body WallPost wallPost);

    @POST( "api/Comment")
    Call<PostSubComment> postSubComments(@Body WallPost wallPost);

    @POST("AssetApi/getAssetImages/{subdomain}/{companyId}")
    Call<ArrayList<AssetImages>> getAssetImages(@Path("subdomain") String subdomain ,
                                            @Path("companyId") int companyId ,
                                            @Body String[] assetId);
    @POST("/Asset/Feedback/{subdomain}")
    Call<APIResponse<DigitalAssets>>sendCustomerFeedbackDetails(@Path("subdomain")String domain,@Body DigitalAssets digitalAssets);

    @POST("/AssetUploadApi/InsertELTagAnalytics/{subdomain}")
    Call<String> insertTaganalytics(@Path("subdomain") String subdomain , @Body TagAnalytics tagAnalytics);

    @POST("/AssetUploadApi/InsertELItemizedBilling/{subdomain}")
    Call<String> insertELItemizedBilling(@Path("subdomain") String subdomain, @Body TagAnalytics tagAnalytics);

    @POST("AssetUploadApi/InsertChildAssetAnalytics/{subdomain}")
    Call<String> insertChildAssetAnalytics(@Path("subdomain") String subdomain, @Body ChildAnalyticsModel childAnalyticsModel);

    @POST("Asset/UserWiseAnalyticsDetails/{subdomain}")
    Call<APIResponse<DigitalAssets>>sendCustomerWiseAnalyticsDetails(@Path("subdomain") String subdomain,@Body DigitalAssets digitalAssets);
}
