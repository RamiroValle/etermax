package com.ramirovalle.etermax;

public class PlaylistNotFoundException extends RuntimeException{

    public PlaylistNotFoundException(String playlistId) {
        super("No se encontro la playlist " + playlistId);
    }
}
