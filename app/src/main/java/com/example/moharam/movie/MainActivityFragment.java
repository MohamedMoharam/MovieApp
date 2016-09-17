package com.example.moharam.movie;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements UpdateFavorites {
    private GridView gridView;
    List<MovieObject> movies;
    boolean favorite = false;
    ImageAdapter adapter;
    int mPos = -1;

    public MainActivityFragment() {
    }

    private void UpdateTask() {

        FetchMovieTask task = new FetchMovieTask();
        task.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) view.findViewById(R.id.grdShow);
        setHasOptionsMenu(true);
        CheckInternet ci = new CheckInternet(getActivity());
        if (ci.isOnline()) {
            UpdateTask();
        } else {
            Toast.makeText(getActivity(), "Check your internet connection !", Toast.LENGTH_LONG).show();
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mPos = position;
                ((SetData) getActivity()).sendObj(movies.get(position));
            }
        });
//        DetailsActivityFragment.updateFavorites=;
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(favorite)
            updateFavorite();
    }

    private void updateFavorite() {
        movies = new MovieDB(getActivity()).getAllMovies();
        adapter = new ImageAdapter(movies, getActivity());
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_most_popular) {
            Movie_BASE_URL = "http://api.themoviedb.org/3/movie/popular?";
            UpdateTask();
            favorite = false;
            return true;
        } else if (id == R.id.action_top_rated) {
            Movie_BASE_URL = "http://api.themoviedb.org/3/movie/top_rated?";
            UpdateTask();
            favorite = false;
            return true;
        } else if (id == R.id.action_favorite) {
            favorite = true;
            updateFavorite();
        }


        return super.onOptionsItemSelected(item);
    }



    String Movie_BASE_URL = "http://api.themoviedb.org/3/movie/popular?";

    @Override
    public void updateMoviesFragment() {
        if(favorite)
        {
            updateFavorite();
        }
    }


    public class FetchMovieTask extends AsyncTask<Void, Void, List<MovieObject>> {
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        List<MovieObject> getMoviesDataFromJson(String movieJsonStr) throws JSONException {

            final String LIST = "results";
            final String ORIGINAL_TITLE = "original_title";
            final String MOVIE_POSTER = "poster_path";
            final String OVERVIEW = "overview";
            final String RATING = "vote_average";
            final String RELEASE_DATE = "release_date";
            final String FILM_ID = "id";
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesArray = movieJson.getJSONArray(LIST);

            MovieObject mov;
            List<MovieObject> moviesList = new ArrayList<MovieObject>();
            for (int i = 0; i < moviesArray.length(); i++) {
                mov = new MovieObject();
                JSONObject movie = moviesArray.getJSONObject(i);
                mov.setMovie_image(movie.getString(MOVIE_POSTER));
                mov.setOriginal_title(movie.getString(ORIGINAL_TITLE));
                mov.setOverview(movie.getString(OVERVIEW));
                mov.setRelease_date(movie.getString(RELEASE_DATE));
                mov.setVote_average(movie.getString(RATING));
                mov.setFilm_id(movie.getString(FILM_ID));
                moviesList.add(mov);
            }
            return moviesList;
        }

        @Override
        protected List<MovieObject> doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                final String APPID = "api_key";
//                https://api.themoviedb.org/3/movie/550?api_key=2a548fe42285a52850d564e96bfbb2ef

                Uri uribuilder = Uri.parse(Movie_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID, BuildConfig.MOVIE_API_KEY)
                        .build();

                //String s = String.format("%1%/%2%/%3%/api_key=%4%",Movie_BASE_URL,Movie_POPULAR,"?",APPID,BuildConfig.MOVIE_API_KEY);

                URL url = new URL(uribuilder.toString());
                //URL url = new URL(s);
                Log.v(LOG_TAG, "Build Uri" + uribuilder.toString());
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

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
            } catch (Exception e) {
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
                    } catch (final Exception e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMoviesDataFromJson(movieJsonStr);

            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(List<MovieObject> strings) {
            if (strings != null) {
                movies = strings;
                ImageAdapter adapter = new ImageAdapter(strings, getActivity());
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }
}



