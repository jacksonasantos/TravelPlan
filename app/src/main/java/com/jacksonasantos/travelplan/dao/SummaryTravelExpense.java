package com.jacksonasantos.travelplan.dao;

public class SummaryTravelExpense {
    public String expense;
    public Double expected_value;
    public Double realized_value;

    public SummaryTravelExpense() {
        this.expense = expense;
        this.expected_value = expected_value;
        this.realized_value = realized_value;
    }

    public String getExpense() { return expense; }
    public void setExpense(String expense) { this.expense = expense; }

    public Double getExpected_value() { return expected_value; }
    public void setExpected_value(Double expected_value) { this.expected_value = expected_value; }

    public Double getRealized_value() { return realized_value; }
    public void setRealized_value(Double realized_value) { this.realized_value = realized_value;}
}