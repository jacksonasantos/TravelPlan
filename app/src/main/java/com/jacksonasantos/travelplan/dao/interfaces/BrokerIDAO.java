package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Broker;

import java.util.ArrayList;
import java.util.List;

public interface BrokerIDAO {
    Broker fetchBrokerById(Integer id);
    List<Broker> fetchAllBroker();
    ArrayList<Broker> fetchArrayBroker();
    boolean addBroker(Broker broker);
    void deleteBroker(Integer id);
    boolean updateBroker(Broker broker);

}