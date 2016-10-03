package es.upv.sdm.labs.bikeroutes.util;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.SearchEvent;
import android.widget.Button;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.upv.sdm.labs.bikeroutes.activities.CreateEventActivity;
import es.upv.sdm.labs.bikeroutes.activities.SearchEventActivity;


public class SearchTimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    Button btnEventTime;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

       // btnEventTime = (Button) getView().findViewById(R.id.btnEventTime);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), (SearchEventActivity)getActivity(), hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(0,0,0,hourOfDay,minute);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        String formattedDate = sdf.format(c.getTime());

    }

}