package com.sanshy.farmmanagement.crops;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sanshy.farmmanagement.R;

import java.util.ArrayList;

public class SingleCropReportAdapter extends ArrayAdapter<SingleCropReportSmallList> {

    ArrayList<SingleCropReportSmallList> ReportList = new ArrayList<>();
    Activity context;

    public SingleCropReportAdapter(@NonNull Activity context, ArrayList<SingleCropReportSmallList> ReportList) {
        super(context, R.layout.single_report_list_custom_list_view, ReportList);

        this.ReportList = ReportList;
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.single_report_list_custom_list_view,null,true);

        SingleCropReportSmallList list = ReportList.get(position);

        TextView Title = rowView.findViewById(R.id.title_single_report);
        TextView Total = rowView.findViewById(R.id.single_report_total);
        TextView Cash = rowView.findViewById(R.id.single_report_cash);
        TextView Borrow = rowView.findViewById(R.id.single_report_borrow);

        Total.setText(list.getTotal());
        Cash.setText(list.getCash());
        Borrow.setText(list.getBorrow());
        Title.setText(list.getTitle());

        return rowView;
    }
}
