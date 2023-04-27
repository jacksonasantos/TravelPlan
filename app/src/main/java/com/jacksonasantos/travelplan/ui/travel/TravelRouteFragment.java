package com.jacksonasantos.travelplan.ui.travel;

import static com.jacksonasantos.travelplan.ui.utility.Abbreviations.getAbbreviationFromState;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Achievement;
import com.jacksonasantos.travelplan.dao.Itinerary;
import com.jacksonasantos.travelplan.dao.Marker;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.TravelExpenses;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.general.AchievementActivity;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class TravelRouteFragment extends Fragment implements LocationListener {

    public boolean clearMap;
    public final boolean flgModeAchievement;
    public Integer nrTravel_Id;
    public final String txt_Search;
    public static Integer nrItinerary_Id;
    public static Integer nrAchievement_Id;

    private Button buttonSeparator;
    private TextView tvTravel;
    private Button btnAddItinerary;
    private Button btnEditItinerary;
    private RecyclerView listItinerary;
    private MapView mMapView;
    private GoogleMap googleMap;
    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView listMarkers;
    // TODO  - Melhorar ações nos botoes do itinerário

    private final MarkerOptions markerOptions = new MarkerOptions();
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();

    final Globals g = Globals.getInstance();
    final String lang = g.getCountry()+"/"+g.getLanguage();

    final RouteClass routeClass = new RouteClass();
    final ArrayList<LatLng> pointsRoute = new ArrayList<>(1);

    public TravelRouteFragment(boolean clearMap, Integer travel_id, String tx_Search, boolean flgModeAchievement) {
        this.clearMap = clearMap;
        this.flgModeAchievement = flgModeAchievement;
        nrTravel_Id = travel_id;
        txt_Search = tx_Search;

        try {
            MapsInitializer.initialize(requireContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_travel_route, container, false);

        tvTravel = rootView.findViewById(R.id.tvTravel);
        btnAddItinerary = rootView.findViewById(R.id.btnAddItinerary);
        btnEditItinerary = rootView.findViewById(R.id.btnEditItinerary);
        listItinerary = rootView.findViewById(R.id.listItinerary);
        buttonSeparator = rootView.findViewById(R.id.buttonSeparator);
        mMapView = rootView.findViewById(R.id.mapView);
        etSearch = rootView.findViewById(R.id.etSearch);
        btnSearch = rootView.findViewById(R.id.btnSearch);
        listMarkers = rootView.findViewById(R.id.listMarkers);

        if (flgModeAchievement) {
            tvTravel.setVisibility(View.GONE);
            btnAddItinerary.setVisibility(View.GONE);
            btnEditItinerary.setVisibility(View.GONE);
            listItinerary.setVisibility(View.GONE);
            buttonSeparator.setVisibility(View.GONE);
            listMarkers.setVisibility(View.GONE);
        }

        if (txt_Search!=null) {
            tvTravel.setVisibility(View.GONE);
            btnAddItinerary.setVisibility(View.GONE);
            listItinerary.setVisibility(View.GONE);
            listMarkers.setVisibility(View.GONE);
            etSearch.setText(txt_Search);
        }

        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(mMap -> {
            googleMap = mMap;
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);            // Show Detect location button
            googleMap.getUiSettings().setZoomControlsEnabled(true);                // Show Zoom buttons

            googleMap.setOnMapClickListener(point -> {
                Geocoder geocoder = new Geocoder(requireContext());
                try {
                    List<Address> addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                    Address address = Objects.requireNonNull(addresses).get(0);
                    String addressText = String.format("%s,\n%s, %s, %s",
                            address.getFeatureName(),
                            address.getAddressLine(0),
                            address.getSubAdminArea(),
                            address.getCountryCode());
                    Toast.makeText(getActivity(), addressText, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(point));
            });

            googleMap.setOnMapLongClickListener(point -> {
                try {
                    if (txt_Search == null) {
                        if (!flgModeAchievement) {
                            if (registryMarker(point)) {
                                drawMarker(point, "", Color.RED, 0);
                            }
                        } else {
                            Intent intent = new Intent(getContext(), AchievementActivity.class);
                            intent.putExtra("Latlng_Achievement", point.latitude+","+point.longitude);
                            myActivityResultLauncher.launch(intent);
                        }
                    } else {
                        Bundle mArgs = new Bundle();
                        mArgs.putString("point_marker", point.latitude + "," + point.longitude);
                        this.setArguments(mArgs);

                        Geocoder geocoder = new Geocoder(requireContext());
                        List<Address> addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                        Address address = Objects.requireNonNull(addresses).get(0);
                        mArgs.putString("feature_marker", address.getFeatureName());
                        this.setArguments(mArgs);
                        mArgs.putString("address_marker", address.getThoroughfare()+", "+address.getSubThoroughfare());
                        this.setArguments(mArgs);
                        mArgs.putString("state_marker", getAbbreviationFromState(address.getAdminArea()));
                        this.setArguments(mArgs);
                        mArgs.putString("city_marker", address.getSubAdminArea());
                        this.setArguments(mArgs);
                        mArgs.putString("country_marker", address.getCountryName());
                        this.setArguments(mArgs);

                        Toast.makeText(getActivity(), getResources().getString(R.string.loaded_coordinates), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.unregistered_bookmark) + " \n" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            googleMap.setOnMarkerClickListener(marker -> {
                if (!flgModeAchievement) {
                    if (removeMarker(marker.getPosition())) {
                        marker.remove();
                        drawItinerary(nrTravel_Id);
                    }
                } else {
                    Intent intent = new Intent(getContext(), AchievementActivity.class);
                    Integer nrMarkerAchievement = (Integer) marker.getTag();
                    intent.putExtra("achievement_id", nrMarkerAchievement);
                    myActivityResultLauncher.launch(intent);
                }
                return true;
            });

            btnSearch.setOnClickListener(v -> {
                String g = etSearch.getText().toString();
                Geocoder geocoder = new Geocoder(requireContext());
                try {
                    List<Address> addresses = geocoder.getFromLocationName(g, 3);
                    if (addresses != null) {
                        showSearch(addresses);
                        etSearch.setText("");
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.location_not_found) + " \n" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            if (txt_Search==null) {
                if (nrTravel_Id != null && nrTravel_Id > 0) {
                    Travel trip1 = Database.mTravelDao.fetchTravelById(nrTravel_Id);
                    tvTravel.setText(trip1.getDescription());
                    nrItinerary_Id = null;
                    clearMap = updateListItinerary();
                    drawItinerary(nrTravel_Id);
                } else {
                    nrTravel_Id = 0;
                    drawAchievement();
                }
            }
        });

        mMapView.onResume();
        return rootView;
    }

    final ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
            }
    );

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                String tag = new ActivityResultContracts.RequestPermission().toString();
                if (isGranted) {
                    Log.e(tag, "onActivityResult: PERMISSION GRANTED");
                } else {
                    Log.e(tag, "onActivityResult: PERMISSION DENIED");
                }
            }
    );

    protected void showSearch(@NonNull List<Address> addresses) {
        Address address = addresses.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        String addressText = String.format("%s, %s", address.getSubAdminArea(), address.getCountryCode());
        markerOptions.position(latLng);
        markerOptions.title(addressText);

        builder = new LatLngBounds.Builder();

        drawMarker(latLng, addressText, Color.MAGENTA, 0);
        zoomMarkers();
    }

    private boolean removeMarker(LatLng point ) {
        boolean result;
        try {
            Marker m = Database.mMarkerDao.fetchMarkerByPoint(nrTravel_Id, point);
            if (m.getAchievement_id()!=null && m.getAchievement_id()>0){
                Achievement a = Database.mAchievementDao.fetchAchievementById(m.getAchievement_id());
                a.setItinerary_id(null);
                a.setTravel_id(null);
                Database.mAchievementDao.updateAchievement(a);
            }
            TravelExpenses te = Database.mTravelExpensesDao.fetchTravelExpensesByTravelMarker(nrTravel_Id, m.getId() );
            result = Database.mTravelExpensesDao.deleteTravelExpenses(te.getId());
            if ( Database.mMarkerDao.deleteMarker(nrTravel_Id, String.valueOf(point.latitude), String.valueOf(point.longitude)) ) {
                result = adjustMarker(nrTravel_Id, m.getItinerary_id(), m.getSequence(), false);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), R.string.Error_Deleting_Data + e.getMessage(), Toast.LENGTH_LONG).show();
            result = false;
        }
        return result;
    }

    private boolean registryMarker(@NonNull final LatLng point) throws IOException {
        final Globals g = Globals.getInstance();
        final Locale locale = new Locale(g.getLanguage(), g.getCountry());
        final DecimalFormat decimalFormatter = (DecimalFormat) DecimalFormat.getNumberInstance(locale);

        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
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
        final Spinner spinMarkerAchievement = promptsView.findViewById(R.id.spinMarkerAchievement);
        final EditText etSeq = promptsView.findViewById(R.id.etSeq);
        final EditText etDescription = promptsView.findViewById(R.id.etDescription);
        final EditText etExpectedValue = promptsView.findViewById(R.id.etExpectedValue);
        final RecyclerView rvListMarkers = promptsView.findViewById(R.id.rvListMarkers);

        tvLat.setText(String.valueOf(point.latitude));
        tvLng.setText(String.valueOf(point.longitude));
        tvZoom.setText(String.valueOf(googleMap.getCameraPosition().zoom));
        tvName.setText(Objects.requireNonNull(addresses).get(0).getFeatureName());
        tvAddress.setText(addresses.get(0).getAddressLine(0));
        tvCity.setText(addresses.get(0).getSubAdminArea());
        tvState.setText(addresses.get(0).getAdminArea());
        tvAbbrCountry.setText(addresses.get(0).getCountryCode());
        tvCountry.setText(addresses.get(0).getCountryName());
        tvCategory.setText("0");

        MarkerListAdapter adapterMarker = new MarkerListAdapter(Database.mMarkerDao.fetchMarkerByTravelItineraryId(nrTravel_Id, nrItinerary_Id), requireContext(), 1, 0);
        rvListMarkers.setAdapter(adapterMarker);
        rvListMarkers.setLayoutManager(new LinearLayoutManager(requireContext()));

        spinMarkerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Marker m = new Marker();
                etExpectedValue.setText(decimalFormatter.format(m.getMarker_typeExpectedValue(i)==null? BigDecimal.ZERO:m.getMarker_typeExpectedValue(i)));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        String[] adapterCols = new String[]{"text1"};
        int[] adapterRowViews = new int[]{android.R.id.text1};

        Cursor cItinerary = Database.mItineraryDao.fetchArrayItinerary(nrTravel_Id);
        SimpleCursorAdapter cursorAdapterI = new SimpleCursorAdapter( requireActivity(),
                android.R.layout.simple_spinner_item, cItinerary, adapterCols, adapterRowViews, 0);
        cursorAdapterI.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinItinerary.setAdapter(cursorAdapterI);
        Utils.setSpinnerToValue(spinItinerary, nrItinerary_Id); // Selected Value of Spinner with value marked maps
        spinItinerary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        Cursor cAchievement = Database.mAchievementDao.fetchArrayAchievement();
        SimpleCursorAdapter cursorAdapterA = new SimpleCursorAdapter( requireActivity(),
                android.R.layout.simple_spinner_item, cAchievement, adapterCols, adapterRowViews, 0);
        cursorAdapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMarkerAchievement.setAdapter(cursorAdapterA);
        Utils.setSpinnerToValue(spinMarkerAchievement, nrAchievement_Id); // Selected Value of Spinner with value marked maps
        spinMarkerAchievement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nrAchievement_Id = Math.toIntExact(spinMarkerAchievement.getSelectedItemId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        AtomicBoolean isSave = new AtomicBoolean(false);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.OK, (dialog, id) -> {
                    if (nrItinerary_Id==null || nrItinerary_Id==0) {
                        Toast.makeText(requireContext(), R.string.itinerary_not_selected, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                    if (!etSeq.getText().toString().isEmpty() ) {

                        Marker m = new Marker();
                        m.setTravel_id(nrTravel_Id);
                        m.setItinerary_id(nrItinerary_Id);
                        m.setAchievement_id(nrAchievement_Id==0?null:nrAchievement_Id);
                        m.setMarker_type(spinMarkerType.getSelectedItemPosition());
                        m.setSequence(Integer.valueOf(etSeq.getText().toString()));
                        m.setDescription(etDescription.getText().toString());

                        m.setName(tvName.getText(). toString());
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
                           isSave.set(adjustMarker(nrTravel_Id, nrItinerary_Id, m.getSequence(), true));
                           isSave.set(Database.mMarkerDao.addMarker(m));

                           if ( nrAchievement_Id > 0 ) {
                               Achievement a = Database.mAchievementDao.fetchAchievementById(nrAchievement_Id);
                               a.setLatlng_achievement(tvLat.getText().toString() + "," + tvLng.getText().toString());
                               a.setTravel_id(a.getTravel_id()==0?nrTravel_Id:a.getTravel_id());
                               a.setItinerary_id(a.getItinerary_id()==0?nrItinerary_Id:a.getItinerary_id());
                               isSave.set(Database.mAchievementDao.updateAchievement(a));
                           } else {
                               if ( m.getMarker_type() == 9 ) {
                                   Achievement a = new Achievement();
                                   a.setTravel_id(nrTravel_Id);
                                   a.setItinerary_id(nrItinerary_Id);
                                   a.setShort_name(tvName.getText().toString());
                                   a.setName(tvName.getText().toString());
                                   a.setCity(tvCity.getText().toString());
                                   a.setState(tvState.getText().toString());
                                   a.setCountry(tvAbbrCountry.getText().toString());
                                   a.setLatlng_achievement(tvLat.getText().toString()+","+tvLng.getText().toString());
                                   a.setStatus_achievement(0);
                                   isSave.set(Database.mAchievementDao.addAchievement(a));
                               }
                           }

                           if ( Double.parseDouble(etExpectedValue.getText().toString()) > 0 ) {
                               LatLng latlng = new LatLng(Double.parseDouble(tvLat.getText().toString()), Double.parseDouble(tvLng.getText().toString()));
                               m = Database.mMarkerDao.fetchMarkerByPoint(nrTravel_Id, latlng);
                               TravelExpenses te = new TravelExpenses();
                               te.setTravel_id(m.getTravel_id());
                               te.setExpense_type(m.getMarker_typeExpenseType(spinMarkerType.getSelectedItemPosition()));
                               te.setExpected_value(Double.parseDouble(etExpectedValue.getText().toString()));
                               te.setNote(getResources().getString(R.string.marker)+" " + m.getId() + " - " + getResources().getString(R.string.marker_itinerary) + ": " + nrItinerary_Id);
                               te.setMarker_id(m.getId());
                               isSave.set(Database.mTravelExpensesDao.addTravelExpenses(te));
                           }
                           drawItinerary(nrTravel_Id);

                       } catch (Exception e) {
                           Toast.makeText(requireContext(), R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                       }
                    } else {
                        Toast.makeText(requireContext(), R.string.marker_sequence_not_informed, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.Cancel, (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return isSave.get();
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

    private Bitmap addBorder(Bitmap bmp, int borderSize) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int length = Math.max(width, height);
        float radius = width > height ? (((float) width)+(borderSize * 2)) / 2f : (((float) height)+(borderSize * 2)) / 2f;
        Bitmap bmpWithBorder = Bitmap.createBitmap(width + (borderSize * 2), height + (borderSize * 2), bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderSize);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        canvas.drawCircle(((float) (length+(borderSize*2)) / 2), ((float)(length+(borderSize*2)) / 2), radius - ((float)borderSize/2), paint);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }
    private void drawMarker(LatLng point, String title, Bitmap drawableIcon, boolean isAlpha, Integer id, boolean drawRoute) {
        int height = 100;
        int width = 100;
        int borderSize = 5;
        Bitmap markerIcon = Bitmap.createScaledBitmap(drawableIcon, width, height, false);
        if (!drawRoute) markerIcon = addBorder(markerIcon, borderSize);
        markerOptions.position(point);
        markerOptions.title(title);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(markerIcon));
        if (isAlpha) markerOptions.alpha(0.6f); else markerOptions.alpha(1f);
        Objects.requireNonNull(googleMap.addMarker(markerOptions)).setTag(id);                      // add the marker to Map
        markerOptions.alpha(1f);
        builder.include(point);
        pointsRoute.add(point);
    }

    private void drawMarker(LatLng point, String title, int color, int drawableIcon) {
        drawableIcon = drawableIcon==0 ? R.drawable.ic_trip_target : drawableIcon;
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(requireActivity().getResources(), drawableIcon, null)), color);
        markerOptions.position(point);
        markerOptions.title(title);
        markerOptions.draggable(true);
        markerOptions.icon(markerIcon);

        googleMap.addMarker(markerOptions);                      // add the marker to Map
        builder.include(point);
        pointsRoute.add(point);
    }

    private @NonNull BitmapDescriptor getMarkerIconFromDrawable(@NonNull Drawable drawable, int color) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getMinimumWidth(), drawable.getMinimumHeight(), Bitmap.Config.ARGB_8888);
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

    private void drawAchievement() {
        List<Achievement> cAchievement = Database.mAchievementDao.fetchAllAchievement();
        // TODO - Implement erasure of achievements in MAP mode
        builder = new LatLngBounds.Builder();

        for (int i = 0; i < cAchievement.size(); i++) {
            Achievement achievement = cAchievement.get(i);

            pointsRoute.clear();
            LatLng latlngSource = null;
            LatLng latlngTarget = null;
            boolean achievementRoute = false;
            LatLng latlngIcon = null;
            Bitmap rawIcon = null;
            boolean isAlpha  = (achievement.getStatus_achievement() != 1);

            if (   (achievement.getLatlng_source()!=null && !achievement.getLatlng_source().equals(""))
                && (achievement.getLatlng_target()!=null && !achievement.getLatlng_target().equals(""))) {
                String[] aLatLngSource = achievement.getLatlng_source().split(",");
                String[] aLatLngTarget = achievement.getLatlng_target().split(",");
                latlngSource = new LatLng(Double.parseDouble(aLatLngSource[0]), Double.parseDouble(aLatLngSource[1]));
                latlngTarget = new LatLng(Double.parseDouble(aLatLngTarget[0]), Double.parseDouble(aLatLngTarget[1]));
                achievementRoute=true;
            }
            if (achievement.getLatlng_achievement() != null && !achievement.getLatlng_achievement().equals("")) {
                String[] aLatLng = achievement.getLatlng_achievement().split(",");
                latlngIcon = new LatLng(Double.parseDouble(aLatLng[0]), Double.parseDouble(aLatLng[1]));

                byte[] imgArray = achievement.getImage();
                if (imgArray != null) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    rawIcon = BitmapFactory.decodeByteArray(imgArray, 0, imgArray.length, options);
                }
            }

            if (latlngSource != null)
               drawMarker(latlngSource, null, ContextCompat.getColor(requireContext(), R.color.colorMarker), 0);
            if (rawIcon != null)
               drawMarker(latlngIcon  , achievement.getName(), rawIcon, isAlpha, achievement.getId(), achievementRoute);
            if (latlngTarget != null)
               drawMarker(latlngTarget, null, ContextCompat.getColor(requireContext(), R.color.colorMarker), 0);

            if (achievementRoute) {
                routeClass.drawRoute(googleMap, getContext(), pointsRoute, false, lang, false, 'A', achievement.getId(), i, null, isAlpha);
            }

        }
        zoomMarkers();
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
                    routeClass.drawRoute(googleMap, getContext(), pointsRoute, false, lang, false, 'T', nrTravel_Id, itinerary.getSequence(), nrItinerary_Id, false);
                    zoomMarkers();
                }
            }
            MarkerListAdapter adapterTmp;
            if (nrItinerary_Id == null) {
                adapterTmp = new MarkerListAdapter(Database.mMarkerDao.fetchMarkerByTravelId(nrTravel_Id), requireContext(), 0, 0);
            } else {
                adapterTmp = new MarkerListAdapter(Database.mMarkerDao.fetchMarkerByTravelItineraryId(nrTravel_Id, nrItinerary_Id), requireContext(), 0, 0);
            }

            if (adapterTmp.getItemCount() > 0) {
                listMarkers.setAdapter(adapterTmp);
                listMarkers.setLayoutManager(new LinearLayoutManager(requireContext()));
            } else {
                listMarkers.setVisibility(View.GONE);
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

        btnAddItinerary.setOnClickListener(view -> {
            Itinerary itinerary = new Itinerary();
            MaintenanceItinerary(itinerary, true);
            drawItinerary(nrTravel_Id);
        });

        btnEditItinerary.setOnClickListener(view -> {
            if (nrItinerary_Id != null) {
                Itinerary itinerary = Database.mItineraryDao.fetchItineraryById(nrItinerary_Id);
                MaintenanceItinerary(itinerary, false);
                drawItinerary(nrTravel_Id);
            } else {
                Toast.makeText(requireContext(), R.string.select_itinerary_to_edit, Toast.LENGTH_LONG).show();
            }
        });

        mMapView.onResume();
    }

    void MaintenanceItinerary(Itinerary i, boolean add){

        LayoutInflater li = LayoutInflater.from(requireContext());
        View promptsView = li.inflate(R.layout.dialog_itinerary, null);

        final EditText etSequence = promptsView.findViewById(R.id.etSequence);
        final EditText etOrig_location = promptsView.findViewById(R.id.etOrig_location);
        final EditText etDest_location = promptsView.findViewById(R.id.etDest_location);
        final EditText etDaily = promptsView.findViewById(R.id.etDaily);
        final LinearLayout llItineraryHasTransport = promptsView.findViewById(R.id.llItineraryHasTransport);
        final RecyclerView rvItineraryHasTransport = promptsView.findViewById(R.id.rvItineraryHasTransport);
        final boolean[] isSave = {false};

        if (add) {
            Itinerary itineraryLast = Database.mItineraryDao.fetchLastItineraryByTravel(nrTravel_Id);
            if (itineraryLast != null) {
                etSequence.setText(String.valueOf(itineraryLast.getSequence() + 1));
                etOrig_location.setText(itineraryLast.getDest_location());
            } else {
                etSequence.setText(String.valueOf(1));
            }
            llItineraryHasTransport.setVisibility(View.INVISIBLE);
        } else {
            nrTravel_Id = i.getTravel_id();
            etSequence.setText(String.valueOf(i.getSequence()));
            etDest_location.setText(i.getDest_location());
            etOrig_location.setText(i.getOrig_location());
            etDaily.setText(String.valueOf(i.getDaily()));
            llItineraryHasTransport.setVisibility(View.VISIBLE);

            // Itinerary has Transport
            final int Show_Header_ItineraryHasTransport = 1  ;
            ItineraryHasTransportListAdapter adapterItineraryHasTransport = new ItineraryHasTransportListAdapter(Database.mItineraryHasTransportDao.fetchAllItineraryHasTransportByTravelItinerary(nrTravel_Id, nrItinerary_Id ), getContext(),"Home", Show_Header_ItineraryHasTransport, nrTravel_Id);
            rvItineraryHasTransport.setAdapter(adapterItineraryHasTransport);
            rvItineraryHasTransport.setLayoutManager(new LinearLayoutManager(getContext()));

        }

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.OK, (dialog, id) -> {
                    if (!etSequence.getText().toString().isEmpty() &&
                            !etOrig_location.getText().toString().isEmpty() &&
                            !etDest_location.getText().toString().isEmpty() &&
                            !etDaily.getText().toString().isEmpty()) {

                        Itinerary itinerary = new Itinerary();
                        if (!add) itinerary.setId(i.getId());
                        itinerary.setTravel_id(nrTravel_Id);
                        itinerary.setSequence(Integer.parseInt(etSequence.getText().toString()));
                        itinerary.setOrig_location(etOrig_location.getText().toString());
                        itinerary.setDest_location(etDest_location.getText().toString());
                        itinerary.setDaily(Integer.parseInt(etDaily.getText().toString()));
                        if (!add) itinerary.setDistance(i.getDistance());
                        if (!add) itinerary.setTime(i.getTime());

                        try {
                            if (add) {
                                isSave[0] = Database.mItineraryDao.addItinerary(itinerary);
                            }
                            else {
                                Database.mItineraryDao.updateItinerary(itinerary);
                                isSave[0] = true;
                            }
                            clearMap = updateListItinerary();

                        } catch (Exception e) {
                            Toast.makeText(requireContext(), (add?R.string.Error_Including_Data:R.string.Error_Changing_Data) + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    if (!isSave[0]) {
                        Toast.makeText(requireContext(), R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.Cancel, (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public boolean updateListItinerary() {
        final int Show_Header_Itinerary = 0; // 0 - NO SHOW HEADER | 1 - SHOW HEADER
        final int Show_Footer_Itinerary = 1; // 0 - NO SHOW Footer | 1 - SHOW Footer
        HomeTravelItineraryListAdapter adapterItinerary = new HomeTravelItineraryListAdapter(Database.mItineraryDao.fetchAllItineraryByTravel(nrTravel_Id), requireContext(), Show_Header_Itinerary, Show_Footer_Itinerary, false);
        if ( adapterItinerary.getItemCount() > 0){
            listItinerary.setAdapter(adapterItinerary);
            listItinerary.setLayoutManager(new LinearLayoutManager(requireContext()));
            return false;
        } else {
            return true;
        }
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
    public void onProviderEnabled(@NonNull String provider) {
        Toast.makeText(getActivity(), "Reconnecting", Toast.LENGTH_LONG).show();
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
    public void onLocationChanged(@NonNull Location location) {
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
        final Context context;
        final int show_header;
        final int show_footer;
        final boolean show_home;
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

            if (viewType == TYPE_HEADER) return new HeaderViewHolder(itineraryView);
            else if (viewType == TYPE_FOOTER) return new FooterViewHolder(itineraryView);
            else return new ItemViewHolder(itineraryView);
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale", "NotifyDataSetChanged"})
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
                footerViewHolder.llItineraryItem.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.7f));
                footerViewHolder.txtSource.setText(R.string.HomeTravelTotal);
                footerViewHolder.txtDaily.setText(Long.toString(vTotDaily));
                footerViewHolder.txtDistance.setText(Long.toString(vTotDistance));
                footerViewHolder.txtTime.setText(String.format("%3d:%02d", totalHr, totalMin));
            }
            else if (holder instanceof ItemViewHolder) {
                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                if (mItinerary.size()>0) {
                    final Itinerary itinerary = mItinerary.get(position - show_header);
                    if (!show_home) {
                        itemViewHolder.itemView.setOnClickListener(view -> {
                            if (position != RecyclerView.NO_POSITION) {
                                nrItinerary_Id = !itinerary.getId().equals(nrItinerary_Id) ? itinerary.getId() : null;
                                lClick = true;

                                // TODO - Update the Map when choosing an itinerary (thickening the line for example)
                                notifyDataSetChanged();
                            }
                        });
                        itemViewHolder.itemView.setOnLongClickListener(view -> {
                            new AlertDialog.Builder(view.getContext())
                                    .setTitle(R.string.Itinerary_Deleting)
                                    .setMessage(context.getResources().getString(R.string.Msg_Confirm) + "\n\n" +
                                            context.getResources().getString(R.string.marker_itinerary) + ":\n" +
                                            context.getResources().getString(R.string.from) + ": " +
                                            itinerary.getOrig_location() + "\n" +
                                            context.getResources().getString(R.string.to) + ": " +
                                            itinerary.getDest_location())
                                    .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                                        try {
                                            Database.mItineraryDao.deleteItinerary(nrItinerary_Id);
                                            mItinerary.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, mItinerary.size());
                                        } catch (Exception e) {
                                            Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }).setNegativeButton(R.string.No, null)
                                    .show();
                            return true;
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
                    if (!lClick) {
                        vTotDaily += itinerary.getDaily();
                        vTotDistance += itinerary.getDistance();
                        if (itinerary.getTime() > 0) {
                            vTotTime += itinerary.getTime();
                        }
                        totalHr = vTotTime / 3600;
                        totalMin = (vTotTime - (totalHr * 3600)) / 60;
                    }
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 && show_header == 1) return TYPE_HEADER;
            if (position == mItinerary.size()+show_header  && (show_footer == 1)) return TYPE_FOOTER;
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