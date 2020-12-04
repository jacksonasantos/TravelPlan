package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jacksonasantos.travelplan.dao.interfaces.VehicleIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleISchema;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class VehicleDAO extends DbContentProvider implements VehicleISchema, VehicleIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public VehicleDAO(SQLiteDatabase db) {
        super(db);
    }

    public Vehicle fetchVehicleById(Long id) {
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
            Log.w("Update Table", ex.getMessage());
        }
        return vehicle;
    }

    public List<Vehicle> fetchAllVehicles() {
        List<Vehicle> vehicleList = new ArrayList<>();

        if (Globals.getInstance().getFilterVehicle()) {
            final String[] selectionArgs = { String.valueOf(Globals.getInstance().getIdVehicle()) };
            final String selection = VEHICLE_ID + " = ?";

            cursor = super.query(VEHICLE_TABLE, VEHICLE_COLUMNS, selection, selectionArgs, VEHICLE_ID);
        } else {
            cursor = super.query(VEHICLE_TABLE, VEHICLE_COLUMNS, null, null, VEHICLE_ID);
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
        Cursor cursor = super.query(VEHICLE_TABLE, VEHICLE_COLUMNS, null,null, VEHICLE_ID);
        if(cursor != null && cursor.moveToFirst()){
            do{
                Vehicle vehicle = cursorToEntity(cursor);
                vehicleList.add(vehicle);
            }while(cursor.moveToNext());
        }
        return vehicleList;
    }

    public void deleteVehicle(Long id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = VEHICLE_ID + " = ?";
        try {
            super.delete(VEHICLE_TABLE, selection, selectionArgs);
        } catch (SQLiteConstraintException ex){
            Log.w("Delete Table", ex.getMessage());
        }
    }

    public boolean updateVehicle(Vehicle vehicle) {
        setContentValue(vehicle);
        final String[] selectionArgs = { String.valueOf(vehicle.getId()) };
        final String selection = VEHICLE_ID + " = ?";
        try {
            return (super.update(VEHICLE_TABLE, getContentValue(), selection, selectionArgs) > 0);
        } catch (SQLiteConstraintException ex){
            Log.w("Update Table", ex.getMessage());
            return false;
        }
    }

    public boolean addVehicle(Vehicle vehicle) {
        setContentValue(vehicle);
        try {
            return (super.insert(VEHICLE_TABLE, getContentValue()) > 0);
        } catch (SQLiteConstraintException ex){
            Log.w("Insert Table", ex.getMessage());
            return false;
        }
    }

    protected Vehicle cursorToEntity(Cursor cursor) {

        Vehicle vehicle = new Vehicle();

        int idIndex;
        int vehicle_typeIndex;
        int nameIndex;
        int short_nameIndex;
        int license_plateIndex;
        int full_capacityIndex;
        int avg_consumptionIndex;
        int brandIndex;
        int fuel_typeIndex;
        int dt_acquisitionIndex;
        int dt_saleIndex;
        int dt_odometerIndex;
        int odometerIndex;
        int modelIndex;
        int colorIndex;
        int year_modelIndex;
        int year_manufactureIndex;
        int vinIndex;
        int licence_numberIndex;
        int stateIndex;
        int cityIndex;
        int doorsIndex;
        int capacityIndex;
        int powerIndex;
        int estimated_valueIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(VEHICLE_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(VEHICLE_ID);
                vehicle.id = cursor.getLong(idIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_VEHICLE_TYPE) != -1) {
                vehicle_typeIndex = cursor.getColumnIndexOrThrow(VEHICLE_VEHICLE_TYPE);
                vehicle.vehicle_type = cursor.getInt(vehicle_typeIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_NAME) != -1) {
                nameIndex = cursor.getColumnIndexOrThrow(VEHICLE_NAME);
                vehicle.name = cursor.getString(nameIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_SHORT_NAME) != -1) {
                short_nameIndex = cursor.getColumnIndexOrThrow(VEHICLE_SHORT_NAME);
                vehicle.short_name = cursor.getString(short_nameIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_LICENCE_PLATE) != -1) {
                license_plateIndex = cursor.getColumnIndexOrThrow(VEHICLE_LICENCE_PLATE);
                vehicle.license_plate = cursor.getString(license_plateIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_FULL_CAPACITY) != -1) {
                full_capacityIndex = cursor.getColumnIndexOrThrow(VEHICLE_FULL_CAPACITY);
                vehicle.full_capacity = cursor.getInt(full_capacityIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_AVG_CONSUMPTION) != -1) {
                avg_consumptionIndex = cursor.getColumnIndexOrThrow(VEHICLE_AVG_CONSUMPTION);
                vehicle.avg_consumption = cursor.getFloat(avg_consumptionIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_BRAND) != -1) {
                brandIndex = cursor.getColumnIndexOrThrow(VEHICLE_BRAND);
                vehicle.brand = cursor.getString(brandIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_FUEL_TYPE) != -1) {
                fuel_typeIndex = cursor.getColumnIndexOrThrow(VEHICLE_FUEL_TYPE);
                vehicle.fuel_type = cursor.getInt(fuel_typeIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_DT_ACQUISITION) != -1) {
                dt_acquisitionIndex = cursor.getColumnIndexOrThrow(VEHICLE_DT_ACQUISITION);
                vehicle.dt_acquisition = Utils.stringToDate(cursor.getString(dt_acquisitionIndex));
            }
            if (cursor.getColumnIndex(VEHICLE_DT_SALE) != -1) {
                dt_saleIndex = cursor.getColumnIndexOrThrow(VEHICLE_DT_SALE);
                vehicle.dt_sale = Utils.stringToDate(cursor.getString(dt_saleIndex));
            }
            if (cursor.getColumnIndex(VEHICLE_DT_ODOMETER) != -1) {
                dt_odometerIndex = cursor.getColumnIndexOrThrow(VEHICLE_DT_ODOMETER);
                vehicle.dt_odometer = Utils.stringToDate(cursor.getString(dt_odometerIndex));
            }
            if (cursor.getColumnIndex(VEHICLE_ODOMETER) != -1) {
                odometerIndex = cursor.getColumnIndexOrThrow(VEHICLE_ODOMETER);
                vehicle.odometer = cursor.getInt(odometerIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_MODEL) != -1) {
                modelIndex = cursor.getColumnIndexOrThrow(VEHICLE_MODEL);
                vehicle.model = cursor.getString(modelIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_COLOR) != -1) {
                colorIndex = cursor.getColumnIndexOrThrow(VEHICLE_COLOR);
                vehicle.color = cursor.getString(colorIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_YEAR_MODEL) != -1) {
                year_modelIndex = cursor.getColumnIndexOrThrow(VEHICLE_YEAR_MODEL);
                vehicle.year_model = cursor.getString(year_modelIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_YEAR_MANUFACTURE) != -1) {
                year_manufactureIndex = cursor.getColumnIndexOrThrow(VEHICLE_YEAR_MANUFACTURE);
                vehicle.year_manufacture = cursor.getString(year_manufactureIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_VIN) != -1) {
                vinIndex = cursor.getColumnIndexOrThrow(VEHICLE_VIN);
                vehicle.vin = cursor.getString(vinIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_LICENCE_NUMBER) != -1) {
                licence_numberIndex = cursor.getColumnIndexOrThrow(VEHICLE_LICENCE_NUMBER);
                vehicle.licence_number = cursor.getString(licence_numberIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_STATE) != -1) {
                stateIndex = cursor.getColumnIndexOrThrow(VEHICLE_STATE);
                vehicle.state = cursor.getString(stateIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_CITY) != -1) {
                cityIndex = cursor.getColumnIndexOrThrow(VEHICLE_CITY);
                vehicle.city = cursor.getString(cityIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_DOORS) != -1) {
                doorsIndex = cursor.getColumnIndexOrThrow(VEHICLE_DOORS);
                vehicle.doors = cursor.getInt(doorsIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_CAPACITY) != -1) {
                capacityIndex = cursor.getColumnIndexOrThrow(VEHICLE_CAPACITY);
                vehicle.capacity = cursor.getInt(capacityIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_POWER) != -1) {
                powerIndex = cursor.getColumnIndexOrThrow(VEHICLE_POWER);
                vehicle.power = cursor.getInt(powerIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_ESTIMATED_VALUE) != -1) {
                estimated_valueIndex = cursor.getColumnIndexOrThrow(VEHICLE_ESTIMATED_VALUE);
                vehicle.estimated_value = cursor.getDouble(estimated_valueIndex);
            }
        }
        return vehicle;
    }

    private void setContentValue(Vehicle vehicle) {
        initialValues = new ContentValues();
        initialValues.put(VEHICLE_ID, vehicle.id);
        initialValues.put(VEHICLE_VEHICLE_TYPE, vehicle.vehicle_type);
        initialValues.put(VEHICLE_NAME, vehicle.name);
        initialValues.put(VEHICLE_LICENCE_PLATE, vehicle.license_plate);
        initialValues.put(VEHICLE_FULL_CAPACITY, vehicle.full_capacity);
        initialValues.put(VEHICLE_AVG_CONSUMPTION, vehicle.avg_consumption);
        initialValues.put(VEHICLE_BRAND, vehicle.brand);
        initialValues.put(VEHICLE_FUEL_TYPE, vehicle.fuel_type);
        initialValues.put(VEHICLE_SHORT_NAME, vehicle.short_name);
        initialValues.put(VEHICLE_DT_ACQUISITION, Utils.dateToString(vehicle.dt_acquisition));
        initialValues.put(VEHICLE_DT_SALE, Utils.dateToString(vehicle.dt_sale));
        initialValues.put(VEHICLE_DT_ODOMETER, Utils.dateToString(vehicle.dt_odometer));
        initialValues.put(VEHICLE_ODOMETER, vehicle.odometer);
        initialValues.put(VEHICLE_MODEL, vehicle.model);
        initialValues.put(VEHICLE_COLOR, vehicle.color);
        initialValues.put(VEHICLE_YEAR_MODEL, vehicle.year_model);
        initialValues.put(VEHICLE_YEAR_MANUFACTURE, vehicle.year_manufacture);
        initialValues.put(VEHICLE_VIN, vehicle.vin);
        initialValues.put(VEHICLE_LICENCE_NUMBER, vehicle.licence_number);
        initialValues.put(VEHICLE_STATE, vehicle.state);
        initialValues.put(VEHICLE_CITY, vehicle.city);
        initialValues.put(VEHICLE_DOORS, vehicle.doors);
        initialValues.put(VEHICLE_CAPACITY, vehicle.capacity);
        initialValues.put(VEHICLE_POWER, vehicle.power);
        initialValues.put(VEHICLE_ESTIMATED_VALUE, vehicle.estimated_value);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}
