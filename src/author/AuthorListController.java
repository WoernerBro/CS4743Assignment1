package author;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import menu.MenuController;

public class AuthorListController implements Initializable {
	private static Logger logger = LogManager.getLogger();
	private static MenuController menuController;
	
	@FXML private Button buttonDeleteAuthor;
	@FXML private ListView<Author> listViewAuthors;
	private ObservableList<Author> listData;
	
	public AuthorListController(ObservableList<Author> authors) {
		menuController = MenuController.getInstanceOfMenuController();
		
		listViewAuthors = new ListView<Author>();
		listData = authors;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.info("calling initialize()");
		
		listViewAuthors.setItems(listData);
		
		listViewAuthors.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent click) {
		        if (click.getClickCount() == 2) {
		        	logger.info("double-click on " + listViewAuthors.getSelectionModel().getSelectedItem());
		           
		        	menuController.loadAuthorDetail(listViewAuthors.getSelectionModel().getSelectedItem());
		        }
		    }
		});
	}
	
	@FXML void deleteAuthor(ActionEvent event) {
		logger.info("calling deleteAuthor()");
		
		try {
			new AuthorTableGateway().deleteAuthor(listViewAuthors.getSelectionModel().getSelectedItem());
		} catch (SQLException invalid) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("SQL Error");
			alert.setHeaderText("There was a problem updating the database.");
			alert.setContentText(invalid.toString());
		}
		
		listData = FXCollections.observableArrayList(new AuthorTableGateway().getAuthors());
		listViewAuthors.setItems(listData);
	}
}
