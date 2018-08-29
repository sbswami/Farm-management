package com.sanshy.farmmanagement.loans;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sanshy.farmmanagement.R;
import com.sanshy.farmmanagement.crops.IncomeCropHistory;
import com.sanshy.farmmanagement.crops.SingleFourColumnsList;
import com.sanshy.farmmanagement.crops.SingleFourColumnsListAdapter;

import java.util.ArrayList;
import java.util.Date;

import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogFrom;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogSingle;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogTo;
import static com.sanshy.farmmanagement.MyStatic.DateToString;
import static com.sanshy.farmmanagement.MyStatic.LOAN_ID;
import static com.sanshy.farmmanagement.MyStatic.LOAN_TYPE;

public class LoansList extends AppCompatActivity {

    String LoanMode;
    ListView LoanListView;
    SingleFourColumnsListAdapter myAdapter;
    ArrayList<SingleFourColumnsList> FourList = new ArrayList<>();
    ArrayList<OneLoanPerperties> LoanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loans_list);

        Intent intent = getIntent();

        LoanMode = intent.getStringExtra(LOAN_TYPE);

        LoanListView = findViewById(R.id.loan_list);
        myAdapter = new SingleFourColumnsListAdapter(this,FourList);
        LoanListView.setAdapter(myAdapter);

        LoanListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent1 = new Intent(LoansList.this,SingleLoan.class);
                    intent1.putExtra(LOAN_ID,LoanList.get(position).getId());
                    intent1.putExtra(LOAN_TYPE,LoanMode);
                    startActivity(intent1);
            }
        });

        LoanListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoansList.this);

                LayoutInflater layoutInflater = (LayoutInflater) LoansList.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.custom_show_loan_detail,null);


                TextView Remark = customView.findViewById(R.id.remark_dialog);
                TextView Amount = customView.findViewById(R.id.amount_dialog);
                TextView Date = customView.findViewById(R.id.date_dialog);
                TextView LoanPerson = customView.findViewById(R.id.loan_person_dialog);
                TextView InterestRate = customView.findViewById(R.id.interest_rate_dialog);
                TextView InterestRateType = customView.findViewById(R.id.interest_rate_type_dialog);
                TextView InterestType = customView.findViewById(R.id.interest_type_dialog);

                Remark.setText(LoanList.get(position).getRemark());
                Amount.setText(String.valueOf(LoanList.get(position).getLoanAmount()));
                Date.setText(DateToString(LoanList.get(position).getStartDate()));
                LoanPerson.setText(LoanList.get(position).getLoanPersonName());
                InterestRate.setText(String.valueOf(LoanList.get(position).getInterestRate()));
                String InterestRateTypeString;
                if (LoanList.get(position).isPerMonthInterest()){
                    InterestRateTypeString = getString(R.string.per_month);
                }else {
                    InterestRateTypeString = getString(R.string.per_year);
                }
                InterestRateType.setText(InterestRateTypeString);
                String InterestTypeString;
                if (LoanList.get(position).isYearlyCompound()){
                    InterestTypeString = getString(R.string.yearly_compound);
                }else if (LoanList.get(position).isSixMonthlyCompound()){
                    InterestTypeString = getString(R.string.six_monthly_compound);
                }else if (LoanList.get(position).isThreeMonthlyCompound()){
                    InterestTypeString = getString(R.string.three_monthly_compound);
                }else{
                    InterestTypeString = getString(R.string.simple_interest);
                }
                InterestType.setText(InterestTypeString);

                builder.setView(customView);
                builder.setPositiveButton(getString(R.string.ok),null);
                builder.setNegativeButton(getString(R.string.undo), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                return true;
            }
        });

        addData();

    }

    private void addData() {
        SingleFourColumnsList item = new SingleFourColumnsList("10/07/2018","ABC","100000","ABC");
        FourList.add(item);
        FourList.add(item);
        FourList.add(item);
        FourList.add(item);
        FourList.add(item);

        OneLoanPerperties item2 = new OneLoanPerperties("ABC","ABC",100000,12,false,false,true,false,false,new Date(),"ABC","ME");
        LoanList.add(item2);
        LoanList.add(item2);
        LoanList.add(item2);
        LoanList.add(item2);
        LoanList.add(item2);
        LoanList.add(item2);

        myAdapter.notifyDataSetChanged();
    }

    public void filterLoanList(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customFilterView = layoutInflater.inflate(R.layout.custom_filter_for_loan_list,null);

        AutoCompleteTextView RemarkFilter = customFilterView.findViewById(R.id.remark_filter);
        AutoCompleteTextView LoanPersonFilter = customFilterView.findViewById(R.id.loan_person_filter);

        RadioGroup FilterInterestRateType = customFilterView.findViewById(R.id.filter_interest_rate_type);
        RadioButton FilterMonthly = customFilterView.findViewById(R.id.filter_monthly_interest_rate);
        RadioButton FilterYearly = customFilterView.findViewById(R.id.filter_yearly_interest_rate);

        EditText MinAmountFilter = customFilterView.findViewById(R.id.min_amount_filter);
        EditText MaxAmountFilter = customFilterView.findViewById(R.id.max_amount_filter);

        EditText MinInterestRateFilter = customFilterView.findViewById(R.id.min_interest_rate_filter);
        EditText MaxInterestRateFilter = customFilterView.findViewById(R.id.max_interest_rate_filter);

        RadioGroup FilterPaidOrNot = customFilterView.findViewById(R.id.filter_paid_or_not);
        RadioButton Paid = customFilterView.findViewById(R.id.filter_paid);
        RadioButton Unpaid = customFilterView.findViewById(R.id.filter_unpaid);

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
                singleChoose.add(0,ChooseDateDialogSingle(LoansList.this,SingleShowDateFilter));
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
                fromChoose.add(0,ChooseDateDialogFrom(LoansList.this,FromShowDate));
            }
        });

        Button ToChooseDate = customFilterView.findViewById(R.id.to_choose_date_filter);
        final TextView ToShowDate = customFilterView.findViewById(R.id.to_show_date_filter);

        final ArrayList<Boolean> toChoose = new ArrayList<>();
        toChoose.add(false);

        ToChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChoose.add(0,ChooseDateDialogTo(LoansList.this,ToShowDate));
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
