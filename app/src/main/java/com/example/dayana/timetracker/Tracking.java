package com.example.dayana.timetracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

public class Tracking extends AppCompatActivity{
    final int EMAIL_INTENT_REQUEST_CODE = 1111;
    Chronometer chron;
    String time_lapsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(MainActivity.checkUserAccount()) {
            setContentView(R.layout.activity_tracking);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        //start tracking
        chron = (Chronometer)findViewById(R.id.tracking_timer);
        chron.start();

        //set default button visibility: submit and cancel button invisible until user stops tracking
        findViewById(R.id.submit_tracking_time_button).setVisibility(View.GONE);

        //button behaviors
        final Button stop_button = (Button)findViewById(R.id.stop_tracking_time_button);
        stop_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                chron.stop();
                time_lapsed = chron.getText().toString();
                stop_button.setBackgroundColor(Color.GRAY);
                findViewById(R.id.submit_tracking_time_button).setVisibility(View.VISIBLE);
                findViewById(R.id.cancel_tracking_time_button).setVisibility(View.VISIBLE);
            }
        });
        final Button submit_button = (Button)findViewById(R.id.submit_tracking_time_button);
        submit_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                EditText et = (EditText)findViewById(R.id.project_name_input);
                if(et.getText().toString().length()>0){
                    //send email and return to dashboard upon successful return code
                    send_email();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please enter a Project name", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final Button cancel_button = (Button)findViewById(R.id.cancel_tracking_time_button);
        cancel_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                cancel_alert(v);
            }
        });

    }

    public void send_email(){
        Intent emailIntent = new Intent("android.intent.action.SEND");
        emailIntent.setType("plain/text");
        Cursor result = MainActivity.mydb.query(MyDbHelper.TABLE_NAME,
                new String[] {"_id", MyDbHelper.COL_ACCOUNT, MyDbHelper.COL_EMAIL},
                null,null,null,null,null,null);
        //create list of recipients
        String[] recipients = new String[result.getCount()];
        int ctr = 0;
        //add database results, if any
        if(result != null){
            result.moveToFirst();
            recipients[ctr] = result.getString(2);
            while(result.moveToNext()){
                ctr++;
                recipients[ctr] = result.getString(2);
            }
            emailIntent.putExtra("android.intent.extra.EMAIL", recipients);
            //enter data
            emailIntent.putExtra("android.intent.extra.SUBJECT", "Time Tracked for "+MainActivity.name);
            EditText project_name = (EditText)findViewById(R.id.project_name_input);
            String comment = "N/A";
            EditText comment_text = (EditText)findViewById(R.id.comment_input);
            //filter for dependent data:
            // comment = "N/A" if empty; otherwise, what the user input
            if(comment_text.getText().toString().length()>0){
                comment = comment_text.getText().toString();
            }
            //units change depending on amount of time elapsed
            String units = "";
            if(time_lapsed.length() > 5){
                units = " (hours:minutes:seconds)";
            }
            else{
                units = " (minutes:seconds)";
            }
            emailIntent.putExtra("android.intent.extra.TEXT", "Project: "+ project_name.getText().toString()+"\nTime: "+time_lapsed+units+"\nComment: "+ comment);
            startActivityForResult(Intent.createChooser(emailIntent, "Send mail..."),EMAIL_INTENT_REQUEST_CODE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == EMAIL_INTENT_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                //Return to Dashboard
                Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                startActivity(intent);
            }
        }
    }

    public void cancel_alert(View v){
        new AlertDialog.Builder(this)
                .setTitle("Leave tracking without submitting")
                .setMessage("Are you sure you want to continue without submitting? Your time tracking will not be saved.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Continue cancel without submitting", Toast.LENGTH_SHORT).show();
                        //stop countdown
                        if(chron.isActivated()){
                            chron.stop();
                        }
                        //back to dashboard
                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Back to tracking screen", Toast.LENGTH_SHORT).show();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
