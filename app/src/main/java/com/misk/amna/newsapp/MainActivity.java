package com.misk.amna.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<New>> {

    final static String REQUEST_URL = "https://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date=2014-01-01&api-key=test";
    TextView EmptyTextView;
    NewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://github.com/udacity/ud843-QuakeReport/blob/lesson-three/app/src/main/java/com/example/android/quakereport/EarthquakeActivity.java

        ListView newListView = (ListView) findViewById(R.id.list);
        EmptyTextView = (TextView) findViewById(R.id.empty_view);
        newListView.setEmptyView(EmptyTextView);
        adapter = new NewAdapter(this, new ArrayList<New>());

        newListView.setAdapter(adapter);
        newListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                New currentNew = adapter.getItem(position);

                Uri newUri = Uri.parse(currentNew.getUrl());

                Intent DetailsIntent = new Intent(Intent.ACTION_VIEW, newUri);

                startActivity(DetailsIntent);
            }
        });
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(1, null, this);
            ;
        } else {
            // Otherwise, display error

            // Update empty state with no connection error message
            EmptyTextView.setText("No Enternet");
        }
    }

    @Override
    public Loader<List<New>> onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL
        return new NewLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<New>> loader, List<New> news) {
// Set empty state text to display "No earthquakes found."
        EmptyTextView.setText("No Result Please enter another keyword");

        // Clear the adapter of previous earthquake data
        adapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty()) {
            adapter.addAll(news);
        }


    }

    @Override
    public void onLoaderReset(Loader<List<New>> loader) {

        adapter.clear();

    }

}



