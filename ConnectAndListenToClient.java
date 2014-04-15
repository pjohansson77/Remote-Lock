package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

/**
 * A class that verifies the password if the client is known.
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 *
 */
public class ConnectAndListenToClient implements Runnable {
	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	String password;
	
	/**
	 * The constructor receives the current socket and streams.
	 * @param socket The active socket.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 */
	public ConnectAndListenToClient( Socket socket, DataOutputStream output, DataInputStream input ) {
		this.socket = socket;
		this.input = input;
		this.output = output;
	}

	/**
	 * A method that verifies the password. If the password is not correct the user is disconnected.
	 */
	public void run() {
		try {
			password = input.readUTF();

			if( password.toLowerCase().equals( "alfa" ) ) {

				output.writeUTF( "sant" ); // Andreas fel ;-)
				output.flush();
//				output.writeBoolean( true );

				Choices choice = new Choices( socket, output, input );
				choice.arduinoChoices();
			} else {
				output.writeUTF( "falskt" ); // Andreas fel ;-)
				output.flush();
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
