package com.jacksonasantos.travelplan.dao;

import java.util.Date;

public class InsuranceCompany {
    public Long id;
    public String company_name;
    public String cnpj;
    public String fip_code;
    public String address;
    public String city;
    public String state;
    public String zip_code;
    public String telephone;
    public Date authorization_date;

    public InsuranceCompany() {
        this.id = id;
        this.company_name = company_name;
        this.cnpj = cnpj;
        this.fip_code = fip_code;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip_code = zip_code;
        this.telephone = telephone;
        this.authorization_date = authorization_date;
    }

    public Long getId() { return id;}

    public void setId(Long id) { this.id = id;}

    public String getCompany_name() { return company_name; }

    public void setCompany_name(String company_name) { this.company_name = company_name; }

    public String getCnpj() { return cnpj; }

    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getFip_code() { return fip_code; }

    public void setFip_code(String fip_code) { this.fip_code = fip_code; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public String getZip_code() { return zip_code; }

    public void setZip_code(String zip_code) { this.zip_code = zip_code; }

    public String getTelephone() { return telephone; }

    public void setTelephone(String telephone) { this.telephone = telephone; }

    public Date getAuthorization_date() { return authorization_date; }

    public void setAuthorization_date(Date authorization_date) { this.authorization_date = authorization_date; }
}
