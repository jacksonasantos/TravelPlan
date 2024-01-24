package com.jacksonasantos.travelplan.dao.general;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.jacksonasantos.travelplan.dao.AccommodationDAO;
import com.jacksonasantos.travelplan.dao.AccountDAO;
import com.jacksonasantos.travelplan.dao.AchievementDAO;
import com.jacksonasantos.travelplan.dao.AchievementResumeDAO;
import com.jacksonasantos.travelplan.dao.BrokerDAO;
import com.jacksonasantos.travelplan.dao.CurrencyQuoteDAO;
import com.jacksonasantos.travelplan.dao.FuelSupplyDAO;
import com.jacksonasantos.travelplan.dao.InsuranceCompanyDAO;
import com.jacksonasantos.travelplan.dao.InsuranceContactDAO;
import com.jacksonasantos.travelplan.dao.InsuranceDAO;
import com.jacksonasantos.travelplan.dao.ItineraryDAO;
import com.jacksonasantos.travelplan.dao.ItineraryHasTransportDAO;
import com.jacksonasantos.travelplan.dao.MaintenanceDAO;
import com.jacksonasantos.travelplan.dao.MaintenanceItemDAO;
import com.jacksonasantos.travelplan.dao.MaintenancePlanDAO;
import com.jacksonasantos.travelplan.dao.MaintenancePlanHasVehicleTypeDAO;
import com.jacksonasantos.travelplan.dao.MarkerDAO;
import com.jacksonasantos.travelplan.dao.NextMaintenanceItemDAO;
import com.jacksonasantos.travelplan.dao.PendingVehicleDAO;
import com.jacksonasantos.travelplan.dao.PersonDAO;
import com.jacksonasantos.travelplan.dao.ReservationDAO;
import com.jacksonasantos.travelplan.dao.SummaryTravelExpenseDAO;
import com.jacksonasantos.travelplan.dao.TourDAO;
import com.jacksonasantos.travelplan.dao.TransportDAO;
import com.jacksonasantos.travelplan.dao.TravelCashDAO;
import com.jacksonasantos.travelplan.dao.TravelDAO;
import com.jacksonasantos.travelplan.dao.TravelExpensesDAO;
import com.jacksonasantos.travelplan.dao.TravelItemExpensesDAO;
import com.jacksonasantos.travelplan.dao.VehicleDAO;
import com.jacksonasantos.travelplan.dao.VehicleHasPlanDAO;
import com.jacksonasantos.travelplan.dao.VehicleHasTravelDAO;
import com.jacksonasantos.travelplan.dao.VehicleMaintenanceItemDAO;
import com.jacksonasantos.travelplan.dao.VehicleStatisticsDAO;
import com.jacksonasantos.travelplan.dao.VehicleStatisticsPeriodDAO;
import com.jacksonasantos.travelplan.dao.interfaces.AccommodationISchema;
import com.jacksonasantos.travelplan.dao.interfaces.AchievementISchema;
import com.jacksonasantos.travelplan.dao.interfaces.BrokerISchema;
import com.jacksonasantos.travelplan.dao.interfaces.CurrencyQuoteISchema;
import com.jacksonasantos.travelplan.dao.interfaces.FuelSupplyISchema;
import com.jacksonasantos.travelplan.dao.interfaces.InsuranceCompanyISchema;
import com.jacksonasantos.travelplan.dao.interfaces.InsuranceContactISchema;
import com.jacksonasantos.travelplan.dao.interfaces.InsuranceISchema;
import com.jacksonasantos.travelplan.dao.interfaces.ItineraryHasTransportISchema;
import com.jacksonasantos.travelplan.dao.interfaces.ItineraryISchema;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenanceISchema;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenanceItemISchema;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenancePlanISchema;
import com.jacksonasantos.travelplan.dao.interfaces.MarkerISchema;
import com.jacksonasantos.travelplan.dao.interfaces.PendingVehicleISchema;
import com.jacksonasantos.travelplan.dao.interfaces.PersonISchema;
import com.jacksonasantos.travelplan.dao.interfaces.ReservationISchema;
import com.jacksonasantos.travelplan.dao.interfaces.TourISchema;
import com.jacksonasantos.travelplan.dao.interfaces.TransportISchema;
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
    private static final int DATABASE_VERSION = 84;

    public static AccommodationDAO mAccommodationDao;
    public static AccountDAO mAccountDao;
    public static AchievementDAO mAchievementDao;
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
    public static PendingVehicleDAO mPendingVehicleDao;
    public static PersonDAO mPersonDao;
    public static ReservationDAO mReservationDao;
    public static TourDAO mTourDao;
    public static TransportDAO mTransportDao;
    public static TravelDAO mTravelDao;
    public static TravelCashDAO mTravelCashDao;
    public static TravelExpensesDAO mTravelExpensesDao;
    public static TravelItemExpensesDAO mTravelItemExpensesDao;
    public static VehicleHasPlanDAO mVehicleHasPlanDao;
    public static VehicleHasTravelDAO mVehicleHasTravelDao;
    public static VehicleDAO mVehicleDao;

    public static SummaryTravelExpenseDAO mSummaryTravelExpenseDao;
    public static VehicleStatisticsDAO mVehicleStatisticsDao;
    public static VehicleStatisticsPeriodDAO mVehicleStatisticsPeriodDao;
    public static NextMaintenanceItemDAO mNextMaintenanceItemDao;
    public static VehicleMaintenanceItemDAO mVehicleMaintenanceItemDao;
    public static MaintenancePlanHasVehicleTypeDAO mMaintenancePlanHasVehicleTypeDAO;
    public static ItineraryHasTransportDAO mItineraryHasTransportDao;
    public static AchievementResumeDAO mAchievementResumeDao;

    public Database(Context context) {
        this.mContext = context;
    }

    public void open() {
        try {
            mDbHelper = new DatabaseHelper(mContext);
            SQLiteDatabase mDb = mDbHelper.getWritableDatabase();

            // List de Table DAO
            mVehicleDao = new VehicleDAO(mDb);
            mFuelSupplyDao = new FuelSupplyDAO(mDb);
            mCurrencyQuoteDao = new CurrencyQuoteDAO(mDb);
            mMaintenanceDao = new MaintenanceDAO(mDb);
            mInsuranceCompanyDao = new InsuranceCompanyDAO(mDb);
            mVehicleStatisticsDao = new VehicleStatisticsDAO(mDb);
            mVehicleStatisticsPeriodDao = new VehicleStatisticsPeriodDAO(mDb);
            mTravelDao = new TravelDAO(mDb);
            mBrokerDao = new BrokerDAO(mDb);
            mInsuranceDao = new InsuranceDAO(mDb);
            mMaintenancePlanDao = new MaintenancePlanDAO(mDb);
            mVehicleHasPlanDao = new VehicleHasPlanDAO(mDb);
            mSummaryTravelExpenseDao = new SummaryTravelExpenseDAO(mDb);
            mVehicleHasTravelDao = new VehicleHasTravelDAO(mDb);
            mMarkerDao = new MarkerDAO(mDb);
            mAccommodationDao = new AccommodationDAO(mDb);
            mReservationDao = new ReservationDAO(mDb);
            mItineraryDao = new ItineraryDAO(mDb);
            mMaintenanceItemDao = new MaintenanceItemDAO(mDb);
            mNextMaintenanceItemDao = new NextMaintenanceItemDAO(mDb);
            mTourDao = new TourDAO(mDb);
            mTransportDao = new TransportDAO(mDb);
            mTravelExpensesDao = new TravelExpensesDAO(mDb);
            mTravelItemExpensesDao = new TravelItemExpensesDAO(mDb);
            mInsuranceContactDao = new InsuranceContactDAO(mDb);
            mAchievementDao = new AchievementDAO(mDb);
            mVehicleMaintenanceItemDao = new VehicleMaintenanceItemDAO(mDb);
            mPendingVehicleDao = new PendingVehicleDAO(mDb);
            mMaintenancePlanHasVehicleTypeDAO = new MaintenancePlanHasVehicleTypeDAO(mDb);
            mPersonDao = new PersonDAO(mDb);
            mItineraryHasTransportDao = new ItineraryHasTransportDAO(mDb);
            mTravelCashDao = new TravelCashDAO(mDb);
            mAccountDao = new AccountDAO(mDb);
            mAchievementResumeDao = new AchievementResumeDAO(mDb);
        } catch (Exception e) {
            Log.i("Open Database ", "Error : " + e.getMessage());
            Toast.makeText(mContext, "Error Open Database : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
            db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V33); Log.w("Table "+MarkerISchema.MARKER_TABLE,"V33 - Alter Table...");
            db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V34); Log.w("Table "+MarkerISchema.MARKER_TABLE,"V34 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V35); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V35 - Alter Table...");
            db.execSQL(AchievementISchema.CREATE_TABLE_ACHIEVEMENT_V36); Log.w("Table "+AchievementISchema.ACHIEVEMENT_TABLE,"V36 - Create Table...");
            db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V39); Log.w("Table "+AchievementISchema.ACHIEVEMENT_TABLE,"V39 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V40); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V40 - Alter Table...");
            db.execSQL(TravelExpensesISchema.ALTER_TABLE_TRAVEL_EXPENSES_V43); Log.w("Table "+TravelExpensesISchema.TRAVEL_EXPENSES_TABLE,"V43 - Alter Table...");
            db.execSQL(TravelItemExpensesISchema.ALTER_TABLE_TRAVEL_ITEM_EXPENSES_V44); Log.w("Ta "+TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_TABLE,"V44 - Alter Table...");
            db.execSQL(TravelItemExpensesISchema.ALTER_TABLE_TRAVEL_ITEM_EXPENSES_V44_1); Log.w("Ta "+TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_TABLE,"V44_1 - Alter Table...");
            db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V45); Log.w("Table "+FuelSupplyISchema.FUEL_SUPPLY_TABLE,"V45 - Alter Table...");
            db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V46_1); Log.w("Table "+AchievementISchema.ACHIEVEMENT_TABLE,"V46_1 - Alter Table...");
            db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V46_2); Log.w("Table "+AchievementISchema.ACHIEVEMENT_TABLE,"V46_2 - Alter Table...");
            db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V46_3); Log.w("Table "+AchievementISchema.ACHIEVEMENT_TABLE,"V46_3 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V48); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V48 - Alter Table...");
            db.execSQL(VehicleHasTravelISchema.ALTER_TABLE_VEHICLE_HAS_TRAVEL_V49);Log.w("Table "+VehicleHasTravelISchema.VEHICLE_HAS_TRAVEL_TABLE,"V49 - Alter Table...");
            db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V50);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V50 - Create Table...");
            db.execSQL(VehicleHasTravelISchema.ALTER_TABLE_VEHICLE_HAS_TRAVEL_V52_1);Log.w("Table "+VehicleHasTravelISchema.VEHICLE_HAS_TRAVEL_TABLE,"V52_1 - Alter Table...");
            db.execSQL(VehicleHasTravelISchema.ALTER_TABLE_VEHICLE_HAS_TRAVEL_V52_2);Log.w("Table "+VehicleHasTravelISchema.VEHICLE_HAS_TRAVEL_TABLE,"V52_2 - Alter Table...");
            db.execSQL(PendingVehicleISchema.CREATE_TABLE_PENDING_VEHICLE_V53); Log.w("Table "+PendingVehicleISchema.PENDING_VEHICLE_TABLE,"V53 - Create Table...");
            db.execSQL(MaintenanceItemISchema.ALTER_TABLE_MAINTENANCE_ITEM_V54);Log.w("Table "+MaintenanceItemISchema.MAINTENANCE_ITEM_TABLE,"V54 - Alter Table...");
            db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V55_1);Log.w("Table "+AchievementISchema.ACHIEVEMENT_TABLE,"V55_1 - Alter Table...");
            db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V55_2);Log.w("Table "+AchievementISchema.ACHIEVEMENT_TABLE,"V55_2 - Alter Table...");
            db.execSQL(MaintenancePlanHasVehicleTypeDAO.CREATE_TABLE_MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_V57); Log.w("Table "+MaintenancePlanHasVehicleTypeDAO.MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_TABLE,"V57 - Create Table...");
            db.execSQL(MaintenancePlanHasVehicleTypeDAO.CREATE_TABLE_MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_V57); Log.w("Table "+MaintenancePlanHasVehicleTypeDAO.MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_TABLE,"V57 - Create Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V58); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V58 - Alter Table...");
            db.execSQL(PersonISchema.CREATE_TABLE_PERSON_V59);Log.w("Table "+PersonISchema.PERSON_TABLE,"V59 - Create Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V60_2); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V60_2 - Alter Table...");
           // db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V60_3); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V60_3 - Alter Table...");
            db.execSQL(TourISchema.CREATE_TABLE_TOUR_V61);Log.w("Table "+TourISchema.TOUR_TABLE,"V61 - Create Table...");
            db.execSQL(TransportISchema.CREATE_TABLE_TRANSPORT_V62);Log.w("Table "+TransportISchema.TRANSPORT_TABLE,"V62 - Create Table...");
            db.execSQL(TourISchema.ALTER_TABLE_TOUR_V63_1); Log.w("Table "+TourISchema.TOUR_TABLE,"V63_1 - Alter Table...");
            db.execSQL(TourISchema.ALTER_TABLE_TOUR_V63_2); Log.w("Table "+TourISchema.TOUR_TABLE,"V63_2 - Alter Table...");
            db.execSQL(TourISchema.ALTER_TABLE_TOUR_V64_1); Log.w("Table "+TourISchema.TOUR_TABLE,"V64_1 - Alter Table...");
            db.execSQL(TourISchema.ALTER_TABLE_TOUR_V64_2); Log.w("Table "+TourISchema.TOUR_TABLE,"V64_2 - Alter Table...");
            db.execSQL(TourISchema.ALTER_TABLE_TOUR_V64_3); Log.w("Table "+TourISchema.TOUR_TABLE,"V64_3 - Alter Table...");
            db.execSQL(TourISchema.ALTER_TABLE_TOUR_V64_4); Log.w("Table "+TourISchema.TOUR_TABLE,"V64_4 - Alter Table...");
            db.execSQL(TourISchema.ALTER_TABLE_TOUR_V64_5); Log.w("Table "+TourISchema.TOUR_TABLE,"V64_5 - Alter Table...");
            db.execSQL(ItineraryHasTransportISchema.CREATE_TABLE_ITINERARY_HAS_TRANSPORT_V65);Log.w("Table "+ItineraryHasTransportISchema.ITINERARY_HAS_TRANSPORT_TABLE,"V65 - Create Table...");
            db.execSQL(VehicleHasTravelISchema.ALTER_TABLE_VEHICLE_HAS_TRAVEL_V66);Log.w("Table "+VehicleHasTravelISchema.VEHICLE_HAS_TRAVEL_TABLE,"V66 - Alter Table...");
            db.execSQL(ItineraryISchema.ALTER_TABLE_ITINERARY_V67);Log.w("Table "+ItineraryISchema.ITINERARY_TABLE,"V67 - Alter Table...");
            db.execSQL(VehicleHasTravelISchema.ALTER_TABLE_VEHICLE_HAS_TRAVEL_V68_1);Log.w("Table "+VehicleHasTravelISchema.VEHICLE_HAS_TRAVEL_TABLE,"V68.1 - Alter Table...");
            db.execSQL(VehicleHasTravelISchema.ALTER_TABLE_VEHICLE_HAS_TRAVEL_V68_2);Log.w("Table "+VehicleHasTravelISchema.VEHICLE_HAS_TRAVEL_TABLE,"V68.2 - Alter Table...");
            db.execSQL(TourISchema.ALTER_TABLE_TOUR_V69);Log.w("Table "+TourISchema.TOUR_TABLE,"V69 - Alter Table...");
            db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V70);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V70 - Alter Table...");
            db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V71);Log.w("Table "+FuelSupplyISchema.FUEL_SUPPLY_TABLE,"V71 - Alter Table...");
            db.execSQL(VehicleHasTravelISchema.ALTER_TABLE_VEHICLE_HAS_TRAVEL_V72);Log.w("Table "+VehicleHasTravelISchema.VEHICLE_HAS_TRAVEL_TABLE,"V72 - Alter Table...");
            db.execSQL(TourISchema.ALTER_TABLE_TOUR_V73);Log.w("Table "+TourISchema.TOUR_TABLE,"V73 - Alter Table...");
            db.execSQL(TourISchema.ALTER_TABLE_TOUR_V74);Log.w("Table "+TourISchema.TOUR_TABLE,"V74 - Alter Table...");
            db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V75);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V75 - Alter Table...");
            db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V77_1);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V77.1 - Alter Table...");
            db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V77_2);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V77.2 - Alter Table...");
            db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V77_3);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V77.3 - Alter Table...");
            db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V77_4);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V77.4 - Alter Table...");
            //db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V77_5);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V77.5 - Alter Table...");
            db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V77_6);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V77.6 - Alter Table...");
            //db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V77_7);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V77.7 - Alter Table...");
            db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V77_8);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V77.8 - Alter Table...");
            db.execSQL(TourISchema.ALTER_TABLE_TOUR_V78);Log.w("Table "+TourISchema.TOUR_TABLE,"V78 - Alter Table...");
            db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V79);Log.w("Table "+AchievementISchema.ACHIEVEMENT_TABLE,"V79 - Alter Table...");
            db.execSQL(TravelCashDAO.CREATE_TABLE_TRAVEL_CASH_V80);Log.w("Table "+TravelCashDAO.TRAVEL_CASH_TABLE,"V80 - Create Table...");
            db.execSQL(AccountDAO.CREATE_TABLE_ACCOUNT_V81_1);Log.w("Table "+AccountDAO.ACCOUNT_TABLE,"V81.1 - Create Table...");
            db.execSQL(TravelCashDAO.ALTER_TABLE_TRAVEL_CASH_V81_2);Log.w("Table "+TravelCashDAO.TRAVEL_CASH_TABLE,"V81.2 - Create Table...");
            db.execSQL(TravelItemExpensesISchema.ALTER_TABLE_TRAVEL_ITEM_EXPENSES_V82_1); Log.w("Ta "+TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_TABLE,"V82.1 - Alter Table...");
            db.execSQL(TravelItemExpensesISchema.ALTER_TABLE_TRAVEL_ITEM_EXPENSES_V82_2); Log.w("Ta "+TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_TABLE,"V82.2 - Alter Table...");
            db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V83);Log.w("Table "+FuelSupplyISchema.FUEL_SUPPLY_TABLE,"V83 - Alter Table...");
            db.execSQL(TravelItemExpensesISchema.ALTER_TABLE_TRAVEL_ITEM_EXPENSES_V84);Log.w("Table "+TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_TABLE,"V84 - Alter Table...");
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
                    case 33:
                        db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V33);
                        break;
                    case 34:
                        db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V34);
                        break;
                    case 35:
                        db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V35);
                        break;
                    case 36:
                        db.execSQL(AchievementISchema.CREATE_TABLE_ACHIEVEMENT_V36);
                        break;
                    case 39:
                        db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V39);
                        break;
                    case 40:
                        db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V40);
                        break;
                    case 43:
                        db.execSQL(TravelExpensesISchema.ALTER_TABLE_TRAVEL_EXPENSES_V43);
                        break;
                    case 44:
                        db.execSQL(TravelItemExpensesISchema.ALTER_TABLE_TRAVEL_ITEM_EXPENSES_V44);
                        db.execSQL(TravelItemExpensesISchema.ALTER_TABLE_TRAVEL_ITEM_EXPENSES_V44_1);
                        break;
                    case 45:
                        db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V45);
                        break;
                    case 46:
                        db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V46_1);
                        db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V46_2);
                        db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V46_3);
                        break;
                    case 48:
                        db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V48);
                        break;
                    case 49:
                        db.execSQL(VehicleHasTravelISchema.ALTER_TABLE_VEHICLE_HAS_TRAVEL_V49);
                        break;
                    case 50:
                        db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V50);
                        break;
                    case 52:
                        db.execSQL(VehicleHasTravelISchema.ALTER_TABLE_VEHICLE_HAS_TRAVEL_V52_1);
                        db.execSQL(VehicleHasTravelISchema.ALTER_TABLE_VEHICLE_HAS_TRAVEL_V52_2);
                        break;
                    case 53:
                        db.execSQL(PendingVehicleISchema.CREATE_TABLE_PENDING_VEHICLE_V53);
                        break;
                    case 54:
                        db.execSQL(MaintenanceItemISchema.ALTER_TABLE_MAINTENANCE_ITEM_V54);
                        break;
                    case 55:
                        db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V55_1);
                        db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V55_2);
                        break;
                    case 57:
                        db.execSQL(MaintenancePlanHasVehicleTypeDAO.CREATE_TABLE_MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_V57);
                        break;
                    case 58:
                        db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V58);
                        break;
                    case 59:
                        db.execSQL(PersonDAO.CREATE_TABLE_PERSON_V59);
                        break;
                    case 60:
                        db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V60_2);
                        db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V60_3);
                        break;
                    case 61:
                        db.execSQL(TourISchema.CREATE_TABLE_TOUR_V61);
                        break;
                    case 62:
                        db.execSQL(TransportISchema.CREATE_TABLE_TRANSPORT_V62);
                        break;
                    case 63:
                        db.execSQL(TourISchema.ALTER_TABLE_TOUR_V63_1);
                        db.execSQL(TourISchema.ALTER_TABLE_TOUR_V63_2);
                        break;
                    case 64:
                        db.execSQL(TourISchema.ALTER_TABLE_TOUR_V64_1);
                        db.execSQL(TourISchema.ALTER_TABLE_TOUR_V64_2);
                        db.execSQL(TourISchema.ALTER_TABLE_TOUR_V64_3);
                        db.execSQL(TourISchema.ALTER_TABLE_TOUR_V64_4);
                        db.execSQL(TourISchema.ALTER_TABLE_TOUR_V64_5);
                        break;
                    case 65:
                        db.execSQL(ItineraryHasTransportISchema.CREATE_TABLE_ITINERARY_HAS_TRANSPORT_V65);
                        break;
                    case 66:
                        db.execSQL(VehicleHasTravelISchema.ALTER_TABLE_VEHICLE_HAS_TRAVEL_V66);
                        break;
                    case 67:
                        db.execSQL(ItineraryISchema.ALTER_TABLE_ITINERARY_V67);
                        break;
                    case 68:
                        db.execSQL(VehicleHasTravelISchema.ALTER_TABLE_VEHICLE_HAS_TRAVEL_V68_1);
                        db.execSQL(VehicleHasTravelISchema.ALTER_TABLE_VEHICLE_HAS_TRAVEL_V68_2);
                        break;
                    case 69:
                        db.execSQL(TourISchema.ALTER_TABLE_TOUR_V69);
                        break;
                    case 70:
                        db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V70);
                        break;
                    case 71:
                        db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V71);
                        break;
                    case 72:
                        db.execSQL(VehicleHasTravelISchema.ALTER_TABLE_VEHICLE_HAS_TRAVEL_V72);
                        break;
                    case 73:
                        db.execSQL(TourISchema.ALTER_TABLE_TOUR_V73);
                        break;
                    case 74:
                        db.execSQL(TourISchema.ALTER_TABLE_TOUR_V74);
                        break;
                    case 75:
                        db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V75);
                        break;
                    case 77:
                        db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V77_1);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V77.1 - Alter Table...");
                        db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V77_2);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V77.2 - Alter Table...");
                        db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V77_3);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V77.3 - Alter Table...");
                        db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V77_4);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V77.4 - Alter Table...");
                        db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V77_5);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V77.5 - Alter Table...");
                        db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V77_6);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V77.6 - Alter Table...");
                        //db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V77_7);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V77.7 - Alter Table...");
                        db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V77_8);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V77.8 - Alter Table...");
                        break;
                    case 78:
                        db.execSQL(TourISchema.ALTER_TABLE_TOUR_V78);Log.w("Table "+TourISchema.TOUR_TABLE,"V78 - Alter Table...");
                        break;
                    case 79:
                        db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V79);Log.w("Table "+AchievementISchema.ACHIEVEMENT_TABLE,"V79 - Alter Table...");
                        break;
                    case 80:
                        db.execSQL(TravelCashDAO.CREATE_TABLE_TRAVEL_CASH_V80);Log.w("Table "+TravelCashDAO.TRAVEL_CASH_TABLE,"V80 - Create Table...");
                        break;
                    case 81:
                        db.execSQL(AccountDAO.CREATE_TABLE_ACCOUNT_V81_1);Log.w("Table "+AccountDAO.ACCOUNT_TABLE,"V81.1 - Create Table...");
                        db.execSQL(TravelCashDAO.ALTER_TABLE_TRAVEL_CASH_V81_2);Log.w("Table "+TravelCashDAO.TRAVEL_CASH_TABLE,"V81.2 - Create Table...");
                        break;
                    case 82:
                        db.execSQL(TravelItemExpensesISchema.ALTER_TABLE_TRAVEL_ITEM_EXPENSES_V82_1); Log.w("Ta "+TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_TABLE,"V82.1 - Alter Table...");
                        db.execSQL(TravelItemExpensesISchema.ALTER_TABLE_TRAVEL_ITEM_EXPENSES_V82_2); Log.w("Ta "+TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_TABLE,"V82.2 - Alter Table...");
                        break;
                    case 83:
                        db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V83);Log.w("Table "+FuelSupplyISchema.FUEL_SUPPLY_TABLE,"V83 - Alter Table...");
                        break;
                    case 84:
                        db.execSQL(TravelItemExpensesISchema.ALTER_TABLE_TRAVEL_ITEM_EXPENSES_V84);Log.w("Table "+TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_TABLE,"V84 - Alter Table...");
                        break;
                    default:
                        break;
                }
                Log.w("Update version "+i,"ok...");
            }
        }
    }
}
