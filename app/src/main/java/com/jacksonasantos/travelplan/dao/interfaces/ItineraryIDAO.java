package com.jacksonasantos.travelplan.dao.interfaces;

import android.database.Cursor;

import com.jacksonasantos.travelplan.dao.Itinerary;

import java.util.List;

public interface ItineraryIDAO {
    Itinerary fetchItineraryById(Integer id);
    Itinerary fetchItineraryByTravelId(Integer travel_id, int sequence);
    List<Itinerary> fetchAllItineraryByTravel( Integer travel_id);
    Itinerary fetchLastItineraryByTravel(Integer travel_id);
    Cursor fetchArrayItinerary(Integer travel_id);
    boolean addItinerary(Itinerary itinerary);
    void deleteItinerary(Integer id);
    void updateItinerary(Itinerary itinerary);
}