package com.jacksonasantos.travelplan.dao.interfaces;

public interface AchievementISchema {

    String ACHIEVEMENT_TABLE = "achievement";

    String ACHIEVEMENT_ID = "id";
    String ACHIEVEMENT_SHORT_NAME = "short_name";
    String ACHIEVEMENT_NAME = "name";
    String ACHIEVEMENT_IMAGE = "image";
    String ACHIEVEMENT_CITY = "city";
    String ACHIEVEMENT_STATE = "state";
    String ACHIEVEMENT_LATLNG_SOURCE = "latlng_source";
    String ACHIEVEMENT_CITY_END = "city_end";
    String ACHIEVEMENT_STATE_END = "state_end";
    String ACHIEVEMENT_LATLNG_TARGET = "latlng_target";
    String ACHIEVEMENT_COUNTRY = "country";
    String ACHIEVEMENT_NOTE = "note";
    String ACHIEVEMENT_LATLNG_ACHIEVEMENT = "latlng_achievement";
    String ACHIEVEMENT_LENGTH_ACHIEVEMENT = "length_achievement";
    String ACHIEVEMENT_STATUS_ACHIEVEMENT = "status_achievement";

    // Version 36
    String CREATE_TABLE_ACHIEVEMENT_V36 = "CREATE TABLE IF NOT EXISTS "
            + ACHIEVEMENT_TABLE + " ("
            + ACHIEVEMENT_ID + " INTEGER PRIMARY KEY, "
            + ACHIEVEMENT_SHORT_NAME + " TEXT, "
            + ACHIEVEMENT_NAME + " TEXT, "
            + ACHIEVEMENT_IMAGE + " BLOB, "
            + ACHIEVEMENT_CITY + " TEXT, "
            + ACHIEVEMENT_STATE + " TEXT, "
            + ACHIEVEMENT_COUNTRY + " TEXT, "
            + ACHIEVEMENT_NOTE + " TEXT, "
            + ACHIEVEMENT_LATLNG_ACHIEVEMENT + " TEXT "
            + ")";

    // Version 39
    String ALTER_TABLE_ACHIEVEMENT_V39 = "ALTER TABLE " + ACHIEVEMENT_TABLE
            + " ADD COLUMN " + ACHIEVEMENT_STATUS_ACHIEVEMENT + " INTEGER DEFAULT 0 ";
    // Version 46
    String ALTER_TABLE_ACHIEVEMENT_V46_1 = "ALTER TABLE " + ACHIEVEMENT_TABLE
            + " ADD COLUMN " + ACHIEVEMENT_LENGTH_ACHIEVEMENT + " DOUBLE ";
    String ALTER_TABLE_ACHIEVEMENT_V46_2 = "ALTER TABLE " + ACHIEVEMENT_TABLE
            + " ADD COLUMN " + ACHIEVEMENT_CITY_END + " TEXT ";
    String ALTER_TABLE_ACHIEVEMENT_V46_3 = "ALTER TABLE " + ACHIEVEMENT_TABLE
            + " ADD COLUMN " + ACHIEVEMENT_STATE_END + " TEXT ";
    // Version 52
    String ALTER_TABLE_ACHIEVEMENT_V55_1 = "ALTER TABLE " + ACHIEVEMENT_TABLE
            + " ADD COLUMN " + ACHIEVEMENT_LATLNG_SOURCE + " TEXT ";
    String ALTER_TABLE_ACHIEVEMENT_V55_2 = "ALTER TABLE " + ACHIEVEMENT_TABLE
            + " ADD COLUMN " + ACHIEVEMENT_LATLNG_TARGET + " TEXT ";

    String[] ACHIEVEMENT_COLUMNS = new String[] {
            ACHIEVEMENT_ID,
            ACHIEVEMENT_SHORT_NAME,
            ACHIEVEMENT_NAME,
            ACHIEVEMENT_IMAGE,
            ACHIEVEMENT_CITY,
            ACHIEVEMENT_STATE,
            ACHIEVEMENT_LATLNG_SOURCE,
            ACHIEVEMENT_CITY_END,
            ACHIEVEMENT_STATE_END,
            ACHIEVEMENT_LATLNG_TARGET,
            ACHIEVEMENT_COUNTRY,
            ACHIEVEMENT_NOTE,
            ACHIEVEMENT_LATLNG_ACHIEVEMENT,
            ACHIEVEMENT_LENGTH_ACHIEVEMENT,
            ACHIEVEMENT_STATUS_ACHIEVEMENT
    };
}
