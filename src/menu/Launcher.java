package menu;

/*
 * CS 4743 Assignment 1 by Sean Woerner and DeMarcus Kennedy
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {
	private static Logger logger = LogManager.getLogger();
	
	@Override
	public void start(Stage stage) throws Exception {
		logger.info("calling start()");

		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("MenuView.fxml"));
		
		MenuController controller = MenuController.getInstanceOfMenuController();
		loader.setController(controller);
		Parent rootNode = loader.load();
		
		Scene scene = new Scene(rootNode, 600, 400);
		stage.setScene(scene);
		stage.setTitle("Library Archive");
		stage.show();
		
	}

	@Override
	public void stop() throws Exception {
		logger.info("calling stop()");

		super.stop();
	}

	@Override
	public void init() throws Exception {
		logger.info("calling init()");

		super.init();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
