package ca.teyssedre.toiletchecker.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import ca.teyssedre.toiletchecker.MainActivity;
import ca.teyssedre.toiletchecker.R;
import ca.teyssedre.toiletchecker.utils.Constantes;

/**
 * Created by darky on 15-02-04,
 * at 21:35.
 */
public class GcmIntentService extends IntentService {

    private static final String TAG = "toiletChecker - GcmIntentService";

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super(Constantes.GCM_INTENT_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            switch (messageType) {
                case GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR:
                    sendNotification("Send error: " + extras.toString());
                    break;
                case GoogleCloudMessaging.MESSAGE_TYPE_DELETED:
                    sendNotification("Deleted messages on server: " + extras.toString());
                    // If it's a regular GCM message, do some work.
                    break;
                case GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE:
                    // This loop represents the service doing some work.
                    for (int i = 0; i < 5; i++) {
                        Log.i(TAG, "Working... " + (i + 1)
                                + "/5 @ " + SystemClock.elapsedRealtime());
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                    Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                    // Post notification of received message.
                    sendNotification("Received: " + extras.toString());
                    Log.i(TAG, "Received: " + extras.toString());
                    break;
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("GCM Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
