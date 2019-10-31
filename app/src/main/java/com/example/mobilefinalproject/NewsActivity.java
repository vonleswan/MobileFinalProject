package com.example.mobilefinalproject;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

public class NewsActivity extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_news:

                return true;

            case R.id.action_currencies:
                // User chose the "Currencies" action, show currencies UI ...
                return true;

            case R.id.action_recipes:
                // User chose the "Recipes" action, show recipes UI ...
                return true;

            case R.id.action_stations:
                // User chose the "Stations" action, show stations UI ...
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
