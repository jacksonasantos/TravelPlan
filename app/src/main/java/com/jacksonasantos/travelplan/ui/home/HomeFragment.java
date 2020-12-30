package com.jacksonasantos.travelplan.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.FuelSupply;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.VehicleGraphStatistics;
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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private Spinner spVehicle;
    private TextView tvLicencePlate;
    private ImageView imVehicleType;

    private TextView tvFuelSupplyDate;
    private TextView tvFuelSupplyLastOdometer;
    private TextView tvFuelSupplyNumberLiters;
    private TextView tvFuelSupplyValue;

    private RecyclerView vehicleStatisticsList;
    private RecyclerView vehicleStatisticsTotalList;
    private GraphView graphStatistics;
    private RecyclerView maintenanceList;
    private RecyclerView insuranceList;

    DataPoint[] dataSeries;
    int tamHorizontalLabels = 4;
    double x_px_min;
    double x_px_max;

    Globals g = Globals.getInstance();

    Locale locale = new Locale(g.getLanguage(), g.getCountry());
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_home, container, false);
        imVehicleType = v.findViewById(R.id.imVehicleType);
        spVehicle =v.findViewById(R.id.spVehicle);
        tvLicencePlate = v.findViewById(R.id.tvLicencePlate);
        ImageButton btnRefuel = v.findViewById(R.id.btnRefuel);
        tvFuelSupplyDate = v.findViewById(R.id.tvFuelSupplyDate);
        tvFuelSupplyLastOdometer = v.findViewById(R.id.tvFuelSupplyLastOdometer);
        tvFuelSupplyNumberLiters = v.findViewById(R.id.tvFuelSupplyNumberLiters);
        tvFuelSupplyValue = v.findViewById(R.id.tvFuelSupplyValue);
        vehicleStatisticsList = v.findViewById(R.id.listVehicleStatistics);
        vehicleStatisticsTotalList = v.findViewById(R.id.listVehicleStatisticsTotal);
        graphStatistics = v.findViewById(R.id.graphStatistics);
        maintenanceList = v.findViewById(R.id.listInVehicleService);
        insuranceList = v.findViewById(R.id.listInsuranceExpiration);

        btnRefuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), FuelSupplyActivity.class);
                intent.putExtra("vehicle_id", g.getIdVehicle());
                v.getContext().startActivity(intent);
            }
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

                // Recupera dados do veículo selecionado no Spinner
                vehicle[0] = (Vehicle) parent.getItemAtPosition(position);
                tvLicencePlate.setText(vehicle[0].getLicense_plate());
                imVehicleType.setImageResource(vehicle[0].getVehicleTypeImage(vehicle[0].getVehicle_type()));

                g.setIdVehicle(vehicle[0].getId());

                // Last Fuel Supply of Vehicle in Global selection
                FuelSupply fuelSupply = Database.mFuelSupplyDao.findLastFuelSupply( g.getIdVehicle() );
                tvFuelSupplyDate.setText(Utils.dateToString(fuelSupply.getSupply_date()));
                tvFuelSupplyLastOdometer.setText(getString(R.string.Odometer)+ ": "+fuelSupply.getVehicle_odometer());
                tvFuelSupplyNumberLiters.setText(fuelSupply.getNumber_liters()==null? "0 "+g.getMeasureCapacity() :fuelSupply.getNumber_liters() +" "+g.getMeasureCapacity());
                tvFuelSupplyValue.setText(currencyFormatter.format(fuelSupply.getSupply_value()==null?BigDecimal.ZERO:fuelSupply.getSupply_value()));

                // Statistics of Vehicle in Global selection
                HomeStatisticsListAdapter adapterVehicle = new HomeStatisticsListAdapter(Database.mVehicleStatisticsDao.findLastVehicleStatistics(g.getIdVehicle()), getContext());
                vehicleStatisticsList.setAdapter(adapterVehicle);
                vehicleStatisticsList.setLayoutManager(new LinearLayoutManager(getContext()));

                HomeStatisticsListAdapter adapterTotalVehicle = new HomeStatisticsListAdapter(Database.mVehicleStatisticsDao.findTotalVehicleStatistics(g.getIdVehicle()), getContext());
                vehicleStatisticsTotalList.setAdapter(adapterTotalVehicle);
                vehicleStatisticsTotalList.setLayoutManager(new LinearLayoutManager(getContext()));

                // Graph Statistics Vehicle - https://github.com/jjoe64/GraphView
                graphStatistics.removeAllSeries();                   // Clear the Graph
                graphStatistics.refreshDrawableState();

                // Series "Estrada"
                dataSeries = getData(2);
                if ( dataSeries.length > tamHorizontalLabels ) tamHorizontalLabels = dataSeries.length;
                LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(dataSeries);
                series1.setColor(Color.BLUE);
                series1.setDrawDataPoints(true);
                series1.setTitle(getString(R.string.road));
                graphStatistics.addSeries((series1));
                x_px_min = series1.getLowestValueX();
                x_px_max = series1.getHighestValueX();

                // Series "Cidade"
                dataSeries = getData(1);
                if ( dataSeries.length > tamHorizontalLabels ) tamHorizontalLabels = dataSeries.length;
                if ( tamHorizontalLabels > 10 ) tamHorizontalLabels = 10;
                LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(dataSeries);
                series2.setColor(Color.RED);
                series2.setDrawDataPoints(true);
                series2.setTitle(getString(R.string.city));
                graphStatistics.addSeries((series2));
                x_px_min = Math.min(series2.getLowestValueX(), x_px_min);
                x_px_max = Math.max(series2.getHighestValueX(), x_px_max);

                // Prepare e Show Legend
                //graphStatistics.getLegendRenderer().setVisible(true);
                //graphStatistics.getLegendRenderer().setTextSize(20);
                //graphStatistics.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

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

                graphStatistics.onDataChanged(true,true);
                graphStatistics.getViewport().setScalable(true);            //         activate horizontal zooming and scrolling
                graphStatistics.getViewport().setScrollable(true);          //         activate horizontal scrolling
                graphStatistics.getViewport().setScalableY(true);           //         activate horizontal and vertical zooming and scrolling
                graphStatistics.getViewport().setScrollableY(true);         //         activate vertical scrolling
                graphStatistics.getViewport().setXAxisBoundsManual(true);
                graphStatistics.getViewport().setMinX(x_px_min);
                graphStatistics.getViewport().setMaxX(x_px_max);

                // In-Vehicle Services
                HomeMaintenanceListAdapter adapterMaintenance = new HomeMaintenanceListAdapter(Database.mMaintenanceDao.findReminderMaintenance( g.getIdVehicle() ), getContext());
                maintenanceList.setAdapter(adapterMaintenance);
                maintenanceList.setLayoutManager(new LinearLayoutManager(getContext()));

                // Insurance
                HomeInsuranceListAdapter adapterInsurance = new HomeInsuranceListAdapter(Database.mInsuranceDao.findReminderInsurance( g.getIdVehicle() ), getContext());
                insuranceList.setAdapter(adapterInsurance);
                insuranceList.setLayoutManager(new LinearLayoutManager(getContext()));

                adapterVehicle.notifyDataSetChanged();
                adapterMaintenance.notifyDataSetChanged();
                adapterInsurance.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vehicle[0] = null;
            }
        });
        adapter.notifyDataSetChanged();
    }

    private DataPoint[] getData(int type) {
        List<VehicleGraphStatistics> graphHelper = Database.mVehicleGraphStatisticsDao.findLastVehicleGraphStatistics(g.getIdVehicle(), type);
        DataPoint[] dp = new DataPoint[graphHelper.size()];
        for(int i = 0;i<graphHelper.size(); i++){
            dp[i] = new DataPoint(graphHelper.get(i).getXaxis_date(), graphHelper.get(i).getYaxis_avg_consumption());
        }
        return dp;
    }

    @Override
    public void onClick(View view) {}
}