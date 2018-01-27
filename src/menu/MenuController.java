package menu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import author.AuthorListController;
import author.AuthorTableGateway;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
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
	private Connection dbConnection;
	
	@FXML private MenuBar menuBar;
	@FXML private MenuItem menuItemAuthorList;
	@FXML private MenuItem menuItemExit;
	@FXML private BorderPane rootPane;
	
	private MenuController() {
		try {
			connectToDB();
		} catch (IOException e) {
			logger.info("try/catch IOException in constructor");
		} catch (SQLException e) {
			logger.info("try/catch SQLException in constructor");
		}
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
		logger.info("calling setRootPane()");
		
		this.rootPane = rootPane;
	}
	
	public Connection getDBConnection() {
		logger.info("calling getDBConnection()");
		
		return dbConnection;
	}
	
	@FXML void handleMenuAction(ActionEvent event) throws IOException {
		logger.info("calling handleMenuAction()");
		
		if(event.getSource() == menuItemAuthorList)
			loadAuthorList();
		else if(event.getSource() == menuItemExit)
			Platform.exit();
	}
	
	public void loadAuthorList() {
		logger.info("calling loadAuthorList()");
		
		AuthorListController controller = new AuthorListController(FXCollections.observableArrayList(new AuthorTableGateway().getAuthors()));
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
	
	private void connectToDB() throws IOException, SQLException {
		logger.info("calling connectToDB()");
		
		Properties props = new Properties();
		FileInputStream fis = null;
        fis = new FileInputStream("db.properties");
        props.load(fis);
        fis.close();
        
        MysqlDataSource ds = new MysqlDataSource();
        ds.setURL(props.getProperty("MYSQL_DB_URL"));
        ds.setUser(props.getProperty("MYSQL_DB_USERNAME"));
        ds.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
        
        dbConnection = ds.getConnection();
	}

}

