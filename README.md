# TimeTracker
Android app to track activities and send e-mail reports to one or more e-mail addresses.

## Usage
Download the code and build your own .apk file or download the provided file located at TimeTracker/app-debug.apk
Log in with your Google account to track time spent on personal or work projects. Add a project name (required) and a comment (optional) describing any updates, progress, or details to be associated with the tracking session. Then, send a copy to yourself and to any desired contacts (set in the Settings menu tab).

## Details

Tracking
-	Users can cancel tracking sessions
- The tracking counter can continue as long as the app is not terminated
-	The counter only stops if it is manually terminated by the user clicking the “CANCEL” button, the back button, or the “STOP TRACKING TIME” button, or if the app is terminated (e.g. if it is closed from the list of open applications)
-	Tracking activity is marked clearly by the color of the “STOP TRACKING TIME” button; it is red when the app is tracking time, and it turns grey when the app stops tracking time

E-mail
-	Users can add multiple e-mail addresses to send tracking details to
-	E-mail list can be cleared (but it will always include the user’s e-mail)
-	Users must set a project name before sending the tracking details

Formatting
-	Empty comments are represented as “N/A” in the e-mail
-	Units are automatically updated depending on the amount of time spanned
-	Displays either XX:XX (minutes:seconds) or XX:XX:XX (hours:minutes:seconds)

## History
12/19/2016: Initial release

## Credits
- https://github.com/googlesamples/google-services/blob/master/android/signin/app/src/main/java/com/google/samples/quickstart/signin/SignInActivity.java
- https://github.com/googlesamples/google-services/issues/144
- https://developers.google.com/identity/sign-in/android/sign-in
- http://www.androiddesignpatterns.com/2013/01/google-play-services-setup.html
- http://stackoverflow.com/questions/36218434/non-static-method-isgoogleplayservicesavailable-and-geterrordialog-cannot-be-ref
- Readme: https://gist.github.com/zenorocha/4526327

## License
MIT License. Please see the LICENSE file for details.
