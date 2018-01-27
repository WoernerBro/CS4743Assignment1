package author;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
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
			    new Author(1, "Ernest", "Cline", LocalDate.of(1972,3,29), "Male", "www.ernestcline.com"),
			    new Author(2, "Robert Anthony", "Salvatore", LocalDate.of(1959,1,20), "Male", "www.rasalvatore.com"),
			    new Author(3, "Richard", "Riordan", LocalDate.of(1964,6,5), "Male", "www.rickriordan.com"));
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
		           
		        	loadAuthorDetail(listViewAuthors.getSelectionModel().getSelectedItem());
		        }
		    }
		});
	}
	
	public void loadAuthorDetail(Author author) {
		logger.info("calling loadAuthorDetail()");
		
		AuthorDetailController controller = new AuthorDetailController(author);
		URL fxmlFile = this.getClass().getResource("../author/AuthorDetailView.fxml");
		FXMLLoader loader = new FXMLLoader(fxmlFile);
		
		loader.setController(controller);
		
		menuController.changeView(loader);
	}
}
