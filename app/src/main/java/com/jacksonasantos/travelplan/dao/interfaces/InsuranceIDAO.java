package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Insurance;

import java.util.List;

public interface InsuranceIDAO {
    Insurance fetchInsuranceById(Integer id);
    Insurance fetchInsuranceByPolicy(String insurance_policy);
    List<Insurance> fetchAllInsurance();
    List<Insurance> findReminderInsurance(  String type,  Integer id);
    boolean addInsurance(Insurance insurance);
    void deleteInsurance(Integer id);
    boolean updateInsurance(Insurance insurance);
}