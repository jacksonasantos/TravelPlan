package com.jacksonasantos.travelplan.dao.interfaces;

public interface MarkerISchema {

    String MARKER_TABLE = "marker";

    String MARKER_ID = "id";
    String MARKER_TRAVEL_ID = "travel_id";
    String MARKER_ITINERARY_ID = "itinerary_id";
    String MARKER_ACHIEVEMENT_ID = "achievement_id";
    String MARKER_TOUR_ID = "tour_id";
    String MARKER_MARKER_TYPE = "marker_type";
    String MARKER_SEQUENCE = "sequence";
    String MARKER_NAME = "name";
    String MARKER_ADDRESS = "address";
    String MARKER_CITY = "city";
    String MARKER_STATE = "state";
    String MARKER_COUNTRY = "country";
    String MARKER_ABBR_COUNTRY = "abbr_country";
    String MARKER_DESCRIPTION = "description";
    String MARKER_LATITUDE = "latitude";
    String MARKER_LONGITUDE = "longitude";
    String MARKER_PREDICTED_STOP_TIME = "predicted_stop_time";

    // Version 26
    String CREATE_TABLE_MARKER_V26 = "CREATE TABLE IF NOT EXISTS "
            + MARKER_TABLE + " ("
            + MARKER_ID + " INTEGER PRIMARY KEY, "
            + MARKER_TRAVEL_ID + " INTEGER REFERENCES " + TravelISchema.TRAVEL_TABLE + " ("+TravelISchema.TRAVEL_ID+"), "
            + MARKER_SEQUENCE + " INTEGER, "
            + MARKER_NAME + " TEXT, "
            + MARKER_ADDRESS + " TEXT, "
            + MARKER_CITY + " TEXT, "
            + MARKER_STATE + " TEXT, "
            + MARKER_COUNTRY + " TEXT, "
            + MARKER_ABBR_COUNTRY + "TEXT, "
            + MARKER_DESCRIPTION + " TEXT, "
            + MARKER_LATITUDE + " TEXT, "
            + MARKER_LONGITUDE + " TEXT "
           + ")";

    // Version 33
    String ALTER_TABLE_MARKER_V33 = "ALTER TABLE " + MARKER_TABLE
            + " ADD COLUMN " + MARKER_MARKER_TYPE + " INT ";

    // Version 34
    String ALTER_TABLE_MARKER_V34 = "ALTER TABLE " + MARKER_TABLE
            + " ADD COLUMN " + MARKER_ITINERARY_ID + " INTEGER REFERENCES " + ItineraryISchema.ITINERARY_TABLE + " ("+ItineraryISchema.ITINERARY_ID+") ";

    // Version 50
    String ALTER_TABLE_MARKER_V50 = "ALTER TABLE " + MARKER_TABLE
            + " ADD COLUMN " + MARKER_ACHIEVEMENT_ID + " INTEGER REFERENCES " + AchievementISchema.ACHIEVEMENT_TABLE + " ("+AchievementISchema.ACHIEVEMENT_ID+") ";

    // Version 70
    String ALTER_TABLE_MARKER_V70 = "ALTER TABLE " + MARKER_TABLE
            + " ADD COLUMN " + MARKER_TOUR_ID + " INTEGER REFERENCES " +TourISchema.TOUR_TABLE + " ("+TourISchema.TOUR_ID+") ";

    // Version 75
    String ALTER_TABLE_MARKER_V75 = "ALTER TABLE " + MARKER_TABLE
            + " ADD COLUMN " + MARKER_PREDICTED_STOP_TIME + " INT ";

    // Version 77
    String ALTER_TABLE_MARKER_V77_1 = "PRAGMA foreign_keys=off ";
    String ALTER_TABLE_MARKER_V77_2 = "BEGIN TRANSACTION ";
    String ALTER_TABLE_MARKER_V77_3 = "ALTER TABLE " + MARKER_TABLE + " RENAME TO _table1_old ";
    String ALTER_TABLE_MARKER_V77_4 = "CREATE TABLE IF NOT EXISTS "
                                            + MARKER_TABLE + " ("
                                            + MARKER_ID + " INTEGER PRIMARY KEY, "
                                            + MARKER_TRAVEL_ID + " INTEGER REFERENCES " + TravelISchema.TRAVEL_TABLE + " ("+TravelISchema.TRAVEL_ID+"), "
                                            + MARKER_ITINERARY_ID + " INTEGER REFERENCES " + ItineraryISchema.ITINERARY_TABLE + " ("+ItineraryISchema.ITINERARY_ID+"), "
                                            + MARKER_ACHIEVEMENT_ID + " INTEGER REFERENCES " + AchievementISchema.ACHIEVEMENT_TABLE + " ("+AchievementISchema.ACHIEVEMENT_ID+"), "
                                            + MARKER_MARKER_TYPE + " INT, "
                                            + MARKER_SEQUENCE + " INTEGER, "
                                            + MARKER_NAME + " TEXT, "
                                            + MARKER_ADDRESS + " TEXT, "
                                            + MARKER_CITY + " TEXT, "
                                            + MARKER_STATE + " TEXT, "
                                            + MARKER_COUNTRY + " TEXT, "
                                            + MARKER_ABBR_COUNTRY + " TEXT, "
                                            + MARKER_DESCRIPTION + " TEXT, "
                                            + MARKER_LATITUDE + " TEXT, "
                                            + MARKER_LONGITUDE + " TEXT, "
                                            + MARKER_PREDICTED_STOP_TIME + " INT "
                                            + ") ";
    String ALTER_TABLE_MARKER_V77_5 = "INSERT INTO " + MARKER_TABLE + " SELECT id,travel_id,itinerary_id,achievement_id,marker_type,sequence,name,address,city,state,country,abbr_country,description,latitude,longitude,predicted_stop_time FROM _table1_old ";
    String ALTER_TABLE_MARKER_V77_6 = "COMMIT ";
    /*String ALTER_TABLE_MARKER_V77_71 = "DELETE TABLE _table1_old ";
    String ALTER_TABLE_MARKER_V77_72 = "DROP TABLE _table1_old ";*/
    String ALTER_TABLE_MARKER_V77_8 = "PRAGMA foreign_keys=on ";

    String[] MARKER_COLUMNS = new String[] {
            MARKER_ID,
            MARKER_TRAVEL_ID,
            MARKER_ITINERARY_ID,
            MARKER_ACHIEVEMENT_ID,
            MARKER_MARKER_TYPE,
            MARKER_SEQUENCE,
            MARKER_NAME,
            MARKER_ADDRESS,
            MARKER_CITY,
            MARKER_STATE,
            MARKER_COUNTRY,
            MARKER_ABBR_COUNTRY,
            MARKER_DESCRIPTION,
            MARKER_LATITUDE,
            MARKER_LONGITUDE,
            MARKER_PREDICTED_STOP_TIME
    };
}
