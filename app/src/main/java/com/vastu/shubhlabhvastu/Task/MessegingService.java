package com.vastu.shubhlabhvastu.Task;


import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.atomic.AtomicInteger;


//
public class MessegingService extends FirebaseMessagingService {


    String title,body,page  ;
	private final static AtomicInteger c = new AtomicInteger(0);

	public static int getID() {
		return c.incrementAndGet();
	}


 public void onMessageReceived (RemoteMessage remoteMessage){
            try
            {
            super.onMessageReceived(remoteMessage);
                Log.e("data_notification", String.valueOf(remoteMessage));
            //title = remoteMessage.getData().get("title");
            //body = remoteMessage.getData().get("message");
            //page = remoteMessage.getData().get("openpage");





            ///////DB Storage///
            //  NoteRepository noteRepository = new NoteRepository(getApplicationContext());
            // noteRepository.insertTask(title, body);
     /*   Intent notificationIntent=null;
             switch (page)
             {
             case "Home":
                notificationIntent  = new Intent(this, Courses.class);
               break;
              case "Chat":
               //notificationIntent  = new Intent(this, ChatList.class);
               break;
              case "Wallet":
               //notificationIntent  = new Intent(this, Wallet.class);
               break;
              case "PayApprove":
               //notificationIntent  = new Intent(this, PaymentReqList.class);
               break;
              case "UserList":
               //notificationIntent  = new Intent(this, MyTeam.class);
               break;
              case "Quiz":
               //notificationIntent  = new Intent(this, Games.class);
               break;
              default:
                 notificationIntent  = new Intent(this, MainActivity.class);
                break;
          }
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra("notification", body);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);
            //Configure Notification Channel
            notificationChannel.setDescription("Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
         .setSmallIcon(R.drawable.awl_icon)
         .setContentTitle(title)
         .setAutoCancel(true)
         .setSound(defaultSound)
         .setContentText(body)
         .setContentIntent(pendingIntent)
         .setWhen(System.currentTimeMillis())
         .setPriority(Notification.PRIORITY_MAX);
          notificationManager.notify(getID(), notificationBuilder.build());
           */ }
            catch(Exception ee)
            {
                Log.e("Firebase_error",ee.getMessage());
            }

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN",s);
    }






}
