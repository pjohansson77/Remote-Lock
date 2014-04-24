package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Class that handles a new user login sequence.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 */
public class LoginNewUserToServer implements Runnable {
	private String message, username, password;
	private DataInputStream input;
	private DataOutputStream output;
	private Client client;
	private ConnectGUI gui;
	private LoginGUI gui2;
	private LoginNewUserGUI gui3;
	private ClientID id;

	/**
	 * Constructor for LoginNewUserToServer class.
	 * 
	 * @param username A client username.
	 * @param password A client password.
	 * @param client A reference to the Client class.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 * @param gui A reference to the ConnectGUI class.
	 * @param gui3 A reference to the LoginNewUserGUI class.
	 * @param id A reference to the ClientID class.
	 */
	public LoginNewUserToServer( String username, String password, Client client, DataOutputStream output, DataInputStream input, ConnectGUI gui, LoginNewUserGUI gui3, ClientID id ) {
		this.username = username;
		this.password = password;
		this.client = client;
		this.input = input;
		this.output = output;
		this.gui = gui;
		this.gui3 = gui3;
		this.id = id;
	}

	/**
	 * A function that sends the username and password to the server and saves a new client id.
	 */
	public void run() {
		try {
			output.writeUTF( username );
			output.flush();

			output.writeUTF( password );
			output.flush();

			message = input.readUTF();
			if( message.equals( "tempfalse" ) ) {
				gui.setInfoDisplay( "Status: Wrong username or password" );
				gui3.hideFrame();
				client.disconnect();
			} else {
				id.setID( message );
				gui3.hideFrame();
				gui2 = new LoginGUI( client, gui );
				gui2.setInfoDisplay( "Added to server" );
			}
		} catch(Exception e1 ) {
			System.out.println( e1 );
		}
	}
}