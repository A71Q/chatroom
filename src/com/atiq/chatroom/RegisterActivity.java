package com.atiq.chatroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.atiq.chatroom.CommonUtilities.GOOGLE_SENDER_ID;
import static com.atiq.chatroom.CommonUtilities.SERVER_URL_REG;

public class RegisterActivity extends Activity {

    AlertDialogManager alertDialogManager = new AlertDialogManager();
    ConnectionDetector connectionDetector;

    EditText txtName;
    EditText txtEmail;
    Button btnRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        connectionDetector = new ConnectionDetector(getApplicationContext());
        if (!connectionDetector.isConnectingToInternet()) {
            alertDialogManager.showAlertDialog(RegisterActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            return;
        }

        if (SERVER_URL_REG.length() == 0 || GOOGLE_SENDER_ID.length() == 0) {
            alertDialogManager.showAlertDialog(RegisterActivity.this, "Configuration Error!",
                    "Please set your Server URL and GCM Sender ID", false);
            return;
        }

        txtName = (EditText) findViewById(R.id.txtName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                String name = txtName.getText().toString();
                String email = txtEmail.getText().toString();

                if (name.trim().length() > 0 && email.trim().length() > 0) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                } else {
                    alertDialogManager.showAlertDialog(RegisterActivity.this, "Registration Error!", "Please enter name and email", false);
                }
            }
        });
    }

}
