package com.jacksonasantos.travelplan.dao.interfaces;

public interface SummaryTravelExpenseISchema {

    String SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE = "expense_type";
    String SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE = "expected_value";
    String SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE = "realized_value";

    String[] SUMMARY_TRAVEL_EXPENSE_COLUMNS = new String[] {
            SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE,
            SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE,
            SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE
    };
}
