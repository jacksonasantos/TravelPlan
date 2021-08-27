package com.jacksonasantos.travelplan.dao;

public class VehicleStatisticsPeriod {
    public Integer vehicle_id;
    public String short_name;
    public String statistic_period;
    public float avg_consumption;

    public VehicleStatisticsPeriod() {
        this.vehicle_id = vehicle_id;
        this.short_name = short_name;
        this.statistic_period = statistic_period;
        this.avg_consumption = avg_consumption;
    }

    public Integer getVehicle_id() { return vehicle_id; }
    public void setVehicle_id(Integer vehicle_id) { this.vehicle_id = vehicle_id; }

    public String getShort_name() { return short_name; }
    public void setShort_name(String short_name) { this.short_name = short_name; }

    public String getStatistic_period_month() { return statistic_period.substring(5,7); }
    public String getStatistic_period_year() { return statistic_period.substring(0,4); }
    public String getStatistic_period() { return statistic_period; }
    public void setStatistic_period(String statistic_period) { this.statistic_period = statistic_period; }

    public float getAvg_consumption() { return avg_consumption; }
    public void setAvg_consumption(float avg_consumption) { this.avg_consumption = avg_consumption; }

}
