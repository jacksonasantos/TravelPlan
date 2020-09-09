package com.jacksonasantos.travelplan.ui.vehicle;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.jacksonasantos.travelplan.DAO.Database;
import com.jacksonasantos.travelplan.DAO.Vehicle;
import com.jacksonasantos.travelplan.R;

import java.util.List;

public class VehicleListAdapterOLD extends Adapter<VehicleListAdapterOLD.MyViewHolder> {

    private List<Vehicle> mVehicle;
    private final Context context;

    public VehicleListAdapterOLD(List<Vehicle> vehicles, final Context context) {
        this.mVehicle = vehicles;
        this.context = context;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtPlate;
        public TextView txtName;
        public TextView txtAVG;
        public ImageButton btnEdit;
        public ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            txtPlate = (TextView) v.findViewById(R.id.txtPlate);
            txtName = (TextView) v.findViewById(R.id.txtName);
            txtAVG = (TextView) v.findViewById(R.id.txtAVG);
            btnEdit = (ImageButton) v.findViewById(R.id.btnEdit);
            btnDelete = (ImageButton) v.findViewById(R.id.btnDelete);
            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    @Override
    public VehicleListAdapterOLD.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        Database mDb = new Database(context);
        mDb.open();

        View vehicleView = inflater.inflate(R.layout.fragment_vehicle_item, parent, false);
        MyViewHolder vh = new MyViewHolder(vehicleView);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Vehicle vehicle = mVehicle.get(position);

        holder.txtPlate.setText(vehicle.getLicense_plate());
        holder.txtName.setText(vehicle.getName());
        holder.txtAVG.setText(Double.toString(vehicle.getAvg_consumption())+" Km/L");

        // btnEdit
        holder.btnEdit.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), VehicleActivity.class);
//                intent.putExtra("vehicle.name", vehicle.getName());
                /*Toast.makeText(v.getContext(), "Exemplo Toast " + vehicle.getId() +
                        "\nname " + vehicle.getName() +
                        "\nlicense_plate " + vehicle.getLicense_plate() +
                        "\nfull_capacity " + vehicle.getFull_capacity()+
                        "\navg_consumption " + vehicle.getAvg_consumption() +
                        "\nbrand " + vehicle.getBrand() +
                        "\ntype_fuel " + vehicle.getType_fuel()
                , Toast.LENGTH_SHORT).show();*/
  //              context.startActivity(intent);
            }
        });

        // btnDelete
        holder.btnDelete.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.Vehicle_Deleting)
                        .setMessage(R.string.Msg_Confirm)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Database.mVehicleDao.deleteVehicle((int)vehicle.getId());  // invoca a deleção do registro
                                mVehicle.remove(position);
                                notifyItemRemoved(position);
                            }
                        }).setNegativeButton(R.string.No, null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVehicle.size();
    }
}