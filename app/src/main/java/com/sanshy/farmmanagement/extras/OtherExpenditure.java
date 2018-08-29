package com.sanshy.farmmanagement.extras;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sanshy.farmmanagement.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.AMOUNT_SIDE_FOR_ALL;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialog;
import static com.sanshy.farmmanagement.MyStatic.DATE_SIDE_FOR_ALL;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ID_SIDE_FOR_ALL;
import static com.sanshy.farmmanagement.MyStatic.OtherExpRemark;
import static com.sanshy.farmmanagement.MyStatic.REMARK_SIDE_FOR_ALL;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.TimeStampKey;
import static com.sanshy.farmmanagement.MyStatic.otherExpLocation;
import static com.sanshy.farmmanagement.MyStatic.setSnipper;
import static com.sanshy.farmmanagement.MyStatic.staticDate;

public class OtherExpenditure extends AppCompatActivity {

    EditText Amount;
    AutoCompleteTextView Remark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_expenditure);

        Amount = findViewById(R.id.other_expenditure_amount);
        Remark = findViewById(R.id.other_expenditure_remark);

        ShowDate = findViewById(R.id.show_date);

    }

    ArrayList<String> RemarkList = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        OtherExpRemark.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    RemarkList = (ArrayList<String>) dataSnapshot.getValue();
                    setSnipper(OtherExpenditure.this,Remark,RemarkList);
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
        startActivity(new Intent(this,OtherExpenditureList.class));
    }
    String RemarkString;
    String AmountString;
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
            OtherExpRemark.setValue(RemarkList);
        }
        SaveIt();

    }

    private void SaveIt() {
        ShowProgress(this);
        String key = TimeStampKey(new Date());
        String Id = RemarkString + "_" + key;
        SingleExpOrInc item = new SingleExpOrInc(Id, RemarkString, Double.parseDouble(AmountString), staticDate);

        Map<String,Object> SaveItem = new HashMap<>();
        SaveItem.put(ID_SIDE_FOR_ALL,item.getId());
        SaveItem.put(REMARK_SIDE_FOR_ALL,item.getRemark());
        SaveItem.put(AMOUNT_SIDE_FOR_ALL,item.getAmount());
        SaveItem.put(DATE_SIDE_FOR_ALL,item.getDate());

        otherExpLocation.document(Id).set(SaveItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    ShowDialog(OtherExpenditure.this, getString(R.string.saved));
                    Remark.setText("");
                    Amount.setText("");
                } else {
                    ShowDialog(OtherExpenditure.this, getString(R.string.try_again_later_or_send_feedback));
                }
                HideProgress();
            }
        });
    }
    public void CancelBt(View view){
        finish();
    }

}
