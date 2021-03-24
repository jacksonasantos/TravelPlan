package com.jacksonasantos.travelplan.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.TravelItemExpenses;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HomeTravelItemExpensesListAdapter extends RecyclerView.Adapter<HomeTravelItemExpensesListAdapter.MyViewHolder> {

    public final List<TravelItemExpenses> mTravelItemExpenses;
    Context context;
    Integer travel_id;

    Globals g = Globals.getInstance();
    Locale locale = new Locale(g.getLanguage(), g.getCountry());
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    public HomeTravelItemExpensesListAdapter(List<TravelItemExpenses> travelItemExpenses, Context context, Integer travel_id) {
        this.mTravelItemExpenses = travelItemExpenses;
        this.context = context;
        this.travel_id = travel_id;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final LinearLayout llItemExpenses;
        private final TextView txtExpenseDate;
        private final TextView txtRealizedValue;
        private final TextView txtNote;
        private final ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            llItemExpenses = v.findViewById(R.id.llItemExpenses);
            txtExpenseDate = v.findViewById(R.id.txtExpenseDate);
            txtRealizedValue = v.findViewById(R.id.txtRealizedValue);
            txtNote = v.findViewById(R.id.txtNote);
            btnDelete = v.findViewById(R.id.btnDelete);
        }

        @Override
        public void onClick(View view) {
        }
    }

    @NonNull
    @Override
    public HomeTravelItemExpensesListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View expenseView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_travel_item_expenses, parent, false);
        return new MyViewHolder(expenseView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final TravelItemExpenses travelItemExpenses = mTravelItemExpenses.get(position);

        holder.txtExpenseDate.setText(Utils.dateToString(travelItemExpenses.getExpense_date()));
        holder.txtRealizedValue.setText(currencyFormatter.format(travelItemExpenses.getRealized_value()==null? BigDecimal.ZERO: travelItemExpenses.getRealized_value()));
        holder.txtNote.setText(travelItemExpenses.getNote());

        // btnDelete
        holder.btnDelete.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.TravelItemExpenses_Deleting)
                        .setMessage(R.string.Msg_Confirm)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    Database.mTravelItemExpensesDao.deleteTravelItemExpenses(travelItemExpenses.getId());
                                    mTravelItemExpenses.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position,mTravelItemExpenses.size());
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
        return mTravelItemExpenses.size();
    }
}