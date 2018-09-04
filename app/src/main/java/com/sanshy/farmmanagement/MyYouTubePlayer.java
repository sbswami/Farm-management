package com.sanshy.farmmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.firestore.DocumentSnapshot;

import static com.sanshy.farmmanagement.MyStatic.API_KEY;
import static com.sanshy.farmmanagement.MyStatic.videoURL;

public class MyYouTubePlayer extends YouTubeBaseActivity {
    public static final String VIDEO_ID = "VideoId";
    YouTubePlayerView myPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_you_tube_player);

        myPlayer = findViewById(R.id.youtube_player);
        myPlayer.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                videoURL(getString(R.string.current_language)).get().addOnSuccessListener(MyYouTubePlayer.this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            youTubePlayer.cueVideo(documentSnapshot.getString(VIDEO_ID));
                        }else{
                            youTubePlayer.cueVideo(getString(R.string.youtube_video_id));
                        }
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(MyYouTubePlayer.this, R.string.failed_to_load_video, Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void HomeBt(View view){
        startActivity(new Intent(this, Home.class));
        finish();
    }
}
