package com.sanshy.farmmanagement.crops;

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
import com.sanshy.farmmanagement.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.ADDRESS;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ID;
import static com.sanshy.farmmanagement.MyStatic.NAME;
import static com.sanshy.farmmanagement.MyStatic.PHONE;
import static com.sanshy.farmmanagement.MyStatic.REMAINING_AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_ID;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_NAME;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_REMAINING_AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_TYPE;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.serviceProviderList;
import static com.sanshy.farmmanagement.MyStatic.serviceProviderLocation;

public class ServiceProviders extends AppCompatActivity {

    AutoCompleteTextView suggestionBox;

    ListView ServiceProviderListView;
    SingleBuyerCropListAdapter myAdapter;
    ArrayList<SingleBuyerCropsList> ServiceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_providers);

        ServiceProviderListView = findViewById(R.id.service_provider_list);

        suggestionBox = findViewById(R.id.service_provider_search);

        myAdapter = new SingleBuyerCropListAdapter(this,ServiceList);

        ServiceProviderListView.setAdapter(myAdapter);

        ServiceProviderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ServiceProviders.this,ServiceProviderSingle.class);

                intent.putExtra(SERVICE_PROVIDER_NAME,ServiceList.get(position).getBuyerName());
                intent.putExtra(SERVICE_PROVIDER_ID,ServiceList.get(position).getBuyerId());
                intent.putExtra(SERVICE_PROVIDER_REMAINING_AMOUNT,String.valueOf(ServiceList.get(position).getRemainingAmount()));

                startActivity(intent);
            }
        });

        ServiceProviderListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ServiceProviders.this);

                builder.setTitle(R.string.delete);
                builder.setMessage(R.string.are_you_sure_you_want_to_delete);

                builder.setNegativeButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Double.parseDouble(ServiceList.get(position).getRemainingAmount())!=0){
                            ShowDialog(ServiceProviders.this,getString(R.string.can_not_delete_bacause_payment_still_remaining));
                        }
                        else{
                            ShowProgress(ServiceProviders.this);
                            serviceProviderLocation.document(ServiceList.get(position).getBuyerId()).delete();
                            serviceProviderList.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        ArrayList<String> Servicer = (ArrayList<String>) dataSnapshot.getValue();

                                        Servicer.remove(ServiceList.get(position).getBuyerId());

                                        serviceProviderList.setValue(Servicer);
                                        ShowDialog(ServiceProviders.this,getString(R.string.deleted));
                                    }
                                    myAdapter.notifyDataSetChanged();
                                    HideProgress();
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

    ArrayList<String> Hint = new ArrayList<>();
    ArrayAdapter<String> serviceAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        addServiceProviderData();

        serviceProviderList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Hint = (ArrayList<String>) dataSnapshot.getValue();
                    assert Hint != null;
                    serviceAdapter = new ArrayAdapter<String>(
                            ServiceProviders.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            Hint);
                    suggestionBox.setAdapter(serviceAdapter);

                    suggestionBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            addServiceProviderData(suggestionBox.getText().toString());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void addServiceProviderData(){
        ServiceList.clear();
        ShowProgress(this);
        serviceProviderLocation.orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this,new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ServiceList.clear();
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){

                    Map<String, Object> CloudItem = queryDocumentSnapshot.getData();

                    SingleServiceProviderData Data = new SingleServiceProviderData(
                            CloudItem.get(ID).toString(),
                            CloudItem.get(NAME).toString(),
                            (Long) CloudItem.get(PHONE),
                            CloudItem.get(ADDRESS).toString(),
                            CloudItem.get(SERVICE_TYPE).toString(),
                            (Double) CloudItem.get(REMAINING_AMOUNT),
                            (Date) CloudItem.get(DATE)
                    );

                    SingleBuyerCropsList item = new SingleBuyerCropsList(Data.getServiceProviderId(),Data.getServiceProviderName(),String.valueOf(Data.getServiceProviderPhone()),String.valueOf(Data.getServiceProviderRemainingPayment()));
                    ServiceList.add(item);
                }
                myAdapter.notifyDataSetChanged();
                HideProgress();
            }
        });

    }
    public void addServiceProviderData(String ServiceId){
        ServiceList.clear();
        ShowProgress(this);
        serviceProviderLocation.document(ServiceId).get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> CloudItem = documentSnapshot.getData();

                SingleServiceProviderData Data = new SingleServiceProviderData(
                        CloudItem.get(ID).toString(),
                        CloudItem.get(NAME).toString(),
                        (Long) CloudItem.get(PHONE),
                        CloudItem.get(ADDRESS).toString(),
                        CloudItem.get(SERVICE_TYPE).toString(),
                        (Double) CloudItem.get(REMAINING_AMOUNT),
                        (Date) CloudItem.get(DATE)
                );

                SingleBuyerCropsList item = new SingleBuyerCropsList(Data.getServiceProviderId(),Data.getServiceProviderName(),String.valueOf(Data.getServiceProviderPhone()),String.valueOf(Data.getServiceProviderRemainingPayment()));
                ServiceList.add(item);

                myAdapter.notifyDataSetChanged();
                HideProgress();
            }
        });

    }

    public void addServiceProviderBt(View view){
        startActivity(new Intent(this,AddServiceProvider.class));
    }
}
