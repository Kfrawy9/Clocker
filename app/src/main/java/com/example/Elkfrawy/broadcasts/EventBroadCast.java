package com.example.Elkfrawy.broadcasts;

import static com.example.Elkfrawy.core.NotificationChannelBuild.CHANNEL_ID;
import static com.example.Elkfrawy.ui.eventUi.AddEventFragment.EVENT_EXTRA_DESC;
import static com.example.Elkfrawy.ui.eventUi.AddEventFragment.EVENT_EXTRA_TITLE;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.example.Elkfrawy.R;
import com.example.Elkfrawy.services.DeleteEvent;
import com.example.Elkfrawy.ui.MainActivity;

public class EventBroadCast extends BroadcastReceiver {

    public static final int NOTIFICATION_ID = 3;
    public static final String DELETE_EVENT_TITLE_KEY = "eTitle";
    public static final String DELETE_EVENT_DESC_KEY = "eDesc";
    public static final String NOTIFICATION_DISMISS_ID = "notificationId";
    public static final int SERVICE_REQUEST_CODE = 11;

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra(EVENT_EXTRA_TITLE);
        String desc = intent.getStringExtra(EVENT_EXTRA_DESC);


        Intent i = new Intent(context, DeleteEvent.class);
        Intent m = new Intent(context, MainActivity.class);

        PendingIntent deepLink = new NavDeepLinkBuilder(context)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.eventFragment)
                .createPendingIntent();

        i.putExtra(DELETE_EVENT_TITLE_KEY, title);
        i.putExtra(DELETE_EVENT_DESC_KEY, desc);
        i.putExtra(NOTIFICATION_DISMISS_ID, NOTIFICATION_ID);
        PendingIntent pi = PendingIntent.getService(context, SERVICE_REQUEST_CODE, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification alert = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(desc)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_launcher_foreground, "Delete", pi)
                .setContentIntent(deepLink)
                .build();


        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(NOTIFICATION_ID, alert);

    }


}
