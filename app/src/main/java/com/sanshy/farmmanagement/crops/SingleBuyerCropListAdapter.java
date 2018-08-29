package com.sanshy.farmmanagement.crops;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sanshy.farmmanagement.R;

import java.util.ArrayList;

public class SingleBuyerCropListAdapter extends ArrayAdapter<SingleBuyerCropsList> {

    ArrayList<SingleBuyerCropsList> BuyerList = new ArrayList<>();
    Activity context;

    public SingleBuyerCropListAdapter(@NonNull Activity context, ArrayList<SingleBuyerCropsList> BuyerList) {
        super(context, R.layout.single_buyer_crops_list, BuyerList);

        this.context = context;
        this.BuyerList = BuyerList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.single_buyer_crops_list,null,true);

        SingleBuyerCropsList list = BuyerList.get(position);

        TextView Name = rowView.findViewById(R.id.buyer_name_single);
        TextView Phone = rowView.findViewById(R.id.buyer_phone_single);
        TextView RemainingAmount = rowView.findViewById(R.id.buyer_remaining_amount);

        Name.setText(list.getBuyerName());
        Phone.setText(context.getString(R.string.phone_number)+": "+list.getBuyerPhone());
        RemainingAmount.setText(context.getString(R.string.remaining_amount)+": "+list.getRemainingAmount());

        return rowView;
    }
}
