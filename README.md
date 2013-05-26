chatroom
========

This is an android application which use Google Cloud Messing to communicate with registered device.

Before compiling the project couple to things you have to do:

1. Go to Google APIs Console page (https://code.google.com/apis/console) and create a new project
2. Enable Google Cloud Messing Service from console
3. From Overview Tab copy the Project Number and put it in CommonUtilities.GOOGLE_SENDER_ID
4. From API Access Tab copy API key and put it in config.php file in crserver php project.


Help
==============
If Intellij don't show Device connected after run then its because daemon not running
>./adb devices -l

Have to use Google API Level -17 while creating AVD otherwise not possible to add google account
which is required for Google Cloud Messing. Use less resolution like 400X800, RAM 512 VM Heap 256 while creating AVD
otherwise it will take more time to load the emulator.
