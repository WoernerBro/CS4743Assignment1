package audit_trail;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import book.Book;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import menu.MenuController;

public class AuditTrailController implements Initializable {
	private static Logger logger = LogManager.getLogger();
	private static MenuController menuController;
	
	@FXML private Label labelAuditTrail;
	@FXML private Button buttonLeaveAuditTrail;
	@FXML private ListView<AuditTrailEntry> listViewAuditTrail;
	private ObservableList<AuditTrailEntry> listData;
	private String auditSubject;
	private Object auditObject;
	
	public AuditTrailController(String auditSubject, ObservableList<AuditTrailEntry> audits, Object auditObject) {
		menuController = MenuController.getInstanceOfMenuController();
		
		listViewAuditTrail = new ListView<AuditTrailEntry>();
		listData = audits;
		this.auditSubject = auditSubject;
		this.auditObject = auditObject;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.info("calling initialize()");
		
		labelAuditTrail.setText("Audit Trail for " + auditSubject);
		listViewAuditTrail.setItems(listData);
	}
	
	@FXML void leaveAuditTrail(ActionEvent event) {
		logger.info("calling leaveAuditTrail()");
		
		menuController.loadBookDetail((Book) auditObject);
	}
}
