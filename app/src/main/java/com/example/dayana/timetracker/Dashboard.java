package com.example.dayana.timetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Dashboard extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(MainActivity.checkUserAccount()) {
            setContentView(R.layout.activity_dashboard);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        Button tracking_button = (Button)(findViewById(R.id.tracking_time_button));
        tracking_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Tracking.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        int id = item.getItemId();
        if (id == R.id.account_menu_item) {
            Intent intent = new Intent(this,Account.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.dashboard_menu_item) {
            Intent intent = new Intent(this,Dashboard.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.settings_menu_item) {
            Intent intent = new Intent(this,Settings.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
