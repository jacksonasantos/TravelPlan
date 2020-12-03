package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.CurrencyQuote;

import java.util.Date;
import java.util.List;

public interface CurrencyQuoteIDAO {
    CurrencyQuote fetchCurrencyQuoteById(Long id);
    List<CurrencyQuote> fetchAllCurrencyQuotes();
    boolean addCurrencyQuote(CurrencyQuote currencyQuote);
    void deleteCurrencyQuote(Long id);
    boolean updateCurrencyQuote(CurrencyQuote currencyQuote);
    CurrencyQuote findQuoteDay(int currency_type, Date quote_date);
}