package audit_trail;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class AuditTrailEntry {
	private int auditID;
	private SimpleObjectProperty<LocalDate> auditDateAdded;
	private SimpleStringProperty auditMessage;
	
	public AuditTrailEntry() {
		auditID = 0;
		auditDateAdded = new SimpleObjectProperty<LocalDate>(LocalDate.now());
		auditMessage = new SimpleStringProperty("");
	}
	
	public AuditTrailEntry(int auditID, LocalDate auditDateAdded, String auditMessage) {
		this();
		this.auditID = auditID;
		this.auditDateAdded.set(auditDateAdded);
		this.auditMessage.set(auditMessage);
	}
	
	public String toString() {
		return auditID + "\t" + getAuditDateAdded().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")) + "\t" + getAuditMessage();
	}
	
	//Accessors
	
	public int getAuditID() {
		return auditID;
	}
	
	public void setAuditID(int auditID) {
		this.auditID = auditID;
	}
	
	public LocalDate getAuditDateAdded() {
		return auditDateAdded.get();
	}
	
	public void setAuditDateAdded(LocalDate auditDateAdded) {
		this.auditDateAdded.set(auditDateAdded);
	}
	
	public String getAuditMessage() {
		return auditMessage.get();
	}
	
	public void setAuditMessage(String auditMessage) {
		this.auditMessage.set(auditMessage);
	}
	
	//Direct Getters
	
	public SimpleObjectProperty<LocalDate> auditDateAddedProperty() {
		return auditDateAdded;
	}
	
	public SimpleStringProperty auditMessageProperty() {
		return auditMessage;
	}
	
}