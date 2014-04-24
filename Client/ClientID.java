package test;

/**
 * Class that handles the client id.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 */
public class ClientID {
	private String id;
	
	/**
	 * Constructor for ClientID class.
	 * @param id String
	 */
	public ClientID( String id ) {
		this.id = id;
	}
	
	/**
	 * Function that returns the client id.
	 * @return client id.
	 */
	public String getID() {
		return this.id;
	}
	
	/**
	 * Function that sets the client id.
	 * @param id String.
	 */
	public void setID( String id ) {
		this.id = id;
	}
}
