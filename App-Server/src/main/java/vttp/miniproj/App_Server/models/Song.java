package vttp.miniproj.App_Server.models;

import java.util.List;

public class Song {
    
    private String songId;
    private String imageUrl;
    private String songTitle;
    private List<String> artistName;
    private long songDuration;

    public String getSongId() {
        return songId;
    }
    public void setSongId(String songId) {
        this.songId = songId;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getSongTitle() {
        return songTitle;
    }
    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }
    public List<String> getArtistName() {
        return artistName;
    }
    public void setArtistName(List<String> artistName) {
        this.artistName = artistName;
    }
    public long getSongDuration() {
        return songDuration;
    }
    public void setSongDuration(long songDuration) {
        this.songDuration = songDuration;
    }

    @Override
    public String toString() {
        return "Song [songId=" + songId + ", imageUrl=" + imageUrl + ", songTitle=" + songTitle + ", artistName="
                + artistName + ", songDuration=" + songDuration + "]";
    }

    
    
}
