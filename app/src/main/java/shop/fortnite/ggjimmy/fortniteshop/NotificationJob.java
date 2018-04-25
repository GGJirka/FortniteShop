package shop.fortnite.ggjimmy.fortniteshop;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by ggjimmy on 4/16/18.
 */

public class NotificationJob extends Job {
    public static final String TAG = "NotificationJO.TAG";
    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        PendingIntent intent = PendingIntent.getActivity(getContext(), 0, new Intent(getContext(), FortniteShop.class),0);

        Notification notification = new NotificationCompat.Builder(getContext())
                .setContentTitle("DAILY ITEMS")
                .setContentText("TESTING")
                .setSmallIcon(R.drawable.all_skins_icon)
                .build();
        NotificationManagerCompat.from(getContext()).notify(new Random().nextInt(),notification);
        return Result.SUCCESS;
    }

    static void schedulePeriodic(){
        new JobRequest.Builder(NotificationJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(1))
                .setUpdateCurrent(true)
                .setPersisted(true)
                .build()
                .schedule();
    }
}
