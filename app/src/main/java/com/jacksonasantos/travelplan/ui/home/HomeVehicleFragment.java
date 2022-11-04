package com.jacksonasantos.travelplan.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.FuelSupply;
import com.jacksonasantos.travelplan.dao.Insurance;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.VehicleStatistics;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.general.InsuranceDialog;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;
import com.jacksonasantos.travelplan.ui.vehicle.FuelSupplyActivity;
import com.jacksonasantos.travelplan.ui.vehicle.PendingVehicleActivity;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class HomeVehicleFragment extends Fragment implements View.OnClickListener {

    private ScrollView layerHomeVehicle;
    private CardView layerMessage;
    private RecyclerView listMessage;

    private Spinner spVehicle;
    private TextView tvLicencePlate;
    private ImageView imVehicleType;

    private ConstraintLayout layerFuelSupply;
    private TextView tvFuelSupplyDate;
    private TextView tvFuelSupplyLastOdometer;
    private TextView tvFuelSupplyNumberLiters;
    private TextView tvFuelSupplyValue;

    private ConstraintLayout layerInsuranceVehicle;
    private ImageView imInsuranceType;
    private TextView txtInsuranceFinalEffectiveDate;

    private ConstraintLayout layerStatisticsVehicle;
    private TextView tvAVGType9;
    private TextView tvAVGType1;
    private TextView tvAVGType2;
    private TextView tvAVGType3;
    private GraphView graphStatistics;

    private ConstraintLayout layerPendingVehicle;
    private ImageView imgAddPendingVehicle;
    private RecyclerView pendingVehicleList;

    private ConstraintLayout layerMaintenanceItemVehicle;
    private RecyclerView nextVehicleMaintenanceList;

    int tamHorizontalLabels = 3;
    int tamVerticalLabels = 3;
    double vMinX = 0;
    double vMaxX = 0;
    double vMinY = 0;
    double vMaxY = 0;

    final Globals g = Globals.getInstance();

    final Locale locale = new Locale(g.getLanguage(), g.getCountry());
    final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
    final NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_home_vehicle, container, false);
        layerHomeVehicle = v.findViewById((R.id.layerHomeVehicle));

        layerMessage = v.findViewById((R.id.layerMessage));
        listMessage = v.findViewById((R.id.listMessage));

        imVehicleType = v.findViewById(R.id.imVehicleType);
        spVehicle =v.findViewById(R.id.spVehicle);
        tvLicencePlate = v.findViewById(R.id.tvLicencePlate);

        layerFuelSupply = v.findViewById(R.id.layerFuelSupply);
        tvFuelSupplyDate = v.findViewById(R.id.tvFuelSupplyDate);
        tvFuelSupplyLastOdometer = v.findViewById(R.id.tvFuelSupplyLastOdometer);
        tvFuelSupplyNumberLiters = v.findViewById(R.id.tvFuelSupplyNumberLiters);
        tvFuelSupplyValue = v.findViewById(R.id.tvFuelSupplyValue);

        layerInsuranceVehicle = v.findViewById(R.id.layerInsuranceVehicle);
        imInsuranceType = v.findViewById(R.id.imInsuranceType);
        txtInsuranceFinalEffectiveDate = v.findViewById(R.id.txtInsuranceFinalEffectiveDate);

        layerStatisticsVehicle = v.findViewById(R.id.layerStatisticsVehicle);
        tvAVGType9 = v.findViewById(R.id.tvAVGType9);
        tvAVGType1 = v.findViewById(R.id.tvAVGType1);
        tvAVGType2 = v.findViewById(R.id.tvAVGType2);
        tvAVGType3 = v.findViewById(R.id.tvAVGType3);
        graphStatistics = v.findViewById(R.id.graphStatistics);

        layerPendingVehicle = v.findViewById(R.id.layerPendingVehicle);
        imgAddPendingVehicle = v.findViewById(R.id.imgAddPendingVehicle);
        pendingVehicleList = v.findViewById(R.id.pendingVehicleList);

        layerMaintenanceItemVehicle = v.findViewById(R.id.layerMaintenanceItemVehicle);
        nextVehicleMaintenanceList = v.findViewById(R.id.listNextVehicleMaintenance);

        layerFuelSupply.setOnClickListener(v1 -> {
            Intent intent = new Intent (v1.getContext(), FuelSupplyActivity.class);
            intent.putExtra("vehicle_id", g.getIdVehicle());
            v1.getContext().startActivity(intent);
        });

        layerHomeVehicle.setFocusable(false);
        layerHomeVehicle.setFocusableInTouchMode(true);
        layerHomeVehicle.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        final Database mDb = new Database(getActivity());
        mDb.open();

        HomeMessageListAdapter adapterMessage = new HomeMessageListAdapter(Database.mVehicleStatisticsDao.findMessages(requireContext()), getContext());
        if (adapterMessage.getItemCount() > 0) {
            layerMessage.setVisibility(View.VISIBLE);
            listMessage.setAdapter(adapterMessage);
            listMessage.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            layerMessage.setVisibility(View.GONE);
        }

        final List<Vehicle> vehicles =  Database.mVehicleDao.fetchArrayVehicles();
        ArrayAdapter<Vehicle> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, vehicles);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spVehicle.setAdapter(adapter);

        if (vehicles.size()>0) {
            for (int x = 0; x <= vehicles.size(); x++) {
                if (vehicles.get(x).getId().equals(g.getIdVehicle())) {
                    spVehicle.setSelection(x);
                    break;
                }
                else
                {
                    spVehicle.setSelection(x);
                }
            }
        }
        final Vehicle[] vehicle = {new Vehicle()};
        spVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint({"SetTextI18n", "SimpleDateFormat", "NotifyDataSetChanged"})
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {

                // Retrieves data from the vehicle selected in Spinner - layerVehicle
                vehicle[0] = (Vehicle) parent.getItemAtPosition(position);
                tvLicencePlate.setText(vehicle[0].getLicense_plate());
                byte[] imgArray = vehicle[0].getImage();
                if (imgArray!=null){
                    Bitmap raw = BitmapFactory.decodeByteArray(imgArray, 0, imgArray.length);
                    imVehicleType.setImageBitmap(raw);
                } else {
                    imVehicleType.setImageResource(vehicle[0].getVehicleTypeImage(vehicle[0].getVehicle_type()));
                }

                g.setIdVehicle(vehicle[0].getId());

                // Last Fuel Supply of Vehicle in Global selection - layerFuelSupply
                FuelSupply fuelSupply = Database.mFuelSupplyDao.findLastFuelSupply( g.getIdVehicle() );
                tvFuelSupplyDate.setText(Utils.dateToString(fuelSupply.getSupply_date()));
                tvFuelSupplyLastOdometer.setText(numberFormatter.format(fuelSupply.getVehicle_odometer()));
                tvFuelSupplyNumberLiters.setText(fuelSupply.getNumber_liters()==0? "0 "+g.getMeasureCapacity() :fuelSupply.getNumber_liters() +" "+g.getMeasureCapacity());
                tvFuelSupplyValue.setText(currencyFormatter.format(fuelSupply.getSupply_value()==0?BigDecimal.ZERO:fuelSupply.getSupply_value()));

                // Insurance - layerInsuranceVehicle
                List<Insurance> insurance = Database.mInsuranceDao.findReminderInsurance("V", g.getIdVehicle() );
                if (!insurance.isEmpty()) {
                    layerInsuranceVehicle.setVisibility(View.VISIBLE);
                    imInsuranceType.setImageResource(insurance.get(0).getInsurance_typeImage(insurance.get(0).getInsurance_type()));
                    imInsuranceType.setColorFilter(insurance.get(0).getColorInsuranceStatus(), PorterDuff.Mode.MULTIPLY);
                    txtInsuranceFinalEffectiveDate.setText(Utils.dateToString(insurance.get(0).getFinal_effective_date()));

                    layerInsuranceVehicle.setOnClickListener(v -> {
                        Insurance x = InsuranceDialog.InsuranceClass(insurance.get(0), v);
                        imInsuranceType.setColorFilter(x.getColorInsuranceStatus(), PorterDuff.Mode.MULTIPLY);
                    });
                } else {
                    layerInsuranceVehicle.setVisibility(View.INVISIBLE);
                }

                // Statistics of Vehicle in Global selection - layerStatisticsVehicle
                HomeVehicleStatisticsListAdapter adapterLastVehicleStatistics = new HomeVehicleStatisticsListAdapter(Database.mVehicleStatisticsDao.findVehicleFuelingStatistics(g.getIdVehicle()), getContext());
                if (adapterLastVehicleStatistics.getItemCount() > 0 ) {
                    layerStatisticsVehicle.setVisibility(View.VISIBLE);

                    // Graph Statistics Vehicle - https://github.com/jjoe64/GraphView
                    vMinX = 0; vMaxX = 0; vMinY = 0; vMaxY = 0;
                    tamHorizontalLabels = 3;
                    tamVerticalLabels = 5;
                    graphStatistics.clearSecondScale();
                    graphStatistics.removeAllSeries();                              // Clear the Graph
                    graphStatistics.onDataChanged(true, false);
                    graphStatistics.getGridLabelRenderer().resetStyles();

                    addDataSeries();

                    graphStatistics.getViewport().setScalable(true);                // activate horizontal zooming and scrolling
                    graphStatistics.getViewport().setScrollable(true);              // activate horizontal scrolling
                    graphStatistics.getViewport().setScalableY(true);               // activate horizontal and vertical zooming and scrolling
                    graphStatistics.getViewport().setScrollableY(true);             // activate vertical scrolling
                    graphStatistics.getViewport().setXAxisBoundsManual(true);
                    graphStatistics.getViewport().setMinX(vMinX);
                    graphStatistics.getViewport().setMaxX(vMaxX);
                    graphStatistics.getViewport().setYAxisBoundsManual(true);
                    graphStatistics.getViewport().setMinY(vMinY);
                    graphStatistics.getViewport().setMaxY(vMaxY);

                    graphStatistics.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
                    graphStatistics.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);
                    graphStatistics.getGridLabelRenderer().setTextSize(20);
                    graphStatistics.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
                    graphStatistics.getGridLabelRenderer().setHumanRounding(false,false);
                    graphStatistics.getGridLabelRenderer().setNumHorizontalLabels(tamHorizontalLabels);
                    graphStatistics.getGridLabelRenderer().setNumVerticalLabels(tamVerticalLabels);
                    graphStatistics.getGridLabelRenderer().setVerticalAxisTitleTextSize(24);
                    graphStatistics.getGridLabelRenderer().setVerticalAxisTitleColor(Color.DKGRAY);
                    graphStatistics.getGridLabelRenderer().setVerticalAxisTitle(g.getMeasureConsumption());
                    graphStatistics.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graphStatistics.getContext(), new SimpleDateFormat("MM/yy")));

                    graphStatistics.refreshDrawableState();
                } else {
                    layerStatisticsVehicle.setVisibility(View.GONE);
                }
                adapterLastVehicleStatistics.notifyDataSetChanged();

                 // Pending Vehicle - layerPendingVehicle
                HomeVehiclePendingVehicleListAdapter adapterPendingVehicle = new HomeVehiclePendingVehicleListAdapter(Database.mPendingVehicleDao.fetchAllPendingVehicle( g.getIdVehicle(), 0 ), getContext(),0);
                layerPendingVehicle.setVisibility(View.VISIBLE);
                pendingVehicleList.setAdapter(adapterPendingVehicle);
                pendingVehicleList.setLayoutManager(new LinearLayoutManager(getContext()));

                imgAddPendingVehicle.setOnClickListener (v -> {
                    Intent intent = new Intent(v.getContext(), PendingVehicleActivity.class);
                    intent.putExtra("vehicle_id", g.getIdVehicle());
                    startActivity(intent);
                });
                adapterPendingVehicle.notifyDataSetChanged();

                // Next Vehicle Maintenance - layerMaintenanceItemVehicle
                HomeVehicleNextMaintenanceListAdapter adapterNextMaintenance = new HomeVehicleNextMaintenanceListAdapter(Database.mNextMaintenanceItemDao.findNextMaintenanceItem( g.getIdVehicle() ), getContext(),0);
                if (adapterNextMaintenance.getItemCount() > 0) {
                    layerMaintenanceItemVehicle.setVisibility(View.VISIBLE);
                    nextVehicleMaintenanceList.setAdapter(adapterNextMaintenance);
                    nextVehicleMaintenanceList.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    layerMaintenanceItemVehicle.setVisibility(View.GONE);
                }
                adapterNextMaintenance.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { vehicle[0] = null; }
        });
        adapter.notifyDataSetChanged();
    }

    private void addDataSeries() {
        tvAVGType1.setText("");
        tvAVGType2.setText("");
        tvAVGType3.setText("");
        tvAVGType9.setText("");
        List<VehicleStatistics> vehicleStatisticsAVGGeneral = Database.mVehicleStatisticsDao.findTotalFuelingVehicleStatistics(g.getIdVehicle());
        String text = requireContext().getString(R.string.general) + ": " + numberFormatter.format(vehicleStatisticsAVGGeneral.get(0).getAvg_consumption()) + " " + g.getMeasureConsumption();
        tvAVGType9.setText(text);
        tvAVGType9.setTextColor(VehicleStatistics.getSupply_reason_type_color(vehicleStatisticsAVGGeneral.get(0).getSupply_reason_type()));

        String[] reasonTypeArray = getResources().getStringArray(R.array.supply_reason_type_array);
        for (int type=1; type<=reasonTypeArray.length; type++) {
            List<VehicleStatistics> vehicleStatisticsGraph = Database.mVehicleStatisticsDao.findVehicleFuelingGraphStatistics(g.getIdVehicle(), type);
            if (vehicleStatisticsGraph.size()>0) {
                VehicleStatistics vehicleStatisticsAVG = Database.mVehicleStatisticsDao.findVehicleFuelingStatistics(g.getIdVehicle(), type);
                text = reasonTypeArray[type - 1] + ": " + numberFormatter.format(vehicleStatisticsAVG.getAvg_consumption()) + " " + g.getMeasureConsumption();
                switch (type) {
                    case 1:
                        tvAVGType1.setText(text);
                        tvAVGType1.setTextColor(VehicleStatistics.getSupply_reason_type_color(vehicleStatisticsAVG.getSupply_reason_type()));
                        break;
                    case 2:
                        tvAVGType2.setText(text);
                        tvAVGType2.setTextColor(VehicleStatistics.getSupply_reason_type_color(vehicleStatisticsAVG.getSupply_reason_type()));
                        break;
                    case 3:
                        tvAVGType3.setText(text);
                        tvAVGType3.setTextColor(VehicleStatistics.getSupply_reason_type_color(vehicleStatisticsAVG.getSupply_reason_type()));
                        break;
                }
                DataPoint[] dataSeries = new DataPoint[vehicleStatisticsGraph.size()];
                DataPoint[] dataSeriesAVG = new DataPoint[vehicleStatisticsGraph.size()];
                DataPoint[] dataSeriesAVGGeneral = new DataPoint[vehicleStatisticsGraph.size()];
                for (int x = 0;x<vehicleStatisticsGraph.size(); x++) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(vehicleStatisticsGraph.get(x).getStatistic_date());
                    int month = cal.get(Calendar.MONTH);
                    int year = cal.get(Calendar.YEAR);
                    Calendar c = new GregorianCalendar(year, month,1,0,0,0);

                    Date d = c.getTime();
                    dataSeries[x]           = new DataPoint(d.getTime(), vehicleStatisticsGraph.get(x).getAvg_consumption());
                    dataSeriesAVG[x]        = new DataPoint(d.getTime(), vehicleStatisticsAVG.getAvg_consumption());
                    dataSeriesAVGGeneral[x] = new DataPoint(d.getTime(), vehicleStatisticsAVGGeneral.get(0).getAvg_consumption());
                }
                if (dataSeries.length > tamHorizontalLabels) tamHorizontalLabels = dataSeries.length+1;
                if (tamHorizontalLabels > 10) tamHorizontalLabels = 10;
                if (dataSeries.length > tamVerticalLabels) tamVerticalLabels = dataSeries.length+1;
                if (tamVerticalLabels > 10) tamVerticalLabels = 10;
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataSeries);
                LineGraphSeries<DataPoint> seriesAVG = new LineGraphSeries<>(dataSeriesAVG);
                LineGraphSeries<DataPoint> seriesAVGGeneral = new LineGraphSeries<>(dataSeriesAVGGeneral);

                series.setColor(VehicleStatistics.getSupply_reason_type_color(type));
                series.setDrawDataPoints(true);
                series.setDataPointsRadius(7);
                vMinX = (vMinX==0?series.getLowestValueX():Math.min(series.getLowestValueX(), vMinX));
                vMaxX = (vMaxX==0?series.getHighestValueX():Math.max(series.getHighestValueX(), vMaxX));
                vMinY = (vMinY==0?series.getLowestValueY():Math.min(series.getLowestValueY(), vMinY));
                vMaxY = (vMaxY==0?series.getHighestValueY():Math.max(series.getHighestValueY(), vMaxY));
                graphStatistics.addSeries(series);

                Paint paintAVG = new Paint();
                paintAVG.setStyle(Paint.Style.STROKE);
                paintAVG.setStrokeWidth(4);
                paintAVG.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
                paintAVG.setColor(VehicleStatistics.getSupply_reason_type_color(type));
                seriesAVG.setDrawAsPath(true);
                seriesAVG.setCustomPaint(paintAVG);
                seriesAVG.setDrawDataPoints(false);
                graphStatistics.addSeries(seriesAVG);

                Paint paint = new Paint();
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
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