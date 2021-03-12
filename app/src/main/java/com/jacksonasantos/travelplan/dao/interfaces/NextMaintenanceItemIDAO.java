package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.NextMaintenanceItem;

import java.util.List;

public interface NextMaintenanceItemIDAO {
    List<NextMaintenanceItem> findNextMaintenanceItem(Integer vehicle_id);
}