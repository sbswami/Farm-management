package com.sanshy.farmmanagement;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sanshy.farmmanagement.PayHistoryDataList;
import com.sanshy.farmmanagement.R;

import java.util.ArrayList;

import static com.sanshy.farmmanagement.MyStatic.DateToString;

public class TwoColumnsAdapter extends ArrayAdapter<PayHistoryDataList> {

    ArrayList<PayHistoryDataList> PayList = new ArrayList<>();
    Activity context;

    public TwoColumnsAdapter(@NonNull Activity context, ArrayList<PayHistoryDataList> PayList) {
        super(context, R.layout.single_pay_history_list, PayList);

        this.context = context;
        this.PayList = PayList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.single_pay_history_list,null,true);

        PayHistoryDataList list = PayList.get(position);

        TextView Date = rowView.findViewById(R.id.date_pay_history);
        TextView Amount = rowView.findViewById(R.id.amount_pay_history);

        Date.setText(DateToString(list.getDate()));
        Amount.setText(String.valueOf(list.getAmount()));

        return rowView;
    }

}
