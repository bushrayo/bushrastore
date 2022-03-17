package userFolder;

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

public class UserDbUtil {

	private static UserDbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/my_store";
	
	
	public static UserDbUtil getInstance() throws Exception {
		if (instance == null) {
			instance = new UserDbUtil();
		}
		
		return instance;
	}
	
	private UserDbUtil() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
		
	public List<User> getUsers() throws Exception {

		List<User> users = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from users_list order by email";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("id");
				String email = myRs.getString("email");
				String password = myRs.getString("password");
				

				// create new user object
				User tempUser = new User(id, email, password);

				// add it to the list of users
				users.add(tempUser);
			}
			
			return users;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}

	public void addUser(User theUser) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "insert into users_list ( email, password) values ( ?, ?)";

			myStmt = myConn.prepareStatement(sql);

			
			
			myStmt.setString(1, theUser.getEmail());
			myStmt.setString(2, theUser.getPassword());
			
			myStmt.execute();			
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public User getUser(int userId) throws Exception {
	
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from users_list where id=?";

			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setInt(1, userId);
			
			myRs = myStmt.executeQuery();

			User theUser = null;
			
			// retrieve data from result set row
			if (myRs.next()) {
				int id = myRs.getInt("id");
				String email = myRs.getString("email");
				String password = myRs.getString("password");
				

				theUser = new User(id,email, password);
			}
			else {
				throw new Exception("Could not find user id: " +  userId);
			}

			return theUser;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	public User getUserByemail(String userEmail) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from users_list where email=?";

			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setString(1, userEmail);
			
			myRs = myStmt.executeQuery();

			User theUser = null;
			
			// retrieve data from result set row
			if (myRs.next()) {
				int id = myRs.getInt("id");
				String email = myRs.getString("email");
				String password = myRs.getString("password");
				

				theUser = new User(id,email, password);
			}
			else {
				throw new Exception("Could not find user email: " +  userEmail);
			}

			return theUser;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	
	public void deleteUser(int userId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "delete from users_list where id=?";

			myStmt = myConn.prepareStatement(sql);

		
			myStmt.setInt(1, userId);
			
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
