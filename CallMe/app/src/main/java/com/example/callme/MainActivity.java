package com.example.callme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import com.bumptech.glide.Glide;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.material.button.MaterialButton;
public class MainActivity extends AppCompatActivity {

    private AppCompatImageView main_IMG_background;
    private MaterialButton[] menu_BTN_options;
    private Intent nextIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        Glide.with(this).load("https://img.freepik.com/free-photo/old-black-background-grunge-texture-dark-wallpaper-blackboard-chalkboard-room-wall_1258-28312.jpg?w=900&t=st=1688747464~exp=1688748064~hmac=01225b1bda96f63cee87b4c7d5b97e37522a3463d95ac849161e9d1772559dc4").centerCrop()
                .placeholder(R.drawable.ic_launcher_background).into(main_IMG_background);
        setAnswersClickListeners();
    }
    private void clicked(String button_name){
        if (button_name.compareTo("Set Call Contacts") == 0) {
            // go to LoginActivity
            Log.e("msg:", "got to here");
            nextIntent = new Intent(this, LoginActivity.class);
        } else{
            // go straight to CallActivity
            nextIntent = new Intent(this, CallActivity.class);
            nextIntent.putExtra("SOURCE_ACTIVITY", "MainActivity");
        }
        startActivity(nextIntent);
        finish();
    }
    private void setAnswersClickListeners() {
        for (MaterialButton mb : menu_BTN_options) {
            mb.setOnClickListener(v -> clicked(mb.getText().toString()));
        }
    }
    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);
        menu_BTN_options = new MaterialButton[]{
                findViewById(R.id.set_contact_btn),
                findViewById(R.id.call_now_btn)};
    }
}