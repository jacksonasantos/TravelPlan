package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Itinerary;

import java.util.List;

public interface ItineraryIDAO {
    Itinerary fetchItineraryById(Integer id);
    List<Itinerary> fetchItineraryByTravelId(Integer travel_id);
    List<Itinerary> fetchAllItinerary();
    boolean addItinerary(Itinerary insurance);
    void deleteItinerary(Integer id);
    boolean updateItinerary(Itinerary insurance);

}