package book;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import menu.MenuController;

public class BookListController implements Initializable {
	private static Logger logger = LogManager.getLogger();
	private static MenuController menuController;
	
	@FXML private TextField textFieldSearchBooks;
	@FXML private Button buttonSearchBooks;
	@FXML private Button buttonDeleteBook;
	@FXML private ListView<Book> listViewBooks;
	private ObservableList<Book> listData;
	
	public BookListController(ObservableList<Book> books) {
		menuController = MenuController.getInstanceOfMenuController();
		
		listViewBooks = new ListView<Book>();
		listData = books;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.info("calling initialize()");
		
		listViewBooks.setItems(listData);
		
		listViewBooks.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent click) {
		        if (click.getClickCount() == 2) {
		        	logger.info("double-click on " + listViewBooks.getSelectionModel().getSelectedItem());
		           
		        	menuController.loadBookDetail(listViewBooks.getSelectionModel().getSelectedItem());
		        }
		    }
		});
	}
	
	@FXML void deleteBook(ActionEvent event) {
		logger.info("calling deleteBook()");
		
		try {
			new BookTableGateway().deleteBook(listViewBooks.getSelectionModel().getSelectedItem());
		} catch (SQLException invalid) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("SQL Error");
			alert.setHeaderText("There was a problem updating the database.");
			alert.setContentText(invalid.toString());
		}
		
		listData = FXCollections.observableArrayList(new BookTableGateway().getBooks());
		listViewBooks.setItems(listData);
	}
	
	@FXML void searchBooks(ActionEvent event) {
		logger.info("calling searchBooks()");
		
		try {
			if (textFieldSearchBooks.equals(""))
				listData = FXCollections.observableArrayList(new BookTableGateway().getBooks());
			else 
				listData = FXCollections.observableArrayList(new BookTableGateway().searchBooks(textFieldSearchBooks.getText()));
		} catch (SQLException invalid) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("SQL Error");
			alert.setHeaderText("There was a problem updating the database.");
			alert.setContentText(invalid.toString());
		}
		
		listViewBooks.setItems(listData);
	}
}
