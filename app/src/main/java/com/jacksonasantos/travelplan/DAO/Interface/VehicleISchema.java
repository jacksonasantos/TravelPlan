package com.jacksonasantos.travelplan.DAO.Interface;

public interface VehicleISchema {

    String VEHICLE_TABLE = "users";

    String VEHICLE_ID = "_id";
    String VEHICLE_OID = "oid_name";
    String VEHICLE_NAME = "name";
    String VEHICLE_LICENCE_PLATE = "license_plate";
    String VEHICLE_FULL_CAPACITY = "full_capacity";
    String VEHICLE_AVG_CONSUMPTION = "avg_consumption";

    String VEHICLE_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
                                + VEHICLE_TABLE
                                + " (" + VEHICLE_ID + " INTEGER PRIMARY KEY, "
                                + VEHICLE_OID + " TEXT NOT NULL, "
                                + VEHICLE_NAME + " TEXT NOT NULL, "
                                + VEHICLE_LICENCE_PLATE + " TEXT,"
                                + VEHICLE_FULL_CAPACITY + " INT,"
                                + VEHICLE_AVG_CONSUMPTION + "DOUBLE "
                                + ")";

    String[] VEHICLE_COLUMNS = new String[] {VEHICLE_ID, VEHICLE_OID, VEHICLE_NAME, VEHICLE_LICENCE_PLATE, VEHICLE_FULL_CAPACITY, VEHICLE_AVG_CONSUMPTION };
}

