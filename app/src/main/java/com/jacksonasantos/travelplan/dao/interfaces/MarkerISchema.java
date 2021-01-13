package com.jacksonasantos.travelplan.dao.interfaces;

public interface MarkerISchema {

    String MARKER_TABLE = "marker";

    String MARKER_ID = "id";
    String MARKER_TRAVEL_ID = "travel_id";
    String MARKER_NAME = "name";
    String MARKER_ADDRESS = "address";
    String MARKER_CATEGORY_TYPE = "category_type";
    String MARKER_DESCRIPTION = "description";
    String MARKER_LATITUDE = "latitude";
    String MARKER_LONGITUDE = "longitude";
    String MARKER_ZOOM_LEVEL = "zoom_level";

    // Version 26
    String CREATE_TABLE_MARKER_V26 = "CREATE TABLE IF NOT EXISTS "
            + MARKER_TABLE + " ("
            + MARKER_ID + " INTEGER PRIMARY KEY, "
            + MARKER_TRAVEL_ID + " INTEGER REFERENCES " + TravelISchema.TRAVEL_TABLE + " ("+TravelISchema.TRAVEL_ID+"), "
            + MARKER_NAME + " TEXT, "
            + MARKER_ADDRESS + " TEXT, "
            + MARKER_CATEGORY_TYPE + " INT, "
            + MARKER_DESCRIPTION + " TEXT, "
            + MARKER_LATITUDE + " TEXT, "
            + MARKER_LONGITUDE + " TEXT, "
            + MARKER_ZOOM_LEVEL + " TEXT "
           + ")";

    String[] MARKER_COLUMNS = new String[] {
            MARKER_ID,
            MARKER_TRAVEL_ID,
            MARKER_NAME,
            MARKER_ADDRESS,
            MARKER_CATEGORY_TYPE,
            MARKER_DESCRIPTION,
            MARKER_LATITUDE,
            MARKER_LONGITUDE,
            MARKER_ZOOM_LEVEL
    };
}
