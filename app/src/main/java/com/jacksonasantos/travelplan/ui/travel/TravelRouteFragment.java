package com.jacksonasantos.travelplan.ui.travel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.dao.Marker;
import com.jacksonasantos.travelplan.dao.Travel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TravelRouteFragment extends Fragment implements LocationListener {

    private final boolean clearMap;
    private Spinner spTravel;
    private Integer nrTravel_Id;
    private MapView mMapView;
    private GoogleMap googleMap;
    private EditText etSearch;
    private Button btnSearch;

    private final MarkerOptions markerOptions = new MarkerOptions();       // Creating an instance of MarkerOptions
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();

    String lang = "portuguese"; // TODO - ver language

    RouteClass routeClass = new RouteClass();
    ArrayList<LatLng> pointsRoute = new ArrayList<>(1);

    public TravelRouteFragment(boolean clearMap) {
        this.clearMap = clearMap;

        try {
            MapsInitializer.initialize(requireActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_travel_route, container, false);

        spTravel = rootView.findViewById(R.id.spTravel);
        mMapView = rootView.findViewById(R.id.mapView);
        etSearch = rootView.findViewById(R.id.etSearch);
        btnSearch = rootView.findViewById(R.id.btnSearch);

        setHasOptionsMenu(true);                                           // activate menu map options
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (googleMap != null) {
                        googleMap.setMyLocationEnabled(true);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);     // Show Detect location button
                    }
                }

                assert googleMap != null;
                //googleMap.setTrafficEnabled(true);                                     // Turns traffic layer on
                googleMap.setIndoorEnabled(true);                                      // Enables indoor maps
                googleMap.setBuildingsEnabled(true);                                   // Turns on 3D buildings
                googleMap.getUiSettings().setZoomControlsEnabled(true);                // Show Zoom buttons

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng point) {
                        Toast.makeText(getActivity(),point.toString(),Toast.LENGTH_SHORT).show();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(point));
                    }
                });

                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng point) {
                        drawMarker( point, "", BitmapDescriptorFactory.HUE_RED);
                        registryMarker( point );
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                        if (deleteMarker( marker.getPosition() )) {
                            marker.remove();
                        } else {  // Change the color of the Markers in Orange added by Search to Markers in Red and record them in the database
                            drawMarker( marker.getPosition(), marker.getTitle(), BitmapDescriptorFactory.HUE_RED);
                            registryMarker(  marker.getPosition() );
                        }
                        return true;
                    }
                });

                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String g = etSearch.getText().toString();
                        Geocoder geocoder = new Geocoder(getContext());
                        try {
                            List<Address> addresses = geocoder.getFromLocationName(g, 3);
                            if (addresses != null) {
                                search(addresses);
                                etSearch.setText("");
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.location_not_found)+" \n"+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        return rootView;
    }

    @SuppressLint("SetTextI18n")
    protected void search(List<Address> addresses) {
        Address address = addresses.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        String addressText = String.format("%s, %s", address.getSubAdminArea(), address.getCountryCode());
        markerOptions.position(latLng);
        markerOptions.title(addressText);
        drawMarker(latLng, addressText, BitmapDescriptorFactory.HUE_ORANGE);
        zoomMarkers();
    }

    private boolean deleteMarker( LatLng point ) {
        try {
            return Database.mMarkerDao.deleteMarker(nrTravel_Id, String.valueOf(point.latitude), String.valueOf(point.longitude));
        } catch (Exception e) {
            Toast.makeText(getActivity(), R.string.Error_Deleting_Data + e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void registryMarker(LatLng point) {
        try {
            Geocoder geocoder;
            List<Address> addresses;

            geocoder = new Geocoder(getContext(), Locale.getDefault());
            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);

            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getSubAdminArea();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String abbr_country = addresses.get(0).getCountryCode();
            String knownName = addresses.get(0).getFeatureName();
            int vSeq = 1;

            Marker m = new Marker();
            m.setTravel_id(nrTravel_Id);
            m.setSequence(vSeq);
            m.setName(knownName);
            m.setAddress(address);
            m.setCity(city);
            m.setState(state);
            m.setCountry(country);
            m.setAbbr_country(abbr_country);
            //m.setCategory_type();
            m.setLatitude(String.valueOf(point.latitude));
            m.setLongitude(String.valueOf(point.longitude));
            m.setZoom_level(String.valueOf(googleMap.getCameraPosition().zoom));

            Database.mMarkerDao.addMarker(m);
        } catch (IOException e) {
            Toast.makeText(getActivity(), R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void drawMarker(LatLng point, String title, float color) {
        markerOptions.position(point);
        markerOptions.title(title);
        markerOptions.draggable(true);
        color = (color != 0) ? color : BitmapDescriptorFactory.HUE_RED;
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(color));
        googleMap.addMarker(markerOptions);                      // add the marker to Map
        builder.include(point);

        pointsRoute.add(point);
        /*if (points.size() > 1){
            route.drawRoute(googleMap, getActivity(), points,true, lang,true);
            points.set(0, points.get(1));
            points.remove(1);
        }*/
    }

    private void zoomMarkers() {
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.25);
        LatLngBounds bounds = builder.build();
        CameraUpdate camFactory = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        googleMap.moveCamera(camFactory);
        googleMap.animateCamera(camFactory);
    }

    private void clearItinerary() {
        googleMap.clear();
        pointsRoute.clear();
    }

    private void drawItinerary(Integer id){
        Cursor cursor = Database.mMarkerDao.fetchMarkerByTravelId(id);
        if (!clearMap) {
            clearItinerary();

            builder = new LatLngBounds.Builder();
            if (cursor.moveToFirst()) {
                do {
                    Marker m = Database.mMarkerDao.cursorToEntity(cursor);
                    LatLng latlng = new LatLng(Double.parseDouble(m.getLatitude()), Double.parseDouble(m.getLongitude()));
                    drawMarker(latlng, m.getName(), BitmapDescriptorFactory.HUE_RED);
                } while (cursor.moveToNext());
                cursor.close();
                routeClass.drawRoute(googleMap, getActivity(), pointsRoute, false, lang, true);
                zoomMarkers();
            }
        } else {
            clearItinerary();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Database mDb = new Database(getContext());
        mDb.open();
        final List<Travel> travels =  Database.mTravelDao.fetchArrayTravel();
        ArrayAdapter<Travel> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, travels);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spTravel.setAdapter(adapter);

        final Travel[] travel = {new Travel()};
        spTravel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Retrieves data from the selected trip in Spinner
                travel[0] = (Travel) parent.getItemAtPosition(position);
                nrTravel_Id = travel[0].getId();
                drawItinerary(nrTravel_Id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                travel[0] = null;
                nrTravel_Id = 0;
            }
        });
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map_menu_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hybrid_map:
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            case R.id.normal_map:
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            default:
                googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        (Toast.makeText(getActivity(), "Reconnecting", Toast.LENGTH_LONG)).show();
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast.makeText(getActivity(),"Connection Failed! Check Network!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(getActivity(), "Latitude " + status, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getActivity(), "Latitude " + location.getLatitude()+"/"+location.getLongitude(), Toast.LENGTH_LONG).show();
    }
}