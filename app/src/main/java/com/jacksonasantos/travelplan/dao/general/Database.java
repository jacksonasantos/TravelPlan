package com.jacksonasantos.travelplan.dao.general;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.jacksonasantos.travelplan.dao.AccommodationDAO;
import com.jacksonasantos.travelplan.dao.AchievementDAO;
import com.jacksonasantos.travelplan.dao.BrokerDAO;
import com.jacksonasantos.travelplan.dao.CurrencyQuoteDAO;
import com.jacksonasantos.travelplan.dao.DriverDAO;
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
import com.jacksonasantos.travelplan.dao.PendingVehicleDAO;
import com.jacksonasantos.travelplan.dao.ReservationDAO;
import com.jacksonasantos.travelplan.dao.SummaryTravelExpenseDAO;
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
import com.jacksonasantos.travelplan.dao.interfaces.DriverISchema;
import com.jacksonasantos.travelplan.dao.interfaces.FuelSupplyISchema;
import com.jacksonasantos.travelplan.dao.interfaces.InsuranceCompanyISchema;
import com.jacksonasantos.travelplan.dao.interfaces.InsuranceContactISchema;
import com.jacksonasantos.travelplan.dao.interfaces.InsuranceISchema;
import com.jacksonasantos.travelplan.dao.interfaces.ItineraryISchema;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenanceISchema;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenanceItemISchema;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenancePlanISchema;
import com.jacksonasantos.travelplan.dao.interfaces.MarkerISchema;
import com.jacksonasantos.travelplan.dao.interfaces.PendingVehicleISchema;
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
    private static final int DATABASE_VERSION = 55;

    public static AccommodationDAO mAccommodationDao;
    public static AchievementDAO mAchievementDao;
    public static BrokerDAO mBrokerDao;
    public static CurrencyQuoteDAO mCurrencyQuoteDao;
    public static DriverDAO mDriverDao;
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
    public static ReservationDAO mReservationDao;
    public static TravelDAO mTravelDao;
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

    public String[][] matrix = {
            {"1" , ""  , "VehicleISchema"           , "CREATE_TABLE_VEHICLE_V1"               , "VEHICLE_TABLE"              , "V1 - Create Table..."    },
            {"2" , ""  , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V2"                , "VEHICLE_TABLE"              , "V2 - Alter Table..."     },
            {"3" , ""  , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V3"                , "VEHICLE_TABLE"              , "V3 - Alter Table..."     },
            {"5" , ""  , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V5"                , "VEHICLE_TABLE"              , "V5 - Alter Table..."     },
            {"6" , ""  , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V6"                , "VEHICLE_TABLE"              , "V6 - Alter Table..."     },
            {"7" , "1" , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V7_1"              , "VEHICLE_TABLE"              , "V7.1 - Alter Table..."   },
            {"7" , "2" , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V7_2"              , "VEHICLE_TABLE"              , "V7.2 - Alter Table..."   },
            {"7" , "3" , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V7_3"              , "VEHICLE_TABLE"              , "V7.3 - Alter Table..."   },
            {"7" , "4" , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V7_4"              , "VEHICLE_TABLE"              , "V7.4 - Alter Table..."   },
            {"8" , ""  , "FuelSupplyISchema"        , "CREATE_TABLE_FUEL_SUPPLY_V8"           , "FUEL_SUPPLY_TABLE"          , "V8 - Create Table..."    },
            {"8" , ""  , "CurrencyQuoteISchema"     , "CREATE_TABLE_CURRENCY_QUOTE_V8"        , "CURRENCY_QUOTE_TABLE"       , "V8 - Create Table..."    },
            {"9" , ""  , "FuelSupplyISchema"        , "ALTER_TABLE_FUEL_SUPPLY_V9"            , "FUEL_SUPPLY_TABLE"          , "V9 - Alter Table..."     },
            {"10", ""  , "MaintenanceISchema"       , "CREATE_TABLE_MAINTENANCE_V10"          , "MAINTENANCE_TABLE"          , "V10 - Create Table..."   },
            {"11", "1" , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V11_1"             , "VEHICLE_TABLE"              , "V11.1 - Alter Table..."  },
            {"11", "2" , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V11_2"             , "VEHICLE_TABLE"              , "V11.2 - Alter Table..."  },
            {"11", "3" , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V11_3"             , "VEHICLE_TABLE"              , "V11.3 - Alter Table..."  },
            {"11", "4" , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V11_4"             , "VEHICLE_TABLE"              , "V11.4 - Alter Table..."  },
            {"11", "5" , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V11_5"             , "VEHICLE_TABLE"              , "V11.5 - Alter Table..."  },
            {"11", "6" , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V11_6"             , "VEHICLE_TABLE"              , "V11.6 - Alter Table..."  },
            {"11", "7" , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V11_7"             , "VEHICLE_TABLE"              , "V11.7 - Alter Table..."  },
            {"11", "8" , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V11_8"             , "VEHICLE_TABLE"              , "V11.8 - Alter Table..."  },
            {"11", "9" , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V11_9"             , "VEHICLE_TABLE"              , "V11.9 - Alter Table..."  },
            {"11", "10", "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V11_10"            , "VEHICLE_TABLE"              , "V11.10 - Alter Table..." },
            {"11", "11", "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V11_11"            , "VEHICLE_TABLE"              , "V11.11 - Alter Table..." },
            {"11", "12", "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V11_12"            , "VEHICLE_TABLE"              , "V11.12 - Alter Table..." },
            {"12", ""  , "InsuranceCompanyISchema"  , "CREATE_TABLE_INSURANCE_COMPANY_V12"    , "INSURANCE_COMPANY_TABLE"    , "V12 - Create Table..."   },
            {"15", ""  , "TravelISchema"            , "CREATE_TABLE_TRAVEL_V15"               , "TRAVEL_TABLE"               , "V15 - Create Table..."   },
            {"16", ""  , "BrokerISchema"            , "CREATE_TABLE_BROKER_V16"               , "BROKER_TABLE"               , "V16 - Create Table..."   },
            {"16", ""  , "InsuranceISchema"         , "CREATE_TABLE_INSURANCE_V16"            , "INSURANCE_TABLE"            , "V16 - Create Table..."   },
            {"18", ""  , "InsuranceISchema"         , "ALTER_TABLE_INSURANCE_V18"             , "INSURANCE_TABLE"            , "V18 - Alter Table..."    },
            {"19", "1" , "InsuranceISchema"         , "ALTER_TABLE_INSURANCE_V19_1"           , "INSURANCE_TABLE"            , "V19.1 - Alter Table..."  },
            {"19", "2" , "InsuranceISchema"         , "ALTER_TABLE_INSURANCE_V19_2"           , "INSURANCE_TABLE"            , "V19.2 - Alter Table..."  },
            {"20", ""  , "FuelSupplyISchema"        , "ALTER_TABLE_FUEL_SUPPLY_V20"           , "FUEL_SUPPLY_TABLE"          , "V20 - Alter Table..."    },
            {"22", ""  , "MaintenancePlanISchema"   , "CREATE_TABLE_MAINTENANCE_PLAN_V22"     , "MAINTENANCE_PLAN_TABLE"     , "V22 - Create Table..."   },
            {"23", ""  , "VehicleHasPlanISchema"    , "CREATE_TABLE_VEHICLE_HAS_PLAN_V23"     , "VEHICLE_HAS_PLAN_TABLE"     , "V23 - Create Table..."   },
            {"24", ""  , "BrokerISchema"            , "ALTER_TABLE_BROKER_V24"                , "BROKER_TABLE"               , "V24 - Alter Table..."    },
            {"25", ""  , "VehicleHasTravelISchema"  , "CREATE_TABLE_VEHICLE_HAS_TRAVEL_V25"   , "VEHICLE_HAS_TRAVEL_TABLE"   , "V25 - Create Table..."   },
            {"26", ""  , "MarkerISchema"            , "CREATE_TABLE_MARKER_V26"               , "MARKER_TABLE"               , "V26 - Create Table..."   },
            {"27", ""  , "AccommodationISchema"     , "CREATE_TABLE_ACCOMMODATION_V27"        , "ACCOMMODATION_TABLE"        , "V27 - Create Table..."   },
            {"27", ""  , "ReservationISchema"       , "CREATE_TABLE_RESERVATION_V27"          , "RESERVATION_TABLE"          , "V27 - Create Table..."   },
            {"27", ""  , "ItineraryISchema"         , "CREATE_TABLE_ITINERARY_V27"            , "ITINERARY_TABLE"            , "V27 - Create Table..."   },
            {"28", "1" , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V28_1"             , "VEHICLE_TABLE"              , "V28.1 - Alter Table..."  },
            {"28", "2" , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V28_2"             , "VEHICLE_TABLE"              , "V28.2 - Alter Table..."  },
            {"28", "3" , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V28_3"             , "VEHICLE_TABLE"              , "V28.3 - Alter Table..."  },
            {"28", "4" , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V28_4"             , "VEHICLE_TABLE"              , "V28.4 - Alter Table..."  },
            {"29", ""  , "MaintenanceItemISchema"   , "CREATE_TABLE_MAINTENANCE_ITEM_V29"     , "MAINTENANCE_ITEM_TABLE"     , "V29 - Create Table..."   },
            {"30", ""  , "FuelSupplyISchema"        , "ALTER_TABLE_FUEL_SUPPLY_V30"           , "FUEL_SUPPLY_TABLE"          , "V30 - Alter Table..."    },
            {"31", ""  , "TravelExpensesISchema"    , "CREATE_TABLE_TRAVEL_EXPENSES_V31"      , "TRAVEL_EXPENSES_TABLE"      , "V31 - Create Table..."   },
            {"31", ""  , "TravelItemExpensesISchema", "CREATE_TABLE_TRAVEL_ITEM_EXPENSES_V31" , "TRAVEL_ITEM_EXPENSES_TABLE" , "V31 - Create Table..."   },
            {"32", ""  , "InsuranceContactISchema"  , "CREATE_TABLE_INSURANCE_CONTACT_V32"    , "INSURANCE_CONTACT_TABLE"    , "V32 - Create Table..."   },
            {"33", ""  , "MarkerISchema"            , "ALTER_TABLE_MARKER_V33"                , "MARKER_TABLE"               , "V33 - Alter Table..."    },
            {"34", ""  , "MarkerISchema"            , "ALTER_TABLE_MARKER_V34"                , "MARKER_TABLE"               , "V34 - Alter Table..."    },
            {"35", ""  , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V35"               , "VEHICLE_TABLE"              , "V35 - Alter Table..."    },
            {"36", ""  , "AchievementISchema"       , "CREATE_TABLE_ACHIEVEMENT_V36"          , "ACHIEVEMENT_TABLE"          , "V36 - Create Table..."   },
            {"38", ""  , "AchievementISchema"       , "ALTER_TABLE_ACHIEVEMENT_V38"           , "ACHIEVEMENT_TABLE"          , "V38 - Alter Table..."    },
            {"39", ""  , "AchievementISchema"       , "ALTER_TABLE_ACHIEVEMENT_V39"           , "ACHIEVEMENT_TABLE"          , "V39 - Alter Table..."    },
            {"40", ""  , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V40"               , "VEHICLE_TABLE"              , "V40 - Alter Table..."    },
            {"43", ""  , "TravelExpensesISchema"    , "ALTER_TABLE_TRAVEL_EXPENSES_V43"       , "TRAVEL_EXPENSES_TABLE"      , "V43 - Alter Table..."    },
            {"44", ""  , "TravelItemExpensesISchema", "ALTER_TABLE_TRAVEL_ITEM_EXPENSES_V44"  , "TRAVEL_ITEM_EXPENSES_TABLE" , "V44 - Alter Table..."    },
            {"44", "1" , "TravelItemExpensesISchema", "ALTER_TABLE_TRAVEL_ITEM_EXPENSES_V44_1", "TRAVEL_ITEM_EXPENSES_TABLE" , "V44.1 - Alter Table..."  },
            {"45", ""  , "FuelSupplyISchema"        , "ALTER_TABLE_FUEL_SUPPLY_V45"           , "FUEL_SUPPLY_TABLE"          , "V45 - Alter Table..."    },
            {"46", "1" , "AchievementISchema"       , "ALTER_TABLE_ACHIEVEMENT_V46_1"         , "ACHIEVEMENT_TABLE"          , "V46.1 - Alter Table..."  },
            {"46", "2" , "AchievementISchema"       , "ALTER_TABLE_ACHIEVEMENT_V46_2"         , "ACHIEVEMENT_TABLE"          , "V46.2 - Alter Table..."  },
            {"46", "3" , "AchievementISchema"       , "ALTER_TABLE_ACHIEVEMENT_V46_3"         , "ACHIEVEMENT_TABLE"          , "V46.3 - Alter Table..."  },
            {"47", ""  , "DriverISchema"            , "CREATE_TABLE_DRIVER_V47"               , "DRIVER_TABLE"               , "V47 - Create Table..."   },
            {"48", ""  , "VehicleISchema"           , "ALTER_TABLE_VEHICLE_V48"               , "VEHICLE_TABLE"              , "V48 - Create Table..."   },
            {"49", ""  , "VehicleHasTravelISchema"  , "ALTER_TABLE_VEHICLE_HAS_TRAVEL_V49"    , "VEHICLE_HAS_TRAVEL_TABLE"   , "V49 - Alter Table..."    },
            {"50", ""  , "MarkerISchema"            , "ALTER_TABLE_MARKER_V50"                , "MARKER_TABLE"               , "V50 - Create Table..."   },
            {"51", ""  , "AchievementISchema"       , "ALTER_TABLE_ACHIEVEMENT_V51"           , "ACHIEVEMENT_TABLE"          , "V51 - Alter Table..."    },
            {"52", "1" , "VehicleHasTravelISchema"  , "ALTER_TABLE_VEHICLE_HAS_TRAVEL_V52_1"  , "VEHICLE_HAS_TRAVEL_TABLE"   , "V52.1 - Alter Table..."  },
            {"52", "2" , "VehicleHasTravelISchema"  , "ALTER_TABLE_VEHICLE_HAS_TRAVEL_V52_2"  , "VEHICLE_HAS_TRAVEL_TABLE"   , "V52.2 - Alter Table..."  },
            {"54", ""  , "MaintenanceItemISchema"   , "ALTER_TABLE_MAINTENANCE_ITEM_V54"      , "MAINTENANCE_ITEM_TABLE"     , "V54 - Alter Table..."    },
            {"53", ""  , "PendingVehicleISchema"    , "CREATE_TABLE_PENDING_VEHICLE_V53"      , "PENDING_VEHICLE_TABLE"      , "V53 - Create Table..."   },
            {"55", "1" , "AchievementISchema"       , "ALTER_TABLE_ACHIEVEMENT_V55_1"         , "ACHIEVEMENT_TABLE"          , "V55.1 - Alter Table..."  },
            {"55", "2" , "AchievementISchema"       , "ALTER_TABLE_ACHIEVEMENT_V55_2"         , "ACHIEVEMENT_TABLE"          , "V54.2 - Alter Table..."  },
    };

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
            mTravelExpensesDao = new TravelExpensesDAO(mDb);
            mTravelItemExpensesDao = new TravelItemExpensesDAO(mDb);
            mInsuranceContactDao = new InsuranceContactDAO(mDb);
            mAchievementDao = new AchievementDAO(mDb);
            mVehicleMaintenanceItemDao = new VehicleMaintenanceItemDAO(mDb);
            mDriverDao = new DriverDAO(mDb);
            mPendingVehicleDao = new PendingVehicleDAO(mDb);
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

            /*
            for (String [] line : matrix) {
                String xExec = line[2] + "." + line[3];
                String xLog = "Tab +"+line[2]+"."+line[4];
                String xLog2 = line[5];

                System.out.print ( xExec );
                System.out.print ( xLog + " " + xLog2 );

                db.execSQL( xExec );
                Log.w( xLog, xLog2 );
            }
            */

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
            db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V38); Log.w("Table "+AchievementISchema.ACHIEVEMENT_TABLE,"V38 - Alter Table...");
            db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V39); Log.w("Table "+AchievementISchema.ACHIEVEMENT_TABLE,"V39 - Alter Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V40); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V40 - Alter Table...");
            db.execSQL(TravelExpensesISchema.ALTER_TABLE_TRAVEL_EXPENSES_V43); Log.w("Table "+TravelExpensesISchema.TRAVEL_EXPENSES_TABLE,"V43 - Alter Table...");
            db.execSQL(TravelItemExpensesISchema.ALTER_TABLE_TRAVEL_ITEM_EXPENSES_V44); Log.w("Ta "+TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_TABLE,"V44 - Alter Table...");
            db.execSQL(TravelItemExpensesISchema.ALTER_TABLE_TRAVEL_ITEM_EXPENSES_V44_1); Log.w("Ta "+TravelItemExpensesISchema.TRAVEL_ITEM_EXPENSES_TABLE,"V44_1 - Alter Table...");
            db.execSQL(FuelSupplyISchema.ALTER_TABLE_FUEL_SUPPLY_V45); Log.w("Table "+FuelSupplyISchema.FUEL_SUPPLY_TABLE,"V45 - Alter Table...");
            db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V46_1); Log.w("Table "+AchievementISchema.ACHIEVEMENT_TABLE,"V46_1 - Alter Table...");
            db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V46_2); Log.w("Table "+AchievementISchema.ACHIEVEMENT_TABLE,"V46_2 - Alter Table...");
            db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V46_3); Log.w("Table "+AchievementISchema.ACHIEVEMENT_TABLE,"V46_3 - Alter Table...");
            db.execSQL(DriverISchema.CREATE_TABLE_DRIVER_V47); Log.w("Table "+DriverISchema.DRIVER_TABLE,"V47 - Create Table...");
            db.execSQL(VehicleISchema.ALTER_TABLE_VEHICLE_V48); Log.w("Table "+VehicleISchema.VEHICLE_TABLE,"V48 - Create Table...");
            db.execSQL(VehicleHasTravelISchema.ALTER_TABLE_VEHICLE_HAS_TRAVEL_V49);Log.w("Table "+VehicleHasTravelISchema.VEHICLE_HAS_TRAVEL_TABLE,"V49 - Alter Table...");
            db.execSQL(MarkerISchema.ALTER_TABLE_MARKER_V50);Log.w("Table "+MarkerISchema.MARKER_TABLE,"V50 - Create Table...");
            db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V51);Log.w("Table "+AchievementISchema.ACHIEVEMENT_TABLE,"V51 - Alter Table...");
            db.execSQL(VehicleHasTravelISchema.ALTER_TABLE_VEHICLE_HAS_TRAVEL_V52_1);Log.w("Table "+VehicleHasTravelISchema.VEHICLE_HAS_TRAVEL_TABLE,"V52_1 - Alter Table...");
            db.execSQL(VehicleHasTravelISchema.ALTER_TABLE_VEHICLE_HAS_TRAVEL_V52_2);Log.w("Table "+VehicleHasTravelISchema.VEHICLE_HAS_TRAVEL_TABLE,"V52_2 - Alter Table...");
            db.execSQL(PendingVehicleISchema.CREATE_TABLE_PENDING_VEHICLE_V53); Log.w("Table "+PendingVehicleISchema.PENDING_VEHICLE_TABLE,"V53 - Create Table...");
            db.execSQL(MaintenanceItemISchema.ALTER_TABLE_MAINTENANCE_ITEM_V54);Log.w("Table "+MaintenanceItemISchema.MAINTENANCE_ITEM_TABLE,"V54 - Alter Table...");
            db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V55_1);Log.w("Table "+AchievementISchema.ACHIEVEMENT_TABLE,"V55_1 - Alter Table...");
            db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V55_2);Log.w("Table "+AchievementISchema.ACHIEVEMENT_TABLE,"V55_2 - Alter Table...");
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
                    case 38:
                        db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V38);
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
                    case 47:
                        db.execSQL(DriverISchema.CREATE_TABLE_DRIVER_V47);
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
                    case 51:
                        db.execSQL(AchievementISchema.ALTER_TABLE_ACHIEVEMENT_V51);
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
                    default:
                        break;
                }
                Log.w("Update version "+i,"ok...");
            }
        }
    }
}
