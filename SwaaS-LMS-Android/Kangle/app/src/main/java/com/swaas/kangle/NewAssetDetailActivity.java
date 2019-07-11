package com.swaas.kangle;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.API.service.AssetService;
import com.swaas.kangle.DigitalAssetPlayer.PlayMode;
import com.swaas.kangle.adapter.NewDiscussionListItemRecyclerAdapter;
import com.swaas.kangle.adapter.NewRelatedAssetListItemRecyclerAdapter;
import com.swaas.kangle.db.DigitalAssetHeaderRepository;
import com.swaas.kangle.db.DigitalAssetOfflineRepository;
import com.swaas.kangle.db.DigitalAssetTransactionRepository;
import com.swaas.kangle.db.Filters.DigitalassetCatTagFilterRepository;
import com.swaas.kangle.db.RetrofitAPIBuilder;
import com.swaas.kangle.fragmants.RatingViewpagerFragment;
import com.swaas.kangle.fragmants.commentViewpagerFragment;
import com.swaas.kangle.models.AssetImages;
import com.swaas.kangle.models.DigitalAssetTransactionMaster;
import com.swaas.kangle.models.DigitalAssetsMaster;
import com.swaas.kangle.models.PostComment;
import com.swaas.kangle.models.WallPost;
import com.swaas.kangle.playerPart.DigitalAssets;
import com.swaas.kangle.preferences.PreferenceUtils;
import com.swaas.kangle.utils.Constants;
import com.swaas.kangle.utils.MessageDialog;
import com.swaas.kangle.utils.NetworkUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class NewAssetDetailActivity extends AppCompatActivity implements LocationListener {

    NewAssetDetailActivity mActivity;
    ImageView companylogo,notification,settings,thumbnail;
    TextView name_of_asset,name_of_asset_below_thumb,rating_count,like_count,view_count,comment_count;
    TextView cat_value,tag_value,cat_name,assettitle;

    TextView cat_text,tag_text,assetanalytics_title,discussiontext,relatedasset_text,
            relatedasset_tags_text;
    ImageView viewsicon,likeicon,commenticon;

    TextView moretagedssets,tag_name;

    TextView readMore,description,fulldescription;

    TextView moreassets,morediscussions;

    ImageView userThumbnailImage;
    String check;
    ImageView playbutton,downloadbutton;
    public ViewPager viewpager;
    EmptyRecyclerView discussrecyclerView,relatedRecyclerView,tagRecyclerView;
    LinearLayoutManager DiscusslayoutManager,RelatedlayoutManager,TagedlayoutManager;
    Context mContext;
    DigitalAssetsMaster digitalAssetsMaster;
    String daid;
    DigitalAssetHeaderRepository digitalAssetHeaderRepository;
    DigitalAssetOfflineRepository digitalAssetoffline;
    DigitalAssetTransactionMaster digitalAssetTransactionMaster;
    DigitalAssetTransactionRepository digitalAssetTransactionRepository;
    String extension = null;
    String filename = null;
    String offlineurl;

    NewDiscussionListItemRecyclerAdapter discussionadapter;
    NewRelatedAssetListItemRecyclerAdapter relatedadapter,tagededadapter;

    RelativeLayout relatedDiscussions,relatedAssetList,tagedAssetList,Reviewssection;

    private ProgressDialog pDialog;

    ArrayList<PostComment> relatedPosts;

    ArrayList<AssetImages> pptassetimage;

    Gson gsonget;
    Retrofit retrofitAPI;
    AssetService assetService;

    View viewpager1,viewpager2;


    LinearLayout header;

    public List<DigitalAssetsMaster> digitalAssetsList;
    public DigitalAssetsMaster intentDigitalasset;
    public int CurrentDigigtalAsset;
    PlayMode playMode;

    //new UI Changes
    ImageView expanddes_cat,likebutton,disabledbutton,likebutton1,disabledbutton1;
    View tagnamesec,catnamesec,detailsfullview;
    EditText comment;
    ImageView post;
    TextView textlength;
    PostComment postsuccess;
    DigitalAssets digitalasset;
    public double latitude,longitude;
    View layoutthumbnail;

    MessageDialog messageDialog;
    CardView cardViewLayout;

    ImageView expanddes_comments;
    View coments_sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_asset_detail);

        mContext = NewAssetDetailActivity.this;
        mActivity = NewAssetDetailActivity.this;
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        // getSupportActionBar().hide();
        digitalAssetHeaderRepository = new DigitalAssetHeaderRepository(mContext);
        digitalAssetoffline = new DigitalAssetOfflineRepository(mContext);
        digitalAssetTransactionRepository = new DigitalAssetTransactionRepository(mContext);
        relatedPosts = new ArrayList<PostComment>();
        pptassetimage = new ArrayList<AssetImages>();
        gsonget = new Gson();
        retrofitAPI = RetrofitAPIBuilder.getInstance();
        assetService = retrofitAPI.create(AssetService.class);

        messageDialog= new MessageDialog(mContext);

        if(getIntent() != null){
            daid = getIntent().getStringExtra("DAID");
            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();
            intentDigitalasset = (DigitalAssetsMaster)bundle.getSerializable("valuesingle");
            //digitalAssetsList = (List<DigitalAssetsMaster>)bundle.getSerializable("value");
            CurrentDigigtalAsset = getIntent().getIntExtra(com.swaas.kangle.DigitalAssetPlayer.customviews.pdf.util.Constants.POSITION, 0);
        }
        postsuccess = new PostComment();
        playMode = new PlayMode(mActivity);
        initializeView();
        bindOnClickEvents();
        setUpRecyclerView();
        setupViewPager(viewpager);
        viewpager.setCurrentItem(0);
        setthemeforView();
        getAssetDetail(daid);

        PreferenceUtils.setVisibleActivityName(mContext,"newAsset");
    }

    public void initializeView(){
        companylogo = (ImageView) findViewById(R.id.companylogo);
        notification= (ImageView) findViewById(R.id.notification);
        settings = (ImageView) findViewById(R.id.settings);
        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        downloadbutton = (ImageView) findViewById(R.id.download_button);
        playbutton = (ImageView) findViewById(R.id.play_button);
        viewpager = (ViewPager) findViewById(R.id.rating_comment);
        name_of_asset = (TextView) findViewById(R.id.name_of_asset);
        name_of_asset_below_thumb = (TextView) findViewById(R.id.name_of_asset_below_thumb);
        rating_count = (TextView) findViewById(R.id.ratingcount);
        like_count = (TextView) findViewById(R.id.likecount);
        view_count = (TextView) findViewById(R.id.viewscount);
        cat_value = (TextView) findViewById(R.id.cat_value);
        tag_value = (TextView) findViewById(R.id.tag_value);
        cat_name = (TextView) findViewById(R.id.cat_name);

        assettitle = (TextView) findViewById(R.id.assettitle);
        userThumbnailImage = (ImageView) findViewById(R.id.userThumbnailImage);

        readMore = (TextView) findViewById(R.id.readmore);
        description = (TextView) findViewById(R.id.description_text);
        fulldescription = (TextView) findViewById(R.id.fulldescription);
        moreassets = (TextView) findViewById(R.id.moreassets);
        morediscussions = (TextView) findViewById(R.id.morediscussions);

        moretagedssets = (TextView) findViewById(R.id.moretagedssets);
        tag_name = (TextView) findViewById(R.id.tag_name);

        relatedDiscussions = (RelativeLayout) findViewById(R.id.relatedDiscussions);
        relatedAssetList = (RelativeLayout) findViewById(R.id.relatedAssetList);
        tagedAssetList = (RelativeLayout) findViewById(R.id.tagedAssetList);

        discussrecyclerView = (EmptyRecyclerView) findViewById(R.id.discussionList);
        relatedRecyclerView = (EmptyRecyclerView) findViewById(R.id.RelatedList);
        tagRecyclerView = (EmptyRecyclerView) findViewById(R.id.TagedList);

        viewpager1 = findViewById(R.id.viewpager1);
        viewpager2 = findViewById(R.id.viewpager2);

        header = (LinearLayout) findViewById(R.id.header);

        expanddes_cat = (ImageView) findViewById(R.id.expanddes_cat);
        catnamesec = findViewById(R.id.catnamesec);
        tagnamesec = findViewById(R.id.tagnamesec);
        detailsfullview = findViewById(R.id.detailsfullview);
        comment_count = (TextView)findViewById(R.id.comment_count);
        likebutton = (ImageView) findViewById(R.id.likebutton);
        disabledbutton = (ImageView)findViewById(R.id.disabledbutton);
        likebutton1 = (ImageView) findViewById(R.id.likebutton1);
        disabledbutton1 = (ImageView)findViewById(R.id.disabledbutton1);

        comment = (EditText) findViewById(R.id.comment);
        post = (ImageView) findViewById(R.id.post);
        textlength = (TextView) findViewById(R.id.textlength);
        layoutthumbnail = findViewById(R.id.layoutthumbnail);

        cardViewLayout = (CardView) findViewById(R.id.cardViewLayout);
        Reviewssection = (RelativeLayout) findViewById(R.id.Reviewssection);
        cat_text = (TextView) findViewById(R.id.cat_text);
        tag_text = (TextView) findViewById(R.id.tag_text);
        assetanalytics_title = (TextView) findViewById(R.id.assetanalytics_title);
        discussiontext = (TextView) findViewById(R.id.discussiontext);
        viewsicon = (ImageView) findViewById(R.id.viewsicon);
        likeicon = (ImageView) findViewById(R.id.likeicon);
        commenticon = (ImageView) findViewById(R.id.commenticon);
        relatedasset_text = (TextView) findViewById(R.id.relatedasset_text);

        expanddes_comments = (ImageView) findViewById(R.id.expanddes_comments);
        coments_sec = findViewById(R.id.coments_sec);

        relatedasset_tags_text = (TextView) findViewById(R.id.relatedasset_tags_text);
    }

    public void setthemeforView(){

        header.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        detailsfullview.setBackgroundColor(Color.parseColor(Constants.COMPANY_COLOR));
        //playbutton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        //downloadbutton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.COMPANY_COLOR)));
        layoutthumbnail.setBackgroundColor(mContext.getResources().getColor(R.color.topbar));
        cardViewLayout.setCardBackgroundColor(Color.parseColor(Constants.CARDBACKGROUND_COLOR));

        companylogo.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        downloadbutton.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        cat_value.setTextColor(Color.parseColor(Constants.TEXT_COLOR));

        name_of_asset.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        name_of_asset_below_thumb.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        fulldescription.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        cat_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        cat_value.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tag_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        tag_value.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        //downloadbutton.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        assetanalytics_title.setTextColor(Color.parseColor(Constants.TEXT_COLOR));

        viewsicon.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        likeicon.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        commenticon.setColorFilter(Color.parseColor(Constants.COMPANY_COLOR));
        view_count.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        like_count.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        comment_count.setTextColor(Color.parseColor(Constants.TEXT_COLOR));

        moreassets.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        moretagedssets.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        morediscussions.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        discussiontext.setTextColor(Color.parseColor(Constants.TEXT_COLOR));

        textlength.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        post.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        expanddes_cat.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        expanddes_comments.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));


        relatedAssetList.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.CARDBACKGROUND_COLOR)));
        Reviewssection.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.CARDBACKGROUND_COLOR)));
        relatedDiscussions.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.CARDBACKGROUND_COLOR)));

        likebutton.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        likebutton1.setColorFilter(Color.parseColor(Constants.TEXT_COLOR));
        disabledbutton.setColorFilter(Color.parseColor(Constants.PARTIALLY_COMPLETED_COLOR));
        disabledbutton1.setColorFilter(Color.parseColor(Constants.PARTIALLY_COMPLETED_COLOR));
        relatedasset_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));
        relatedasset_tags_text.setTextColor(Color.parseColor(Constants.TEXT_COLOR));

        morediscussions.setTypeface(morediscussions.getTypeface(), Typeface.ITALIC);
        morediscussions.setText(Html.fromHtml("<u>"+mActivity.getResources().getString(R.string.showmore)+"</u>"));
        moreassets.setTypeface(morediscussions.getTypeface(), Typeface.ITALIC);
        moreassets.setText(Html.fromHtml("<u>"+mActivity.getResources().getString(R.string.showmore)+"</u>"));
        moretagedssets.setTypeface(morediscussions.getTypeface(), Typeface.ITALIC);
        moretagedssets.setText(Html.fromHtml("<u>"+mActivity.getResources().getString(R.string.showmore)+"</u>"));
    }

    public void bindOnClickEvents(){

        companylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceUtils.getOfflineMode(mContext).equalsIgnoreCase("true")){
                    //playMode.offlinePlayClcik(digitalAssetsList.get(CurrentDigigtalAsset),0);
                    playMode.offlinePlayClcik(intentDigitalasset,0);
                } else {
                    if (NetworkUtils.checkIfNetworkAvailable(mActivity)) {
                        //playMode.onlinePlayClcik(digitalAssetsList.get(CurrentDigigtalAsset),0);
                        playMode.onlinePlayClcik(intentDigitalasset,0);
                    } else {
                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        downloadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtils.checkIfNetworkAvailable(mContext)){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        int storagePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
                        if (storagePermission == PackageManager.PERMISSION_GRANTED) {
                            showdownloadpopup();
                        } else {
                            NewAssetDetailActivity.this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_PERMISSION_STORAGE);
                        }
                    } else {
                        showdownloadpopup();
                    }
                } else{
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
            }
        });

        morediscussions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewAssetDetailActivity.this, DiscussionsListActivity.class);
                i.putExtra("Asset",digitalAssetsMaster);
                startActivity(i);
            }
        });

        moreassets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewAssetDetailActivity.this,AssetListByCategoryActivity.class);
                intent.putExtra("categoreyName",digitalAssetsMaster.getDACategoryName());
                startActivity(intent);
            }
        });

        moretagedssets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewAssetDetailActivity.this,AssetListByCategoryActivity.class);
                intent.putExtra("TagsName",digitalAssetsMaster.getUDTags());
                startActivity(intent);
            }
        });

        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(description.isShown()) {
                    description.setVisibility(View.GONE);
                    fulldescription.setVisibility(View.VISIBLE);
                    readMore.setText(getResources().getString(R.string.readless));
                }else{
                    description.setVisibility(View.VISIBLE);
                    fulldescription.setVisibility(View.GONE);
                    readMore.setText(getResources().getString(R.string.readmore));
                }
            }
        });

        expanddes_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fulldescription.isShown()) {
                    fulldescription.setVisibility(View.GONE);
                    expanddes_cat.setImageResource(R.drawable.ic_keyboard_arrow_down_black_36dp);
                }else{
                    fulldescription.setVisibility(View.VISIBLE);
                    expanddes_cat.setImageResource(R.drawable.ic_keyboard_arrow_up_black_36dp);
                }
            }
        });

        expanddes_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(coments_sec.isShown()) {
                    coments_sec.setVisibility(View.GONE);
                    expanddes_comments.setImageResource(R.drawable.ic_keyboard_arrow_down_black_36dp);
                }else{
                    coments_sec.setVisibility(View.VISIBLE);
                    expanddes_comments.setImageResource(R.drawable.ic_keyboard_arrow_up_black_36dp);
                }
            }
        });


        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textlength.setText(100 - comment.getText().toString().trim().length()+" "
                        + getResources().getString(R.string.charactersremainig));
                if(comment.getText().toString().trim().length() > 100){
                    post.setVisibility(View.GONE);
                }else{
                    post.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String comentText = comment.getText().toString().trim();
                if(comment.getText().toString().trim().length() == 0){
                    post.setVisibility(View.GONE);
                }
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitNewpost();
            }
        });

        likebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disabledbutton.setVisibility(View.VISIBLE);
                disabledbutton1.setVisibility(View.VISIBLE);
                likebutton.setVisibility(View.GONE);
                likebutton1.setVisibility(View.GONE);
                Toast.makeText(mContext,mContext.getResources().getString(R.string.thankyou),Toast.LENGTH_LONG).show();
                //like_count.setText(String.valueOf(digitalAssetsMaster.getTotalLikes()+1));
                submitlike();
            }
        });

        likebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disabledbutton.setVisibility(View.VISIBLE);
                disabledbutton1.setVisibility(View.VISIBLE);
                likebutton.setVisibility(View.GONE);
                likebutton1.setVisibility(View.GONE);
                Toast.makeText(mContext,mContext.getResources().getString(R.string.thankyou),Toast.LENGTH_LONG).show();
                //like_count.setText(String.valueOf(digitalAssetsMaster.getTotalLikes()+1));
                submitlike();
            }
        });
    }

    private void setUpRecyclerView() {
        DiscusslayoutManager = new LinearLayoutManager(this);
        RelatedlayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        TagedlayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        discussrecyclerView.setLayoutManager(DiscusslayoutManager);
        relatedRecyclerView.setLayoutManager(RelatedlayoutManager);
        tagRecyclerView.setLayoutManager(TagedlayoutManager);
    }

    public void getAssetDetail(String assetId){
        if(PreferenceUtils.getOfflineMode(mContext).equalsIgnoreCase("true")){
            digitalAssetsMaster = digitalAssetoffline.getOfflineAssetDetail(assetId);
            digitalAssetTransactionMaster = new DigitalAssetTransactionMaster();
            digitalAssetTransactionMaster.setDAID(digitalAssetsMaster.getDAID());
        }else{
            digitalAssetsMaster = digitalAssetHeaderRepository.getAssetDetail(assetId);
            digitalAssetTransactionMaster = new DigitalAssetTransactionMaster();
            digitalAssetTransactionMaster.setDAID(digitalAssetsMaster.getDAID());
        }
        loadView();
    }

    public void loadView(){
        if(digitalAssetsMaster != null){
            if(digitalAssetsMaster.getIsDownloadable().equalsIgnoreCase("y")){
                downloadbutton.setVisibility(View.VISIBLE);
            }
            if(digitalAssetsMaster.getIsViewable().equalsIgnoreCase("y")){
                playbutton.setVisibility(View.VISIBLE);
            }

          /*  if(digitalAssetsMaster.getUser_Like() == 0 ||digitalAssetsMaster.getUser_Like() >0){
                disabledbutton.setVisibility(View.VISIBLE);
                likebutton.setVisibility(View.GONE);
                disabledbutton1.setVisibility(View.VISIBLE);
                likebutton1.setVisibility(View.GONE);
            }else{
                disabledbutton.setVisibility(View.GONE);
                likebutton.setVisibility(View.VISIBLE);
                disabledbutton1.setVisibility(View.GONE);
                likebutton1.setVisibility(View.VISIBLE);
            }*/


            if(digitalAssetsMaster.getUser_Like() == 0 ||digitalAssetsMaster.getUser_Like() >0){
            //    disabledbutton.setVisibility(View.VISIBLE);
                likebutton.setVisibility(View.VISIBLE);
                disabledbutton1.setVisibility(View.GONE);
           //     likebutton1.setVisibility(View.GONE);
            }else{
           //     disabledbutton.setVisibility(View.GONE);
                likebutton.setVisibility(View.VISIBLE);
                disabledbutton1.setVisibility(View.GONE);
           //     likebutton1.setVisibility(View.VISIBLE);
            }


            if(digitalAssetsMaster.getThumnailURL().contains("wideangleimages")){
                if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("video")) {
                    thumbnail.setImageResource(R.drawable.icon_mp4);
                } else if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("articulate")
                        || digitalAssetsMaster.getDA_Type().equalsIgnoreCase("html5")) {
                    thumbnail.setImageResource(R.drawable.icon_html);
                } else if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("image")) {
                    thumbnail.setImageResource(R.drawable.icon_jpeg);
                } else if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("document")) {
                    if(digitalAssetsMaster.getOnlineURL().endsWith(".pdf")){
                        thumbnail.setImageResource(R.drawable.icon_pdf);
                    } else if(digitalAssetsMaster.getOnlineURL().endsWith(".ppt")
                            || digitalAssetsMaster.getOnlineURL().endsWith(".pptx")){
                        thumbnail.setImageResource(R.drawable.icon_ppt2);
                    }  else if(digitalAssetsMaster.getOnlineURL().endsWith("xls")
                            || digitalAssetsMaster.getOnlineURL().endsWith(".xlsx")){
                        thumbnail.setImageResource(R.drawable.icon_excel);
                    } else{
                        thumbnail.setImageResource(R.drawable.icon_doc);
                    }
                }

                    thumbnail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.ICON_COLOR)));

            }else {
                if (NetworkUtils.checkIfNetworkAvailable(mActivity)) {
                    if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("image")) {
                        Ion.with(thumbnail).fitXY().placeholder(R.drawable.icon_jpeg).fitXY().error(R.drawable.icon_jpeg).fitXY().load(
                                (!TextUtils.isEmpty(digitalAssetsMaster.getThumnailURL()))
                                        ? digitalAssetsMaster.getThumnailURL() : digitalAssetsMaster.getThumnailURL());
                    } else if(digitalAssetsMaster.getDA_Type().equalsIgnoreCase("video")){
                        Ion.with(thumbnail).fitXY().placeholder(R.drawable.icon_mp4).fitXY().error(R.drawable.icon_mp4).fitXY().load(
                                (!TextUtils.isEmpty(digitalAssetsMaster.getThumnailURL()))
                                        ? digitalAssetsMaster.getThumnailURL() : digitalAssetsMaster.getThumnailURL());
                    }else if(digitalAssetsMaster.getDA_Type().equalsIgnoreCase("articulate")
                            || digitalAssetsMaster.getDA_Type().equalsIgnoreCase("html5")){
                        Ion.with(thumbnail).fitXY().placeholder(R.drawable.icon_html).fitXY().error(R.drawable.icon_html).fitXY().load(
                                (!TextUtils.isEmpty(digitalAssetsMaster.getThumnailURL()))
                                        ? digitalAssetsMaster.getThumnailURL() : digitalAssetsMaster.getThumnailURL());
                    }else if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("document")) {
                        if(digitalAssetsMaster.getOnlineURL().endsWith(".pdf")){
                            Ion.with(thumbnail).fitXY().placeholder(R.drawable.icon_pdf).fitXY().error(R.drawable.icon_pdf).fitXY().load(
                                    (!TextUtils.isEmpty(digitalAssetsMaster.getThumnailURL()))
                                            ? digitalAssetsMaster.getThumnailURL() : digitalAssetsMaster.getThumnailURL());
                        } else if(digitalAssetsMaster.getOnlineURL().endsWith(".ppt")
                                || digitalAssetsMaster.getOnlineURL().endsWith(".pptx")){
                            Ion.with(thumbnail).fitXY().placeholder(R.drawable.icon_ppt2).fitXY().error(R.drawable.icon_ppt2).fitXY().load(
                                    (!TextUtils.isEmpty(digitalAssetsMaster.getThumnailURL()))
                                            ? digitalAssetsMaster.getThumnailURL() : digitalAssetsMaster.getThumnailURL());
                        }else if(digitalAssetsMaster.getOnlineURL().endsWith(".xls") || digitalAssetsMaster.getOnlineURL().endsWith(".xlsx")){
                            Ion.with(thumbnail).fitXY().placeholder(R.drawable.icon_excel).fitXY().error(R.drawable.icon_excel).fitXY().load(
                                    (!TextUtils.isEmpty(digitalAssetsMaster.getThumnailURL()))
                                            ? digitalAssetsMaster.getThumnailURL() : digitalAssetsMaster.getThumnailURL());
                        } else{
                            Ion.with(thumbnail).fitXY().placeholder(R.drawable.icon_doc).fitXY().error(R.drawable.icon_doc).fitXY().load(
                                    (!TextUtils.isEmpty(digitalAssetsMaster.getThumnailURL()))
                                            ? digitalAssetsMaster.getThumnailURL() : digitalAssetsMaster.getThumnailURL());
                        }
                    }
                    //thumbnail.setImageBitmap(getBitmapFromURL(digitalAssetsMaster.getThumnailURL()));
                } else {
                    if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("video")) {
                        thumbnail.setImageResource(R.drawable.icon_mp4);
                    } else if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("articulate") ||
                            digitalAssetsMaster.getDA_Type().equalsIgnoreCase("html5")) {
                        thumbnail.setImageResource(R.drawable.icon_html);
                    } else if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("image")) {
                        thumbnail.setImageResource(R.drawable.icon_jpeg);
                    } else if (digitalAssetsMaster.getDA_Type().equalsIgnoreCase("document")) {
                        if (digitalAssetsMaster.getOnlineURL().endsWith(".pdf")) {
                            thumbnail.setImageResource(R.drawable.icon_pdf);
                        } else if (digitalAssetsMaster.getOnlineURL().endsWith(".ppt")
                                || digitalAssetsMaster.getOnlineURL().endsWith(".pptx")) {
                            thumbnail.setImageResource(R.drawable.icon_ppt2);
                        } else if(digitalAssetsMaster.getOnlineURL().endsWith("xls")
                                    || digitalAssetsMaster.getOnlineURL().endsWith(".xlsx")){
                            thumbnail.setImageResource(R.drawable.icon_excel);
                        } else {
                            thumbnail.setImageResource(R.drawable.icon_doc);
                        }
                    }
                }

                    thumbnail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.ICON_COLOR)));

            }

            if(PreferenceUtils.getOfflineMode(mContext).equalsIgnoreCase("true")){
                downloadbutton.setVisibility(View.GONE);
            }

            if(digitalAssetsMaster.getDA_Type().equalsIgnoreCase("articulate")
                    || digitalAssetsMaster.getDA_Type().equalsIgnoreCase("html5")||digitalAssetsMaster.getIsDownloadable().equalsIgnoreCase("N")) {
                downloadbutton.setVisibility(View.GONE);
                playbutton.setVisibility(View.VISIBLE);
            }
            Gson gsonget = new Gson();
            User userobj = gsonget.fromJson(PreferenceUtils.getUser(mContext), User.class);

            Ion.with(userThumbnailImage).fitCenter().placeholder(R.drawable.default_user_icon).fitXY().error(R.drawable.icon_jpeg).fitXY().load(
                    (!TextUtils.isEmpty(userobj.getProfile_Photo_BLOB_URL()))? userobj.getProfile_Photo_BLOB_URL() : userobj.getProfile_Photo_BLOB_URL());
            //userThumbnailImage.setImageBitmap(getBitmapFromURL(userobj.getProfile_Photo_BLOB_URL()));

            name_of_asset.setText(digitalAssetsMaster.getDAName());
            name_of_asset_below_thumb.setText(digitalAssetsMaster.getDAName());
            assettitle.setText(digitalAssetsMaster.getDAName());
            rating_count.setText(String.valueOf(digitalAssetsMaster.getTotalRatings()));
            cat_name.setText(digitalAssetsMaster.getDACategoryName());

            description.setText(digitalAssetsMaster.getDA_Description().toString());
            fulldescription.setText(digitalAssetsMaster.getDA_Description().toString());

            String[] words=digitalAssetsMaster.getUDTags().split("#");
            String tagname = "";
            String tagdisplay = "";
            List<String> tags = new ArrayList<String>();
            for (String word : words){
                tagdisplay = word;
                tags.add(word);
            }
            for(int i=0;i<tags.size();i++) {
                tagname = tagname+","+ tags.get(i);
            }
            if(tagname.length() > 0) {
                StringBuilder sb = new StringBuilder(tagname);
                sb.deleteCharAt(0);
                tag_value.setText(sb.toString());
            }else{
                tag_value.setText(" - ");
            }
            cat_value.setText(digitalAssetsMaster.getDACategoryName());
            tag_name.setText(tagdisplay);

            if(PreferenceUtils.getOfflineMode(mContext).equalsIgnoreCase("true")) {
                relatedDiscussions.setVisibility(View.GONE);
                relatedAssetList.setVisibility(View.GONE);
                tagedAssetList.setVisibility(View.GONE);
            }else if(!NetworkUtils.checkIfNetworkAvailable(mContext)){
                relatedDiscussions.setVisibility(View.GONE);
                relatedAssetList.setVisibility(View.GONE);
                tagedAssetList.setVisibility(View.GONE);
            }else{
                relatedAssetList.setVisibility(View.VISIBLE);
                tagedAssetList.setVisibility(View.VISIBLE);
                getrelatedassetsbyCategorey(digitalAssetsMaster.getDACategoryName());
                check = digitalAssetsMaster.getDACategoryName();
                getAssetsDAIDs();
                getposts();
            }
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    public void getrelatedassetsbyCategorey(String categ){
        List<DigitalAssetsMaster> asset = digitalAssetHeaderRepository.getRelatedAssetsByCategoryNameLimit5(categ,digitalAssetsMaster.getDAID());
        List<DigitalAssetsMaster> assetlisttodisplay = new ArrayList<DigitalAssetsMaster>();
        if(asset != null){
            if(asset.size()>0){
                if(asset.size() > 5){
                    int i=0;
                    for(DigitalAssetsMaster digitalAssetsMaster : asset) {
                        assetlisttodisplay.add(digitalAssetsMaster);
                        i++;
                        if(i>4){
                            break;
                        }
                    }
                }else {
                    assetlisttodisplay = asset;
                }

                if(assetlisttodisplay.size() == 5){
                    moreassets.setVisibility(View.VISIBLE);
                }else{
                    moreassets.setVisibility(View.GONE);
                }
                relatedadapter = new NewRelatedAssetListItemRecyclerAdapter(NewAssetDetailActivity.this,assetlisttodisplay);
                relatedRecyclerView.setAdapter(relatedadapter);
            }else {
                relatedAssetList.setVisibility(View.GONE);
            }
        }else{
            relatedAssetList.setVisibility(View.GONE);
        }
    }


    public void getAssetsDAIDs(){
        try{
            String TAGS = "";
            String[] tags = digitalAssetsMaster.getUDTags().split("#");
            for (String t : tags) {
                TAGS =  TAGS + "," + "'" + t.toString() + "'";
            }
            StringBuilder sb = new StringBuilder(TAGS);
            sb.deleteCharAt(0);
            String values = sb.toString().replace("''", "'");
            values.length();

            String selectQuery = "SELECT tbl_DIGASSETS_CAT_TAG_MASTER.DAID FROM tbl_DIGASSETS_CAT_TAG_MASTER WHERE tbl_DIGASSETS_CAT_TAG_MASTER.Tags in ("+values+") ";
            selectQuery += "AND tbl_DIGASSETS_CAT_TAG_MASTER.DAID not in ("+digitalAssetsMaster.getDAID()+") Group by DAID";

            DigitalassetCatTagFilterRepository digitalassetCatTagFilterRepository = new DigitalassetCatTagFilterRepository(mContext);
            List<DigitalAssetsMaster> DAIDlist = digitalassetCatTagFilterRepository.getAssetIdbySelectedCatTag(selectQuery);
            DAIDlist.size();
            getrelatedassetsbyTags(DAIDlist);
        }catch(Exception e){

        }
    }

    public void getrelatedassetsbyTags(List<DigitalAssetsMaster> DAIDlist){

        try{
            List<String> assetsId = new ArrayList<>();
            String assetIdnames = "";
            for (DigitalAssetsMaster catname : DAIDlist) {
                assetsId.add(catname.getDAID());
            }
            for (String catname : assetsId) {
                assetIdnames =  assetIdnames + "," + "'" + catname.toString() + "'";
            }
            StringBuilder sb = new StringBuilder(assetIdnames);
            sb.deleteCharAt(0);
            String values = sb.toString().replace("''", "'");
            values.length();
            List<DigitalAssetsMaster> asset = digitalAssetHeaderRepository.getFilteredAssets(values);
            List<DigitalAssetsMaster> assetlisttodisplay = new ArrayList<DigitalAssetsMaster>();
            if(asset != null){
                if(asset.size()>0){
                    if(asset.size() > 5){
                        int i=0;
                        for(DigitalAssetsMaster digitalAssetsMaster : asset) {
                            assetlisttodisplay.add(digitalAssetsMaster);
                            i++;
                            if(i>4){
                                break;
                            }
                        }
                    }else {
                        assetlisttodisplay = asset;
                    }

                    if(assetlisttodisplay.size() == 5){
                        moretagedssets.setVisibility(View.VISIBLE);
                    }else{
                        moretagedssets.setVisibility(View.GONE);
                    }
                    tagededadapter = new NewRelatedAssetListItemRecyclerAdapter(NewAssetDetailActivity.this,assetlisttodisplay);
                    tagRecyclerView.setAdapter(tagededadapter);
                } else if(check.equals(digitalAssetsMaster.getDACategoryName()))
                {
                    tagedAssetList.setVisibility(View.GONE);
                }
                else {
                    tagedAssetList.setVisibility(View.GONE);
                }
            }else{
                tagedAssetList.setVisibility(View.GONE);
            }
        }catch (Exception e){
            Log.e("Exception",e.toString());
            tagedAssetList.setVisibility(View.GONE);
        }
    }

    public void getposts(){
        String assetportion = ("?assetId="+digitalAssetsMaster.getDAID());
        Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
        AssetService assetService = retrofitAPI.create(AssetService.class);
        Map<String, String> data1 = new HashMap<>();
        data1.put("assetId", digitalAssetsMaster.getDAID());
        Call call = assetService.getNewComments(data1);
        call.enqueue(new Callback<ArrayList<PostComment>>() {

            @Override
            public void onResponse(Response<ArrayList<PostComment>> response, Retrofit retrofit) {
                ArrayList<PostComment> apiResponse = response.body();
                if (apiResponse != null) {
                    relatedPosts = apiResponse;
                    Log.d("log","assetsForBrowses");
                    loadAdapter(relatedPosts);
                    //gotoCategoriesView();
                } else {
                    Log.d("retrofit","error 2");
                    //error
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("retrofit","error 2");
            }
        });
    }

    public void loadAdapter(List<PostComment> relatedPosts){
        like_count.setText(String.valueOf(digitalAssetsMaster.getTotalLikes()));
        view_count.setText(String.valueOf(digitalAssetsMaster.getTotalViews()));
        comment_count.setText(String.valueOf(relatedPosts.size()));
        dismissProgressDialog();
        if(relatedPosts.isEmpty()){
            discussrecyclerView.setVisibility(View.GONE);
            //relatedDiscussions.setVisibility(View.GONE);
        }else {
            relatedDiscussions.setVisibility(View.VISIBLE);
            discussrecyclerView.setVisibility(View.VISIBLE);
            if(relatedPosts.size()>3){
                ArrayList<PostComment> minimumpost = new ArrayList<PostComment>();
                int count = 0;
                for(PostComment postComment: relatedPosts){
                    minimumpost.add(postComment);
                    count++;
                    if(count>2){
                        break;
                    }
                }
                discussionadapter = new NewDiscussionListItemRecyclerAdapter(NewAssetDetailActivity.this, minimumpost,digitalAssetsMaster.getDAID());
                discussrecyclerView.setAdapter(discussionadapter);
                morediscussions.setVisibility(View.VISIBLE);
            }else{
                morediscussions.setVisibility(View.GONE);
                discussionadapter = new NewDiscussionListItemRecyclerAdapter(NewAssetDetailActivity.this, relatedPosts,digitalAssetsMaster.getDAID());
                discussrecyclerView.setAdapter(discussionadapter);
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {

        DetailsTabsPagerAdapter assetDetailsViewPager = new DetailsTabsPagerAdapter(getSupportFragmentManager());

        assetDetailsViewPager.addFragment(new RatingViewpagerFragment(), "Details");
        assetDetailsViewPager.addFragment(new commentViewpagerFragment(), "Discussion");
        viewPager.setAdapter(assetDetailsViewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1){
                    viewpager1.setBackgroundColor(getResources().getColor(R.color.grey));
                    viewpager2.setBackgroundColor(getResources().getColor(R.color.black));
                }else{
                    viewpager1.setBackgroundColor(getResources().getColor(R.color.black));
                    viewpager2.setBackgroundColor(getResources().getColor(R.color.grey));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class DetailsTabsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public DetailsTabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        public Fragment getItem(int index) {

            switch (index) {
                case 0:
                    RatingViewpagerFragment ratingfragment = new RatingViewpagerFragment();
                    Bundle masterBundle = new Bundle();
                    masterBundle.putSerializable("assetDetails", digitalAssetsMaster);
                    ratingfragment.setArguments(masterBundle);
                    return ratingfragment;
                case 1:
                    commentViewpagerFragment commentfragment = new commentViewpagerFragment();
                    Bundle discussionbundle = new Bundle();
                    discussionbundle.putSerializable("assetDetails", digitalAssetsMaster);
                    commentfragment.setArguments(discussionbundle);
                    return commentfragment;
            }

            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    public void showProgressDialog(String Message){
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(Message);
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    public void showLoading(String Message){
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(Message);
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    public void dismissProgressDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }


    public void submitNewpost(){
        if(NetworkUtils.checkIfNetworkAvailable(mActivity)) {
            String message = comment.getText().toString().trim();
            mActivity.showLoading(mActivity.getResources().getString(R.string.postingcoments));
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            AssetService assetService = retrofitAPI.create(AssetService.class);
            Gson gsonget = new Gson();
            User userobj = gsonget.fromJson(PreferenceUtils.getUser(mActivity), User.class);
            WallPost wallPost = new WallPost();
            wallPost.setCompany_Code(userobj.getCompany_Code());
            wallPost.setCompany_Id(userobj.getCompany_Id());
            wallPost.setContent_Id(intentDigitalasset.getDAID());
            wallPost.setEmployee_Name(userobj.getEmployee_Name());
            wallPost.setMessage(message);
            wallPost.setUser_Id(userobj.getUserID());
            Call call = assetService.postComments(wallPost);
            call.enqueue(new Callback<PostComment>() {

                @Override
                public void onResponse(Response<PostComment> response, Retrofit retrofit) {
                    PostComment apiResponse = response.body();
                    if (apiResponse != null) {
                        postsuccess = apiResponse;
                        Log.d("log", postsuccess.toString());
                        relatedPosts.add(postsuccess);
                        comment.setText("");
                        //gotoCategoriesView();
                        dismissProgressDialog();
                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.postsuccess), Toast.LENGTH_SHORT).show();
                        mActivity.loadAdapter(relatedPosts);
                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    } else {
                        mActivity.dismissProgressDialog();
                        Log.d("retrofit", "error 2");
                        //error
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    mActivity.dismissProgressDialog();
                    Log.d("retrofit", "error 2");
                }
            });

        }else{
            Toast.makeText(mActivity,mActivity.getResources().getString(R.string.error_message),Toast.LENGTH_SHORT).show();
        }
    }

    public void submitlike(){
        digitalasset = new DigitalAssets();
        digitalasset.setDA_Code(Integer.parseInt(intentDigitalasset.getDAID()));
        digitalasset.setUser_Like(1);
        digitalasset.setLike(intentDigitalasset.getTotalLikes()+1);
        digitalasset.setDetailed_DateTime(new Date().toString());
        digitalasset.setLattitude(latitude);
        digitalasset.setLongitude(longitude);
        digitalAssetTransactionRepository.insertOrUpdateDAAnalytics(digitalasset,false);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public void showdownloadpopup(){
        String name = mContext.getResources().getString(R.string.filename)+ digitalAssetsMaster.getDAName();
        String filesize = getResources().getString(R.string.file_size) +String.valueOf(digitalAssetsMaster.getDA_Size_In_MB())+" Mb";
            messageDialog.showConfirmDownloadPopup(mContext,name,filesize,new View.OnClickListener() {
                @Override
                public void onClick(View Approve) {
                    messageDialog.dialogDismiss();
                    playMode.onDownloadClicked(digitalAssetsMaster);
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View close) {
                    messageDialog.dialogDismiss();
                }
            }, true);
    }

}
