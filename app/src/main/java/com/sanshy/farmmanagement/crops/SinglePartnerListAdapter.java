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

public class SinglePartnerListAdapter extends ArrayAdapter<SinglePartnerListData> {

    Activity context;
    ArrayList<SinglePartnerListData> PartnerList;

    public SinglePartnerListAdapter(@NonNull Activity context, ArrayList<SinglePartnerListData> PartnerList) {
        super(context, R.layout.single_partner_list,PartnerList);

        this.context = context;
        this.PartnerList = PartnerList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.single_partner_list,null,true);

        SinglePartnerListData list = PartnerList.get(position);

        TextView Name = rowView.findViewById(R.id.partner_name_single);
        TextView Phone = rowView.findViewById(R.id.partner_phone_single);

        Name.setText(list.getPartnerName());
        Phone.setText(list.getPartnerPhone());

        return rowView;
    }
}
