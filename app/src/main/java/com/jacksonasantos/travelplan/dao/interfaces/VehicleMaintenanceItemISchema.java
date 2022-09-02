package com.jacksonasantos.travelplan.dao.interfaces;

public interface VehicleMaintenanceItemISchema {

    String VEHICLE_MAINTENANCE_ITEM_VEHICLE_ID = "vehicle_id";
    String VEHICLE_MAINTENANCE_ITEM_SERVICE_TYPE = "service_type";
    String VEHICLE_MAINTENANCE_ITEM_DATE = "date";
    String VEHICLE_MAINTENANCE_ITEM_ODOMETER = "odometer";
    String VEHICLE_MAINTENANCE_ITEM_DESCRIPTION = "description";
    String VEHICLE_MAINTENANCE_ITEM_NOTE = "note";

    String[] VEHICLE_MAINTENANCE_ITEM_COLUMNS = new String[] {
            VEHICLE_MAINTENANCE_ITEM_VEHICLE_ID,
            VEHICLE_MAINTENANCE_ITEM_SERVICE_TYPE,
            VEHICLE_MAINTENANCE_ITEM_DATE,
            VEHICLE_MAINTENANCE_ITEM_ODOMETER,
            VEHICLE_MAINTENANCE_ITEM_DESCRIPTION,
            VEHICLE_MAINTENANCE_ITEM_NOTE
    };
}
