package author;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.Initializable;
import menu.MenuController;

public class AuthorDetailController implements Initializable {
	private static Logger logger = LogManager.getLogger();
	private static MenuController menuController;

	public AuthorDetailController() {
		menuController = MenuController.getInstanceOfMenuController();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.info("calling initialize()");
	}
	
}
