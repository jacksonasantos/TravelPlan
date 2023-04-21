package com.jacksonasantos.travelplan.ui.utility;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Calendar;

public class DateTimeInputMask implements TextWatcher {

    private String current = "";
    private final String mask = "DDMMYYYYHHMISS";
    private final Calendar cal = Calendar.getInstance();
    private final EditText input;

    public DateTimeInputMask(EditText input) {
        this.input = input;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().equals(current)) {
            return;
        }

        String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
        String cleanC = current.replaceAll("[^\\d.]|\\.", "");

        int cl = clean.length();
        int sel = cl;
        for (int i = 2; i <= cl && i < 12; i += 2) {
            sel++;
        }
        if (clean.equals(cleanC)) sel--;

        if (clean.length() < 14){
            clean = clean + mask.substring(clean.length());
        }else{
            int day  = Integer.parseInt(clean.substring(0,2));
            int mon  = Integer.parseInt(clean.substring(2,4));
            int year = Integer.parseInt(clean.substring(4,8));
            int hour = Integer.parseInt(clean.substring(8,10));
            int min  = Integer.parseInt(clean.substring(10,12));
            int sec  = Integer.parseInt(clean.substring(12,14));


            mon = (mon < 1) ? 1 : Math.min(mon, 12);
            cal.set(Calendar.MONTH, mon-1);
            year = (year < 1900) ? 1900 : Math.min(year, 2100);
            cal.set(Calendar.YEAR, year);
            day = Math.min(day, cal.getActualMaximum(Calendar.DATE));
            hour = (hour<0) ? 0 : Math.min(hour, 23);
            cal.set(Calendar.HOUR, hour);
            min = (min<0) ? 0 : Math.min(min, 59);
            cal.set(Calendar.MINUTE, min);
            sec = (sec<0) ? 0 : Math.min(sec, 59);
            cal.set(Calendar.SECOND, sec);

            clean = String.format("%02d%02d%04d%02d%02d%02d", day, mon, year, hour, min, sec);
        }

        clean = String.format("%s/%s/%s %s:%s:%s",
                clean.substring(0, 2),
                clean.substring(2, 4),
                clean.substring(4, 8),
                clean.substring(8, 10),
                clean.substring(10, 12),
                clean.substring(12, 14));
        sel = Math.max(sel, 0);
        current = clean;
        input.setText(current);
        input.setSelection(Math.min(sel, current.length()));
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}