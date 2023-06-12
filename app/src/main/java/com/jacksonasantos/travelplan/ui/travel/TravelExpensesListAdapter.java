package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.jacksonasantos.travelplan.dao.TravelExpenses;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TravelExpensesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private final List<TravelExpenses> mTravelExpenses;
    final Context context;
    final int show_header;
    final int show_footer;
    final Integer travel_id;
    final boolean isHome;

    final Globals g = Globals.getInstance();
    final Locale locale = new Locale(g.getLanguage(), g.getCountry());
    final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
    Double vExpectedValue = 0.0;

    public TravelExpensesListAdapter(Integer travel_id, List<TravelExpenses> travelExpenses, Context context, int show_header, int show_footer, Boolean isHome) {
        this.mTravelExpenses = travelExpenses;
        this.context = context;
        this.show_header = show_header>=1?1:0;
        this.show_footer = show_footer>=1?1:0;
        this.travel_id = travel_id;
        this.isHome = isHome;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View travelExpensesView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_travel_expenses, parent, false);
        if (viewType == TYPE_HEADER) return new HeaderViewHolder(travelExpensesView);
        else if (viewType == TYPE_FOOTER) return new FooterViewHolder(travelExpensesView);
        else return new ItemViewHolder(travelExpensesView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.llItemTravelExpense.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            if (isHome) headerViewHolder.txtExpenseType.setVisibility(View.GONE);
            else headerViewHolder.txtExpenseType.setText(R.string.TravelExpenses_ExpenseType);
            headerViewHolder.txtExpectedValue.setText(R.string.TravelExpenses_ExpectedValue);
            headerViewHolder.txtNote.setText(R.string.TravelExpenses_Note);
            headerViewHolder.btnAddExpenses.setImageResource(R.drawable.ic_button_add);
            headerViewHolder.btnEdit.setVisibility(View.INVISIBLE);
            headerViewHolder.btnAddExpenses.setOnClickListener(v -> {
                LayoutInflater li = LayoutInflater.from(v.getContext());
                View promptsView = li.inflate(R.layout.dialog_travel_expenses, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setView(promptsView);
                final Spinner spinExpenseType = promptsView.findViewById(R.id.spinExpenseType);
                final EditText etExpectedValue = promptsView.findViewById(R.id.etExpectedValue);
                final EditText etNote = promptsView.findViewById(R.id.etNote);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(R.string.OK, (dialog, id) -> {
                            boolean isSave = false;
                            TravelExpenses TE = new TravelExpenses();
                            TE.setTravel_id(travel_id);
                            TE.setMarker_id(null);
                            TE.setExpense_type(spinExpenseType.getSelectedItemPosition());
                            if (!etExpectedValue.getText().toString().isEmpty()) {
                                TE.setExpected_value(Double.parseDouble(etExpectedValue.getText().toString()));
                            }
                            TE.setNote(etNote.getText().toString());

                            try {
                                isSave = Database.mTravelExpensesDao.addTravelExpenses(TE);
                            } catch (Exception e) {
                                Toast.makeText(context, R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            if (!isSave) {
                                Toast.makeText(context, R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                            } else {
                                mTravelExpenses.add(TE);
                                notifyItemInserted(mTravelExpenses.size());
                            }
                        })
                        .setNegativeButton(R.string.Cancel, (dialog, id) -> dialog.cancel());
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            });
        }
        else if (holder instanceof FooterViewHolder) {
            final FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.llItemTravelExpense.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            footerViewHolder.txtExpenseType.setVisibility(View.GONE);
            footerViewHolder.txtExpectedValue.setText(currencyFormatter.format(vExpectedValue));
            footerViewHolder.txtNote.setText("");
            footerViewHolder.btnEdit.setVisibility(View.INVISIBLE);
            footerViewHolder.btnDelete.setVisibility(View.INVISIBLE);
        }
        else if (holder instanceof ItemViewHolder) {
                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                final TravelExpenses travelExpenses = mTravelExpenses.get(position-show_header);
            if (isHome) itemViewHolder.txtExpenseType.setVisibility(View.GONE);
            else itemViewHolder.txtExpenseType.setText(context.getResources().getStringArray(R.array.expenses_type_array)[travelExpenses.getExpense_type()]);
            itemViewHolder.txtExpectedValue.setText(currencyFormatter.format(travelExpenses.getExpected_value()));
            vExpectedValue += travelExpenses.getExpected_value();
            itemViewHolder.txtNote.setText(travelExpenses.getNote());
            itemViewHolder.btnEdit.setOnClickListener(v -> {
                LayoutInflater li = LayoutInflater.from(v.getContext());
                View promptsView = li.inflate(R.layout.dialog_travel_expenses, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

                alertDialogBuilder.setView(promptsView);
                final Spinner spinExpenseType = promptsView.findViewById(R.id.spinExpenseType);
                final EditText etExpectedValue = promptsView.findViewById(R.id.etExpectedValue);
                final EditText etNote = promptsView.findViewById(R.id.etNote);
                TravelExpenses TE = Database.mTravelExpensesDao.fetchTravelExpensesById(travelExpenses.getId());
                spinExpenseType.setSelection(TE.getExpense_type());
                etExpectedValue.setText(TE.getExpected_value().toString());
                etNote.setText(TE.getNote());

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(R.string.OK, (dialog, id) -> {
                            boolean isSave = false;

                            TE.setTravel_id(travel_id);
                            TE.setMarker_id(TE.getMarker_id()==0?null:TE.getMarker_id());
                            TE.setExpense_type(spinExpenseType.getSelectedItemPosition());
                            if (!etExpectedValue.getText().toString().isEmpty()) {
                                TE.setExpected_value(Double.parseDouble(etExpectedValue.getText().toString()));
                            }
                            TE.setNote(etNote.getText().toString());

                            try {
                                isSave = Database.mTravelExpensesDao.updateTravelExpenses(TE);
                            } catch (Exception e) {
                                Toast.makeText(context, R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            if (!isSave) {
                                Toast.makeText(context, R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                            } else {
                                mTravelExpenses.set(position-show_header, TE);
                                notifyItemRangeChanged(position-show_header, mTravelExpenses.size());
                            }
                        })
                        .setNegativeButton(R.string.Cancel, (dialog, id) -> dialog.cancel());
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            });
            itemViewHolder.btnDelete.setOnClickListener (v -> new AlertDialog.Builder(v.getContext())
                    .setTitle(R.string.Travel_Expenses_Deleting)
                    .setMessage(R.string.Msg_Confirm)
                    .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                        try {
                            Database.mTravelExpensesDao.deleteTravelExpenses(travelExpenses.getId());
                            mTravelExpenses.remove(position-show_header);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position , mTravelExpenses.size());
                        } catch (Exception e) {
                            Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).setNegativeButton(R.string.No, null)
                    .show()
            );
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0 && show_header == 1) {
            return TYPE_HEADER;
        } if (position == mTravelExpenses.size()+show_header && show_footer == 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }
    @Override
    public int getItemCount() {
        return mTravelExpenses.size()+show_header+show_footer;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llItemTravelExpense;
        public final TextView txtExpenseType;
        public final TextView txtExpectedValue;
        public final TextView txtNote;
        public final ImageButton btnAddExpenses;
        public final ImageButton btnEdit;

        public HeaderViewHolder(View v) {
            super(v);
            llItemTravelExpense = v.findViewById(R.id.llItemTravelExpense);
            txtExpenseType = v.findViewById(R.id.txtExpenseType);
            txtExpectedValue = v.findViewById(R.id.txtExpectedValue);
            txtNote = v.findViewById(R.id.txtNote);
            btnAddExpenses = v.findViewById(R.id.btnDelete);
            btnEdit = v.findViewById(R.id.btnEdit);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final TextView txtExpenseType;
        public final TextView txtExpectedValue;
        public final TextView txtNote;
        public final ImageButton btnEdit;
        public final ImageButton btnDelete;

        public ItemViewHolder(View v) {
            super(v);
            txtExpenseType = v.findViewById(R.id.txtExpenseType);
            txtExpectedValue = v.findViewById(R.id.txtExpectedValue);
            txtNote = v.findViewById(R.id.txtNote);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llItemTravelExpense;
        public final TextView txtExpenseType;
        public final TextView txtExpectedValue;
        public final TextView txtNote;
        public final ImageButton btnEdit;
        public final ImageButton btnDelete;

        public FooterViewHolder(View v) {
            super(v);
            llItemTravelExpense = v.findViewById(R.id.llItemTravelExpense);
            txtExpenseType = v.findViewById(R.id.txtExpenseType);
            txtExpectedValue = v.findViewById(R.id.txtExpectedValue);
            txtNote = v.findViewById(R.id.txtNote);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}