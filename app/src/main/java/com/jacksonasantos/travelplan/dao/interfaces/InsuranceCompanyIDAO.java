package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.InsuranceCompany;

import java.text.ParseException;
import java.util.List;

public interface InsuranceCompanyIDAO {
    InsuranceCompany fetchInsuranceCompanyById(Long id) throws ParseException;
    InsuranceCompany fetchInsuranceCompanyByCNPJ(String cnpj) throws ParseException;
    List<InsuranceCompany> fetchAllInsuranceCompanies() throws ParseException;
    boolean addInsuranceCompany(InsuranceCompany insuranceCompany);
    void deleteInsuranceCompany(Long insuranceCompany);
    boolean updateInsuranceCompany(InsuranceCompany insuranceCompany);
}
