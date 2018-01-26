package author;

import java.time.LocalDate;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Author {
	private int authorID;
	private SimpleStringProperty authorFirstName;
	private SimpleStringProperty authorLastName;
	private SimpleObjectProperty<LocalDate> authorDOB;
	private SimpleStringProperty authorGender;
	private SimpleStringProperty authorWebsite;
	
	public Author() {
		authorID = -1;
		authorFirstName = new SimpleStringProperty();
		authorLastName = new SimpleStringProperty();
		authorDOB = new SimpleObjectProperty<LocalDate>();
		authorGender = new SimpleStringProperty();
		authorWebsite = new SimpleStringProperty();
	}
	
	public Author(int authorID, String authorFirstName, String authorLastName, LocalDate authorDOB, String authorGender, String authorWebsite) {
		this();
		this.authorID = authorID;
		this.authorFirstName.set(authorFirstName);
		this.authorLastName.set(authorLastName);
		this.authorDOB.set(authorDOB);
		this.authorGender.set(authorGender);
		this.authorWebsite.set(authorWebsite);
	}
	
	public String toString() {
		return authorID + "\t" + getAuthorFirstName() + " " + getAuthorLastName();
	}
	
	public int getAuthorID() {
		return authorID;
	}
	
	public void setAuthorID(int authorID) {
		this.authorID = authorID;
	}
	
	public String getAuthorFirstName() {
		return authorFirstName.get();
	}
	
	public void setAuthorFirstName(String authorFirstName) {
		this.authorFirstName.set(authorFirstName);
	}
	
	public String getAuthorLastName() {
		return authorLastName.get();
	}
	
	public void setAuthorLastName(String authorLastName) {
		this.authorLastName.set(authorLastName);
	}
	
	public LocalDate getAuthorDOB() {
		return authorDOB.get();
	}
	
	public void setAuthorDOB(LocalDate authorDOB) {
		this.authorDOB.set(authorDOB);
	}
	
	public String getAuthorGender() {
		return authorGender.get();
	}
	
	public void setAuthorGender(String authorGender) {
		this.authorGender.set(authorGender);
	}
	
	public String getAuthorWebsite() {
		return authorWebsite.get();
	}
	
	public void setAuthorWebsite(String authorWebsite) {
		this.authorWebsite.set(authorWebsite);
	}
	
	public SimpleStringProperty authorFirstNameProperty() {
		return authorFirstName;
	}
	
	public SimpleStringProperty authorLastNameProperty() {
		return authorLastName;
	}
	
	public SimpleObjectProperty<LocalDate> authorDOBProperty() {
		return authorDOB;
	}
	
	public SimpleStringProperty authorGenderProperty() {
		return authorGender;
	}
	
	public SimpleStringProperty authorWebsiteProperty() {
		return authorWebsite;
	}
	
}