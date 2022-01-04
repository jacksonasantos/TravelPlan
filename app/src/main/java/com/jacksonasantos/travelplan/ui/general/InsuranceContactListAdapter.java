package com.jacksonasantos.travelplan.ui.general;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.dao.InsuranceContact;

import java.util.List;

public class InsuranceContactListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    public final List<InsuranceContact> mInsuranceContact;
    final Context context;
    final int show_header;
    final int show_footer;
    final Integer insurance_id;
    final boolean show_button;

    public InsuranceContactListAdapter(Integer insurance_id, List<InsuranceContact> insuranceContact, Context context, int show_header, int show_footer, boolean show_button) {
        this.mInsuranceContact = insuranceContact;
        this.context = context;
        this.show_header = show_header>=1?1:0;
        this.show_footer = show_footer>=1?1:0;
        this.insurance_id = insurance_id;
        this.show_button = show_button;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View insuranceContactView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_insurance_contact, parent, false);

        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(insuranceContactView);
        } else if (viewType == TYPE_FOOTER) {
            return new FooterViewHolder(insuranceContactView);
        } else return new ItemViewHolder(insuranceContactView);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.llItemInsuranceContact.setBackgroundColor(Color.LTGRAY);
            headerViewHolder.txtTypeContact.setText(R.string.Insurance_Contact_Type);
            headerViewHolder.txtDescriptionContact.setText(R.string.Insurance_Contact_Description);
            headerViewHolder.txtDetailContact.setText(R.string.Insurance_Contact_Detail);
            if (show_button) {
                headerViewHolder.btnAddInsuranceContact.setImageResource(R.drawable.ic_button_add);
                headerViewHolder.btnAddInsuranceContact.setOnClickListener(v -> {
                    LayoutInflater li = LayoutInflater.from(v.getContext());
                    View promptsView = li.inflate(R.layout.dialog_insurance_contact, null);

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

                    alertDialogBuilder.setView(promptsView);
                    final EditText etType_Contact = promptsView.findViewById(R.id.etType_Contact);
                    final EditText etDescription_Contact = promptsView.findViewById(R.id.etDescription_Contact);
                    final EditText etDetail_Contact = promptsView.findViewById(R.id.etDetail_Contact);

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton(R.string.OK, (dialog, id) -> {
                                boolean isSave = false;

                                InsuranceContact IC = new InsuranceContact();

                                IC.setInsurance_id(insurance_id);
                                IC.setType_contact(etType_Contact.getText().toString());
                                IC.setDescription_contact(etDescription_Contact.getText().toString());
                                IC.setDetail_contact(etDetail_Contact.getText().toString());

                                try {
                                    isSave = Database.mInsuranceContactDao.addInsuranceContact(IC);
                                } catch (Exception e) {
                                    Toast.makeText(context, R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                                if (!isSave) {
                                    Toast.makeText(context, R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                                } else {
                                    mInsuranceContact.add(IC);
                                    notifyItemInserted(mInsuranceContact.size());
                                }
                            })
                            .setNegativeButton(R.string.Cancel, (dialog, id) -> dialog.cancel());
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                });
            } else {
                headerViewHolder.btnAddInsuranceContact.setVisibility(View.INVISIBLE);
            }
        }
        else if (holder instanceof ItemViewHolder) {

            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final InsuranceContact insuranceContact = mInsuranceContact.get(position-show_header);

            itemViewHolder.txtTypeContact.setText(insuranceContact.getType_contact());
            itemViewHolder.txtDescriptionContact.setText(insuranceContact.getDescription_contact());
            itemViewHolder.txtDetailContact.setText(insuranceContact.getDetail_contact());
            // btnDelete
            if (show_button) {
                itemViewHolder.btnDelete.setOnClickListener(v -> new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.Insurance_Contact_Deleting)
                        .setMessage(R.string.Msg_Confirm)
                        .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                            try {
                                Database.mInsuranceContactDao.deleteInsuranceContact(insuranceContact.getId());
                                mInsuranceContact.remove(position - show_header);
                                notifyItemRemoved(position - show_header);
                                notifyItemRangeChanged(position - show_header, mInsuranceContact.size());
                            } catch (Exception e) {
                                Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton(R.string.No, null)
                        .show());
            } else {
                itemViewHolder.btnDelete.setVisibility(View.INVISIBLE);
            }
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0 && show_header == 1) {
            return TYPE_HEADER;
        } if (position == mInsuranceContact.size()+show_header && show_footer == 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mInsuranceContact.size()+show_header+show_footer;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout llItemInsuranceContact;
        private final TextView txtTypeContact;
        private final TextView txtDescriptionContact;
        private final TextView txtDetailContact;
        private final ImageButton btnAddInsuranceContact;

        public HeaderViewHolder(View v) {
            super(v);
            llItemInsuranceContact = v.findViewById(R.id.llItemInsuranceContact);
            txtTypeContact = v.findViewById(R.id.txtTypeContact);
            txtDescriptionContact = v.findViewById(R.id.txtDescriptionContact);
            txtDetailContact = v.findViewById(R.id.txtDetailContact);
            btnAddInsuranceContact = v.findViewById(R.id.btnDelete);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTypeContact;
        private final TextView txtDescriptionContact;
        private final TextView txtDetailContact;
        private final ImageButton btnDelete;

        public ItemViewHolder(View v) {
            super(v);
            txtTypeContact = v.findViewById(R.id.txtTypeContact);
            txtDescriptionContact = v.findViewById(R.id.txtDescriptionContact);
            txtDetailContact = v.findViewById(R.id.txtDetailContact);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout llItemInsuranceContact;
        private final TextView txtTypeContact;
        private final TextView txtDescriptionContact;
        private final TextView txtDetailContact;
        private final ImageButton btnDelete;

        public FooterViewHolder(View v) {
            super(v);
            llItemInsuranceContact = v.findViewById(R.id.llItemInsuranceContact);
            txtTypeContact = v.findViewById(R.id.txtTypeContact);
            txtDescriptionContact = v.findViewById(R.id.txtDescriptionContact);
            txtDetailContact = v.findViewById(R.id.txtDetailContact);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
