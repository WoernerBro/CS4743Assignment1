package author;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import menu.MenuController;

public class AuthorTableGateway {
	private static Logger logger = LogManager.getLogger();
	private static MenuController menuController;
	
	public AuthorTableGateway() {
		menuController = MenuController.getInstanceOfMenuController();
	}
	
	public List<Author> getAuthors() {
		logger.info("calling getAuthors()");
		
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Author> authors = new ArrayList<Author>();
		
		try {
			//TODO: Needs to be parameterized
			String query = "SELECT * FROM authorTable";
			st = menuController.getDBConnection().prepareStatement(query);
			rs = st.executeQuery();
			
			//TODO: Doesn't account for blanks or validation problems
			while(rs.next()) {
				authors.add(new Author(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getDate("dob").toLocalDate(), rs.getString("gender"), rs.getString("web_site")));
			}
		} catch (SQLException e) {
			logger.info("try/catch SQLException in getAuthors(");
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				logger.info("try/catch/finally SQLException in getAuthors()");
			}
		}
		
		return authors;
	}
}
