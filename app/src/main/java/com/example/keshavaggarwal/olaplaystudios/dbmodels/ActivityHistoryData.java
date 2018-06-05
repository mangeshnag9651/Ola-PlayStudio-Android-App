package com.example.keshavaggarwal.olaplaystudios.dbmodels;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by KeshavAggarwal on 18/12/17.
 */
@Table(name = "activity_history_data")
public class ActivityHistoryData extends Model {

    @Column(name = "name")
    private String name;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "artists")
    private String artists;

    @Column(name = "url")
    private String url;

    @Column(name = "isDownloaded")
    private int isDownloaded;

    public ActivityHistoryData(String name, String coverImage, String artists, String url, int isDownloaded) {
        this.name = name;
        this.coverImage = coverImage;
        this.artists = artists;
        this.url = url;
        this.isDownloaded = isDownloaded;
    }

    public ActivityHistoryData(String name, String coverImage, String artists, String url) {
        this.name = name;
        this.coverImage = coverImage;
        this.artists = artists;
        this.url = url;
    }

    public ActivityHistoryData(){
        super();
    }

    public static List<ActivityHistoryData> getAllHistoryData(){
        return new Select().from(ActivityHistoryData.class).execute();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIsDownloaded() {
        return isDownloaded;
    }

    public void setIsDownloaded(int isDownloaded) {
        this.isDownloaded = isDownloaded;
    }
}
