package book;

import javafx.beans.property.SimpleStringProperty;

public class Publisher {
	
	private int publisherID;
	private SimpleStringProperty publisherName;
	
	public Publisher() {
		publisherID = 0;
		publisherName = new SimpleStringProperty("");
	}
	
	public Publisher(int publisherID, String publisherName) {
		this();
		this.publisherID = publisherID;
		this.publisherName.set(publisherName);
	}
	
	public String toString() {
		return publisherID + "\t" + getPublisherName();
	}
	
	//Accessors
	
	public int getPublisherID() {
		return publisherID;
	}
	
	public void setPublisherID(int publisherID) {
		this.publisherID = publisherID;
	}
	
	public String getPublisherName() {
		return publisherName.get();
	}
	
	public void setPublisherName(String publisherName) {
		this.publisherName.set(publisherName);
	}
	
	//Direct Getters
	
	public SimpleStringProperty publisherNameProperty() {
		return publisherName;
	}
	
}