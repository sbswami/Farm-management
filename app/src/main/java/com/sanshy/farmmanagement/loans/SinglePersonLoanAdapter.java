package com.sanshy.farmmanagement.loans;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sanshy.farmmanagement.R;
import com.sanshy.farmmanagement.crops.SinglePartnerListData;

import java.util.ArrayList;

public class SinglePersonLoanAdapter extends ArrayAdapter<SinglePersonLoan> {
    Activity context;
    ArrayList<SinglePersonLoan> LoanPersonList;

    public SinglePersonLoanAdapter(@NonNull Activity context, ArrayList<SinglePersonLoan> LoanPersonList) {
        super(context, R.layout.single_partner_list, LoanPersonList);

        this.context = context;
        this.LoanPersonList = LoanPersonList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.single_partner_list,null,true);

        SinglePersonLoan list = LoanPersonList.get(position);

        TextView Name = rowView.findViewById(R.id.partner_name_single);
        TextView Phone = rowView.findViewById(R.id.partner_phone_single);

        Name.setText(list.getName());
        Phone.setText(String.valueOf(list.getPhone()));

        return rowView;
    }
}
