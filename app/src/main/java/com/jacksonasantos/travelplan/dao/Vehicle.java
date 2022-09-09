package com.jacksonasantos.travelplan.dao;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.Date;

public class Vehicle {
    public Integer id;
    public byte[] image;
    public int vehicle_type;
    public String name;
    public String short_name;
    public String brand;
    public String model;
    public int fuel_type;

    public Integer owner_driver_id;
    public String year_model;
    public String year_manufacture;
    public String license_plate;
    public String color;
    public int color_code;
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
    public float avg_cost_litre;
    public Date dt_odometer;
    public int odometer;

    public Date dt_last_fueling;
    public int last_supply_reason_type;
    public double accumulated_number_liters;
    public double accumulated_supply_value;

    public Vehicle() {
    }
    @NonNull
    @Override
    public String toString() { return name; }

    public Integer getId() { return id; }
    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getImage() {return image;}
    public void setImage(byte[] image) {this.image = image;}

    public static int getVehicle_color(String color_code) {
        color_code = color_code==null ? "000000" : color_code;
        int[] x = Utils.getRGB(color_code);
        return Color.rgb(x[0],x[1],x[2]);
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
                draw = R.drawable.ic_error;
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

    public Integer getOwner_driver_id() {
        return owner_driver_id;
    }
    public void setOwner_driver_id(Integer owner_driver_id) { this.owner_driver_id = owner_driver_id; }

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

    public float getAvg_consumption() {return avg_consumption; }
    public void setAvg_consumption(float avg_consumption) { this.avg_consumption = avg_consumption; }

    public float getAvg_cost_litre() {return avg_cost_litre; }
    public void setAvg_cost_litre(float avg_cost_litre) { this.avg_cost_litre = avg_cost_litre; }

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

    public int getColor_code() {return color_code;}
    public void setColor_code(int color_code) {this.color_code = color_code;}

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

    public Date getDt_last_fueling() { return dt_last_fueling; }
    public void setDt_last_fueling(Date dt_last_fueling) { this.dt_last_fueling = dt_last_fueling; }

    public int getLast_supply_reason_type() { return last_supply_reason_type; }
    public void setLast_supply_reason_type(int last_supply_reason_type) {this.last_supply_reason_type = last_supply_reason_type;}

    public double getAccumulated_number_liters() { return accumulated_number_liters; }
    public void setAccumulated_number_liters(double accumulated_number_liters) {this.accumulated_number_liters = accumulated_number_liters;}

    public double getAccumulated_supply_value() { return accumulated_supply_value; }
    public void setAccumulated_supply_value(double accumulated_supply_value) {this.accumulated_supply_value = accumulated_supply_value;}
}
