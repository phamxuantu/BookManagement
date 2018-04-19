package com.example.sev_user.bookmanagement;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Calendar;


public class Util {

    static JobScheduler jobScheduler;
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void scheduleJob(Context context,Calendar calendar) {
        ComponentName serviceComponent = new ComponentName(context, JobScheduling.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(calendar.getTimeInMillis()-System.currentTimeMillis());
        builder.setOverrideDeadline(calendar.getTimeInMillis()-System.currentTimeMillis()+5*1000);
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }
public static void stopJob(int idJob){
    jobScheduler.cancel(idJob);
}
}
