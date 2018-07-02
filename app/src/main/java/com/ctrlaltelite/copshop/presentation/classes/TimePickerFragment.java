package com.ctrlaltelite.copshop.presentation.classes;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ctrlaltelite.copshop.R;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    int id;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Bundle passedParams = this.getArguments();
        if (passedParams != null && !passedParams.isEmpty()) {
            id = passedParams.getInt("id");
            if (id == 1) {
                TextView startDate = getActivity().findViewById(R.id.txtStartDate);
                startDate.setText(startDate.getText() + " " + hourOfDay + ":" + minute);
            } else if (id == 2) {
                TextView endDate = getActivity().findViewById(R.id.txtEndDate);
                endDate.setText(endDate.getText() + " " + hourOfDay + ":" + minute);
            }
        }
    }
}