package com.sanshy.farmmanagement.loans;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sanshy.farmmanagement.R;
import com.sanshy.farmmanagement.PayHistoryDataList;
import com.sanshy.farmmanagement.TwoColumnsAdapter;
import com.sanshy.farmmanagement.crops.PayHistoryBuyer;

import java.util.ArrayList;
import java.util.Date;

import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogFrom;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogSingle;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogTo;
import static com.sanshy.farmmanagement.MyStatic.LOAN_ID;
import static com.sanshy.farmmanagement.MyStatic.LOAN_TYPE;

public class PayHistorySingle extends AppCompatActivity {

    String LoanId;
    String LoanMode;

    ListView PayListView;
    ArrayList<PayHistoryDataList> PayList = new ArrayList<>();
    TwoColumnsAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_history_single);

        Intent intent = getIntent();

        LoanId = intent.getStringExtra(LOAN_ID);
        LoanMode = intent.getStringExtra(LOAN_TYPE);

        PayListView = findViewById(R.id.pay_history_single_loan_list);
        myAdapter = new TwoColumnsAdapter(this,PayList);
        PayListView.setAdapter(myAdapter);

        addData();
    }

    private void addData() {
        PayHistoryDataList item = new PayHistoryDataList(LoanId,new Date(),1000);
        PayList.add(item);
        PayList.add(item);
        PayList.add(item);
        PayList.add(item);
        PayList.add(item);
        PayList.add(item);

        myAdapter.notifyDataSetChanged();
    }

    public void FilterPayHistorySingleLoanBt(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customFilterView = layoutInflater.inflate(R.layout.custom_filter_pay,null);


        EditText MinAmountFilter = customFilterView.findViewById(R.id.min_amount_filter);
        EditText MaxAmountFilter = customFilterView.findViewById(R.id.max_amount_filter);

        RadioGroup DateFilter = customFilterView.findViewById(R.id.date_filter);
        RadioButton SingleDateFilter = customFilterView.findViewById(R.id.single_date_filter);
        RadioButton BetweenDateFilter = customFilterView.findViewById(R.id.between_date_filter);

        LinearLayout SingleDateContainer = customFilterView.findViewById(R.id.single_date_filter_container);
        Button SingleChooseDateFilter = customFilterView.findViewById(R.id.single_choose_date_filter);

        final TextView SingleShowDateFilter = customFilterView.findViewById(R.id.single_show_date_filter);

        final ArrayList<Boolean> singleChoose = new ArrayList<>();
        singleChoose.add(false);

        SingleChooseDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleChoose.add(0,ChooseDateDialogSingle(PayHistorySingle.this,SingleShowDateFilter));
            }
        });

        LinearLayout BetweenDateContainer = customFilterView.findViewById(R.id.between_dates_cotainer);
        Button FromChooseDate = customFilterView.findViewById(R.id.from_choose_date_filter);
        final TextView FromShowDate = customFilterView.findViewById(R.id.from_show_date_filter);

        final ArrayList<Boolean> fromChoose = new ArrayList<>();
        fromChoose.add(false);

        FromChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromChoose.add(0,ChooseDateDialogFrom(PayHistorySingle.this,FromShowDate));
            }
        });

        Button ToChooseDate = customFilterView.findViewById(R.id.to_choose_date_filter);
        final TextView ToShowDate = customFilterView.findViewById(R.id.to_show_date_filter);

        final ArrayList<Boolean> toChoose = new ArrayList<>();
        toChoose.add(false);

        ToChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChoose.add(0,ChooseDateDialogTo(PayHistorySingle.this,ToShowDate));
            }
        });

        Button FilterBt = customFilterView.findViewById(R.id.filter_bt);

        FilterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        builder.setView(customFilterView);

        builder.create().show();
    }
}
