package lock;

import java.net.*;
import java.io.*;

/**
 * Client class that connects the client to the server.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class Client {
	private String message, serverIP;
	private int serverPort;
	private Socket socket;
	boolean connected = true;
	private ConnectGUI gui;
	private DataOutputStream output;
	private DataInputStream input;
	private Client client;
	private ClientID id;
	private String idTextFile;

	/**
	 * Constructor for Client class.
	 * 
	 * @param serverIP The ip-address to the server.
	 * @param serverPort The port that the server listens for clients.
	 * @param gui A reference to the ConnectGUI class.
	 * @param idTextFile The client id textfile.
	 */
	public Client( String serverIP, int serverPort, ConnectGUI gui, String idTextFile ) { 
		id = new ClientID( "" );
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.gui = gui;
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
				new LoginGUI( client, output, input, gui );
			} else if( message.equals( "newuser" ) ) {
				new LoginNewUserGUI( client, output, input, gui, id, idTextFile );
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
	 * @param filename A reference to the file that contains the client id.
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