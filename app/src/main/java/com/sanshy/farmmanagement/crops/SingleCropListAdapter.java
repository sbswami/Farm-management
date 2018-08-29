package com.sanshy.farmmanagement.crops;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanshy.farmmanagement.R;

import java.util.ArrayList;

public class SingleCropListAdapter extends ArrayAdapter<SingleCropList> {

    ArrayList<SingleCropList> CropList = new ArrayList<>();
    Activity context;

    public SingleCropListAdapter(@NonNull Activity context,ArrayList<SingleCropList> CropList) {
        super(context, R.layout.single_crop_list,CropList);
        this.CropList = CropList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.single_crop_list,null,true);

        SingleCropList list = CropList.get(position);

        TextView Name = rowView.findViewById(R.id.crop_name_single);
        TextView Year = rowView.findViewById(R.id.crop_year_single);
        TextView AreaOfLand = rowView.findViewById(R.id.area_of_land_crop_single);
        TextView CropType = rowView.findViewById(R.id.type_crop_single);

        LinearLayout PartnerContainer = rowView.findViewById(R.id.partner_container_single);

        TextView Partner1 = rowView.findViewById(R.id.partner_name_and_sharing_single1);
        TextView Partner2 = rowView.findViewById(R.id.partner_name_and_sharing_single2);
        TextView Partner3 = rowView.findViewById(R.id.partner_name_and_sharing_single3);
        TextView Partner4 = rowView.findViewById(R.id.partner_name_and_sharing_single4);
        TextView Partner5 = rowView.findViewById(R.id.partner_name_and_sharing_single5);

        Name.setText(list.getCropName());
        Year.setText(list.getCropYear());
        AreaOfLand.setText(context.getString(R.string.area_of_land)+": "+list.getCropLandArea());
        CropType.setText(context.getString(R.string.crop_type)+": "+list.getCropType());

        if (list.isPartner()){
            PartnerContainer.setVisibility(View.VISIBLE);

            if (list.getPartner1()!=null){
                Partner1.setText(list.getPartner1());
                Partner1.setVisibility(View.VISIBLE);
            }
            if (list.getPartner2()!=null){
                Partner2.setText(list.getPartner2());
                Partner2.setVisibility(View.VISIBLE);
            }
            if (list.getPartner3()!=null){
                Partner3.setText(list.getPartner3());
                Partner3.setVisibility(View.VISIBLE);
            }
            if (list.getPartner4()!=null){
                Partner4.setText(list.getPartner4());
                Partner4.setVisibility(View.VISIBLE);
            }
            if (list.getPartner5()!=null){
                Partner5.setText(list.getPartner5());
                Partner5.setVisibility(View.VISIBLE);
            }

        }

        return rowView;
    }
}
