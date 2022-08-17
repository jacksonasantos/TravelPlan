package com.jacksonasantos.travelplan.ui.travel;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.core.text.HtmlCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jacksonasantos.travelplan.MainActivity;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Itinerary;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RouteClass {
    GoogleMap mMap;
    Context context;
    String lang;
    Integer nrTravel_Id;

    private static final String GOOGLE_API_KEY =  MainActivity.getAppResources().getString(R.string.google_maps_key);
    private final ExecutorService myExecutor = Executors.newSingleThreadExecutor();
    private final Handler myHandler = new Handler(Looper.getMainLooper());

    public void drawRoute(GoogleMap map, Context c, ArrayList<LatLng> points, boolean withIndications, String language, boolean optimize, Integer travel_id, int sequence, Integer sequenceSelected) {
        mMap = map;
        context = c;
        lang = language;
        nrTravel_Id = travel_id;
        String mode = "driving";
        String url = null;

        if (points.size() == 2) {
            url = makeURL(points.get(0).latitude, points.get(0).longitude, points.get(1).latitude, points.get(1).longitude, mode);
        } else if (points.size() > 2) {
            url = makeURL(points, mode, optimize);
        }
        connectAsyncTask(url, withIndications, sequence, sequenceSelected);
    }

    private String makeURL(ArrayList<LatLng> points, String mode, boolean optimize) {
        StringBuilder urlString = new StringBuilder();

        if (mode == null) mode = "driving";

        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");
        urlString.append(points.get(0).latitude);
        urlString.append(',');
        urlString.append(points.get(0).longitude);
        urlString.append("&destination=");
        urlString.append(points.get(points.size() - 1).latitude);
        urlString.append(',');
        urlString.append(points.get(points.size() - 1).longitude);
        urlString.append("&waypoints=");
        if (optimize)
            urlString.append("optimize:true|");
        urlString.append(points.get(1).latitude);
        urlString.append(',');
        urlString.append(points.get(1).longitude);
        for (int i = 2; i < points.size() - 1; i++) {
            urlString.append('|');
            urlString.append(points.get(i).latitude);
            urlString.append(',');
            urlString.append(points.get(i).longitude);
        }
        urlString.append("&mode=").append(mode);
        urlString.append("&language=").append(lang);
        urlString.append("&key=").append(GOOGLE_API_KEY);
        return urlString.toString();
    }

    private String makeURL(double sourceLat, double sourceLog, double destLat, double destLog, String mode) {
        StringBuilder urlString = new StringBuilder();

        if (mode == null) mode = "driving";

        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");
        urlString.append(sourceLat);
        urlString.append(",");
        urlString.append(sourceLog);
        urlString.append("&destination=");
        urlString.append(destLat);
        urlString.append(",");
        urlString.append(destLog);
        urlString.append("&mode=").append(mode);
        urlString.append("&alternatives=true");
        urlString.append("&language=").append(lang);
        urlString.append("&key=").append(GOOGLE_API_KEY);
        return urlString.toString();
    }

    private void connectAsyncTask(String urlPass, boolean withSteps,int sequence, Integer sequenceSelected ) {

        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Fetching route, Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        myExecutor.execute(() -> {

            JSONParser jParser = new JSONParser();
            String r = jParser.getJSONFromUrl(urlPass);

            myHandler.post(() -> {
                progressDialog.dismiss();
                if (r != null) {
                    drawPath(r, withSteps, sequence, sequenceSelected);
                }
            });
        });
    }

    private void drawPath(String result, boolean withSteps, int nrSequence, Integer nrSequenceSelected) {
        try {
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONArray arrayLegs = routes.getJSONArray("legs");
            Itinerary itinerary = Database.mItineraryDao.fetchItineraryByTravelId(nrTravel_Id, nrSequence);
            int nrDuration = 0, nrDistance = 0;
            for (int y =0; y < arrayLegs.length(); y++) {
                JSONObject legs = arrayLegs.getJSONObject(y);
                JSONArray stepsArray = legs.getJSONArray("steps");

                nrDuration += legs.getJSONObject("duration").getInt("value");
                nrDistance += legs.getJSONObject("distance").getInt("value");

                for (int i = 0; i < stepsArray.length(); i++) {
                    String polyline = (String) ((JSONObject) ((JSONObject) stepsArray.get(i)).get("polyline")).get("points");
                    int vColor = nrSequence%2==0 ? Color.MAGENTA : Color.BLUE;
                    int vWidth = 4;
                    if (nrSequenceSelected != null) {
                        vColor = nrSequence==nrSequenceSelected ? vColor : Color.RED;
                        vWidth = nrSequence==nrSequenceSelected ? vWidth : 8;
                    }
                    mMap.addPolyline(new PolylineOptions()
                            .addAll(decodePoly(polyline))
                            .width(vWidth)
                            .clickable(true)
                            .color(vColor)
                            .geodesic(true)
                    );
                    if (withSteps) {
                        Step step = new Step(stepsArray.getJSONObject(i));
                        mMap.addMarker(new MarkerOptions()
                                .position(step.location)
                                .title(step.distance + "/" + step.duration)
                                .snippet(step.instructions)
                                .draggable(true)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }
                }
            }
            itinerary.setDistance(nrDistance);
            itinerary.setTime(nrDuration);
            Database.mItineraryDao.updateItinerary(itinerary);
        } catch (JSONException e) {
            Toast.makeText(context, "Draw Path \n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private static class Step {
        public String distance;
        public String duration;
        public LatLng location;
        public String instructions;

        Step(JSONObject stepJSON) {
            JSONObject startLocation;
            try {
                distance = stepJSON.getJSONObject("distance").getString("text");
                duration = stepJSON.getJSONObject("duration").getString("text");
                startLocation = stepJSON.getJSONObject("start_location");
                location = new LatLng(startLocation.getDouble("lat"), startLocation.getDouble("lng"));
                try {
                    instructions = URLDecoder.decode(HtmlCompat.fromHtml(stepJSON.getString("html_instructions"), HtmlCompat.FROM_HTML_MODE_LEGACY).toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dLat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dLat;

            shift = 0; result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dLng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dLng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
}