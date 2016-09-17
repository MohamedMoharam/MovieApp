package com.example.moharam.movie;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Moharam on 13-Aug-16.
 */
public class ImageAdapter extends BaseAdapter {

    List<MovieObject> list;
    Context context;
    ImageView image;
    public ImageAdapter(List<MovieObject> list, Context context)
    {
        this.list = list;
        this.context=context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view=convertView;
        if(convertView ==null)
        {
            final LayoutInflater inflater = ((Activity) context).getLayoutInflater();
             view = inflater.inflate(R.layout.photoshow, parent, false);
        }
        Picasso.with(context).load("http://image.tmdb.org/t/p/w342/"+list.get(position).getMovie_image()).into((ImageView) view.findViewById(R.id.showing));
        return view;

    }

}

//img.youtube.com/v1/"  +  film_id   + "/0.jpg"
//realm DB