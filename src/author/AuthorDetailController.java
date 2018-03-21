package author;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import menu.MenuController;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class AuthorDetailController implements Initializable {
	private static Logger logger = LogManager.getLogger();
	private static MenuController menuController;
	
	@FXML private TextField textFieldAuthorFirstName;
	@FXML private TextField textFieldAuthorLastName;
	@FXML private DatePicker datePickerAuthorDOB;
	@FXML private TextField textFieldAuthorGender;
	@FXML private TextField textFieldAuthorWebsite;
	@FXML private Button buttonSaveAuthorDetail;
	@FXML private Button buttonViewAuditTrail;
	
	private Author author;

	public AuthorDetailController(Author author) {
		menuController = MenuController.getInstanceOfMenuController();
		
		this.author = author;
	}
	
	@FXML void saveAuthorDetail(ActionEvent event) {
		logger.info("calling saveAuthorDetail()");
		
		try {
			author.saveAuthor(author.getAuthorID(), 
				textFieldAuthorFirstName.getText(), 
				textFieldAuthorLastName.getText(), 
				datePickerAuthorDOB.getValue(), 
				textFieldAuthorGender.getText(), 
				textFieldAuthorWebsite.getText(), 
				author.getAuthorLastModified());
		} catch (Throwable invalid) {
			logger.info(invalid);
			
			Alert alert = new Alert(AlertType.ERROR);
			if (invalid.toString().contains("SQL")) {
				alert.setTitle("SQL Error");
				alert.setHeaderText("There was a problem updating the database.");
				alert.setContentText(invalid.toString());
			} if (invalid.toString().contains("out of date")) {
				alert.setTitle("Refresh Author");
				alert.setHeaderText("Please return to Author List to refresh this author.");
				alert.setContentText(invalid.toString());
			} else {
				alert.setTitle("Invalid Input");
				alert.setHeaderText("Please input valid data.");
				alert.setContentText(invalid.toString());
			}
			alert.showAndWait();
		}
	}
	
	@FXML void viewAuditTrail(ActionEvent event) {
		logger.info("viewAuditTrail() for " + author.getAuthorFirstName() + " " + author.getAuthorLastName());
		
		try {
			menuController.loadAuditTrail(author.getAuthorFirstName() + " " + author.getAuthorLastName(), new AuthorTableGateway().getAuditTrail(author.getAuthorID()), author, "AUTHOR");
		} catch (Throwable error) {
			logger.info(error);
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Audit Trail Error");
			alert.setHeaderText(error.toString());
			alert.setContentText("Please save the new author before attempting to view the Audit Trail for this author.");
			alert.showAndWait();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.info("calling initialize()");
		
		textFieldAuthorFirstName.textProperty().bindBidirectional(author.authorFirstNameProperty());
		textFieldAuthorLastName.textProperty().bindBidirectional(author.authorLastNameProperty());
		datePickerAuthorDOB.valueProperty().bindBidirectional(author.authorDOBProperty());
		textFieldAuthorGender.textProperty().bindBidirectional(author.authorGenderProperty());
		textFieldAuthorWebsite.textProperty().bindBidirectional(author.authorWebsiteProperty());
	}
	
}
