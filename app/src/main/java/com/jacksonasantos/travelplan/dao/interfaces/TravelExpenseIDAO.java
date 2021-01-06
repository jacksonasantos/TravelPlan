package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.TravelExpense;

import java.util.List;

public interface TravelExpenseIDAO {
    List<TravelExpense> findTravelExpense(Integer travel_id);
}