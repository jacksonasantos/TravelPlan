package com.jacksonasantos.travelplan.ui.utility;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JSONParser {

    static InputStream is = null;
    static String json = "";

    public JSONParser() {
    }

    public static String getJSONFromUrl(String url) {
        try {
            URL urlNew = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlNew.openConnection();
            is = new BufferedInputStream(urlConnection.getInputStream());
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
            Log.e("Buffer Error", "Error converting result " + e);
        }
        Log.d("JSON_RUTA", json);
        return json;
    }
}
