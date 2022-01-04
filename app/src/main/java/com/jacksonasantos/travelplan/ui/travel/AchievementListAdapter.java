package com.jacksonasantos.travelplan.ui.travel;

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
import com.jacksonasantos.travelplan.dao.Achievement;
import com.jacksonasantos.travelplan.dao.general.Database;

import java.util.List;

public class AchievementListAdapter extends RecyclerView.Adapter<AchievementListAdapter.MyViewHolder> {

    private final List<Achievement> mAchievement;
    final Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView txtNameAchievement;
        public final ImageButton btnEdit;
        public final ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            txtNameAchievement = v.findViewById(R.id.txtNameAchievement);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public AchievementListAdapter(List<Achievement> achievement, Context context) {
        this.mAchievement = achievement;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public AchievementListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View achievementView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_achievement, parent, false);

        return new MyViewHolder(achievementView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Achievement achievement = mAchievement.get(position);

        holder.txtNameAchievement.setText(achievement.getName());
        // btnEdit
        holder.btnEdit.setOnClickListener (v -> {
            Intent intent = new Intent (v.getContext(), AchievementActivity.class);
            intent.putExtra("achievement_id", achievement.getId());
            context.startActivity(intent);
            notifyItemChanged(position);
        });

        // btnDelete
        holder.btnDelete.setOnClickListener (v -> new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.Achievement_Deleting)
                .setMessage(R.string.Msg_Confirm)
                .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                    try {
                        Database.mAchievementDao.deleteAchievement(achievement.getId());
                        mAchievement.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mAchievement.size());
                    } catch (Exception e) {
                        Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton(R.string.No, null)
                .show());
    }

    @Override
    public int getItemCount() {
        return mAchievement.size();
    }
}