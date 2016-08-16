package com.ipn.hayoferta;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ErikFernando on 07/04/2015.
 */
public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> implements Filterable {
    Context context;
    List<Ofertas> ofertasList;
    List<Ofertas> parentOfertas;
    OnItemClickListener mItemClickListener;

    public CardsAdapter(Context context, List<Ofertas> ofertasList) {
        this.context = context;
        this.ofertasList = ofertasList;
        if (ofertasList != null) {
            parentOfertas = new ArrayList<>(ofertasList);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_cards, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final Ofertas item = ofertasList.get(i);

        viewHolder.desCard.setText(item.getDescripcion());
        viewHolder.marCard.setText(item.getMarca());
        Picasso.with(context).load(item.getImagen()).error(R.drawable.no_image).into(viewHolder.imageCard);

        //Accion al tocar el Toggle
        viewHolder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Snackbar.make(buttonView, item.getDescripcion()+" te gusta", Snackbar.LENGTH_LONG).setAction("Accion", null).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != ofertasList ? ofertasList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView desCard;
        public TextView marCard;
        public ImageView imageCard;
        public ToggleButton toggleButton;

        public ViewHolder(View itemView) {
            super(itemView);
            desCard = (TextView) itemView.findViewById(R.id.textCard);
            marCard = (TextView) itemView.findViewById(R.id.marCard);
            imageCard = (ImageView) itemView.findViewById(R.id.imageCard);
            toggleButton = (ToggleButton) itemView.findViewById(R.id.toggleCard);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public Filter getFilter() {
        return new FilterSearch(this, parentOfertas);
    }
}
