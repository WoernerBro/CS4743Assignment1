package book;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
			logger.info("try/catch SQLException in getBooks(");
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
			String query = "UPDATE bookTable SET id=?,title=?,summary=?,year_published=?,publisher_id=?,isbn=?,date_added=? WHERE id=?";
			st = menuController.getDBConnection().prepareStatement(query);
			st.setInt(1, book.getBookID());
			st.setString(2, book.getBookTitle());
			st.setString(3, book.getBookSummary());
			st.setInt(4, book.getBookYearPublished());
			st.setInt(5, book.getBookPublisher().getPublisherID());
			st.setString(6, book.getBookISBN());
			st.setDate(7, Date.valueOf(book.getBookDateAdded()));
			st.setInt(8, book.getBookID());
			st.executeUpdate();
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in updateBook(");
			
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
			String query = "INSERT INTO bookTable (title,summary,year_published,publisher_id,isbn,date_added) VALUES (?,?,?,?,?,?)";
			st = menuController.getDBConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			st.setString(1, book.getBookTitle());
			st.setString(2, book.getBookSummary());
			st.setInt(3, book.getBookYearPublished());
			st.setInt(4, book.getBookPublisher().getPublisherID());
			st.setString(5, book.getBookISBN());
			st.setDate(6, Date.valueOf(book.getBookDateAdded()));
			st.executeUpdate();
			
			rs = st.getGeneratedKeys();
			if(rs != null && rs.next())
				insertIndex = rs.getInt(1);
			return insertIndex;
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in insertBook(");
			
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
			logger.info("try/catch SQLException in deleteBook(");
			
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
			logger.info("try/catch SQLException in searchBooks(");
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
}
