package author;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import menu.MenuController;

public class AuthorDetailController implements Initializable {
	private static Logger logger = LogManager.getLogger();
	private static MenuController menuController;
	
	@FXML private TextField textFieldAuthorFirstName;
	@FXML private TextField textFieldAuthorLastName;
	@FXML private DatePicker textFieldAuthorDOB;
	@FXML private TextField textFieldAuthorGender;
	@FXML private TextField textFieldAuthorWebsite;
	@FXML private Button saveAuthorDetailButton;
	
	private Author author;

	public AuthorDetailController(Author author) {
		menuController = MenuController.getInstanceOfMenuController();
		this.author = author;
	}
	
	@FXML void saveAuthorDetail(ActionEvent event) {
		logger.info("calling saveAuthorDetail()");
		
		logger.info("saved author: " + author.toString());
		
		menuController.loadAuthorList();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.info("calling initialize()");
		
		textFieldAuthorFirstName.textProperty().bindBidirectional(author.authorFirstNameProperty());
		textFieldAuthorLastName.textProperty().bindBidirectional(author.authorLastNameProperty());
		textFieldAuthorDOB.valueProperty().bindBidirectional(author.authorDOBProperty());
		textFieldAuthorGender.textProperty().bindBidirectional(author.authorGenderProperty());
		textFieldAuthorWebsite.textProperty().bindBidirectional(author.authorWebsiteProperty());
	}
	
}
