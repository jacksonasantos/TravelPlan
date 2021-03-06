package com.jacksonasantos.travelplan.ui.utility;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JSONParser {

    static InputStream is = null;
    static String json = "";

    public JSONParser() {
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getJSONFromUrl(String url) {
        try {
            URL urlNew = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlNew.openConnection();
            is = new BufferedInputStream(urlConnection.getInputStream());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.ISO_8859_1), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            json = sb.toString();
            is.close();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        Log.d("JSON_RUTA", json);
        return json;
    }
}
