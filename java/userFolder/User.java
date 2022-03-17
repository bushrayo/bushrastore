package userFolder;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class User {

	private String password;
	private String email;
	private int id;
	
	public User() {
	}
	public User(int id, String email,String password) {
		this.email=email;
		this.password=password;
		this.id=id;
		
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return " User [id=" + id + ", email=" + email + ", Password="+ password  + "]";
	}

}
