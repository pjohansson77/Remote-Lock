package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

/**
 * A server class that listens for clients and verifies a trusted client with an unique id.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög
 */
public class ListenForClients implements Runnable {
	private int port;
	private ServerSocket serverSocket;
	private Socket socket;
	private String id;
	private DataOutputStream output;
	private DataInputStream input;
	private ServerGUI gui;
	private HashtableOH<String, User> table;
	private User user;

	/**
	 * A constructor that recieves a port number to listen on and a reference to the ServerGUI.
	 * 
	 * @param port The port that the server listens on.
	 * @param gui A reference to the ServerGUI class.
	 */
	public ListenForClients( int port, ServerGUI gui ) {
		this.port = port;
		this.table = new HashtableOH<String, User>(10);
//		table = MySQL.readMySQL( table, user );
		this.gui = gui;
	}

	/**
	 * A function that listens for new clients and sends them the right way.
	 */
	public void run() {
		try {
			serverSocket = new ServerSocket( port ); 
			while( true ) {
				try {
					socket = serverSocket.accept();

					gui.showText( "Connected: " + getTime() + "\nIP-adress: " + socket.getInetAddress().getHostAddress() );

					input = new DataInputStream( socket.getInputStream() );
					id = input.readUTF();
					gui.showText( "Inloggningsid: " + id + "\n");

					// If the unique id is known the client only needs to input the password. 
					if( table.containsKey( id ) ) {
						gui.showText( "Status: User " + table.get( id ).getName() + " is trusted\n" );
						output = new DataOutputStream( socket.getOutputStream() );
						output.writeUTF( "connected" );
						output.flush();
						Thread clientThread = new Thread( new ListenToClientPassword( socket, output, input, gui, table, id ) );
						clientThread.start();

						// If the unique id is empty it's the first login and the client needs a username and password.
					} else if( id.equals( "" ) ) {
						gui.showText( "Status: New user\n" );
						output = new DataOutputStream( socket.getOutputStream() );
						output.writeUTF( "newuser" );
						output.flush();
						Thread clientThread = new Thread( new ListenToNewClient( socket, output, input, gui, table ) );
						clientThread.start();

						// If the unique id is not empty the user is not allowed to log in.
					} else {
						gui.showText( "Status: User not trusted\n" );
						output = new DataOutputStream( socket.getOutputStream() );
						output.writeUTF( "unknown" );
						output.flush();
						gui.showText( "Disconnected: " + getTime() + "\nIP-adress: " + socket.getInetAddress().getHostAddress() + "\n" );
						socket.close();
						output.close();
						input.close();
					}
				} catch( IOException e1 ) {
					gui.showText( "Disconnected: " + getTime() + "\nIP-adress: " + socket.getInetAddress().getHostAddress() + "\n" );
				}
			}
		} catch( IOException e1 ) {}
		try {
			serverSocket.close();
		} catch( Exception e ) {}
	}

	/**
	 * A private method that returns the date and time.
	 * 
	 * @return date and time
	 */
	private Date getTime() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

	/**
	 * A function that terminates the connection with the client.
	 */
	public void terminate() {
		try {
			socket.close();
			output.close();
			input.close();
		} catch( Exception e ) {}
	}
}
