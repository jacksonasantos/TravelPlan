package com.jacksonasantos.travelplan.dao.interfaces;

import android.database.Cursor;

import com.jacksonasantos.travelplan.dao.Driver;

import java.util.ArrayList;
import java.util.List;

public interface DriverIDAO {
    Driver fetchDriverById(Integer id);
    List<Driver> fetchAllDriver();
    ArrayList<Driver> fetchArrayDriver();
    Cursor fetchCursorDriver();
    boolean addDriver(Driver broker);
    void deleteDriver(Integer id);
    boolean updateDriver(Driver broker);

}