package com.jacksonasantos.travelplan.dao.interfaces;

public interface ReservationISchema {

    String RESERVATION_TABLE = "reservation";

    String RESERVATION_ID = "id";
    String RESERVATION_TRAVEL_ID = "travel_id";
    String RESERVATION_ACCOMMODATION_ID ="accommodation_id";
    String RESERVATION_VOUCHER_NUMBER = "voucher_number";
    String RESERVATION_CHECKIN_DATE = "checkin_date";
    String RESERVATION_CHECKOUT_DATE = "checkout_date";
    String RESERVATION_APT_TYPE = "apt_type";
    String RESERVATION_DAILY_RATE = "daily_rate";
    String RESERVATION_OTHER_RATE = "other_rate";
    String RESERVATION_RESERVATION_AMOUNT = "reservation_amount";
    String RESERVATION_CURRENCY_TYPE = "currency_type";
    String RESERVATION_NOTE = "note";
    String RESERVATION_STATUS_RESERVATION = "status_reservation";

    // Version 27
    String CREATE_TABLE_RESERVATION_V27 = "CREATE TABLE IF NOT EXISTS "
            + RESERVATION_TABLE + " ("
            + RESERVATION_ID + " INTEGER PRIMARY KEY, "
            + RESERVATION_TRAVEL_ID + " INTEGER REFERENCES " + TravelISchema.TRAVEL_TABLE + " ("+TravelISchema.TRAVEL_ID+"), "
            + RESERVATION_ACCOMMODATION_ID + " INTEGER REFERENCES " + AccommodationISchema.ACCOMMODATION_TABLE + " ("+AccommodationISchema.ACCOMMODATION_ID+"), "
            + RESERVATION_VOUCHER_NUMBER + " TEXT, "
            + RESERVATION_CHECKIN_DATE + " DATE, "
            + RESERVATION_CHECKOUT_DATE + " DATE, "
            + RESERVATION_APT_TYPE + " TEXT, "
            + RESERVATION_DAILY_RATE + " DOUBLE, "
            + RESERVATION_OTHER_RATE + " DOUBLE, "
            + RESERVATION_RESERVATION_AMOUNT + " DOUBLE, "
            + RESERVATION_NOTE + " TEXT, "
            + RESERVATION_STATUS_RESERVATION + " INT "
            + ")";

    // Version 85
    String ALTER_TABLE_RESERVATION_V86 = "ALTER TABLE " + RESERVATION_TABLE
            + " ADD COLUMN " + RESERVATION_CURRENCY_TYPE + " INT ";

    String[] RESERVATION_COLUMNS = new String[] {
            RESERVATION_ID,
            RESERVATION_TRAVEL_ID,
            RESERVATION_ACCOMMODATION_ID,
            RESERVATION_VOUCHER_NUMBER,
            RESERVATION_CHECKIN_DATE,
            RESERVATION_CHECKOUT_DATE,
            RESERVATION_APT_TYPE,
            RESERVATION_DAILY_RATE,
            RESERVATION_OTHER_RATE,
            RESERVATION_RESERVATION_AMOUNT,
            RESERVATION_CURRENCY_TYPE,
            RESERVATION_NOTE,
            RESERVATION_STATUS_RESERVATION
    };
}
