package com.example.peter.blocspot.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peter.blocspot.BlocSpotApplication;
import com.example.peter.blocspot.R;
import com.example.peter.blocspot.api.DataSource;
import com.example.peter.blocspot.api.model.PoiItem;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemAdapterViewHolder> {

    DataSource sharedDataSource = BlocSpotApplication.getSharedDataSource();

    @Override
    public ItemAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.poi_item, viewGroup, false);
        return new ItemAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ItemAdapterViewHolder itemAdapterViewHolder, int index) {
        itemAdapterViewHolder.update(sharedDataSource.getPoiItemList().get(index));
    }

    @Override
    public int getItemCount() {
        return sharedDataSource.getPoiItemList().size();
    }

    class ItemAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public ItemAdapterViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.poi_item_name);
        }

        void update(PoiItem poiItem) {
            title.setText(poiItem.getName());
        }
    }
}
