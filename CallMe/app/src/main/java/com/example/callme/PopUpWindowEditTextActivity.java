package com.example.callme;

import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class PopUpWindowEditTextActivity extends AppCompatActivity {
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private MaterialButton submit_btn;
    private String uid;
    private AppCompatImageView main_IMG_background;
    private TextInputEditText text_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_window_edit_text);
        uid = getIntent().getStringExtra("UID");
        findViews();
        Glide.with(this).load("https://img.freepik.com/free-photo/old-black-background-grunge-texture-dark-wallpaper-blackboard-chalkboard-room-wall_1258-28312.jpg?w=900&t=st=1688747464~exp=1688748064~hmac=01225b1bda96f63cee87b4c7d5b97e37522a3463d95ac849161e9d1772559dc4").centerCrop()
                .placeholder(R.drawable.ic_launcher_background).into(main_IMG_background);
        getText();
        submit_btn.setOnClickListener(v -> subToDB());
    }

    private void getText() {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text = dataSnapshot.child("text").getValue(String.class);
                text_layout.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // An error occurred while retrieving the data
                // Handle the error appropriately
            }
        });
    }

    private void subToDB() {
        String text = text_layout.getText().toString();
        if (!text.isEmpty()) {
            //add to db
            ref.child("text").setValue(text);
            finish();
        }
    }

    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);
        submit_btn = findViewById(R.id.submit_text_btn);
        text_layout = findViewById(R.id.text_input);
        db = FirebaseDatabase.getInstance("https://callme-3f33e-default-rtdb.europe-west1.firebasedatabase.app");
        ref = db.getReference().child("users/"+ uid +"/text call");
    }

}