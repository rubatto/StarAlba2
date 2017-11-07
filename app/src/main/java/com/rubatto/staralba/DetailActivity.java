package com.rubatto.staralba;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private ArrayList<Integer> mOriThemeDrawableList;
    private ArrayList<String> mOriThemeStringList;

    private int mAddCount;
    private String mGuaranteeResult;
    private String mAddressResult;

    private ImageView mBackImg;
    private TextView mBusinessNameTv;
    private ImageView mProfileImg;
    private TextView mProfileTv;
    private TextView mTitleTv;
    private TextView mNameTv;
    private TextView mPhoneTv;
    private TextView mCategoryTv;
    private LinearLayout mPayLayout;
    private TextView mPaySeTv;
    private TextView mPayTv;
    private RelativeLayout mAddressLayout;
    private TextView mAddressTv;
    private LinearLayout mThemeLayout;
    //private TextView mThemeTv;
    private LinearLayout mGuaranteeLayout;
    //private TextView mGuaranteeTv;
    private TextView mIntroduceTv;

    private LinearLayout mCallLayout;
    private LinearLayout mSMSLayout;
    private LinearLayout mChatLayout;
    private LinearLayout mStarLayout;

    //private Button mRegisterBt;

    Geocoder mGeocoder;

    private String mPay;

    private String mTheme;
    private String mGuarantee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String position = intent.getExtras().getString("POS");

        mToolbar = (Toolbar) findViewById(R.id.detail_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOriThemeDrawableList = new ArrayList<>();
        mOriThemeStringList = new ArrayList<>();

        mOriThemeDrawableList.add(R.drawable.theme_tjsqnfrksmd);
        mOriThemeDrawableList.add(R.drawable.theme_ekddlfwlrmq);
        mOriThemeDrawableList.add(R.drawable.theme_tjdgudwldnjs);
        mOriThemeDrawableList.add(R.drawable.theme_ckqlwldnjs);
        mOriThemeDrawableList.add(R.drawable.theme_tnrtlrwldnjs);
        mOriThemeDrawableList.add(R.drawable.theme_tlfwkdwlrdnjsakeka);
        mOriThemeDrawableList.add(R.drawable.theme_chqhrksmd);
        mOriThemeDrawableList.add(R.drawable.theme_tjqlddnpdlxj);
        mOriThemeDrawableList.add(R.drawable.theme_rudfurdneo);

        mOriThemeStringList.add("선불가능");
        mOriThemeStringList.add("당일지급");
        mOriThemeStringList.add("성형지원");
        mOriThemeStringList.add("차비지원");
        mOriThemeStringList.add("숙식지원");
        mOriThemeStringList.add("실장/직원/마담");
        mOriThemeStringList.add("초보가능");
        mOriThemeStringList.add("서빙/웨이터");
        mOriThemeStringList.add("경력우대");

        final int businessSeq = DataBusiness.getDataList().get(Integer.parseInt(position)).BUSINESS_SEQ;
        String businessName = DataBusiness.getDataList().get(Integer.parseInt(position)).BUSINESS_NAME;
        String title = DataBusiness.getDataList().get(Integer.parseInt(position)).TITLE;
        String name = DataBusiness.getDataList().get(Integer.parseInt(position)).BUSINESS_USER;
        String phone = DataBusiness.getDataList().get(Integer.parseInt(position)).BUSINESS_PHONE;
        String category = DataBusiness.getDataList().get(Integer.parseInt(position)).CATEGORY_SE_NM;
        String paySeCd = DataBusiness.getDataList().get(Integer.parseInt(position)).PAY_SE_CD;
        int pay = DataBusiness.getDataList().get(Integer.parseInt(position)).PAY;
        String realPay = "";
        if (paySeCd.equals("0")) {
            paySeCd = "협의";
            realPay = "0";
        } else {
            paySeCd = "시급";
            realPay = String.valueOf(pay);
        }
        String address = DataBusiness.getDataList().get(Integer.parseInt(position)).ADDRESS;
        ArrayList<String> theme = new ArrayList<>();
        theme = DataBusiness.getDataList().get(Integer.parseInt(position)).THEME_LIST;
        ArrayList<String> guarantee = new ArrayList<>();
        guarantee = DataBusiness.getDataList().get(Integer.parseInt(position)).GUARANTEE_LIST;
        String content = DataBusiness.getDataList().get(Integer.parseInt(position)).CONTENT;
        final double business_lat = DataBusiness.getDataList().get(Integer.parseInt(position)).BUSINESS_LAT;
        final double business_lng = DataBusiness.getDataList().get(Integer.parseInt(position)).BUSINESS_LNG;

        //mBackImg = (ImageView) findViewById(R.id.register_backbutton);
        mBusinessNameTv = (TextView) findViewById(R.id.detail_business_name_tv);
        mProfileImg = (ImageView) findViewById(R.id.detail_profile_img);
        mProfileTv = (TextView) findViewById(R.id.detail_profile_tv);
        mTitleTv = (TextView) findViewById(R.id.detail_title_tv);
        mNameTv = (TextView) findViewById(R.id.detail_name_tv);
        mPhoneTv = (TextView) findViewById(R.id.detail_phone_tv);
        mCategoryTv = (TextView) findViewById(R.id.detail_category_tv);
        mPayLayout = (LinearLayout) findViewById(R.id.detail_pay_layout);
        mPaySeTv = (TextView) findViewById(R.id.detail_pay_se_tv);
        mPayTv = (TextView) findViewById(R.id.detail_pay_tv);
        mAddressLayout = (RelativeLayout) findViewById(R.id.detail_address_layout);
        mAddressTv = (TextView) findViewById(R.id.detail_address_tv);
        mThemeLayout = (LinearLayout) findViewById(R.id.detail_theme_layout);
        //mThemeTv = (TextView) findViewById(R.id.detail_theme_tv);
        mGuaranteeLayout = (LinearLayout) findViewById(R.id.detail_guarantee_layout);
        //mGuaranteeTv = (TextView) findViewById(R.id.detail_gurantee_tv);
        mIntroduceTv = (TextView) findViewById(R.id.detail_introduce_tv);
        //mRegisterBt = (Button) findViewById(R.id.detail_bt);
        mCallLayout = (LinearLayout) findViewById(R.id.detail_call_layout);
        mSMSLayout = (LinearLayout) findViewById(R.id.detail_sms_layout);
        mChatLayout = (LinearLayout) findViewById(R.id.detail_chat_layout);
        mStarLayout = (LinearLayout) findViewById(R.id.detail_star_layout);

        // settext
        mBusinessNameTv.setText(businessName);
        mProfileTv.setText(businessName);
        mTitleTv.setText(title);
        mNameTv.setText(name);
        mPhoneTv.setText(phone);
        mCategoryTv.setText(category);
        mPaySeTv.setText(paySeCd);
        mPayTv.setText(realPay);
        mAddressTv.setText(address);

        // 테마 이미지로 넣기
        int theme_row = 0;
        int theme_rest = 3;
        Log.e("ThemeList", theme.toString());
        if (theme.size() % 3 == 0) {
            theme_row = theme.size() / 3;
            Log.e("theme_row", String.valueOf(theme_row));
            Log.e("theme_rest", String.valueOf(theme_rest));
        } else {
            theme_row = (theme.size() / 3) + 1;
            theme_rest = theme.size() % 3;
            Log.e("theme_row", String.valueOf(theme_row));
            Log.e("theme_rest", String.valueOf(theme_rest));
        }
        for (int i = 0; i < theme_row; i++) {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER);
            layout.setPadding(20, 20, 20, 20);

            int theme_size = 0;
            if (i + 1 == theme_row) {
                theme_size = theme_rest;
            } else {
                theme_size = 3;
            }

            for (int j = 3 * i; j < (3 * i) + theme_size; j++) {
                Log.e("int i", String.valueOf(i));
                Log.e("int j", String.valueOf(j));
                TextView tv = new TextView(this);
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llp.setMargins(30, 30, 30, 30); // llp.setMargins(left, top, right, bottom);
                tv.setText(theme.get(j));
                tv.setLayoutParams(llp);

                // 원래 있는 themeList에서 선택한 checkString 있는 index 가져오기
                int index = mOriThemeStringList.indexOf(theme.get(j));
                tv.setCompoundDrawablesWithIntrinsicBounds(0, mOriThemeDrawableList.get(index), 0, 0);
                tv.setCompoundDrawablePadding(5);
                layout.addView(tv);
            }
            mThemeLayout.addView(layout);
        }

        // 테마 이미지로 넣기
        int guarantee_row = 0;
        int guarantee_rest = 2;
        Log.e("guaranteeList", guarantee.toString());
        if (guarantee.size() % 2 == 0) {
            guarantee_row = guarantee.size() / 2;
            Log.e("guarantee_row", String.valueOf(guarantee_row));
            Log.e("guarantee_rest", String.valueOf(guarantee_rest));
        } else {
            guarantee_row = (guarantee.size() / 2) + 1;
            guarantee_rest = guarantee.size() % 2;
            Log.e("guarantee_row", String.valueOf(guarantee_row));
            Log.e("guarantee_rest", String.valueOf(guarantee_rest));
        }
        for (int i = 0; i < guarantee_row; i++) {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER);
            layout.setPadding(20, 20, 20, 20);

            int guarantee_size = 0;
            if (i + 1 == guarantee_row) {
                guarantee_size = guarantee_rest;
            } else {
                guarantee_size = 2;
            }

            for (int j = 2 * i; j < (2 * i) + guarantee_size; j++) {
                Log.e("int i", String.valueOf(i));
                Log.e("int j", String.valueOf(j));
                TextView tv = new TextView(this);
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llp.setMargins(20, 20, 20, 20); // llp.setMargins(left, top, right, bottom);
                tv.setText(guarantee.get(j));
                tv.setLayoutParams(llp);

                // 원래 있는 guaranteeList에서 선택한 checkString 있는 index 가져오기
                /*int index = mOriThemeStringList.indexOf(guarantee.get(j));
                tv.setCompoundDrawablesWithIntrinsicBounds(0, mOriThemeDrawableList.get(index), 0, 0);
                tv.setCompoundDrawablePadding(5);*/
                layout.addView(tv);
            }
            mGuaranteeLayout.addView(layout);
        }

        /*mTheme = theme.toString().replace("[", "");
        mTheme = mTheme.replace("]", "");
        //mThemeTv.setText(mTheme);

        mGuarantee = guarantee.toString().replace("[", "");
        mGuarantee = mGuarantee.replace("]", "");
        //mGuaranteeTv.setText(mGuarantee);*/

        mIntroduceTv.setText(content);

        /*mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

        mProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailActivity.this, "프로필 이미지 선택", Toast.LENGTH_SHORT).show();
            }
        });

        mAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailActivity.this, BusinessWebviewActivity.class);
                i.putExtra("LAT", String.valueOf(business_lat));
                i.putExtra("LNT", String.valueOf(business_lng));
                startActivity(i);
            }
        });

        mCallLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailActivity.this, "업체회원은 이용하실 수 없습니다", Toast.LENGTH_SHORT).show();
            }
        });
        mSMSLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailActivity.this, "업체회원은 이용하실 수 없습니다", Toast.LENGTH_SHORT).show();
            }
        });
        mChatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailActivity.this, "업체회원은 이용하실 수 없습니다", Toast.LENGTH_SHORT).show();
            }
        });
        mStarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailActivity.this, "업체회원은 이용하실 수 없습니다", Toast.LENGTH_SHORT).show();
            }
        });
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

    public String toFormat(Double d) {
        DecimalFormat df = new DecimalFormat("#,##0");

        return df.format(d);
    }
}
