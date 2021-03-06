package book;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import author.Author;
import authorbook.AuthorBook;
import authorbook.AuthorBookGateway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.converter.NumberStringConverter;
import menu.MenuController;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BookDetailController implements Initializable {
	private static Logger logger = LogManager.getLogger();
	private static MenuController menuController;
	
	@FXML private TextField textFieldBookTitle;
	@FXML private TextArea textAreaBookSummary;
	@FXML private TextField textFieldBookYearPublished;
	@FXML private ComboBox<Publisher> comboBoxBookPublisher;
	@FXML private TextField textFieldBookISBN;
	@FXML private ListView<AuthorBook> listViewBookAuthors;
	@FXML private Button buttonAddAuthorBook;
	@FXML private Button buttonRemoveAuthorBook;
	@FXML private Button buttonSaveBookDetail;
	@FXML private Button buttonViewAuditTrail;
	
	private Book book;
	private List<Publisher> publishers;
	private ObservableList<AuthorBook> listData;

	public BookDetailController(Book book, List<Publisher> publishers) {
		menuController = MenuController.getInstanceOfMenuController();
		
		listViewBookAuthors = new ListView<AuthorBook>();
		
		this.book = book;
		this.publishers = publishers;
		listData = FXCollections.observableArrayList(book.getAuthors());
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
			} else if (invalid.toString().contains("Number")) {
				alert.setTitle("Invalid Input");
				alert.setHeaderText("Please default Year Published to 0.");
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
		logger.info("calling viewAuditTrail() for " + book.getBookTitle());
		
		try {
			menuController.loadAuditTrail(book.getBookTitle(), new BookTableGateway().getAuditTrail(book.getBookID()), book, "BOOK");
		} catch (Throwable error) {
			logger.info(error);
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Audit Trail Error");
			alert.setHeaderText(error.toString());
			alert.setContentText("Please save the new book before attempting to view the Audit Trail for this book.");
			alert.showAndWait();
		}
	}
	
	@FXML void addAuthorBook(ActionEvent event) {
		logger.info("calling addBookAuthor()");
		
		if (book.getBookID() != 0)
			menuController.loadAuthorBook("Book", new AuthorBook(new Author(), book, 0, true));
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Book Author Warning");
			alert.setHeaderText("Incomplete Book");
			alert.setContentText("Please save this new book before attempting to add an Author.");
			alert.showAndWait();
		}
	}
	
	@FXML void editAuthorBook(ActionEvent event) {
		logger.info("calling editBookAuthor()");
		
		if (listViewBookAuthors.getSelectionModel().getSelectedItem() != null)
			menuController.loadAuthorBook("Book", listViewBookAuthors.getSelectionModel().getSelectedItem());
	}
	
	@FXML void removeAuthorBook(ActionEvent event) {
		logger.info("calling removeBookAuthor()");
		
		try {
			if (listViewBookAuthors.getSelectionModel().getSelectedItem() != null) {
				new AuthorBookGateway().removeAuthorBook(listViewBookAuthors.getSelectionModel().getSelectedItem());
				
				listData = FXCollections.observableArrayList(book.getAuthors());
				listViewBookAuthors.setItems(listData);
			}
		} catch (Throwable error) {
			logger.info(error);
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
		
		listViewBookAuthors.setItems(listData);
	}
	
}
