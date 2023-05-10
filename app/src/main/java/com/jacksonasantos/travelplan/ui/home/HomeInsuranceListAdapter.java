package com.jacksonasantos.travelplan.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import com.jacksonasantos.travelplan.ui.general.InsuranceDialog;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class HomeInsuranceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<Insurance> mInsurance;
    final Context context;
    final int show_header;

    public HomeInsuranceListAdapter(List<Insurance> insurance, Context context, int show_header) {
        this.mInsurance = insurance;
        this.context = context;
        this.show_header = show_header;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View insuranceView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_item_insurance, parent, false);

        if (viewType == TYPE_HEADER) return new HeaderViewHolder(insuranceView);
        else return new ItemViewHolder(insuranceView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.llInsuranceItem.setBackgroundColor(Color.LTGRAY);
            headerViewHolder.imInsuranceType.setVisibility(View.INVISIBLE);
            headerViewHolder.imInsuranceStatus.setVisibility(View.INVISIBLE);
            headerViewHolder.txtInsuranceFinalEffectiveDate.setText(R.string.Insurance_Final_Effective_Date);
            headerViewHolder.txtInsurancePolicy.setText(R.string.Insurance_Insurance_Policy);
            headerViewHolder.txtInsuranceCompany.setText(R.string.Insurance_Company);
            headerViewHolder.txtInsuranceBroker.setText(R.string.Insurance_Broker);
            headerViewHolder.btnDoneInsurance.setVisibility(View.INVISIBLE);
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final Insurance insurance = mInsurance.get(position-show_header);
            if (position % 2 == 0) {
                itemViewHolder.llInsuranceItem.setBackgroundColor(Color.LTGRAY);
            } else {
                itemViewHolder.llInsuranceItem.setBackgroundColor(Color.WHITE);
            }

            itemViewHolder.llInsuranceItem.setOnClickListener(v -> InsuranceDialog.InsuranceClass(insurance, v));

            itemViewHolder.imInsuranceType.setImageResource(insurance.getInsurance_typeImage(insurance.getInsurance_type()));
            itemViewHolder.imInsuranceStatus.setImageResource(R.drawable.ic_ball);
            itemViewHolder.imInsuranceStatus.setColorFilter(insurance.getColorInsuranceStatus(), PorterDuff.Mode.MULTIPLY);
            itemViewHolder.txtInsuranceCompany.setText(Database.mInsuranceCompanyDao.fetchInsuranceCompanyById(insurance.getInsurance_company_id()).getCompany_name());
            itemViewHolder.txtInsuranceBroker.setText(Database.mBrokerDao.fetchBrokerById(insurance.getBroker_id()).getName());
            itemViewHolder.txtInsurancePolicy.setText(String.valueOf(insurance.getInsurance_policy()));
            itemViewHolder.txtInsuranceFinalEffectiveDate.setText(Utils.dateToString(insurance.getFinal_effective_date()));
            itemViewHolder.btnDoneInsurance.setOnClickListener(v -> {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                    alertDialogBuilder
                            .setTitle(context.getString(R.string.Insurance_Termination))
                            .setCancelable(false)
                            .setPositiveButton(R.string.OK, (dialog, id) -> {
                                try {
                                    Insurance i1 = Database.mInsuranceDao.fetchInsuranceById(insurance.getId());
                                    i1.setStatus(insurance.getStatus() == 1 ? 2 : 1);
                                    i1.setTravel_id(insurance.getTravel_id() == 0 ? null : insurance.getTravel_id());
                                    i1.setVehicle_id(insurance.getVehicle_id() == 0 ? null : insurance.getVehicle_id());
                                    i1.setBroker_id(insurance.getBroker_id() == 0 ? null : insurance.getBroker_id());
                                    if (Database.mInsuranceDao.updateInsurance(i1)) {
                                        mInsurance.remove(position);
                                        notifyItemRemoved(position);
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(context, context.getString(R.string.Error_Changing_Data) + "\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton(R.string.Cancel, (dialog, id) -> dialog.cancel());
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
            });
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0 && show_header == 1) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mInsurance.size()+show_header;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout llInsuranceItem;
        private final ImageView imInsuranceType;
        private final ImageView imInsuranceStatus;
        private final TextView txtInsuranceFinalEffectiveDate;
        private final TextView txtInsurancePolicy;
        private final TextView txtInsuranceCompany;
        private final TextView txtInsuranceBroker;
        private final ImageButton btnDoneInsurance;

        public HeaderViewHolder(View v) {
            super(v);
            llInsuranceItem = v.findViewById(R.id.llInsuranceItem);
            imInsuranceType = v.findViewById(R.id.imInsuranceType);
            imInsuranceStatus = v.findViewById(R.id.imInsuranceStatus);
            txtInsuranceFinalEffectiveDate = v.findViewById(R.id.txtInsuranceFinalEffectiveDate);
            txtInsurancePolicy = v.findViewById(R.id.txtInsurancePolicy);
            txtInsuranceBroker = v.findViewById(R.id.txtInsuranceBroker);
            txtInsuranceCompany = v.findViewById(R.id.txtInsuranceCompany);
            btnDoneInsurance = v.findViewById(R.id.btnDoneInsurance);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout llInsuranceItem;
        private final ImageView imInsuranceType;
        private final ImageView imInsuranceStatus;
        private final TextView txtInsuranceFinalEffectiveDate;
        private final TextView txtInsurancePolicy;
        private final TextView txtInsuranceCompany;
        private final TextView txtInsuranceBroker;
        private final ImageButton btnDoneInsurance;

        public ItemViewHolder(View v) {
            super(v);
            llInsuranceItem = v.findViewById(R.id.llInsuranceItem);
            imInsuranceType = v.findViewById(R.id.imInsuranceType);
            imInsuranceStatus = v.findViewById(R.id.imInsuranceStatus);
            txtInsuranceFinalEffectiveDate = v.findViewById(R.id.txtInsuranceFinalEffectiveDate);
            txtInsurancePolicy = v.findViewById(R.id.txtInsurancePolicy);
            txtInsuranceBroker = v.findViewById(R.id.txtInsuranceBroker);
            txtInsuranceCompany = v.findViewById(R.id.txtInsuranceCompany);
            btnDoneInsurance = v.findViewById(R.id.btnDoneInsurance);
        }
    }
}
