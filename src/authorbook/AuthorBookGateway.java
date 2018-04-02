package authorbook;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import book.BookTableGateway;
import menu.MenuController;

public class AuthorBookGateway {
	private static Logger logger = LogManager.getLogger();
	private static MenuController menuController;
	
	public AuthorBookGateway() {
		menuController = MenuController.getInstanceOfMenuController();
	}
	
	public void addAuthorBook(AuthorBook authorBook) throws Throwable {
		logger.info("calling insertAuditTrailEntry()");
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			String query = "INSERT INTO author_book (author_id,book_id,royalty) VALUES (?,?,?)";
			st = menuController.getDBConnection().prepareStatement(query);
			st.setInt(1, authorBook.getAuthor().getAuthorID());
			st.setInt(2, authorBook.getBook().getBookID());
			st.setFloat(3, authorBook.getRoyalty()/100);
			st.executeUpdate();
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in addAuthorBook(): "+sqlError);
			
			throw sqlError;
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in addAuthorBook(): "+sqlError);
				
				throw sqlError;
			}
		}
	}
	
	public void updateAuthorBook(AuthorBook authorBook) throws Throwable {
		logger.info("calling updateAuthorBook()");
		
		PreparedStatement st = null;
		
		try {
			String query = "UPDATE author_book SET author_id=?,book_id=?,royalty=? WHERE author_id=? AND book_id=?";
			st = menuController.getDBConnection().prepareStatement(query);
			st.setInt(1, authorBook.getAuthor().getAuthorID());
			st.setInt(2, authorBook.getBook().getBookID());
			st.setFloat(3, authorBook.getRoyalty()/100);
			st.setInt(4, authorBook.getAuthor().getAuthorID());
			st.setInt(5, authorBook.getBook().getBookID());
			st.executeUpdate();
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in updateAuthorBook(): "+sqlError);
			
			throw sqlError;
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in updateAuthorBook(): "+sqlError);
				
				throw sqlError;
			}
		}
	}
	
	public void removeAuthorBook(AuthorBook authorBook) throws Throwable {
		logger.info("calling removeAuthorBook()");
		
		PreparedStatement st = null;
		
		try {
			String query = "DELETE FROM author_book WHERE author_id=? AND book_id=?";
			st = menuController.getDBConnection().prepareStatement(query);
			st.setInt(1, authorBook.getAuthor().getAuthorID());
			st.setInt(2, authorBook.getBook().getBookID());
			st.executeUpdate();
			new BookTableGateway().insertAuditTrailEntry(authorBook.getBook().getBookID(), "Author removed from Book: " + authorBook.getAuthor().getAuthorFirstName() + " " + authorBook.getAuthor().getAuthorLastName());
		} catch (SQLException sqlError) {
			logger.info("try/catch SQLException in removeAuthorBook(): "+sqlError);
			
			throw sqlError;
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException sqlError) {
				logger.info("try/catch/finally SQLException in removeAuthorBook(): "+sqlError);
				
				throw sqlError;
			}
		}
	}

}
