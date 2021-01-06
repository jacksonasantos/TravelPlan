package com.jacksonasantos.travelplan.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.MainActivity;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.interfaces.FuelSupplyISchema;
import com.jacksonasantos.travelplan.dao.interfaces.InsuranceISchema;
import com.jacksonasantos.travelplan.dao.interfaces.TravelExpenseIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.TravelExpenseISchema;

import java.util.ArrayList;
import java.util.List;

public class TravelExpenseDAO extends DbContentProvider implements TravelExpenseISchema, TravelExpenseIDAO {

    private Cursor cursor;

    public TravelExpenseDAO(SQLiteDatabase db) {
        super(db);
    }

    public List<TravelExpense> findTravelExpense(Integer travel_id) {
        List<TravelExpense> travelExpenseList = new ArrayList<>();

        cursor = super.rawQuery(
                "SELECT '" + MainActivity.getAppResources().getString(R.string.Combustible) + "' " + TravelExpenseISchema.TRAVEL_EXPENSE_EXPENSE + ", " +       // Combustivel
                          " 0 " + TravelExpenseISchema.TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                          " SUM (" + FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_VALUE +") " + TravelExpenseISchema.TRAVEL_EXPENSE_REALIZED_VALUE +
                     " FROM " + FuelSupplyISchema.FUEL_SUPPLY_TABLE +
                    " WHERE " + FuelSupplyISchema.FUEL_SUPPLY_ASSOCIATED_TRAVEL_ID + " = ? " +
                    " UNION " +
                    "SELECT '" + MainActivity.getAppResources().getString(R.string.Food) + "' " + TravelExpenseISchema.TRAVEL_EXPENSE_EXPENSE + ", " +              //  TODO - Calcular quando implementado Pedágio
                           " 0 " + TravelExpenseISchema.TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                           " 0 " + TravelExpenseISchema.TRAVEL_EXPENSE_REALIZED_VALUE +
                    " UNION " +
                    "SELECT '" + MainActivity.getAppResources().getString(R.string.Toll) + "' " + TravelExpenseISchema.TRAVEL_EXPENSE_EXPENSE + ", " +              //  TODO - Calcular quando implementado Alimentação
                          " 0 " + TravelExpenseISchema.TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                          " 0 " + TravelExpenseISchema.TRAVEL_EXPENSE_REALIZED_VALUE +
                    " UNION " +
                    "SELECT '" + MainActivity.getAppResources().getString(R.string.Tours) + "' " + TravelExpenseISchema.TRAVEL_EXPENSE_EXPENSE + ", " +              //  TODO - Calcular quando implementado Passeios
                          " 0 " + TravelExpenseISchema.TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                          " 0 " + TravelExpenseISchema.TRAVEL_EXPENSE_REALIZED_VALUE +
                    " UNION " +
                    "SELECT '" + MainActivity.getAppResources().getString(R.string.Accommodation) + "' " + TravelExpenseISchema.TRAVEL_EXPENSE_EXPENSE + ", " +      //  TODO - Calcular quando implementado Hospedagem
                          " 0 " + TravelExpenseISchema.TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                          " 0 " + TravelExpenseISchema.TRAVEL_EXPENSE_REALIZED_VALUE +
                    " UNION " +
                    "SELECT '" + MainActivity.getAppResources().getString(R.string.Extras) + "' " + TravelExpenseISchema.TRAVEL_EXPENSE_EXPENSE + ", " +             //  TODO - Calcular quando implementado Extras
                          " 0 " + TravelExpenseISchema.TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                          " 0 " + TravelExpenseISchema.TRAVEL_EXPENSE_REALIZED_VALUE +
                    " UNION " +
                   " SELECT '" + MainActivity.getAppResources().getString(R.string.Insurance) + "' "+ TravelExpenseISchema.TRAVEL_EXPENSE_EXPENSE + ", " +          // Seguros
                          " 0 " + TravelExpenseISchema.TRAVEL_EXPENSE_EXPECTED_VALUE + ", " +
                          " SUM (" + InsuranceISchema.INSURANCE_TOTAL_PREMIUM_VALUE + ") " + TravelExpenseISchema.TRAVEL_EXPENSE_REALIZED_VALUE +
                     " FROM " + InsuranceISchema.INSURANCE_TABLE +
                    " WHERE " + InsuranceISchema.INSURANCE_TRAVEL_ID + " = ? " ,
                new String[] { String.valueOf(travel_id), String.valueOf(travel_id)});
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    TravelExpense travelExpense = cursorToEntity(cursor);
                    travelExpenseList.add(travelExpense);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return travelExpenseList;
    }

    protected TravelExpense cursorToEntity(Cursor cursor) {

        TravelExpense travelExpense = new TravelExpense();

        int expenseIndex;
        int expected_valueIndex;
        int realized_valueIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(TRAVEL_EXPENSE_EXPENSE) != -1) {
                expenseIndex = cursor.getColumnIndexOrThrow(TRAVEL_EXPENSE_EXPENSE);
                travelExpense.setExpense(cursor.getString(expenseIndex));
            }
            if (cursor.getColumnIndex(TRAVEL_EXPENSE_EXPECTED_VALUE) != -1) {
                expected_valueIndex = cursor.getColumnIndexOrThrow(TRAVEL_EXPENSE_EXPECTED_VALUE);
                travelExpense.setExpected_value(cursor.getDouble(expected_valueIndex));
            }
            if (cursor.getColumnIndex(TRAVEL_EXPENSE_REALIZED_VALUE) != -1) {
                realized_valueIndex = cursor.getColumnIndexOrThrow(TRAVEL_EXPENSE_REALIZED_VALUE);
                travelExpense.setRealized_value(cursor.getDouble(realized_valueIndex));
            }
        }
        return travelExpense;
    }
}
