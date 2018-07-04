package com.ctrlaltelite.copshop.presentation.classes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import com.ctrlaltelite.copshop.R;
import com.ctrlaltelite.copshop.presentation.activities.EditListingActivity;

import java.util.Calendar;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    int id;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance(Locale.CANADA);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        int actualMonth = month + 1; // Because month index start from zero
        Bundle passedParams = this.getArguments();
        if (passedParams != null && !passedParams.isEmpty()) {
            id = passedParams.getInt("id");
            if (id == 1) {
                TextView startDate = getActivity().findViewById(R.id.txtStartDate);
                startDate.setText(day + "/" + actualMonth + "/" + year);
                getActivity().findViewById(R.id.btnStartTime).callOnClick();
                EditListingActivity.touchStartDate();
            } else if (id == 2) {
                TextView endDate = getActivity().findViewById(R.id.txtEndDate);
                endDate.setText(day + "/" + actualMonth + "/" + year);
                getActivity().findViewById(R.id.btnEndTime).callOnClick();
                EditListingActivity.touchEndDate();
            }
        }
    }


}
