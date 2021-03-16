package com.jacksonasantos.travelplan.dao;

public class TravelExpenses {
    public Integer id;
    public Integer travel_id;
    public int expense_type;
    public Double expected_value;
    public String note;

    public TravelExpenses() {
        this.id = id;
        this.travel_id = travel_id;
        this.expense_type = expense_type;
        this.expected_value = expected_value;
        this.note = note;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getTravel_id() { return travel_id; }
    public void setTravel_id(Integer travel_id) { this.travel_id = travel_id; }

    public int getExpense_type() { return expense_type; }
    public void setExpense_type(int expense_type) { this.expense_type = expense_type; }

    public Double getExpected_value() { return expected_value; }
    public void setExpected_value(Double expected_value) { this.expected_value = expected_value;}

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}