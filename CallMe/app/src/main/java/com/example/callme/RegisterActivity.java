package com.example.callme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import com.bumptech.glide.Glide;
import com.example.callme.Objects.Contact;
import com.example.callme.Objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private final String default_text = "default text";
    private GeneralActivity generalActivity;
    private AppCompatImageView main_IMG_background;
    private MaterialButton submit_BTN;
    private TextInputEditText email_layout, password_layout;
    private Intent nextIntent;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private  String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
        Glide.with(this).load("https://img.freepik.com/free-photo/old-black-background-grunge-texture-dark-wallpaper-blackboard-chalkboard-room-wall_1258-28312.jpg?w=900&t=st=1688747464~exp=1688748064~hmac=01225b1bda96f63cee87b4c7d5b97e37522a3463d95ac849161e9d1772559dc4").centerCrop()
                .placeholder(R.drawable.ic_launcher_background).into(main_IMG_background);
    }
    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);
        email_layout = findViewById(R.id.email_input);
        password_layout = findViewById(R.id.password_input);
        submit_BTN = findViewById(R.id.submit_btn);
        submit_BTN.setOnClickListener(v -> clicked());
        mAuth = FirebaseAuth.getInstance();
        generalActivity = GeneralActivity.getInstance(this);
        db = FirebaseDatabase.getInstance("https://callme-3f33e-default-rtdb.europe-west1.firebasedatabase.app");
        ref = db.getReference();
    }

    private void clicked(){
        email = String.valueOf(email_layout.getText());
        password = String.valueOf(password_layout.getText());
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
            return;
        if(password.length() <= 6) {
            generalActivity.make_toast("Password too short");
            return;
        }
        
        if (!isValidEmail(email)) {
            generalActivity.make_toast("Invalid email format");
            return;
        }
        
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isComplete()) {
                    generalActivity.make_toast("register in successfully");
                    String userPath = "users/" + mAuth.getUid();
                    ref.child(userPath + "/text call/text").setValue(default_text);
                    String contactKey = ref.push().getKey();
                    ref.child(userPath + "/contacts/" + contactKey + "/name").setValue("John Doe");
                    ref.child(userPath + "/contacts/" + contactKey + "/number").setValue("0521231231");


                    nextIntent = new Intent(RegisterActivity.this, SetContactsActivity.class);
                    nextIntent.putExtra("UID", mAuth.getUid());
                    startActivity(nextIntent);
                    finish();
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
}
