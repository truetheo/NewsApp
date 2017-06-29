package com.example.magda.theonewsapp;

import android.text.TextUtils;
import android.util.Log;

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
import java.util.List;

/**
 * Created by Magda on 27.06.2017.
 */

public final class QueryUtils {
    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final int READ_TIMEOUT = 5000; /* milliseconds */
    private static final int CONNECT_TIMEOUT = 6000; /* milliseconds */

    public QueryUtils() {
    }

    public static List<News> fetchBookData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        // Extract relevant fields from the JSON response and create a list of {@link News}s
        // Return the list of {@link News}s
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == urlConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
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

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<News> newsList = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            // Extract the JSONArray associated with the key called "results",
            // which represents a list of items.
            JSONArray newsArray = response.getJSONArray("results");

            // For each news in the newsArray, create an {@link News} object
            for (int i = 0; i < newsArray.length(); i++) {

                // Get a single news at position i within the list of news
                JSONObject currentNews = newsArray.getJSONObject(i);

                // For a given news, extract the JSONObject associated with the
                // key called "results", which represents a list of detailed information
                // for that book.
                String title;
                if (currentNews.has("webTitle")) {
                    // Extract the value for the key called "webTitle"
                    title = currentNews.getString("webTitle");

                } else {
                    title = "No title";
                }
                // Extract the Array for the key called "sectionName"
                String sectionId;
                if (currentNews.has("sectionName")) {
                    sectionId = currentNews.getString("sectionName");

                } else {
                    sectionId = "section unknown";
                }
                String webUrl;
                if (currentNews.has("webUrl")) {
                    // Extract the value for the key called "webTitle"
                    webUrl = currentNews.getString("webUrl");

                } else {
                    webUrl = "No webUrl";
                }
                String webPublicationDate;
                if (currentNews.has("webPublicationDate")) {
                    // Extract the value for the key called "webTitle"
                    webPublicationDate = currentNews.getString("webPublicationDate");

                } else {
                    webPublicationDate = "No webPublicationDate";
                }
                // Create a new {@link GoogleBook} object with the title, author.
                News article = new News(title, sectionId, webUrl, webPublicationDate);
                // Add the new {@link GoogleBook} to the list of books.
                newsList.add(article);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("Utils", "Problem parsing the news JSON results", e);
        }

        // Return the list of books
        return newsList;
    }
}
