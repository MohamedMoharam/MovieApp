package com.example.moharam.movie;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Moharam on 16-Sep-16.
 */
public class CheckInternet {
    Context context;

    public CheckInternet(Context context)
    {
        this.context=context;
    }

    public  boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm!=null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if(info!=null&&info.getState()== NetworkInfo.State.CONNECTED)
                return true;
        }
        return false;
    }
}
