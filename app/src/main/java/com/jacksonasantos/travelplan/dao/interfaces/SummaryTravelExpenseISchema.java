package com.jacksonasantos.travelplan.dao.interfaces;

public interface SummaryTravelExpenseISchema {

    String TRAVEL_EXPENSE_EXPENSE = "expense";
    String TRAVEL_EXPENSE_EXPECTED_VALUE = "expected_value";
    String TRAVEL_EXPENSE_REALIZED_VALUE = "realized_value";

    String[] TRAVEL_EXPENSE_COLUMNS = new String[] {
            TRAVEL_EXPENSE_EXPENSE,
            TRAVEL_EXPENSE_EXPECTED_VALUE,
            TRAVEL_EXPENSE_REALIZED_VALUE
    };
}
