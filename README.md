## CallMe
A fake caller app built in android studio, this app allow you to manage a list of contacts and time your fake call for your convinience.
Firebase authentication and real time database.

### App Flow
The App opens on the main menu where the user can select to set contacts or and immediate call, call now option will start a fake call with default contact, the set contcts will open the login screen.
In a fake call the contact info will show and a screen with buttons and timer, the user will be able to answer the call and will be met with text to speech response.
From the login screen you can login with a valid user email and password or register into the system.
Once logged in the user can add or delete a contact, he can select a contact to start a call from and can toggle the timed option for the call.
For timed call selection the app with open up the main menu until the jon schedular will start and activate the call.

### Firebase
The users will be saved in a firebase system and for each user a list of contact and a text to speech string, in the app each user would have access only to the contacts that's assosicated with him.
