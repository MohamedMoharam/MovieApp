package com.example.moharam.movie;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
public class DetailsActivityFragment extends Fragment {

    MovieObject object = null;
    ListView lstTrailers, lstReviews;
    ArrayAdapter feachTrailers;
    ImageButton imageButton;

    public DetailsActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_details, container, false);

        imageButton = (ImageButton) root.findViewById(R.id.btnMarkasFevDetails);

        if (getActivity().getIntent().hasExtra(Intent.EXTRA_TEXT)) {
            object = (MovieObject) getActivity().getIntent().getSerializableExtra(Intent.EXTRA_TEXT);
        } else if (getArguments() != null && getArguments().containsKey(Intent.EXTRA_TEXT)) {
            object = (MovieObject) getArguments().getSerializable(Intent.EXTRA_TEXT);
        }

        if (object != null) {
            ((TextView) root.findViewById(R.id.titleDetails)).setText(object.getOriginal_title());
            ((TextView) root.findViewById(R.id.overviewDetails)).setText(object.getOverview());
            ((TextView) root.findViewById(R.id.rateDetails)).setText(object.getVote_average() + "/10");
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + object.getMovie_image()).into((ImageView) root.findViewById(R.id.imgDetails));



            if (new MovieDB(getActivity()).founded(object.getFilm_id())) {
                imageButton.setImageResource(R.drawable.on);
            } else {
                imageButton.setImageResource(R.drawable.off);

            }
            lstTrailers = (ListView) root.findViewById(R.id.listTrailers);
            lstTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + ((TextView) root.findViewById(R.id.txtTrailar)).getText())));
                }
            });
            lstTrailers.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                    v.onTouchEvent(event);
                    return true;
                }
            });
            lstReviews = (ListView) root.findViewById(R.id.listReviews);
            lstReviews.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                    v.onTouchEvent(event);
                    return true;
                }
            });
            if (object != null) {
                FeachTrailors feach = new FeachTrailors();
                feach.execute();
                FetchReviews fetchReviews = new FetchReviews();
                fetchReviews.execute();
            }

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MovieDB db = new MovieDB(getActivity());
                    if (db.founded(object.getFilm_id())) {
                        db.deleteMovie(object.getFilm_id());
                        imageButton.setImageResource(R.drawable.off);
                    } else {
                        db.insertMovie(object);
                        imageButton.setImageResource(R.drawable.on);
                    }
                }

            });
        }
        return root;

    }


    public class FeachTrailors extends AsyncTask<Void, Void, List<String>> {
        private final String LOG_TAG = FeachTrailors.class.getSimpleName();
        final String Movie_BASE_URL = "http://api.themoviedb.org/3/movie/" + object.getFilm_id() + "/videos?api_key=2a548fe42285a52850d564e96bfbb2ef";

        List<String> getMoviesDataFromJson(String movieJsonStr) throws JSONException {

            final String LIST = "results";
            final String KEY = "key";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesArray = movieJson.getJSONArray(LIST);

            List<String> Trailers = new ArrayList<String>();
            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject trailers = moviesArray.getJSONObject(i);
                Trailers.add(trailers.getString(KEY));
            }
            return Trailers;
        }


        @Override
        protected List<String> doInBackground(Void... params) {
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
        protected void onPostExecute(List<String> strings) {
            if (strings != null) {
//            {
//                ImageAdapter adapter = new ImageAdapter(strings,getActivity());
//                gridView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();{          if()
                feachTrailers = new ArrayAdapter(getActivity(), R.layout.trailarshow, R.id.txtTrailar, strings);
                lstTrailers.setAdapter(feachTrailers);

            }
        }
    }
    //http://api.themoviedb.org/3/movie/film id/reviews?api_key=2a548fe42285a52850d564e96bfbb2ef
    //https://www.youtube.com/watch?v=
    //http://api.themoviedb.org/3/movie/film id/videos?api_key=2a548fe42285a52850d564e96bfbb2ef

    public class FetchReviews extends AsyncTask<Void, Void, List<ReviewObject>> {
        private final String LOG_TAG = FeachTrailors.class.getSimpleName();
        final String Movie_BASE_URL = "http://api.themoviedb.org/3/movie/" + object.getFilm_id() + "/reviews?api_key=2a548fe42285a52850d564e96bfbb2ef";


        List<ReviewObject> getMoviesDataFromJson(String movieJsonStr) throws JSONException {

            final String LIST = "results";
            final String AUTHOR = "author";
            final String CONTENT = "content";

            List<ReviewObject> lst = new ArrayList<ReviewObject>();
            ReviewObject review;
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesArray = movieJson.getJSONArray(LIST);

            for (int i = 0; i < moviesArray.length(); i++) {
                review = new ReviewObject();
                JSONObject trailers = moviesArray.getJSONObject(i);
                review.auther = trailers.getString(AUTHOR);
                review.review = trailers.getString(CONTENT);
                lst.add(review);
            }
            return lst;
        }


        @Override
        protected List<ReviewObject> doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                final String APPID = "api_key";

                Uri uribuilder = Uri.parse(Movie_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID, BuildConfig.MOVIE_API_KEY)
                        .build();

                URL url = new URL(uribuilder.toString());

                Log.v(LOG_TAG, "Build Uri" + uribuilder.toString());

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
        protected void onPostExecute(List<ReviewObject> strings) {
            if (strings != null) {
                ReviewAdapter adapter = new ReviewAdapter(getActivity(), strings);
                lstReviews.setAdapter(adapter);
            }

        }
    }
    //http://api.themoviedb.org/3/movie/film id/reviews?api_key=2a548fe42285a52850d564e96bfbb2ef
    //https://www.youtube.com/watch?v=
    //http://api.themoviedb.org/3/movie/film id/videos?api_key=2a548fe42285a52850d564e96bfbb2ef

}
