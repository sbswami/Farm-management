package com.sanshy.farmmanagement;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.sanshy.farmmanagement.crops.AllCropsReport;
import com.sanshy.farmmanagement.crops.BuyerCrops;
import com.sanshy.farmmanagement.crops.MyCrops;
import com.sanshy.farmmanagement.crops.PartnerCrop;
import com.sanshy.farmmanagement.crops.ServiceProviders;
import com.sanshy.farmmanagement.extras.HomeExpenditure;
import com.sanshy.farmmanagement.extras.OtherExpenditure;
import com.sanshy.farmmanagement.extras.SideBusiness;
import com.sanshy.farmmanagement.loans.LoanPerson;
import com.sanshy.farmmanagement.loans.LoansList;
import com.sanshy.farmmanagement.loans.NewLoan;
import com.sanshy.farmmanagement.loans.ReportLoan;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.GET_MODE;
import static com.sanshy.farmmanagement.MyStatic.RATE;
import static com.sanshy.farmmanagement.MyStatic.RC_SIGN_IN;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.UPDATE;
import static com.sanshy.farmmanagement.MyStatic.currentUser;
import static com.sanshy.farmmanagement.MyStatic.db;
import static com.sanshy.farmmanagement.MyStatic.rateUsReference;
import static com.sanshy.farmmanagement.MyStatic.setCurrentUserId;
import static com.sanshy.farmmanagement.MyStatic.updateReference;

public class Home extends AppCompatActivity {

    LinearLayout cropContainer,loanContainer,extraContainer,settingContainer;

    LinearLayout myCrops,serviceProvider,partnerCrops,allCropsReport;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_crop:
                    cropContainer.setVisibility(View.VISIBLE);
                    loanContainer.setVisibility(View.GONE);
                    extraContainer.setVisibility(View.GONE);
                    settingContainer.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_loan:
                    loanContainer.setVisibility(View.VISIBLE);
                    cropContainer.setVisibility(View.GONE);
                    extraContainer.setVisibility(View.GONE);
                    settingContainer.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_extra:
                    extraContainer.setVisibility(View.VISIBLE);
                    cropContainer.setVisibility(View.GONE);
                    loanContainer.setVisibility(View.GONE);
                    settingContainer.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_setting:
                    settingContainer.setVisibility(View.VISIBLE);
                    extraContainer.setVisibility(View.GONE);
                    cropContainer.setVisibility(View.GONE);
                    loanContainer.setVisibility(View.GONE);
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        cropContainer = findViewById(R.id.crop_container);
        loanContainer = findViewById(R.id.loan_container);
        extraContainer = findViewById(R.id.extra_container);
        settingContainer = findViewById(R.id.setting_container);

        myCrops = findViewById(R.id.my_crops);
        serviceProvider = findViewById(R.id.service_provider);
        partnerCrops = findViewById(R.id.partner_crop);
        allCropsReport = findViewById(R.id.all_crops_report);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    private void rateAndUpdate() {
        updateReference.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    try{
                        int playVersion = (int) documentSnapshot.get(UPDATE);


                        try {
                            PackageInfo pInfo = Home.this.getPackageManager().getPackageInfo(getPackageName(), 0);
                            String version = pInfo.versionName;
                            int code = pInfo.versionCode;
                            if (playVersion>code)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                                builder.setTitle(R.string.update)
                                        .setMessage(getString(R.string.wow_new_version_))
                                        .setPositiveButton(getString(R.string.update_now_), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                intent.setData(Uri.parse(getString(R.string.direct_play_store)));
                                                startActivity(intent);
                                            }
                                        })
                                        .create().show();
                            }

                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }catch (Exception ex){}
                }
            }
        });

        rateUsReference.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    boolean rated = (boolean) documentSnapshot.get(RATE);
                    if (!rated){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);

                        builder.setTitle(getString(R.string.rate_us_text))
                                .setMessage(getString(R.string.rate_us_request_dialog_))
                                .setPositiveButton(getString(R.string.rate_us_text), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(getString(R.string.direct_play_store)));
                                        startActivity(intent);
                                        Map<String, Object> rateMap = new HashMap<String, Object>();
                                        rateMap.put(RATE, true);
                                        rateUsReference.set(rateMap);
                                    }
                                })
                                .setNegativeButton(getString(R.string.not_now_),null)
                                .setNeutralButton(getString(R.string.never_), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Map<String, Object> rateMap = new HashMap<String, Object>();
                                        rateMap.put(RATE, true);
                                        rateUsReference.set(rateMap);
                                    }
                                });

                        builder.create();
                        try{
                            builder.show();
                        }catch (Exception ex){
                            Log.d("RateWindow: ",ex.toString());
                        }
                    }
                }
                else {
                    Map<String, Object> rateMap = new HashMap<String, Object>();
                    rateMap.put(RATE, false);
                    rateUsReference.set(rateMap);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        signIn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                MyStatic.currentUser = FirebaseAuth.getInstance().getCurrentUser();
                setCurrentUserId(currentUser.getUid());
                MyStatic.CurrentUserId = currentUser.getUid();
                // ...
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setCancelable(false);

                builder.setMessage(R.string.please_sign_in_again);
                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signIn();
                    }
                });

                builder.create().show();

                signIn();

                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    public void signIn(){

        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.first_open), Context.MODE_PRIVATE);
        boolean opened = sharedPreferences.getBoolean(getString(R.string.opened), false);

        if (!opened){
            finish();
            startActivity(new Intent(this,IntroductionActivity.class));

        }

        if (MyStatic.currentUser==null){
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.PhoneBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setLogo(R.drawable.my_logo)
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }
        else{
            rateAndUpdate();
        }

    }

    public void myCropsBt(View view){
        startActivity(new Intent(this,MyCrops.class));
    }
    public void serviceProviderBt(View view){
        startActivity(new Intent(this,ServiceProviders.class));
    }
    public void partnerCropsBt(View view){
        startActivity(new Intent(this, PartnerCrop.class));
    }
    public void buyerCropBt(View view){
        startActivity(new Intent(this, BuyerCrops.class));
    }
    public void allCropsReportBt(View view){
        startActivity(new Intent(this, AllCropsReport.class));
    }

    public void newLoanBt(View view){
        startActivity(new Intent(this,NewLoan.class));
    }
    public void loanPersonBt(View view){
        startActivity(new Intent(this, LoanPerson.class));
    }
    public void getLoanListBt(View view){
        Intent intent = new Intent(this, LoansList.class);

        intent.putExtra(GET_MODE,true);

        startActivity(intent);
    }
    public void giveLoanListBt(View view){
        Intent intent = new Intent(this, LoansList.class);

        intent.putExtra(GET_MODE,false);

        startActivity(intent);
    }
    public void ReportBt(View view){
        startActivity(new Intent(this, ReportLoan.class));
    }

    public void SideBusinessBt(View view){
        startActivity(new Intent(this,SideBusiness.class));
    }
    public void HomeExpenditureBt(View view){
        startActivity(new Intent(this, HomeExpenditure.class));
    }
    public void OtherExpenditureBt(View view){
        startActivity(new Intent(this, OtherExpenditure.class));
    }

    public void ResetAllBt(View view){
        ShowDialog(this,getString(R.string.cooming_soon));
    }

    public void ShareApp(View view){
        String shareText ="Install This Amazing App!\n" +
                "खेती एंव ऋण का पूरा हिसाब मोबाइल में \n " +
                "आपका खाता सुरक्षित रहेगा गूगल पर किसी भी परिस्थिति में\n" +
                "अभी डाउनलोड करें PlayStore से "+

                getString(R.string.play_store_address);
        String shareSubject =getString(R.string.share_subject);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,shareText);
        intent.putExtra(Intent.EXTRA_SUBJECT,shareSubject);
        startActivity(Intent.createChooser(intent, getString(R.string.share_app_via)));
    }
    public void HelpBt(View view){
        ShowDialog(this,getString(R.string.email_address) +"\n"+
                getString(R.string.whats_app_detail) );
    }
    public void HelpVideoBt(View view){
        startActivity(new Intent(this, MyYouTubePlayer.class));
    }

    public void BuySellApp(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(R.string.buy_sell_play_store_location)));
        startActivity(intent);
    }

    public void LogOut(View view){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        SharedPreferences sharedPreferences = Home.this.getSharedPreferences(getString(R.string.first_open), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(getString(R.string.opened),false);
                        MyStatic.currentUser = null;
                        editor.commit();
                        startActivity(new Intent(Home.this,IntroductionActivity.class));
                        Home.this.finish();
                    }
                });
    }

    public void SanjayChannelBt(View view){
        Intent intent = new Intent(
                Intent.ACTION_VIEW ,
                Uri.parse("https://www.youtube.com/user/sanjaykumarswami"));
        this.startActivity(intent);

//        intent.setComponent(new ComponentName("com.google.android.youtube","com.google.android.youtube.PlayerActivity"));
//
//        PackageManager manager = this.getPackageManager();
//        List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
//        if (infos.size() > 0) {
//            this.startActivity(intent);
//        }else{
//            Toast.makeText(this, "App Not Find", Toast.LENGTH_SHORT).show();
//        }
    }
}
