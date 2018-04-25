package shop.fortnite.ggjimmy.fortniteshop;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by ggjimmy on 4/16/18.
 */

public class DemoJobCreator implements JobCreator {
    @Override
    public Job create(String tag) {
        switch(tag){
            case NotificationJob.TAG:
                return new NotificationJob();
        }
        return null;
    }
}
