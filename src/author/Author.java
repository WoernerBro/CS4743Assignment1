package author;

import javafx.beans.property.SimpleStringProperty;

public class Author {
	private SimpleStringProperty authorFirstName;
	private SimpleStringProperty authorLastName;
	private SimpleStringProperty authorDOB;
	private SimpleStringProperty authorGender;
	private SimpleStringProperty authorWebsite;
	
	public Author() {
		authorFirstName = new SimpleStringProperty();
		authorLastName = new SimpleStringProperty();
		authorDOB = new SimpleStringProperty();
		authorGender = new SimpleStringProperty();
		authorWebsite = new SimpleStringProperty();
	}
	
	public Author(String authorFirstName, String authorLastName, String authorDOB, String authorGender, String authorWebsite) {
		this();
		this.authorFirstName.set(authorFirstName);
		this.authorLastName.set(authorLastName);
		this.authorDOB.set(authorDOB);
		this.authorGender.set(authorGender);
		this.authorWebsite.set(authorWebsite);
	}
	
	public String toString() {
		return getAuthorFirstName() + " " + getAuthorLastName();
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
	
	public String getAuthorDOB() {
		return authorDOB.get();
	}
	
	public void setAuthorDOB(String authorDOB) {
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
	
	public SimpleStringProperty authorDOBProperty() {
		return authorDOB;
	}
	
	public SimpleStringProperty authorGenderProperty() {
		return authorGender;
	}
	
	public SimpleStringProperty authorWebsiteProperty() {
		return authorWebsite;
	}
	
}