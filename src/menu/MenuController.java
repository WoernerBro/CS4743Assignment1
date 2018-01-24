package menu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import author.AuthorListController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

public class MenuController implements Initializable {
	private static Logger logger = LogManager.getLogger();
	private static MenuController menuControllerSingleton;
	
	@FXML private MenuBar menuBar;
	@FXML private MenuItem menuItemAuthorList;
	@FXML private MenuItem menuItemExit;
	@FXML private BorderPane rootPane;
	
	private MenuController() {
	}
	
	public static MenuController getInstanceOfMenuController() {
		logger.info("calling getInstanceOfMenuController()");
		
		if (menuControllerSingleton == null)
			menuControllerSingleton = new MenuController();
		return menuControllerSingleton;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.info("calling initialization()");
		
		menuBar.setFocusTraversable(true);
	}
	
	public void setRootPane(BorderPane rootPane) {
		this.rootPane = rootPane;
	}
	
	@FXML void handleMenuAction(ActionEvent event) throws IOException {
		logger.info("calling handleMenuAction()");
		
		if(event.getSource() == menuItemAuthorList) {
			loadAuthorList();
		} else if(event.getSource() == menuItemExit)
			Platform.exit();
	}
	
	public void loadAuthorList() {
		logger.info("calling loadAuthorList()");
		
		AuthorListController controller = new AuthorListController();
		URL fxmlFile = this.getClass().getResource("../author/AuthorListView.fxml");
		FXMLLoader loader = new FXMLLoader(fxmlFile);
		
		loader.setController(controller);
		
		changeView(loader);
	}
	
	public void changeView(FXMLLoader loader) {
		logger.info("calling changeView()");
		
		try {
			Parent contentView = loader.load();
			rootPane.setCenter(null);
			rootPane.setCenter(contentView);
		} catch (IOException e) {
			logger.info("try/catch error in changeView()");
		}
    }
	
}

