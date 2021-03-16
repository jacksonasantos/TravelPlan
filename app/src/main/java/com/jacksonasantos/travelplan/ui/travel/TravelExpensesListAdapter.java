package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import com.jacksonasantos.travelplan.dao.TravelExpenses;
import com.jacksonasantos.travelplan.ui.utility.Globals;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TravelExpensesListAdapter extends RecyclerView.Adapter<TravelExpensesListAdapter.MyViewHolder> {

    private final List<TravelExpenses> mTravelExpenses;
    Context context;
    public String form;

    Globals g = Globals.getInstance();
    Locale locale = new Locale(g.getLanguage(), g.getCountry());
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtExpenseType;
        public TextView txtExpectedValue;
        public TextView txtNote;
        public ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            txtExpenseType = v.findViewById(R.id.txtExpenseType);
            txtExpectedValue = v.findViewById(R.id.txtExpectedValue);
            txtNote = v.findViewById(R.id.txtNote);
            btnDelete = v.findViewById(R.id.btnDelete);

            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public TravelExpensesListAdapter(List<TravelExpenses> travelExpenses, Context context) {
        this.mTravelExpenses = travelExpenses;
        this.context = context;
    }

    @NonNull
    @Override
    public TravelExpensesListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View travelExpensesView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_travel_expenses, parent, false);
        return new MyViewHolder(travelExpensesView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final TravelExpenses travelExpenses = mTravelExpenses.get(position);

        holder.txtExpenseType.setText(context.getResources().getStringArray(R.array.expenses_type_array)[travelExpenses.getExpense_type()]);
        holder.txtExpectedValue.setText(currencyFormatter.format(travelExpenses.getExpected_value()));
        holder.txtNote.setText(travelExpenses.getNote());

        holder.btnDelete.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.Travel_Expenses_Deleting)
                        .setMessage(R.string.Msg_Confirm)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    Database.mTravelExpensesDao.deleteTravelExpenses(travelExpenses.getId());
                                    mTravelExpenses.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position,mTravelExpenses.size());
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
        return mTravelExpenses.size();
    }
}