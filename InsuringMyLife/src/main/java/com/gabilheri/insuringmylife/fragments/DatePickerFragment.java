package com.gabilheri.insuringmylife.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by marcus on 1/19/14.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    int id;

    public DatePickerFragment(int id) {
        this.id = id;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker.

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        String monthS;
        String dayS;

        if(month < 10) {
            monthS = "0" + (month + 1);
        } else {
            monthS = "" + (month + 1);
        }

        if(day < 10) {
            dayS = "0" + day;
        } else {
            dayS = "" + day;
        }

        String myDate = monthS + "/" + dayS + "/" + year;

        Button pickDateButton = (Button) getActivity().findViewById(id);
        pickDateButton.setText(myDate);
    }
}
