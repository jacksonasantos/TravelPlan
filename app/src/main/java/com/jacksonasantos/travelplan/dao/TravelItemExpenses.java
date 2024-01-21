package com.jacksonasantos.travelplan.dao;

import java.util.Date;

public class TravelItemExpenses {
    public Integer id;
    public int expense_type;
    public Date expense_date;
    public Double realized_value;
    public String note;
    public Integer travel_id;
    public Integer account_id;
    public int currency_id;
    public String expense_type_key;  // Atributo utilizado para identificar a origem do lancamento de realização de pagamento. Ex.: "transport_id = 1"

    public TravelItemExpenses() {
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

    public Integer getAccount_id() { return account_id; }
    public void setAccount_id(Integer account_id) { this.account_id = account_id; }

    public int getCurrency_id() { return currency_id; }
    public void setCurrency_id(int currency_id) { this.currency_id = currency_id; }

    public String getExpense_type_key() {return expense_type_key;}
    public void setExpense_type_key(String expense_type_key) {this.expense_type_key = expense_type_key;}
}