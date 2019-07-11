package com.swaas.kangle.db;

import java.io.Serializable;

/**
 * Created by Sayellessh on 27-04-2017.
 */

public class DigitalAssetUrlRepository implements Serializable {

    public static final String TABLE_DIGITAL_ASSET_URLS = "tbl_DIGASSETS_URLS";

    public static final String SlNo= "SlNo";
    public static final String DAEntryID = "DAEntryID";
    public static final String DAID = "DAID";
    public static final String DAMode = "DAMode";
    public static final String offlineURL = "offlineURL";
    public static final String onlineURL = "onlineURL";
    public static final String Tags= "Tags";
}
