package lock;

import java.net.*;
import java.io.*;

/**
 * Client class that connects the client to the server.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class Client {
	private String message;
	private Socket socket;
	boolean connected = true;
	private ConnectGUI gui;
	private DataOutputStream output;
	private DataInputStream input;
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
	public Client( ConnectGUI gui, String idTextFile ) { 
		id = new ClientID( "" );
		this.gui = gui;
		this.idTextFile = idTextFile;
		readID( idTextFile );
	}

	/**
	 * A function that connects the client to the server and sends the client id.
	 */
	public void connect( String serverIP, int serverPort ) {
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress( serverIP, serverPort ), 5000);
			output = new DataOutputStream( socket.getOutputStream() );

			output.writeUTF( id.getID() );
			output.flush(); 

			input = new DataInputStream( socket.getInputStream() );
			message = input.readUTF();

			if( message.equals( "connected" ) ) {
				new LoginGUI( socket, output, input, gui );
			} else if( message.equals( "newuser" ) ) {
				new LoginNewUserGUI( socket, output, input, gui, id, idTextFile );
			} else {
				gui.setInfoDisplay( "Not trusted" );
				gui.showLogIn();
				socket.close();
			}
		} catch(Exception e1 ) {
			try{
				gui.setInfoDisplay( "Server not reachable" );
				gui.showLogIn();
				socket.close();
			} catch (IOException e) {};
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
					new FileInputStream( filename ), "UTF-8"));
			while ( ( str = reader.readLine() ) != null ) {
				id.setID( str );
				gui.showDeleteIDBtn( true );
			}
			reader.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * A function that deletes the user password.
	 */
	public void deleteID() {
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter( idTextFile ) );

			writer.write( "" );

			writer.close();
		} catch( IOException e1 ) {}
			id.setID( "" );
			gui.setInfoDisplay( "User ID deleted" );
	}
}