package com.ramirovalle.etermax;

import java.util.HashMap;

public class DatabaseMock {

    /**
     * Mock de una base de datos que guarda jsons
     */
    private final HashMap<String, String> playlistStore = new HashMap<>();
    private static final Object lock = new Object();

    public void storePlaylist(String playlistId, String playlist) {
        synchronized (lock) {
            playlistStore.put(playlistId, playlist);
        }
    }

    public boolean updatePlaylist(String playlistId, String playlist) {
        synchronized (lock) {
            return playlistStore.replace(playlistId, playlist) != null;
        }
    }

    public String getPlaylist(String playlistId) {
        synchronized (lock) {
            return playlistStore.get(playlistId);
        }
    }

    public boolean deletePlaylist(String playlistId) {
        synchronized (lock) {
            return playlistStore.remove(playlistId) != null;
        }
    }
}
