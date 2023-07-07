package com.example.callme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import android.os.Bundle;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.example.callme.Objects.Contact;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PopUpWindowAddContactActivity extends AppCompatActivity {
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private MaterialButton add_btn;
    private String uid;
    private AppCompatImageView main_IMG_background;
    private TextInputEditText name_layout, number_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_window_add_contact);
        uid = getIntent().getStringExtra("UID");
        findViews();
        Glide.with(this).load("https://img.freepik.com/free-photo/old-black-background-grunge-texture-dark-wallpaper-blackboard-chalkboard-room-wall_1258-28312.jpg?w=900&t=st=1688747464~exp=1688748064~hmac=01225b1bda96f63cee87b4c7d5b97e37522a3463d95ac849161e9d1772559dc4").centerCrop()
                .placeholder(R.drawable.ic_launcher_background).into(main_IMG_background);
        add_btn.setOnClickListener(v -> addToDB());
    }
    private void addToDB() {
        String name = name_layout.getText().toString();
        String number = number_layout.getText().toString();
        if (!name.isEmpty() && !number.isEmpty()) {
            db = FirebaseDatabase.getInstance("https://callme-3f33e-default-rtdb.europe-west1.firebasedatabase.app");
            ref = db.getReference().child("users/" + uid + "/contacts");
            Log.e("msg", uid);
            String id = ref.push().getKey(); // Generate a unique key for the user
            Contact contact = new Contact(name, number);
            ref.child(id).setValue(contact);
            finish();
        }


    }
    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);
        add_btn = findViewById(R.id.add_btn);
        number_layout = findViewById(R.id.number_input);
        name_layout = findViewById(R.id.name_input);
    }
}