package com.example.dayana.timetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Account extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(MainActivity.checkUserAccount()) {
            setContentView(R.layout.activity_account);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        //set data to account data
        TextView tv_name = (TextView) findViewById(R.id.account_display_name);
        tv_name.setText(MainActivity.name);
        TextView tv_email = (TextView) findViewById(R.id.account_display_email);
        tv_email.setText(MainActivity.email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
