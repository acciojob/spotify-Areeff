package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user=new User(name,mobile);
        users.add(user);
        return user;
    }

    public Artist createArtist(String name) {
        Artist artist=new Artist(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
        Album album=new Album(title);
        albums.add(album);
        List<Album> albumsList=artistAlbumMap.getOrDefault(artistName,new ArrayList<>());
        albumsList.add(album);
        Optional<Artist>artist=findArtist(artistName);
        artistAlbumMap.put(artist.get(),albumsList);
        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Song song=new Song(title,length);
        songs.add(song);
        Optional<Album>albumOptional=findAlbum(albumName);
        List<Song> songList=albumSongMap.getOrDefault(albumOptional.get(),new ArrayList<>());
        songList.add(song);
        albumSongMap.put(albumOptional.get(),songList);
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {

    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {

    }

    public Song likeSong(String mobile, String songTitle) throws Exception {

    }

    public String mostPopularArtist() {
    }

    public String mostPopularSong() {
    }

    public Optional<Artist> findArtist(String artistName) {
        for(Artist artist:artists){
            if(artist.getName().equals(artistName)){
                return Optional.of(artist);
            }
        }
        return Optional.empty();
    }

    public Optional<Album> findAlbum(String albumName) {
        for(Album album:albums){
            if(album.getTitle().equals(albumName)){
                Optional.of(album);
            }
        }
        Optional.empty();
    }



    public Playlist CreatePlayList(String title,int length,String mobile) {
        List<Song> songList=new ArrayList<>();
        for(Song song:songs){
            if(song.getLength()==length){
                songList.add(song);
            }
        }
        Playlist playlist=new Playlist(title);
        playlists.add(playlist);
        playlistSongMap.put(playlist,songList);
        Optional<User> userOptional=findUser(mobile);
        List<User> userList=playlistListenerMap.getOrDefault(playlist,new ArrayList<>());
        userList.add(userOptional.get());
        playlistListenerMap.put(playlist,userList);
        return playlist;
    }

    public Optional<User> findUser(String mobile) {
        for(User user:users){
            if(user.getMobile().equals(mobile)){
                return Optional.of(user);
            }
        }
        Optional.empty();
    }
}
