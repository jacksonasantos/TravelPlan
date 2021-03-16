package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.TravelItemExpenses;

import java.util.List;

public interface TravelItemExpensesIDAO {
    TravelItemExpenses fetchTravelItemExpensesById(Integer id);
    List<TravelItemExpenses> fetchAllTravelItemExpenses();
    boolean addTravelItemExpenses(TravelItemExpenses travel);
    void deleteTravelItemExpenses(Integer id);
    boolean updateTravelItemExpenses(TravelItemExpenses travel);
}
