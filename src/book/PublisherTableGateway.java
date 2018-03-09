package book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import menu.MenuController;

public class PublisherTableGateway {
	private static Logger logger = LogManager.getLogger();
	private static MenuController menuController;
	
	public PublisherTableGateway() {
		menuController = MenuController.getInstanceOfMenuController();
	}
	
	public List<Publisher> getPublishers() {
		logger.info("calling getPublishers()");
		
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Publisher> publishers = new ArrayList<Publisher>();
		
		try {
			String query = "SELECT * FROM publisherTable";
			st = menuController.getDBConnection().prepareStatement(query);
			rs = st.executeQuery();
			
			while(rs.next()) {
				publishers.add(new Publisher(rs.getInt("id"), rs.getString("publisher_name")));
			}
		} catch (SQLException e) {
			logger.info("try/catch SQLException in getPublishers()");
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				logger.info("try/catch/finally SQLException in getPublishers()");
			}
		}
		
		return publishers;
	}
	
	public Publisher getPublisherByID(int publisherID) {
		logger.info("calling getPublishersByID()");
		
		PreparedStatement st = null;
		ResultSet rs = null;
		Publisher publisher = new Publisher();
		
		try {
			String query = "SELECT * FROM publisherTable WHERE id=?";
			st = menuController.getDBConnection().prepareStatement(query);
			st.setInt(1, publisherID);
			rs = st.executeQuery();
			
			while(rs.next()) {
				publisher = new Publisher(rs.getInt("id"), rs.getString("publisher_name"));
			}
		} catch (SQLException e) {
			logger.info("try/catch SQLException in getPublisherByID()");
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				logger.info("try/catch/finally SQLException in getPublisherByID()");
			}
		}
		
		return publisher;
	}
}
