package com.example.jodygryanapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.jodygryanapp.blog.FeedListFragment;
import com.example.jodygryanapp.blog.SavedFragment;
import com.google.android.material.snackbar.Snackbar;


public class BlogActivity extends AppCompatActivity{
    private String query = "";
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
        startFeedList();

    }

    public void startFeedList(){
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

            case R.id.action_saved:
                //Start Feed Fragment
                SavedFragment saveFrag = new SavedFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, saveFrag) //Add the fragment in FrameLayout
                        .addToBackStack(null) //make the back button undo the transaction
                        .commit(); //actually load the fragment.
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
        return super.onKeyDown(keyCode, event);
    }

}
