package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Travel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public interface TravelIDAO {
    Travel fetchTravelById(Integer id);
    Travel fetchTravelByStatus(int status);
    List<Travel> fetchAllTravel();
    ArrayList<Travel> fetchArrayTravel();
    boolean addTravel(Travel travel);
    void deleteTravel(Integer id);
    boolean updateTravel(Travel travel);
}
