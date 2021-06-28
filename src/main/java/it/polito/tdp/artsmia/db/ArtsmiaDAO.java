package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getRuoli() {
		String sql = "SELECT DISTINCT role "
				+ "FROM authorship";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("role"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void getVertici(Map<Integer, Artist> idMap, String ruolo) {
		String sql = "SELECT a.artist_id, a.name "
				+ "FROM artists a, authorship au "
				+ "WHERE a.artist_id = au.artist_id AND au.role = ?";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if (idMap.get(res.getInt("artist_id"))==null) {
					Artist a = new Artist(res.getInt("artist_id"), res.getString("name"));
					idMap.put(res.getInt("artist_id"), a);
				}
				
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Adiacenza> getArchi(Map<Integer, Artist> idMap, String ruolo) {
		String sql = "SELECT a1.artist_id AS art1, a2.artist_id AS art2, COUNT(*) AS peso "
				+ "FROM authorship a1, authorship a2, objects o1, objects o2, exhibition_objects eo1, exhibition_objects eo2 "
				+ "WHERE a1.object_id = o1.object_id AND o1.object_id = eo1.object_id AND a2.object_id = o2.object_id AND o2.object_id = eo2.object_id AND eo1.exhibition_id = eo2.exhibition_id AND a1.artist_id > a2.artist_id "
				+ "AND a1.role = ? AND a2.role = ? "
				+ "GROUP BY a1.artist_id, a2.artist_id";
		Connection conn = DBConnect.getConnection();
		List<Adiacenza> risultato = new LinkedList<>();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			st.setString(2, ruolo);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if (idMap.get(res.getInt("art1"))!=null && idMap.get(res.getInt("art2"))!=null) {
					Adiacenza a = new Adiacenza(idMap.get(res.getInt("art1")), idMap.get(res.getInt("art2")), res.getInt("peso"));
					risultato.add(a);
				}
				
			}
			conn.close();
			return risultato;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
