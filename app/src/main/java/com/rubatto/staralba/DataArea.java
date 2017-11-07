package com.rubatto.staralba;

/**
 * Created by Administrator on 2017-09-21.
 */

public class DataArea {
    private static String firstArea = "";
    private static String secondArea = "";
    private static String thirdArea = "";

    public static String getFirstArea() {
        return firstArea;
    }

    public static void setFirstArea(String firstArea) {
        DataArea.firstArea = firstArea;
    }

    public static String getSecondArea() {
        return secondArea;
    }

    public static void setSecondArea(String secondArea) {
        DataArea.secondArea = secondArea;
    }

    public static String getThirdArea() {
        return thirdArea;
    }

    public static void setThirdArea(String thirdArea) {
        DataArea.thirdArea = thirdArea;
    }
}
