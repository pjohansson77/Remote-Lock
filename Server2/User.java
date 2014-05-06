package lock;

/**
 * Class that handles user information.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög
 */
public class User {
	private String id;
	private String name;
	private String password;
	
	/**
	 * A constructor that recieves the user id, the username and the user password.
	 * 
	 * @param id The user id.
	 * @param name The username.
	 * @param password The user password.
	 */
	public User( String id, String name, String password ) {
		this.id = id;
		this.name = name;
		this.password = password;
	}
	
	/**
	 * Function that returns an id.
	 * @return user id.
	 */
	public String getID() {
		return id;
	}
	
	/**
	 * Function that returns a username.
	 * @return username.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Function that returns the user password.
	 * @return user password.
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Function that sets the user password.
	 * @param password String.
	 */
	public void setPassword( String password ) {
		this.password = password;
	}
}
