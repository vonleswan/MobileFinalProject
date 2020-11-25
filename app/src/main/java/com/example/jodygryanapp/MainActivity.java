package com.example.jodygryanapp;;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

    }

    public void news(View view){
        // User chose the "News" item, show the app news UI...
        Intent activity_intent = new Intent(MainActivity.this, NewsActivity.class);
        startActivity(activity_intent);
    }

    public void stations(View view){
        // User chose the "News" item, show the app news UI...
        Intent activity_intent = new Intent(MainActivity.this, StationsActivity.class);
        startActivity(activity_intent);
    }

    public void currency(View view){
        // User chose the "News" item, show the app news UI...
        Intent activity_intent = new Intent(MainActivity.this, CurrencyActivity.class);
        startActivity(activity_intent);
    }

    public void recipes(View view){
        // User chose the "News" item, show the app news UI...
        Intent activity_intent = new Intent(MainActivity.this, RecipesActivity.class);
        startActivity(activity_intent);
    }

}
