package lock;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

/**
 * Class that handles a new user login sequence.
 * 
 * @author  Peter Johansson, Andree Höög, Jesper Hansen
 */
public class LoginNewUserToServer {
	private String message, idTextFile;
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private ConnectGUI gui;
	private LoginGUI gui2;
	private ClientID id;
	private LoginNewUserGUI gui3;

	/**
	 * Constructor for LoginNewUserToServer class.
	 * 
	 * @param username A client username.
	 * @param password A client password.
	 * @param client A reference to the Client class.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 * @param gui A reference to the ConnectGUI class.
	 * @param id A reference to the ClientID class.
	 * @param idTextFile The user id textfile.
	 * @param gui3 A reference to the LoginNewUserGUI class.
	 */
	public LoginNewUserToServer( Socket socket, DataOutputStream output, 
			DataInputStream input, ConnectGUI gui, ClientID id, String idTextFile, LoginNewUserGUI gui3 ) {
		this.socket = socket;
		this.input = input;
		this.output = output;
		this.gui = gui;
		this.id = id;
		this.idTextFile = idTextFile;
		this.gui3 = gui3;
	}

	/**
	 * A function that sends the server username and password to the server.
	 * 
	 * @param username Login username.
	 * @param password Login password.
	 */
	public void sendLogin( String username, String password ) {
		try {
			output.writeUTF( username + ";" + MD5.encryption( password ) );
			output.flush();

			message = input.readUTF();
			if( message.equals( "tempfalse" ) ) {
				gui.setInfoDisplay( "Wrong username or password" );
				gui.showLogIn();
				socket.close();
			} else if( message.equals( "databasedown" ) ) {
				gui.setInfoDisplay( "Database unreachable" );
				gui.showLogIn();
				socket.close();
			} else if( message.equals( "closed" ) ) {
				gui3.hideFrame();
				gui.setInfoDisplay( "Disconnected" );
				gui.showLogIn();
			} else {
				gui3.setLogin( true );
				gui3.setInfoDisplay( "Choose your user login info" );
				gui3.showLogIn();
			}
		} catch(Exception e1 ) {
			System.out.println( e1 );
		}
	}

	/**
	 * A function that sends a chosen username and password to the server and saves a new client id.
	 * 
	 * @param username New client username.
	 * @param password New client password.
	 */
	public void startInfoLogin( String username, String password ) {
		try {
			output.writeUTF( username + ";" + MD5.encryption(password) );
			output.flush();
			message = input.readUTF();
			
			if( message.equals( "occupiedusername" ) ) {
				gui3.setInfoDisplay( "Username occupied" );
				gui3.showLogIn();
			} else if( message.equals( "closed" ) ) {
				gui3.hideFrame();
				gui.setInfoDisplay( "Disconnected" );
				gui.showLogIn();
				socket.close();
			} else {
				id.setID( message );
				writeID( idTextFile );
			
				gui.showDeleteIDBtn( true );
				gui2 = new LoginGUI( socket, output, input, gui );
				gui2.setInfoDisplay( "Added to server" );
			}
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