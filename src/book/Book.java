package book;

import java.sql.SQLException;
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	
	public Book() {
		bookID = 0;
		bookTitle = new SimpleStringProperty("");
		bookSummary = new SimpleStringProperty("");
		bookYearPublished = new SimpleIntegerProperty();
		bookPublisher = new SimpleObjectProperty<Publisher>();
		bookISBN = new SimpleStringProperty("");
		bookDateAdded = new SimpleObjectProperty<LocalDate>(LocalDate.now());
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
	}
	
	public String toString() {
		return bookID + "\t" + getBookTitle();
	}
	
	public void saveBook(int bookID, String bookTitle, String bookSummary, int bookYearPublished, Publisher bookPublisher, String bookISBN, LocalDate bookDateAdded) throws Throwable {
		try {
			if (!validateBookID(bookID)) throw new Exception("ID must be positive");
			if (!validateBookTitle(bookTitle)) throw new Exception("Title must have a length of 1-255");
			if (!validateBookSummary(bookSummary)) throw new Exception("Summary must have a length under 65536");
			if (!validateBookYearPublished(bookYearPublished)) throw new Exception("Year Published must be greater than the current year");
			if (!validateBookISBN(bookISBN)) throw new Exception("ISBN must have a length of 13 or less");
		} catch (Exception invalid) {
			logger.info("saveBook() failed");
			
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
	
	public void setBookTitle(String bookTitle) {
		this.bookTitle.set(bookTitle);
	}
	
	public String getBookSummary() {
		return bookSummary.get();
	}
	
	public void setBookSummary(String bookSummary) {
		this.bookSummary.set(bookSummary);
	}
	
	public int getBookYearPublished() {
		return bookYearPublished.get();
	}
	
	public void setBookYearPublished(int bookYearPublished) {
		this.bookYearPublished.set(bookYearPublished);
	}
	
	public Publisher getBookPublisher() {
		return bookPublisher.get();
	}
	
	public void setBookPublisher(Publisher bookPublisher) {
		this.bookPublisher.set(bookPublisher);
	}
	
	public String getBookISBN() {
		return bookISBN.get();
	}
	
	public void setBookISBN(String bookISBN) {
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