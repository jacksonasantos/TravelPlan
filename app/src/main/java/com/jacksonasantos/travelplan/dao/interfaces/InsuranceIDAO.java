package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Insurance;

import java.util.List;

public interface InsuranceIDAO {
    Insurance fetchInsuranceById(Integer id);
    List<Insurance> fetchAllInsurance();
    List<Insurance> findReminderInsurance( Integer id);
    boolean addInsurance(Insurance insurance);
    void deleteInsurance(Integer id);
    boolean updateInsurance(Insurance insurance);

}