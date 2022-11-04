package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.PendingVehicle;

import java.util.ArrayList;
import java.util.List;

public interface PendingVehicleIDAO {
    ArrayList<PendingVehicle> fetchArrayPendingVehicle();
    PendingVehicle fetchPendingVehicleById(Integer id);
    PendingVehicle fetchPendingVehicleByService(int type);
    List<PendingVehicle> fetchAllPendingVehicle();
    List<PendingVehicle> fetchAllPendingVehicle(Integer vehicle_id, int status);
    boolean addPendingVehicle(PendingVehicle pendingVehicle);
    void deletePendingVehicle(Integer id);
    boolean updatePendingVehicle(PendingVehicle pendingVehicle);
}