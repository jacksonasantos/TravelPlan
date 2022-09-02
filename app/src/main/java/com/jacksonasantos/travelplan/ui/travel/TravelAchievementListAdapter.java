package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Achievement;
import com.jacksonasantos.travelplan.dao.general.Database;

import java.util.List;

public class TravelAchievementListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<Achievement> mAchievement;
    final Context context;
    final int show_header;
    public final String form;

    public TravelAchievementListAdapter(List<Achievement> achievements, Context context, String form, int show_header) {
        this.mAchievement = achievements;
        this.context = context;
        this.form = form;
        this.show_header = show_header; // 0 - NO SHOW HEADER | 1 - SHOW HEADER
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View achievementView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_travel_achievement, parent, false);

        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(achievementView);
        } else return new ItemViewHolder(achievementView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.llAchievementTravelItem.setBackgroundColor(Color.LTGRAY);
            headerViewHolder.imgAchievement.setImageBitmap(null);
            headerViewHolder.txtNameAchievement.setText(R.string.Achievement);
            headerViewHolder.btnDelete.setVisibility(View.INVISIBLE);
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            byte[] imgArray = mAchievement.get(position-show_header).getImage();
            if (imgArray!=null){
                Bitmap raw = BitmapFactory.decodeByteArray(imgArray, 0, imgArray.length);
                itemViewHolder.imgAchievement.setImageBitmap(raw);
                if (mAchievement.get(position-show_header).getStatus_achievement() == 1){
                    itemViewHolder.imgAchievement.setAlpha(1f);
                } else {
                    itemViewHolder.imgAchievement.setAlpha(0.5f);
                }
            }
            itemViewHolder.txtNameAchievement.setText(mAchievement.get(position-show_header).getName());

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
                        Achievement mAchievementNew = mAchievement.get(position-show_header);
                        if ( mAchievementNew.getStatus_achievement()==0 ) {
                            mAchievementNew.setStatus_achievement(1);
                        } else {
                            mAchievementNew.setStatus_achievement(0);
                        }
                        Database.mAchievementDao.updateAchievement(mAchievementNew);
                        notifyItemRangeChanged(position, mAchievement.size());
                    } catch (Exception e) {
                        Toast.makeText(context, context.getString(R.string.Error_Changing_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            itemViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemViewHolder.btnDelete.setOnClickListener(this);
                    new AlertDialog.Builder(v.getContext())
                            .setTitle(R.string.Achievement_Travel_Deleting)
                            .setMessage(R.string.Msg_Confirm)
                            .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                                try {
                                    Achievement mAchievementNew = mAchievement.get(position-show_header);
                                    mAchievementNew.setTravel_id(null);
                                    Database.mAchievementDao.updateAchievement(mAchievementNew);
                                    mAchievement.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, mAchievement.size());
                                } catch (Exception e) {
                                    Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            }).setNegativeButton(R.string.No, null)
                            .show();
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
        public final ImageButton btnDelete;

        public HeaderViewHolder(View v) {
            super(v);
            llAchievementTravelItem = v.findViewById(R.id.llAchievementTravelItem);
            imgAchievement = v.findViewById(R.id.imgAchievement);
            txtNameAchievement = v.findViewById(R.id.txtNameAchievement);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llAchievementTravelItem;
        public final ImageView imgAchievement;
        public final TextView txtNameAchievement;
        public final ImageButton btnDelete;

        public ItemViewHolder(View v) {
            super(v);
            llAchievementTravelItem = v.findViewById(R.id.llAchievementTravelItem);
            imgAchievement = v.findViewById(R.id.imgAchievement);
            txtNameAchievement = v.findViewById(R.id.txtNameAchievement);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
