package com.sanshy.farmmanagement.crops;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sanshy.farmmanagement.PayHistoryDataList;
import com.sanshy.farmmanagement.R;
import com.sanshy.farmmanagement.loans.PayHistory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.ALL;
import static com.sanshy.farmmanagement.MyStatic.AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.BUYER;
import static com.sanshy.farmmanagement.MyStatic.BUYER_ID;
import static com.sanshy.farmmanagement.MyStatic.BUYER_NAME;
import static com.sanshy.farmmanagement.MyStatic.BUYER_REMAIN_AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialog;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ID;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.TimeStampKey;
import static com.sanshy.farmmanagement.MyStatic.lessAllRemainingReport;
import static com.sanshy.farmmanagement.MyStatic.payBuyerLocation;
import static com.sanshy.farmmanagement.MyStatic.removeRemainingAmountBuyer;
import static com.sanshy.farmmanagement.MyStatic.staticDate;

public class BuyerCropsSingle extends AppCompatActivity {

    TextView BuyerName,RemainingAmount,ShowingDate;
    EditText PayingAmount;
    String BuyerN;
    String BuyerId;
    String BuyerRemainingAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_crops_single);

        Intent intent = getIntent();
        BuyerN = intent.getStringExtra(BUYER_NAME);
        BuyerId = intent.getStringExtra(BUYER_ID);
        BuyerRemainingAmount = intent.getStringExtra(BUYER_REMAIN_AMOUNT);

        BuyerName = findViewById(R.id.buyer_crop_name_single);
        RemainingAmount = findViewById(R.id.buyer_crop_remaining_amount_single);
        PayingAmount = findViewById(R.id.buyer_crop_paying_amount);
        ShowingDate = findViewById(R.id.buyer_crop_single_show_date);

        BuyerName.setText(BuyerN);
        RemainingAmount.setText(BuyerRemainingAmount);

    }

    public void HistoryOfPayBt(View view){
        Intent intent = new Intent(this,PayHistoryBuyer.class);
        intent.putExtra(BUYER_NAME,BuyerN);
        intent.putExtra(BUYER_ID,BuyerId);
        startActivity(intent);
    }
    boolean isDate = false;
    public void dateChooserBuyerCropBt(View view){
        isDate = ChooseDateDialog(this,ShowingDate);
    }
    String PayingAString;
    public void PayNowBt(View view){
        PayingAString = PayingAmount.getText().toString();
        if (PayingAString.isEmpty()){
            PayingAmount.setError(getString(R.string.fill_it));
            return;
        }

        if (!isDate){
            Toast.makeText(this, getString(R.string.please_choose_any_date), Toast.LENGTH_LONG).show();
            return;
        }
        SaveIt();
    }

    private void SaveIt() {
        ShowProgress(this);
        String Key = TimeStampKey(new Date());
        PayHistoryDataList item = new PayHistoryDataList(Key,staticDate,Double.parseDouble(PayingAString));
        Map<String, Object> CloudItem = new HashMap<>();
        CloudItem.put(ID,item.getId());
        CloudItem.put(DATE,item.getDate());
        CloudItem.put(AMOUNT,item.getAmount());
        payBuyerLocation(BuyerId).document(Key).set(CloudItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    removeRemainingAmountBuyer(BuyerId,Double.parseDouble(PayingAString));
                    lessAllRemainingReport(ALL,Double.parseDouble(PayingAString),BUYER);
                    PayingAmount.setText("");
                    double nowRemain = Double.parseDouble(BuyerRemainingAmount)-Double.parseDouble(PayingAString);
                    RemainingAmount.setText(String.valueOf(nowRemain));
                    ShowDialog(BuyerCropsSingle.this,getString(R.string.saved));
                }
                else {
                    ShowDialog(BuyerCropsSingle.this,getString(R.string.try_again_later_or_send_feedback));
                }
                HideProgress();
            }
        });
    }

    public void HistoryOfPurchaseBt(View view){
        Intent intent = new Intent(this,HistoryOfSingleBuyerPurchase.class);
        intent.putExtra(BUYER_NAME,BuyerN);
        intent.putExtra(BUYER_ID,BuyerId);
        startActivity(intent);
    }
}
