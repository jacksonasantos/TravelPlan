package com.jacksonasantos.travelplan.ui.general;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import com.jacksonasantos.travelplan.ui.home.HomeInsuranceContactDataAdapter;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class InsuranceDialog {

    @SuppressLint("NotifyDataSetChanged")
    public static Insurance InsuranceClass(Insurance insurance, View v) {
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
        final GridLayoutManager gd = new GridLayoutManager(v.getContext(), 2);
        gd.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int p1) {
                return adapterInsuranceContact.getItemViewType(p1) == ListItemDialog.TYPE_HEADER ? 2 : 1;
            }
        });
        List<InsuranceContact> insuranceContact = Database.mInsuranceContactDao.fetchInsuranceContactByInsurance(insurance.getId());
        String str = "";
        for (int i = 0; i < insuranceContact.size(); i++) {
            if (!str.equals(insuranceContact.get(i).getType_contact())) {
                items.add(new DialogHeader(insuranceContact.get(i).getType_contact()));
                str = insuranceContact.get(i).getType_contact();
            }
            items.add(new DialogItem(insuranceContact.get(i).getDescription_contact(), insuranceContact.get(i).getDetail_contact()));
        }
        rvContact.setLayoutManager(gd);
        rvContact.setAdapter(adapterInsuranceContact);
        adapterInsuranceContact.notifyDataSetChanged();

        txtInsuranceVehicle.setText(Database.mVehicleDao.fetchVehicleById(insurance.getVehicle_id()).getName());
        // TODO - Incluir detalhes do veículo
        txtInsuranceTravel.setText((Database.mTravelDao.fetchTravelById(insurance.getTravel_id()).getDescription()));

        Insurance i1 = Database.mInsuranceDao.fetchInsuranceById(insurance.getId());
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext(), R.style.MyDialogStyle);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(R.string.OK, (dialog, id) -> {
                })
                .setNeutralButton(R.string.bt_Closed_Insurance, (dialog, id) -> {
                    try {
                        i1.setStatus(insurance.getStatus() == 0 ? 1 : 0);
                        i1.setTravel_id(insurance.getTravel_id() == 0 ? null : insurance.getTravel_id());
                        i1.setVehicle_id(insurance.getVehicle_id() == 0 ? null : insurance.getVehicle_id());
                        Database.mInsuranceDao.updateInsurance(i1);
                    } catch (Exception e) {
                        Toast.makeText(v.getContext(), v.getContext().getString(R.string.Error_Changing_Data) + "\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return i1;
    }
}
