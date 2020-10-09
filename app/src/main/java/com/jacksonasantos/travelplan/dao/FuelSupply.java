package com.jacksonasantos.travelplan.dao;

import java.util.Date;

public class FuelSupply {
    public Long id;
    public Long vehicle_id=0L;
    public String gas_station;
    public String gas_station_location;
    public Date supply_date;
    public Double number_liters;
    public int combustible;
    public int full_tank;
    public int currency_type;
    public Double supply_value;
    public Double fuel_value;
    public int vehicle_odometer;
    public int vehicle_travelled_distance;
    public Double stat_avg_fuel_consumption;
    public Double stat_cost_per_litre;
    public int supply_reason_type;
    public String supply_reason;
    public Long associated_trip;

    public FuelSupply() {
        this.id = id;
        this.vehicle_id=vehicle_id;
        this.gas_station=gas_station;
        this.gas_station_location=gas_station_location;
        this.supply_date=supply_date;
        this.number_liters=number_liters;
        this.combustible=combustible;
        this.full_tank=full_tank;
        this.currency_type=currency_type;
        this.supply_value=supply_value;
        this.fuel_value=fuel_value;
        this.vehicle_odometer=vehicle_odometer;
        this.vehicle_travelled_distance=vehicle_travelled_distance;
        this.stat_avg_fuel_consumption=stat_avg_fuel_consumption;
        this.stat_cost_per_litre=stat_cost_per_litre;
        this.supply_reason_type = supply_reason_type;
        this.supply_reason=supply_reason;
        this.associated_trip=associated_trip;
    }

    public long getId() { return id;}

    public void setId(long id) { this.id = id;}

    public long getVehicle_id() {return vehicle_id;}

    public void setVehicle_id(long vehicle_id) {this.vehicle_id = vehicle_id;}

    public String getGas_station() {return gas_station;}

    public void setGas_station(String gas_station) {this.gas_station = gas_station; }

    public String getGas_station_location() {return gas_station_location;}

    public void setGas_station_location(String gas_station_location) {this.gas_station_location = gas_station_location; }

    public Date getSupply_date() { return supply_date;}

    public void setSupply_date(Date supply_date) { this.supply_date = supply_date;}

    public Double getNumber_liters() {return number_liters;}

    public void setNumber_liters(Double number_liters) {this.number_liters = number_liters;}

    public int getCombustible() { return combustible; }

    public void setCombustible(int combustible) {this.combustible = combustible; }

    public int getFull_tank() {return full_tank;}

    public void setFull_tank(int full_tank) {this.full_tank = full_tank;}

    public int getCurrency_type() {return currency_type;}

    public void setCurrency_type(int currency_type) {this.currency_type = currency_type;}

    public Double getSupply_value() {return supply_value;}

    public void setSupply_value(Double supply_value) {this.supply_value = supply_value;}

    public Double getFuel_value() {return fuel_value;}

    public void setFuel_value(Double fuel_value) {this.fuel_value = fuel_value; }

    public int getVehicle_odometer() {return vehicle_odometer;}

    public void setVehicle_odometer(int vehicle_odometer) {this.vehicle_odometer = vehicle_odometer;}

    public int getVehicle_travelled_distance() {return vehicle_travelled_distance;}

    public void setVehicle_travelled_distance(int vehicle_travelled_distance) {this.vehicle_travelled_distance = vehicle_travelled_distance;}

    public Double getStat_avg_fuel_consumption() {return stat_avg_fuel_consumption; }

    public void setStat_avg_fuel_consumption(Double stat_avg_fuel_consumption) { this.stat_avg_fuel_consumption = stat_avg_fuel_consumption; }

    public Double getStat_cost_per_litre() {return stat_cost_per_litre; }

    public void setStat_cost_per_litre(Double stat_cost_per_litre) { this.stat_cost_per_litre = stat_cost_per_litre; }

    public String getSupply_reason() {return supply_reason; }

    public void setSupply_reason(String supply_reason) { this.supply_reason = supply_reason; }

    public int getSupply_reason_type() {return supply_reason_type; }

    public void setSupply_reason_type(int supply_reason_type) { this.supply_reason_type = supply_reason_type; }

    public Long getAssociated_trip() {return associated_trip;}

    public void setAssociated_trip(Long associated_trip) {this.associated_trip = associated_trip; }
}
