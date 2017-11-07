package com.rubatto.staralba;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import java.util.concurrent.ExecutionException;

public class MypageActivity extends AppCompatActivity {
    private String mSharedPhone;

    private Toolbar mToolbar;

    private ImageView mProfileImg;
    private EditText mAgeEd;
    private EditText mNicknameEd;
    private EditText mEmailEd;
    private EditText mPhoneEd;
    private RadioButton mFemaleRadio;
    private RadioButton mMaleRadio;
    private Button mMypageBt;

    private String mAge;
    private String mNickname;
    private String mEmail;
    private String mPhone;
    private String mSex;

    private GetMyPageTask mGetTask;
    private UserUpdateTask mUpdateTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        // TODO: 2017-09-20 sharedphone으로 바꿔야함
        // mSharedPhone = SharedPreferenceData.getPreferences(this, SharedPreferenceData.mPhoneTag);
        mSharedPhone = "01074787551";

        mToolbar = (Toolbar) findViewById(R.id.mypage_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProfileImg = (ImageView) findViewById(R.id.mypage_profile_img);
        mAgeEd = (EditText) findViewById(R.id.mypage_age_ed);
        mNicknameEd = (EditText) findViewById(R.id.mypage_nickname_ed);
        mEmailEd = (EditText) findViewById(R.id.mypage_mail_ed);
        mPhoneEd = (EditText) findViewById(R.id.mypage_phone_ed);
        mFemaleRadio = (RadioButton) findViewById(R.id.mypage_female_radio);
        mMaleRadio = (RadioButton) findViewById(R.id.mypage_male_radio);
        mMypageBt = (Button) findViewById(R.id.mypage_bt);

        // 마이페이지 정보가 있는지 없는지
        String userInfoResult = getUserInfo(mSharedPhone);
        if (userInfoResult.equals("") || userInfoResult.isEmpty()) {
            Toast.makeText(this, "마이페이지 정보가 없습니다", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            try {
                getJsonData(userInfoResult);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mPhoneEd.setText(mSharedPhone);

            mProfileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MypageActivity.this, "프로필 이미지", Toast.LENGTH_SHORT).show();
                }
            });

            mMypageBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAge = mAgeEd.getText().toString();
                    mNickname = mNicknameEd.getText().toString();
                    mEmail = mEmailEd.getText().toString();

                    if (mFemaleRadio.isChecked()) {
                        // 여자
                        mSex = "1";
                    } else if (mMaleRadio.isChecked()) {
                        // 남자
                        mSex = "0";
                    } else {
                        mSex = "";
                    }

                    if (mAge.isEmpty()) {
                        Toast.makeText(MypageActivity.this, "나이를 입력해 주세요", Toast.LENGTH_SHORT).show();
                    } else if (mNickname.isEmpty()) {
                        Toast.makeText(MypageActivity.this, "별명을 입력해 주세요", Toast.LENGTH_SHORT).show();
                    } else if (mEmail.isEmpty()) {
                        Toast.makeText(MypageActivity.this, "이메일을 입력해 주세요", Toast.LENGTH_SHORT).show();
                    } else if (mPhone.isEmpty()) {
                        Toast.makeText(MypageActivity.this, "전화번호를 입력해 주세요", Toast.LENGTH_SHORT).show();
                    } else if (mSex.equals("")) {
                        Toast.makeText(MypageActivity.this, "성별을 선택해 주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        String updateOk = updateUserInfo(mSharedPhone, mAge, mNickname, mEmail, mSex);
                        if (updateOk.equals("1")) {
                            Toast.makeText(MypageActivity.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                            DataSharedPreference.savePreferences(MypageActivity.this, DataSharedPreference.mNickNameTag, mNickname);
                            finish();
                        } else {
                            Toast.makeText(MypageActivity.this, "관리자에게 문의해 주세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    // get
    private String getUserInfo(String myNumber) {
        String result = "";

        mGetTask = new GetMyPageTask();
        try {
            result = mGetTask.execute(myNumber).get();
            Log.e("mypage]result", result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    // update
    private String updateUserInfo(String phone, String age, String nickname, String email, String sex) {
        String updateOk = "";

        mUpdateTask = new UserUpdateTask();
        try {
            String result = mUpdateTask.execute(phone, age, nickname, email, sex).get();

            JSONObject getJson = new JSONObject(result);
            updateOk = getJson.getString("data");
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        return updateOk;
    }

    // 받아온 정보 json 분해
    private void getJsonData(String result) throws JSONException {
        JSONObject jsonObj = new JSONObject(result);
        JSONArray jsonArray = jsonObj.getJSONArray("mypage");
        JSONObject myPageObj = (JSONObject) jsonArray.get(0);
        mAge = myPageObj.getString("AGE");
        mNickname = myPageObj.getString("USER_ID");
        mEmail = myPageObj.getString("EMAIL");
        mPhone = myPageObj.getString("PHONE");
        mSex = myPageObj.getString("SEX");

        mAgeEd.setText(mAge);
        mNicknameEd.setText(mNickname);
        mEmailEd.setText(mEmail);
        mPhoneEd.setText(mPhone);
        if (mSex.equals("1")) {
            mFemaleRadio.setChecked(true);
        } else if (mSex.equals("0")) {
            mMaleRadio.setChecked(true);
        }
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

    // get
    public class GetMyPageTask extends AsyncTask<String, Void, String> {
        private String mSendMsg;
        private String mReceiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://staralba3336.cafe24.com/star/mobile/myPageInfo.do");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                String phone = strings[0];
                mSendMsg = "&PHONE=" + phone;
                osw.write(mSendMsg);
                osw.flush();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder buffer = new StringBuilder();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                        mReceiveMsg = buffer.toString();
                    }
                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mReceiveMsg;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onCancelled() {
            mGetTask = null;
        }
    }

    // update
    public class UserUpdateTask extends AsyncTask<String, Void, String> {
        private String mSendMsg;
        private String mReceiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://staralba3336.cafe24.com/star/mobile/updateUserInfo.do");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                String phone = strings[0];
                String age = strings[1];
                String nickname = strings[2];
                String email = strings[3];
                String sex = strings[4];
                mSendMsg = "&PHONE=" + phone + "&AGE=" + age + "&USER_ID=" + nickname + "&EMAIL=" + email + "&SEX=" + sex;
                osw.write(mSendMsg);
                osw.flush();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder buffer = new StringBuilder();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                        mReceiveMsg = buffer.toString();
                    }
                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mReceiveMsg;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onCancelled() {
            mUpdateTask = null;
        }
    }
}
