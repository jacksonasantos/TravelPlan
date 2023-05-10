package com.jacksonasantos.travelplan.ui.general;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Insurance;
import com.jacksonasantos.travelplan.dao.general.Database;

import java.util.List;

public class InsuranceListAdapter extends RecyclerView.Adapter<InsuranceListAdapter.MyViewHolder> {

    private final List<Insurance> mInsurance;
    final Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ConstraintLayout clInsurance;
        public final TextView txtInsurancePolicy;
        public final TextView txtInsuranceDescription;
        public final ImageView imInsuranceType;
        public final ImageView imInsuranceStatus;
        public final ImageButton btnEdit;
        public final ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            clInsurance = v.findViewById(R.id.clInsurance);
            imInsuranceType = v.findViewById(R.id.imInsuranceType);
            imInsuranceStatus = v.findViewById(R.id.imInsuranceStatus);
            txtInsurancePolicy = v.findViewById(R.id.txtPolicy);
            txtInsuranceDescription = v.findViewById(R.id.txtDescription);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            clInsurance.setOnClickListener(this);
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

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Insurance insurance = mInsurance.get(position);

        holder.imInsuranceType.setImageResource(insurance.getInsurance_typeImage(insurance.getInsurance_type()));
        holder.imInsuranceStatus.setImageResource(R.drawable.ic_ball );
        holder.imInsuranceStatus.setColorFilter(insurance.getColorInsuranceStatus());
        holder.txtInsurancePolicy.setText(insurance.getInsurance_policy());
        holder.txtInsuranceDescription.setText(insurance.getDescription());

        // btnEdit
        holder.btnEdit.setOnClickListener (v -> {
            Intent intent = new Intent (v.getContext(), InsuranceActivity.class);
            intent.putExtra("insurance_id", insurance.getId());
            context.startActivity(intent);
            notifyItemChanged(position);
        });
        // btnDelete
        holder.btnDelete.setOnClickListener (v -> new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.Insurance_Deleting)
                .setMessage(R.string.Msg_Confirm)
                .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                    try {
                        Database.mInsuranceDao.deleteInsurance(insurance.getId());
                        mInsurance.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mInsurance.size());
                    } catch (Exception e) {
                        Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton(R.string.No, null)
                .show());
    }

    @Override
    public int getItemCount() {
        return mInsurance.size();
    }
}