package com.ipn.hayoferta;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
/**
 * Created by ErikFernando on 08/04/2015.
 */
public class BrandsAdapter extends RecyclerView.Adapter<BrandsAdapter.ViewHolder> {
    private Context context;
    private List<Marca> marcaList;

    public BrandsAdapter(Context context, List<Marca> marcaList) {
        this.context = context;
        this.marcaList = marcaList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_brands, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Marca item = marcaList.get(position);

        holder.titleBrand.setText(item.getMarca());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Establecimiento: "+item.getMarca(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != marcaList ? marcaList.size() : 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleBrand;

        public ViewHolder(View itemView) {
            super(itemView);
            titleBrand = (TextView) itemView.findViewById(R.id.titleBrand);
        }
    }
}
