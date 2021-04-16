package com.jacksonasantos.travelplan.dao.interfaces;

public interface ItineraryISchema {

    String ITINERARY_TABLE = "itinerary";

    String ITINERARY_ID = "id";
    String ITINERARY_TRAVEL_ID = "travel_id";
    String ITINERARY_SEQUENCE ="sequence";
    String ITINERARY_ORIG_LOCATION = "orig_location";
    String ITINERARY_DEST_LOCATION = "dest_location";
    String ITINERARY_DAILY = "daily";
    String ITINERARY_DISTANCE = "distance";
    String ITINERARY_TIME = "time";

    // Version 27
    String CREATE_TABLE_ITINERARY_V27 = "CREATE TABLE IF NOT EXISTS "
            + ITINERARY_TABLE + " ("
            + ITINERARY_ID + " INTEGER PRIMARY KEY, "
            + ITINERARY_TRAVEL_ID + " INTEGER REFERENCES " + TravelISchema.TRAVEL_TABLE + " ("+TravelISchema.TRAVEL_ID+"), "
            + ITINERARY_SEQUENCE + " INT, "
            + ITINERARY_ORIG_LOCATION + " TEXT, "
            + ITINERARY_DEST_LOCATION + " TEXT, "
            + ITINERARY_DAILY + "INT, "
            + ITINERARY_DISTANCE + " INT, "
            + ITINERARY_TIME + " INT "
            + ")";

    String[] ITINERARY_COLUMNS = new String[] {
            ITINERARY_ID,
            ITINERARY_TRAVEL_ID,
            ITINERARY_SEQUENCE,
            ITINERARY_ORIG_LOCATION,
            ITINERARY_DEST_LOCATION,
            ITINERARY_DAILY,
            ITINERARY_DISTANCE,
            ITINERARY_TIME
    };
}
