package com.jacksonasantos.travelplan.dao;

import android.icu.text.DecimalFormat;

public class CurrencyQuote {
    public Long id;
    public int currency_type;
    public String quote_date;
    public double currency_value;

    public CurrencyQuote() {
        this.id = id;
        this.currency_type = currency_type;
        this.quote_date = quote_date;
        this.currency_value = currency_value;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public int getCurrency_type() { return currency_type; }

    public void setCurrency_type(int currency_type) { this.currency_type = currency_type; }

    public String getQuote_date() { return quote_date; }

    public void setQuote_date(String quote_date) { this.quote_date = quote_date; }

    public double getCurrency_value() { return currency_value; }

    public void setCurrency_value(double currency_value) { this.currency_value = currency_value; }
}
