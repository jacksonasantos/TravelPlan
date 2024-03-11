package com.jacksonasantos.travelplan.dao;

import java.util.Date;

public class CurrencyQuoteResponse {
    public int currency_type;
    public String code;
    public String codein;
    public String name;
    public float high;
    public float low;
    public float varBid;
    public float pctChange;
    public float bid;
    public float ask;
    public int timestamp;
    public Date create_date;

    public CurrencyQuoteResponse(){}

    @Override
    public String toString() {
        return name;
    }

    public int getCurrency_type() {
        return currency_type;
    }
    public void setCurrency_type(int currency_type) {
        this.currency_type = currency_type;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getCodein() {
        return codein;
    }
    public void setCodein(String codein) {
        this.codein = codein;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public float getHigh() {
        return high;
    }
    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }
    public void setLow(float low) {
        this.low = low;
    }

    public float getVarBid() {
        return varBid;
    }
    public void setVarBid(float varBid) {
        this.varBid = varBid;
    }

    public float getPctChange() {
        return pctChange;
    }
    public void setPctChange(float pctChange) {
        this.pctChange = pctChange;
    }

    public float getBid() {
        return bid;
    }
    public void setBid(float bid) {
        this.bid = bid;
    }

    public float getAsk() {
        return ask;
    }
    public void setAsk(float ask) {
        this.ask = ask;
    }

    public int getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public Date getCreate_date() {
        return create_date;
    }
    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }
}
