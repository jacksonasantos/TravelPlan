package com.jacksonasantos.travelplan.ui.general;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacksonasantos.travelplan.R;

import java.io.File;
import java.util.ArrayList;

public class MyGalleryImageAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<File> listImages;

    public MyGalleryImageAdapter(Context context, ArrayList<File> listImages) {
        this.context = context;
        this.listImages = listImages;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater =(LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolderItem viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.dialog_my_files_item, parent, false);

            viewHolder = new ViewHolderItem();
            viewHolder.textViewItem = convertView.findViewById(R.id.grid_item_label);
            viewHolder.imageViewItem = convertView.findViewById(R.id.grid_item_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        Bitmap myBitmap = BitmapFactory.decodeFile(listImages.get(position).getAbsolutePath());

        viewHolder.textViewItem.setText(listImages.get(position).getName());
        viewHolder.textViewItem.setTag(position);
        viewHolder.imageViewItem.setImageBitmap(myBitmap);

        return convertView;
    }

    static class ViewHolderItem {
        TextView textViewItem;
        ImageView imageViewItem;
    }

    @Override
    public int getCount() { return listImages.size(); }

    @Override
    public Object getItem(int position) {
        return listImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}