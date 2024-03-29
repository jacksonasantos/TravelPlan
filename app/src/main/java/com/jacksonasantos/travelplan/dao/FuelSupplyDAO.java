package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
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

    public List<FuelSupply> fetchAllFuelSupplyHasTravelByTravel(Integer travel_id) {
        List<FuelSupply> fuelSupplyList = new ArrayList<>();
        String order = FUEL_SUPPLY_VEHICLE_ID + ", " + FUEL_SUPPLY_SUPPLY_DATE + ", " + FUEL_SUPPLY_ID;
        String[] selectionArgs;
        String selection;
        if (Globals.getInstance().getFilterVehicle()) {
            selectionArgs = new String[]{String.valueOf(travel_id), String.valueOf(Globals.getInstance().getIdVehicle())};
            selection = FUEL_SUPPLY_ASSOCIATED_TRAVEL_ID + " = ? AND " + FUEL_SUPPLY_VEHICLE_ID + " = ?";
        } else {
            selectionArgs = new String[]{String.valueOf(travel_id)};
            selection = FUEL_SUPPLY_ASSOCIATED_TRAVEL_ID + " = ? ";
        }
        cursor = super.query(FUEL_SUPPLY_TABLE, FUEL_SUPPLY_COLUMNS, selection, selectionArgs, order);
        if (cursor.moveToFirst()) {
            do {
                FuelSupply fuelSupply = cursorToEntity(cursor);
                fuelSupplyList.add(fuelSupply);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return fuelSupplyList;
    }

    public FuelSupply findLastFuelSupply(Integer vehicle_id) {
        FuelSupply fuelSupply = new FuelSupply();
        cursor = super.rawQuery("SELECT " + FUEL_SUPPLY_ID + ", * " +
                                     " FROM " + FUEL_SUPPLY_TABLE +
                                    " WHERE " + FUEL_SUPPLY_VEHICLE_ID + "=? " +
                                      " AND " + FUEL_SUPPLY_SUPPLY_DATE +"||" + FUEL_SUPPLY_ID +
                                           "= (SELECT MAX( "+ FUEL_SUPPLY_SUPPLY_DATE + "||"+FUEL_SUPPLY_ID + ") "+
                                               " FROM " + FUEL_SUPPLY_TABLE +
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

    public FuelSupply findAVGConsumptionTravelVehicle(Integer vehicle_id, Integer travel_id) {
        FuelSupply fuelSupply = new FuelSupply();
        cursor = super.rawQuery("SELECT SUM(" + FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + ")/SUM(" + FUEL_SUPPLY_NUMBER_LITERS +"+"+FUEL_SUPPLY_ACCUMULATED_NUMBER_LITERS+") stat_avg_fuel_consumption " +
                        " FROM " + FUEL_SUPPLY_TABLE +
                        " WHERE " + FUEL_SUPPLY_VEHICLE_ID + "= ? " +
                        " AND " + FUEL_SUPPLY_ASSOCIATED_TRAVEL_ID + " = ? " +
                        " AND " + FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + " > 0 ",
                new String[] { String.valueOf(vehicle_id), String.valueOf(travel_id)});
        if (null != cursor) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                fuelSupply = cursorToEntity(cursor);
            }
            cursor.close();
        }
        return fuelSupply;
    }

    public FuelSupply findAVGConsumptionTravelTransport( Integer transport_id, Integer travel_id) {
        FuelSupply fuelSupply = new FuelSupply();
        cursor = super.rawQuery("SELECT SUM(" + FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + ")/SUM(" + FUEL_SUPPLY_NUMBER_LITERS +"+"+FUEL_SUPPLY_ACCUMULATED_NUMBER_LITERS+") stat_avg_fuel_consumption " +
                                     " FROM " + FUEL_SUPPLY_TABLE +
                                    " WHERE " + FUEL_SUPPLY_TRANSPORT_ID + "= ? " +
                                      " AND " + FUEL_SUPPLY_ASSOCIATED_TRAVEL_ID + " = ? " +
                                      " AND " + FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + " > 0 ",
                new String[] { String.valueOf(transport_id), String.valueOf(travel_id)});
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
        if (descOrder) { order = order + " DESC "; }
        order = order + "," + FUEL_SUPPLY_VEHICLE_ID;
        order = order + "," + FUEL_SUPPLY_ID;
        if (descOrder) { order = order + " DESC "; }
        String[] selectionArgs = null;
        String selection = null;
        if (Globals.getInstance().getFilterVehicle()) {
            selectionArgs = new String[]{String.valueOf(Globals.getInstance().getIdVehicle())};
            selection = FUEL_SUPPLY_VEHICLE_ID + " = ?";
        }
        cursor = super.query(FUEL_SUPPLY_TABLE, FUEL_SUPPLY_COLUMNS, selection, selectionArgs, order);
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

    @Override
    public boolean addFuelSupply(FuelSupply fuelSupply) {
        setContentValue(fuelSupply);
        return (super.insert(FUEL_SUPPLY_TABLE, getContentValue()) > 0);
    }

    protected FuelSupply cursorToEntity(Cursor c) {
        FuelSupply fS = new FuelSupply();
        if (c != null) {
            if (c.getColumnIndex(FUEL_SUPPLY_ID) != -1)                         {fS.setId(c.getInt(c.getColumnIndexOrThrow(FUEL_SUPPLY_ID))); }
            if (c.getColumnIndex(FUEL_SUPPLY_VEHICLE_ID) != -1)                 if (c.getInt(c.getColumnIndexOrThrow(FUEL_SUPPLY_VEHICLE_ID)) == 0) fS.setVehicle_id(null);
                                                                                else fS.setVehicle_id(c.getInt(c.getColumnIndexOrThrow(FUEL_SUPPLY_VEHICLE_ID)));
            if (c.getColumnIndex(FUEL_SUPPLY_TRANSPORT_ID) != -1)               if (c.getInt(c.getColumnIndexOrThrow(FUEL_SUPPLY_TRANSPORT_ID)) == 0) fS.setTransport_id(null);
                                                                                else fS.setTransport_id(c.getInt(c.getColumnIndexOrThrow(FUEL_SUPPLY_TRANSPORT_ID)));
            if (c.getColumnIndex(FUEL_SUPPLY_GAS_STATION) != -1)                {fS.setGas_station(c.getString(c.getColumnIndexOrThrow(FUEL_SUPPLY_GAS_STATION))); }
            if (c.getColumnIndex(FUEL_SUPPLY_GAS_STATION_LOCATION) != -1)       {fS.setGas_station_location(c.getString(c.getColumnIndexOrThrow(FUEL_SUPPLY_GAS_STATION_LOCATION))); }
            if (c.getColumnIndex(FUEL_SUPPLY_SUPPLY_DATE) != -1)                {fS.setSupply_date(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(FUEL_SUPPLY_SUPPLY_DATE)))); }
            if (c.getColumnIndex(FUEL_SUPPLY_NUMBER_LITERS) != -1)              {fS.setNumber_liters(c.getDouble(c.getColumnIndexOrThrow(FUEL_SUPPLY_NUMBER_LITERS))); }
            if (c.getColumnIndex(FUEL_SUPPLY_ACCUMULATED_NUMBER_LITERS) != -1)  {fS.setAccumulated_Number_liters(c.getDouble(c.getColumnIndexOrThrow(FUEL_SUPPLY_ACCUMULATED_NUMBER_LITERS))); }
            if (c.getColumnIndex(FUEL_SUPPLY_ACCUMULATED_SUPPLY_VALUE) != -1)   {fS.setAccumulated_supply_value(c.getDouble(c.getColumnIndexOrThrow(FUEL_SUPPLY_ACCUMULATED_SUPPLY_VALUE))); }
            if (c.getColumnIndex(FUEL_SUPPLY_COMBUSTIBLE) != -1)                {fS.setCombustible(c.getInt(c.getColumnIndexOrThrow(FUEL_SUPPLY_COMBUSTIBLE))); }
            if (c.getColumnIndex(FUEL_SUPPLY_FULL_TANK) != -1)                  {fS.setFull_tank(c.getInt(c.getColumnIndexOrThrow(FUEL_SUPPLY_FULL_TANK))); }
            if (c.getColumnIndex(FUEL_SUPPLY_CURRENCY_TYPE) != -1)              {fS.setCurrency_type(c.getInt(c.getColumnIndexOrThrow(FUEL_SUPPLY_CURRENCY_TYPE))); }
            if (c.getColumnIndex(FUEL_SUPPLY_CURRENCY_QUOTE_ID) != -1)          if (c.getInt(c.getColumnIndexOrThrow(FUEL_SUPPLY_CURRENCY_QUOTE_ID)) == 0) fS.setCurrency_quote_id(null);
                                                                                else fS.setCurrency_quote_id(c.getInt(c.getColumnIndexOrThrow(FUEL_SUPPLY_CURRENCY_QUOTE_ID)));
            if (c.getColumnIndex(FUEL_SUPPLY_SUPPLY_VALUE) != -1)               {fS.setSupply_value(c.getDouble(c.getColumnIndexOrThrow(FUEL_SUPPLY_SUPPLY_VALUE))); }
            if (c.getColumnIndex(FUEL_SUPPLY_FUEL_VALUE) != -1)                 {fS.setFuel_value(c.getDouble(c.getColumnIndexOrThrow(FUEL_SUPPLY_FUEL_VALUE))); }
            if (c.getColumnIndex(FUEL_SUPPLY_VEHICLE_ODOMETER) != -1)           {fS.setVehicle_odometer(c.getInt(c.getColumnIndexOrThrow(FUEL_SUPPLY_VEHICLE_ODOMETER))); }
            if (c.getColumnIndex(FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE) != -1) {fS.setVehicle_travelled_distance(c.getInt(c.getColumnIndexOrThrow(FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE))); }
            if (c.getColumnIndex(FUEL_SUPPLY_STAT_AVG_FUEL_CONSUMPTION) != -1)  {fS.setStat_avg_fuel_consumption(c.getFloat(c.getColumnIndexOrThrow(FUEL_SUPPLY_STAT_AVG_FUEL_CONSUMPTION))); }
            if (c.getColumnIndex(FUEL_SUPPLY_STAT_COST_PER_LITRE) != -1)        {fS.setStat_cost_per_litre(c.getFloat(c.getColumnIndexOrThrow(FUEL_SUPPLY_STAT_COST_PER_LITRE))); }
            if (c.getColumnIndex(FUEL_SUPPLY_SUPPLY_REASON_TYPE) != -1)         {fS.setSupply_reason_type(c.getInt(c.getColumnIndexOrThrow(FUEL_SUPPLY_SUPPLY_REASON_TYPE))); }
            if (c.getColumnIndex(FUEL_SUPPLY_SUPPLY_REASON) != -1)              {fS.setSupply_reason(c.getString(c.getColumnIndexOrThrow(FUEL_SUPPLY_SUPPLY_REASON))); }
            if (c.getColumnIndex(FUEL_SUPPLY_ASSOCIATED_TRAVEL_ID) != -1)       if (c.getInt(c.getColumnIndexOrThrow(FUEL_SUPPLY_ASSOCIATED_TRAVEL_ID)) == 0) fS.setAssociated_travel_id(null);
                                                                                else fS.setAssociated_travel_id(c.getInt(c.getColumnIndexOrThrow(FUEL_SUPPLY_ASSOCIATED_TRAVEL_ID)));
            if (c.getColumnIndex(FUEL_SUPPLY_ACCOUNT_ID) != -1)                 if (c.getInt(c.getColumnIndexOrThrow(FUEL_SUPPLY_ACCOUNT_ID)) == 0) fS.setAccount_id(null);
                                                                                else fS.setAccount_id(c.getInt(c.getColumnIndexOrThrow(FUEL_SUPPLY_ACCOUNT_ID)));
        }
        return fS;
    }

    private void setContentValue(FuelSupply fS) {
        initialValues = new ContentValues();
        initialValues.put(FUEL_SUPPLY_ID, fS.id);
        initialValues.put(FUEL_SUPPLY_VEHICLE_ID, fS.vehicle_id);
        initialValues.put(FUEL_SUPPLY_TRANSPORT_ID, fS.transport_id);
        initialValues.put(FUEL_SUPPLY_GAS_STATION, fS.gas_station);
        initialValues.put(FUEL_SUPPLY_GAS_STATION_LOCATION, fS.gas_station_location);
        initialValues.put(FUEL_SUPPLY_SUPPLY_DATE, Utils.dateFormat(fS.supply_date));
        initialValues.put(FUEL_SUPPLY_NUMBER_LITERS, fS.number_liters);
        initialValues.put(FUEL_SUPPLY_ACCUMULATED_NUMBER_LITERS, fS.accumulated_number_liters);
        initialValues.put(FUEL_SUPPLY_ACCUMULATED_SUPPLY_VALUE, fS.accumulated_supply_value);
        initialValues.put(FUEL_SUPPLY_COMBUSTIBLE, fS.combustible);
        initialValues.put(FUEL_SUPPLY_FULL_TANK, fS.full_tank);
        initialValues.put(FUEL_SUPPLY_CURRENCY_TYPE, fS.currency_type);
        initialValues.put(FUEL_SUPPLY_CURRENCY_QUOTE_ID, fS.currency_quote_id);
        initialValues.put(FUEL_SUPPLY_SUPPLY_VALUE, fS.supply_value);
        initialValues.put(FUEL_SUPPLY_FUEL_VALUE, fS.fuel_value);
        initialValues.put(FUEL_SUPPLY_VEHICLE_ODOMETER, fS.vehicle_odometer);
        initialValues.put(FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE, fS.vehicle_travelled_distance);
        initialValues.put(FUEL_SUPPLY_STAT_AVG_FUEL_CONSUMPTION, fS.stat_avg_fuel_consumption);
        initialValues.put(FUEL_SUPPLY_STAT_COST_PER_LITRE, fS.stat_cost_per_litre);
        initialValues.put(FUEL_SUPPLY_SUPPLY_REASON_TYPE, fS.supply_reason_type);
        initialValues.put(FUEL_SUPPLY_SUPPLY_REASON, fS.supply_reason);
        initialValues.put(FUEL_SUPPLY_ASSOCIATED_TRAVEL_ID, fS.associated_travel_id);
        initialValues.put(FUEL_SUPPLY_ACCOUNT_ID, fS.account_id);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}