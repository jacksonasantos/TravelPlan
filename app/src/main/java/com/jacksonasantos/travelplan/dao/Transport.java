package com.jacksonasantos.travelplan.dao;

import androidx.annotation.NonNull;

import com.jacksonasantos.travelplan.R;

import java.util.Date;

public class Transport {
    public Integer id;
    public Integer travel_id;
    public int transport_type;
    public String identifier;
    public String description;
    public String company;
    public String contact;
    public String start_location;
    public Date start_date;
    public String end_location;
    public Date end_date;
    public Double service_value;
    public Double service_tax;
    public String note;

    public Transport() { }

    @NonNull
    @Override
    public String toString() {return description;}

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public Integer getTravel_id() {return travel_id;}
    public void setTravel_id(Integer travel_id) {this.travel_id = travel_id;}

    public static int getTransportTypeImage( int tour_type ) {
        int draw;
        switch(tour_type) {
            case 0:
                draw = R.drawable.ic_transport_own;
                break;
            case 1:
                draw = R.drawable.ic_transport_voo;
                break;
            case 2:
                draw = R.drawable.ic_transport_rental;
                break;
            case 3:
                draw = R.drawable.ic_transport_hiring;
                break;
            default:
                draw = R.drawable.ic_error;
                break;
        }
        return draw;
    }
    public int getTransport_type() {return transport_type;}
    public void setTransport_type(int transport_type) {this.transport_type = transport_type;}

    public String getIdentifier() {return identifier;}
    public void setIdentifier(String identifier) {this.identifier = identifier;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getCompany() {return company;}
    public void setCompany(String company) {this.company = company;}

    public String getContact() {return contact;}
    public void setContact(String contact) {this.contact = contact;}

    public String getStart_location() {return start_location;}
    public void setStart_location(String start_location) {this.start_location = start_location;}

    public Date getStart_date() {return start_date;}
    public void setStart_date(Date start_date) {this.start_date = start_date;}

    public String getEnd_location() {return end_location;}
    public void setEnd_location(String end_location) {this.end_location = end_location;}

    public Date getEnd_date() {return end_date;}
    public void setEnd_date(Date end_date) {this.end_date = end_date;}

    public Double getService_value() {return service_value;}
    public void setService_value(Double service_value) {this.service_value = service_value;}

    public Double getService_tax() {return service_tax;}
    public void setService_tax(Double service_tax) {this.service_tax = service_tax;}

    public String getNote() {return note;}
    public void setNote(String note) {this.note = note;}
}