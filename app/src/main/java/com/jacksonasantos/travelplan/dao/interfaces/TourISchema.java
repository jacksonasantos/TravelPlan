package com.jacksonasantos.travelplan.dao.interfaces;

public interface TourISchema {

    String TOUR_TABLE = "tour";

    String TOUR_ID = "id";
    String TOUR_TRAVEL_ID = "travel_id";
    String TOUR_TOUR_TYPE = "tour_type";
    String TOUR_LOCAL_TOUR = "local_tour";
    String TOUR_CURRENCY_TYPE = "currency_type";
    String TOUR_VALUE_ADULT = "value_adult";
    String TOUR_VALUE_CHILD = "value_child";
    String TOUR_NUMBER_ADULT = "number_adult";
    String TOUR_NUMBER_CHILD = "number_child";
    String TOUR_TOUR_DATE = "tour_date";
    String TOUR_TOUR_SEQUENCE = "tour_sequence";
    String TOUR_OPENING_HOURS = "opening_hours";
    String TOUR_DISTANCE = "distance";
    String TOUR_VISITATION_TIME = "visitation_time";
    String TOUR_NOTE = "note";

    String TOUR_ADDRESS_TOUR = "address_tour";
    String TOUR_CITY_TOUR = "city_tour";
    String TOUR_STATE_TOUR = "state_tour";
    String TOUR_COUNTRY_TOUR = "country_tour";
    String TOUR_LATLNG_TOUR = "latlng_tour";
    String TOUR_ACHIEVEMENT_ID = "achievement_id";

    // Version 61
    String CREATE_TABLE_TOUR_V61 = "CREATE TABLE IF NOT EXISTS "
            + TOUR_TABLE + " ("
            + TOUR_ID + " INTEGER PRIMARY KEY, "
            + TOUR_TRAVEL_ID + " INTEGER REFERENCES " + TravelISchema.TRAVEL_TABLE + " ("+TravelISchema.TRAVEL_ID+"), "
            + TOUR_TOUR_TYPE + " INT, "
            + TOUR_LOCAL_TOUR + " TEXT, "
            + TOUR_CURRENCY_TYPE + " INT, "
            + TOUR_VALUE_ADULT + " DOUBLE, "
            + TOUR_VALUE_CHILD + " DOUBLE, "
            + TOUR_TOUR_DATE + " DATE, "
            + TOUR_OPENING_HOURS + " TEXT, "
            + TOUR_DISTANCE + " INT, "
            + TOUR_VISITATION_TIME + " TEXT, "
            + TOUR_NOTE + " TEXT "
            + ")";

    // Version 63
    String ALTER_TABLE_TOUR_V63_1 = "ALTER TABLE " + TOUR_TABLE
            + " ADD COLUMN " + TOUR_NUMBER_ADULT + " INT ";

    String ALTER_TABLE_TOUR_V63_2 = "ALTER TABLE " + TOUR_TABLE
            + " ADD COLUMN " + TOUR_NUMBER_CHILD + " INT ";

    // Version 64
    String ALTER_TABLE_TOUR_V64_1 = "ALTER TABLE " + TOUR_TABLE
            + " ADD COLUMN " + TOUR_ADDRESS_TOUR + " TEXT ";
    String ALTER_TABLE_TOUR_V64_2 = "ALTER TABLE " + TOUR_TABLE
            + " ADD COLUMN " + TOUR_CITY_TOUR + " TEXT ";
    String ALTER_TABLE_TOUR_V64_3 = "ALTER TABLE " + TOUR_TABLE
            + " ADD COLUMN " + TOUR_STATE_TOUR + " TEXT ";
    String ALTER_TABLE_TOUR_V64_4 = "ALTER TABLE " + TOUR_TABLE
            + " ADD COLUMN " + TOUR_COUNTRY_TOUR + " TEXT ";
    String ALTER_TABLE_TOUR_V64_5 = "ALTER TABLE " + TOUR_TABLE
            + " ADD COLUMN " + TOUR_LATLNG_TOUR + " TEXT ";

    // Version 69
    String ALTER_TABLE_TOUR_V69 = "ALTER TABLE " + TOUR_TABLE
            + " ADD COLUMN " + TOUR_ACHIEVEMENT_ID + " INTEGER REFERENCES " + AchievementISchema.ACHIEVEMENT_TABLE + " ("+AchievementISchema.ACHIEVEMENT_ID+") ";

    // Version 69
    String ALTER_TABLE_TOUR_V73 = "ALTER TABLE " + TOUR_TABLE
            + " ADD COLUMN " + TOUR_TOUR_SEQUENCE + " INT ";

    String[] TOUR_COLUMNS = new String[] {
            TOUR_ID,
            TOUR_TRAVEL_ID,
            TOUR_TOUR_TYPE,
            TOUR_LOCAL_TOUR,
            TOUR_CURRENCY_TYPE,
            TOUR_VALUE_ADULT,
            TOUR_VALUE_CHILD,
            TOUR_NUMBER_ADULT,
            TOUR_NUMBER_CHILD,
            TOUR_TOUR_DATE,
            TOUR_TOUR_SEQUENCE,
            TOUR_OPENING_HOURS,
            TOUR_DISTANCE,
            TOUR_VISITATION_TIME,
            TOUR_NOTE,
            TOUR_ADDRESS_TOUR,
            TOUR_CITY_TOUR,
            TOUR_STATE_TOUR,
            TOUR_COUNTRY_TOUR,
            TOUR_LATLNG_TOUR,
            TOUR_ACHIEVEMENT_ID
   };
}
