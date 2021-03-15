package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.TravelExpenses;

import java.util.List;

public interface TravelExpensesIDAO {
    TravelExpenses fetchTravelExpensesById(Integer id);
    List<TravelExpenses> fetchAllTravelExpenses();
    boolean addTravelExpenses(TravelExpenses travel);
    void deleteTravelExpenses(Integer id);
    boolean updateTravelExpenses(TravelExpenses travel);
}
