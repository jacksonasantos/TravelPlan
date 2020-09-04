package com.jacksonasantos.travelplan.DAO;

public class Vehicle {
    public String id;
    public String name;
    public String license_plate;
    public int full_capacity;
    public double avg_consumption;
    public String brand;
    public String type_fuel;

    public Vehicle() {
        this.id = id;
        this.name = name;
        this.license_plate = license_plate;
        this.full_capacity = full_capacity;
        this.avg_consumption = avg_consumption;
        this.brand = brand;
        this.type_fuel = type_fuel;
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

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

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

    public double getAvg_consumption() {
        return avg_consumption;
    }

    public void setAvg_consumption(double avg_consumption) {
        this.avg_consumption = avg_consumption;
    }
}
