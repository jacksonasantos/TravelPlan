package com.jacksonasantos.travelplan.ui.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Utils extends AppCompatActivity {

    private static final Globals g = Globals.getInstance();
    public static int selected_position = RecyclerView.NO_POSITION;

    public static int getFlagResource(String country, String state)
    {
        if (!Objects.equals(state.toUpperCase(), "")) {
            if (country.equalsIgnoreCase("BRASIL")) {
                switch (state.toUpperCase()) {
                    case "AC":
                        return R.drawable.ic_flag_br_ac;
                    case "AL":
                        return R.drawable.ic_flag_br_al;
                    case "AM":
                        return R.drawable.ic_flag_br_am;
                    case "AP":
                        return R.drawable.ic_flag_br_ap;
                    case "BA":
                        return R.drawable.ic_flag_br_ba;
                    case "CE":
                        return R.drawable.ic_flag_br_ce;
                    case "DF":
                        return R.drawable.ic_flag_br_df;
                    case "ES":
                        return R.drawable.ic_flag_br_es;
                    case "GO":
                        return R.drawable.ic_flag_br_go;
                    case "MA":
                        return R.drawable.ic_flag_br_ma;
                    case "MG":
                        return R.drawable.ic_flag_br_mg;
                    case "MS":
                        return R.drawable.ic_flag_br_ms;
                    case "MT":
                        return R.drawable.ic_flag_br_mt;
                    case "PA":
                        return R.drawable.ic_flag_br_pa;
                    case "PB":
                        return R.drawable.ic_flag_br_pb;
                    case "PE":
                        return R.drawable.ic_flag_br_pe;
                    case "PI":
                        return R.drawable.ic_flag_br_pi;
                    case "PR":
                        return R.drawable.ic_flag_br_pr;
                    case "RD":
                        return R.drawable.ic_flag_br_rd;
                    case "RJ":
                        return R.drawable.ic_flag_br_rj;
                    case "RN":
                        return R.drawable.ic_flag_br_rn;
                    case "RR":
                        return R.drawable.ic_flag_br_rr;
                    case "RS":
                        return R.drawable.ic_flag_br_rs;
                    case "SC":
                        return R.drawable.ic_flag_br_sc;
                    case "SE":
                        return R.drawable.ic_flag_br_se;
                    case "SP":
                        return R.drawable.ic_flag_br_sp;
                    case "TO":
                        return R.drawable.ic_flag_br_to;
                    default:
                        return 0;
                }
            }
        } else {
            switch (country.toUpperCase()) {
                case "ARGENTINA":
                    return R.drawable.ic_flag_argentina;
                case "BRASIL":
                    return R.drawable.ic_flag_brasil;
                case "CHILE":
                    return R.drawable.ic_flag_chile;
                case "ESTADOS UNIDOS":
                    return R.drawable.ic_flag_estados_unidos;
                case "URUGUAI":
                    return R.drawable.ic_flag_uruguai;
            }
        }
        return 0;
    }

    public static ArrayList<File> imageReader(File root)
    {
        ArrayList<File> a = new  ArrayList<>();
        File[] files = root.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    a.addAll(imageReader(file));
                } else {
                    a.add(file);
                }
            }
        }
        return a;
    }

    public static double convertStrCurrencyToDouble(String arg){
        double numberDouble = 0;
        try {
            // get a NumberFormat for the default Locale
            NumberFormat nf = NumberFormat.getNumberInstance(new Locale(g.getLanguage(), g.getCountry()));
            // convert a number with comma ex: 2,56 for double
            arg = arg.replace(".",",");
            numberDouble = Objects.requireNonNull(nf.parse(arg)).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numberDouble;
    }
    
    public static int[] getRGB(final String rgb)
    {
        final int[] ret = new int[3];
        for (int i = 0; i < 3; i++)
        {
            ret[i] = Integer.parseInt(rgb.substring(i * 2, i * 2 + 2), 16);
        }
        return ret;
    }

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

    public static Date stringToDate(String d)  {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return (d==null?null:dateFormat.parse(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date stringToDateTime(String dt) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            return (dt==null?null:dateFormat.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date stringToDateTime2(String dt, boolean truncate) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatTruncate = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date ret;
            if(dt==null) {
                ret = null;
            } else {
                if (truncate) {
                    ret = dateFormatTruncate.parse(dt);
                } else {
                    ret = dateFormat.parse(dt);
                }
            }
            return ret;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String returnDayWeek(Date d, Context context) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int pos = cal.get(Calendar.DAY_OF_WEEK)-1;
        return context.getResources().getStringArray(R.array.day_week_array)[pos];
    }

    public static String dateToString(Date d) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return (d==null?"":dateFormat.format(d));
    }

    public static String dateTimeToString(Date d) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return (d==null?null:dateFormat.format(d));
    }

    public static String dateToStringDate(Date d) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return (d==null?null:dateFormat.format(d));
    }

    public static int diffBetweenDate( Date firstDate, Date secondDate ) {
            long diffInMillis = Math.abs(secondDate.getTime() - firstDate.getTime());
        return (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        }

    public static void createSpinnerResources(int resource_array, Spinner spin, Context context) {
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
        nRadio.setText(txt); // define the text
        nRadio.setTextSize(11);
        nRadio.setId(i);     // define o code - sequence of FOR
        return nRadio;
    }
}
