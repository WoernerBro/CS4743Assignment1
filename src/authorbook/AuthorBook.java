package authorbook;

import author.Author;
import book.Book;

public class AuthorBook {
	
	private Author author;
	private Book book;
	private int royalty;
	private boolean newRecord;
	
	public AuthorBook() {
		author = new Author();
		book = new Book();
		royalty = 0;
		newRecord = true;
	}
	
	public AuthorBook(Author author, Book book, int royalty) {
		this.author = author;
		this.book = book;
		this.royalty = royalty;
		newRecord = false;
	}
	
	//Validators
	
	public boolean validateRoyalty(int royalty) {
		if (royalty < 0 || royalty > 100000)
			return false;
		return true;
	}
	
	//Accessors
	
	public Author getAuthor() {
		return author;
	}
	
	public void setAuthor(Author author) {
		this.author = author;
	}
	
	public Book getBook() {
		return book;
	}
	
	public void setBook(Book book) {
		this.book = book;
	}
	
	public int getRoyalty() {
		return royalty;
	}
	
	public void setRoyalty(int royalty) {
		this.royalty = royalty;
	}
	
	public boolean getNewRecord() {
		return newRecord;
	}
	
	public void setNewRecord(boolean newRecord) {
		this.newRecord = newRecord;
	}
}
