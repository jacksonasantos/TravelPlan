package com.jacksonasantos.travelplan.dao.interfaces;

public interface AchievementISchema {

    String ACHIEVEMENT_TABLE = "achievement";

    String ACHIEVEMENT_ID = "id";
    String ACHIEVEMENT_TRAVEL_ID = "travel_id";
    String ACHIEVEMENT_SHORT_NAME = "short_name";
    String ACHIEVEMENT_NAME = "name";
    String ACHIEVEMENT_IMAGE = "image";
    String ACHIEVEMENT_CITY = "city";
    String ACHIEVEMENT_STATE = "state";
    String ACHIEVEMENT_COUNTRY = "country";
    String ACHIEVEMENT_NOTE = "note";
    String ACHIEVEMENT_LATLNG_ACHIEVEMENT = "latlng_achievement";
    String ACHIEVEMENT_STATUS_ACHIEVEMENT = "status_achievement";

    // Version 36
    String CREATE_TABLE_ACHIEVEMENT_V36 = "CREATE TABLE IF NOT EXISTS "
            + ACHIEVEMENT_TABLE + " ("
            + ACHIEVEMENT_ID + " INTEGER PRIMARY KEY, "
            + ACHIEVEMENT_SHORT_NAME + " TEXT, "
            + ACHIEVEMENT_NAME + " TEXT, "
            + ACHIEVEMENT_IMAGE + " TEXT, "
            + ACHIEVEMENT_CITY + " TEXT, "
            + ACHIEVEMENT_STATE + " TEXT, "
            + ACHIEVEMENT_COUNTRY + " TEXT, "
            + ACHIEVEMENT_NOTE + " TEXT, "
            + ACHIEVEMENT_LATLNG_ACHIEVEMENT + " TEXT "
            + ")";

    // Version 38
    String ALTER_TABLE_ACHIEVEMENT_V38 = "ALTER TABLE " + ACHIEVEMENT_TABLE
            + " ADD COLUMN " + ACHIEVEMENT_TRAVEL_ID + " INTEGER REFERENCES " + TravelISchema.TRAVEL_TABLE + " ("+TravelISchema.TRAVEL_ID+") ";
    // Version 39
    String ALTER_TABLE_ACHIEVEMENT_V39 = "ALTER TABLE " + ACHIEVEMENT_TABLE
            + " ADD COLUMN " + ACHIEVEMENT_STATUS_ACHIEVEMENT + " INTEGER DEFAULT 0 ";

    String[] ACHIEVEMENT_COLUMNS = new String[] {
            ACHIEVEMENT_ID,
            ACHIEVEMENT_TRAVEL_ID,
            ACHIEVEMENT_SHORT_NAME,
            ACHIEVEMENT_NAME,
            ACHIEVEMENT_IMAGE,
            ACHIEVEMENT_CITY,
            ACHIEVEMENT_STATE,
            ACHIEVEMENT_COUNTRY,
            ACHIEVEMENT_NOTE,
            ACHIEVEMENT_LATLNG_ACHIEVEMENT,
            ACHIEVEMENT_STATUS_ACHIEVEMENT
    };
}
