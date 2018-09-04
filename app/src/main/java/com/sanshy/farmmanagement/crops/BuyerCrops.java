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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanshy.farmmanagement.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.ADDRESS;
import static com.sanshy.farmmanagement.MyStatic.BUYER_ID;
import static com.sanshy.farmmanagement.MyStatic.BUYER_NAME;
import static com.sanshy.farmmanagement.MyStatic.BUYER_REMAIN_AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ID;
import static com.sanshy.farmmanagement.MyStatic.NAME;
import static com.sanshy.farmmanagement.MyStatic.PHONE;
import static com.sanshy.farmmanagement.MyStatic.REMAINING_AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.buyerList;
import static com.sanshy.farmmanagement.MyStatic.buyerLocation;

public class BuyerCrops extends AppCompatActivity {
    AutoCompleteTextView suggestionBox;
    ArrayList<String> BuyerHint = new ArrayList<>();
    ArrayAdapter<String> buyerAdapter;

    ListView BuyerCropList;
    SingleBuyerCropListAdapter myAdapter;
    ArrayList<SingleBuyerCropsList> BuyerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_crops);

        BuyerCropList = findViewById(R.id.buyer_crops_list);

        suggestionBox = findViewById(R.id.buyer_crops_search);

        myAdapter = new SingleBuyerCropListAdapter(this,BuyerList);

        BuyerCropList.setAdapter(myAdapter);



        BuyerCropList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BuyerCrops.this,BuyerCropsSingle.class);

                intent.putExtra(BUYER_NAME,BuyerList.get(position).getBuyerName());
                intent.putExtra(BUYER_ID,BuyerList.get(position).getBuyerId());
                intent.putExtra(BUYER_REMAIN_AMOUNT,String.valueOf(BuyerList.get(position).getRemainingAmount()));

                startActivity(intent);
            }
        });

        BuyerCropList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(BuyerCrops.this);

                builder.setTitle(R.string.delete);
                builder.setMessage(R.string.are_you_sure_you_want_to_delete);

                builder.setNegativeButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Double.parseDouble(BuyerList.get(position).getRemainingAmount())!=0){
                            ShowDialog(BuyerCrops.this,getString(R.string.can_not_delete_bacause_payment_still_remaining));
                        }
                        else{
                            ShowProgress(BuyerCrops.this);
                            buyerLocation.document(BuyerList.get(position).getBuyerId()).delete();
                            buyerList.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        ArrayList<String> Servicer = (ArrayList<String>) dataSnapshot.getValue();

                                        Servicer.remove(BuyerList.get(position).getBuyerId());

                                        buyerList.setValue(Servicer);
                                        ShowDialog(BuyerCrops.this,getString(R.string.deleted));

                                        BuyerHint.remove(BuyerList.get(position).getBuyerName());
                                        BuyerList.remove(position);
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

    @Override
    protected void onResume() {
        super.onResume();
        BuyerCropsAddData();

        buyerList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    BuyerHint = (ArrayList<String>) dataSnapshot.getValue();
                    assert BuyerHint != null;
                    buyerAdapter = new ArrayAdapter<String>(
                            BuyerCrops.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            BuyerHint);
                    suggestionBox.setAdapter(buyerAdapter);

                    suggestionBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            BuyerCropsAddData(suggestionBox.getText().toString());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void BuyerCropsAddData(){
        BuyerList.clear();
        ShowProgress(this);
        buyerLocation.get().addOnSuccessListener(this,new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                BuyerList.clear();
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){

                    Map<String, Object> CloudItems = queryDocumentSnapshot.getData();

                    SingleBuyerCropsData Data = new SingleBuyerCropsData(
                            (String) CloudItems.get(ID),
                            (String) CloudItems.get(NAME),
                            (Long) CloudItems.get(PHONE),
                            (String) CloudItems.get(ADDRESS),
                            (Double) CloudItems.get(REMAINING_AMOUNT),
                            (Date) CloudItems.get(DATE)
                    );
                    SingleBuyerCropsList item = new SingleBuyerCropsList(Data.getBuyerCropsId(),Data.getBuyerCropsName(),String.valueOf(Data.getBuyerCropsPhone()),String.valueOf(Data.getBuyerCropsRemainingPayment()));
                    BuyerList.add(item);
                }
                myAdapter.notifyDataSetChanged();
                HideProgress();
            }
        });

    }
    public void BuyerCropsAddData(String BuyerId){
        BuyerList.clear();
        ShowProgress(this);
        buyerLocation.document(BuyerId).get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> CloudItems = documentSnapshot.getData();

                        SingleBuyerCropsData Data = new SingleBuyerCropsData(
                                (String) CloudItems.get(ID),
                                (String) CloudItems.get(NAME),
                                (Long) CloudItems.get(PHONE),
                                (String) CloudItems.get(ADDRESS),
                                (Double) CloudItems.get(REMAINING_AMOUNT),
                                (Date) CloudItems.get(DATE)
                        );
                        SingleBuyerCropsList item = new SingleBuyerCropsList(Data.getBuyerCropsId(),Data.getBuyerCropsName(),String.valueOf(Data.getBuyerCropsPhone()),String.valueOf(Data.getBuyerCropsRemainingPayment()));
                        BuyerList.add(item);
                        myAdapter.notifyDataSetChanged();
                        HideProgress();
                    }
                });

    }

    public void addBuyerCropsBt(View view){
        startActivity(new Intent(this,AddBuyerCrops.class));
    }
}
