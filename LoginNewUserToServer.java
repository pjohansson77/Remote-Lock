package lock;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class that handles a new user login sequence.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 */
public class LoginNewUserToServer implements Runnable {
	private String message, username, password, idTextFile;
	private DataInputStream input;
	private DataOutputStream output;
	private Client client;
	private ConnectGUI gui;
	private LoginGUI gui2;
	private LoginInfoGUI gui4;
	private ClientID id;
	private LoginNewUserToServer loginNewUserToServer;

	/**
	 * Constructor for LoginNewUserToServer class.
	 * 
	 * @param username A client username.
	 * @param password A client password.
	 * @param client A reference to the Client class.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 * @param gui2 A reference to the LoginNewUserGUI class.
	 * @param id A reference to the ClientID class.
	 * @param idTextFile The user id textfile.
	 */
	public LoginNewUserToServer( String username, String password, Client client, DataOutputStream output, DataInputStream input, ConnectGUI gui, ClientID id, String idTextFile ) {
		this.username = username;
		this.password = password;
		this.client = client;
		this.input = input;
		this.output = output;
		this.gui = gui;
		this.id = id;
		this.idTextFile = idTextFile;
		this.loginNewUserToServer = this;
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
				gui.setInfoDisplay( "Wrong username or password" );
				client.disconnect();
			} else {
				gui4 = new LoginInfoGUI( client, loginNewUserToServer );
				gui4.setStatusDisplay( "Waiting for username and password" );
			}
		} catch(Exception e1 ) {
			System.out.println( e1 );
		}
	}

	public void startInfoLogin( String username, String password ) {
		try {
			output.writeUTF( username );
			output.flush();

			output.writeUTF( password );
			output.flush();

			message = input.readUTF();
			id.setID( message );
			writeID( idTextFile );
			
			gui2 = new LoginGUI( client );
			gui2.setStatusDisplay( "Added to server" );
		} catch(Exception e1 ) {
			System.out.println( e1 );
		}
	}
	
	/**
	 * A private function that writes a user id to a textfile.
	 * 
	 * @param filename Name of file that contains a user id.
	 */
	private void writeID( String filename ) {
		String str;
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter( filename ) );

			str = id.getID();

			writer.write( str ); // Skriva strängen till textfilen
			writer.newLine(); // Skriva ny-rad-tecken till textfilen

			writer.close();
		} catch( IOException e ) {
			System.out.println( e );
		}
	}
}