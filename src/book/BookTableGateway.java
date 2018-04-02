package book;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import audit_trail.AuditTrailEntry;
import author.Author;
import author.AuthorTableGateway;
import authorbook.AuthorBook;
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
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in getBooks(): "+sqlError);
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in getBooks(): "+sqlError);
			}
		}
		
		return books;
	}
	
	public Book getBook(int bookID) {
		logger.info("calling getBook()");
		
		PreparedStatement st = null;
		ResultSet rs = null;
		Book book = null;
		
		try {
			String query = "SELECT * FROM bookTable WHERE id=?";
			st = menuController.getDBConnection().prepareStatement(query);
			st.setInt(1, bookID);
			rs = st.executeQuery();
			
			rs.next();
			book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("summary"), rs.getInt("year_published"), new PublisherTableGateway().getPublisherByID(rs.getInt("publisher_id")), rs.getString("isbn"), rs.getDate("date_added").toLocalDate());
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in getBook(): "+sqlError);
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in getBook(): "+sqlError);
			}
		}
		
		return book;
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
			logger.info("try/catch SQLException in updateBook(): "+sqlError);
			
			throw sqlError;
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in updateBook(): "+sqlError);
				
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
			logger.info("try/catch SQLException in insertBook(): "+sqlError);
			
			throw sqlError;
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in insertBook(): "+sqlError);
				
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
			logger.info("try/catch SQLException in deleteBook(): "+sqlError);
			
			throw sqlError;
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in deleteBook(): "+sqlError);
				
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
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in searchBooks(): "+sqlError);
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in searchBooks(): "+sqlError);
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
			
			rs.previous();
			while(rs.next()) {
				int tempID = rs.getInt("id");
				rs.getInt("book_id");
				Date tempDateAdded = new Date(rs.getTimestamp("date_added").getTime());
				String tempMessage = rs.getString("entry_msg");
				
				auditTrail.add(new AuditTrailEntry(tempID, tempDateAdded, tempMessage));
			}
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in getAuditTrail(): "+sqlError);
		} catch (Exception error) {
			logger.info("getAuditTrail() failed");
			
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
	
	public List<AuthorBook> getAuthorsForBook(int bookID) {
		logger.info("calling getAuthorsForBook()");
		
		PreparedStatement st = null;
		ResultSet rs = null;
		List<AuthorBook> authors = new ArrayList<AuthorBook>();
		
		try {
			String query = "SELECT * FROM author_book WHERE book_id=?";
			st = menuController.getDBConnection().prepareStatement(query);
			st.setInt(1, bookID);
			rs = st.executeQuery();
			
			while(rs.next()) {
				Author author = new AuthorTableGateway().getAuthor(rs.getInt("author_id"));
				Book book = new BookTableGateway().getBook(rs.getInt("book_id"));
				float royalty = rs.getBigDecimal("royalty").multiply(new BigDecimal(100)).floatValue();
				rs.getTimestamp("last_modified");
				authors.add(new AuthorBook(author, book, royalty, false));
			}
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in getAuthorsForBook(): "+sqlError);
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in getAuthorsForBook(): "+sqlError);
			}
		}
		
		return authors;
	}
}
