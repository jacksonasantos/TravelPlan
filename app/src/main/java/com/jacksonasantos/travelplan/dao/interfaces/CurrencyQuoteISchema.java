package com.jacksonasantos.travelplan.dao.interfaces;

public interface CurrencyQuoteISchema {

    String CURRENCY_QUOTE_TABLE = "currency_quote";

    String CURRENCY_QUOTE_ID = "id";
    String CURRENCY_QUOTE_CURRENCY_TYPE = "currency_type";
    String CURRENCY_QUOTE_QUOTE_DATE = "quote_date";
    String CURRENCY_QUOTE_CURRENCY_VALUE = "currency_value";

    // Version 8
    String CREATE_TABLE_CURRENCY_QUOTE_V8 = "CREATE TABLE IF NOT EXISTS "
            + CURRENCY_QUOTE_TABLE + " ("
            + CURRENCY_QUOTE_ID + " INTEGER PRIMARY KEY, "
            + CURRENCY_QUOTE_CURRENCY_TYPE + " INT, "
            + CURRENCY_QUOTE_QUOTE_DATE + " DATE, "
            + CURRENCY_QUOTE_CURRENCY_VALUE + " DOUBLE "
            + ")";

    String[] CURRENCY_QUOTE_COLUMNS = new String[] {
            CURRENCY_QUOTE_ID,
            CURRENCY_QUOTE_CURRENCY_TYPE,
            CURRENCY_QUOTE_QUOTE_DATE,
            CURRENCY_QUOTE_CURRENCY_VALUE
    };
}
