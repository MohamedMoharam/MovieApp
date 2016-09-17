package com.example.moharam.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.GridView;

public class MainActivity extends ActionBarActivity implements SetData {

    private GridView gridView;
    private ImageAdapter gridAdapter;
boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flDetails);
        if(fragment !=null) {
            if (id == R.id.action_most_popular||id == R.id.action_top_rated||id == R.id.action_favorite)
            {
          getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        return super.onOptionsItemSelected(item);
    }
        return true;
    }
    @Override
    public void sendObj(MovieObject movie) {
        if (findViewById(R.id.flDetails) != null) {
            DetailsActivityFragment newFragment = new DetailsActivityFragment();
            Bundle args = new Bundle();
            args.putSerializable(Intent.EXTRA_TEXT, movie);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FragmentManager manager = getSupportFragmentManager();
            transaction.replace(R.id.flDetails,newFragment);
            transaction.commit();
        }
        else
        {
            Intent i = new Intent(this, DetailsActivity.class);
            i.putExtra(Intent.EXTRA_TEXT, movie);
            startActivity(i);
        }
    }


}

