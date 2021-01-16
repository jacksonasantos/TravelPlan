package com.jacksonasantos.travelplan.dao.interfaces;

import android.database.Cursor;

import com.jacksonasantos.travelplan.dao.Marker;

public interface MarkerIDAO {
    Marker fetchMarkerById(Integer id);
    Cursor fetchMarkerByTravelId(Integer travel_id);
    //List<Marker> fetchAllMarker();
    boolean addMarker(Marker insurance);
    boolean deleteMarker(Integer id);
    boolean deleteMarker(Integer travel_id, String latitude, String longitude);
    boolean updateMarker(Marker insurance);

}