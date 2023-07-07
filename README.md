## CallMe
A fake caller app built in Android Studio allows you to manage a list of contacts and time your fake call for your convenience.
Firebase authentication and a real-time database

### App Flow
The App opens on the main menu, where the user can select to set contacts or make an immediate call. The call now option will start a fake call with the default contact, and setting contacts will open the login screen.
In a fake call, the contact information will show on a screen with buttons and a timer. The user will be able to answer the call and will be met with a text-to-speech response.
From the login screen, you can login with a valid user email and password or register for the system.
Once logged in, the user can add or delete a contact, select a contact to start a call from, and toggle the timed option for the call.
For timed call selection, the app will open up the main menu until the job scheduler starts and activates the call.

### Firebase
The users will be saved in a Firebase system, and for each user, a list of contacts and a text-to-speech string will be stored. In the app, each user will have access only to the contacts that are associated with him.

<p align="center">
  <img src="/rtb.png" width="350" title="rtb pic"/>
</p>
