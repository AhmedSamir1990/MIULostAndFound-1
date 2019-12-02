package com.example.miulostandfound;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static int NOTIFICATION_ID =1;
    private final String CHANNEL_ID="personal_notifications";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        generateNotifications(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());
    }

    private void generateNotifications(String body, String title) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent= PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder .setSmallIcon(R.drawable.ic_message);
        builder .setContentTitle(title);
        builder.setContentText(body);
        builder .setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if(NOTIFICATION_ID>1073741824)
        {
            NOTIFICATION_ID=0;
        }
        notificationManager.notify(NOTIFICATION_ID++,builder.build());

    }
}
