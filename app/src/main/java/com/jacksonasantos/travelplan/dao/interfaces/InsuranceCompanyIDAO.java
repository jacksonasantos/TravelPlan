package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.InsuranceCompany;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public interface InsuranceCompanyIDAO {
    InsuranceCompany fetchInsuranceCompanyById(Long id);
    InsuranceCompany fetchInsuranceCompanyByCNPJ(String cnpj);
    Long fetchInsuranceCompanyByName(String name);
    List<InsuranceCompany> fetchAllInsuranceCompanies();
    ArrayList<InsuranceCompany> fetchArrayInsuranceCompany();
    boolean addInsuranceCompany(InsuranceCompany insuranceCompany);
    void deleteInsuranceCompany(Long id);
    boolean updateInsuranceCompany(InsuranceCompany insuranceCompany);
}
