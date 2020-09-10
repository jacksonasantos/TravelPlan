package com.jacksonasantos.travelplan.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jacksonasantos.travelplan.DAO.Interface.VehicleIDAO;
import com.jacksonasantos.travelplan.DAO.Interface.VehicleISchema;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO extends DbContentProvider implements VehicleISchema, VehicleIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public VehicleDAO(SQLiteDatabase db) {
        super(db);
    }

    public Vehicle fetchVehicleById(int id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = VEHICLE_ID + " = ?";
        Vehicle vehicle = new Vehicle();
        cursor = super.query(VEHICLE_TABLE, VEHICLE_COLUMNS, selection, selectionArgs, VEHICLE_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                vehicle = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return vehicle;
    }

        public List<Vehicle> fetchAllVehicles() {
        List<Vehicle> vehicleList = new ArrayList<>();

        cursor = super.query(VEHICLE_TABLE, VEHICLE_COLUMNS, null,null, VEHICLE_ID);

        if (cursor.moveToFirst()) {
            do {
                Vehicle vehicle = cursorToEntity(cursor);
                vehicleList.add(vehicle);
                } while (cursor.moveToNext());

            cursor.close();
        }
        return vehicleList;
    }

    public boolean deleteVehicle(int id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = VEHICLE_ID + " = ?";
        try {
            return super.delete(VEHICLE_TABLE, selection, selectionArgs) > 0;
        } catch (SQLiteConstraintException ex){
            Log.w("Delete Table", ex.getMessage());
            return false;
        }
    }

    public boolean updateVehicle(Vehicle vehicle) {
        setContentValue(vehicle);
        final String[] selectionArgs = { String.valueOf(vehicle.getId()) };
        final String selection = VEHICLE_ID + " = ?";
        try {
            return super.update(VEHICLE_TABLE, getContentValue(), selection, selectionArgs) > 0;
        } catch (SQLiteConstraintException ex){
            Log.w("Update Table", ex.getMessage());
            return false;
        }
    }

    public boolean addVehicle(Vehicle vehicle) {
        // set values
        setContentValue(vehicle);
        try {
            return super.insert(VEHICLE_TABLE, getContentValue()) > 0;
        } catch (SQLiteConstraintException ex){
            Log.w("Insert Table", ex.getMessage());
            return false;
        }
    }

    protected Vehicle cursorToEntity(Cursor cursor) {

        Vehicle vehicle = new Vehicle();

        int idIndex;
        int typeIndex;
        int nameIndex;
        int short_nameIndex;
        int license_plateIndex;
        int full_capacityIndex;
        int avg_consumptionIndex;
        int brandIndex;
        int type_fuelIndex;
        int dt_acquisitionIndex;
        int dt_saleIndex;
        int dt_odometerIndex;
        int odometerIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(VEHICLE_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(VEHICLE_ID);
                vehicle.id = cursor.getLong(idIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_TYPE) != -1) {
                typeIndex = cursor.getColumnIndexOrThrow(VEHICLE_TYPE);
                vehicle.type = cursor.getInt(typeIndex);
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
                vehicle.avg_consumption = cursor.getDouble(avg_consumptionIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_BRAND) != -1) {
                brandIndex = cursor.getColumnIndexOrThrow(VEHICLE_BRAND);
                vehicle.brand = cursor.getString(brandIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_TYPE_FUEL) != -1) {
                type_fuelIndex = cursor.getColumnIndexOrThrow(VEHICLE_TYPE_FUEL);
                vehicle.type_fuel = cursor.getString(type_fuelIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_DT_ACQUISITION) != -1) {
                dt_acquisitionIndex = cursor.getColumnIndexOrThrow(VEHICLE_DT_ACQUISITION);
                vehicle.dt_acquisition = cursor.getLong(dt_acquisitionIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_DT_SALE) != -1) {
                dt_saleIndex = cursor.getColumnIndexOrThrow(VEHICLE_DT_SALE);
                vehicle.dt_sale = cursor.getLong(dt_saleIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_DT_ODOMETER) != -1) {
                dt_odometerIndex = cursor.getColumnIndexOrThrow(VEHICLE_DT_ODOMETER);
                vehicle.dt_odometer = cursor.getLong(dt_odometerIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_ODOMETER) != -1) {
                odometerIndex = cursor.getColumnIndexOrThrow(VEHICLE_ODOMETER);
                vehicle.odometer = cursor.getInt(odometerIndex);
            }
        }
        return vehicle;
    }

    private void setContentValue(Vehicle vehicle) {
        initialValues = new ContentValues();
        initialValues.put(VEHICLE_TYPE, vehicle.type);
        initialValues.put(VEHICLE_NAME, vehicle.name);
        initialValues.put(VEHICLE_LICENCE_PLATE, vehicle.license_plate);
        initialValues.put(VEHICLE_FULL_CAPACITY, vehicle.full_capacity);
        initialValues.put(VEHICLE_AVG_CONSUMPTION, vehicle.avg_consumption);
        initialValues.put(VEHICLE_BRAND, vehicle.brand);
        initialValues.put(VEHICLE_TYPE_FUEL, vehicle.type_fuel);
        initialValues.put(VEHICLE_SHORT_NAME, vehicle.short_name);
        initialValues.put(VEHICLE_DT_ACQUISITION, String.valueOf(vehicle.dt_acquisition));
        initialValues.put(VEHICLE_DT_SALE, String.valueOf(vehicle.dt_sale));
        initialValues.put(VEHICLE_DT_ODOMETER, String.valueOf(vehicle.dt_odometer));
        initialValues.put(VEHICLE_ODOMETER, vehicle.odometer);

    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}
