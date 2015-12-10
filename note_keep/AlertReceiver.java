package com.example.note_keep;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
        createNotification(context, "Пора что-то сделать!", "Проверьте ваши заметки", 
        		"Note Keep");
	}
	
	public void createNotification(Context context, String msg, String msgText, String msgAlert){
		
		PendingIntent notificIntent = PendingIntent.getActivity(context, 0, 
				new Intent(context, NoteListActivity.class), 0); 
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_action_alarms)
				.setContentTitle(msg)
				.setTicker(msgAlert)
				.setContentText(msgText);
		
		mBuilder.setContentIntent(notificIntent);
		
		mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
		
		mBuilder.setAutoCancel(true);
		
		NotificationManager mNotificationManager = 
				(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		mNotificationManager.notify(1, mBuilder.build());
		
	}

}
