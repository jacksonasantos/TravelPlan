package com.jacksonasantos.travelplan.dao;

import com.jacksonasantos.travelplan.R;

import java.util.Date;

public class Insurance {
    public Integer id;
    public Integer insurance_company_id;
    public Integer broker_id;
    public int insurance_type;
    public String description;
    public String insurance_policy;
    public Date issuance_date;
    public Date initial_effective_date;
    public Date final_effective_date;
    public double net_premium_value;
    public double tax_amount;
    public double total_premium_value;
    public double insurance_deductible;
    public int bonus_class;
    public String note;
    public int status;
    public Integer travel_id;
    public Integer vehicle_id;

    public Insurance() {
        this.id = id;
        this.insurance_company_id = insurance_company_id;
        this.broker_id = broker_id;
        this.insurance_type = insurance_type;
        this.description = description;
        this.insurance_policy = insurance_policy;
        this.issuance_date = issuance_date;
        this.initial_effective_date = initial_effective_date;
        this.final_effective_date = final_effective_date;
        this.net_premium_value = net_premium_value;
        this.tax_amount = tax_amount;
        this.total_premium_value = total_premium_value;
        this.insurance_deductible = insurance_deductible;
        this.bonus_class = bonus_class;
        this.note = note;
        this.status = status;
        this.travel_id = travel_id;
        this.vehicle_id = vehicle_id;
    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public Integer getInsurance_company_id() { return insurance_company_id; }
    public void setInsurance_company_id(Integer insurance_company_id) { this.insurance_company_id = insurance_company_id; }

    public Integer getBroker_id() { return broker_id; }
    public void setBroker_id(Integer broker_id) { this.broker_id = broker_id; }

    public int getInsurance_typeImage( int insurance_type ) {
        int draw;
        switch(insurance_type) {
            case 1: draw = R.drawable.ic_insurance_vehicle; break;
            case 2: draw = R.drawable.ic_insurance_life; break;
            case 3: draw = R.drawable.ic_insurance_property; break;
            case 4: draw = R.drawable.ic_insurance_travel; break;
            default: draw = R.drawable.ic_error; break;
        }
        return draw;
    }

    public int getInsurance_type() { return insurance_type; }
    public void setInsurance_type(int insurance_type) { this.insurance_type = insurance_type; }

    public String getInsurance_policy() { return insurance_policy; }
    public void setInsurance_policy(String insurance_policy) { this.insurance_policy = insurance_policy; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getIssuance_date() { return issuance_date; }
    public void setIssuance_date(Date issuance_date) { this.issuance_date = issuance_date; }

    public Date getInitial_effective_date() { return initial_effective_date; }
    public void setInitial_effective_date(Date initial_effective_date) { this.initial_effective_date = initial_effective_date; }

    public Date getFinal_effective_date() { return final_effective_date; }
    public void setFinal_effective_date(Date final_effective_date) { this.final_effective_date = final_effective_date; }

    public double getNet_premium_value() { return net_premium_value; }
    public void setNet_premium_value(double net_premium_value) { this.net_premium_value = net_premium_value; }

    public double getTax_amount() { return tax_amount; }
    public void setTax_amount(double tax_amount) { this.tax_amount = tax_amount; }

    public double getTotal_premium_value() { return total_premium_value; }
    public void setTotal_premium_value(double total_premium_value) { this.total_premium_value = total_premium_value; }

    public double getInsurance_deductible() { return insurance_deductible; }
    public void setInsurance_deductible(double insurance_deductible) { this.insurance_deductible = insurance_deductible; }

    public int getBonus_class() { return bonus_class; }
    public void setBonus_class(int bonus_class) { this.bonus_class = bonus_class; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public Integer getTravel_id() { return travel_id; }
    public void setTravel_id(Integer travel_id) { this.travel_id = travel_id; }

    public Integer getVehicle_id() { return vehicle_id; }
    public void setVehicle_id(Integer vehicle_id) { this.vehicle_id = vehicle_id; }

}
