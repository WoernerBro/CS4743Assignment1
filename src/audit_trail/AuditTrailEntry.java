package audit_trail;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class AuditTrailEntry {
	private int auditID;
	private SimpleObjectProperty<Date> auditDateAdded;
	private SimpleStringProperty auditMessage;
	
	public AuditTrailEntry() {
		auditID = 0;
		auditDateAdded = new SimpleObjectProperty<Date>(new Date());
		auditMessage = new SimpleStringProperty("");
	}
	
	public AuditTrailEntry(int auditID, Date auditDateAdded, String auditMessage) {
		this();
		this.auditID = auditID;
		this.auditDateAdded.set(auditDateAdded);
		this.auditMessage.set(auditMessage);
	}
	
	public String toString() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getAuditDateAdded()) + "\t" + getAuditMessage();
	}
	
	//Accessors
	
	public int getAuditID() {
		return auditID;
	}
	
	public void setAuditID(int auditID) {
		this.auditID = auditID;
	}
	
	public Date getAuditDateAdded() {
		return auditDateAdded.get();
	}
	
	public void setAuditDateAdded(Date auditDateAdded) {
		this.auditDateAdded.set(auditDateAdded);
	}
	
	public String getAuditMessage() {
		return auditMessage.get();
	}
	
	public void setAuditMessage(String auditMessage) {
		this.auditMessage.set(auditMessage);
	}
	
	//Direct Getters
	
	public SimpleObjectProperty<Date> auditDateAddedProperty() {
		return auditDateAdded;
	}
	
	public SimpleStringProperty auditMessageProperty() {
		return auditMessage;
	}
	
}