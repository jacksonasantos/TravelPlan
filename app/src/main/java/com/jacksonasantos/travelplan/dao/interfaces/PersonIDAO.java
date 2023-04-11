package com.jacksonasantos.travelplan.dao.interfaces;

import android.database.Cursor;

import com.jacksonasantos.travelplan.dao.Person;

import java.util.ArrayList;
import java.util.List;

public interface PersonIDAO {
    Person fetchPersonById(Integer id);
    List<Person> fetchAllPerson();
    ArrayList<Person> fetchArrayPerson();
    Cursor fetchCursorPerson();
    boolean addPerson(Person person);
    void deletePerson(Integer id);
    boolean updatePerson(Person person);
}