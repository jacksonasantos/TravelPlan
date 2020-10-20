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
            case 0: draw = R.drawable.ic_service_alignment; break;
            case 1: draw = R.drawable.ic_service_balancing; break;
            case 2: draw = R.drawable.ic_service_battery; break;
            case 3: draw = R.drawable.ic_service_belt; break;
            case 4: draw = R.drawable.ic_service_clutch; break;
            case 5: draw = R.drawable.ic_service_exhaust; break;
            case 6: draw = R.drawable.ic_service_extinguisher; break;
            case 7: draw = R.drawable.ic_service_lighthouse_flashlight; break;
            case 8: draw = R.drawable.ic_service_air_filter; break;
            case 9: draw = R.drawable.ic_service_fuel_filter; break;
            case 10: draw = R.drawable.ic_service_oil_filter; break;
            case 11: draw = R.drawable.ic_service_brake; break;
            case 12: draw = R.drawable.ic_service_injection_carburetor; break;
            case 13: draw = R.drawable.ic_service_other_services; break;
            case 14: draw = R.drawable.ic_service_tire; break;
            case 15: draw = R.drawable.ic_service_radiator; break;
            case 16: draw = R.drawable.ic_service_suspension; break;
            case 17: draw = R.drawable.ic_service_transmission; break;
            case 18: draw = R.drawable.ic_service_parts_exchange; break;
            case 19: draw = R.drawable.ic_service_oil_change; break;
            case 20: draw = R.drawable.ic_service_spark_plug; break;
            default: draw = R.drawable.ic_error; break;
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
