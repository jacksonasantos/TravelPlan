package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Tour;

import java.util.ArrayList;
import java.util.List;

public interface TourIDAO {
    Tour fetchTourById(Integer id);
    List<Tour> fetchAllTourByTravel(Integer travel_id);
    List<Tour> fetchAllTourByTravelItinerary(Integer travel_id, Integer itinerary_id);
    List<Tour> fetchAllTour();
    ArrayList<Tour> fetchArrayTour();
    boolean addTour(Tour tour);
    void deleteTour(Integer id);
    boolean updateTour(Tour tour);
}