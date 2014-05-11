package lock;

import java.net.*;
import java.io.*;

/**
 * Client class that connects the client to the server.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög
 */
public class Client {
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
		connect();
	}

	/**
	 * A function that connects the client to the server and sends the client id.
	 */
	public void connect() {
		try {
			socket = new Socket( InetAddress.getByName( serverIP ), serverPort );
			output = new DataOutputStream( socket.getOutputStream() );

			output.writeUTF( id.getID() );
			output.flush(); 

			input = new DataInputStream( socket.getInputStream() );
			message = input.readUTF();

			if( message.equals( "connected" ) ) {
				gui2 = new LoginGUI( client, output, input, gui );
				gui2.setStatusDisplay( "Waiting for password" );
			} else if( message.equals( "newuser" ) ) {
				gui3 = new LoginNewUserGUI( client, output, input, gui, id, idTextFile );
				gui3.setStatusDisplay( "Waiting for temp username and password" );
			} else {
				disconnect();
			}

		} catch(Exception e1 ) {
			System.out.println( e1 );
		}
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
			gui.showLogIn();
			socket.close();
		} catch( IOException e ) {
			System.out.println( e );
		}
	}
}