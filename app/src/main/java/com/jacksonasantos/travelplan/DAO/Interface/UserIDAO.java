package com.jacksonasantos.travelplan.DAO.Interface;

import com.jacksonasantos.travelplan.DAO.User;

import java.util.List;

public interface UserIDAO {
    User fetchUserById(int userId);
    List<User> fetchAllUsers();
    boolean addUser(User user);
    boolean deleteUser(int id);
}