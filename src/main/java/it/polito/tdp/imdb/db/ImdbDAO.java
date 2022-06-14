package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {

	private Map<Integer, Actor> mappa;

	public List<Actor> listAllActors(HashMap<Integer, Actor> mapActors) {
		return new ArrayList<>(mapActors.values());
	}

	public List<Movie> listAllMovies() {
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), res.getInt("year"),
						res.getDouble("rank"));

				result.add(movie);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Director> listAllDirectors() {
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"),
						res.getString("last_name"));

				result.add(director);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Map<Integer, Actor> idMapActorsGenre(String genere) {
		String sql = "SELECT DISTINCT(a.id) AS id, a.* FROM roles r, movies_genres mg, movies m, actors a "
				+ " WHERE mg.movie_id = m.id  AND m.id = r.movie_id AND r.actor_id = a.id AND mg.genre = ?";
		Map<Integer, Actor> ris = new HashMap<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				Actor actor = new Actor(id, res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));

				ris.put(id, actor);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		mappa = ris;
		return ris;
	}

	public List<String> listGenres() {
		String sql = "SELECT DISTINCT(genre) FROM movies_genres";
		List<String> ris = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				ris.add(res.getString("genre"));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ris;
	}

	public List<Adiacenza> getAdiacenzeGenre(String genere) {
		String sql = "SELECT r1.actor_id as id1, r2.actor_id as id2, COUNT(DISTINCT r1.movie_id) AS peso FROM roles r1, roles r2, movies_genres mg WHERE r1.actor_id > r2.actor_id "
				+ " AND r1.movie_id = r2.movie_id AND mg.movie_id = r1.movie_id "
				+ "AND mg.genre = ? GROUP BY r1.actor_id, r2.actor_id";
		List<Adiacenza> ris = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Adiacenza a = new Adiacenza(mappa.get(res.getInt("id1")), mappa.get(res.getInt("id2")),
						res.getInt("peso"));
				ris.add(a);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ris;
	}

}
