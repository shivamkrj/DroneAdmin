package in.shivamkrj.droneadmin;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        Log.d("onNewToken", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("zzzMessageReceoved", "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("zzzPayload", "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
//                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("zzzGetNotification", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference notificationReference = database.getReference("ADMIN-NOTIFICATION");
            itemData item = new itemData();
            item.data = title+" @ "+body;
            item.key = notificationReference.push().getKey();
            notificationReference.child(item.key).setValue(item);
        }
    }
}
