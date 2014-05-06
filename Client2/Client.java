package lock;

import java.net.*;
import java.io.*;

/**
 * Client class that connects the client to the server.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög
 */
public class Client implements Runnable {
	private String message, serverIP;
	private int serverPort;
	private Socket socket;
	boolean connected = true;
	private ConnectGUI gui;
	private LoginGUI gui2;
	private LoginNewUserGUI gui3;
	private DataOutputStream output;
	private DataInputStream input;
	private Client client;
	private ClientID id;
	private String idTextFile;

	/**
	 * Constructor for Client class.
	 * 
	 * @param idTextFile The user id textfile.
	 */
	public Client( String serverIP, int serverPort, ConnectGUI gui, ClientID id, String idTextFile ) { 
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.gui = gui;
		this.id = id;
		this.idTextFile = idTextFile;
		client = this;
		readID( idTextFile );
	}

	/**
	 * A function that connects the client to the server and sends the client id.
	 */
	public void run() {
		try {
			socket = new Socket( InetAddress.getByName( serverIP ), serverPort );
			output = new DataOutputStream( socket.getOutputStream() );

			output.writeUTF( id.getID() );
			output.flush(); 

			input = new DataInputStream( socket.getInputStream() );
			message = input.readUTF();

			if( message.equals( "connected" ) ) {
				gui2 = new LoginGUI( client );
				gui2.setStatusDisplay( "Waiting for password" );
			} else if( message.equals( "newuser" ) ) {
				gui3 = new LoginNewUserGUI( client );
				gui3.setStatusDisplay( "Waiting for temp username and password" );
			} else {
				try {
					socket.close();
					gui.showLogIn();
				} catch( IOException e ) {
					System.out.println( e );
				}
			}

		} catch(Exception e1 ) {
			System.out.println( e1 );
		}
	}

	/**
	 * A function that starts the login to the server
	 * @param password User password.
	 * @param gui2 A reference to the LoginGUI class.
	 */
	public void startLogin( String password, LoginGUI gui2 ) {
		Thread newThread = new Thread( new LoginToServer( password, client, output, input, gui ) );
		newThread.start();
	}

	/**
	 * A function that starts the new user login to the server
	 * @param password Server username.
	 * @param password Server password.
	 * @param id A reference to the ClientID class.
	 */
	public void startNewUserLogin( String username, String password ) {
		Thread newThread = new Thread( new LoginNewUserToServer( username, password, client, output, input, gui, id, idTextFile ) );
		newThread.start();
	}

	/**
	 * A private function that reads a user id textfile.
	 * 
	 * @param filename Name of file that contains all users.
	 */
	private void readID( String filename ) {
		String str;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream( filename ), "ISO-8859-1"));
			while ( ( str = reader.readLine() ) != null ) {
				id.setID( str );
			}
			reader.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * A function that disconnects the clients from the server.
	 */
	public void disconnect() {
		try {
			socket.close();
			output.close();
			input.close();
			gui.showLogIn();
		} catch( IOException e ) {
			System.out.println( e );
		}
	}
}