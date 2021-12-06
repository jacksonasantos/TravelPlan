package com.jacksonasantos.travelplan.dao.interfaces;

public interface AchievementISchema {

    String ACHIEVEMENT_TABLE = "achievement";

    String ACHIEVEMENT_ID = "id";
    String ACHIEVEMENT_NAME = "name";
    String ACHIEVEMENT_CITY = "city";
    String ACHIEVEMENT_STATE = "state";
    String ACHIEVEMENT_COUNTRY = "country";
    String ACHIEVEMENT_STATUS = "status";
    String ACHIEVEMENT_NOTE = "note";
    String ACHIEVEMENT_LATLNG_ACHIEVEMENT = "latlng_achievement";

    // Version 36
    String CREATE_TABLE_ACHIEVEMENT_V36 = "CREATE TABLE IF NOT EXISTS "
            + ACHIEVEMENT_TABLE + " ("
            + ACHIEVEMENT_ID + " INTEGER PRIMARY KEY, "
            + ACHIEVEMENT_NAME + " TEXT, "
            + ACHIEVEMENT_CITY + " TEXT, "
            + ACHIEVEMENT_STATE + " TEXT, "
            + ACHIEVEMENT_COUNTRY + " TEXT, "
            + ACHIEVEMENT_STATUS + " TEXT, "
            + ACHIEVEMENT_NOTE + " TEXT, "
            + ACHIEVEMENT_LATLNG_ACHIEVEMENT + " TEXT "
            + ")";

    String[] ACHIEVEMENT_COLUMNS = new String[] {
            ACHIEVEMENT_ID,
            ACHIEVEMENT_NAME,
            ACHIEVEMENT_CITY,
            ACHIEVEMENT_STATE,
            ACHIEVEMENT_COUNTRY,
            ACHIEVEMENT_STATUS,
            ACHIEVEMENT_NOTE,
            ACHIEVEMENT_LATLNG_ACHIEVEMENT
    };
}
