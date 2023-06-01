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
        List<Album> albumsList =new ArrayList<>();
        Optional<Artist> artist=findArtist(artistName);
        Artist artist1 = null;
        if(artist.isPresent()) artist1 = artist.get();
        if(artistAlbumMap.containsKey(artist1)){
           albumsList=artistAlbumMap.get(artist1);
        }
       // List<Album> albumsList=new ArrayList<>();
        albumsList.add(album);

        artistAlbumMap.put(artist1,albumsList);
        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Song song=new Song(title,length);
        songs.add(song);
        Optional<Album>albumOptional=findAlbum(albumName);
        Album album=null;
        if(albumOptional.isPresent()){
            album=albumOptional.get();
        }
        List<Song> songList=albumSongMap.getOrDefault(album,new ArrayList<>());
        songList.add(song);
        albumSongMap.put(album,songList);
        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
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
        User user=null;
        if(userOptional.isPresent()){
            user=userOptional.get();
        }
        List<User> userList=playlistListenerMap.getOrDefault(playlist,new ArrayList<>());
        userList.add(user);
        playlistListenerMap.put(playlist,userList);
        creatorPlaylistMap.put(user,playlist);
        List<Playlist> playlistList=userPlaylistMap.getOrDefault(user,new ArrayList<>());
        playlistList.add(playlist);
        userPlaylistMap.put(user,playlistList);
        return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
            Playlist playlist=new Playlist(title);
            playlists.add(playlist);
            List<Song>songList=new ArrayList<>();
            for(String Songtitle:songTitles){
                for (Song song:songs){
                    if(Songtitle.equals(song.getTitle())){
                        songList.add(song);
                    }
                }
            }
            playlistSongMap.put(playlist,songList);
           Optional<User> userOptional=findUser(mobile);
           User user=null;
           if(userOptional.isPresent()){
               user=userOptional.get();
           }
        List<User> userList=playlistListenerMap.getOrDefault(playlist,new ArrayList<>());
        userList.add(user);
        playlistListenerMap.put(playlist,userList);
            creatorPlaylistMap.put(user,playlist);
           List<Playlist> playlistList=userPlaylistMap.getOrDefault(user,new ArrayList<>());
           playlistList.add(playlist);
            userPlaylistMap.put(user,playlistList);
            return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
//        for(Playlist playlist:playlists){
//            if(playlist.getTitle().equals(playlistTitle)){
//                return playlist;
//            }
//        }
//        return null;
        User user = null;
        for(User user1:users){
            if(user1.getMobile()==mobile){
                user=user1;
                break;
            }
        }
        if(user==null)
            throw new Exception("User does not exist");

        Playlist playlist = null;
        for(Playlist playlist1:playlists){
            if(playlist1.getTitle()==playlistTitle){
                playlist=playlist1;
                break;
            }
        }
        if(playlist==null)
            throw new Exception("Playlist does not exist");

        if(creatorPlaylistMap.containsKey(user))
            return playlist;

        List<User> listener = playlistListenerMap.get(playlist);
        for(User user1:listener){
            if(user1==user)
                return playlist;
        }

        listener.add(user);
        playlistListenerMap.put(playlist,listener);

        List<Playlist> playlists1 = userPlaylistMap.get(user);
        if(playlists1 == null){
            playlists1 = new ArrayList<>();
        }
        playlists1.add(playlist);
        userPlaylistMap.put(user,playlists1);

        return playlist;

    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
//         Optional<User>userOptional=findUser(mobile);
//         User user=null;
//         if(userOptional.isPresent()){
//             user=userOptional.get();
//         }
//         Optional<Song>songOptional=findSong(songTitle);
//         Song song=null;
//         if(songOptional.isPresent()) song=songOptional.get();
//
////        public HashMap<Artist, List<Album>> artistAlbumMap;
////        public HashMap<Album, List<Song>> albumSongMap;
//        if(songLikeMap.containsKey(song)){
//            List<User> userList=songLikeMap.get(song);
//            if(userList.contains(user)){
//                return song;
//            }
//            else{
//                int likes=song.getLikes()+1;
//                song.setLikes(likes);
//                userList.add(user);
//                songLikeMap.put(song,userList);
//                Album album=null;
//                for(Map.Entry<Album,List<Song>> map:albumSongMap.entrySet()){
//                    if(map.getValue().contains(song)){
//                        album=map.getKey();
//                        break;
//                    }
//                }
//                Artist artist=null;
//                for(Map.Entry<Artist,List<Album>> Hashmap:artistAlbumMap.entrySet()){
//                    if(Hashmap.getValue().contains(album)){
//                        artist=Hashmap.getKey();
//                        break;
//                    }
//                }
//                artist.setLikes(artist.getLikes()+1);
//                artists.add(artist);
//                return song;
//            }
//
//        }else {
//            Album album = null;
//            for (Map.Entry<Album, List<Song>> map : albumSongMap.entrySet()) {
//                if (map.getValue().contains(song)) {
//                    album = map.getKey();
//                    break;
//                }
//            }
//            Artist artist = null;
//            for (Map.Entry<Artist, List<Album>> Hashmap : artistAlbumMap.entrySet()) {
//                if (Hashmap.getValue().contains(album)) {
//                    artist = Hashmap.getKey();
//                    break;
//                }
//            }
//            //songs.remove(song);
//            song.setLikes(song.getLikes() + 1);
//            songs.add(song);
//            //artists.remove(artist);
//            artist.setLikes(artist.getLikes() + 1);
//            artists.add(artist);
//            List<User> userList = songLikeMap.getOrDefault(song, new ArrayList<>());
//            userList.add(user);
//            songLikeMap.put(song, userList);
//            return song;
        User user = null;
        for(User user1:users){
            if(user1.getMobile()==mobile){
                user=user1;
                break;
            }
        }
        if(user==null)
            throw new Exception("User does not exist");

        Song song = null;
        for(Song song1:songs){
            if(song1.getTitle()==songTitle){
                song=song1;
                break;
            }
        }
        if (song==null)
            throw new Exception("Song does not exist");

        if(songLikeMap.containsKey(song)){
            List<User> list = songLikeMap.get(song);
            if(list.contains(user)){
                return song;
            }else {
                int likes = song.getLikes() + 1;
                song.setLikes(likes);
                list.add(user);
                songLikeMap.put(song,list);

                Album album=null;
                for(Album album1:albumSongMap.keySet()){
                    List<Song> songList = albumSongMap.get(album1);
                    if(songList.contains(song)){
                        album = album1;
                        break;
                    }
                }
                Artist artist = null;
                for(Artist artist1:artistAlbumMap.keySet()){
                    List<Album> albumList = artistAlbumMap.get(artist1);
                    if (albumList.contains(album)){
                        artist = artist1;
                        break;
                    }
                }
                int likes1 = artist.getLikes() +1;
                artist.setLikes(likes1);
                artists.add(artist);
                return song;
            }
        }else {
            int likes = song.getLikes() + 1;
            song.setLikes(likes);
            List<User> list = new ArrayList<>();
            list.add(user);
            songLikeMap.put(song,list);

            Album album=null;
            for(Album album1:albumSongMap.keySet()){
                List<Song> songList = albumSongMap.get(album1);
                if(songList.contains(song)){
                    album = album1;
                    break;
                }
            }
            Artist artist = null;
            for(Artist artist1:artistAlbumMap.keySet()){
                List<Album> albumList = artistAlbumMap.get(artist1);
                if (albumList.contains(album)){
                    artist = artist1;
                    break;
                }
            }
            int likes1 = artist.getLikes() +1;
            artist.setLikes(likes1);
            artists.add(artist);

            return song;
        }
    }

    public String mostPopularArtist() {
        int mostLikes=0;
        String name=null;
        for(Artist artist:artists){
            if(artist.getLikes()>mostLikes){
                mostLikes=artist.getLikes();
                name=artist.getName();
            }
        }
        return name;
    }

    public String mostPopularSong() {
        String name=null;
        int likes=0;
        //public HashMap<Song, List<User>> songLikeMap;
        for(Song song:songLikeMap.keySet()){
            if(song.getLikes()>likes){
                likes=song.getLikes();
                name=song.getTitle();
            }
        }
        return name;
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
                return Optional.of(album);
            }
        }
       return Optional.empty();
    }



//    public Playlist CreatePlayList(String title,int length,String mobile) {
//        List<Song> songList=new ArrayList<>();
//        for(Song song:songs){
//            if(song.getLength()==length){
//                songList.add(song);
//            }
//        }
//        Playlist playlist=new Playlist(title);
//        playlists.add(playlist);
//        playlistSongMap.put(playlist,songList);
//        Optional<User> userOptional=findUser(mobile);
//        List<User> userList=playlistListenerMap.getOrDefault(playlist,new ArrayList<>());
//        userList.add(userOptional.get());
//        playlistListenerMap.put(playlist,userList);
//        return playlist;
//    }

    public Optional<User> findUser(String mobile) {
        for(User user:users){
            if(user.getMobile().equals(mobile)){
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public void addAsListener(String mobile, String playlistTitle)  {
        Optional<User> userOptional=findUser(mobile);
        User user=null;
        if(userOptional.isPresent()){
            user=userOptional.get();
        }

        Playlist playlist = findingPlaylist(mobile,playlistTitle);
//        public HashMap<Playlist, List<User>> playlistListenerMap;
//        public HashMap<User, Playlist> creatorPlaylistMap;
//        public HashMap<User, List<Playlist>> userPlaylistMap;
        if(creatorPlaylistMap.containsKey(user)) return;
        creatorPlaylistMap.put(user,playlist);
        List<User> userList=playlistListenerMap.getOrDefault(playlist,new ArrayList<>());
        if(userList.contains(user)) return;
            userList.add(user);
            playlistListenerMap.put(playlist, userList);
        List<Playlist> playlistList=userPlaylistMap.getOrDefault(user,new ArrayList<>());
        if(playlistList.contains(playlist)) return;
            playlistList.add(playlist);
            userPlaylistMap.put(user, playlistList);
    }

    public Optional<Song> findSong(String songTitle) {
      for(Song song:songs){
          if(song.equals(songTitle)){
              return Optional.of(song);
          }
      }
      return  Optional.empty();
    }
    public Playlist findingPlaylist(String mobile, String playlistTitle) {
        for(Playlist playlist:playlists){
            if(playlist.getTitle().equals(playlistTitle)){
                return playlist;
            }
        }
        return null;

    }

}
