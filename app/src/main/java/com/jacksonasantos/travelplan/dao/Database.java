package com.jacksonasantos.travelplan.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.jacksonasantos.travelplan.dao.interfaces.CurrencyQuoteISchema;
import com.jacksonasantos.travelplan.dao.interfaces.FuelSupplyISchema;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenanceISchema;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleISchema;
import com.jacksonasantos.travelplan.dao.interfaces.InsuranceCompanyISchema;

import static com.jacksonasantos.travelplan.dao.interfaces.VehicleISchema.VEHICLE_TABLE;
import static com.jacksonasantos.travelplan.dao.interfaces.FuelSupplyISchema.FUEL_SUPPLY_TABLE;
import static com.jacksonasantos.travelplan.dao.interfaces.CurrencyQuoteISchema.CURRENCY_QUOTE_TABLE;
import static com.jacksonasantos.travelplan.dao.interfaces.MaintenanceISchema.MAINTENANCE_TABLE;
import static com.jacksonasantos.travelplan.dao.interfaces.InsuranceCompanyISchema.INSURANCE_COMPANY_TABLE;

public class Database {

    private DatabaseHelper mDbHelper;
    private final Context mContext;

    private static final String DATABASE_NAME = "TravelPlan.db";
    private static final int DATABASE_VERSION = 13;

    public static VehicleDAO mVehicleDao;
    public static FuelSupplyDAO mFuelSupplyDao;
    public static CurrencyQuoteDAO mCurrencyQuoteDao;
    public static MaintenanceDAO mMaintenanceDao;
    public static InsuranceCompanyDAO mInsuranceCompanyDao;

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
        mInsuranceCompanyDao = new InsuranceCompanyDAO(mDb);
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
// TODO - Ajustar para melhorar a chamada dos comandos de manutenção de BD
        @SuppressLint("SQLiteString")
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(VehicleISchema.CREATE_TABLE_VEHICLE_V1); Log.w("Table "+VEHICLE_TABLE,"V1 - Create Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V2); Log.w("Table "+VEHICLE_TABLE,"V2 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V3); Log.w("Table "+VEHICLE_TABLE,"V3 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V5); Log.w("Table "+VEHICLE_TABLE,"V5 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V6); Log.w("Table "+VEHICLE_TABLE,"V6 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_1); Log.w("Table "+VEHICLE_TABLE,"V7.1 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_2); Log.w("Table "+VEHICLE_TABLE,"V7.2 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_3); Log.w("Table "+VEHICLE_TABLE,"V7.3 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_4); Log.w("Table "+VEHICLE_TABLE,"V7.4 - Alter Table...");
            db.execSQL(FuelSupplyISchema.CREATE_TABLE_FUEL_SUPPLY_V8); Log.w("Table "+FUEL_SUPPLY_TABLE,"V8 - Create Table...");
            db.execSQL(CurrencyQuoteISchema.CREATE_TABLE_CURRENCY_QUOTE_V8); Log.w("Table "+CURRENCY_QUOTE_TABLE,"V8 - Create Table...");
            db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V9); Log.w("Table "+FUEL_SUPPLY_TABLE,"V9 - Alter Table...");
            db.execSQL(MaintenanceISchema.CREATE_TABLE_MAINTENANCE_V10); Log.w("Table "+MAINTENANCE_TABLE,"V10 - Create Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_1); Log.w("Table "+VEHICLE_TABLE,"V11.1 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_2); Log.w("Table "+VEHICLE_TABLE,"V11.2 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_3); Log.w("Table "+VEHICLE_TABLE,"V11.3 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_4); Log.w("Table "+VEHICLE_TABLE,"V11.4 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_5); Log.w("Table "+VEHICLE_TABLE,"V11.5 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_6); Log.w("Table "+VEHICLE_TABLE,"V11.6 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_7); Log.w("Table "+VEHICLE_TABLE,"V11.7 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_8); Log.w("Table "+VEHICLE_TABLE,"V11.8 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_9); Log.w("Table "+VEHICLE_TABLE,"V11.9 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_10); Log.w("Table "+VEHICLE_TABLE,"V11.10 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_11); Log.w("Table "+VEHICLE_TABLE,"V11.11 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_12); Log.w("Table "+VEHICLE_TABLE,"V11.12 - Alter Table...");
            db.execSQL(InsuranceCompanyISchema.CREATE_TABLE_INSURANCE_COMPANY_V12); Log.w("Table "+INSURANCE_COMPANY_TABLE,"V12 - Create Table...");
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
                db.execSQL(FuelSupplyISchema.CREATE_TABLE_FUEL_SUPPLY_V8);
                db.execSQL(CurrencyQuoteISchema.CREATE_TABLE_CURRENCY_QUOTE_V8);
                db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V9);
                db.execSQL(MaintenanceISchema.CREATE_TABLE_MAINTENANCE_V10);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_4);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_5);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_6);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_7);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_8);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_9);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_10);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_11);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_12);
                db.execSQL(InsuranceCompanyISchema.CREATE_TABLE_INSURANCE_COMPANY_V12);
            } else if (oldVersion == 2) {
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V5);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V6);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_4);
                db.execSQL(FuelSupplyISchema.CREATE_TABLE_FUEL_SUPPLY_V8);
                db.execSQL(CurrencyQuoteISchema.CREATE_TABLE_CURRENCY_QUOTE_V8);
                db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V9);
                db.execSQL(MaintenanceISchema.CREATE_TABLE_MAINTENANCE_V10);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_4);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_5);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_6);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_7);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_8);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_9);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_10);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_11);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_12);
                db.execSQL(InsuranceCompanyISchema.CREATE_TABLE_INSURANCE_COMPANY_V12);
            } else if (oldVersion == 3) {
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V5);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V6);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_4);
                db.execSQL(FuelSupplyISchema.CREATE_TABLE_FUEL_SUPPLY_V8);
                db.execSQL(CurrencyQuoteISchema.CREATE_TABLE_CURRENCY_QUOTE_V8);
                db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V9);
                db.execSQL(MaintenanceISchema.CREATE_TABLE_MAINTENANCE_V10);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_4);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_5);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_6);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_7);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_8);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_9);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_10);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_11);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_12);
                db.execSQL(InsuranceCompanyISchema.CREATE_TABLE_INSURANCE_COMPANY_V12);
            } else if (oldVersion == 4) {
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V5);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V6);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_4);
                db.execSQL(FuelSupplyISchema.CREATE_TABLE_FUEL_SUPPLY_V8);
                db.execSQL(CurrencyQuoteISchema.CREATE_TABLE_CURRENCY_QUOTE_V8);
                db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V9);
                db.execSQL(MaintenanceISchema.CREATE_TABLE_MAINTENANCE_V10);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_4);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_5);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_6);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_7);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_8);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_9);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_10);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_11);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_12);
                db.execSQL(InsuranceCompanyISchema.CREATE_TABLE_INSURANCE_COMPANY_V12);
            } else if (oldVersion == 5) {
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V6);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_4);
                db.execSQL(FuelSupplyISchema.CREATE_TABLE_FUEL_SUPPLY_V8);
                db.execSQL(CurrencyQuoteISchema.CREATE_TABLE_CURRENCY_QUOTE_V8);
                db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V9);
                db.execSQL(MaintenanceISchema.CREATE_TABLE_MAINTENANCE_V10);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_4);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_5);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_6);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_7);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_8);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_9);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_10);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_11);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_12);
                db.execSQL(InsuranceCompanyISchema.CREATE_TABLE_INSURANCE_COMPANY_V12);
            } else if (oldVersion == 6) {
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_4);
                db.execSQL(FuelSupplyISchema.CREATE_TABLE_FUEL_SUPPLY_V8);
                db.execSQL(CurrencyQuoteISchema.CREATE_TABLE_CURRENCY_QUOTE_V8);
                db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V9);
                db.execSQL(MaintenanceISchema.CREATE_TABLE_MAINTENANCE_V10);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_4);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_5);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_6);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_7);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_8);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_9);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_10);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_11);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_12);
                db.execSQL(InsuranceCompanyISchema.CREATE_TABLE_INSURANCE_COMPANY_V12);
            } else if (oldVersion == 7) {
                db.execSQL(FuelSupplyISchema.CREATE_TABLE_FUEL_SUPPLY_V8);
                db.execSQL(CurrencyQuoteISchema.CREATE_TABLE_CURRENCY_QUOTE_V8);
                db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V9);
                db.execSQL(MaintenanceISchema.CREATE_TABLE_MAINTENANCE_V10);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_4);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_5);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_6);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_7);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_8);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_9);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_10);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_11);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_12);
                db.execSQL(InsuranceCompanyISchema.CREATE_TABLE_INSURANCE_COMPANY_V12);
            } else if (oldVersion == 8) {
                db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V9);
                db.execSQL(MaintenanceISchema.CREATE_TABLE_MAINTENANCE_V10);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_4);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_5);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_6);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_7);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_8);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_9);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_10);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_11);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_12);
                db.execSQL(InsuranceCompanyISchema.CREATE_TABLE_INSURANCE_COMPANY_V12);
            } else if (oldVersion == 9 ) {
                db.execSQL(MaintenanceISchema.CREATE_TABLE_MAINTENANCE_V10);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_4);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_5);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_6);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_7);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_8);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_9);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_10);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_11);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_12);
                db.execSQL(InsuranceCompanyISchema.CREATE_TABLE_INSURANCE_COMPANY_V12);
            } else if (oldVersion == 10 ) {
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_1);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_3);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_4);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_5);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_6);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_7);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_8);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_9);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_10);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_11);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_12);
                db.execSQL(InsuranceCompanyISchema.CREATE_TABLE_INSURANCE_COMPANY_V12);
            } else if (oldVersion == 11 ) {
                db.execSQL(InsuranceCompanyISchema.CREATE_TABLE_INSURANCE_COMPANY_V12);
            } else if (oldVersion == 12 ) {
            }
        }
    }
}