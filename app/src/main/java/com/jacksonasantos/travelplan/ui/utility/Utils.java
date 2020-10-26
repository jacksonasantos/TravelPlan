package com.jacksonasantos.travelplan.ui.utility;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Utils {
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
}
