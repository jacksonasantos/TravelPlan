package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.TravelExpenses;

import java.util.List;

public interface TravelExpensesIDAO {
    TravelExpenses fetchTravelExpensesByTravelMarker( Integer travel_id, Integer marker_id) ;
    List<TravelExpenses> fetchAllTravelExpensesByTravel( Integer travel_id);
    List<TravelExpenses> fetchAllTravelExpensesByTravelType( Integer travel_id, Integer expense_type);
    boolean addTravelExpenses(TravelExpenses travel);
    void deleteTravelExpenses(Integer id);
}
