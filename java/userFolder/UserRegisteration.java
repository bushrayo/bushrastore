package userFolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;


@ManagedBean
@SessionScoped
public class UserRegisteration implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<User> users;
	private UserDbUtil userDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());
	boolean show_wrongPasswordMessge = false;
	boolean show_wrongEmailMessge = false;
	boolean existEmailMassege=false;
	boolean loginOk=false;
	boolean adminUser=false;
	
	String adminEmail="admin@mail.com";
	
	
	
	// create no-arg constructor
	public UserRegisteration() throws Exception  {
		
		users = new ArrayList<>();
		
		userDbUtil = UserDbUtil.getInstance();
		
	}

	public List<User> getUsers() {
		return users;
	}
	
	public void loadUsers() {

		logger.info("Loading users");
		
		users.clear();

		try {
			
			// get all users from database
			users = userDbUtil.getUsers();
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading users", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
	}
	
	//sign up or the admin add new user 
	public String addUser(String userEmail,String userPassword) {
		loginOk=false;
		existEmailMassege=false;
		if (isExistEmail(userEmail)& !(adminUser)) {
			existEmailMassege=true;
			return "SignUp";
		}
		else {
			
			User theUser=new User( 0, userEmail, userPassword);
			logger.info("Adding user: " + theUser);
			try {
				// add user to the database
				userDbUtil.addUser(theUser);
				
			} catch (Exception exc) {
				// send this to server logs
				logger.log(Level.SEVERE, "Error adding user", exc);
				
				// add error message for JSF page
				addErrorMessage(exc);
	
				return null;
			}
			loginOk=true;
			if (adminUser)
				return "adminPage";
			else 
				return "home?faces-redirect=true";
			
			}
	}
	
	
	public String validateLogin(String userEmail, String userPassword) {
		loginOk=false;
		show_wrongPasswordMessge = false;
		show_wrongEmailMessge = false;
		
		logger.info("loading user: " + userEmail);
				
		try {
			// get user from database
			User theUser = userDbUtil.getUserByemail(userEmail);
			
			if (!theUser.getPassword().equals(userPassword)) {
				show_wrongPasswordMessge=true;
				return "login";
			}
			
			
		} catch (Exception exc) {
			show_wrongEmailMessge=true;
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading user email:" + userEmail, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return "login";
		}
		loginOk=true;
		show_wrongPasswordMessge=false;
		show_wrongEmailMessge=false;
		
		if(userEmail.equals(adminEmail)) {
			adminUser=true;
			return "adminPage";			
		}
		else
			return "home";		
		
	}
	
	
	public String loadUser(int userId) {
			
			logger.info("loading user: " + userId);
			
			try {
				// get user from database
				User theUser = userDbUtil.getUser(userId);
				
				// put in the request attribute ... so we can use it on the form page
				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();		
	
				Map<String, Object> requestMap = externalContext.getRequestMap();
				requestMap.put("user", theUser);	
				
			} catch (Exception exc) {
				// send this to server logs
				logger.log(Level.SEVERE, "Error loading user id:" + userId, exc);
				
				// add error message for JSF page
				addErrorMessage(exc);
				
				return null;
			}
					
			return "update-user-form.xhtml";
		}	
	
	
	public String deleteUser(int userId) {

	logger.info("Deleting user id: " + userId);
	
	try {

		// delete the user from the database
		userDbUtil.deleteUser(userId);
		
	} catch (Exception exc) {
		// send this to server logs
		logger.log(Level.SEVERE, "Error deleting user id: " + userId, exc);
		
		// add error message for JSF page
		addErrorMessage(exc);
		
		return null;
	}
	
	return "adminPage";	
}	

	
	
	
	public boolean getShow_wrongPasswordMessge() {
		return  show_wrongPasswordMessge;
	}
	
	public boolean getShow_wrongEmailMessge() {
		return  show_wrongEmailMessge;
	}
	
	public boolean getExistEmailMassege() {
		return  existEmailMassege;
	}
	
	public boolean getLoginOk() {
		return  loginOk;
	}
	public boolean getAdminUser() {
		return  adminUser;
	}
	
		
	public void setShow_wrongPasswordMessge() {
		show_wrongPasswordMessge=true;
	}
	
	public void setShow_wrongEmailMessge() {
		show_wrongEmailMessge=true;
	}
	
	public void setExistEmailMassege() {
		  existEmailMassege=true;
	}
	
	public void setLoginOk() {
		  loginOk=true;
	}
		
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public boolean isExistEmail(String email1) {
		 for(User x:users)  
			  if (x.getEmail().equals(email1))
				  return true;
		return false;
	}
	
	public void gologOut() {
		loginOk=false;
		adminUser=false;
	}
	
		
}
