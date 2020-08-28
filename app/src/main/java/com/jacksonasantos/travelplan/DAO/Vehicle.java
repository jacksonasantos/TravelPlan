package com.jacksonasantos.travelplan.DAO;

import java.io.Serializable;

public class Vehicle implements Serializable {
    private static final long serialVersionUID = 163383301108440384L;
    public int id;
    public String oid_vehicle;
    public String name_vehicle;
    public String license_plate;
    public int full_capacity;
    public double avg_consumption;
}
