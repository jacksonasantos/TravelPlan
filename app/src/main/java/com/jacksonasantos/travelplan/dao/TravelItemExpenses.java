package com.jacksonasantos.travelplan.dao;

import java.util.Date;

public class TravelItemExpenses {
    public Integer id;
    public Integer travel_expense_id;
    public Date expense_date;
    public Double realized_value;
    public String note;

    public TravelItemExpenses() {
        this.id = id;
        this.travel_expense_id = travel_expense_id;
        this.expense_date=expense_date;
        this.realized_value = realized_value;
        this.note = note;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getTravel_expense_id() { return travel_expense_id; }
    public void setTravel_expense_id(Integer travel_expense_id) { this.travel_expense_id = travel_expense_id; }

    public Date getExpense_date() {return expense_date; }
    public void setExpense_date(Date expense_date) {this.expense_date =expense_date; }

    public Double getRealized_value() { return realized_value; }
    public void setRealized_value(Double realized_value) { this.realized_value = realized_value;}

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}