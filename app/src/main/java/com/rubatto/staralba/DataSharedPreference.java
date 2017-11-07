package com.rubatto.staralba;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Administrator on 2017-09-20.
 */

public class DataSharedPreference {
    public final static String mAlarmModeTag = "ALARM_MODE";
    public final static String mStartHourTag = "START_HOUT";
    public final static String mStartMinuteTag = "START_MINUTE";
    public final static String mEndHourTag = "END_HOUT";
    public final static String mEndMinuteTag = "END_MINUTE";

    public final static String mNickNameTag = "NICKNAME";
    public final static String mPhoneTag = "PHONE";

    // 값 불러오기
    public static String getPreferences(Context context, String key){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(key, "");
    }

    // 값 저장하기
    public static void savePreferences(Context context, String key, String value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
