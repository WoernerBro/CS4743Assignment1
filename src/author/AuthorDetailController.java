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
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class AuthorDetailController implements Initializable {
	private static Logger logger = LogManager.getLogger();
	
	@FXML private TextField textFieldAuthorFirstName;
	@FXML private TextField textFieldAuthorLastName;
	@FXML private DatePicker datePickerAuthorDOB;
	@FXML private TextField textFieldAuthorGender;
	@FXML private TextField textFieldAuthorWebsite;
	@FXML private Button saveAuthorDetailButton;
	
	private Author author;

	public AuthorDetailController(Author author) {
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
				textFieldAuthorWebsite.getText());
		} catch (Throwable invalid) {
			logger.info(invalid);
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Input");
			alert.setHeaderText(invalid.toString());
			alert.setContentText("Please input valid data.");
			alert.showAndWait();
			return;
		}
		
		try {
			new AuthorTableGateway().updateAuthor(author);
		} catch (Throwable e) {
			logger.info(e);
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("SQL Error");
			alert.setHeaderText(e.toString());
			alert.setContentText("There was a problem updating the database.");
			alert.showAndWait();
			return;
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
