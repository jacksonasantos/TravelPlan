package com.jacksonasantos.travelplan.dao;

public class InsuranceContact {
    public Integer id;
    public Integer insurance_id;
    public String type_contact;
    public String description_contact;
    public String detail_contact;

    public InsuranceContact() {
        this.id = id;
        this.insurance_id = insurance_id;
        this.type_contact = type_contact;
        this.description_contact = description_contact;
        this.detail_contact = detail_contact;
    }

    public Integer getId() { return id;}
    public void setId(Integer id) { this.id = id;}

    public Integer getInsurance_id() { return insurance_id; }
    public void setInsurance_id(Integer insurance_id) { this.insurance_id = insurance_id; }

    public String getType_contact() { return type_contact; }
    public void setType_contact(String type_contact) { this.type_contact = type_contact; }

    public String getDescription_contact() { return description_contact; }
    public void setDescription_contact(String description_contact) { this.description_contact = description_contact; }

    public String getDetail_contact() { return detail_contact; }
    public void setDetail_contact(String detail_contact) { this.detail_contact = detail_contact; }

}
