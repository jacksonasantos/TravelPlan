package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Travel;

import java.text.ParseException;
import java.util.List;

public interface TravelIDAO {
    Travel fetchTravelById(Long id) throws ParseException;
    Travel fetchTravelByStatus(int status) throws ParseException;
    List<Travel> fetchAllTravel() throws ParseException;
    boolean addTravel(Travel travel);
    void deleteTravel(Long id);
    boolean updateTravel(Travel travel);
}
