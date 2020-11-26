package com.jacksonasantos.travelplan.dao.interfaces;

public interface TravelISchema {

    String TRAVEL_TABLE = "travel";

    String TRAVEL_ID = "rowid";
    String TRAVEL_DESCRIPTION = "description";
    String TRAVEL_DEPARTURE_DATE = "departure_date";
    String TRAVEL_RETURN_DATE = "return_date";
    String TRAVEL_NOTE = "note";
    String TRAVEL_STATUS = "status";

    // Version 15
    String CREATE_TABLE_TRAVEL_V15 = "CREATE TABLE IF NOT EXISTS "
            + TRAVEL_TABLE + " ("
            + TRAVEL_DESCRIPTION + " TEXT, "
            + TRAVEL_DEPARTURE_DATE + " DATE, "
            + TRAVEL_RETURN_DATE + " DATE, "
            + TRAVEL_NOTE + " TEXT, "
            + TRAVEL_STATUS + " INT "
            + ")";

    String[] TRAVEL_COLUMNS = new String[] {
            TRAVEL_ID,
            TRAVEL_DESCRIPTION,
            TRAVEL_DEPARTURE_DATE,
            TRAVEL_RETURN_DATE,
            TRAVEL_NOTE,
            TRAVEL_STATUS
   };
}
