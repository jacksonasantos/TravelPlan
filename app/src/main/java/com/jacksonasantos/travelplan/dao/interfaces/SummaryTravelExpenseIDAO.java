package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.SummaryTravelExpense;

import java.util.List;

public interface SummaryTravelExpenseIDAO {
    List<SummaryTravelExpense> findTravelExpense(Integer travel_id);
}