package com.jacksonasantos.travelplan.dao.general;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.jacksonasantos.travelplan.dao.AccommodationDAO;
import com.jacksonasantos.travelplan.dao.BrokerDAO;
import com.jacksonasantos.travelplan.dao.CurrencyQuoteDAO;
import com.jacksonasantos.travelplan.dao.FuelSupplyDAO;
import com.jacksonasantos.travelplan.dao.InsuranceCompanyDAO;
import com.jacksonasantos.travelplan.dao.InsuranceContactDAO;
import com.jacksonasantos.travelplan.dao.InsuranceDAO;
import com.jacksonasantos.travelplan.dao.ItineraryDAO;
import com.jacksonasantos.travelplan.dao.MaintenanceDAO;
import com.jacksonasantos.travelplan.dao.MaintenanceItemDAO;
import com.jacksonasantos.travelplan.dao.MaintenancePlanDAO;
import com.jacksonasantos.travelplan.dao.MarkerDAO;
import com.jacksonasantos.travelplan.dao.NextMaintenanceItemDAO;
import com.jacksonasantos.travelplan.dao.ReservationDAO;
import com.jacksonasantos.travelplan.dao.SummaryTravelExpenseDAO;
import com.jacksonasantos.travelplan.dao.TravelDAO;
import com.jacksonasantos.travelplan.dao.TravelExpensesDAO;
import com.jacksonasantos.travelplan.dao.TravelItemExpensesDAO;
import com.jacksonasantos.travelplan.dao.VehicleDAO;
import com.jacksonasantos.travelplan.dao.VehicleGraphStatisticsDAO;
import com.jacksonasantos.travelplan.dao.VehicleHasPlanDAO;
import com.jacksonasantos.travelplan.dao.VehicleHasTravelDAO;
import com.jacksonasantos.travelplan.dao.VehicleStatisticsDAO;
import com.jacksonasantos.travelplan.dao.interfaces.AccommodationISchema;
import com.jacksonasantos.travelplan.dao.interfaces.BrokerISchema;
import com.jacksonasantos.travelplan.dao.interfaces.CurrencyQuoteISchema;
import com.jacksonasantos.travelplan.dao.interfaces.FuelSupplyISchema;
import com.jacksonasantos.travelplan.dao.interfaces.InsuranceCompanyISchema;
import com.jacksonasantos.travelplan.dao.interfaces.InsuranceContactISchema;
import com.jacksonasantos.travelplan.dao.interfaces.InsuranceISchema;
import com.jacksonasantos.travelplan.dao.interfaces.ItineraryISchema;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenanceISchema;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenanceItemISchema;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenancePlanISchema;
import com.jacksonasantos.travelplan.dao.interfaces.MarkerISchema;
import com.jacksonasantos.travelplan.dao.interfaces.ReservationISchema;
import com.jacksonasantos.travelplan.dao.interfaces.TravelExpensesISchema;
import com.jacksonasantos.travelplan.dao.interfaces.TravelISchema;
import com.jacksonasantos.travelplan.dao.interfaces.TravelItemExpensesISchema;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleHasPlanISchema;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleHasTravelISchema;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleISchema;

public class Database {

    private DatabaseHelper mDbHelper;
    private final Context mContext;

    private static final String DATABASE_NAME = "TravelPlan.db";
    private static final int DATABASE_VERSION = 32;

    public static AccommodationDAO mAccommodationDao;
    public static BrokerDAO mBrokerDao;
    public static CurrencyQuoteDAO mCurrencyQuoteDao;
    public static FuelSupplyDAO mFuelSupplyDao;
    public static InsuranceCompanyDAO mInsuranceCompanyDao;
    public static InsuranceContactDAO mInsuranceContactDao;
    public static InsuranceDAO mInsuranceDao;
    public static ItineraryDAO mItineraryDao;
    public static MaintenanceDAO mMaintenanceDao;
    public static MaintenanceItemDAO mMaintenanceItemDao;
    public static MaintenancePlanDAO mMaintenancePlanDao;
    public static MarkerDAO mMarkerDao;
    public static ReservationDAO mReservationDao;
    public static TravelDAO mTravelDao;
    public static TravelExpensesDAO mTravelExpensesDao;
    public static TravelItemExpensesDAO mTravelItemExpensesDao;
    public static VehicleHasPlanDAO mVehicleHasPlanDao;
    public static VehicleHasTravelDAO mVehicleHasTravelDao;
    public static VehicleDAO mVehicleDao;

    public static SummaryTravelExpenseDAO mSummaryTravelExpenseDao;
    public static VehicleGraphStatisticsDAO mVehicleGraphStatisticsDao;
    public static VehicleStatisticsDAO mVehicleStatisticsDao;
    public static NextMaintenanceItemDAO mNextMaintenanceItemDao;

    public Database(Context context) {
        this.mContext = context;
    }

    public void open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase mDb = mDbHelper.getWritableDatabase();

        // List de Table DAO
        mVehicleDao = new VehicleDAO(mDb);
        mFuelSupplyDao = new FuelSupplyDAO(mDb);
        mCurrencyQuoteDao = new CurrencyQuoteDAO(mDb);
        mMaintenanceDao = new MaintenanceDAO(mDb);
        mInsuranceCompanyDao = new InsuranceCompanyDAO(mDb);
        mVehicleStatisticsDao = new VehicleStatisticsDAO(mDb);
        mTravelDao = new TravelDAO(mDb);
        mBrokerDao = new BrokerDAO(mDb);
        mInsuranceDao = new InsuranceDAO(mDb);
        mMaintenancePlanDao = new MaintenancePlanDAO(mDb);
        mVehicleHasPlanDao = new VehicleHasPlanDAO(mDb);
        mVehicleGraphStatisticsDao = new VehicleGraphStatisticsDAO(mDb);
        mSummaryTravelExpenseDao = new SummaryTravelExpenseDAO(mDb);
        mVehicleHasTravelDao = new VehicleHasTravelDAO(mDb);
        mMarkerDao = new MarkerDAO(mDb);
        mAccommodationDao = new AccommodationDAO(mDb);
        mReservationDao = new ReservationDAO(mDb);
        mItineraryDao = new ItineraryDAO(mDb);
        mMaintenanceItemDao = new MaintenanceItemDAO(mDb);
        mNextMaintenanceItemDao = new NextMaintenanceItemDAO(mDb);
        mTravelExpensesDao = new TravelExpensesDAO(mDb);
        mTravelItemExpensesDao = new TravelItemExpensesDAO(mDb);
        mInsuranceContactDao = new InsuranceContactDAO(mDb);
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
            db.execSQL(VehicleISchema.CREATE_TABLE_VEHICLE_V1); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V1 - Create Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V2); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V2 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V3); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V3 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V5); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V5 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V6); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V6 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_1); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V7.1 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_2); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V7.2 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_3); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V7.3 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_4); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V7.4 - Alter Table...");
            db.execSQL(FuelSupplyISchema.CREATE_TABLE_FUEL_SUPPLY_V8); Log.w("Table "+FuelSupplyISchema.FUEL_SUPPLY_TABLE,"V8 - Create Table...");
            db.execSQL(CurrencyQuoteISchema.CREATE_TABLE_CURRENCY_QUOTE_V8); Log.w("Table "+CurrencyQuoteISchema.CURRENCY_QUOTE_TABLE,"V8 - Create Table...");
            db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V9); Log.w("Table "+FuelSupplyISchema.FUEL_SUPPLY_TABLE,"V9 - Alter Table...");
            db.execSQL(MaintenanceISchema.CREATE_TABLE_MAINTENANCE_V10); Log.w("Table "+MaintenanceISchema.MAINTENANCE_TABLE,"V10 - Create Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_1); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V11.1 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_2); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V11.2 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_3); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V11.3 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_4); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V11.4 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_5); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V11.5 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_6); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V11.6 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_7); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V11.7 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_8); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V11.8 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_9); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V11.9 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_10); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V11.10 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_11); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V11.11 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V11_12); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V11.12 - Alter Table...");
            db.execSQL(InsuranceCompanyISchema.CREATE_TABLE_INSURANCE_COMPANY_V12); Log.w("Table "+InsuranceCompanyISchema.INSURANCE_COMPANY_TABLE,"V12 - Create Table...");
            db.execSQL(TravelISchema.CREATE_TABLE_TRAVEL_V15); Log.w("Tab: "+TravelISchema.TRAVEL_TABLE,"V15 - Create Table...");
            db.execSQL(BrokerISchema.CREATE_TABLE_BROKER_V16); Log.w("Tab: "+BrokerISchema.BROKER_TABLE,"V16 - Create Table...");
            db.execSQL(InsuranceISchema.CREATE_TABLE_INSURANCE_V16); Log.w("Table "+InsuranceISchema.INSURANCE_TABLE,"V16 - Create Table...");
            db.execSQL(InsuranceISchema.ALTER_TABLE_INSURANCE_V18); Log.w("Table "+InsuranceISchema.INSURANCE_TABLE,"V18 - Alter Table...");
            db.execSQL(InsuranceISchema.ALTER_TABLE_INSURANCE_V19_1); Log.w("Table "+InsuranceISchema.INSURANCE_TABLE,"V19.1 - Alter Table...");
            db.execSQL(InsuranceISchema.ALTER_TABLE_INSURANCE_V19_2); Log.w("Table "+InsuranceISchema.INSURANCE_TABLE,"V19.2 - Alter Table...");
            db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V20); Log.w("Table "+FuelSupplyISchema.FUEL_SUPPLY_TABLE,"V20 - Alter Table...");
            db.execSQL(MaintenancePlanISchema.CREATE_TABLE_MAINTENANCE_PLAN_V22); Log.w("Table  "+MaintenancePlanISchema.MAINTENANCE_PLAN_TABLE,"V22 - Create Table...");
            db.execSQL(VehicleHasPlanISchema.CREATE_TABLE_VEHICLE_HAS_PLAN_V23); Log.w("Table "+ VehicleHasPlanISchema.VEHICLE_HAS_PLAN_TABLE,"V23 - Create Table...");
            db.execSQL(BrokerISchema.ALTER_TABLE_BROKER_V24); Log.w("Table "+ BrokerISchema.BROKER_TABLE,"V24 - Alter Table...");
            db.execSQL(VehicleHasTravelISchema.CREATE_TABLE_VEHICLE_HAS_TRAVEL_V25); Log.w("Tab "+ VehicleHasTravelISchema.VEHICLE_HAS_TRAVEL_TABLE,"V25 - Create Table...");
            db.execSQL(MarkerISchema.CREATE_TABLE_MARKER_V26); Log.w("Table "+ MarkerISchema.MARKER_TABLE,"V26 - Create Table...");
            db.execSQL(AccommodationISchema.CREATE_TABLE_ACCOMMODATION_V27); Log.w("Table "+ AccommodationISchema.ACCOMMODATION_TABLE,"V27 - Create Table...");
            db.execSQL(ReservationISchema.CREATE_TABLE_RESERVATION_V27); Log.w("Table "+ ReservationISchema.RESERVATION_TABLE,"V27 - Create Table...");
            db.execSQL(ItineraryISchema.CREATE_TABLE_ITINERARY_V27); Log.w("Table "+ ItineraryISchema.ITINERARY_TABLE,"V27 - Create Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V28_1); Log.w("Table "+ VehicleISchema.VEHICLE_TABLE,"V28.1 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V28_2); Log.w("Table "+ VehicleISchema.VEHICLE_TABLE,"V28.2 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V28_3); Log.w("Table "+ VehicleISchema.VEHICLE_TABLE,"V28.3 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V28_4); Log.w("Table "+ VehicleISchema.VEHICLE_TABLE,"V28.4 - Alter Table...");
            db.execSQL(MaintenanceItemISchema.CREATE_TABLE_MAINTENANCE_ITEM_V29); Log.w("Table "+MaintenanceItemISchema.MAINTENANCE_ITEM_TABLE,"V29 - Create Table...");
            db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V30); Log.w("Table "+FuelSupplyISchema.FUEL_SUPPLY_TABLE,"V30 - Alter Table...");
            db.execSQL(TravelExpensesISchema.CREATE_TABLE_TRAVEL_EXPENSES_V31); Log.w("Table "+TravelExpensesISchema.TRAVEL_EXPENSES_TABLE,"V31 - Create Table...");
            db.execSQL(TravelItemExpensesISchema.CREATE_TABLE_TRAVEL_ITEM_EXPENSES_V31); Log.w("Tb."+TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_TABLE,"V31 - Create Table...");
            db.execSQL(InsuranceContactISchema.CREATE_TABLE_INSURANCE_CONTACT_V32); Log.w("Table "+InsuranceContactISchema.INSURANCE_CONTACT_TABLE,"V32 - Create Table...");
        }

        @SuppressLint("SQLiteString")
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("Database", "Upgrading database from version "
                    + oldVersion + " to "
                    + newVersion + " without destroying the old data");

            Toast.makeText(mContext,"Upgrading database from version : old " + oldVersion + " to new "+ newVersion, Toast.LENGTH_LONG).show();

            for (int i = oldVersion+1 ; i <= newVersion; i += 1) {
                switch (i) {
                    case 1:
                        db.execSQL(VehicleISchema.CREATE_TABLE_VEHICLE_V1);
                        break;
                    case 2:
                        db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V2);
                        break;
                    case 3:
                        db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V3);
                        break;
                    case 5:
                        db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V5);
                        break;
                    case 6:
                        db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V6);
                        break;
                    case 7:
                        db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_1);
                        db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_2);
                        db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_3);
                        db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V7_4);
                        break;
                    case 8:
                        db.execSQL(FuelSupplyISchema.CREATE_TABLE_FUEL_SUPPLY_V8);
                        db.execSQL(CurrencyQuoteISchema.CREATE_TABLE_CURRENCY_QUOTE_V8);
                        break;
                    case 9:
                        db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V9);
                        break;
                    case 10:
                        db.execSQL(MaintenanceISchema.CREATE_TABLE_MAINTENANCE_V10);
                        break;
                    case 11:
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
                        break;
                    case 12:
                        db.execSQL(InsuranceCompanyISchema.CREATE_TABLE_INSURANCE_COMPANY_V12);
                        break;
                    case 15:
                        db.execSQL(TravelISchema.CREATE_TABLE_TRAVEL_V15);
                        break;
                    case 16:
                        db.execSQL(BrokerISchema.CREATE_TABLE_BROKER_V16);
                        db.execSQL(InsuranceISchema.CREATE_TABLE_INSURANCE_V16);
                        break;
                    case 18:
                        db.execSQL(InsuranceISchema.ALTER_TABLE_INSURANCE_V18);
                        break;
                    case 19:
                        db.execSQL(InsuranceISchema.ALTER_TABLE_INSURANCE_V19_1);
                        db.execSQL(InsuranceISchema.ALTER_TABLE_INSURANCE_V19_2);
                        break;
                    case 20:
                        db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V20);
                        break;
                    case 22:
                        db.execSQL(MaintenancePlanISchema.CREATE_TABLE_MAINTENANCE_PLAN_V22);
                        break;
                    case 23:
                        db.execSQL(VehicleHasPlanISchema.CREATE_TABLE_VEHICLE_HAS_PLAN_V23);
                        break;
                    case 24:
                        db.execSQL(BrokerISchema.ALTER_TABLE_BROKER_V24);
                        break;
                    case 25:
                        db.execSQL(VehicleHasTravelISchema.CREATE_TABLE_VEHICLE_HAS_TRAVEL_V25);
                        break;
                    case 26:
                        db.execSQL(MarkerISchema.CREATE_TABLE_MARKER_V26);
                        break;
                    case 27:
                        db.execSQL(AccommodationISchema.CREATE_TABLE_ACCOMMODATION_V27);
                        db.execSQL(ReservationISchema.CREATE_TABLE_RESERVATION_V27);
                        db.execSQL(ItineraryISchema.CREATE_TABLE_ITINERARY_V27);
                        break;
                    case 28:
                        db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V28_1);
                        db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V28_2);
                        db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V28_3);
                        db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V28_4);
                        break;
                    case 29:
                        db.execSQL(MaintenanceItemISchema.CREATE_TABLE_MAINTENANCE_ITEM_V29);
                        break;
                    case 30:
                        db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V30);
                        break;
                    case 31:
                        db.execSQL(TravelExpensesISchema.CREATE_TABLE_TRAVEL_EXPENSES_V31);
                        db.execSQL(TravelItemExpensesISchema.CREATE_TABLE_TRAVEL_ITEM_EXPENSES_V31);
                        break;
                    case 32:
                        db.execSQL(InsuranceContactISchema.CREATE_TABLE_INSURANCE_CONTACT_V32);
                        break;
                    default:
                        break;
                }
                Log.w("Update version "+i,"ok...");
            }
        }
    }
}
