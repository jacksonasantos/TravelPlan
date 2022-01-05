package com.jacksonasantos.travelplan.dao.interfaces;

public interface TravelItemExpensesISchema {

    String TRAVEL_ITEM_EXPENSES_TABLE = "travel_item_expenses";

    String TRAVEL_ITEM_EXPENSES_ID = "id";
    String TRAVEL_ITEM_EXPENSES_EXPENSE_TYPE = "expense_type";
    String TRAVEL_ITEM_EXPENSES_EXPENSE_DATE = "expense_date";
    String TRAVEL_ITEM_EXPENSES_REALIZED_VALUE = "realized_value";
    String TRAVEL_ITEM_EXPENSES_NOTE = "note";
    String TRAVEL_ITEM_EXPENSES_TRAVEL_ID = "travel_id";

    // Version 31
    String CREATE_TABLE_TRAVEL_ITEM_EXPENSES_V31 = "CREATE TABLE IF NOT EXISTS "
            + TRAVEL_ITEM_EXPENSES_TABLE + " ("
            + TRAVEL_ITEM_EXPENSES_ID + " INTEGER PRIMARY KEY, "
            + TRAVEL_ITEM_EXPENSES_EXPENSE_DATE + " DATE, "
            + TRAVEL_ITEM_EXPENSES_REALIZED_VALUE + " DOUBLE, "
            + TRAVEL_ITEM_EXPENSES_NOTE + " TEXT "
            + ")";

    // Version 44
    String ALTER_TABLE_TRAVEL_ITEM_EXPENSES_V44 = "ALTER TABLE " + TRAVEL_ITEM_EXPENSES_TABLE
            + " ADD COLUMN " + TRAVEL_ITEM_EXPENSES_TRAVEL_ID + " INTEGER REFERENCES " + TravelISchema.TRAVEL_TABLE + " ("+TravelISchema.TRAVEL_ID+") ";

    // Version 44.1
    String ALTER_TABLE_TRAVEL_ITEM_EXPENSES_V44_1 = "ALTER TABLE " + TRAVEL_ITEM_EXPENSES_TABLE
            + " ADD COLUMN " + TRAVEL_ITEM_EXPENSES_EXPENSE_TYPE + " INT ";

    String[] TRAVEL_ITEM_EXPENSES_COLUMNS = new String[] {
            TRAVEL_ITEM_EXPENSES_ID,
            TRAVEL_ITEM_EXPENSES_EXPENSE_TYPE,
            TRAVEL_ITEM_EXPENSES_EXPENSE_DATE,
            TRAVEL_ITEM_EXPENSES_REALIZED_VALUE,
            TRAVEL_ITEM_EXPENSES_NOTE,
            TRAVEL_ITEM_EXPENSES_TRAVEL_ID
   };
}
