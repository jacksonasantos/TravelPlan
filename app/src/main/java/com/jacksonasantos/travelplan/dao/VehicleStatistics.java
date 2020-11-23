package com.jacksonasantos.travelplan.dao;

import java.util.Date;

public class VehicleStatistics {
    public long id;
    public long vehicle_id;
    public int supply_reason_type;
    public Date statistic_date;
    public float avg_consumption;

    public VehicleStatistics() {
        this.id = id;
        this.vehicle_id = vehicle_id;
        this.supply_reason_type = supply_reason_type;
        this.statistic_date = statistic_date;
        this.avg_consumption = avg_consumption;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getVehicle_id() { return vehicle_id; }
    public void setVehicle_id(long vehicle_id) { this.vehicle_id = vehicle_id; }

    public Date getStatistic_date() { return statistic_date; }
    public void setStatistic_date(Date statistic_date) { this.statistic_date = statistic_date; }

    public int getSupply_reason_type() { return supply_reason_type; }
    public void setSupply_reason_type(int supply_reason_type) { this.supply_reason_type = supply_reason_type; }

    public float getAvg_consumption() { return avg_consumption; }
    public void setAvg_consumption(float avg_consumption) { this.avg_consumption = avg_consumption; }
}