package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Transport;

import java.util.List;

public interface TransportIDAO {
    Transport fetchTransportById(Integer id);
    List<Transport> fetchAllTransport();
    Integer addTransport(Transport transport);
    void deleteTransport(Integer id);
    boolean updateTransport(Transport transport);
}