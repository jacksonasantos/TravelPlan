package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.TravelCash;

import java.util.List;

public interface TravelCashIDAO {
    boolean addTravelCash(TravelCash travelCash);
    void deleteTravelCash(Integer id);
    boolean updateTravelCash(TravelCash travelCash);
    TravelCash fetchTravelCashById(Integer id);
    List<TravelCash> fetchAllTravelCash();
    List<TravelCash> fetchAllTravelCashByTravel( Integer travel_id);
}