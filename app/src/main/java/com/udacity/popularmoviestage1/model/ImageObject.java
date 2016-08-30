package com.udacity.popularmoviestage1.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rj125e on 8/25/2016.
 */
public class ImageObject implements Parcelable{



    private String overview;
    private String original_title;
    private String poster_path;
    private String vote_average;
    private String release_date;

    public ImageObject(Parcel source) {

        overview = source.readString();
        original_title = source.readString();
        poster_path = source.readString();
        vote_average = source.readString();
        release_date = source.readString();
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(overview);
        dest.writeString(original_title);
        dest.writeString(poster_path);
        dest.writeString(vote_average);
        dest.writeString(release_date);
    }

    public static final Parcelable.Creator<ImageObject> CREATOR
            = new Parcelable.Creator<ImageObject>() {

        public ImageObject createFromParcel(Parcel in) {
            return new ImageObject(in);
        }

        public ImageObject[] newArray(int size) {
            return new ImageObject[size];
        }
    };
}
