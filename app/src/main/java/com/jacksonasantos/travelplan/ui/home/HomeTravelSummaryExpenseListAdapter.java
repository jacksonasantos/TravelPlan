package com.jacksonasantos.travelplan.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.SummaryTravelExpense;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HomeTravelSummaryExpenseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private Double vTotExpected = (double) 0;
    private Double vTotRealized = (double) 0;

    public final List<SummaryTravelExpense> mSummaryTravelExpense;
    final Context context;
    final int show_header;
    final int show_footer;

    final Globals g = Globals.getInstance();
    final Locale locale = new Locale(g.getLanguage(), g.getCountry());
    final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    public HomeTravelSummaryExpenseListAdapter(List<SummaryTravelExpense> summaryTravelExpense, Context context,  int show_header, int show_footer) {
        this.mSummaryTravelExpense = summaryTravelExpense;
        this.context = context;
        this.show_header = show_header>=1?1:0;
        this.show_footer = show_footer>=1?1:0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View expenseView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_home_travel_item_expense, parent, false);

        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(expenseView);
        } else if (viewType == TYPE_FOOTER) {
          return new FooterViewHolder(expenseView);
        }
        else return new ItemViewHolder(expenseView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = ( HeaderViewHolder) holder;
            headerViewHolder.llExpenseItem.setBackgroundColor(Color.LTGRAY);
            headerViewHolder.imgExpense.setVisibility(View.INVISIBLE);
            headerViewHolder.txtExpense.setText("");
            headerViewHolder.txtExpectedValue.setText(R.string.expected);
            headerViewHolder.txtRealizedValue.setText(R.string.realized);
        }
        else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = ( FooterViewHolder ) holder;
            footerViewHolder.llExpenseItem.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.7f));
            footerViewHolder.imgExpense.setVisibility(View.INVISIBLE);
            footerViewHolder.txtExpense.setText(R.string.HomeTravelTotal);
            footerViewHolder.txtExpectedValue.setText(currencyFormatter.format(vTotExpected==null? BigDecimal.ZERO: vTotExpected));
            footerViewHolder.txtRealizedValue.setText(currencyFormatter.format(vTotRealized==null? BigDecimal.ZERO: vTotRealized));
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final SummaryTravelExpense summaryTravelExpense = mSummaryTravelExpense.get(position-show_header);
            itemViewHolder.llExpenseItem.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            itemViewHolder.imgExpense.setImageResource(summaryTravelExpense.getExpense_type_image(summaryTravelExpense.getExpense_type()));
            itemViewHolder.txtExpense.setText(context.getResources().getStringArray(R.array.expenses_type_array)[summaryTravelExpense.getExpense_type()]);
            itemViewHolder.txtExpectedValue.setText(currencyFormatter.format(summaryTravelExpense.getExpected_value()==null? BigDecimal.ZERO: summaryTravelExpense.getExpected_value()));
            itemViewHolder.txtRealizedValue.setText(currencyFormatter.format(summaryTravelExpense.getRealized_value()==null? BigDecimal.ZERO: summaryTravelExpense.getRealized_value()));
            vTotExpected += summaryTravelExpense.getExpected_value();
            vTotRealized += summaryTravelExpense.getRealized_value();
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0 && show_header == 1) {
            return TYPE_HEADER;
        } if (position == mSummaryTravelExpense.size()+show_header && show_footer == 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mSummaryTravelExpense.size()+show_header+show_footer;
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

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout llExpenseItem;
        private final ImageView imgExpense;
        private final TextView txtExpense;
        private final TextView txtExpectedValue;
        private final TextView txtRealizedValue;

        public FooterViewHolder(View v) {
            super(v);
            llExpenseItem = v.findViewById(R.id.llExpenseItem);
            imgExpense = v.findViewById(R.id.imgExpense);
            txtExpense = v.findViewById(R.id.txtExpense);
            txtExpectedValue = v.findViewById(R.id.txtExpectedValue);
            txtRealizedValue = v.findViewById(R.id.txtRealizedValue);
        }
    }
}