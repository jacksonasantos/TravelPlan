package com.jacksonasantos.travelplan.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.NextMaintenanceItem;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeVehicleNextMaintenanceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<NextMaintenanceItem> mNextMaintenanceItem;
    final int show_header;
    String[] measureArray;

    final Globals g = Globals.getInstance();

    public HomeVehicleNextMaintenanceListAdapter(List<NextMaintenanceItem> nextMaintenanceItem, Context context, int show_header) {
        this.mNextMaintenanceItem = nextMaintenanceItem;
        this.show_header = show_header;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View nextMaintenanceItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_vehicle_next_maintenance, parent, false);
        measureArray = parent.getResources().getStringArray(R.array.measure_item);

        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(nextMaintenanceItemView);
        }
        else return new ItemViewHolder(nextMaintenanceItemView);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.llNextMaintenanceItem.setBackgroundColor(Color.LTGRAY);
            headerViewHolder.txtDescription.setText(R.string.NextMaintenance_Description);
            headerViewHolder.txtMeasure.setText(R.string.NextMaintenance_Measure);
            headerViewHolder.txtNextService.setText(R.string.NextMaintenance_Service);
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final Vehicle v1 = Database.mVehicleDao.fetchVehicleById(g.getIdVehicle());
            final NextMaintenanceItem nextMaintenanceItem = mNextMaintenanceItem.get(position-show_header);

            if (position % 2 == 0) {
                itemViewHolder.llNextMaintenanceItem.setBackgroundColor(Color.LTGRAY);
            } else {
                itemViewHolder.llNextMaintenanceItem.setBackgroundColor(Color.WHITE);
            }

            int vStatus = 0;
            if (nextMaintenanceItem.getMeasure() == 1) {         // KMs
                if (v1.getOdometer() > Integer.parseInt(nextMaintenanceItem.getNext_service())) {
                    vStatus = 2;
                } else if (v1.getOdometer() > Integer.parseInt(nextMaintenanceItem.getNext_service()) - g.getKMsPreviousAlert() ||
                        v1.getOdometer() == Integer.parseInt(nextMaintenanceItem.getNext_service())) {
                    vStatus = 1;
                }
            } else {                                            // Date
                Date vData;
                Date vDataNow = new Date();

                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    vData = dateFormat.parse(nextMaintenanceItem.getNext_service());
                    Date vDataLim = vData;

                    Calendar c = Calendar.getInstance();
                    if (vDataLim != null) {
                        c.setTime(vDataLim);
                    }
                    c.add(Calendar.DATE, g.getDaysPreviousAlert() * -1);
                    vDataLim = c.getTime();
                    nextMaintenanceItem.setNext_service(Utils.dateToString(vData));
                    if (vDataNow.compareTo(vData) > 0) {
                        vStatus = 2;
                    } else if (vDataNow.compareTo(vData) <= 0 && vDataNow.compareTo(vDataLim) > 0) {
                        vStatus = 1;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            itemViewHolder.imgServiceType.setImageResource(nextMaintenanceItem.getServiceTypeImage(nextMaintenanceItem.getService_type()));
            itemViewHolder.txtDescription.setText(nextMaintenanceItem.getDescription());
            itemViewHolder.txtMeasure.setText(measureArray[nextMaintenanceItem.getMeasure() - 1]);
            switch (vStatus) {
                case 0:
                    itemViewHolder.txtNextService.setTextColor(Color.BLACK);
                    break;    // Normal
                case 1:
                    itemViewHolder.txtNextService.setTextColor(Color.MAGENTA);
                    break;    // To Win
                case 2:
                    itemViewHolder.txtNextService.setTextColor(Color.RED);
                    break;    // Expired
            }
            itemViewHolder.txtNextService.setText(nextMaintenanceItem.getNext_service());
        }
    }
    public int getItemViewType(int position) {
        if (position == 0 && show_header == 1) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mNextMaintenanceItem.size()+show_header;
    }


    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout llNextMaintenanceItem;
        private final TextView txtDescription;
        private final TextView txtMeasure;
        private final TextView txtNextService;

        public HeaderViewHolder(View v) {
            super(v);
            llNextMaintenanceItem = v.findViewById(R.id.llNextMaintenanceItem);
            txtDescription = v.findViewById(R.id.txtDescription);
            txtMeasure = v.findViewById(R.id.txtMeasure);
            txtNextService = v.findViewById(R.id.txtNextService);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout llNextMaintenanceItem;
        private final ImageView imgServiceType;
        private final TextView txtDescription;
        private final TextView txtMeasure;
        private final TextView txtNextService;

        public ItemViewHolder(View v) {
            super(v);
            llNextMaintenanceItem = v.findViewById(R.id.llNextMaintenanceItem);
            imgServiceType = v.findViewById(R.id.imgServiceType);
            txtDescription = v.findViewById(R.id.txtDescription);
            txtMeasure = v.findViewById(R.id.txtMeasure);
            txtNextService = v.findViewById(R.id.txtNextService);
        }
    }
}