package com.jacksonasantos.travelplan.dao;

// TODO - Inclusion currency_type
public class TravelExpenses {
    public Integer id;
    public Integer travel_id;
    public int expense_type;
    public Double expected_value;
    public String note;
    public Integer marker_id;

    public TravelExpenses() {
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

    public Integer getMarker_id() { return marker_id; }
    public void setMarker_id(Integer marker_id) { this.marker_id = marker_id; }
}