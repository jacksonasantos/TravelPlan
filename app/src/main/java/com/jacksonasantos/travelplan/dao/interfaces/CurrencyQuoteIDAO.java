package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.CurrencyQuote;

import java.util.Date;

public interface CurrencyQuoteIDAO {
    void deleteCurrencyQuote(Integer id);
    boolean updateCurrencyQuote(CurrencyQuote currencyQuote);
    CurrencyQuote findQuoteDay(int currency_type, Date quote_date);
}