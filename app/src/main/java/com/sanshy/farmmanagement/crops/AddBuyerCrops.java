package com.sanshy.farmmanagement.crops;

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
import static com.sanshy.farmmanagement.MyStatic.buyerList;
import static com.sanshy.farmmanagement.MyStatic.buyerLocation;

public class    AddBuyerCrops extends AppCompatActivity {

    EditText BuyerCropsName,BuyerCropsPhoneNumber, BuyerCropsAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_buyer_crops);

        BuyerCropsName = findViewById(R.id.add_buyer_crops_name);
        BuyerCropsPhoneNumber = findViewById(R.id.add_buyer_crops_phone);
        BuyerCropsAddress = findViewById(R.id.add_buyer_crops_address);

    }
    String BuyerNString;
    String BuyerCropPNString;
    String BuyerCropsAString;
    public void AddBuyerCropsBt(View view){
        BuyerNString = BuyerCropsName.getText().toString();
        BuyerCropPNString = BuyerCropsPhoneNumber.getText().toString();
        BuyerCropsAString = BuyerCropsAddress.getText().toString();

        if (BuyerNString.isEmpty()){
            BuyerCropsName.setError(getString(R.string.fill_it));
            return;
        }
        if (BuyerCropPNString.isEmpty()){
            BuyerCropsPhoneNumber.setError(getString(R.string.fill_it));
            return;
        }
        if (BuyerCropPNString.length()!=10){
            ShowDialog(this,getString(R.string.phone_number_should_in_10_digits));
            return;
        }

        ShowProgress(this);
        buyerLocation.document(BuyerNString).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                HideProgress();
                if (documentSnapshot.exists()){
                    ShowDialog(AddBuyerCrops.this,getString(R.string.person_with_same_name_already_exists));
                }else {
                    SaveData();
                }
            }
        });
    }

    public void SaveData(){
        ShowProgress(this);

        SingleBuyerCropsData item = new SingleBuyerCropsData(BuyerNString,BuyerNString,Long.parseLong(BuyerCropPNString),BuyerCropsAString,0,new Date());

        Map<String, Object> SaveItem = new HashMap<>();
        SaveItem.put(MyStatic.ID,item.getBuyerCropsId());
        SaveItem.put(MyStatic.NAME,item.getBuyerCropsName());
        SaveItem.put(MyStatic.PHONE,item.getBuyerCropsPhone());
        SaveItem.put(MyStatic.ADDRESS,item.getBuyerCropsAddress());
        SaveItem.put(MyStatic.REMAINING_AMOUNT,item.getBuyerCropsRemainingPayment());
        SaveItem.put(MyStatic.DATE,item.getBuyerCropsAddingDate());

        buyerLocation.document(BuyerNString).set(SaveItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    ShowDialog(AddBuyerCrops.this,getString(R.string.saved));
                    BuyerCropsName.setText("");
                    BuyerCropsPhoneNumber.setText("");
                    BuyerCropsAddress.setText("");
                    buyerList.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<String> buyersList = new ArrayList<>();
                            if (dataSnapshot.exists()){
                                buyersList = (ArrayList<String>) dataSnapshot.getValue();
                            }
                            buyersList.add(BuyerNString);
                            buyerList.setValue(buyersList);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    ShowDialog(AddBuyerCrops.this,getString(R.string.try_again_later_or_send_feedback));
                }
                HideProgress();
            }
        });
    }
    public void AddBuyerCropsCancleBt(View view){
        finish();
    }
}
