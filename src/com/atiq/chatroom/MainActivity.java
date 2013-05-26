package com.atiq.chatroom;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gcm.GCMRegistrar;

import static com.atiq.chatroom.CommonUtilities.*;

/**
 * @author Atiqur Rahman
 */
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    TextView lblMessage;
    EditText sendText;
    Button btnSend;
    AsyncTask<Void, Void, Void> asyncTask;

    AlertDialogManager alert = new AlertDialogManager();

    ConnectionDetector connectionDetector;

    public static String name;
    public static String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectionDetector = new ConnectionDetector(getApplicationContext());

        if (!connectionDetector.isConnectingToInternet()) {
            alert.showAlertDialog(MainActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            return;
        }

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");

        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);

        lblMessage = (TextView) findViewById(R.id.lblMessage);
        sendText = (EditText) findViewById(R.id.sendText);
        btnSend = (Button) findViewById(R.id.btnSend);

        registerReceiver(broadcastReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
        final String regId = GCMRegistrar.getRegistrationId(this);

        btnSend.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                final String text = sendText.getText().toString();
                if (text.trim().length() > 0) {
                    Log.i(TAG, "Sending Text:" + text);
                    asyncTask = new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {
                            ServerUtilities.sendTextToServerForBroadcast(MainActivity.this, text, regId);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            asyncTask = null;
                        }

                    };
                    asyncTask.execute(null, null, null);
                }
            }
        });

        if (regId.equals("")) {
            GCMRegistrar.register(this, GOOGLE_SENDER_ID);
        } else {
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
            } else {
                final Context context = this;
                asyncTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        ServerUtilities.register(context, name, email, regId);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        asyncTask = null;
                    }

                };
                asyncTask.execute(null, null, null);
            }
        }
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getExtras().getString(EXTRA_MESSAGE);

            WakeLocker.acquire(getApplicationContext());

            lblMessage.append(msg + "\n");
            lblMessage.setMovementMethod(new ScrollingMovementMethod());
            lblMessage.setScrollBarStyle(0x03000000);
            lblMessage.setVerticalScrollBarEnabled(true);
            lblMessage.setTextColor(0xFF000000);
            Toast.makeText(getApplicationContext(), "New Message: " + msg, Toast.LENGTH_LONG).show();

            WakeLocker.release();
        }
    };

    @Override
    protected void onDestroy() {
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
        try {
            unregisterReceiver(broadcastReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

}
