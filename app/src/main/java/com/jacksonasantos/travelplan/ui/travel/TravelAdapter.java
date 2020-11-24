package com.jacksonasantos.travelplan.ui.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.R;

import java.util.List;

public class TravelAdapter extends ArrayAdapter<Travel> {
    private List<Travel> travels;
    private LayoutInflater inflater;

    public TravelAdapter(Context context, List<Travel> travels) {
        super(context, 0, travels);
        //this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.travels = travels;
    }

    public void novosDados(List<Travel> travels) {
        this.travels = travels;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(travels != null) {
            return travels.size();
        }else{
            return 0;
        }
    }

    @Override
    public Travel getItem(int position) {
        return travels.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.fragment_item_travel, null);
        ((TextView) (v.findViewById(R.id.randomText))).setText((CharSequence) travels.get(position));

        v.findViewById(R.id.btnEditar).setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Exemplo Toast" + travels.get(position), Toast.LENGTH_SHORT).show();

                        edita(travels.get(position));
                    }
                });

        v.findViewById(R.id.btnExcluir)
                .setOnClickListener
                        (new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleta(travels.get(position));
                            }
                        });

        return v;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void edita(Travel travel) {   }

    public void deleta(Travel travel) {    }
}

