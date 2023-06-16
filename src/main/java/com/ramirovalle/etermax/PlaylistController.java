package com.ramirovalle.etermax;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class PlaylistController {

    public final DatabaseMock db = new DatabaseMock();

    @RequestMapping(path="/api/playlists",method=RequestMethod.POST)
    public Playlist postPlaylist(@RequestBody Playlist playlist) {
        var mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(playlist);
        } catch (JsonProcessingException ignored) {
        }
        db.storePlaylist(playlist.name(), json);
        return playlist;
    }

    @RequestMapping(path="/api/playlists/{playlistId}",method=RequestMethod.PUT)
    public Playlist putPlaylist(@RequestBody Playlist playlist, @PathVariable String playlistId) {
        var mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(playlist);
        } catch (JsonProcessingException ignored) {
        }
        if (db.updatePlaylist(playlistId, json)) {
            return playlist;
        } else {
            throw new PlaylistNotFoundException(playlistId);
        }
    }

    @RequestMapping(path="/api/playlists",method=RequestMethod.GET)
    public Playlist getPlaylist(@RequestParam(value = "cancion") String nombre) {
        var mapper = new ObjectMapper();
        String json = db.getPlaylist(nombre);
        if (json != null) {
            try {
                return mapper.readValue(json, Playlist.class);
            } catch (IOException ignored) { }
        } else {
            throw new PlaylistNotFoundException(nombre);
        }

        return null;
    }

    @RequestMapping(path="/api/playlists/{playlistId}",method=RequestMethod.DELETE)
    public void deletePlaylist(@PathVariable String playlistId) {
        if (!db.deletePlaylist(playlistId)) {
            throw new PlaylistNotFoundException(playlistId);
        }
    }
}
