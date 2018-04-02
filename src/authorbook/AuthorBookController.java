package authorbook;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import author.Author;
import author.AuthorTableGateway;
import book.Book;
import book.BookTableGateway;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.util.converter.NumberStringConverter;
import menu.MenuController;

public class AuthorBookController implements Initializable {
	private static Logger logger = LogManager.getLogger();
	private static MenuController menuController;
	
	@FXML private ComboBox<Author> comboBoxAuthor;
	@FXML private ComboBox<Book> comboBoxBook;
	@FXML TextField textFieldRoyalty;
	@FXML private Button buttonSaveAuthorBook;
	private String previousType;
	private AuthorBook authorBook;
	
	public AuthorBookController(String previousType, AuthorBook authorBook) {
		menuController = MenuController.getInstanceOfMenuController();
		
		this.previousType = previousType;
		this.authorBook = authorBook;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.info("calling initialize()");

		comboBoxAuthor.getItems().addAll(new AuthorTableGateway().getAuthors());
		comboBoxAuthor.valueProperty().bindBidirectional(authorBook.authorProperty());
		comboBoxBook.getItems().addAll(new BookTableGateway().getBooks());
		comboBoxBook.valueProperty().bindBidirectional(authorBook.bookProperty());
		textFieldRoyalty.textProperty().bindBidirectional(authorBook.royaltyProperty(), new NumberStringConverter("###.###"));
		
		if (previousType.equals("Author") || !authorBook.getNewRecord())
			comboBoxAuthor.setDisable(true);
		if (previousType.equals("Book") || !authorBook.getNewRecord())
			comboBoxBook.setDisable(true);
		
	}
	
	@FXML void saveAuthorBook(ActionEvent event) {
		logger.info("calling saveAuthorBook()");

		try {
			authorBook.saveAuthorBook(comboBoxAuthor.getValue(), comboBoxBook.getValue(), Float.parseFloat(textFieldRoyalty.getText()));
			
			if (previousType.equals("Author"))
				menuController.loadAuthorDetail(authorBook.getAuthor());
			if (previousType.equals("Book"))
				menuController.loadBookDetail(authorBook.getBook());
		} catch (Throwable invalid) {
			logger.info(invalid);
			
			Alert alert = new Alert(AlertType.ERROR);
			if (invalid.toString().contains("SQL")) {
				alert.setTitle("SQL Error");
				alert.setHeaderText("There was a problem updating the database.");
				alert.setContentText(invalid.toString());
			} else {
				alert.setTitle("Invalid Input");
				alert.setHeaderText("Please input valid data.");
				alert.setContentText(invalid.toString());
			}
			alert.showAndWait();
		}
	}
}
