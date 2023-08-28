package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Itinerary;
import com.jacksonasantos.travelplan.dao.ItineraryHasTransport;
import com.jacksonasantos.travelplan.dao.Person;
import com.jacksonasantos.travelplan.dao.Transport;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.VehicleHasTravel;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class ItineraryHasTransportActivity extends AppCompatActivity implements TransportTypeListAdapter.ItemClickListener {

    private boolean opInsert = true;

    private ItineraryHasTransport itineraryHasTransport;

    private LinearLayoutManager linearLayoutManager;

    public TransportTypeListAdapter adapterTransportType;

    private TextView tvTravel;
    private RecyclerView rvTransportType ;
    private int nrTransportType = -1;
    private LinearLayout llTransportTypeOwn, llTransportTypeOthers, llAVG;
    private Spinner spOwnVehicle, spTransport, spItinerary, spPerson;
    private Integer nrTravel, nrOwnVehicle, nrTransport, nrItinerary, nrPerson;
    private CheckBox cbDriver;
    private TextView tvSequenceItinerary, tvMeasureConsumption, tvMeasureCostLitre;
    private EditText etAVGConsumption, etAVGCostLitre;
    private ImageButton btAddTransport;
    private ArrayAdapter<Transport> adapterT;

    final Globals g = Globals.getInstance();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Itinerary_has_Transport);
        setContentView(R.layout.dialog_itinerary_has_transport);

        Bundle extras = getIntent().getExtras();
        itineraryHasTransport = new ItineraryHasTransport();

        tvTravel = findViewById(R.id.tvTravel);
        llAVG = findViewById(R.id.llAVG);
        llTransportTypeOwn = findViewById(R.id.llTransportTypeOwn);
        llTransportTypeOthers = findViewById(R.id.llTransportTypeOthers);
        spOwnVehicle = findViewById(R.id.spOwnVehicle);
        spTransport = findViewById(R.id.spTransport);
        spItinerary = findViewById(R.id.spItinerary);
        spPerson = findViewById(R.id.spPerson);
        cbDriver = findViewById(R.id.cbDriver);
        tvSequenceItinerary = findViewById(R.id.tvSequenceItinerary);
        etAVGConsumption = findViewById(R.id.etAVGConsumption);
        tvMeasureConsumption = findViewById(R.id.tvMeasureConsumption);
        etAVGCostLitre = findViewById(R.id.etAVGCostLitre);
        tvMeasureCostLitre = findViewById(R.id.tvMeasureCostLitre);
        btAddTransport = findViewById(R.id.btAddTransport);

        llTransportTypeOwn.setVisibility(View.VISIBLE);
        llTransportTypeOthers.setVisibility(View.GONE);
        llAVG.setVisibility(View.GONE);
        tvMeasureConsumption.setText(g.getMeasureConsumption());
        tvMeasureCostLitre.setText(String.format("%s/%s", getResources().getStringArray(R.array.currency_array)[g.getIdCurrency()], g.getMeasureCost()));

        if (extras != null) {
            if (extras.getInt( "itinerary_has_transport_id") > 0) {
                itineraryHasTransport.setId(extras.getInt("itinerary_has_transport_id"));
                itineraryHasTransport = Database.mItineraryHasTransportDao.fetchItineraryHasTransportById(itineraryHasTransport.getId());
                VehicleHasTravel vehicleHasTravel;
                if (itineraryHasTransport.getVehicle_id() == null || itineraryHasTransport.getVehicle_id() == 0) {
                    vehicleHasTravel = Database.mVehicleHasTravelDao.findVehicleHasTravelByTravelTransport(itineraryHasTransport.getTravel_id(), itineraryHasTransport.getTransport_id());
                } else {
                    vehicleHasTravel = Database.mVehicleHasTravelDao.findVehicleHasTravelByTravelVehicle(itineraryHasTransport.getTravel_id(), itineraryHasTransport.getVehicle_id());
                }
                nrTravel = itineraryHasTransport.getTravel_id();
                nrOwnVehicle = itineraryHasTransport.getVehicle_id();
                nrTransport = itineraryHasTransport.getTransport_id();
                nrItinerary = itineraryHasTransport.getItinerary_id();
                nrPerson = itineraryHasTransport.getPerson_id();
                cbDriver.setChecked(itineraryHasTransport.getDriver()==1);
                etAVGConsumption.setText(String.valueOf(vehicleHasTravel.getAvg_consumption()));
                etAVGCostLitre.setText(String.valueOf(vehicleHasTravel.getAvg_cost_litre()));

                opInsert = false;
                llTransportTypeOwn.setVisibility(View.INVISIBLE);
                llTransportTypeOthers.setVisibility(View.INVISIBLE);
                llAVG.setVisibility(View.INVISIBLE);
                if (itineraryHasTransport.getTransport_type()==0) {
                    llTransportTypeOwn.setVisibility(View.VISIBLE);
                } else {
                    llTransportTypeOthers.setVisibility(View.VISIBLE);
                    if (itineraryHasTransport.getTransport_type()==2) {
                        llAVG.setVisibility(View.VISIBLE);
                    } else {
                        llAVG.setVisibility(View.GONE);
                    }
                }
            } else {
                if (extras.getInt("travel_id") > 0) {
                    nrTravel = extras.getInt("travel_id");
                    itineraryHasTransport.setTravel_id(nrTravel);
                }
                if (extras.getInt("itinerary_id") > 0) {
                    nrItinerary = extras.getInt("itinerary_id");
                    itineraryHasTransport.setItinerary_id(nrItinerary);
                }
                if (extras.getInt("transport_type") > 0) {
                    nrTransportType = extras.getInt("transport_type");
                    itineraryHasTransport.setTransport_type(nrTransportType);
                }
                if (extras.getInt("vehicle_id") > 0) {
                    nrOwnVehicle = extras.getInt("vehicle_id");
                    itineraryHasTransport.setVehicle_id(nrOwnVehicle);
                }
                if (extras.getInt("transport_id") > 0) {
                    nrTransport = extras.getInt("transport_id");
                    itineraryHasTransport.setTransport_id(nrTransport);
                }
                if (extras.getInt("person_id") > 0) {
                    nrPerson = extras.getInt("person_id");
                    itineraryHasTransport.setPerson_id(nrPerson);
                }
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();

        Travel travel = Database.mTravelDao.fetchTravelById(nrTravel);
        tvTravel.setText(travel.getDescription());
        rvTransportType = findViewById(R.id.rvTransportType);
        nrTransportType = 0;

        List<Integer> vTransportType = new ArrayList<>();
        for(int i = 0; i < getApplicationContext().getResources().getStringArray(R.array.transport_type_array).length; i++) {
            vTransportType.add(i);
        }
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rvTransportType.setLayoutManager(linearLayoutManager);
        adapterTransportType = new TransportTypeListAdapter(vTransportType, this, nrTransportType);
        adapterTransportType.addItemClickListener(this);
        rvTransportType.setAdapter(adapterTransportType);

        // spOwnVehicle
        final List<Vehicle> vehicles =  Database.mVehicleDao.fetchArrayVehicles();
        vehicles.add(0, new Vehicle());
        ArrayAdapter<Vehicle> adapterV = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehicles);
        adapterV.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spOwnVehicle.setAdapter(adapterV);
        if (nrOwnVehicle != null && nrOwnVehicle > 0) {
            Vehicle v1 = Database.mVehicleDao.fetchVehicleById(nrOwnVehicle);
            for (int x = 1; x <= spOwnVehicle.getAdapter().getCount(); x++) {
                if (spOwnVehicle.getAdapter().getItem(x).toString().equals(v1.toString())) {
                    spOwnVehicle.setSelection(x);
                    nrOwnVehicle = v1.getId();
                    break;
                }
            }
        }
        final Vehicle[] v1 = {new Vehicle()};
        spOwnVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                v1[0] = (Vehicle) parent.getItemAtPosition(position);
                nrOwnVehicle = v1[0].getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrOwnVehicle = 0;
            }
        });
        adapterV.notifyDataSetChanged();

        // spTransport
        final List<Transport> transports =  Database.mTransportDao.fetchAllTransportTravel(nrTravel);
        transports.add(0, new Transport());
        adapterT = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, transports);
        adapterT.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spTransport.setAdapter(adapterT);
        if (nrTransport != null && nrTransport > 0) {
            Transport t1 = Database.mTransportDao.fetchTransportById(nrTransport);
            for (int x = 1; x <= spTransport.getAdapter().getCount(); x++) {
                if (spTransport.getAdapter().getItem(x).toString().equals(t1.toString())) {
                    spTransport.setSelection(x);
                    nrTransport = t1.getId();
                    break;
                }
            }
        }
        final Transport[] t1 = {new Transport()};
        spTransport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                t1[0] = (Transport) parent.getItemAtPosition(position);
                nrTransport = t1[0].getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrTransport = 0;
            }
        });
        adapterT.notifyDataSetChanged();

        // spItinerary
        final List<Itinerary> itineraries =  Database.mItineraryDao.fetchAllItineraryByTravel(nrTravel);
        itineraries.add(0, new Itinerary());
        ArrayAdapter<Itinerary> adapterI = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itineraries);
        adapterI.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spItinerary.setAdapter(adapterI);
        if (nrItinerary != null && nrItinerary > 0) {
            Itinerary i1 = Database.mItineraryDao.fetchItineraryById(nrItinerary);
            for (int x = 1; x <= spItinerary.getAdapter().getCount(); x++) {
                if (spItinerary.getAdapter().getItem(x).toString().equals(i1.toString())) {
                    spItinerary.setSelection(x);
                    tvSequenceItinerary.setText(String.valueOf(i1.getSequence()));
                    nrItinerary = i1.getId();
                    break;
                }
            }
        }
        final Itinerary[] i1 = {new Itinerary()};
        spItinerary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                i1[0] = (Itinerary) parent.getItemAtPosition(position);
                nrItinerary = i1[0].getId();
                tvSequenceItinerary.setText(String.valueOf(i1[0].getSequence()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrItinerary = 0;
            }
        });
        adapterI.notifyDataSetChanged();

        // spPerson
        final List<Person> persons =  Database.mPersonDao.fetchAllPerson();
        persons.add(0, new Person());
        ArrayAdapter<Person> adapterP = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, persons);
        adapterP.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spPerson.setAdapter(adapterP);
        if (nrPerson!= null && nrPerson > 0) {
            Person p1 = Database.mPersonDao.fetchPersonById(nrPerson);
            for (int x = 1; x <= spPerson.getAdapter().getCount(); x++) {
                if (spPerson.getAdapter().getItem(x).toString().equals(p1.toString())) {
                    spPerson.setSelection(x);
                    nrPerson = p1.getId();
                    break;
                }
            }
        }
        final Person[] p1 = {new Person()};
        spPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                p1[0] = (Person) parent.getItemAtPosition(position);
                nrPerson = p1[0].getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrPerson = 0;
            }
        });
        adapterP.notifyDataSetChanged();

        btAddTransport.setOnClickListener(v -> {
            Intent intent = new Intent (v.getContext(), TransportActivity.class);
            intent.putExtra("op_result", true);
            intent.putExtra("travel_id", nrTravel);
            intent.putExtra("transport_type", nrTransportType);
            myActivityResultLauncher.launch(intent);
        } );

        if (itineraryHasTransport != null) {
            nrTravel = itineraryHasTransport.getTravel_id();
            travel = Database.mTravelDao.fetchTravelById(nrTravel);
            tvTravel.setText(travel.getDescription());
            nrTransportType = itineraryHasTransport.getTransport_type();
            Utils.selected_position = nrTransportType;
            cbDriver.setChecked(itineraryHasTransport.getDriver()==1);
            nrOwnVehicle = itineraryHasTransport.getVehicle_id();
            if (nrOwnVehicle!=null && nrOwnVehicle>0) {
                Vehicle v2 = Database.mVehicleDao.fetchVehicleById(nrOwnVehicle);
                for (int x = 1; x <= spOwnVehicle.getAdapter().getCount(); x++) {
                    if (spOwnVehicle.getAdapter().getItem(x).toString().equals(v2.toString())) {
                        spOwnVehicle.setSelection(x);
                        break;
                    }
                }
            }
            nrTransport = itineraryHasTransport.getTransport_id();
            if (nrTransport!=null && nrTransport>0) {
                Transport t2 = Database.mTransportDao.fetchTransportById(nrTransport);
                for (int x = 1; x <= spTransport.getAdapter().getCount(); x++) {
                    if (spTransport.getAdapter().getItem(x).toString().equals(t2.toString())) {
                        spTransport.setSelection(x);
                        break;
                    }
                }
            }
            nrItinerary = itineraryHasTransport.getItinerary_id();
            if (nrItinerary!=null && nrItinerary>0) {
                Itinerary i2 = Database.mItineraryDao.fetchItineraryById(nrItinerary);
                for (int x = 1 ; x <= spItinerary.getAdapter().getCount(); x++) {
                    if (spItinerary.getAdapter().getItem(x).toString().equals(i2.toString())) {
                        spItinerary.setSelection(x);
                        tvSequenceItinerary.setText(String.valueOf(i2.getSequence()));
                        break;
                    }
                }
            }
            nrPerson = itineraryHasTransport.getPerson_id();
            if (nrPerson!=null && nrPerson>0) {
                Person p2 = Database.mPersonDao.fetchPersonById(nrPerson);
                for (int x = 1; x <= spPerson.getAdapter().getCount(); x++) {
                    if (spPerson.getAdapter().getItem(x).toString().equals(p2.toString())) {
                        spPerson.setSelection(x);
                        break;
                    }
                }
            }
            VehicleHasTravel vehicleHasTravel;
            if (itineraryHasTransport.getVehicle_id() == null || itineraryHasTransport.getVehicle_id() == 0) {
                vehicleHasTravel = Database.mVehicleHasTravelDao.findVehicleHasTravelByTravelTransport(itineraryHasTransport.getTravel_id(), itineraryHasTransport.getTransport_id());
            } else {
                vehicleHasTravel = Database.mVehicleHasTravelDao.findVehicleHasTravelByTravelVehicle(itineraryHasTransport.getTravel_id(), itineraryHasTransport.getVehicle_id());
            }
            etAVGConsumption.setText(String.valueOf(vehicleHasTravel.getAvg_consumption()));
            etAVGCostLitre.setText(String.valueOf(vehicleHasTravel.getAvg_cost_litre()));
        }
    }

    final ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 128) {
                        Intent data = result.getData();

                        if (data != null) {
                            int x = data.getIntExtra("resulted_value", 0);
                            List<Transport> transports = Database.mTransportDao.fetchAllTransport();
                            adapterT.clear();
                            adapterT.addAll(transports);
                            adapterT.notifyDataSetChanged() ;
                            for (int i = 0; i < transports.size(); i++) {
                                if (transports.get(i).getId() == x) spTransport.setSelection(i);
                            }
                            spTransport.requestFocus();
                        }
                    }
                }
            }
    );

    @Override
    public void onItemClick(int position) {

        if (nrTransportType != position) { nrTransportType = position; }
        else { nrTransportType = -1; }

        Utils.selected_position = nrTransportType;

        if (nrTransportType == 0) {
            llTransportTypeOwn.setVisibility(View.VISIBLE);
            llTransportTypeOthers.setVisibility(View.GONE);
            llAVG.setVisibility(View.GONE);
        } else {
            llTransportTypeOwn.setVisibility(View.GONE);
            llTransportTypeOthers.setVisibility(View.VISIBLE);
            if (nrTransportType==2) {
                llAVG.setVisibility(View.VISIBLE);
            } else {
                llAVG.setVisibility(View.GONE);
            }
        }
    }

    public void addListenerOnButtonSave() {
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            boolean isSave = true;

            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                ItineraryHasTransport it1 = new ItineraryHasTransport();
                if (nrItinerary != null && nrItinerary>0) {
                    it1.setTravel_id(nrTravel);
                    it1.setTransport_type(nrTransportType);
                    it1.setVehicle_id(nrOwnVehicle);
                    it1.setTransport_id(nrTransport);
                    it1.setItinerary_id(nrItinerary);
                    it1.setPerson_id(nrPerson);
                    it1.setDriver(cbDriver.isChecked() ? 1 : 0);
                    it1.setSequence_itinerary(Integer.parseInt(tvSequenceItinerary.getText().toString()));
                }
                VehicleHasTravel vt1;
                if (nrOwnVehicle == null || nrOwnVehicle == 0) {
                    vt1 = Database.mVehicleHasTravelDao.findVehicleHasTravelByTravelTransport(nrTravel, nrTransport);
                } else {
                    vt1 = Database.mVehicleHasTravelDao.findVehicleHasTravelByTravelVehicle(nrTravel, nrOwnVehicle);
                }
                if (cbDriver.isChecked()) {
                    vt1.setTravel_id(nrTravel);
                    vt1.setVehicle_id(nrOwnVehicle);
                    vt1.setTransport_id(nrTransport);
                    vt1.setPerson_id(nrPerson);
                    if (!etAVGConsumption.getText().toString().isEmpty()) {
                        vt1.setAvg_consumption(Float.parseFloat(etAVGConsumption.getText().toString()));
                    } else { vt1.setAvg_consumption(0); }
                    if (!etAVGCostLitre.getText().toString().isEmpty()) {
                        vt1.setAvg_cost_litre(Float.parseFloat(etAVGCostLitre.getText().toString()));
                    } else { vt1.setAvg_cost_litre(0); }
                }

                if (!opInsert) {
                    try {
                        if (nrItinerary != null && nrItinerary>0) {
                            it1.setId(itineraryHasTransport.getId());
                            isSave = Database.mItineraryHasTransportDao.updateItineraryHasTransport(it1);
                        }
                        if (cbDriver.isChecked() && isSave) {
                            isSave = Database.mVehicleHasTravelDao.updateVehicleHasTravel(vt1);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        if (nrItinerary != null && nrItinerary>0) {
                            isSave = Database.mItineraryHasTransportDao.addItineraryHasTransport(it1);
                        }
                        if (cbDriver.isChecked() && isSave) {
                            if(vt1.getId()==null || vt1.getId()==0){
                                isSave = Database.mVehicleHasTravelDao.addVehicleHasTravel(vt1);
                            } else {
                                isSave =Database.mVehicleHasTravelDao.updateVehicleHasTravel(vt1);
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                setResult(isSave ? 1 : 0);
                if (isSave) {
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private boolean validateData() {
        boolean isValid = true;
        try {
            if (nrTransportType == -1 ||
                nrTravel == 0 ||
                (nrTransportType==0 ? nrOwnVehicle == 0 : nrTransport == 0) ||
                //nrItinerary == 0 ||
                nrPerson == 0 )
            {
                isValid = false;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.Data_Validator_Error +" - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }
}