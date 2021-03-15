package com.jacksonasantos.travelplan.dao.interfaces;

public interface TravelExpensesISchema {

    String TRAVEL_EXPENSES_TABLE = "travel_expenses";

    String TRAVEL_EXPENSES_ID = "id";
    String TRAVEL_EXPENSES_TRAVEL_ID = "travel_id";
    String TRAVEL_EXPENSES_EXPENSE_TYPE = "expense_type";
    String TRAVEL_EXPENSES_EXPECTED_VALUE = "expected_value";
    String TRAVEL_EXPENSES_REALIZED_VALUE = "realized_value";
    String TRAVEL_EXPENSES_NOTE = "note";

    // Version 31
    String CREATE_TABLE_TRAVEL_EXPENSES_V31 = "CREATE TABLE IF NOT EXISTS "
            + TRAVEL_EXPENSES_TABLE + " ("
            + TRAVEL_EXPENSES_ID + " INTEGER PRIMARY KEY, "
            + TRAVEL_EXPENSES_TRAVEL_ID + " INTEGER REFERENCES " + TravelISchema.TRAVEL_TABLE + " ("+TravelISchema.TRAVEL_ID+"), "
            + TRAVEL_EXPENSES_EXPENSE_TYPE + " INT, "
            + TRAVEL_EXPENSES_EXPECTED_VALUE + " DOUBLE, "
            + TRAVEL_EXPENSES_REALIZED_VALUE + " DOUBLE, "
            + TRAVEL_EXPENSES_NOTE + " TEXT "
            + ")";

    String[] TRAVEL_EXPENSES_COLUMNS = new String[] {
            TRAVEL_EXPENSES_ID,
            TRAVEL_EXPENSES_TRAVEL_ID,
            TRAVEL_EXPENSES_EXPENSE_TYPE,
            TRAVEL_EXPENSES_EXPECTED_VALUE,
            TRAVEL_EXPENSES_REALIZED_VALUE,
            TRAVEL_EXPENSES_NOTE
   };
}
