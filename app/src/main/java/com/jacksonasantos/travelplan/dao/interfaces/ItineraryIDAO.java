package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Itinerary;

import java.util.List;

public interface ItineraryIDAO {
    Itinerary fetchItineraryById(Integer id);
    List<Itinerary> fetchAllItinerary();
    List<Itinerary> fetchItineraryByTravelId( Integer travel_id);
    boolean addItinerary(Itinerary itinerary);
    void deleteItinerary(Integer id);
    boolean updateItinerary(Itinerary itinerary);
}