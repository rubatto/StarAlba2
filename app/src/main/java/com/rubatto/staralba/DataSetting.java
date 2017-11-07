package com.rubatto.staralba;

/**
 * Created by Administrator on 2017-09-20.
 */

public class DataSetting {
    private static String alarmMode;
    private static String startHour;
    private static String startMinute;
    private static String endHour;
    private static String endMinute;

    public static String getAlarmMode() {
        return alarmMode;
    }

    public static void setAlarmMode(String alarmMode) {
        DataSetting.alarmMode = alarmMode;
    }

    public static String getStartHour() {
        return startHour;
    }

    public static void setStartHour(String startHour) {
        DataSetting.startHour = startHour;
    }

    public static String getStartMinute() {
        return startMinute;
    }

    public static void setStartMinute(String startMinute) {
        DataSetting.startMinute = startMinute;
    }

    public static String getEndHour() {
        return endHour;
    }

    public static void setEndHour(String endHour) {
        DataSetting.endHour = endHour;
    }

    public static String getEndMinute() {
        return endMinute;
    }

    public static void setEndMinute(String endMinute) {
        DataSetting.endMinute = endMinute;
    }
}
