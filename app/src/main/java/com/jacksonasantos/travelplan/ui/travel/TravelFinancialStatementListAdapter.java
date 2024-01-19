package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.SummaryTravelExpense;
import com.jacksonasantos.travelplan.dao.TravelItemExpenses;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TravelFinancialStatementListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<TravelItemExpenses> mTravelItemExpenses;
    final Context context;
    final int show_header;
    final Integer travel_id;
    final boolean isHome;
    double balance = 0.0;
    Integer last_account = 0;
    Integer last_currency = 0;

    final Globals g = Globals.getInstance();
    final Locale locale = new Locale(g.getLanguage(), g.getCountry());
    final NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);

    public TravelFinancialStatementListAdapter(Integer travel_id, List<TravelItemExpenses> travelItemExpenses, Context context, int show_header, Boolean isHome) {
        this.mTravelItemExpenses = travelItemExpenses;
        this.context = context;
        this.show_header = show_header>=1?1:0;
        this.travel_id = travel_id;
        this.isHome = isHome;
        numberFormatter.setMinimumFractionDigits(2);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View travelItemExpensesView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_travel_financial_statement, parent, false);
        if (viewType == TYPE_HEADER) return new HeaderViewHolder(travelItemExpensesView);
        else return new ItemViewHolder(travelItemExpensesView);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.llTravelFinancialStatement.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            headerViewHolder.txtAccount.setText(R.string.TravelItemExpenses_Account);
            headerViewHolder.txtCurrency.setText(R.string.TravelItemExpenses_Currency);
            headerViewHolder.txtExpenseDate.setText(R.string.TravelItemExpenses_ExpenseDate);
            headerViewHolder.txtRealizedValue.setText(R.string.TravelItemExpenses_RealizedValue);
            headerViewHolder.txtRealizedBalance.setText(R.string.TravelItemExpenses_RealizedBalance);
            headerViewHolder.txtNote.setText(R.string.TravelItemExpenses_Note);
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final TravelItemExpenses travelItemExpenses = mTravelItemExpenses.get(position-show_header);
            final SummaryTravelExpense summaryTravelExpense = new SummaryTravelExpense();
            if (!Objects.equals(last_account, travelItemExpenses.getAccount_id()) ||
                !Objects.equals(last_currency, travelItemExpenses.getCurrency_id()) ) {
                last_account = travelItemExpenses.getAccount_id();
                last_currency = travelItemExpenses.getCurrency_id();
                balance = travelItemExpenses.getRealized_value();

                itemViewHolder.llTravelFinancialStatement.setPadding(0,20,0,0);

            } else {
                balance += travelItemExpenses.getRealized_value();
            }
            itemViewHolder.txtAccount.setText(Database.mAccountDao.fetchAccountById(travelItemExpenses.getAccount_id()).getDescription());
            itemViewHolder.txtCurrency.setText(context.getResources().getStringArray(R.array.currency_array)[travelItemExpenses.getCurrency_id()]);
            itemViewHolder.txtExpenseDate.setText(Utils.dateToString(travelItemExpenses.getExpense_date()));
            itemViewHolder.imgType.setImageResource(summaryTravelExpense.getExpense_type_image(travelItemExpenses.getExpense_type()));
            if (travelItemExpenses.getRealized_value()<0) {
                itemViewHolder.txtRealizedValue.setTextColor(Color.RED);
            } else {
                itemViewHolder.txtRealizedValue.setTextColor(Color.GRAY);
            }
            itemViewHolder.txtRealizedValue.setText(numberFormatter.format(travelItemExpenses.getRealized_value()));
            if (balance<0) {
                itemViewHolder.txtRealizedBalance.setTextColor(Color.RED);
            } else {
                itemViewHolder.txtRealizedBalance.setTextColor(Color.GRAY);
            }
            itemViewHolder.txtRealizedBalance.setText(numberFormatter.format(balance));
            itemViewHolder.txtNote.setText(travelItemExpenses.getNote().trim());
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
        return mTravelItemExpenses.size()+show_header;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llTravelFinancialStatement;
        public final TextView txtAccount;
        public final TextView txtCurrency;
        public final ImageView imgType;
        public final TextView txtExpenseDate;
        public final TextView txtRealizedValue;
        public final TextView txtRealizedBalance;
        public final TextView txtNote;

        public HeaderViewHolder(View v) {
            super(v);
            llTravelFinancialStatement = v.findViewById(R.id.llTravelFinancialStatement);
            txtAccount = v.findViewById(R.id.txtAccount);
            txtCurrency = v.findViewById(R.id.txtCurrency);
            imgType = v.findViewById(R.id.imgType);
            txtExpenseDate = v.findViewById(R.id.txtExpenseDate);
            txtRealizedValue = v.findViewById(R.id.txtRealizedValue);
            txtRealizedBalance = v.findViewById(R.id.txtRealizedBalance);
            txtNote = v.findViewById(R.id.txtNote);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llTravelFinancialStatement;
        public final TextView txtAccount;
        public final TextView txtCurrency;
        public final ImageView imgType;
        public final TextView txtExpenseDate;
        public final TextView txtRealizedValue;
        public final TextView txtRealizedBalance;
        public final TextView txtNote;

        public ItemViewHolder(View v) {
            super(v);
            llTravelFinancialStatement = v.findViewById(R.id.llTravelFinancialStatement);
            txtAccount = v.findViewById(R.id.txtAccount);
            txtCurrency = v.findViewById(R.id.txtCurrency);
            imgType = v.findViewById(R.id.imgType);
            txtExpenseDate = v.findViewById(R.id.txtExpenseDate);
            txtRealizedValue = v.findViewById(R.id.txtRealizedValue);
            txtRealizedBalance = v.findViewById(R.id.txtRealizedBalance);
            txtNote = v.findViewById(R.id.txtNote);
        }
    }
}