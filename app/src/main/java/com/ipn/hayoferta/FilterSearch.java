package com.ipn.hayoferta;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ErikFernando on 13/07/2015.
 */
public class FilterSearch extends Filter {

    List<Ofertas> mOfertasList;
    List<Ofertas> filteredList;
    CardsAdapter mAdapter;

    public FilterSearch(CardsAdapter mAdapter, List<Ofertas> mOfertasList) {
        this.mAdapter = mAdapter;
        this.mOfertasList = new LinkedList<>(mOfertasList);
        filteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();

        if (constraint.length() == 0) {
            filteredList.addAll(mOfertasList);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();

            for (final Ofertas ofertas : mOfertasList) {
                if (ofertas.getMarca().toLowerCase().contains(constraint)) {
                    filteredList.add(ofertas);
                }
                else if (ofertas.getDescripcion().toLowerCase().contains(constraint))
                {
                    filteredList.add(ofertas);

                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        mAdapter.ofertasList.clear();
        mAdapter.ofertasList.addAll((ArrayList<Ofertas>) results.values);
        mAdapter.notifyDataSetChanged();
    }
}
