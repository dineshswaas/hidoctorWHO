
package com.swaas.kangle.playerPart.customviews.pdf.util;

public class Constants {

    public static boolean DEBUG_MODE = false;

    /** Between 0 and 1, the thumbnails quality (default 0.3). Increasing this value may cause performance decrease */
    public static float THUMBNAIL_RATIO = 0.3f;

    /**
     * The size of the rendered parts (default 256)
     * Tinier : a little bit slower to have the whole page rendered but more reactive.
     * Bigger : user will have to wait longer to have the first visual results
     */
    public static float PART_SIZE = 256;

    /** Number of preloaded rows or columns */
    public static int PRELOAD_COUNT = 6;

    public static class Cache {


        public static int GRID_SIZE = 7;
        public static int CACHE_SIZE = (int) Math.pow(GRID_SIZE, 2d);
        /** The size of the cache (number of bitmaps kept) */
        //public static int CACHE_SIZE = 150;

        //public static int THUMBNAILS_CACHE_SIZE = 10;
        public static int THUMBNAILS_CACHE_SIZE = 10;



    }

    public static class Pinch {

        public static float MAXIMUM_ZOOM = 10;

        public static float MINIMUM_ZOOM = 1;

    }


    public static final int IMAGEASSET = 1;
    public static final int VIDEOASSET = 2;
    public static final int PDFASSET = 4;
    public static  final  int AUDIOASSET = 3;
    public static  final  int HTMLASSET = 5;
    public static  final  int BRIGHTCOVE = 6;
    public static final int OFFLINE = 0;
    public static final int ONLINE = 1;
    public static final String LIST  = "assetlist";
    public static final String POSITION  = "position";
    public static final String COURSESTATUS  = "coursestatus";

    public static final int MULTIPLE_CHOICE_TYPE = 1;
    public static  final  int RADIO_TYPE = 2;
    public static  final  int SCALING_TYPE = 3;
    public static  final  int YES_NO_NA_TYPE = 4;
    public static final int SINGLE_CHOICE_BUTTON_TYPE = 5;
    public static final int ESSAY_TYPE = 6;
    public static final int MATCH_THE_FOLLOWING = 7;

}
