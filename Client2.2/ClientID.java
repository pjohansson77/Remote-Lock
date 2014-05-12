package lock;

/**
 * Class that handles the client id.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class ClientID {
	private String id;
	
	/**
	 * Constructor for ClientID class.
	 * 
	 * @param id String
	 */
	public ClientID( String id ) {
		this.id = id;
	}
	
	/**
	 * Function that returns the client id.
	 * 
	 * @return client id.
	 */
	public String getID() {
		return this.id;
	}
	
	/**
	 * Function that sets the client id.
	 * 
	 * @param id String.
	 */
	public void setID( String id ) {
		this.id = id;
	}
}
