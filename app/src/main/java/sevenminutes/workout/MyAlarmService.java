package sevenminutes.workout;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
                           

public class MyAlarmService extends Service 

{
     private NotificationManager mManager;

     @Override
     public IBinder onBind(Intent arg0)
     {
       // TODO Auto-generated method stub
        return null;
     }

    @Override
    public void onCreate() 
    {
       // TODO Auto-generated method stub  
       super.onCreate();
    }

   @SuppressWarnings("static-access")
   @Override
   public void onStart(Intent intent, int startId)
   {
       super.onStart(intent, startId);
     
       this.getApplicationContext();
	mManager = (NotificationManager) this.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
	   Intent intent1 = new Intent(this.getApplicationContext(),Home.class);
	
	   Notification notification = new Notification(R.drawable.start_circle,"Time to Workout", System.currentTimeMillis());
	
	   intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);

	   PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);

       Notification.Builder builder = new Notification.Builder(getApplicationContext()).setContentIntent(pendingNotificationIntent)
               .setSmallIcon(R.drawable.ic_launcher).setContentTitle("7 Min Workout")
               .setContentText("Time to Workout");
       builder.setAutoCancel(true);
       notification.flags |= Notification.FLAG_AUTO_CANCEL;
       notification = builder.build();
      // notification.flags |= Notification.FLAG_AUTO_CANCEL;
      // notification.setLatestEventInfo(this.getApplicationContext(), "7 Min Workout", "Time to Workout", pendingNotificationIntent);
       Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
       notification.sound = uri;
       mManager.notify(0, notification);
    }

    @Override
    public void onDestroy() 
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}