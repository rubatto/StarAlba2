package com.rubatto.staralba;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(), this, hour, min, DateFormat.is24HourFormat(getContext())
        );
        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

        TextView startTv = (TextView) getActivity().findViewById(R.id.setting_start_time_tv);
        TextView endTv = (TextView) getActivity().findViewById(R.id.setting_end_time_tv);

        String minuteString = "";

        if (minute == 0) {
            minuteString = "00";
        } else if (minute / 10 == 0) {
            minuteString = "0" +String.valueOf(minute);
        } else {
            minuteString = String.valueOf(minute);
        }

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        if (null != fragmentManager.findFragmentByTag("START")) {
            startTv.setText(String.valueOf(hour) +" : " +minuteString);
            startTv.setTextColor(getResources().getColor(R.color.black));

            DataSetting.setStartHour(String.valueOf(hour));
            DataSetting.setStartMinute(minuteString);
        } else if (null != fragmentManager.findFragmentByTag("END")) {
            endTv.setText(String.valueOf(hour) +" : " +minuteString);
            endTv.setTextColor(getResources().getColor(R.color.black));

            DataSetting.setEndHour(String.valueOf(hour));
            DataSetting.setEndMinute(minuteString);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
}
