package com.example.callme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CallActivity extends AppCompatActivity {

    private  final String DEFAULT_TEXT = "Hey, what's up.";
    private final int ONE_SEC = 1000;
    private MediaPlayer mediaPlayer;
    private DatabaseReference ref;
    private FirebaseDatabase db;
    private TextToSpeech textToSpeech;
    private AppCompatImageView main_IMG_background;
    private MaterialButton[] menu_BTN_options;
    private MaterialButton[] inCall_BTN_options;
    private MaterialTextView timer;
    private MaterialTextView name;
    private MaterialTextView number;
    private String sourceActivity;
    private int callTime;
    private String uid;
    private String text = DEFAULT_TEXT;
    private final Handler score_handler = new Handler();
    private final Runnable score_runnable = new Runnable() {
        @Override
        public void run() {
            score_handler.postDelayed(this, ONE_SEC);
            timerTick();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("msg", "in call");

        setContentView(R.layout.activity_call);
        sourceActivity = getIntent().getStringExtra("SOURCE_ACTIVITY");
        uid = getIntent().getStringExtra("UID");
        findViews();
        setAnswersClickListeners();
       if (sourceActivity != null && sourceActivity.equals("SetContactsActivity")) {
           setContactDetails();
       }
       mediaPlayer = MediaPlayer.create(this, R.raw.ringtone);
       mediaPlayer.setLooping(true);
       mediaPlayer.setVolume(1.0f, 1.0f);
       mediaPlayer.start();
    }
    private void setContactDetails() {
        String contact_name = getIntent().getStringExtra("CONTACT_NAME");
        String contact_number = getIntent().getStringExtra("CONTACT_NUMBER");
        name.setText(contact_name);
        number.setText(contact_number);
    }
    private void setAnswersClickListeners() {
        menu_BTN_options[0].setOnClickListener(v -> answer());
        menu_BTN_options[1].setOnClickListener(v -> endCall());
    }
    private void endCall() {
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
//        finishAffinity();
        System.exit(0);
    }

    private void answer() {
        setTextToSpeech();
        showInCallBtn();
        callTime = 0;
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        score_handler.postDelayed(score_runnable, ONE_SEC);
    }
    private void setTextToSpeech() {
        db = FirebaseDatabase.getInstance("https://callme-3f33e-default-rtdb.europe-west1.firebasedatabase.app");
        ref = db.getReference().child( "users/" + uid +"/text call");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (uid != null){
                    text = dataSnapshot.child("text").getValue(String.class);
                }
                initTextToSpeech();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // An error occurred while retrieving the data
                // Handle the error appropriately
            }
        });
    }
    private void initTextToSpeech() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setPitch(0.5f);
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    // Handle initialization error
                }
            }
        });
    }
    private void showInCallBtn() {
        for (MaterialButton mb : inCall_BTN_options)
            mb.setVisibility(View.VISIBLE);
        timer.setVisibility(View.VISIBLE);
        menu_BTN_options[0].setVisibility(View.INVISIBLE);
        menu_BTN_options[0].setEnabled(false);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)  menu_BTN_options[1].getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, 0); // Remove alignParentEnd rule
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL); // Add centerHorizontal rule
        menu_BTN_options[1].setLayoutParams(layoutParams);
    }
    private void timerTick() {
        callTime++;
        int s = callTime % 60;
        int m = (callTime % 3600)/60;
        int h = callTime / 3600;
        String t =  String.format("%02d:%02d:%02d", h, m, s);;
        timer.setText(t);
    }
    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);
        menu_BTN_options = new MaterialButton[]{
                findViewById(R.id.call_start),
                findViewById(R.id.call_stop)};
        inCall_BTN_options = new MaterialButton[]{
                findViewById(R.id.speaker),
                findViewById(R.id.mute),
                findViewById(R.id.keypad),
                findViewById(R.id.add),
                findViewById(R.id.video),
                findViewById(R.id.bluetooth)};
        timer = findViewById(R.id.timer);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
    }
}