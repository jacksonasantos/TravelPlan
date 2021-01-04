package com.jacksonasantos.travelplan.ui.general;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Insurance;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

public class InsuranceListAdapter extends RecyclerView.Adapter<InsuranceListAdapter.MyViewHolder> {

    private final List<Insurance> mInsurance;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtInsurancePolicy;
        public TextView txtInsuranceDescription;
        public ImageView imInsuranceType;
        public ImageView imInsuranceStatus;
        public ImageButton btnEdit;
        public ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            imInsuranceType = v.findViewById(R.id.imInsuranceType);
            imInsuranceStatus = v.findViewById(R.id.imInsuranceStatus);
            txtInsurancePolicy = v.findViewById(R.id.txtPolicy);
            txtInsuranceDescription = v.findViewById(R.id.txtDescription);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public InsuranceListAdapter(List<Insurance> insurance, Context context) {
        this.mInsurance = insurance;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public InsuranceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View insuranceView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_insurance, parent, false);

        return new MyViewHolder(insuranceView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Insurance insurance = mInsurance.get(position);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        holder.imInsuranceType.setImageResource(insurance.getInsurance_typeImage(insurance.getInsurance_type()));
        holder.imInsuranceStatus.setImageResource(R.drawable.ic_ball );
        try {
            if (insurance.getStatus() == 1) {
                holder.imInsuranceStatus.setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
            } else {
                if (!(insurance.getFinal_effective_date() == null)) {
                    if (System.currentTimeMillis() < Objects.requireNonNull(sdf.parse(Objects.requireNonNull(Utils.dateToString(insurance.getFinal_effective_date())))).getTime()) {
                        holder.imInsuranceStatus.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                    } else {
                        holder.imInsuranceStatus.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }holder.txtInsurancePolicy.setText(insurance.getInsurance_policy());
        holder.txtInsuranceDescription.setText(insurance.getDescription());
        // btnEdit
        holder.btnEdit.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), InsuranceActivity.class);
                intent.putExtra("insurance_id", insurance.getId());
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });
        // btnDelete
        holder.btnDelete.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.Insurance_Deleting)
                        .setMessage(R.string.Msg_Confirm)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    Database.mInsuranceDao.deleteInsurance(insurance.getId());
                                    mInsurance.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position,mInsurance.size());
                                } catch (Exception e) {
                                    Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }).setNegativeButton(R.string.No, null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mInsurance.size();
    }
}