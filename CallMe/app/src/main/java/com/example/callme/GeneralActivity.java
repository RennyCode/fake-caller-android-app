package com.example.callme;

import android.content.Context;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GeneralActivity extends AppCompatActivity {
    private static GeneralActivity instance = null;
    private Toast toast;
    private Context context;
    GeneralActivity(Context context) {
        this.context = context;
    }
    public static GeneralActivity getInstance(Context context) {
        if (instance == null) {
            instance = new GeneralActivity(context);
        }
        return instance;
    }
    public void make_toast(String toast_text){
        toast = Toast.makeText(context, toast_text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
