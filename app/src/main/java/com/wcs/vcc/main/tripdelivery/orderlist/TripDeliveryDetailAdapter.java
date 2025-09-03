package com.wcs.vcc.main.tripdelivery.orderlist;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemTripDeliveryDetailBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aang on 026/07/2018.
 */

public class TripDeliveryDetailAdapter extends DataBoundListAdapter<TripDeliveryDetail, ItemTripDeliveryDetailBinding> implements OnMapReadyCallback {

    private int altColor;
    private ItemTripDeliveryDetailListener itemListener;
    private View root;
    Context context;
    private double Lat, Long;


    GoogleMap mMap;
    LatLng latLng;

    @BindView(R.id.btnDetailLocationMap)
    ImageButton btn;

    public TripDeliveryDetailAdapter(ItemTripDeliveryDetailListener itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    protected ItemTripDeliveryDetailBinding createBinding(ViewGroup parent, int viewType) {
        context = parent.getContext();
        altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);
        SupportMapFragment mapFragment = (SupportMapFragment) ((FragmentActivity)context).getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ButterKnife.bind((Activity) context);

        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_trip_delivery_detail, parent, false);
    }

    @Override
    protected void bind(ItemTripDeliveryDetailBinding binding, final TripDeliveryDetail item, final int position) {
        binding.setItem(item);
        root = binding.getRoot();

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onClick(item, position);
            }
        });

        binding.btnDetailLocationMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, getItemCount()+"", Toast.LENGTH_SHORT).show();
                Lat = item.LAT;
                Long = item.LON;
                latLng = new LatLng(Lat, Long);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Lat: " + Lat + " - Long: " + Long + "\n");
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));

            }
        });

        root.setBackgroundColor(position % 2 == 0 ? altColor : Color.WHITE);
    }

    @Override
    protected boolean areItemsTheSame(TripDeliveryDetail oldItem, TripDeliveryDetail newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(TripDeliveryDetail oldItem, TripDeliveryDetail newItem) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int i = 0; i < getItemCount(); i++){
            Lat = getItems().get(i).LAT;
            Long = getItems().get(i).LON;
            latLng = new LatLng(Lat, Long);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Lat: " + Lat + " - Long: " + Long + "\n");
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
            mMap.addMarker(markerOptions);
        }

    }


}
