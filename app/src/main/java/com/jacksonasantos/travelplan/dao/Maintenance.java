package com.jacksonasantos.travelplan.dao;

import com.jacksonasantos.travelplan.R;

import java.util.Date;

public class Maintenance {
    public Long id;
    public Long vehicle_id=0L;
    public int type;
    public String detail;
    public Date date;
    public Date expiration_date;
    public int expiration_km;
    public int odometer;
    public Double value;
    public String location;
    public String note;

    public Maintenance() {
        this.id = id;
        this.vehicle_id=vehicle_id;
        this.type=type;
        this.detail=detail;
        this.date=date;
        this.expiration_date=expiration_date;
        this.expiration_km=expiration_km;
        this.odometer=odometer;
        this.value=value;
        this.location=location;
        this.note=note;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getVehicle_id() { return vehicle_id; }

    public void setVehicle_id(Long vehicle_id) { this.vehicle_id = vehicle_id; }

    public int getTypeImage( int type ) {
        int draw;
        switch(type) {
            case 6:
                draw = R.drawable.ic_service_extinguisher;
                break;
            default:
                draw = R.drawable.ic_error;
                break;
        }
        return draw;
    }

    public int getType() { return type; }

    public void setType(int type) { this.type = type; }

    public String getDetail() { return detail; }

    public void setDetail(String detail) { this.detail = detail; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public Date getExpiration_date() { return expiration_date; }

    public void setExpiration_date(Date expiration_date) { this.expiration_date = expiration_date; }

    public int getExpiration_km() { return expiration_km; }

    public void setExpiration_km(int expiration_km) { this.expiration_km = expiration_km; }

    public int getOdometer() { return odometer; }

    public void setOdometer(int odometer) { this.odometer = odometer; }

    public Double getValue() { return value; }

    public void setValue(Double value) { this.value = value; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public String getNote() { return note; }

    public void setNote(String note) { this.note = note; }
}
