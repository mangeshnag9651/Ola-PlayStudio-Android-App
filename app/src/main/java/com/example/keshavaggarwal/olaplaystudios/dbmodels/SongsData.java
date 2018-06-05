package com.example.keshavaggarwal.olaplaystudios.dbmodels;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by KeshavAggarwal on 17/12/17.
 */
@Table(name = "songs_data")
public class SongsData extends Model {

    @Column(name = "name")
    private String name;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "artists")
    private String artists;

    @Column(name = "favorites")
    private int favorites;

    @Column(name = "url")
    private String url;

    @Column(name = "isDownloaded")
    private int isDownloaded;



    public SongsData(String name, String coverImage, String artists, int favorites, String url, int isDownloaded) {
        this.name = name;
        this.coverImage = coverImage;
        this.artists = artists;
        this.favorites = favorites;
        this.url = url;
        this.isDownloaded = isDownloaded;
    }

    public SongsData() {
        super();
    }

    public static List<SongsData> getAllSongs() {
        return new Select()
                .from(SongsData.class)
                .execute();
    }

    public static void updateDownloadStatus(String name){
        SongsData obj = new Select().from(SongsData.class).where("name = ?", name).executeSingle();
        obj.setIsDownloaded(1);
        obj.save();
    }

    public static void updateFavorites(String name){

        SongsData obj =  new Select().from(SongsData.class).where("name = ?", name).executeSingle();
        obj.setFavorites(1);
        obj.save();
    }

    public static SongsData getParticulerSongFavorite(String name){
        return new Select().from(SongsData.class).where("name = ?", name).executeSingle();
    }

    public static SongsData getParticulerDownload(String name){
        return new Select().from(SongsData.class).where("name = ?", name).executeSingle();
    }
    public static List<SongsData> getFavoriteSongs(){
        return new Select().from(SongsData.class).where("favorites = ?", 1).execute();
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

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
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

    public static void updateFavoritesDec(String name) {
        SongsData obj =  new Select().from(SongsData.class).where("name = ?", name).executeSingle();
        obj.setFavorites(0);
        obj.save();
    }
}
