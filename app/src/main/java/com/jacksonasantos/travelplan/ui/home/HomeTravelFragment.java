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

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class HomeTravelFragment extends Fragment implements View.OnClickListener {

    private ConstraintLayout layerTravel;
    private ImageView imTravelStatus;
    private Spinner spTravel;
    private TextView tvNote;
    private TextView tvDeparture;
    private TextView tvReturn;
    private TextView tvDays;

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
                int vColor;
                switch (travel[0].getStatus()) {
                    case 0: vColor = Color.BLUE;
                    case 1: vColor = Color.YELLOW;
                    case 2: vColor = Color.GREEN;
                    case 3: vColor = Color.MAGENTA;
                    default: vColor = Color.GRAY;
                }
                imTravelStatus.setColorFilter(vColor, PorterDuff.Mode.MULTIPLY);
                tvNote.setText(travel[0].getNote());
                tvDeparture.setText(Utils.dateToString(travel[0].getDeparture_date()));
                tvReturn.setText(Utils.dateToString(travel[0].getReturn_date()));
                long m1 = travel[0].getDeparture_date().getTime();
                long m2 = travel[0].getReturn_date().getTime();
                int dias = (int) ((m2 - m1) / (24*60*60*1000));
                tvDays.setText((dias + 1) + " "+getString(R.string.days));
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