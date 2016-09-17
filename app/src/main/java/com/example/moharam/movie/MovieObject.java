package com.example.moharam.movie;

import java.io.Serializable;

/**
 * Created by Moharam on 13-Aug-16.
 */
public class MovieObject implements Serializable{

   private String film_id;

   public void setFilm_id(String film_id) {
      this.film_id = film_id;
   }

   public String getFilm_id() {
      return film_id;
   }

   private String original_title;

   public void setMovie_image(String movie_image) {
      this.movie_image = movie_image;
   }

   public String getMovie_image() {
      return movie_image;
   }

   private String movie_image;

   public void setOriginal_title(String original_title) {
      this.original_title = original_title;
   }

   public String getOriginal_title() {
      return original_title;
   }

   private String overview;

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
   private String vote_average;
   private String release_date;


    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
