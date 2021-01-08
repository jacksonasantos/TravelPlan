package com.jacksonasantos.travelplan.dao.interfaces;

public interface ItineraryISchema {

    String ITINERARY_TABLE = "itinerary";

    String ITINERARY_ID = "id";
    String ITINERARY_TRAVEL_ID = "travel_id";
    String ITINERARY_NAME = "name";
    String ITINERARY_ADDRESS = "address";
    String ITINERARY_CATEGORY_TYPE = "category_type";
    String ITINERARY_DESCRIPTION = "description";
    String ITINERARY_LATITUDE = "latitude";
    String ITINERARY_LONGITUDE = "longitude";
    String ITINERARY_ZOOM_LEVEL = "zoom_level";

    // Version 26
    String CREATE_TABLE_ITINERARY_V26 = "CREATE TABLE IF NOT EXISTS "
            + ITINERARY_TABLE + " ("
            + ITINERARY_ID + " INTEGER PRIMARY KEY, "
            + ITINERARY_TRAVEL_ID + " INTEGER REFERENCES " + TravelISchema.TRAVEL_TABLE + " ("+TravelISchema.TRAVEL_ID+"), "
            + ITINERARY_NAME + " TEXT, "
            + ITINERARY_ADDRESS + " TEXT, "
            + ITINERARY_CATEGORY_TYPE + " INT, "
            + ITINERARY_DESCRIPTION + " TEXT, "
            + ITINERARY_LATITUDE + " TEXT, "
            + ITINERARY_LONGITUDE + " TEXT, "
            + ITINERARY_ZOOM_LEVEL + " TEXT "
           + ")";

    String[] ITINERARY_COLUMNS = new String[] {
            ITINERARY_ID,
            ITINERARY_TRAVEL_ID,
            ITINERARY_NAME,
            ITINERARY_ADDRESS,
            ITINERARY_CATEGORY_TYPE,
            ITINERARY_DESCRIPTION,
            ITINERARY_LATITUDE,
            ITINERARY_LONGITUDE,
            ITINERARY_ZOOM_LEVEL
    };
}
