package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.CurrencyQuote;

import java.util.List;

public interface CurrencyQuotelDAO {
    CurrencyQuote fetchCurrencyQuoteById(int currencyQuoteId);
    List<CurrencyQuote> fetchAllCurrencyQuotes();
    boolean addCurrencyQuote(CurrencyQuote currencyQuote);
    boolean deleteCurrencyQuote(int currencyQuoteId);
    boolean updateCurrencyQuote(CurrencyQuote currencyQuote);

    CurrencyQuote findQuoteDay( int currency_type, String quote_date);
}