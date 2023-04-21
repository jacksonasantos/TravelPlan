package com.jacksonasantos.travelplan.dao.interfaces;

public interface TransportISchema {

    String TRANSPORT_TABLE = "transport";

    String TRANSPORT_ID = "id";
    String TRANSPORT_TRAVEL_ID = "travel_id";
    String TRANSPORT_TRANSPORT_TYPE = "transport_type";
    String TRANSPORT_IDENTIFIER = "identifier";
    String TRANSPORT_DESCRIPTION = "description";
    String TRANSPORT_COMPANY = "company";
    String TRANSPORT_CONTACT = "contact";
    String TRANSPORT_START_LOCATION = "start_location";
    String TRANSPORT_START_DATE = "start_date";
    String TRANSPORT_END_LOCATION = "end_location";
    String TRANSPORT_END_DATE = "end_date";
    String TRANSPORT_SERVICE_VALUE = "service_value";
    String TRANSPORT_SERVICE_TAX = "service_tax";
    String TRANSPORT_AMOUNT_PAID = "amount_paid";
    String TRANSPORT_NOTE = "note";


    // Version 62
    String CREATE_TABLE_TRANSPORT_V62 = "CREATE TABLE IF NOT EXISTS "
            + TRANSPORT_TABLE + " ("
            + TRANSPORT_ID + " INTEGER PRIMARY KEY, "
            + TRANSPORT_TRAVEL_ID + " INTEGER REFERENCES " + TravelISchema.TRAVEL_TABLE + " ("+TravelISchema.TRAVEL_ID+"), "
            + TRANSPORT_TRANSPORT_TYPE + " INT, "
            + TRANSPORT_IDENTIFIER + " TEXT, "
            + TRANSPORT_DESCRIPTION + " TEXT, "
            + TRANSPORT_COMPANY + " TEXT, "
            + TRANSPORT_CONTACT + " TEXT, "
            + TRANSPORT_START_LOCATION + " TEXT, "
            + TRANSPORT_START_DATE + " DATE, "
            + TRANSPORT_END_LOCATION + " TEXT, "
            + TRANSPORT_END_DATE + " DATE, "
            + TRANSPORT_SERVICE_VALUE + " DOUBLE, "
            + TRANSPORT_SERVICE_TAX + " DOUBLE, "
            + TRANSPORT_AMOUNT_PAID + " DOUBLE, "
            + TRANSPORT_NOTE + " TEXT "
            + ")";

    String[] TRANSPORT_COLUMNS = new String[] {
            TRANSPORT_ID,
            TRANSPORT_TRAVEL_ID,
            TRANSPORT_TRANSPORT_TYPE,
            TRANSPORT_IDENTIFIER,
            TRANSPORT_DESCRIPTION,
            TRANSPORT_COMPANY,
            TRANSPORT_CONTACT ,
            TRANSPORT_START_LOCATION,
            TRANSPORT_START_DATE,
            TRANSPORT_END_LOCATION,
            TRANSPORT_END_DATE,
            TRANSPORT_SERVICE_VALUE,
            TRANSPORT_SERVICE_TAX,
            TRANSPORT_AMOUNT_PAID,
            TRANSPORT_NOTE
   };
}
