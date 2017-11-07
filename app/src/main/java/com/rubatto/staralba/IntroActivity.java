package com.rubatto.staralba;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class IntroActivity extends Activity implements LocationListener {
    Handler h;//핸들러 선언

    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    // 전화번호
    private String mPhone = "";
    private String validity = "";
    private int permissionCheck = 0;

    private UserLoginTask mLoginTask;

    // 최소 GPS 정보 업데이트 거리 10미터
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    // 최소 GPS 정보 업데이트 시간 밀리세컨이므로 1분
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    // 현재 GPS 사용유무
    private boolean isGPSEnabled = false;
    // 네트워크 사용유무
    private boolean isNetworkEnabled = false;

    private Location mLocation;
    private LocationManager mLocationManager;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허가
                    setLocation();
                } else {
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
                    Toast.makeText(this, "위치정보 권한을 허용해야 스타알바를 이용하실 수 있습니다", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); // 인트로화면이므로 타이틀바를 없앤다
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        // intent handler
        h = new Handler();
        // GPS 접근 권한 체크
        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        /*// 저장된 휴대폰번호가 없을 경우
        if ("".equals(mPhone)) {

        } else {
            validity = getUserInfo(mPhone);
        }*/

        // 권한없음
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            Log.e("intro] 권한", "X");
            //android 6.0 에 따른 permission 재확인 및 권한 습득
            if (ContextCompat.checkSelfPermission(IntroActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 이 권한을 필요한 이유를 설명해야하는가?
                if (ActivityCompat.shouldShowRequestPermissionRationale(IntroActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
                    // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    // 제목셋팅
                    alertDialogBuilder.setTitle("위치정보 권한 요청");
                    // AlertDialog 셋팅
                    alertDialogBuilder
                            .setMessage("스타알바는 주변업체 표시를 위해 위치정보를 가져옵니다")
                            .setCancelable(false)
                            .setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            ActivityCompat.requestPermissions(IntroActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                                        }
                                    });
                    // 다이얼로그 생성
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // 다이얼로그 보여주기
                    alertDialog.show();
                } else {
                    // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다
                    ActivityCompat.requestPermissions(IntroActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                }
            }
        } else {
            setLocation();
            Log.e("intro] 권한", "O");
        }

        /*} else {
            Log.e("intro]", "7");
            Log.e("intro] trueValidity", validity);
            goNextStage(validity);
        }*/
    }

    // 위치 가져오기
    public void setLocation() {
        CheckGpsStatus();

        // 네트워크 정보로 위치값 가져오기
        if (isNetworkEnabled) {
            Log.e("intro", "network enabled");
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            if (mLocationManager != null) {
                mLocation = mLocationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (mLocation != null) {
                    // 위도 경도 저장
                    DataMyLocation.setLatitude(mLocation.getLatitude());
                    DataMyLocation.setLongitude(mLocation.getLongitude());

                    h.postDelayed(mMainRun, 500);
                    Log.e("intro] Network", "위도:" +String.valueOf(DataMyLocation.getLatitude()) +" / 경도 : " +String.valueOf(DataMyLocation.getLongitude()));
                }
            }
        } else if (isGPSEnabled) {
            Log.e("intro", "gps enabled");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (mLocation == null) {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (mLocationManager != null) {
                    mLocation = mLocationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (mLocation != null) {
                        // 위도 경도 저장
                        DataMyLocation.setLatitude(mLocation.getLatitude());
                        DataMyLocation.setLongitude(mLocation.getLongitude());

                        h.postDelayed(mMainRun, 500);
                        Log.e("intro] GPS", "위도:" +String.valueOf(DataMyLocation.getLatitude()) +" / 경도 : " +String.valueOf(DataMyLocation.getLongitude()));
                    }
                }
            }
        } else {
            // GPS 셋팅 창 이동
            showSettingsAlert();
            Log.e("intro", "gps disabled");
        }
    }

    public void CheckGpsStatus(){
        Log.e("intro", "check GPS status");
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // GPS 정보 가져오기
        isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 현재 네트워크 상태 값 알아오기
        isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // gps 셋팅 창 이동
    public void showSettingsAlert() {
        Log.e("intro", "gps setting");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("GPS 사용유무 설정");
        alertDialog.setMessage("주변 업체를 가져오기 위해 GPS를 켜야합니다. \n 설정창으로 가시겠습니까?");

        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
        // Cancle 하면 종료 합니다.
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog.show();
    }

    // 메인으로
    Runnable mMainRun = new Runnable() {
        @Override
        public void run() {
            Log.e("intro", "main Run");
            Intent i = new Intent(IntroActivity.this, MainActivity.class); //인텐트 생성(현 액티비티, 새로 실행할 액티비티)
            startActivity(i);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            //overridePendingTransition 이란 함수를 이용하여 fade in,out 효과를줌. 순서가 중요
        }
    };

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public class UserLoginTask extends AsyncTask<String, Void, String> {
        private String mSendMsg;
        private String mReceiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://miningalarm.cafe24.com/miningAlarm/selectUserCheck.do");
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
            mLoginTask = null;
        }
    }

    /*// 회원가입으로
    Runnable mJoinRun = new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(IntroActivity.this, JoinActivity.class); //인텐트 생성(현 액티비티, 새로 실행할 액티비티)
            startActivity(i);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            //overridePendingTransition 이란 함수를 이용하여 fade in,out 효과를줌. 순서가 중요
        }
    };*/

    /*private void goNextStage(String validity) {
        // 로그인 성공
        if (validity.equals("1")) {
            if (null != mLoginTask) {
                mLoginTask.onCancelled();
            }
            // 유효성 TRUE 저장, id 저장
            shared.setStringPref(IntroActivity.this, new ValueData().getIdTag(), mPhone);
            shared.setStringPref(IntroActivity.this, new ValueData().getValidityTag(), validity);

            Log.e("intro] login", "success!");
            h.postDelayed(mMainRun, 1500);
        }
        // 로그인 실패
        else if (validity.equals("0")) {
            if (null != mLoginTask) {
                mLoginTask.onCancelled();
            }
            Log.e("intro] login", "fail!");
            h.postDelayed(mJoinRun, 1500);
        }
        // 결제 필요 => 결제 페이지로 loadUrl
        else if (validity.equals("2")) {
            if (null != mLoginTask) {
                mLoginTask.onCancelled();
            }
            Log.e("intro] login", "need pay!");
            h.postDelayed(mPayRun, 1500);
        } else {
            if (null != mLoginTask) {
                mLoginTask.onCancelled();
            }
            Toast.makeText(this, "관리자에게 문의해 주세요", Toast.LENGTH_SHORT).show();
        }
    }*/
}