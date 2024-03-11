package com.jacksonasantos.travelplan.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.CurrencyQuoteISchema;
import com.jacksonasantos.travelplan.dao.interfaces.FuelSupplyISchema;
import com.jacksonasantos.travelplan.dao.interfaces.InsuranceISchema;
import com.jacksonasantos.travelplan.dao.interfaces.ItineraryISchema;
import com.jacksonasantos.travelplan.dao.interfaces.ReservationISchema;
import com.jacksonasantos.travelplan.dao.interfaces.SummaryTravelExpenseIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.SummaryTravelExpenseISchema;
import com.jacksonasantos.travelplan.dao.interfaces.TourISchema;
import com.jacksonasantos.travelplan.dao.interfaces.TransportISchema;
import com.jacksonasantos.travelplan.dao.interfaces.TravelExpensesISchema;
import com.jacksonasantos.travelplan.dao.interfaces.TravelItemExpensesISchema;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleHasTravelISchema;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleISchema;
import com.jacksonasantos.travelplan.ui.utility.Globals;

import java.util.ArrayList;
import java.util.List;

public class SummaryTravelExpenseDAO extends DbContentProvider implements SummaryTravelExpenseISchema, SummaryTravelExpenseIDAO {

    private Cursor cursor;

    final Globals g = Globals.getInstance();

    public SummaryTravelExpenseDAO(SQLiteDatabase db) {
        super(db);
    }

    public List<SummaryTravelExpense> findTravelExpense(Integer travel_id) {
        List<SummaryTravelExpense> summaryTravelExpenseList = new ArrayList<>();
        cursor = super.rawQuery(
                "SELECT " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " +
                               " SUM( " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ") " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                               " SUM( " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE + ") " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                     " FROM ( SELECT 0 " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " + // Combustible
                                   " (SELECT ( SUM([IFNULL](t1." + TourISchema.TOUR_DISTANCE + ", 0)/" + g.getMeasureIndexInMeter() + ") + " +
                                              " SUM(i." + ItineraryISchema.ITINERARY_DISTANCE + "/" + g.getMeasureIndexInMeter() +") ) * " +
                                              " AVG([IFNULL](vt." + VehicleHasTravelISchema.VEHICLE_HAS_TRAVEL_AVG_COST_LITRE + ", v." + VehicleISchema.VEHICLE_AVG_COST_LITRE+")) " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE +
                                      " FROM " + ItineraryISchema.ITINERARY_TABLE + " i " +
                                      " LEFT JOIN (SELECT t." + TourDAO.TOUR_TRAVEL_ID + ", " +
                                                        " t." + TourDAO.TOUR_ITINERARY_ID + ", SUM(t." + TourDAO.TOUR_DISTANCE + ") " + TourDAO.TOUR_DISTANCE +
                                                   " FROM " + TourDAO.TOUR_TABLE + " t" +
                                                  " GROUP BY t." + TourDAO.TOUR_TRAVEL_ID + ", t." + TourDAO.TOUR_ITINERARY_ID + ") t1 " +
                                             " ON t1." + TourDAO.TOUR_TRAVEL_ID + " = i." + ItineraryDAO.ITINERARY_TRAVEL_ID +
                                            " AND t1." + TourDAO.TOUR_ITINERARY_ID + " = i." + ItineraryDAO.ITINERARY_ID +
                                      " LEFT JOIN " + ItineraryHasTransportDAO.ITINERARY_HAS_TRANSPORT_TABLE + " it ON it." + ItineraryHasTransportDAO.ITINERARY_HAS_TRANSPORT_TRAVEL_ID + " = i." + ItineraryDAO.ITINERARY_TRAVEL_ID +
                                                                                                                 " AND it." + ItineraryHasTransportDAO.ITINERARY_HAS_TRANSPORT_ITINERARY_ID + " = i." + ItineraryDAO.ITINERARY_ID +
                                                                                                                 " AND it." + ItineraryHasTransportDAO.ITINERARY_HAS_TRANSPORT_DRIVER + " = 1 " +
                                      " LEFT JOIN " + VehicleHasTravelDAO.VEHICLE_HAS_TRAVEL_TABLE + " vt ON vt." + VehicleHasTravelDAO.VEHICLE_HAS_TRAVEL_TRAVEL_ID + " = i." + ItineraryDAO.ITINERARY_TRAVEL_ID +
                                                                                                       " AND vt." + VehicleHasTravelDAO.VEHICLE_HAS_TRAVEL_TRANSPORT_ID + " = it." + ItineraryHasTransportDAO.ITINERARY_HAS_TRANSPORT_TRANSPORT_ID +
                                      " LEFT JOIN " + VehicleHasTravelDAO.VEHICLE_HAS_TRAVEL_TABLE + " vt1 ON vt1." + VehicleHasTravelDAO.VEHICLE_HAS_TRAVEL_TRAVEL_ID + " = i." + ItineraryDAO.ITINERARY_TRAVEL_ID +
                                      " LEFT JOIN " + VehicleDAO.VEHICLE_TABLE + " v ON v." + VehicleDAO.VEHICLE_ID + " = vt1." + ItineraryHasTransportDAO.ITINERARY_HAS_TRANSPORT_VEHICLE_ID +
                                     " WHERE i." + ItineraryDAO.ITINERARY_TRAVEL_ID + " = ?) " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                                   " SUM(" + FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_VALUE +") " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                              " FROM " + FuelSupplyISchema.FUEL_SUPPLY_TABLE +
                             " WHERE " + FuelSupplyISchema.FUEL_SUPPLY_ASSOCIATED_TRAVEL_ID + " = ? " +
                            " UNION " +
                            " SELECT 4 " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " + // Accommodation
                                   " SUM(" + ReservationISchema.RESERVATION_RESERVATION_AMOUNT + ") " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                                   " 0 " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                              " FROM " + ReservationISchema.RESERVATION_TABLE +
                             " WHERE " + ReservationISchema.RESERVATION_TRAVEL_ID + " = ? " +
                            " UNION " +
                            " SELECT 3 "+ SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " +  // Tour
                                   " SUM((" + TourISchema.TOUR_VALUE_ADULT + "*" + TourISchema.TOUR_NUMBER_ADULT +")+("+TourISchema.TOUR_VALUE_CHILD + "*" + TourISchema.TOUR_NUMBER_CHILD + ")) " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                                   " 0 " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                              " FROM " + TourISchema.TOUR_TABLE +
                             " WHERE " + TourISchema.TOUR_TRAVEL_ID + " = ? " +
                            " UNION " +
                            " SELECT 6 "+ SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " +  // Insurance
                                   " SUM(" + InsuranceISchema.INSURANCE_TOTAL_PREMIUM_VALUE + ") " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                                   " 0 " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                              " FROM " + InsuranceISchema.INSURANCE_TABLE +
                             " WHERE " + InsuranceISchema.INSURANCE_TRAVEL_ID + " = ? " +
                            " UNION " +
                            " SELECT 7 "+ SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " +  // Transport
                                   " SUM(" + TransportISchema.TRANSPORT_SERVICE_VALUE +"+"+ TransportISchema.TRANSPORT_SERVICE_TAX+ ") "+ SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                                   " 0 " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                              " FROM " + TransportISchema.TRANSPORT_TABLE +
                             " WHERE " + TransportISchema.TRANSPORT_TRAVEL_ID + " = ? " +
                            " UNION " +                                                                             // Travel Expenses EXPECTED_VALUE
                            " SELECT " + TravelExpensesISchema.TRAVEL_EXPENSES_EXPENSE_TYPE + " " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " +
                                   " SUM(" + TravelExpensesISchema.TRAVEL_EXPENSES_EXPECTED_VALUE + ") " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                                   " 0 " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                              " FROM " + TravelExpensesISchema.TRAVEL_EXPENSES_TABLE +
                             " WHERE " + TravelExpensesISchema.TRAVEL_EXPENSES_TRAVEL_ID + " = ? " +
                             " GROUP BY " + TravelExpensesISchema.TRAVEL_EXPENSES_EXPENSE_TYPE + " " +
                            " UNION " +                                                                             // Travel Expenses REALIZED_VALUE
                            " SELECT " + TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_EXPENSE_TYPE + " " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " +
                                   " 0 " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                                   " SUM(" + TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_REALIZED_VALUE+") " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                              " FROM " + TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_TABLE + " tie " +
                             " WHERE " + TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_TRAVEL_ID + " = ? " +
                             " GROUP BY " + TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_EXPENSE_TYPE + " " +
                           ") " +
                  " GROUP BY " + TravelExpensesISchema.TRAVEL_EXPENSES_EXPENSE_TYPE,
        new String[] { String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(travel_id)});
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    SummaryTravelExpense summaryTravelExpense = cursorToEntity(cursor);
                    summaryTravelExpenseList.add(summaryTravelExpense);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return summaryTravelExpenseList;
    }

    public List<SummaryTravelExpense> findTravelExpenseCurrencyType(Integer travel_id) {
        List<SummaryTravelExpense> summaryTravelExpenseList = new ArrayList<>();
        cursor = super.rawQuery(
                "SELECT " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " +
                        SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_CURRENCY_TYPE + ", " +
                        " SUM(" + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ") " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                        " SUM(" + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE + ") " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                        " FROM ( SELECT 0 " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " + // Combustible
                        "0 currency_type, " +
                        " (SELECT ( SUM([IFNULL](t1." + TourISchema.TOUR_DISTANCE + ", 0)/" + g.getMeasureIndexInMeter() + ") + " +
                        " SUM(i." + ItineraryISchema.ITINERARY_DISTANCE + "/" + g.getMeasureIndexInMeter() +") ) * " +
                        " AVG([IFNULL](vt." + VehicleHasTravelISchema.VEHICLE_HAS_TRAVEL_AVG_COST_LITRE + ", v." + VehicleISchema.VEHICLE_AVG_COST_LITRE+")) " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE +
                        " FROM " + ItineraryISchema.ITINERARY_TABLE + " i " +
                        " LEFT JOIN (SELECT t." + TourDAO.TOUR_TRAVEL_ID + ", " +
                        " t." + TourDAO.TOUR_ITINERARY_ID + ", SUM(t." + TourDAO.TOUR_DISTANCE + ") " + TourDAO.TOUR_DISTANCE +
                        " FROM " + TourDAO.TOUR_TABLE + " t" +
                        " GROUP BY t." + TourDAO.TOUR_TRAVEL_ID + ", t." + TourDAO.TOUR_ITINERARY_ID + ") t1 " +
                        " ON t1." + TourDAO.TOUR_TRAVEL_ID + " = i." + ItineraryDAO.ITINERARY_TRAVEL_ID +
                        " AND t1." + TourDAO.TOUR_ITINERARY_ID + " = i." + ItineraryDAO.ITINERARY_ID +
                        " LEFT JOIN " + ItineraryHasTransportDAO.ITINERARY_HAS_TRANSPORT_TABLE + " it ON it." + ItineraryHasTransportDAO.ITINERARY_HAS_TRANSPORT_TRAVEL_ID + " = i." + ItineraryDAO.ITINERARY_TRAVEL_ID +
                        " AND it." + ItineraryHasTransportDAO.ITINERARY_HAS_TRANSPORT_ITINERARY_ID + " = i." + ItineraryDAO.ITINERARY_ID +
                        " AND it." + ItineraryHasTransportDAO.ITINERARY_HAS_TRANSPORT_DRIVER + " = 1 " +
                        " LEFT JOIN " + VehicleHasTravelDAO.VEHICLE_HAS_TRAVEL_TABLE + " vt ON vt." + VehicleHasTravelDAO.VEHICLE_HAS_TRAVEL_TRAVEL_ID + " = i." + ItineraryDAO.ITINERARY_TRAVEL_ID +
                        " AND vt." + VehicleHasTravelDAO.VEHICLE_HAS_TRAVEL_TRANSPORT_ID + " = it." + ItineraryHasTransportDAO.ITINERARY_HAS_TRANSPORT_TRANSPORT_ID +
                        " LEFT JOIN " + VehicleHasTravelDAO.VEHICLE_HAS_TRAVEL_TABLE + " vt1 ON vt1." + VehicleHasTravelDAO.VEHICLE_HAS_TRAVEL_TRAVEL_ID + " = i." + ItineraryDAO.ITINERARY_TRAVEL_ID +
                        " LEFT JOIN " + VehicleDAO.VEHICLE_TABLE + " v ON v." + VehicleDAO.VEHICLE_ID + " = vt1." + ItineraryHasTransportDAO.ITINERARY_HAS_TRANSPORT_VEHICLE_ID +
                        " WHERE i." + ItineraryDAO.ITINERARY_TRAVEL_ID + " = ?) " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                        " SUM(" + FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_VALUE +") " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                        " FROM " + FuelSupplyISchema.FUEL_SUPPLY_TABLE +
                        " WHERE " + FuelSupplyISchema.FUEL_SUPPLY_ASSOCIATED_TRAVEL_ID + " = ? " +
                        " UNION " +
/*     SELECT 4 expense_type ,
              CASE WHEN r.currency_type = 0 THEN r.currency_type
                   WHEN r.currency_type = cq.currency_type THEN 0
                   ELSE r.currency_type
               END currency_type,
               SUM(r.reservation_amount * IFNULL(cq.currency_value,1)) expected_value,
               0 realized_value
         FROM reservation r
         LEFT JOIN currency_quote cq
                ON r.currency_type = cq.currency_type
               AND DATE(cq.quote_date) = DATE('now')
        WHERE r.travel_id = 27
        GROUP BY 1,2*/
                        " SELECT 4 " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " + // Accommodation
                               " CASE WHEN r." + ReservationISchema.RESERVATION_CURRENCY_TYPE + " = 0 THEN r."+ReservationISchema.RESERVATION_CURRENCY_TYPE +
                                    " WHEN r." + ReservationISchema.RESERVATION_CURRENCY_TYPE + " = cq."+CurrencyQuoteISchema.CURRENCY_QUOTE_CURRENCY_TYPE + " THEN 0 " +
                                    " ELSE r." + ReservationISchema.RESERVATION_CURRENCY_TYPE +
                               " END " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_CURRENCY_TYPE + ", " +
                               " SUM(r." + ReservationISchema.RESERVATION_RESERVATION_AMOUNT + " * IFNULL(cq."+CurrencyQuoteISchema.CURRENCY_QUOTE_CURRENCY_VALUE+",1)) " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                               " 0 " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                          " FROM " + ReservationISchema.RESERVATION_TABLE + " r " +
                          " LEFT JOIN " + CurrencyQuoteISchema.CURRENCY_QUOTE_TABLE + " cq " +
                                 " ON r."+ ReservationISchema.RESERVATION_CURRENCY_TYPE + " = cq." + CurrencyQuoteISchema.CURRENCY_QUOTE_CURRENCY_TYPE +
                                " AND DATE(cq."+CurrencyQuoteISchema.CURRENCY_QUOTE_QUOTE_DATE + ") = DATE('now') " +
                         " WHERE r." + ReservationISchema.RESERVATION_TRAVEL_ID + " = ? " +
                         " GROUP BY 1,2 " +
                        " UNION " +
/*     SELECT 3 expense_type ,
              CASE WHEN t.currency_type = 0 THEN t.currency_type
                       WHEN t.currency_type = cq.currency_type THEN 0
                         ELSE t.currency_type
              END currency_type,
              SUM((t.VALUE_ADULT * t.NUMBER_ADULT )+(t.VALUE_CHILD *t.NUMBER_CHILD )) * ifnull(cq.currency_value,1) expected_value ,
              0 realized_value
         FROM Tour t
         LEFT JOIN currency_quote cq
                ON t.currency_type = cq.currency_type
               AND date(cq.quote_date) = date('now')
        WHERE t.TRAVEL_ID  = 27
        GROUP BY 1,2*/
                        " SELECT 3 "+ SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " +  // Tour
                               " CASE WHEN t." + TourISchema.TOUR_CURRENCY_TYPE + " = 0 THEN t."+TourISchema.TOUR_CURRENCY_TYPE +
                                    " WHEN t." + TourISchema.TOUR_CURRENCY_TYPE + " = cq."+CurrencyQuoteISchema.CURRENCY_QUOTE_CURRENCY_TYPE + " THEN 0 " +
                                    " ELSE t."+TourISchema.TOUR_CURRENCY_TYPE +
                               " END " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_CURRENCY_TYPE + ", " +
                               " SUM((t." + TourISchema.TOUR_VALUE_ADULT + " * t." + TourISchema.TOUR_NUMBER_ADULT +")+(t."+TourISchema.TOUR_VALUE_CHILD + " * t." + TourISchema.TOUR_NUMBER_CHILD + "))"+" * IFNULL(cq."+CurrencyQuoteISchema.CURRENCY_QUOTE_CURRENCY_VALUE+",1)"+" " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                               " 0 " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                          " FROM " + TourISchema.TOUR_TABLE + " t " +
                          " LEFT JOIN " + CurrencyQuoteISchema.CURRENCY_QUOTE_TABLE + " cq " +
                                 " ON t."+ TourISchema.TOUR_CURRENCY_TYPE + " = cq." + CurrencyQuoteISchema.CURRENCY_QUOTE_CURRENCY_TYPE +
                                " AND DATE(cq."+CurrencyQuoteISchema.CURRENCY_QUOTE_QUOTE_DATE + ") = DATE('now') " +
                         " WHERE t." + TourISchema.TOUR_TRAVEL_ID + " = ? " +
                         " GROUP BY 1,2 " +
                        " UNION " +
                        " SELECT 6 "+ SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " +  // Insurance
                        " 0 currency_type , " +
                        " SUM(" + InsuranceISchema.INSURANCE_TOTAL_PREMIUM_VALUE + ") " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                        " 0 " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                        " FROM " + InsuranceISchema.INSURANCE_TABLE +
                        " WHERE " + InsuranceISchema.INSURANCE_TRAVEL_ID + " = ? " +
                        " UNION " +
                        " SELECT 7 "+ SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " +  // Transport
                        " 0 currency_type , " +
                        " SUM(" + TransportISchema.TRANSPORT_SERVICE_VALUE +"+"+ TransportISchema.TRANSPORT_SERVICE_TAX+ ") "+ SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                        " 0 " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                        " FROM " + TransportISchema.TRANSPORT_TABLE +
                        " WHERE " + TransportISchema.TRANSPORT_TRAVEL_ID + " = ? " +
                        " UNION " +                                                                             // Travel Expenses EXPECTED_VALUE
                        " SELECT " + TravelExpensesISchema.TRAVEL_EXPENSES_EXPENSE_TYPE + " " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " +
                        " 0 currency_type , " +
                        " SUM(" + TravelExpensesISchema.TRAVEL_EXPENSES_EXPECTED_VALUE + ") " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                        " 0 " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                        " FROM " + TravelExpensesISchema.TRAVEL_EXPENSES_TABLE +
                        " WHERE " + TravelExpensesISchema.TRAVEL_EXPENSES_TRAVEL_ID + " = ? " +
                        " GROUP BY " + TravelExpensesISchema.TRAVEL_EXPENSES_EXPENSE_TYPE + " " +
                        " UNION " +                                                                             // Travel Expenses REALIZED_VALUE
                        " SELECT " + TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_EXPENSE_TYPE + " " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " +
                        " 0 currency_type , " +
                        " 0 " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                        " SUM(" + TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_REALIZED_VALUE+") " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                        " FROM " + TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_TABLE + " tie " +
                        " WHERE " + TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_TRAVEL_ID + " = ? " +
                        " GROUP BY " + TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_EXPENSE_TYPE + " " +
                        ") " +
                        " GROUP BY " + TravelExpensesISchema.TRAVEL_EXPENSES_EXPENSE_TYPE + ", "+ SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_CURRENCY_TYPE,
                new String[] { String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(travel_id)});
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    SummaryTravelExpense summaryTravelExpense = cursorToEntity(cursor);
                    summaryTravelExpenseList.add(summaryTravelExpense);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return summaryTravelExpenseList;
    }

    protected SummaryTravelExpense cursorToEntity(Cursor c) {
        SummaryTravelExpense sTE = new SummaryTravelExpense();
        if (c != null) {
            if (c.getColumnIndex(SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE) != -1)   {sTE.setExpense_type(cursor.getInt(c.getColumnIndexOrThrow(SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE))); }
            if (c.getColumnIndex(SUMMARY_TRAVEL_EXPENSE_CURRENCY_TYPE) != -1)  {sTE.setCurrency_type(cursor.getInt(c.getColumnIndexOrThrow(SUMMARY_TRAVEL_EXPENSE_CURRENCY_TYPE))); }
            if (c.getColumnIndex(SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE) != -1) {sTE.setExpected_value(cursor.getDouble(c.getColumnIndexOrThrow(SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE))); }
            if (c.getColumnIndex(SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE) != -1) {sTE.setRealized_value(cursor.getDouble(c.getColumnIndexOrThrow(SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE))); }
        }
        return sTE;
    }
}