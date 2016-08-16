package com.ipn.hayoferta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ErikFernando on 01/04/2015.
 */
public class ListViewAdapter extends BaseAdapter{
    // Declarar Variables
    Context context;
    String[] titles;
    int[] images;
    LayoutInflater inflater;

    public ListViewAdapter(Context context, String[] titles, int[] images) {
        this.context = context;
        this.titles = titles;
        this.images = images;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Declarar variables
        TextView txtTitle;
        ImageView imgImage;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.list_row, parent, false);

        txtTitle = (TextView) itemView.findViewById(R.id.list_row_title);
        imgImage = (ImageView) itemView.findViewById(R.id.list_row_image);

        txtTitle.setText(titles[position]);
        imgImage.setImageResource(images[position]);

        return itemView;
    }
}
