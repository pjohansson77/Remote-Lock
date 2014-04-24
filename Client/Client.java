package test;

import java.net.*;
import java.io.*;

/**
 * Client class that connects the client to the server.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 */
public class Client implements Runnable {
	private String message, serverIP;
	private int serverPort;
	private Socket socket;
	private ConnectGUI gui;
	private LoginGUI gui2;
	private LoginNewUserGUI gui3;
	private DataOutputStream output;
	private DataInputStream input;
	private Client client;
	private ClientID id;

	/**
	 * Constructor for Client class.
	 * 
	 * @param serverIP Server IP-address.
	 * @param serverPort Server port.
	 * @param gui A reference to the ConnectGUI class.
	 * @param gui A reference to the ClientID class.
	 */
	public Client( String serverIP, int serverPort, ConnectGUI gui, ClientID id ) { 
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.gui = gui;
		client = this;
		this.id = id;
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
				gui2 = new LoginGUI( this, gui );
				gui2.setInfoDisplay( "Connected" );
			} else if( message.equals( "newuser" ) ) {
				gui3 = new LoginNewUserGUI( client, gui, id );
				gui3.setInfoDisplay( "New user" );
			} else {
				try {
					socket.close();
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
		Thread newThread = new Thread( new LoginToServer( password, client, output, input, gui, gui2 ) );
		newThread.start();
	}

	/**
	 * A function that starts the new user login to the server
	 * @param password Server username.
	 * @param password Server password.
	 * @param id A reference to the ClientID class.
	 */
	public void startNewUserLogin( String username, String password, ClientID id ) {
		Thread newThread = new Thread( new LoginNewUserToServer( username, password, client, output, input, gui, gui3, id ) );
		newThread.start();
	}

	/**
	 * A private function that disconnects the clients from the server.
	 */
	public void disconnect() {
		try {
			gui.frameStatus( true );
			gui.setInfoDisplay( "Disconnected" );
			socket.close();
		} catch( IOException e ) {
			System.out.println( e );
		}
	}
}