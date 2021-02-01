package com.jacksonasantos.travelplan.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.MainActivity;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.interfaces.FuelSupplyISchema;
import com.jacksonasantos.travelplan.dao.interfaces.InsuranceISchema;
import com.jacksonasantos.travelplan.dao.interfaces.ItineraryISchema;
import com.jacksonasantos.travelplan.dao.interfaces.ReservationISchema;
import com.jacksonasantos.travelplan.dao.interfaces.SummaryTravelExpenseIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.SummaryTravelExpenseISchema;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleHasTravelISchema;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleISchema;

import java.util.ArrayList;
import java.util.List;

public class SummaryTravelExpenseDAO extends DbContentProvider implements SummaryTravelExpenseISchema, SummaryTravelExpenseIDAO {

    private Cursor cursor;

    public SummaryTravelExpenseDAO(SQLiteDatabase db) {
        super(db);
    }

    public List<SummaryTravelExpense> findTravelExpense(Integer travel_id) {
        List<SummaryTravelExpense> summaryTravelExpenseList = new ArrayList<>();

        cursor = super.rawQuery(
                "SELECT '" + MainActivity.getAppResources().getString(R.string.Combustible) + "' " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_EXPENSE + ", " +
                        " ( SELECT SUM(i." + ItineraryISchema.ITINERARY_DISTANCE+"*v."+ VehicleISchema.VEHICLE_AVG_COST_LITRE+") " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_REALIZED_VALUE +
                            " FROM " + ItineraryISchema.ITINERARY_TABLE + " i " +
                                ", " + VehicleHasTravelISchema.VEHICLE_HAS_TRAVEL_TABLE + " vt " +
                                ", " + VehicleISchema.VEHICLE_TABLE + " v " +
                           " WHERE i." + ItineraryISchema.ITINERARY_TRAVEL_ID + " = vt." + VehicleHasTravelISchema.VEHICLE_HAS_TRAVEL_TRAVEL_ID +
                             " AND vt." + VehicleHasTravelISchema.VEHICLE_HAS_TRAVEL_VEHICLE_ID + " = v." + VehicleISchema.VEHICLE_ID +
                             " AND i." + ItineraryISchema.ITINERARY_TRAVEL_ID + " = ?) " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                        " SUM(" + FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_VALUE +") " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_REALIZED_VALUE +
                        " FROM " + FuelSupplyISchema.FUEL_SUPPLY_TABLE +
                        " WHERE " + FuelSupplyISchema.FUEL_SUPPLY_ASSOCIATED_TRAVEL_ID + " = ? " +
                        " UNION " +
                        " SELECT '" + MainActivity.getAppResources().getString(R.string.Food) + "' " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_EXPENSE + ", " +              //  TODO - Calcular quando implementado Alimentação
                        " 0 " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                        " 0 " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_REALIZED_VALUE +
                        " UNION " +
                        " SELECT '" + MainActivity.getAppResources().getString(R.string.Toll) + "' " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_EXPENSE + ", " +              //  TODO - Calcular quando implementado Pedágio
                        " 0 " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                        " 0 " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_REALIZED_VALUE +
                        " UNION " +
                        " SELECT '" + MainActivity.getAppResources().getString(R.string.Tours) + "' " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_EXPENSE + ", " +              //  TODO - Calcular quando implementado Passeios
                        " 0 " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                        " 0 " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_REALIZED_VALUE +
                        " UNION " +
                        " SELECT '" + MainActivity.getAppResources().getString(R.string.Accommodation) + "' " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_EXPENSE + ", " +
                        " SUM(" + ReservationISchema.RESERVATION_RESERVATION_AMOUNT + ") " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                        " SUM(" + ReservationISchema.RESERVATION_AMOUNT_PAID + ") " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_REALIZED_VALUE +
                        " FROM " + ReservationISchema.RESERVATION_TABLE +
                        " WHERE " + ReservationISchema.RESERVATION_TRAVEL_ID + " = ? " +
                        " UNION " +
                        " SELECT '" + MainActivity.getAppResources().getString(R.string.Extras) + "' " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_EXPENSE + ", " +             //  TODO - Calcular quando implementado Extras
                        " 0 " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                        " 0 " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_REALIZED_VALUE +
                        " UNION " +
                        " SELECT '" + MainActivity.getAppResources().getString(R.string.Insurance) + "' "+ SummaryTravelExpenseISchema.TRAVEL_EXPENSE_EXPENSE + ", " +
                        " 0 " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                        " SUM(" + InsuranceISchema.INSURANCE_TOTAL_PREMIUM_VALUE + ") " + SummaryTravelExpenseISchema.TRAVEL_EXPENSE_REALIZED_VALUE +
                        " FROM " + InsuranceISchema.INSURANCE_TABLE +
                        " WHERE " + InsuranceISchema.INSURANCE_TRAVEL_ID + " = ? " ,
                new String[] { String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(travel_id)});
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

    protected SummaryTravelExpense cursorToEntity(Cursor cursor) {

        SummaryTravelExpense summaryTravelExpense = new SummaryTravelExpense();

        int expenseIndex;
        int expected_valueIndex;
        int realized_valueIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(TRAVEL_EXPENSE_EXPENSE) != -1) {
                expenseIndex = cursor.getColumnIndexOrThrow(TRAVEL_EXPENSE_EXPENSE);
                summaryTravelExpense.setExpense(cursor.getString(expenseIndex));
            }
            if (cursor.getColumnIndex(TRAVEL_EXPENSE_EXPECTED_VALUE) != -1) {
                expected_valueIndex = cursor.getColumnIndexOrThrow(TRAVEL_EXPENSE_EXPECTED_VALUE);
                summaryTravelExpense.setExpected_value(cursor.getDouble(expected_valueIndex));
            }
            if (cursor.getColumnIndex(TRAVEL_EXPENSE_REALIZED_VALUE) != -1) {
                realized_valueIndex = cursor.getColumnIndexOrThrow(TRAVEL_EXPENSE_REALIZED_VALUE);
                summaryTravelExpense.setRealized_value(cursor.getDouble(realized_valueIndex));
            }
        }
        return summaryTravelExpense;
    }
}