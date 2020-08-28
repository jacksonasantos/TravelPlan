package com.jacksonasantos.travelplan.ui.vehicle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jacksonasantos.travelplan.DAO.Vehicle;
import com.jacksonasantos.travelplan.R;

import java.util.List;

public  class VehicleAdapter extends ArrayAdapter<Vehicle> {
    //public abstract class VehicleAdapter extends BaseAdapter {

    private List<Vehicle> vehicles;
    private LayoutInflater inflater;

    public VehicleAdapter(Context context, List<Vehicle> vehicles) {
        super(context, 0, vehicles);
        //this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.vehicles = vehicles;
    }

    public void novosDados(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(vehicles != null) {
            return vehicles.size();
        }else{
            return 0;
        }
    }

    @Override
    public Vehicle getItem(int position) {
        return vehicles.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.fragment_vehicle_item, null);
        ((TextView) (v.findViewById(R.id.txtNome))).setText((CharSequence) vehicles.get(position));

        ((ImageButton) (v.findViewById(R.id.btnEditar))).setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edita(vehicles.get(position));
                    }
                });

        ((ImageButton) (v.findViewById(R.id.btnExcluir)))
                .setOnClickListener
                        (new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleta(vehicles.get(position));
                            }
                        });

        return v;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void edita(Vehicle vehicle) {

    }

    public void deleta(Vehicle vehicle) {

    }
}