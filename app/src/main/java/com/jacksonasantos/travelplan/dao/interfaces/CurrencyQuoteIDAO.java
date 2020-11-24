package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.CurrencyQuote;

import java.util.Date;
import java.util.List;

public interface CurrencyQuoteIDAO {
    CurrencyQuote fetchCurrencyQuoteById(Long currencyQuoteId);
    List<CurrencyQuote> fetchAllCurrencyQuotes();
    boolean addCurrencyQuote(CurrencyQuote currencyQuote);
    boolean deleteCurrencyQuote(Long currencyQuoteId);
    boolean updateCurrencyQuote(CurrencyQuote currencyQuote);

    CurrencyQuote findQuoteDay(int currency_type, Date quote_date);
}