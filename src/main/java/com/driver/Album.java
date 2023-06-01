package com.driver;

import java.util.Date;
import java.util.List;

public class Album {
    private String title;
    private String artistName;
    private Date releaseDate;

    public Album(){

    }



    public Album(String title, String artistName) {
        this.title = title;
        this.artistName = artistName;
        this.releaseDate=new Date();
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Album(String title){
        this.title = title;
        this.releaseDate = new Date();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}
