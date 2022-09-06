package com.jacksonasantos.travelplan.ui.vehicle;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.VehicleMaintenanceItem;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;

import java.util.ArrayList;
import java.util.List;

public class VehicleMaintenanceItemActivity extends AppCompatActivity implements ServiceTypeListAdapter.ItemClickListener {

    private TextView txVehicleName;
    private ImageView imVehicleType;
    private TextView txVehicleLicencePlate;
    private TextView txVehicleOdometer;
    private Integer nrVehicleId = 0;
    private Integer nrServiceType = -1;
    public RecyclerView listServiceType;
    public RecyclerView listMaintenanceItem;
    public Button btnDone;

    public ServiceTypeListAdapter adapterServiceType;

    final Globals g = Globals.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Vehicle_MaintenanceItem);
        setContentView(R.layout.dialog_vehicle_maintenance_item);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getInt( "vehicle_id") > 0) {
                nrVehicleId = extras.getInt("vehicle_id");
            } else {
                nrVehicleId = g.getIdVehicle();
            }
        }

        addListenerOnButtonDone();
        txVehicleName = findViewById(R.id.txVehicleName);
        imVehicleType = findViewById(R.id.imVehicleType);
        txVehicleLicencePlate = findViewById(R.id.txVehicleLicencePlate);
        txVehicleOdometer = findViewById(R.id.txVehicleOdometer);
        listServiceType = findViewById(R.id.listServiceType);
        listMaintenanceItem = findViewById(R.id.listMaintenanceItem);
        btnDone  = findViewById(R.id.btnDone);

        final Vehicle vehicle = Database.mVehicleDao.fetchVehicleById(nrVehicleId);
        txVehicleName.setText(vehicle.getName());
        txVehicleLicencePlate.setText(vehicle.getLicense_plate());
        txVehicleOdometer.setText(String.valueOf(vehicle.getOdometer()));
        imVehicleType.setImageResource(vehicle.getVehicleTypeImage(vehicle.getVehicle_type()));

        nrServiceType = -1;

        List<Integer> vServiceType = new ArrayList<>();
        for(int i = 0; i < 23; i++) {
            vServiceType.add(i);
        }

        adapterServiceType = new ServiceTypeListAdapter(vServiceType, this);

        adapterServiceType.addItemClickListener(this);
        listServiceType.setAdapter(adapterServiceType);
        listServiceType.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<VehicleMaintenanceItem> mVehicle_maintenance_item = Database.mVehicleMaintenanceItemDao.findVehicleMaintenanceItems(nrVehicleId, nrServiceType);
        VehicleMaintenanceItemListAdapter adapterVehicleMaintenanceItem = new VehicleMaintenanceItemListAdapter(mVehicle_maintenance_item, this);
        listMaintenanceItem.setAdapter(adapterVehicleMaintenanceItem);
        listMaintenanceItem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onItemClick(int position) {
        if (nrServiceType == position) nrServiceType = -1;
        else nrServiceType = position;

        List<VehicleMaintenanceItem> mVehicle_maintenance_item = Database.mVehicleMaintenanceItemDao.findVehicleMaintenanceItems(nrVehicleId, nrServiceType);
        VehicleMaintenanceItemListAdapter adapterVehicleMaintenanceItem = new VehicleMaintenanceItemListAdapter(mVehicle_maintenance_item, this);
        listMaintenanceItem.setAdapter(adapterVehicleMaintenanceItem);
        listMaintenanceItem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    public void addListenerOnButtonDone() {
        Button btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(v -> finish());
    }
}