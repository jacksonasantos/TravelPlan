package com.jacksonasantos.travelplan.ui.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utils extends AppCompatActivity  {

    public static int getColorWithAlpha(int color, float ratio) {
        return Color.argb(
                Math.round(Color.alpha(color) * ratio),
                Color.red(color),
                Color.green(color),
                Color.blue(color));
    }

    public static void setSpinnerToValue(Spinner spinner, Integer value) {
        int index = 0;
        if (value != null && value > 0) {
            SpinnerAdapter adapter = spinner.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItemId(i) == value) {
                    index = i;
                    break;
                }
            }
        }
        spinner.setSelection(index);
    }

    public static Date dateParse(String data ){
        Globals g = Globals.getInstance();
        Date d = null;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(g.getDateFormat());
        try {
            d = (data == null ? null : dateFormat.parse(data));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Date dateParse(Integer data ){
        Globals g = Globals.getInstance();
        Date d = null;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(g.getDateFormat());
        try {
            d = (data == null ? null : dateFormat.parse(data.toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static String dateFormat(Date data ){
        Globals g = Globals.getInstance();
        String d;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(g.getDateFormat());
        d = (data == null ? null : dateFormat.format(data));
        return d;
    }

    public static Date stringToDate(String d) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return (d==null?null:dateFormat.parse(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToString(Date d) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return (d==null?null:dateFormat.format(d));
    }

    public static int diffBetweenDate( Date firstDate, Date secondDate ) {
            long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        return (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        }

    public static void createSpinnerResources(int resource_array, AutoCompleteTextView spin, Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
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
