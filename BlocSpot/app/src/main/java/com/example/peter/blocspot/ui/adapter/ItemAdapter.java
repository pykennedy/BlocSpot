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
import com.example.peter.blocspot.ui.activity.MapsActivity;
import com.example.peter.blocspot.ui.animations.BlocSpotAnimator;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemAdapterViewHolder> {

    DataSource sharedDataSource = BlocSpotApplication.getSharedDataSource();
    GoogleMap mMap;

    private List<PoiItem> poiItemList = null;

    public ItemAdapter(GoogleMap mMap) {
        poiItemList = sharedDataSource.getPoiItemList();
        this.mMap = mMap;
    }

    @Override
    public ItemAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.poi_item, viewGroup, false);
        return new ItemAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ItemAdapterViewHolder itemAdapterViewHolder, int index) {
        itemAdapterViewHolder.update(poiItemList.get(index));
    }

    @Override
    public int getItemCount() {
        return poiItemList.size();
    }

    public void setCategory(String category) {
        poiItemList = sharedDataSource.getPoiByCategory(category);
        this.notifyDataSetChanged();
    }

    public void setAll() {
        poiItemList = sharedDataSource.getPoiItemList();
        this.notifyDataSetChanged();
    }



    class ItemAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        PoiItem poiItem;

        public ItemAdapterViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.poi_item_name);
            itemView.setOnClickListener(this);
        }

        void update(PoiItem poiItem) {
            this.poiItem = poiItem;
            title.setText(poiItem.getName());
        }

        @Override
        public void onClick(View v) {
            BlocSpotAnimator.centerMapOnPointZoomedIn(new LatLng(poiItem.getLatitude(), poiItem.getLongitude()),
                    MapsActivity.STANDARD_CAMERA_SPEED, mMap);
            BlocSpotAnimator.collapse(MapsActivity.getCurrentWindow());
            MapsActivity.windowIsOpen = false;
            MapsActivity.activeMenu = "";
            MapsActivity.menu.setIcon(R.drawable.menu_dark);
        }
    }
}
