package com.driver;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class SpotifyService {

    //Auto-wire will not work in this case, no need to change this and add autowire

    SpotifyRepository spotifyRepository = new SpotifyRepository();

    public User createUser(String name, String mobile){
        return spotifyRepository.createUser(name,mobile);
    }

    public Artist createArtist(String name) {
         return spotifyRepository.createArtist(name);
    }

    public Album createAlbum(String title, String artistName) {
        //If the artist does not exist, first create an artist with given name
        //Create an album with given title and artist
        Optional<Artist> artist=spotifyRepository.findArtist(artistName);
        if(artist.isEmpty()){
            Artist artist1=spotifyRepository.createArtist(artistName);
        }
        return spotifyRepository.createAlbum(title,artistName);

    }

    public Song createSong(String title, String albumName, int length) throws Exception {
        return spotifyRepository.createSong(title,albumName,length);
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        //Create a playlist with given title and add all songs having the given length in the database to that playlist
        //The creater of the playlist will be the given user and will also be the only listener at the time of playlist creation
        //If the user does not exist, throw "User does not exist" exception
       Playlist playlist= spotifyRepository.createPlaylistOnLength(mobile,title,length);
       return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
          return spotifyRepository.createPlaylistOnName(mobile,title,songTitles);
    }

    public Optional<Playlist>findPlaylist(String mobile, String playlistTitle) throws Exception {
           return  spotifyRepository.findPlaylist(mobile,playlistTitle);
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
          return spotifyRepository.likeSong(mobile,songTitle);
    }

    public String mostPopularArtist() {
       return spotifyRepository.mostPopularArtist();
    }

    public String mostPopularSong() {
       return spotifyRepository.mostPopularSong();
    }

    public boolean findAlbum(String albumName) {
        Optional<Album> album= spotifyRepository.findAlbum(albumName);
        if(album.isEmpty()){
            return false;
        }
        return true;
    }

    public Optional<User> findUser(String mobile) {
     return spotifyRepository.findUser(mobile);
    }

    public void addaslistener(String mobile, String playlistTitle) throws Exception {
        spotifyRepository.addAsListener(mobile,playlistTitle);
        return;
    }

    public Optional<Song> findSong(String songTitle) {
        return spotifyRepository.findSong(songTitle);
    }
}
