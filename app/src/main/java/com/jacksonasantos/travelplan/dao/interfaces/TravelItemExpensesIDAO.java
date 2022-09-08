package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.TravelItemExpenses;

import java.util.List;

public interface TravelItemExpensesIDAO {
    List<TravelItemExpenses> fetchTravelItemExpensesByExpenseType(Integer travel_id, Integer expense_type);
    boolean addTravelItemExpenses(TravelItemExpenses travel);
    void deleteTravelItemExpenses(Integer id);
}
