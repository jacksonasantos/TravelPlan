package com.jacksonasantos.travelplan.ui.general;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Account;
import com.jacksonasantos.travelplan.dao.general.Database;

import java.util.List;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.MyViewHolder> {

    private final List<Account> mAccount;
    final Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final LinearLayout llAccount;
        public final TextView txtDescription;
        public final TextView txtTypeAccount;
        public final ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            llAccount = v.findViewById(R.id.llAccount);
            txtDescription = v.findViewById(R.id.txtDescription);
            txtTypeAccount = v.findViewById(R.id.txtTypeAccount);
            btnDelete = v.findViewById(R.id.btnDelete);
            
            llAccount.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public AccountListAdapter(List<Account> account, Context context) {
        this.mAccount = account;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public AccountListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View accountView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_account, parent, false);

        return new MyViewHolder(accountView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Account account = mAccount.get(position);

        holder.txtDescription.setText(account.getDescription());
        holder.txtTypeAccount.setText(context.getResources().getStringArray(R.array.account_type_array)[account.getAccount_type()]);
        // llAccount
        holder.llAccount.setOnClickListener (v -> {
            Intent intent = new Intent (v.getContext(), AccountActivity.class);
            intent.putExtra("account_id", account.getId());
            context.startActivity(intent);
            notifyItemChanged(position);
        });

        // btnDelete
        holder.btnDelete.setOnClickListener (v -> new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.Account_Deleting)
                .setMessage(R.string.Msg_Confirm)
                .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                    try {
                        Database.mAccountDao.deleteAccount(account.getId());
                        mAccount.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mAccount.size());
                    } catch (Exception e) {
                        Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton(R.string.No, null)
                .show());
    }

    @Override
    public int getItemCount() {
        return mAccount.size();
    }
}