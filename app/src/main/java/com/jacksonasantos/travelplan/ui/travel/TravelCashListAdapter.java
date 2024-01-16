package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Account;
import com.jacksonasantos.travelplan.dao.TravelCash;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class TravelCashListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<TravelCash> mTravelCash;
    final Context context;
    final int show_header;
    public final String form;

    final Globals g = Globals.getInstance();

    public TravelCashListAdapter(List<TravelCash> travelCash, Context context, String form, int show_header) {
        this.mTravelCash = travelCash;
        this.context = context;
        this.form = form;
        this.show_header = show_header; // 0 - NO SHOW HEADER | 1 - SHOW HEADER
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vehicleView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_travel_item_cash, parent, false);

        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(vehicleView);
        } else return new ItemViewHolder(vehicleView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.llItemCash.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            headerViewHolder.txtAccount.setText(R.string.TravelCash_Account);
            headerViewHolder.txtCurrency.setText(R.string.TravelCash_Currency);
            headerViewHolder.txtDtCash.setText(R.string.TravelCash_DateCash);
            headerViewHolder.txtVlrCash.setText(R.string.TravelCash_AmountCash);
            headerViewHolder.btnAddCash.setVisibility(View.VISIBLE);
            headerViewHolder.btnAddCash.setImageResource(R.drawable.ic_button_add);
            headerViewHolder.btnAddCash.setOnClickListener(v -> {
                LayoutInflater li = LayoutInflater.from(v.getContext());
                View promptsView = li.inflate(R.layout.dialog_travel_cash, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setView(promptsView);
                final Spinner spinAccount = promptsView.findViewById(R.id.spinAccount);
                final Spinner spinCurrency = promptsView.findViewById(R.id.spinCurrency);
                final int[] nrSpCurrency = new int[1];
                final EditText etDateCash = promptsView.findViewById(R.id.etDateCash);
                final EditText etAmountCash = promptsView.findViewById(R.id.etAmountCash);
                final Integer[] nrSpinAccount = {null};

                etDateCash.addTextChangedListener(new DateInputMask(etDateCash));

                final List<Account> accounts =  Database.mAccountDao.fetchAllAccount();
                accounts.add(0, new Account());
                ArrayAdapter<Account> adapterA = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, accounts);
                adapterA.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                spinAccount.setAdapter(adapterA);
                if (nrSpinAccount[0] != null && nrSpinAccount[0] > 0) {
                    Account acc1 = Database.mAccountDao.fetchAccountById(nrSpinAccount[0]);
                    for (int x = 1; x <= spinAccount.getAdapter().getCount(); x++) {
                        if (spinAccount.getAdapter().getItem(x).toString().equals(acc1.getDescription())) {
                            spinAccount.setSelection(x);
                            nrSpinAccount[0] = acc1.getId();
                            break;
                        }
                    }
                }
                final Account[] a1 = {new Account()};
                spinAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                        a1[0] = (Account) parent.getItemAtPosition(position);
                        nrSpinAccount[0] = a1[0].getId();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        nrSpinAccount[0] = null;
                    }
                });
                adapterA.notifyDataSetChanged();

                Utils.createSpinnerResources(R.array.currency_array, spinCurrency, context);
                spinCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                        nrSpCurrency[0] = position;
                        spinCurrency.setSelection(nrSpCurrency[0]);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        nrSpCurrency[0] =0;
                    }
                });

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(R.string.OK, (dialog, id) -> {
                            boolean isSave = false;
                            TravelCash TC = new TravelCash();
                            TC.setAccount_id(nrSpinAccount[0]);
                            TC.setTravel_id(g.getIdTravel());
                            TC.setCurrency_id(spinCurrency.getSelectedItemPosition());
                            TC.setCash_deposit(Utils.stringToDate(etDateCash.getText().toString()));
                            if (!etAmountCash.getText().toString().isEmpty()) {
                                TC.setAmount_deposit(Double.parseDouble(etAmountCash.getText().toString()));
                            }

                            try {
                                isSave = Database.mTravelCashDao.addTravelCash(TC);
                            } catch (Exception e) {
                                Toast.makeText(context, R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            if (!isSave) {
                                Toast.makeText(context, R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                            } else {
                                mTravelCash.add(TC);
                                notifyItemInserted(mTravelCash.size()+show_header);
                            }
                        })
                        .setNegativeButton(R.string.Cancel, (dialog, id) -> dialog.cancel());
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            });
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final TravelCash travelCash = mTravelCash.get(position-show_header);

            itemViewHolder.txtAccount.setText(Database.mAccountDao.fetchAccountById(travelCash.getAccount_id()).getDescription());
            itemViewHolder.txtCurrency.setText(context.getResources().getStringArray(R.array.currency_array)[travelCash.getCurrency_id()]);
            itemViewHolder.txtDtCash.setText(Utils.dateToString(travelCash.getCash_deposit()));
            itemViewHolder.txtVlrCash.setText(Double.toString(travelCash.getAmount_deposit()));

            itemViewHolder.btnDelete.setOnClickListener(v -> new AlertDialog.Builder(v.getContext())
                    .setTitle(R.string.TravelCash_Deleting)
                    .setMessage(R.string.Msg_Confirm)
                    .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                        try {
                            Database.mTravelCashDao.deleteTravelCash(travelCash.getId());
                            mTravelCash.remove(position - show_header);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mTravelCash.size()+show_header);
                        } catch (Exception e) {
                            Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).setNegativeButton(R.string.No, null)
                    .show()
            );

            itemViewHolder.llItemCash.setOnClickListener(v -> {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.dialog_travel_cash, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setView(promptsView);

                final Spinner spinAccount = promptsView.findViewById(R.id.spinAccount);
                final Spinner spinCurrency = promptsView.findViewById(R.id.spinCurrency);
                final int[] nrSpCurrency = new int[1];
                final EditText etDateCash = promptsView.findViewById(R.id.etDateCash);
                final EditText etAmountCash = promptsView.findViewById(R.id.etAmountCash);
                final Integer[] nrSpinAccount = {travelCash.getAccount_id()};

                etDateCash.addTextChangedListener(new DateInputMask(etDateCash));

                final List<Account> accounts =  Database.mAccountDao.fetchAllAccount();
                accounts.add(0, new Account());
                ArrayAdapter<Account> adapterA = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, accounts);
                adapterA.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                spinAccount.setAdapter(adapterA);
                if (nrSpinAccount[0] != null && nrSpinAccount[0] > 0) {
                    Account acc1 = Database.mAccountDao.fetchAccountById(nrSpinAccount[0]);
                    for (int x = 1; x <= spinAccount.getAdapter().getCount(); x++) {
                        if (spinAccount.getAdapter().getItem(x).toString().equals(acc1.getDescription())) {
                            spinAccount.setSelection(x);
                            nrSpinAccount[0] = acc1.getId();
                            break;
                        }
                    }
                }
                final Account[] a1 = {new Account()};
                spinAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                        a1[0] = (Account) parent.getItemAtPosition(position);
                        nrSpinAccount[0] = a1[0].getId();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        nrSpinAccount[0] = null;
                    }
                });
                adapterA.notifyDataSetChanged();

                Utils.createSpinnerResources(R.array.currency_array, spinCurrency, context);
                spinCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                        nrSpCurrency[0] = position;
                        spinCurrency.setSelection(nrSpCurrency[0]);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        nrSpCurrency[0] =0;
                    }
                });

                spinCurrency.setSelection(travelCash.getCurrency_id());
                etDateCash.setText(Utils.dateToString(travelCash.getCash_deposit()));
                etAmountCash.setText(String.valueOf(travelCash.getAmount_deposit()));

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(R.string.OK, (dialog, id) -> {
                            boolean isSave = false;

                            travelCash.setAccount_id(nrSpinAccount[0]);
                            travelCash.setCurrency_id(nrSpCurrency[0]);
                            travelCash.setCash_deposit(Utils.stringToDate(etDateCash.getText().toString()));
                            travelCash.setAmount_deposit(Double.parseDouble(etAmountCash.getText().toString()));

                            try {
                                isSave = Database.mTravelCashDao.updateTravelCash(travelCash);
                            } catch (Exception e) {
                                Toast.makeText(context, R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            if (!isSave) {
                                Toast.makeText(context, R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                            } else {
                                mTravelCash.set(position-show_header, travelCash);
                                notifyItemRangeChanged(position-show_header, mTravelCash.size());
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
        return mTravelCash.size()+show_header;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llItemCash;
        public final TextView txtAccount;
        public final TextView txtCurrency;
        public final TextView txtDtCash;
        public final TextView txtVlrCash;
        public final ImageButton btnAddCash;

        public HeaderViewHolder(View v) {
            super(v);
            llItemCash = v.findViewById(R.id.llItemCash);
            txtAccount = v.findViewById(R.id.txtAccount);
            txtCurrency = v.findViewById(R.id.txtCurrency);
            txtDtCash = v.findViewById(R.id.txtDtCash);
            txtVlrCash = v.findViewById(R.id.txtVlrCash);
            btnAddCash = v.findViewById(R.id.btnDelete);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llItemCash;
        public final TextView txtAccount;
        public final TextView txtCurrency;
        public final TextView txtDtCash;
        public final TextView txtVlrCash;
        public final ImageButton btnDelete;

        public ItemViewHolder(View v) {
            super(v);
            llItemCash = v.findViewById(R.id.llItemCash);
            txtAccount = v.findViewById(R.id.txtAccount);
            txtCurrency = v.findViewById(R.id.txtCurrency);
            txtDtCash = v.findViewById(R.id.txtDtCash);
            txtVlrCash = v.findViewById(R.id.txtVlrCash);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
