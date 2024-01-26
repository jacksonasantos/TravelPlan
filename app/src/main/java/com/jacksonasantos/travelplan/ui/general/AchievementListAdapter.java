package com.jacksonasantos.travelplan.ui.general;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Achievement;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;
import java.util.Objects;

public class AchievementListAdapter extends RecyclerView.Adapter<AchievementListAdapter.MyViewHolder> {

    private final List<Achievement> mAchievement;
    final Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ConstraintLayout llAchievement;
        public final ImageView imgAchievement;
        public final ImageView imgTypeAchievement;
        public final TextView txtShortNameAchievement;
        public final ImageView imgFlagCountry;
        public final ImageView imgFlagState;
        public final TextView txtNameAchievement;
        public final ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            llAchievement = v.findViewById(R.id.llAchievement);
            imgAchievement = v.findViewById(R.id.imgAchievement);
            imgTypeAchievement = v.findViewById(R.id.imgTypeAchievement);
            txtShortNameAchievement = v.findViewById(R.id.txtShortNameAchievement);
            imgFlagCountry = v.findViewById(R.id.imgFlagCountry);
            imgFlagState = v.findViewById(R.id.imgFlagState);
            txtNameAchievement = v.findViewById(R.id.txtNameAchievement);
            btnDelete = v.findViewById(R.id.btnDelete);
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

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Achievement achievement = mAchievement.get(position);

        byte[] imgArray = achievement.getImage();
        if (imgArray!=null){
            Bitmap raw = BitmapFactory.decodeByteArray(imgArray, 0, imgArray.length);
            holder.imgAchievement.setImageBitmap(raw);
            if (achievement.getStatus_achievement() == 1){
                holder.imgAchievement.setAlpha(1f);
                holder.txtNameAchievement.setTextColor(Color.BLACK);
                holder.txtShortNameAchievement.setTextColor(Color.BLACK);
            } else {
                holder.imgAchievement.setAlpha(0.5f);
                holder.txtNameAchievement.setTextColor(Color.LTGRAY);
                holder.txtShortNameAchievement.setTextColor(Color.LTGRAY);
            }
        }
        String vState;
        if (!Objects.equals(achievement.getState(), achievement.getState_end()) && Objects.equals(achievement.getState_end(), "")) {
            vState = achievement.getState();
        } else {
            if(!Objects.equals(achievement.getState(), achievement.getState_end())){
                vState="--";
            } else {
                vState= achievement.getState();
            }
        }
        holder.imgFlagCountry.setBackgroundResource(Utils.getFlagResource(achievement.getCountry(), ""));
        holder.imgFlagState.setBackgroundResource(Utils.getFlagResource(achievement.getCountry(), vState));
        holder.imgTypeAchievement.setImageResource(achievement.getAchievement_typeImage(achievement.getType_achievement()));
        holder.txtShortNameAchievement.setText(achievement.getShort_name());
        holder.txtNameAchievement.setText(achievement.getName());
        // btnEdit
        holder.llAchievement.setOnClickListener (v -> {
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