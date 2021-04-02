package com.jacksonasantos.travelplan.dao.interfaces;

import com.google.android.gms.maps.model.LatLng;
import com.jacksonasantos.travelplan.dao.Marker;

import java.util.List;

public interface MarkerIDAO {
    Marker fetchMarkerById(Integer id);
    Marker fetchMarkerByPoint(Integer travel_id, LatLng point);
    List<Marker> fetchMarkerByTravelId(Integer travel_id);
    boolean addMarker(Marker insurance);
    boolean deleteMarker(Integer id);
    boolean deleteMarker(Integer travel_id, String latitude, String longitude);
    boolean updateMarker(Marker insurance);

}