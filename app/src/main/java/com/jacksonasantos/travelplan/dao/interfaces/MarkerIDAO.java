package com.jacksonasantos.travelplan.dao.interfaces;

import com.google.android.gms.maps.model.LatLng;
import com.jacksonasantos.travelplan.dao.Marker;

import java.util.List;

public interface MarkerIDAO {
    Marker fetchMarkerById(Integer marker_id);
    Marker fetchMarkerByTour(Integer tour_id);
    Marker fetchMarkerByPoint(Integer travel_id, LatLng point);
    List<Marker> fetchMarkerByTravelItineraryId(Integer travel_id, Integer itinerary_id);
    List<Marker> fetchMarkerByTravelId(Integer travel_id);
    Integer addMarker(Marker insurance);
    boolean deleteMarker(Integer id);
    boolean deleteMarker(Integer travel_id, String latitude, String longitude);
    boolean updateMarker(Marker insurance);
}