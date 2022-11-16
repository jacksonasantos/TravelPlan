package com.jacksonasantos.travelplan.ui.vehicle;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.general.MyGalleryImageAdapter;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class VehicleActivity extends AppCompatActivity {

    private RadioGroup rgVehicleType;
    private int rbVehicleType;
    public ImageView imgVehicle_Image;                   // TODO - Adjust image search and conversion (directory or internet)
    public ImageView imgEdit_Image;
    public ImageView imgDelete_Image;
    Bitmap raw;
    private byte[] imgArray;
    private EditText etNameVehicle;
    private EditText etShortNameVehicle;
    private EditText etBrand;                            // TODO - Implement API of BRAND´s
    private EditText etModel;
    private AutoCompleteTextView spinFuelType;
    private int nrSpinFuelType;

    private EditText etYearModel;
    private EditText etYearManufacture;
    private EditText etLicencePlateVehicle;
    private EditText etColor;
    private Button btColorCode;
    private EditText etVin;
    private EditText etLicenceNumber;
    private EditText etStateVehicle;
    private EditText etCityVehicle;
    private EditText etAcquisition;
    private EditText etSale;

    private EditText etDoors;
    private EditText etCapacity;
    private EditText etPower;
    private EditText etEstimatedValue;
    private EditText etFullCapacity;
    private EditText etAVGConsumption;
    private EditText etAVGCostLitre;
    private EditText etDtOdometer;
    private EditText etOdometer;

    // Attributes that serve to record the supply information without the Odometer Value information, used to calculate the consumption average
    private EditText etDtLastFueling;
    private RadioGroup rgLastSupplyReasonType;
    private int rbLastSupplyReasonType =0;
    private EditText etAccumulatedNumberLiters;
    private EditText etAccumulatedSupplyValue;

    private Vehicle vehicle;
    private boolean opInsert = true;

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.Vehicle_Id);
        setContentView(R.layout.activity_vehicle);

        Bundle extras = getIntent().getExtras();
        vehicle = new Vehicle();
        if (extras != null) {
            vehicle.setId(extras.getInt("id"));
            vehicle = Database.mVehicleDao.fetchVehicleById(vehicle.getId());
            opInsert = false;
        } else {
            vehicle.setColor_code(Color.BLACK);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();
        addListenerOnImageVehicle();
        addListenerOnDeleteImageVehicle();
        addListenerOnEditImageVehicle();

        rgVehicleType = findViewById(R.id.rgVehicleType);
        etNameVehicle = findViewById(R.id.etNameVehicle);
        imgVehicle_Image = findViewById(R.id.imgVehicle_Image);
        imgEdit_Image = findViewById(R.id.imgEdit_Image);
        imgDelete_Image = findViewById(R.id.imgDelete_Image);
        etShortNameVehicle = findViewById(R.id.etShortNameVehicle);
        etBrand = findViewById(R.id.etBrand);
        etModel = findViewById(R.id.etModel);
        spinFuelType = findViewById(R.id.spinFuelType);
        etYearModel = findViewById(R.id.etYearModel);
        etYearManufacture = findViewById(R.id.etYearManufacture);
        etLicencePlateVehicle = findViewById(R.id.etLicencePlateVehicle);
        etColor = findViewById(R.id.etColor);
        btColorCode = findViewById(R.id.btColorCode);
        etVin = findViewById(R.id.etVin);
        etLicenceNumber = findViewById(R.id.etLicenceNumber);
        etStateVehicle = findViewById(R.id.etStateVehicle);
        etCityVehicle = findViewById(R.id.etCityVehicle);
        etAcquisition = findViewById(R.id.etAcquisition);
        etSale = findViewById(R.id.etSale);
        etDoors = findViewById(R.id.etDoors);
        etCapacity = findViewById(R.id.etCapacity);
        etPower = findViewById(R.id.etPower);
        etEstimatedValue = findViewById(R.id.etEstimatedValue);
        etFullCapacity = findViewById(R.id.etFullCapacity);
        etAVGConsumption = findViewById(R.id.etAVGConsumption);
        etAVGCostLitre = findViewById(R.id.etAVGCostLitre);
        etDtOdometer = findViewById(R.id.etDtOdometer);
        etOdometer = findViewById(R.id.etOdometer);
        etDtLastFueling = findViewById(R.id.etDtLastFueling);
        rgLastSupplyReasonType = findViewById(R.id.rgLastSupplyReasonType);
        etAccumulatedNumberLiters = findViewById(R.id.etAccumulatedNumberLiters);
        etAccumulatedSupplyValue= findViewById(R.id.etAccumulatedSupplyValue);

        btColorCode.setOnClickListener(view -> ColorPickerDialogBuilder
                .with(VehicleActivity.this)
                .setTitle("Choose color")
                .showAlphaSlider(false)
                //.initialColor(vehicle.getColor_code())
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(6)
                .setOnColorSelectedListener(selectedColor -> btColorCode.setText(String.valueOf(selectedColor)))
                .setPositiveButton("ok", (dialog, selectedColor, allColors) -> btColorCode.setBackgroundColor(selectedColor))
                .setNegativeButton("cancel", (dialog, which) -> { })
                .build()
                .show());

        Utils.addRadioButtonResources(R.array.vehicle_type_array, rgVehicleType, this);
        rgVehicleType.setOnCheckedChangeListener((group, checkedId) -> rbVehicleType = checkedId);
        Utils.createSpinnerResources(R.array.fuel_type_array, spinFuelType, this);
        nrSpinFuelType = 0;
        spinFuelType.setOnItemClickListener((adapterView, view, i, l) -> nrSpinFuelType = (int) adapterView.getItemIdAtPosition(i));
        etAcquisition.addTextChangedListener(new DateInputMask(etAcquisition));
        etSale.addTextChangedListener(new DateInputMask(etSale));
        etDtOdometer.addTextChangedListener(new DateInputMask(etDtOdometer));

        etDtLastFueling.addTextChangedListener(new DateInputMask(etDtLastFueling));
        Utils.addRadioButtonResources(R.array.supply_reason_type_array, rgLastSupplyReasonType, this);
        rgLastSupplyReasonType.setOnCheckedChangeListener((group, checkedId) -> rbLastSupplyReasonType = checkedId);

        if (vehicle != null) {
            rgVehicleType.check(vehicle.getVehicle_type());
            etNameVehicle.setText(vehicle.getName());
            imgArray = vehicle.getImage();
            if(imgArray!=null){
                raw = BitmapFactory.decodeByteArray(imgArray,0, imgArray.length);
                imgVehicle_Image.setImageBitmap(raw);
            } else {
                imgVehicle_Image.setImageResource(vehicle.getVehicleTypeImage(vehicle.getVehicle_type()));
            }
            etShortNameVehicle.setText(vehicle.getShort_name());
            etBrand.setText(vehicle.getBrand());
            etModel.setText(vehicle.getModel());
            nrSpinFuelType=vehicle.getFuel_type();
            spinFuelType.setText(getResources().getStringArray(R.array.fuel_type_array)[nrSpinFuelType],false);
            etYearModel.setText(vehicle.getYear_model());
            etYearManufacture.setText(vehicle.getYear_manufacture());
            etLicencePlateVehicle.setText(vehicle.getLicense_plate());
            etColor.setText(vehicle.getColor());
            btColorCode.setText(String.valueOf(vehicle.getColor_code()));
            btColorCode.setBackgroundColor(vehicle.getColor_code());
            etVin.setText(vehicle.getVin());
            etLicenceNumber.setText(vehicle.getLicence_number());
            etStateVehicle.setText(vehicle.getState());
            etCityVehicle.setText(vehicle.getCity());
            etAcquisition.setText(Utils.dateToString(vehicle.getDt_acquisition()));
            etSale.setText(Utils.dateToString(vehicle.getDt_sale()));
            etDoors.setText(String.valueOf(vehicle.getDoors()));
            etCapacity.setText(String.valueOf(vehicle.getCapacity()));
            etPower.setText(String.valueOf(vehicle.getPower()));
            etEstimatedValue.setText(String.valueOf(vehicle.getEstimated_value()));
            etFullCapacity.setText(String.valueOf(vehicle.getFull_capacity()));
            etAVGConsumption.setText(String.valueOf(vehicle.getAvg_consumption()));
            etAVGCostLitre.setText(String.valueOf(vehicle.getAvg_cost_litre()));
            etDtOdometer.setText(Utils.dateToString(vehicle.getDt_odometer()));
            etOdometer.setText(vehicle.getOdometer()==0?null:String.valueOf(vehicle.getOdometer()));
            etDtLastFueling.setText(Utils.dateToString(vehicle.getDt_last_fueling()));
            rgLastSupplyReasonType.check(vehicle.getLast_supply_reason_type());
            etAccumulatedNumberLiters.setText(String.valueOf(vehicle.getAccumulated_number_liters()));
            etAccumulatedSupplyValue.setText(String.valueOf(vehicle.getAccumulated_supply_value()));
        }
    }

    private void button() {
        AtomicInteger imgPos = new AtomicInteger();

        ArrayList<File> list = Utils.imageReader(Objects.requireNonNull(getExternalFilesDir("/vehicles")));
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_my_files, null);
        GridView gV = v.findViewById(R.id.gridView1);
        gV.setAdapter(new MyGalleryImageAdapter(this, list));
        gV.setOnItemClickListener((parent, view1, position, id) -> imgPos.set(position));
        final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setView(v)
                .setPositiveButton(R.string.OK, (dialog, which) -> {
                    Bitmap myBitmap = BitmapFactory.decodeFile(list.get(imgPos.get()).getAbsolutePath());
                    imgVehicle_Image.setImageBitmap(myBitmap);
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.Cancel, (dialog, which) -> dialog.cancel());
        builder2.setCancelable(false);
        builder2.create().show();
    }

    private void addListenerOnImageVehicle() {
        ImageView imgVehicle_Image = findViewById(R.id.imgVehicle_Image);
        imgVehicle_Image.setOnClickListener( view -> button());
    }

    private void addListenerOnEditImageVehicle() {
        ImageView imgEditVehicle = findViewById(R.id.imgEdit_Image);
        imgEditVehicle.setOnClickListener(view -> button());
    }

    private void addListenerOnDeleteImageVehicle() {
        ImageView imgDeleteVehicle = findViewById(R.id.imgDelete_Image);
        imgDeleteVehicle.setOnClickListener(view -> {
            imgVehicle_Image.setImageBitmap(null);
            imgArray= vehicle.getImage();
        });
    }

    public void addListenerOnButtonSave() {
       Button btSaveVehicle = findViewById(R.id.btSaveVehicle);

        btSaveVehicle.setOnClickListener(v -> {
            boolean isSave = false;
            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                final Vehicle v1 = new Vehicle();
                v1.setVehicle_type(rbVehicleType);
                v1.setName(etNameVehicle.getText().toString());

                if (imgVehicle_Image.getDrawable().isFilterBitmap()) {
                    Bitmap bitmap = ((BitmapDrawable) imgVehicle_Image.getDrawable()).getBitmap();
                    if (bitmap != null) {
                        ByteArrayOutputStream saida = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, saida);
                        byte[] img = saida.toByteArray();
                        v1.setImage(img);
                    }
                }
                v1.setShort_name(etShortNameVehicle.getText().toString());
                v1.setBrand(etBrand.getText().toString());
                v1.setModel(etModel.getText().toString());
                v1.setFuel_type(nrSpinFuelType);
                v1.setYear_model(etYearModel.getText().toString());
                v1.setYear_manufacture(etYearManufacture.getText().toString());
                v1.setLicense_plate(etLicencePlateVehicle.getText().toString());
                v1.setColor(etColor.getText().toString());
                if (!btColorCode.getText().toString().isEmpty()) {
                    v1.setColor_code(Integer.parseInt(btColorCode.getText().toString()));
                }
                v1.setVin(etVin.getText().toString());
                v1.setLicence_number(etLicenceNumber.getText().toString());
                v1.setState(etStateVehicle.getText().toString());
                v1.setCity(etCityVehicle.getText().toString());
                v1.setDt_acquisition(Utils.stringToDate(etAcquisition.getText().toString()));
                v1.setDt_sale(Utils.stringToDate(etSale.getText().toString()));
                if (!etDoors.getText().toString().isEmpty()) {
                    v1.setDoors(Integer.parseInt(etDoors.getText().toString()));
                } else { v1.setDoors(0); }
                if (!etCapacity.getText().toString().isEmpty()) {
                    v1.setCapacity(Integer.parseInt(etCapacity.getText().toString()));
                } else { v1.setCapacity(0); }
                if (!etPower.getText().toString().isEmpty()) {
                    v1.setPower(Integer.parseInt(etPower.getText().toString()));
                } else { v1.setPower(0); }
                if (!etEstimatedValue.getText().toString().isEmpty()) {
                    v1.setEstimated_value(Double.parseDouble(etEstimatedValue.getText().toString()));
                } else { v1.setEstimated_value(0); }
                if (!etFullCapacity.getText().toString().isEmpty()) {
                    v1.setFull_capacity(Integer.parseInt(etFullCapacity.getText().toString()));
                } else { v1.setFull_capacity(0); }
                if (!etAVGConsumption.getText().toString().isEmpty()) {
                    v1.setAvg_consumption(Float.parseFloat(etAVGConsumption.getText().toString()));
                } else { v1.setAvg_consumption(0); }
                if (!etAVGCostLitre.getText().toString().isEmpty()) {
                    v1.setAvg_cost_litre(Float.parseFloat(etAVGCostLitre.getText().toString()));
                } else { v1.setAvg_cost_litre(0); }
                v1.setDt_odometer(Utils.stringToDate(etDtOdometer.getText().toString()));
                if (!etOdometer.getText().toString().isEmpty()) {
                    v1.setOdometer(Integer.parseInt(etOdometer.getText().toString()));
                } else { v1.setOdometer(0); }
                v1.setDt_last_fueling(Utils.stringToDate(etDtLastFueling.getText().toString()));
                if (rbLastSupplyReasonType>0||rgLastSupplyReasonType.getCheckedRadioButtonId()!= 0){
                    v1.setLast_supply_reason_type(findViewById(rbLastSupplyReasonType).getId());
                } else {v1.setLast_supply_reason_type(0);}
                if (!etAccumulatedNumberLiters.getText().toString().isEmpty()) {
                    v1.setAccumulated_number_liters(Double.parseDouble(etAccumulatedNumberLiters.getText().toString()));
                } else { v1.setAccumulated_number_liters(0); }
                if (!etAccumulatedSupplyValue.getText().toString().isEmpty()) {
                    v1.setAccumulated_supply_value(Double.parseDouble(etAccumulatedSupplyValue.getText().toString()));
                } else { v1.setAccumulated_supply_value(0); }

                if (!opInsert) {
                    try {
                        v1.setId(vehicle.getId());
                        isSave = Database.mVehicleDao.updateVehicle(v1);
                    } catch (Exception e ){
                        Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        isSave = Database.mVehicleDao.addVehicle(v1);
                    } catch ( Exception e ) {
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
            if ( rbVehicleType==0 ||
                etNameVehicle.getText().toString().isEmpty() ||
                etShortNameVehicle.getText().toString().isEmpty() ||
                etBrand.getText().toString().isEmpty() ||
                etModel.getText().toString().isEmpty() ||
                String.valueOf(nrSpinFuelType).isEmpty() ||
                etYearModel.getText().toString().isEmpty() ||
                etYearManufacture.getText().toString().isEmpty() ||
                etLicencePlateVehicle.getText().toString().isEmpty() ||
                etColor.getText().toString().isEmpty() ||
                //etColorCode.getText().toString().isEmpty() ||
                etVin.getText().toString().isEmpty() ||
                etLicenceNumber.getText().toString().isEmpty() ||
                etStateVehicle.getText().toString().isEmpty() ||
                etCityVehicle.getText().toString().isEmpty() ||
                etAcquisition.getText().toString().isEmpty() ||
                //etSale.getText().toString().isEmpty() ||
                etDoors.getText().toString().isEmpty() ||
                etCapacity.getText().toString().isEmpty() ||
                etPower.getText().toString().isEmpty() ||
                //etEstimatedValue.getText().toString().isEmpty() ||
                etFullCapacity.getText().toString().isEmpty() //||
                //etAVGConsumption.getText().toString().isEmpty() ||
                //etAVGCostLitre.getText().toString().isEmpty() ||
                //etDtOdometer.getText().toString().isEmpty() ||
                //etOdometer.getText().toString().isEmpty() ||
                //etDtLastFueling.getText().toString().isEmpty() ||
                //etAccumulatedNumberLiters.getText().toString().isEmpty() ||
                //etAccumulatedSupplyValue.getText().toString().isEmpty() ||
                //String.valueOf(findViewById(rbLastSupplyReasonType).getId()).trim().isEmpty()
            ){
                isValid = false;
            }
        }catch ( Exception e ) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Data_Validator_Error )+" - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }
}