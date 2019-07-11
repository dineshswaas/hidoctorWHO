package com.swaas.kangle.utils;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Sayellessh on 26-04-2017.
 */

public class CommonUtils {

    public static String getUtcOffset() {
        TimeZone tz = TimeZone.getDefault();
        Date now = new Date();
        int offsetFromUtc = tz.getOffset(now.getTime()) / 1000 / 60;
        String utcofffset = String.valueOf(offsetFromUtc);
        //offsetFromUtc = 10000 + offsetFromUtc;
        return utcofffset;
    }

    public static String getUtcOffsetincluded10k() {
        TimeZone tz = TimeZone.getDefault();
        Date now = new Date();
        int offsetFromUtc = tz.getOffset(now.getTime()) / 1000 / 60;
        if(offsetFromUtc > 0){
            offsetFromUtc = 10000+offsetFromUtc;
        }else{
            offsetFromUtc = -(offsetFromUtc);
        }
        String utcofffset = String.valueOf(offsetFromUtc);
        //offsetFromUtc = 10000 + offsetFromUtc;
        return utcofffset;
    }
}
