package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.InsuranceCompany;

import java.util.ArrayList;
import java.util.List;

public interface InsuranceCompanyIDAO {
    InsuranceCompany fetchInsuranceCompanyById(Integer id);
    InsuranceCompany fetchInsuranceCompanyByCNPJ(String cnpj);
    Integer fetchInsuranceCompanyByName(String name);
    List<InsuranceCompany> fetchAllInsuranceCompanies();
    ArrayList<InsuranceCompany> fetchArrayInsuranceCompany();
    boolean addInsuranceCompany(InsuranceCompany insuranceCompany);
    void deleteInsuranceCompany(Integer id);
    boolean updateInsuranceCompany(InsuranceCompany insuranceCompany);
}
