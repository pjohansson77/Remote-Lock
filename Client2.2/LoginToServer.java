package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Class that handles a login sequence.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class LoginToServer {
	private String message;
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
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
	public LoginToServer( Socket socket, DataOutputStream output, DataInputStream input, ConnectGUI gui, LoginGUI gui2 ) {
		this.input = input;
		this.output = output;
		this.gui = gui;
		this.gui2 = gui2;
		this.loginToServer = this;
	}

	/**
	 * A function that sends the password to the server and starts the ChoicesGUI class.
	 */
	public void connected( String password ) {
		try {
			output.writeUTF( password );
			output.flush();

			message = input.readUTF();
			if( message.equals( "passwordtrue" ) ) {
				
				choice = new ChoicesGUI( loginToServer );
				Thread Status = new Thread( new Status( choice, output, input, gui2 ) );
				Status.start();
			} else {
				gui.setInfoDisplay( "Wrong username or password" );
				gui.showLogIn();
				socket.close();
			} 
		} catch( Exception e1 ) {
			System.out.println( e1 );
		}
	}
	
	/**
	 * A function that sends the clients arduino choice to the server.
	 * @param str Arduino choices.
	 */
	public void choices( String str ) {
		String arduinoChoice;
		try {
			arduinoChoice = str;
			if( arduinoChoice.equals( "0" ) ) {
				output.writeUTF( arduinoChoice );
				output.flush();
				gui.setInfoDisplay( "Not connected" );
				gui.showLogIn();
				socket.close();
			} else if( arduinoChoice.equals( "3" ) ) {
				gui2.getChoiceGUI( choice );
				choice.hideFrame();
				gui2.setchangepassword( true );
				gui2.setnewpassword( false );
				gui2.setInfoDisplay( "Type old password" );
				gui2.showLogIn();
			} else {
				output.writeUTF( arduinoChoice );
				output.flush();
			}
		} catch(Exception e1 ) {
			System.out.println( e1 );
		}
	}
	
	/**
	 * A function that sends the clients old password to the server.
	 * 
	 * @param oldpassword The old password.
	 */
	public void changePassword( String oldpassword) {
		try {
			output.writeUTF( "changepassword;" + oldpassword );
			output.flush();	
		} catch(Exception e1 ) {}
	}
	
	/**
	 * A function that sends the clients new password to the server.
	 * 
	 * @param newPassword The new password.
	 */
	public void newPassword( String newPassword) {
		try {
			output.writeUTF( "newpassword;" + newPassword );
			output.flush();
			choice.showChoices();
		} catch(Exception e1 ) {}
	}
}