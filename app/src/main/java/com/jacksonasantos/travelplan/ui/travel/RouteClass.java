package com.jacksonasantos.travelplan.ui.travel;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.core.text.HtmlCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jacksonasantos.travelplan.MainActivity;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Achievement;
import com.jacksonasantos.travelplan.dao.Itinerary;
import com.jacksonasantos.travelplan.dao.Marker;
import com.jacksonasantos.travelplan.dao.Tour;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RouteClass {
    GoogleMap mMap;
    Context context;
    String lang, mode;
    String typeUpdate;
    Integer nrTravel_Id, nrKey_Id;
    boolean isAlpha = false;
    int predicted_stop_time;

    private static final String GOOGLE_API_KEY =  MainActivity.getAppResources().getString(R.string.google_maps_key);
    private final ExecutorService myExecutor = Executors.newSingleThreadExecutor();
    private final Handler myHandler = new Handler(Looper.getMainLooper());

    public void drawRoute(GoogleMap map, Context context, ArrayList<LatLng> points, boolean withIndications, String language, boolean optimize, String typeUpdateTable, Integer id, int sequence, Integer sequenceSelected, boolean alpha, int travel_mode, int predicted_stop_time) {
        this.mMap = map;
        this.context = context;
        this.lang = language;
        this.typeUpdate = typeUpdateTable;
        this.nrTravel_Id = null;
        this.nrKey_Id = null;
        this.isAlpha = alpha;
        this.predicted_stop_time = predicted_stop_time;

        String url = null;

        if (mode == null) mode = "driving";
        String[] travelModes = context.getResources().getStringArray(R.array.travel_mode_array);
        mode = travelModes[travel_mode];

        if (Objects.equals(typeUpdate, "Itinerary")) {
            nrTravel_Id = id;
        } else if (Objects.equals(typeUpdate, "Achievement")) {
            nrKey_Id = id;
        } else if (Objects.equals(typeUpdate, "Tour")) {
            nrKey_Id = id;
        }
        if (points.size() == 2) {
            url = makeURL(points.get(0).latitude, points.get(0).longitude, points.get(1).latitude, points.get(1).longitude, mode);
        } else if (points.size() > 2) {
            url = makeURL(points, mode, optimize);
        }
        connectAsyncTask(url, withIndications, sequence, sequenceSelected, typeUpdate, nrKey_Id, predicted_stop_time);
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

    private void connectAsyncTask(String urlPass, boolean withSteps,int sequence, Integer sequenceSelected, String typeUpdate, Integer nrKey_Id, int predicted_stop_time ) {
        myExecutor.execute(() -> {
            JSONParser jParser = new JSONParser();
            String r = jParser.getJSONFromUrl(urlPass);
            myHandler.post(() -> {
                if (r != null && !r.equals("")) {
                    drawPath(r, withSteps, sequence, sequenceSelected, typeUpdate, nrKey_Id, predicted_stop_time);
                }
            });
        });
    }

    private void drawPath(String result, boolean withSteps, int nrSequence, Integer nrSequenceSelected, String typeUpdate, Integer nrKey_Id, int predicted_stop_time) {

        final List<PatternItem> PATTERN_DRIVING = Collections.singletonList(new Dash(1));
        final List<PatternItem> PATTERN_WALKING = Arrays.asList(new Dot(), new Gap(10), new Dash(20), new Gap(10));
        final List<PatternItem> PATTERN_BICYCLING = Collections.singletonList(new Dot());
        final List<PatternItem> PATTERN_TRANSIT = Collections.singletonList(new Dash(1));
        final List<PatternItem> PATTERN_FLYING = Arrays.asList(new Dot(), new Dot(), new Dot(), new Gap(15), new Dash(20), new Gap(15));
        try {
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONArray arrayLegs = routes.getJSONArray("legs");
            int nrDuration = 0, nrDistance = 0;
            for (int y = 0; y < arrayLegs.length(); y++) {
                JSONObject legs = arrayLegs.getJSONObject(y);
                JSONArray stepsArray = legs.getJSONArray("steps");

                nrDuration += legs.getJSONObject("duration").getInt("value");
                nrDistance += legs.getJSONObject("distance").getInt("value");

                for (int i = 0; i < stepsArray.length(); i++) {
                    String polyline = (String) ((JSONObject) ((JSONObject) stepsArray.get(i)).get("polyline")).get("points");

                    int vColor = Itinerary.getColorItinerary(nrSequence);
                    int vWidth = 8;
                    if (nrSequenceSelected != null && nrSequenceSelected > 0) {
                        vColor = nrSequence == nrSequenceSelected ? vColor : Color.BLACK;
                        vWidth = nrSequence == nrSequenceSelected ? vWidth : 14;
                    }

                    Itinerary itinerary = Database.mItineraryDao.fetchItineraryByTravelId(nrTravel_Id, nrSequence);

                    List<PatternItem> PATTERN_POLYLINE;
                    switch (itinerary.getTravel_mode()) {
                        case 1: PATTERN_POLYLINE = PATTERN_WALKING; break;
                        case 2: PATTERN_POLYLINE = PATTERN_BICYCLING; break;
                        case 3: PATTERN_POLYLINE = PATTERN_TRANSIT; break;
                        case 4: PATTERN_POLYLINE = PATTERN_FLYING; break;
                        default: PATTERN_POLYLINE = PATTERN_DRIVING;
                    }

                    if (itinerary.getTravel_mode() == 4) {
                        List<Marker> marker = Database.mMarkerDao.fetchMarkerByTravelItineraryId(nrTravel_Id, itinerary.getId());
                        LatLng markerOrigin = null, markerTarget = null;
                        for (int ii = 0; ii < marker.size(); ii++) {
                            if (marker.get(ii).getMarker_type() == 0) {
                                markerOrigin = new LatLng(Double.parseDouble(marker.get(ii).getLatitude()), Double.parseDouble(marker.get(ii).getLongitude()));
                            }
                            if (marker.get(ii).getMarker_type() == 1) {
                                markerTarget = new LatLng(Double.parseDouble(marker.get(ii).getLatitude()), Double.parseDouble(marker.get(ii).getLongitude()));
                            }
                        }
                        mMap.addPolyline(new PolylineOptions()
                                .add(markerOrigin, markerTarget)
                                .width(vWidth)
                                .clickable(true)
                                .color(vColor)
                                .geodesic(true)
                                .pattern(PATTERN_POLYLINE)
                        );
                    } else {
                        mMap.addPolyline(new PolylineOptions()
                                .addAll(decodePoly(polyline))
                                .width(vWidth)
                                .clickable(true)
                                .color(vColor)
                                .geodesic(true)
                                .pattern(PATTERN_POLYLINE)
                        );
                    }
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
            if (Objects.equals(typeUpdate, "Itinerary")) {
                if (Database.mItineraryDao.fetchItineraryByTravelId(nrTravel_Id, nrSequence).getTravel_mode() < 4) {
                    Itinerary itinerary = Database.mItineraryDao.fetchItineraryByTravelId(nrTravel_Id, nrSequence);
                    itinerary.setDistance(nrDistance);
                    itinerary.setTime(nrDuration + predicted_stop_time);
                    Database.mItineraryDao.updateItinerary(itinerary);
                }
            } else if (Objects.equals(typeUpdate, "Achievement")) {
                Achievement achievement = Database.mAchievementDao.fetchAchievementById(nrKey_Id);
                achievement.setLength_achievement(nrDistance);
                if (!Database.mAchievementDao.updateAchievement(achievement)){
                    Toast.makeText(context, R.string.Error_Changing_Data + "-" + R.string.Achievement, Toast.LENGTH_LONG).show();
                }
            } else if (Objects.equals(typeUpdate, "Tour")) {
                Tour tour = Database.mTourDao.fetchTourById(nrKey_Id);
                tour.setDistance(nrDistance);
                if (!Database.mTourDao.updateTour(tour)) {
                    Toast.makeText(context, R.string.Error_Changing_Data + "-" + R.string.Tour, Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
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