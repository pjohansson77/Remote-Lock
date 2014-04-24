package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Class that handles a login sequence.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 */
public class LoginToServer implements Runnable {
	private String message, password;
	private DataInputStream input;
	private DataOutputStream output;
	private Client client;
	private ChoicesGUI choice;
	private ConnectGUI gui;
	private LoginGUI gui2;
	private LoginToServer loginToServer;

	/**
	 * Constructor for LoginToServer class.
	 * 
	 * @param password A client password.
	 * @param client A reference to the Client class.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 * @param gui A reference to the ConnectGUI class.
	 * @param gui2 A reference to the LoginGUI class.
	 */
	public LoginToServer( String password, Client client, DataOutputStream output, DataInputStream input, ConnectGUI gui, LoginGUI gui2 ) {
		this.password = password;
		this.client = client;
		this.input = input;
		this.output = output;
		this.gui = gui;
		this.gui2 = gui2;
		this.loginToServer = this;
	}

	/**
	 * A function that sends the password to the server and starts the ChoicesGUI class.
	 */
	public void run() {
		try {
			output.writeUTF( password );
			output.flush();

			message = input.readUTF();
			if( message.equals( "passwordtrue" ) ) {
				gui2.hideFrame();
				choice = new ChoicesGUI( loginToServer );
				choice.setStatusDisplay("Connected" );
			} else {
				gui.setInfoDisplay( "Status: Wrong username or password" );
				gui2.hideFrame();
				client.disconnect();
			} 
		} catch(Exception e1 ) {
			System.out.println( e1 );
		}
	}
	
	/**
	 * A function that sends the clients Arduino choice to the server.
	 * @param str Arduino choices.
	 */
	public void choices( String str ) {
		String arduinoChoice, message;
		try {
			arduinoChoice = str;
			if( arduinoChoice.equals("0") ) {
				choice.hideFrame();
				gui.setInfoDisplay( "" );
				client.disconnect();
			} else {
				output.writeUTF( arduinoChoice );
				output.flush();
				message = input.readUTF();
				choice.setInfoDisplay( message );
			}
		} catch(Exception e1 ) {
			System.out.println( e1 );
		}
	}
}