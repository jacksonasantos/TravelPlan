package com.jacksonasantos.travelplan.ui.utility;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Utils extends AppCompatActivity  {
    public static Date stringToDate(String d) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return (d==null?null:dateFormat.parse(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToString(Date d) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return (d==null?null:dateFormat.format(d));
    }

    public static long getTimeMillis(String dateString, String dateFormat) throws ParseException {
        /*Use date format as according to your need! Ex. - yyyy/MM/dd HH:mm:ss */
        String myDate = dateString;//"2017/12/20 18:10:45";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat/*"yyyy/MM/dd HH:mm:ss"*/);
        Date date = sdf.parse(myDate);
        long millis = date.getTime();
        return millis;
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat/*"yyyy/MM/dd HH:mm:ss"*/);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static void createSpinnerResources(int resource_array, AutoCompleteTextView spin, Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.select_dialog_item,
                context.getResources().getStringArray(resource_array));
        spin.setAdapter(adapter);
    }

    public static void addRadioButtonResources(int resource_array, RadioGroup rg, Context context) {
        int i = 0;
        String[] S = context.getResources().getStringArray(resource_array);
        for (String item : S) {
            RadioButton newRadio = createRadioButton(item, ++i, context);
            rg.addView(newRadio);
        }
    }

    public static RadioButton createRadioButton(String txt, int i, Context context) {
        RadioButton nRadio = new RadioButton(context );
        LinearLayout.LayoutParams params = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);
        nRadio.setLayoutParams(params);
        nRadio.setText(txt); // define o texto
        nRadio.setId(i);     // define o codigo - sequencia do for
        return nRadio;
    }
}
