package com.jacksonasantos.travelplan.dao.interfaces;

public interface ItineraryISchema {

    String ITINERARY_TABLE = "itinerary";

    String ITINERARY_ID = "id";
    String ITINERARY_TRAVEL_ID = "travel_id";
    String ITINERARY_SEQUENCE ="sequence";
    String ITINERARY_ORIG_LOCATION = "orig_location";
    String ITINERARY_DEST_LOCATION = "dest_location";
    String ITINERARY_LATLNG_TRIP_ORIG = "latlng_trip_orig";
    String ITINERARY_LATLNG_TRIP_DEST = "latlng_trip_dest";
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
            + ITINERARY_LATLNG_TRIP_ORIG + " TEXT, "
            + ITINERARY_LATLNG_TRIP_DEST + " TEXT, "
            + ITINERARY_DISTANCE + " INT, "
            + ITINERARY_TIME + " TEXT "
            + ")";

    String[] ITINERARY_COLUMNS = new String[] {
            ITINERARY_ID,
            ITINERARY_TRAVEL_ID,
            ITINERARY_SEQUENCE,
            ITINERARY_ORIG_LOCATION,
            ITINERARY_DEST_LOCATION,
            ITINERARY_LATLNG_TRIP_ORIG,
            ITINERARY_LATLNG_TRIP_DEST,
            ITINERARY_DISTANCE,
            ITINERARY_TIME
    };
}
