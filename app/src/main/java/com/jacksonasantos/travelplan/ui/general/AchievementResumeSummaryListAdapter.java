package com.jacksonasantos.travelplan.ui.general;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Achievement;
import com.jacksonasantos.travelplan.dao.AchievementResume;

import java.util.List;

public class AchievementResumeSummaryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;

    final List<AchievementResume> mResume;
    final Context context;

    final Achievement achievement = new Achievement();

    public AchievementResumeSummaryListAdapter(List<AchievementResume> resume, Context context) {
        this.mResume = resume;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View resumeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_achievement_resume_summary_item, parent, false);
        return new ItemViewHolder(resumeView);
    }

    @SuppressLint({"DefaultLocale", "UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            AchievementResume list = mResume.get(position);
            itemViewHolder.imgTypeAchievement.setImageResource(achievement.getAchievement_typeImage(list.getType_achievement()));
            itemViewHolder.txtTypeAchievement.setText(context.getResources().getStringArray(R.array.achievement_type_array)[list.getType_achievement()-1]);
            itemViewHolder.txtVlConquered.setText(String.valueOf(list.getVl_conquered()));
            itemViewHolder.txtVlTotal.setText(String.valueOf(list.getVl_total()));
            itemViewHolder.txtPercConquered.setText((int) list.getPerc_conquered() +" %");
            itemViewHolder.progress_bar.setProgress((int)list.getPerc_conquered());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mResume.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imgTypeAchievement;
        public final TextView txtTypeAchievement;
        public final TextView txtVlConquered;
        public final TextView txtVlTotal;
        public final TextView txtPercConquered;
        public final ProgressBar progress_bar;

        public ItemViewHolder(View v) {
            super(v);
            imgTypeAchievement = v.findViewById(R.id.imgTypeAchievement);
            txtTypeAchievement = v.findViewById(R.id.txtTypeAchievement);
            txtVlConquered = v.findViewById(R.id.txtVlConquered);
            txtVlTotal = v.findViewById(R.id.txtVlTotal);
            txtPercConquered = v.findViewById(R.id.txtPercConquered);
            progress_bar = v.findViewById(R.id.progress_bar);
        }
    }
}