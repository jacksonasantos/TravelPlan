package com.jacksonasantos.travelplan.DAO;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.jacksonasantos.travelplan.DAO.Interface.UserISchema;
import com.jacksonasantos.travelplan.DAO.Interface.VehicleISchema;

import static com.jacksonasantos.travelplan.DAO.Interface.UserISchema.USER_TABLE;
import static com.jacksonasantos.travelplan.DAO.Interface.VehicleISchema.VEHICLE_TABLE;

public class Database {

    private static final String DATABASE_NAME = "TravelPlan.db";
    private static final int DATABASE_VERSION = 4;

    private DatabaseHelper mDbHelper;
    private final Context mContext;

    public static UserDAO mUserDao;
    public static VehicleDAO mVehicleDao;

    public Database(Context context) {
        this.mContext = context;
    }

    public Database open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase mDb = mDbHelper.getWritableDatabase();  // Chama o onCreate

        // Lista de Tabelas DAO
        mUserDao = new UserDAO(mDb);
        mVehicleDao = new VehicleDAO(mDb);
        return this;
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

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(VehicleISchema.CREATE_TABLE_VEHICLE_V1); Log.w("Table "+VEHICLE_TABLE,"V1 - Create Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V2); Log.w("Table "+VEHICLE_TABLE,"V2 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V3); Log.w("Table "+VEHICLE_TABLE,"V3 - Alter Table...");
            db.execSQL(UserISchema.CREATE_TABLE_USER_V4); Log.w("Table "+USER_TABLE,"V4 - Create Table...");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("Database", "Upgrading database from version "
                    + oldVersion + " to "
                    + newVersion + " which destroys all old data");

            Toast.makeText(mContext,"Database : old " + oldVersion + " new "+ newVersion, Toast.LENGTH_LONG).show();

            if (oldVersion == 1) {
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V2);
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V3);
                db.execSQL(UserISchema.CREATE_TABLE_USER_V4);
            } else if (oldVersion == 2) {
                db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V3);
                db.execSQL(UserISchema.CREATE_TABLE_USER_V4);
            } else if (oldVersion == 3) {
                db.execSQL(UserISchema.CREATE_TABLE_USER_V4);
            } else if (oldVersion == 4) {
            }
        }
    }
}
