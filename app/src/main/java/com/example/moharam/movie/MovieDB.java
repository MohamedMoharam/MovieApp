package com.example.moharam.movie;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moharam on 13-Sep-16.
 */
public class MovieDB extends SQLiteOpenHelper {

    SQLiteDatabase dbWrite = getWritableDatabase();
    SQLiteDatabase dbRead = getReadableDatabase();
    public  MovieDB(Context context) {
        super(context,"moviesdb",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE MOVIE(FilmID VARCHAR(150) PRIMARY KEY,OriginalTitle VARCHAR(150),MovieImage VARCHAR(150),Overview VARCHAR(150),VoteAverage CHAR(150),ReleaseDate VARCHAR(150))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS MOVIE");
        onCreate(db);
    }

    public boolean insertMovie(MovieObject object)
    {
            if(!founded(object.getFilm_id())) {
                dbWrite.execSQL("insert into movie values(\"" + object.getFilm_id() + "\",\"" + object.getOriginal_title() + "\",\"" + object.getMovie_image() + "\",\"" + object.getOverview() + "\",\"" + object.getVote_average() + "\",\"" + object.getRelease_date() + "\")");
                return true;
            }
        else
                return false;

    }

    public boolean founded (String id)
    {
        Cursor cursor = dbRead.rawQuery( "SELECT * FROM MOVIE WHERE FilmID =?", new String[]{id});
        if(cursor.getCount() >0)
            return true;
        else
            return false;
    }

    public List<MovieObject> getAllMovies() {
        List<MovieObject> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM MOVIE",null);
        if (res.moveToFirst()) {
            do {
                MovieObject object = new MovieObject();
                object.setFilm_id(res.getString(0));
                object.setOriginal_title(res.getString(1));
                object.setMovie_image(res.getString(2));
                object.setOverview(res.getString(3));
                object.setVote_average(res.getString(4));
                object.setRelease_date(res.getString(5));
                list.add(object);
            } while (res.moveToNext());
        }
        return list;
    }

    public boolean deleteMovie(String id)
    {
        if(founded(id)){
            dbWrite.delete("MOVIE","FilmID =?",new String[]{id});
            return true;
        }
        else
            return false;
    }
}
