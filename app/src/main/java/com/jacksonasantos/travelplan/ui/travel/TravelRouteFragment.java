package com.jacksonasantos.travelplan.ui.travel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.jacksonasantos.travelplan.dao.Marker;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.home.HomeTravelItineraryListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TravelRouteFragment extends Fragment implements LocationListener {

    public boolean clearMap;
    private Spinner spTravel;
    private Integer nrTravel_Id;
    private RecyclerView listItinerary;
    private MapView mMapView;
    private GoogleMap googleMap;
    private EditText etSearch;
    private Button btnSearch;

    private final MarkerOptions markerOptions = new MarkerOptions();       // Creating an instance of MarkerOptions
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();

    private static final int REQUEST_PERMISSOES = 1;

    String lang = "portuguese"; // TODO - ver language

    RouteClass routeClass = new RouteClass();
    ArrayList<LatLng> pointsRoute = new ArrayList<>(1);

    public TravelRouteFragment(boolean clearMap, Integer travel_id) {
        this.clearMap = clearMap;
        this.nrTravel_Id = travel_id;

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
        listItinerary = rootView.findViewById(R.id.listItinerary);
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
                if (googleMap != null) {
                    if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        String[] permissions = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
                        requestPermissions(permissions, REQUEST_PERMISSOES);
                        return;
                    }
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);    // Show Detect location button
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
                        try {
                            if (registryMarker( point )) {
                                drawMarker( point, "", BitmapDescriptorFactory.HUE_RED);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.unregistered_bookmark)+" \n"+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                        if (deleteMarker( marker.getPosition() )) {
                            marker.remove();
                            drawItinerary(nrTravel_Id);
                        } else {  // Change the color of the Markers in Orange added by Search to Markers in Red and record them in the database
                            try {
                                if (registryMarker(marker.getPosition())) {
                                    drawMarker(marker.getPosition(), marker.getTitle(), BitmapDescriptorFactory.HUE_RED);
                                }
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.unregistered_bookmark)+" \n"+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
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
                                showSearch(addresses);
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
    protected void showSearch(List<Address> addresses) {
        Address address = addresses.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        String addressText = String.format("%s, %s", address.getSubAdminArea(), address.getCountryCode());
        markerOptions.position(latLng);
        markerOptions.title(addressText);
        drawMarker(latLng, addressText, BitmapDescriptorFactory.HUE_ORANGE);
        zoomMarkers();
    }

    private boolean deleteMarker( LatLng point ) {
        boolean result = false;
        try {
            Marker m = Database.mMarkerDao.fetchMarkerByPoint(nrTravel_Id, point);
            if ( Database.mMarkerDao.deleteMarker(nrTravel_Id, String.valueOf(point.latitude), String.valueOf(point.longitude)) ) {
                result = adjustMarker(nrTravel_Id, m.getSequence(), false);
            }
            return result;
        } catch (Exception e) {
            Toast.makeText(getActivity(), R.string.Error_Deleting_Data + e.getMessage(), Toast.LENGTH_LONG).show();
            return result;
        }
    }

    private boolean registryMarker(final LatLng point) throws IOException {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        final List<Address> addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);

        LayoutInflater li = LayoutInflater.from(requireContext());
        View promptsView = li.inflate(R.layout.dialog_marker, null);

        final TextView tvLat = promptsView.findViewById(R.id.tvLat);
        final TextView tvLng = promptsView.findViewById(R.id.tvLng);
        final TextView tvZoom = promptsView.findViewById(R.id.tvZoom);
        final TextView tvName = promptsView.findViewById(R.id.tvName);
        final TextView tvAddress = promptsView.findViewById(R.id.tvAddress);
        final TextView tvCity = promptsView.findViewById(R.id.tvCity);
        final TextView tvState = promptsView.findViewById(R.id.tvState);
        final TextView tvAbbrCountry = promptsView.findViewById(R.id.tvAbbrCountry);
        final TextView tvCountry = promptsView.findViewById(R.id.tvCountry);
        final TextView tvCategory = promptsView.findViewById(R.id.tvCategory);

        final Spinner spinMarkerType = promptsView.findViewById(R.id.spinMarkerType);
        final EditText etSeq = promptsView.findViewById(R.id.etSeq);
        final EditText etDescription = promptsView.findViewById(R.id.etDescription);
        final RecyclerView rvListMarkers = promptsView.findViewById(R.id.rvListMarkers);

        tvLat.setText(String.valueOf(point.latitude));
        tvLng.setText(String.valueOf(point.longitude));
        tvZoom.setText(String.valueOf(googleMap.getCameraPosition().zoom));
        tvName.setText(addresses.get(0).getFeatureName());
        tvAddress.setText(addresses.get(0).getAddressLine(0));
        tvCity.setText(addresses.get(0).getSubAdminArea());
        tvState.setText(addresses.get(0).getAdminArea());
        tvAbbrCountry.setText(addresses.get(0).getCountryCode());
        tvCountry.setText(addresses.get(0).getCountryName());
        tvCategory.setText("0");

        MarkerListAdapter adapterMarker = new MarkerListAdapter(Database.mMarkerDao.fetchMarkerByTravelId(nrTravel_Id), requireContext(), 1, 0);
        rvListMarkers.setAdapter(adapterMarker);
        rvListMarkers.setLayoutManager(new LinearLayoutManager(requireContext()));

        final boolean[] isSave = {false};

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!etSeq.getText().toString().isEmpty() ) {

                            Marker m = new Marker();
                            m.setTravel_id(nrTravel_Id);
                            m.setMarker_type(spinMarkerType.getSelectedItemPosition());
                            m.setSequence(Integer.valueOf(etSeq.getText().toString()));
                            m.setDescription(etDescription.getText().toString());

                            m.setName(tvName.getText().toString());
                            m.setAddress(tvAddress.getText().toString());
                            m.setCity(tvCity.getText().toString());
                            m.setState(tvState.getText().toString());
                            m.setAbbr_country(tvAbbrCountry.getText().toString());
                            m.setCountry(tvCountry.getText().toString());
                            m.setCategory_type(Integer.parseInt(tvCategory.getText().toString()));
                            m.setLatitude(tvLat.getText().toString());
                            m.setLongitude(tvLng.getText().toString());
                            m.setZoom_level(tvZoom.getText().toString());

                            try {
                                isSave[0] = adjustMarker( nrTravel_Id, m.getSequence(), true );
                                isSave[0] = Database.mMarkerDao.addMarker(m);
                                drawItinerary(nrTravel_Id);
                            } catch (Exception e) {
                                Toast.makeText(requireContext(), R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        if (!isSave[0]) {
                            Toast.makeText(requireContext(), R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return isSave[0];
    }

    private boolean adjustMarker ( Integer travel_id, int sequence, boolean increment ){
        boolean result = false;
        List<Marker> cursor = Database.mMarkerDao.fetchMarkerByTravelId(travel_id);
        for (int x = 0; x < cursor.size(); x++) {
            Marker m1 = cursor.get(x);
            if (m1.getSequence() >= sequence ){
                if (increment) {
                    m1.setSequence(m1.getSequence() + 1);
                } else {
                    m1.setSequence(m1.getSequence() - 1);
                }
                Database.mMarkerDao.updateMarker(m1);
                result = true;
            }
        }
        return result;
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

    private void drawItinerary(Integer travel_id){
        List<Marker> cursor = Database.mMarkerDao.fetchMarkerByTravelId(travel_id);
        if (!clearMap) {
            clearItinerary();

            builder = new LatLngBounds.Builder();
            for (int x = 0; x < cursor.size(); x++) {
                Marker m = cursor.get(x);
                LatLng latlng = new LatLng(Double.parseDouble(m.getLatitude()), Double.parseDouble(m.getLongitude()));
                drawMarker(latlng, m.getName(), BitmapDescriptorFactory.HUE_RED);
            }
            routeClass.drawRoute(googleMap, getActivity(), pointsRoute, false, lang, true);
            zoomMarkers();

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

        if (nrTravel_Id != null && nrTravel_Id > 0) {
            Travel trip1 = Database.mTravelDao.fetchTravelById(nrTravel_Id);
            for (int x = 0; x < spTravel.getAdapter().getCount(); x++) {
                if (spTravel.getAdapter().getItem(x).toString().equals(trip1.getDescription())) {
                    spTravel.setSelection(x);
                    break;
                }
            }
        }

        final Travel[] travel = {new Travel()};
        spTravel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                travel[0] = (Travel) parent.getItemAtPosition(position);
                nrTravel_Id = travel[0].getId();

                final int Show_Header_Itinerary = 0; // 0 - NO SHOW HEADER | 1 - SHOW HEADER
                final int Show_Footer_Itinerary = 1; // 0 - NO SHOW Footer | 1 - SHOW Footer
                HomeTravelItineraryListAdapter adapterItinerary = new HomeTravelItineraryListAdapter(Database.mItineraryDao.fetchAllItineraryByTravel(nrTravel_Id ), requireContext(), Show_Header_Itinerary, Show_Footer_Itinerary);
                if ( adapterItinerary.getItemCount() > 0){
                    listItinerary.setAdapter(adapterItinerary);
                    listItinerary.setLayoutManager(new LinearLayoutManager(requireContext()));
                    clearMap = false;
                } else {
                    clearMap = true;
                }
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