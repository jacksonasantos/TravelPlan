package com.jacksonasantos.travelplan.dao;

import com.jacksonasantos.travelplan.R;

public class MaintenanceItem {
    public Integer id;
    public Integer maintenance_plan_id;
    public Integer maintenance_id;
    public int service_type;
    public int measure_type;
    public int expiration_value;
    public Double value;
    public String note;

    public MaintenanceItem() {
        this.id = id;
        this.maintenance_plan_id=maintenance_plan_id;
        this.maintenance_id=maintenance_id;
        this.service_type=service_type;
        this.measure_type=measure_type;
        this.expiration_value=expiration_value;
        this.value=value;
        this.note=note;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getMaintenance_plan_id() { return maintenance_plan_id; }
    public void setMaintenance_plan_id(Integer maintenance_plan_id) { this.maintenance_plan_id = maintenance_plan_id; }

    public Integer getMaintenance_id() { return maintenance_id; }
    public void setMaintenance_id(Integer maintenance_id) { this.maintenance_id = maintenance_id; }

    public int getServiceTypeImage( int service_type ) {
        int draw;
        switch(service_type) {
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
            case 14: draw = R.drawable.ic_service_overhaul; break;
            case 15: draw = R.drawable.ic_service_tire; break;
            case 16: draw = R.drawable.ic_service_radiator; break;
            case 17: draw = R.drawable.ic_service_suspension; break;
            case 18: draw = R.drawable.ic_service_transmission; break;
            case 19: draw = R.drawable.ic_service_parts_exchange; break;
            case 20: draw = R.drawable.ic_service_oil_change; break;
            case 21: draw = R.drawable.ic_service_spark_plug; break;
            case 22: draw = R.drawable.ic_accessory; break;
            default: draw = R.drawable.ic_error; break;
        }
        return draw;
    }

    public int getService_type() { return service_type; }
    public void setService_type(int service_type) { this.service_type = service_type; }

    public int getMeasure_type() { return measure_type; }
    public void setMeasure_type(int measure_type) { this.measure_type = measure_type; }

    public int getExpiration_value() { return expiration_value; }
    public void setExpiration_value(int expiration_value) { this.expiration_value = expiration_value; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
