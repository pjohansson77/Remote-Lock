package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

/**
 *  A class that verifies the password if the client is unknown.
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 *
 */
public class ListenToNewClient implements Runnable {
	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	String username, password;
	boolean connected = true;

	/**
	 * A method that verifies the password. If the password is not correct the user is disconnected.
	 * @param socket The active socket.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 */
	public ListenToNewClient( Socket socket, DataOutputStream output, DataInputStream input ) {
		this.socket = socket;
		this.input = input;
		this.output = output;
	}

	/**
	 * A method that verifies the password and the "admin" user.
	 */
	public void run() {
		try {
			username = input.readUTF();
			password = input.readUTF();
			
			if( username.toLowerCase().equals( "admin" ) && password.toLowerCase().equals( "alfa" ) ) {
				output.writeUTF( "DA211P1-14" ); // Skickar ett unikt id
				output.flush();
				System.out.println( "Status: Tillagd i server\n" );
//				output.writeBoolean( true );
				
//				ArduinoChoices choice = new ArduinoChoices( socket, output, input );
//				choice.listenToArduinoChoices();
			} else {
				output.writeUTF( "falskt" ); // Andreas fel ;-)
				output.flush();
				System.out.println( "Status: Fel användarnamn eller lösenord\n" );
//				output.writeBoolean( false );
			} 
		} catch(IOException e) {} 
		try {
			System.out.println( "Disconnected: " + getTime() + "\nIP-adress: " + socket.getInetAddress().getHostAddress() + "\n" );
			socket.close();
		} catch( Exception e ) {}
	} 
	
	/**
	 * A private method that returns the date and time.
	 * @return date and time
	 */
	private Date getTime() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
}
