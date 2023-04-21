package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Transport;

import java.util.ArrayList;
import java.util.List;

public interface TransportIDAO {
    Transport fetchTransportById(Integer id);
    List<Transport> fetchAllTransportByTravel(Integer travel_id);
    List<Transport> fetchAllTransport();
    ArrayList<Transport> fetchArrayTransport();
    boolean addTransport(Transport transport);
    void deleteTransport(Integer id);
    boolean updateTransport(Transport transport);
}