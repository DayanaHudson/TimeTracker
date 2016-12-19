package com.example.dayana.timetracker;
//credit:
//https://github.com/googlesamples/google-services/blob/master/android/signin/app/src/main/java/com/google/samples/quickstart/signin/SignInActivity.java
//https://github.com/googlesamples/google-services/issues/144

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    public static GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 1;
    public static String name;
    public static String email;
    public static String id;
    public static String clientID = "1068603518264-i9k06pke3qd70ea6tpca4qdosaelm2bk.apps.googleusercontent.com"; //Web app ID
    public static SQLiteDatabase mydb;
    public static MyDbHelper mydbh;

    //https://developers.google.com/identity/sign-in/android/sign-in
    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    static GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(clientID)
            .requestServerAuthCode(clientID)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydbh = new MyDbHelper(this);
        mydb = mydbh.getWritableDatabase();
        OnClickListener ocl = new View.OnClickListener(){
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        };

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        // Button Listeners
        findViewById(R.id.sign_in_button).setOnClickListener(ocl);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mGoogleApiClient == null){
            // Build a GoogleApiClient with access to the Google Sign-In API and the
            // options specified by gso.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .addApi(Drive.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();
        System.out.println("trying to connect..."+mGoogleApiClient.isConnecting());
        if(checkPlayServices() && checkUserAccount()){
            //Toast.makeText(getApplicationContext(), "checkPlayServices true", Toast.LENGTH_SHORT).show();
        }
        else{
            //Toast.makeText(getApplicationContext(), "checkPlayservices false", Toast.LENGTH_SHORT).show();
        }
    }

    //http://www.androiddesignpatterns.com/2013/01/google-play-services-setup.html
    //http://stackoverflow.com/questions/36218434/non-static-method-isgoogleplayservicesavailable-and-geterrordialog-cannot-be-ref
    private boolean checkPlayServices() {
        GoogleApiAvailability ga = GoogleApiAvailability.getInstance();
        System.out.println("ga == null? "+(ga == null));
        int status = ga.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            ga.getErrorDialog(this, status, 1).show();
        }
        System.out.println("checkPlayServices result: "+(status == ConnectionResult.SUCCESS));
        return status == ConnectionResult.SUCCESS;
    }

    public static boolean checkUserAccount() {
        return (id!=null);
    }

    private void signIn() {
        System.out.println("Signing in");
        System.out.println("trying to connect..."+mGoogleApiClient.isConnecting());
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public static void externalSignOut(View v) {
        name = null;
        id = null;
        email = null;
        mGoogleApiClient.connect();
        System.out.println("2 Connected? "+mGoogleApiClient.isConnected());
        System.out.println("Signing out");
        Toast.makeText(v.getContext(), "SIGNING OUT)", Toast.LENGTH_SHORT).show();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        System.out.println("SIGNED OUT");
                    }
                });
    }

    private void revokeAccess() {
        Toast.makeText(getApplicationContext(), "Disconnected method called (need to check if GoogleApiClient.onConnected has been called)", Toast.LENGTH_SHORT).show();
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("requestcode: "+requestCode);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        String[] allColumns = new String[] {"_id", MyDbHelper.COL_ACCOUNT, MyDbHelper.COL_EMAIL};
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        System.out.println("successful? "+result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            name = acct.getDisplayName();
            email = acct.getEmail();
            id = acct.getId();
            //check if user already exists in database
            String selection = MyDbHelper.COL_EMAIL+" = ?";
            String[] args = new String[]{email};
            Cursor cursor = mydb.query(MyDbHelper.TABLE_NAME,
                    allColumns,
                    selection,args,null,null,null,null);
            ContentValues cv = new ContentValues();
            cv.put("Email", email);
            if(cursor.moveToFirst()) {
                //Toast.makeText(getApplicationContext(), "email already exists", Toast.LENGTH_SHORT).show();
            }
            else{
                mydb.execSQL("INSERT INTO emails(Account, Email) VALUES('"+id+"','"+email+"');");
            }

            updateUI(true);
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            Intent intent = new Intent(this,Dashboard.class);
            startActivity(intent);
        }
    }

    public void onConnected(Bundle bundle){
        System.out.print("-------------------------CONNECTED ---------------------------------------");
    };
    public void onConnectionSuspended(int i){
        System.out.print("------------------------CONNECTION SUSPENDED-------------------------------");
    };
    public void onConnectionFailed(ConnectionResult c){
        System.out.print("-----------------------CONNECTION FAILED-------------------------------"+c.getErrorMessage()+" error code: "+c.getErrorCode());
    };
}