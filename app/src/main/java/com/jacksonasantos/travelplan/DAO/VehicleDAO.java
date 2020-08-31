package com.jacksonasantos.travelplan.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jacksonasantos.travelplan.DAO.Interface.VehicleIDAO;
import com.jacksonasantos.travelplan.DAO.Interface.VehicleISchema;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.ID;

public class VehicleDAO extends DbContentProvider implements VehicleISchema, VehicleIDAO {

    private Cursor cursor;
    private ContentValues initialValues;
    private static final long serialVersionUID = 163383301108440384L;

    public VehicleDAO(SQLiteDatabase db) { super(db); }

    public Vehicle fetchUserById(int id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = ID + " = ?";
        Vehicle vehicle = new Vehicle();
        cursor = super.query(VEHICLE_TABLE, VEHICLE_COLUMNS, selection,
                selectionArgs, VEHICLE_ID);
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

    public List<Vehicle> fetchAllUsers() {
        List<Vehicle> vehicleList = new ArrayList<>();
        cursor = super.query(VEHICLE_TABLE, VEHICLE_COLUMNS, null,
                null, VEHICLE_ID);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Vehicle vehicle = cursorToEntity(cursor);
                vehicleList.add(vehicle);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return vehicleList;
    }

    public boolean deleteVehicle(int id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = ID + " = ?";
        Vehicle vehicle = new Vehicle();
        try {
            return super.delete(VEHICLE_TABLE, selection, selectionArgs) > 0;
        } catch (SQLiteConstraintException ex){
            Log.w("Database", ex.getMessage());
            return false;
        }
    }

    public boolean addVehicle(Vehicle vehicle) {
        // set values
        setContentValue(vehicle);
        try {
            return super.insert(VEHICLE_TABLE, getContentValue()) > 0;
        } catch (SQLiteConstraintException ex){
            Log.w("Database", ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean addVehicles(List<Vehicle> vehicles) {
        return false;
    }

    @Override
    public boolean deleteAllVehicles() {
        return false;
    }

    protected Vehicle cursorToEntity(Cursor cursor) {

        Vehicle vehicle = new Vehicle();

        int idIndex;
        int OidIndex;
        int NameIndex;
        int license_plateIndex;
        int full_capacityIndex;
        int avg_consumptionIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(VEHICLE_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(VEHICLE_ID);
                vehicle.id = cursor.getInt(idIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_OID) != -1) {
                OidIndex = cursor.getColumnIndexOrThrow(
                        VEHICLE_OID);
                vehicle.oid = cursor.getString(OidIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_NAME) != -1) {
                NameIndex = cursor.getColumnIndexOrThrow(
                        VEHICLE_NAME);
                vehicle.name = cursor.getString(NameIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_LICENCE_PLATE) != -1) {
                license_plateIndex = cursor.getColumnIndexOrThrow(
                        VEHICLE_LICENCE_PLATE);
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
        }
        return vehicle;
    }

    private void setContentValue(Vehicle vehicle) {
        initialValues = new ContentValues();
        initialValues.put(VEHICLE_ID, vehicle.id);
        initialValues.put(VEHICLE_OID, vehicle.oid);
        initialValues.put(VEHICLE_NAME, vehicle.name);
        initialValues.put(VEHICLE_LICENCE_PLATE, vehicle.license_plate);
        initialValues.put(VEHICLE_FULL_CAPACITY, vehicle.full_capacity);
        initialValues.put(VEHICLE_AVG_CONSUMPTION, vehicle.avg_consumption);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

    /*
    public static long insereVehicle(SQLiteDatabase db, String oid_vehicle, String name_vehicle, String license_plate,
                                     int full_capacity, double avg_consumption) {
        ContentValues campos = new ContentValues();
        campos.put(KEY_OID_VEHICLE, oid_vehicle);
        campos.put(KEY_NAME_VEHICLE, name_vehicle);
        campos.put(KEY_LICENCE_PLATE, license_plate);
        campos.put(KEY_FULL_CAPACITY, full_capacity);
        campos.put(KEY_AVG_CONSUMPTION, avg_consumption);
        return db.insert(NOME_TABELA, null, campos);
    }

    public boolean apagaVehicle(SQLiteDatabase db, long id) {
        return db.delete(NOME_TABELA, KEY_ID + "=" + id, null) > 0;
    }

    public Cursor retornaTodosVehicle(SQLiteDatabase db) {
        return db.query(NOME_TABELA,
                new String[]{KEY_ID,
                        KEY_OID_VEHICLE,
                        KEY_NAME_VEHICLE,
                        KEY_LICENCE_PLATE,
                        KEY_FULL_CAPACITY,
                        KEY_AVG_CONSUMPTION},
                null, null, null, null, null);
    }

    public static boolean atualizaVehicle(SQLiteDatabase db, long id, String oid_vehicle, String name_vehicle, String license_plate,
                                          int full_capacity, double avg_consumption) {
        ContentValues args = new ContentValues();
        args.put(KEY_OID_VEHICLE, oid_vehicle);
        args.put(KEY_NAME_VEHICLE, name_vehicle);
        args.put(KEY_LICENCE_PLATE, license_plate);
        args.put(KEY_FULL_CAPACITY, full_capacity);
        args.put(KEY_AVG_CONSUMPTION, avg_consumption);
        return db.update(NOME_TABELA, args, KEY_ID + "=" + id, null)
                > 0;
    }*/
}
