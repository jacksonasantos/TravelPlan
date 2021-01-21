package com.jacksonasantos.travelplan.ui.home;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.SummaryTravelExpense;
import com.jacksonasantos.travelplan.ui.utility.Globals;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HomeSummaryTravelExpenseListAdapter extends RecyclerView.Adapter<HomeSummaryTravelExpenseListAdapter.MyViewHolder> {

    public final List<SummaryTravelExpense> mSummaryTravelExpense;
    Context context;

    Globals g = Globals.getInstance();
    Locale locale = new Locale(g.getLanguage(), g.getCountry());
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    public HomeSummaryTravelExpenseListAdapter(List<SummaryTravelExpense> summaryTravelExpense, Context context) {
        this.mSummaryTravelExpense = summaryTravelExpense;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ConstraintLayout llExpenseItem;
        private final TextView txtExpense;
        private final TextView txtExpectedValue;
        private final TextView txtRealizedValue;

        public MyViewHolder(View v) {
            super(v);
            llExpenseItem = v.findViewById(R.id.llExpenseItem);
            txtExpense = v.findViewById(R.id.txtExpense);
            txtExpectedValue = v.findViewById(R.id.txtExpectedValue);
            txtRealizedValue = v.findViewById(R.id.txtRealizedValue);
        }

        @Override
        public void onClick(View view) {
        }
    }

    @NonNull
    @Override
    public HomeSummaryTravelExpenseListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View expenseView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_travel_item_expense, parent, false);
        return new MyViewHolder(expenseView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final SummaryTravelExpense summaryTravelExpense = mSummaryTravelExpense.get(position);
        holder.txtExpense.setText(summaryTravelExpense.getExpense());
        holder.txtExpectedValue.setText(currencyFormatter.format(summaryTravelExpense.getExpected_value()==null? BigDecimal.ZERO: summaryTravelExpense.getExpected_value()));
        holder.txtRealizedValue.setText(currencyFormatter.format(summaryTravelExpense.getRealized_value()==null? BigDecimal.ZERO: summaryTravelExpense.getRealized_value()));
    }

    @Override
    public int getItemCount() {
        return mSummaryTravelExpense.size();
    }
}