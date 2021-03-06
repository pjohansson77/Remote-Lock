package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A class that listens for changes from the server.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class Status implements Runnable {
	private DataOutputStream output;
	private DataInputStream input;
	private String message;
	private ChoicesGUI choice;
	private LoginGUI gui2;
	private ConnectGUI gui;
	private boolean connected = true;

	/**
	 * A class constructor that gets a reference to the ChoicesGUI class, current streams and a reference to the LoginGUI class.
	 * 
	 * @param gui A reference to the ConnectGUI class.
	 * @param choice A reference to the ChoiceGUI class. 
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 * @param gui2 A reference to the LoginGUI class.
	 */
	public Status( ConnectGUI gui, ChoicesGUI choice, DataOutputStream output, DataInputStream input, LoginGUI gui2 ) {
		this.gui = gui;
		this.output = output;
		this.input = input;
		this.choice = choice;
		this.gui2 = gui2;
	}

	/**
	 * A function that listens for changes from the server.
	 */
	public void run() {
		while( connected ) {
			try {
				message = input.readUTF();
				if( message.equals( "sendnewpassword" ) ) {
					gui2.setnewpassword( true );
					gui2.setInfoDisplay( "Type new password" );
					gui2.showLogIn();
				} else if( message.equals( "wrongpassword" ) ) {
					choice.setInfoDisplay( "Wrong password" );
					choice.showChoices();
				} else if( message.equals( "databasedown" ) ) {
					choice.setInfoDisplay( "Database unreachable" );
				} else if( message.equals( "passwordchanged" ) ) {
					choice.setInfoDisplay( "Password changed" );
				} else if( message.equals( "closed" ) ) {
					connected = false;
					choice.hideFrame();
					gui.setInfoDisplay( "Disconnected" );
					gui.showLogIn();
				} else if( message.equals( "powerdown" ) ) {
					choice.setInfoDisplay( "Power supply is down" );
					doorStatus();
					output.writeUTF( "ok" );
					output.flush();
				} else {
					choice.setInfoDisplay( "Door " + message );
					doorStatus();
					output.writeUTF( "ok" );
					output.flush();
				}
			} catch(IOException e) {}
		}
	}
	
	/**
	 * A private function that dims different buttons based on the message content.
	 */
	private void doorStatus() {
		if( message.equals( "unlocked" ) ) {
			choice.lockedChoice();
		} else if( message.equals( "locked" ) ) {
			choice.unlockedChoice();
		} else {
			choice.noChoice();
		}
	}
}
