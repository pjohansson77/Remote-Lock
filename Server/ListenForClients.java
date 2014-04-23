package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

/**
 * A server that listens for clients and verifies a known client with an unique id.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 */
public class ListenForClients implements Runnable {
	private int port;
	private ServerSocket serverSocket;
	private Socket socket;
	private String id;
	private DataOutputStream output;
	private DataInputStream input;
	private ServerGUI gui;

	/**
	 * A constructor that gets a port number to listen on.
	 * 
	 * @param port The port that the server listens on.
	 * @param gui The server GUI.
	 */
	public ListenForClients( int port, ServerGUI gui ) {
		this.port = port;
		this.gui = gui;
	}

	/**
	 * A method that listens for new clients.
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
					gui.showText( "Login ID: " + id + "\n");
					
					// If the unique id is known the client only needs to input the password. 
					if( id.equals( "DA211P1-14" ) ) {
						gui.showText( "Status: User trusted\n" );
						output = new DataOutputStream( socket.getOutputStream() );
						output.writeUTF( "connected" );
						output.flush();
						Thread clientThread = new Thread( new ListenToClientPassword( socket, output, input, gui ) );
						clientThread.start();

						// If the unique id is empty it's the first login and the client needs a username and password.
					} else if( id.equals( "" ) ) {
						gui.showText( "Status: New user\n" );
						output = new DataOutputStream( socket.getOutputStream() );
						output.writeUTF( "newuser" );
						output.flush();
						Thread clientThread = new Thread( new ListenToNewClient( socket, output, input, gui ) );
						clientThread.start();
						
						// If the unique id is not empty the user is not allowed to log in.
					} else {
						gui.showText( "Status: User not trusted\n" );
						output = new DataOutputStream( socket.getOutputStream() );
						output.writeUTF( "unknown" );
						output.flush();
						gui.showText( "Disconnected: " + getTime() + "\nIP-adress: " + socket.getInetAddress().getHostAddress() + "\n" );
						socket.close();
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
	 * A private method that terminates the connection.
	 * 
	 * @return date and time
	 */
	public void terminate() {
		try {
			socket.close();
		} catch( Exception e ) {}
	}
}
