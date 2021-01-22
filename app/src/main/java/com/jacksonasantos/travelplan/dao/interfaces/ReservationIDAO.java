package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Reservation;

import java.util.List;

public interface ReservationIDAO {
    Reservation fetchReservationById(Integer id);
    List<Reservation> fetchAllReservation();
    boolean addReservation(Reservation reservation);
    void deleteReservation(Integer id);
    boolean updateReservation(Reservation reservation);
}