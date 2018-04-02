package book;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import authorbook.AuthorBook;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {
	private static Logger logger = LogManager.getLogger();
	
	private int bookID;
	private SimpleStringProperty bookTitle;
	private SimpleStringProperty bookSummary;
	private SimpleIntegerProperty bookYearPublished;
	private SimpleObjectProperty<Publisher> bookPublisher;
	private SimpleStringProperty bookISBN;
	private SimpleObjectProperty<LocalDate> bookDateAdded;
	
	private String title;
	private String summary;
	private int yearPublished;
	private Publisher publisher;
	private String isbn;
	
	public Book() {
		bookID = 0;
		bookTitle = new SimpleStringProperty("");
		bookSummary = new SimpleStringProperty("");
		bookYearPublished = new SimpleIntegerProperty();
		bookPublisher = new SimpleObjectProperty<Publisher>();
		bookISBN = new SimpleStringProperty("");
		bookDateAdded = new SimpleObjectProperty<LocalDate>(LocalDate.now());
		
		title = "";
		summary = "";
		yearPublished = 0;
		publisher = new Publisher();
		isbn = "";
	}
	
	public Book(int bookID, String bookTitle, String bookSummary, int bookYearPublished, Publisher bookPublisher, String bookISBN, LocalDate bookDateAdded) {
		this();
		this.bookID = bookID;
		this.bookTitle.set(bookTitle);
		this.bookSummary.set(bookSummary);
		this.bookYearPublished.set(bookYearPublished);
		this.bookPublisher.set(bookPublisher);
		this.bookISBN.set(bookISBN);
		this.bookDateAdded.set(bookDateAdded);

		title = bookTitle;
		summary = bookSummary;
		yearPublished = bookYearPublished;
		publisher = bookPublisher;
		isbn = bookISBN;
	}
	
	public String toString() {
		return getBookTitle();
	}
	
	public void saveBook(int bookID, String bookTitle, String bookSummary, int bookYearPublished, Publisher bookPublisher, String bookISBN, LocalDate bookDateAdded) throws Throwable {
		try {
			if (!validateBookID(bookID)) throw new Exception("ID must be positive");
			if (!validateBookTitle(bookTitle)) throw new Exception("Title must have a length of 1-255");
			if (!validateBookSummary(bookSummary)) throw new Exception("Summary must have a length under 65536");
			if (!validateBookYearPublished(bookYearPublished)) throw new Exception("Year Published must be greater than the current year");
			if (!validateBookISBN(bookISBN)) throw new Exception("ISBN must have a length of 13 or less");
		} catch (Exception invalid) {
			logger.info("saveBook() failed: "+invalid);
			
			throw invalid;
		}

		setBookID(bookID);
		setBookTitle(bookTitle);
		setBookSummary(bookSummary);
		setBookYearPublished(bookYearPublished);
		setBookPublisher(bookPublisher);
		setBookISBN(bookISBN);
		setBookDateAdded(bookDateAdded);
		
		if (bookID == 0) {
			try {
				setBookID(new BookTableGateway().insertBook(this));
				new BookTableGateway().insertAuditTrailEntry(getBookID(), "Book inserted: "+getBookTitle());
			} catch (SQLException sqlError) {
				logger.info(sqlError);
				
				throw sqlError;
			}
		} else {
			try {
				new BookTableGateway().updateBook(this);
			} catch (SQLException sqlError) {
				logger.info(sqlError);
				
				throw sqlError;
			}
		}
	}
	
	public List<AuthorBook> getAuthors() {
		return new BookTableGateway().getAuthorsForBook(bookID);
	}
	
	//Validators
	
	public boolean validateBookID(int id) {
		if (id < 0)
			return false;
		return true;
	}
	
	public boolean validateBookTitle(String bookTitle) {
		if (bookTitle.length() < 1 || bookTitle.length() > 255)
			return false;
		return true;
	}
	
	public boolean validateBookSummary(String bookSummary) {
		if (bookSummary.length() >= 65536)
			return false;
		return true;
	}
	
	public boolean validateBookYearPublished(int bookYearPublished) {
		if (bookYearPublished > LocalDate.now().getYear())
			return false;
		return true;
	}
	
	public boolean validateBookISBN(String bookISBN) {
		if (bookISBN.length() > 13)
			return false;
		return true;
	}
	
	//Accessors
	
	public int getBookID() {
		return bookID;
	}
	
	public void setBookID(int bookID) {
		this.bookID = bookID;
	}
	
	public String getBookTitle() {
		return bookTitle.get();
	}
	
	public void setBookTitle(String bookTitle){
		if (getBookID() != 0 && !title.equals(bookTitle)) {
			try {
				new BookTableGateway().insertAuditTrailEntry(getBookID(),"title changed from "+title+" to "+bookTitle);
			} catch (Throwable error) {
				logger.info("insertAuditTrailEntry() failed in setBookTitle(): "+error);
			}
		}
		title = bookTitle;
		this.bookTitle.set(bookTitle);
	}
	
	public String getBookSummary() {
		return bookSummary.get();
	}
	
	public void setBookSummary(String bookSummary) {
		if (getBookID() != 0 && !summary.equals(bookSummary)) {
			try {
				new BookTableGateway().insertAuditTrailEntry(getBookID(),"summary changed from "+summary+" to "+bookSummary);
			} catch (Throwable error) {
				logger.info("insertAuditTrailEntry() failed in setBookSummary(): "+error); 
			}
		}
		summary = bookSummary;
		this.bookSummary.set(bookSummary);
	}
	
	public int getBookYearPublished() {
		return bookYearPublished.get();
	}
	
	public void setBookYearPublished(int bookYearPublished) {
		if (getBookID() != 0 && yearPublished != bookYearPublished) {
			try {
				new BookTableGateway().insertAuditTrailEntry(getBookID(),"year_published changed from "+yearPublished+" to "+bookYearPublished);
			} catch (Throwable error) {
				logger.info("insertAuditTrailEntry() failed in setBookYearPublished(): "+error); 
			}
		}
		yearPublished = bookYearPublished;
		this.bookYearPublished.set(bookYearPublished);
	}
	
	public Publisher getBookPublisher() {
		return bookPublisher.get();
	}
	
	public void setBookPublisher(Publisher bookPublisher) {
		if (getBookID() != 0 && publisher != bookPublisher) {
			try {
				new BookTableGateway().insertAuditTrailEntry(getBookID(),"publisher changed from "+publisher+" to "+bookPublisher);
			} catch (Throwable error) {
				logger.info("insertAuditTrailEntry() failed in setBookPublisher(): "+error); 
			}
		}
		publisher = bookPublisher;
		this.bookPublisher.set(bookPublisher);
	}
	
	public String getBookISBN() {
		return bookISBN.get();
	}
	
	public void setBookISBN(String bookISBN) {
		if (getBookID() != 0 && !isbn.equals(bookISBN)) {
			try {
				new BookTableGateway().insertAuditTrailEntry(getBookID(),"isbn changed from "+isbn+" to "+bookISBN);
			} catch (Throwable error) {
				logger.info("insertAuditTrailEntry() failed in setBookISBN(): "+error); 
			}
		}
		isbn = bookISBN;
		this.bookISBN.set(bookISBN);
	}
	
	public LocalDate getBookDateAdded() {
		return bookDateAdded.get();
	}
	
	public void setBookDateAdded(LocalDate bookDateAdded) {
		this.bookDateAdded.set(bookDateAdded);
	}
	
	//Direct Getters
	
	public SimpleStringProperty bookTitleProperty() {
		return bookTitle;
	}
	
	public SimpleStringProperty bookSummaryProperty() {
		return bookSummary;
	}
	
	public SimpleIntegerProperty bookYearPublishedProperty() {
		return bookYearPublished;
	}
	
	public SimpleObjectProperty<Publisher> bookPublisherProperty() {
		return bookPublisher;
	}
	
	public SimpleStringProperty bookISBNProperty() {
		return bookISBN;
	}
	
	public SimpleObjectProperty<LocalDate> bookDateAddedProperty() {
		return bookDateAdded;
	}
	
}