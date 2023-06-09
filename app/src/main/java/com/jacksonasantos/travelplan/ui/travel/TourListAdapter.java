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
import com.jacksonasantos.travelplan.dao.Itinerary;
import com.jacksonasantos.travelplan.dao.Tour;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;
import java.util.Objects;

public class TourListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<Tour> mTour;
    final Context context;
    final int show_header;
    final Integer mTravel_id;

    public TourListAdapter(List<Tour> tours, Context context, int show_header, Integer travel_id) {
        this.mTour = tours;
        this.mTravel_id = travel_id;
        this.context = context;
        this.show_header = show_header;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View tourView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_tour, parent, false);
        if (viewType == TYPE_HEADER) return new HeaderViewHolder(tourView);
        else return new ItemViewHolder(tourView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.llTourItem.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            headerViewHolder.imgTourType.setImageBitmap(null);
            headerViewHolder.txtTourDate.setText(R.string.Tour_Date);
            headerViewHolder.txtLocalTour.setText(R.string.Tour_Local_Tour);
            headerViewHolder.txtDistance.setText(R.string.Itinerary_Distance);
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
            final Tour tour = mTour.get(position-show_header);
            final Itinerary itinerary = Database.mItineraryDao.fetchItineraryById(tour.getItinerary_id());
            itemViewHolder.imgTourType.setImageResource(Tour.getTourTypeImage(tour.getTour_type()));
            itemViewHolder.txtTourDate.setText(Objects.requireNonNull(Utils.dateToString(tour.getTour_date())).substring(0,5));
            itemViewHolder.txtTourSequence.setText(itinerary.getSequence() + "." + tour.getTour_sequence());
            itemViewHolder.txtLocalTour.setText(tour.getLocal_tour());
            itemViewHolder.txtDistance.setText(Integer.toString(tour.getDistanceMeasureIndex()));
            byte[] imgArray = Database.mAchievementDao.fetchAchievementById(tour.getAchievement_id()).getImage();
            if(imgArray!=null){
                Bitmap raw = BitmapFactory.decodeByteArray(imgArray, 0, imgArray.length);
                itemViewHolder.imgAchievement.setImageBitmap(raw);
            }
            itemViewHolder.llTourItem.setOnClickListener( v-> {
                Intent intent = new Intent (v.getContext(), TourActivity.class);
                intent.putExtra("tour_id", tour.getId());
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
                                    Database.mTourDao.deleteTour(tour.getId());
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
        public final LinearLayout llTourItem;
        public final ImageView imgTourType;
        public final TextView txtTourDate;
        public final TextView txtLocalTour;
        public final TextView txtDistance;
        public final ImageView imgAchievement;
        public final ImageButton btnAddTour;

        public HeaderViewHolder(View v) {
            super(v);
            llTourItem = v.findViewById(R.id.llTourItem);
            imgTourType = v.findViewById(R.id.imgTourType);
            txtTourDate = v.findViewById(R.id.txtTourDate);
            txtLocalTour = v.findViewById(R.id.txtLocalTour);
            txtDistance = v.findViewById(R.id.txtDistance);
            imgAchievement = v.findViewById(R.id.imgAchievement);
            btnAddTour = v.findViewById(R.id.btnDelete);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llTourItem;
        public final ImageView imgTourType;
        public final TextView txtTourDate;
        public final TextView txtTourSequence;
        public final TextView txtLocalTour;
        public final TextView txtDistance;
        public final ImageView imgAchievement;
        public final ImageButton btnDelete;

        public ItemViewHolder(View v) {
            super(v);
            llTourItem = v.findViewById(R.id.llTourItem);
            imgTourType = v.findViewById(R.id.imgTourType);
            txtTourDate = v.findViewById(R.id.txtTourDate);
            txtTourSequence = v.findViewById(R.id.txtTourSequence);
            txtLocalTour = v.findViewById(R.id.txtLocalTour);
            txtDistance = v.findViewById(R.id.txtDistance);
            imgAchievement = v.findViewById(R.id.imgAchievement);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}