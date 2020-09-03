package com.jacksonasantos.travelplan.ui.vehicle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jacksonasantos.travelplan.DAO.Database;
import com.jacksonasantos.travelplan.DAO.Vehicle;
import com.jacksonasantos.travelplan.DAO.VehicleDAO;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.ui.utility.CustomOnItemSelectedListener;

public class VehicleActivity extends Activity {

    private EditText etNameVehicle;
    private EditText etLicencePlateVehicle;
    private EditText etFullCapacity;
    private EditText etAVGConsumption;
    private Spinner spintypefuel;
    private Button btSaveVehicle;
    private Vehicle vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);
        setTitle("Identificação do Veículo");

        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

        etNameVehicle = findViewById(R.id.etNameVehicle);
        etLicencePlateVehicle = findViewById(R.id.etLicencePlateVehicle);
        etFullCapacity = findViewById(R.id.etFullCapacity);
        etAVGConsumption = findViewById(R.id.etAVGConsumption);
        spintypefuel = findViewById(R.id.spintypefuel);

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.type_fuel_array, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spintypefuel.setAdapter(adapterSpinner);

        Intent intent = getIntent();
        vehicle = (Vehicle) intent.getSerializableExtra("vehicle");
        if (vehicle != null) {
            etNameVehicle.setText(vehicle.name);
            etLicencePlateVehicle.setText(vehicle.license_plate);
            etFullCapacity.setText(vehicle.full_capacity);
            etAVGConsumption.setText((int) vehicle.avg_consumption);
            //spintypefuel.setText(vehicle.type_fuel);
        }
    }

    public void addListenerOnButton() {
        etNameVehicle = findViewById(R.id.etNameVehicle);
        etLicencePlateVehicle = findViewById(R.id.etLicencePlateVehicle);
        etFullCapacity = findViewById(R.id.etFullCapacity);
        etAVGConsumption = findViewById(R.id.etAVGConsumption);
        spintypefuel = findViewById(R.id.spintypefuel);
        btSaveVehicle = findViewById(R.id.btSaveVehicle);

        btSaveVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSave = false;

                Toast.makeText(VehicleActivity.this,
                        "OnClickListener : " +
                                "\nSpinner 1 : "+ String.valueOf(spintypefuel.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();

                Database db = new Database(VehicleActivity.this);
//                db.open();

                //Spinner spinner = (Spinner) findViewById(R.id.spintypefuel);
/*                if (vehicle != null){
                    Toast.makeText(this, "Exemplo UPDATE", Toast.LENGTH_SHORT).show();
                    isSave = Database.mVehicleDao.updateVehicle(vehicle);
                } else {
                    Toast.makeText(this, "Exemplo ADD", Toast.LENGTH_SHORT).show();
                    vehicle.oid = etNameVehicle.getText().toString();
                    vehicle.name = etOIDVehicle.getText().toString();
                    vehicle.license_plate = etLicencePlateVehicle.getText().toString();
                    vehicle.full_capacity = Integer.parseInt(etFullCapacity.getText().toString());
                    vehicle.avg_consumption = Double.parseDouble(etAVGConsumption.getText().toString());
                    //vehicle.type_fuel = spinner.setOnItemSelectedListener(this);

                    isSave = Database.mVehicleDao.addVehicle(vehicle);
                    Toast.makeText(this, "Exemplo ADD + "+isSave, Toast.LENGTH_SHORT).show();
                }
*/
//                db.close();
                setResult(isSave ? 1 : 0);
                finish();
            }
        });
    }

    public void addListenerOnSpinnerItemSelection() {
        spintypefuel = (Spinner) findViewById(R.id.spintypefuel);
        spintypefuel.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }
}

/*      //em qualquer parte do seu aplicativo, você pode pegar um usuário por seu id desta forma:
        Vehicle vehicle = Database.mVehicleDao.fetchVehicleById(VehicleId);
 */