package au.com.thecommuters.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import au.com.thecommuters.DriverRatingActivity;
import au.com.thecommuters.R;

public class SolutionFinder extends Service {

	private NotificationManager mNM;

	public static int i = 10;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {

		super.onCreate();
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Timer timer = new Timer();
		SolutionFinderUpdater updater = new SolutionFinderUpdater();
		timer.schedule(updater,0, 30000);

	}

	private class SolutionFinderUpdater extends TimerTask {

		
		@Override
		public void run() {
			Notification notification = new Notification(R.drawable.star, "",
					System.currentTimeMillis());

			// The PendingIntent to launch our activity if the user selects this
			// notification
			PendingIntent contentIntent =
			PendingIntent.getActivity(SolutionFinder.this, 0,
			new Intent(SolutionFinder.this, DriverRatingActivity.class), 0);
			System.out.println("running");
			// Set the info for the views that show in the notification panel.
			if(i>0){
				notification.setLatestEventInfo(SolutionFinder.this, " Bus status update", "your bus will arrive in "+ i+" minutes", contentIntent);

				// Send the notification.
				mNM.notify(1, notification);

			}else{
				notification.setLatestEventInfo(SolutionFinder.this, " Bus status update", "Your bus arrived", contentIntent);

				// Send the notification.
				mNM.notify(1, notification);
			}
						
			i--;

		}

	}

}
