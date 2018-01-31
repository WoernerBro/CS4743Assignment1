package author;

import java.sql.SQLException;
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Author {
	private static Logger logger = LogManager.getLogger();
	
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
		authorWebsite = new SimpleStringProperty("");
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
	
	public void saveAuthor(int authorID, String authorFirstName, String authorLastName, LocalDate authorDOB, String authorGender, String authorWebsite) throws Throwable {
		if (authorID == -1) {
			try {
				setAuthorID(new AuthorTableGateway().insertAuthor(this));
			} catch (SQLException sqlError) {
				logger.info(sqlError);
				
				throw sqlError;
			}
		} else {
			try {
				if (!validateAuthorID(authorID)) throw new Exception("ID must be positive");
				if (!validateAuthorFirstName(authorFirstName)) throw new Exception("First name must have a length of 1-100");
				if (!validateAuthorLastName(authorLastName)) throw new Exception("Last name must have a length of 1-100");
				if (!validateAuthorDOB(authorDOB)) throw new Exception("Date of birth must be before today's date");
				if (!validateAuthorGender(authorGender)) throw new Exception("Gender must be \"Male\", \"Female\", or \"Unknown\"");
				if (!validateAuthorWebsite(authorWebsite)) throw new Exception("Website must have a length of 0-100");
			} catch (Exception invalid) {
				logger.info("saveAuthor() failed");
				
				throw invalid;
			}
			
			setAuthorID(authorID);
			setAuthorFirstName(authorFirstName);
			setAuthorLastName(authorLastName);
			setAuthorDOB(authorDOB);
			setAuthorGender(authorGender);
			setAuthorWebsite(authorWebsite);
			
			try {
				new AuthorTableGateway().updateAuthor(this);
			} catch (SQLException sqlError) {
				logger.info(sqlError);
				
				throw sqlError;
			}
		}
	}
	
	//Validators
	
	public boolean validateAuthorID(int id) {
		if (id < 0)
			return false;
		return true;
	}
	
	public boolean validateAuthorFirstName(String authorFirstName) {
		if (authorFirstName.length() < 1 || authorFirstName.length() > 100)
			return false;
		return true;
	}
	
	public boolean validateAuthorLastName(String authorLastName) {
		if (authorLastName.length() < 1 || authorLastName.length() > 100)
			return false;
		return true;
	}
	
	public boolean validateAuthorDOB(LocalDate authorDOB) {
		if (authorDOB.isBefore(LocalDate.now()))
			return true;
		return false;
	}
	
	public boolean validateAuthorGender(String authorGender) {
		if (authorGender.equals("Male") || authorGender.equals("Female") || authorGender.equals("Unknown"))
			return true;
		return false;
	}
	
	public boolean validateAuthorWebsite(String authorWebsite) {
		if (authorWebsite.length() > 100)
			return false;
		return true;
	}
	
	//Accessors
	
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
	
	//Direct Getters
	
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