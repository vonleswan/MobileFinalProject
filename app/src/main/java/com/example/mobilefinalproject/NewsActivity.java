package com.example.mobilefinalproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.example.mobilefinalproject.news.FeedListFragment;
import com.example.mobilefinalproject.news.SavedFragment;
import com.google.android.material.snackbar.Snackbar;


public class NewsActivity extends AppCompatActivity{
    private String query = "bitcoin";
    private FeedListFragment feedFrag;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle(null);

        //Start Feed Fragment
        feedFrag = new FeedListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentLocation, feedFrag) //Add the fragment in FrameLayout
                .addToBackStack("AnyName") //make the back button undo the transaction
                .commit(); //actually load the fragment.
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
            case R.id.action_search:
                showDialog();
                break;

            case R.id.action_toast:
                Toast.makeText(this, "You like toast huh?", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_saved:
                //Start Feed Fragment
                SavedFragment saveFrag = new SavedFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, saveFrag) //Add the fragment in FrameLayout
                        .addToBackStack(null) //make the back button undo the transaction
                        .commit(); //actually load the fragment.
                break;

            case R.id.action_snackbar:
                Snackbar sb = Snackbar.make(findViewById(R.id.toolbar), "Go Back?", Snackbar.LENGTH_LONG)
                        .setAction("Exit", e -> finish());
                sb.show();
                break;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
        return true;
    }

    public void showDialog() {
        View layout = getLayoutInflater().inflate(R.layout.search_dialog, null);
        final EditText search = layout.findViewById(R.id.input);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        query = search.getText().toString();
                        feedFrag.updateList(query);
                    }
                })
                .setView(layout);
        builder.create().show();
    }

    public void saved(View view){

    }

}
