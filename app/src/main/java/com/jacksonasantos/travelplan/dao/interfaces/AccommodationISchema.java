package com.jacksonasantos.travelplan.dao.interfaces;

public interface AccommodationISchema {

    String ACCOMMODATION_TABLE = "accommodation";

    String ACCOMMODATION_ID = "id";
    String ACCOMMODATION_NAME = "name";
    String ACCOMMODATION_ADDRESS ="address";
    String ACCOMMODATION_CITY = "city";
    String ACCOMMODATION_STATE = "state";
    String ACCOMMODATION_COUNTRY = "country";
    String ACCOMMODATION_CONTACT_NAME = "contact_name";
    String ACCOMMODATION_PHONE = "phone";
    String ACCOMMODATION_EMAIL = "email";
    String ACCOMMODATION_SITE = "site";
    String ACCOMMODATION_LATLNG_ACCOMMODATION = "latlng_accommodation";
    String ACCOMMODATION_ACCOMMODATION_TYPE = "accommodation_type";

    // Version 27
    String CREATE_TABLE_ACCOMMODATION_V27 = "CREATE TABLE IF NOT EXISTS "
            + ACCOMMODATION_TABLE + " ("
            + ACCOMMODATION_ID + " INTEGER PRIMARY KEY, "
            + ACCOMMODATION_NAME + " TEXT, "
            + ACCOMMODATION_ADDRESS + " TEXT, "
            + ACCOMMODATION_CITY + " TEXT, "
            + ACCOMMODATION_STATE + " TEXT, "
            + ACCOMMODATION_COUNTRY + " TEXT, "
            + ACCOMMODATION_CONTACT_NAME + " TEXT, "
            + ACCOMMODATION_PHONE + " TEXT, "
            + ACCOMMODATION_EMAIL + " TEXT, "
            + ACCOMMODATION_SITE + " TEXT, "
            + ACCOMMODATION_LATLNG_ACCOMMODATION + " TEXT, "
            + ACCOMMODATION_ACCOMMODATION_TYPE + " INT "
            + ")";

    String[] ACCOMMODATION_COLUMNS = new String[] {
            ACCOMMODATION_ID,
            ACCOMMODATION_NAME,
            ACCOMMODATION_ADDRESS,
            ACCOMMODATION_CITY,
            ACCOMMODATION_STATE,
            ACCOMMODATION_COUNTRY,
            ACCOMMODATION_CONTACT_NAME,
            ACCOMMODATION_PHONE,
            ACCOMMODATION_EMAIL,
            ACCOMMODATION_SITE,
            ACCOMMODATION_LATLNG_ACCOMMODATION,
            ACCOMMODATION_ACCOMMODATION_TYPE
    };
}
