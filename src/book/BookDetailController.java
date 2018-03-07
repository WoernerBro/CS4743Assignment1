package book;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.converter.NumberStringConverter;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BookDetailController implements Initializable {
	private static Logger logger = LogManager.getLogger();
	
	@FXML private TextField textFieldBookTitle;
	@FXML private TextArea textAreaBookSummary;
	@FXML private TextField textFieldBookYearPublished;
	@FXML private ComboBox<Publisher> comboBoxBookPublisher;
	@FXML private TextField textFieldBookISBN;
	@FXML private Button saveBookDetailButton;
	
	private Book book;
	private List<Publisher> publishers;

	public BookDetailController(Book book, List<Publisher> publishers) {
		this.book = book;
		this.publishers = publishers;
	}
	
	@FXML void saveBookDetail(ActionEvent event) {
		logger.info("calling saveBookDetail()");
		
		try {
			book.saveBook(book.getBookID(), 
				textFieldBookTitle.getText(), 
				textAreaBookSummary.getText(), 
				Integer.parseInt(textFieldBookYearPublished.getText()), 
				comboBoxBookPublisher.getValue(), 
				textFieldBookISBN.getText(), 
				book.getBookDateAdded());
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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.info("calling initialize()");
		
		textFieldBookTitle.textProperty().bindBidirectional(book.bookTitleProperty());
		textAreaBookSummary.textProperty().bindBidirectional(book.bookSummaryProperty());
		textFieldBookYearPublished.textProperty().bindBidirectional(book.bookYearPublishedProperty(), new NumberStringConverter("####"));
		comboBoxBookPublisher.getItems().addAll(publishers);
		comboBoxBookPublisher.valueProperty().bindBidirectional(book.bookPublisherProperty());
		if (book.getBookTitle().equals(""))
			comboBoxBookPublisher.getSelectionModel().selectFirst();
		textFieldBookISBN.textProperty().bindBidirectional(book.bookISBNProperty());
	}
	
}
