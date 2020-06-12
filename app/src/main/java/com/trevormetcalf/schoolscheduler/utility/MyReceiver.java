package com.trevormetcalf.schoolscheduler.utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.trevormetcalf.schoolscheduler.R;
import com.trevormetcalf.schoolscheduler.model.Assessment;
import com.trevormetcalf.schoolscheduler.model.Course;
import com.trevormetcalf.schoolscheduler.view.AddAssessmentActivity;
import com.trevormetcalf.schoolscheduler.view.AddCourseActivity;

/*
    This class defines the push notifications for course start and end dates as well as
    assessment due dates. The type of notification is discovered by checking hasExtra().
 */

public class MyReceiver extends BroadcastReceiver {

    int notificationID;
    public static final String CHANNEL_1_ID = "Course Alerts";
    public static final String CHANNEL_2_ID = "Assessment Alerts";
    Notification notification;
    NotificationManager notificationManager;



    @Override
    public void onReceive(Context context, Intent intent) {
    // Course start date notification.
        if (intent.hasExtra(AddCourseActivity.EXTRA_COURSE_DATE_START)) {
            // Serial provided from TermDetailActivity or CourseDetailActivity.
            String courseSerial = intent.getStringExtra(AddCourseActivity.EXTRA_COURSE_DATE_START);
            // Use hash to provide unique integer for notification ID.
            notificationID = courseSerial.hashCode();
            // Get course from serialized data, show a course specific toast message.
            Course course = (Course) Serializer.deserialize(courseSerial);
            Toast.makeText(context, course.getTitle() + "\n"
                    + "Start Date: " + DateFormatter.formatDate(course.getDateStart()), Toast.LENGTH_LONG).show();
            createNotificationChannel(context, CHANNEL_1_ID);
            // Build and show the notification.
            notification = new Notification.Builder(context, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_school)
                    .setContentText(course.getTitle() + " begins today!")
                    .setContentTitle("Course Alert!").build();
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationID, notification);

    // Course end date notification.
        } else if (intent.hasExtra(AddCourseActivity.EXTRA_COURSE_DATE_END)) {
            // Serial provided from TermDetailActivity or CourseDetailActivity.
            String courseSerial = intent.getStringExtra(AddCourseActivity.EXTRA_COURSE_DATE_END);
            // Use hash to provide unique integer for notification ID.
            notificationID = courseSerial.hashCode() + 1;
            // Get course from serialized data, show a course specific toast message.
            Course course = (Course) Serializer.deserialize(courseSerial);
            Toast.makeText(context, course.getTitle() + "\n"
                    + "End Date: " + DateFormatter.formatDate(course.getDateEnd()), Toast.LENGTH_LONG).show();
            createNotificationChannel(context, CHANNEL_1_ID);
            // Build and show the notification.
            notification = new Notification.Builder(context, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_school)
                    .setContentText(course.getTitle() + " ends today!")
                    .setContentTitle("Course Alert.").build();
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationID, notification);

    // Assessment due date notification
        } else if (intent.hasExtra(AddAssessmentActivity.EXTRA_ASSESSMENT)) {
            // Serial passed from CourseDetailActivity or AssessmentDetailActivity.
            String assessmentSerial = intent.getStringExtra(AddAssessmentActivity.EXTRA_ASSESSMENT);
            // Use hash to provide unique integer for notification ID.
            notificationID = assessmentSerial.hashCode();
            // Get assessment from serialized data, show the assessment specific toast message.
            Assessment assessment = (Assessment) Serializer.deserialize(assessmentSerial);
            Toast.makeText(context, assessment.getTitle() + "\n"
                    + "Due Date: " + DateFormatter.formatDate(assessment.getDateDue()), Toast.LENGTH_LONG).show();
            createNotificationChannel(context, CHANNEL_2_ID);
            // Build and show the notification.
            notification = new NotificationCompat.Builder(context, CHANNEL_2_ID)
                    .setSmallIcon(R.drawable.ic_school)
                    .setContentText(assessment.getTitle() + " due today!")
                    .setContentTitle("Assessment Alert.").build();
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationID, notification);
        }
    }

    // Create a notification channel.
    private void createNotificationChannel(Context context, String channelID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getResources().getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system.
            // Behaviors and importance cannot be changed after this.
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
