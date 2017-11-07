package com.rubatto.staralba;

/**
 * Created by Administrator on 2017-09-14.
 */

public class DataMyLocation {
    private static Double longitude;
    private static Double latitude;

    public static Double getLongitude() {
        return longitude;
    }

    public static void setLongitude(Double longitude) {
        DataMyLocation.longitude = longitude;
    }

    public static Double getLatitude() {
        return latitude;
    }

    public static void setLatitude(Double latitude) {
        DataMyLocation.latitude = latitude;
    }
}
