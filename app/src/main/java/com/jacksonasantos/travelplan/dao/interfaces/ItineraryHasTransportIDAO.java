package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.ItineraryHasTransport;

import java.util.List;

public interface ItineraryHasTransportIDAO {
    ItineraryHasTransport fetchItineraryHasTransportById(Integer id);
    List<ItineraryHasTransport> fetchAllItineraryHasTransportByTravel( Integer travel_id );
    List<ItineraryHasTransport> fetchAllItineraryHasTransportByTravelType(Integer travel_id, Integer transport_type);
    List<ItineraryHasTransport> fetchAllItineraryHasTransportByTravelItinerary(Integer travel_id, Integer itinerary_id);
    boolean addItineraryHasTransport(ItineraryHasTransport itineraryHasTransport);
    void deleteItineraryHasTransport(Integer id);
    boolean updateItineraryHasTransport(ItineraryHasTransport itineraryHasTransport);
}