package com.wcs.vcc.main.containerandtruckinfor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wcs.vcc.main.containerandtruckinfor.model.CarNumber;
import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemCarInOutBinding;

import java.util.ArrayList;
import java.util.List;

public class CarCheckOutAdapter extends ArrayAdapter<CarNumber> {

    private List<CarNumber> list;
    private ItemCarInOutBinding binding;

    public CarCheckOutAdapter(@NonNull Context context, @NonNull List<CarNumber> list) {
        super(context, 0, list);
        this.list = new ArrayList<>(list);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            binding =  ItemCarInOutBinding.bind(LayoutInflater.from(getContext()).inflate(R.layout.item_car_in_out, parent, false));
        }

        CarNumber carNumber = getItem(position);

        if (carNumber != null){
            binding.itemTvCarNumber.setText(carNumber.containerNum);
            binding.itemTvTimeIn.setText("Vào: "+ carNumber.getTimeIn());
            binding.itemTvTimeOut.setText("Ra: "+ carNumber.getTimeOut());
            binding.itemImgStateData.setImageResource(carNumber.userCheckOut ? R.drawable.check : R.drawable.multiply);
            binding.itemImgStateSecurity.setImageResource(carNumber.checkOut ? R.drawable.check : R.drawable.multiply);
        }

        return binding.getRoot();
    }
}
