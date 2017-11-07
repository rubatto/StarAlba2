package com.rubatto.staralba;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-09-13.
 */

public class DataMyBusinessInfo {
    public int BUSINESS_SEQ;
    public String PHONE;
    public String BUSINESS_NAME;
    public String TITLE;
    public String BUSINESS_USER;
    public String BUSINESS_PHONE;
    public String CONTENT;
    public String CATEGORY_SE_CD;
    public String PAY_SE_CD;
    public int PAY;
    public ArrayList<String> THEME_LIST;
    public ArrayList<String> GUARANTEE_LIST;
    public String ADDRESS;
    public String CATEGORY_SE_NM;
    public String START_DT;
    public String END_DT;
    public String USE_YN;
    public double LAT;
    public double LNG;

    public DataMyBusinessInfo(int BUSINESS_SEQ, String PHONE, String BUSINESS_NAME, String TITLE, String BUSINESS_USER, String BUSINESS_PHONE,
                              String CONTENT, String CATEGORY_SE_CD, String PAY_SE_CD, int PAY, ArrayList<String> THEME_LIST, ArrayList<String> GUARANTEE_LIST,
                              String ADDRESS, String CATEGORY_SE_NM, String START_DT, String END_DT, String USE_YN, double LAT, double LNG) {
        this.BUSINESS_SEQ = BUSINESS_SEQ;
        this.PHONE = PHONE;
        this.BUSINESS_NAME = BUSINESS_NAME;
        this.TITLE = TITLE;
        this.BUSINESS_USER = BUSINESS_USER;
        this.BUSINESS_PHONE = BUSINESS_PHONE;
        this.CONTENT = CONTENT;
        this.CATEGORY_SE_CD = CATEGORY_SE_CD;
        this.PAY_SE_CD = PAY_SE_CD;
        this.PAY = PAY;
        this.THEME_LIST = THEME_LIST;
        this.GUARANTEE_LIST = GUARANTEE_LIST;
        this.ADDRESS = ADDRESS;
        this.CATEGORY_SE_NM = CATEGORY_SE_NM;
        this.START_DT = START_DT;
        this.END_DT = END_DT;
        this.USE_YN = USE_YN;
        this.LAT = LAT;
        this.LNG = LNG;
    }

    public static ArrayList<DataMyBusinessInfo> myBusinessInfoData = new ArrayList<>();

    public static ArrayList<DataMyBusinessInfo> getMyBusinessInfoData() {
        return myBusinessInfoData;
    }

    public static void setMyBusinessInfoData(ArrayList<DataMyBusinessInfo> myBusinessInfoData) {
        DataMyBusinessInfo.myBusinessInfoData = myBusinessInfoData;
    }
}
