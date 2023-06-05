package com.jacksonasantos.travelplan.ui.travel;

import static com.jacksonasantos.travelplan.ui.travel.MarkerActivity.adjustMarker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Achievement;
import com.jacksonasantos.travelplan.dao.Marker;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;
import java.util.Objects;

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
        View achievementView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_travel_achievement, parent, false);

        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(achievementView);
        } else return new ItemViewHolder(achievementView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            final int[] nrSpinAchievement = {0};
            final int[] nrSpinItinerary = {0};

            headerViewHolder.llAchievementTravelItem.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            headerViewHolder.imgAchievement.setImageBitmap(null);
            headerViewHolder.txtNameAchievement.setText(R.string.Achievement);
            headerViewHolder.txtShortNameAchievement.setText(R.string.Achievement_Short_Name);
            headerViewHolder.txtSequenceAchievement.setText(R.string.Itinerary_Sequence);
            headerViewHolder.btnAddAchievement.setImageResource(R.drawable.ic_button_add);
            headerViewHolder.btnAddAchievement.setOnClickListener(v -> {
                LayoutInflater li = LayoutInflater.from(v.getContext());
                View promptsView = li.inflate(R.layout.dialog_travel_achievement, null);

                String[] adapterCols = new String[]{"text1"};
                int[] adapterRowViews = new int[]{android.R.id.text1};
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setView(promptsView);
                final Spinner spinAchievement = promptsView.findViewById(R.id.spinAchievement);
                final Spinner spinItinerary = promptsView.findViewById(R.id.spinItinerary);

                Cursor cAchievement = Database.mAchievementDao.fetchArrayAchievement();
                SimpleCursorAdapter cursorAdapterA = new SimpleCursorAdapter(li.getContext(),
                        android.R.layout.simple_spinner_item, cAchievement, adapterCols, adapterRowViews, 0);
                cursorAdapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinAchievement.setAdapter(cursorAdapterA);
                Utils.setSpinnerToValue(spinAchievement, nrSpinAchievement[0]);
                spinAchievement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        nrSpinAchievement[0] = Math.toIntExact(spinAchievement.getSelectedItemId());
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                Cursor cItinerary = Database.mItineraryDao.fetchArrayItinerary(mTravel_id);
                SimpleCursorAdapter cursorAdapterI = new SimpleCursorAdapter(li.getContext(),
                        android.R.layout.simple_spinner_item, cItinerary, adapterCols, adapterRowViews, 0);
                cursorAdapterI.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinItinerary.setAdapter(cursorAdapterI);
                Utils.setSpinnerToValue(spinItinerary, nrSpinItinerary[0]);
                spinItinerary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        nrSpinItinerary[0] = Math.toIntExact(spinItinerary.getSelectedItemId());
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(R.string.OK, (dialog, id) -> {
                            boolean isSave = false;

                            Achievement ac1 = Database.mAchievementDao.fetchAchievementById(nrSpinAchievement[0]);

                            ac1.setTravel_id(mTravel_id);
                            ac1.setItinerary_id(nrSpinItinerary[0]);
                            try {
                                isSave = Database.mAchievementDao.updateAchievement(ac1);
                            } catch (Exception e) {
                                Toast.makeText(context, R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            if (!isSave) {
                                Toast.makeText(context, R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                            } else {
                                mAchievement.add(ac1);
                                notifyItemInserted(mAchievement.size());
                            }
                        })
                        .setNegativeButton(R.string.Cancel, (dialog, id) -> dialog.cancel());
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            });
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
            itemViewHolder.txtSequenceAchievement.setText(String.valueOf(Database.mItineraryDao.fetchItineraryById(achievement.getItinerary_id()).getSequence()));
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

            itemViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemViewHolder.btnDelete.setOnClickListener(this);
                    new AlertDialog.Builder(v.getContext())
                            .setTitle(R.string.Achievement_Travel_Deleting)
                            .setMessage(R.string.Msg_Confirm)
                            .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                                try {
                                    Achievement achievementNew = mAchievement.get(position-show_header);
                                    if ((achievementNew.getItinerary_id()!=null) && (achievementNew.getItinerary_id()>0)) {
                                        List<Marker> markers = Database.mMarkerDao.fetchMarkerByTravelItineraryId(achievementNew.getTravel_id(), achievementNew.getItinerary_id());
                                        if (markers.size() > 0) {
                                            for (int x = 0; x < markers.size(); x++) {
                                                Marker marker = markers.get(x);
                                                if (Objects.equals(marker.getAchievement_id(), achievementNew.getId())) {
                                                    if (  Database.mMarkerDao.deleteMarker(marker.getId()) ) {
                                                        adjustMarker(marker.getTravel_id(), marker.getItinerary_id(), marker.getSequence(), false);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    achievementNew.setTravel_id(null);
                                    achievementNew.setItinerary_id(null);
                                    Database.mAchievementDao.updateAchievement(achievementNew);
                                    mAchievement.remove(position-show_header);
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
