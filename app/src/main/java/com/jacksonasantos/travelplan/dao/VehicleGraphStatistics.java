package com.jacksonasantos.travelplan.dao;

import java.util.Date;

public class VehicleGraphStatistics {
    public Integer vehicle_id;
    public int supply_reason_type;
    public Date xaxis_date;
    public Integer yaxis_avg_consumption;
    public Integer yaxis_avg_cost_litre;

    public VehicleGraphStatistics() {
        this.vehicle_id = vehicle_id;
        this.supply_reason_type = supply_reason_type;
        this.xaxis_date = xaxis_date;
        this.yaxis_avg_consumption = yaxis_avg_consumption;
        this.yaxis_avg_cost_litre = yaxis_avg_cost_litre;
    }

    public Integer getVehicle_id() { return vehicle_id; }
    public void setVehicle_id(Integer vehicle_id) { this.vehicle_id = vehicle_id; }

    public int getSupply_reason_type() { return supply_reason_type; }
    public void setSupply_reason_type(int supply_reason_type) { this.supply_reason_type = supply_reason_type; }

    public Date getXaxis_date() { return xaxis_date; }
    public void setXaxis_date(Date xaxis_date) { this.xaxis_date = xaxis_date; }

    public Integer getYaxis_avg_consumption() { return yaxis_avg_consumption; }
    public void setYaxis_avg_consumption(Integer yaxis_avg_consumption) { this.yaxis_avg_consumption = yaxis_avg_consumption; }

    public Integer getYaxis_avg_cost_litre() { return yaxis_avg_cost_litre; }
    public void setYaxis_avg_cost_litre(Integer yaxis_avg_cost_litre) { this.yaxis_avg_cost_litre = yaxis_avg_cost_litre; }
}
