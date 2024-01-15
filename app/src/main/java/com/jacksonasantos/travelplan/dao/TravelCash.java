package com.jacksonasantos.travelplan.dao;

import java.util.Date;

public class TravelCash {
    public Integer id;
    public Integer travel_id;
    public int currency_id;
    public Date cash_deposit;
    public double amount_deposit;

    public TravelCash() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getTravel_id() { return travel_id; }
    public void setTravel_id(Integer travel_id) { this.travel_id = travel_id; }

    public int getCurrency_id() { return currency_id; }
    public void setCurrency_id(int currency_id) { this.currency_id = currency_id; }

    public Date getCash_deposit() { return cash_deposit; }
    public void setCash_deposit(Date cash_deposit) { this.cash_deposit = cash_deposit; }

    public double getAmount_deposit() { return amount_deposit; }
    public void setAmount_deposit(double amount_deposit) { this.amount_deposit = amount_deposit; }
}