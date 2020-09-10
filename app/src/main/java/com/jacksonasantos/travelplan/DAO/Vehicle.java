package com.jacksonasantos.travelplan.DAO;

import java.sql.Date;

public class Vehicle {
    public Long id;
    public int type;
    public String name;
    public String short_name;
    public String license_plate;
    public int full_capacity;
    public Double avg_consumption;
    public String brand;
    public String type_fuel;
    public Long dt_acquisition;
    public Long dt_sale;
    public Long dt_odometer;
    public int odometer;

    public Vehicle() {
        this.id = id;
        this.type = type;
        this.name = name;
        this.short_name = short_name;
        this.license_plate = license_plate;
        this.full_capacity = full_capacity;
        this.avg_consumption = avg_consumption;
        this.brand = brand;
        this.type_fuel = type_fuel;
        this.dt_acquisition = dt_acquisition;
        this.dt_sale = dt_sale;
        this.dt_odometer = dt_odometer;
        this.odometer = odometer;

    }

    public long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() { return type; }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) { this.license_plate = license_plate; }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType_fuel() {
        return type_fuel;
    }

    public void setType_fuel(String type_fuel) {
        this.type_fuel = type_fuel;
    }

    public int getFull_capacity() {
        return full_capacity;
    }

    public void setFull_capacity(int full_capacity) {
        this.full_capacity = full_capacity;
    }

    public Double getAvg_consumption() {
        return avg_consumption;
    }

    public void setAvg_consumption(Double avg_consumption) { this.avg_consumption = avg_consumption; }

    public String getShort_name() {return short_name;}

    public void setShort_name(String short_name) {this.short_name = short_name; }

    public Long getDt_acquisition() {return dt_acquisition;}

    public void setDt_acquisition(Long acquisition) {this.dt_acquisition = acquisition;}

    public Long getDt_sale() {return dt_sale;}

    public void setDt_sale(Long sale) {this.dt_sale = sale;}

    public Long getDt_odometer() {return dt_odometer;}

    public void setDt_odometer(Long dt_odometer) {this.dt_odometer = dt_odometer;}

    public int getOdometer() {return odometer;}

    public void setOdometer(int odometer) {this.odometer = odometer;}
}
