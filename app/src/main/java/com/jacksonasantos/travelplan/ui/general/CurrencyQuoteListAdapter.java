package com.jacksonasantos.travelplan.ui.general;

import android.content.Context;
import android.content.Intent;
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
import com.jacksonasantos.travelplan.dao.CurrencyQuote;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class CurrencyQuoteListAdapter extends RecyclerView.Adapter<CurrencyQuoteListAdapter.MyViewHolder> {

    private final List<CurrencyQuote> mCurrencyQuote;
    Context context;
    String[] currencyArray;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtCurrency;
        public TextView txtDate;
        public TextView txtQuote;
        public ImageButton btnEdit;
        public ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            txtCurrency = v.findViewById(R.id.txtCurrency);
            txtDate = v.findViewById(R.id.txtDate);
            txtQuote = v.findViewById(R.id.txtQuote);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public CurrencyQuoteListAdapter(List<CurrencyQuote> currencyQuote, Context context) {
        this.mCurrencyQuote = currencyQuote;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public CurrencyQuoteListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View currencyQuoteView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_currency_quote, parent, false);
        currencyArray = parent.getResources().getStringArray(R.array.currency_array);

        return new MyViewHolder(currencyQuoteView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final CurrencyQuote currencyQuote = mCurrencyQuote.get(position);

        holder.txtCurrency.setText(currencyArray[currencyQuote.getCurrency_type()]);
        holder.txtDate.setText(Utils.dateToString(currencyQuote.getQuote_date()));
        //NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        holder.txtQuote.setText(String.valueOf(currencyQuote.getCurrency_value()));

        // btnEdit
        holder.btnEdit.setOnClickListener (v -> {
            Intent intent = new Intent (v.getContext(), CurrencyQuoteActivity.class);
            intent.putExtra("currencyQuote_id", currencyQuote.getId());
            context.startActivity(intent);
            notifyDataSetChanged();
        });

        // btnDelete
        holder.btnDelete.setOnClickListener (v -> new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.Currency_Quote_Deleting)
                .setMessage(R.string.Msg_Confirm)
                .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                    try {
                        Database.mCurrencyQuoteDao.deleteCurrencyQuote(currencyQuote.getId());
                        mCurrencyQuote.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mCurrencyQuote.size());
                    } catch (Exception e) {
                        Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton(R.string.No, null)
                .show());
    }

    @Override
    public int getItemCount() {
        return mCurrencyQuote.size();
    }
}