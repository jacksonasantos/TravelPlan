package com.jacksonasantos.travelplan.dao.interfaces;

public interface NextMaintenanceItemISchema {


    String NEXT_MAINTENANCE_ITEM_SERVICE_ITEM = "service_type";
    String NEXT_MAINTENANCE_ITEM_MAINTENANCE_PLAN_ID = "maintenance_plan_id";
    String NEXT_MAINTENANCE_ITEM_DESCRIPTION = "description";
    String NEXT_MAINTENANCE_ITEM_MEASURE = "measure";
    String NEXT_MAINTENANCE_ITEM_NEXT_SERVICE = "next_service";

    String[] NEXT_MAINTENANCE_ITEM_COLUMNS = new String[] {
            NEXT_MAINTENANCE_ITEM_SERVICE_ITEM,
            NEXT_MAINTENANCE_ITEM_MAINTENANCE_PLAN_ID,
            NEXT_MAINTENANCE_ITEM_DESCRIPTION,
            NEXT_MAINTENANCE_ITEM_MEASURE,
            NEXT_MAINTENANCE_ITEM_NEXT_SERVICE
    };
}
