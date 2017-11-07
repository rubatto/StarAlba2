package com.rubatto.staralba;

import android.app.AlertDialog;
import android.content.Context;
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

public class RegisterAdActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private ArrayList<Integer> mOriThemeDrawableList;
    private ArrayList<String> mOriThemeStringList;
    private ArrayList<String> mThemeCheckList;

    private int mAddCount;
    private String mGuaranteeResult;
    private String mAddressResult;
    private Double mLon;
    private Double mLng;

    private ImageView mBackImg;
    private EditText mBusinessNameEd;
    private ImageView mProfileImg;
    private EditText mTitleEd;
    private EditText mNameEd;
    private EditText mPhoneEd;
    private TextView mCategoryTv;
    private LinearLayout mPayLayout;
    private TextView mPaySeTv;
    private TextView mPayTv;
    private LinearLayout mAddressLayout;
    private TextView mAddressTv;
    private LinearLayout mThemeLayout;
    private TextView mThemeTv;
    private LinearLayout mGuaranteeLayout;
    private TextView mGuaranteeTv;
    private EditText mIntroduceTv;

    private Button mRegisterBt;

    Geocoder mGeocoder;

    private PostDataTask mDataTask;

    private String mPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ad);

        mToolbar = (Toolbar) findViewById(R.id.register_toolbar);

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

        //mBackImg = (ImageView) findViewById(R.id.register_backbutton);
        mBusinessNameEd = (EditText) findViewById(R.id.register_business_name_ed);
        mProfileImg = (ImageView) findViewById(R.id.register_profile_img);
        mTitleEd = (EditText) findViewById(R.id.register_title_ed);
        mNameEd = (EditText) findViewById(R.id.register_name_edit);
        mPhoneEd = (EditText) findViewById(R.id.register_phone_edit);
        mCategoryTv = (TextView) findViewById(R.id.register_category_tv);
        mPayLayout = (LinearLayout) findViewById(R.id.register_pay_layout);
        mPaySeTv = (TextView) findViewById(R.id.register_pay_se_tv);
        mPayTv = (TextView) findViewById(R.id.register_pay_tv);
        mAddressLayout = (LinearLayout) findViewById(R.id.register_address_layout);
        mAddressTv = (TextView) findViewById(R.id.register_address_tv);
        mThemeLayout = (LinearLayout) findViewById(R.id.register_theme_layout);
        mThemeTv = (TextView) findViewById(R.id.register_theme_tv);
        mGuaranteeLayout = (LinearLayout) findViewById(R.id.register_guarantee_layout);
        mGuaranteeTv = (TextView) findViewById(R.id.register_gurantee_tv);
        mIntroduceTv = (EditText) findViewById(R.id.register_introduce_ed);

        mRegisterBt = (Button) findViewById(R.id.register_bt);

        /*mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

        mProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterAdActivity.this, "프로필 이미지 선택", Toast.LENGTH_SHORT).show();
            }
        });

        mCategoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.register_category_dialog, null);
                AlertDialog.Builder buider = new AlertDialog.Builder(RegisterAdActivity.this); //AlertDialog.Builder 객체 생성
                buider.setTitle("업종 선택"); //Dialog 제목
                buider.setIcon(R.drawable.register_category); //제목옆의 아이콘 이미지(원하는 이미지 설정)
                buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)

                final AlertDialog dialog = buider.create();
                dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                dialog.show();

                final RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.category_dialog_radio_group);
                Button confirmBt = (Button) dialogView.findViewById(R.id.category_dialog_confirm);
                confirmBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id = radioGroup.getCheckedRadioButtonId();

                        if (id == -1) {
                            Toast.makeText(RegisterAdActivity.this, "업종을 선택해 주세요", Toast.LENGTH_SHORT).show();
                        } else {
                            RadioButton radioButton = (RadioButton) dialogView.findViewById(id);
                            mCategoryTv.setText(radioButton.getText().toString());
                            mCategoryTv.setTextColor(getColor(R.color.black));
                            //mCategoryTv.setTypeface(Typeface.DEFAULT_BOLD);
                            dialog.dismiss();
                        }
                    }
                });
                Button cancelBt = (Button) dialogView.findViewById(R.id.category_dialog_cancel);
                cancelBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        mPayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.register_pay_se_dialog, null);
                AlertDialog.Builder buider = new AlertDialog.Builder(RegisterAdActivity.this); //AlertDialog.Builder 객체 생성
                buider.setTitle("급여 선택"); //Dialog 제목
                buider.setIcon(R.drawable.register_pay_se); //제목옆의 아이콘 이미지(원하는 이미지 설정)
                buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)

                final AlertDialog dialog = buider.create();
                dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                dialog.show();

                final RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.pay_se_dialog_radio_group);
                final EditText payEd = (EditText) dialogView.findViewById(R.id.pay_se_dialog_pay_ed);
                payEd.setVisibility(View.INVISIBLE);
                Button confirmBt = (Button) dialogView.findViewById(R.id.pay_se_dialog_confirm);
                final RadioButton payRadioBt = (RadioButton) dialogView.findViewById(R.id.pay_se_dialog_radio1);
                final RadioButton talkRadioBt = (RadioButton) dialogView.findViewById(R.id.pay_se_dialog_radio2);
                payRadioBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        payEd.setVisibility(View.VISIBLE);
                    }
                });
                talkRadioBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        payEd.setVisibility(View.INVISIBLE);
                    }
                });
                confirmBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id = radioGroup.getCheckedRadioButtonId();

                        if (id == -1) {
                            Toast.makeText(RegisterAdActivity.this, "급여를 선택해 주세요", Toast.LENGTH_SHORT).show();
                        } else if (payRadioBt.isChecked()) {

                            if (payEd.getText().toString().isEmpty()) {
                                Toast.makeText(RegisterAdActivity.this, "시급을 입력해 주세요", Toast.LENGTH_SHORT).show();
                            } else {
                                Double payEdGetText = Double.parseDouble(payEd.getText().toString());
                                mPaySeTv.setText("시급");
                                mPaySeTv.setTextColor(getColor(R.color.black));
                                mPayTv.setText(toFormat(payEdGetText));

                                if (!(payEd.getText().toString().isEmpty() || payEd.getText().toString().equals(""))) {
                                    mPay = payEd.getText().toString();
                                }
                                mPayTv.setTextColor(getColor(R.color.deepBlue));
                                dialog.dismiss();
                            }
                        } else if (talkRadioBt.isChecked()) {
                            mPaySeTv.setText("협의");
                            mPaySeTv.setTextColor(getColor(R.color.black));
                            mPayTv.setText("");
                            dialog.dismiss();
                        }
                    }
                });
                Button cancelBt = (Button) dialogView.findViewById(R.id.pay_se_dialog_cancel);
                cancelBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        mAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.register_address_dialog, null);
                final AlertDialog.Builder buider = new AlertDialog.Builder(RegisterAdActivity.this); //AlertDialog.Builder 객체 생성
                buider.setTitle("주소 입력"); //Dialog 제목
                //buider.setIcon(); //제목옆의 아이콘 이미지(원하는 이미지 설정)
                buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
                final AlertDialog dialog = buider.create();
                dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                dialog.show();

                Button confirmBt = (Button) dialogView.findViewById(R.id.address_dialog_confirm);
                confirmBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText addressEd = (EditText) dialogView.findViewById(R.id.address_dialog_ed);

                        mGeocoder = new Geocoder(RegisterAdActivity.this);
                        List<Address> list = null;

                        String address = addressEd.getText().toString();

                        if (address.isEmpty()) {
                            Toast.makeText(RegisterAdActivity.this, "주소를 입력해 주세요", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                list = mGeocoder.getFromLocationName(
                                        address, // 지역 이름
                                        10); // 읽을 개수
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
                            }

                            if (list != null) {
                                if (list.size() == 0) {
                                    Toast.makeText(RegisterAdActivity.this, "주소를 확인해 주세요", Toast.LENGTH_SHORT).show();
                                } else {
                                    mAddressResult = list.get(0).getAddressLine(0);
                                    mLon = list.get(0).getLatitude();
                                    mLng = list.get(0).getLongitude();

                                    mAddressTv.setText(mAddressResult);
                                    mAddressTv.setTextColor(getColor(R.color.black));

                                    Toast.makeText(RegisterAdActivity.this, String.valueOf(mLon) + "/" + String.valueOf(mLng), Toast.LENGTH_SHORT).show();
                                    //          list.get(0).getCountryName();  // 국가명
                                    //          list.get(0).getLatitude();        // 위도
                                    //          list.get(0).getLongitude();    // 경도
                                    dialog.dismiss();
                                }
                            }
                        }
                    }
                });
                Button cancelBt = (Button) dialogView.findViewById(R.id.address_dialog_cancel);
                cancelBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        mThemeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddCount = 0;

                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.register_theme_dialog, null);
                final AlertDialog.Builder buider = new AlertDialog.Builder(RegisterAdActivity.this); //AlertDialog.Builder 객체 생성
                buider.setTitle("관심테마 선택"); //Dialog 제목
                //buider.setIcon(); //제목옆의 아이콘 이미지(원하는 이미지 설정)
                buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
                final AlertDialog dialog = buider.create();
                dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                dialog.show();

                final CheckBox checkBox1 = (CheckBox) dialogView.findViewById(R.id.theme_dialog_checkbox1);
                final CheckBox checkBox2 = (CheckBox) dialogView.findViewById(R.id.theme_dialog_checkbox2);
                final CheckBox checkBox3 = (CheckBox) dialogView.findViewById(R.id.theme_dialog_checkbox3);
                final CheckBox checkBox4 = (CheckBox) dialogView.findViewById(R.id.theme_dialog_checkbox4);
                final CheckBox checkBox5 = (CheckBox) dialogView.findViewById(R.id.theme_dialog_checkbox5);
                final CheckBox checkBox6 = (CheckBox) dialogView.findViewById(R.id.theme_dialog_checkbox6);
                final CheckBox checkBox7 = (CheckBox) dialogView.findViewById(R.id.theme_dialog_checkbox7);
                final CheckBox checkBox8 = (CheckBox) dialogView.findViewById(R.id.theme_dialog_checkbox8);
                final CheckBox checkBox9 = (CheckBox) dialogView.findViewById(R.id.theme_dialog_checkbox9);

                Button plusBt = (Button) dialogView.findViewById(R.id.theme_dialog_plus_bt);
                Button minusBt = (Button) dialogView.findViewById(R.id.theme_dialog_minus_bt);

                final LinearLayout plusLayout = (LinearLayout) dialogView.findViewById(R.id.theme_dialog_plus_layout);

                // 추가할 edittext 담을 리스트
                final ArrayList<EditText> editTextList = new ArrayList<EditText>();

                // TODO: 2017-09-19 plus minus 버튼 구현
                /*plusBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText editText = new EditText(RegisterAdActivity.this);
                        editText.setBackground(getResources().getDrawable(R.drawable.round_stroke_border));
                        editTextList.add(mAddCount, editText);
                        mAddCount++;
                        plusLayout.addView(editText);
                        *//*final RelativeLayout plusLayout1 = (RelativeLayout) dialogView.findViewById(R.id.theme_dialog_plus_layout1);
                        final RelativeLayout plusLayout2 = (RelativeLayout) dialogView.findViewById(R.id.theme_dialog_plus_layout2);
                        final RelativeLayout plusLayout3 = (RelativeLayout) dialogView.findViewById(R.id.theme_dialog_plus_layout3);
                        final RelativeLayout plusLayout4 = (RelativeLayout) dialogView.findViewById(R.id.theme_dialog_plus_layout4);
                        final RelativeLayout plusLayout5 = (RelativeLayout) dialogView.findViewById(R.id.theme_dialog_plus_layout5);

                        if (plusLayout1.getVisibility() == View.INVISIBLE) {
                            plusLayout1.setVisibility(View.VISIBLE);
                            ImageView minusImg1 = (ImageView) dialogView.findViewById(R.id.theme_dialog_plus_minus_img1);
                            minusImg1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (plusLayout5.getVisibility() == View.VISIBLE) {
                                        plusLayout5.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout4.getVisibility() == View.VISIBLE) {
                                        plusLayout4.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout3.getVisibility() == View.VISIBLE) {
                                        plusLayout3.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout2.getVisibility() == View.VISIBLE) {
                                        plusLayout2.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout1.getVisibility() == View.VISIBLE) {
                                        plusLayout1.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                        } else if (plusLayout2.getVisibility() == View.INVISIBLE) {
                            plusLayout2.setVisibility(View.VISIBLE);
                            ImageView minusImg2 = (ImageView) dialogView.findViewById(R.id.theme_dialog_plus_minus_img2);
                            minusImg2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (plusLayout5.getVisibility() == View.VISIBLE) {
                                        plusLayout5.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout4.getVisibility() == View.VISIBLE) {
                                        plusLayout4.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout3.getVisibility() == View.VISIBLE) {
                                        plusLayout3.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout2.getVisibility() == View.VISIBLE) {
                                        plusLayout2.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout1.getVisibility() == View.VISIBLE) {
                                        plusLayout1.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                        } else if (plusLayout3.getVisibility() == View.INVISIBLE) {
                            plusLayout3.setVisibility(View.VISIBLE);
                            ImageView minusImg3 = (ImageView) dialogView.findViewById(R.id.theme_dialog_plus_minus_img3);
                            minusImg3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (plusLayout5.getVisibility() == View.VISIBLE) {
                                        plusLayout5.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout4.getVisibility() == View.VISIBLE) {
                                        plusLayout4.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout3.getVisibility() == View.VISIBLE) {
                                        plusLayout3.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout2.getVisibility() == View.VISIBLE) {
                                        plusLayout2.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout1.getVisibility() == View.VISIBLE) {
                                        plusLayout1.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                        } else if (plusLayout4.getVisibility() == View.INVISIBLE) {
                            plusLayout4.setVisibility(View.VISIBLE);
                            ImageView minusImg4 = (ImageView) dialogView.findViewById(R.id.theme_dialog_plus_minus_img4);
                            minusImg4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (plusLayout5.getVisibility() == View.VISIBLE) {
                                        plusLayout5.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout4.getVisibility() == View.VISIBLE) {
                                        plusLayout4.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout3.getVisibility() == View.VISIBLE) {
                                        plusLayout3.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout2.getVisibility() == View.VISIBLE) {
                                        plusLayout2.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout1.getVisibility() == View.VISIBLE) {
                                        plusLayout1.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                        } else if (plusLayout5.getVisibility() == View.INVISIBLE) {
                            plusLayout5.setVisibility(View.VISIBLE);
                            ImageView minusImg5 = (ImageView) dialogView.findViewById(R.id.theme_dialog_plus_minus_img5);
                            minusImg5.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (plusLayout5.getVisibility() == View.VISIBLE) {
                                        plusLayout5.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout4.getVisibility() == View.VISIBLE) {
                                        plusLayout4.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout3.getVisibility() == View.VISIBLE) {
                                        plusLayout3.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout2.getVisibility() == View.VISIBLE) {
                                        plusLayout2.setVisibility(View.INVISIBLE);
                                    } else if (plusLayout1.getVisibility() == View.VISIBLE) {
                                        plusLayout1.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                        }*//*
                    }
                });
                minusBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mAddCount > 0) {
                            plusLayout.removeView(editTextList.get(mAddCount - 1));
                            mAddCount--;
                        }
                    }
                });*/

                Button confirmBt = (Button) dialogView.findViewById(R.id.theme_dialog_confirm);
                confirmBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mThemeCheckList = new ArrayList<String>();
                        int count = 0;
                        if (checkBox1.isChecked()) {
                            mThemeCheckList.add(checkBox1.getText().toString());
                            count++;
                            Log.e("ischeck1]", checkBox1.getText().toString());
                            Log.e("mThemeCheckList1]", mThemeCheckList.toString());
                        }
                        if (checkBox2.isChecked()) {
                            mThemeCheckList.add(checkBox2.getText().toString());
                            count++;
                            Log.e("ischeck2]", checkBox2.getText().toString());
                            Log.e("mThemeCheckList2]", mThemeCheckList.toString());
                        }
                        if (checkBox3.isChecked()) {
                            mThemeCheckList.add(checkBox3.getText().toString());
                            count++;
                            Log.e("ischeck3]", checkBox3.getText().toString());
                            Log.e("mThemeCheckList3]", mThemeCheckList.toString());
                        }
                        if (checkBox4.isChecked()) {
                            mThemeCheckList.add(checkBox4.getText().toString());
                            count++;
                            Log.e("ischeck4]", checkBox4.getText().toString());
                            Log.e("mThemeCheckList4]", mThemeCheckList.toString());
                        }
                        if (checkBox5.isChecked()) {
                            mThemeCheckList.add(checkBox5.getText().toString());
                            count++;
                            Log.e("ischeck5]", checkBox5.getText().toString());
                            Log.e("mThemeCheckList5]", mThemeCheckList.toString());
                        }
                        if (checkBox6.isChecked()) {
                            mThemeCheckList.add(checkBox6.getText().toString());
                            count++;
                            Log.e("ischeck6]", checkBox6.getText().toString());
                            Log.e("mThemeCheckList6]", mThemeCheckList.toString());
                        }
                        if (checkBox7.isChecked()) {
                            mThemeCheckList.add(checkBox7.getText().toString());
                            count++;
                            Log.e("ischeck7]", checkBox7.getText().toString());
                            Log.e("mThemeCheckList7]", mThemeCheckList.toString());
                        }
                        if (checkBox8.isChecked()) {
                            mThemeCheckList.add(checkBox8.getText().toString());
                            count++;
                            Log.e("ischeck8]", checkBox8.getText().toString());
                            Log.e("mThemeCheckList8]", mThemeCheckList.toString());
                        }
                        if (checkBox9.isChecked()) {
                            mThemeCheckList.add(checkBox9.getText().toString());
                            count++;
                            Log.e("ischeck9]", checkBox9.getText().toString());
                            Log.e("mThemeCheckList9]", mThemeCheckList.toString());
                        }

                        if (count == 0) {
                            Toast.makeText(RegisterAdActivity.this, "관심테마를 선택해 주세요", Toast.LENGTH_SHORT).show();
                        } else {
                            mThemeLayout.removeAllViews();
                            mThemeLayout.setOrientation(LinearLayout.VERTICAL);
                            Log.e("count", String.valueOf(count));
                            // 추가될 로우 개수 구하기
                            int row = 0;
                            int rest = 3;
                            if (count % 3 == 0) {
                                row = count / 3;
                                Log.e("row", String.valueOf(row));
                                Log.e("rest", String.valueOf(rest));
                            } else {
                                row = (count / 3) + 1;
                                rest = count % 3;
                                Log.e("row", String.valueOf(row));
                                Log.e("rest", String.valueOf(rest));
                            }
                            for (int i = 0; i < row; i++) {
                                LinearLayout layout = new LinearLayout(RegisterAdActivity.this);
                                layout.setOrientation(LinearLayout.HORIZONTAL);
                                layout.setGravity(Gravity.CENTER);
                                layout.setPadding(20, 20, 20, 20);

                                int size = 0;
                                if (i + 1 == row) {
                                    size = rest;
                                } else {
                                    size = 3;
                                }

                                for (int j = 3 * i; j < (3 * i) + size; j++) {
                                    TextView tv = new TextView(RegisterAdActivity.this);
                                    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    llp.setMargins(30, 30, 30, 30); // llp.setMargins(left, top, right, bottom);
                                    tv.setText(mThemeCheckList.get(j));
                                    tv.setLayoutParams(llp);

                                    // 원래 있는 themeList에서 선택한 checkString 있는 index 가져오기
                                    int index = mOriThemeStringList.indexOf(mThemeCheckList.get(j));
                                    tv.setCompoundDrawablesWithIntrinsicBounds(0, mOriThemeDrawableList.get(index), 0, 0);
                                    tv.setCompoundDrawablePadding(5);
                                    layout.addView(tv);
                                }
                                mThemeLayout.addView(layout);
                            }
                            dialog.dismiss();
                        }
                       /* int id = radioGroup.getCheckedRadioButtonId();

                        if (id == -1) {
                            Toast.makeText(RegisterAdActivity.this, "급여를 선택해 주세요", Toast.LENGTH_SHORT).show();
                        } else if (payRadioBt.isChecked()) {

                            if (payEd.getText().toString().isEmpty()) {
                                Toast.makeText(RegisterAdActivity.this, "시급을 입력해 주세요", Toast.LENGTH_SHORT).show();
                            } else {
                                Double payEdGetText = Double.parseDouble(payEd.getText().toString());
                                mPaySeTv.setText("시급 " + toFormat(payEdGetText));
                                mPaySeTv.setTextColor(getColor(R.color.black));
                                dialog.dismiss();
                            }
                        } else if (talkRadioBt.isChecked()) {
                            mPaySeTv.setText("협의");
                            mPaySeTv.setTextColor(getColor(R.color.black));
                            dialog.dismiss();
                        }*/
                    }
                });
                Button cancelBt = (Button) dialogView.findViewById(R.id.theme_dialog_cancel);
                cancelBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        mGuaranteeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.register_guarantee_dialog, null);
                final AlertDialog.Builder buider = new AlertDialog.Builder(RegisterAdActivity.this); //AlertDialog.Builder 객체 생성
                buider.setTitle("보장내용 선택"); //Dialog 제목
                //buider.setIcon(); //제목옆의 아이콘 이미지(원하는 이미지 설정)
                buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
                final AlertDialog dialog = buider.create();
                dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                dialog.show();

                final CheckBox checkBox1 = (CheckBox) dialogView.findViewById(R.id.guarantee_dialog_check1);
                final CheckBox checkBox2 = (CheckBox) dialogView.findViewById(R.id.guarantee_dialog_check2);
                final CheckBox checkBox3 = (CheckBox) dialogView.findViewById(R.id.guarantee_dialog_check3);
                final CheckBox checkBox4 = (CheckBox) dialogView.findViewById(R.id.guarantee_dialog_check4);
                final CheckBox checkBox5 = (CheckBox) dialogView.findViewById(R.id.guarantee_dialog_check5);
                final CheckBox checkBox6 = (CheckBox) dialogView.findViewById(R.id.guarantee_dialog_check6);
                final CheckBox checkBox7 = (CheckBox) dialogView.findViewById(R.id.guarantee_dialog_check7);
                final CheckBox checkBox8 = (CheckBox) dialogView.findViewById(R.id.guarantee_dialog_check8);
                final CheckBox checkBox9 = (CheckBox) dialogView.findViewById(R.id.guarantee_dialog_check9);
                final CheckBox checkBox10 = (CheckBox) dialogView.findViewById(R.id.guarantee_dialog_check10);
                final CheckBox checkBox11 = (CheckBox) dialogView.findViewById(R.id.guarantee_dialog_check11);
                final CheckBox checkBox12 = (CheckBox) dialogView.findViewById(R.id.guarantee_dialog_check12);
                final CheckBox checkBox13 = (CheckBox) dialogView.findViewById(R.id.guarantee_dialog_check13);
                final CheckBox checkBox14 = (CheckBox) dialogView.findViewById(R.id.guarantee_dialog_check14);
                final CheckBox checkBox15 = (CheckBox) dialogView.findViewById(R.id.guarantee_dialog_check15);
                final CheckBox checkBox16 = (CheckBox) dialogView.findViewById(R.id.guarantee_dialog_check16);

                mGuaranteeResult = "";

                Button confirmBt = (Button) dialogView.findViewById(R.id.guarantee_dialog_confirm);
                confirmBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mGuaranteeLayout.removeAllViews();

                        if (checkBox1.isChecked()) {
                            if (mGuaranteeResult.isEmpty()) {
                                mGuaranteeResult = checkBox1.getText().toString();
                            } else {
                                mGuaranteeResult = mGuaranteeResult + " · " + checkBox1.getText().toString();
                            }
                        }
                        if (checkBox2.isChecked()) {
                            if (mGuaranteeResult.isEmpty()) {
                                mGuaranteeResult = checkBox2.getText().toString();
                            } else {
                                mGuaranteeResult = mGuaranteeResult + " · " + checkBox2.getText().toString();
                            }
                        }
                        if (checkBox3.isChecked()) {
                            if (mGuaranteeResult.isEmpty()) {
                                mGuaranteeResult = checkBox3.getText().toString();
                            } else {
                                mGuaranteeResult = mGuaranteeResult + " · " + checkBox3.getText().toString();
                            }
                        }
                        if (checkBox4.isChecked()) {
                            if (mGuaranteeResult.isEmpty()) {
                                mGuaranteeResult = checkBox4.getText().toString();
                            } else {
                                mGuaranteeResult = mGuaranteeResult + " · " + checkBox4.getText().toString();
                            }
                        }
                        if (checkBox5.isChecked()) {
                            if (mGuaranteeResult.isEmpty()) {
                                mGuaranteeResult = checkBox5.getText().toString();
                            } else {
                                mGuaranteeResult = mGuaranteeResult + " · " + checkBox5.getText().toString();
                            }
                        }
                        if (checkBox6.isChecked()) {
                            if (mGuaranteeResult.isEmpty()) {
                                mGuaranteeResult = checkBox6.getText().toString();
                            } else {
                                mGuaranteeResult = mGuaranteeResult + " · " + checkBox6.getText().toString();
                            }
                        }
                        if (checkBox7.isChecked()) {
                            if (mGuaranteeResult.isEmpty()) {
                                mGuaranteeResult = checkBox7.getText().toString();
                            } else {
                                mGuaranteeResult = mGuaranteeResult + " · " + checkBox7.getText().toString();
                            }
                        }
                        if (checkBox8.isChecked()) {
                            if (mGuaranteeResult.isEmpty()) {
                                mGuaranteeResult = checkBox8.getText().toString();
                            } else {
                                mGuaranteeResult = mGuaranteeResult + " · " + checkBox8.getText().toString();
                            }
                        }
                        if (checkBox9.isChecked()) {
                            if (mGuaranteeResult.isEmpty()) {
                                mGuaranteeResult = checkBox9.getText().toString();
                            } else {
                                mGuaranteeResult = mGuaranteeResult + " · " + checkBox9.getText().toString();
                            }
                        }
                        if (checkBox10.isChecked()) {
                            if (mGuaranteeResult.isEmpty()) {
                                mGuaranteeResult = checkBox10.getText().toString();
                            } else {
                                mGuaranteeResult = mGuaranteeResult + " · " + checkBox10.getText().toString();
                            }
                        }
                        if (checkBox11.isChecked()) {
                            if (mGuaranteeResult.isEmpty()) {
                                mGuaranteeResult = checkBox11.getText().toString();
                            } else {
                                mGuaranteeResult = mGuaranteeResult + " · " + checkBox11.getText().toString();
                            }
                        }
                        if (checkBox12.isChecked()) {
                            if (mGuaranteeResult.isEmpty()) {
                                mGuaranteeResult = checkBox12.getText().toString();
                            } else {
                                mGuaranteeResult = mGuaranteeResult + " · " + checkBox12.getText().toString();
                            }
                        }
                        if (checkBox13.isChecked()) {
                            if (mGuaranteeResult.isEmpty()) {
                                mGuaranteeResult = checkBox13.getText().toString();
                            } else {
                                mGuaranteeResult = mGuaranteeResult + " · " + checkBox13.getText().toString();
                            }
                        }
                        if (checkBox14.isChecked()) {
                            if (mGuaranteeResult.isEmpty()) {
                                mGuaranteeResult = checkBox14.getText().toString();
                            } else {
                                mGuaranteeResult = mGuaranteeResult + " · " + checkBox14.getText().toString();
                            }
                        }
                        if (checkBox15.isChecked()) {
                            if (mGuaranteeResult.isEmpty()) {
                                mGuaranteeResult = checkBox15.getText().toString();
                            } else {
                                mGuaranteeResult = mGuaranteeResult + " · " + checkBox15.getText().toString();
                            }
                        }
                        if (checkBox16.isChecked()) {
                            if (mGuaranteeResult.isEmpty()) {
                                mGuaranteeResult = checkBox16.getText().toString();
                            } else {
                                mGuaranteeResult = mGuaranteeResult + " · " + checkBox16.getText().toString();
                            }
                        }

                        if (mGuaranteeResult.isEmpty()) {
                            Toast.makeText(RegisterAdActivity.this, "보장정보를 선택해 주세요", Toast.LENGTH_SHORT).show();
                        } else {
                            TextView tv = new TextView(RegisterAdActivity.this);
                            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            llp.setMargins(30, 30, 30, 30);
                            tv.setText(mGuaranteeResult);
                            tv.setGravity(Gravity.CENTER);
                            tv.setLayoutParams(llp);
                            tv.setTextColor(getColor(R.color.black));
                            mGuaranteeLayout.addView(tv);

                            dialog.dismiss();
                        }
                    }
                });
                Button cancelBt = (Button) dialogView.findViewById(R.id.guarantee_dialog_cancel);
                cancelBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        // 등록 버튼
        mRegisterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String businessName = mBusinessNameEd.getText().toString();
                String title = mTitleEd.getText().toString();
                String name = mNameEd.getText().toString();
                String phone = mPhoneEd.getText().toString();
                String categoty = mCategoryTv.getText().toString();
                String category_se_cd = "";
                if (categoty.equals("요정")) {
                    category_se_cd = "CATEGORY_1";
                } else if (categoty.equals("노래주점")) {
                    category_se_cd = "CATEGORY_2";
                } else if (categoty.equals("룸")) {
                    category_se_cd = "CATEGORY_3";
                } else if (categoty.equals("바(BAR)")) {
                    category_se_cd = "CATEGORY_4";
                } else if (categoty.equals("텐프로")) {
                    category_se_cd = "CATEGORY_5";
                } else if (categoty.equals("까페")) {
                    category_se_cd = "CATEGORY_6";
                } else if (categoty.equals("다방")) {
                    category_se_cd = "CATEGORY_7";
                } else if (categoty.equals("직업소개소")) {
                    category_se_cd = "CATEGORY_8";
                }
                String paySe = mPaySeTv.getText().toString();
                String pay_se_cd = "";
                String pay = mPayTv.getText().toString();
                String realPay = "0";
                if (paySe.equals("협의")) {
                    pay_se_cd = "0";
                } else {
                    pay_se_cd = "1";
                    if (!pay.equals("")) {
                        realPay = mPay;
                    }
                }
                String address = mAddressTv.getText().toString();
                String lon = String.valueOf(mLon);
                String lng = String.valueOf(mLng);
                String theme = "";
                if (!(null == mThemeCheckList)) {
                    for (int i = 0; i < mThemeCheckList.size(); i++) {
                        if (theme.isEmpty() || theme.equals("")) {
                            theme = mThemeCheckList.get(i);
                        } else {
                            theme = theme + "," + mThemeCheckList.get(i);
                        }
                    }
                }

                String guarantee = "";
                if (!(null == mGuaranteeResult || "".equals(mGuaranteeResult))) {
                    guarantee = mGuaranteeResult.replaceAll(" ", "");
                    Log.e("guarantee1", guarantee);
                    guarantee = guarantee.replaceAll("·", ",");
                    Log.e("guarantee2", guarantee);
                }
                String introduce = mIntroduceTv.getText().toString();

                if (businessName.isEmpty() || businessName.equals("")) {
                    Toast.makeText(RegisterAdActivity.this, "업체명을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (title.isEmpty() || title.equals("")) {
                    Toast.makeText(RegisterAdActivity.this, "제목을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (name.isEmpty() || name.equals("")) {
                    Toast.makeText(RegisterAdActivity.this, "실명/직급을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (phone.isEmpty() || phone.equals("")) {
                    Toast.makeText(RegisterAdActivity.this, "전화번호를 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (category_se_cd.isEmpty() || category_se_cd.equals("")) {
                    Toast.makeText(RegisterAdActivity.this, "업종을 선택해 주세요", Toast.LENGTH_SHORT).show();
                } else if (pay_se_cd.isEmpty() || pay_se_cd.equals("")) {
                    Toast.makeText(RegisterAdActivity.this, "급여를 선택해 주세요", Toast.LENGTH_SHORT).show();
                } else if (address.isEmpty() || address.equals("")) {
                    Toast.makeText(RegisterAdActivity.this, "주소를 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (theme.isEmpty() || theme.equals("")) {
                    Toast.makeText(RegisterAdActivity.this, "관심테마를 선택해 주세요", Toast.LENGTH_SHORT).show();
                } else if (guarantee.isEmpty() || guarantee.equals("")) {
                    Toast.makeText(RegisterAdActivity.this, "보장정보를 선택해 주세요", Toast.LENGTH_SHORT).show();
                } else if (introduce.isEmpty() || introduce.equals("")) {
                    Toast.makeText(RegisterAdActivity.this, "소개를 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    String myNumber = "01041844910";
                    postData(myNumber, businessName, title, name, phone, category_se_cd, pay_se_cd, realPay, address, theme, guarantee, introduce, lon, lng);
                }
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

    private void postData(String myNumber, String businessName, String title, String name, String phone, String category_se_cd, String pay_se_cd,
                          String realPay, String address, String theme, String guarantee, String introduce, String lon, String lng) {
        String result = "";
        String insertOk = "";
        mDataTask = new PostDataTask();
        try {
            result = mDataTask.execute(myNumber, businessName, title, name, phone, category_se_cd, pay_se_cd, realPay, address, theme, guarantee, introduce, lon, lng).get();

            if (!("".equals(result) || null == result)) {
                JSONObject jsonObj = new JSONObject(result);
                insertOk = jsonObj.getString("data");

                if (insertOk.equals("1")) {
                    Toast.makeText(this, "저장되었습니다!", Toast.LENGTH_SHORT).show();
                    finish();
                    // TODO: 2017-09-21 intent 작성
                } else {
                    Toast.makeText(this, "관리자에게 문의해 주세요", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "잘못된 형식입니다.\n다시 작성해 주세요", Toast.LENGTH_SHORT).show();
                finish();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String toFormat(Double d) {
        DecimalFormat df = new DecimalFormat("#,##0");

        return df.format(d);
    }

    private class PostDataTask extends AsyncTask<String, Void, String> {
        private String sendMsg;
        private String receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlString = "http://staralba3336.cafe24.com/star/mobile/insertBusinessInfo.do";
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                String phone = strings[0];
                String business_name = strings[1];
                String title = strings[2];
                String name = strings[3];
                String business_phone = strings[4];
                String category_se_cd = strings[5];
                String pay_se_cd = strings[6];
                String real_pay = strings[7];
                String address = strings[8];
                String theme = strings[9];
                String guarantee = strings[10];
                String introduce = strings[11];
                String lat = strings[12];
                String lng = strings[13];
                sendMsg = "PHONE=" + phone + "&BUSINESS_NAME=" + business_name + "&TITLE=" + title + "&BUSINESS_USER=" + name
                        + "&BUSINESS_PHONE=" + business_phone + "&CATEGORY_SE_CD=" + category_se_cd + "&PAY_SE_CD=" + pay_se_cd
                        + "&PAY=" + real_pay + "&ADDRESS=" + address + "&THEME=" + theme + "&GUARANTEE=" + guarantee
                        + "&CONTENT=" + introduce + "&LAT=" + lat + "&LNG=" + lng;
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
        }

        @Override
        protected void onCancelled() {
            mDataTask = null;
        }
    }
}
