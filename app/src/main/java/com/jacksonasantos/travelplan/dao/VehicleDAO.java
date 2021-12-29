package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleISchema;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VehicleDAO extends DbContentProvider implements VehicleISchema, VehicleIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public VehicleDAO(SQLiteDatabase db) {
        super(db);
    }

    public Vehicle fetchVehicleById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = VEHICLE_ID + " = ?";
        Vehicle vehicle = new Vehicle();
        try {
            cursor = super.query(VEHICLE_TABLE, VEHICLE_COLUMNS, selection, selectionArgs, VEHICLE_ID);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    vehicle = cursorToEntity(cursor);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (SQLiteConstraintException ex) {
            Log.w("Update Table", Objects.requireNonNull(ex.getMessage()));
        }
        return vehicle;
    }

    public List<Vehicle> fetchAllVehicles() {
        List<Vehicle> vehicleList = new ArrayList<>();

        if (Globals.getInstance().getFilterVehicle()) {
            final String[] selectionArgs = { String.valueOf(Globals.getInstance().getIdVehicle()) };
            final String selection = VEHICLE_ID + " = ?";

            cursor = super.query(VEHICLE_TABLE, VEHICLE_COLUMNS, selection, selectionArgs, VEHICLE_SHORT_NAME);
        } else {
            cursor = super.query(VEHICLE_TABLE, VEHICLE_COLUMNS, null, null, VEHICLE_SHORT_NAME);
        }

        if (cursor.moveToFirst()) {
            do {
                Vehicle vehicle = cursorToEntity(cursor);
                vehicleList.add(vehicle);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return vehicleList;
    }

    public ArrayList<Vehicle> fetchArrayVehicles(){
        ArrayList<Vehicle> vehicleList = new ArrayList<>();
        final String selection = VEHICLE_DT_SALE + " IS NULL";
        Cursor cursor = super.query(VEHICLE_TABLE, VEHICLE_COLUMNS, selection,null, VEHICLE_ID);
        if(cursor != null && cursor.moveToFirst()){
            do{
                Vehicle vehicle = cursorToEntity(cursor);
                vehicleList.add(vehicle);
            }while(cursor.moveToNext());
        }
        return vehicleList;
    }

    public Cursor selectVehicles(){
        String[] VEHICLE_COLUMNS = new String[] {
                VEHICLE_ID,
                VEHICLE_NAME
        };
        return super.query(VEHICLE_TABLE, VEHICLE_COLUMNS, null,null, VEHICLE_NAME);
    }

    public void deleteVehicle(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = VEHICLE_ID + " = ?";
        super.delete(VEHICLE_TABLE, selection, selectionArgs);
    }

    public boolean updateVehicle(Vehicle vehicle) {
        setContentValue(vehicle);
        final String[] selectionArgs = { String.valueOf(vehicle.getId()) };
        final String selection = VEHICLE_ID + " = ?";
        return (super.update(VEHICLE_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addVehicle(Vehicle vehicle) {
        setContentValue(vehicle);
        return (super.insert(VEHICLE_TABLE, getContentValue()) > 0);
    }

    protected Vehicle cursorToEntity(Cursor c) {
        Vehicle v = new Vehicle();
        if (c != null) {
            if (c.getColumnIndex(VEHICLE_ID) != -1)                         {v.setId( c.getInt(c.getColumnIndexOrThrow(VEHICLE_ID))); }
            if (c.getColumnIndex(VEHICLE_IMAGE) != -1)                      {v.setImage(c.getBlob(c.getColumnIndexOrThrow(VEHICLE_IMAGE))); }
            if (c.getColumnIndex(VEHICLE_VEHICLE_TYPE) != -1)               {v.setVehicle_type(c.getInt(c.getColumnIndexOrThrow(VEHICLE_VEHICLE_TYPE))); }
            if (c.getColumnIndex(VEHICLE_NAME) != -1)                       {v.setName(c.getString(c.getColumnIndexOrThrow(VEHICLE_NAME))); }
            if (c.getColumnIndex(VEHICLE_SHORT_NAME) != -1)                 {v.setShort_name(c.getString( c.getColumnIndexOrThrow(VEHICLE_SHORT_NAME))); }
            if (c.getColumnIndex(VEHICLE_LICENCE_PLATE) != -1)              {v.setLicense_plate(c.getString(c.getColumnIndexOrThrow(VEHICLE_LICENCE_PLATE))); }
            if (c.getColumnIndex(VEHICLE_FULL_CAPACITY) != -1)              {v.setFull_capacity(c.getInt(c.getColumnIndexOrThrow(VEHICLE_FULL_CAPACITY))); }
            if (c.getColumnIndex(VEHICLE_AVG_CONSUMPTION) != -1)            {v.setAvg_consumption(c.getFloat(c.getColumnIndexOrThrow(VEHICLE_AVG_CONSUMPTION))); }
            if (c.getColumnIndex(VEHICLE_AVG_COST_LITRE) != -1)             {v.setAvg_cost_litre(c.getFloat(c.getColumnIndexOrThrow(VEHICLE_AVG_COST_LITRE))); }
            if (c.getColumnIndex(VEHICLE_BRAND) != -1)                      {v.setBrand(c.getString(c.getColumnIndexOrThrow(VEHICLE_BRAND))); }
            if (c.getColumnIndex(VEHICLE_FUEL_TYPE) != -1)                  {v.setFuel_type(c.getInt(c.getColumnIndexOrThrow(VEHICLE_FUEL_TYPE))); }
            if (c.getColumnIndex(VEHICLE_DT_ACQUISITION) != -1)             {v.setDt_acquisition(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(VEHICLE_DT_ACQUISITION)))); }
            if (c.getColumnIndex(VEHICLE_DT_SALE) != -1)                    {v.setDt_sale(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(VEHICLE_DT_SALE)))); }
            if (c.getColumnIndex(VEHICLE_DT_ODOMETER) != -1)                {v.setDt_odometer(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(VEHICLE_DT_ODOMETER)))); }
            if (c.getColumnIndex(VEHICLE_ODOMETER) != -1)                   {v.setOdometer(c.getInt(c.getColumnIndexOrThrow(VEHICLE_ODOMETER))); }
            if (c.getColumnIndex(VEHICLE_MODEL) != -1)                      {v.setModel(c.getString(c.getColumnIndexOrThrow(VEHICLE_MODEL))); }
            if (c.getColumnIndex(VEHICLE_COLOR) != -1)                      {v.setColor(c.getString(c.getColumnIndexOrThrow(VEHICLE_COLOR))); }
            if (c.getColumnIndex(VEHICLE_COLOR_CODE) != -1)                 {v.setColor_code(c.getInt(c.getColumnIndexOrThrow(VEHICLE_COLOR_CODE))); }
            if (c.getColumnIndex(VEHICLE_YEAR_MODEL) != -1)                 {v.setYear_model(c.getString(c.getColumnIndexOrThrow(VEHICLE_YEAR_MODEL))); }
            if (c.getColumnIndex(VEHICLE_YEAR_MANUFACTURE) != -1)           {v.setYear_manufacture(c.getString(c.getColumnIndexOrThrow(VEHICLE_YEAR_MANUFACTURE))); }
            if (c.getColumnIndex(VEHICLE_VIN) != -1)                        {v.setVin(c.getString(c.getColumnIndexOrThrow(VEHICLE_VIN))); }
            if (c.getColumnIndex(VEHICLE_LICENCE_NUMBER) != -1)             {v.setLicence_number(c.getString(c.getColumnIndexOrThrow(VEHICLE_LICENCE_NUMBER))); }
            if (c.getColumnIndex(VEHICLE_STATE) != -1)                      {v.setState(c.getString(c.getColumnIndexOrThrow(VEHICLE_STATE))); }
            if (c.getColumnIndex(VEHICLE_CITY) != -1)                       {v.setCity(c.getString(c.getColumnIndexOrThrow(VEHICLE_CITY))); }
            if (c.getColumnIndex(VEHICLE_DOORS) != -1)                      {v.setDoors(c.getInt(c.getColumnIndexOrThrow(VEHICLE_DOORS))); }
            if (c.getColumnIndex(VEHICLE_CAPACITY) != -1)                   {v.setCapacity(c.getInt(c.getColumnIndexOrThrow(VEHICLE_CAPACITY))); }
            if (c.getColumnIndex(VEHICLE_POWER) != -1)                      {v.setPower(c.getInt(c.getColumnIndexOrThrow(VEHICLE_POWER))); }
            if (c.getColumnIndex(VEHICLE_ESTIMATED_VALUE) != -1)            {v.setEstimated_value(c.getDouble(c.getColumnIndexOrThrow(VEHICLE_ESTIMATED_VALUE))); }
            if (c.getColumnIndex(VEHICLE_DT_LAST_FUELING) != -1)            {v.setDt_last_fueling(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(VEHICLE_DT_LAST_FUELING)))); }
            if (c.getColumnIndex(VEHICLE_LAST_SUPPLY_REASON_TYPE) != -1)    {v.setLast_supply_reason_type(c.getInt(c.getColumnIndexOrThrow(VEHICLE_LAST_SUPPLY_REASON_TYPE))); }
            if (c.getColumnIndex(VEHICLE_ACCUMULATED_NUMBER_LITERS) != -1)  {v.setAccumulated_number_liters(c.getDouble(c.getColumnIndexOrThrow(VEHICLE_ACCUMULATED_NUMBER_LITERS))); }
            if (c.getColumnIndex(VEHICLE_ACCUMULATED_SUPPLY_VALUE) != -1)   {v.setAccumulated_supply_value(c.getDouble(c.getColumnIndexOrThrow(VEHICLE_ACCUMULATED_SUPPLY_VALUE))); }
        }
        return v;
    }

    private void setContentValue(Vehicle v) {
        initialValues = new ContentValues();
        initialValues.put(VEHICLE_ID, v.id);
        initialValues.put(VEHICLE_IMAGE, v.image);
        initialValues.put(VEHICLE_VEHICLE_TYPE, v.vehicle_type);
        initialValues.put(VEHICLE_NAME, v.name);
        initialValues.put(VEHICLE_LICENCE_PLATE, v.license_plate);
        initialValues.put(VEHICLE_FULL_CAPACITY, v.full_capacity);
        initialValues.put(VEHICLE_AVG_CONSUMPTION, v.avg_consumption);
        initialValues.put(VEHICLE_BRAND, v.brand);
        initialValues.put(VEHICLE_FUEL_TYPE, v.fuel_type);
        initialValues.put(VEHICLE_SHORT_NAME, v.short_name);
        initialValues.put(VEHICLE_DT_ACQUISITION, Utils.dateFormat(v.dt_acquisition));
        initialValues.put(VEHICLE_DT_SALE, Utils.dateFormat(v.dt_sale));
        initialValues.put(VEHICLE_DT_ODOMETER, Utils.dateFormat(v.dt_odometer));
        initialValues.put(VEHICLE_ODOMETER, v.odometer);
        initialValues.put(VEHICLE_MODEL, v.model);
        initialValues.put(VEHICLE_COLOR, v.color);
        initialValues.put(VEHICLE_COLOR_CODE, v.color_code);
        initialValues.put(VEHICLE_YEAR_MODEL, v.year_model);
        initialValues.put(VEHICLE_YEAR_MANUFACTURE, v.year_manufacture);
        initialValues.put(VEHICLE_VIN, v.vin);
        initialValues.put(VEHICLE_LICENCE_NUMBER, v.licence_number);
        initialValues.put(VEHICLE_STATE, v.state);
        initialValues.put(VEHICLE_CITY, v.city);
        initialValues.put(VEHICLE_DOORS, v.doors);
        initialValues.put(VEHICLE_CAPACITY, v.capacity);
        initialValues.put(VEHICLE_POWER, v.power);
        initialValues.put(VEHICLE_ESTIMATED_VALUE, v.estimated_value);
        initialValues.put(VEHICLE_DT_LAST_FUELING, Utils.dateFormat(v.dt_last_fueling)) ;
        initialValues.put(VEHICLE_LAST_SUPPLY_REASON_TYPE, v.last_supply_reason_type);
        initialValues.put(VEHICLE_ACCUMULATED_NUMBER_LITERS, v.accumulated_number_liters);
        initialValues.put(VEHICLE_ACCUMULATED_SUPPLY_VALUE, v.accumulated_supply_value);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}
