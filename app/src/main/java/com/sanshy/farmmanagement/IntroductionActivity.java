package com.sanshy.farmmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class IntroductionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
    }


    public void GoHomeBt(View view){
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.first_open), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.opened),true);
        editor.commit();
        startActivity(new Intent(this, MyYouTubePlayer.class));
        finish();
    }


}
