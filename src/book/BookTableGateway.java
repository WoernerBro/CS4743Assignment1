package book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import audit_trail.AuditTrailEntry;
import menu.MenuController;

public class BookTableGateway {
	private static Logger logger = LogManager.getLogger();
	private static MenuController menuController;
	
	public BookTableGateway() {
		menuController = MenuController.getInstanceOfMenuController();
	}
	
	public List<Book> getBooks() {
		logger.info("calling getBooks()");
		
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Book> books = new ArrayList<Book>();
		
		try {
			String query = "SELECT * FROM bookTable";
			st = menuController.getDBConnection().prepareStatement(query);
			rs = st.executeQuery();
			
			while(rs.next()) {
				books.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("summary"), rs.getInt("year_published"), new PublisherTableGateway().getPublisherByID(rs.getInt("publisher_id")), rs.getString("isbn"), rs.getDate("date_added").toLocalDate()));
			}
		} catch (SQLException e) {
			logger.info("try/catch SQLException in getBooks()");
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				logger.info("try/catch/finally SQLException in getBooks()");
			}
		}
		
		return books;
	}
	
	public void updateBook(Book book) throws Throwable {
		logger.info("calling updateBook()");
		
		PreparedStatement st = null;
		
		try {
			String query = "UPDATE bookTable SET title=?,summary=?,year_published=?,publisher_id=?,isbn=? WHERE id=?";
			st = menuController.getDBConnection().prepareStatement(query);
			st.setString(1, book.getBookTitle());
			if (book.getBookSummary().equals(""))
				st.setNull(2, java.sql.Types.VARCHAR);
			else
				st.setString(2, book.getBookSummary());
			if (book.getBookYearPublished() == 0)
				st.setNull(3, java.sql.Types.INTEGER);
			else
				st.setInt(3, book.getBookYearPublished());
			st.setInt(4, book.getBookPublisher().getPublisherID());
			if (book.getBookISBN().equals(""))
				st.setNull(5, java.sql.Types.VARCHAR);
			else
				st.setString(5, book.getBookISBN());
			st.setInt(6, book.getBookID());			
			st.executeUpdate();
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in updateBook()");
			
			throw sqlError;
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in updateBook()");
				
				throw sqlError;
			}
		}
	}
	
	public int insertBook(Book book) throws Throwable {
		logger.info("calling insertBook()");
		
		PreparedStatement st = null;
		ResultSet rs = null;
		int insertIndex = -1;
		
		try {
			String query = "INSERT INTO bookTable (title,summary,year_published,publisher_id,isbn) VALUES (?,?,?,?,?)";
			st = menuController.getDBConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			st.setString(1, book.getBookTitle());
			if (book.getBookSummary().equals(""))
				st.setNull(2, java.sql.Types.VARCHAR);
			else
				st.setString(2, book.getBookSummary());
			if (book.getBookYearPublished() == 0)
				st.setNull(3, java.sql.Types.INTEGER);
			else
				st.setInt(3, book.getBookYearPublished());
			st.setInt(4, book.getBookPublisher().getPublisherID());
			if (book.getBookISBN().equals(""))
				st.setNull(5, java.sql.Types.VARCHAR);
			else
				st.setString(5, book.getBookISBN());
			st.executeUpdate();
			
			rs = st.getGeneratedKeys();
			if(rs != null && rs.next())
				insertIndex = rs.getInt(1);
			return insertIndex;
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in insertBook()");
			
			throw sqlError;
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in insertBook()");
				
				throw sqlError;
			}
		}
	}
	
	public void deleteBook(Book book) throws SQLException {
		logger.info("calling deleteBook()");
		
		PreparedStatement st = null;
		
		try {
			String query = "DELETE FROM bookTable WHERE id=?";
			st = menuController.getDBConnection().prepareStatement(query);
			st.setInt(1, book.getBookID());
			st.executeUpdate();
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in deleteBook()");
			
			throw sqlError;
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in deleteBook()");
				
				throw sqlError;
			}
		}
	}
	
	public List<Book> searchBooks(String searchTitle) throws SQLException {
		logger.info("calling searchBooks()");
		
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Book> books = new ArrayList<Book>();
		searchTitle = "%" + searchTitle + "%";
		
		try {
			String query = "SELECT * FROM bookTable WHERE title LIKE ?";
			st = menuController.getDBConnection().prepareStatement(query);
			st.setString(1, searchTitle);
			rs = st.executeQuery();
			
			while(rs.next()) {
				books.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("summary"), rs.getInt("year_published"), new PublisherTableGateway().getPublisherByID(rs.getInt("publisher_id")), rs.getString("isbn"), rs.getDate("date_added").toLocalDate()));
			}
		} catch (SQLException e) {
			logger.info("try/catch SQLException in searchBooks()");
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				logger.info("try/catch/finally SQLException in searchBooks()");
			}
		}
		
		return books;
	}
	
	public List<AuditTrailEntry> getAuditTrail(int bookID) throws Throwable {
		logger.info("calling getAuditTrail()");
		
		PreparedStatement st = null;
		ResultSet rs = null;
		List<AuditTrailEntry> auditTrail = new ArrayList<AuditTrailEntry>();
		
		try {
			String query = "SELECT * FROM book_audit_trail WHERE book_id=?";
			st = menuController.getDBConnection().prepareStatement(query);
			st.setInt(1, bookID);
			rs = st.executeQuery();
			
			if (!rs.next()) throw new Exception("Empty Audit Trail");
			
			while(rs.next()) {
				int tempID = rs.getInt("id");
				rs.getInt("book_id");
				LocalDate tempDateAdded = rs.getDate("date_added").toLocalDate();
				String tempMessage = rs.getString("entry_msg");
				
				auditTrail.add(new AuditTrailEntry(tempID, tempDateAdded, tempMessage));
			}
		} catch (SQLException e) {
			logger.info("try/catch SQLException in getAuditTrail()");
		} catch (Exception error) {
			logger.info("getAuditTrail() failed");
			
			throw error;
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				logger.info("try/catch/finally SQLException in getAuditTrail()");
			}
		}
		
		return auditTrail;
	}
	
	public void insertAuditTrailEntry(int auditBookID, String auditMessage) throws Throwable {
		logger.info("calling insertAuditTrailEntry()");
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			String query = "INSERT INTO book_audit_trail (book_id,entry_msg) VALUES (?,?)";
			st = menuController.getDBConnection().prepareStatement(query);
			st.setInt(1, auditBookID);
			st.setString(2, auditMessage);
			st.executeUpdate();
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in insertAuditTrailEntry()");
			
			throw sqlError;
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in insertAuditTrailEntry()");
				
				throw sqlError;
			}
		}
	}
}
