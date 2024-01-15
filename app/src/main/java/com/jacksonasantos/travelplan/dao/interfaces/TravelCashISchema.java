package com.jacksonasantos.travelplan.dao.interfaces;

public interface TravelCashISchema {

    String TRAVEL_CASH_TABLE = "travel_cash";

    String TRAVEL_CASH_ID = "id";
    String TRAVEL_CASH_TRAVEL_ID = "travel_id";
    String TRAVEL_CASH_CURRENCY_ID = "currency_id";
    String TRAVEL_CASH_CASH_DEPOSIT = "cash_deposit";
    String TRAVEL_CASH_AMOUNT_DEPOSIT = "amount_deposit";

    // Version 80
    String CREATE_TABLE_TRAVEL_CASH_V80 = "CREATE TABLE IF NOT EXISTS "
            + TRAVEL_CASH_TABLE + " ("
            + TRAVEL_CASH_ID + " INTEGER PRIMARY KEY, "
            + TRAVEL_CASH_TRAVEL_ID + " INTEGER REFERENCES " + TravelISchema.TRAVEL_TABLE + " ("+TravelISchema.TRAVEL_ID+"), "
            + TRAVEL_CASH_CURRENCY_ID + " INT, "
            + TRAVEL_CASH_CASH_DEPOSIT + " DATE, "
            + TRAVEL_CASH_AMOUNT_DEPOSIT + " DOUBLE "
            + ")";

    String[] TRAVEL_CASH_COLUMNS = new String[] {
            TRAVEL_CASH_ID,
            TRAVEL_CASH_TRAVEL_ID,
            TRAVEL_CASH_CURRENCY_ID,
            TRAVEL_CASH_CASH_DEPOSIT,
            TRAVEL_CASH_AMOUNT_DEPOSIT
    };
}
