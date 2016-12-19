package com.example.dayana.timetracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    Cursor dbCursor;
    SimpleCursorAdapter dbAdapter;
    String[] allColumns;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(MainActivity.checkUserAccount()) {
            setContentView(R.layout.activity_settings);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        allColumns = new String[] {"_id", MyDbHelper.COL_ACCOUNT, MyDbHelper.COL_EMAIL};
        dbCursor = MainActivity.mydb.query(MyDbHelper.TABLE_NAME, null, null, null, null, null, null);
        if (dbCursor!= null) dbCursor.moveToFirst();
        dbAdapter = new SimpleCursorAdapter(getBaseContext(),
                R.layout.line,
                dbCursor,
                new String[] {MyDbHelper.COL_EMAIL},
                new int[]{R.id.tv_email}, 0);
        ListView listView = (ListView) findViewById(R.id.email_list);
        listView.setAdapter(dbAdapter);

        Button delete_emails_button = (Button)findViewById(R.id.clear_email_list_button);
        delete_emails_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                MyDbHelper.drop();
                Toast.makeText(getApplicationContext(), "E-mail list cleared.", Toast.LENGTH_SHORT).show();

            }
        });

        Button button = (Button)findViewById(R.id.add_email_button);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                EditText email = (EditText)findViewById(R.id.email_input);
                if(email.getText().toString().length()>0){
                    //add email to database
                    String selection = MyDbHelper.COL_EMAIL+" = ?";
                    String[] args = new String[]{email.getText().toString()};
                    Cursor result = MainActivity.mydb.query(MyDbHelper.TABLE_NAME,
                            allColumns,
                            selection,args,null,null,null,null);
                    ContentValues cv = new ContentValues();
                    cv.put("Email", email.getText().toString());
                    if(result.moveToFirst()) {
                        Toast.makeText(getApplicationContext(), "email already exists", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        MainActivity.mydb.insert(MyDbHelper.TABLE_NAME, null, cv);
                        Toast.makeText(getApplicationContext(), "email inserted", Toast.LENGTH_SHORT).show();
                        email.setText("");
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "email can't be empty", Toast.LENGTH_SHORT).show();
                }
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
