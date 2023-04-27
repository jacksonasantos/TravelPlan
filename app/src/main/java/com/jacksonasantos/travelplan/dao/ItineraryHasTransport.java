package com.jacksonasantos.travelplan.dao;

public class ItineraryHasTransport {
    public Integer id;
    public Integer travel_id;
    public Integer transport_id;
    public Integer vehicle_id;
    public Integer itinerary_id;
    public Integer person_id;
    public int transport_type;
    public int driver;
    public int sequence_itinerary;

    public ItineraryHasTransport() {
    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public Integer getTravel_id() { return travel_id; }
    public void setTravel_id(Integer travel_id) { this.travel_id = travel_id; }

    public Integer getTransport_id() { return transport_id; }
    public void setTransport_id(Integer transport_id) { this.transport_id = transport_id; }

    public Integer getVehicle_id() { return vehicle_id; }
    public void setVehicle_id(Integer vehicle_id) { this.vehicle_id = vehicle_id; }

    public Integer getItinerary_id() { return itinerary_id; }
    public void setItinerary_id(Integer itinerary_id) { this.itinerary_id = itinerary_id; }

    public Integer getPerson_id() { return person_id; }
    public void setPerson_id(Integer person_id) { this.person_id = person_id; }

    public int getTransport_type() { return transport_type; }
    public void setTransport_type(int transport_type) { this.transport_type = transport_type; }

    public int getDriver() { return driver; }
    public void setDriver(int driver) { this.driver = driver; }

    public int getSequence_itinerary() { return sequence_itinerary; }
    public void setSequence_itinerary(int sequence_itinerary) { this.sequence_itinerary = sequence_itinerary; }
}
