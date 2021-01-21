package com.jacksonasantos.travelplan.dao;

import android.graphics.Color;

import java.util.Date;

public class VehicleStatistics {
    public Integer id;
    public Integer vehicle_id;
    public int supply_reason_type;
    public Date statistic_date;
    public float avg_consumption;
    public float avg_cost_litre;

    public VehicleStatistics() {
        this.id = id;
        this.vehicle_id = vehicle_id;
        this.supply_reason_type = supply_reason_type;
        this.statistic_date = statistic_date;
        this.avg_consumption = avg_consumption;
        this.avg_cost_litre = avg_cost_litre;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getVehicle_id() { return vehicle_id; }
    public void setVehicle_id(Integer vehicle_id) { this.vehicle_id = vehicle_id; }

    public Date getStatistic_date() { return statistic_date; }
    public void setStatistic_date(Date statistic_date) { this.statistic_date = statistic_date; }

    public int getSupply_reason_type() { return supply_reason_type; }
    public void setSupply_reason_type(int supply_reason_type) { this.supply_reason_type = supply_reason_type; }

    public static int getSupply_ranson_type_color(int type) {
        switch (type) {
            case 1:
                return Color.RED;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.MAGENTA;
            default:
                return Color.GRAY;
        }
    }

    public float getAvg_consumption() { return avg_consumption; }
    public void setAvg_consumption(float avg_consumption) { this.avg_consumption = avg_consumption; }

    public float getAvg_cost_litre() { return avg_cost_litre; }
    public void setAvg_cost_litre(float avg_cost_litre) { this.avg_cost_litre = avg_cost_litre; }
}
