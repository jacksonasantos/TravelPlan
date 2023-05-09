package com.jacksonasantos.travelplan.ui.travel;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Tour;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;
import java.util.Objects;

public class TravelTourListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<Tour> mTour;
    final Context context;
    final int show_header;
    final Integer mTravel_id;
    public final String form;

    public TravelTourListAdapter(List<Tour> tours, Context context, String form, int show_header, Integer travel_id) {
        this.mTour = tours;
        this.mTravel_id = travel_id;
        this.context = context;
        this.form = form;
        this.show_header = show_header; // 0 - NO SHOW HEADER | 1 - SHOW HEADER
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View tourView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_travel_tour, parent, false);

        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(tourView);
        } else return new ItemViewHolder(tourView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.llTourTravelItem.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            headerViewHolder.imgTourType.setImageBitmap(null);
            headerViewHolder.txtTourDate.setText(R.string.Tour_Date);
            headerViewHolder.txtLocalTour.setText(R.string.Tour_Local_Tour);
            headerViewHolder.imgAchievement.setImageBitmap(null);
            headerViewHolder.btnAddTour.setImageResource(R.drawable.ic_button_add);
            headerViewHolder.btnAddTour.setOnClickListener(v -> {
                Intent intent = new Intent (v.getContext(), TourActivity.class);
                intent.putExtra("travel_id", mTravel_id);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                notifyItemChanged(position);
            });
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            itemViewHolder.imgTourType.setImageResource(Tour.getTourTypeImage(mTour.get(position-show_header).getTour_type()));
            itemViewHolder.txtTourDate.setText(Objects.requireNonNull(Utils.dateToString(mTour.get(position - show_header).getTour_date())).substring(0,5));
            itemViewHolder.txtLocalTour.setText(mTour.get(position-show_header).getLocal_tour());
            byte[] imgArray = Database.mAchievementDao.fetchAchievementById(mTour.get(position - show_header).getAchievement_id()).getImage();
            if(imgArray!=null){
                Bitmap raw = BitmapFactory.decodeByteArray(imgArray, 0, imgArray.length);
                itemViewHolder.imgAchievement.setImageBitmap(raw);
            }
            if (form.equals("Home")) {
                itemViewHolder.btnDelete.setVisibility(View.INVISIBLE);
            } else {
                itemViewHolder.btnDelete.setVisibility(View.VISIBLE);
            }
            itemViewHolder.llTourTravelItem.setOnClickListener( v-> {
                Intent intent = new Intent (v.getContext(), TourActivity.class);
                intent.putExtra("tour_id", mTour.get(position-show_header).getId());
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                notifyItemChanged(position);
            });
            itemViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemViewHolder.btnDelete.setOnClickListener(this);
                    new AlertDialog.Builder(v.getContext())
                            .setTitle(R.string.Tour_Deleting)
                            .setMessage(R.string.Msg_Confirm)
                            .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                                try {
                                    Database.mTourDao.deleteTour(mTour.get(position-show_header).getId());
                                    mTour.remove(position-show_header);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, mTour.size());
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
        return mTour.size()+show_header;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llTourTravelItem;
        public final ImageView imgTourType;
        public final TextView txtTourDate;
        public final TextView txtLocalTour;
        public final ImageView imgAchievement;
        public final ImageButton btnAddTour;

        public HeaderViewHolder(View v) {
            super(v);
            llTourTravelItem = v.findViewById(R.id.llTourTravelItem);
            imgTourType = v.findViewById(R.id.imgTourType);
            txtTourDate = v.findViewById(R.id.txtTourDate);
            txtLocalTour = v.findViewById(R.id.txtLocalTour);
            imgAchievement = v.findViewById(R.id.imgAchievement);
            btnAddTour = v.findViewById(R.id.btnDelete);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llTourTravelItem;
        public final ImageView imgTourType;
        public final TextView txtTourDate;
        public final TextView txtLocalTour;
        public final ImageView imgAchievement;
        public final ImageButton btnDelete;

        public ItemViewHolder(View v) {
            super(v);
            llTourTravelItem = v.findViewById(R.id.llTourTravelItem);
            imgTourType = v.findViewById(R.id.imgTourType);
            txtTourDate = v.findViewById(R.id.txtTourDate);
            txtLocalTour = v.findViewById(R.id.txtLocalTour);
            imgAchievement = v.findViewById(R.id.imgAchievement);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}