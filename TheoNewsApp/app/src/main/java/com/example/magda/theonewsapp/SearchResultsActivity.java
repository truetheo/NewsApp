package com.example.magda.theonewsapp;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Magda on 28.06.2017.
 */

public class SearchResultsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            String searchPart = query.replace(" ", "%20");
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("search", searchPart);
            startActivity(i);

        }
    }
}
