package com.example.magda.theonewsapp;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private NewsAdapter mAdapter;
    private static final String GUARDIAN_API_URL =
            "https://content.guardianapis.com/search?q=";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 1;
    private TextView mEmptyStateView;
    private View loadingIndicator;
    private String searchText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list);
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        listView.setAdapter(mAdapter);
        mEmptyStateView = (TextView) findViewById(R.id.empty_list_view);
        listView.setEmptyView(mEmptyStateView);
        loadingIndicator = findViewById(R.id.loading_indicator);

        if (isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(LOADER_ID, null, MainActivity.this);
        } else {

            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateView.setText(R.string.no_connection);
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri guardianUri = Uri.parse(currentNews.getWebUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, guardianUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
        //Intent intent = getIntent();
        //String query = intent.getExtras().getString("search");
        //if(!query.isEmpty()){
        //}

    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String tag = sharedPrefs.getString(
                getString(R.string.settings_tags_key),
                getString(R.string.settings_tags_default));
        Uri baseUri;
        if (!searchText.isEmpty()) {
            baseUri = Uri.parse(GUARDIAN_API_URL + searchText);
        } else {
            baseUri = Uri.parse(GUARDIAN_API_URL);
        }

        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("total", "20");
        uriBuilder.appendQueryParameter("orderBy", "newest");
        if (!tag.isEmpty()) {
            uriBuilder.appendQueryParameter("tag", tag);
        }

        Log.v(LOG_TAG, "request:" + uriBuilder.toString() + "&api-key=test");

        return new NewsLoader(this, uriBuilder.toString() + "&api-key=test");
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        loadingIndicator.setVisibility(View.GONE);
        Log.v(LOG_TAG, "Data loaded");
        if (newsList != null && !newsList.isEmpty()) {
            this.mAdapter.addAll(newsList);
        } else {
            mEmptyStateView.setText(R.string.no_news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }

    public boolean isConnected() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        //SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//              if (searchView.isExpanded() && TextUtils.isEmpty(newText)) {
                callSearch(newText);
//              }
                return true;
            }

            public void callSearch(String query) {
                mAdapter.clear();
                if (!query.isEmpty()) {
                    searchText = query.replace(" ", "%20");
                }
                getLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);

            }

        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
