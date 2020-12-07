package com.jacksonasantos.travelplan.ui.general;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.InsuranceCompany;
import java.util.List;

public class InsuranceCompanyListAdapter extends RecyclerView.Adapter<InsuranceCompanyListAdapter.MyViewHolder> {

    private final List<InsuranceCompany> mInsuranceCompany;
    Context context;


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtCompanyName;
        public ImageButton btnEdit;
        public ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            txtCompanyName = v.findViewById(R.id.txtCompanyName);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public InsuranceCompanyListAdapter(List<InsuranceCompany> insuranceCompany, Context context) {
        this.mInsuranceCompany = insuranceCompany;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public InsuranceCompanyListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View insuranceCompanyView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_insurance_company, parent, false);

        return new MyViewHolder(insuranceCompanyView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final InsuranceCompany insuranceCompany = mInsuranceCompany.get(position);

        holder.txtCompanyName.setText(insuranceCompany.getCompany_name());
        // btnEdit
        holder.btnEdit.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), InsuranceCompanyActivity.class);
                intent.putExtra("insuranceCompany_id", insuranceCompany.getId());
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });

        // btnDelete
        holder.btnDelete.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.Insurance_Company_Deleting)
                        .setMessage(R.string.Msg_Confirm)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    Database.mInsuranceCompanyDao.deleteInsuranceCompany(insuranceCompany.getId());
                                    mInsuranceCompany.remove(position);
                                    notifyItemRemoved(position);
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
        return mInsuranceCompany.size();
    }
}