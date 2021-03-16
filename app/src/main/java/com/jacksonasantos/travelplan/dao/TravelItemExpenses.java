package com.jacksonasantos.travelplan.dao;

public class TravelItemExpenses {
    public Integer id;
    public Integer travel_expense_id;
    public Double realized_value;
    public String note;

    public TravelItemExpenses() {
        this.id = id;
        this.travel_expense_id = travel_expense_id;
        this.realized_value = realized_value;
        this.note = note;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getTravel_expense_id() { return travel_expense_id; }
    public void setTravel_expense_id(Integer travel_expense_id) { this.travel_expense_id = travel_expense_id; }


    public Double getRealized_value() { return realized_value; }
    public void setRealized_value(Double realized_value) { this.realized_value = realized_value;}

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}