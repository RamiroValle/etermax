package com.ramirovalle.etermax;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EtermaxApplicationTests {

	@Autowired
	MockMvc mvc;
	private final ObjectMapper mapper = new ObjectMapper();

	private String getToken() throws Exception{
		MvcResult result = this.mvc.perform(post("/login")
						.with(httpBasic("admin", "pass")))
				.andReturn();
		return result.getResponse().getContentAsString();
	}

	private ResultActions playlistPost(String token) throws Exception {
		var cancion1 = new Cancion("cancion1", "artista1", "album1");
		var cancion2 = new Cancion("cancion2", "artista2", "album2");
		var playlist = new Playlist("name", Arrays.asList(cancion1, cancion2));
		var json = mapper.writeValueAsString(playlist);
		return this.mvc.perform(post("/api/playlists")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.header("Authorization", "Bearer " + token));
	}

	@Test
	@WithAnonymousUser
	public void anonymousUserLogin() throws Exception {
		String token = getToken();

		this.mvc.perform(get("/api/playlists")
				.param("cancion", "noexiste")
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithAnonymousUser
	public void anonymousUserNotAuthorized() throws Exception {
		this.mvc.perform(get("/api/playlists")
						.param("cancion", "noexiste")
						.header("Authorization", "Bearer " + "asd"))
				.andExpect(status().isForbidden());
	}

	@Test
	public void authorizedUserPlaylistPost() throws Exception {
		String token = getToken();
		var cancion1 = new Cancion("cancion1", "artista1", "album1");
		var cancion2 = new Cancion("cancion2", "artista2", "album2");
		var playlist = new Playlist("name", Arrays.asList(cancion1, cancion2));
		var json = mapper.writeValueAsString(playlist);
		this.mvc.perform(post("/api/playlists")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk());
	}

	@Test
	public void authorizedUserPlaylistPut() throws Exception {
		String token = getToken();
		playlistPost(token);
		var cancion1 = new Cancion("cancion1", "artista1", "album1");
		var cancion2 = new Cancion("cancion2", "artista2", "album2");
		var cancion3 = new Cancion("cancion3", "artista2", "album2");
		var playlist = new Playlist("name", Arrays.asList(cancion1, cancion2, cancion3));
		var json = mapper.writeValueAsString(playlist);
		this.mvc.perform(put("/api/playlists/name")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk());
	}

	@Test
	public void authorizedUserPlaylistGet() throws Exception {
		String token = getToken();
		playlistPost(token);
		this.mvc.perform(get("/api/playlists")
						.param("cancion", "name")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk());
	}

	@Test
	public void authorizedUserPlaylistGetNotFound() throws Exception {
		String token = getToken();
		this.mvc.perform(get("/api/playlists")
						.param("cancion", "name2")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isNotFound());
	}

	@Test
	public void authorizedUserPlaylistDelete() throws Exception {
		String token = getToken();
		playlistPost(token);
		this.mvc.perform(delete("/api/playlists/name")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk());
	}

}
