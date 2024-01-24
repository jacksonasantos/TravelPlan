package com.jacksonasantos.travelplan.ui.general;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.AchievementResume;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;
import java.util.Objects;

public class AchievementResumeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    final List<AchievementResume> mResume;
    final Context context;
    final int show_header;
    String vCountry = null;

    public AchievementResumeListAdapter(List<AchievementResume> resume, Context context, int show_header) {
        this.mResume = resume;
        this.context = context;
        this.show_header = show_header;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View resumeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_achievement_resume_item, parent, false);

        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(resumeView);
        } else return new ItemViewHolder(resumeView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.tvCountry.setVisibility(View.GONE);
            headerViewHolder.llResume.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            headerViewHolder.tvState.setText(R.string.Achievement_State);
            headerViewHolder.tvType0.setText(R.string.Achievement_noType);
            headerViewHolder.tvType1.setText(context.getResources().getStringArray(R.array.achievement_type_array)[0]);
            headerViewHolder.tvType2.setText(context.getResources().getStringArray(R.array.achievement_type_array)[1]);
            headerViewHolder.tvType3.setText(context.getResources().getStringArray(R.array.achievement_type_array)[2]);
            headerViewHolder.tvType4.setText(context.getResources().getStringArray(R.array.achievement_type_array)[3]);
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            AchievementResume list = mResume.get(position - show_header);
            if (!Objects.equals(vCountry, list.getCountry())) {
                itemViewHolder.tvCountry.setVisibility(View.VISIBLE);
                itemViewHolder.tvCountry.setText(list.getCountry());
                vCountry = list.getCountry();
            } else {
                itemViewHolder.tvCountry.setVisibility(View.GONE);
            }
            itemViewHolder.tvState.setText(list.getState());
            itemViewHolder.tvType0.setText(list.getVl_noType());
            itemViewHolder.tvType1.setText(list.getVl_mountainRange());
            itemViewHolder.tvType2.setText(list.getVl_road());
            itemViewHolder.tvType3.setText(list.getVl_cave());
            itemViewHolder.tvType4.setText(list.getVl_touristSpot());
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
        return mResume.size()+show_header;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvCountry;
        public final LinearLayout llResume;
        public final TextView tvState;
        public final TextView tvType0;
        public final TextView tvType1;
        public final TextView tvType2;
        public final TextView tvType3;
        public final TextView tvType4;

        public HeaderViewHolder(View v) {
            super(v);
            llResume = v.findViewById(R.id.llResume);
            tvCountry = v.findViewById(R.id.tvCountry);
            tvState = v.findViewById(R.id.tvState);
            tvType0 = v.findViewById(R.id.tvType0);
            tvType1 = v.findViewById(R.id.tvType1);
            tvType2 = v.findViewById(R.id.tvType2);
            tvType3 = v.findViewById(R.id.tvType3);
            tvType4 = v.findViewById(R.id.tvType4);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvCountry;
        public final LinearLayout llResume;
        public final TextView tvState;
        public final TextView tvType0;
        public final TextView tvType1;
        public final TextView tvType2;
        public final TextView tvType3;
        public final TextView tvType4;

        public ItemViewHolder(View v) {
            super(v);
            llResume = v.findViewById(R.id.llResume);
            tvCountry = v.findViewById(R.id.tvCountry);
            tvState = v.findViewById(R.id.tvState);
            tvType0 = v.findViewById(R.id.tvType0);
            tvType1 = v.findViewById(R.id.tvType1);
            tvType2 = v.findViewById(R.id.tvType2);
            tvType3 = v.findViewById(R.id.tvType3);
            tvType4 = v.findViewById(R.id.tvType4);
        }
    }
}