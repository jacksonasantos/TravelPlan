package com.jacksonasantos.travelplan.dao.interfaces;

public interface TravelExpensesISchema {

    String TRAVEL_EXPENSES_TABLE = "travel_expenses";

    String TRAVEL_EXPENSES_ID = "id";
    String TRAVEL_EXPENSES_TRAVEL_ID = "travel_id";
    String TRAVEL_EXPENSES_EXPENSE_TYPE = "expense_type";
    String TRAVEL_EXPENSES_EXPECTED_VALUE = "expected_value";
    String TRAVEL_EXPENSES_NOTE = "note";
    String TRAVEL_EXPENSES_MARKER_ID = "marker_id";

    // Version 31
    String CREATE_TABLE_TRAVEL_EXPENSES_V31 = "CREATE TABLE IF NOT EXISTS "
            + TRAVEL_EXPENSES_TABLE + " ("
            + TRAVEL_EXPENSES_ID + " INTEGER PRIMARY KEY, "
            + TRAVEL_EXPENSES_TRAVEL_ID + " INTEGER REFERENCES " + TravelISchema.TRAVEL_TABLE + " ("+TravelISchema.TRAVEL_ID+"), "
            + TRAVEL_EXPENSES_EXPENSE_TYPE + " INT, "
            + TRAVEL_EXPENSES_EXPECTED_VALUE + " DOUBLE, "
            + TRAVEL_EXPENSES_NOTE + " TEXT "
            + ")";

    // Version 43
    String ALTER_TABLE_TRAVEL_EXPENSES_V43 = "ALTER TABLE " + TRAVEL_EXPENSES_TABLE
            + " ADD COLUMN " + TRAVEL_EXPENSES_MARKER_ID + " INTEGER REFERENCES " + MarkerISchema.MARKER_TABLE + " ("+MarkerISchema.MARKER_ID+") ";

    String[] TRAVEL_EXPENSES_COLUMNS = new String[] {
            TRAVEL_EXPENSES_ID,
            TRAVEL_EXPENSES_TRAVEL_ID,
            TRAVEL_EXPENSES_EXPENSE_TYPE,
            TRAVEL_EXPENSES_EXPECTED_VALUE,
            TRAVEL_EXPENSES_NOTE,
            TRAVEL_EXPENSES_MARKER_ID
   };
}
