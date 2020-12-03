package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.InsuranceCompany;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public interface InsuranceCompanyIDAO {
    InsuranceCompany fetchInsuranceCompanyById(Long id) throws ParseException;
    InsuranceCompany fetchInsuranceCompanyByCNPJ(String cnpj) throws ParseException;
    Long fetchInsuranceCompanyByName(String name) throws ParseException;
    List<InsuranceCompany> fetchAllInsuranceCompanies() throws ParseException;
    ArrayList<InsuranceCompany> fetchArrayInsuranceCompany() throws ParseException;
    boolean addInsuranceCompany(InsuranceCompany insuranceCompany);
    void deleteInsuranceCompany(Long id);
    boolean updateInsuranceCompany(InsuranceCompany insuranceCompany);
}
