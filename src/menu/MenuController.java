package menu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import app.Launcher;
import audit_trail.AuditTrailController;
import audit_trail.AuditTrailEntry;
import author.Author;
import author.AuthorDetailController;
import author.AuthorListController;
import author.AuthorTableGateway;
import book.Book;
import book.BookDetailController;
import book.BookListController;
import book.BookTableGateway;
import book.PublisherTableGateway;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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
	@FXML private MenuItem menuItemAddAuthor;
	@FXML private MenuItem menuItemBookList;
	@FXML private MenuItem menuItemAddBook;
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
		else if(event.getSource() == menuItemAddAuthor)
			loadAuthorDetail(new Author());
		else if(event.getSource() == menuItemBookList)
			loadBookList();
		else if(event.getSource() == menuItemAddBook)
			loadBookDetail(new Book());
		else if(event.getSource() == menuItemExit)
			Platform.exit();
	}
	
	public void loadAuthorList() {
		logger.info("calling loadAuthorList()");
		
		AuthorListController controller = new AuthorListController(FXCollections.observableArrayList(new AuthorTableGateway().getAuthors()));
		URL fxmlFile = this.getClass().getResource("../author/AuthorListView.fxml");
		FXMLLoader loader = new FXMLLoader(fxmlFile);
		
		loader.setController(controller);
		
		Launcher.stage.setTitle("Author List");
		changeView(loader);
	}
	
	public void loadAuthorDetail(Author author) {
		logger.info("calling loadAuthorDetail()");
		
		AuthorDetailController controller = new AuthorDetailController(author);
		URL fxmlFile = this.getClass().getResource("../author/AuthorDetailView.fxml");
		FXMLLoader loader = new FXMLLoader(fxmlFile);
		
		loader.setController(controller);
		
		if (author.getAuthorID() == 0)
			Launcher.stage.setTitle("Add New Author");
		else
			Launcher.stage.setTitle("Edit Author");
		changeView(loader);
	}
	
	public void loadBookList() {
		logger.info("calling loadBookList()");
		
		BookListController controller = new BookListController(FXCollections.observableArrayList(new BookTableGateway().getBooks()));
		URL fxmlFile = this.getClass().getResource("../book/BookListView.fxml");
		FXMLLoader loader = new FXMLLoader(fxmlFile);
		
		loader.setController(controller);
		
		Launcher.stage.setTitle("Book List");
		changeView(loader);
	}
	
	public void loadBookDetail(Book book) {
		logger.info("calling loadBookDetail()");
		
		BookDetailController controller = new BookDetailController(book, new PublisherTableGateway().getPublishers());
		URL fxmlFile = this.getClass().getResource("../book/BookDetailView.fxml");
		FXMLLoader loader = new FXMLLoader(fxmlFile);
		
		loader.setController(controller);
		
		if (book.getBookID() == 0)
			Launcher.stage.setTitle("Add New Book");
		else
			Launcher.stage.setTitle("Edit Book");
		changeView(loader);
	}
	
	public void loadAuditTrail(String title, List<AuditTrailEntry> list, Object auditObject) {
		logger.info("calling loadAuditTrail()");
		
		AuditTrailController controller = new AuditTrailController(title, FXCollections.observableArrayList(list), auditObject);
		URL fxmlFile = this.getClass().getResource("../audit_trail/AuditTrailView.fxml");
		FXMLLoader loader = new FXMLLoader(fxmlFile);
		
		loader.setController(controller);
		
		Launcher.stage.setTitle("Audit Trail");
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

