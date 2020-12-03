package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Insurance;

import java.util.List;

public interface InsuranceIDAO {
    Insurance fetchInsuranceById(Long id);
    List<Insurance> fetchAllInsurance();
    boolean addInsurance(Insurance insurance);
    void deleteInsurance(Long id);
    boolean updateInsurance(Insurance insurance);

}