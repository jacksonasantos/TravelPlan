package com.jacksonasantos.travelplan.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
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
                                              "SUM(i." + ItineraryISchema.ITINERARY_DISTANCE + "/" + g.getMeasureIndexInMeter() +") ) * " +
                                              "AVG([IFNULL](vt." + VehicleHasTravelISchema.VEHICLE_HAS_TRAVEL_AVG_COST_LITRE + ", v." + VehicleISchema.VEHICLE_AVG_COST_LITRE+")) " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE +
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
                                   " SUM(" + ReservationISchema.RESERVATION_AMOUNT_PAID + ") " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                              " FROM " + ReservationISchema.RESERVATION_TABLE +
                             " WHERE " + ReservationISchema.RESERVATION_TRAVEL_ID + " = ? " +
                            " UNION " +
                            " SELECT 3 "+ SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " +  // Tour
                                   " SUM((" + TourISchema.TOUR_VALUE_ADULT + "*" + TourISchema.TOUR_NUMBER_ADULT +")+("+TourISchema.TOUR_VALUE_CHILD + "*" + TourISchema.TOUR_NUMBER_CHILD + ")) " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE + ", " +
                                   " 0 " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE +
                              " FROM " + TourISchema.TOUR_TABLE +
                             " WHERE " + TourISchema.TOUR_TRAVEL_ID + " = ? " +
                            " UNION " +                                                                             // Travel Expenses
                            " SELECT 6 "+ SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " +  // Insurance
                                   " 0 " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                                   " SUM(" + InsuranceISchema.INSURANCE_TOTAL_PREMIUM_VALUE + ") " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                              " FROM " + InsuranceISchema.INSURANCE_TABLE +
                             " WHERE " + InsuranceISchema.INSURANCE_TRAVEL_ID + " = ? " +
                            " UNION " +
                            " SELECT 7 "+ SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " +  // Transport
                                   " SUM(" + TransportISchema.TRANSPORT_SERVICE_VALUE +"+"+ TransportISchema.TRANSPORT_SERVICE_TAX+ ") "+ SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                                   " SUM(" + TransportISchema.TRANSPORT_AMOUNT_PAID + ") " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                              " FROM " + TransportISchema.TRANSPORT_TABLE +
                             " WHERE " + TransportISchema.TRANSPORT_TRAVEL_ID + " = ? " +
                            " UNION " +                                                                             // Travel Expenses
                            " SELECT " + TravelExpensesISchema.TRAVEL_EXPENSES_EXPENSE_TYPE + " " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPENSE_TYPE + ", " +
                                   " SUM(" + TravelExpensesISchema.TRAVEL_EXPENSES_EXPECTED_VALUE + ") " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                                   " [IFNULL]( (SELECT SUM(tie." + TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_REALIZED_VALUE+") " +
                                                " FROM " + TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_TABLE+" tie " +
                                               " WHERE tie." + TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_TRAVEL_ID + " = " + TravelExpensesISchema.TRAVEL_EXPENSES_TABLE +"."+TravelExpensesISchema.TRAVEL_EXPENSES_TRAVEL_ID +
                                               " AND tie." + TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_EXPENSE_TYPE + " = " + TravelExpensesISchema.TRAVEL_EXPENSES_TABLE +"."+TravelExpensesISchema.TRAVEL_EXPENSES_EXPENSE_TYPE + " )" +
                                            ", 0) " + SummaryTravelExpenseISchema.SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE +
                              " FROM " + TravelExpensesISchema.TRAVEL_EXPENSES_TABLE +
                             " WHERE " + TravelExpensesISchema.TRAVEL_EXPENSES_TRAVEL_ID + " = ? " +
                             " GROUP BY " + TravelExpensesISchema.TRAVEL_EXPENSES_EXPENSE_TYPE + " " +
                           ") " +
                  " GROUP BY " + TravelExpensesISchema.TRAVEL_EXPENSES_EXPENSE_TYPE,
        new String[] { String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(travel_id)});
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
            if (c.getColumnIndex(SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE) != -1) {sTE.setExpected_value(cursor.getDouble(c.getColumnIndexOrThrow(SUMMARY_TRAVEL_EXPENSE_EXPECTED_VALUE))); }
            if (c.getColumnIndex(SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE) != -1) {sTE.setRealized_value(cursor.getDouble(c.getColumnIndexOrThrow(SUMMARY_TRAVEL_EXPENSE_REALIZED_VALUE))); }
        }
        return sTE;
    }
}