package author;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import audit_trail.AuditTrailEntry;
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
				authors.add(new Author(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getDate("dob").toLocalDate(), rs.getString("gender"), rs.getString("web_site"), rs.getTimestamp("last_modified").toLocalDateTime()));
			}
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in getAuthors(): "+sqlError);
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in getAuthors(): "+sqlError);
			}
		}
		
		return authors;
	}
	
	public void updateAuthor(Author author) throws Throwable {
		logger.info("calling updateAuthor()");
		
		PreparedStatement st = null;
		ResultSet rs = null;
		LocalDateTime tempTimestamp = null;
		
		try {
			String query = "SELECT last_modified FROM authorTable WHERE id=?";
			st = menuController.getDBConnection().prepareStatement(query);
			st.setInt(1, author.getAuthorID());
			rs = st.executeQuery();
			
			rs.next();
			tempTimestamp = rs.getTimestamp("last_modified").toLocalDateTime();
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in updateAuthors()\n"+sqlError);
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in updateAuthors(): "+sqlError);
			}
		}

		if (!author.getAuthorLastModified().equals(tempTimestamp))
			throw new Exception("Current author out of date with author in database.");

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
			logger.info("try/catch SQLException in updateAuthor(): "+sqlError);
			
			throw sqlError;
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in updateAuthor(): "+sqlError);
				
				throw sqlError;
			}
		}
		
		try {
			String query = "SELECT last_modified FROM authorTable WHERE id=?";
			st = menuController.getDBConnection().prepareStatement(query);
			st.setInt(1, author.getAuthorID());
			rs = st.executeQuery();
			
			rs.next();
			author.setAuthorLastModified(rs.getTimestamp("last_modified").toLocalDateTime());
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in updateAuthors()\n"+sqlError);
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in updateAuthors(): "+sqlError);
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
			logger.info("try/catch SQLException in insertAuthor(): "+sqlError);
			
			throw sqlError;
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in insertAuthor(): "+sqlError);
				
				throw sqlError;
			}
		}
	}
	
	public void deleteAuthor(Author author) throws SQLException {
		logger.info("calling deleteAuthor()");
		
		PreparedStatement st = null;
		
		try {
			String query = "DELETE FROM authorTable WHERE id=?";
			st = menuController.getDBConnection().prepareStatement(query);
			st.setInt(1, author.getAuthorID());
			st.executeUpdate();
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in deleteAuthor(): "+sqlError);
			
			throw sqlError;
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in deleteAuthor(): "+sqlError);
				
				throw sqlError;
			}
		}
	}
	
	public List<AuditTrailEntry> getAuditTrail(int authorID) throws Throwable {
		logger.info("calling getAuditTrail()");
		
		PreparedStatement st = null;
		ResultSet rs = null;
		List<AuditTrailEntry> auditTrail = new ArrayList<AuditTrailEntry>();
		
		try {
			String query = "SELECT * FROM author_audit_trail WHERE author_id=?";
			st = menuController.getDBConnection().prepareStatement(query);
			st.setInt(1, authorID);
			rs = st.executeQuery();
			
			if (!rs.next()) throw new Exception("Empty Audit Trail");
			
			rs.previous();
			while(rs.next()) {
				int tempID = rs.getInt("id");
				rs.getInt("author_id");
				Date tempDateAdded = new Date(rs.getTimestamp("date_added").getTime());
				String tempMessage = rs.getString("entry_msg");
				
				auditTrail.add(new AuditTrailEntry(tempID, tempDateAdded, tempMessage));
			}
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in getAuditTrail(): "+sqlError);
		} catch (Exception error) {
			logger.info("getAuditTrail() failed: "+error);
			
			throw error;
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in getAuditTrail(): "+sqlError);
			}
		}
		
		Arrays.toString(auditTrail.toArray());
		
		return auditTrail;
	}
	
	public void insertAuditTrailEntry(int auditAuthorID, String auditMessage) throws Throwable {
		logger.info("calling insertAuditTrailEntry()");
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			String query = "INSERT INTO author_audit_trail (author_id,entry_msg) VALUES (?,?)";
			st = menuController.getDBConnection().prepareStatement(query);
			st.setInt(1, auditAuthorID);
			st.setString(2, auditMessage);
			st.executeUpdate();
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in insertAuditTrailEntry(): "+sqlError);
			
			throw sqlError;
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in insertAuditTrailEntry(): "+sqlError);
				
				throw sqlError;
			}
		}
	}
}
