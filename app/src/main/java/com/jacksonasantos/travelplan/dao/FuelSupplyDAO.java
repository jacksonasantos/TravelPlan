package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.interfaces.FuelSupplyIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.FuelSupplyISchema;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class FuelSupplyDAO extends DbContentProvider implements FuelSupplyISchema, FuelSupplyIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public FuelSupplyDAO(SQLiteDatabase db) {
        super(db);
    }

    public FuelSupply fetchFuelSupplyById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = FUEL_SUPPLY_ID + " = ?";
        FuelSupply fuelSupply = new FuelSupply();
        cursor = super.query(FUEL_SUPPLY_TABLE, FUEL_SUPPLY_COLUMNS, selection, selectionArgs, FUEL_SUPPLY_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                fuelSupply = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return fuelSupply;
    }

    public FuelSupply findLastFuelSupply(Integer vehicle_id) {
        FuelSupply fuelSupply = new FuelSupply();
        cursor = super.rawQuery("SELECT " + FUEL_SUPPLY_ID + ", * " +
                        " FROM " + FUEL_SUPPLY_TABLE +
                        " WHERE " + FUEL_SUPPLY_VEHICLE_ID + "=? " +
                        " AND " + FUEL_SUPPLY_SUPPLY_DATE +"||" + FUEL_SUPPLY_ID +
                            "=(SELECT MAX( "+ FUEL_SUPPLY_SUPPLY_DATE +
                            "||"+FUEL_SUPPLY_ID +
                            ") FROM " + FUEL_SUPPLY_TABLE +
                            " WHERE " + FUEL_SUPPLY_VEHICLE_ID + " =?) " +
                        " ORDER BY "+FUEL_SUPPLY_SUPPLY_DATE,
                new String[] { String.valueOf(vehicle_id), String.valueOf(vehicle_id)});
        if (null != cursor) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                fuelSupply = cursorToEntity(cursor);
            }
            cursor.close();
        }
        return fuelSupply;
    }

    public List<FuelSupply> fetchAllFuelSupplies( boolean descOrder) {
        List<FuelSupply> fuelSupplyList = new ArrayList<>();


        String order = FUEL_SUPPLY_SUPPLY_DATE;
        if (descOrder) { order = order + " DESC"; }

        if (Globals.getInstance().getFilterVehicle()) {
            final String[] selectionArgs = { String.valueOf(Globals.getInstance().getIdVehicle()) };
            final String selection = FUEL_SUPPLY_VEHICLE_ID + " = ?";

            cursor = super.query(FUEL_SUPPLY_TABLE, FUEL_SUPPLY_COLUMNS, selection, selectionArgs, order);
        } else {
            cursor = super.query(FUEL_SUPPLY_TABLE, FUEL_SUPPLY_COLUMNS, null, null, order);
        }

        if (cursor.moveToFirst()) {
            do {
                FuelSupply fuelSupply = cursorToEntity(cursor);
                fuelSupplyList.add(fuelSupply);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return fuelSupplyList;
    }

    public void deleteFuelSupply(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = FUEL_SUPPLY_ID + " = ?";
        super.delete(FUEL_SUPPLY_TABLE, selection, selectionArgs);
    }

    public boolean updateFuelSupply(FuelSupply fuelSupply) {
        setContentValue(fuelSupply);
        final String[] selectionArgs = { String.valueOf(fuelSupply.getId()) };
        final String selection = FUEL_SUPPLY_ID + " = ?";
        return (super.update(FUEL_SUPPLY_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addFuelSupply(FuelSupply fuelSupply) {
        setContentValue(fuelSupply);
        return (super.insert(FUEL_SUPPLY_TABLE, getContentValue()) > 0);
    }

    protected FuelSupply cursorToEntity(Cursor cursor) {

        FuelSupply fuelSupply = new FuelSupply();

        int idIndex;
        int vehicle_idIndex;
        int gas_stationIndex;
        int gas_station_locationIndex;
        int supply_dateIndex;
        int number_litersIndex;
        int combustibleIndex;
        int full_tankIndex;
        int currency_typeIndex;
        int currency_quote_idIndex;
        int supply_valueIndex;
        int fuel_valueIndex;
        int vehicle_odometerIndex;
        int vehicle_travelled_distanceIndex;
        int stat_avg_fuel_consumptionIndex;
        int stat_cost_per_litreIndex;
        int supply_reason_typeIndex;
        int supply_reasonIndex;
        int associated_travel_idIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(FUEL_SUPPLY_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(FUEL_SUPPLY_ID);
                fuelSupply.setId(cursor.getInt(idIndex));
            }
            if (cursor.getColumnIndex(FUEL_SUPPLY_VEHICLE_ID) != -1) {
                vehicle_idIndex = cursor.getColumnIndexOrThrow(FUEL_SUPPLY_VEHICLE_ID);
                fuelSupply.setVehicle_id(cursor.getInt(vehicle_idIndex));
            }
            if (cursor.getColumnIndex(FUEL_SUPPLY_GAS_STATION) != -1) {
                gas_stationIndex = cursor.getColumnIndexOrThrow(FUEL_SUPPLY_GAS_STATION);
                fuelSupply.setGas_station(cursor.getString(gas_stationIndex));
            }
            if (cursor.getColumnIndex(FUEL_SUPPLY_GAS_STATION_LOCATION) != -1) {
                gas_station_locationIndex = cursor.getColumnIndexOrThrow(FUEL_SUPPLY_GAS_STATION_LOCATION);
                fuelSupply.setGas_station_location(cursor.getString(gas_station_locationIndex));
            }
            if (cursor.getColumnIndex(FUEL_SUPPLY_SUPPLY_DATE) != -1) {
                supply_dateIndex = cursor.getColumnIndexOrThrow(FUEL_SUPPLY_SUPPLY_DATE);
                fuelSupply.setSupply_date(Utils.dateParse(cursor.getString(supply_dateIndex)));
            }
            if (cursor.getColumnIndex(FUEL_SUPPLY_NUMBER_LITERS) != -1) {
                number_litersIndex = cursor.getColumnIndexOrThrow(FUEL_SUPPLY_NUMBER_LITERS);
                fuelSupply.setNumber_liters(cursor.getDouble(number_litersIndex));
            }
            if (cursor.getColumnIndex(FUEL_SUPPLY_COMBUSTIBLE) != -1) {
                combustibleIndex = cursor.getColumnIndexOrThrow(FUEL_SUPPLY_COMBUSTIBLE);
                fuelSupply.setCombustible(cursor.getInt(combustibleIndex));
            }
            if (cursor.getColumnIndex(FUEL_SUPPLY_FULL_TANK) != -1) {
                full_tankIndex = cursor.getColumnIndexOrThrow(FUEL_SUPPLY_FULL_TANK);
                fuelSupply.setFull_tank(cursor.getInt(full_tankIndex));
            }
            if (cursor.getColumnIndex(FUEL_SUPPLY_CURRENCY_TYPE) != -1) {
                currency_typeIndex = cursor.getColumnIndexOrThrow(FUEL_SUPPLY_CURRENCY_TYPE);
                fuelSupply.setCurrency_type(cursor.getInt(currency_typeIndex));
            }
            if (cursor.getColumnIndex(FUEL_SUPPLY_CURRENCY_QUOTE_ID) != -1) {
                currency_quote_idIndex = cursor.getColumnIndexOrThrow(FUEL_SUPPLY_CURRENCY_QUOTE_ID);
                fuelSupply.setCurrency_quote_id(cursor.getInt(currency_quote_idIndex));
            }
            if (cursor.getColumnIndex(FUEL_SUPPLY_SUPPLY_VALUE) != -1) {
                supply_valueIndex = cursor.getColumnIndexOrThrow(FUEL_SUPPLY_SUPPLY_VALUE);
                fuelSupply.setSupply_value(cursor.getDouble(supply_valueIndex));
            }
            if (cursor.getColumnIndex(FUEL_SUPPLY_FUEL_VALUE) != -1) {
                fuel_valueIndex = cursor.getColumnIndexOrThrow(FUEL_SUPPLY_FUEL_VALUE);
                fuelSupply.setFuel_value(cursor.getDouble(fuel_valueIndex));
            }
            if (cursor.getColumnIndex(FUEL_SUPPLY_VEHICLE_ODOMETER) != -1) {
                vehicle_odometerIndex = cursor.getColumnIndexOrThrow(FUEL_SUPPLY_VEHICLE_ODOMETER);
                fuelSupply.setVehicle_odometer(cursor.getInt(vehicle_odometerIndex));
            }
            if (cursor.getColumnIndex(FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE) != -1) {
                vehicle_travelled_distanceIndex = cursor.getColumnIndexOrThrow(FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE);
                fuelSupply.setVehicle_travelled_distance(cursor.getInt(vehicle_travelled_distanceIndex));
            }
            if (cursor.getColumnIndex(FUEL_SUPPLY_STAT_AVG_FUEL_CONSUMPTION) != -1) {
                stat_avg_fuel_consumptionIndex = cursor.getColumnIndexOrThrow(FUEL_SUPPLY_STAT_AVG_FUEL_CONSUMPTION);
                fuelSupply.setStat_avg_fuel_consumption(cursor.getFloat(stat_avg_fuel_consumptionIndex));
            }
            if (cursor.getColumnIndex(FUEL_SUPPLY_STAT_COST_PER_LITRE) != -1) {
                stat_cost_per_litreIndex = cursor.getColumnIndexOrThrow(FUEL_SUPPLY_STAT_COST_PER_LITRE);
                fuelSupply.setStat_cost_per_litre(cursor.getFloat(stat_cost_per_litreIndex));
            }
            if (cursor.getColumnIndex(FUEL_SUPPLY_SUPPLY_REASON_TYPE) != -1) {
                supply_reason_typeIndex = cursor.getColumnIndexOrThrow(FUEL_SUPPLY_SUPPLY_REASON_TYPE);
                fuelSupply.setSupply_reason_type(cursor.getInt(supply_reason_typeIndex));
            }
            if (cursor.getColumnIndex(FUEL_SUPPLY_SUPPLY_REASON) != -1) {
                supply_reasonIndex = cursor.getColumnIndexOrThrow(FUEL_SUPPLY_SUPPLY_REASON);
                fuelSupply.setSupply_reason(cursor.getString(supply_reasonIndex));
            }
            if (cursor.getColumnIndex(FUEL_SUPPLY_ASSOCIATED_TRAVEL_ID) != -1) {
                associated_travel_idIndex = cursor.getColumnIndexOrThrow(FUEL_SUPPLY_ASSOCIATED_TRAVEL_ID);
                fuelSupply.setAssociated_travel_id(cursor.getInt(associated_travel_idIndex));
            }
        }
        return fuelSupply;
    }

    private void setContentValue(FuelSupply fuelSupply) {

        initialValues = new ContentValues();
        initialValues.put(FUEL_SUPPLY_ID, fuelSupply.id);
        initialValues.put(FUEL_SUPPLY_VEHICLE_ID, fuelSupply.vehicle_id);
        initialValues.put(FUEL_SUPPLY_GAS_STATION, fuelSupply.gas_station);
        initialValues.put(FUEL_SUPPLY_GAS_STATION_LOCATION, fuelSupply.gas_station_location);
        initialValues.put(FUEL_SUPPLY_SUPPLY_DATE, Utils.dateFormat(fuelSupply.supply_date));
        initialValues.put(FUEL_SUPPLY_NUMBER_LITERS, fuelSupply.number_liters);
        initialValues.put(FUEL_SUPPLY_COMBUSTIBLE, fuelSupply.combustible);
        initialValues.put(FUEL_SUPPLY_FULL_TANK, fuelSupply.full_tank);
        initialValues.put(FUEL_SUPPLY_CURRENCY_TYPE, fuelSupply.currency_type);
        initialValues.put(FUEL_SUPPLY_CURRENCY_QUOTE_ID, fuelSupply.currency_quote_id);
        initialValues.put(FUEL_SUPPLY_SUPPLY_VALUE, fuelSupply.supply_value);
        initialValues.put(FUEL_SUPPLY_FUEL_VALUE, fuelSupply.fuel_value);
        initialValues.put(FUEL_SUPPLY_VEHICLE_ODOMETER, fuelSupply.vehicle_odometer);
        initialValues.put(FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE, fuelSupply.vehicle_travelled_distance);
        initialValues.put(FUEL_SUPPLY_STAT_AVG_FUEL_CONSUMPTION, fuelSupply.stat_avg_fuel_consumption);
        initialValues.put(FUEL_SUPPLY_STAT_COST_PER_LITRE, fuelSupply.stat_cost_per_litre);
        initialValues.put(FUEL_SUPPLY_SUPPLY_REASON_TYPE, fuelSupply.supply_reason_type);
        initialValues.put(FUEL_SUPPLY_SUPPLY_REASON, fuelSupply.supply_reason);
        initialValues.put(FUEL_SUPPLY_ASSOCIATED_TRAVEL_ID, fuelSupply.associated_travel_id);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}
