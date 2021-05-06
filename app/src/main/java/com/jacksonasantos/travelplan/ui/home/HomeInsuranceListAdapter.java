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
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Broker;
import com.jacksonasantos.travelplan.dao.Insurance;
import com.jacksonasantos.travelplan.dao.InsuranceContact;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.dao.general.DialogHeader;
import com.jacksonasantos.travelplan.dao.general.DialogItem;
import com.jacksonasantos.travelplan.dao.general.ListItemDialog;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeInsuranceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<Insurance> mInsurance;
    Context context;
    int show_header;
    String[] insurance_typeArray;

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
        insurance_typeArray = parent.getResources().getStringArray(R.array.insurance_type_array);

        if (viewType == TYPE_ITEM) {
            return new ItemViewHolder(insuranceView);
        } else if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(insuranceView);
        }
        else return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            if (position % 2 == 0) {
                itemViewHolder.llInsuranceItem.setBackgroundColor(Color.LTGRAY);
            } else {
                itemViewHolder.llInsuranceItem.setBackgroundColor(Color.WHITE);
            }
            itemViewHolder.imInsuranceType.setImageResource(insurance.getInsurance_typeImage(insurance.getInsurance_type()));
            itemViewHolder.imInsuranceStatus.setImageResource(R.drawable.ic_ball);
            try {
                if (insurance.getStatus() == 1) {
                    itemViewHolder.imInsuranceStatus.setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
                } else {
                    if (!(insurance.getFinal_effective_date() == null)) {
                        if (System.currentTimeMillis() < Objects.requireNonNull(sdf.parse(Objects.requireNonNull(Utils.dateToString(insurance.getFinal_effective_date())))).getTime()) {
                            itemViewHolder.imInsuranceStatus.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        } else {
                            itemViewHolder.imInsuranceStatus.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            itemViewHolder.txtInsuranceCompany.setText(Database.mInsuranceCompanyDao.fetchInsuranceCompanyById(insurance.getInsurance_company_id()).getCompany_name());
            itemViewHolder.txtInsuranceBroker.setText(Database.mBrokerDao.fetchBrokerById(insurance.getBroker_id()).getName());
            itemViewHolder.txtInsurancePolicy.setText(String.valueOf(insurance.getInsurance_policy()));
            itemViewHolder.txtInsuranceFinalEffectiveDate.setText(Utils.dateToString(insurance.getFinal_effective_date()));

            itemViewHolder.llInsuranceItem.setOnClickListener(v -> {
                LayoutInflater li = LayoutInflater.from(v.getContext());
                View promptsView = li.inflate(R.layout.dialog_home_insurance, null);

                TextView txtBrokerName = promptsView.findViewById(R.id.txtBrokerName);
                TextView txtContactName = promptsView.findViewById(R.id.txtContactName);
                TextView txtBrokerPhone = promptsView.findViewById(R.id.txtBrokerPhone);
                TextView txtBrokerEmail = promptsView.findViewById(R.id.txtBrokerEmail);

                ImageView imgInsuranceType = promptsView.findViewById(R.id.imgInsuranceType);
                TextView txtCompanyName = promptsView.findViewById(R.id.txtCompanyName);
                TextView txtDescription = promptsView.findViewById(R.id.txtDescription);
                TextView txtInsurancePolicy = promptsView.findViewById(R.id.txtInsurancePolicy);
                TextView txtIssuanceDate = promptsView.findViewById(R.id.txtIssuanceDate);
                TextView txtInitialDate = promptsView.findViewById(R.id.txtInitialDate);
                TextView txtFinalDate = promptsView.findViewById(R.id.txtFinalDate);

                RecyclerView rvContact = promptsView.findViewById(R.id.rvContact);

                TextView txtInsuranceVehicle = promptsView.findViewById(R.id.txtInsuranceVehicle);
                TextView txtInsuranceTravel = promptsView.findViewById(R.id.txtInsuranceTravel);

                Broker brokerList = Database.mBrokerDao.fetchBrokerById(insurance.getBroker_id());
                txtBrokerName.setText(brokerList.getName());
                txtContactName.setText(brokerList.getContact_name());
                txtBrokerPhone.setText(brokerList.getPhone());
                txtBrokerEmail.setText(brokerList.getEmail());

                imgInsuranceType.setImageResource(insurance.getInsurance_typeImage(insurance.getInsurance_type()));
                txtCompanyName.setText(Database.mInsuranceCompanyDao.fetchInsuranceCompanyById(insurance.getInsurance_company_id()).getCompany_name().trim());
                txtDescription.setText(insurance.getDescription());
                txtInsurancePolicy.setText(insurance.getInsurance_policy());
                txtIssuanceDate.setText(Utils.dateToString(insurance.getIssuance_date()));
                txtInitialDate.setText(Utils.dateToString(insurance.getInitial_effective_date()));
                txtFinalDate.setText(Utils.dateToString(insurance.getFinal_effective_date()));

                final ArrayList<ListItemDialog> items = new ArrayList<>();
                final HomeInsuranceContactDataAdapter adapterInsuranceContact = new HomeInsuranceContactDataAdapter(items);
                final GridLayoutManager gd = new GridLayoutManager(context, 2);
                gd.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position1) {
                        return adapterInsuranceContact.getItemViewType(position1) == ListItemDialog.TYPE_HEADER ? 2 : 1;
                    }
                });
                List<InsuranceContact> insuranceContact = Database.mInsuranceContactDao.fetchInsuranceContactByInsurance(insurance.getId());
                String x = "";
                for (int i=0; i<insuranceContact.size(); i++) {
                    if (!x.equals(insuranceContact.get(i).getType_contact())){
                        items.add(new DialogHeader(insuranceContact.get(i).getType_contact()));
                        x = insuranceContact.get(i).getType_contact();
                    }
                    items.add(new DialogItem(insuranceContact.get(i).getDescription_contact(),insuranceContact.get(i).getDetail_contact()));
                }
                rvContact.setLayoutManager(gd);
                rvContact.setAdapter(adapterInsuranceContact);
                adapterInsuranceContact.notifyDataSetChanged();

                txtInsuranceVehicle.setText(Database.mVehicleDao.fetchVehicleById(insurance.getVehicle_id()).getName());
                txtInsuranceTravel.setText((Database.mTravelDao.fetchTravelById(insurance.getTravel_id()).getDescription()));

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext(), R.style.MyDialogStyle);
                alertDialogBuilder.setView(promptsView);
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton(R.string.OK, (dialog, id) -> {
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            });

            // btnDone - change Status for Service for completed and remove of list
            itemViewHolder.btnDoneInsurance.setOnClickListener(v -> {
                try {
                    Insurance i1 = Database.mInsuranceDao.fetchInsuranceById(insurance.getId());
                    i1.setStatus(insurance.getStatus() == 0 ? 1 : 0);
                    if (Database.mInsuranceDao.updateInsurance(i1)) {
                        mInsurance.remove(position);
                        notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, context.getString(R.string.Error_Changing_Data) + "\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
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
