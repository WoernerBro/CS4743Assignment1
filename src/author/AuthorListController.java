package author;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import menu.MenuController;

public class AuthorListController implements Initializable {
	private static Logger logger = LogManager.getLogger();
	private static MenuController menuController;
	
	@FXML private ListView<Author> listViewAuthors;
	private ObservableList<Author> listData;
	
	public AuthorListController() {
		menuController = MenuController.getInstanceOfMenuController();
		
		listViewAuthors = new ListView<Author>();
		listData = FXCollections.observableArrayList (
			    new Author("Ernest", "Cline", "3/29/1972", "Male", "www.ernestcline.com"),
			    new Author("Robert Anthony", "Salvatore", "1/20/1959", "Male", "www.rasalvatore.com"),
			    new Author("Richard", "Riordan", "6/5/1964", "Male", "www.rickriordan.com"));
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.info("calling initialize()");
		
		listViewAuthors.setItems(listData);
		
		//TODO: Double Click for AuthorDetail
	}
}
