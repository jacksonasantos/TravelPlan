package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.InsuranceContact;

import java.util.List;

public interface InsuranceContactIDAO {
    List<InsuranceContact> fetchInsuranceContactByInsurance(Integer insurance_id);
    void deleteInsuranceContact(Integer id);
    boolean addInsuranceContact(InsuranceContact insuranceContact);
}
