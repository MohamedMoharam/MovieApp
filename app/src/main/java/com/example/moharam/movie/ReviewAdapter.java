package com.example.moharam.movie;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Moharam on 27-Aug-16.
 */
public class ReviewAdapter extends BaseAdapter {

    List<ReviewObject>list;
    Context context;
    public ReviewAdapter(Context context, List<ReviewObject> list)
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
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if(convertView ==null)
        {
            final LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.review_layout, parent, false);
        }
        ((TextView)view.findViewById(R.id.txtName)).setText(list.get(position).auther);
        ((TextView)view.findViewById(R.id.content)).setText(list.get(position).review);
        return view;
    }
}
