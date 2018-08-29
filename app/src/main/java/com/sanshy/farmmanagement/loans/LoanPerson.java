package com.sanshy.farmmanagement.loans;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.sanshy.farmmanagement.R;

import java.util.ArrayList;
import java.util.Date;

public class LoanPerson extends AppCompatActivity {

    ListView LoanPersonListView;
    SinglePersonLoanAdapter myAdapter;
    ArrayList<SinglePersonLoan> LoanPersonList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_person);

        LoanPersonListView = findViewById(R.id.loan_person_list);

        myAdapter = new SinglePersonLoanAdapter(this,LoanPersonList);

        LoanPersonListView.setAdapter(myAdapter);

        addListData();

    }

    private void addListData() {
        SinglePersonLoan lister = new SinglePersonLoan("ABC","NAME",1234567890,"ADSD",new Date());
        LoanPersonList.add(lister);
        LoanPersonList.add(lister);
        LoanPersonList.add(lister);
        LoanPersonList.add(lister);
        LoanPersonList.add(lister);

        myAdapter.notifyDataSetChanged();
    }

    public void addLoanPerson(View view){
        startActivity(new Intent(this,AddLoanPerson.class));
    }
}
