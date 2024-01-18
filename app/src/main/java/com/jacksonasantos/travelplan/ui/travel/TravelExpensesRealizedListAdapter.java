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
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.TravelItemExpenses;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TravelExpensesRealizedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    public final List<TravelItemExpenses> mTravelItemExpenses;
    final Context context;
    final int show_header;
    final int show_footer;
    final Integer mTravel_id;
    final int mExpense_type;
    Double vTotal = 0.0;
    Integer nPositionChanged = null;

    final Globals g = Globals.getInstance();
    final Locale locale = new Locale(g.getLanguage(), g.getCountry());
    final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    public TravelExpensesRealizedListAdapter(List<TravelItemExpenses> travelItemExpenses, Context context, int show_header, int show_footer, Integer travel_id, int expense_type) {
        this.mTravelItemExpenses = travelItemExpenses;
        this.context = context;
        this.show_header = show_header >= 1 ? 1 : 0;
        this.show_footer = show_footer >= 1 ? 1 : 0;
        this.mTravel_id = travel_id;
        this.mExpense_type = expense_type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View expenseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_travel_item_expenses, parent, false);
        if (viewType == TYPE_HEADER) return new HeaderViewHolder(expenseView);
        else if (viewType == TYPE_FOOTER) return new FooterViewHolder(expenseView);
        else return new ItemViewHolder(expenseView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            Travel mTravel = Database.mTravelDao.fetchTravelById(mTravel_id);
            headerViewHolder.llTravelExpenses.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            headerViewHolder.txtAccount.setText(R.string.TravelItemExpenses_Account);
            headerViewHolder.txtExpenseDate.setText(R.string.TravelItemExpenses_Date);
            headerViewHolder.txtRealizedValue.setText(R.string.TravelItemExpenses_Value);
            headerViewHolder.txtNote.setText(R.string.TravelExpenses_Note);
            headerViewHolder.btnAdd.setImageResource(R.drawable.ic_button_add);
            headerViewHolder.btnAdd.setOnClickListener(v -> {
                if (mTravel.getStatus() < 2) {
                    Toast.makeText(context, R.string.Travel_Status_Planning, Toast.LENGTH_LONG).show();
                } else {
                    LayoutInflater li = LayoutInflater.from(v.getContext());
                    View promptsView = li.inflate(R.layout.dialog_travel_expenses_realized, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                    alertDialogBuilder.setView(promptsView);
                    final Spinner spinAccount = promptsView.findViewById(R.id.spinAccount);
                    final Spinner spinCurrency = promptsView.findViewById(R.id.spinCurrency);
                    final EditText etExpenseDate = promptsView.findViewById(R.id.etExpenseDate);
                    final EditText etExpenseItemRealizedValue = promptsView.findViewById(R.id.etExpenseItemRealizedValue);
                    final EditText etExpenseItemNote = promptsView.findViewById(R.id.etExpenseItemNote);
                    etExpenseDate.addTextChangedListener(new DateInputMask(etExpenseDate));
                    final int[] nrSpCurrency = new int[1];
                    final Integer[] nrSpinAccount = {null};
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
                            .setPositiveButton(R.string.OK, (dialog, id1) -> {
                                boolean isSave = true;
                                TravelItemExpenses tie = new TravelItemExpenses();
                                try {
                                    tie.setTravel_id(mTravel_id);
                                    tie.setAccount_id(nrSpinAccount[0]);
                                    tie.setCurrency_id(nrSpCurrency[0]);
                                    tie.setExpense_type(mExpense_type);
                                    tie.setExpense_date(Utils.stringToDate(etExpenseDate.getText().toString()));
                                    if (!etExpenseItemRealizedValue.getText().toString().isEmpty()) {
                                        tie.setRealized_value(Double.parseDouble(etExpenseItemRealizedValue.getText().toString()));
                                    }
                                    tie.setNote(etExpenseItemNote.getText().toString());
                                    isSave = Database.mTravelItemExpensesDao.addTravelItemExpenses(tie);
                                } catch (Exception e) {
                                    Toast.makeText(context, R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                                if (!isSave) {
                                    Toast.makeText(context, R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                                }else {
                                    mTravelItemExpenses.add(tie);
                                    notifyItemInserted(mTravelItemExpenses.size()+show_header+show_footer);
                                }
                            })
                            .setNegativeButton(R.string.Cancel, (dialog, id1) -> dialog.cancel());
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });

        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.llTravelExpenses.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            footerViewHolder.txtAccount.setText("");
            footerViewHolder.txtExpenseDate.setText(R.string.TravelExpenses_Sum);
            footerViewHolder.txtRealizedValue.setText(currencyFormatter.format(vTotal));
            footerViewHolder.txtNote.setText("");
            footerViewHolder.btnDelete.setVisibility(View.INVISIBLE);

        } else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final TravelItemExpenses travelItemExpenses = mTravelItemExpenses.get(position - show_header);
            itemViewHolder.txtAccount.setText(Database.mAccountDao.fetchAccountById(travelItemExpenses.getAccount_id()).getDescription());
            itemViewHolder.txtExpenseDate.setText(Utils.dateToString(travelItemExpenses.getExpense_date()));
            itemViewHolder.txtNote.setText(travelItemExpenses.getNote());

            vTotal += travelItemExpenses.getRealized_value();
            if (nPositionChanged!=null && position==nPositionChanged ) {
                try {
                    vTotal -= Double.parseDouble(Objects.requireNonNull(currencyFormatter.parse(itemViewHolder.txtRealizedValue.getText().toString())).toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                vTotal += travelItemExpenses.getRealized_value();
                nPositionChanged = 0;
            }
            if (nPositionChanged!=null && position!=nPositionChanged) {
                vTotal -= travelItemExpenses.getRealized_value();
            }
            itemViewHolder.txtRealizedValue.setText(currencyFormatter.format(travelItemExpenses.getRealized_value()));

            itemViewHolder.llTravelExpenses.setOnClickListener(v -> {
                LayoutInflater li = LayoutInflater.from(v.getContext());
                View promptsView = li.inflate(R.layout.dialog_travel_expenses_realized, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setView(promptsView);
                final Spinner spinAccount = promptsView.findViewById(R.id.spinAccount);
                final Spinner spinCurrency = promptsView.findViewById(R.id.spinCurrency);
                final EditText etExpenseDate = promptsView.findViewById(R.id.etExpenseDate);
                final EditText etExpenseItemRealizedValue = promptsView.findViewById(R.id.etExpenseItemRealizedValue);
                final EditText etExpenseItemNote = promptsView.findViewById(R.id.etExpenseItemNote);

                final int[] nrSpCurrency = new int[1];
                final Integer[] nrSpinAccount = {travelItemExpenses.getAccount_id()};
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

                spinCurrency.setSelection(travelItemExpenses.getCurrency_id());

                etExpenseDate.setText(Utils.dateToString(travelItemExpenses.getExpense_date()));
                etExpenseItemRealizedValue.setText(String.valueOf(travelItemExpenses.getRealized_value()));
                etExpenseItemNote.setText(travelItemExpenses.getNote());

                etExpenseDate.addTextChangedListener(new DateInputMask(etExpenseDate));
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(R.string.OK, (dialog, id1) -> {
                            boolean isSave = true;
                            TravelItemExpenses tie = new TravelItemExpenses();
                            try {
                                tie.setId(travelItemExpenses.getId());
                                tie.setTravel_id(mTravel_id);
                                tie.setAccount_id(nrSpinAccount[0]);
                                tie.setCurrency_id(nrSpCurrency[0]);
                                tie.setExpense_type(mExpense_type);
                                tie.setExpense_date(Utils.stringToDate(etExpenseDate.getText().toString()));
                                if (!etExpenseItemRealizedValue.getText().toString().isEmpty()) {
                                    tie.setRealized_value(Double.parseDouble(etExpenseItemRealizedValue.getText().toString()));
                                }
                                tie.setNote(etExpenseItemNote.getText().toString());
                                isSave = Database.mTravelItemExpensesDao.updateTravelItemExpenses(tie);
                            } catch (Exception e) {
                                Toast.makeText(context, R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            if (!isSave) {
                                Toast.makeText(context, R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                            } else {
                                nPositionChanged = position;
                                mTravelItemExpenses.set(position - show_header, tie);
                                notifyItemRangeChanged(position - show_header, mTravelItemExpenses.size()+show_header+show_footer,mTravelItemExpenses);
                            }
                        })
                        .setNegativeButton(R.string.Cancel, (dialog, id1) -> dialog.cancel());
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            });
            // btnDelete
            itemViewHolder.btnDelete.setOnClickListener(v -> new AlertDialog.Builder(v.getContext())
                    .setTitle(R.string.TravelItemExpenses_Deleting)
                    .setMessage(R.string.Msg_Confirm)
                    .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                        try {
                            Database.mTravelItemExpensesDao.deleteTravelItemExpenses(travelItemExpenses.getId());
                            mTravelItemExpenses.remove(position - show_header);
                            notifyItemRemoved(position );
                            notifyItemRangeChanged(position, mTravelItemExpenses.size()+show_header+show_footer);
                        } catch (Exception e) {
                            Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).setNegativeButton(R.string.No, null)
                    .show());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && show_header == 1) return TYPE_HEADER;
        if (position == mTravelItemExpenses.size() + show_header && show_footer == 1) return TYPE_FOOTER;
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() { return mTravelItemExpenses.size() + show_header + show_footer; }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout llTravelExpenses;
        private final TextView txtAccount;
        private final TextView txtExpenseDate;
        private final TextView txtRealizedValue;
        private final TextView txtNote;
        private final ImageButton btnAdd;

        public HeaderViewHolder(View v) {
            super(v);
            llTravelExpenses = v.findViewById(R.id.llTravelExpenses);
            txtAccount = v.findViewById(R.id.txtAccount);
            txtExpenseDate = v.findViewById(R.id.txtExpenseDate);
            txtRealizedValue = v.findViewById(R.id.txtRealizedValue);
            txtNote = v.findViewById(R.id.txtNote);
            btnAdd = v.findViewById(R.id.btnDelete);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout llTravelExpenses;
        private final TextView txtAccount;
        private final TextView txtExpenseDate;
        private final TextView txtRealizedValue;
        private final TextView txtNote;
        private final ImageButton btnDelete;

        public ItemViewHolder(View v) {
            super(v);
            llTravelExpenses = v.findViewById(R.id.llTravelExpenses);
            txtAccount = v.findViewById(R.id.txtAccount);
            txtExpenseDate = v.findViewById(R.id.txtExpenseDate);
            txtRealizedValue = v.findViewById(R.id.txtRealizedValue);
            txtNote = v.findViewById(R.id.txtNote);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout llTravelExpenses;
        private final TextView txtAccount;
        private final TextView txtExpenseDate;
        private final TextView txtRealizedValue;
        private final TextView txtNote;
        private final ImageButton btnDelete;

        public FooterViewHolder(View v) {
            super(v);
            llTravelExpenses = v.findViewById(R.id.llTravelExpenses);
            txtAccount = v.findViewById(R.id.txtAccount);
            txtExpenseDate = v.findViewById(R.id.txtExpenseDate);
            txtRealizedValue = v.findViewById(R.id.txtRealizedValue);
            txtNote = v.findViewById(R.id.txtNote);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}