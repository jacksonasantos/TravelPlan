package com.jacksonasantos.travelplan.dao;

import androidx.annotation.NonNull;

import com.jacksonasantos.travelplan.R;

import java.util.Date;

public class Vehicle {
    public long id;
    public int vehicle_type;
    public String name;
    public String short_name;
    public String brand;
    public String model;
    public int fuel_type;

    public String year_model;
    public String year_manufacture;
    public String license_plate;
    public String color;
    public String vin;
    public String licence_number;
    public String state;
    public String city;
    public Date dt_acquisition;
    public Date dt_sale;

    public int doors;
    public int capacity;
    public int power;
    public double estimated_value;
    public int full_capacity;
    public float avg_consumption;
    public Date dt_odometer;
    public int odometer;

    public Vehicle() {
        this.id = id;
        this.vehicle_type = vehicle_type;
        this.name = name;
        this.short_name = short_name;
        this.brand = brand;
        this.model = model;
        this.fuel_type = fuel_type;

        this.year_model = year_model;
        this.year_manufacture = year_manufacture;
        this.license_plate = license_plate;
        this.vin = vin;
        this.licence_number = licence_number;
        this.state = state;
        this.city = city;
        this.color = color;
        this.dt_acquisition = dt_acquisition;
        this.dt_sale = dt_sale;

        this.doors = doors;
        this.capacity = capacity;
        this.power = power;
        this.full_capacity = full_capacity;
        this.avg_consumption = avg_consumption;
        this.dt_odometer = dt_odometer;
        this.odometer = odometer;
        this.estimated_value = estimated_value;
    }
    @NonNull
    @Override
    public String toString() { return name; }

    public long getId() { return id; }
    public void setId(long id) {
        this.id = id;
    }

    public int getVehicleTypeImage( int vehicle_type ) {
        int draw;
        switch(vehicle_type) {
            case 1:
                draw = R.drawable.ic_vehicle_fusca;
                break;
            case 2:
                draw = R.drawable.ic_vehicle_motorcycle;
                break;
            case 3:
                draw = R.drawable.ic_vehicle_suv;
                break;
            default:
                draw = R.drawable.ic_button_error;
                break;
        }
        return draw;
    }
    public int getVehicle_type() { return vehicle_type; }
    public void setVehicle_type(int vehicle_type) {
        this.vehicle_type = vehicle_type;
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

    public int getFuel_type() {
        return fuel_type;
    }
    public void setFuel_type(int fuel_type) {
        this.fuel_type = fuel_type;
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

    public Date getDt_acquisition() {return dt_acquisition;}
    public void setDt_acquisition(Date acquisition) {this.dt_acquisition = acquisition;}

    public Date getDt_sale() {return dt_sale;}
    public void setDt_sale(Date sale) {this.dt_sale = sale;}

    public Date getDt_odometer() {return dt_odometer;}
    public void setDt_odometer(Date dt_odometer) {this.dt_odometer = dt_odometer;}

    public int getOdometer() {return odometer;}
    public void setOdometer(int odometer) {this.odometer = odometer;}

    public String getModel() {return model;}
    public void setModel(String model) {this.model = model;}

    public String getColor() {return color;}
    public void setColor(String color) {this.color = color;}

    public String getYear_model() {return year_model;}
    public void setYear_model(String year_model) {this.year_model = year_model;}

    public String getYear_manufacture() {return year_manufacture;}
    public void setYear_manufacture(String year_manufacture) {this.year_manufacture = year_manufacture;}

    public String getVin() {return vin;}
    public void setVin(String vin) {this.vin = vin;}

    public String getLicence_number() {return licence_number;}
    public void setLicence_number(String licence_number) {this.licence_number = licence_number;}

    public String getState() {return state;}
    public void setState(String state) {this.state = state;}

    public String getCity() {return city;}
    public void setCity(String city) {this.city = city;}

    public int getDoors() {return doors;}
    public void setDoors(int doors) {this.doors = doors;}

    public int getCapacity() {return capacity;}
    public void setCapacity(int capacity) {this.capacity = capacity;}

    public int getPower() {return power;}
    public void setPower(int power) {this.power = power;}

    public double getEstimated_value() {return estimated_value;}
    public void setEstimated_value(double estimated_value) {this.estimated_value = estimated_value;}
}
