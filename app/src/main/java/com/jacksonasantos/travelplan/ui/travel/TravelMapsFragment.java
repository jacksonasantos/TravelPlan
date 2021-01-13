package com.jacksonasantos.travelplan.ui.travel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Marker;
import com.jacksonasantos.travelplan.dao.Travel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TravelMapsFragment extends Fragment {

    private Spinner spTravel;
    private Integer nrTravel_Id;
    private MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_travel_maps, container, false);

        spTravel = rootView.findViewById(R.id.spTravel);
        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        setHasOptionsMenu(true);            // activate menu map options

        mMapView.onResume();

        try {
            MapsInitializer.initialize(requireActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (googleMap != null) {
                        googleMap.setMyLocationEnabled(true);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);     // Show Detect location button
                    }
                }
                //googleMap.setTrafficEnabled(true);                              // Turns traffic layer on
                googleMap.setIndoorEnabled(true);                               // Enables indoor maps
                googleMap.setBuildingsEnabled(true);                            // Turns on 3D buildings
                googleMap.getUiSettings().setZoomControlsEnabled(true);         // Show Zoom buttons

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
                        drawMarker( point, "" );
                        registryMarker( point );
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                        marker.remove();
                        deleteMarker( marker.getPosition() );
                        return true;
                    }
                });
            }
        });

        return rootView;
    }

    private void deleteMarker( LatLng point ) {
        try {
            Database.mMarkerDao.deleteMarker(nrTravel_Id, String.valueOf(point.latitude), String.valueOf(point.longitude));
        } catch (Exception e) {
            Toast.makeText(getActivity(), R.string.Error_Deleting_Data + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void registryMarker(LatLng point) {
        try {
            Geocoder geocoder;
            List<Address> addresses;

            geocoder = new Geocoder(getContext(), Locale.getDefault());
            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            //String city = addresses.get(0).getSubAdminArea();
            //String state = addresses.get(0).getAdminArea();
            //String country = addresses.get(0).getCountryName();
            String knownName = addresses.get(0).getFeatureName();

            Marker i = new Marker();
            i.setTravel_id(nrTravel_Id);
            i.setName(knownName);
            i.setAddress(address);
            //i.setCategory_type();
            i.setLatitude(String.valueOf(point.latitude));
            i.setLongitude(String.valueOf(point.longitude));
            i.setZoom_level(String.valueOf(googleMap.getCameraPosition().zoom));

            Database.mMarkerDao.addMarker(i);
        } catch (IOException e) {
            Toast.makeText(getActivity(), R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void drawMarker(LatLng point, String title) {
        MarkerOptions markerOptions = new MarkerOptions();       // Creating an instance of MarkerOptions
        markerOptions.position(point);
        markerOptions.title(title);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        googleMap.addMarker(markerOptions);                      // add the marker to Map
    }

    private void drawItinerary(Integer id ){
        Cursor cursor = Database.mMarkerDao.fetchMarkerByTravelId(id);
        googleMap.clear();
        if (cursor.moveToFirst()) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            LatLngBounds bounds = builder.build();
            do {
                Marker marker = Database.mMarkerDao.cursorToEntity(cursor);
                LatLng latlng = new LatLng(Double.parseDouble(marker.getLatitude()), Double.parseDouble(marker.getLongitude()));
                builder.include(latlng);
                drawMarker(latlng, marker.getName());
            } while (cursor.moveToNext());
            cursor.close();

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.10);
            CameraUpdate camFactory = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

            googleMap.moveCamera(camFactory);
            googleMap.animateCamera(camFactory);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

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
}