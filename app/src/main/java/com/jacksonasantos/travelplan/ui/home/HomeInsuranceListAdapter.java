package com.jacksonasantos.travelplan.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Insurance;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

public class HomeInsuranceListAdapter extends RecyclerView.Adapter<HomeInsuranceListAdapter.MyViewHolder> {

    private final List<Insurance> mInsurance;
    Context context;
    String[] insurance_typeArray;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ConstraintLayout llInsuranceItem;
        private final ImageView imInsuranceType;
        private final ImageView imInsuranceStatus;
        private final TextView txtInsuranceFinalEffectiveDate;
        private final TextView txtInsurancePolicy;
        private final TextView txtDescription;
        private final ImageButton btnDoneInsurance;

        public MyViewHolder(View v) {
            super(v);
            llInsuranceItem = v.findViewById(R.id.llInsuranceItem);
            imInsuranceType = v.findViewById(R.id.imInsuranceType);
            imInsuranceStatus = v.findViewById(R.id.imInsuranceStatus);
            txtInsuranceFinalEffectiveDate = v.findViewById(R.id.txtInsuranceFinalEffectiveDate);
            txtInsurancePolicy = v.findViewById(R.id.txtInsurancePolicy);
            txtDescription = v.findViewById(R.id.txtDescription);
            btnDoneInsurance = v.findViewById(R.id.btnDoneInsurance);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public HomeInsuranceListAdapter(List<Insurance> insurance, Context context) {
        this.mInsurance = insurance;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public HomeInsuranceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View insuranceView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_item_insurance, parent, false);
        insurance_typeArray = parent.getResources().getStringArray(R.array.insurance_type_array);

        return new MyViewHolder(insuranceView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Insurance insurance = mInsurance.get(position);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (position%2==0) {
            holder.llInsuranceItem.setBackgroundColor(Color.rgb(209,193,233));
        } else {
            holder.llInsuranceItem.setBackgroundColor(Color.WHITE);
        }
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
        }
        holder.txtInsuranceFinalEffectiveDate.setText(Utils.dateToString(insurance.getFinal_effective_date()));
        holder.txtInsurancePolicy.setText(String.valueOf(insurance.getInsurance_policy()));
        holder.txtDescription.setText(insurance.getDescription());

        // btnDone - change Status for Service for completed and remove of list
        holder.btnDoneInsurance.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Insurance i1 = Database.mInsuranceDao.fetchInsuranceById(insurance.getId());
                    i1.setStatus(insurance.getStatus() == 0 ? 1 : 0);
                    if (Database.mInsuranceDao.updateInsurance(i1)) {
                        notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, context.getString(R.string.Error_Changing_Data)+ "\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mInsurance.size();
    }
}