package com.jacksonasantos.travelplan.ui.travel;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.jacksonasantos.travelplan.ui.utility.Abbreviations.getAbbreviationFromState;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.general.AchievementActivity;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TravelRouteFragment extends Fragment implements LocationListener {

    public boolean clearMap;
    public final boolean flgModeAchievement;
    public final String txt_Search;
    public Context context;

    public static Integer nrTravel_Id;
    public static Integer nrItinerary_Id;

    private Button buttonSeparator;
    private TextView tvTravel;
    private RecyclerView listItinerary;
    private MapView mMapView;
    private GoogleMap googleMap;
    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView listMarkers;

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
        this.txt_Search = tx_Search;
        this.context = getContext();

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
        listItinerary = rootView.findViewById(R.id.listItinerary);
        buttonSeparator = rootView.findViewById(R.id.buttonSeparator);
        mMapView = rootView.findViewById(R.id.mapView);
        etSearch = rootView.findViewById(R.id.etSearch);
        btnSearch = rootView.findViewById(R.id.btnSearch);
        listMarkers = rootView.findViewById(R.id.listMarkers);

        if (flgModeAchievement) {
            tvTravel.setVisibility(View.GONE);
            listItinerary.setVisibility(View.GONE);
            buttonSeparator.setVisibility(View.GONE);
            listMarkers.setVisibility(View.GONE);
        }

        if (txt_Search!=null) {
            tvTravel.setVisibility(View.GONE);
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
                    if (MarkerActivity.removeMarker(context, nrTravel_Id, marker.getPosition())) {
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



    private boolean registryMarker(@NonNull final LatLng point) {
        Intent intent = new Intent (requireContext(), MarkerActivity.class);
        intent.putExtra("travel_id", nrTravel_Id);
        if (nrItinerary_Id != null) intent.putExtra("itinerary_id", nrItinerary_Id);
        intent.putExtra("lat", point.latitude);
        intent.putExtra("lng", point.longitude);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        drawItinerary(nrTravel_Id);
        return true;
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
                routeClass.drawRoute(googleMap, getContext(), pointsRoute, false, lang, false, 'A', achievement.getId(), i, null, isAlpha, 0);
            }

        }
        zoomMarkers();
    }

    @SuppressLint("DefaultLocale")
    private void drawItinerary(Integer travel_id){
        List<Itinerary> cItinerary = Database.mItineraryDao.fetchAllItineraryByTravel(travel_id);
        if (!clearMap) {
            clearItinerary();
            builder = new LatLngBounds.Builder();
            for (int i = 0; i < cItinerary.size(); i++) {
                Itinerary itinerary = cItinerary.get(i);
                pointsRoute.clear();
                List<Marker> cMarker = Database.mMarkerDao.fetchMarkerByTravelItineraryId(travel_id, itinerary.getId());
                if (cMarker.size() > 0) {
                    for (int x = 0; x < cMarker.size(); x++) {
                        Marker marker = cMarker.get(x);
                        LatLng latlng = new LatLng(Double.parseDouble(marker.getLatitude()), Double.parseDouble(marker.getLongitude()));
                        drawMarker(latlng, marker.getName(), ContextCompat.getColor(requireContext(), R.color.colorMarker), Marker.getMarker_typeImage(marker.getMarker_type()));
                    }
                    routeClass.drawRoute(googleMap, getContext(), pointsRoute, false, lang, false, 'T', nrTravel_Id, itinerary.getSequence(), nrItinerary_Id, false, itinerary.getTravel_mode());
                    zoomMarkers();
                }
            }
            MarkerListAdapter adapterTmp;
            if (nrItinerary_Id == null) {
                adapterTmp = new MarkerListAdapter(Database.mMarkerDao.fetchMarkerByTravelId(nrTravel_Id), requireContext(), 0, 0, true, nrTravel_Id, null);
            } else {
                adapterTmp = new MarkerListAdapter(Database.mMarkerDao.fetchMarkerByTravelItineraryId(nrTravel_Id, nrItinerary_Id), requireContext(), 0, 0, true, nrTravel_Id, nrItinerary_Id);
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

        mMapView.onResume();
    }

    public boolean updateListItinerary() {
        final int Show_Header_Itinerary = 1;
        final int Show_Footer_Itinerary = 1;
        HomeTravelItineraryListAdapter adapterItinerary = new HomeTravelItineraryListAdapter(Database.mItineraryDao.fetchAllItineraryByTravel(nrTravel_Id), requireContext(), Show_Header_Itinerary, Show_Footer_Itinerary, false, nrTravel_Id);
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

        public List<Itinerary> mItinerary;
        final Context context;
        final int show_header;
        final int show_footer;
        final boolean show_home;
        boolean lClick = false;

        public HomeTravelItineraryListAdapter(List<Itinerary> itinerary, Context context, int show_header, int show_footer, boolean show_home, Integer travel_id) {
            this.mItinerary = itinerary;
            this.context = context;
            this.show_header = show_header>=1?1:0;
            this.show_footer = show_footer>=1?1:0;
            this.show_home = show_home;
            nrTravel_Id = travel_id;
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
                headerViewHolder.llItineraryItem.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
                headerViewHolder.txtDate.setText(R.string.Itinerary_Date);
                headerViewHolder.txtSource.setText(R.string.Itinerary_Source);
                headerViewHolder.txtTarget.setText(R.string.Itinerary_Target);
                headerViewHolder.txtDaily.setText(R.string.Itinerary_Daily);
                headerViewHolder.txtDistance.setText(R.string.Itinerary_Distance);
                headerViewHolder.txtTime.setText(R.string.Itinerary_Time);
                headerViewHolder.btAdd.setImageResource(R.drawable.ic_button_add);
                headerViewHolder.btAdd.setOnClickListener(v -> {
                    Intent intent = new Intent (v.getContext(), ItineraryActivity.class);
                    intent.putExtra("travel_id", nrTravel_Id );
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    notifyItemChanged(position);
                });
            }
            else if (holder instanceof FooterViewHolder) {
                FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
                footerViewHolder.llItineraryItem.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.5f));
                footerViewHolder.txtSource.setText(R.string.HomeTravelTotal);
                footerViewHolder.txtDaily.setText(Long.toString(vTotDaily));
                footerViewHolder.txtDistance.setText(Long.toString(vTotDistance));
                footerViewHolder.txtTime.setText(String.format("%3d:%02d", totalHr, totalMin));
            }
            else if (holder instanceof ItemViewHolder) {
                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                if (mItinerary.size()>0) {
                    final Itinerary itinerary = mItinerary.get(position - show_header);

                    Date itineraryDate = Database.mItineraryDao.fetchItineraryDateSequence(itinerary.getTravel_id(),itinerary.getSequence());

                    itemViewHolder.txtDate.setText(Utils.dateToString(itineraryDate).substring(0,5));
                    itemViewHolder.txtDayWeek.setText(Utils.returnDayWeek(itineraryDate, context).substring(0,3));
                    itemViewHolder.txtSequence.setText(Integer.toString(itinerary.getSequence()));
                    itemViewHolder.txtSource.setText(itinerary.getOrig_location());
                    itemViewHolder.txtTarget.setText(itinerary.getDest_location());
                    itemViewHolder.txtDaily.setText(Integer.toString(itinerary.getDaily()));
                    itemViewHolder.txtDistance.setText(Integer.toString(itinerary.getDistanceMeasureIndex()));
                    itemViewHolder.txtTime.setText(itinerary.getDuration());
                    if (!lClick) {
                        vTotDaily += itinerary.getDaily();
                        vTotDistance += itinerary.getDistanceMeasureIndex();
                        if (itinerary.getTime() > 0) {
                            vTotTime += itinerary.getTime();
                        }
                        totalHr = vTotTime / 3600;
                        totalMin = (vTotTime - (totalHr * 3600)) / 60;
                    }
                    itemViewHolder.itemView.setOnLongClickListener(view -> {
                        if (position != RecyclerView.NO_POSITION) {
                            nrItinerary_Id = !itinerary.getId().equals(nrItinerary_Id) ? itinerary.getId() : null;
                            lClick = true;
                            // TODO - Update the Map when choosing an itinerary (thickening the line for example)
                            notifyDataSetChanged();
                        }
                        return true;
                    });
                    itemViewHolder.itemView.setOnClickListener(view -> {
                        final Itinerary i1 = mItinerary.get(position - show_header);
                        Intent intent = new Intent (view.getContext(), ItineraryActivity.class);
                        intent.putExtra("itinerary_id", i1.getId() );
                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        notifyItemChanged(position);
                    });
                    itemViewHolder.btDelete.setOnClickListener (v -> new AlertDialog.Builder(v.getContext())
                            .setTitle(R.string.Itinerary_Deleting)
                            .setMessage(context.getResources().getString(R.string.Msg_Confirm) + "\n\n" +
                                    context.getResources().getString(R.string.marker_itinerary) + ": " +itinerary.getSequence()+"\n"+
                                    context.getResources().getString(R.string.from) + ": " +
                                    itinerary.getOrig_location() + "\n" +
                                    context.getResources().getString(R.string.to) + ": " +
                                    itinerary.getDest_location())
                            .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                                try {
                                    Database.mItineraryDao.deleteItinerary(itinerary.getId());
                                    ItineraryActivity.adjustItinerary( itinerary.getTravel_id(), itinerary.getSequence(), false);
                                    mItinerary = Database.mItineraryDao.fetchAllItineraryByTravel(nrTravel_Id);
                                    notifyItemRemoved(position - show_header);
                                    notifyItemRangeChanged(position - show_header, mItinerary.size());
                                } catch (Exception e) {
                                    Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            }).setNegativeButton(R.string.No, null)
                            .show()
                    );
                    if (itinerary.getId().equals(nrItinerary_Id)) {
                        itemViewHolder.llItineraryItem.setBackgroundColor(Color.GRAY);
                    } else {
                        itemViewHolder.llItineraryItem.setBackgroundColor(Color.WHITE);
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
            private final TextView txtDate;
            private final TextView txtSource;
            private final TextView txtTarget;
            private final TextView txtDaily;
            private final TextView txtDistance;
            private final TextView txtTime;
            private final ImageButton btAdd;

            public HeaderViewHolder(View v) {
                super(v);
                llItineraryItem = v.findViewById(R.id.llItineraryItem);
                txtDate = v.findViewById(R.id.txtDate);
                txtSource = v.findViewById(R.id.txtSource);
                txtTarget = v.findViewById(R.id.txtTarget);
                txtDaily = v.findViewById(R.id.txtDaily);
                txtDistance = v.findViewById(R.id.txtDistance);
                txtTime = v.findViewById(R.id.txtTime);
                btAdd = v.findViewById(R.id.btnDelete);
            }
        }

        public static class ItemViewHolder extends RecyclerView.ViewHolder {
            private final LinearLayout llItineraryItem;
            private final TextView txtDate;
            private final TextView txtDayWeek;
            private final TextView txtSequence;
            private final TextView txtSource;
            private final TextView txtTarget;
            private final TextView txtDaily;
            private final TextView txtDistance;
            private final TextView txtTime;
            private final ImageButton btDelete;

            public ItemViewHolder(View v) {
                super(v);
                llItineraryItem = v.findViewById(R.id.llItineraryItem);
                txtDate = v.findViewById(R.id.txtDate);
                txtDayWeek = v.findViewById(R.id.txtDayWeek);
                txtSequence = v.findViewById(R.id.txtSequence);
                txtSource = v.findViewById(R.id.txtSource);
                txtTarget = v.findViewById(R.id.txtTarget);
                txtDaily = v.findViewById(R.id.txtDaily);
                txtDistance = v.findViewById(R.id.txtDistance);
                txtTime = v.findViewById(R.id.txtTime);
                btDelete = v.findViewById(R.id.btnDelete);
            }
        }

        public static class FooterViewHolder extends RecyclerView.ViewHolder {
            private final LinearLayout llItineraryItem;
            private final TextView txtSource;
            private final TextView txtDaily;
            private final TextView txtDistance;
            private final TextView txtTime;
            private final ImageButton btDelete;

            public FooterViewHolder(View v) {
                super(v);
                llItineraryItem = v.findViewById(R.id.llItineraryItem);
                txtSource = v.findViewById(R.id.txtSource);
                txtDaily = v.findViewById(R.id.txtDaily);
                txtDistance = v.findViewById(R.id.txtDistance);
                txtTime = v.findViewById(R.id.txtTime);
                btDelete = v.findViewById(R.id.btnDelete);
                btDelete.setVisibility(View.INVISIBLE);
            }
        }
    }
}