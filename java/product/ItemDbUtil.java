package product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ItemDbUtil {

	private static ItemDbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/my_store";
	
	public static ItemDbUtil getInstance() throws Exception {
		if (instance == null) {
			instance = new ItemDbUtil();
		}
		
		return instance;
	}
	
	private ItemDbUtil() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName); //Java Naming and Directory Interface (JNDI)
		
		return theDataSource;
	}
		
	public List<Item> getItems() throws Exception {

		List<Item> items = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from products_list order by id";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("id");
				String name = myRs.getString("name");
				int price = myRs.getInt("price");
				int amount = myRs.getInt("amount");
				String link = myRs.getString("link");

				// create new item object
				Item tempItem = new Item(id, name, price,amount,link);

				// add it to the list of items
				items.add(tempItem);
			}
			
			return items;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}

	public void addItem(Item theItem) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "insert into products_list (name, price, amount, link) values (?, ?, ?, ?)";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setString(1, theItem.getName());
			myStmt.setInt(2, theItem.getPrice());
			myStmt.setInt(3, theItem.getAmount());
			myStmt.setString(4, theItem.getLink());
			
			myStmt.execute();			
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public Item getItem(int itemId) throws Exception {
	
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from products_list where id=?";

			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setInt(1, itemId);
			
			myRs = myStmt.executeQuery();

			Item theItem = null;
			
			// retrieve data from result set row
			if (myRs.next()) {
				////
				int id = myRs.getInt("id");
				String name = myRs.getString("name");
				int price = myRs.getInt("price");
				int amount = myRs.getInt("amount");
				String link = myRs.getString("link");

				theItem = new Item(id, name, price,amount,link);
			}
			else {
				throw new Exception("Could not find item id: " + itemId);
			}

			return theItem;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	public void updateItem(Item theItem) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();
			
			String sql = "update products_list "
					+ " set name=?, price=?, amount=?, link=?"
					+ " where id=?";
			
			myStmt = myConn.prepareStatement(sql);
			
			
			// set params
			myStmt.setString(1, theItem.getName());
			myStmt.setInt(2, theItem.getPrice());
			myStmt.setInt(3, theItem.getAmount());
			myStmt.setString(4, theItem.getLink());
			myStmt.setInt(5, theItem.getId());
			
			myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public void deleteItem(int itemId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "delete from products_list where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, itemId);
			
			myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
		}		
	}	
	
	private Connection getConnection() throws Exception {

		Connection theConn = dataSource.getConnection();
		
		return theConn;
	}
	
	private void close(Connection theConn, Statement theStmt) {
		close(theConn, theStmt, null);
	}
	
	private void close(Connection theConn, Statement theStmt, ResultSet theRs) {

		try {
			if (theRs != null) {
				theRs.close();
			}

			if (theStmt != null) {
				theStmt.close();
			}

			if (theConn != null) {
				theConn.close();
			}
			
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}	
}
