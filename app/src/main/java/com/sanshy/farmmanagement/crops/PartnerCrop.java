package com.sanshy.farmmanagement.crops;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.MessagePattern;
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
import static com.sanshy.farmmanagement.MyStatic.NAME;
import static com.sanshy.farmmanagement.MyStatic.PARTNER;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_COUNT;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_1;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_2;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_3;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_4;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_5;
import static com.sanshy.farmmanagement.MyStatic.PHONE;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.partnerList;
import static com.sanshy.farmmanagement.MyStatic.partnerLocation;
import static com.sanshy.farmmanagement.MyStatic.singleCropLocation;

public class PartnerCrop extends AppCompatActivity {

    AutoCompleteTextView suggestionBox;
    ArrayList<String> Hint = new ArrayList<>();
    ArrayAdapter<String> partnerAdapter;

    SinglePartnerListAdapter myAdapter;
    ArrayList<SinglePartnerListData> PartnerList = new ArrayList<>();

    ListView PartnerCropListView;

    boolean CheckPartnerShare = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_crop);

        PartnerCropListView = findViewById(R.id.partner_list);

        myAdapter = new SinglePartnerListAdapter(this,PartnerList);

        suggestionBox = findViewById(R.id.partner_search);

        PartnerCropListView.setAdapter(myAdapter);

        PartnerCropListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PartnerCrop.this);

                builder.setTitle(R.string.delete);
                builder.setMessage(R.string.are_you_sure_you_want_to_delete);

                builder.setNegativeButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String PartnerName = PartnerList.get(position).getPartnerName();
                        ShowProgress(PartnerCrop.this);
                        singleCropLocation.orderBy(MyStatic.DATE_OF_CROP_ADDING, Query.Direction.DESCENDING).get()
                                .addOnSuccessListener(PartnerCrop.this,new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                    if (documentSnapshot.getBoolean(PARTNER)){
                                        long pC = documentSnapshot.getLong(PARTNER_COUNT);
                                        int PC = (int) pC;
                                        switch (PC){
                                            case 5 :
                                                if (PartnerName.equals(documentSnapshot.getString(PARTNER_NAME_5))){
                                                CheckPartnerShare = true;
                                                break;
                                                }
                                            case 4 :
                                                if (PartnerName.equals(documentSnapshot.getString(PARTNER_NAME_4))){
                                                    CheckPartnerShare = true;
                                                    break;
                                                }
                                            case 3:
                                                if (PartnerName.equals(documentSnapshot.getString(PARTNER_NAME_3))){
                                                    CheckPartnerShare = true;
                                                    break;
                                                }
                                            case 2:
                                                if (PartnerName.equals(documentSnapshot.getString(PARTNER_NAME_2))){
                                                    CheckPartnerShare = true;
                                                    break;
                                                }
                                            case 1:
                                                if (PartnerName.equals(documentSnapshot.getString(PARTNER_NAME_1))){
                                                    CheckPartnerShare = true;
                                                    break;
                                                }
                                        }
                                        if (CheckPartnerShare){
                                            break;
                                        }
                                    }
                                }
                                if (CheckPartnerShare){
                                    HideProgress();
                                    ShowDialog(PartnerCrop.this,getString(R.string.can_not_delete_bacause_partner_has_share_in_some_crop));
                                }
                                else{
                                    partnerLocation.document(PartnerList.get(position).getPartnerId()).delete().addOnSuccessListener(PartnerCrop.this, new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            myAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    partnerList.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                ArrayList<String> Servicer = (ArrayList<String>) dataSnapshot.getValue();

                                                Servicer.remove(PartnerList.get(position).getPartnerId());

                                                partnerList.setValue(Servicer);
                                                Hint.remove(PartnerList.get(position).getPartnerName());
                                                PartnerList.remove(position);
                                            }
                                            myAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    ShowDialog(PartnerCrop.this,getString(R.string.deleted));
                                }
                                myAdapter.notifyDataSetChanged();
                                HideProgress();
                            }
                        });

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
        addPartnerListData();

        partnerList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Hint = (ArrayList<String>) dataSnapshot.getValue();
                    assert Hint != null;
                    partnerAdapter = new ArrayAdapter<String>(
                            PartnerCrop.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            Hint);
                    suggestionBox.setAdapter(partnerAdapter);

                    suggestionBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            addPartnerListData(suggestionBox.getText().toString());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void addPartnerListData(){
        PartnerList.clear();
        ShowProgress(this);
        partnerLocation.get().addOnSuccessListener(this,new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                PartnerList.clear();
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){

                    Map<String, Object> CloudItem = queryDocumentSnapshot.getData();

                    SinglePartnerData Data = new SinglePartnerData(
                            CloudItem.get(ID).toString(),
                            CloudItem.get(NAME).toString(),
                            (Long) CloudItem.get(PHONE),
                            CloudItem.get(ADDRESS).toString(),
                            (Date) CloudItem.get(DATE)
                    );

                    SinglePartnerListData item = new SinglePartnerListData(Data.getPartnerCropId(),
                            Data.getPartnerCropName(),String.valueOf(Data.getPartnerCropPhone()));
                    PartnerList.add(item);
                }
                myAdapter.notifyDataSetChanged();
                HideProgress();
            }
        });

    }
    public void addPartnerListData(String partnerId){
        PartnerList.clear();
        ShowProgress(this);
        partnerLocation.document(partnerId).get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> CloudItem = documentSnapshot.getData();

                SinglePartnerData Data = new SinglePartnerData(
                        CloudItem.get(ID).toString(),
                        CloudItem.get(NAME).toString(),
                        (Long) CloudItem.get(PHONE),
                        CloudItem.get(ADDRESS).toString(),
                        (Date) CloudItem.get(DATE)
                );

                SinglePartnerListData item = new SinglePartnerListData(Data.getPartnerCropId(),
                        Data.getPartnerCropName(),String.valueOf(Data.getPartnerCropPhone()));
                PartnerList.add(item);

                myAdapter.notifyDataSetChanged();
                HideProgress();
            }
        });

    }

    public void addPartnerBt(View view){
        startActivity(new Intent(this,AddPartnerCrop.class));
    }

}
