package lock;

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
public class ListenToClientPassword implements Runnable {
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private String password;
	private ServerGUI gui;
	
	/**
	 * The constructor receives the current socket and streams.
	 * @param socket The active socket.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 */
	public ListenToClientPassword( Socket socket, DataOutputStream output, DataInputStream input, ServerGUI gui, String password ) {
		this.socket = socket;
		this.input = input;
		this.output = output;
		this.gui = gui;
		this.password = password;
	}

	/**
	 * A method that verifies the password. If the password is not correct the user is disconnected.
	 */
	public void run() {
		try {
			String clientPassword = input.readUTF();

			if( clientPassword.equals( password ) ) {

				output.writeUTF( "passwordtrue" );
				output.flush();
//				output.writeBoolean( true );

				ArduinoChoices choice = new ArduinoChoices( socket, output, input, gui );
				choice.listenToArduinoChoices();
			} else {
				output.writeUTF( "passwordfalse" );
				output.flush();
				gui.showText( "Status: Wrong username or password\n" );
//				output.writeBoolean( false );

			}
		} catch(IOException e) {} 
		try {
			gui.showText( "Disconnected: " + getTime() + "\nIP-adress: " + socket.getInetAddress().getHostAddress() + "\n" );
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
