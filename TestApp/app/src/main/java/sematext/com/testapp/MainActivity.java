package sematext.com.testapp;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sematext.android.Logsene;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity with two buttons that shows how to use Logsene for Android.
 *
 * NOTE: You will need to update the manifest with your app token. You can get the app token by
 * registering for a free account and creating a Logsene app:
 *
 *     https://apps.sematext.com/users-web/register.do
 *
 */
public class MainActivity extends Activity {
    private Logsene logsene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logsene = new Logsene(this);

        Log.e("INFO", "Android version: " + Build.VERSION.RELEASE);

        try {
            // Set some default meta properties to be sent with each message
            JSONObject meta = new JSONObject();
            meta.put("user", "user@example.com");
            meta.put("userType", "free");
            Logsene.setDefaultMeta(meta);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logsene.info("Hello World!");
            }
        });

        Button troubleButton = (Button) findViewById(R.id.troubleButton);
        troubleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // will always fail
                    JSONObject obj = new JSONObject("not valid json!");
                } catch (JSONException e) {
                    // send to centralized log with stacktrace
                    logsene.error(e);
                }
            }
        });

        Button crashButton = (Button) findViewById(R.id.crashButton);
        crashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 1 / 0;
            }
        });

        try {
            JSONObject event = new JSONObject();
            event.put("activity", this.getClass().getSimpleName());
            event.put("action", "started");
            logsene.event(event);
        } catch (JSONException e) {
            Log.e("myapp", "Unable to construct json", e);
        }
    }
}
