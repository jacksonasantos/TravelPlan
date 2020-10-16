package com.jacksonasantos.travelplan.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.jacksonasantos.travelplan.dao.interfaces.CurrencyQuotelSchema;
import com.jacksonasantos.travelplan.dao.interfaces.FuelSupplylSchema;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenanceISchema;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleISchema;

import static com.jacksonasantos.travelplan.dao.interfaces.VehicleISchema.VEHICLE_TABLE;
import static com.jacksonasantos.travelplan.dao.interfaces.FuelSupplylSchema.FUEL_SUPPLY_TABLE;
import static com.jacksonasantos.travelplan.dao.interfaces.CurrencyQuotelSchema.CURRENCY_QUOTE_TABLE;
import static com.jacksonasantos.travelplan.dao.interfaces.MaintenanceISchema.MAINTENANCE_TABLE;

public class Database {

    private static final String DATABASE_NAME = "TravelPlan.db";
    private static final int DATABASE_VERSION = 10;

    private DatabaseHelper mDbHelper;
    private final Context mContext;

    public static VehicleDAO mVehicleDao;
    public static FuelSupplyDAO mFuelSupplyDao;
    public static CurrencyQuoteDAO mCurrencyQuoteDao;
    public static MaintenanceDAO mMaintenanceDao;

    public Database(Context context) {
        this.mContext = context;
    }

    public void open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase mDb = mDbHelper.getWritableDatabase();  // Chama o onCreate

        // Lista de Tabelas DAO
        mVehicleDao = new VehicleDAO(mDb);
        mFuelSupplyDao = new FuelSupplyDAO(mDb);
        mCurrencyQuoteDao = new CurrencyQuoteDAO(mDb);
        mMaintenanceDao = new MaintenanceDAO(mDb);
    }

    public void close() {
        mDbHelper.close();
    }

    public class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
        }

        @SuppressLint("SQLiteString")
        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(VehicleISchema.CREATE_TABLE_VEHICLE_V1); Log.w("Table "+VEHICLE_TABLE,"V1 - Create Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V2); Log.w("Table "+VEHICLE_TABLE,"V2 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V3); Log.w("Table "+VEHICLE_TABLE,"V3 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V5); Log.w("Table "+VEHICLE_TABLE,"V5 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V6); Log.w("Table "+VEHICLE_TABLE,"V6 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_1); Log.w("Table "+VEHICLE_TABLE,"V71 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_2); Log.w("Table "+VEHICLE_TABLE,"V72 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_3); Log.w("Table "+VEHICLE_TABLE,"V73 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_4); Log.w("Table "+VEHICLE_TABLE,"V74 - Alter Table...");
            db.execSQL(FuelSupplylSchema.CREATE_TABLE_FUEL_SUPPLY_V8); Log.w("Table "+FUEL_SUPPLY_TABLE,"V8 - Create Table...");
            db.execSQL(CurrencyQuotelSchema.CREATE_TABLE_CURRENCY_QUOTE_V8); Log.w("Table "+CURRENCY_QUOTE_TABLE,"V8 - Create Table...");
            db.execSQL(FuelSupplylSchema.ALTER_TABLE_FUEL_SUPPLY_V9); Log.w("Table "+FUEL_SUPPLY_TABLE,"V9 - Alter Table...");
            db.execSQL(MaintenanceISchema.CREATE_TABLE_MAINTENANCE_V10); Log.w("Table "+MAINTENANCE_TABLE,"V10 - Create Table...");
        }

        @SuppressLint("SQLiteString")
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("Database", "Upgrading database from version "
                    + oldVersion + " to "
                    + newVersion + " without destroying the old data");

            Toast.makeText(mContext,"Upgrading database from version : old " + oldVersion + " to new "+ newVersion, Toast.LENGTH_LONG).show();

            if (oldVersion == 1) {
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V5);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V6);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_4);
                db.execSQL(FuelSupplylSchema.CREATE_TABLE_FUEL_SUPPLY_V8);
                db.execSQL(CurrencyQuotelSchema.CREATE_TABLE_CURRENCY_QUOTE_V8);
                db.execSQL(FuelSupplylSchema.ALTER_TABLE_FUEL_SUPPLY_V9);
            } else if (oldVersion == 2) {
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V5);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V6);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_4);
                db.execSQL(FuelSupplylSchema.CREATE_TABLE_FUEL_SUPPLY_V8);
                db.execSQL(CurrencyQuotelSchema.CREATE_TABLE_CURRENCY_QUOTE_V8);
                db.execSQL(FuelSupplylSchema.ALTER_TABLE_FUEL_SUPPLY_V9);
            } else if (oldVersion == 3) {
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V5);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V6);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_4);
                db.execSQL(FuelSupplylSchema.CREATE_TABLE_FUEL_SUPPLY_V8);
                db.execSQL(CurrencyQuotelSchema.CREATE_TABLE_CURRENCY_QUOTE_V8);
                db.execSQL(FuelSupplylSchema.ALTER_TABLE_FUEL_SUPPLY_V9);
            } else if (oldVersion == 4) {
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V5);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V6);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_4);
                db.execSQL(FuelSupplylSchema.CREATE_TABLE_FUEL_SUPPLY_V8);
                db.execSQL(CurrencyQuotelSchema.CREATE_TABLE_CURRENCY_QUOTE_V8);
                db.execSQL(FuelSupplylSchema.ALTER_TABLE_FUEL_SUPPLY_V9);
            } else if (oldVersion == 5) {
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V6);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_4);
                db.execSQL(FuelSupplylSchema.CREATE_TABLE_FUEL_SUPPLY_V8);
                db.execSQL(CurrencyQuotelSchema.CREATE_TABLE_CURRENCY_QUOTE_V8);
                db.execSQL(FuelSupplylSchema.ALTER_TABLE_FUEL_SUPPLY_V9);
            } else if (oldVersion == 6) {
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_4);
                db.execSQL(FuelSupplylSchema.CREATE_TABLE_FUEL_SUPPLY_V8);
                db.execSQL(CurrencyQuotelSchema.CREATE_TABLE_CURRENCY_QUOTE_V8);
                db.execSQL(FuelSupplylSchema.ALTER_TABLE_FUEL_SUPPLY_V9);
            } else if (oldVersion == 7) {
                db.execSQL(FuelSupplylSchema.CREATE_TABLE_FUEL_SUPPLY_V8);
                db.execSQL(CurrencyQuotelSchema.CREATE_TABLE_CURRENCY_QUOTE_V8);
                db.execSQL(FuelSupplylSchema.ALTER_TABLE_FUEL_SUPPLY_V9);
            } else if (oldVersion == 8) {
                db.execSQL(FuelSupplylSchema.ALTER_TABLE_FUEL_SUPPLY_V9);
            } else if (oldVersion == 9 ) {
                db.execSQL(MaintenanceISchema.CREATE_TABLE_MAINTENANCE_V10);
            } else if (oldVersion == 10 ) {
            }
        }
    }
}