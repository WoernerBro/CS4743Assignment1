package author;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import menu.MenuController;

public class AuthorTableGateway {
	private static Logger logger = LogManager.getLogger();
	private static MenuController menuController;
	
	public AuthorTableGateway() {
		menuController = MenuController.getInstanceOfMenuController();
	}
	
	public List<Author> getAuthors() {
		logger.info("calling getAuthors()");
		
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Author> authors = new ArrayList<Author>();
		
		try {
			String query = "SELECT * FROM authorTable";
			st = menuController.getDBConnection().prepareStatement(query);
			rs = st.executeQuery();
			
			while(rs.next()) {
				authors.add(new Author(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getDate("dob").toLocalDate(), rs.getString("gender"), rs.getString("web_site")));
			}
		} catch (SQLException e) {
			logger.info("try/catch SQLException in getAuthors(");
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				logger.info("try/catch/finally SQLException in getAuthors()");
			}
		}
		
		return authors;
	}
	
	public void updateAuthor(Author author) throws Throwable {
		logger.info("calling updateAuthor()");
		
		PreparedStatement st = null;
		
		try {
			String query = "UPDATE authorTable SET id=?,first_name=?,last_name=?,dob=?,gender=?,web_site=? WHERE id=?";
			st = menuController.getDBConnection().prepareStatement(query);
			st.setInt(1, author.getAuthorID());
			st.setString(2, author.getAuthorFirstName());
			st.setString(3, author.getAuthorLastName());
			st.setDate(4, Date.valueOf(author.getAuthorDOB()));
			st.setString(5, author.getAuthorGender());
			st.setString(6, author.getAuthorWebsite());
			st.setInt(7, author.getAuthorID());
			st.executeUpdate();
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in updateAuthor(");
			
			throw sqlError;
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in updateAuthor()");
				
				throw sqlError;
			}
		}
	}
	
	public int insertAuthor(Author author) throws Throwable {
		logger.info("calling insertAuthor()");
		
		PreparedStatement st = null;
		ResultSet rs = null;
		int insertIndex = -1;
		
		try {
			String query = "INSERT INTO authorTable (first_name,last_name,dob,gender,web_site) VALUES (?,?,?,?,?)";
			st = menuController.getDBConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			st.setString(1, author.getAuthorFirstName());
			st.setString(2, author.getAuthorLastName());
			st.setDate(3, Date.valueOf(author.getAuthorDOB()));
			st.setString(4, author.getAuthorGender());
			st.setString(5, author.getAuthorWebsite());
			st.executeUpdate();
			
			rs = st.getGeneratedKeys();
			if(rs != null && rs.next())
				insertIndex = rs.getInt(1);
			return insertIndex;
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in insertAuthor(");
			
			throw sqlError;
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in insertAuthor()");
				
				throw sqlError;
			}
		}
	}
}
