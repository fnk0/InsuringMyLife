package com.gabilheri.insuringmylife.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by marcus on 3/7/14.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private int id;

    public TimePickerFragment(int id) {
        this.id = id;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {


        String timeSet = "AM";
        String time = "";

        if(hour > 12) {
            hour -= 12;
            timeSet = "PM";
        }

        String sHour = "";
        String sMinute ="";

        if(hour < 10) {
            sHour = "0" + hour;
        } else {
            sHour = "" + hour;
        }

        if(minute < 10) {
            sMinute = "0" + minute;
        } else {
            sMinute = "" + minute;
        }

        time += sHour + ":" + sMinute + " " + timeSet;


        Button button = (Button) getActivity().findViewById(id);
        button.setText(time);

    }
}
