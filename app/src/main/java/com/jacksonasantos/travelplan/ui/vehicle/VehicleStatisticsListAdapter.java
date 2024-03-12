package com.jacksonasantos.travelplan.ui.vehicle;

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
import com.jacksonasantos.travelplan.dao.VehicleStatisticsYearResponse;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class VehicleStatisticsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    final Globals g = Globals.getInstance();
    final Locale locale = new Locale(g.getLanguage(), g.getCountry());
    final NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);

    private final List<VehicleStatisticsYearResponse> mVehicleStatisticsYearResponses;
    final Context context;
    final int show_header, show_footer;
    int vTotal1, vTotal2, vTotal3, vTotal4, vTotal5 = 0;

    public VehicleStatisticsListAdapter( List<VehicleStatisticsYearResponse> vehicleStatisticsYearResponses, Context context, int show_header, int show_footer) {
        this.mVehicleStatisticsYearResponses = vehicleStatisticsYearResponses;
        this.context = context;
        this.show_header = show_header>=1?1:0;
        this.show_footer = show_footer>=1?1:0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View travelItemExpensesView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_vehicle_statistics_year, parent, false);
        if (viewType == TYPE_HEADER) return new HeaderViewHolder(travelItemExpensesView);
        else if (viewType == TYPE_FOOTER) return new FooterViewHolder(travelItemExpensesView);
        else return new ItemViewHolder(travelItemExpensesView);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            Calendar cal = Calendar.getInstance();
            String vAno1 = "<= "+ (cal.get(Calendar.YEAR) - 3);
            String vAno2 = String.valueOf(cal.get(Calendar.YEAR) - 2);
            String vAno3 = String.valueOf(cal.get(Calendar.YEAR) - 1);
            String vAno4 = String.valueOf(cal.get(Calendar.YEAR));
            headerViewHolder.llVehicleStatistics.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.2f));
            headerViewHolder.txtSupplyReasonType.setText(R.string.VehicleStatistics_SupplyReasonType);
            headerViewHolder.txtAno1.setText(vAno1);
            headerViewHolder.txtAno2.setText(vAno2);
            headerViewHolder.txtAno3.setText(vAno3);
            headerViewHolder.txtAno4.setText(vAno4);
            headerViewHolder.txtTotalType.setText(R.string.VehicleStatistics_TotalType);
        }
        else if (holder instanceof FooterViewHolder) {
            final FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.llVehicleStatistics.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.4f));
            footerViewHolder.txtSupplyReasonType.setText(context.getResources().getString(R.string.VehicleStatistics_TotalType) + " (Kms)");
            footerViewHolder.txtAno1.setText(numberFormatter.format(vTotal1));
            footerViewHolder.txtAno2.setText(numberFormatter.format(vTotal2));
            footerViewHolder.txtAno3.setText(numberFormatter.format(vTotal3));
            footerViewHolder.txtAno4.setText(numberFormatter.format(vTotal4));
            footerViewHolder.txtTotalType.setText(numberFormatter.format(vTotal5));
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final VehicleStatisticsYearResponse vehicleStatisticsYearResponses = mVehicleStatisticsYearResponses.get(position-show_header);
            itemViewHolder.txtSupplyReasonType.setText(context.getResources().getStringArray(R.array.supply_reason_type_array)[vehicleStatisticsYearResponses.getSupply_reason_type()-1]);
            itemViewHolder.txtAno1.setText(numberFormatter.format(vehicleStatisticsYearResponses.getAno1()));
            itemViewHolder.txtAno2.setText(numberFormatter.format(vehicleStatisticsYearResponses.getAno2()));
            itemViewHolder.txtAno3.setText(numberFormatter.format(vehicleStatisticsYearResponses.getAno3()));
            itemViewHolder.txtAno4.setText(numberFormatter.format(vehicleStatisticsYearResponses.getAno4()));
            itemViewHolder.txtTotalType.setText(numberFormatter.format(vehicleStatisticsYearResponses.getTotal_type()));
            vTotal1 += vehicleStatisticsYearResponses.getAno1();
            vTotal2 += vehicleStatisticsYearResponses.getAno2();
            vTotal3 += vehicleStatisticsYearResponses.getAno3();
            vTotal4 += vehicleStatisticsYearResponses.getAno4();
            vTotal5 += vehicleStatisticsYearResponses.getTotal_type();
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0 && show_header == 1) return TYPE_HEADER;
        if (position == mVehicleStatisticsYearResponses.size() + show_header && show_footer == 1) return TYPE_FOOTER;
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mVehicleStatisticsYearResponses.size()+show_header+show_footer;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llVehicleStatistics;
        public final TextView txtSupplyReasonType;
        public final TextView txtAno1;
        public final TextView txtAno2;
        public final TextView txtAno3;
        public final TextView txtAno4;
        public final TextView txtTotalType;

        public HeaderViewHolder(View v) {
            super(v);
            llVehicleStatistics = v.findViewById(R.id.llVehicleStatistics);
            txtSupplyReasonType = v.findViewById(R.id.txtSupplyReasonType);
            txtAno1 = v.findViewById(R.id.txtAno1);
            txtAno2 = v.findViewById(R.id.txtAno2);
            txtAno3 = v.findViewById(R.id.txtAno3);
            txtAno4 = v.findViewById(R.id.txtAno4);
            txtTotalType = v.findViewById(R.id.txtTotalType);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llVehicleStatistics;
        public final TextView txtSupplyReasonType;
        public final TextView txtAno1;
        public final TextView txtAno2;
        public final TextView txtAno3;
        public final TextView txtAno4;
        public final TextView txtTotalType;

        public ItemViewHolder(View v) {
            super(v);
            llVehicleStatistics = v.findViewById(R.id.llVehicleStatistics);
            txtSupplyReasonType = v.findViewById(R.id.txtSupplyReasonType);
            txtAno1 = v.findViewById(R.id.txtAno1);
            txtAno2 = v.findViewById(R.id.txtAno2);
            txtAno3 = v.findViewById(R.id.txtAno3);
            txtAno4 = v.findViewById(R.id.txtAno4);
            txtTotalType = v.findViewById(R.id.txtTotalType);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llVehicleStatistics;
        public final TextView txtSupplyReasonType;
        public final TextView txtAno1;
        public final TextView txtAno2;
        public final TextView txtAno3;
        public final TextView txtAno4;
        public final TextView txtTotalType;

        public FooterViewHolder(View v) {
            super(v);
            llVehicleStatistics = v.findViewById(R.id.llVehicleStatistics);
            txtSupplyReasonType = v.findViewById(R.id.txtSupplyReasonType);
            txtAno1 = v.findViewById(R.id.txtAno1);
            txtAno2 = v.findViewById(R.id.txtAno2);
            txtAno3 = v.findViewById(R.id.txtAno3);
            txtAno4 = v.findViewById(R.id.txtAno4);
            txtTotalType = v.findViewById(R.id.txtTotalType);
        }
    }
}