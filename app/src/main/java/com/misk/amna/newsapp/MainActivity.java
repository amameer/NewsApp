package com.misk.amna.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final static String REQUEST_URL = "https://newsapi.org/v1/articles?source=reuters&sortBy=top&apiKey=57d375397bdc4921bea4a7f3537fa45d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //from https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html#DetermineType
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected){
            Toast.makeText(getApplicationContext(),"No Enternet!!",Toast.LENGTH_LONG).show();

            return;
        }


        new NewAsyncTask().execute();

    }



    private class NewAsyncTask extends AsyncTask<URL, Void, ArrayList<New>> {

        @Override
        protected ArrayList<New> doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(REQUEST_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e("HTTP request ::", "Problem making the HTTP request.", e);
            }

            ArrayList<New> news = extractFeatureFromJson(jsonResponse);

            if (news != null)
                return news;
            else
                return null;
        }

        @Override
        protected void onPostExecute(ArrayList<New> news) {
            UpdateList(news);

        }


        private void UpdateList(ArrayList<New> news) {

            ListView newListView = (ListView) findViewById(R.id.list);
            TextView EmptyTextView = (TextView) findViewById(R.id.empty_view);
            newListView.setEmptyView(EmptyTextView);
            final NewAdapter adapter = new NewAdapter(getApplicationContext(), news);

            if (news != null) {

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

            } else {

                EmptyTextView.setText("No Result Please enter another keyword");
                //clear if no result
                newListView.setAdapter(null);
            }
        }


        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e("URL:::", "Error with creating URL", exception);
                return null;
            }
            return url;
        }


        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            // If the URL is null, then return early.
            if (url == null) {
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();

                // If the request was successful (response code 200),
                // then read the input stream and parse the response.
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e("Error::", "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e("retrieving::", "Problem retrieving the JSON results.", e);

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }


        public ArrayList<New> extractFeatureFromJson(String newJson) {

            ArrayList<New> news = new ArrayList<>();

            try {
                JSONObject baseJsonResponse = new JSONObject(newJson);


                JSONArray newArray = baseJsonResponse.getJSONArray("articles");

                for (int i = 0; i < newArray.length(); i++) {

                    JSONObject CurrentNew = newArray.getJSONObject(i);
                    String Title = CurrentNew.getString("title");
                    String Description = CurrentNew.getString("description");
                    String Author = CurrentNew.getString("author");
                    String Url = CurrentNew.getString("url");


                    New mNew =new New(Author,Title , Url,Description);
                    news.add(mNew);

                }
                Log.i("hjkhjk",news.get(0).toString());
                return news;
            } catch (JSONException e) {

                Log.e("extractFromJson::", "Problem parsing the earthquake JSON results", e);
            }
            return null;

        }
    }

}
