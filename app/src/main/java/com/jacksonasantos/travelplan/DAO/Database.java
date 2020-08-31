package com.jacksonasantos.travelplan.DAO;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jacksonasantos.travelplan.DAO.Interface.UserISchema;
import com.jacksonasantos.travelplan.DAO.Interface.VehicleISchema;

public class Database {

    // NEW
    private static final String TAG = "MyDatabase";
    private static final String DATABASE_NAME = "TravelPlan.db";
    private static final int DATABASE_VERSION = 1;
    private DatabaseHelper mDbHelper;
    private final Context mContext;

    public static UserDAO mUserDao;
    public static VehicleDAO mVehicleDao;

    public Database open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase mDb = mDbHelper.getWritableDatabase();

        // Lista de Tabelas DAO para criação
        mUserDao = new UserDAO(mDb);
        mVehicleDao = new VehicleDAO(mDb);

        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public Database(Context context) {
        this.mContext = context;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(UserISchema.USER_TABLE_CREATE);
            db.execSQL(VehicleISchema.VEHICLE_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            Log.w(TAG, "Upgrading database from version "
                    + oldVersion + " to "
                    + newVersion + " which destroys all old data");

            db.execSQL("DROP TABLE IF EXISTS " + UserISchema.USER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + VehicleISchema.VEHICLE_TABLE);
            onCreate(db);
        }
    }


    // OLD
    /*final String NOME_BANCO = "db_TravelPlan";
    final int VERSAO_BANCO = 1;

    final Context context;
    final MeuOpenHelper openHelper;
    SQLiteDatabase db;

    public Database(Context ctx) {
        this.context = ctx;
        openHelper = new MeuOpenHelper(context);
    }

    private class MeuOpenHelper extends SQLiteOpenHelper {
        MeuOpenHelper(Context context) {
            super(context, NOME_BANCO, null, VERSAO_BANCO);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                Vehicle.createTableVehicle((SQLiteDatabase) db);
                Vehicle.insereVehicle( db, "XL1200ca", "Harley Davidson Custom", "XXX-9988", 17, 18.5);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Vehicle.dropTableVehicle((SQLiteDatabase) db);
            onCreate(db);
        }
    }

    public Database abrir() throws SQLException {
        db = openHelper.getWritableDatabase();
        return this;
    }

    public void fechar() {
        openHelper.close();
    }*/
}
