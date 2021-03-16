package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.TravelExpenses;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.VehicleHasTravel;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class TravelActivity extends AppCompatActivity {
    private EditText etDescription;
    private EditText etDeparture_date;
    private EditText etReturn_date;
    private EditText etNote;
    private int nrStatus;
    private TextView tvStatus;

    private AutoCompleteTextView spinVehicle;
    private int nrspinVehicle;
    private RecyclerView rvVehicleTravel;

    private ConstraintLayout labelTravelExpenses;
    private RecyclerView rvTravelExpenses;

    private VehicleTravelListAdapter adapterVehicleTravel;
    private TravelExpensesListAdapter adapterTravelExpenses;

    private boolean opInsert = true;
    private Travel travel;

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.Travel);
        setContentView(R.layout.activity_travel);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();
        addListenerOnButtonAdd();

        etDescription = findViewById(R.id.etDescription);
        etDeparture_date = findViewById(R.id.etDeparture_date);
        etReturn_date = findViewById(R.id.etReturn_date);
        etNote = findViewById(R.id.etNote);
        tvStatus = findViewById(R.id.tvStatus);

        spinVehicle = findViewById(R.id.spinVehicle);
        rvVehicleTravel = findViewById(R.id.rvVehicleTravel);

        labelTravelExpenses = findViewById(R.id.labelTravelExpenses);
        rvTravelExpenses = findViewById(R.id.rvTravelExpenses);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    protected void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            travel = new Travel();
            if (extras.getInt( "travel_id") > 0) {
                travel.setId(extras.getInt("travel_id"));
                travel = Database.mTravelDao.fetchTravelById(travel.getId());
                opInsert = false;
            }
        }

        if (travel != null) {
            etDescription.setText(travel.getDescription());
            etDeparture_date.setText(Utils.dateToString(travel.getDeparture_date()));
            etReturn_date.setText(Utils.dateToString(travel.getReturn_date()));
            etNote.setText(travel.getNote());
            nrStatus=travel.getStatus();
            tvStatus.setText(getResources().getStringArray(R.array.travel_status_array)[nrStatus]);
        }

        etDeparture_date.addTextChangedListener(new DateInputMask(etDeparture_date));
        etReturn_date.addTextChangedListener(new DateInputMask(etReturn_date));

        final List<Vehicle> vehicles = Database.mVehicleDao.fetchArrayVehicles();
        ArrayAdapter<Vehicle> adapterVehicle = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehicles);
        adapterVehicle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinVehicle.setAdapter(adapterVehicle);

        final Vehicle[] v1 = {new Vehicle()};
        spinVehicle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                v1[0] = (Vehicle) parent.getItemAtPosition(position);
                nrspinVehicle = v1[0].getId();
            }
        });
        adapterVehicle.notifyDataSetChanged();

        if (travel != null) {
            adapterVehicleTravel = new VehicleTravelListAdapter(Database.mVehicleHasTravelDao.fetchAllVehicleHasTravelByTravel(travel.getId()), getApplicationContext(),"Travel");
            rvVehicleTravel.setAdapter(adapterVehicleTravel);
            rvVehicleTravel.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapterVehicleTravel.notifyDataSetChanged();

            @SuppressLint("InflateParams") View vL = getLayoutInflater().inflate(R.layout.fragment_item_travel_expenses, null);
            labelTravelExpenses.removeAllViews();
            TextView lblExpenseType = vL.findViewById(R.id.txtExpenseType);
            TextView lblExpectedValue = vL.findViewById(R.id.txtExpectedValue);
            TextView lblNote = vL.findViewById(R.id.txtNote);
            ImageButton btnAddExpenses = vL.findViewById(R.id.btnDelete);

            lblExpenseType.setText(getString(R.string.TravelExpenses_ExpenseType));
            lblExpectedValue.setText(getString(R.string.TravelExpenses_ExpectedValue));
            lblNote.setText(getString(R.string.TravelExpenses_Note));
            btnAddExpenses.setImageResource(R.drawable.ic_button_add);
            labelTravelExpenses.setBackgroundColor(Color.rgb(209,193,233));
            labelTravelExpenses.addView(vL);

            adapterTravelExpenses = new TravelExpensesListAdapter(Database.mTravelExpensesDao.fetchAllTravelExpensesByTravel(travel.getId()), getApplicationContext());
            rvTravelExpenses.setAdapter(adapterTravelExpenses);
            rvTravelExpenses.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapterTravelExpenses.notifyDataSetChanged();

            btnAddExpenses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater li = LayoutInflater.from(v.getContext());
                    View promptsView = li.inflate(R.layout.activity_travel_expenses, null);

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

                    alertDialogBuilder.setView(promptsView);
                    final Spinner spinExpenseType = promptsView.findViewById(R.id.spinExpenseType);
                    final EditText etExpectedValue = promptsView.findViewById(R.id.etExpectedValue);
                    final EditText etNote = promptsView.findViewById(R.id.etNote);

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    boolean isSave = false;

                                    TravelExpenses TE = new TravelExpenses();

                                    TE.setTravel_id(travel.getId());
                                    TE.setExpense_type(spinExpenseType.getSelectedItemPosition());
                                    if (!etExpectedValue.getText().toString().isEmpty())            { TE.setExpected_value(Double.parseDouble(etExpectedValue.getText().toString()));}
                                    TE.setNote(etNote.getText().toString());

                                    try {
                                        isSave = Database.mTravelExpensesDao.addTravelExpenses(TE);
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                    setResult(isSave ? 1 : 0);
                                    if (!isSave) {
                                        Toast.makeText(getApplicationContext(), R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                                    } else {
                                        adapterTravelExpenses = new TravelExpensesListAdapter(Database.mTravelExpensesDao.fetchAllTravelExpensesByTravel(travel.getId()), getApplicationContext());
                                        rvTravelExpenses.setAdapter(adapterTravelExpenses);
                                        rvTravelExpenses.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                        adapterTravelExpenses.notifyDataSetChanged();
                                    }
                                }
                            })
                            .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
        }
    }

    public void addListenerOnButtonAdd() {
        ImageButton btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSave = false;

                if (nrspinVehicle==0) {
                    Toast.makeText(getApplicationContext(), "Veiculo não selecionado", Toast.LENGTH_LONG).show();
                } else {
                    final VehicleHasTravel vt1 = new VehicleHasTravel();

                    vt1.setTravel_id(travel.getId());
                    vt1.setVehicle_id(nrspinVehicle);
                    try {
                        isSave = Database.mVehicleHasTravelDao.addVehicleHasTravel(vt1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    setResult(isSave ? 1 : 0);
                    if (!isSave) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                    } else {
                        adapterVehicleTravel = new VehicleTravelListAdapter(Database.mVehicleHasTravelDao.fetchAllVehicleHasTravelByTravel(travel.getId()), getApplicationContext(),"travel");
                        rvVehicleTravel.setAdapter(adapterVehicleTravel);
                        rvVehicleTravel.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapterVehicleTravel.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void addListenerOnButtonSave() {
        Button btSaveTravel = findViewById(R.id.btSaveTravel);

        btSaveTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSave = false;

                if (!validateData()) {
                    Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
                } else {
                    final Travel t1 = new Travel();

                    t1.setDescription(etDescription.getText().toString());
                    t1.setDeparture_date(Utils.stringToDate(etDeparture_date.getText().toString()));
                    t1.setReturn_date(Utils.stringToDate(etReturn_date.getText().toString()));
                    t1.setNote(etNote.getText().toString());
                    t1.setStatus(nrStatus);

                    if (!opInsert) {
                        try {
                            t1.setId(travel.getId());
                            isSave = Database.mTravelDao.updateTravel(t1);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            isSave = Database.mTravelDao.addTravel(t1);
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
            }
        });
    }

    private boolean validateData() {
        boolean isValid = false;

        try {
            if (!etDescription.getText().toString().trim().isEmpty()
                && !etDeparture_date.getText().toString().trim().isEmpty()
                && !etReturn_date.getText().toString().trim().isEmpty()
                //&& !etNote.getText().toString().trim().isEmpty()
                //&& !tvStatus.getText().toString().trim().isEmpty()
                )
            {
                isValid = true;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.Data_Validator_Error +" - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }
}