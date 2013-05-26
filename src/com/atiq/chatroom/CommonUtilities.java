package com.atiq.chatroom;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {

    static final String SERVER_URL_REG = "http://10.1.1.12/register.php";
    static final String SERVER_URL_BROADCAST = "http://10.1.1.12/broadcast.php";

    static final String GOOGLE_SENDER_ID = "456287124796";

    static final String DISPLAY_MESSAGE_ACTION = "com.atiq.chatroom.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";

    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
