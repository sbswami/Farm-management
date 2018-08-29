package com.sanshy.farmmanagement.loans;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sanshy.farmmanagement.R;
import com.sanshy.farmmanagement.ThreeColumnsList;
import com.sanshy.farmmanagement.ThreeColumnsListAdapter;

import java.util.ArrayList;
import java.util.Date;

import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogFrom;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogSingle;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogTo;
import static com.sanshy.farmmanagement.MyStatic.LOAN_ID;
import static com.sanshy.farmmanagement.MyStatic.LOAN_TYPE;

public class PayHistory extends AppCompatActivity {

    String LoanMode;

    ListView PayListView;
    ArrayList<LoanPayingDataList> PayList = new ArrayList<>();
    ArrayList<ThreeColumnsList> ShowList = new ArrayList<>();
    ThreeColumnsListAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_history);

        Intent intent = getIntent();
        LoanMode = intent.getStringExtra(LOAN_TYPE);

        PayListView = findViewById(R.id.pay_history_list);
        myAdapter = new ThreeColumnsListAdapter(this,ShowList);
        PayListView.setAdapter(myAdapter);

        PayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(PayHistory.this,SingleLoan.class);

                intent1.putExtra(LOAN_TYPE,LoanMode);
                intent1.putExtra(LOAN_ID,PayList.get(position).getLoanId());

                startActivity(intent1);
            }
        });

        addData();

    }

    private void addData() {
        LoanPayingDataList item = new LoanPayingDataList("ABC","ABC",new Date(),1000,"Ram");
        PayList.add(item);
        PayList.add(item);
        PayList.add(item);
        PayList.add(item);
        PayList.add(item);

        ThreeColumnsList item2 = new ThreeColumnsList("12/12/1389","1000","Ram");
        ShowList.add(item2);
        ShowList.add(item2);
        ShowList.add(item2);
        ShowList.add(item2);
        ShowList.add(item2);
        ShowList.add(item2);

    }

    public void FilterPayHistory(View view){
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
                singleChoose.add(0,ChooseDateDialogSingle(PayHistory.this,SingleShowDateFilter));
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
                fromChoose.add(0,ChooseDateDialogFrom(PayHistory.this,FromShowDate));
            }
        });

        Button ToChooseDate = customFilterView.findViewById(R.id.to_choose_date_filter);
        final TextView ToShowDate = customFilterView.findViewById(R.id.to_show_date_filter);

        final ArrayList<Boolean> toChoose = new ArrayList<>();
        toChoose.add(false);

        ToChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChoose.add(0,ChooseDateDialogTo(PayHistory.this,ToShowDate));
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
