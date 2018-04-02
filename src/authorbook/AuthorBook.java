package authorbook;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import author.Author;
import book.Book;
import book.BookTableGateway;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;

public class AuthorBook {
	private static Logger logger = LogManager.getLogger();
	
	private SimpleObjectProperty<Author> author;
	private SimpleObjectProperty<Book> book;
	private SimpleFloatProperty royalty;
	private boolean newRecord;
	
	private float oldRoyalty;
	
	public AuthorBook() {
		author = new SimpleObjectProperty<Author>();
		book = new SimpleObjectProperty<Book>();
		royalty = new SimpleFloatProperty();
		newRecord = true;
		
		oldRoyalty = -1;
	}
	
	public AuthorBook(Author author, Book book, float royalty, boolean newRecord) {
		this();
		this.author.set(author);
		this.book.set(book);
		this.royalty.set(royalty);
		this.newRecord = newRecord;
		
		oldRoyalty = royalty;
	}
	
	public String toString() {
		return getAuthor().getAuthorFirstName() + " " + getAuthor().getAuthorLastName() + "\t\t" + getBook().getBookTitle() + "\t\t" + getRoyalty() + "%";
	}
	
	public void saveAuthorBook(Author author, Book book, float royalty) throws Throwable {
		try {
			if (!validateAuthor(author)) throw new Exception("Author must not be blank");
			if (!validateBook(book)) throw new Exception("Book must not be blank");
			if (!validateRoyalty(royalty)) throw new Exception("Royalty must be from 0% to 100%");
		} catch (Exception invalid) {
			logger.info("saveAuthorBook() failed: "+invalid);
			
			throw invalid;
		}

		setAuthor(author);
		setBook(book);
		setRoyalty(royalty);
		
		if (newRecord) {
			try {
				new AuthorBookGateway().addAuthorBook(this);
				new BookTableGateway().insertAuditTrailEntry(book.getBookID(), "Author added : "+author.getAuthorFirstName() + " " + author.getAuthorLastName());
			} catch (SQLException sqlError) {
				logger.info(sqlError);
				
				throw sqlError;
			}
		} else {
			try {
				new AuthorBookGateway().updateAuthorBook(this);
			} catch (SQLException sqlError) {
				logger.info(sqlError);
				
				throw sqlError;
			}
		}
	}
	
	//Validators
	
	public boolean validateAuthor(Author author) {
		if (author.getAuthorID() == 0)
			return false;
		return true;
	}
	
	public boolean validateBook(Book book) {
		if (book.getBookID() == 0)
			return false;
		return true;
	}
	public boolean validateRoyalty(float royalty) {
		if (royalty < 0f || royalty > 100f)
			return false;
		return true;
	}
	
	//Accessors
	
	public Author getAuthor() {
		return author.get();
	}
	
	public void setAuthor(Author author) {
		this.author.set(author);
	}
	
	public Book getBook() {
		return book.get();
	}
	
	public void setBook(Book book) {
		this.book.set(book);
	}
	
	public float getRoyalty() {
		return royalty.get();
	}
	
	public void setRoyalty(float royalty) {
		if (!newRecord && oldRoyalty != royalty) {
			try {
				new BookTableGateway().insertAuditTrailEntry(getBook().getBookID(),"royalty of " + getAuthor().getAuthorFirstName() + " " + getAuthor().getAuthorLastName() + " changed from "+oldRoyalty+" to "+royalty);
			} catch (Throwable error) {
				logger.info("insertAuditTrailEntry() failed in setRoyalty(): "+error); 
			}
		}
		oldRoyalty = royalty;
		this.royalty.set(royalty);
	}
	
	public boolean getNewRecord() {
		return newRecord;
	}
	
	public void setNewRecord(boolean newRecord) {
		this.newRecord = newRecord;
	}
	
	//Direct Getters
	
	public SimpleObjectProperty<Author> authorProperty() {
		return author;
	}
	
	public SimpleObjectProperty<Book> bookProperty() {
		return book;
	}
	
	public SimpleFloatProperty royaltyProperty() {
		return royalty;
	}
	
}
