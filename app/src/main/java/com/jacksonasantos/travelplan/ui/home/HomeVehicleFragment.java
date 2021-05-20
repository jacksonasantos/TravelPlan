package com.jacksonasantos.travelplan.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.FuelSupply;
import com.jacksonasantos.travelplan.dao.Insurance;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.VehicleGraphStatistics;
import com.jacksonasantos.travelplan.dao.VehicleStatistics;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;
import com.jacksonasantos.travelplan.ui.vehicle.FuelSupplyActivity;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeVehicleFragment extends Fragment implements View.OnClickListener {

    private Spinner spVehicle;
    private TextView tvLicencePlate;
    private ImageView imVehicleType;

    private ConstraintLayout layerFuelSupply;
    private TextView tvFuelSupplyDate;
    private TextView tvFuelSupplyLastOdometer;
    private TextView tvFuelSupplyNumberLiters;
    private TextView tvFuelSupplyValue;

    private ConstraintLayout layerInsuranceVehicle2;
    private ImageView imInsuranceType;
    private ImageView imInsuranceStatus;
    private TextView txtInsuranceFinalEffectiveDate;

    private ConstraintLayout layerStatisticsVehicle;
    private TextView tvAVGType1;
    private TextView tvAVGType2;
    private TextView tvAVGType3;
    private TextView tvAVGType9;
    private GraphView graphStatistics;

    private ConstraintLayout layerServiceVehicle;
    private RecyclerView maintenanceList;

    private ConstraintLayout layerMaintenanceItemVehicle;
    private RecyclerView nextVehicleMaintenanceList;

    private ConstraintLayout layerInsuranceVehicle;
    private RecyclerView insuranceList;

    int tamHorizontalLabels = 3;
    double vMinX = 0;
    double vMaxX = 0;
    double vMinY = 0;
    double vMaxY = 0;

    Globals g = Globals.getInstance();

    Locale locale = new Locale(g.getLanguage(), g.getCountry());
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_home_vehicle, container, false);
        imVehicleType = v.findViewById(R.id.imVehicleType);
        spVehicle =v.findViewById(R.id.spVehicle);
        tvLicencePlate = v.findViewById(R.id.tvLicencePlate);

        layerFuelSupply = v.findViewById(R.id.layerFuelSupply);
        tvFuelSupplyDate = v.findViewById(R.id.tvFuelSupplyDate);
        tvFuelSupplyLastOdometer = v.findViewById(R.id.tvFuelSupplyLastOdometer);
        tvFuelSupplyNumberLiters = v.findViewById(R.id.tvFuelSupplyNumberLiters);
        tvFuelSupplyValue = v.findViewById(R.id.tvFuelSupplyValue);

        layerInsuranceVehicle2 = v.findViewById(R.id.layerInsuranceVehicle2);
        imInsuranceType = v.findViewById(R.id.imInsuranceType);
        imInsuranceStatus = v.findViewById(R.id.imInsuranceStatus);
        txtInsuranceFinalEffectiveDate = v.findViewById(R.id.txtInsuranceFinalEffectiveDate);

        layerStatisticsVehicle = v.findViewById(R.id.layerStatisticsVehicle);
        tvAVGType1 = v.findViewById(R.id.tvAVGType1);
        tvAVGType2 = v.findViewById(R.id.tvAVGType2);
        tvAVGType3 = v.findViewById(R.id.tvAVGType3);
        tvAVGType9 = v.findViewById(R.id.tvAVGType9);
        graphStatistics = v.findViewById(R.id.graphStatistics);

        layerServiceVehicle = v.findViewById(R.id.layerServiceVehicle);
        maintenanceList = v.findViewById(R.id.listLastVehicleService);

        layerMaintenanceItemVehicle = v.findViewById(R.id.layerMaintenanceItemVehicle);
        nextVehicleMaintenanceList = v.findViewById(R.id.listNextVehicleMaintenance);

        layerInsuranceVehicle = v.findViewById(R.id.layerInsuranceVehicle);
        insuranceList = v.findViewById(R.id.listInsuranceExpiration);

        layerFuelSupply.setOnClickListener(v1 -> {
            Intent intent = new Intent (v1.getContext(), FuelSupplyActivity.class);
            intent.putExtra("vehicle_id", g.getIdVehicle());
            v1.getContext().startActivity(intent);
        });
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();

        final Database mDb = new Database(getActivity());
        mDb.open();

        final List<Vehicle> vehicles =  Database.mVehicleDao.fetchArrayVehicles();
        ArrayAdapter<Vehicle> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, vehicles);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spVehicle.setAdapter(adapter);

        int nrPosVehicle = g.getIdVehicle()-1;
        spVehicle.setSelection(nrPosVehicle);

        final Vehicle[] vehicle = {new Vehicle()};
        spVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Retrieves data from the vehicle selected in Spinner - layerVehicle
                vehicle[0] = (Vehicle) parent.getItemAtPosition(position);
                tvLicencePlate.setText(vehicle[0].getLicense_plate());
                imVehicleType.setImageResource(vehicle[0].getVehicleTypeImage(vehicle[0].getVehicle_type()));

                g.setIdVehicle(vehicle[0].getId());

                // Last Fuel Supply of Vehicle in Global selection - layerFuelSupply
                FuelSupply fuelSupply = Database.mFuelSupplyDao.findLastFuelSupply( g.getIdVehicle() );
                tvFuelSupplyDate.setText(Utils.dateToString(fuelSupply.getSupply_date()));
                tvFuelSupplyLastOdometer.setText(getString(R.string.Odometer)+ ": "+fuelSupply.getVehicle_odometer());
                tvFuelSupplyNumberLiters.setText(fuelSupply.getNumber_liters()==0? "0 "+g.getMeasureCapacity() :fuelSupply.getNumber_liters() +" "+g.getMeasureCapacity());
                tvFuelSupplyValue.setText(currencyFormatter.format(fuelSupply.getSupply_value()==0?BigDecimal.ZERO:fuelSupply.getSupply_value()));

                // Statistics of Vehicle in Global selection - layerStatisticsVehicle
                HomeVehicleStatisticsListAdapter adapterVehicle = new HomeVehicleStatisticsListAdapter(Database.mVehicleStatisticsDao.findLastVehicleStatistics(g.getIdVehicle()), getContext());
                if (adapterVehicle.getItemCount() > 0 ) {
                    layerStatisticsVehicle.setVisibility(View.VISIBLE);

                    // Graph Statistics Vehicle - https://github.com/jjoe64/GraphView
                    vMinX = 0; vMaxX = 0; vMinY = 0; vMaxY = 0;
                    tamHorizontalLabels = 3;
                    graphStatistics.removeAllSeries();                   // Clear the Graph
                    graphStatistics.onDataChanged(true, true);

                    addDataSeries();

                    graphStatistics.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
                    graphStatistics.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);
                    graphStatistics.getGridLabelRenderer().setTextSize(20);
                    graphStatistics.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graphStatistics.getContext(), new SimpleDateFormat("dd/MM")));
                    graphStatistics.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
                    graphStatistics.getGridLabelRenderer().setNumHorizontalLabels(tamHorizontalLabels);
                    graphStatistics.getGridLabelRenderer().setHumanRounding(false);
                    graphStatistics.getGridLabelRenderer().setVerticalAxisTitleTextSize(20);
                    graphStatistics.getGridLabelRenderer().setVerticalAxisTitleColor(R.color.design_default_color_secondary);
                    graphStatistics.getGridLabelRenderer().setVerticalAxisTitle(g.getMeasureConsumption());

                    graphStatistics.getViewport().setScalable(true);            //         activate horizontal zooming and scrolling
                    graphStatistics.getViewport().setScrollable(true);          //         activate horizontal scrolling
                    graphStatistics.getViewport().setScalableY(true);           //         activate horizontal and vertical zooming and scrolling
                    graphStatistics.getViewport().setScrollableY(true);         //         activate vertical scrolling
                    graphStatistics.getViewport().setXAxisBoundsManual(true);
                    graphStatistics.getViewport().setMinX(vMinX);
                    graphStatistics.getViewport().setMaxX(vMaxX);
                    graphStatistics.getViewport().setYAxisBoundsManual(true);
                    graphStatistics.getViewport().setMinY(vMinY);
                    graphStatistics.getViewport().setMaxY(vMaxY);

                    graphStatistics.refreshDrawableState();
                } else {
                    layerStatisticsVehicle.setVisibility(View.GONE);
                }
                adapterVehicle.notifyDataSetChanged();

                // Last Vehicle Services - layerServiceVehicle
                HomeVehicleMaintenanceListAdapter adapterMaintenance = new HomeVehicleMaintenanceListAdapter(Database.mMaintenanceDao.findReminderMaintenance( g.getIdVehicle() ), getContext(), 0);
                if (adapterMaintenance.getItemCount() > 0){
                    layerServiceVehicle.setVisibility(View.VISIBLE);
                    maintenanceList.setAdapter(adapterMaintenance);
                    maintenanceList.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    layerServiceVehicle.setVisibility(View.GONE);
                }
                adapterMaintenance.notifyDataSetChanged();

                // Next Vehicle Maintenance - layerMaintenanceItemVehicle
                HomeVehicleNextMaintenanceListAdapter adapterNextMaintenance = new HomeVehicleNextMaintenanceListAdapter(Database.mNextMaintenanceItemDao.findNextMaintenanceItem( g.getIdVehicle() ), getContext(),0);
                if (adapterNextMaintenance.getItemCount() > 0){

                    layerInsuranceVehicle.setVisibility(View.VISIBLE);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    List<Insurance> insurance = Database.mInsuranceDao.findReminderInsurance("V", g.getIdVehicle() );
                    imInsuranceType.setImageResource(insurance.get(0).getInsurance_typeImage(insurance.get(0).getInsurance_type()));
                    imInsuranceStatus.setImageResource(R.drawable.ic_ball);
                    try {
                        if (insurance.get(0).getStatus() == 1) {
                            imInsuranceStatus.setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
                        } else {
                            if (!(insurance.get(0).getFinal_effective_date() == null)) {
                                if (System.currentTimeMillis() < Objects.requireNonNull(sdf.parse(Objects.requireNonNull(Utils.dateToString(insurance.get(0).getFinal_effective_date())))).getTime()) {
                                    imInsuranceStatus.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                                } else {
                                    imInsuranceStatus.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                                }
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    txtInsuranceFinalEffectiveDate.setText(Utils.dateToString(insurance.get(0).getFinal_effective_date()));


                    layerMaintenanceItemVehicle.setVisibility(View.VISIBLE);
                    nextVehicleMaintenanceList.setAdapter(adapterNextMaintenance);
                    nextVehicleMaintenanceList.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    layerMaintenanceItemVehicle.setVisibility(View.GONE);
                    layerInsuranceVehicle.setVisibility(View.GONE);
                }
                adapterNextMaintenance.notifyDataSetChanged();

                // Insurance - layerInsuranceVehicle
                HomeInsuranceListAdapter adapterInsurance = new HomeInsuranceListAdapter(Database.mInsuranceDao.findReminderInsurance("V", g.getIdVehicle() ), getContext(),0);
                if ( adapterInsurance.getItemCount() > 0){
                    layerInsuranceVehicle.setVisibility(View.VISIBLE);
                    insuranceList.setAdapter(adapterInsurance);
                    insuranceList.setLayoutManager(new LinearLayoutManager(getContext()));
                }else {
                    layerInsuranceVehicle.setVisibility(View.GONE);
                }
                adapterInsurance.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vehicle[0] = null;
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void addDataSeries() {
        List<VehicleStatistics> vehicleStatisticsList = Database.mVehicleStatisticsDao.findTotalVehicleStatistics(g.getIdVehicle());
        String text = requireContext().getString(R.string.general) + ": " + numberFormat.format(vehicleStatisticsList.get(0).getAvg_consumption()) + " " + g.getMeasureConsumption();
        tvAVGType9.setText(text);
        tvAVGType9.setTextColor(VehicleStatistics.getSupply_reason_type_color(vehicleStatisticsList.get(0).getSupply_reason_type()));

        String[] reasonTypeArray;
        reasonTypeArray = getResources().getStringArray(R.array.supply_reason_type_array);
        for (int type=1; type<=reasonTypeArray.length; type++) {
            List<VehicleGraphStatistics> graphHelper = Database.mVehicleGraphStatisticsDao.findLastVehicleGraphStatistics(g.getIdVehicle(), type);
            VehicleStatistics vehicleStatistics = Database.mVehicleStatisticsDao.findLastVehicleStatistics(g.getIdVehicle(), type);
            text = reasonTypeArray[type - 1] + ": " + numberFormat.format(vehicleStatistics.getAvg_consumption()) + " " + g.getMeasureConsumption();
            switch (type) {
                case 1:
                    tvAVGType1.setText(text);
                    tvAVGType1.setTextColor(VehicleStatistics.getSupply_reason_type_color(vehicleStatistics.getSupply_reason_type()));
                case 2:
                    tvAVGType2.setText(text);
                    tvAVGType2.setTextColor(VehicleStatistics.getSupply_reason_type_color(vehicleStatistics.getSupply_reason_type()));
                case 3:
                    tvAVGType3.setText(text);
                    tvAVGType3.setTextColor(VehicleStatistics.getSupply_reason_type_color(vehicleStatistics.getSupply_reason_type()));
            }
            DataPoint[] dataSeries = new DataPoint[graphHelper.size()];
            DataPoint[] dataSeriesAVG = new DataPoint[graphHelper.size()];
            DataPoint[] dataSeriesAVGGeneral = new DataPoint[graphHelper.size()];
            if (graphHelper.size()>0) {
                for (int x = 0;x<graphHelper.size(); x++) {
                    dataSeries[x] = new DataPoint(graphHelper.get(x).getXaxis_date(), graphHelper.get(x).getYaxis_avg_consumption());
                    dataSeriesAVG[x] = new DataPoint(graphHelper.get(x).getXaxis_date(), vehicleStatistics.getAvg_consumption());
                    dataSeriesAVGGeneral[x] = new DataPoint(graphHelper.get(x).getXaxis_date(), vehicleStatisticsList.get(0).getAvg_consumption());
                }
                if (dataSeries.length > tamHorizontalLabels)
                    tamHorizontalLabels = dataSeries.length;
                if (tamHorizontalLabels > 10) tamHorizontalLabels = 10;
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataSeries);
                LineGraphSeries<DataPoint> seriesAVG = new LineGraphSeries<>(dataSeriesAVG);
                LineGraphSeries<DataPoint> seriesAVGGeneral = new LineGraphSeries<>(dataSeriesAVGGeneral);
                Paint paintAVG = new Paint();

                series.setColor(VehicleStatistics.getSupply_reason_type_color(type));
                series.setDrawDataPoints(true);
                series.setDataPointsRadius(7);
                vMinX = vMinX==0?series.getLowestValueX():Math.min(series.getLowestValueX(), vMinX);
                vMaxX = vMaxX==0?series.getHighestValueX():Math.max(series.getHighestValueX(), vMaxX);
                vMinY = vMinY==0?series.getLowestValueY():Math.min(series.getLowestValueY(), vMinY);
                vMaxY = vMaxY==0?series.getHighestValueY():Math.max(series.getHighestValueY(), vMaxY);
                graphStatistics.addSeries(series);

                paintAVG.setStyle(Paint.Style.STROKE);
                paintAVG.setStrokeWidth(4);
                paintAVG.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
                paintAVG.setColor(VehicleStatistics.getSupply_reason_type_color(type));
                seriesAVG.setDrawAsPath(true);
                seriesAVG.setCustomPaint(paintAVG);
                seriesAVG.setDrawDataPoints(false);
                graphStatistics.addSeries(seriesAVG);

                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(4);
                paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
                paint.setColor(VehicleStatistics.getSupply_reason_type_color(9));
                seriesAVGGeneral.setDrawAsPath(true);
                seriesAVGGeneral.setCustomPaint(paint);
                seriesAVGGeneral.setDrawDataPoints(false);
                graphStatistics.addSeries(seriesAVGGeneral);
            }
        }
    }

    @Override
    public void onClick(View view) {}
}