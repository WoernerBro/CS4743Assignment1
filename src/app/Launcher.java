package app;

/*
 * CS 4743 Assignment 2 by Sean Woerner and DeMarcus Kennedy
 */
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import menu.MenuController;

public class Launcher extends Application {
	private static Logger logger = LogManager.getLogger();
	public static Stage stage;
	
	@Override
	public void start(Stage stage) throws Exception {
		logger.info("calling start()");
		
		Launcher.stage = stage;

		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../menu/MenuView.fxml"));
		
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
		
		MenuController.getInstanceOfMenuController().getDBConnection().close();
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
