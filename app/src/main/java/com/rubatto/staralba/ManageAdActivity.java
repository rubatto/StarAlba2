package com.rubatto.staralba;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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

public class ManageAdActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    private LinearLayout mRegister;
    private LinearLayout mNotice;
    private LinearLayout mGuide;

    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

    private GetDataTask mDataTask;
    private ArrayList<DataMyBusinessInfo> mDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_ad);

        mToolbar = (Toolbar) findViewById(R.id.manage_ad_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // TODO: 2017-09-21
        DataMyBusinessInfo.getMyBusinessInfoData().clear();
        String myNumber = "01041844910";
        getListData(myNumber);

        //
        mRegister = (LinearLayout) findViewById(R.id.manage_ad_resgister_linear);
        mNotice = (LinearLayout) findViewById(R.id.manage_ad_notice_linear);
        mGuide = (LinearLayout) findViewById(R.id.manage_ad_guide_linear);
        // recycler
        mProgressBar = (ProgressBar) findViewById(R.id.manage_ad_progress);
        mRecyclerView = (RecyclerView) findViewById(R.id.manage_ad_recyclerview);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ManageAdActivity.this, RegisterAdActivity.class);
                startActivity(i);
            }
        });
        mNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ManageAdActivity.this, NoticeActivity.class);
                startActivity(i);
            }
        });
        mGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ManageAdActivity.this, GuideAdActivity.class);
                startActivity(i);
            }
        });

        // 리사이클러
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);

        RecyclerView.Adapter mAdapter = new ManageAdRecyclerAdapter(mDataArrayList, this);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 백버튼
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return true;
    }

    private void getListData(String myNumber) {
        String result = "";
        mDataArrayList = new ArrayList<>();

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
        String category_se_nm = "";
        String start_dt = "";
        String end_dt = "";
        String use_yn = "";
        double lat = 0.0;
        double lng = 0.0;

        mDataTask = new GetDataTask();
        try {
            result = mDataTask.execute(myNumber).get();

            JSONObject jsonObj = new JSONObject(result);
            Log.e("manageAd] result", result);
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
                // 카테고리 명
                category_se_nm = eachObj.getString("CATEGORY_SE_NM");
                // 시작 날짜
                start_dt = eachObj.getString("START_DT");
                // 종료 날짜
                end_dt = eachObj.getString("END_DT");
                Log.e("end_dt", end_dt);
                // 사용 여부
                use_yn = eachObj.getString("USE_YN");
                // 좌표
                lat = eachObj.getDouble("LAT");
                lng = eachObj.getDouble("LNG");

                // DataBusiness에 데이터 저장
                DataMyBusinessInfo myDataBusiness = new DataMyBusinessInfo(business_seq, phone, business_name, title, business_user, business_phone, content, category_se_cd, pay_se_cd, pay, themeList, guaranteeList, address, category_se_nm, start_dt, end_dt, use_yn, lat, lng);
                // databusiness 가진 listd에 databusiness 저장
                mDataArrayList.add(myDataBusiness);
            }
            DataMyBusinessInfo.setMyBusinessInfoData(mDataArrayList);

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }

    private class GetDataTask extends AsyncTask<String, Void, String> {
        private String sendMsg;
        private String receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlString = "http://staralba3336.cafe24.com/star/mobile/selectUserBusinessInfo.do";
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                String id = strings[0];
                sendMsg = "PHONE=" + id;
                Log.e("manageAd]sendmsg", sendMsg);
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
