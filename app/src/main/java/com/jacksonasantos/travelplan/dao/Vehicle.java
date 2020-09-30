package com.jacksonasantos.travelplan.dao;

import androidx.annotation.NonNull;

import com.jacksonasantos.travelplan.R;

public class Vehicle {
    public long id;
    public int type;
    public String name;
    public String short_name;
    public String license_plate;
    public int full_capacity;
    public float avg_consumption;
    public String brand;
    public String type_fuel;
    public String dt_acquisition;
    public String dt_sale;
    public String dt_odometer;
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
    @NonNull
    @Override
    public String toString() { return name; }

    public long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTypeImage( int type ) {
        int draw;
        switch(type) {
            case 1:
                draw = R.drawable.ic_vehicle_car;
                break;
            case 2:
                draw = R.drawable.ic_vehicle_motorcycle;
                break;
            case 3:
                draw = R.drawable.ic_vehicle_suv;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return draw;
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

    public float getAvg_consumption() {
        return avg_consumption;
    }

    public void setAvg_consumption(float avg_consumption) { this.avg_consumption = avg_consumption; }

    public String getShort_name() {return short_name;}

    public void setShort_name(String short_name) {this.short_name = short_name; }

    public String getDt_acquisition() {return dt_acquisition;}

    public void setDt_acquisition(String acquisition) {this.dt_acquisition = acquisition;}

    public String getDt_sale() {return dt_sale;}

    public void setDt_sale(String sale) {this.dt_sale = sale;}

    public String getDt_odometer() {return dt_odometer;}

    public void setDt_odometer(String dt_odometer) {this.dt_odometer = dt_odometer;}

    public int getOdometer() {return odometer;}

    public void setOdometer(int odometer) {this.odometer = odometer;}
}
