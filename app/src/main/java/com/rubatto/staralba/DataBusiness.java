package com.rubatto.staralba;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-09-13.
 */

public class DataBusiness {
    public int BUSINESS_SEQ;
    public String PHONE;
    public String BUSINESS_NAME;
    public String TITLE;
    public String BUSINESS_USER;
    public String BUSINESS_PHONE;
    public String CONTENT;
    public String CATEGORY_SE_CE;
    public String PAY_SE_CD;
    public int PAY;
    public ArrayList<String> THEME_LIST;
    public ArrayList<String> GUARANTEE_LIST;
    public String ADDRESS;
    public Double DISTANCE;
    public String CATEGORY_SE_NM;
    public double BUSINESS_LAT;
    public double BUSINESS_LNG;

    public DataBusiness(int BUSINESS_SEQ, String PHONE, String BUSINESS_NAME, String TITLE, String BUSINESS_USER, String BUSINESS_PHONE,
                        String CONTENT, String CATEGORY_SE_CE, String PAY_SE_CD, int PAY, ArrayList<String> THEME_LIST, ArrayList<String> GUARANTEE_LIST,
                        String ADDRESS, Double DISTANCE, String CATEGORY_SE_NM, double BUSINESS_LAT, double BUSINESS_LNG) {
        this.BUSINESS_SEQ = BUSINESS_SEQ;
        this.PHONE = PHONE;
        this.BUSINESS_NAME = BUSINESS_NAME;
        this.TITLE = TITLE;
        this.BUSINESS_USER = BUSINESS_USER;
        this.BUSINESS_PHONE = BUSINESS_PHONE;
        this.CONTENT = CONTENT;
        this.CATEGORY_SE_CE = CATEGORY_SE_CE;
        this.PAY_SE_CD = PAY_SE_CD;
        this.PAY = PAY;
        this.THEME_LIST = THEME_LIST;
        this.GUARANTEE_LIST = GUARANTEE_LIST;
        this.ADDRESS = ADDRESS;
        this.DISTANCE = DISTANCE;
        this.CATEGORY_SE_NM = CATEGORY_SE_NM;
        this.BUSINESS_LAT = BUSINESS_LAT;
        this.BUSINESS_LNG = BUSINESS_LNG;
    }

    public static ArrayList<DataBusiness> dataList = new ArrayList<>();
    public static ArrayList<DataBusiness> post_dataList = new ArrayList<>();

    public static ArrayList<DataBusiness> getDataList() {
        return dataList;
    }

    public static void setDataList(ArrayList<DataBusiness> dataList) {
        DataBusiness.dataList = dataList;
    }

    public static ArrayList<DataBusiness> getPost_dataList() {
        return post_dataList;
    }

    public static void setPost_dataList(ArrayList<DataBusiness> post_dataList) {
        DataBusiness.post_dataList = post_dataList;
    }
}
