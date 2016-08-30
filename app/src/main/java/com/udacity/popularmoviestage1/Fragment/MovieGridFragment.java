package com.udacity.popularmoviestage1.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.udacity.popularmoviestage1.BuildConfig;
import com.udacity.popularmoviestage1.MovieDetailActivity;
import com.udacity.popularmoviestage1.R;
import com.udacity.popularmoviestage1.adapter.ImageAdapter;
import com.udacity.popularmoviestage1.model.ImageObject;
import com.udacity.popularmoviestage1.model.Results;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by rj125e on 8/25/2016.
 */
public class MovieGridFragment extends Fragment{

   View rootView;
   private GridView gridView= null;
   private ArrayList<ImageObject> mListImgObj = null;
   private final String LOG_TAG = MovieGridFragment.class.getSimpleName();
   ImageAdapter imageAdapter;

   public MovieGridFragment(){
       // super();
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      //setHasOptionsMenu(true);
   }

 /*  @Override
   public boolean onOptionsItemSelected(MenuItem item) {

      int id = item.getItemId();
      if(id == R.id.action_Highest){
         updateMovieGrid();
         return true;
      }else if(id == R.id.action_popular){
         updateMovieGrid();
         return true;
      }

      return super.onOptionsItemSelected(item);
   }
*/
   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      rootView = inflater.inflate(R.layout.content_main,container,false);
      initView();
      return rootView;
   }

   public void initView(){

      mListImgObj = new ArrayList<ImageObject>();

      gridView = (GridView) rootView.findViewById(R.id.gridMovie);
      imageAdapter = new ImageAdapter(getActivity(),R.layout.content_main, mListImgObj);
     // imageAdapter = new ImageAdapter(getActivity(), mListImgObj);
      gridView.setAdapter(imageAdapter);
      gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getActivity(), "Grid View Click", Toast.LENGTH_SHORT).show();
            ImageObject imgObj = mListImgObj.get(position);
            Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                    .putExtra("image_details", imgObj);
            startActivity(intent);


         }
      });
   }


   @Override
   public void onStart() {
      super.onStart();
      updateMovieGrid();
   }

   private void updateMovieGrid(){
      FetchMovieListTask movieListTask = new FetchMovieListTask();
      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
      String sortOrder = prefs.getString(getString(R.string.pref_popular_key),
              getString(R.string.pref_popular_default));
      movieListTask.execute(sortOrder);
   }

   private class FetchMovieListTask extends AsyncTask<String, Void, ArrayList<ImageObject>> {

      private final String LOG_TAG = FetchMovieListTask.class.getSimpleName();
      private final String API_KEY_STRING = "api_key";
      private final String API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;
      private final String HTTP = "https";
      private final String URL = "api.themoviedb.org";
      private final String DATA ="3";
      private final String MOVIE = "movie";
      private final String SORT = "sort_by";



      @Override
      protected ArrayList<ImageObject> doInBackground(String... params) {



         // If there's no zip code, there's nothing to look up.  Verify size of params.
         if (params.length == 0) {
            return null;
         }

         // These two need to be declared outside the try/catch
         // so that they can be closed in the finally block.
         HttpURLConnection urlConnection = null;
         BufferedReader reader = null;

         // Will contain the raw JSON response as a string.
         String MovieJsonStr = null;

         int numDays = 7;

         try {


            Uri.Builder uri = new Uri.Builder();
            uri.scheme(HTTP)
                    .authority(URL)
                    .appendPath(DATA)
                    .appendPath(MOVIE)
                    .appendPath(params[0])
                    .appendQueryParameter(API_KEY_STRING,API_KEY);
            String baseURL = uri.build().toString();

            URL url = new URL(baseURL);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
               // Nothing to do.
               return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
               // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
               // But it does make debugging a *lot* easier if you print out the completed
               // buffer for debugging.
               buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
               // Stream was empty.  No point in parsing.
               return null;
            }
            MovieJsonStr = buffer.toString();
         } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
         } finally {
            if (urlConnection != null) {
               urlConnection.disconnect();
            }
            if (reader != null) {
               try {
                  reader.close();
               } catch (final IOException e) {
                  Log.e(LOG_TAG, "Error closing stream", e);
               }
            }
         }

         try {
            return parseMovieJson(MovieJsonStr);
         } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
         }

         // This will only happen if there was an error getting or parsing the forecast.
         return null;
      }
      private ArrayList<ImageObject> parseMovieJson(String movieDBJsonStr) throws JSONException {

         final Gson gson = new Gson();

         Results serverResult = gson.fromJson(movieDBJsonStr,Results.class);
         return serverResult.getmImgList();

       /*  final String RESULTS = "results";
         final String POSTER_PATH = "poster_path";
         final String OVERVIEW = "overview";
         final String RELEASE_DATE = "release_date";
         final String VOTE_AVERAGE = "vote_average";
         final String ORIGINAL_TITLE ="original_title";

         JSONObject json = new JSONObject(movieDBJsonStr);
         JSONArray listArray = json.getJSONArray(RESULTS);
         ArrayList<ImageObject> movieArray = new ArrayList<>();
         for(int i=0;i<listArray.length();i++){
            JSONObject obj  = listArray.getJSONObject(i);
            ImageObject imageObject = new ImageObject();

          *//*  imageObject.setPoster_path(obj.getString(POSTER_PATH));
            imageObject.setOriginal_title(obj.getString(ORIGINAL_TITLE));
            imageObject.setOverview(obj.getString(OVERVIEW));
            imageObject.setRelease_date(obj.getString(RELEASE_DATE));
            imageObject.setVote_average(obj.getString(VOTE_AVERAGE));
*//*
            movieArray.add(imageObject);

         }
         return movieArray;*/
      }


      @Override
      protected void onPostExecute(ArrayList<ImageObject> imageObjects) {
         mListImgObj.clear();

         if(imageObjects!=null){

            for(ImageObject a : imageObjects){
               mListImgObj.add(a);
            }
            imageAdapter.notifyDataSetChanged();
         }
      }
   }
}
