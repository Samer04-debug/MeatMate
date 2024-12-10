package com.example.meatmateapplication.Activity.SendNotification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.meatmateapplication.Activity.adminFoodPanel.AdminPreparedOrderView;
import com.example.meatmateapplication.Activity.AdminFoodPanel_BottomNavigation;
import com.example.meatmateapplication.Activity.CustomerFoodPanel_BottomNavigation;
import com.example.meatmateapplication.Activity.DeliveryFoodPanel_BottomNavigation;
import com.example.meatmateapplication.Activity.MainActivity;
import com.example.meatmateapplication.Activity.customerFoodPanel.PayableOrders;
import com.example.meatmateapplication.R;

import java.util.Random;

public class ShowNotification {

    private static final String CHANNEL_ID = "NOTICE";
    private static final String CHANNEL_NAME = "NOTICE";

    public static void ShowNotif(Context context, String title, String message, String page) {

        // Create Notification Channel (only once)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        // Prepare Intent and PendingIntent
        Intent intent = getIntentForPage(context, page);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE // Ensure compatibility with newer Android versions
        );

        // Build Notification
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.butchering)
                .setColor(ContextCompat.getColor(context, R.color.red))
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        // Display Notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        int randomId = new Random().nextInt(9999 - 1) + 1;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(randomId, nBuilder.build());
    }

    private static Intent getIntentForPage(Context context, String page) {
        Intent intent;
        switch (page.trim().toLowerCase()) {
            case "order":
                intent = new Intent(context, AdminFoodPanel_BottomNavigation.class).putExtra("PAGE", "Orderpage");
                break;
            case "payment":
                intent = new Intent(context, PayableOrders.class);
                break;
            case "home":
                intent = new Intent(context, CustomerFoodPanel_BottomNavigation.class).putExtra("PAGE", "Homepage");
                break;
            case "confirm":
                intent = new Intent(context, AdminFoodPanel_BottomNavigation.class).putExtra("PAGE", "Confirmpage");
                break;
            case "preparing":
                intent = new Intent(context, CustomerFoodPanel_BottomNavigation.class).putExtra("PAGE", "Preparingpage");
                break;
            case "prepared":
                intent = new Intent(context, CustomerFoodPanel_BottomNavigation.class).putExtra("PAGE", "Preparedpage");
                break;
            case "deliveryorder":
                intent = new Intent(context, DeliveryFoodPanel_BottomNavigation.class).putExtra("PAGE", "DeliveryOrderpage");
                break;
            case "deliverorder":
                intent = new Intent(context, CustomerFoodPanel_BottomNavigation.class).putExtra("PAGE", "DeliverOrderpage");
                break;
            case "acceptorder":
                intent = new Intent(context, AdminFoodPanel_BottomNavigation.class).putExtra("PAGE", "AcceptOrderpage");
                break;
            case "rejectorder":
                intent = new Intent(context, AdminPreparedOrderView.class).putExtra("PAGE", "RejectOrderpage");
                break;
            case "thankyou":
                intent = new Intent(context, CustomerFoodPanel_BottomNavigation.class).putExtra("PAGE", "ThankYoupage");
                break;
            case "delivered":
                intent = new Intent(context, AdminFoodPanel_BottomNavigation.class).putExtra("PAGE", "Deliveredpage");
                break;
            default:
                // Default fallback intent
                intent = new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        return intent;
    }
}
