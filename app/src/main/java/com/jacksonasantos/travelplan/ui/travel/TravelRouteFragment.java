package com.jacksonasantos.travelplan.ui.travel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
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
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Itinerary;
import com.jacksonasantos.travelplan.dao.Marker;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TravelRouteFragment extends Fragment implements LocationListener {

    public boolean clearMap;
    public static Integer nrTravel_Id;
    public static Integer nrItinerary_Id;
    private Spinner spTravel;
    private Button btnAddItinerary;
    private RecyclerView listItinerary;
    private MapView mMapView;
    private GoogleMap googleMap;
    private EditText etSearch;
    private Button btnSearch;

    private final MarkerOptions markerOptions = new MarkerOptions();       // Creating an instance of MarkerOptions
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();

    private static final int REQUEST_PERMISSION = 1; // TODO - testar quando não dá permissão de localização

    String lang = "portuguese"; // TODO - ver language

    RouteClass routeClass = new RouteClass();
    ArrayList<LatLng> pointsRoute = new ArrayList<>(1);

    public TravelRouteFragment(boolean clearMap, Integer travel_id) {
        this.clearMap = clearMap;
        nrTravel_Id = travel_id;

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
        btnAddItinerary = rootView.findViewById(R.id.btnAddItinerary);
        listItinerary = rootView.findViewById(R.id.listItinerary);
        mMapView = rootView.findViewById(R.id.mapView);
        etSearch = rootView.findViewById(R.id.etSearch);
        btnSearch = rootView.findViewById(R.id.btnSearch);

        setHasOptionsMenu(true);                                           // activate menu map options
        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                if (googleMap != null) {
                    if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        String[] permissions = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
                        requestPermissions(permissions, REQUEST_PERMISSION);
                        return;
                    }
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);    // Show Detect location button
                }

                assert googleMap != null;
                googleMap.setIndoorEnabled(true);                                      // Enables indoor maps
                googleMap.setBuildingsEnabled(true);                                   // Turns on 3D buildings
                googleMap.getUiSettings().setZoomControlsEnabled(true);                // Show Zoom buttons

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng point) {
                        Geocoder geocoder = new Geocoder(requireContext());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                            Address address = addresses.get(0);
                            String addressText = String.format("%s,\n%s,\n%s, %s",
                                    address.getFeatureName(),
                                    address.getAddressLine(0),
                                    address.getSubAdminArea(),
                                    address.getCountryCode());
                            Toast.makeText(getActivity(),addressText,Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(point));
                    }
                });

                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng point) {
                        try {
                            if (registryMarker( point )) {
                                drawMarker( point, "", Color.RED,0);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.unregistered_bookmark)+" \n"+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                        if (removeMarker( marker.getPosition() )) {
                            marker.remove();
                            drawItinerary(nrTravel_Id);
                        }
                        return true;
                    }
                });

                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String g = etSearch.getText().toString();
                        Geocoder geocoder = new Geocoder(requireContext());
                        try {
                            List<Address> addresses = geocoder.getFromLocationName(g, 3);
                            if (addresses != null) {
                                showSearch(addresses);
                                etSearch.setText("");
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.location_not_found)+" \n"+ e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        mMapView.onResume();
        return rootView;
    }

    @SuppressLint("SetTextI18n")
    protected void showSearch(List<Address> addresses) {
        Address address = addresses.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        String addressText = String.format("%s, %s", address.getSubAdminArea(), address.getCountryCode());
        markerOptions.position(latLng);
        markerOptions.title(addressText);
        drawMarker(latLng, addressText, Color.MAGENTA, 0);
        zoomMarkers();
    }

    private boolean removeMarker(LatLng point ) {
        boolean result = true;
        try {
            Marker m = Database.mMarkerDao.fetchMarkerByPoint(nrTravel_Id, point);
            if ( Database.mMarkerDao.deleteMarker(nrTravel_Id, String.valueOf(point.latitude), String.valueOf(point.longitude)) ) {
                result = adjustMarker(nrTravel_Id, m.getItinerary_id(), m.getSequence(), false);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), R.string.Error_Deleting_Data + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return result;
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

        final Spinner spinItinerary = promptsView.findViewById(R.id.spinItinerary);
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

        MarkerListAdapter adapterMarker = new MarkerListAdapter(Database.mMarkerDao.fetchMarkerByTravelItineraryId(nrTravel_Id, nrItinerary_Id), requireContext(), 1, 0);
        rvListMarkers.setAdapter(adapterMarker);
        rvListMarkers.setLayoutManager(new LinearLayoutManager(requireContext()));

        Cursor cursor = Database.mItineraryDao.fetchArrayItinerary(nrTravel_Id);
        String[] adapterCols = new String[]{"text1"};
        int[] adapterRowViews = new int[]{android.R.id.text1};

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter( requireActivity(),
                android.R.layout.simple_spinner_item, cursor, adapterCols, adapterRowViews, 0);
        cursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinItinerary.setAdapter(cursorAdapter);

        Utils.setSpinnerToValue(spinItinerary, nrItinerary_Id); // Selected Value of Spinner with value marked maps
        spinItinerary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nrItinerary_Id = Math.toIntExact(spinItinerary.getSelectedItemId());
                MarkerListAdapter adapterMarker = new MarkerListAdapter(Database.mMarkerDao.fetchMarkerByTravelItineraryId(nrTravel_Id, nrItinerary_Id), requireContext(), 1, 0);
                rvListMarkers.setAdapter(adapterMarker);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

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
                            m.setItinerary_id(nrItinerary_Id);
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
                                isSave[0] = adjustMarker( nrTravel_Id, nrItinerary_Id, m.getSequence(), true );
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

    private boolean adjustMarker ( Integer travel_id, Integer itinerary_id, int sequence, boolean increment ){
        boolean result = false;
        List<Marker> cursor = Database.mMarkerDao.fetchMarkerByTravelItineraryId(travel_id, itinerary_id);
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

    private void drawMarker(LatLng point, String title, int color, int drawableIcon) {
        drawableIcon = drawableIcon==0 ? R.drawable.ic_trip_target : drawableIcon;
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(getResources().getDrawable(drawableIcon), color);
        markerOptions.position(point);
        markerOptions.title(title);
        markerOptions.draggable(true);
        markerOptions.icon(markerIcon);

        googleMap.addMarker(markerOptions);                      // add the marker to Map
        builder.include(point);
        pointsRoute.add(point);
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable, int color) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        DrawableCompat.setTint(drawable,  color);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
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
    }

    @SuppressLint("DefaultLocale")
    private void drawItinerary(Integer travel_id){
        List<Itinerary> cItinerary = Database.mItineraryDao.fetchAllItineraryByTravel(travel_id);
        if (!clearMap) {
            clearItinerary();
            for (int i = 0; i < cItinerary.size(); i++) {
                Itinerary itinerary = cItinerary.get(i);
                pointsRoute.clear();
                List<Marker> cMarker = Database.mMarkerDao.fetchMarkerByTravelItineraryId(travel_id, itinerary.getId());
                builder = new LatLngBounds.Builder();
                if (cMarker.size() > 0) {
                    for (int x = 0; x < cMarker.size(); x++) {
                        Marker marker = cMarker.get(x);
                        LatLng latlng = new LatLng(Double.parseDouble(marker.getLatitude()), Double.parseDouble(marker.getLongitude()));
                        drawMarker(latlng, marker.getName(), ContextCompat.getColor(requireContext(), R.color.colorMarker), marker.getMarker_typeImage(marker.getMarker_type()));
                    }
                    routeClass.drawRoute(googleMap, getContext(), pointsRoute, false, lang, false, nrTravel_Id, itinerary.getSequence());
                    zoomMarkers();
                }
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
        btnAddItinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(requireContext());
                View promptsView = li.inflate(R.layout.dialog_itinerary, null);

                final EditText etSequence = promptsView.findViewById(R.id.etSequence);
                final EditText etOrig_location = promptsView.findViewById(R.id.etOrig_location);
                final EditText etDest_location = promptsView.findViewById(R.id.etDest_location);
                final EditText etDaily = promptsView.findViewById(R.id.etDaily);

                final boolean[] isSave = {false};

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
                alertDialogBuilder.setView(promptsView);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (!etSequence.getText().toString().isEmpty() &&
                                    !etOrig_location.getText().toString().isEmpty() &&
                                    !etDest_location.getText().toString().isEmpty() &&
                                    !etDaily.getText().toString().isEmpty()  ) {

                                    Itinerary itinerary = new Itinerary();
                                    itinerary.setTravel_id(nrTravel_Id);
                                    itinerary.setSequence(Integer.parseInt(etSequence.getText().toString()));
                                    itinerary.setOrig_location(etOrig_location.getText().toString());
                                    itinerary.setDest_location(etDest_location.getText().toString());
                                    itinerary.setDaily(Integer.parseInt(etDaily.getText().toString()));

                                    try {
                                        //isSave[0] = adjustMarker( nrTravel_Id, nrItinerary_Id, m.getSequence(), true );
                                        isSave[0] = Database.mItineraryDao.addItinerary(itinerary);
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
            }
        });

        spTravel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                travel[0] = (Travel) parent.getItemAtPosition(position);
                nrTravel_Id = travel[0].getId();
                nrItinerary_Id = null;

                final int Show_Header_Itinerary = 0; // 0 - NO SHOW HEADER | 1 - SHOW HEADER
                final int Show_Footer_Itinerary = 1; // 0 - NO SHOW Footer | 1 - SHOW Footer
                HomeTravelItineraryListAdapter adapterItinerary = new HomeTravelItineraryListAdapter(Database.mItineraryDao.fetchAllItineraryByTravel(nrTravel_Id), requireContext(), Show_Header_Itinerary, Show_Footer_Itinerary, false);
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
        super.onCreateOptionsMenu(menu, inflater);  // TODO - ver porque nao aparece
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

    public static class HomeTravelItineraryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private static final int TYPE_FOOTER = 2;

        private long vTotDaily = 0;
        private long vTotDistance = 0;
        private long vTotTime = 0;
        private long totalHr = 0;
        private long totalMin = 0;

        public final List<Itinerary> mItinerary;
        Context context;
        int show_header;
        int show_footer;
        boolean show_home;
        boolean lClick = false;

        public HomeTravelItineraryListAdapter(List<Itinerary> itinerary, Context context, int show_header, int show_footer, boolean show_home) {
            this.mItinerary = itinerary;
            this.context = context;
            this.show_header = show_header>=1?1:0;
            this.show_footer = show_footer>=1?1:0;
            this.show_home = show_home;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itineraryView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_home_travel_item_itinerary, parent, false);

            if (viewType == TYPE_HEADER) {
                return new HeaderViewHolder(itineraryView);
            } else if (viewType == TYPE_FOOTER) {
                return new FooterViewHolder(itineraryView);
            }
            else return new ItemViewHolder(itineraryView);
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof HeaderViewHolder) {
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                headerViewHolder.llItineraryItem.setBackgroundColor(Color.LTGRAY);
                headerViewHolder.txtSequence.setText("");
                headerViewHolder.txtSource.setText(R.string.Itinerary_Source);
                headerViewHolder.txtTarget.setText(R.string.Itinerary_Target);
                headerViewHolder.txtDaily.setText(R.string.Itinerary_Daily);
                headerViewHolder.txtDistance.setText(R.string.Itinerary_Distance);
                headerViewHolder.txtTime.setText(R.string.Itinerary_Time);
            }
            else if (holder instanceof FooterViewHolder) {
                FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
                footerViewHolder.llItineraryItem.setBackgroundColor(Color.rgb(209,193,233));
                footerViewHolder.txtSource.setText(R.string.HomeTravelTotal);
                footerViewHolder.txtDaily.setText(Long.toString(vTotDaily));
                footerViewHolder.txtDistance.setText(Long.toString(vTotDistance));
                footerViewHolder.txtTime.setText(String.format("%3d:%02d", totalHr, totalMin));
            }
            else if (holder instanceof ItemViewHolder) {

                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                final Itinerary itinerary = mItinerary.get(position-show_header);
                if (!show_home) {
                    itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (position!=RecyclerView.NO_POSITION){
                                nrItinerary_Id = !itinerary.getId().equals(nrItinerary_Id) ? itinerary.getId() : null;
                                lClick = true;
                                notifyDataSetChanged();
                                // TODO - Atualizar o Mapa quando escolhe um itinerario (engrossar a linha por exemplo)
                            }
                        }
                    });
                    if (itinerary.getId().equals(nrItinerary_Id)) {
                        itemViewHolder.llItineraryItem.setBackgroundColor(Color.GRAY);
                    } else {
                        itemViewHolder.llItineraryItem.setBackgroundColor(Color.WHITE);
                    }
                }
                itemViewHolder.txtSequence.setText(Integer.toString(itinerary.getSequence()));
                itemViewHolder.txtSource.setText(itinerary.getOrig_location());
                itemViewHolder.txtTarget.setText(itinerary.getDest_location());
                itemViewHolder.txtDaily.setText(Integer.toString(itinerary.getDaily()));
                itemViewHolder.txtDistance.setText(Integer.toString(itinerary.getDistance()));
                itemViewHolder.txtTime.setText(itinerary.getDuration());
                if (!lClick){
                    vTotDaily += itinerary.getDaily();
                    vTotDistance += itinerary.getDistance();
                    if (itinerary.getTime() >0) {
                        int time = itinerary.getTime();
                        vTotTime += time;
                    }
                    totalHr = vTotTime / 3600;
                    totalMin = (vTotTime-(totalHr * 3600)) / 60;
                }
            }
        }
        @Override
        public int getItemViewType(int position) {
            if (position == 0 && show_header == 1) {
                return TYPE_HEADER;
            } if (position == mItinerary.size()+show_header && show_footer == 1) {
                return TYPE_FOOTER;
            }
            return TYPE_ITEM;
        }

        @Override
        public int getItemCount() {
            return mItinerary.size()+show_header+show_footer;
        }

        private static class HeaderViewHolder extends RecyclerView.ViewHolder {
            private final LinearLayout llItineraryItem;
            private final TextView txtSequence;
            private final TextView txtSource;
            private final TextView txtTarget;
            private final TextView txtDaily;
            private final TextView txtDistance;
            private final TextView txtTime;

            public HeaderViewHolder(View v) {
                super(v);
                llItineraryItem = v.findViewById(R.id.llItineraryItem);
                txtSequence = v.findViewById(R.id.txtSequence);
                txtSource = v.findViewById(R.id.txtSource);
                txtTarget = v.findViewById(R.id.txtTarget);
                txtDaily = v.findViewById(R.id.txtDaily);
                txtDistance = v.findViewById(R.id.txtDistance);
                txtTime = v.findViewById(R.id.txtTime);
            }
        }

        public static class ItemViewHolder extends RecyclerView.ViewHolder {
            private final LinearLayout llItineraryItem;
            private final TextView txtSequence;
            private final TextView txtSource;
            private final TextView txtTarget;
            private final TextView txtDaily;
            private final TextView txtDistance;
            private final TextView txtTime;

            public ItemViewHolder(View v) {
                super(v);
                llItineraryItem = v.findViewById(R.id.llItineraryItem);
                txtSequence = v.findViewById(R.id.txtSequence);
                txtSource = v.findViewById(R.id.txtSource);
                txtTarget = v.findViewById(R.id.txtTarget);
                txtDaily = v.findViewById(R.id.txtDaily);
                txtDistance = v.findViewById(R.id.txtDistance);
                txtTime = v.findViewById(R.id.txtTime);
            }
        }

        public static class FooterViewHolder extends RecyclerView.ViewHolder {
            private final LinearLayout llItineraryItem;
            private final TextView txtSource;
            private final TextView txtDaily;
            private final TextView txtDistance;
            private final TextView txtTime;

            public FooterViewHolder(View v) {
                super(v);
                llItineraryItem = v.findViewById(R.id.llItineraryItem);
                txtSource = v.findViewById(R.id.txtSource);
                txtDaily = v.findViewById(R.id.txtDaily);
                txtDistance = v.findViewById(R.id.txtDistance);
                txtTime = v.findViewById(R.id.txtTime);
            }
        }
    }
}