package com.jacksonasantos.travelplan.ui.utility;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
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

    public static void createSpinnerResources(int resource_array, AutoCompleteTextView spin, Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.select_dialog_item,
                context.getResources().getStringArray(resource_array));
        spin.setAdapter(adapter);
    }
}
