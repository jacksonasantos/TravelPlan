package com.jacksonasantos.travelplan.ui.home;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HomeTravelFragment extends Fragment implements View.OnClickListener {

    private ConstraintLayout layerTravel;
    private ImageView imTravelStatus;
    private Spinner spTravel;
    private TextView tvNote;
    private TextView tvDeparture;
    private TextView tvReturn;
    private TextView tvDays;

    private ConstraintLayout layerExpense;
    private ConstraintLayout labelTravelExpenses;
    private RecyclerView listTravelExpenses;
    private ConstraintLayout totalTravelExpenses;

    private ConstraintLayout layerInsurance;
    private RecyclerView listInsuranceExpiration;

    Globals g = Globals.getInstance();
    Locale locale = new Locale(g.getLanguage(), g.getCountry());
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_home_travel, container, false);
        layerTravel = v.findViewById(R.id.layerTravel);
        spTravel = v.findViewById(R.id.spTravel);
        imTravelStatus = v.findViewById(R.id.imTravelStatus);
        tvNote = v.findViewById(R.id.tvNote);
        tvDeparture = v.findViewById(R.id.tvDeparture);
        tvReturn = v.findViewById(R.id.tvReturn);
        tvDays = v.findViewById(R.id.tvDays);

        layerExpense = v.findViewById(R.id.layerExpense);
        labelTravelExpenses = v.findViewById(R.id.labelTravelExpenses);
        listTravelExpenses = v.findViewById(R.id.listTravelExpenses);
        totalTravelExpenses = v.findViewById(R.id.totalTravelExpenses);

        layerInsurance = v.findViewById(R.id.layerInsurance);
        listInsuranceExpiration = v.findViewById(R.id.listInsuranceExpiration);

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();

        final List<Travel> travels =  Database.mTravelDao.fetchArrayTravel();
        ArrayAdapter<Travel> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, travels);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spTravel.setAdapter(adapter);

        final Travel[] travel = {new Travel()};
        spTravel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Recupera dados da viagem selecionada no Spinner - layerTravel
                travel[0] = (Travel) parent.getItemAtPosition(position);
                imTravelStatus.setImageResource(R.drawable.ic_ball );
                imTravelStatus.setColorFilter(travel[0].getColorStatus(), PorterDuff.Mode.MULTIPLY);
                tvNote.setText(travel[0].getNote());
                tvDeparture.setText(Utils.dateToString(travel[0].getDeparture_date()));
                tvReturn.setText(Utils.dateToString(travel[0].getReturn_date()));
                long d1 = travel[0].getDeparture_date().getTime();
                long d2 = travel[0].getReturn_date().getTime();
                int dias = (int) ((d2 - d1) / (24*60*60*1000)); // calcula em dias
                tvDays.setText((dias + 1) + " "+getString(R.string.days));

                // Expenses - LayerExpense
                HomeTravelExpenseListAdapter adapterTravelExpense = new HomeTravelExpenseListAdapter( Database.mTravelExpenseDao.findTravelExpense(travel[0].getId() ), getContext());
                if ( adapterTravelExpense.getItemCount() > 0){
                    layerExpense.setVisibility(View.VISIBLE);

                    View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home_travel_item_expense, parent, false);
                    TextView lblExpense = v.findViewById(R.id.txtExpense);
                    TextView lblExpectedValue = v.findViewById(R.id.txtExpectedValue);
                    TextView lblRealizedValue = v.findViewById(R.id.txtRealizedValue);
                    lblExpense.setText("");
                    lblExpectedValue.setText(getString(R.string.Expected));
                    lblRealizedValue.setText(getString(R.string.Realized));
                    labelTravelExpenses.addView(v);
                    labelTravelExpenses.setBackgroundColor(Color.rgb(209,193,233));

                    listTravelExpenses.setAdapter(adapterTravelExpense);
                    listTravelExpenses.setLayoutManager(new LinearLayoutManager(getContext()));

                    double vExpected = 0;
                    double vRealized = 0;
                    for (int x=1; x < adapterTravelExpense.getItemCount(); x++) {
                        vExpected = vExpected + adapterTravelExpense.mTravelExpense.get(x).getExpected_value();
                        vRealized = vRealized + adapterTravelExpense.mTravelExpense.get(x).getRealized_value();
                    }
                    v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home_travel_item_expense, parent, false);
                    totalTravelExpenses.removeAllViews();
                    TextView totExpense = v.findViewById(R.id.txtExpense);
                    TextView totExpectedValue = v.findViewById(R.id.txtExpectedValue);
                    TextView totRealizedValue = v.findViewById(R.id.txtRealizedValue);
                    totExpense.setText("      TOTAL");
                    totExpectedValue.setText(currencyFormatter.format(vExpected));
                    totRealizedValue.setText(currencyFormatter.format(vRealized));
                    totalTravelExpenses.addView(v);
                    totalTravelExpenses.setBackgroundColor(Color.rgb(209,193,233));
                } else {
                    layerExpense.setVisibility(View.GONE);
                }

                // Insurance - layerInsurance
                HomeInsuranceListAdapter adapterInsurance = new HomeInsuranceListAdapter(Database.mInsuranceDao.findReminderInsurance("T", travel[0].getId() ), getContext());
                if ( adapterInsurance.getItemCount() > 0){
                    layerInsurance.setVisibility(View.VISIBLE);
                    listInsuranceExpiration.setAdapter(adapterInsurance);
                    listInsuranceExpiration.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    layerInsurance.setVisibility(View.GONE);
                }

                adapterInsurance.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                travel[0] = null;
            }
        });
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {}
}