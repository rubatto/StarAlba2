package com.rubatto.staralba;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private LinearLayout mStartLayout;
    private LinearLayout mEndLayout;
    private TextView mStartTv;
    private TextView mEndTv;
    private Button mSettingBt;

    private RadioButton mSoundRadio;
    private RadioButton mVibeRadio;
    private RadioButton mMuteRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mToolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        mStartLayout = (LinearLayout) findViewById(R.id.setting_start_time_layout);
        mEndLayout = (LinearLayout) findViewById(R.id.setting_end_time_layout);
        mSettingBt = (Button) findViewById(R.id.setting_bt);
        mStartTv = (TextView) findViewById(R.id.setting_start_time_tv);
        mEndTv = (TextView) findViewById(R.id.setting_end_time_tv);

        mSoundRadio = (RadioButton) findViewById(R.id.setting_radio_sound);
        mVibeRadio = (RadioButton) findViewById(R.id.setting_radio_vibe);
        mMuteRadio = (RadioButton) findViewById(R.id.setting_radio_mute);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String alarmMode = DataSharedPreference.getPreferences(this, DataSharedPreference.mAlarmModeTag);
        if (alarmMode.equals("소리")) {
            mSoundRadio.setChecked(true);
        } else if (alarmMode.equals("진동")) {
            mVibeRadio.setChecked(true);
        } else if (alarmMode.equals("무음")) {
            mMuteRadio.setChecked(true);
        }

        String startHour = DataSharedPreference.getPreferences(this, DataSharedPreference.mStartHourTag);
        String startMinute = DataSharedPreference.getPreferences(this, DataSharedPreference.mStartMinuteTag);
        String endHour = DataSharedPreference.getPreferences(this, DataSharedPreference.mEndHourTag);
        String endMinute = DataSharedPreference.getPreferences(this, DataSharedPreference.mEndMinuteTag);
        if (!(startHour.equals("") || startMinute.equals("") || endHour.equals("") || endMinute.equals(""))) {
            mStartTv.setText(startHour +" : " +startMinute);
            mEndTv.setText(endHour +" : " +endMinute);

            mStartTv.setTextColor(getResources().getColor(R.color.black));
            mEndTv.setTextColor(getResources().getColor(R.color.black));
        }

        mStartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getSupportFragmentManager(), "START");
            }
        });
        mEndLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getSupportFragmentManager(), "END");
            }
        });

        mSettingBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSoundRadio.isChecked()) {
                    DataSetting.setAlarmMode(mSoundRadio.getText().toString());

                    DataSharedPreference.savePreferences(getApplicationContext(), DataSharedPreference.mAlarmModeTag, mSoundRadio.getText().toString());
                    DataSharedPreference.savePreferences(getApplicationContext(), DataSharedPreference.mStartHourTag, DataSetting.getStartHour());
                    DataSharedPreference.savePreferences(getApplicationContext(), DataSharedPreference.mStartMinuteTag, DataSetting.getStartMinute());
                    DataSharedPreference.savePreferences(getApplicationContext(), DataSharedPreference.mEndHourTag, DataSetting.getEndHour());
                    DataSharedPreference.savePreferences(getApplicationContext(), DataSharedPreference.mEndMinuteTag, DataSetting.getEndMinute());

                    Toast.makeText(SettingActivity.this, "설정되었습니다", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (mVibeRadio.isChecked()) {
                    DataSetting.setAlarmMode(mVibeRadio.getText().toString());

                    DataSharedPreference.savePreferences(getApplicationContext(), DataSharedPreference.mAlarmModeTag, mVibeRadio.getText().toString());
                    DataSharedPreference.savePreferences(getApplicationContext(), DataSharedPreference.mStartHourTag, DataSetting.getStartHour());
                    DataSharedPreference.savePreferences(getApplicationContext(), DataSharedPreference.mStartMinuteTag, DataSetting.getStartMinute());
                    DataSharedPreference.savePreferences(getApplicationContext(), DataSharedPreference.mEndHourTag, DataSetting.getEndHour());
                    DataSharedPreference.savePreferences(getApplicationContext(), DataSharedPreference.mEndMinuteTag, DataSetting.getEndMinute());

                    Toast.makeText(SettingActivity.this, "설정되었습니다", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (mMuteRadio.isChecked()) {
                    DataSetting.setAlarmMode(mMuteRadio.getText().toString());

                    DataSharedPreference.savePreferences(getApplicationContext(), DataSharedPreference.mAlarmModeTag, mMuteRadio.getText().toString());
                    DataSharedPreference.savePreferences(getApplicationContext(), DataSharedPreference.mStartHourTag, DataSetting.getStartHour());
                    DataSharedPreference.savePreferences(getApplicationContext(), DataSharedPreference.mStartMinuteTag, DataSetting.getStartMinute());
                    DataSharedPreference.savePreferences(getApplicationContext(), DataSharedPreference.mEndHourTag, DataSetting.getEndHour());
                    DataSharedPreference.savePreferences(getApplicationContext(), DataSharedPreference.mEndMinuteTag, DataSetting.getEndMinute());

                    Toast.makeText(SettingActivity.this, "설정되었습니다", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SettingActivity.this, "알림을 선택해 주세요", Toast.LENGTH_SHORT).show();
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
}
