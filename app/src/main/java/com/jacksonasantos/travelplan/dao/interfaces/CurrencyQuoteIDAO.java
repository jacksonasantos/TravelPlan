package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.CurrencyQuote;

import java.util.Date;
import java.util.List;

public interface CurrencyQuoteIDAO {
    CurrencyQuote fetchCurrencyQuoteById(Integer id);
    List<CurrencyQuote> fetchAllCurrencyQuotes();
    boolean addCurrencyQuote(CurrencyQuote currencyQuote);
    void deleteCurrencyQuote(Integer id);
    boolean updateCurrencyQuote(CurrencyQuote currencyQuote);
    CurrencyQuote findQuoteDay(int currency_type, Date quote_date);
}