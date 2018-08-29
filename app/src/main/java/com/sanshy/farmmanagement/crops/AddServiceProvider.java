package com.sanshy.farmmanagement.crops;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
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
import static com.sanshy.farmmanagement.MyStatic.serviceProviderList;
import static com.sanshy.farmmanagement.MyStatic.serviceProviderLocation;
import static com.sanshy.farmmanagement.MyStatic.serviceType;
import static com.sanshy.farmmanagement.MyStatic.setSnipper;

public class AddServiceProvider extends AppCompatActivity {

    EditText ServiceProviderName,ServiceProviderPhoneNumber, ServiceProviderAddress;
    AutoCompleteTextView ServiceType;

    ArrayList<String> HintList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service_provider);

        ServiceProviderName = findViewById(R.id.add_service_provider_name);
        ServiceProviderPhoneNumber = findViewById(R.id.add_service_provider_phone);
        ServiceProviderAddress = findViewById(R.id.add_service_provider_address);

        ServiceType = findViewById(R.id.add_service_provider_type);

    }

    @Override
    protected void onStart() {
        super.onStart();

        serviceType.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HintList.clear();
                if (dataSnapshot.exists()){
                    HintList = (ArrayList<String>) dataSnapshot.getValue();
                    setSnipper(AddServiceProvider.this,ServiceType,HintList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    String NameString;
    String PhoneNString;
    String AddressString;
    String ServiceTypeString;

    public void AddServiceProviderBt(View view){
        NameString = ServiceProviderName.getText().toString();
        PhoneNString = ServiceProviderPhoneNumber.getText().toString();
        AddressString = ServiceProviderAddress.getText().toString();
        ServiceTypeString = ServiceType.getText().toString();

        if (NameString.isEmpty()){
            ServiceProviderName.setError(getString(R.string.fill_it));
            return;
        }
        if (PhoneNString.isEmpty()){
            ServiceProviderPhoneNumber.setError(getString(R.string.fill_it));
            return;
        }
        if (PhoneNString.length()!=10){
            ShowDialog(this,getString(R.string.phone_number_should_in_10_digits));
            return;
        }
        if (!(HintList.contains(ServiceTypeString))){
            HintList.add(ServiceTypeString);
            serviceType.setValue(HintList);
        }
        ShowProgress(this);
        serviceProviderLocation.document(NameString).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                HideProgress();
                if (documentSnapshot.exists()){
                    ShowDialog(AddServiceProvider.this,getString(R.string.person_with_same_name_already_exists));
                }else{
                    SaveIt();
                }
            }
        });
    }

    private void SaveIt() {
        ShowProgress(this);

        SingleServiceProviderData item = new SingleServiceProviderData(NameString,NameString,Long.parseLong(PhoneNString),AddressString,ServiceTypeString,0,new Date());


        Map<String, Object> SaveItem = new HashMap<>();
        SaveItem.put(MyStatic.ID,item.getServiceProviderId());
        SaveItem.put(MyStatic.NAME,item.getServiceProviderName());
        SaveItem.put(MyStatic.PHONE,item.getServiceProviderPhone());
        SaveItem.put(MyStatic.ADDRESS,item.getServiceProviderAddress());
        SaveItem.put(SERVICE_TYPE,item.getServiceProviderServiceType());
        SaveItem.put(MyStatic.REMAINING_AMOUNT,item.getServiceProviderRemainingPayment());
        SaveItem.put(MyStatic.DATE,item.getServiceProviderAddingDate());


        serviceProviderLocation.document(NameString).set(SaveItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                HideProgress();
                if (task.isSuccessful()){
                    ShowDialog(AddServiceProvider.this,getString(R.string.saved));
                    ServiceProviderName.setText("");
                    ServiceProviderPhoneNumber.setText("");
                    ServiceProviderAddress.setText("");
                    ServiceProviderAddress.setText("");

                    serviceProviderList.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<String> SPsList = new ArrayList<>();
                            if (dataSnapshot.exists()){
                                SPsList = (ArrayList<String>) dataSnapshot.getValue();
                            }
                            SPsList.add(NameString);
                            serviceProviderList.setValue(SPsList);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else {
                    ShowDialog(AddServiceProvider.this,getString(R.string.try_again_later_or_send_feedback));
                }
            }
        });
    }

    public void AddServiceProviderCancleBt(View view){
        finish();
    }
}
