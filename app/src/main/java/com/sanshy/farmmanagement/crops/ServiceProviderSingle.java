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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.ALL;
import static com.sanshy.farmmanagement.MyStatic.AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.BUYER;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialog;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ID;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_ID;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_NAME;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_REMAINING_AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.TimeStampKey;
import static com.sanshy.farmmanagement.MyStatic.lessAllRemainingReport;
import static com.sanshy.farmmanagement.MyStatic.payServiceProviderLocation;
import static com.sanshy.farmmanagement.MyStatic.removeRemainingAmountBuyer;
import static com.sanshy.farmmanagement.MyStatic.removeRemainingAmountServiceProvider;
import static com.sanshy.farmmanagement.MyStatic.staticDate;

public class ServiceProviderSingle extends AppCompatActivity {

    TextView ServicePName,RemainingAmount,ShowingDate;
    EditText PayingAmount;
    String ServicePN;
    String ServicePId;
    String ServicePRemainingAmount;
    double nowRemain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_single);
        Intent intent = getIntent();

        ServicePN = intent.getStringExtra(SERVICE_PROVIDER_NAME);
        ServicePId = intent.getStringExtra(SERVICE_PROVIDER_ID);
        ServicePRemainingAmount = intent.getStringExtra(SERVICE_PROVIDER_REMAINING_AMOUNT);
        nowRemain = Double.parseDouble(ServicePRemainingAmount);

        ServicePName = findViewById(R.id.service_provider_name_single);
        RemainingAmount = findViewById(R.id.service_provider_remaining_amount_single);
        PayingAmount = findViewById(R.id.service_provider_paying_amount);
        ShowingDate = findViewById(R.id.service_provider_single_show_date);

        ServicePName.setText(ServicePN);
        RemainingAmount.setText(ServicePRemainingAmount);

    }
    public void HistoryOfServiceBt(View view){
        Intent intent = new Intent(this,HistoryOfServiceForSingleServiceProvider.class);
        intent.putExtra(SERVICE_PROVIDER_NAME, ServicePN);
        intent.putExtra(SERVICE_PROVIDER_ID, ServicePId);
        startActivity(intent);
    }

    public void HistoryOfPaySBt(View view){
        Intent intent = new Intent(this,PayHistoryServiceProvider.class);
        intent.putExtra(SERVICE_PROVIDER_NAME, ServicePN);
        intent.putExtra(SERVICE_PROVIDER_ID, ServicePId);
        startActivity(intent);
    }
    boolean isDate = false;
    public void dateChooserServicePBt(View view){
        isDate = ChooseDateDialog(this,ShowingDate);
    }
    String PayingAString;
    public void PayNowSBt(View view){
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
        payServiceProviderLocation(ServicePId).document(Key).set(CloudItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    removeRemainingAmountServiceProvider(ServicePId,Double.parseDouble(PayingAString));
                    lessAllRemainingReport(ALL,Double.parseDouble(PayingAString),SERVICE_PROVIDER);
                    PayingAmount.setText("");
                    nowRemain = nowRemain-Double.parseDouble(PayingAString);
                    RemainingAmount.setText(String.valueOf(nowRemain));
                    ShowDialog(ServiceProviderSingle.this,getString(R.string.saved));
                }
                else {
                    ShowDialog(ServiceProviderSingle.this,getString(R.string.try_again_later_or_send_feedback));
                }
                HideProgress();
            }
        });
    }
}
