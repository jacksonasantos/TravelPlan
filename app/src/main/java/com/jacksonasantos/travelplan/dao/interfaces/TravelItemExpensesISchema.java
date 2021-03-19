package com.jacksonasantos.travelplan.dao.interfaces;

public interface TravelItemExpensesISchema {

    String TRAVEL_ITEM_EXPENSES_TABLE = "travel_item_expenses";

    String TRAVEL_ITEM_EXPENSES_ID = "id";
    String TRAVEL_ITEM_EXPENSES_TRAVEL_EXPENSE_ID = "travel_expense_id";
    String TRAVEL_ITEM_EXPENSES_EXPENSE_DATE = "expense_date";
    String TRAVEL_ITEM_EXPENSES_REALIZED_VALUE = "realized_value";
    String TRAVEL_ITEM_EXPENSES_NOTE = "note";

    // Version 31
    String CREATE_TABLE_TRAVEL_ITEM_EXPENSES_V31 = "CREATE TABLE IF NOT EXISTS "
            + TRAVEL_ITEM_EXPENSES_TABLE + " ("
            + TRAVEL_ITEM_EXPENSES_ID + " INTEGER PRIMARY KEY, "
            + TRAVEL_ITEM_EXPENSES_TRAVEL_EXPENSE_ID + " INTEGER REFERENCES " + TravelExpensesISchema.TRAVEL_EXPENSES_TABLE + " ("+TravelExpensesISchema.TRAVEL_EXPENSES_ID+"), "
            + TRAVEL_ITEM_EXPENSES_EXPENSE_DATE + " DATE, "
            + TRAVEL_ITEM_EXPENSES_REALIZED_VALUE + " DOUBLE, "
            + TRAVEL_ITEM_EXPENSES_NOTE + " TEXT "
            + ")";

    String[] TRAVEL_ITEM_EXPENSES_COLUMNS = new String[] {
            TRAVEL_ITEM_EXPENSES_ID,
            TRAVEL_ITEM_EXPENSES_TRAVEL_EXPENSE_ID,
            TRAVEL_ITEM_EXPENSES_EXPENSE_DATE,
            TRAVEL_ITEM_EXPENSES_REALIZED_VALUE,
            TRAVEL_ITEM_EXPENSES_NOTE
   };
}
