package com.jacksonasantos.travelplan.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

public class HomeTravelSummaryExpenseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private Double vTotExpected = (double) 0;
    private Double vTotRealized = (double) 0;
    private int vCurrency = 0;
    private boolean vUniqueCurrency = true;

    public final List<SummaryTravelExpense> mSummaryTravelExpense;
    final Context context;
    final int show_header;
    final int show_footer;

    final NumberFormat numberFormatter = NumberFormat.getNumberInstance();

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

        numberFormatter.setMaximumFractionDigits(2);
        numberFormatter.setMinimumFractionDigits(2);

        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(expenseView);
        } else if (viewType == TYPE_FOOTER) {
            return new FooterViewHolder(expenseView);
        }
        else return new ItemViewHolder(expenseView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = ( HeaderViewHolder) holder;
            headerViewHolder.llExpenseItem.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            headerViewHolder.imgExpense.setVisibility(View.INVISIBLE);
            headerViewHolder.txtExpense.setText("");
            headerViewHolder.txtCurrencyType.setText("");
            headerViewHolder.txtExpectedValue.setText(R.string.expected);
            headerViewHolder.txtRealizedValue.setText(R.string.realized);
        }
        else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = ( FooterViewHolder ) holder;
            footerViewHolder.llExpenseItem.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.5f));
            footerViewHolder.imgExpense.setVisibility(View.INVISIBLE);
            footerViewHolder.txtExpense.setText(R.string.HomeTravelTotal);
            footerViewHolder.txtCurrencyType.setText(context.getResources().getStringArray(R.array.currency_array)[vCurrency]);
            if (!vUniqueCurrency) {
                footerViewHolder.txtCurrencyType.setText(footerViewHolder.txtCurrencyType.getText()+" *");
            }
            footerViewHolder.txtExpectedValue.setText(numberFormatter.format(vTotExpected==null? BigDecimal.ZERO: vTotExpected));
            footerViewHolder.txtRealizedValue.setText(numberFormatter.format(vTotRealized==null? BigDecimal.ZERO: vTotRealized));
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final SummaryTravelExpense summaryTravelExpense = mSummaryTravelExpense.get(position-show_header);
            if(position%2==0) {
                itemViewHolder.llExpenseItem.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList, 0.05f));
            }
            if (summaryTravelExpense.getCurrency_type() != vCurrency) {
                vUniqueCurrency = false;
            }
            vCurrency = summaryTravelExpense.getCurrency_type();
            itemViewHolder.imgExpense.setImageResource(summaryTravelExpense.getExpense_type_image(summaryTravelExpense.getExpense_type()));
            itemViewHolder.txtExpense.setText(context.getResources().getStringArray(R.array.expenses_type_array)[summaryTravelExpense.getExpense_type()]);
            itemViewHolder.txtCurrencyType.setText(context.getResources().getStringArray(R.array.currency_array)[summaryTravelExpense.getCurrency_type()]);
            itemViewHolder.txtExpectedValue.setText(numberFormatter.format(summaryTravelExpense.getExpected_value()==null? BigDecimal.ZERO: summaryTravelExpense.getExpected_value()));
            itemViewHolder.txtRealizedValue.setText(numberFormatter.format(summaryTravelExpense.getRealized_value()==null? BigDecimal.ZERO: summaryTravelExpense.getRealized_value()));
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
        private final TextView txtCurrencyType;
        private final TextView txtExpectedValue;
        private final TextView txtRealizedValue;

        public HeaderViewHolder(View v) {
            super(v);
            llExpenseItem = v.findViewById(R.id.llExpenseItem);
            imgExpense = v.findViewById(R.id.imgExpense);
            txtExpense = v.findViewById(R.id.txtExpense);
            txtCurrencyType = v.findViewById(R.id.txtCurrencyType);
            txtExpectedValue = v.findViewById(R.id.txtExpectedValue);
            txtRealizedValue = v.findViewById(R.id.txtRealizedValue);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout llExpenseItem;
        private final ImageView imgExpense;
        private final TextView txtExpense;
        private final TextView txtCurrencyType;
        private final TextView txtExpectedValue;
        private final TextView txtRealizedValue;

        public ItemViewHolder(View v) {
            super(v);
            llExpenseItem = v.findViewById(R.id.llExpenseItem);
            imgExpense = v.findViewById(R.id.imgExpense);
            txtExpense = v.findViewById(R.id.txtExpense);
            txtCurrencyType = v.findViewById(R.id.txtCurrencyType);
            txtExpectedValue = v.findViewById(R.id.txtExpectedValue);
            txtRealizedValue = v.findViewById(R.id.txtRealizedValue);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout llExpenseItem;
        private final ImageView imgExpense;
        private final TextView txtExpense;
        private final TextView txtCurrencyType;
        private final TextView txtExpectedValue;
        private final TextView txtRealizedValue;

        public FooterViewHolder(View v) {
            super(v);
            llExpenseItem = v.findViewById(R.id.llExpenseItem);
            imgExpense = v.findViewById(R.id.imgExpense);
            txtExpense = v.findViewById(R.id.txtExpense);
            txtCurrencyType = v.findViewById(R.id.txtCurrencyType);
            txtExpectedValue = v.findViewById(R.id.txtExpectedValue);
            txtRealizedValue = v.findViewById(R.id.txtRealizedValue);
        }
    }
}