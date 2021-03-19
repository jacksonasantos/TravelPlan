package com.jacksonasantos.travelplan.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class HomeTravelSummaryExpenseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public final List<SummaryTravelExpense> mSummaryTravelExpense;
    Context context;
    Integer travel_id;
    int show_header;

    Globals g = Globals.getInstance();
    Locale locale = new Locale(g.getLanguage(), g.getCountry());
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    public HomeTravelSummaryExpenseListAdapter(List<SummaryTravelExpense> summaryTravelExpense, Context context, Integer travel_id, int show_header) {
        this.mSummaryTravelExpense = summaryTravelExpense;
        this.context = context;
        this.travel_id = travel_id;
        this.show_header = show_header;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View expenseView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_home_travel_item_expense, parent, false);

        if (viewType == TYPE_ITEM) {
            return new ItemViewHolder(expenseView);
        } else if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(expenseView);
        }
        else return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = ( HeaderViewHolder) holder;

            headerViewHolder.llExpenseItem.setBackgroundColor(Color.LTGRAY);
            headerViewHolder.imgExpense.setVisibility(View.INVISIBLE);
            headerViewHolder.txtExpense.setText("");
            headerViewHolder.txtExpectedValue.setText(R.string.Expected);
            headerViewHolder.txtRealizedValue.setText(R.string.Realized);
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final SummaryTravelExpense summaryTravelExpense = mSummaryTravelExpense.get(position-show_header);

            itemViewHolder.imgExpense.setImageResource(summaryTravelExpense.getExpense_type_image(summaryTravelExpense.getExpense_type()));
            itemViewHolder.txtExpense.setText(context.getResources().getStringArray(R.array.expenses_type_array)[summaryTravelExpense.getExpense_type()]);
            itemViewHolder.txtExpectedValue.setText(currencyFormatter.format(summaryTravelExpense.getExpected_value()==null? BigDecimal.ZERO: summaryTravelExpense.getExpected_value()));
            itemViewHolder.txtRealizedValue.setText(currencyFormatter.format(summaryTravelExpense.getRealized_value()==null? BigDecimal.ZERO: summaryTravelExpense.getRealized_value()));
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
        return mSummaryTravelExpense.size()+show_header;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout llExpenseItem;
        private final ImageView imgExpense;
        private final TextView txtExpense;
        private final TextView txtExpectedValue;
        private final TextView txtRealizedValue;

        public HeaderViewHolder(View v) {
            super(v);
            llExpenseItem = v.findViewById(R.id.llExpenseItem);
            imgExpense = v.findViewById(R.id.imgExpense);
            txtExpense = v.findViewById(R.id.txtExpense);
            txtExpectedValue = v.findViewById(R.id.txtExpectedValue);
            txtRealizedValue = v.findViewById(R.id.txtRealizedValue);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout llExpenseItem;
        private final ImageView imgExpense;
        private final TextView txtExpense;
        private final TextView txtExpectedValue;
        private final TextView txtRealizedValue;

        public ItemViewHolder(View v) {
            super(v);
            llExpenseItem = v.findViewById(R.id.llExpenseItem);
            imgExpense = v.findViewById(R.id.imgExpense);
            txtExpense = v.findViewById(R.id.txtExpense);
            txtExpectedValue = v.findViewById(R.id.txtExpectedValue);
            txtRealizedValue = v.findViewById(R.id.txtRealizedValue);
        }
    }
}