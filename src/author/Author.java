package author;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
	private LocalDateTime authorLastModified;
	
	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
	private String gender;
	private String website;
	
	public Author() {
		authorID = 0;
		authorFirstName = new SimpleStringProperty("");
		authorLastName = new SimpleStringProperty("");
		authorDOB = new SimpleObjectProperty<LocalDate>(LocalDate.now());
		authorGender = new SimpleStringProperty("");
		authorWebsite = new SimpleStringProperty("");
		authorLastModified = LocalDateTime.now();
		
		firstName = "";
		lastName = "";
		dateOfBirth = LocalDate.now();
		gender = "";
		website = "";
	}
	
	public Author(int authorID, String authorFirstName, String authorLastName, LocalDate authorDOB, String authorGender, String authorWebsite, LocalDateTime authorLastModified) {
		this();
		this.authorID = authorID;
		this.authorFirstName.set(authorFirstName);
		this.authorLastName.set(authorLastName);
		this.authorDOB.set(authorDOB);
		this.authorGender.set(authorGender);
		this.authorWebsite.set(authorWebsite);
		this.authorLastModified = authorLastModified;
		
		firstName = authorFirstName;
		lastName = authorLastName;
		dateOfBirth = authorDOB;
		gender = authorGender;
		website = authorWebsite;
	}
	
	public String toString() {
		return getAuthorFirstName() + " " + getAuthorLastName();
	}
	
	public void saveAuthor(int authorID, String authorFirstName, String authorLastName, LocalDate authorDOB, String authorGender, String authorWebsite, LocalDateTime authorLastModified) throws Throwable {
		try {
			if (!validateAuthorID(authorID)) throw new Exception("ID must be positive");
			if (!validateAuthorFirstName(authorFirstName)) throw new Exception("First name must have a length of 1-100");
			if (!validateAuthorLastName(authorLastName)) throw new Exception("Last name must have a length of 1-100");
			if (!validateAuthorDOB(authorDOB)) throw new Exception("Date of birth must be before today's date");
			if (!validateAuthorGender(authorGender)) throw new Exception("Gender must be \"Male\", \"Female\", or \"Unknown\"");
			if (!validateAuthorWebsite(authorWebsite)) throw new Exception("Website must have a length of 0-100");
		} catch (Exception invalid) {
			logger.info("saveAuthor() failed: "+invalid);
			
			throw invalid;
		}
		
		setAuthorID(authorID);
		setAuthorFirstName(authorFirstName);
		setAuthorLastName(authorLastName);
		setAuthorDOB(authorDOB);
		setAuthorGender(authorGender);
		setAuthorWebsite(authorWebsite);
		setAuthorLastModified(authorLastModified);
		
		if (authorID == 0) {
			try {
				setAuthorID(new AuthorTableGateway().insertAuthor(this));
				new AuthorTableGateway().insertAuditTrailEntry(getAuthorID(), "Author inserted: "+getAuthorFirstName()+" "+getAuthorLastName());
			} catch (SQLException sqlError) {
				logger.info(sqlError);
				
				throw sqlError;
			}
		} else {
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
		if (getAuthorID() != 0 && !firstName.equals(authorFirstName)) {
			try {
				new AuthorTableGateway().insertAuditTrailEntry(getAuthorID(),"first name changed from "+firstName+" to "+authorFirstName);
			} catch (Throwable error) {
				logger.info("insertAuditTrailEntry() failed in setAuthorFirstName(): "+error);
			}
		}
		firstName = authorFirstName;
		this.authorFirstName.set(authorFirstName);
	}
	
	public String getAuthorLastName() {
		return authorLastName.get();
	}
	
	public void setAuthorLastName(String authorLastName) {
		if (getAuthorID() != 0 && !lastName.equals(authorLastName)) {
			try {
				new AuthorTableGateway().insertAuditTrailEntry(getAuthorID(),"last name changed from "+lastName+" to "+authorLastName);
			} catch (Throwable error) {
				logger.info("insertAuditTrailEntry() failed in setAuthorLastName(): "+error);
			}
		}
		lastName = authorLastName;
		this.authorLastName.set(authorLastName);
	}
	
	public LocalDate getAuthorDOB() {
		return authorDOB.get();
	}
	
	public void setAuthorDOB(LocalDate authorDOB) {
		if (getAuthorID() != 0 && !dateOfBirth.equals(authorDOB)) {
			try {
				new AuthorTableGateway().insertAuditTrailEntry(getAuthorID(),"date of birth changed from "+dateOfBirth+" to "+authorDOB);
			} catch (Throwable error) {
				logger.info("insertAuditTrailEntry() failed in setAuthorDOB(): "+error);
			}
		}
		dateOfBirth = authorDOB;
		this.authorDOB.set(authorDOB);
	}
	
	public String getAuthorGender() {
		return authorGender.get();
	}
	
	public void setAuthorGender(String authorGender) {
		if (getAuthorID() != 0 && !gender.equals(authorGender)) {
			try {
				new AuthorTableGateway().insertAuditTrailEntry(getAuthorID(),"gender changed from "+gender+" to "+authorGender);
			} catch (Throwable error) {
				logger.info("insertAuditTrailEntry() failed in setAuthorGender(): "+error);
			}
		}
		gender = authorGender;
		this.authorGender.set(authorGender);
	}
	
	public String getAuthorWebsite() {
		return authorWebsite.get();
	}
	
	public void setAuthorWebsite(String authorWebsite) {
		if (getAuthorID() != 0 && !website.equals(authorWebsite)) {
			try {
				new AuthorTableGateway().insertAuditTrailEntry(getAuthorID(),"web_site changed from "+website+" to "+authorWebsite);
			} catch (Throwable error) {
				logger.info("insertAuditTrailEntry() failed in setAuthorWebsite(): "+error);
			}
		}
		website = authorWebsite;
		this.authorWebsite.set(authorWebsite);
	}
	
	public LocalDateTime getAuthorLastModified() {
		return authorLastModified;
	}
	
	public void setAuthorLastModified(LocalDateTime authorLastModified) {
		this.authorLastModified = authorLastModified;
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