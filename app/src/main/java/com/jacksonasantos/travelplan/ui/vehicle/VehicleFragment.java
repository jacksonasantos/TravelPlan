package com.jacksonasantos.travelplan.ui.vehicle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.VehicleStatisticsPeriod;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class VehicleFragment extends Fragment  {

    final Globals g = Globals.getInstance();

    private RecyclerView listVehicles;
    private GraphView graphStatistics;
    private VehicleListAdapter adapterVehicle;

    int tamHorizontalLabels = 3;
    int tamVerticalLabels = 3;
    double vMinX = 0;
    double vMaxX = 0;
    double vMaxY = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        adapterVehicle = new VehicleListAdapter(Database.mVehicleDao.fetchAllVehicles(), getContext());

        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_generic_list_graphic, container, false);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onResume() {
        super.onResume();
        Database mDb = new Database(getContext());
        mDb.open();

        listVehicles = this.requireView().findViewById(R.id.list);
        graphStatistics = this.requireView().findViewById(R.id.graph);

        // Graph Statistics Vehicle - https://github.com/jjoe64/GraphView

        vMinX = 0; vMaxX = 0; vMaxY = 0;
        tamHorizontalLabels = 3;
        tamVerticalLabels = 5;
        graphStatistics.clearSecondScale();
        graphStatistics.removeAllSeries();                              // Clear the Graph
        graphStatistics.onDataChanged(true, false);
        graphStatistics.getGridLabelRenderer().resetStyles();

        addDataSeries();

        graphStatistics.getViewport().setXAxisBoundsManual(true);
        graphStatistics.getViewport().setMinX(vMinX);
        graphStatistics.getViewport().setMaxX(vMaxX);
        graphStatistics.getViewport().setYAxisBoundsManual(true);
        graphStatistics.getViewport().setMaxXAxisSize(vMaxX);
        graphStatistics.getViewport().setMinY(0);
        graphStatistics.getViewport().setMaxY(vMaxY+2);

        graphStatistics.getGridLabelRenderer().setHorizontalLabelsAngle(135);
        graphStatistics.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
        graphStatistics.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);
        graphStatistics.getGridLabelRenderer().setTextSize(20);
        graphStatistics.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        graphStatistics.getGridLabelRenderer().setNumHorizontalLabels(tamHorizontalLabels);
        graphStatistics.getGridLabelRenderer().setNumVerticalLabels(tamVerticalLabels);
        graphStatistics.getGridLabelRenderer().setVerticalAxisTitleTextSize(24);
        graphStatistics.getGridLabelRenderer().setVerticalAxisTitleColor(Color.DKGRAY);
        graphStatistics.getGridLabelRenderer().setVerticalAxisTitle(g.getMeasureConsumption());
        graphStatistics.getGridLabelRenderer().setLabelFormatter( new DateAsXAxisLabelFormatter(graphStatistics.getContext(), new SimpleDateFormat("yy/MM")));
        graphStatistics.getGridLabelRenderer().setHumanRounding(true);

        graphStatistics.refreshDrawableState();

        listVehicles.setAdapter(adapterVehicle);
        listVehicles.setLayoutManager(new LinearLayoutManager(getContext()));
        listVehicles.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        adapterVehicle.notifyDataSetChanged();
    }

    private Menu mMenu;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        this.mMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem m1 = menu.findItem(R.id.addmenu);
        MenuItem m2 = menu.findItem(R.id.savemenu);
        MenuItem m3 = menu.findItem(R.id.filtermenu);
        m1.setVisible(true);
        m2.setVisible(false);
        m3.setVisible(true);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.addmenu:
                intent = new Intent( getContext(), VehicleActivity.class );
                startActivity( intent );
                return super.onOptionsItemSelected(item);

            case R.id.filtermenu:
                Globals.getInstance().setFilterVehicle(!Globals.getInstance().getFilterVehicle());
                if ( Globals.getInstance().getFilterVehicle() ) {
                    this.mMenu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_button_filter));
                } else {
                    this.mMenu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_button_filter_no));
                }

                VehicleListAdapter adapterVehicle = new VehicleListAdapter(Database.mVehicleDao.fetchAllVehicles(), getContext());
                listVehicles.setAdapter(adapterVehicle);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addDataSeries() {

        for (int v=0; v<adapterVehicle.getItemCount(); v++) {
            List<VehicleStatisticsPeriod> vehicleStatisticsPeriodGraph = Database.mVehicleStatisticsPeriodDao.findVehicleStatisticsPeriod(adapterVehicle.mVehicle.get(v).getId());
            if (vehicleStatisticsPeriodGraph.size()>0) {
                DataPoint[] dataSeries = new DataPoint[vehicleStatisticsPeriodGraph.size()];
                for (int x = 0; x<vehicleStatisticsPeriodGraph.size(); x++) {
                    Calendar c = new GregorianCalendar(Integer.parseInt(vehicleStatisticsPeriodGraph.get(x).getStatistic_period_year()),
                            Integer.parseInt(vehicleStatisticsPeriodGraph.get(x).getStatistic_period_month())-1,      // Jan = 0, Dec = 11
                            1,//Calendar.DAY_OF_MONTH,
                            0,
                            0,
                            0);

                    Date d = c.getTime();
                    dataSeries[x] = new DataPoint(d.getTime(), vehicleStatisticsPeriodGraph.get(x).getAvg_consumption());
                    vMinX = (vMinX==0?d.getTime():Math.min( vMinX, d.getTime()));
                    vMaxX = (vMaxX==0?d.getTime():Math.max( vMaxX, d.getTime()));
                    vMaxY = (vMaxY==0?vehicleStatisticsPeriodGraph.get(x).getAvg_consumption():Math.max(vMaxY, vehicleStatisticsPeriodGraph.get(x).getAvg_consumption()));
                }
                if (dataSeries.length > tamHorizontalLabels) tamHorizontalLabels = dataSeries.length;
                if (dataSeries.length > tamVerticalLabels) tamVerticalLabels = dataSeries.length;
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataSeries);

                series.setColor(adapterVehicle.mVehicle.get(v).getColor_code());
                series.setDrawDataPoints(true);
                series.setDataPointsRadius(7);
                graphStatistics.addSeries(series);
            }
        }
    }
}
