package com.jacksonasantos.travelplan.DAO.Interface;

import com.jacksonasantos.travelplan.DAO.User;

import java.util.List;

public interface UserIDAO {
    User fetchUserById(int userId);
    List<User> fetchAllUsers();
    // add user
    boolean addUser(User user);
    // add users in bulk
    boolean addUsers(List<User> users);
    boolean deleteAllUsers();
    boolean deleteUser(int id);
}