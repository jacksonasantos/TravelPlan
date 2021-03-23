package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.InsuranceContact;

import java.util.List;

public interface InsuranceContactIDAO {
    InsuranceContact fetchInsuranceContactById(Integer id);
    InsuranceContact fetchInsuranceContactByInsurance(Integer insurance_id);
    List<InsuranceContact> fetchAllInsuranceContacts();
    void deleteInsuranceContact(Integer id);
    boolean updateInsuranceContact(InsuranceContact insuranceContact);
    boolean addInsuranceContact(InsuranceContact insuranceContact);
}
