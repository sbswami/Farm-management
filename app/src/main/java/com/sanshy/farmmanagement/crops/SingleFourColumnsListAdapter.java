package com.sanshy.farmmanagement.crops;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sanshy.farmmanagement.R;

import java.util.ArrayList;

public class SingleFourColumnsListAdapter extends ArrayAdapter<SingleFourColumnsList> {

    Activity activity;
    ArrayList<SingleFourColumnsList> FourList = new ArrayList<>();

    public SingleFourColumnsListAdapter(@NonNull Activity activity, ArrayList<SingleFourColumnsList> FourList) {
        super(activity, R.layout.single_four_columns ,FourList);
        this.FourList = FourList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.single_four_columns,null,true);

        SingleFourColumnsList list = FourList.get(position);

        TextView DateView = rowView.findViewById(R.id.four_columns_date);
        TextView RemarkView = rowView.findViewById(R.id.four_columns_remark);
        TextView AmountView = rowView.findViewById(R.id.four_columns_amount);
        TextView NameSOrB = rowView.findViewById(R.id.four_columns_name_s_or_b);

        DateView.setText(list.getDate());
        RemarkView.setText(list.getRemark());
        AmountView.setText(list.getAmount());
        NameSOrB.setText(list.getServiceOrBuyer());

        return rowView;
    }
}
