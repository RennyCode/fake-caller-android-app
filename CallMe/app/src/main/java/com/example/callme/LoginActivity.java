package com.example.callme;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.Log;

public class LoginActivity extends AppCompatActivity {

    private GeneralActivity generalActivity;
    private AppCompatImageView main_IMG_background;
    private MaterialButton[] menu_BTN_options;
    private TextInputEditText email_layout, password_layout;
    private  Intent nextIntent;
    private  FirebaseAuth mAuth;
    private  String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.e("msg", "in Login!");
        findViews();
        setAnswersClickListeners();
        Glide.with(this).load("https://img.freepik.com/free-photo/old-black-background-grunge-texture-dark-wallpaper-blackboard-chalkboard-room-wall_1258-28312.jpg?w=900&t=st=1688747464~exp=1688748064~hmac=01225b1bda96f63cee87b4c7d5b97e37522a3463d95ac849161e9d1772559dc4").centerCrop()
                .placeholder(R.drawable.ic_launcher_background).into(main_IMG_background);
    }

    @Override
    public void onStart(){
        super.onStart();
        //check if user is signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            nextIntent = new Intent(LoginActivity.this, SetContactsActivity.class);
            nextIntent.putExtra("UID", currentUser.getUid());
            startActivity(nextIntent);
            finish();
        }

    }


    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);
        email_layout = findViewById(R.id.email_input);
        password_layout = findViewById(R.id.password_input);
        menu_BTN_options = new MaterialButton[]{
                findViewById(R.id.login_btn),
                findViewById(R.id.register_btn)};
        mAuth = FirebaseAuth.getInstance();
        generalActivity = GeneralActivity.getInstance(this);

    }

    private void setAnswersClickListeners() {
        for (MaterialButton mb : menu_BTN_options) {
            mb.setOnClickListener(v -> clicked(mb.getText().toString()));
        }
    }

    private void clicked(String button_name){

        if (button_name.compareTo("Login") == 0) {
            // go check login... on success go to SetContactsActivity
            email = String.valueOf(email_layout.getText());
            password = String.valueOf(password_layout.getText());

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                return;

            mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    generalActivity.make_toast("sign in successfully");
                    nextIntent = new Intent(LoginActivity.this, SetContactsActivity.class);

                    nextIntent.putExtra("UID", mAuth.getUid());

                    startActivity(nextIntent);
                    finish();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Display a toast message for the sign-in failure
                    generalActivity.make_toast("Failed to sign in");
                }
            });

        } else{
            // go  to RegisterActivity
            nextIntent = new Intent(this, RegisterActivity.class);
            startActivity(nextIntent);
            finish();
        }

    }



}