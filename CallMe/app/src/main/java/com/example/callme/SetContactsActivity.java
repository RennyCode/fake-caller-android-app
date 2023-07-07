package com.example.callme;

import static com.example.callme.MyJobService.scheduleJob;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TimePicker;
import com.example.callme.Objects.Contact;
import com.google.android.material.button.MaterialButton;
import java.util.Calendar;

public class SetContactsActivity extends AppCompatActivity {
    private final int HOUR_PLUS = 3;
    private GeneralActivity generalActivity;
    private AppCompatImageView main_IMG_background;
    private ListFragment listFragment;
    private MaterialButton[] menu_BTN_options; // 0 - add, 1 - select, 2 - delete
    private MaterialButton timer_btn;
    private Switch switch_btn;
    private String uid;
    private TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_contacts);
        uid = getIntent().getStringExtra("UID");
        findViews();
        setAnswersClickListeners();
        listFragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("UID", uid);
        listFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_list, listFragment).commit();

    }
    private void setAnswersClickListeners() {
        menu_BTN_options[0].setOnClickListener(v -> addToList());
        menu_BTN_options[1].setOnClickListener(v -> selectSelected());
        menu_BTN_options[2].setOnClickListener(v -> deleteSelected());
        menu_BTN_options[3].setOnClickListener(v -> editText());
        switch_btn.setOnClickListener(v -> timer_btn.setEnabled(!timer_btn.isEnabled()));
        initTimer();
    }
    private void initTimer() {
        timer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_TimePikerDialog();
            }
        });

    }
    private void open_TimePikerDialog() {
        Calendar cal = Calendar.getInstance();
        int h = cal.get(Calendar.HOUR_OF_DAY) + HOUR_PLUS;
        int m = cal.get(Calendar.MINUTE);
        boolean is24hourView = true;

        timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hour = Integer.toString(hourOfDay);
                String min = Integer.toString(minute);
                if(hour.length() != 2 )
                    hour = "0" + hour;
                if(min.length() != 2 )
                    min = "0" + min;

                timer_btn.setText(hour + ":" + min);
            }
        }, h, m, is24hourView);

        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        timePickerDialog.setTitle("Select time");
        timePickerDialog.show();

    }
    private void editText() {
        Intent popupWindow = new Intent(this, PopUpWindowEditTextActivity.class);
        popupWindow.putExtra("UID", uid);
        startActivity(popupWindow);
    }
    private void selectSelected(){
        Contact contact = listFragment.getSelected();
        if (contact == null){
            generalActivity.make_toast("Nothing was selected");
            return;
        }

        if (switch_btn.isChecked()){
            Log.e("msg", "switch is on");
            setTimeForCall(contact);
        }
        else {

            startFakeCallScreen(contact);
            finish();
        }
    }
    private void deleteSelected() {
        Contact contact = listFragment.getSelected();
        if (contact == null) {
            generalActivity.make_toast("Nothing was selected");
            return;
        }
        listFragment.deleteSelected(uid);
    }

    private void addToList() {
       Intent popupWindow = new Intent(this, PopUpWindowAddContactActivity.class);
       popupWindow.putExtra("UID", uid);
       startActivity(popupWindow);
    }

    private void setTimeForCall(Contact contact) {
        long delayMillis = calculateTime();
        Log.e("msg", "delay in sec: " + delayMillis/1000);
        scheduleJob( this, contact, delayMillis);
        Intent nextIntent = new Intent(this, MainActivity.class);
        startActivity(nextIntent);
        finish();
    }

    private void startFakeCallScreen(Contact contact) {
        Log.e("msg", "switch is off");
        Intent nextIntent = new Intent(this, CallActivity.class);
        nextIntent.putExtra("SOURCE_ACTIVITY", "SetContactsActivity");
        nextIntent.putExtra("CONTACT_NAME", contact.getName());
        nextIntent.putExtra("CONTACT_NUMBER", contact.getNumber());
        nextIntent.putExtra("UID", uid);
        startActivity(nextIntent);
    }


    private long calculateTime() {
        String time = (String) timer_btn.getText();
        int desiredHour = Integer.parseInt(time.substring(0,2));
        int desiredMinute = Integer.parseInt(time.substring(3,5));

        Log.e("msg", "desiredHour = "+ desiredHour + ", desiredMinute = "+ desiredMinute);

        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMin = calendar.get(Calendar.MINUTE);
        int currentSec = calendar.get(Calendar.SECOND);

        Log.e("msg", "currentHour = "+ currentHour + ", currentMin = "+ currentMin +", currentSec = " + currentSec);


        long desiredMillis = (desiredHour * 60 * 60 * 1000) + (desiredMinute * 60 * 1000);
        long currentMillis = ( (HOUR_PLUS + currentHour) * 60 * 60 * 1000) + (currentMin * 60 * 1000) + (currentSec * 1000);
        long timeDiffMillis = desiredMillis - currentMillis;

        Log.e("msg", "sec = " + timeDiffMillis/1000);

        if (timeDiffMillis<0)
            return 0;
        else
            return timeDiffMillis;
    }


    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);
        menu_BTN_options = new MaterialButton[]{
                findViewById(R.id.add_btn),
                findViewById(R.id.select_btn),
                findViewById(R.id.delete_btn),
                findViewById(R.id.edit_text_btn)};
        timer_btn = findViewById(R.id.timer_btn);
        switch_btn = findViewById(R.id.switch_btn);
        generalActivity = GeneralActivity.getInstance(this);
        setButtonTIme();
    }

    private void setButtonTIme() {
        Calendar cal = Calendar.getInstance();
        String h = String.valueOf(cal.get(Calendar.HOUR_OF_DAY) + HOUR_PLUS);
        String m = String.valueOf(cal.get(Calendar.MINUTE));
        if(h.length() != 2 )
            h = "0" + h;
        if(m.length() != 2 )
            m = "0" + m;
        timer_btn.setText(h + ":" + m);
    }

}