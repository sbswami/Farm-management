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
import static com.sanshy.farmmanagement.MyStatic.SERVICE_TYPE;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.partnerList;
import static com.sanshy.farmmanagement.MyStatic.partnerLocation;

public class AddPartnerCrop extends AppCompatActivity {

    EditText PartnerName,PartnerPhoneNumber,PartnerAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_partner_crop);

        PartnerName = findViewById(R.id.add_partner_name);
        PartnerPhoneNumber = findViewById(R.id.add_partner_phone);
        PartnerAddress = findViewById(R.id.add_partner_address);

    }

    String PartnerNString;
    String PartnerCropPNString;
    String PartnerCropsAString;
    public void AddPartnerBt(View view){
        PartnerNString = PartnerName.getText().toString();
        PartnerCropPNString = PartnerPhoneNumber.getText().toString();
        PartnerCropsAString = PartnerAddress.getText().toString();

        if (PartnerNString.isEmpty()){
            PartnerName.setError(getString(R.string.fill_it));
            return;
        }
        if (PartnerCropPNString.isEmpty()){
            PartnerPhoneNumber.setError(getString(R.string.fill_it));
            return;
        }
        if (PartnerCropPNString.length()!=10){
            ShowDialog(this,getString(R.string.phone_number_should_in_10_digits));
            return;
        }

        ShowProgress(this);
        partnerLocation.document(PartnerNString).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                HideProgress();
                if (documentSnapshot.exists()){
                    ShowDialog(AddPartnerCrop.this,getString(R.string.person_with_same_name_already_exists));
                }else {
                    SaveData();
                }
            }
        });
    }


    public void SaveData(){
        ShowProgress(this);

        SinglePartnerData item = new SinglePartnerData(PartnerNString, PartnerNString,Long.parseLong(PartnerCropPNString), PartnerCropsAString,new Date());


        Map<String, Object> SaveItem = new HashMap<>();
        SaveItem.put(MyStatic.ID,item.getPartnerCropId());
        SaveItem.put(MyStatic.NAME,item.getPartnerCropName());
        SaveItem.put(MyStatic.PHONE,item.getPartnerCropPhone());
        SaveItem.put(MyStatic.ADDRESS,item.getPartnerCropAddress());
        SaveItem.put(MyStatic.DATE,item.getPartnerCropAddingDate());

        partnerLocation.document(PartnerNString).set(SaveItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    ShowDialog(AddPartnerCrop.this,getString(R.string.saved));
                    PartnerName.setText("");
                    PartnerPhoneNumber.setText("");
                    PartnerAddress.setText("");
                    partnerList.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<String> PartnersList = new ArrayList<>();
                            if (dataSnapshot.exists()){
                                PartnersList = (ArrayList<String>) dataSnapshot.getValue();
                            }
                            PartnersList.add(PartnerNString);
                            partnerList.setValue(PartnersList);
                         }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    ShowDialog(AddPartnerCrop.this,getString(R.string.try_again_later_or_send_feedback));
                }
                HideProgress();
            }
        });
    }

    public void AddPartnerCancelBt(View view){
        finish();
    }
}
