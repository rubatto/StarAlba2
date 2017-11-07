package com.rubatto.staralba;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainSurroundFragment extends Fragment {
    private ArrayList<DataBusiness> dataArrayList;
    private GetDataTask mDataTask;
    private ProgressBar mProgressBar;
    private Dialog mDialog;
    private DataBusiness dataBusiness;

    private String mLat;
    private String mLon;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //dataList = new ArrayList<>();
        // TODO: 2017-09-16 change myNumber
        DataBusiness.getDataList().clear();
        String myNumber = "01041844910";
        getListData(myNumber);
    }

    private void getListData(String myNumber) {
        String result = "";
        dataArrayList = new ArrayList<>();

        int business_seq = 0;
        String phone = "";
        String business_name = "";
        String title = "";
        String business_user = "";
        String business_phone = "";
        String content = "";
        String category_se_cd = "";
        String pay_se_cd = "";
        int pay = 0;
        ArrayList<String> themeList;
        ArrayList<String> guaranteeList;
        String address = "";
        double distance = 0.0;
        String category_se_nm = "";
        String business_lat = "";
        String business_lng = "";

        mDataTask = new GetDataTask();

        mLat = String.valueOf(DataMyLocation.getLatitude());
        mLon = String.valueOf(DataMyLocation.getLongitude());
        try {
            result = mDataTask.execute(myNumber, mLat, mLon).get();

            JSONObject jsonObj = new JSONObject(result);
            Log.e("sourroundfrag] result", result);
            result = jsonObj.getString("data");
            JSONArray jsonArr = new JSONArray(result);

            int arrLength = jsonArr.length();
            for (int i = 0; i < arrLength; i++) {
                themeList = new ArrayList<>();
                guaranteeList = new ArrayList<>();

                JSONObject eachObj = new JSONObject(jsonArr.get(i).toString());
                //Log.e("getListData] eachObj " +i +"", eachObj.toString());

                // 업체_SEQ
                business_seq = eachObj.getInt("BUSINESS_SEQ");
                // 전화번호
                phone = eachObj.getString("PHONE");
                // 업체명
                business_name = eachObj.getString("BUSINESS_NAME");
                // 제목
                title = eachObj.getString("TITLE");
                // 업체 담당자
                business_user = eachObj.getString("BUSINESS_USER");
                // 업체 담당자
                business_phone = eachObj.getString("BUSINESS_PHONE");
                // 내용
                content = eachObj.getString("CONTENT");
                // 업종 구분
                category_se_cd = eachObj.getString("CATEGORY_SE_CD");
                // 급여 구분
                pay_se_cd = eachObj.getString("PAY_SE_CD");
                // 급여
                pay = eachObj.getInt("PAY");
                // 테마
                JSONArray themeArr = eachObj.getJSONArray("THEME");
                if (themeArr != null) {
                    int themeArrLenth = themeArr.length();
                    for (int j = 0; j < themeArrLenth; j++) {
                        // THEME
                        themeList.add(themeArr.getString(j));
                    }
                }
                // 보장
                JSONArray guranteeArr = eachObj.getJSONArray("GUARANTEE");
                if (guranteeArr != null) {
                    int guranteeArrLenth = guranteeArr.length();
                    for (int j = 0; j < guranteeArrLenth; j++) {
                        // GUARANTEE
                        guaranteeList.add(guranteeArr.getString(j));
                    }
                }
                // 주소
                address = eachObj.getString("ADDRESS");
                // 거리
                distance = eachObj.getDouble("DISTANCE");
                // 카테고리 명
                category_se_nm = eachObj.getString("CATEGORY_SE_NM");
                // 업체 좌표
                business_lat = eachObj.getString("LAT");
                business_lng = eachObj.getString("LNG");

                // DataBusiness에 데이터 저장
                dataBusiness = new DataBusiness(business_seq, phone, business_name, title, business_user, business_phone, content, category_se_cd, pay_se_cd, pay, themeList, guaranteeList, address, distance, category_se_nm, Double.parseDouble(business_lat), Double.parseDouble(business_lng));
                // databusiness 가진 listd에 databusiness 저장
                dataArrayList.add(dataBusiness);
            }
            DataBusiness.setDataList(dataArrayList);

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_surround, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.surround_fragment_progress);

        // recyclerview 적용
        final RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.main_surround_recyclerview);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);

        RecyclerView.Adapter mAdapter = new MainFragmentAdapter(dataArrayList, getActivity());

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    private class GetDataTask extends AsyncTask<String, Void, String> {
        private String sendMsg;
        private String receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlString = "http://staralba3336.cafe24.com/star/mobile/selectAllBusinessInfo.do";
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                String id = strings[0];
                String lat = strings[1];
                String lon = strings[2];
                sendMsg = "PHONE=" + id + "&LAT=" + lat + "&LNG=" + lon;
                Log.e("mainfrag]sendmsg", sendMsg);
                osw.write(sendMsg);
                osw.flush();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder buffer = new StringBuilder();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                        receiveMsg = buffer.toString();
                    }
                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onCancelled() {
            mDataTask = null;
        }
    }
}
