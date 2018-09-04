package com.sanshy.farmmanagement.loans;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanshy.farmmanagement.MyStatic;
import com.sanshy.farmmanagement.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.ADDRESS;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ID;
import static com.sanshy.farmmanagement.MyStatic.LOAN_PERSON_ID;
import static com.sanshy.farmmanagement.MyStatic.NAME;
import static com.sanshy.farmmanagement.MyStatic.PHONE;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.USED;
import static com.sanshy.farmmanagement.MyStatic.loanPersonList;
import static com.sanshy.farmmanagement.MyStatic.loanPersonLocation;
import static com.sanshy.farmmanagement.MyStatic.singleGetLoanPersonLocation;

public class LoanPerson extends AppCompatActivity {

    ListView LoanPersonListView;
    SinglePersonLoanAdapter myAdapter;
    ArrayList<SinglePersonLoan> LoanPersonList = new ArrayList<>();

    AutoCompleteTextView suggestionBox;
    ArrayList<String> Hint = new ArrayList<>();
    ArrayAdapter<String> loanPersonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_person);

        LoanPersonListView = findViewById(R.id.loan_person_list);

        myAdapter = new SinglePersonLoanAdapter(this,LoanPersonList);

        LoanPersonListView.setAdapter(myAdapter);
        
        suggestionBox = findViewById(R.id.loan_person_search);

        LoanPersonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(LoanPerson.this,SingleLoanPerson.class);
                intent1.putExtra(LOAN_PERSON_ID,LoanPersonList.get(position).getId());
                startActivity(intent1);
            }
        });

        LoanPersonListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final String ThisLoanPerson = LoanPersonList.get(position).getName();
                final boolean ThisIsUsed = LoanPersonList.get(position).isUsed();

                AlertDialog.Builder builder = new AlertDialog.Builder(LoanPerson.this);

                builder.setTitle(R.string.delete);
                builder.setMessage(R.string.are_you_sure_you_want_to_delete);

                builder.setNegativeButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ThisIsUsed){
                            ShowDialog(LoanPerson.this,getString(R.string.can_not_delete));
                        }
                        else{
                            ShowProgress(LoanPerson.this);
                            loanPersonLocation.document(ThisLoanPerson).delete();
                            LoanPersonList.remove(position);
                            Hint.remove(ThisLoanPerson);
                            myAdapter.notifyDataSetChanged();
                            loanPersonList.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        try{
                                            ArrayList<String> PList = (ArrayList<String>) dataSnapshot.getValue();
                                            PList.remove(ThisLoanPerson);
                                            loanPersonList.setValue(PList);
                                        }catch (NullPointerException ex){}
                                        HideProgress();
                                        ShowDialog(LoanPerson.this,getString(R.string.deleted));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
                builder.setPositiveButton(getString(R.string.close),null);

                builder.create().show();


                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        addLoanPersonData();

        loanPersonList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Hint = (ArrayList<String>) dataSnapshot.getValue();
                    assert Hint != null;
                    loanPersonAdapter = new ArrayAdapter<String>(
                            LoanPerson.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            Hint);
                    suggestionBox.setAdapter(loanPersonAdapter);

                    suggestionBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            addLoanPersonData(suggestionBox.getText().toString());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void addLoanPersonData(){
        LoanPersonList.clear();
        ShowProgress(this);
        loanPersonLocation.get().addOnSuccessListener(this,new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                LoanPersonList.clear();
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){

                    Map<String, Object> CloudItem = queryDocumentSnapshot.getData();

                    SinglePersonLoan Data = new SinglePersonLoan(
                            CloudItem.get(ID).toString(),
                            CloudItem.get(NAME).toString(),
                            (Long) CloudItem.get(PHONE),
                            CloudItem.get(ADDRESS).toString(),
                            (Date) CloudItem.get(DATE),
                            (Boolean) CloudItem.get(USED)
                    );

                    LoanPersonList.add(Data);
                }
                myAdapter.notifyDataSetChanged();
                HideProgress();
            }
        });

    }
    public void addLoanPersonData(String personId){
        LoanPersonList.clear();
        ShowProgress(this);
        loanPersonLocation.document(personId).get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> CloudItem = documentSnapshot.getData();

                SinglePersonLoan Data = new SinglePersonLoan(
                        CloudItem.get(ID).toString(),
                        CloudItem.get(NAME).toString(),
                        (Long) CloudItem.get(PHONE),
                        CloudItem.get(ADDRESS).toString(),
                        (Date) CloudItem.get(DATE),
                        (Boolean) CloudItem.get(USED)
                );

               LoanPersonList.add(Data);

                myAdapter.notifyDataSetChanged();
                HideProgress();
            }
        });

    }

    public void addLoanPerson(View view){
        startActivity(new Intent(this,AddLoanPerson.class));
    }
}
