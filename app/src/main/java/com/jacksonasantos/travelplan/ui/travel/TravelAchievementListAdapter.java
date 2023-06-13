package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Achievement;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class TravelAchievementListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<Achievement> mAchievement;
    final Context context;
    final int show_header;
    final Integer mTravel_id;
    public final String form;

    public TravelAchievementListAdapter(List<Achievement> achievements, Context context, String form, int show_header, Integer travel_id) {
        this.mAchievement = achievements;
        this.mTravel_id = travel_id;
        this.context = context;
        this.form = form;
        this.show_header = show_header; // 0 - NO SHOW HEADER | 1 - SHOW HEADER
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View achievementView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_travel_achievement, parent, false);

        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(achievementView);
        } else return new ItemViewHolder(achievementView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.llAchievementTravelItem.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            headerViewHolder.imgAchievement.setImageBitmap(null);
            headerViewHolder.txtNameAchievement.setText(R.string.Achievement);
            headerViewHolder.txtShortNameAchievement.setText(R.string.Achievement_Short_Name);
            headerViewHolder.txtSequenceAchievement.setText(R.string.Itinerary_Sequence);
            headerViewHolder.btnAddAchievement.setImageResource(R.drawable.ic_button_add);
            headerViewHolder.btnAddAchievement.setOnClickListener(v -> {  });
            // TODO- Implement relationship on achievement and travel
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Achievement achievement = mAchievement.get(position-show_header);
            byte[] imgArray = achievement.getImage();
            if (imgArray!=null){
                Bitmap raw = BitmapFactory.decodeByteArray(imgArray, 0, imgArray.length);
                itemViewHolder.imgAchievement.setImageBitmap(raw);
                if (achievement.getStatus_achievement() == 1){
                    itemViewHolder.imgAchievement.setAlpha(1f);
                } else {
                    itemViewHolder.imgAchievement.setAlpha(0.5f);
                }
            }
            //itemViewHolder.txtSequenceAchievement.setText(String.valueOf(Database.mItineraryDao.fetchItineraryById(achievement.getItinerary_id()).getSequence()));
            itemViewHolder.txtNameAchievement.setText(achievement.getName());
            itemViewHolder.txtShortNameAchievement.setText(achievement.getShort_name());

            if (form.equals("Home")) {
                itemViewHolder.btnDelete.setVisibility(View.INVISIBLE);
            } else {
                itemViewHolder.btnDelete.setVisibility(View.VISIBLE);
            }

            itemViewHolder.imgAchievement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemViewHolder.imgAchievement.setOnClickListener(this);
                    try {
                        Achievement achievementNew = mAchievement.get(position-show_header);
                        if ( achievementNew.getStatus_achievement()==0 ) {
                            achievementNew.setStatus_achievement(1);
                        } else {
                            achievementNew.setStatus_achievement(0);
                        }
                        Database.mAchievementDao.updateAchievement(achievementNew);
                        notifyItemRangeChanged(position, mAchievement.size());
                    } catch (Exception e) {
                        Toast.makeText(context, context.getString(R.string.Error_Changing_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
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
        return mAchievement.size()+show_header;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llAchievementTravelItem;
        public final ImageView imgAchievement;
        public final TextView txtNameAchievement;
        public final TextView txtShortNameAchievement;
        public final TextView txtSequenceAchievement;
        public final ImageButton btnAddAchievement;

        public HeaderViewHolder(View v) {
            super(v);
            llAchievementTravelItem = v.findViewById(R.id.llAchievementTravelItem);
            imgAchievement = v.findViewById(R.id.imgAchievement);
            txtNameAchievement = v.findViewById(R.id.txtNameAchievement);
            txtShortNameAchievement = v.findViewById(R.id.txtShortNameAchievement);
            txtSequenceAchievement = v.findViewById(R.id.txtSequenceAchievement);
            btnAddAchievement = v.findViewById(R.id.btnDelete);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llAchievementTravelItem;
        public final ImageView imgAchievement;
        public final TextView txtNameAchievement;
        public final TextView txtShortNameAchievement;
        public final TextView txtSequenceAchievement;
        public final ImageButton btnDelete;

        public ItemViewHolder(View v) {
            super(v);
            llAchievementTravelItem = v.findViewById(R.id.llAchievementTravelItem);
            imgAchievement = v.findViewById(R.id.imgAchievement);
            txtNameAchievement = v.findViewById(R.id.txtNameAchievement);
            txtShortNameAchievement = v.findViewById(R.id.txtShortNameAchievement);
            txtSequenceAchievement = v.findViewById(R.id.txtSequenceAchievement);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
