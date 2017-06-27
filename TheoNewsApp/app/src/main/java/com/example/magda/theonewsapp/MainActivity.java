package com.example.magda.theonewsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private NewsAdapter mAdapter;
    private static final String GUARDIAN_API_URL =
            "https://content.guardianapis.com/search?q=debate&api-key=test";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list);
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        listView.setAdapter(mAdapter);
        LoaderManager loaderManager = getLoaderManager();
        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(LOADER_ID, null, MainActivity.this);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "Try to fetch the news");
        return new NewsLoader(this, GUARDIAN_API_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        Log.v(LOG_TAG, "Data loaded");
        if (newsList != null && !newsList.isEmpty()) {
            mAdapter.addAll(newsList);
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
}
