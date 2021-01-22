package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Accommodation;

import java.util.ArrayList;
import java.util.List;

public interface AccommodationIDAO {
    Accommodation fetchAccommodationById(Integer id);
    List<Accommodation> fetchAllAccommodation();
    ArrayList<Accommodation> fetchArrayAccommodation();
    boolean addAccommodation(Accommodation accommodation);
    void deleteAccommodation(Integer id);
    boolean updateAccommodation(Accommodation accommodation);
}