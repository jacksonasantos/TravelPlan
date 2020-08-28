package com.jacksonasantos.travelplan.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DAO {
    public static final String KEY_ID = "id";
    public static final String KEY_OID_VEHICLE = "oid_vehicle";
    public static final String KEY_NAME_VEHICLE = "name_vehicle";
    public static final String KEY_LICENCE_PLATE = "license_plate";
    public static final String KEY_FULL_CAPACITY = "full_capacity";
    public static final String KEY_AVG_CONSUMPTION = "avg_consumption";

    final String NOME_BANCO = "db_TravelPlan";
    final String NOME_TABELA = "vehicle";
    final int VERSAO_BANCO = 1;

    final String SQL_CREATE_TABLE = "create table " + NOME_TABELA + " " +
            "(" + KEY_ID + " integer primary key autoincrement, " +
            KEY_OID_VEHICLE + " text not null, " +
            KEY_NAME_VEHICLE + " text, " +
            KEY_LICENCE_PLATE + " text, " +
            KEY_FULL_CAPACITY + " integer, " +
            KEY_AVG_CONSUMPTION + " double);";

    final Context context;
    final MeuOpenHelper openHelper;
    SQLiteDatabase db;

    public DAO(Context ctx) {
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
                db.execSQL(SQL_CREATE_TABLE);
                insereVehicle("XL1200ca","Harley Davidson Custom","XXX-9988",17,18.5);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade( SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + NOME_TABELA + " ");
            onCreate(db);
        }
    }

    public DAO abrir() throws SQLException {
        db = openHelper.getWritableDatabase();
        return this;
    }

    public void fechar() {
        openHelper.close();
    }

    public long insereVehicle(String oid_vehicle, String name_vehicle, String license_plate,
                             int full_capacity, double avg_consumption) {
        ContentValues campos = new ContentValues();
        campos.put(KEY_OID_VEHICLE, oid_vehicle);
        campos.put(KEY_NAME_VEHICLE, name_vehicle);
        campos.put(KEY_LICENCE_PLATE, license_plate);
        campos.put(KEY_FULL_CAPACITY, full_capacity);
        campos.put(KEY_AVG_CONSUMPTION, avg_consumption);
        return db.insert(NOME_TABELA, null, campos);
    }

    public boolean apagaVehicle(long id) {
        return db.delete(NOME_TABELA, KEY_ID + "=" + id, null) > 0;
    }

    public Cursor retornaTodosVehicle() {
        return db.query(NOME_TABELA,
                        new String[] {  KEY_ID,
                                        KEY_OID_VEHICLE,
                                        KEY_NAME_VEHICLE,
                                        KEY_LICENCE_PLATE,
                                        KEY_FULL_CAPACITY,
                                        KEY_AVG_CONSUMPTION },
                        null, null, null, null,null);
    }

    public boolean atualizaVehicle(long id, String oid_vehicle, String name_vehicle, String license_plate,
                                   int full_capacity, double avg_consumption) {
        ContentValues args = new ContentValues();
        args.put(KEY_OID_VEHICLE, oid_vehicle);
        args.put(KEY_NAME_VEHICLE, name_vehicle);
        args.put(KEY_LICENCE_PLATE, license_plate);
        args.put(KEY_FULL_CAPACITY, full_capacity);
        args.put(KEY_AVG_CONSUMPTION, avg_consumption);
        return db.update(NOME_TABELA, args, KEY_ID + "=" + id, null)
                > 0;
    }
}

