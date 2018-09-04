package com.sanshy.farmmanagement.loans;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.sanshy.farmmanagement.MyStatic;
import com.sanshy.farmmanagement.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.loanPersonList;
import static com.sanshy.farmmanagement.MyStatic.loanPersonLocation;

public class AddLoanPerson extends AppCompatActivity {

    EditText LoanPersonName, LoanPersonPhoneNumber, LoanPersonAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loan_person);

        LoanPersonName = findViewById(R.id.add_loan_person_name);
        LoanPersonPhoneNumber = findViewById(R.id.add_loan_person_phone);
        LoanPersonAddress = findViewById(R.id.add_loan_person_address);

    }

    String LoanPersonNString;
    String LoanPersonPNString;
    String LoanPersonAString;

    public void AddLoanPersonBt(View view){
        LoanPersonNString = LoanPersonName.getText().toString();
        LoanPersonPNString = LoanPersonPhoneNumber.getText().toString();
        LoanPersonAString = LoanPersonAddress.getText().toString();

        if (LoanPersonNString.isEmpty()){
            LoanPersonName.setError(getString(R.string.fill_it));
            return;
        }
        if (LoanPersonPNString.isEmpty()){
            LoanPersonPhoneNumber.setError(getString(R.string.fill_it));
            return;
        }
        if (LoanPersonPNString.length()!=10){
            ShowDialog(this,getString(R.string.phone_number_should_in_10_digits));
            return;
        }

        ShowProgress(this);
        loanPersonLocation.document(LoanPersonNString).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                HideProgress();
                if (documentSnapshot.exists()){
                    ShowDialog(AddLoanPerson.this,getString(R.string.person_with_same_name_already_exists));
                }else {
                    SaveData();
                }
            }
        });
    }


    public void SaveData(){
        ShowProgress(this);

        SinglePersonLoan item = new SinglePersonLoan(LoanPersonNString,
                LoanPersonNString,
                Long.parseLong(LoanPersonPNString),
                LoanPersonAString,
                new Date(),
                false);


        Map<String, Object> SaveItem = new HashMap<>();
        SaveItem.put(MyStatic.ID,item.getId());
        SaveItem.put(MyStatic.NAME,item.getName());
        SaveItem.put(MyStatic.PHONE,item.getPhone());
        SaveItem.put(MyStatic.ADDRESS,item.getAddress());
        SaveItem.put(MyStatic.DATE,item.getDateOfCreation());
        SaveItem.put(MyStatic.USED,item.isUsed());

        loanPersonLocation.document(LoanPersonNString).set(SaveItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    ShowDialog(AddLoanPerson.this,getString(R.string.saved));
                    LoanPersonName.setText("");
                    LoanPersonPhoneNumber.setText("");
                    LoanPersonAddress.setText("");
                    loanPersonList.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<String> CloudList = new ArrayList<>();
                            if (dataSnapshot.exists()){
                                CloudList = (ArrayList<String>) dataSnapshot.getValue();
                            }
                            CloudList.add(LoanPersonNString);
                            loanPersonList.setValue(CloudList);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    ShowDialog(AddLoanPerson.this,getString(R.string.try_again_later_or_send_feedback));
                }
                HideProgress();
            }
        });
    }

    public void AddLoanPersonCancelBt(View view){
        finish();
    }}
