package com.jacksonasantos.travelplan.dao;

import java.util.Date;

public class TravelItemExpenses {
    public Integer id;
    public int expense_type;
    public Date expense_date;
    public Double realized_value;
    public String note;
    public Integer travel_id;

    public TravelItemExpenses() {
        this.id = id;
        this.expense_type = expense_type;
        this.expense_date=expense_date;
        this.realized_value = realized_value;
        this.note = note;
        this.travel_id = travel_id;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getExpense_type() { return expense_type; }
    public void setExpense_type(Integer expense_type) { this.expense_type = expense_type; }

    public Date getExpense_date() {return expense_date; }
    public void setExpense_date(Date expense_date) {this.expense_date =expense_date; }

    public Double getRealized_value() { return realized_value; }
    public void setRealized_value(Double realized_value) { this.realized_value = realized_value;}

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public Integer getTravel_id() { return travel_id; }
    public void setTravel_id(Integer travel_id) { this.travel_id = travel_id; }
}