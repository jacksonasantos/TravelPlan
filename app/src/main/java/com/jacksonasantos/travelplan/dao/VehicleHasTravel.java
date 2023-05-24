package com.jacksonasantos.travelplan.dao;

public class VehicleHasTravel {
    public Integer id;
    public Integer vehicle_id;
    public Integer travel_id;
    public Integer transport_id;
    public Integer person_id;
    public int start_odometer;
    public int final_odometer;
    public float avg_consumption;
    public float avg_cost_litre;
    public int last_odometer;

    public VehicleHasTravel() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getVehicle_id() { return vehicle_id; }
    public void setVehicle_id(Integer vehicle_id) { this.vehicle_id = vehicle_id; }

    public Integer getTravel_id() { return travel_id; }
    public void setTravel_id(Integer travel_id) { this.travel_id = travel_id; }

    public Integer getTransport_id() { return transport_id; }
    public void setTransport_id(Integer transport_id) { this.transport_id = transport_id; }

    public Integer getPerson_id() { return person_id; }
    public void setPerson_id(Integer person_id) { this.person_id = person_id; }

    public int getStart_odometer() { return start_odometer; }
    public void setStart_odometer(int start_odometer) { this.start_odometer = start_odometer; }

    public int getFinal_odometer() { return final_odometer; }
    public void setFinal_odometer(int final_odometer) { this.final_odometer = final_odometer; }

    public int getLast_odometer() { return last_odometer; }
    public void setLast_odometer(int last_odometer) { this.last_odometer = last_odometer; }

    public float getAvg_consumption() {return avg_consumption;}
    public void setAvg_consumption(float avg_consumption) {this.avg_consumption = avg_consumption;}

    public float getAvg_cost_litre() {return avg_cost_litre;}
    public void setAvg_cost_litre(float avg_cost_litre) {this.avg_cost_litre = avg_cost_litre;}
}
