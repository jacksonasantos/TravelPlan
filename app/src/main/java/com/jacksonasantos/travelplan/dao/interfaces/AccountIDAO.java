package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Account;

import java.util.List;

public interface AccountIDAO {
    Account fetchAccountById(Integer id);
    List<Account> fetchAllAccount();
    boolean addAccount(Account account);
    void deleteAccount(Integer id);
    boolean updateAccount(Account account);

}