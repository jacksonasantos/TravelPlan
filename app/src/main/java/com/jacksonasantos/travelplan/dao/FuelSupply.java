package com.jacksonasantos.travelplan.dao;

import java.util.Date;

public class FuelSupply {
    public Integer id;
    public Integer vehicle_id;
    public Integer transport_id;
    public String gas_station;
    public String gas_station_location;
    public Date supply_date;
    public double number_liters;
    public double accumulated_number_liters;
    public double accumulated_supply_value;
    public int combustible;
    public int full_tank;
    public int currency_type;
    public Integer currency_quote_id;
    public double supply_value;
    public double fuel_value;
    public int vehicle_odometer;
    public int vehicle_travelled_distance;
    public float stat_avg_fuel_consumption;
    public float stat_cost_per_litre;
    public int supply_reason_type;
    public String supply_reason;
    public Integer associated_travel_id;
    public Integer account_id;

    public FuelSupply() { }

    public Integer getId() { return id;}
    public void setId(Integer id) { this.id = id;}

    public Integer getVehicle_id() {return vehicle_id;}
    public void setVehicle_id(Integer vehicle_id) {this.vehicle_id = vehicle_id;}

    public Integer getTransport_id() {return transport_id;}
    public void setTransport_id(Integer transport_id) {this.transport_id = transport_id;}

    public String getGas_station() {return gas_station;}
    public void setGas_station(String gas_station) {this.gas_station = gas_station; }

    public String getGas_station_location() {return gas_station_location;}
    public void setGas_station_location(String gas_station_location) {this.gas_station_location = gas_station_location; }

    public Date getSupply_date() { return supply_date;}
    public void setSupply_date(Date supply_date) { this.supply_date = supply_date;}

    public double getNumber_liters() {return number_liters;}
    public void setNumber_liters(double number_liters) {this.number_liters = number_liters;}

    public double getAccumulated_number_liters() {return accumulated_number_liters;}
    public void setAccumulated_Number_liters(double accumulated_number_liters) {this.accumulated_number_liters = accumulated_number_liters;}

    public double getAccumulated_supply_value() {return accumulated_supply_value;}
    public void setAccumulated_supply_value(double accumulated_supply_value) {this.accumulated_supply_value = accumulated_supply_value;}

    public int getCombustible() { return combustible; }
    public void setCombustible(int combustible) {this.combustible = combustible; }

    public int getFull_tank() {return full_tank;}
    public void setFull_tank(int full_tank) {this.full_tank = full_tank;}

    public int getCurrency_type() {return currency_type;}
    public void setCurrency_type(int currency_type) {this.currency_type = currency_type;}

    public Integer getCurrency_quote_id() {return currency_quote_id;}
    public void setCurrency_quote_id(Integer currency_quote_id) {this.currency_quote_id = currency_quote_id;}

    public double getSupply_value() {return supply_value;}
    public void setSupply_value(double supply_value) {this.supply_value = supply_value;}

    public double getFuel_value() {return fuel_value;}
    public void setFuel_value(double fuel_value) {this.fuel_value = fuel_value; }

    public int getVehicle_odometer() {return vehicle_odometer;}
    public void setVehicle_odometer(int vehicle_odometer) {this.vehicle_odometer = vehicle_odometer;}

    public int getVehicle_travelled_distance() {return vehicle_travelled_distance;}
    public void setVehicle_travelled_distance(int vehicle_travelled_distance) {this.vehicle_travelled_distance = vehicle_travelled_distance;}

    public float getStat_avg_fuel_consumption() {return stat_avg_fuel_consumption; }
    public void setStat_avg_fuel_consumption(float stat_avg_fuel_consumption) { this.stat_avg_fuel_consumption = stat_avg_fuel_consumption; }

    public float getStat_cost_per_litre() {return stat_cost_per_litre; }
    public void setStat_cost_per_litre(float stat_cost_per_litre) { this.stat_cost_per_litre = stat_cost_per_litre; }

    public String getSupply_reason() {return supply_reason; }
    public void setSupply_reason(String supply_reason) { this.supply_reason = supply_reason; }

    public int getSupply_reason_type() {return supply_reason_type; }
    public void setSupply_reason_type(int supply_reason_type) { this.supply_reason_type = supply_reason_type; }

    public Integer getAssociated_travel_id() {return associated_travel_id;}
    public void setAssociated_travel_id(Integer associated_travel_id) {this.associated_travel_id = associated_travel_id; }

    public Integer getAccount_id() {return account_id;}
    public void setAccount_id(Integer account_id) {this.account_id = account_id; }
}