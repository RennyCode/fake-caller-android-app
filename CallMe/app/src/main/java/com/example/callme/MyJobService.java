package com.example.callme;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.util.Log;

import com.example.callme.Objects.Contact;

public class MyJobService extends JobService {
    private static final int JOB_ID = 1;

    @Override
    public boolean onStartJob(JobParameters params) {
        String name = params.getExtras().getString("NAME");
        String number = params.getExtras().getString("NUMBER");
        String sourceActivity = params.getExtras().getString("SOURCE_ACTIVITY");
        Intent newIntent = new Intent(getApplicationContext(), CallActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        newIntent.putExtra("CONTACT_NAME", name);
        newIntent.putExtra("CONTACT_NUMBER", number);
        newIntent.putExtra("SOURCE_ACTIVITY", sourceActivity);
        startActivity(newIntent);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    public static void scheduleJob(Context context, Contact contact, long delayMillis){
        PersistableBundle extras = new PersistableBundle();
        extras.putString("NAME", contact.getName());
        extras.putString("NUMBER", contact.getNumber());
        extras.putString("SOURCE_ACTIVITY", "SetContactsActivity");
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(context, MyJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
                .setMinimumLatency(delayMillis) //  0 to Start immediately
                .setOverrideDeadline(delayMillis + 100)
                .setExtras(extras)
                .build();
        int resJob = jobScheduler.schedule(jobInfo);
        if(resJob == JobScheduler.RESULT_SUCCESS)
            Log.e("msg" ,"Jon Scheduled.");
    }


}
