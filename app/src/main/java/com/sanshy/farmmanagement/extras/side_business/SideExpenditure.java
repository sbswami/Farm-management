package com.sanshy.farmmanagement.extras.side_business;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.sanshy.farmmanagement.MyStatic;
import com.sanshy.farmmanagement.R;
import com.sanshy.farmmanagement.extras.SingleExpOrInc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.AMOUNT_SIDE_FOR_ALL;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialog;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.DATE_SIDE_FOR_ALL;
import static com.sanshy.farmmanagement.MyStatic.DateToString2;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ID_SIDE_FOR_ALL;
import static com.sanshy.farmmanagement.MyStatic.REMARK_SIDE_FOR_ALL;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.TimeStampKey;
import static com.sanshy.farmmanagement.MyStatic.setSnipper;
import static com.sanshy.farmmanagement.MyStatic.sideExpLocation;
import static com.sanshy.farmmanagement.MyStatic.sideExpRemark;
import static com.sanshy.farmmanagement.MyStatic.sideReportLocation;
import static com.sanshy.farmmanagement.MyStatic.staticDate;

public class SideExpenditure extends AppCompatActivity {

    EditText Amount;
    AutoCompleteTextView Remark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_expenditure);

        Amount = findViewById(R.id.side_expenditure_amount);
        Remark = findViewById(R.id.side_expenditure_remark);

        ShowDate = findViewById(R.id.show_date);

    }

    ArrayList<String> RemarkList = new ArrayList<>();
    @Override
    protected void onStart() {
        super.onStart();
        sideExpRemark.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    RemarkList = (ArrayList<String>) dataSnapshot.getValue();
                    setSnipper(SideExpenditure.this,Remark,RemarkList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    boolean isDate = false;
    TextView ShowDate;
    public void dateChooserBt(View view){
        isDate = ChooseDateDialog(this,ShowDate);
    }

    public void HistoryBt(View view){
        startActivity(new Intent(this,SideExpenditureHistory.class));
    }
    String RemarkString;
    String AmountString;
    String Id;
    public void SaveBt(View view){
        RemarkString = Remark.getText().toString();
        AmountString = Amount.getText().toString();
        if (!isDate){
            ShowDialog(this,getString(R.string.please_choose_any_date));
            return;
        }
        if (RemarkString.isEmpty()){
            Remark.setError(getString(R.string.fill_it));
            return;
        }
        if (AmountString.isEmpty()){
            Amount.setError(getString(R.string.fill_it));
            return;
        }
        if (!RemarkList.contains(RemarkString)){
            RemarkList.add(RemarkString);
            sideExpRemark.setValue(RemarkList);
        }
        SaveIt();

    }


    private void SaveIt() {

        ShowProgress(this);
        String key = TimeStampKey(new Date());
        Id = RemarkString+"_"+key;
        SingleExpOrInc item = new SingleExpOrInc(Id,RemarkString,Double.parseDouble(AmountString),staticDate);

        sideReportLocation.document(DateToString2(staticDate)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Map<String,Object> DataMap = new HashMap<>();
                if (documentSnapshot.exists()){
                    double oldValue = documentSnapshot.getDouble(MyStatic.EXPENDITURE_FIRESTORE_FIELD_KEY);

                    double newValue = oldValue+Double.parseDouble(AmountString);
                    DataMap.put(MyStatic.EXPENDITURE_FIRESTORE_FIELD_KEY,newValue);

                    sideReportLocation.document(DateToString2(staticDate)).update(DataMap);

                }else{
                    DataMap.put(MyStatic.EXPENDITURE_FIRESTORE_FIELD_KEY,Double.parseDouble(AmountString));
                    DataMap.put(MyStatic.INCOME_FIRESTORE_FIELD_KEY,0.0);
                    DataMap.put(DATE,staticDate);

                    sideReportLocation.document(DateToString2(staticDate)).set(DataMap);
                }
                HideProgress();
            }
        });

        Map<String,Object> SaveItem = new HashMap<>();
        SaveItem.put(ID_SIDE_FOR_ALL,item.getId());
        SaveItem.put(REMARK_SIDE_FOR_ALL,item.getRemark());
        SaveItem.put(AMOUNT_SIDE_FOR_ALL,item.getAmount());
        SaveItem.put(DATE_SIDE_FOR_ALL,item.getDate());


        sideExpLocation.document(Id).set(SaveItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    ShowDialog(SideExpenditure.this,getString(R.string.saved));
                    Remark.setText("");
                    Amount.setText("");
                }
                else{
                    ShowDialog(SideExpenditure.this,getString(R.string.try_again_later_or_send_feedback));
                }
                HideProgress();
            }
        });


    }

    public void CancelBt(View view){
        finish();
    }


}
