package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.CurrencyQuote;

import java.util.Date;

public interface CurrencyQuoteIDAO {
    // --Commented out by Inspection (15/08/2022 22:25):CurrencyQuote fetchCurrencyQuoteById(Integer id);
    // --Commented out by Inspection (15/08/2022 22:25):List<CurrencyQuote> fetchAllCurrencyQuotes();
 // --Commented out by Inspection (15/08/2022 22:25):   // --Commented out by Inspection (15/08/2022 22:25):boolean// --Commented out by Inspection (15/08/2022 22:25): addCurrencyQuote(CurrencyQuote currencyQuote);
// --Commented out by Inspection START (15/08/2022 22:25):
// --Commented out by Inspection STOP (15/08/2022 22:25)
    void deleteCurrencyQuote(Integer id);
    boolean updateCurrencyQuote(CurrencyQuote currencyQuote);
    CurrencyQuote findQuoteDay(int currency_type, Date quote_date);
}